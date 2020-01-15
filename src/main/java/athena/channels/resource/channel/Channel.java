package athena.channels.resource.channel;

import athena.channels.resource.channel.badge.Badge;
import athena.channels.resource.channel.member.ChannelMember;

import java.util.List;

/**
 * Represents a channel.
 */
public final class Channel {
    /**
     * The channel ID and name and number of unread messages?
     */
    private String channelId, name, unreadCount;
    /**
     * The badge of this channel.
     */
    private Badge badge;
    /**
     * The badge colors.
     */
    private List<String> badgeColor;
    /**
     * List of members in this channel.
     */
    private List<ChannelMember> members;

    /**
     * @return the ID of this channel.
     */
    public String channelId() {
        return channelId;
    }

    /**
     * @return the name of this channel.
     */
    public String name() {
        return name;
    }

    /**
     * @return the badge of this channel.
     */
    public Badge badge() {
        return badge;
    }

    /**
     * @return the badge colors.
     */
    public List<String> badgeColor() {
        return badgeColor;
    }

    /**
     * @return list of members in this channel.
     */
    public List<ChannelMember> members() {
        return members;
    }

    /**
     * @return number of unread messages?
     */
    public int unreadCount() {
        return Integer.parseInt(unreadCount);
    }
}
