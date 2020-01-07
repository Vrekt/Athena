package athena.fortnite.creative.matchmaking;

import com.google.gson.annotations.SerializedName;

/**
 * Represents matchmaking data for a {@link athena.fortnite.creative.data.CreativeMetadata entry
 */
public final class CreativeMatchmaking {

    /**
     * "Spectate",
     * "off"
     */
    private String joinInProgressType, mmsType;
    /**
     * Relating to teams and player count, etc.
     */
    private int playersPerTeam, maximumNumberOfPlayers, playerCount, numberOfTeams, minimumNumberOfPlayers;
    /**
     * If join in progress is allowed.
     */
    @SerializedName("bAllowJoinInProgress")
    private boolean allowJoinInProgress;

    /**
     * @return the join in progress type, eg "Spectate"
     */
    public String joinInProgressType() {
        return joinInProgressType;
    }

    /**
     * @return mms type ? "off"
     */
    public String mmsType() {
        return mmsType;
    }

    /**
     * @return players per team.
     */
    public int playersPerTeam() {
        return playersPerTeam;
    }

    /**
     * @return max number of players
     */
    public int maximumNumberOfPlayers() {
        return maximumNumberOfPlayers;
    }

    /**
     * @return the player count
     */
    public int playerCount() {
        return playerCount;
    }

    /**
     * @return number of teams
     */
    public int numberOfTeams() {
        return numberOfTeams;
    }

    /**
     * @return min number of players
     */
    public int minimumNumberOfPlayers() {
        return minimumNumberOfPlayers;
    }

    /**
     * @return {@code true} if join in progress is allowed.
     */
    public boolean allowJoinInProgress() {
        return allowJoinInProgress;
    }
}
