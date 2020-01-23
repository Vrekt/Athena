package athena.channels.resource.responses.dm;

import java.util.List;

/**
 * Represents a direct message channel.
 *
 * @author Vrekt, Armisto
 */
public final class DirectMessage {

    /**
     * List of participants by account ID.
     */
    private List<String> participants;
    /**
     * Number of unread messages?
     */
    private String unreadCount;

    /**
     * @return List of participants by account ID.
     */
    public List<String> participants() {
        return participants;
    }

    /**
     * @return Number of unread messages?
     */
    public int unreadCount() {
        return Integer.parseInt(unreadCount);
    }
}
