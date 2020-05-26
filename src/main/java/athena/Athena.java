package athena;

import athena.account.Accounts;
import athena.account.resource.device.Device;
import athena.account.service.AccountPublicService;
import athena.authentication.service.AuthenticationService;
import athena.authentication.session.Session;
import athena.authentication.type.AuthClient;
import athena.authentication.type.GrantType;
import athena.channels.service.ChannelsPublicService;
import athena.chat.FriendChat;
import athena.eula.service.EulatrackingPublicService;
import athena.events.Events;
import athena.events.service.EventsPublicService;
import athena.exception.EpicGamesErrorException;
import athena.exception.UnsupportedBuildException;
import athena.fortnite.Fortnite;
import athena.fortnite.service.FortnitePublicService;
import athena.friend.Friends;
import athena.friend.service.FriendsPublicService;
import athena.groups.service.GroupsService;
import athena.interceptor.InterceptorAction;
import athena.party.Parties;
import athena.party.service.PartyService;
import athena.presence.Presences;
import athena.presence.service.PresencePublicService;
import athena.stats.StatisticsV2;
import athena.stats.service.StatsproxyPublicService;
import athena.types.Platform;
import athena.xmpp.XMPPConnectionManager;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * The default Athena implementation.
 */
public interface Athena {

    /**
     * Various tokens for authorization.
     * TODO: Use {@link AuthClient}
     */
    String EPIC_GAMES_LAUNCHER_TOKEN = "MzQ0NmNkNzI2OTRjNGE0NDg1ZDgxYjc3YWRiYjIxNDE6OTIwOWQ0YTVlMjVhNDU3ZmI5YjA3NDg5ZDMxM2I0MWE=";
    String FORTNITE_TOKEN = "ZWM2ODRiOGM2ODdmNDc5ZmFkZWEzY2IyYWQ4M2Y1YzY6ZTFmMzFjMjExZjI4NDEzMTg2MjYyZDM3YTEzZmM4NGQ=";
    String KAIROS_TOKEN = "NWI2ODU2NTNiOTkwNGMxZDkyNDk1ZWU4ODU5ZGNiMDA6N1EybWNtbmV5dXZQbW9SWWZ3TTdnZkVyQTZpVWpoWHI=";

    /**
     * Creates a new default Athena instance without XMPP.
     *
     * @return the athena instance
     */
    static Athena athenaWithoutXMPP(String username, String password) {
        return new Builder(username, password).killOtherSessions().handleShutdown().refreshAutomatically().build();
    }

    /**
     * Creates a new default Athena instance with XMPP.
     *
     * @return the athena instance
     */
    static Athena athenaWithXMPP(String username, String password) {
        return new Builder(username, password).killOtherSessions().handleShutdown().refreshAutomatically().enableXmpp().platform(Platform.WIN).app("Fortnite").build();
    }

    /**
     * Global GSON instance.
     */
    Gson GSON = new Gson();

    /**
     * Adds a new {@link InterceptorAction} that will be called upon when a new request is being executed.
     *
     * @param action the action
     */
    void addInterceptorAction(InterceptorAction action);

    /**
     * Removes a {@link InterceptorAction} from the list.
     *
     * @param action the action
     */
    void removeInterceptorAction(InterceptorAction action);

    /**
     * @return internal GSON instance with all registered type adapters.
     */
    Gson gson();

    /**
     * @return the accounts class that handles account related requests.
     */
    Accounts account();

    /**
     * @return the {@link AccountPublicService} instance
     */
    AccountPublicService accountPublicService();

    /**
     * @return the friends class that handles friend related requests.
     */
    Friends friend();

    /**
     * @return the {@link FriendsPublicService} instance
     */
    FriendsPublicService friendsPublicService();

    /**
     * @return the statistics v2 class that handles stats.
     */
    StatisticsV2 statisticsV2();

    /**
     * @return the {@link StatsproxyPublicService} instance
     */
    StatsproxyPublicService statsproxyPublicService();

