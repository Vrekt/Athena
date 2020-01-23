package athena.groups.requests.creating;

import athena.groups.privacy.GroupPrivacy;

/**
 * A request used to create a group.
 *
 * @author Vrekt, Armisto
 */
public final class CreateGroupRequest {

    /**
     * The name, motto, description and language of this group.
     */
    private String name, motto, description, lang, mode;

    /**
     * Static factory method to create a new {@link CreateGroupRequest}
     *
     * @param name the name of the group.
     * @return a new {@link CreateGroupRequest}
     */
    public static CreateGroupRequest create(String name) {
        return new CreateGroupRequest(name, name, name, "en", GroupPrivacy.PUBLIC);
    }

    /**
     * Static factory method to create a new {@link CreateGroupRequest}
     *
     * @param name        the name of the group.
     * @param motto       the motto of the group.
     * @param description the description of the group.
     * @return a new {@link CreateGroupRequest}
     */
    public static CreateGroupRequest create(String name, String motto, String description) {
        return new CreateGroupRequest(name, motto, description, "en", GroupPrivacy.PUBLIC);
    }

    /**
     * Static factory method to create a new {@link CreateGroupRequest}
     *
     * @param name        the name of the group.
     * @param motto       the motto of the group.
     * @param description the description of the group.
     * @param lang        the language of the group, "en" for example
     * @return a new {@link CreateGroupRequest}
     */
    public static CreateGroupRequest create(String name, String motto, String description, String lang) {
        return new CreateGroupRequest(name, motto, description, lang, GroupPrivacy.PUBLIC);
    }

    /**
     * Static factory method to create a new {@link CreateGroupRequest}
     *
     * @param name        the name of the group.
     * @param motto       the motto of the group.
     * @param description the description of the group.
     * @param lang        the language of the group, "en" for example
     * @param privacy     the privacy of the group - cannot be changed later.
     * @return a new {@link CreateGroupRequest}
     */
    public static CreateGroupRequest create(String name, String motto, String description, String lang, GroupPrivacy privacy) {
        return new CreateGroupRequest(name, motto, description, lang, privacy);
    }

    private CreateGroupRequest(String name, String motto, String description, String lang, GroupPrivacy privacy) {
        this.name = name;
        this.motto = motto;
        this.description = description;
        this.lang = lang;
        this.mode = privacy.name();
    }

}
