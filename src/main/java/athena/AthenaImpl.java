package athena;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.account.resource.external.ExternalAuth;
import athena.account.service.AccountPublicService;
import athena.authentication.FortniteAuthenticationManager;
import athena.authentication.session.Session;
import athena.context.AthenaContext;
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
import athena.friend.resource.types.FriendDirection;
import athena.friend.resource.types.FriendStatus;
import athena.friend.service.FriendsPublicService;
import athena.friend.xmpp.notification.type.FNotificationType;
import athena.interceptor.InterceptorAction;
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
import athena.util.json.BasicJsonDeserializer;
import athena.util.json.BasicPostProcessor;
import athena.xmpp.XMPPConnectionManager;
import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.gsonfire.GsonFireBuilder;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     * Scheduled executor service for refreshes.
     */
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * The reference for the session.
     */
    private final AtomicReference<Session> session = new AtomicReference<>();

    /**
     * A list of interceptor actions.
     */
    private final CopyOnWriteArrayList<InterceptorAction> interceptorActions = new CopyOnWriteArrayList<>();
    /**
     * Manages account actions like: Finding them by ID and display name.
     */
    private final Accounts accounts;
    /**
     * Manages friend actions like: Sending friend requests, accepting, deleting, blocking, etc.
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
     * Retrofit services
     */
    private final AccountPublicService accountPublicService;
    private final FriendsPublicService friendsPublicService;
    private final StatsproxyPublicService statsproxyPublicService;
    private final EulatrackingPublicService eulatrackingPublicService;
    private final EventsPublicService eventsPublicService;
    private final FortnitePublicService fortnitePublicService;
    private final PresencePublicService presencePublicService;

    /**
     * GSON instance.
     */
    private final Gson gson;

    /**
     * This context.
     */
    private final AthenaContext context;

    /**
     * This account.
     */
    private Account account;

    /**
     * XMPP connection manager
     */
    private XMPPConnectionManager connectionManager;

    AthenaImpl(Builder builder) throws FortniteAuthenticationException {
        // Create a new cookie manager for the cookie jar.
        final var manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        // build the client.
        client = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(manager)).addInterceptor(this).build();

        // initialize our gson instance
        gson = initializeGson();
        fortniteAuthenticationManager = new FortniteAuthenticationManager(builder.email(), builder.password(), builder.code(), builder.epicGamesLauncherToken(), builder.shouldRememberDevice(), client, gson);
        // authenticate!
        final var session = fortniteAuthenticationManager.authenticate();
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

        // initialize the context for this instance.
        context = new AthenaContext();
        context.accountId(session.accountId());
        context.accountPublicService(accountPublicService);
        context.friendsPublicService(friendsPublicService);
        context.statsproxyPublicService(statsproxyPublicService);
        context.eventsPublicService(eventsPublicService);
        context.fortnitePublicService(fortnitePublicService);
        context.presencePublicService(presencePublicService);
        context.connectionManager(connectionManager);
        context.gson(gson);

        // initialize each wrapper class.
        accounts = new Accounts(context);
        friends = new Friends(context, builder.shouldEnableXmpp());
        context.friends(friends);

        statisticsV2 = new StatisticsV2(context);
        events = new Events(context);
        fortnite = new Fortnite(context);
        presences = new Presences(context, builder.shouldEnableXmpp());

        // find the account that belongs to this instance.
        account = accounts.findByAccountId(session.accountId());
        LOGGER.atInfo().log("Ready!");
    }

    /**
     * Initialize our GSON instance.
     *
     * @return the GSON instance.
     */
    private Gson initializeGson() {
        final var fireGson = new GsonFireBuilder();

        fireGson.enableHooks(UnfilteredStatistic.class);
        fireGson.enableHooks(ExternalAuth.class);
        fireGson.enableHooks(Account.class);
        fireGson.enableHooks(Profile.class);
        fireGson.enableHooks(Friend.class);
        fireGson.enumDefaultValue(FriendStatus.class, FriendStatus.UNKNOWN);
        fireGson.enumDefaultValue(FriendDirection.class, FriendDirection.UNKNOWN);
        fireGson.enumDefaultValue(FNotificationType.class, FNotificationType.UNKNOWN);
        fireGson.registerPostProcessor(Account.class, (BasicPostProcessor<Account>) (result, src, gson) -> result.postProcess(context));
        fireGson.registerPostProcessor(Friend.class, (BasicPostProcessor<Friend>) (result, src, gson) -> result.postProcess(context));
        fireGson.registerPostProcessor(Profile.class, (BasicPostProcessor<Profile>) (result, src, gson) -> result.postProcess(context));
        fireGson.registerPostProcessor(FortnitePresence.class, (BasicPostProcessor<FortnitePresence>) (result, src, gson) -> result.postProcess(context));

        // register our type adapters.
        final var builder = fireGson.createGsonBuilder();
        builder
                .registerTypeAdapter(Instant.class, (BasicJsonDeserializer<Instant>) (json) -> Instant.parse(json.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(Input.class, (BasicJsonDeserializer<Input>) (json) -> Input.typeOf(json.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(Platform.class, (BasicJsonDeserializer<Platform>) (json) -> Platform.typeOf(json.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(Region.class, (BasicJsonDeserializer<Region>) (json) -> Region.valueOf(json.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(new TypeToken<List<Profile>>() {
                }.getType(), (BasicJsonDeserializer<List<Profile>>) (json) -> {
                    // check if its an array for friends summary first
                    // otherwise get the friends array.
                    final var array = json.isJsonArray() ? json.getAsJsonArray() : json.getAsJsonObject().getAsJsonArray("friends");
                    final var list = new ArrayList<Profile>();
                    array.forEach(element -> list.add(gson.fromJson(element, Profile.class)));
                    return list;
                })
                .registerTypeAdapter(LastOnlineResponse.class, (BasicJsonDeserializer<LastOnlineResponse>) (json) -> {
                    final var object = json.getAsJsonObject();
                    final var map = new HashMap<String, Instant>();

                    object.keySet().forEach(key -> {
                        final var array = object.getAsJsonArray(key);
                        final var time = Instant.parse(array.get(0).getAsJsonObject().get("last_online").getAsString());
                        map.put(key, time);
                    });
                    return new LastOnlineResponse(map, context);
                });

        // fixes an issue with superclasses/post processing.
        builder.excludeFieldsWithModifiers(Modifier.PROTECTED);
        return builder.create();
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
            session.set(fortniteAuthenticationManager.retrieveRefreshSession(old.refreshToken()));
            // kill the old token.
            fortniteAuthenticationManager.killToken(old.accessToken());
            if (connectionManager != null) {
                cleanXmppResources();

                connectionManager.disconnect();
                connectionManager.connect(session().accountId(), session().accessToken());
                refreshXmppResources();
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
            session.set(fortniteAuthenticationManager.authenticate());
            // kill the old token.
            fortniteAuthenticationManager.killToken(old.accessToken());
            if (connectionManager != null) {
                cleanXmppResources();
                connectionManager.disconnect();
                connectionManager.connect(session().accountId(), session().accessToken());
                refreshXmppResources();
            }
        } catch (IOException | FortniteAuthenticationException exception) {
            LOGGER.atSevere().withCause(exception).log("Failed to refresh session.");
            close();
        }
    }

    /**
     * Clean each XMPP resource.
     */
    private void cleanXmppResources() {
        friends.clean();
        presences.clean();
    }

    /**
     * Refresh each XMPP resource.
     */
    private void refreshXmppResources() {
        friends.refresh(context);
        presences.refresh(context);
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
    public OkHttpClient httpClient() {
        return client;
    }

    @Override
    public String accountId() {
        return session().accountId();
    }

    @Override
    public String displayName() {
        return account.displayName();
    }

    @Override
    public Session session() {
        return session.get();
    }

    @Override
    public void close() {
        try {
            friends.dispose();
            presences.dispose();

            if (connectionManager != null) connectionManager.close();
            fortniteAuthenticationManager.killToken(session().accessToken());
            client.dispatcher().executorService().shutdownNow();
            client.connectionPool().evictAll();
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
        final var finalRequest = nextRequestChain.newBuilder()
                .addHeader("Authorization", "bearer " + session.get().accessToken())
                .build();
        return chain.proceed(finalRequest);
    }

}
