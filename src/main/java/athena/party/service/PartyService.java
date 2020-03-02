package athena.party.service;

import athena.party.resource.Party;
import athena.party.resource.authentication.status.PartyJoinStatus;
import athena.party.resource.notification.UndeliveredNotifications;
import athena.party.resource.user.UserPartyProfile;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface PartyService {

    String BASE_URL = "https://party-service-prod.ol.epicgames.com/";

    //	Line 1183324: /api/v1/{namespace}/parties/{partyId}/invites/{accountId}
    //	Line 1183331: /api/v1/{namespace}/parties/{partyId}/invites/{accountId}/decline
    //	Line 1183378: /api/v1/{namespace}/parties/{partyId}/members/{accountId}
    //	Line 1183380: /api/v1/{namespace}/parties/{partyId}/members/{accountId}/conferences/connection
    //	Line 1183382: /api/v1/{namespace}/parties/{partyId}/members/{accountId}/confirm
    //	Line 1183384: /api/v1/{namespace}/parties/{partyId}/members/{accountId}/disconnect
    //	Line 1183386: /api/v1/{namespace}/parties/{partyId}/members/{accountId}/join
    //	Line 1183388: /api/v1/{namespace}/parties/{partyId}/members/{accountId}/meta
    //	Line 1183390: /api/v1/{namespace}/parties/{partyId}/members/{accountId}/promote
    //	Line 1183392: /api/v1/{namespace}/parties/{partyId}/members/{accountId}/reject
    //	Line 1183438: /api/v1/{namespace}/parties
    //	Line 1183439: /api/v1/{namespace}/parties/{partyId} [x]
    //	Line 1183445: /api/v1/{namespace}/user/{accountId}/pings/{fromAccountId}
    //	Line 1183449: /api/v1/{namespace}/user/{accountId}/pings/{fromAccountId}/join
    //	Line 1183451: /api/v1/{namespace}/user/{accountId}/pings/{fromAccountId}/parties [x]
    //	Line 1183460: /api/v1/{namespace}/user/{accountId} [x]
    //	Line 1183463: /api/v1/{namespace}/user/{accountId}/notifications/undelivered/count [x]

    /**
     * Get a party by the provided {@code partyId}
     *
     * @param partyId the party ID.
     * @return a {@link Party}
     */
    @GET("party/api/v1/Fortnite/parties/{partyId}")
    Call<Party> getParty(@Path("partyId") String partyId);

    /**
     * Get a list of parties the user {@code fromAccountId} has.
     *
     * @param accountId     the account ID.
     * @param fromAccountId who to check
     * @return a list of {@link Party}
     */
    @GET("party/api/v1/Fortnite/user/{accountId}/pings/{fromAccountId}/parties")
    Call<List<Party>> getUserParties(@Path("accountId") String accountId, @Path("fromAccountId") String fromAccountId);

    /**
     * Get party user-data for the provided {@code accountId}
     *
     * @param accountId the account ID.
     * @return a {@link UserPartyProfile}
     */
    @GET("party/api/v1/Fortnite/user/{accountId}")
    Call<UserPartyProfile> userData(@Path("accountId") String accountId);

    /**
     * Get a count of undelivered party notifications.
     *
     * @param accountId the account ID.
     * @return a {@link UndeliveredNotifications}
     */
    @GET("party/api/v1/Fortnite/user/{accountId}/notifications/undelivered/count")
    Call<UndeliveredNotifications> undeliveredNotifications(@Path("accountId") String accountId);

    /**
     * Joins the provided party.
     *
     * @param partyId   the ID of the party
     * @param accountId the account ID.
     * @return a {@link PartyJoinStatus}
     */
    @POST("party/api/v1/Fortnite/parties/{partyId}/members/{accountId}/join")
    Call<PartyJoinStatus> joinParty(@Path("partyId") String partyId, @Path("accountId") String accountId, @Body JsonObject payload);

    /**
     * Leaves the provided party.
     *
     * @param partyId   the ID of the party
     * @param accountId the account ID.
     * @return Void
     */
    @DELETE("party/api/v1/Fortnite/parties/{partyId}/members/{accountId}")
    Call<Void> leaveParty(@Path("partyId") String partyId, @Path("accountId") String accountId);

    /**
     * Update/patch a party member.
     *
     * @param partyId   the party ID
     * @param accountId the account ID
     * @param payload   the payload
     * @return void
     */
    @PATCH("party/api/v1/Fortnite/parties/{partyId}/members/{accountId}/meta")
    Call<Void> patch(@Path("partyId") String partyId, @Path("accountId") String accountId, @Body JsonObject payload);

}
