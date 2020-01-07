package athena.fortnite.creative;

import athena.fortnite.service.FortnitePublicService;

import java.util.List;

/**
 * Represents response from "fortnite/api/game/v2/creative/history/{accountId}" {@link FortnitePublicService}
 */
public final class CreativeHistoryResponse {

    /**
     * List of results.
     */
    private List<CreativeHistoryResult> results;

    /**
     * @return results
     */
    public List<CreativeHistoryResult> results() {
        return results;
    }
}
