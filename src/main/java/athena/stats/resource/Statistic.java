package athena.stats.resource;

import athena.stats.resource.type.StatisticType;

import java.time.Instant;
import java.util.Map;

/**
 * Represents a statistic
 */
public final class Statistic {

    /**
     * All values
     */
    private final int playersOutlived, kills, timesPlaced1st, timesPlaced3rd, timesPlaced5th, timesPlaced6th, timesPlaced10th, timesPlaced25th, minutesPlayed, matchesPlayed, score, battlePassLevel;
    private final Instant lastModified;

    Statistic(Map<StatisticType, Long> values) {
        lastModified = Instant.ofEpochSecond(values.get(StatisticType.LAST_MODIFIED));
        playersOutlived = values.get(StatisticType.PLAYERS_OUTLIVED).intValue();
        timesPlaced1st = values.get(StatisticType.PLACED_TOP1).intValue();
        timesPlaced3rd = values.get(StatisticType.PLACED_TOP3).intValue();
        timesPlaced5th = values.get(StatisticType.PLACED_TOP5).intValue();
        timesPlaced6th = values.get(StatisticType.PLACED_TOP6).intValue();
        timesPlaced10th = values.get(StatisticType.PLACED_TOP10).intValue();
        timesPlaced25th = values.get(StatisticType.PLACED_TOP25).intValue();
        minutesPlayed = values.get(StatisticType.MINUTES_PLAYED).intValue();
        matchesPlayed = values.get(StatisticType.MATCHES_PLAYED).intValue();
        battlePassLevel = values.get(StatisticType.BP_LEVEL).intValue();
        score = values.get(StatisticType.SCORE).intValue();
        kills = values.get(StatisticType.KILLS).intValue();
        values.clear();
    }

    /**
     * @return total number of players outlived.
     */
    public int playersOutlived() {
        return playersOutlived;
    }

    /**
     * @return total number of kills.
     */
    public int kills() {
        return kills;
    }

    /**
     * @return total number of wins.
     */
    public int wins() {
        return timesPlaced1st;
    }

    /**
     * @return times placed 3rd.
     */
    public int timesPlaced3rd() {
        return timesPlaced3rd;
    }

    /**
     * @return times placed 5th.
     */
    public int timesPlaced5th() {
        return timesPlaced5th;
    }

    /**
     * @return times placed 6th.
     */
    public int timesPlaced6th() {
        return timesPlaced6th;
    }

    /**
     * @return times placed 10th.
     */
    public int timesPlaced10th() {
        return timesPlaced10th;
    }

    /**
     * @return times placed 25th.
     */
    public int timesPlaced25th() {
        return timesPlaced25th;
    }

    /**
     * @return total number of minutes played.
     */
    public int minutesPlayed() {
        return minutesPlayed;
    }

    /**
     * @return total matches played.
     */
    public int matchesPlayed() {
        return matchesPlayed;
    }

    /**
     * @return total score
     */
    public int score() {
        return score;
    }

    /**
     * @return the seasons current battle pass level.
     */
    public int battlePassLevel() {
        return battlePassLevel;
    }

    /**
     * @return when this statistic was last modified.
     * This value will only be accurate when filtered by input AND playlist, otherwise all values are summed together.
     */
    public Instant lastModified() {
        return lastModified;
    }

}
