package athena.events.resource.download.template.tiebreaker;

/**
 * A singular component for a {@link EventTiebreakerFormula}
 */
public final class EventTiebreakerFormulaComponent {

    /**
     * The tracked stat, ex: "VICTORY_ROYALE_STAT"
     * The aggregation, "avg", "sum"
     */
    private String trackedStat, aggregation;
    /**
     * bits is X, multiplier can be null/0.
     */
    private int bits, multiplier;

    /**
     * @return the tracked stat.
     */
    public String trackedStat() {
        return trackedStat;
    }

    /**
     * @return the aggregation
     */
    public String aggregation() {
        return aggregation;
    }

    /**
     * @return the bits
     */
    public int bits() {
        return bits;
    }

    /**
     * @return the multiplier.
     */
    public int multiplier() {
        return multiplier;
    }
}
