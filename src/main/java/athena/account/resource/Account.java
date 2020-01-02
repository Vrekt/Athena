package athena.account.resource;

import athena.account.resource.external.ExternalAuth;
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
     * Add this account as a friend.
     */
    public void addAsFriend() {
        Requests.executeVoidCall("Failed to add account " + accountId + " as a friend.", friendsPublicService.removeFriendByAccountId(localAccountId, accountId));
    }

    /**
     * Remove this account as a friend.
     */
    public void removeFriend() {
        Requests.executeVoidCall("Failed to remove friend " + accountId, friendsPublicService.removeFriendByAccountId(localAccountId, accountId));
    }

    /**
     * Block this account.
     */
    public void block() {
        Requests.executeVoidCall("Failed to block account " + accountId, friendsPublicService.blockFriendByAccountId(localAccountId, accountId));
    }

    /**
     * Unblock this account.
     */
    public void unblock() {
        Requests.executeVoidCall("Failed to unblock account " + accountId, friendsPublicService.unblockFriendByAccountId(localAccountId, accountId));
    }

}
