package athena.party.resource;

import athena.exception.EpicGamesErrorException;
import athena.party.resource.assignment.SquadAssignment;
import athena.party.resource.configuration.PartyConfiguration;
import athena.party.resource.configuration.privacy.PartyPrivacy;
import athena.party.resource.meta.PartyMeta;
import athena.party.resource.playlist.PartyPlaylistData;
import athena.party.service.PartyService;
import athena.util.json.builder.JsonObjectBuilder;
import athena.util.request.Requests;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Used as a controller for a self-hosted party or one promoted in.
 */
public final class ClientParty {

    /**
     * The party service.
     */
    private final PartyService service;

    /**
     * GSON
     */
    private final Gson gson;

    /**
     * The current revision.
     */
    private int revision;

    /**
     * The party
     */
    private Party party;

    /**
     * The request queue.
     */
    private final BlockingQueue<Meta> queue = new LinkedBlockingQueue<>();

    /**
     * Initialize
     *
     * @param service the service
     * @param party   the party
     * @param gson    the gson
     */
    public ClientParty(PartyService service, Party party, Gson gson) {
        this.service = service;
        this.party = party;
        this.gson = gson;
    }

    /**
     * Set the custom key.
     *
     * @param customKey the custom key
     */
    public void setCustomKey(String customKey) {
        update(newMeta().customMatchKey(customKey));
    }

    /**
     * Set the privacy
     *
     * @param privacy the privacy
     */
    public void setPrivacy(PartyPrivacy privacy) {
        final var meta = newMeta().privacySettings(privacy);
        final var delete = new ArrayList<String>();

        if (!privacy.isPrivate()) {
            delete.add("urn:epic:cfg:not-accepting-members");
            meta.presencePerm("Anyone");
        } else {
            meta.notAcceptingMembersReason("7");
            meta.presencePerm("Noone");
        }
        update(meta, delete);
    }

    /**
     * Set the squad assignments
     *
     * @param squadAssignments the squad assignments
     */
    public void setSquadAssignments(List<SquadAssignment> squadAssignments) {
        update(newMeta().squadAssignments(squadAssignments));
    }

    /**
     * Set the playlist
     *
     * @param playlist the playlist
     */
    public void setPlaylist(PartyPlaylistData playlist) {
        update(newMeta().playlistData(playlist));
    }

    /**
     * Set squad fill
     *
     * @param squadFill the squad fill status
     */
    public void setSquadFill(boolean squadFill) {
        update(newMeta().squadFill(Boolean.toString(squadFill)));
    }

    /**
     * @return a new {@link PartyMeta} object
     */
    public PartyMeta newMeta() {
        return new PartyMeta();
    }

    /**
     * Initialize our base meta for when we create a party.
     *
     * @param privacy the privacy
     * @return this
     */
    public PartyMeta initializeBaseMeta(PartyPrivacy privacy) {
        return newMeta()
                .partyMatchmakingInfo(new PartyMeta.PartyMatchmakingInfo())
                .platformSessions(new PartyMeta.PlatformSessions())
                .lobbyConnectionStarted("false")
                .customMatchKey("")
                .zoneInstanceId("")
                .partyIsJoinedInProgress("false")
                .isCriticalMission("false")
                .presencePerm(privacy.isPrivate() ? "Noone" : "Anyone")
                .acceptingMembers(privacy.isPrivate() ? "false" : "true")
                .allowJoinInProgress("false")
                .matchmakingResult("NoResults")
                .privacySettings(privacy)
                .joinRequestAction("Manual")
                .lfgTime(Instant.parse("0001-01-01T00:00:00.000Z"))
                .invitePermission("Anyone")
                .tileStates(new PartyMeta.TileStates())
                .theaterId("")
                .notAcceptingMembersReason(privacy.isPrivate() ? "7" : null)
                .matchmakingState("NotMatchmaking")
                .partyState("BattleRoyaleView")
                .chatEnabled("true")
                .sessionId("")
                .zoneTileIndex("-1")
                .sessionKey("")
                .canJoin("true")
                .playlistData(new PartyPlaylistData("Playlist_DefaultSolo", "", "", "NAE"))
                .squadFill("false");
    }

