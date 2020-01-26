package athena.presence;

import athena.context.DefaultAthenaContext;
import athena.presence.resource.FortnitePresence;
import athena.presence.resource.annotation.PresenceEvent;
import athena.presence.resource.filter.PresenceFilter;
import athena.presence.resource.listener.FortnitePresenceListener;
import athena.util.event.EventFactory;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.PresenceTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Provides XMPP access to {@link Presences}
 */
public final class PresenceXMPPProvider implements StanzaListener {

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
     * The context
     */
    private DefaultAthenaContext context;

    PresenceXMPPProvider(DefaultAthenaContext context) {
        this.context = context;
        context
                .connectionManager()
                .connection()
                .addAsyncStanzaListener(this, PresenceTypeFilter.AVAILABLE);
    }

    @Override
    public void processStanza(Stanza packet) {
        final var presence = (Presence) packet;
        if (presence.getStatus() == null) return;
        final var accountId = presence.getFrom().getLocalpartOrNull().asUnescapedString();
        final var fortnitePresence = context.gson().fromJson(presence.getStatus(), FortnitePresence.class);
        fortnitePresence.setFrom(presence.getFrom());

        factory.invoke(PresenceEvent.class, fortnitePresence);
        listeners.forEach(listener -> listener.presenceReceived(fortnitePresence));
        filters.stream().filter(filter -> filter.active() && filter.ready() && filter.isRelevant(accountId)).forEach(filter -> filter.consume(fortnitePresence));
    }

    /**
     * Add a filter
     *
     * @param filter the filter
     */
    void useFilter(PresenceFilter filter) {
        filters.add(filter);
    }

    /**
     * Remove a filter.
     *
     * @param filter the filter
     */
    void removeFilter(PresenceFilter filter) {
        filters.remove(filter);
    }

    /**
     * Register a presence listener
     *
     * @param listener the listener
     */
    void registerPresenceListener(FortnitePresenceListener listener) {
        listeners.add(listener);
    }

    /**
     * Register a presence listener.
     *
     * @param listener the listener.
     */
    void unregisterPresenceListener(FortnitePresenceListener listener) {
        listeners.remove(listener);
    }

    /**
     * Register an event listener.
     *
     * @param type the class/type to register.
     */
    void registerEventListener(Object type) {
        factory.registerEventListener(type);
    }

    /**
     * Unregister an event listener.
     *
     * @param type the class/type to register.
     */
    void unregisterEventListener(Object type) {
        factory.unregisterEventListener(type);
    }

    /**
     * Invoked after refreshing to re-add the stanza listener.
     *
     * @param context the new context.
     */
    void afterRefresh(DefaultAthenaContext context) {
        this.context = context;

        context
                .connectionManager()
                .connection()
                .addAsyncStanzaListener(this, PresenceTypeFilter.AVAILABLE);
    }

    /**
     * Invoked before a refresh to remove the stanza listener.
     */
    void beforeRefresh() {
        context.connectionManager().connection().removeAsyncStanzaListener(this);
    }

    /**
     * Invoked to shutdown this provider.
     */
    void shutdown() {
        context.connectionManager().connection().removeAsyncStanzaListener(this);

        factory.dispose();
        listeners.clear();
        filters.clear();
    }

}
