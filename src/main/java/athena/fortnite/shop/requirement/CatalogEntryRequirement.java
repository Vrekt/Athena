package athena.fortnite.shop.requirement;

/**
 * Represents requirements for the catalog entry.
 */
public final class CatalogEntryRequirement {

    /**
     * Requirement type and the ID.
     */
    private String requirementType, requiredId;
    /**
     * Minimal quantity required.
     */
    private int minQuantity;

    /**
     * @return the requirement-type.
     */
    public String requirementType() {
        return requirementType;
    }

    /**
     * @return the required ID.
     */
    public String requiredId() {
        return requiredId;
    }

    /**
     * @return the minimum quantity.
     */
    public int minQuantity() {
        return minQuantity;
    }
}
