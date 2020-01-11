package athena.fortnite.shop.price;

import java.time.Instant;

/**
 * Represents the price for a catalog/item.
 */
public final class CatalogPrice {

    /**
     * Currency types and subtypes, ex: "MtxCurrency", ""
     */
    private String currencyType, currencySubType;
    /**
     * Various prices of the item.
     */
    private int regularPrice, dynamicRegularPrice, finalPrice, basePrice;
    /**
     * When this sale expires.
     */
    private Instant saleExpiration;

    /**
     * @return the currency type.
     */
    public String currencyType() {
        return currencyType;
    }

    /**
     * @return the currency sub-type.
     */
    public String currencySubType() {
        return currencySubType;
    }

    /**
     * @return the regular price.
     */
    public int regularPrice() {
        return regularPrice;
    }

    /**
     * @return the dynamic regular price
     */
    public int dynamicRegularPrice() {
        return dynamicRegularPrice;
    }

    /**
     * @return the final price.
     */
    public int finalPrice() {
        return finalPrice;
    }

    /**
     * @return the base price.
     */
    public int basePrice() {
        return basePrice;
    }

    /**
     * @return when the sale expires.
     */
    public Instant saleExpiration() {
        return saleExpiration;
    }
}
