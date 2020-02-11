package athena.party.resource.invite;

import athena.account.resource.Account;
import athena.context.AthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.friend.resource.summary.Profile;
import athena.types.Platform;
import athena.util.request.Requests;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Represents a party invitation
 */
public final class PartyInvitation extends AthenaContext {

    /**
     * The ID of the party ID.
     */
    @SerializedName("party_id")
    private String partyId;

    /**
     * Who the invite was sent by.
     */
    @SerializedName("sent_by")
    private String sentBy;

    /**
     * The meta of this invitation
     */
    private InvitationMeta meta;

    /**
     * Who the invitation was sent to.
     */
    @SerializedName("sent_to")
    private String sentTo;

    /**
     * When the invite was sent.
     */
    @SerializedName("sent_at")
    private Instant sentAt;
    /**
     * When the invite was updated.
     */
    @SerializedName("updated_at")
    private Instant updatedAt;
    /**
     * When the invite expires.
     */
    @SerializedName("expires_at")
    private Instant expiresAt;
    /**
     * The status of this invite - "SENT"
     */
    private String status;

    /**
     * @return the ID of the party
     */
    public String partyId() {
        return partyId;
    }

    /**
     * @return who it was sent by
     */
    public String sentByAccountId() {
        return sentBy;
    }

    /**
     * @return who it was sent by - their account.
     */
    public Account sentByAccount() {
        return Requests.executeCall(accountPublicService.findByDisplayName(displayName()));
    }

    /**
     * @return the friend profile of who sent this invite.
     */
    public Profile sentByProfile() {
        return Requests.executeCall(friendsPublicService.profile(localAccountId, sentBy, true));
    }

    /**
     * @return the connection type of who sent the invite.
     */
    public String type() {
        return meta.type;
    }

    /**
     * @return the platform of who sent the invite
     */
    public Platform platform() {
        return meta.platform;
    }

    /**
     * @return the display name of who sent the invite
     */
    public String displayName() {
        return meta.displayName;
    }

    /**
     * @return the build ID.
     */
    public String buildId() {
        return meta.buildId;
    }

    /**
     * @return the platform data
     */
    public String platformData() {
        return meta.platformData;
    }

    /**
     * @return the account ID of who the invite was sent to.
     */
    public String sentTo() {
        return sentTo;
    }

    /**
     * @return the {@link Account} of who the invite was sent to.
     */
    public Account sentToAccount() {
        final var call = accountPublicService.findOneByAccountId(sentTo);
        final var result = Requests.executeCall(call);
        if (result.length == 0) throw EpicGamesErrorException.create("Cannot find account " + sentTo);
        return result[0];
    }

    /**
     * @return the {@link Profile} of who the invite was sent to.
     * They must be a friend for this call to succeed.
     */
    public Profile sentToProfile() {
        return Requests.executeCall(friendsPublicService.profile(localAccountId, sentTo, true));
    }

    /**
     * @return when this invite was sent
     */
    public Instant sentAt() {
        return sentAt;
    }

    /**
     * @return when this invite was updated.
     */
    public Instant updatedAt() {
        return updatedAt;
    }

    /**
     * @return when this invite expires.
     */
    public Instant expiresAt() {
        return expiresAt;
    }

    /**
     * @return the status of this invite, "SENT"/???
     */
    public String status() {
        return status;
    }

    /**
     * The meta of this invitation.
     */
    private static final class InvitationMeta {

        /**
         * The type of connection, usually "GAME"
         */
        @SerializedName("urn:epic:conn:type_s")
        private String type;

        /**
         * The platform of who sent the invite
         */
        @SerializedName("urn:epic:conn:platform_s")
        private Platform platform;

        /**
         * The display name of who sent the invite
         */
        @SerializedName("urn:epic:member:dn_s")
        private String displayName;

        /**
         * The build ID.
         */
        @SerializedName("urn:epic:cfg:build-id_s")
        private String buildId;

        /**
         * The platform data.
         */
        @SerializedName("urn:epic:invite:platformdata_s")
        private String platformData;

    }

}
