package athena.channels.service;

import athena.channels.resource.channel.Channel;
import athena.channels.resource.requests.channel.CreateChannel;
import athena.channels.resource.requests.channel.EditChannel;
import athena.channels.resource.requests.member.AddMembers;
import athena.channels.resource.requests.message.SendMessage;
import athena.channels.resource.responses.channel.UserChannels;
import athena.channels.resource.responses.message.GetMessage;
import athena.channels.resource.responses.message.Messages;
import athena.channels.resource.responses.message.SentMessage;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Provides access to the channels public service.
 */
public interface ChannelsPublicService {

    String BASE_URL = "https://channels-public-service-prod.ol.epicgames.com/";

    /**
     * Get all channels and DMs for the provided {@code accountId}
     *
     * @param accountId the accountID
     * @param type      the type, usually "all"
     * @return a {@link Call} containing the {@link athena.channels.resource.responses.channel.UserChannel} if the call was successful.
     */
    @GET("api/v1/user/{accountId}")
    Call<UserChannels> getAll(@Path("accountId") String accountId, @Query("type") String type);

    /**
     * Creates a new channel.
     * TODO: Currently, the server returns an error HTTP 400/500 but group creation still works.
     * TODO: We need to monitor this to see when proper responses are returned.
     * TODO: For now, only void is returned in this case.
     *
     * @param createChannel the {@link CreateChannel} request.
     * @return Void
     */
    @POST("api/v1/channel")
    Call<Void> createNewChannel(@Body CreateChannel createChannel);

    /**
     * Get a channel by the provided {@code channelId}
     *
     * @param channelId the ID of the channel.
     * @return a {@link Call} containing the {@link Channel} if the call was successful.
     */
    @GET("api/v1/channel/{channelId}")
    Call<Channel> getChannel(@Path("channelId") String channelId);

    /**
     * Send a message to a channel.
     *
     * @param channelId the ID of the channel.
     * @param message   the message.
     * @return a {@link Call} containing the {@link SentMessage} if the call was successful.
     */
    @POST("api/v1/channel/{channelId}/messages")
    Call<SentMessage> sendMessage(@Path("channelId") String channelId, @Body SendMessage message);

    /**
     * Get a message by ID.
     *
     * @param channelId the channel ID.
     * @param messageId the message ID.
     * @return a {@link Call} returned by retrofit containing the {@link GetMessage} if the call was successful.
     */
    @GET("api/v1/channel/{channelId}/messages/{messageId}")
    Call<GetMessage> getMessage(@Path("channelId") String channelId, @Path("messageId") String messageId);

    /**
     * Retrieve messages for a channel.
     *
     * @param channelId the channel ID.
     * @return a {@link Call} returned by retrofit containing the {@link Messages} if the call was successful.
     */
    @GET("api/v1/channel/{channelId}/messages")
    Call<Messages> getMessages(@Path("channelId") String channelId);

    /**
     * Delete a message.
     *
     * @param channelId the channel ID.
     * @param messageId the message ID.
     * @return Void
     */
    @DELETE("api/v1/channel/{channelId}/messages/{messageId}")
    Call<Void> deleteMessage(@Path("channelId") String channelId, @Path("messageId") String messageId);

    /**
     * Edit a channel.
     *
     * @param channelId the channel ID.
     * @param request   the request.
     * @return Void
     */
    @POST("api/v1/channel/{channelId}")
    Call<Void> editChannel(@Path("channelId") String channelId, @Body EditChannel request);

    /**
     * Leave a channel.
     *
     * @param channelId the channel ID.
     * @param accountId the account ID.
     * @return Void
     */
    @DELETE("api/v1/channel/{channelId}/members/{accountId}")
    Call<Void> leaveChannel(@Path("channelId") String channelId, @Path("accountId") String accountId);

    /**
     * Add members to a channel.
     * TODO: Monitor this.
     *
     * @param channelId the channel ID.
     * @param members   the members to add
     * @return Void
     */
    @POST("api/v1/channel/{channelId}/members")
    Call<Void> addMembersToChannel(@Path("channelId") String channelId, @Body AddMembers members);

    /**
     * Send a message to a DM.
     *
     * @param accountId the account ID of the current authenticated account.
     * @param otherId   the account ID of who to send the message to.
     * @param message   the message.
     * @return a {@link Call} containing the {@link SentMessage} if the call was successful.
     */
    @POST("api/v1/dm/{accountId}/{otherId}/messages")
    Call<SentMessage> sendMessageTo(@Path("accountId") String accountId, @Path("otherId") String otherId, @Body SendMessage message);

    /**
     * Retrieve messages from a DM.
     *
     * @param accountId the account ID of the current authenticated account.
     * @param otherId   the account ID of who to send the message to.
     * @return a {@link Call} returned by retrofit containing the {@link Messages} if the call was successful.
     */
    @GET("api/v1/dm/{accountId}/{otherId}/messages")
    Call<Messages> getMessagesIn(@Path("accountId") String accountId, @Path("otherId") String otherId);

    /**
     * Delete a DM message.
     *
     * @param accountId the account ID of the current authenticated account.
     * @param otherId   the account ID of who to send the message to.
     * @param messageId the ID of the message.
     * @return Void
     */
    @DELETE("api/v1/dm/{accountId}/{otherId}/messages/{messageId}")
    Call<Void> deleteMessageIn(@Path("accountId") String accountId, @Path("otherId") String otherId, @Path("messageId") String messageId);

}
