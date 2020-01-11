package athena.fortnite.shop;

import athena.fortnite.shop.catalog.CatalogEntry;

import java.util.List;
import java.util.Optional;

/**
 * Represents a singular storefront.
 */
public final class Storefront {

    /**
     * Name of this storefront.
     */
    private String name;
    /**
     * List of entries.
     */
    private List<CatalogEntry> catalogEntries;

    /**
     * Get an entry via the {@code devName}
     *
     * @param devName the dev name.
     * @return a {@link Optional} containing the {@link CatalogEntry} if found.
     */
    public Optional<CatalogEntry> getEntryByDevName(String devName) {
        return catalogEntries.stream().filter(catalogEntry -> catalogEntry.devName().equalsIgnoreCase(devName)).findAny();
    }

    /**
     * Get an entry via the {@code offerId}
     *
     * @param offerId the offer ID.
     * @return a {@link Optional} containing the {@link CatalogEntry} if found.
     */
    public Optional<CatalogEntry> getEntryByOfferId(String offerId) {
        return catalogEntries.stream().filter(catalogEntry -> catalogEntry.offerId().equalsIgnoreCase(offerId)).findAny();
    }

    /**
     * @return the name of this store-front.
     */
    public String name() {
        return name;
    }

    /**
     * @return list of catalog entries.
     */
    public List<CatalogEntry> catalogEntries() {
        return catalogEntries;
    }
}
