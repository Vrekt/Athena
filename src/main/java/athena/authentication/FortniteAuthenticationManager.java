package athena.authentication;

import athena.authentication.session.Session;
import athena.exception.EpicGamesErrorException;
import athena.exception.FortniteAuthenticationException;
import athena.util.json.JsonFind;
import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;

/**
 * This class handles all things related to authentication, this includes:
 * 1. Logging in
 * 2. Killing other sessions
 * 3. Renewing tokens
 * 4. Retiring the token
 * 5. EULA
 * 6. ... etc
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
     * Credentials.
     */
    private final String epicGamesLauncherToken;
    private final String emailAddress, password, code;
    private final boolean rememberDevice, use2fa;

    /**
     * The constructor to initialize a new authentication manager.
     *
     * @param emailAddress           The email address of the account
     * @param password               the password of the account.
     * @param code                   the 2FA code if 2FA is enabled.
     * @param epicGamesLauncherToken the current epic games launcher token.
     * @param rememberDevice         should be {@code true} if 2FA devices should be remembered.
     * @param client                 the HTTP client.
     */
    public FortniteAuthenticationManager(String emailAddress, String password, String code,
                                         String epicGamesLauncherToken, boolean rememberDevice, OkHttpClient client, Gson gson) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.code = code;

        this.epicGamesLauncherToken = epicGamesLauncherToken;
        this.rememberDevice = rememberDevice;
        this.client = client;

        use2fa = code != null && !code.isEmpty();
        this.gson = gson;
    }

    /**
     * Attempts to authenticate with Fortnite.
     *
     * @return a new {@link Session} if authentication was successful.
     * @throws FortniteAuthenticationException if an error occurred while authenticating.
     */
    public Session authenticate() throws FortniteAuthenticationException {
        try {
            var token = retrieveXSRFToken();
            // if there is a conflict, retry.
            if (postLoginForm(token, false) == 409) return authenticate();

            if (use2fa) {
                // retrieve a new token since we are using 2FA.
                token = retrieveXSRFToken();
                postLoginForm(token, true);
            }

            final var code = performExchange(token);
            if (code == null) {
                LOGGER.atSevere().log("Exchange code was not given via endpoint [https://www.epicgames.com/id/api/exchange]");
                throw new FortniteAuthenticationException("Failed to exchange code. [Exchange code was not given]");
            }
            return retrieveSession(token, code);
        } catch (final EpicGamesErrorException | IOException exception) {
            throw new FortniteAuthenticationException("Failed to authenticate.", exception);
        }
    }

    /**
     * The first step in authenticating, the XSRF-TOKEN must be retrieved for future requests.
     */
    private String retrieveXSRFToken() throws IOException {
        final var url = "https://www.epicgames.com/id/api/csrf";
        final var response = client.newCall(new Request.Builder()
                .url(url)
                .get()
                .build())
                .execute();

        response.close();

        // load the cookies for this request and then find the XSRF token and return it if present.
        final var cookies = client.cookieJar().loadForRequest(response.request().url());
        return cookies.stream().filter(cookie -> cookie.name().equalsIgnoreCase("XSRF-TOKEN")).findAny().map(Cookie::value).orElse("");
    }

    /**
     * Posts the login form to the login endpoint.
     *
     * @param token the token
     * @return the HTTP code of the response.
     * @throws IOException if an error occurred
     */
    private int postLoginForm(String token, boolean use2faForm) throws IOException {
        final var body = createLoginForm(use2faForm);
        final var url = "https://www.epicgames.com/id/api/login" + (use2faForm ? "/mfa" : "");

        // execute the request.
        final var response = client.newCall(new Request.Builder()
                .url(url)
                .header("x-xsrf-token", token)
                .post(body)
                .build())
                .execute();

        final var code = response.code();
        response.close();
        return code;
    }

    /**
     * Creates a form body based on 2FA preferences.
     * https://gist.github.com/iXyles/ec40cb40a2a186425ec6bfb9dcc2ddda
     *
     * @return the form body.
     */
    private FormBody createLoginForm(boolean use2faForm) {
        final var body = new FormBody.Builder();
        if (use2faForm) {
            body.add("code", code);
            body.add("method", "authenticator");
            body.add("rememberDevice", rememberDevice + "");
        } else {
            body.add("email", emailAddress);
            body.add("password", password);
            body.add("rememberMe", "false");
        }
        return body.build();
    }

    /**
     * Sends a GET request to the redirect endpoint.
     * TODO: Currently not required for authentication flow.
     *
     * @param token the token
     * @throws IOException if an error occurred
     */
    private void notifyRedirect(String token) throws IOException {
        final var url = "https://www.epicgames.com/id/api/redirect";
        client.newCall(new Request.Builder()
                .url(url)
                .header("x-xsrf-token", token)
                .header("Referer", "https://www.epicgames.com/id/login")
                .get()
                .build())
                .execute()
                .close();
    }

    /**
     * Sends a GET request to the authentication endpoint.
     * TODO: Currently not required for authentication flow.
     *
     * @throws IOException if an error occurred
     */
    private void notifyAuthentication(String token) throws IOException {
        final var url = "https://www.epicgames.com/id/api/authenticate";
        client.newCall(new Request.Builder()
                .url(url)
                .header("x-xsrf-token", token)
                .get()
                .build())
                .execute()
                .close();
    }

    /**
     * Performs an exchange and retrieves the code.
     *
     * @param token the token
     * @return the code given for authentication
     * @throws IOException             if an error occurred
     * @throws EpicGamesErrorException if the API response returned an error.
     */
    @SuppressWarnings("ConstantConditions")
    private String performExchange(String token) throws IOException, EpicGamesErrorException {
        final var url = "https://www.epicgames.com/id/api/exchange";
        final var response = client.newCall(new Request.Builder()
                .url(url)
                .header("x-xsrf-token", token)
                .get()
                .build())
                .execute();

        final var body = response.body().string();
        response.close();

        final var json = gson.fromJson(body, JsonObject.class);
        if (response.isSuccessful()) {
            return JsonFind.findStringOptional(json, "code").orElse(null);
        } else {
            throw EpicGamesErrorException.create(url, json);
        }
    }

    /**
     * Finally, exchange the code and retrieve the new session!
     *
     * @param token the token
     * @param code  the exchange code
     * @return a new session
     * @throws IOException             if an error occurred
     * @throws EpicGamesErrorException if the API response returned an error.
     */
    @SuppressWarnings("ConstantConditions")
    private Session retrieveSession(String token, String code) throws IOException, EpicGamesErrorException {
        final var url = "https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/token";
        final var body = new FormBody.Builder()
                .add("grant_type", "exchange_code")
                .add("exchange_code", code)
                .add("includePerms", "false")
                .add("token_type", "eg1")
                .build();

        final var response = client.newCall(new Request.Builder()
                .url(url)
                .header("x-xsrf-token", token)
                .header("Authorization", "basic " + epicGamesLauncherToken)
                .post(body)
                .build())
                .execute();

        final var result = response.body().string();
        response.close();

        if (response.isSuccessful()) return gson.fromJson(result, Session.class);
        throw EpicGamesErrorException.create(url, gson.fromJson(result, JsonObject.class));
    }

    /**
     * Retrieve the refresh session.
     *
     * @param refreshToken the refresh token.
     * @return a new session
     * @throws IOException             if an error occurred
     * @throws EpicGamesErrorException if the API response returned an error.
     */
    @SuppressWarnings("ConstantConditions")
    public Session retrieveRefreshSession(String refreshToken) throws IOException, EpicGamesErrorException {
        final var url = "https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/token";
        final var body = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", refreshToken)
                .build();

        final var response = client.newCall(new Request.Builder()
                .url(url)
                .header("Authorization", "basic " + epicGamesLauncherToken)
                .post(body)
                .build())
                .execute();

        final var result = response.body().string();
        response.close();

        if (response.isSuccessful()) return gson.fromJson(result, Session.class);
        throw EpicGamesErrorException.create(url, gson.fromJson(result, JsonObject.class));
    }

    /**
     * Kills other tokens that are active.
     *
     * @throws IOException if an error occurred.
     */
    public void killOtherSessions() throws IOException {
        final var request = new Request.Builder()
                .url("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/sessions/kill?killType=OTHERS_ACCOUNT_CLIENT_SERVICE")
                .delete()
                .build();
        client.newCall(request).execute().close();
    }

    /**
     * Accepts the Fortnite EULA if needed.
     *
     * @param accountId the ID of the current authenticated account
     * @throws IOException if an error occurred executing the request.
     */
    public void acceptEulaIfNeeded(String accountId) throws IOException {
        final var version = getEulaVersion(accountId);
        if (version == 0) return; // no EULA version?
        acceptEula(version, accountId);
        grantAccess(accountId);
    }

    /**
     * Kills the access token granted for this session
     *
     * @param accessToken the old access token
     * @throws IOException if an error occurred.
     */
    public void killToken(String accessToken) throws IOException {
        final var request = new Request.Builder()
                .url("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/sessions/kill/" + accessToken)
                .delete()
                .build();

        client.newCall(request).execute().close();
    }

    /**
     * Attempts to get the current Fortnite EULA version.
     *
     * @param accountId the ID of the current authenticated account
     * @return the current Fortnite EULA version.
     * @throws IOException             if an error occurred executing the request.
     * @throws EpicGamesErrorException if the API response returned an error.
     */
    @SuppressWarnings("ConstantConditions")
    private int getEulaVersion(String accountId) throws IOException, EpicGamesErrorException {
        final var response = client.newCall(new Request.Builder()
                .url("https://eulatracking-public-service-prod-m.ol.epicgames.com/eulatracking/api/public/agreements/fn/account/" + accountId + "?locale=en-US")
                .get().build()).execute();

        final var result = response.body().string();
        response.close();

        final var object = gson.fromJson(result, JsonObject.class);

        if (response.isSuccessful()) return JsonFind.findInt(object, "version");
        throw EpicGamesErrorException.create(response.request().url().toString(), object);
    }

    /**
     * Attempts to accept the EULA.
     *
     * @param version   the current version of the Fortnite EULA.
     * @param accountId the ID of the current authenticated account
     * @throws IOException             if an error occurred executing the request.
     * @throws EpicGamesErrorException if the API response returned an error.
     */
    @SuppressWarnings("ConstantConditions")
    private void acceptEula(int version, String accountId) throws IOException, EpicGamesErrorException {
        final var response = client.newCall(new Request.Builder()
                .url("https://eulatracking-public-service-prod-m.ol.epicgames.com/eulatracking/api/public/agreements/fn/version/" + version + "/account/" + accountId + "/accept?locale=en")
                .post(RequestBody.create(new byte[]{}, null)).build()).execute();
        final var result = response.body().string();
        response.close();

        final var object = gson.fromJson(result, JsonObject.class);
        if (!response.isSuccessful()) throw EpicGamesErrorException.create(response.request().url().toString(), object);
    }

    /**
     * Attempts to grant access to Fortnite services.
     *
     * @param accountId the ID of the current authenticated account
     * @throws IOException             if an error occurred executing the request.
     * @throws EpicGamesErrorException if the API response returned an error.
     */
    @SuppressWarnings("ConstantConditions")
    private void grantAccess(String accountId) throws IOException, EpicGamesErrorException {
        try {
            final var response = client.newCall(new Request.Builder()
                    .url("https://fortnite-public-service-prod11.ol.epicgames.com/fortnite/api/game/v2/grant_access/" + accountId)
                    .post(RequestBody.create(new byte[]{}, null)).build()).execute();

            final var result = response.body().string();
            response.close();

            final var object = gson.fromJson(result, JsonObject.class);
            if (!response.isSuccessful()) throw EpicGamesErrorException.create(response.request().url().toString(), object);
        } catch (final EpicGamesErrorException exception) {
            System.err.println(exception.errorCode());
            // we want to re-throw the exception if its not equal to the error below.
            // the error below indicates the EULA is already accepted.
            if (!exception.errorCode().equalsIgnoreCase("errors.com.epicgames.fortnite.free_grant_access_unnecessary")) {
                ExceptionUtils.rethrow(exception);
            }
        }
    }

}
