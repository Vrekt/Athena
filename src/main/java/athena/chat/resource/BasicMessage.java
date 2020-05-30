package athena.chat.resource;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.friend.resource.summary.Profile;
import athena.friend.service.FriendsPublicService;
import athena.util.request.Requests;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.Jid;

/**
 * Represents a basic XMPP message.
 */
public final class BasicMessage {

    /**
     * The message and account ID of who sent it.
     */
    private final String message, accountId, localAccountId;
    /**
     * The JID of who it was from.
     */
    private final Jid from;

    /**
     * Services
     */
    private final Accounts accounts;
    private final FriendsPublicService friendsPublicService;

    /**
     * The XMPP connection.
     */
    private final XMPPTCPConnection connection;

    /**
     * Initialize
     *
     * @param message              the message
     * @param from                 who it was from
     * @param localAccountId       the athena account ID
     * @param accounts             accounts provider
     * @param friendsPublicService friends public service
     * @param connection           XMPP connection
     */
    public BasicMessage(String message, Jid from, String localAccountId, Accounts accounts, FriendsPublicService friendsPublicService, XMPPTCPConnection connection) {
        this.message = message;
        this.from = from;
        this.localAccountId = localAccountId;
        this.accountId = from.getLocalpartOrThrow().asUnescapedString();

        this.accounts = accounts;
        this.friendsPublicService = friendsPublicService;
        this.connection = connection;
    }

    /**
     * @return the message
     */
    public String message() {
        return message;
    }

    /**
     * @return the account ID of who sent the message.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the jid of who it was from.
     */
    public Jid from() {
        return from;
    }

    /**
     * The profile of who sent the message.
     *
     * @return the profile
     */
    public Profile friendProfile() {
        return Requests.executeCall(friendsPublicService.profile(localAccountId, accountId, true));
    }

    /**
     * The account of who sent the message.
     *
     * @return the account
     */
    public Account account() {
        return accounts.findByAccountId(accountId);
    }

    /**
     * Reply to this message.
     *
     * @param message the message
     */
    public void reply(String message) {
        final var messagePacket = new Message(from, Message.Type.chat);
        messagePacket.setBody(message);

        try {
            connection.sendStanza(messagePacket);
        } catch (SmackException.NotConnectedException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

}
