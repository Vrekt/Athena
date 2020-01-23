package athena.groups.membership;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;

/**
 * Represents a group somebody is a member of
 */
public final class MembershipOf {

    /**
     * The account ID
     * The group ID
     * the name of the group.
     */
    private String accountId, groupId, name;

    /**
     * The group owner account ID.
     */
    @SerializedName("owner")
    private String ownerId;

    /**
     * {@code true} if the provided account ID is admin
     * {@code true} if the provided account ID is owner.
     */
    private boolean admin, isOwner;

    /**
     * When the provided account ID joined.
     */
    private Instant joinedAt;

    /**
     * The group size.
     */
    private int size;

    /**
     * TODO:
     */
    private JsonObject metadata;

    /**
     * List of "top oldest members" for this group.
     */
    private List<GroupMember> topOldestMembers;

    /**
     * @return the account ID of this membership
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the group ID this member is in.
     */
    public String groupId() {
        return groupId;
    }

    /**
     * @return the name of the group this member is in.
     */
    public String name() {
        return name;
    }

    /**
     * @return the owner account ID of the group.
     */
    public String ownerId() {
        return ownerId;
    }

    /**
     * @return {@code true} if the provided account ID is admin.
     */
    public boolean admin() {
        return admin;
    }

    /**
     * @return {@code true} if the provided account ID is owner.
     */
    public boolean isOwner() {
        return isOwner;
    }

    /**
     * @return when this member joined.
     */
    public Instant joinedAt() {
        return joinedAt;
    }

    /**
     * @return the size of the group.
     */
    public int size() {
        return size;
    }

    /**
     * TODO: Metadata.
     *
     * @return ??
     */
    public JsonObject metadata() {
        return metadata;
    }

    /**
     * @return list of "top oldest members" ?
     */
    public List<GroupMember> topOldestMembers() {
        return topOldestMembers;
    }
}

