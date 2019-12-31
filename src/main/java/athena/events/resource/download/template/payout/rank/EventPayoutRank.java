package athena.events.resource.download.template.payout.rank;

import athena.events.resource.download.template.payout.EventPayout;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a singular payout rank for {@link athena.events.resource.download.template.payout.EventPayoutTable}
 */
public final class EventPayoutRank {

    /**
     * The threshold for this payout rank.
     */
    private double threshold;

    /**
     * List of payouts.
     */
    private List<EventPayout> payouts;

    /**
     * @return the threshold.
     */
    public double threshold() {
        return threshold;
    }

    /**
     * @return the payouts.
     */
    public List<EventPayout> payouts() {
        return payouts;
    }

    /**
     * Get a list of payouts with the specified {@code rewardType}
     *
     * @param rewardType the reward type
     * @return a list of {@link EventPayout}s
     */
    public List<EventPayout> getPayoutsForRewardType(String rewardType) {
        return payouts.stream().filter(payout -> payout.rewardType().equalsIgnoreCase(rewardType)).collect(Collectors.toList());
    }

    /**
     * Get a list of payouts with the specified {@code rewardMode}
     *
     * @param rewardMode the reward mode
     * @return a list of {@link EventPayout}s
     */
    public List<EventPayout> getPayoutsForRewardMode(String rewardMode) {
        return payouts.stream().filter(payout -> payout.rewardMode().equalsIgnoreCase(rewardMode)).collect(Collectors.toList());
    }

}
