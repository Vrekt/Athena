package athena.presence.resource.listener;

import athena.presence.resource.FortnitePresence;

/**
 * Used to listen for presences.
 */
public interface FortnitePresenceListener {

    /**
     * Invoked when a presence is received.
     *
     * @param presence the presence.
     */
    void presenceReceived(FortnitePresence presence);

}
