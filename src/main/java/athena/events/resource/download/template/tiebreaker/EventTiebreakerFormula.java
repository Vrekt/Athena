package athena.events.resource.download.template.tiebreaker;

import java.util.List;
import java.util.Optional;

/**
 * Tie-breaker formula for a {@link athena.events.resource.download.template.EventTemplate}
 */
public final class EventTiebreakerFormula {

    /**
     * Base bits.
     */
    private int basePointsBits;
    /**
     * List of components for this formula.
     */
    private List<EventTiebreakerFormulaComponent> components;

    /**
     * @return base point bits.
     */
    public int basePointsBits() {
        return basePointsBits;
    }

    /**
     * @return a list of components for this event.
     */
    public List<EventTiebreakerFormulaComponent> components() {
        return components;
    }

    /**
     * Get a {@link EventTiebreakerFormulaComponent} by the tracked stat name.
     *
     * @param trackedStat the tracked stat.
     * @return a {@link Optional} containing the {@link EventTiebreakerFormulaComponent} if found.
     */
    public Optional<EventTiebreakerFormulaComponent> get(String trackedStat) {
        return components.stream().filter(component -> component.trackedStat().equalsIgnoreCase(trackedStat)).findAny();
    }

}
