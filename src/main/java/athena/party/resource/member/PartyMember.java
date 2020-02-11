package athena.party.resource.member;

import athena.party.resource.connection.Connection;
import athena.party.resource.member.role.PartyRole;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;

/**
 * Represents a party member.
 */
public final class PartyMember {

    /**
     * The account ID of this member.
     */
    @SerializedName("account_id")
    private String accountId;

    /**
     * List of connections for this member.
     */
    private List<Connection> connections;

    /**
     * The primary connection.
     */
    private Connection connection;

    /**
     * The revision of this member.
     */
    private int revision;

    /**
     * When this member was updated.
     */
    @SerializedName("updated_at")
    private Instant updatedAt;
    /**
     * When this member joined.
     */
    @SerializedName("joined_at")
    private Instant joinedAt;

    /**
     * The role of this member.
     */
    private PartyRole role;


    public String accountId() {
        return accountId;
    }

    public List<Connection> connections() {
        return connections;
    }

    public Connection connection() {
        return connection;
    }

    public int revision() {
        return revision;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Instant joinedAt() {
        return joinedAt;
    }

    public PartyRole role() {
        return role;
    }
}
