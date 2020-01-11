package athena;

import athena.account.Accounts;
import athena.account.service.AccountPublicService;
import athena.authentication.session.Session;
import athena.eula.service.EulatrackingPublicService;
import athena.events.Events;
import athena.events.service.EventsPublicService;
import athena.exception.FortniteAuthenticationException;
import athena.exception.UnsupportedBuildException;
import athena.fortnite.Fortnite;
import athena.fortnite.service.FortnitePublicService;
import athena.friend.Friends;
import athena.friend.service.FriendsPublicService;
import athena.interceptor.InterceptorAction;
import athena.presence.service.PresencePublicService;
import athena.stats.StatisticsV2;
import athena.stats.service.StatsproxyPublicService;
import athena.types.Platform;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;

public interface Athena {

    /**
     * General public-static resources like tokens.
     */
    String EPIC_GAMES_LAUNCHER_TOKEN = "MzQ0NmNkNzI2OTRjNGE0NDg1ZDgxYjc3YWRiYjIxNDE6OTIwOWQ0YTVlMjVhNDU3ZmI5YjA3NDg5ZDMxM2I0MWE=";
    String FORTNITE_TOKEN = "ZWM2ODRiOGM2ODdmNDc5ZmFkZWEzY2IyYWQ4M2Y1YzY6ZTFmMzFjMjExZjI4NDEzMTg2MjYyZDM3YTEzZmM4NGQ=";

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
     * Close this instance of Athena.
     */
    void close();

    /**
     * Represents a builder, used for building instances of {@link Athena}
     */
    final class Builder implements Cloneable {

        private String epicGamesLauncherToken = EPIC_GAMES_LAUNCHER_TOKEN;

        /**
         * Email address, password, and if 2FA is enabled the 2FA code.
         */
        private String email, password, code;
        /**
         * {@code rememberDevice} is only applicable if 2FA is being used.
         * {@code killOtherSessions} allows you to kill other tokens that are in use.
         * {@code acceptEula} will accept the eula if needed.
         * {@code handleShutdown} will create a hook that will automatically close athena on shutdown.
         * {@code refreshAutomatically} handles refreshing the access token automatically.
         */
        private boolean rememberDevice, killOtherSessions, acceptEula, handleShutdown, refreshAutomatically;

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

        public Builder token(String epicGamesLauncherToken) {
            this.epicGamesLauncherToken = epicGamesLauncherToken;
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

        public Builder rememberDevice() {
            rememberDevice = true;
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

        String epicGamesLauncherToken() {
            return epicGamesLauncherToken;
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

        boolean shouldRememberDevice() {
            return rememberDevice;
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

        public boolean shouldReconnectOnError() {
            return reconnectOnError;
        }

        public boolean debugXmpp() {
            return debugXmpp;
        }

        public Platform platform() {
            return platform;
        }

        public String appType() {
            return appType;
        }

        /**
         * Builds this instance into a new {@link Athena}
         *
         * @return a new {@link Athena} instance
         * @throws UnsupportedBuildException       if there are fields like username and password left empty.
         * @throws FortniteAuthenticationException if there was an authentication exception
         */
        public Athena build() throws UnsupportedBuildException, FortniteAuthenticationException {
            if (email == null || email.isEmpty()) throw new UnsupportedBuildException("Athena needs an email address to login.");
            if (password == null || password.isEmpty()) throw new UnsupportedBuildException("Athena needs a password to login.");
            if (enableXmpp && (platform == null || appType == null)) throw new UnsupportedBuildException("Platform and app must be set for XMPP.");
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
