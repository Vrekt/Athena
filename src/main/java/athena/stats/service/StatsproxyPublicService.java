package athena.stats.service;

import athena.stats.resource.UnfilteredStatistic;
import athena.stats.resource.leaderboard.LeaderboardResponse;
import athena.stats.resource.query.IndividualQueryResponse;
import athena.stats.resource.query.StatisticsQuery;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface StatsproxyPublicService {

    /**
     * The base URL for the stats proxy service.
     */
    String BASE_URL = "https://statsproxy-public-service-live.ol.epicgames.com/";

    /**
     * Retrieve stats for the provided {@code accountId}
     *
     * @param accountId the ID of the account
     * @return a {@link UnfilteredStatistic} object that will contain the statistics.
     */
    @GET("statsproxy/api/statsv2/account/{accountId}")
    Call<UnfilteredStatistic> stats(@Path("accountId") String accountId);

    /**
     * Query specific stats for some accounts.
     *
     * @param query the query body.
     * @return a {@link List} that contains each statistic represented as {@link IndividualQueryResponse}
     */
    @POST("statsproxy/api/statsv2/query")
    Call<List<IndividualQueryResponse>> query(@Body StatisticsQuery query);

    /**
     * Retrieve leaderboard entries for a specific statistic.
     *
     * @param type the type of statistic, ex: 'br_placetop1_keyboardmouse_m0_playlist_defaultsolo'
     * @return a {@link LeaderboardResponse} that represents the response.
     */
    @GET("statsproxy/api/statsv2/leaderboards/{type}")
    Call<LeaderboardResponse> leaderboard(@Path("type") String type);

}
