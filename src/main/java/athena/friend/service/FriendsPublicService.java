package athena.friend.service;

import athena.friend.resource.Friend;
import athena.friend.resource.blocked.Blocked;
import athena.friend.resource.settings.FriendSettings;
import athena.friend.resource.summary.Profile;
import athena.friend.resource.summary.Summary;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Provides access to the friend public service
 */
public interface FriendsPublicService {

    /**
     * The base url for the FriendsPublicService
     */
    String BASE_URL = "https://friends-public-service-prod.ol.epicgames.com/";
    /**
     * Media type for note and alias.
     */
    MediaType MEDIA_TYPE = MediaType.get("text/plain");

    /**
     * Gets all friends for the specified {@code accountId}
     *
     * @param accountId      the account ID of the current authenticated account.
     * @param includePending {@code true} if pending friend requests should be included.
     * @return a {@link Call} returned by Retrofit with the friend(s), if the call was successful.
     */
    @GET("friends/api/public/friends/{accountId}")
    Call<List<Friend>> friends(@Path("accountId") String accountId, @Query("includePending") boolean includePending);

    /**
     * Gets all block friends for the specified {@code accountId}
     *
     * @param accountId the account ID of the current authenticated account.
     * @return a {@link Call} returned by Retrofit with the blocked list, if the call was successful.
     */
    @GET("friends/api/v1/{accountId}/blocklist")
    Call<List<Blocked>> blocked(@Path("accountId") String accountId);

    /**
     * Adds a friend by account ID.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to add.
     */
    @POST("friends/api/v1/{localAccountId}/friends/{accountId}")
    Call<Void> add(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Removes a friend by account ID.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to remove.
     */
    @DELETE("friends/api/v1/{localAccountId}/friends/{accountId}")
    Call<Void> remove(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Blocks a friend by account ID.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to remove.
     */
    @POST("friends/api/public/blocklist/{localAccountId}/{accountId}")
    Call<Void> block(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Unblocks a friend by account ID.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to unblock.
     */
    @DELETE("friends/api/public/blocklist/{localAccountId}/{accountId}")
    Call<Void> unblock(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Sets a friend alias.
     * The request-body MUST be "text/plain" and must contain the alias text.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to apply the alias to.
     * @param alias          the request body {@code RequestBody.create(text, MediaType.get("text/plain")}
     */
    @PUT("friends/api/v1/{localAccountId}/friends/{accountId}/alias")
    Call<Void> setAlias(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId, @Body RequestBody alias);

    /**
     * Removes a friend alias.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to remove the alias from.
     */
    @DELETE("friends/api/v1/{localAccountId}/friends/{accountId}/alias")
    Call<Void> removeAlias(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Sets the note for a friend.
     * The request-body MUST be "text/plain" and must contain the note text.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to apply the note to.
     * @param note           the request body {@code RequestBody.create(text, MediaType.get("text/plain")}
     */
    @PUT("friends/api/v1/{localAccountId}/friends/{accountId}/note")
    Call<Void> setNote(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId, @Body RequestBody note);

    /**
     * Removes a friend note.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to remove the note from.
     */
    @DELETE("friends/api/v1/{localAccountId}/friends/{accountId}/note")
    Call<Void> removeNote(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Retrieve the summary of a friend.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID of the friend.
     * @param displayNames   {@code true} if the display name should be given.
     * @return a {@link Call} returned by retrofit containing the {@link Profile} if the call was successful.
     */
    @GET("friends/api/v1/{localAccountId}/friends/{accountId}")
    Call<Profile> profile(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId, @Query("displayNames") boolean displayNames);

    /**
     * Retrieve the summary of all friends.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param displayNames   {@code true} if display names should be given with each summary.
     * @return a {@link Call} returned by retrofit containing the {@link Summary} if the call was successful.
     */
    @GET("friends/api/v1/{localAccountId}/summary")
    Call<Summary> summary(@Path("localAccountId") String localAccountId, @Query("displayNames") boolean displayNames);

    /**
     * Get the friend settings for the current authenticated account.
     *
     * @param accountId the account ID of the current authenticated account.
     * @return a {@link Call} returned by retrofit containing the {@link FriendSettings} if the call was successful.
     */
    @GET("friends/api/v1/{accountId}/settings")
    Call<FriendSettings> settings(@Path("accountId") String accountId);

    /**
     * Set the friend settings.
     *
     * @param accountId the account ID of the current authenticated account.
     * @param settings  the settings
     * @return a {@link Call} returned by retrofit containing the {@link FriendSettings} if the call was successful.
     */
    @PUT("friends/api/v1/{accountId}/settings")
    Call<FriendSettings> setSettings(@Path("accountId") String accountId, @Body FriendSettings settings);

}
