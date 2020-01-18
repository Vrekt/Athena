package athena.fortnite;

import athena.context.DefaultAthenaContext;
import athena.fortnite.service.FortnitePublicService;
import athena.fortnite.shop.Storefront;
import athena.fortnite.shop.StorefrontCatalog;
import athena.util.request.Requests;

/**
 * Provides easy access to various things within {@link athena.fortnite.service.FortnitePublicService}
 */
public final class Fortnite {

    /**
     * The service
     */
    private final FortnitePublicService service;

    public Fortnite(DefaultAthenaContext context) {
        this.service = context.fortnite();
    }

    /**
     * Retrieve the entire catalog.
     *
     * @return the entire catalog.
     */
    public StorefrontCatalog storefrontCatalog() {
        final var call = service.storefrontCatalog();
        return Requests.executeCall(call);
    }

    /**
     * @return the daily store-front.
     */
    public Storefront dailyStorefront() {
        return storefrontCatalog().get("BRDailyStorefront");
    }

    /**
     * @return the weekly store-front.
     */
    public Storefront weeklyStorefront() {
        return storefrontCatalog().get("BRWeeklyStorefront");
    }

    /**
     * Get a specific storefront by name.
     *
     * @param name the name.
     * @return the storefront.
     */
    public Storefront getStorefront(String name) {
        return storefrontCatalog().get(name);
    }

}
