package athena;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.account.resource.external.ExternalAuth;
import athena.account.service.AccountPublicService;
import athena.authentication.FortniteAuthenticationManager;
import athena.authentication.session.Session;
import athena.authentication.type.GrantType;
import athena.channels.service.ChannelsPublicService;
import athena.chat.XMPPChat;
import athena.context.DefaultAthenaContext;
import athena.eula.service.EulatrackingPublicService;
import athena.events.Events;
import athena.events.service.EventsPublicService;
import athena.exception.EpicGamesErrorException;
import athena.exception.FortniteAuthenticationException;
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
import athena.party.resource.connection.Connection;
import athena.party.resource.invite.PartyInvitation;
import athena.party.resource.member.meta.PartyMemberMeta;
import athena.party.resource.meta.PartyMeta;
import athena.party.resource.ping.PartyPing;
import athena.party.service.PartyService;
import athena.party.xmpp.event.invite.PartyInviteEvent;
import athena.party.xmpp.event.invite.PartyPingEvent;
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
import athena.util.cleanup.AfterRefresh;
import athena.util.cleanup.BeforeRefresh;
import athena.util.cleanup.Shutdown;
import athena.util.event.EventFactory;
import athena.util.json.context.AthenaContextAdapterFactory;
import athena.util.json.converters.InputConverter;
import athena.util.json.converters.InstantConverter;
import athena.util.json.converters.LastOnlineResponseConverter;
import athena.util.json.hooks.HooksAdapterFactory;
import athena.util.json.wrapped.WrappedTypeAdapterFactory;
import athena.xmpp.XMPPConnectionManager;
import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
     * The event factory.
     */
    private final EventFactory eventFactory = EventFactory.createAnnotatedFactory(BeforeRefresh.class, AfterRefresh.class, Shutdown.class);

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
    private final XMPPChat chat;
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

    /**
     * GSON instance.
     */
    private final Gson gson;
    /**
     * This context.
     */
    private final DefaultAthenaContext context;
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

    AthenaImpl(Builder builder) throws FortniteAuthenticationException {
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

        // initialize our gson instance
        gson = initializeGson();
        fortniteAuthenticationManager = new FortniteAuthenticationManager(
                builder.email(),
                builder.password(),
                builder.code(),
                builder.accountId(),
                builder.deviceId(),
                builder.secret(),
                builder.authorizationToken(),
                builder.shouldRememberMe(),
                builder.grantType(),
                client,
                gson);

        // authenticate!
        final var session = builder.authenticateKairos() ? fortniteAuthenticationManager.authenticateKairos() : fortniteAuthenticationManager.authenticate();
        this.session.set(session); // set the session

        LOGGER.atInfo().log("Account " + session.accountId() + " successfully authenticated.");
        // schedule the refresh and handle the shutdown hook.
        if (builder.shouldHandleShutdown()) Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        if (builder.shouldRefreshAutomatically()) scheduleRefresh();

        try {
            // kill other sessions and accept the EULA.
            if (builder.shouldKillOtherSessions()) fortniteAuthenticationManager.killOtherSessions();
            if (builder.shouldAcceptEula()) fortniteAuthenticationManager.acceptEulaIfNeeded(session.accountId());
            LOGGER.atInfo().log("Finished post-authentication requests.");
        } catch (final IOException exception) {
            LOGGER.atWarning().withCause(exception).log("Failed to make additional authentication requests.");
        }

        // handle connecting the XMPP service.
        if (builder.shouldEnableXmpp()) {
            LOGGER.atInfo().log("Connecting to the Fortnite XMPP service.");
            connectionManager = new XMPPConnectionManager(builder.shouldLoadRoster(), builder.shouldReconnectOnError(), builder.debugXmpp(), builder.platform(), builder.appType());
            connectionManager.connect(session.accountId(), session.accessToken());
        }

        // initialize each retrofit service.
        LOGGER.atInfo().log("Initializing Retrofit services.");
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

        // initialize our context
        context = new DefaultAthenaContext();
        context.initializeServicesOnly(this);
        context.initializeOtherOnly(this);

        // initialize our resources.
        accounts = new Accounts(context);

        // find the account that belongs to this instance.
        account = accounts.findByAccountId(session.accountId());
        context.initializeAccountOnly(this);

        // initialize other resources
        friends = new Friends(context, builder.shouldEnableXmpp());
        statisticsV2 = new StatisticsV2(context);
        events = new Events(context);
        fortnite = new Fortnite(context);
        presences = new Presences(context, builder.shouldEnableXmpp());

        // Initialize XMPP dependant resources.
        if (builder.shouldEnableXmpp()) {
            chat = new XMPPChat(context);
            parties = new Parties(context);
            // register their events
            eventFactory.registerEventListener(chat);
            eventFactory.registerEventListener(parties);

            // register XMPP resources with the event factory.
            eventFactory.registerEventListener(friends);
            eventFactory.registerEventListener(presences);
        } else {
            // we dont have XMPP so just set them to null.
            chat = null;
            parties = null;
        }

        // finally initialize resources inside the context.
        context.initializeResourcesOnly(this);

        LOGGER.atInfo().log("Ready!");
    }

    /**
     * Initialize our GSON instance.
     *
     * @return the GSON instance.
     */
    private Gson initializeGson() {
        final var gsonBuilder = new GsonBuilder();

        // TODO: Create separate type adapter factory that combines both hooks/context?
        // Deserialize and context adapters.
        gsonBuilder.registerTypeAdapterFactory(new HooksAdapterFactory(UnfilteredStatistic.class));
        gsonBuilder.registerTypeAdapterFactory(new HooksAdapterFactory(ExternalAuth.class));
        gsonBuilder.registerTypeAdapterFactory(new HooksAdapterFactory(Account.class));
        gsonBuilder.registerTypeAdapterFactory(new HooksAdapterFactory(Profile.class));
        gsonBuilder.registerTypeAdapterFactory(new HooksAdapterFactory(Friend.class));
        gsonBuilder.registerTypeAdapterFactory(new HooksAdapterFactory(Connection.class));
        gsonBuilder.registerTypeAdapterFactory(new HooksAdapterFactory(Party.class));
        gsonBuilder.registerTypeAdapterFactory(new HooksAdapterFactory(PartyMemberMeta.class));

        gsonBuilder.registerTypeAdapterFactory(new AthenaContextAdapterFactory(Account.class, this));
        gsonBuilder.registerTypeAdapterFactory(new AthenaContextAdapterFactory(Profile.class, this));
        gsonBuilder.registerTypeAdapterFactory(new AthenaContextAdapterFactory(Friend.class, this));
        gsonBuilder.registerTypeAdapterFactory(new AthenaContextAdapterFactory(FortnitePresence.class, this));
        gsonBuilder.registerTypeAdapterFactory(new AthenaContextAdapterFactory(LastOnlineResponse.class, this));
        gsonBuilder.registerTypeAdapterFactory(new AthenaContextAdapterFactory(Party.class, this));
        gsonBuilder.registerTypeAdapterFactory(new AthenaContextAdapterFactory(PartyInvitation.class, this));
        gsonBuilder.registerTypeAdapterFactory(new AthenaContextAdapterFactory(PartyPing.class, this));
        gsonBuilder.registerTypeAdapterFactory(new AthenaContextAdapterFactory(PartyPingEvent.class, this));
        gsonBuilder.registerTypeAdapterFactory(new AthenaContextAdapterFactory(PartyInviteEvent.class, this));
        gsonBuilder.registerTypeAdapterFactory(new WrappedTypeAdapterFactory(PartyMeta.class));
        gsonBuilder.registerTypeAdapterFactory(new WrappedTypeAdapterFactory(PartyMemberMeta.class));

        // converters.
        gsonBuilder.registerTypeAdapter(Input.class, new InputConverter());
        gsonBuilder.registerTypeAdapter(LastOnlineResponse.class, new LastOnlineResponseConverter());
        gsonBuilder.registerTypeAdapter(Instant.class, new InstantConverter());

        // constants type adapters.
        // TODO: More clean way to do this in the future.
        gsonBuilder.registerTypeAdapter(Platform.class, (JsonDeserializer<Platform>) (json, typeOfT, context) -> Platform.typeOf(json.getAsJsonPrimitive().getAsString()));
        gsonBuilder.registerTypeAdapter(Region.class, (JsonDeserializer<Region>) (json, typeOfT, context) -> Region.valueOf(json.getAsJsonPrimitive().getAsString()));

        // ignore super classes basically.
        gsonBuilder.excludeFieldsWithModifiers(Modifier.PROTECTED);
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
        final var refreshWhen = Instant.now().plusSeconds(300).until(session.get().accessTokenExpiresAt(), ChronoUnit.SECONDS);
        final var authenticateWhen = Instant.now().plusSeconds(300).until(session.get().refreshTokenExpiresAt(), ChronoUnit.SECONDS);
        scheduledExecutorService.schedule(this::refresh, refreshWhen, TimeUnit.SECONDS);
        scheduledExecutorService.schedule(this::authenticateNew, authenticateWhen, TimeUnit.SECONDS);
    }

    /**
     * Grant the refresh session and replace/kill the old session.
     */
    private void refresh() {
        try {
            final var old = session();
            // retrieve the refresh session.
            session.set(fortniteAuthenticationManager.retrieveSession(null, null, old.refreshToken(), GrantType.REFRESH_TOKEN));
            // kill the old token.
            fortniteAuthenticationManager.killToken(old.accessToken());
            if (connectionManager != null) {
                beforeRefresh();

                connectionManager.disconnect();
                connectionManager.connect(session().accountId(), session().accessToken());
                afterRefresh();
            }
        } catch (IOException | EpicGamesErrorException exception) {
            LOGGER.atSevere().withCause(exception).log("Failed to refresh session.");
            close();
        }
    }

    /**
     * Establish a new session, will not work with 2FA enabled of-course.
     */
    private void authenticateNew() {
        try {
            final var old = session();
            // retrieve the refresh session.
            session.set(builder.authenticateKairos() ? fortniteAuthenticationManager.authenticateKairos() : fortniteAuthenticationManager.authenticate());
            // kill the old token.
            fortniteAuthenticationManager.killToken(old.accessToken());
            if (connectionManager != null) {
                beforeRefresh();
                connectionManager.disconnect();
                connectionManager.connect(session().accountId(), session().accessToken());
                afterRefresh();
            }
        } catch (IOException | FortniteAuthenticationException exception) {
            LOGGER.atSevere().withCause(exception).log("Failed to refresh session.");
            close();
        }
    }

    /**
     * Invoked before refreshing.
     */
    private void beforeRefresh() {
        eventFactory.invoke(BeforeRefresh.class);
    }

    /**
     * Invoked after refreshing
     */
    private void afterRefresh() {
        eventFactory.invoke(AfterRefresh.class, context);
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
    public XMPPChat chat() {
        if (!builder.shouldEnableXmpp()) throw new UnsupportedOperationException("XMPP is not enabled.");
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
        try {
            eventFactory.invoke(Shutdown.class);

            if (connectionManager != null) connectionManager.close();
            fortniteAuthenticationManager.killToken(session().accessToken());
            client.dispatcher().executorService().shutdownNow();
            client.connectionPool().evictAll();
            eventFactory.dispose();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
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
                .addHeader("User-Agent", "Fortnite/++Fortnite+Release-11.40-CL-11039906 {0}")
                .addHeader("X-Epic-Correlation-ID", UUID.randomUUID().toString())
                .build();
        return chain.proceed(finalRequest);
    }

}
