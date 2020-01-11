package athena.presence.resource.subscription;

/**
 * Provides response/request for subscription settings in {@link athena.presence.service.PresencePublicService}
 */
public final class SubscriptionSettings {

    /**
     * The broadcast settings.
     */
    private Broadcast broadcast;

    public SubscriptionSettings(boolean enableBroadcast) {
        broadcast = new Broadcast();
        broadcast.enabled = enableBroadcast;
    }

    /**
     * @return {@code true} if broadcast is enabled.
     */
    public boolean enabled() {
        return broadcast.enabled;
    }

    /**
     * Broadcast settings.
     */
    private static final class Broadcast {
        private boolean enabled;
    }

}
