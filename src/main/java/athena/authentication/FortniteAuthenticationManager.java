package athena.authentication;

import athena.account.resource.device.Device;
import athena.account.service.AccountPublicService;
import athena.authentication.service.AuthenticationService;
import athena.authentication.session.Session;
import athena.authentication.type.AuthClient;
import athena.authentication.type.GrantType;
import athena.eula.service.EulatrackingPublicService;
import athena.exception.EpicGamesErrorException;
import athena.fortnite.service.FortnitePublicService;
import athena.util.request.Requests;
import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * This class handles all things related to authentication, this includes:
 * 1. Logging in
 * 2. Killing other sessions
 * 3. Renewing tokens
 * 4. Retiring the token
 * 5. EULA
 * 6. ... etc
 * <p>
 * Built with help from: iXyles, Loukios#6383, and Joe for whole Kairos auth-flow.
 * https://gist.github.com/iXyles/ec40cb40a2a186425ec6bfb9dcc2ddda
 *
 * @author Vrekt, iXyles, Loukios, Mix, RobertoGraham
 */
public final class FortniteAuthenticationManager {
    /**
     * The LOGGER.
     */
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();
    /**
     * The authentication method to use and our GSON instance for this authentication manager.
     */
    private final Gson gson;
    /**
     * The HTTP client.
     */
    private final OkHttpClient client;

    /**
     * The services needed for authentication.
     */
    private final AuthenticationService authenticationService;
    private final AccountPublicService accountPublicService;
    private final EulatrackingPublicService eulatrackingPublicService;
    private final FortnitePublicService fortnitePublicService;

    /**
     * Credentials.
     */
    private final String authorizationToken;
    private final String emailAddress, password, code;
    private final String accountId, deviceId, secret;
    private final boolean rememberMe, kairos;
    private GrantType grantType;
    private Device device;

    /**
     * The constructor to initialize a new authentication manager.
     *
     * @param emailAddress       The email address of the account
     * @param password           the password of the account.
     * @param code               the 2FA code if 2FA is enabled.
     * @param accountId          the account ID for device-auth
     * @param deviceId           the device ID for device-auth
     * @param secret             the device ID for device-auth
     * @param authorizationToken the authorization token.
     * @param rememberMe         if user/device should be remembered.
     * @param grantType          the grant type to use.
     * @param device             the device to use for device auth.
     * @param client             the HTTP client.
     * @param gson               the {@link athena.Athena} internal GSON instance.
     */
    public FortniteAuthenticationManager(String emailAddress, String password, String code, String accountId, String deviceId, String secret,
                                         String authorizationToken, boolean rememberMe, boolean kairos, GrantType grantType, Device device,
                                         AuthenticationService authenticationService, AccountPublicService accountPublicService,
                                         EulatrackingPublicService eulatrackingPublicService, FortnitePublicService fortnitePublicService,
                                         OkHttpClient client, Gson gson) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.code = code;
        this.accountId = accountId;
        this.deviceId = deviceId;
        this.secret = secret;

        this.authorizationToken = authorizationToken;
        this.rememberMe = rememberMe;
        this.kairos = kairos;
        this.grantType = grantType;
        this.device = device;

        this.authenticationService = authenticationService;
        this.accountPublicService = accountPublicService;
        this.eulatrackingPublicService = eulatrackingPublicService;
        this.fortnitePublicService = fortnitePublicService;

        this.client = client;

