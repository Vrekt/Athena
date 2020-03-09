package athena.party.resource.chat;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatException;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;

/**
 * Represents the party chat.
 */
public final class PartyChat {

    /**
     * The MUC manager
     */
    private final MultiUserChatManager manager;

    /**
     * The chat
     */
    private MultiUserChat chat;

    public PartyChat(MultiUserChatManager manager) {
        this.manager = manager;
    }

    /**
     * Join a new chat.
     *
     * @param partyId     the party ID.
     * @param displayName the display name
     * @param accountId   the account ID
     * @param resource    the resource party of the user JID.
     */
    public void joinNewChat(String partyId, String displayName, String accountId, String resource) {
        join(partyId, displayName, accountId, resource);
    }

    /**
     * Join a new chat.
     *
     * @param partyId     the party ID.
     * @param displayName the display name
     * @param accountId   the account ID
     * @param resource    the resource party of the user JID.
     */
    private void join(String partyId, String displayName, String accountId, String resource) {
        chat = manager.getMultiUserChat(JidCreate.entityBareFromOrThrowUnchecked("Party-" + partyId + "@muc.prod.ol.epicgames.com"));
        final var nickname = Resourcepart.fromOrThrowUnchecked(displayName + ":" + accountId + ":" + resource);
        try {
            chat.join(nickname);
        } catch (SmackException.NoResponseException
                | XMPPException.XMPPErrorException
                | SmackException.NotConnectedException
                | MultiUserChatException.NotAMucServiceException
                | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Leave this party chat.
     */
    public void leave() {
        if (!chat.isJoined()) return;
        try {
            chat.leave();
        } catch (SmackException.NotConnectedException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Send a message to this chat.
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        try {
            chat.sendMessage(message);
        } catch (SmackException.NotConnectedException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @return the chat
     */
    public MultiUserChat chat() {
        return chat;
    }
}
