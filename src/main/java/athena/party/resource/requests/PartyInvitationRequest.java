package athena.party.resource.requests;

import athena.party.Parties;
import athena.types.Platform;
import com.google.gson.annotations.SerializedName;

/**
 * A request used to invite a user.
 */
public final class PartyInvitationRequest {

    /**
     * The connection type
     */
    @SerializedName("urn:epic:conn:type_s")
    private final String connectionType = "game";

    /**
     * the display name
     */
    @SerializedName("urn:epic:member:dn_s")
    private final String displayName;

    /**
     * The platform
     */
    @SerializedName("urn:epic:conn:platform_s")
    private final Platform platform;

    /**
     * The build ID.
     */
    @SerializedName("urn:epic:cfg:build-id_s")
    private final String buildId = Parties.BUILD_ID;

    /**
     * Platform data.
     */
    @SerializedName("urn:epic:invite:platformdata_s")
    private final String platformData = "";

    /**
     * Initialize this request.
     *
     * @param displayName the display name
     * @param platform    the platform
     */
    public PartyInvitationRequest(String displayName, Platform platform) {
        this.displayName = displayName;
        this.platform = platform;
    }

}
