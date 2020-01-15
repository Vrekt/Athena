package athena.channels.resource.requests.member;

import java.util.List;

/**
 * A request used to add members to a channel.
 */
public final class AddMembers {

    /**
     * The channel ID.
     */
    private String containerId;
    /**
     * List of account IDs you want to add.
     */
    private List<String> members;

    /**
     * Initialize this request.
     *
     * @param channelId  the channel ID.
     * @param accountIds list of account IDs.
     */
    public AddMembers(String channelId, List<String> accountIds) {
        this.containerId = channelId;
        this.members = accountIds;
    }

}
