package athena.channels.resource.responses.channel.message;

import athena.channels.resource.channel.message.ChannelMessage;

/**
 * Represents the most recent message.
 *
 * @author Vrekt, Armisto
 */
public final class MostRecentMessage {

    /**
     * The message.
     */
    private ChannelMessage message;

    /**
     * @return the message.
     */
    public ChannelMessage message() {
        return message;
    }
}
