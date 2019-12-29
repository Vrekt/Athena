package athena.account.resource;

import athena.Athena;
import athena.adapter.ObjectJsonAdapter;
import athena.friend.service.FriendsPublicService;
import athena.util.request.Requests;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Fortnite account.
 */
public final class Account {

    /**
     * @return a new adapter for this object.
     */
    public static ObjectJsonAdapter<Account> newAdapter() {
        return new Adapter();
    }

    /**
     * Account ID and display name.
     */
    private final String accountId, displayName;

    /**
     * A list of external auths
     */
    private final Map<ExternalAuth.ExternalPlatform, ExternalAuth> externalAuth;

    /**
     * Used to manage friend actions from within this class.
     */
    private FriendsPublicService friendsPublicService;
    private String localAccountId;

    private Account(String accountId, String displayName, Map<ExternalAuth.ExternalPlatform, ExternalAuth> externalAuth,
                    String localAccountId, FriendsPublicService friendsPublicService) {
        this.accountId = accountId;
        this.displayName = displayName;
        this.externalAuth = externalAuth;
        this.localAccountId = localAccountId;
        this.friendsPublicService = friendsPublicService;
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
     * @return a map of external auths for this account.
     * Things such as the external platform (psn, xbl), the auth ID and the display name for that platform.
     */
    public Map<ExternalAuth.ExternalPlatform, ExternalAuth> externalAuth() {
        return externalAuth;
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
        Requests.executeVoidCall("Failed to block account " + accountId + ".", friendsPublicService.blockFriendByAccountId(localAccountId, accountId));
    }

    /**
     * Unblock this account.
     */
    public void unblock() {
        Requests.executeVoidCall("Failed to unblock account " + accountId, friendsPublicService.unblockFriendByAccountId(localAccountId, accountId));
    }

    /**
     * The adapter to convert JsonObjects to this type.
     */
    private static final class Adapter implements ObjectJsonAdapter<Account> {

        /**
         * Used for each individual account.
         */
        private String localAccountId;
        private FriendsPublicService friendsPublicService;

        @Override
        public Account deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final var object = json.isJsonArray() ? json.getAsJsonArray().get(0).getAsJsonObject() : json.getAsJsonObject();
            final var accountId = object.get("id").getAsString();
            final var displayName = object.get("displayName").getAsString();
            final var auths = object.getAsJsonObject("externalAuths");
            final var map = new HashMap<ExternalAuth.ExternalPlatform, ExternalAuth>();

            // go through all external auths and add them to a list if valid
            auths.keySet().forEach(key -> ExternalAuth.ExternalPlatform.get(key).ifPresent(platform -> {
                final var authObject = auths.getAsJsonObject(key);

                // For some reason there is externalAuthIdType instead of externalAuthId for some accounts i think its only with XBL.
                // so we need to support this.
                if (authObject.has("externalAuthIdType")) {
                    final var type = authObject.get("externalAuthIdType").getAsString();
                    final var authIdsArray = authObject.getAsJsonArray("authIds");
                    // go through all the auth ids to find the right type.
                    authIdsArray.forEach(jsonElement -> {
                        final var elementAsJsonObject = jsonElement.getAsJsonObject();
                        if (elementAsJsonObject.get("type").getAsString().equalsIgnoreCase(type)) {
                            // we have the correct ID.
                            final var authId = elementAsJsonObject.get("id").getAsString();
                            map.put(platform, new ExternalAuth(platform, authId, authObject.get("externalDisplayName").getAsString()));
                        }
                    });
                } else {
                    map.put(platform, new ExternalAuth(platform, authObject.get("externalAuthId").getAsString(), authObject.get("externalDisplayName").getAsString()));
                }
            }));

            return new Account(accountId, displayName, map, localAccountId, friendsPublicService);
        }

        @Override
        public void initialize(Athena athena) {
            this.localAccountId = athena.accountId();
            this.friendsPublicService = athena.friendsPublicService();
        }
    }
}
