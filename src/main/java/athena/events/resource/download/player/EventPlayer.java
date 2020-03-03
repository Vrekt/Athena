package athena.events.resource.download.player;

import athena.events.service.EventsPublicService;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Represents a player in the {@link EventsPublicService}
 */
public final class EventPlayer {

    /**
     * account ID is that of this player.
     */
    private String accountId;
    /**
     * Tokens and pending payouts.
     */
    private List<String> tokens, pendingPayouts;

    /**
     * A map of teams.
     */
    private Map<String, List<String>> teams;

    /**
     * A map of scores.
     */
    private Map<String, Integer> persistentScores;

    /**
     * Currently set as objects because unknown.
     */
    private JsonObject pendingPenalties;

    /**
     * @return the account ID of this player.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return a list of tokens.
     */
    public List<String> tokens() {
        return tokens;
    }


    /**
     * @return a list of pending payouts.
     */
    public List<String> pendingPayouts() {
        return pendingPayouts;
    }

    /**
     * Get a list of account IDs who participated in the team.
     *
     * @param eventId       the event ID.
     * @param eventWindowId the event window ID.
     * @return a list of account IDs
     */
    public List<String> getTeamAccountIds(String eventId, String eventWindowId) {
        return getTeamAccountIds(eventId + ":" + eventWindowId);
    }

    /**
     * Get a list of account IDs who participated in the team.
     *
     * @param eventIdAndWindow the event ID and window, must be formatted like so: eventId:eventWindowId
     * @return a list of account IDs
     */
    public List<String> getTeamAccountIds(String eventIdAndWindow) {
        return teams.getOrDefault(eventIdAndWindow, List.of());
    }

    /**
     * @return a {@link JsonObject} of pending penalties, unknown what the object looks like so I can't map it.
     * TODO:
     */
    public JsonObject pendingPenalties() {
        return pendingPenalties;
    }

    /**
     * Get a score. For example getting the score for the current hype would just be "Hype"
     * Getting a score for a previous season score would be "Hype_S<season number>" for example:
     * HS10 - hype season 10
     *
     * @param scoreName the score name
     * @return the score or -1 if not found.
     */
    public int getScore(String scoreName) {
        return persistentScores.getOrDefault(scoreName, -1);
    }

}
