package athena.account.resource;

import athena.account.resource.external.ExternalAuth;
import athena.types.Platform;
import athena.util.json.PostProcessable;
import athena.util.request.Requests;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Represents a Fortnite account.
 */
public final class Account extends PostProcessable {

    /**
     * The account ID for this account.
     */
    @SerializedName("id")
    private String accountId;
    /**
     * The display name of this account.
     */
    private String displayName;
    /**
     * A list of external auths.
     */
    private Map<String, ExternalAuth> externalAuths;

    private Account() {
    }

    /**
     * @return the ID of this account.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the display name of this account.
     */
    public String displayName() {
        return displayName;
    }

    /**
     * @return a map of external auths.
     */
    public Map<String, ExternalAuth> externalAuths() {
        return externalAuths;
    }

    /**
     * Checks if the external auth is present.
     *
     * @param platform the platform, ex: "psn, "xbl"
     * @return {@code true} if the external auth is present.
     */
    public boolean hasExternalAuth(String platform) {
        return externalAuths.containsKey(platform);
    }

    /**
     * Checks if the external auth is present.
     *
     * @param platform the platform.
     * @return {@code true} if the external auth is present.
     */
    public boolean hasExternalAuth(Platform platform) {
        return externalAuths.values().stream().anyMatch(externalAuth -> externalAuth.platform() == platform);
    }

    /**
     * Get the external auth.
     *
     * @param platform the platform, ex: "psn, "xbl"
     * @return the external auth or {@code null} if not found.
     */
    public ExternalAuth getExternalAuth(String platform) {
        return externalAuths.get(platform);
    }

    /**
     * Get the external auth.
     *
     * @param platform the platform
     * @return the external auth.
     * @throws java.util.NoSuchElementException if the external auth was not found.
     */
    public ExternalAuth getExternalAuth(Platform platform) {
        return externalAuths.values().stream().filter(externalAuth -> externalAuth.platform() == platform).findAny().orElseThrow();
    }

    /**
     * Add this account as a friend.
     */
    public void friend() {
        Requests.executeVoidCall(friendsPublicService.add(localAccountId, accountId));
    }

    /**
     * Remove this account as a friend.
     */
    public void unfriend() {
        Requests.executeVoidCall(friendsPublicService.remove(localAccountId, accountId));
    }

    /**
     * Block this account.
     */
    public void block() {
        Requests.executeVoidCall(friendsPublicService.block(localAccountId, accountId));
    }

    /**
     * Unblock this account.
     */
    public void unblock() {
        Requests.executeVoidCall(friendsPublicService.unblock(localAccountId, accountId));
    }

}
