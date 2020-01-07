package athena.eula.service;

import athena.eula.resource.Eula;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Used for accepting/getting versions of the EULA.
 */
public interface EulatrackingPublicService {

    String BASE_URL = "https://eulatracking-public-service-prod06.ol.epicgames.com/";

    /**
     * Get the current EULA version.
     *
     * @param accountId the ID of the account
     * @param locale    the locale, usually 'en-US'
     * @return a {@link Call} returned by retrofit containing the {@link Eula} if the call was successful.
     */
    @GET("eulatracking/api/public/agreements/fn/account/{accountId}?locale=en-US")
    Call<Eula> eula(@Path("accountId") String accountId, @Query("locale") String locale);

    /**
     * Accept the EULA.
     *
     * @param version   the current EULA version.
     * @param accountId the ID of the account
     * @param locale    the locale, usually 'en-US'
     */
    @POST("eulatracking/api/public/agreements/fn/version/{version}/{accountId}/accept")
    Call<Void> acceptEula(@Path("version") int version, @Path("accountId") String accountId, @Query("locale") String locale);

}
