package athena.events.resource.download.template.payout;

import athena.events.resource.download.template.payout.rank.EventPayoutRank;

import java.util.List;
import java.util.Optional;

/**
 * Represents a payout table for a {@link athena.events.resource.download.template.EventTemplate}
 */
public final class EventPayoutTable {

    /**
     * "null"
     * "rank"
     * <p>
     * "Hype_S11"
     * "value"
     */
    private String persistentScoreId, scoringType;

    /**
     * List of ranks.
     */
    private List<EventPayoutRank> ranks;

    /**
     * @return persistent score ID, {@code null} sometimes.
     */
    public String persistentScoreId() {
        return persistentScoreId;
    }

    /**
     * @return the scoring type.
     */
    public String scoringType() {
        return scoringType;
    }

    /**
     * @return list of payout ranks.
     */
    public List<EventPayoutRank> ranks() {
        return ranks;
    }

    /**
     * Get a list of {@link EventPayout} for the specified {@code threshold}
     *
     * @param threshold the threshold
     * @return a list of {@link EventPayout} or an empty {@link List} if threshold wasn't found.
     */
    public List<EventPayout> getPayoutsForThreshold(double threshold) {
        final var rank = ranks.stream().filter(eventPayoutRank -> eventPayoutRank.threshold() == threshold).findAny().orElse(null);
        if (rank == null) return List.of();
        return rank.payouts();
    }

    /**
     * Get a {@link EventPayoutRank} that has the specified {@code threshold}
     *
     * @param threshold the threshold
     * @return a {@link Optional} containing the {@link EventPayoutRank} if found
     */
    public Optional<EventPayoutRank> getEventPayoutRankForThreshold(double threshold) {
        return ranks.stream().filter(eventPayoutRank -> eventPayoutRank.threshold() == threshold).findAny();
    }

}
