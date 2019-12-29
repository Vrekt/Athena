package athena.stats.resource.type;

import java.util.List;

public enum StatisticType {

    PLAYERS_OUTLIVED("playersoutlived"),
    KILLS("kills"),
    PLACED_TOP1("placetop1"),
    PLACED_TOP3("placetop3"),
    PLACED_TOP5("placetop5"),
    PLACED_TOP6("placetop6"),
    PLACED_TOP10("placetop10"),
    PLACED_TOP12("placetop12"),
    PLACED_TOP25("placetop25"),
    LAST_MODIFIED("lastmodified"),
    MINUTES_PLAYED("minutesplayed"),
    MATCHES_PLAYED("matchesplayed"),
    SCORE("score"),
    BP_LEVEL("s11_social_bp_level"),
    UNKNOWN("");

    private static final List<StatisticType> TYPES = List.of(values());
    private String identifier;

    StatisticType(String identifier) {
        this.identifier = identifier;
    }

    public String identifier() {
        return identifier;
    }

    /**
     * Allow setting the identifier for unknown values.
     * @param identifier the identifier.
     */
    public StatisticType identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    /**
     * Gets the {@link StatisticType} from the provided {@code key} name.
     *
     * @param key the name of the statistic type.
     * @return a {@link StatisticType} or {@code null} if not found.
     */
    public static StatisticType typeOf(String key) {
        if (key == null) return null;
        return TYPES.stream().filter(type -> type.identifier().equalsIgnoreCase(key)).findAny().orElse(null);
    }

}
