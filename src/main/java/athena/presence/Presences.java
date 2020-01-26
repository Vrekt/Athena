package athena.presence;

import athena.context.DefaultAthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.presence.resource.LastOnlineResponse;
import athena.presence.resource.filter.PresenceFilter;
import athena.presence.resource.listener.FortnitePresenceListener;
import athena.presence.resource.subscription.PresenceSubscription;
import athena.presence.resource.subscription.SubscriptionSettings;
import athena.presence.service.PresencePublicService;
import athena.util.cleanup.AfterRefresh;
import athena.util.cleanup.BeforeRefresh;
import athena.util.cleanup.Shutdown;
import athena.util.request.Requests;

import java.util.List;

/**
 * Provides easy access to the {@link athena.presence.service.PresencePublicService} and XMPP.
 */
public final class Presences {

    private final PresencePublicService service;
    private PresenceXMPPProvider provider;
    private final String localAccountId;

    public Presences(DefaultAthenaContext context, boolean enableXmpp) {
        this.service = context.presence();
        this.localAccountId = context.localAccountId();
        provider = enableXmpp ? new PresenceXMPPProvider(context) : null;
    }

    /**
     * Get the last online time of friends in your friends list.
     *
     * @return the {@link LastOnlineResponse}
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public LastOnlineResponse lastOnline() throws EpicGamesErrorException {
        final var call = service.lastOnline(localAccountId);
        return Requests.executeCall(call);
    }

    /**
     * Get the subscription settings
     *
     * @return the {@link SubscriptionSettings}
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public SubscriptionSettings settings() throws EpicGamesErrorException {
        final var call = service.subscriptionSettings(localAccountId);
        return Requests.executeCall(call);
    }

    /**
     * Set the subscription settings.
     *
     * @param settings the settings
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void setSettings(SubscriptionSettings settings) throws EpicGamesErrorException {
        final var call = service.setSubscriptionSettings(localAccountId, settings);
        Requests.executeVoidCall(call);
    }

    /**
     * Subscribe to an account ID.
     *
     * @param accountId the account ID.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void subscribe(String accountId) throws EpicGamesErrorException {
        final var call = service.subscribe(localAccountId, accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Unsubscribe from an account
     *
     * @param accountId the account ID.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void unsubscribe(String accountId) throws EpicGamesErrorException {
        final var call = service.unsubscribe(localAccountId, accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Get the list of subscriptions
     *
     * @return a list of {@link PresenceSubscription}
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public List<PresenceSubscription> subscriptions() throws EpicGamesErrorException {
        final var call = service.subscriptions(localAccountId);
        return Requests.executeCall(call);
    }

    /**
     * Broadcast you are playing Fortnite.
     * Can only be done ever ~30 minutes.
     *
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void broadcast() throws EpicGamesErrorException {
        final var call = service.broadcast("fn", localAccountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Add a filter
     *
     * @param filter the filter
     */
    public void useFilter(PresenceFilter filter) {
        if (provider != null) provider.useFilter(filter);
    }

    /**
     * Remove a filter.
     *
     * @param filter the filter
     */
    public void removeFilter(PresenceFilter filter) {
        if (provider != null) provider.removeFilter(filter);
    }

    /**
     * Register a presence listener
     *
     * @param listener the listener
     */
    public void registerPresenceListener(FortnitePresenceListener listener) {
        if (provider != null) provider.registerPresenceListener(listener);
    }

    /**
     * Register a presence listener.
     *
     * @param listener the listener.
     */
    public void unregisterPresenceListener(FortnitePresenceListener listener) {
        if (provider != null) provider.unregisterPresenceListener(listener);
    }

    /**
     * Register an event listener.
     *
     * @param type the class/type to register.
     */
    public void registerEventListener(Object type) {
        if (provider != null) provider.registerEventListener(type);
    }

    /**
     * Unregister an event listener.
     *
     * @param type the class/type to register.
     */
    public void unregisterEventListener(Object type) {
        if (provider != null) provider.unregisterEventListener(type);
    }

    @AfterRefresh
    private void after(DefaultAthenaContext context) {
        if (provider != null) provider.afterRefresh(context);
    }

    @BeforeRefresh
    private void before() {
        if (provider != null) provider.beforeRefresh();
    }

    @Shutdown
    private void shutdown() {
        if (provider != null) provider.shutdown();
    }
}
