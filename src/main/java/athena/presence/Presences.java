package athena.presence;

import athena.context.DefaultAthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.presence.resource.FortnitePresence;
import athena.presence.resource.LastOnlineResponse;
import athena.presence.resource.annotation.PresenceEvent;
import athena.presence.resource.filter.PresenceFilter;
import athena.presence.resource.listener.FortnitePresenceListener;
import athena.presence.resource.subscription.PresenceSubscription;
import athena.presence.resource.subscription.SubscriptionSettings;
import athena.presence.service.PresencePublicService;
import athena.util.event.EventFactory;
import athena.util.request.Requests;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.PresenceTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

import java.io.Closeable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Provides easy access to the {@link athena.presence.service.PresencePublicService} and XMPP.
 */
public final class Presences implements Closeable {

    /**
     * The presence service.
     */
    private final PresencePublicService service;

    /**
     * The athena context.
     */
    private final DefaultAthenaContext context;

    /**
     * The event factory for presence events
     */
    private final EventFactory factory = EventFactory.createAnnotatedFactory(PresenceEvent.class);
    /**
     * COW list of listeners.
     */
    private final CopyOnWriteArrayList<FortnitePresenceListener> listeners = new CopyOnWriteArrayList<>();
    /**
     * COW list of filters.
     */
    private final CopyOnWriteArrayList<PresenceFilter> filters = new CopyOnWriteArrayList<>();

    /**
     * The XMPP event listener.
     */
    private final Listener eventListener = new Listener();

    public Presences(DefaultAthenaContext context) {
        this.context = context;
        this.service = context.presence();
        if (context.xmppEnabled()) context.connection().addAsyncStanzaListener(eventListener, PresenceTypeFilter.AVAILABLE);
    }

    /**
     * Get the last online time of friends in your friends list.
     *
     * @return the {@link LastOnlineResponse}
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public LastOnlineResponse lastOnline() throws EpicGamesErrorException {
        final var call = service.lastOnline(context.localAccountId());
        return Requests.executeCall(call);
    }

    /**
     * Get the subscription settings
     *
     * @return the {@link SubscriptionSettings}
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public SubscriptionSettings settings() throws EpicGamesErrorException {
        final var call = service.subscriptionSettings(context.localAccountId());
        return Requests.executeCall(call);
    }

    /**
     * Set the subscription settings.
     *
     * @param settings the settings
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void setSettings(SubscriptionSettings settings) throws EpicGamesErrorException {
        final var call = service.setSubscriptionSettings(context.localAccountId(), settings);
        Requests.executeVoidCall(call);
    }

    /**
     * Subscribe to an account ID.
     *
     * @param accountId the account ID.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void subscribe(String accountId) throws EpicGamesErrorException {
        final var call = service.subscribe(context.localAccountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Unsubscribe from an account
     *
     * @param accountId the account ID.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void unsubscribe(String accountId) throws EpicGamesErrorException {
        final var call = service.unsubscribe(context.localAccountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Get the list of subscriptions
     *
     * @return a list of {@link PresenceSubscription}
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public List<PresenceSubscription> subscriptions() throws EpicGamesErrorException {
        final var call = service.subscriptions(context.localAccountId());
        return Requests.executeCall(call);
    }

    /**
     * Broadcast you are playing Fortnite.
     * Can only be done ever ~30 minutes.
     *
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void broadcast() throws EpicGamesErrorException {
        final var call = service.broadcast("fn", context.localAccountId());
        Requests.executeVoidCall(call);
    }

    /**
     * Add a filter
     *
     * @param filter the filter
     */
    public void useFilter(PresenceFilter filter) {
        filters.add(filter);
    }

    /**
     * Remove a filter.
     *
     * @param filter the filter
     */
    public void removeFilter(PresenceFilter filter) {
        filters.remove(filter);
    }

    /**
     * Register a presence listener
     *
     * @param listener the listener
     */
    public void registerPresenceListener(FortnitePresenceListener listener) {
        listeners.add(listener);
    }

    /**
     * Register a presence listener.
     *
     * @param listener the listener.
     */
    public void unregisterPresenceListener(FortnitePresenceListener listener) {
        listeners.remove(listener);
    }

    /**
     * Register an event listener.
     *
     * @param type the class/type to register.
     */
    public void registerEventListener(Object type) {
        factory.registerEventListener(type);
    }

    /**
     * Unregister an event listener.
     *
     * @param type the class/type to register.
     */
    public void unregisterEventListener(Object type) {
        factory.unregisterEventListener(type);
    }

    @Override
    public void close() {
        if (context.xmppEnabled()) context.connection().removeAsyncStanzaListener(eventListener);

        factory.dispose();
        listeners.clear();
        filters.clear();
    }

    /**
     * The XMPP event listener
     */
    private final class Listener implements StanzaListener {
        @Override
        public void processStanza(Stanza packet) {
            final var presence = (Presence) packet;
            if (presence.getStatus() == null) return;
            final var accountId = presence.getFrom().getLocalpartOrNull().asUnescapedString();
            final var fortnitePresence = context.gson().fromJson(presence.getStatus(), FortnitePresence.class);
            // ignore presences that aren't Fortnite
            // ideally we want to ignore them before deserializing but whatever
            if (fortnitePresence.productName() == null || !fortnitePresence.productName().equalsIgnoreCase("Fortnite")) return;

            fortnitePresence.setFrom(presence.getFrom());
            factory.invoke(PresenceEvent.class, fortnitePresence);
            listeners.forEach(listener -> listener.presenceReceived(fortnitePresence));
            filters.stream().filter(filter -> filter.active() && filter.ready() && filter.isRelevant(accountId)).forEach(filter -> filter.consume(fortnitePresence));
        }
    }

}
