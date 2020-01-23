package athena.channels.resource.responses.channel;

import athena.channels.resource.channel.badge.Badge;
import athena.channels.resource.channel.message.ChannelMessage;
import athena.channels.resource.responses.channel.message.MostRecentMessage;

import java.util.List;

/**
 * Represents a channel a user is in.
 *
 * @author Vrekt, Armisto
 */
public final class UserChannel {

    /**
     * The channel ID and name.
     */
    private String channelId, name, unreadCount;
    /**
     * The badge of this channel.
     */
    private Badge badge;
    /**
     * The badge colors and list of members by account ID.
     */
    private List<String> badgeColor, members;

    /**
     * The most recent message.
     */
    private MostRecentMessage mostRecentMessage;

    /**
     * @return the channel ID.
     */
    public String channelId() {
        return channelId;
    }

    /**
     * @return the name of the channel.
     */
    public String name() {
        return name;
    }

    /**
     * @return number of unread messages.
     */
    public int unreadCount() {
        return Integer.parseInt(unreadCount);
    }

    /**
     * @return the channel badge.
     */
    public Badge badge() {
        return badge;
    }

    /**
     * @return list of badge colors.
     */
    public List<String> badgeColor() {
        return badgeColor;
    }

    /**
     * @return list of members by account ID.
     */
    public List<String> members() {
        return members;
    }

    /**
     * @return the most recent message.
     */
    public ChannelMessage mostRecentMessage() {
        return mostRecentMessage == null ? null : mostRecentMessage.message();
    }
}
