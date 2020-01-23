package athena.groups.response;

import athena.groups.group.Group;
import athena.groups.paging.Pageable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a response of all groups.
 */
public final class BaseGroupsResponse extends Pageable {

    /**
     * All groups registered.
     */
    private List<Group> elements;

    /**
     * Get a {@link Group} by {@code groupId}
     *
     * @param groupId the ID of the group.
     * @return the {@link Group} or {@code null}
     */
    public Group getById(String groupId) {
        return elements.stream().filter(group -> group.groupId().equals(groupId)).findAny().orElse(null);
    }

    /**
     * Get a {@link Group} by {@code groupName}
     *
     * @param groupName the group name.
     * @return the {@link Group} or {@code null}
     */
    public Group getByName(String groupName) {
        return elements.stream().filter(group -> group.name().equals(groupName)).findAny().orElse(null);
    }

    /**
     * Collect groups that only match the language of the provided {@code language}
     * This method will use {@code equalsIgnoreCase}
     *
     * @param language the language, ex: "en"
     * @return a list of {@link Group}
     */
    public List<Group> filterByLanguage(String language) {
        return elements.stream().filter(group -> group.lang().equalsIgnoreCase(language)).collect(Collectors.toList());
    }

    /**
     * @return the list of groups.
     */
    public List<Group> groups() {
        return elements;
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
