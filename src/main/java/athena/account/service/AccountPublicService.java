package athena.account.service;

import athena.Athena;
import athena.account.resource.Account;
import athena.authentication.session.Session;
import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Provides access to the account public service.
 */
public interface AccountPublicService {

    /**
     * The base url for the AccountPublicService
     */
    String BASE_URL = "https://account-public-service-prod03.ol.epicgames.com";

    /**
     * Finds an account by display name.
     *
     * @param displayName the display name of the account
     */
    @GET("account/api/public/account/displayName/{displayName}")
    Call<Account> findByDisplayName(@Path("displayName") String displayName);

    /**
     * Finds a single account by account ID.
     *
     * @param account the account ID.
     */
    @GET("account/api/public/account")
    Call<Account> findOneByAccountId(@Query("accountId") String account);

    /**
     * Finds multiple accounts by account ID.
     *
     * @param accounts an array of accounts.
     */
    @GET("account/api/public/account")
    Call<List<Account>> findManyByAccountId(@Query("accountId") String... accounts);

    /**
     * Kills other authorization sessions, valid types are:
     * OTHERS_ACCOUNT_CLIENT_SERVICE
     * OTHERS_ACCOUNT_CLIENT
     * ALL_ACCOUNT_CLIENT
     * OTHERS
     *
     * @param killType the kill type
     */
    @DELETE("account/api/oauth/sessions/kill")
    Call<Void> killSessions(@Query("killType") String killType);

    /**
     * Kills an active access token.
     *
     * @param accessToken the access token
     */
    @DELETE("account/api/oauth/sessions/kill/{accessToken}")
    Call<Void> killAccessToken(@Path("accessToken") String accessToken);

    /**
     * Authenticate and retrieve the session.
     *
     * @param xsrfToken the XSRF token
     * @param body      the form-body.
     *                  <p>
     *                  final var body = new FormBody.Builder()
     *                  .add("grant_type", "exchange_code")
     *                  .add("exchange_code", code)
     *                  .add("includePerms", "false")
     *                  .add("token_type", "eg1")
     *                  .build();
     * @return a new {@link Session}
     * TODO: May or may not work.
     */
    @Headers("Authorization: basic " + Athena.EPIC_GAMES_LAUNCHER_TOKEN)
    @POST("account/api/oauth/token")
    Call<Session> retrieveSession(@Header("x-xsrf-token") String xsrfToken, @Body FormBody body);

}
