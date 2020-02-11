package athena.chat.resource.listener;

import athena.chat.resource.BasicMessage;

/**
 * A basic listener for incoming XMPP messages in {@link athena.chat.XMPPChat}
 */
public interface IncomingMessageListener {

    /**
     * Invoked when a message is received
     *
     * @param message the message
     */
    void onMessage(BasicMessage message);

}
