package athena.events.resource.download.template.payout;

/**
 * Represents an event payout for a {@link athena.events.resource.download.template.payout.rank.EventPayoutRank}
 * {
 * "rewardType": "token",
 * "rewardMode": "rolling",
 * "value": "ARENA_S11_Division8",
 * "quantity": 1
 * },
 */
public final class EventPayout {

    /**
     * "ecomm"
     * "standard"
     * "USD"
     * <p>
     * "persistentScore"
     * "standard"
     * "FNCS_S11_Points_NAE"
     */
    private String rewardType, rewardMode, value;
    /**
     * Quantity of this payout.
     */
    private int quantity;

    /**
     * @return the reward type, ex: "ecomm"
     */
    public String rewardType() {
        return rewardType;
    }

    /**
     * @return the reward mode, ex: "standard"
     */
    public String rewardMode() {
        return rewardMode;
    }

    /**
     * @return the value, ex: "USD"
     */
    public String value() {
        return value;
    }

    /**
     * @return the quantity, $$$ payout or points
     */
    public int quantity() {
        return quantity;
    }
}
