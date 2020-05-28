package athena;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.account.service.AccountPublicService;
import athena.authentication.FortniteAuthenticationManager;
import athena.authentication.service.AuthenticationService;
import athena.authentication.session.Session;
import athena.channels.service.ChannelsPublicService;
import athena.chat.FriendChat;
import athena.context.DefaultAthenaContext;
import athena.eula.service.EulatrackingPublicService;
import athena.events.Events;
import athena.events.service.EventsPublicService;
import athena.exception.EpicGamesErrorException;
import athena.fortnite.Fortnite;
import athena.fortnite.service.FortnitePublicService;
import athena.friend.Friends;
import athena.friend.resource.Friend;
import athena.friend.resource.summary.Profile;
import athena.friend.service.FriendsPublicService;
import athena.groups.service.GroupsService;
import athena.interceptor.InterceptorAction;
import athena.party.Parties;
import athena.party.resource.Party;
import athena.party.resource.invite.PartyInvitation;
import athena.party.resource.member.PartyMember;
import athena.party.resource.member.meta.PartyMemberMeta;
import athena.party.resource.member.meta.joinrequest.JoinRequestUsers;
import athena.party.resource.meta.PartyMeta;
import athena.party.resource.ping.PartyPing;
import athena.party.service.PartyService;
import athena.party.xmpp.event.invite.PartyInviteEvent;
import athena.party.xmpp.event.invite.PartyPingEvent;
import athena.party.xmpp.event.member.*;
import athena.party.xmpp.event.party.PartyUpdatedEvent;
import athena.presence.Presences;
import athena.presence.resource.FortnitePresence;
import athena.presence.resource.LastOnlineResponse;
import athena.presence.service.PresencePublicService;
import athena.stats.StatisticsV2;
import athena.stats.resource.UnfilteredStatistic;
import athena.stats.service.StatsproxyPublicService;
import athena.types.Input;
import athena.types.Platform;
import athena.types.Region;
import athena.util.json.converters.*;
import athena.util.json.service.AthenaServiceAdapterFactory;
import athena.util.json.wrapped.FortniteTypeAdapterFactory;
import athena.util.request.Requests;
import athena.xmpp.XMPPConnectionManager;
import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.Jid;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

final class AthenaImpl implements Athena, Interceptor {

    /**
     * The LOGGER.
     */
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();
    /**
     * The HTTP client.
     */
    private final OkHttpClient client;
    /**
     * The authentication manager.
     */
    private final FortniteAuthenticationManager fortniteAuthenticationManager;

    /**
     * The original builder.
     */
    private final Builder builder;

    /**
     * Scheduled executor service for refreshes.
     */
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    /**
     * The reference for the session.
     */
    private final AtomicReference<Session> session = new AtomicReference<>();
    /**
     * A list of interceptor hooks.
     */
    private final CopyOnWriteArrayList<InterceptorAction> interceptorActions = new CopyOnWriteArrayList<>();

    /**
     * Manages account hooks like: Finding them by ID and display name.
     */
    private final Accounts accounts;
    /**
     * Manages friend hooks like: Sending friend requests, accepting, deleting, blocking, etc.
     */
    private final Friends friends;
    /**
     * Manages statistics.
     */
    private final StatisticsV2 statisticsV2;
    /**
     * Manages events/tournaments.
     */
    private final Events events;
    /**
     * Manages fortnite services
     */
    private final Fortnite fortnite;
    /**
     * Manages presence.
     */
    private final Presences presences;
    /**
     * Manages XMPP Chat.
     */
    private final FriendChat chat;
    /**
     * Manages parties.
     */
    private final Parties parties;

    /**
     * Retrofit services
     */
    private final AccountPublicService accountPublicService;
    private final FriendsPublicService friendsPublicService;
    private final StatsproxyPublicService statsproxyPublicService;
    private final EulatrackingPublicService eulatrackingPublicService;
    private final EventsPublicService eventsPublicService;
    private final FortnitePublicService fortnitePublicService;
    private final PresencePublicService presencePublicService;
    private final ChannelsPublicService channelsPublicService;
    private final GroupsService groupsService;
    private final PartyService partyService;
    private final AuthenticationService authenticationService;