    /**
     * Reset the party.
     *
     * @param party the party
     */
    public void resetParty(Party party) {
        this.party = party;
        revision = party.revision();
    }

    /**
     * Update the party instance
     *
     * @param party the party.
     */
    public void updateParty(Party party) {
        this.party = party;
    }

    /**
     * Update the party.
     *
     * @param meta the meta
     */
    public void update(PartyMeta meta) {
        updateInternal(new Meta(meta, new ArrayList<>()));
    }

    /**
     * Update the party.
     *
     * @param meta   the meta
     * @param delete the meta to delete
     */
    public void update(PartyMeta meta, List<String> delete) {
        updateInternal(new Meta(meta, delete));
    }

    /**
     * Update
     *
     * @param meta the meta
     */
    private void updateInternal(Meta meta) {
        System.err.println("Update " + (meta.update.privacySettings() == null ? "Hi" : "!"));
        if (queue.size() == 0) {
            queue.add(meta);
            // run async
            CompletableFuture.runAsync(() -> {
                Meta update;
                while ((update = queue.poll()) != null) {
                    // process the queue.
                    try {
                        dispatchInternal(update);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            System.err.println("UPDAT2");
            queue.add(meta);
        }
    }

    /**
     * Dispatch the patch request.
     *
     * @param meta the meta
     */
    private void dispatchInternal(Meta meta) {
        try {
            patch(payload(meta.update, meta.delete));
        } catch (EpicGamesErrorException exception) {
            if (exception.errorCode().equals("errors.com.epicgames.social.party.stale_revision")) {
                // retry the request, our revision is outdated.
                // grab the revision from the error.
                final var variables = exception.messageVars();
                revision = Integer.parseInt(variables.get(1));
                patch(payload(meta.update, meta.delete));
            } else {
                exception.printStackTrace();
                throw exception;
            }
        }
    }


    /**
     * Builds the payload
     *
     * @return the payload.
     */
    private JsonObject payload(PartyMeta updateMeta, List<String> deleteMeta) {
        return new JsonObjectBuilder()
                .add("config", buildConfig())
                .add("meta", new JsonObjectBuilder()
                        .add("delete", gson.toJsonTree(deleteMeta))
                        .add("update", gson.toJsonTree(updateMeta)).build())
                .add("party_state_overridden", new JsonObject())
                .add("party_privacy_type", party.config().joinability().name())
                .add("party_type", "DEFAULT")
                .add("party_sub_type", "default")
                .add("max_number_of_members", party.config().maxSize())
                .add("invite_ttl_seconds", PartyConfiguration.INVITE_TTL)
                .add("revision", revision)
                .build();
    }

    /**
     * Build the configuration
     *
     * @return the configuration
     */
    private JsonObject buildConfig() {
        return new JsonObjectBuilder()
                .add("join_confirmation", party.config().joinConfirmation())
                .add("joinability", party.config().joinability().name())
                .add("max_size", party.config().maxSize())
                .build();
    }

    /**
     * Dispatches the HTTP request and patches the party.
     *
     * @param payload the payload to send.
     * @throws EpicGamesErrorException if an error occurred
     */
    private void patch(JsonObject payload) throws EpicGamesErrorException {
        System.err.println(payload.toString());
        final var patch = service.updateParty(party.partyId(), payload);
        Requests.executeCall(patch);
    }

    /**
     * Holds the meta-update.
     */
    private static final class Meta {
        /**
         * The update
         */
        private final PartyMeta update;
        /**
         * The meta to delete.
         */
        private final List<String> delete;

        public Meta(PartyMeta update, List<String> delete) {
            this.update = update;
            this.delete = delete;
        }
    }

}