        this.gson = gson;
    }

    /**
     * Attempts to authenticate with Fortnite.
     *
     * @return a new {@link Session} if authentication was successful.
     * @throws EpicGamesErrorException if an error occurred while authenticating.
     */
    public Session authenticate() throws EpicGamesErrorException {
        // since we are using device auth we can authenticate right away.
        if (grantType == GrantType.DEVICE_AUTH)
            return Requests.executeCall(accountPublicService.grantSession(
                    "basic " + authorizationToken,
                    "device_auth",
                    Map.of("account_id", accountId, "device_id", deviceId, "secret", secret)));

        // otherwise, request our XSRF token
        Requests.executeCall(authenticationService.csrf());
        // retrieve it from cookies, or else throw.
        var token = xsrf();

        // next, retrieve our reputation
        final var verdict = Requests.executeCall(authenticationService.reputation(token)).verdict();
        if (verdict.equalsIgnoreCase("arkose")) {
            // we have a captcha for logging in, throw an exception.
            throw EpicGamesErrorException.create("Encountered a captcha while trying to login.");
        }

        // now we can try and post the login form.
        try {
            // build the login request based off kairos
            final var loginCall = authenticationService.login(token, kairos ? AuthClient.KAIROS_PC_CLIENT.clientId() : null, kairos ? "code" : null, buildForm(false));
            Requests.executeCall(loginCall);
        } catch (EpicGamesErrorException exception) {
            if (exception.errorCode().equalsIgnoreCase("errors.com.epicgames.common.two_factor_authentication.required")) {
                // 2FA is required, retrieve a new token and then continue.
                Requests.executeCall(authenticationService.csrf());
                token = xsrf();

                // now login with MFA based off kairos
                Requests.executeCall(authenticationService.loginMfa(token, kairos ? AuthClient.KAIROS_PC_CLIENT.clientId() : null, kairos ? "code" : null, buildForm(true)));
            } else {
                // a different exception, rethrow it
                throw exception;
            }
        }

        // now we can retrieve our exchange code depending on the method.
        String code;
        if (kairos) {
            // kairos authentication
            final var exchange = Requests.executeCall(authenticationService.redirect(token, "code", AuthClient.KAIROS_PC_CLIENT.clientId()));
            code = StringUtils.substringAfter(exchange.redirectUrl(), "code=");
        } else {
            // regular authentication
            code = Requests.executeCall(authenticationService.exchange(token)).code();
        }

        // create the map based on kairos
        final var map = kairos ? Map.of("code", code) : Map.of("exchange_code", code, "token_type", "eg1");
        // finally retrieve the session
        return Requests.executeCall(accountPublicService.grantSession(
                "basic " + authorizationToken,
                grantType.name().toLowerCase(), map));
    }

    /**
     * Retrieve the XSRF token from cookies.
     *
     * @return the token or ""
     */
    private String xsrf() {
        final var cookies = client.cookieJar().loadForRequest(HttpUrl.get("https://www.epicgames.com/id/api/csrf"));
        return cookies
                .stream()
                .filter(cookie -> cookie.name().equalsIgnoreCase("XSRF-TOKEN"))
                .findAny()
                .map(Cookie::value)
                .orElseThrow(() -> EpicGamesErrorException.create("Could not find the XSRF token from cookies!"));
    }

    /**
     * Creates a form body based on 2FA preferences.
     *
     * @param twoFactor {@code true} if two factor is being used.
     * @return the form body.
     */
    private FormBody buildForm(boolean twoFactor) {
        final var body = new FormBody.Builder();
        if (twoFactor) {
            body.add("code", code);
            body.add("method", "authenticator");
            body.add("rememberDevice", rememberMe + "");
        } else {
            body.add("email", emailAddress);
            body.add("password", password);
            body.add("rememberMe", rememberMe + "");
        }
        return body.build();
    }

    /**
     * Kills other tokens that are active.
     *
     * @throws EpicGamesErrorException if an API error occurred.
     */
    public void killOtherSessions() throws EpicGamesErrorException {
        Requests.executeCall(accountPublicService.killSessions("OTHERS_ACCOUNT_CLIENT_SERVICE"));
    }

    /**
     * Accepts the Fortnite EULA if needed.
     *
     * @param accountId the ID of the current authenticated account
     * @throws EpicGamesErrorException if an API error occurred.
     */
    public void acceptEulaIfNeeded(String accountId) throws EpicGamesErrorException {
        final var version = getEulaVersion(accountId);
        acceptEula(version, accountId);
        grantAccess(accountId);
    }

    /**
     * Kills the access token granted for this session
     *
     * @param accessToken the old access token
     * @throws EpicGamesErrorException if an API error occurred.
     */
    public void killToken(String accessToken) throws EpicGamesErrorException {
        Requests.executeCall(accountPublicService.killAccessToken(accessToken));
    }

    /**
     * Attempts to get the current Fortnite EULA version.
     *
     * @param accountId the ID of the current authenticated account
     * @return the current Fortnite EULA version.
     * @throws EpicGamesErrorException if an API error occurred.
     */
    private int getEulaVersion(String accountId) throws EpicGamesErrorException {
        return Requests.executeCall(eulatrackingPublicService.eula(accountId, "en-US")).version();
    }

    /**
     * Attempts to accept the EULA.
     *
     * @param version   the current version of the Fortnite EULA.
     * @param accountId the ID of the current authenticated account
     * @throws EpicGamesErrorException if an API error occurred.
     */
    private void acceptEula(int version, String accountId) throws EpicGamesErrorException {
        Requests.executeCall(eulatrackingPublicService.acceptEula(version, accountId, "en"));
    }

    /**
     * Attempts to grant access to Fortnite services.
     *
     * @param accountId the ID of the current authenticated account
     * @throws EpicGamesErrorException if an API error occurred.
     */
    private void grantAccess(String accountId) throws EpicGamesErrorException {
        try {
            Requests.executeCall(fortnitePublicService.grantAccess(accountId));
        } catch (EpicGamesErrorException exception) {
            // suppress the error if we already have been granted access.
            if (!exception.errorCode().equalsIgnoreCase("errors.com.epicgames.fortnite.free_grant_access_unnecessary")) {
                throw exception;
            }
        }
    }

}
