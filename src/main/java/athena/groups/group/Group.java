package athena.groups.group;

import athena.groups.privacy.GroupPrivacy;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Represents a group.
 */
public final class Group {

    /**
     * The group ID.
     */
    @SerializedName("id")
    private String groupId;

    /**
     * The group owner ID.
     */
    @SerializedName("ownerId")
    private String ownerId;

    /**
     * Namespace
     * Name of the group.
     * The motto
     * The language
     * The description
     * the type, eg "COMMON"
     */
    private String namespace, name, motto, lang, description, type;

    /**
     * The size of the group, headcount limit and revision.
     */
    private int size, headcountLimit, revision;

    /**
     * When this group was created and updated.
     */
    private Instant createdAt, updatedAt;

    /**
     * Mode/privacy of the group.
     */
    @SerializedName("mode")
    private GroupPrivacy privacy;

    /**
     * If the group is enabled
     * If you are owner of this group
     * If you are admin of this group
     */
    private boolean enabled, isOwner, isAdmin;

    /**
     * @return the group ID.
     */
    public String groupId() {
        return groupId;
    }

    /**
     * @return the owner ID.
     */
    public String ownerId() {
        return ownerId;
    }

    /**
     * @return the namespace, usually "kairos"
     */
    public String namespace() {
        return namespace;
    }

    /**
     * @return the name of the group.
     */
    public String name() {
        return name;
    }

    /**
     * @return the motto of the group.
     */
    public String motto() {
        return motto;
    }

    /**
     * @return the language of the group.
     */
    public String lang() {
        return lang;
    }

    /**
     * @return the description of the group.
     */
    public String description() {
        return description;
    }

    /**
     * @return the type of group, usually "COMMON"
     */
    public String type() {
        return type;
    }

    /**
     * @return size of the group.
     */
    public int size() {
        return size;
    }

    /**
     * @return size limit of the group?
     */
    public int headcountLimit() {
        return headcountLimit;
    }

    /**
     * @return revision/edits made to the group.
     */
    public int revision() {
        return revision;
    }

    /**
     * @return when the group was created.
     */
    public Instant createdAt() {
        return createdAt;
    }

    /**
     * @return when the group was updated.
     */
    public Instant updatedAt() {
        return updatedAt;
    }

    /**
     * @return the group privacy
     */
    public GroupPrivacy privacy() {
        return privacy;
    }

    /**
     * @return {@code true} if the group is enabled.
     */
    public boolean enabled() {
        return enabled;
    }

    /**
     * @return if you are owner of this group
     */
    public boolean isOwner() {
        return isOwner;
    }

    /**
     * @return if you are admin of this group
     */
    public boolean isAdmin() {
        return isAdmin;
    }
}
