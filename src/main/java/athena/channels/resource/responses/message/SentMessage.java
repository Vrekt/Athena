package athena.channels.resource.responses.message;

import athena.channels.resource.channel.message.MessageAttachedEntity;

import java.util.List;

/**
 * The response of a sent message.
 */
public final class SentMessage {

    /**
     * The ID of the message.
     */
    private String id;
    /**
     * List of entities attached with the message.
     */
    private List<MessageAttachedEntity> entities;

    /**
     * @return ID of the message.
     */
    public String id() {
        return id;
    }

    /**
     * @return list of entities.
     */
    public List<MessageAttachedEntity> entities() {
        return entities;
    }

}
