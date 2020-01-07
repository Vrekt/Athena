package athena.stats.resource;

import athena.stats.resource.type.StatisticType;
import athena.types.Input;
import io.gsonfire.annotations.PostDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents an unfiltered statistic, basically all statistics grouped together.
 */
public final class UnfilteredStatistic {

    /**
     * The start and end time of this statistic.
     */
    private long startTime, endTime;
    /**
     * The statistics that GSON will map to.
     */
    private Map<String, Long> stats;

    /**
     * The post types that will be collected from the resulting {@code stats}
     */
    private final Map<StatisticType, Long> statsByType = new HashMap<>();
    private final List<StatisticValue> statisticValues = new ArrayList<>();

    /**
     * The account ID of who these stats belong to.
     */
    private String accountId;

    /**
     * Fill the {@code statsByType} and {@code statisticValues}
     */
    @PostDeserialize
    private void postDeserialize() {
        stats.forEach((key, value) -> {
            final var statisticValue = new StatisticValue(key, value);
            statisticValues.add(statisticValue);
            statsByType.put(statisticValue.type(), statsByType.getOrDefault(statisticValue.type(), 0L) + value);
        });
    }

    /**
     * Filter statistics by the provided {@code input} type.
     *
     * @param input the input type
     * @return a new {@link Statistic} containing only stats from the provided {@code input}
     */
    public Statistic filterByInput(Input input) {
        final var groups = statisticValues.stream().filter(group -> group.input() == input).collect(Collectors.toSet());
        final var map = new HashMap<StatisticType, Long>();
        for (final var type : StatisticType.values()) {
            final var withType = groups.stream().filter(value -> value.type() == type).collect(Collectors.toSet());
            map.put(type, withType.stream().mapToLong(StatisticValue::value).sum());
        }
        return new Statistic(map);
    }

    /**
     * Filter statistics by the provided {@code playlist}.
     * Playlists must contain the "playlist_" part, for example "playlist_defaultsolo";
     *
     * @param playlist the playlist
     * @return a new {@link Statistic} containing only stats from the provided {@code playlist}
     */
    public Statistic filterByPlaylist(String playlist) {
        final var groups = statisticValues.stream().filter(group -> group.playlist().equalsIgnoreCase(playlist)).collect(Collectors.toSet());
        final var map = new HashMap<StatisticType, Long>();
        for (final var type : StatisticType.values()) {
            final var withType = groups.stream().filter(value -> value.type() == type).collect(Collectors.toSet());
            map.put(type, withType.stream().mapToLong(StatisticValue::value).sum());
        }
        return new Statistic(map);
    }

    /**
     * Filter statistics by the provided {@code input} and {@code playlist}
     * Playlists must contain the "playlist_" part, for example "playlist_defaultsolo";
     *
     * @param input    the input type
     * @param playlist the playlist
     * @return a new {@link Statistic} containing only stats from the provided {@code playlist} and {@code input}
     */
    public Statistic filterByInputAndPlaylist(Input input, String playlist) {
        final var groups = statisticValues.stream().filter(group -> group.input() == input && group.playlist().equalsIgnoreCase(playlist)).collect(Collectors.toSet());
        final var map = new HashMap<StatisticType, Long>();
        for (final var type : StatisticType.values()) {
            final var withType = groups.stream().filter(value -> value.type() == type).collect(Collectors.toSet());
            map.put(type, withType.stream().mapToLong(StatisticValue::value).sum());
        }
        return new Statistic(map);
    }

    /**
     * @return the startTime.
     */
    public long startTime() {
        return startTime;
    }

    /**
     * @return the endTime.
     */
    public long endTime() {
        return endTime;
    }

    /**
     * @return all statistics unfiltered.
     */
    public Map<String, Long> rawStatistics() {
        return stats;
    }

    /**
     * @return statistics collected by type.
     */
    public Map<StatisticType, Long> statisticsByType() {
        return statsByType;
    }

    /**
     * @return a list of {@link StatisticValue}
     */
    public List<StatisticValue> statisticValues() {
        return statisticValues;
    }

    /**
     * @return The account ID of who these stats belong to.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return total number of players outlived all together.
     */
    public int playersOutlived() {
        return statsByType.get(StatisticType.PLAYERS_OUTLIVED).intValue();
    }

    /**
     * @return total number of kills all together.
     */
    public int kills() {
        return statsByType.get(StatisticType.KILLS).intValue();
    }

    /**
     * @return total number of wins all together.
     */
    public int wins() {
        return statsByType.get(StatisticType.PLACED_TOP1).intValue();
    }

    /**
     * @return total number of 3rd places all together.
     */
    public int timesPlaced3rd() {
        return statsByType.get(StatisticType.PLACED_TOP3).intValue();
    }

    /**
     * @return total number of 5th places all together.
     */
    public int timesPlaced5th() {
        return statsByType.get(StatisticType.PLACED_TOP5).intValue();
    }

    /**
     * @return total number of 6th places all together.
     */
    public int timesPlaced6th() {
        return statsByType.get(StatisticType.PLACED_TOP6).intValue();
    }

    /**
     * @return total number of 10th places all together.
     */
    public int timesPlaced10th() {
        return statsByType.get(StatisticType.PLACED_TOP10).intValue();
    }

    /**
     * @return total number of 25th places all together.
     */
    public int timesPlaced25th() {
        return statsByType.get(StatisticType.PLACED_TOP25).intValue();
    }

    /**
     * @return total number of minutes played all together
     */
    public int minutesPlayed() {
        return statsByType.get(StatisticType.MINUTES_PLAYED).intValue();
    }

    /**
     * @return total number of matches played all together
     */
    public int matchesPlayed() {
        return statsByType.get(StatisticType.MATCHES_PLAYED).intValue();
    }

    /**
     * @return total score all together
     */
    public int score() {
        return statsByType.get(StatisticType.SCORE).intValue();
    }

    /**
     * @return current BP level.
     */
    public int battlePassLevel() {
        return statsByType.get(StatisticType.BP_LEVEL).intValue();
    }
}
