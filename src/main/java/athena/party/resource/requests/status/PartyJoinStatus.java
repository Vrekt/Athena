package athena.party.resource.requests.status;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the response when you try to join a party.
 */
public final class PartyJoinStatus {

    /**
     * The status: "PENDING_CONFIRMATION"
     */
    private String status;
    /**
     * The party ID.
     */
    @SerializedName("party_id")
    private String partyId;

    /**
     * @return The status, ex: "PENDING_CONFIRMATION"
     */
    public String status() {
        return status;
    }

    /**
     * @return the party ID.
     */
    public String partyId() {
        return partyId;
    }
}
