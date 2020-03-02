package athena.party.resource.meta.invites;

import athena.types.Platform;
import com.google.gson.annotations.SerializedName;

/**
 * Represents metadata for a ping or an invitation.
 */
public final class PingOrInvitationMeta {


    /**
     * The type of connection, usually "game"
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

    /**
     * @return the type of connection
     */
    public String type() {
        return type;
    }

    /**
     * @return their platform
     */
    public Platform platform() {
        return platform;
    }

    /**
     * @return their display name
     */
    public String displayName() {
        return displayName;
    }

    /**
     * @return the current build ID.
     */
    public String buildId() {
        return buildId;
    }

    /**
     * @return usually {@code ""}
     */
    public String platformData() {
        return platformData;
    }
}
