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

    /**
     * @return the account ID of this party member.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return a list of connections
     */
    public List<Connection> connections() {
        return connections;
    }

    /**
     * @return the first or primary connection for this member.
     */
    public Connection connection() {
        return connection;
    }

    /**
     * @return the current revision
     */
    public int revision() {
        return revision;
    }

    /**
     * @return when this member was updated.
     */
    public Instant updatedAt() {
        return updatedAt;
    }

    /**
     * @return when this member joined.
     */
    public Instant joinedAt() {
        return joinedAt;
    }

    /**
     * @return the role of this member.
     */
    public PartyRole role() {
        return role;
    }
}
