package athena;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.account.service.AccountPublicService;
import athena.adapter.ObjectJsonAdapter;
import athena.authentication.session.Session;
import athena.eula.EulatrackingPublicService;
import athena.events.Events;
import athena.events.EventsPublicService;
import athena.exception.FortniteAuthenticationException;
import athena.exception.UnsupportedBuildException;
import athena.friend.Friends;
import athena.friend.resource.Friend;
import athena.friend.service.FriendsPublicService;
import athena.interceptor.InterceptorAction;
import athena.stats.StatisticsV2;
import athena.stats.service.StatsproxyPublicService;
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
     * @return internal {@link ObjectJsonAdapter<Account>} adapter.
     */
    ObjectJsonAdapter<Account> accountAdapter();

    /**
     * @return internal {@link ObjectJsonAdapter<Friend>} adapter.
     */
    ObjectJsonAdapter<Friend> friendAdapter();

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
         * Remember device is only applicable if 2FA is being used.
         * Killing other sessions allows you to kill other tokens that are in use.
         * Accept EULA will accept the eula if needed.
         * Handle shutdown will create a hook that will automatically close athena on shutdown.
         */
        private boolean rememberDevice, killOtherSessions, acceptEula, handleShutdown;

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

        public Builder setEpicGamesLauncherToken(String epicGamesLauncherToken) {
            this.epicGamesLauncherToken = epicGamesLauncherToken;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public Builder setRememberDevice(boolean rememberDevice) {
            this.rememberDevice = rememberDevice;
            return this;
        }

        public Builder setKillOtherSessions(boolean killOtherSessions) {
            this.killOtherSessions = killOtherSessions;
            return this;
        }

        public Builder setAcceptEula(boolean acceptEula) {
            this.acceptEula = acceptEula;
            return this;
        }

        public Builder setHandleShutdown(boolean handleShutdown) {
            this.handleShutdown = handleShutdown;
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

        boolean rememberDevice() {
            return rememberDevice;
        }

        boolean killOtherSessions() {
            return killOtherSessions;
        }

        boolean acceptEula() {
            return acceptEula;
        }

        boolean handleShutdown() {
            return handleShutdown;
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
