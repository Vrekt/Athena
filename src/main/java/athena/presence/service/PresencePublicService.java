package athena.presence.service;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Provides access to the presence public service.
 */
public interface PresencePublicService {

    // /presence/api/v1/_/`id/last-online
    ///presence/api/v1/_/`id/settings/subscriptions
    ///presence/api/v1/_/`id/subscriptions/`otherId
    ///presence/api/v1/_/`id/subscriptions
    ///presence/api/v1/`namespace/`id/subscriptions/broadcast
    ///presence/api/v1/`namespace/`id/subscriptions/nudged/`otherId
    ///presence/api/v1/`namespace/`id/subscriptions/nudged

    String BASE_URL = "https://presence-public-service-prod.ol.epicgames.com/";

    @GET("presence/api/v1/_/{accountId}/last-online")
    Call<JsonElement> lastOnline(@Path("accountId") String accountId);

    @GET("presence/api/v1/_/{accountId}/settings/subscriptions")
    Call<JsonElement> test(@Path("accountId") String accountId);


}
