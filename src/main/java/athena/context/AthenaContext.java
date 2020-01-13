package athena.context;

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
 * Holds a basic state of {@link athena.Athena}
 * For example, services, account, gson, xmpp and more.
 */
public final class AthenaContext {

    /**
     * Account ID of the athena instance.
     */
    private String accountId;
    /**
     * Services.
     */
    private AccountPublicService accountPublicService;
    private FriendsPublicService friendsPublicService;
    private StatsproxyPublicService statsproxyPublicService;
    private EventsPublicService eventsPublicService;
    private FortnitePublicService fortnitePublicService;
    private PresencePublicService presencePublicService;

    /**
     * Athena GSON instance.
     */
    private Gson gson;
    /**
     * The XMPP connection manager.
     */
    private XMPPConnectionManager connectionManager;

    /**
     * Friends provider.
     */
    private Friends friends;

    /**
     * @param accountId the account ID of athena.
     */
    public void accountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * @param accountPublicService the {@link AccountPublicService}
     */
    public void accountPublicService(AccountPublicService accountPublicService) {
        this.accountPublicService = accountPublicService;
    }

    /**
     * @param friendsPublicService the {@link FriendsPublicService}
     */
    public void friendsPublicService(FriendsPublicService friendsPublicService) {
        this.friendsPublicService = friendsPublicService;
    }

    /**
     * @param statsproxyPublicService the {@link StatsproxyPublicService}
     */
    public void statsproxyPublicService(StatsproxyPublicService statsproxyPublicService) {
        this.statsproxyPublicService = statsproxyPublicService;
    }

    /**
     * @param eventsPublicService the {@link EventsPublicService}
     */
    public void eventsPublicService(EventsPublicService eventsPublicService) {
        this.eventsPublicService = eventsPublicService;
    }

    /**
     * @param fortnitePublicService the {@link FortnitePublicService}
     */
    public void fortnitePublicService(FortnitePublicService fortnitePublicService) {
        this.fortnitePublicService = fortnitePublicService;
    }

    /**
     * @param presencePublicService the {@link PresencePublicService}
     */
    public void presencePublicService(PresencePublicService presencePublicService) {
        this.presencePublicService = presencePublicService;
    }

    /**
     * @param gson the athena GSON instance.
     */
    public void gson(Gson gson) {
        this.gson = gson;
    }

    /**
     * @param connectionManager the connection manager.
     */
    public void connectionManager(XMPPConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void friends(Friends friends) {
        this.friends = friends;
    }

    /**
     * @return the athena account ID.
     */
    public String accountId() {
        return accountId;
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
}
