package athena.fortnite.service;

import athena.fortnite.creative.CreativeHistoryResponse;
import athena.fortnite.receipt.Receipt;
import athena.fortnite.shop.StorefrontCatalog;
import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

/**
 * Provides access to the fortnite-public-service
 *
 * @author Vrekt, Armisto
 */
public interface FortnitePublicService {

    String BASE_URL = "https://fortnite-public-service-prod11.ol.epicgames.com/";

    /**
     * Get the store-front catalog.
     *
     * @return a {@link Call} returned by retrofit containing the {@link StorefrontCatalog} if the call was successful.
     */
    @GET("fortnite/api/storefront/v2/catalog")
    Call<StorefrontCatalog> storefrontCatalog();

    /**
     * Retrieve the creative history for the provided {@code accountId}
     *
     * @param accountId the account ID.
     * @return a {@link Call} returned by retrofit containing the {@link CreativeHistoryResponse} if the call was successful.
     */
    @GET("fortnite/api/game/v2/creative/history/{accountId}")
    Call<CreativeHistoryResponse> creativeHistory(@Path("accountId") String accountId);

    /**
     * Retrieve the creative history for the provided {@code accountId}
     *
     * @param accountId the account ID.
     * @param limit     the limit to get.
     * @param olderThan Use Instant UTC. (ISO 8601)
     * @return a {@link Call} returned by retrofit containing the {@link CreativeHistoryResponse} if the call was successful.
     */
    @GET("fortnite/api/game/v2/creative/history/{accountId}")
    Call<CreativeHistoryResponse> creativeHistory(@Path("accountId") String accountId, @Query("limit") int limit, @Query("olderThan") String olderThan);

    /**
     * Retrieve the keychain.
     *
     * @param numKeysDownloaded number of keys downloaded.
     * @return a {@link Call} returned by retrofit containing the {@link List<String>} if the call was successful.
     */
    @GET("fortnite/api/storefront/v2/keychain")
    Call<List<String>> keychain(@Query("numKeysDownloaded") int numKeysDownloaded);

    /**
     * Retrieve a list of receipts. (purchases?)
     *
     * @param accountId the account ID.
     * @return a {@link Call} returned by retrofit containing the {@link List<Receipt>} if the call was successful.
     */
    @GET("fortnite/api/receipts/v1/account/{accountId}/receipts")
    Call<List<Receipt>> receipts(@Path("accountId") String accountId);

    /**
     * TODO:
     *
     * @return json element.
     */
    @GET("fortnite/api/calendar/v1/timeline")
    Call<JsonElement> calendarTimeline();

}
