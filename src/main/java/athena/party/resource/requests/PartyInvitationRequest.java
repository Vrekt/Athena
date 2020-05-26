package athena.party.resource.requests;

import athena.party.Parties;
import athena.util.json.builder.JsonObjectBuilder;
import com.google.gson.JsonObject;

public final class PartyInvitationRequest {

    public static JsonObject forUser(final String displayName) {
        return new JsonObjectBuilder()
                .add("urn:epic:invite:platformdata_s", "")
                .add("urn:epic:member:dn_s", displayName)
                .add("urn:epic:conn:platform_s", "WIN")
                .add("urn:epic:conn:type_s", "game")
                .add("urn:epic:cfg:build-id_s", Parties.BUILD_ID)
                .build();
    }

}
