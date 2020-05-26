package athena.party.resource.requests;

import athena.party.Parties;
import athena.party.resource.configuration.PartyConfiguration;
import athena.types.Platform;
import athena.util.json.builder.JsonObjectBuilder;
import com.google.gson.JsonObject;

/**
 * A request for creating parties.
 */
public final class PartyCreateRequest {

    /**
     * Creates the request for the new party.
     *
     * @param configuration the configuration
     * @param jid           the user JID
     * @param platform      the user platform
     * @return a {@link JsonObject} representing the payload.
     */
    public static JsonObject forParty(PartyConfiguration configuration, String jid, Platform platform) {
        return new JsonObjectBuilder()
                .add("config", new JsonObjectBuilder()
                        .add("join_confirmation", configuration.joinConfirmation())
                        .add("joinability", configuration.joinability().name())
                        .add("max_size", configuration.maxSize()).build())
                .add("join_info", new JsonObjectBuilder()
                        .add("connection", new JsonObjectBuilder()
                                .add("id", jid)
                                .add("meta", new JsonObjectBuilder()
                                        .add("urn:epic:conn:platform_s", platform.primaryName())
                                        .add("urn:epic:conn:type_s", "game").build()).build()).build())
                .add("meta", new JsonObjectBuilder()
                        .add("urn:epic:cfg:party-type-id_s", "default")
                        .add("urn:epic:cfg:build-id_s", Parties.BUILD_ID)
                        .add("urn:epic:cfg:join-request-action_s", "Manual")
                        .add("urn:epic:cfg:chat-enabled_b", true)
                        .build())
                .build();
    }

}