    /**
     * GSON instance.
     */
    private final Gson gson;
    /**
     * The platform
     */
    private final Platform platform;
    /**
     * This account.
     */
    private Account account;
    /**
     * XMPP connection manager
     */
    private XMPPConnectionManager connectionManager;

    AthenaImpl(Builder builder) throws EpicGamesErrorException {
        this.builder = builder;
        this.platform = builder.platform();

        // Create a new cookie manager for the cookie jar.
        final var manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        // build the client.
        client = new OkHttpClient.Builder()
                .followRedirects(false)
                .cookieJar(new JavaNetCookieJar(manager))
                .addInterceptor(this).build();

        gson = initializeGson();
        final var factory = GsonConverterFactory.create(gson);
        accountPublicService = initializeRetrofitService(AccountPublicService.BASE_URL, factory, AccountPublicService.class);
        friendsPublicService = initializeRetrofitService(FriendsPublicService.BASE_URL, factory, FriendsPublicService.class);
        statsproxyPublicService = initializeRetrofitService(StatsproxyPublicService.BASE_URL, factory, StatsproxyPublicService.class);
        eulatrackingPublicService = initializeRetrofitService(EulatrackingPublicService.BASE_URL, factory, EulatrackingPublicService.class);
        eventsPublicService = initializeRetrofitService(EventsPublicService.BASE_URL, factory, EventsPublicService.class);
        fortnitePublicService = initializeRetrofitService(FortnitePublicService.BASE_URL, factory, FortnitePublicService.class);
        presencePublicService = initializeRetrofitService(PresencePublicService.BASE_URL, factory, PresencePublicService.class);
        channelsPublicService = initializeRetrofitService(ChannelsPublicService.BASE_URL, factory, ChannelsPublicService.class);
        groupsService = initializeRetrofitService(GroupsService.BASE_URL, factory, GroupsService.class);
        partyService = initializeRetrofitService(PartyService.BASE_URL, factory, PartyService.class);
        authenticationService = initializeRetrofitService(AuthenticationService.BASE_URL, factory, AuthenticationService.class);

        // create our authentication manager.
        fortniteAuthenticationManager = new FortniteAuthenticationManager(
                builder.email(),
                builder.password(),
                builder.code(),
                builder.accountId(),
                builder.deviceId(),
                builder.secret(),
                builder.authorizationToken(),
                builder.shouldRememberMe(),
                builder.authenticateKairos(),
                builder.grantType(),
                builder.device(),
                authenticationService,
                accountPublicService,
                eulatrackingPublicService,
                fortnitePublicService,
                client,
                gson);

        // authenticate
        final var session = fortniteAuthenticationManager.authenticate();
        this.session.set(session); // set the session
        // schedule the refresh and handle the shutdown hook.
        if (builder.shouldHandleShutdown()) Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        if (builder.shouldRefreshAutomatically()) scheduleRefresh();
        // kill other sessions and accept the EULA.
        if (builder.shouldKillOtherSessions()) fortniteAuthenticationManager.killOtherSessions();
        if (builder.shouldAcceptEula()) fortniteAuthenticationManager.acceptEulaIfNeeded(session.accountId());

        // handle connecting the XMPP service.
        if (builder.shouldEnableXmpp()) {
            connectionManager = new XMPPConnectionManager(builder.shouldLoadRoster(), builder.shouldReconnectOnError(), builder.debugXmpp(), builder.platform(), builder.appType());
            connectionManager.connect(session.accountId(), session.accessToken());
        }

        // initialize our context
        final var context = new DefaultAthenaContext();
        context.initializeServicesOnly(this);
        context.initializeOtherOnly(this);

        // initialize our resources.
        accounts = new Accounts(context);

        // find the account that belongs to this instance.
        account = accounts.findByAccountId(session.accountId());
        context.initializeAccountOnly(this);

        // initialize other resources
        friends = new Friends(context);
        statisticsV2 = new StatisticsV2(context);
        events = new Events(context);
        fortnite = new Fortnite(context);
        presences = new Presences(context);
        chat = builder.shouldEnableXmpp() ? new FriendChat(context) : null;
        parties = builder.shouldEnableXmpp() ? new Parties(context) : null;
        // finally initialize resources inside the context.
        context.initializeResourcesOnly(this);
        LOGGER.atInfo().log("Account " + session.accountId() + " successfully authenticated.");
    }

