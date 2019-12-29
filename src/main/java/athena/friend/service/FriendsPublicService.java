package athena.friend.service;

import athena.friend.resource.Friend;
import athena.friend.resource.profile.FriendProfile;
import athena.friend.resource.settings.FriendSettings;
import com.google.gson.JsonObject;
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
    String BASE_URL = "https://friends-public-service-prod.ol.epicgames.com";

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
     * TODO:
     *
     * @param accountId the account ID of the current authenticated account.
     * @return a {@link Call} returned by Retrofit with a {@link JsonObject} that will contain any blocked.
     */
    @GET("friends/api/v1/{accountId}/blocklist")
    Call<JsonObject> blocked(@Path("accountId") String accountId);

    /**
     * Adds a friend by account ID.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to add.
     */
    @POST("friends/api/v1/{localAccountId}/friends/{accountId}")
    Call<Void> addFriendByAccountId(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Removes a friend by account ID.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to remove.
     */
    @DELETE("friends/api/v1/{localAccountId}/friends/{accountId}")
    Call<Void> removeFriendByAccountId(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Blocks a friend by account ID.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to remove.
     */
    @POST("friends/api/public/blocklist/{localAccountId}/{accountId}")
    Call<Void> blockFriendByAccountId(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Unblocks a friend by account ID.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to unblock.
     */
    @DELETE("friends/api/public/blocklist/{localAccountId}/{accountId}")
    Call<Void> unblockFriendByAccountId(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Sets a friend alias.
     * The request-body MUST be "text/plain" and must contain the alias text.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to apply the alias to.
     * @param alias          the alias.
     */
    @Headers("Content-Type: text/plain")
    @PUT("friends/api/v1/{localAccountId}/friends/{accountId}/alias")
    Call<Void> setFriendAlias(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId, @Body String alias);

    /**
     * Removes a friend alias.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to remove the alias from.
     */
    @DELETE("friends/api/v1/{localAccountId}/friends/{accountId}/alias")
    Call<Void> removeFriendAlias(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Sets the note for a friend.
     * The request-body MUST be "text/plain" and must contain the note text.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to apply the note to.
     * @param note           the note.
     */
    @Headers("Content-Type: text/plain")
    @PUT("friends/api/v1/{localAccountId}/friends/{accountId}/note")
    Call<Void> setFriendNote(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId, @Body String note);

    /**
     * Removes a friend note.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID to remove the note from.
     */
    @DELETE("friends/api/v1/{localAccountId}/friends/{accountId}/note")
    Call<Void> removeFriendNote(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Retrieve the profile of a friend.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID of the friend.
     */
    @GET("friends/api/v1/{localAccountId}/friends/{accountId}")
    Call<FriendProfile> friendProfile(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Get the friend settings for the current authenticated account.
     *
     * @param accountId the account ID of the current authenticated account.
     */
    @GET("friends/api/v1/{accountId}/settings")
    Call<FriendSettings> settings(@Path("accountId") String accountId);

    /**
     * Set the friend settings.
     *
     * @param accountId the account ID of the current authenticated account.
     * @param settings  the settings
     */
    @PUT("friends/api/v1/{accountId}/settings")
    Call<FriendSettings> setSettings(@Path("accountId") String accountId, @Body FriendSettings settings);

}
