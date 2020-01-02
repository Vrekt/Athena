package athena.account.resource.external;

/**
 * Represents an external auth ID.
 * * {
 * * "id": "xxx",
 * * "type": "nintendo_id"
 * * },
 * * {
 * * "id": "xxx",
 * * "type": "nsa_id"
 * * }
 */
public final class ExternalAuthId {

    /**
     * The ID of the external auth and the type.
     */
    private String id, type;

    /**
     * @return the ID.
     */
    public String id() {
        return id;
    }

    /**
     * @return the type
     */
    public String type() {
        return type;
    }
}
