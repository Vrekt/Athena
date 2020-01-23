package athena.channels.resource.requests.message;

import athena.channels.resource.channel.message.MessageAttachedEntity;

import java.util.List;

/**
 * A request used to send messages to a {@link athena.channels.resource.channel.Channel}
 *
 * @author Vrekt, Armisto
 */
public final class SendMessage {

    /**
     * The message.
     */
    private String message;
    /**
     * List of entities attached with this message.
     */
    private List<MessageAttachedEntity> entities;

    /**
     * Initialize this request.
     *
     * @param message  the message
     * @param entities the entities
     */
    public SendMessage(String message, List<MessageAttachedEntity> entities) {
        this.message = message;
        this.entities = entities;
    }
}
