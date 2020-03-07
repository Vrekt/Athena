package athena.authentication.service;

import athena.authentication.exchange.Exchange;
import athena.authentication.redirect.Redirect;
import athena.authentication.reputation.Reputation;
import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Used for authentication
 */
public interface AuthenticationService {

    /**
     * Base URL
     */
    String BASE_URL = "https://www.epicgames.com/id/api/";

    /**
     * GETs on csrf, retrieving the XSRF-TOKEN.
     *
     * @return Void
     */
    @GET("csrf")
    Call<Void> csrf();

    /**
     * Gets the reputation.
     *
     * @param xsrfToken the XSRF token
     * @return the {@link Reputation}
     */
    @GET("reputation")
    Call<Reputation> reputation(@Header("x-xsrf-token") String xsrfToken);

    /**
     * Login normally.
     *
     * @param xsrfToken the XSRF token
     * @param body      the form
     * @return Void
     */
    //@POST("login")
    // Call<Void> login(@Header("x-xsrf-token") String xsrfToken, @Body FormBody body);

    /**
     * Login using 2FA.
     *
     * @param xsrfToken the XSRF token
     * @param body      the form
     * @return Void
     */
    @POST("login/mfa")
    Call<Void> loginMfa(@Header("x-xsrf-token") String xsrfToken, @Query("client_id") String clientId, @Query("response_type") String responseType, @Body FormBody body);

    /**
     * Login using the specified client ID and response type.
     *
     * @param xsrfToken    the XSRF token
     * @param clientId     the client ID. {@link athena.authentication.type.AuthClient}
     * @param responseType the response type, usually "code"
     * @param body         the form
     * @return Void
     */
    @POST("login")
    Call<Void> login(@Header("x-xsrf-token") String xsrfToken, @Query("client_id") String clientId, @Query("response_type") String responseType, @Body FormBody body);

    /**
     * Retrieve the exchange code.
     *
     * @param xsrfToken the XSRF token
     * @return the {@link Exchange}
     */
    @GET("exchange")
    Call<Exchange> exchange(@Header("x-xsrf-token") String xsrfToken);

    /**
     * Redirect, used for kairos.
     *
     * @param xsrfToken    the token
     * @param responseType the response type, usually "code"
     * @param clientId     the client ID. {@link athena.authentication.type.AuthClient}
     * @return the {@link Redirect}
     */
    @GET("redirect")
    Call<Redirect> redirect(@Header("x-xsrf-token") String xsrfToken, @Query("responseType") String responseType, @Query("clientId") String clientId);

}