    /**
     * Initialize our GSON instance.
     *
     * @return the GSON instance.
     */
    private Gson initializeGson() {
        final var gsonBuilder = new GsonBuilder();
        // hooks only
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withHooksOnly(UnfilteredStatistic.class, this));
        // context only
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(FortnitePresence.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(LastOnlineResponse.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyInvitation.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyPing.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyPingEvent.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyInviteEvent.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyMemberJoinedEvent.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyMemberKickedEvent.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyMemberLeftEvent.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyMemberNewCaptainEvent.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyMemberUpdatedEvent.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyMember.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyMemberDisconnectedEvent.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyUpdatedEvent.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.withContextOnly(PartyMemberRequireConfirmationEvent.class, this));
        // both
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.of(Account.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.of(Profile.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.of(Friend.class, this));
        gsonBuilder.registerTypeAdapterFactory(AthenaServiceAdapterFactory.of(Party.class, this));
        // other
        gsonBuilder.registerTypeAdapterFactory(FortniteTypeAdapterFactory.of(JoinRequestUsers.class));
        gsonBuilder.registerTypeAdapterFactory(FortniteTypeAdapterFactory.of(PartyMeta.class));
        gsonBuilder.registerTypeAdapterFactory(FortniteTypeAdapterFactory.of(PartyMemberMeta.class));

        // converters.
        gsonBuilder.registerTypeAdapter(Input.class, new InputConverter());
        gsonBuilder.registerTypeAdapter(Instant.class, new InstantConverter());
        gsonBuilder.registerTypeAdapter(Platform.class, new PlatformConverter());
        gsonBuilder.registerTypeAdapter(Region.class, new RegionConverter());
        gsonBuilder.registerTypeAdapter(Jid.class, new JidConverter());
        gsonBuilder.registerTypeAdapter(LastOnlineResponse.class, new LastOnlineResponseConverter());

        // ignore protected, static and transient fields.
        gsonBuilder.excludeFieldsWithModifiers(Modifier.PROTECTED, Modifier.STATIC, Modifier.TRANSIENT);
        return gsonBuilder.create();
    }

    /**
     * Initialize a single retrofit service.
     *
     * @param baseUrl the base URL.
     * @param factory the converter factory.
     * @param type    the type
     * @param <T>     TYPE.
     * @return the new service
     */
    private <T> T initializeRetrofitService(String baseUrl, GsonConverterFactory factory, Class<T> type) {
        return new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(factory).client(client).build().create(type);
    }

    /**
     * Schedule the refresh/authenticate process.
     */
    private void scheduleRefresh() {
        final var refreshWhen = Instant.now().plusSeconds(200).until(session.get().accessTokenExpiresAt(), ChronoUnit.SECONDS);
        scheduledExecutorService.schedule(this::refresh, refreshWhen, TimeUnit.SECONDS);
    }

    /**
     * Grant the refresh session and replace/kill the old session.
     */
    private void refresh() {
        try {
            final var old = session();

            // retrieve the refresh session.
            final var newSession = Requests.executeCall(accountPublicService.grantSession(
                    "basic " + builder.authorizationToken(),
                    "refresh_token",
                    Map.of("refresh_token", old.refreshToken())));
            session.set(newSession);

            // schedule our next refresh
            scheduleRefresh();

            // refresh our XMPP connection and resources
            if (xmppEnabled()) {
                connectionManager.reconnect(session().accountId(), session().accessToken());
            }

            LOGGER.atInfo().log("Successfully re-authenticated.");
        } catch (EpicGamesErrorException exception) {
            LOGGER.atSevere().withCause(exception).log("Failed to refresh session.");
            close();
        }
    }

    @Override
    public void addInterceptorAction(InterceptorAction action) {
        interceptorActions.add(action);
    }

    @Override
    public void removeInterceptorAction(InterceptorAction action) {
        interceptorActions.remove(action);
    }

    @Override
    public Gson gson() {
        return gson;
    }

    @Override
    public Accounts account() {
        return accounts;
    }

    @Override
    public AccountPublicService accountPublicService() {
        return accountPublicService;
    }

    @Override
    public Friends friend() {
        return friends;
    }

    @Override
    public FriendsPublicService friendsPublicService() {
        return friendsPublicService;
    }

    @Override
    public StatisticsV2 statisticsV2() {
        return statisticsV2;
    }

    @Override
    public StatsproxyPublicService statsproxyPublicService() {
        return statsproxyPublicService;
    }

    @Override
    public Events events() {
        return events;
    }

    @Override
    public EventsPublicService eventsPublicService() {
        return eventsPublicService;
    }

    @Override
    public EulatrackingPublicService eulatrackingPublicService() {
        return eulatrackingPublicService;
    }

    @Override
    public FortnitePublicService fortnitePublicService() {
        return fortnitePublicService;
    }

    @Override
    public Fortnite fortnite() {
        return fortnite;
    }

    @Override
    public PresencePublicService presencePublicService() {
        return presencePublicService;
    }

    @Override
    public Presences presence() {
        return presences;
    }

    @Override
    public FriendChat chat() {
        return chat;
    }

    @Override
    public ChannelsPublicService channelsPublicService() {
        return channelsPublicService;
    }

    @Override
    public GroupsService groupsService() {
        return groupsService;
    }

    @Override
    public AuthenticationService authenticationService() {
        return authenticationService;
    }

    @Override
    public PartyService partyService() {
        return partyService;
    }

    @Override
    public Parties party() {
        return parties;
    }

    @Override
    public XMPPConnectionManager xmpp() {
        return connectionManager;
    }

    @Override
    public XMPPTCPConnection connection() {
        return connectionManager.connection();
    }

    @Override
    public OkHttpClient httpClient() {
        return client;
    }

    @Override
    public String accountId() {
        return session().accountId();
    }

    @Override
    public String displayName() {
        if (account == null) return "";
        return account.displayName();
    }

    @Override
    public Session session() {
        return session.get();
    }

    @Override
    public Platform platform() {
        return platform;
    }

    @Override
    public boolean xmppEnabled() {
        return builder.shouldEnableXmpp();
    }

    @Override
    public void close() {
        chat.close();
        parties.close();
        friends.close();
        presences.close();

        // close the XMPP connection
        if (connectionManager != null) connectionManager.close();
        // kill our token
        fortniteAuthenticationManager.killToken(session().accessToken());
        // shutdown OkHttp
        client.dispatcher().executorService().shutdownNow();
        client.connectionPool().evictAll();
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        final var request = chain.request();

        // Go through all interceptor chains.
        // Each action will take the request from the last action.
        var nextRequestChain = request;
        for (InterceptorAction action : interceptorActions) nextRequestChain = action.run(nextRequestChain);

        // we lost the original request!
        if (nextRequestChain == null) nextRequestChain = request;
        // we already have an authorization header or we don't have a session yet, continue.
        if (nextRequestChain.headers().names().contains("Authorization") || session.get() == null)
            return chain.proceed(request);

        // add in the authorization header for the final request.
        // Credits: armisto for user-agent, X-Epic-Correlation-ID info
        // TODO: diff random uuid gen for fn requests
        final var finalRequest = nextRequestChain.newBuilder()
                .addHeader("Authorization", "bearer " + session.get().accessToken())
                .addHeader("User-Agent", "Fortnite/++Fortnite+Release-12.61-CL-13498980 IOS/13.4.1")
                .addHeader("X-Epic-Correlation-ID", UUID.randomUUID().toString())
                .build();
        return chain.proceed(finalRequest);
    }

}