    /**
     * @return the events class that handles events/tournaments.
     */
    Events events();

    /**
     * @return the {@link EventsPublicService} instance.
     */
    EventsPublicService eventsPublicService();

    /**
     * @return the {@link EulatrackingPublicService} instance
     */
    EulatrackingPublicService eulatrackingPublicService();

    /**
     * @return the {@link FortnitePublicService} instance.
     */
    FortnitePublicService fortnitePublicService();

    /**
     * @return the fortnite class that handles fortnite related services.
     */
    Fortnite fortnite();

    /**
     * @return the {@link PresencePublicService} instance.
     */
    PresencePublicService presencePublicService();

    /**
     * @return the presences class that handles presence.
     */
    Presences presence();

    /**
     * @return the FriendChat class that handles chat.
     */
    FriendChat chat();

    /**
     * @return the {@link PartyService} instance.
     */
    PartyService partyService();

    /**
     * @return the parties class that handles parties.
     */
    Parties party();

    /**
     * @return the {@link ChannelsPublicService} instance.
     */
    ChannelsPublicService channelsPublicService();

    /**
     * @return the {@link GroupsService} instance.
     */
    GroupsService groupsService();

    /**
     * @return the {@link AuthenticationService} instance.
     */
    AuthenticationService authenticationService();

    /**
     * @return the {@link XMPPConnectionManager} instance.
     */
    XMPPConnectionManager xmpp();

    /**
     * @return the {@link XMPPTCPConnection} instance inside {@link XMPPConnectionManager}
     */
    XMPPTCPConnection connection();

    /**
     * @return the HTTP client used within Athena.
     */
    OkHttpClient httpClient();

    /**
     * @return the account ID of this athena instance.
     */
    String accountId();

    /**
     * @return the display name of this athena instance.
     */
    String displayName();

    /**
     * @return the session of this athena instance.
     */
    Session session();

    /**
     * @return the platform of this athena instance.
     */
    Platform platform();

    /**
     * @return {@code true} if XMPP is enabled.
     */
    boolean xmppEnabled();

    /**
     * Close this instance of Athena.
     */
    void close();

    /**
     * Represents a builder, used for building instances of {@link Athena}
     */
    final class Builder implements Cloneable {

        private String authorizationToken = EPIC_GAMES_LAUNCHER_TOKEN;

        /**
         * Email address, password, and if 2FA is enabled the 2FA code.
         * {@code accountId} the account ID for device_auth
         * {@code deviceId} the device ID for device_auth
         * {@code secret} the secret for device_auth
         */
        private String email, password, code, accountId, deviceId, secret;
        /**
         * {@code rememberDevice} if user/device should be remembered.
         * {@code killOtherSessions} allows you to kill other tokens that are in use.
         * {@code acceptEula} will accept the eula if needed.
         * {@code handleShutdown} will create a hook that will automatically close athena on shutdown.
         * {@code refreshAutomatically} handles refreshing the access token automatically.
         * {@code kairos} will grant a kairos token if true.
         */
        private boolean rememberMe, killOtherSessions, acceptEula, handleShutdown, refreshAutomatically, kairos;

        /**
         * {@code enableXmpp} if true XMPP will be enabled and used.
         * {@code loadRoster} if true the XMPP roster will be loaded, not recommended for large friend accounts.
         * {@code reconnectOnError} if true the XMPP connection will be re-established in the event its closed.
         * {@code debugXmpp} if true xmpp traffic will be logged.
         */
        private boolean enableXmpp, loadRoster, reconnectOnError, debugXmpp;

        /**
         * Platform and app types.
         * Platform.WIN
         * "Fortnite"
         */
        private Platform platform;
        private String appType;

        /**
         * The grant type to use.
         * Default: EXCHANGE_CODE
         */
        private GrantType grantType = GrantType.EXCHANGE_CODE;

        /**
         * Used for creating new device auths.
         */
        private Device deviceAuth;

        public Builder(String email, String password, String code) {
            this.email = email;
            this.password = password;
            this.code = code;
        }

