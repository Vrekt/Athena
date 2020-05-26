package athena.party.resource.requests;

import athena.types.Platform;
import athena.util.json.builder.JsonArrayBuilder;
import athena.util.json.builder.JsonObjectBuilder;
import com.google.gson.JsonObject;
import org.jxmpp.jid.Jid;

/**
 * A party join request.
 */
public final class PartyJoinRequest {

    /**
     * Creates the party join request payload for the specified user.
     *
     * @param accountId   their account ID.
     * @param displayName their display name.
     * @param userJid     their user JID.
     * @param platform    their platform.
     * @return a new {@link JsonObject} payload
     */
    public static JsonObject forUser(String accountId, String displayName, Jid userJid, Platform platform) {
        return new JsonObjectBuilder()
                .add("connection", new JsonObjectBuilder()
                        .add("id", userJid.asUnescapedString())
                        .add("meta", new JsonObjectBuilder()
                                .add("urn:epic:conn:platform_s", platform.primaryName())
                                .add("urn:epic:conn:type_s", "game").build())
                        .add("yield_leadership", false)
                        .build())
                .add("meta", new JsonObjectBuilder()
                        .add("urn:epic:member:dn_s", displayName)
                        .add("urn:epic:member:joinrequestusers_j", new JsonObjectBuilder()
                                .add("users", new JsonArrayBuilder()
                                        .add(new JsonObjectBuilder()
                                                .add("id", accountId)
                                                .add("dn", displayName)
                                                .add("plat", platform.primaryName())
                                                .add("data", new JsonObjectBuilder()
                                                        .add("CrossplayPreference", "1")
                                                        .add("SubGame_u", "1").build().toString())
                                                .build())
                                        .build())
                                .build().toString())
                        .build())
                .build();
    }

}
