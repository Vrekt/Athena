package athena.account.resource.external;

import athena.types.Platform;

import java.util.List;
import java.util.Optional;

/**
 * Holds data about external auths, the platform, id and display name.
 */
public final class ExternalAuth {

    /**
     * Platform type
     */
    private Platform type;
    /**
     * "nintendo",
     * "XXX" (only present for PSN)
     * "psn_user_id, xuid, nsa_id"
     * "accountId",
     * external display name
     */
    private String externalAuthId, externalAuthIdType, accountId, externalDisplayName;
    /**
     * List of external auth IDs.
     * {
     * "id": "xxx",
     * "type": "nintendo_id"
     * },
     * {
     * "id": "xxx",
     * "type": "nsa_id"
     * }
     */
    private List<ExternalAuthId> authIds;

    /**
     * Get a {@link ExternalAuthId} by the type.
     *
     * @param type the type
     * @return a {@link Optional} containing the type if found.
     */
    public Optional<ExternalAuthId> getByType(String type) {
        return authIds.stream().filter(externalAuthId -> externalAuthId.type().equalsIgnoreCase(type)).findAny();
    }

    /**
     * @return the external auth ID type.
     */
    public String externalAuthIdType() {
        return externalAuthIdType;
    }

    /**
     * @return the account ID.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the platform of this external auth
     */
    public Platform platform() {
        return type;
    }

    /**
     * @return the ID of this external auth
     */
    public String externalAuthId() {
        if (externalAuthId != null) return externalAuthId;
        final var authId = getByType(platform().primaryName());
        if (authId.isEmpty()) return null;
        return authId.get().id();
    }

    /**
     * @return the external display name
     */
    public String externalDisplayName() {
        return externalDisplayName;
    }

    /**
     * @return the list of authIds.
     */
    public List<ExternalAuthId> authIds() {
        return authIds;
    }

}
