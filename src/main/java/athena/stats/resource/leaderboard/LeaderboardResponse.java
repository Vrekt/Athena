package athena.stats.resource.leaderboard;

import athena.util.json.BasicJsonDeserializer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The response implementation
 */
public final class LeaderboardResponse {

    /**
     * the JSON adapter.
     */
    public static final BasicJsonDeserializer<LeaderboardResponse> ADAPTER = json -> {
        final var array = json.getAsJsonObject().getAsJsonArray("entries");
        final var map = new LinkedHashMap<String, Integer>();

        array.forEach(jsonElement -> {
            final var object = jsonElement.getAsJsonObject();
            final var account = object.get("account").getAsString();
            final var value = object.get("value").getAsInt();
            map.put(account, value);
        });
        return new LeaderboardResponse(map);
    };

    /**
     * Entries.
     */
    private final Map<String, Integer> entries;

    private LeaderboardResponse(Map<String, Integer> map) {
        this.entries = map;
    }

    /**
     * @return all values
     */
    public Map<String, Integer> values() {
        return entries;
    }

    /**
     * @return maps the map of values to a list of {@link LeaderboardEntry}
     */
    public List<LeaderboardEntry> asEntries() {
        return entries.entrySet().stream().map(entry -> new LeaderboardEntry(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

}
