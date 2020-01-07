package athena.fortnite.receipt;

import athena.fortnite.service.FortnitePublicService;

/**
 * Represents a receipt from "fortnite/api/receipts/v1/account/{accountId}/receipts" {@link FortnitePublicService}
 */
public final class Receipt {

    private String appStore, appStoreId, receiptId, receiptInfo;

    /**
     * @return the app store, ex: "EpicPurchasingService"
     */
    public String appStore() {
        return appStore;
    }

    /**
     * @return the app store ID.
     */
    public String appStoreId() {
        return appStoreId;
    }

    /**
     * @return the receipt ID.
     */
    public String receiptId() {
        return receiptId;
    }

    /**
     * @return the receipt info, ex: "ENTITLEMENT"
     */
    public String receiptInfo() {
        return receiptInfo;
    }
}
