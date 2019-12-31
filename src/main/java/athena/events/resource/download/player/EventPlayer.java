package athena.events.resource.download.player;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * Represents a player in the {@link athena.events.EventsPublicService}
 */
public final class EventPlayer {

    /**
     * Game ID is "Fortnite" and account ID is that of this player.
     */
    private String gameId, accountId;
    /**
     * Tokens and pending payouts.
     */
    private List<String> tokens, pendingPayouts;
    /**
     * Currently set as objects because unknown.
     */
    private JsonObject teams, pendingPenalties, persistentScores;

    /**
     * @return the game ID, always "Fortnite"
     */
    public String gameId() {
        return gameId;
    }

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
     * @return a {@link JsonObject} of teams, currently unknown what the object looks like so I can't map it.
     * TODO:
     */
    public JsonObject teams() {
        return teams;
    }

    /**
     * @return a {@link JsonObject} of pending penalties, unknown what the object looks like so I can't map it.
     * TODO:
     */
    public JsonObject pendingPenalties() {
        return pendingPenalties;
    }

    /**
     * @return a {@link JsonObject} of persistent scores, unknown what the object looks like so I can't map it.
     * TODO:
     */
    public JsonObject persistentScores() {
        return persistentScores;
    }
}
