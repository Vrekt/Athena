package athena.context;

import athena.Athena;
import athena.account.service.AccountPublicService;
import athena.chat.FriendChat;
import athena.events.service.EventsPublicService;
import athena.fortnite.service.FortnitePublicService;
import athena.friend.Friends;
import athena.friend.service.FriendsPublicService;
import athena.party.Parties;
import athena.party.service.PartyService;
import athena.presence.service.PresencePublicService;
import athena.stats.service.StatsproxyPublicService;
import athena.types.Platform;
import athena.util.json.service.context.annotation.Context;
import athena.xmpp.XMPPConnectionManager;
import com.google.gson.Gson;

/**
 * Holds services and resources from the local {@link athena.Athena} instance
 */
public abstract class AthenaContext {

    /**
     * The accountId and displayName of the {@link Athena} instance.
     */
    protected String localAccountId, displayName;
    /**
     * The platform of the {@link Athena} instance.
     */
    protected Platform platform;

    /**
     * Various services.
     */
    protected AccountPublicService accountPublicService;
    protected FriendsPublicService friendsPublicService;
    protected StatsproxyPublicService statsproxyPublicService;
    protected EventsPublicService eventsPublicService;
    protected FortnitePublicService fortnitePublicService;
    protected PresencePublicService presencePublicService;
    protected PartyService partyService;

    /**
     * The XMPP connection manager.
     */
    protected XMPPConnectionManager connectionManager;
    /**
     * Friends provider.
     */
    protected Friends friends;
    /**
     * XMPP chat provider.
     */
    protected FriendChat chat;

    /**
     * Parties
     */
    protected Parties parties;

    /**
     * Athena GSON instance.
     */
    protected Gson gson;

    /**
     * If XMPP is enabled or not.
     */
    protected boolean xmppEnabled;

    /**
     * Initialize the context.
     *
     * @param athena the athena instance.
     */
    @Context
    protected void initializeContext(Athena athena) {
        this.localAccountId = athena.accountId();
        this.displayName = athena.displayName();
        this.platform = athena.platform();
        this.accountPublicService = athena.accountPublicService();
        this.friendsPublicService = athena.friendsPublicService();
        this.statsproxyPublicService = athena.statsproxyPublicService();
        this.eventsPublicService = athena.eventsPublicService();
        this.fortnitePublicService = athena.fortnitePublicService();
        this.presencePublicService = athena.presencePublicService();
        this.partyService = athena.partyService();
        this.connectionManager = athena.xmpp();
        this.friends = athena.friend();
        this.chat = athena.chat();
        this.parties = athena.party();
        this.gson = athena.gson();
        this.xmppEnabled = athena.xmppEnabled();
    }

    protected void initializeServices(Athena athena) {
        this.accountPublicService = athena.accountPublicService();
        this.friendsPublicService = athena.friendsPublicService();
        this.statsproxyPublicService = athena.statsproxyPublicService();
        this.eventsPublicService = athena.eventsPublicService();
        this.fortnitePublicService = athena.fortnitePublicService();
        this.presencePublicService = athena.presencePublicService();
        this.partyService = athena.partyService();
    }

    protected void initializeOther(Athena athena) {
        this.platform = athena.platform();
        this.connectionManager = athena.xmpp();
        this.gson = athena.gson();
        this.localAccountId = athena.accountId();
        this.xmppEnabled = athena.xmppEnabled();
    }

    protected void initializeResources(Athena athena) {
        this.friends = athena.friend();
        this.chat = athena.chat();
        this.parties = athena.party();
    }

    protected void initializeAccount(Athena athena) {
        this.displayName = athena.displayName();
    }

}
