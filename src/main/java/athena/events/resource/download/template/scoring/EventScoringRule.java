package athena.events.resource.download.template.scoring;

import java.util.List;

/**
 * Represents a scoring rule for {@link athena.events.resource.download.template.EventTemplate}
 */
public final class EventScoringRule {

    /**
     * The tracked stat, ex: PLACEMENT_STAT_INDEX
     * The match rule, ex: 'lte'
     */
    private String trackedStat, matchRule;
    /**
     * List of reward tiers.
     */
    private List<EventRewardTier> rewardTiers;

    /**
     * @return the tracked stat
     */
    public String trackedStat() {
        return trackedStat;
    }

    /**
     * @return the match rule
     */
    public String matchRule() {
        return matchRule;
    }

    /**
     * @return list of reward tiers.
     */
    public List<EventRewardTier> rewardTiers() {
        return rewardTiers;
    }
}
