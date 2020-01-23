package athena.channels.resource.requests.channel;

import athena.channels.resource.channel.badge.Badge;

import java.util.List;

/**
 * A request used to edit a channel.
 *
 * @author Vrekt, Armisto
 */
public final class EditChannel {

    /**
     * The new name of the channel.
     */
    private String name;
    /**
     * The new badge.
     */
    private Badge badge;
    /**
     * List of new badge colors.
     */
    private List<String> badgeColor;

    /**
     * Initialize this request.
     *
     * @param name       the name.
     * @param badge      the badge
     * @param badgeColor list of badge colors.
     */
    public EditChannel(String name, Badge badge, List<String> badgeColor) {
        this.name = name;
        this.badge = badge;
        this.badgeColor = badgeColor;
    }

}
