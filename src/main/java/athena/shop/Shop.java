package athena.shop;

import athena.fortnite.FortnitePublicService;
import athena.util.request.Requests;

/**
 * Provides easy access to {@link athena.fortnite.FortnitePublicService} shop endpoint.
 */
public final class Shop {

    private final FortnitePublicService fortnitePublicService;

    public Shop(FortnitePublicService fortnitePublicService) {
        this.fortnitePublicService = fortnitePublicService;
    }

    /**
     * @return the service
     */
    public FortnitePublicService service() {
        return fortnitePublicService;
    }

    /**
     * Retrieve the entire catalog.
     *
     * @return the entire catalog.
     */
    public StorefrontCatalog catalog() {
        final var call = fortnitePublicService.storefrontCatalog();
        return Requests.executeCall("Failed to get daily storefront.", call);
    }

    /**
     * @return the daily store-front.
     */
    public Storefront daily() {
        return catalog().get("BRDailyStorefront");
    }

    /**
     * @return the weekly store-front.
     */
    public Storefront weekly() {
        return catalog().get("BRWeeklyStorefront");
    }

}
