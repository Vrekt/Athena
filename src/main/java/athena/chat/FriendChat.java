package athena.chat;

import athena.account.resource.Account;
import athena.chat.resource.BasicMessage;
import athena.chat.resource.listener.IncomingMessageListener;
import athena.context.DefaultAthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.friend.resource.Friend;
import athena.friend.resource.summary.Profile;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;

import java.io.Closeable;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

/**
 * Provides access to the XMPP chat.
 */
public final class FriendChat implements StanzaListener, Closeable {

    /**
     * List of consumers/listeners to accept messages.
     */
    private final CopyOnWriteArraySet<Consumer<BasicMessage>> messageConsumers = new CopyOnWriteArraySet<>();
    private final CopyOnWriteArraySet<IncomingMessageListener> messageListeners = new CopyOnWriteArraySet<>();

    /**
     * The context
     */
    private DefaultAthenaContext context;

    public FriendChat(DefaultAthenaContext context) {
        this.context = context;
        context.connection().addAsyncStanzaListener(this, MessageTypeFilter.CHAT);
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

    /**
     * Send a message to the specified {@code accountId}
     *
     * @param accountId the account ID.
     * @param message   the message
     * @throws EpicGamesErrorException if the message could not be sent.
     */
    public void sendMessage(String accountId, String message) {
        sendMessage(JidCreate.bareFromOrThrowUnchecked(accountId + "@prod.ol.epicgames.com"), message);
    }

    /**
     * Send a message to the specified {@code account}
     *
     * @param account the account
     * @param message the message
     * @throws EpicGamesErrorException if the message could not be sent.
     */
    public void sendMessage(Account account, String message) {
        sendMessage(JidCreate.bareFromOrThrowUnchecked(account.accountId() + "@prod.ol.epicgames.com"), message);
    }

    /**
     * Send a message to the specified {@code friend}
     *
     * @param friend  the friend
     * @param message the message
     * @throws EpicGamesErrorException if the message could not be sent.
     */
    public void sendMessage(Friend friend, String message) {
        sendMessage(JidCreate.bareFromOrThrowUnchecked(friend.accountId() + "@prod.ol.epicgames.com"), message);
    }

    /**
     * Send a message to the specified {@code jid}
     *
     * @param profile the profile
     * @param message the message
     * @throws EpicGamesErrorException if the message could not be sent.
     */
    public void sendMessage(Profile profile, String message) {
        sendMessage(JidCreate.bareFromOrThrowUnchecked(profile.accountId() + "@prod.ol.epicgames.com"), message);
    }

    /**
     * Send a message to the specified {@code jid}
     *
     * @param jid     the jid
     * @param message the message
     * @throws EpicGamesErrorException if the message could not be sent.
     */
    public void sendMessage(Jid jid, String message) {
        final var packet = new Message(jid, Message.Type.chat);
        packet.setBody(message);

        try {
            context.connection().sendStanza(packet);
        } catch (SmackException.NotConnectedException | InterruptedException exception) {
            throw EpicGamesErrorException.createFromOther(exception);
        }
    }

    @Override
    public void processStanza(Stanza packet) {
        final var message = (Message) packet;
        final var toBasicMessage = new BasicMessage(message.getBody(), message.getFrom(), context);
        messageConsumers.forEach(consumer -> consumer.accept(toBasicMessage));
        messageListeners.forEach(messageListener -> messageListener.onMessage(toBasicMessage));
    }

    @Override
    public void close() {
        context.connection().removeAsyncStanzaListener(this);

        messageConsumers.clear();
        messageListeners.clear();
    }


}
