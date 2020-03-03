package athena.events.service;

import athena.events.resource.download.event.FortniteEventDownload;
import athena.events.resource.download.player.EventPlayer;
import athena.events.resource.leaderboard.EventLeaderboard;
import athena.events.resource.player.PlayerTokenResponse;
import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Provides access to the events-public-service.
 * <p>
 * // TODO: api/v1/events/Fortnite/data/{accountId}, api/v1/players/Fortnite/{accountId}
 * // TODO: api/v1/events/Fortnite/{eventId}/{eventWindowId}/history/{accountId}, api/v1/events/Fortnite/{eventId}/history/{accountId}
 *
 * @author Vrekt, Armisto
 */
public interface EventsPublicService {

    String BASE_URL = "https://events-public-service-live.ol.epicgames.com/";

    /**
     * Download all events.
     *
     * @param accountId      the account ID.
     * @param region         the region
     * @param platform       the platform.
     * @param teamAccountIds the team account IDs, usually just the {@code accountId}
     * @return a {@link Call} returned by retrofit containing the {@link FortniteEventDownload} if the call was successful.
     */
    @GET("api/v1/events/Fortnite/download/{accountId}")
    Call<FortniteEventDownload> download(@Path("accountId") String accountId, @Query("region") String region, @Query("platform") String platform, @Query("teamAccountIds") String[] teamAccountIds);

    /**
     * Get the player data.
     *
     * @param accountId the account ID
     * @return a {@link Call} returned by retrofit containing the {@link EventPlayer} if the call was successful.
     */
    @GET("api/v1/players/Fortnite/{accountId}")
    Call<EventPlayer> player(@Path("accountId") String accountId);

    /**
     * Get the player tokens for each account ID
     *
     * @param teamAccountIds the array of team account IDs
     * @return a {@link Call} returned by retrofit containing the {@link PlayerTokenResponse} if the call was successful.
     */
    @GET("api/v1/players/Fortnite/tokens")
    Call<PlayerTokenResponse> tokens(@Query("teamAccountIds") String[] teamAccountIds);

    /**
     * Retrieve the leaderboards for a specific event.
     *
     * @param eventId          the event ID.
     * @param eventWindowId    the event window ID.
     * @param accountId        the account ID.
     * @param page             the page, usually 0.
     * @param rank             the rank, usually 0.
     * @param teamAccountIds   the account IDs, usually "".
     * @param appId            the app ID, always "Fortnite"
     * @param showLiveSessions {@code true} if live sessions should be given.
     * @return a {@link Call} returned by retrofit containing the {@link EventLeaderboard} if the call was successful.
     */
    @GET("api/v1/leaderboards/Fortnite/{eventId}/{eventWindowId}/{accountId}")
    Call<EventLeaderboard> leaderboards(@Path("eventId") String eventId, @Path("eventWindowId") String eventWindowId, @Path("accountId") String accountId, @Query("page") int page, @Query("rank") int rank, @Query("teamAccountIds") String teamAccountIds, @Query("appId") String appId, @Query("showLiveSessions") boolean showLiveSessions);

    /**
     * Retrieve the leaderboards for a specific event.
     *
     * @param eventId          the event ID.
     * @param eventWindowId    the event window ID.
     * @param accountId        the account ID.
     * @param page             the page, usually 0.
     * @param rank             the rank, usually 0.
     * @param showLiveSessions {@code true} if live sessions should be given.
     * @return a {@link Call} returned by retrofit containing the {@link EventLeaderboard} if the call was successful.
     */
    @GET("api/v1/leaderboards/Fortnite/{eventId}/{eventWindowId}/{accountId}")
    Call<EventLeaderboard> leaderboards(@Path("eventId") String eventId, @Path("eventWindowId") String eventWindowId, @Path("accountId") String accountId, @Query("page") int page, @Query("rank") int rank, @Query("showLiveSessions") boolean showLiveSessions);

    /**
     * Get the event data for the provided {@code accountId}
     *
     * @param accountId      the account ID
     * @param region         the region, eg: NAE
     * @param showPastEvents {@code true} if past events should be given?
     * @return a {@link Call} returned by retrofit containing the {@link FortniteEventDownload} if the call was successful.
     */
    @GET("api/v1/events/Fortnite/data/{accountId}")
    Call<FortniteEventDownload> data(@Path("accountId") String accountId, @Query("region") String region, @Query("showPastEvents") boolean showPastEvents);

    /**
     * Get the event window history for the provided {@code accountId}
     * TODO:
     *
     * @param eventId       the event ID.
     * @param eventWindowId the event window ID.
     * @param accountId     the account ID.
     */
    @GET("api/v1/events/Fortnite/{eventId}/{eventWindowId}/history/{accountId}")
    Call<JsonElement> eventWindowHistory(@Path("eventId") String eventId, @Path("eventWindowId") String eventWindowId, @Path("accountId") String accountId);

}
