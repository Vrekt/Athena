package athena.groups.requests.updating;

/**
 * A request used to update a group.
 *
 * @author Vrekt, Armisto
 */
public final class UpdateGroupRequest {

    /**
     * Motto, description and language.
     */
    private final String motto, description, lang;

    /**
     * Static factory method to create a new {@link UpdateGroupRequest}
     *
     * @param motto       the new motto
     * @param description the new description
     * @param lang        the new lang
     * @return a new {@link UpdateGroupRequest}
     */
    public static UpdateGroupRequest create(String motto, String description, String lang) {
        return new UpdateGroupRequest(motto, description, lang);
    }

    private UpdateGroupRequest(String motto, String description, String lang) {
        this.motto = motto;
        this.description = description;
        this.lang = lang;
    }
}
