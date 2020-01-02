package athena.shop.requirement;

/**
 * Represents requirements for the catalog entry.
 */
public final class CatalogEntryRequirement {

    private String requirementType, requiredId;
    private int minQuantity;

    public String requirementType() {
        return requirementType;
    }

    public String requiredId() {
        return requiredId;
    }

    public int minQuantity() {
        return minQuantity;
    }
}
