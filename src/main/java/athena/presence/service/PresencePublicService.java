package athena.presence.service;

import athena.presence.resource.LastOnlineResponse;
import athena.presence.resource.subscription.PresenceSubscription;
import athena.presence.resource.subscription.SubscriptionSettings;
import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Provides access to the presence public service.
 */
public interface PresencePublicService {

    String BASE_URL = "https://presence-public-service-prod.ol.epicgames.com/";

    /**
     * Get the last online times of all friends.
     *
     * @param accountId the account ID.
     * @return a {@link Call} returned by retrofit containing the {@link LastOnlineResponse} if the call was successful.
     */
    @GET("presence/api/v1/_/{accountId}/last-online")
    Call<LastOnlineResponse> lastOnline(@Path("accountId") String accountId);

    /**
     * Get the subscription settings.
     *
     * @param accountId the account ID.
     * @return a {@link Call} returned by retrofit containing the {@link SubscriptionSettings} if the call was successful.
     */
    @GET("presence/api/v1/_/{accountId}/settings/subscriptions")
    Call<SubscriptionSettings> subscriptionSettings(@Path("accountId") String accountId);

    /**
     * Set the subscription settings
     *
     * @param accountId the account ID.
     * @param settings  the new settings\
     */
    @PATCH("presence/api/v1/_/{accountId}/settings/subscriptions")
    Call<Void> setSubscriptionSettings(@Path("accountId") String accountId, @Body SubscriptionSettings settings);

    /**
     * Subscribe to an account
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID.
     */
    @POST("presence/api/v1/_/{localAccountId}/subscriptions/{accountId}")
    Call<Void> subscribeTo(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Unsubscribe from an account.
     *
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the account ID.
     */
    @DELETE("presence/api/v1/_/{localAccountId}/subscriptions/{accountId}")
    Call<Void> unsubscribe(@Path("localAccountId") String localAccountId, @Path("accountId") String accountId);

    /**
     * Get a list of subscriptions.
     *
     * @param accountId the account ID of the current authenticated account.
     * @return a {@link Call} returned by retrofit containing the {@link List<PresenceSubscription>} if the call was successful.
     */
    @GET("presence/api/v1/_/{accountId}/subscriptions")
    Call<List<PresenceSubscription>> subscriptions(@Path("accountId") String accountId);

    /**
     * Broadcast the account is playing Fortnite. (namespace must be fn)
     *
     * @param namespace the namespace usually fn
     * @param accountId the account ID of the current authenticated account.
     */
    @POST("presence/api/v1/{namespace}/{accountId}/subscriptions/broadcast")
    Call<Void> broadcast(@Path("namespace") String namespace, @Path("accountId") String accountId);

    /**
     * Get the nudged array.
     * TODO: Currently unknown.
     *
     * @param namespace the namespace usually fn
     * @param accountId the account ID of the current authenticated account.
     * @return a {@link JsonElement}
     */
    @GET("presence/api/v1/{namespace}/{accountId}/subscriptions/nudged")
    Call<JsonElement> nudged(@Path("namespace") String namespace, @Path("accountId") String accountId);

    /**
     * Nudge an account?
     * TODO: Currently unknown.
     *
     * @param namespace      the namespace usually fn
     * @param localAccountId the account ID of the current authenticated account.
     * @param accountId      the other account ID.
     */
    @PUT("presence/api/v1/{namespace}/{localAccountId}/subscriptions/nudged/{accountId}")
    Call<Void> nudgeAccount(@Path("namespace") String namespace, @Path("localAccountId") String localAccountId, @Path("accountId") String accountId);
}
