package athena.shop.price;

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

    public String currencyType() {
        return currencyType;
    }

    public String currencySubType() {
        return currencySubType;
    }

    public int regularPrice() {
        return regularPrice;
    }

    public int dynamicRegularPrice() {
        return dynamicRegularPrice;
    }

    public int finalPrice() {
        return finalPrice;
    }

    public int basePrice() {
        return basePrice;
    }

    public Instant saleExpiration() {
        return saleExpiration;
    }
}
