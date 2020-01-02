package athena.stats.resource.leaderboard;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The response implementation
 */
public final class LeaderboardResponse {

    /**
     * List of entries.
     */
    private LinkedList<LeaderboardEntry> entries;

    /**
     * Collect the list of entries to a map and return.
     *
     * @return a map of entries, key is account ID and value is value.
     */
    public LinkedHashMap<String, Integer> mapOfEntries() {
        return entries.stream().collect(Collectors.toMap(LeaderboardEntry::accountId, LeaderboardEntry::value, ((integer, integer2) -> integer), LinkedHashMap::new));
    }

    /**
     * @return list of entries.
     */
    public List<LeaderboardEntry> listOfEntries() {
        return entries;
    }
}
