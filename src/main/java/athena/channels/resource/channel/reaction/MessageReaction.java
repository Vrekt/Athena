package athena.channels.resource.channel.reaction;

import java.util.List;

/**
 * Represents a {@link athena.channels.resource.channel.message.ChannelMessage} reaction.
 */
public final class MessageReaction {

    /**
     * The reaction.
     */
    private String reaction;
    /**
     * List of users who reacted?
     */
    private List<String> users;

    /**
     * @return the reaction
     */
    public String reaction() {
        return reaction;
    }

    /**
     * @return List of users who reacted?
     */
    public List<String> users() {
        return users;
    }
}
