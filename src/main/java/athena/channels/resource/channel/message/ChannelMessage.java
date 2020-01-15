package athena.channels.resource.channel.message;

import athena.channels.resource.channel.reaction.MessageReaction;

import java.time.Instant;
import java.util.List;

/**
 * Represents a message in a {@link athena.channels.resource.channel.Channel}
 */
public final class ChannelMessage {

    /**
     * The id of the message
     * the container type, ex: 'channel'
     * the channel/container ID.
     * the account ID of who sent the message
     * the message
     * the timestamp, unix epoch milli
     */
    private String id, containerType, containerId, accountId, message, timestamp;
    /**
     * List of reactions
     */
    private List<MessageReaction> reactions;
    /**
     * List of attached entities.
     */
    private List<MessageAttachedEntity> entities;

    /**
     * @return the message ID.
     */
    public String id() {
        return id;
    }

    /**
     * @return the container type, ex: 'channel'
     */
    public String containerType() {
        return containerType;
    }

    /**
     * @return the channel/container ID.
     */
    public String containerId() {
        return containerId;
    }

    /**
     * @return the account ID of who sent the message.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the message.
     */
    public String message() {
        return message;
    }

    /**
     * @return timestamp of when the message was sent.
     */
    public Instant timestamp() {
        return Instant.ofEpochMilli(Long.parseLong(timestamp));
    }

    /**
     * @return list of reactions
     */
    public List<MessageReaction> reactions() {
        return reactions;
    }

    /**
     * @return list of entities.
     */
    public List<MessageAttachedEntity> entities() {
        return entities;
    }
}
