package athena.context;

import athena.Athena;
import athena.account.service.AccountPublicService;
import athena.events.service.EventsPublicService;
import athena.fortnite.service.FortnitePublicService;
import athena.friend.Friends;
import athena.friend.service.FriendsPublicService;
import athena.presence.service.PresencePublicService;
import athena.stats.service.StatsproxyPublicService;
import athena.xmpp.XMPPConnectionManager;
import com.google.gson.Gson;

/**
 * Basic implementation of the {@link AthenaContext}
 */
public final class DefaultAthenaContext extends AthenaContext {

    public DefaultAthenaContext(Athena athena) {
        initializeContext(athena);
    }

    /**
     * @return the athena account ID.
     */
    public String localAccountId() {
        return localAccountId;
    }

    /**
     * @return the account public service.
     */
    public AccountPublicService account() {
        return accountPublicService;
    }

    /**
     * @return the friends public service.
     */
    public FriendsPublicService friendsService() {
        return friendsPublicService;
    }

    /**
     * @return the stats proxy public service.
     */
    public StatsproxyPublicService statsProxy() {
        return statsproxyPublicService;
    }

    /**
     * @return the events public service
     */
    public EventsPublicService events() {
        return eventsPublicService;
    }

    /**
     * @return the fortnite public service.
     */
    public FortnitePublicService fortnite() {
        return fortnitePublicService;
    }

    /**
     * @return the presence public service.
     */
    public PresencePublicService presence() {
        return presencePublicService;
    }

    /**
     * @return the gson
     */
    public Gson gson() {
        return gson;
    }

    /**
     * @return the XMPP connection manager.
     */
    public XMPPConnectionManager connectionManager() {
        return connectionManager;
    }

    /**
     * @return friends provider.
     */
    public Friends friends() {
        return friends;
    }

    public void friends(Friends friends) {
        this.friends = friends;
    }

}
