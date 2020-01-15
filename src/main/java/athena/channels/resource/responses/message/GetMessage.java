package athena.channels.resource.responses.message;

import athena.channels.resource.channel.member.ChannelMember;
import athena.channels.resource.channel.message.ChannelMessage;

import java.util.List;

/**
 * A response for getting a singular message in a {@link athena.channels.resource.channel.Channel}
 */
public final class GetMessage {

    /**
     * The message.
     */
    private ChannelMessage message;
    /**
     * List of users associated with the message?
     */
    private List<ChannelMember> users;

    /**
     * @return the message
     */
    public ChannelMessage message() {
        return message;
    }

    /**
     * @return list of users.
     */
    public List<ChannelMember> users() {
        return users;
    }
}
