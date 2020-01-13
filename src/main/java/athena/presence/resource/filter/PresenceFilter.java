package athena.presence.resource.filter;

import athena.presence.resource.FortnitePresence;

/**
 * Used to filter {@link athena.presence.resource.FortnitePresence}s
 */
public interface PresenceFilter {

    /**
     * @return {@code true} if this filter is ready to accept presences.
     */
    boolean ready();

    /**
     * @return {@code true} if this filter is active.
     */
    boolean active();

    /**
     * @param accountId the account ID of who the presence came from.
     * @return {@code true} if the presence is relevant.
     */
    boolean isRelevant(String accountId);

    /**
     * Consume the presence, only invoked if {@code ready} is {@code true} and {@code active} is {@code true} and {@code isRelevant} is {@code true}
     *
     * @param presence the presence
     */
    void consume(FortnitePresence presence);

}
