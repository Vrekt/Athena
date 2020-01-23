package athena.groups.application.response;

import athena.groups.application.GroupApplication;
import athena.groups.paging.Pageable;

import java.util.List;

/**
 * Represents group applications response
 */
public final class GroupApplicationsResponse extends Pageable {

    /**
     * List of group applications.
     */
    private List<GroupApplication> elements;

    private GroupApplicationsResponse() {}

    /**
     * @return list of applications
     */
    public List<GroupApplication> applications() {
        return elements;
    }

    /**
     * Get a {@link GroupApplication} by {@code groupName}
     *
     * @param groupName the group name.
     * @return the {@link GroupApplication} if found or {@code null}
     */
    public GroupApplication getByName(String groupName) {
        return elements
                .stream()
                .filter(groupApplication -> groupApplication.name().equals(groupName))
                .findAny()
                .orElse(null);
    }

    /**
     * Get a {@link GroupApplication} by {@code groupId}
     *
     * @param groupId the group ID.
     * @return the {@link GroupApplication} if found or {@code null}
     */
    public GroupApplication getById(String groupId) {
        return elements
                .stream()
                .filter(groupApplication -> groupApplication.groupId().equals(groupId))
                .findAny()
                .orElse(null);
    }

    /**
     * @return count of applications
     */
    public int countApplications() {
        return count();
    }

    /**
     * @return start index?
     */
    public int startApplications() {
        return start();
    }

    /**
     * @return total number of applications
     */
    public int totalApplications() {
        return total();
    }

}
