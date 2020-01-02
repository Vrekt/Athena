package athena.shop.catalog;

import athena.shop.gift.CatalogGiftInfo;
import athena.shop.grant.CatalogEntryItemGrant;
import athena.shop.price.CatalogPrice;
import athena.shop.requirement.CatalogEntryRequirement;

import java.util.List;
import java.util.Optional;

/**
 * An entry within the {@link athena.shop.Storefront}
 */
public final class CatalogEntry {

    /**
     * Development name, offer ID and the offer type.
     */
    private String devName, offerId, offerType;
    /**
     * Daily, weekly and monthly limit.
     * others are ?? (usually 0)
     */
    private int dailyLimit, weeklyLimit, monthlyLimit, sortPriority, catalogGroupPriority;
    /**
     * {@code true} if this entry is refundable?
     */
    private boolean refundable;

    /**
     * List of categories, ex "Daily 4"
     * App store IDs
     */
    private List<String> categories, appStoreId;
    /**
     * List of prices for this entry.
     */
    private List<CatalogPrice> prices;
    /**
     * Requirements for this entry.
     */
    private List<CatalogEntryRequirement> requirements;
    /**
     * Gifting info for this entry.
     */
    private CatalogGiftInfo giftInfo;
    /**
     * List of item grants (what this entry gives)
     */
    private List<CatalogEntryItemGrant> itemGrants;

    /**
     * Check if the provided {@code requirementType} is required.
     *
     * @param requirementType the requirement type.
     * @return {@code true} if the {@code requirementType} is present.
     */
    public boolean isRequired(String requirementType) {
        return requirements.stream().anyMatch(catalogEntryRequirement -> catalogEntryRequirement.requirementType().equalsIgnoreCase(requirementType));
    }

    /**
     * Check if the provided {@code requirementType} and {@code requirementId} is required.
     *
     * @param requirementType the requirement type.
     * @param requirementId   the requirement ID.
     * @return {@code true} if the {@code requirementType} and {@code requirementId} is present.
     */
    public boolean isRequired(String requirementType, String requirementId) {
        return requirements
                .stream()
                .anyMatch(catalogEntryRequirement -> catalogEntryRequirement.requirementType().equalsIgnoreCase(requirementType)
                        && catalogEntryRequirement.requiredId().equalsIgnoreCase(requirementId));
    }

    /**
     * Check if a {@link CatalogEntryRequirement} is present with the provided {@code requirementType}, {@code requirementId}, {@code quantity}
     *
     * @param requirementType the requirement type.
     * @param requirementId   the requirement ID.
     * @param quantity        the quantity
     * @return {@code true} if all requirements are present and met.
     */
    public boolean hasRequirements(String requirementType, String requirementId, int quantity) {
        final var entry = getCatalogEntryRequirement(requirementType, requirementId).orElse(null);
        if (entry == null) return false;
        return quantity >= entry.minQuantity();
    }

    /**
     * Get entry catalog requirement.
     *
     * @param requirementType the requirement type.
     * @return a {@link Optional} that will contain the {@link CatalogEntryRequirement} if found
     */
    public Optional<CatalogEntryRequirement> getCatalogEntryRequirement(String requirementType) {
        return requirements.stream().filter(catalogEntryRequirement -> catalogEntryRequirement.requirementType().equalsIgnoreCase(requirementType)).findAny();
    }

    /**
     * Get entry catalog requirement.
     *
     * @param requirementType the requirement type.
     * @param requirementId   the requirement ID.
     * @return a {@link Optional} that will contain the {@link CatalogEntryRequirement} if found
     */
    public Optional<CatalogEntryRequirement> getCatalogEntryRequirement(String requirementType, String requirementId) {
        return requirements
                .stream()
                .filter(catalogEntryRequirement -> catalogEntryRequirement.requirementType().equalsIgnoreCase(requirementType)
                        && catalogEntryRequirement.requiredId().equalsIgnoreCase(requirementId)).findAny();
    }

    /**
     * @return the first price given in the list.
     */
    public CatalogPrice getPrimaryPrice() {
        return prices.get(0);
    }

    /**
     * Get the price for the {@code currencyType}
     *
     * @param currencyType the currencyType, ex: "MtxCurrency"
     * @return a {@link Optional} containing the {@link CatalogPrice} if found
     */
    public Optional<CatalogPrice> getPriceForCurrencyType(String currencyType) {
        return prices.stream().filter(catalogPrice -> catalogPrice.currencyType().equalsIgnoreCase(currencyType)).findAny();
    }

    /**
     * @return {@code true} if gifting is enabled for this entry.
     */
    public boolean isGiftingEnabled() {
        return giftInfo != null && giftInfo.isEnabled();
    }

    /**
     * @return the development name
     */
    public String devName() {
        return devName;
    }

    /**
     * @return the offer ID.
     */
    public String offerId() {
        return offerId;
    }

    /**
     * @return the offer type.
     */
    public String offerType() {
        return offerType;
    }

    /**
     * @return daily limit for this entry.
     */
    public int dailyLimit() {
        return dailyLimit;
    }

    /**
     * @return weekly limit for this entry.
     */
    public int weeklyLimit() {
        return weeklyLimit;
    }

    /**
     * @return monthly limit for this entry.
     */
    public int monthlyLimit() {
        return monthlyLimit;
    }

    /**
     * @return the sort priority.
     */
    public int sortPriority() {
        return sortPriority;
    }

    /**
     * @return catalog group priority.
     */
    public int catalogGroupPriority() {
        return catalogGroupPriority;
    }

    /**
     * @return {@code true} if this entry is refundable.
     */
    public boolean refundable() {
        return refundable;
    }

    /**
     * @return list of categories
     */
    public List<String> categories() {
        return categories;
    }

    /**
     * @return list of app store IDs, (can be empty strings)
     */
    public List<String> appStoreId() {
        return appStoreId;
    }

    /**
     * @return list of prices.
     */
    public List<CatalogPrice> prices() {
        return prices;
    }

    /**
     * @return list of requirements.
     */
    public List<CatalogEntryRequirement> requirements() {
        return requirements;
    }

    /**
     * @return gifting info for this entry.
     */
    public CatalogGiftInfo giftInfo() {
        return giftInfo;
    }

    /**
     * @return list of item grants this entry gives.
     */
    public List<CatalogEntryItemGrant> itemGrants() {
        return itemGrants;
    }
}
