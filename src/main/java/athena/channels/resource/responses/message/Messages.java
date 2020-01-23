package athena.channels.resource.responses.message;

import athena.channels.resource.channel.member.ChannelMember;
import athena.channels.resource.channel.message.ChannelMessage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A response of all messages.
 * TODO: metadata (probably a map right?)
 */
public final class Messages {

    /**
     * List of channel messages.
     */
    private List<ChannelMessage> messages;
    /**
     * List of users.
     */
    private List<ChannelMember> users;

    /**
     * @return list of channel messages.
     */
    public List<ChannelMessage> messages() {
        return messages;
    }

    /**
     * @return list of users who sent a message?
     */
    public List<ChannelMember> users() {
        return users;
    }

    /**
     * Filter messages by account ID.
     *
     * @param accountId the account ID.
     * @return a list of {@link ChannelMessage}
     */
    public List<ChannelMessage> filterByAccountId(String accountId) {
        return messages.stream().filter(channelMessage -> channelMessage.accountId().equals(accountId)).collect(Collectors.toList());
    }

    /**
     * Get a channel message by ID.
     *
     * @param messageId the message ID.
     * @return the {@link ChannelMessage} or {@code null} if not found.
     */
    public ChannelMessage getById(String messageId) {
        return messages.stream().filter(channelMessage -> channelMessage.id().equals(messageId)).findAny().orElse(null);
    }

}
