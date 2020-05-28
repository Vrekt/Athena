package athena.party.resource.member.meta.joinrequest;

import athena.types.Platform;
import athena.util.json.wrapped.annotation.FortniteArray;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class JoinRequestUsers {

    //     "urn:epic:member:joinrequestusers_j": "{\"users\":[{\"id\":\"192fd8eb7a144605bfda0e386f10ed18\",\"dn\":\"vrekt_\",\"plat\":\"WIN\",\"data\":\"{\\\"CrossplayPreference_i\\\":\\\"1\\\",\\\"SubGame_u\\\":\\\"1\\\"}\"}]}"

    /**
     * List of users.
     */
    @FortniteArray(value = "users", type = User.class)
    private List<User> users;

    /**
     * @return the users
     */
    public List<User> users() {
        return users;
    }

    /**
     * Represents the joining user.
     */
    public static final class User {

        /**
         * Account ID, display name and platform.
         */
        private String id, dn;
        private Platform plat;
        /**
         * The user data.
         */
        private UserData data;

        public String id() {
            return id;
        }

        public String dn() {
            return dn;
        }

        public Platform plat() {
            return plat;
        }

        public UserData data() {
            return data;
        }
    }

    /**
     * Represents the user join data.
     */
    public static final class UserData {
        /**
         * The crossplay preference, usually 1
         */
        @SerializedName("CrossplayPreference_i")
        private String crossPlayPreference;
        /**
         * The sub-game, usually 1
         */
        @SerializedName("SubGame_u")
        private String subGame;

        public String crossPlayPreference() {
            return crossPlayPreference;
        }

        public String subGame() {
            return subGame;
        }
    }

}
