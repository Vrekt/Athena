package athena.channels.resource.requests.channel;

import athena.channels.resource.channel.badge.Badge;

import java.util.List;

/**
 * A request used to create channels.
 */
public final class CreateChannel {

    /**
     * The name of the channel.
     */
    private String name;
    /**
     * The channel badge.
     */
    private Badge badge;
    /**
     * List of badge colors.
     */
    private List<String> badgeColor;
    /**
     * List of channel members by account ID.
     */
    private List<String> members;

    /**
     * Initialize this request.
     *
     * @param name       the name of the channel.
     * @param badge      the badge
     * @param badgeColor the badge colors.
     * @param members    list of members to initialize this channel with.
     */
    public CreateChannel(String name, Badge badge, List<String> badgeColor, List<String> members) {
        this.name = name;
        this.badge = badge;
        this.badgeColor = badgeColor;
        this.members = members;
    }

    /**
     * Initialize this request.
     *
     * @param name    the name.
     * @param members array of members to initialize this channel with.
     */
    public CreateChannel(String name, String... members) {
        this.name = name;
        this.badge = Badge.ace;
        this.badgeColor = List.of("#FFFFFF");
        this.members = List.of(members);
    }

}
