package athena.shop;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public final class StorefrontCatalog {

    /**
     * Refresh interval and time to purchase items?
     */
    private int refreshIntervalHrs, dailyPurchaseHrs;

    /**
     * When the catalog expires.
     */
    private Instant expiration;

    /**
     * A list of storefronts.
     */
    private List<Storefront> storefronts;

    /**
     * Get a store front.
     *
     * @param name the name of the store front.
     * @return the {@link Storefront} or {@code null} if not found.
     */
    public Storefront get(String name) {
        return storefronts.stream().filter(storefront -> storefront.name().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    /**
     * Get a store front.
     *
     * @param name the name of the store front.
     * @return a {@link Optional} containing the {@link Storefront} if found.
     */
    public Optional<Storefront> getOptional(String name) {
        return Optional.ofNullable(get(name));
    }

}
