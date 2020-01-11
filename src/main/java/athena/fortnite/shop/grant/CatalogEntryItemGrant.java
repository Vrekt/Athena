package athena.shop.grant;

/**
 * Represents an item grant for an entry.
 */
public final class CatalogEntryItemGrant {

    /**
     * The template ID.
     */
    private String templateId;
    /**
     * The quantity to give.
     */
    private int quantity;

    /**
     * @return template ID.
     */
    public String templateId() {
        return templateId;
    }

    /**
     * @return The quantity to grant.
     */
    public int quantity() {
        return quantity;
    }
}
