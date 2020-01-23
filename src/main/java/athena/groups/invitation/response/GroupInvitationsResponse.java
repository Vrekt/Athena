package athena.groups.invitation.response;

import athena.groups.invitation.GroupInvitation;
import athena.groups.paging.Pageable;

import java.util.List;

/**
 * Represents a group invitation response.
 */
public final class GroupInvitationsResponse extends Pageable {

    /**
     * List of group invitations
     */
    private List<GroupInvitation> elements;

    private GroupInvitationsResponse() {
    }


    /**
     * @return list of invitations
     */
    public List<GroupInvitation> invitations() {
        return elements;
    }

    /**
     * Get a {@link GroupInvitation} by {@code groupName}
     *
     * @param groupName the group name.
     * @return the {@link GroupInvitation} if found or {@code null}
     */
    public GroupInvitation getByName(String groupName) {
        return elements
                .stream()
                .filter(groupInvitation -> groupInvitation.name().equals(groupName))
                .findAny()
                .orElse(null);
    }

    /**
     * Get a {@link GroupInvitation} by {@code groupId}
     *
     * @param groupId the group ID.
     * @return the {@link GroupInvitation} if found or {@code null}
     */
    public GroupInvitation getById(String groupId) {
        return elements
                .stream()
                .filter(groupInvitation -> groupInvitation.groupId().equals(groupId))
                .findAny()
                .orElse(null);
    }

    /**
     * @return count of invitations
     */
    public int countinvitations() {
        return count();
    }

    /**
     * @return start index?
     */
    public int startinvitations() {
        return start();
    }

    /**
     * @return total number of invitations
     */
    public int totalinvitations() {
        return total();
    }
}
