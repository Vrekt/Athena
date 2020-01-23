package athena.groups.membership.response;

import athena.groups.membership.MembershipOf;
import athena.groups.paging.Pageable;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents a response when you query the membership of an account ID.
 */
public final class GroupsMembershipResponse extends Pageable {

    /**
     * List of groups the member is in.
     */
    @SerializedName("elements")
    private List<MembershipOf> membership;

    /**
     * Get a {@link MembershipOf} by {@code groupId}
     *
     * @param groupId the ID of the group.
     * @return the {@link MembershipOf} or {@code null}
     */
    public MembershipOf getById(String groupId) {
        return membership.stream().filter(group -> group.groupId().equals(groupId)).findAny().orElse(null);
    }

    /**
     * Get a {@link MembershipOf} by {@code groupName}
     *
     * @param groupName the group name.
     * @return the {@link MembershipOf} or {@code null}
     */
    public MembershipOf getByName(String groupName) {
        return membership.stream().filter(group -> group.name().equals(groupName)).findAny().orElse(null);
    }

    /**
     * @return count of groups.
     */
    public int countGroups() {
        return count();
    }

    /**
     * @return start index?
     */
    public int startGroups() {
        return start();
    }

    /**
     * @return total number of groups
     */
    public int totalGroups() {
        return total();
    }

}
