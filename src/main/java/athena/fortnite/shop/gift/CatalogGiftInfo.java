package athena.fortnite.shop.gift;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Gifting info for a catalog entry.
 */
public final class CatalogGiftInfo {

    /**
     * If gifting is enabled for this catalog entry.
     */
    @SerializedName("bIsEnabled")
    private boolean isEnabled;

    /**
     * ??
     */
    private String forcedGiftBoxTemplateId;

    /**
     * ??
     */
    private List<String> purchaseRequirements, giftRecordIds;

    /**
     * @return {@code true} if gifting is enabled.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * @return ??
     */
    public String forcedGiftBoxTemplateId() {
        return forcedGiftBoxTemplateId;
    }

    /**
     * @return ??
     */
    public List<String> purchaseRequirements() {
        return purchaseRequirements;
    }

    /**
     * @return ??
     */
    public List<String> giftRecordIds() {
        return giftRecordIds;
    }
}
