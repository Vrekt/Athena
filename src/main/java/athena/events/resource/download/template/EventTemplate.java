package athena.events.resource.download.template;

import athena.events.resource.download.template.payout.EventPayoutTable;
import athena.events.resource.download.template.scoring.EventScoringRule;
import athena.events.resource.download.template.tiebreaker.EventTiebreakerFormula;

import java.util.List;
import java.util.Optional;

/**
 * Represents the template for a {@link athena.events.resource.download.event.FortniteEvent}
 */
public final class EventTemplate {

    /**
     * "Fortnite",
     * "EventData_S11_FNCS_Finals_NAE_Finals",
     * "Playlist_ShowdownTournament_Squads",
     * "Hype_S11"
     */
    private String gameId, eventTemplateId, playlistId, persistentScoreId;
    /**
     * The match cap for this event.
     */
    private int matchCap;
    /**
     * If true each player apart of the team has their own score?
     */
    private boolean useIndividualScores;
    /**
     * List of live session attributes? ex: "MATCHSTARTTIME"
     */
    private List<String> liveSessionAttributes;
    /**
     * List of scoring rules.
     */
    private List<EventScoringRule> scoringRules;
    /**
     * The tiebreaker formula for this event.
     */
    private EventTiebreakerFormula tiebreakerFormula;

    /**
     * A list of payout tables.
     */
    private List<EventPayoutTable> payoutTable;

    /**
     * @return "Fortnite"
     */
    public String gameId() {
        return gameId;
    }

    /**
     * @return the event template ID.
     */
    public String eventTemplateId() {
        return eventTemplateId;
    }

    /**
     * @return the playlist ID.
     */
    public String playlistId() {
        return playlistId;
    }

    /**
     * @return the persistent score ID.
     */
    public String persistentScoreId() {
        return persistentScoreId;
    }

    /**
     * @return the match cap
     */
    public int matchCap() {
        return matchCap;
    }

    /**
     * @return {@code true} each player apart of the team has their own score?
     */
    public boolean useIndividualScores() {
        return useIndividualScores;
    }

    /**
     * @return list of session attributes
     */
    public List<String> liveSessionAttributes() {
        return liveSessionAttributes;
    }

    /**
     * @return list of scoring rules.
     */
    public List<EventScoringRule> scoringRules() {
        return scoringRules;
    }

    /**
     * @return the tiebreaker formula for this template.
     */
    public EventTiebreakerFormula tiebreakerFormula() {
        return tiebreakerFormula;
    }

    /**
     * @return a list of payout tables.
     */
    public List<EventPayoutTable> payoutTable() {
        return payoutTable;
    }

    /**
     * Find the {@link EventScoringRule} that has the {@code trackedStat}
     *
     * @param trackedStat the tracked stat
     * @return a {@link Optional} containing the {@link EventScoringRule} if found
     */
    public Optional<EventScoringRule> getScoringRule(String trackedStat) {
        return scoringRules.stream().filter(eventScoringRule -> eventScoringRule.trackedStat().equalsIgnoreCase(trackedStat)).findAny();
    }

}
