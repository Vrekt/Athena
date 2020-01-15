package athena.channels.resource.responses.channel;

import athena.channels.resource.responses.dm.DirectMessage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A response of all channels and DMs a user is in.
 */
public final class UserChannels {

    /**
     * List of channels.
     */
    private List<UserChannel> channels;

    /**
     * List of direct messages.
     */
    private List<DirectMessage> dms;

    /**
     * @return the channels.
     */
    public List<UserChannel> channels() {
        return channels;
    }

    /**
     * @return the dms.
     */
    public List<DirectMessage> dms() {
        return dms;
    }

    /**
     * Get a channel by channel ID.
     *
     * @param channelId the channel ID.
     * @return the {@link UserChannel} or {@code null} if not found.
     */
    public UserChannel getChannelById(String channelId) {
        return channels.stream().filter(userChannel -> userChannel.channelId().equals(channelId)).findAny().orElse(null);
    }

    /**
     * Get a list of channels by name.
     *
     * @param channelName the channel name.
     * @return a list of {@link UserChannel}
     */
    public List<UserChannel> getChannelsByName(String channelName) {
        return channels.stream().filter(userChannel -> userChannel.name().equals(channelName)).collect(Collectors.toList());
    }

    /**
     * Get a direct message between you and another account.
     *
     * @param accountId the account ID.
     * @return the {@link DirectMessage} or {@code null}
     */
    public DirectMessage getDirectMessage(String accountId) {
        return dms.stream().filter(directMessage -> directMessage.participants().contains(accountId)).findAny().orElse(null);
    }

}