        public Builder(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public Builder() {
        }

        public Builder token(String authorizationToken) {
            this.authorizationToken = authorizationToken;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder createDeviceAuth(Device device) {
            this.deviceAuth = device;
            return this;
        }

        public Builder useDeviceAuth(String accountId, String deviceId, String secret) {
            this.accountId = accountId;
            this.deviceId = deviceId;
            this.secret = secret;
            this.grantType = GrantType.DEVICE_AUTH;
            return this;
        }

        public Builder rememberMe() {
            rememberMe = true;
            return this;
        }

        public Builder killOtherSessions() {
            killOtherSessions = true;
            return this;
        }

        public Builder acceptEula() {
            acceptEula = true;
            return this;
        }

        public Builder handleShutdown() {
            handleShutdown = true;
            return this;
        }

        public Builder refreshAutomatically() {
            refreshAutomatically = true;
            return this;
        }

        public Builder kairos() {
            kairos = true;
            grantType = GrantType.AUTHORIZATION_CODE;
            return this;
        }

        public Builder enableXmpp() {
            enableXmpp = true;
            return this;
        }

        public Builder loadRoster() {
            loadRoster = true;
            return this;
        }

        public Builder reconnectOnError() {
            reconnectOnError = true;
            return this;
        }

        public Builder enableDebugXmpp() {
            debugXmpp = true;
            return this;
        }

        public Builder platform(Platform platform) {
            this.platform = platform;
            return this;
        }

        public Builder app(String app) {
            this.appType = app;
            return this;
        }

        public Builder grantType(GrantType grantType) {
            this.grantType = grantType;
            return this;
        }

        String authorizationToken() {
            return authorizationToken;
        }

        String email() {
            return email;
        }

        String password() {
            return password;
        }

        String code() {
            return code;
        }

        String accountId() {
            return accountId;
        }

        String deviceId() {
            return deviceId;
        }

        String secret() {
            return secret;
        }

        boolean shouldRememberMe() {
            return rememberMe;
        }

        boolean shouldKillOtherSessions() {
            return killOtherSessions;
        }

        boolean shouldAcceptEula() {
            return acceptEula;
        }

        boolean shouldHandleShutdown() {
            return handleShutdown;
        }

        boolean shouldRefreshAutomatically() {
            return refreshAutomatically;
        }

        boolean shouldEnableXmpp() {
            return enableXmpp;
        }

        boolean shouldLoadRoster() {
            return loadRoster;
        }

        boolean shouldReconnectOnError() {
            return reconnectOnError;
        }

        boolean debugXmpp() {
            return debugXmpp;
        }

        boolean authenticateKairos() {
            return kairos;
        }

        Platform platform() {
            return platform;
        }

        String appType() {
            return appType;
        }

        GrantType grantType() {
            return grantType;
        }

        Device device() {
            return deviceAuth;
        }

        /**
         * Builds this instance into a new {@link Athena}
         *
         * @return a new {@link Athena} instance
         * @throws UnsupportedBuildException if there are fields like username and password left empty.
         * @throws EpicGamesErrorException   if there was an authentication exception
         */
        public Athena build() throws UnsupportedBuildException, EpicGamesErrorException {
            if (email == null || email.isEmpty()) throw new UnsupportedBuildException("Athena needs an email address to login.");
            if (password == null || password.isEmpty()) throw new UnsupportedBuildException("Athena needs a password to login.");
            if (enableXmpp && (platform == null || appType == null)) throw new UnsupportedBuildException("Platform and app must be set for XMPP.");
            if (kairos && authorizationToken.equals(EPIC_GAMES_LAUNCHER_TOKEN)) authorizationToken = KAIROS_TOKEN;
            return new AthenaImpl(this);
        }

        /**
         * TODO
         *
         * @return
         * @throws CloneNotSupportedException
         */
        @Override
        protected Builder clone() throws CloneNotSupportedException {
            final var instance = (Builder) super.clone();
            return instance;
        }
    }


}
