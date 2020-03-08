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
import athena.xmpp.XMPPConnectionManager;
import com.google.gson.Gson;

/**
 * Basic implementation of the {@link AthenaContext}
 */
public final class DefaultAthenaContext extends AthenaContext {

    /**
     * Initialize services only.
     *
     * @param athena the athena instance.
     */
    public void initializeServicesOnly(Athena athena) {
        initializeServices(athena);
    }

    /**
     * Initialize resources only.
     *
     * @param athena the athena instance.
     */
    public void initializeResourcesOnly(Athena athena) {
        initializeResources(athena);
    }

    /**
     * Initialize other things only.
     *
     * @param athena the athena instance.
     */
    public void initializeOtherOnly(Athena athena) {
        initializeOther(athena);
    }

    /**
     * Initializes account information
     *
     * @param athena the athena instance
     */
    public void initializeAccountOnly(Athena athena) {
        initializeAccount(athena);
    }

    /**
     * @return the athena account ID.
     */
    public String localAccountId() {
        return localAccountId;
    }

    /**
     * @return the athena display name.
     */
    public String displayName() {
        return displayName;
    }

    /**
     * @return the athena platform
     */
    public Platform platform() {
        return platform;
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
     * @return the party service
     */
    public PartyService partyService() {
        return partyService;
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

    /**
     * @return XMPP chat provider.
     */
    public FriendChat chat() {
        return chat;
    }

    /**
     * @return party provider
     */
    public Parties party() {
        return parties;
    }

    /**
     * @return {@code true} if XMPP is enabled.
     */
    public boolean xmppEnabled() {
        return xmppEnabled;
    }

}
