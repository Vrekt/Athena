package athena.groups.service;

import athena.groups.application.response.GroupApplicationsResponse;
import athena.groups.group.Group;
import athena.groups.invitation.response.GroupInvitationsResponse;
import athena.groups.membership.response.GroupsMembershipResponse;
import athena.groups.requests.creating.CreateGroupRequest;
import athena.groups.requests.updating.UpdateGroupRequest;
import athena.groups.response.BaseGroupsResponse;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Provides access to the groups-service.
 *
 * @author Vrekt, Armisto
 */
public interface GroupsService {

    //api/v1/groups/in/`ns [x]
    //api/v1/groups/in/`ns?q=`query [x]
    //api/v1/user/in/`ns/`accountId/membership [x]
    //api/v1/user/in/`ns/`accountId/applications/outgoing [x]
    //api/v1/user/in/`ns/`accountId/applications/incoming [x]
    //api/v1/user/in/`ns/`accountId/invitations/outgoing [x]
    //api/v1/user/in/`ns/`accountId/invitations/incoming [x]
    //api/v1/groups/`groupId [x]
    //api/v1/groups/`groupId/owner/`accountId
    //api/v1/groups/`groupId/members
    //api/v1/groups/`groupId/members/`accountId
    //api/v1/groups/`groupId/admins
    //api/v1/groups/`groupId/admins/`accountId
    //api/v1/groups/in/`ns/`name/exist
    //api/v1/groups/`groupId/invitations
    //api/v1/groups/`groupId/invitations/`accountId
    //api/v1/groups/`groupId/invitations/`accountId/accept
    //api/v1/groups/`groupId/invitations/`accountId/reject
    //api/v1/groups/`groupId/applications
    //api/v1/groups/`groupId/applications/`accountId
    //api/v1/groups/`groupId/applications/`accountId/accept
    //api/v1/groups/`groupId/applications/`accountId/reject
    //api/v1/groups/`groupId/blacklist
    //api/v1/groups/`groupId/blacklist/`accountId
    //api/v1/config/`ns/limits/headcount
    //api/v1/config/`ns/limits/membership

    /**
     * The base URL.
     */
    String BASE_URL = "https://groups-service-prod06.ol.epicgames.com/";

    /**
     * Get all groups in the provided {@code namespace}
     *
     * @param namespace the namespace, usually "kairos"
     * @return a {@link Call} returned by retrofit containing the {@link BaseGroupsResponse} if the call was successful.
     */
    @GET("groups/api/v1/groups/in/{namespace}")
    Call<BaseGroupsResponse> groupsIn(@Path("namespace") String namespace);

    /**
     * Create a new group.
     *
     * @param namespace the namespace, usually "kairos"
     * @param request   the create group request.
     * @return a {@link Call} returned by retrofit containing the {@link Group} if the call was successful.
     */
    @POST("groups/api/v1/groups/in/{namespace}")
    Call<Group> createGroup(@Path("namespace") String namespace, @Body CreateGroupRequest request);

    /**
     * Get groups by name.
     *
     * @param namespace the namespace, usually "kairos"
     * @param groupName the group name.
     * @param page      the page number
     * @return a {@link Call} returned by retrofit containing the {@link BaseGroupsResponse} if the call was successful.
     */
    @GET("groups/api/v1/groups/in/{namespace}")
    Call<BaseGroupsResponse> getGroupsByName(@Path("namespace") String namespace, @Query("q") String groupName, @Query("page") int page);

    /**
     * Get all the groups a member if apart of.
     *
     * @param namespace the namespace, usually "kairos"
     * @param accountId the account ID.
     * @return a {@link Call} returned by retrofit containing the {@link GroupsMembershipResponse} if the call was successful.
     */
    @GET("groups/api/v1/user/in/{namespace}/{accountId}/membership")
    Call<GroupsMembershipResponse> getMembershipsFor(@Path("namespace") String namespace, @Path("accountId") String accountId);

    /**
     * Get outgoing applications for the provided {@code accountId}
     *
     * @param namespace the namespace, usually "kairos"
     * @param accountId the account ID.
     * @return a {@link Call} returned by retrofit containing the {@link GroupApplicationsResponse} if the call was successful.
     */
    @GET("groups/api/v1/user/in/{namespace}/{accountId}/applications/outgoing")
    Call<GroupApplicationsResponse> getOutgoingApplications(@Path("namespace") String namespace, @Path("accountId") String accountId);

    /**
     * Get incoming applications for the provided {@code accountId}
     *
     * @param namespace the namespace, usually "kairos"
     * @param accountId the account ID.
     * @return a {@link Call} returned by retrofit containing the {@link GroupApplicationsResponse} if the call was successful.
     */
    @GET("groups/api/v1/user/in/{namespace}/{accountId}/applications/incoming")
    Call<GroupApplicationsResponse> getIncomingApplications(@Path("namespace") String namespace, @Path("accountId") String accountId);

    /**
     * Get outgoing invitations for the provided {@code accountId}
     *
     * @param namespace the namespace, usually "kairos"
     * @param accountId the account ID.
     * @return a {@link Call} returned by retrofit containing the {@link GroupInvitationsResponse} if the call was successful.
     */
    @GET("groups/api/v1/user/in/{namespace}/{accountId}/invitations/outgoing")
    Call<GroupInvitationsResponse> getOutgoingInvitations(@Path("namespace") String namespace, @Path("accountId") String accountId);

    /**
     * Get incoming invitations for the provided {@code accountId}
     *
     * @param namespace the namespace, usually "kairos"
     * @param accountId the account ID.
     * @return a {@link Call} returned by retrofit containing the {@link GroupInvitationsResponse} if the call was successful.
     */
    @GET("groups/api/v1/user/in/{namespace}/{accountId}/invitations/incoming")
    Call<GroupInvitationsResponse> getIncomingInvitations(@Path("namespace") String namespace, @Path("accountId") String accountId);

    /**
     * Get a group by {@code groupId}
     *
     * @param groupId the group ID.
     * @return a {@link Call} returned by retrofit containing the {@link Group} if the call was successful.
     */
    @GET("groups/api/v1/groups/{groupId}")
    Call<Group> group(@Path("groupId") String groupId);

    /**
     * Update a group.
     *
     * @param groupId the group ID.
     * @param request the update group request.
     * @return a {@link Call} returned by retrofit containing the {@link Group} if the call was successful.
     */
    @POST("groups/api/v1/groups/{groupId}")
    Call<Group> updateGroup(@Path("groupId") String groupId, @Body UpdateGroupRequest request);

    /**
     * Delete a group.
     *
     * @param groupId the group ID.
     * @return Void.
     */
    @DELETE("groups/api/v1/groups/{groupId}")
    Call<Void> deleteGroup(@Path("groupId") String groupId);

}
