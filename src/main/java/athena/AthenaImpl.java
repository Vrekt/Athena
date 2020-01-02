package athena;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.account.resource.external.ExternalAuth;
import athena.account.service.AccountPublicService;
import athena.authentication.FortniteAuthenticationManager;
import athena.authentication.session.Session;
import athena.eula.EulatrackingPublicService;
import athena.events.Events;
import athena.events.EventsPublicService;
import athena.exception.FortniteAuthenticationException;
import athena.fortnite.FortnitePublicService;
import athena.friend.Friends;
import athena.friend.resource.Friend;
import athena.friend.resource.types.FriendDirection;
import athena.friend.resource.types.FriendStatus;
import athena.friend.service.FriendsPublicService;
import athena.interceptor.InterceptorAction;
import athena.shop.Shop;
import athena.stats.StatisticsV2;
import athena.stats.resource.UnfilteredStatistic;
import athena.stats.service.StatsproxyPublicService;
import athena.types.Input;
import athena.types.Platform;
import athena.types.Region;
import athena.util.json.BasicJsonDeserializer;
import athena.util.json.BasicPostProcessor;
import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import io.gsonfire.GsonFireBuilder;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.time.Instant;
import java.util.concurrent.CopyOnWriteArrayList;
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
     * Manages the shop.
     */
    private final Shop shop;

    /**
     * Retrofit services
     */
    private final AccountPublicService accountPublicService;
    private final FriendsPublicService friendsPublicService;
    private final StatsproxyPublicService statsproxyPublicService;
    private final EulatrackingPublicService eulatrackingPublicService;
    private final EventsPublicService eventsPublicService;
    private final FortnitePublicService fortnitePublicService;

    /**
     * GSON instance.
     */
    private final Gson gson;

    /**
     * This account.
     */
    private Account account;

    AthenaImpl(final Builder builder) throws FortniteAuthenticationException {
        // Create a new cookie manager for the cookie jar.
        final var manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        // build the client.
        client = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(manager)).addInterceptor(this).build();

        // initialize our gson instance
        gson = initializeGson();
        fortniteAuthenticationManager = new FortniteAuthenticationManager(builder.email(), builder.password(), builder.code(), builder.epicGamesLauncherToken(), builder.rememberDevice(), client, gson);
        // authenticate!
        final var session = fortniteAuthenticationManager.authenticate();
        this.session.set(session); // set the session

        LOGGER.atInfo().log("Successfully authenticated.");

        try {
            // create shutdown hook if necessary
            if (builder.handleShutdown()) Runtime.getRuntime().addShutdownHook(new Thread(this::close));
            // kill other sessions if enabled.
            if (builder.killOtherSessions()) fortniteAuthenticationManager.killOtherSessions();
            // accept EULA.
            if (builder.acceptEula()) fortniteAuthenticationManager.acceptEulaIfNeeded(session.accountId());

            LOGGER.atInfo().log("Finished post-authentication actions.");
        } catch (final IOException exception) {
            // TODO: Throw FortniteAuthenticationException? we are already authenticated at this point.
            LOGGER.atWarning().withCause(exception).log("Failed to kill other sessions or accept the EULA because of an IO error.");
        }

        LOGGER.atInfo().log("Initializing Retrofit services.");

        // initialize retrofit services.
        accountPublicService = new Retrofit.Builder()
                .baseUrl(AccountPublicService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(AccountPublicService.class);
        friendsPublicService = new Retrofit.Builder()
                .baseUrl(FriendsPublicService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(FriendsPublicService.class);
        statsproxyPublicService = new Retrofit.Builder()
                .baseUrl(StatsproxyPublicService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(StatsproxyPublicService.class);
        eulatrackingPublicService = new Retrofit.Builder()
                .baseUrl(EulatrackingPublicService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(EulatrackingPublicService.class);
        eventsPublicService = new Retrofit.Builder()
                .baseUrl(EventsPublicService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(EventsPublicService.class);
        fortnitePublicService = new Retrofit.Builder()
                .baseUrl(FortnitePublicService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(FortnitePublicService.class);

        LOGGER.atInfo().log("Initializing resources.");
        // initialize each resource/provider.
        accounts = new Accounts(accountPublicService);
        friends = new Friends(friendsPublicService, session.accountId());
        statisticsV2 = new StatisticsV2(statsproxyPublicService, accountPublicService);
        events = new Events(eventsPublicService, session.accountId());
        shop = new Shop(fortnitePublicService);

        // find our own account.
        accounts.findOneByAccountId(session.accountId()).ifPresent(acc -> this.account = acc);

        // done!
        LOGGER.atInfo().log("Ready!");
    }

    /**
     * Initialize our GSON instance.
     *
     * @return the GSON instance.
     */
    private Gson initializeGson() {
        final var fireGson = new GsonFireBuilder();

        // enable post-deserialize hook for statistics/external auth.
        fireGson.enableHooks(UnfilteredStatistic.class);
        fireGson.enableHooks(ExternalAuth.class);
        // default enum values for friends.
        fireGson.enumDefaultValue(FriendStatus.class, FriendStatus.UNKNOWN);
        fireGson.enumDefaultValue(FriendDirection.class, FriendDirection.UNKNOWN);
        // post processors for account and friend.
        fireGson.registerPostProcessor(Account.class, (BasicPostProcessor<Account>) (result, src, gson) ->
                result.postProcess(accountPublicService, friendsPublicService, session().accountId()));
        fireGson.registerPostProcessor(Friend.class, (BasicPostProcessor<Friend>) (result, src, gson) ->
                result.postProcess(accountPublicService, friendsPublicService, session().accountId()));
        // register our type adapters.
        final var builder = fireGson.createGsonBuilder();
        builder
                .registerTypeAdapter(Instant.class, (BasicJsonDeserializer<Instant>) (json) -> Instant.parse(json.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(Input.class, (BasicJsonDeserializer<Input>) (json) -> Input.typeOf(json.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(Platform.class, (BasicJsonDeserializer<Platform>) (json) -> Platform.typeOf(json.getAsJsonPrimitive().getAsString()))
                .registerTypeAdapter(Region.class, (BasicJsonDeserializer<Region>) (json) -> Region.valueOf(json.getAsJsonPrimitive().getAsString()));

        return builder.create();
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
    public Shop shop() {
        return shop;
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
        return account.accountId();
    }

    @Override
    public Session session() {
        return session.get();
    }

    @Override
    public void close() {
        try {
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
