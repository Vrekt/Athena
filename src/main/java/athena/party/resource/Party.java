package athena.party.resource;

import athena.party.resource.configuration.PartyConfiguration;
import athena.party.resource.member.PartyMember;
import athena.party.resource.meta.PartyMeta;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;

/**
 * Represents a Fortnite party.
 */
public final class Party {

    /**
     * The ID of this party.
     */
    @SerializedName("id")
    private String partyId;

    /**
     * When the party was created.
     */
    @SerializedName("created_at")
    private Instant createdAt;
    /**
     * When the party was updated.
     */
    @SerializedName("updated_at")
    private Instant updatedAt;

    /**
     * The party configuration
     */
    private PartyConfiguration config;

    /**
     * The party members
     */
    private List<PartyMember> members;

    private PartyMeta meta;

    public PartyMeta meta() {
        return meta;
    }
}
