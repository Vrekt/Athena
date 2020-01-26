package athena.context;

import athena.Athena;
import athena.account.service.AccountPublicService;
import athena.events.service.EventsPublicService;
import athena.fortnite.service.FortnitePublicService;
import athena.friend.Friends;
import athena.friend.service.FriendsPublicService;
import athena.presence.service.PresencePublicService;
import athena.stats.service.StatsproxyPublicService;
import athena.util.json.context.annotation.Context;
import athena.xmpp.XMPPConnectionManager;
import com.google.gson.Gson;

/**
 * Holds services and resources from the local {@link athena.Athena} instance
 */
public abstract class AthenaContext {

    /**
     * The accountId of the {@link Athena} instance.
     */
    protected String localAccountId;

    /**
     * Various services.
     */
    protected AccountPublicService accountPublicService;
    protected FriendsPublicService friendsPublicService;
    protected StatsproxyPublicService statsproxyPublicService;
    protected EventsPublicService eventsPublicService;
    protected FortnitePublicService fortnitePublicService;
    protected PresencePublicService presencePublicService;

    /**
     * The XMPP connection manager.
     */
    protected XMPPConnectionManager connectionManager;
    /**
     * Friends provider.
     */
    protected Friends friends;
    /**
     * Athena GSON instance.
     */
    protected Gson gson;

    /**
     * Initialize the context.
     *
     * @param athena the athena instance.
     */
    @Context
    protected void initializeContext(Athena athena) {
        this.localAccountId = athena.accountId();
        this.accountPublicService = athena.accountPublicService();
        this.friendsPublicService = athena.friendsPublicService();
        this.statsproxyPublicService = athena.statsproxyPublicService();
        this.eventsPublicService = athena.eventsPublicService();
        this.fortnitePublicService = athena.fortnitePublicService();
        this.presencePublicService = athena.presencePublicService();
        this.connectionManager = athena.xmpp();
        this.friends = athena.friend();
        this.gson = athena.gson();
    }

    protected void initializeServices(Athena athena) {
        this.accountPublicService = athena.accountPublicService();
        this.friendsPublicService = athena.friendsPublicService();
        this.statsproxyPublicService = athena.statsproxyPublicService();
        this.eventsPublicService = athena.eventsPublicService();
        this.fortnitePublicService = athena.fortnitePublicService();
        this.presencePublicService = athena.presencePublicService();
    }

    protected void initializeOther(Athena athena) {
        this.localAccountId = athena.accountId();
        this.connectionManager = athena.xmpp();
        this.gson = athena.gson();
    }

    protected void initializeResources(Athena athena) {
        this.friends = athena.friend();
    }

}
