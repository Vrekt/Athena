package athena.events.resource.download.template.scoring;

/**
 * Represents a singular reward tier for a {@link EventScoringRule}
 * <p>
 * "trackedStat": "PLACEMENT_STAT_INDEX",
 * "matchRule": "lte",
 * "rewardTiers": [
 * {
 * "keyValue": 1,
 * "pointsEarned": 3,
 * "multiplicative": false
 * },
 */
public final class EventRewardTier {
    /**
     * key value I think is what their earned, for example "5" for placing 5th.
     * Then the points earned for placing 5th would be {@code pointsEarned}, 3 points for 5th?
     */
    private int keyValue, pointsEarned;
    /**
     * Don't know really
     */
    private boolean multiplicative;

    /**
     * @return the key value
     */
    public int keyValue() {
        return keyValue;
    }

    /**
     * @return points earned for achieving the key value?
     */
    public int pointsEarned() {
        return pointsEarned;
    }

    /**
     * @return {@code true} if multiplicative
     */
    public boolean multiplicative() {
        return multiplicative;
    }
}
