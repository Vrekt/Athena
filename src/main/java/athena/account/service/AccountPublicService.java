package athena.account.service;

import athena.account.resource.Account;
import athena.account.resource.EpicGamesProfile;
import athena.account.resource.address.AccountAddress;
import athena.account.resource.device.DeviceAuth;
import athena.authentication.session.Session;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * Provides access to the account public service.
 * Credit: https://gist.github.com/Amrsatrio/20d2174583354ae4f0a24cf63764049f for some endpoints.
 *
 * @author Vrekt, Armisto
 */
public interface AccountPublicService {

    /**
     * The base url for the AccountPublicService
     */
    String BASE_URL = "https://account-public-service-prod.ol.epicgames.com/";

    /**
     * Finds an account by display name.
     *
     * @param displayName the display name of the account
     * @return a {@link Call} returned by retrofit containing the {@link Account} if the call was successful.
     */
    @GET("account/api/public/account/displayName/{displayName}")
    Call<Account> findByDisplayName(@Path("displayName") String displayName);

    /**
     * Finds a single account by account ID.
     *
     * @param account the account ID.
     * @return a {@link Call} returned by retrofit containing the {@link Account[]} if the call was successful.
     */
    @GET("account/api/public/account")
    Call<List<Account>> findOneByAccountId(@Query("accountId") String account);

    /**
     * Finds multiple accounts by account ID.
     *
     * @param accounts an array of accounts.
     * @return a {@link Call} returned by retrofit containing the {@link List<Account>} if the call was successful.
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
     * @return Void
     */
    @DELETE("account/api/oauth/sessions/kill")
    Call<Void> killSessions(@Query("killType") String killType);

    /**
     * Kills an active access token.
     *
     * @param accessToken the access token
     * @return Void
     */
    @DELETE("account/api/oauth/sessions/kill/{accessToken}")
    Call<Void> killAccessToken(@Path("accessToken") String accessToken);

    /**
     * Grant a new session.
     *
     * @param authorizationToken the authorization token, see {@link athena.authentication.type.AuthClient}
     * @param grantType          the grant_type, see {@link athena.authentication.type.GrantType} or https://github.com/MixV2/EpicResearch#grant-types
     * @param fields             required fields for the grant type, see https://github.com/MixV2/EpicResearch#grant-types
     * @return a {@link Call} returned by retrofit containing the {@link Session} if the call was successful.
     */
    @FormUrlEncoded
    @POST("account/api/oauth/token")
    Call<Session> grantSession(@Header("Authorization") String authorizationToken, @Field("grant_type") String grantType, @FieldMap Map<String, String> fields);

    /**
     * Get the profile of the current authenticated account.
     *
     * @param accountId the current authenticated account ID.
     * @return a {@link Call} returned by retrofit containing the {@link EpicGamesProfile} if the call was successful.
     */
    @GET("account/api/public/account/{accountId}")
    Call<EpicGamesProfile> profile(@Path("accountId") String accountId);

    /**
     * Get device auths for the provided {@code accountId}
     *
     * @param accountId the account ID.
     * @return a {@link Call} returned by retrofit containing the {@link List<DeviceAuth>} if the call was successful.
     */
    @GET("account/api/public/account/{accountId}/deviceAuth")
    Call<List<DeviceAuth>> deviceAuths(@Path("accountId") String accountId);

    /**
     * Get a specific device-auth for the provided {@code accountId}
     *
     * @param accountId the account ID.
     * @param deviceId  the device ID.
     * @return a {@link Call} returned by retrofit containing the {@link DeviceAuth} if the call was successful.
     */
    @GET("account/api/public/account/{accountId}/deviceAuth/{deviceId}")
    Call<DeviceAuth> deviceAuth(@Path("accountId") String accountId, @Path("deviceId") String deviceId);

    /**
     * Create a new device-auth.
     *
     * @param accountId the account ID.
     * @param device    the device info as JSON, see {@link athena.account.resource.device.Device}
     * @return a {@link Call} returned by retrofit containing the {@link DeviceAuth} if the call was successful.
     */
    @POST("account/api/public/account/{accountId}/deviceAuth")
    Call<DeviceAuth> createDeviceAuth(@Path("accountId") String accountId, @Header("X-Epic-Device-Info") String device);

    /**
     * Delete a device-auth.
     *
     * @param accountId the account ID.
     * @param deviceId  the device ID.
     * @return Void
     */
    @DELETE("account/api/public/account/{accountId}/deviceAuth/{deviceId}")
    Call<Void> deleteDeviceAuth(@Path("accountId") String accountId, @Path("deviceId") String deviceId);

    /**
     * Get a list of addresses for an account.
     * Credit: Armisto
     *
     * @param accountId the account ID.
     * @return a {@link Call} returned by retrofit containing the {@link List<AccountAddress>} if the call was successful.
     */
    @GET("account/api/public/account/{accountId}/addresses")
    Call<List<AccountAddress>> accountAddresses(@Path("accountId") String accountId);


}
