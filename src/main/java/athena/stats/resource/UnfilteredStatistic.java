package athena.stats.resource;

import athena.types.Input;
import athena.stats.resource.type.StatisticType;
import athena.util.json.BasicJsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents an unfiltered statistic, basically all statistics grouped together.
 */
public final class UnfilteredStatistic {

    /**
     * The adapter for this object
     */
    public static final BasicJsonDeserializer<UnfilteredStatistic> ADAPTER = json -> {
        // get the stats object.
        final var object = json.getAsJsonObject().getAsJsonObject("stats");
        // a map of all statistics.
        final var all = new HashMap<StatisticType, Long>();
        // initialize map with all types.
        for (final var type : StatisticType.values()) all.put(type, 0L);
        // map object to a statistic value object.
        final var values = object.entrySet().stream().map(entry -> new StatisticValue(entry.getKey(), entry.getValue())).collect(Collectors.toSet());
        // collect all stats
        values.forEach(value -> all.put(value.type(), all.getOrDefault(value.type(), 0L) + value.value()));
        return new UnfilteredStatistic(values, all);
    };

    /**
     * The groups of values and all values added together.
     */
    private final Set<StatisticValue> statisticGroups;
    private final Map<StatisticType, Long> values;

    private UnfilteredStatistic(Set<StatisticValue> statisticGroups, Map<StatisticType, Long> values) {
        this.statisticGroups = statisticGroups;
        this.values = values;
    }

    /**
     * Filter statistics by the provided {@code input} type.
     *
     * @param input the input type
     * @return a new {@link Statistic} containing only stats from the provided {@code input}
     */
    public Statistic byInput(Input input) {
        final var groups = statisticGroups.stream().filter(group -> group.input() == input).collect(Collectors.toSet());
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
    public Statistic byPlaylist(String playlist) {
        final var groups = statisticGroups.stream().filter(group -> group.playlist().equalsIgnoreCase(playlist)).collect(Collectors.toSet());
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
    public Statistic byInputAndPlaylist(Input input, String playlist) {
        final var groups = statisticGroups.stream().filter(group -> group.input() == input && group.playlist().equalsIgnoreCase(playlist)).collect(Collectors.toSet());
        final var map = new HashMap<StatisticType, Long>();
        for (final var type : StatisticType.values()) {
            final var withType = groups.stream().filter(value -> value.type() == type).collect(Collectors.toSet());
            map.put(type, withType.stream().mapToLong(StatisticValue::value).sum());
        }
        return new Statistic(map);
    }

    /**
     * @return total number of players outlived all together.
     */
    public int playersOutlived() {
        return values.get(StatisticType.PLAYERS_OUTLIVED).intValue();
    }

    /**
     * @return total number of kills all together.
     */
    public int kills() {
        return values.get(StatisticType.KILLS).intValue();
    }

    /**
     * @return total number of wins all together.
     */
    public int wins() {
        return values.get(StatisticType.PLACED_TOP1).intValue();
    }

    /**
     * @return total number of 3rd places all together.
     */
    public int timesPlaced3rd() {
        return values.get(StatisticType.PLACED_TOP3).intValue();
    }

    /**
     * @return total number of 5th places all together.
     */
    public int timesPlaced5th() {
        return values.get(StatisticType.PLACED_TOP5).intValue();
    }

    /**
     * @return total number of 6th places all together.
     */
    public int timesPlaced6th() {
        return values.get(StatisticType.PLACED_TOP6).intValue();
    }

    /**
     * @return total number of 10th places all together.
     */
    public int timesPlaced10th() {
        return values.get(StatisticType.PLACED_TOP10).intValue();
    }

    /**
     * @return total number of 25th places all together.
     */
    public int timesPlaced25th() {
        return values.get(StatisticType.PLACED_TOP25).intValue();
    }

    /**
     * @return total number of minutes played all together
     */
    public int minutesPlayed() {
        return values.get(StatisticType.MINUTES_PLAYED).intValue();
    }

    /**
     * @return total number of matches played all together
     */
    public int matchesPlayed() {
        return values.get(StatisticType.MATCHES_PLAYED).intValue();
    }

    /**
     * @return total score all together
     */
    public int score() {
        return values.get(StatisticType.SCORE).intValue();
    }

    /**
     * @return current BP level.
     */
    public int battlePassLevel() {
        return values.get(StatisticType.BP_LEVEL).intValue();
    }

}
