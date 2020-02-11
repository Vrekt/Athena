package athena.chat;

import athena.chat.resource.BasicMessage;
import athena.chat.resource.listener.IncomingMessageListener;
import athena.context.DefaultAthenaContext;
import athena.util.cleanup.AfterRefresh;
import athena.util.cleanup.BeforeRefresh;
import athena.util.cleanup.Shutdown;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

/**
 * Provides access to the XMPP chat.
 */
public final class XMPPChat implements StanzaListener {

    /**
     * List of consumers/listeners to accept messages.
     */
    private final CopyOnWriteArraySet<Consumer<BasicMessage>> messageConsumers = new CopyOnWriteArraySet<>();
    private final CopyOnWriteArraySet<IncomingMessageListener> messageListeners = new CopyOnWriteArraySet<>();

    /**
     * The context
     */
    private DefaultAthenaContext context;

    public XMPPChat(DefaultAthenaContext context) {
        this.context = context;
        context.connectionManager().connection().addAsyncStanzaListener(this, MessageTypeFilter.CHAT);
    }

    /**
     * Adds a new message consumer/listener.
     *
     * @param messageConsumer the message
     */
    public void onMessage(Consumer<BasicMessage> messageConsumer) {
        messageConsumers.add(messageConsumer);
    }

    /**
     * Add a message listener.
     *
     * @param messageListener the message listener
     */
    public void addMessageListener(IncomingMessageListener messageListener) {
        messageListeners.add(messageListener);
    }

    /**
     * Remove a message listener.
     *
     * @param messageListener the message listener.
     */
    public void removeMessageListener(IncomingMessageListener messageListener) {
        messageListeners.remove(messageListener);
    }

    @Override
    public void processStanza(Stanza packet) {
        final var message = (Message) packet;
        final var toBasicMessage = new BasicMessage(message.getBody(), message.getFrom(), context);
        messageConsumers.forEach(consumer -> consumer.accept(toBasicMessage));
        messageListeners.forEach(messageListener -> messageListener.onMessage(toBasicMessage));
    }

    /**
     * Invoked after refreshing to re-add the stanza listener.
     *
     * @param context the new context.
     */
    @AfterRefresh
    private void afterRefresh(DefaultAthenaContext context) {
        this.context = context;

        context
                .connectionManager()
                .connection()
                .addAsyncStanzaListener(this, MessageTypeFilter.NORMAL);
    }

    /**
     * Invoked before a refresh to remove the stanza listener.
     */
    @BeforeRefresh
    private void beforeRefresh() {
        context.connectionManager().connection().removeAsyncStanzaListener(this);
    }

    /**
     * Invoked to shutdown this provider.
     */
    @Shutdown
    private void shutdown() {
        context.connectionManager().connection().removeAsyncStanzaListener(this);

        messageConsumers.clear();
        messageListeners.clear();
    }


}
