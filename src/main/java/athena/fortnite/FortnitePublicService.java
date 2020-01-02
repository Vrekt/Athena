package athena.fortnite;

import athena.shop.StorefrontCatalog;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Provides access to the fortnite-public-service
 */
public interface FortnitePublicService {

    String BASE_URL = "https://fortnite-public-service-prod11.ol.epicgames.com/";

    /**
     * Get the store-front catalog.
     */
    @GET("fortnite/api/storefront/v2/catalog")
    Call<StorefrontCatalog> storefrontCatalog();

}
