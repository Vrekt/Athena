package athena.party.resource;

import athena.party.resource.configuration.PartyConfiguration;
import athena.party.resource.configuration.privacy.PartyPrivacy;
import athena.party.resource.configuration.types.Joinability;
import athena.party.resource.invite.PartyInvitation;
import athena.party.resource.member.PartyMember;
import athena.party.resource.meta.PartyMeta;
import athena.util.json.hooks.annotation.PostDeserialize;
import athena.util.json.hooks.annotation.PreDeserialize;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;

/**
 * Represents a Fortnite party.
 */
public final class Party {

    /**
     * The ID of this party.
     */
    @SerializedName("id")
    private String partyId;

    /**
     * When the party was created.
     */
    @SerializedName("created_at")
    private Instant createdAt;
    /**
     * When the party was updated.
     */
    @SerializedName("updated_at")
    private Instant updatedAt;

    /**
     * The party configuration
     */
    private PartyConfiguration config;

    /**
     * The party members
     */
    private List<PartyMember> members;

    /**
     * The party metadata.
     */
    private PartyMeta meta;

    /**
     * List of pending invites sent out for this party.
     */
    private List<PartyInvitation> invites;

    /**
     * Current revision for this party.
     */
    private int revision;

    /**
     * @return the party ID.
     */
    public String partyId() {
        return partyId;
    }

    /**
     * @return when this party was created.
     */
    public Instant createdAt() {
        return createdAt;
    }

    /**
     * @return when this party was updated.
     */
    public Instant updatedAt() {
        return updatedAt;
    }

    /**
     * @return the party configuration.
     */
    public PartyConfiguration config() {
        return config;
    }

    /**
     * @return the joinability of this party.
     */
    public Joinability joinability() {
        return config.joinability();
    }

    /**
     * @return the max size of this party
     */
    public int maxSize() {
        return config.maxSize();
    }

    /**
     * @return {@code true} if join confirmation is required.
     */
    public boolean joinConfirmation() {
        return config.joinConfirmation();
    }

    /**
     * @return {@code true} if the lobby connection has been started.
     */
    public boolean lobbyConnectionStarted() {
        return meta.lobbyConnectionStarted();
    }

    /**
     * @return the custom match key or {@code ""}
     */
    public String customMatchKey() {
        return meta.customMatchKey();
    }

    /**
     * @return {@code true} if the party is joined in progress.
     */
    public boolean partyIsJoinedInProgress() {
        return meta.partyIsJoinedInProgress();
    }

    /**
     * @return the build ID of this party.
     */
    public String buildId() {
        return meta.buildId();
    }

    /**
     * @return the presence permission
     */
    public String presencePermission() {
        return meta.presencePerm();
    }

    /**
     * @return {@code true} if this party is accepting members.
     */
    public boolean acceptingMembers() {
        return meta.acceptingMembers();
    }

    /**
     * @return {@code true} if allowed to join the game in progress.
     */
    public boolean allowJoinInProgress() {
        return meta.allowJoinInProgress();
    }

    /**
     * @return the current matchmaking result, ex "NoResults" for not matchmaking.
     */
    public String matchmakingResult() {
        return meta.matchmakingResult();
    }

    /**
     * @return the privacy settings for this party.
     */
    public PartyPrivacy privacySettings() {
        return meta.privacySettings();
    }

    /**
     * @return {@code true} if this party is public.
     */
    public boolean isPublic() {
        return privacySettings().partyType().equals("Public");
    }

    /**
     * @return {@code true} if this party is friends only.
     */
    public boolean isFriendsOnly() {
        return privacySettings().partyType().equals("FriendsOnly");
    }

    /**
     * @return {@code true} if this party is private.
     */
    public boolean isPrivate() {
        return privacySettings().partyType().equals("Private");
    }

    /**
     * @return the invite permission of this party.
     */
    public String invitePermission() {
        return meta.invitePermission();
    }

    /**
     * @return the matchmaking state, ex: "NotMatchmaking"
     */
    public String matchmakingState() {
        return meta.matchmakingState();
    }

    /**
     * @return the state of the party, where they are basically, ex: "BattleRoyaleView"
     */
    public String partyState() {
        return meta.partyState();
    }

    /**
     * @return {@code true} if party chat is enabled.
     */
    public boolean chatEnabled() {
        return meta.chatEnabled();
    }

    /**
     * @return the session ID of the game.
     */
    public String sessionId() {
        return meta.sessionId();
    }

    /**
     * @return the session join key of the game.
     */
    public String sessionKey() {
        return meta.sessionKey();
    }

    /**
     * @return {@code true} if this party can be joined.
     */
    public boolean canJoin() {
        return meta.canJoin();
    }

    /**
     * @return the current playlist for the party.
     */
    public String playlist() {
        return meta.playlistData().playlistName();
    }

    /**
     * @return the tournament ID for the playlist or {@code ""}
     */
    public String tournamentId() {
        return meta.playlistData().tournamentId();
    }

    /**
     * @return the event window ID for the tournament or {@code ""}
     */
    public String eventWindowId() {
        return meta.playlistData().eventWindowId();
    }

    /**
     * @return the region ID for playlists.
     */
    public String regionId() {
        return meta.playlistData().regionId();
    }

    /**
     * @return {@code true} if squad fill is enabled.
     */
    public boolean squadFill() {
        return meta.squadFill();
    }

    /**
     * @return a list of party members.
     */
    public List<PartyMember> members() {
        return members;
    }

    /**
     * @return a list of invites for this party.
     */
    public List<PartyInvitation> invites() {
        return invites;
    }

    /**
     * @return the current party revision
     */
    public int revision() {
        return revision;
    }

    /**
     * Write the rest of the privacy settings.
     */
    @PostDeserialize
    private void postDeserialize() {
        if (config != null && meta != null) {
            config.presencePermission(meta.presencePerm());
            config.acceptingMembers(Boolean.toString(meta.acceptingMembers()));
            config.joinRequestAction(meta.joinRequestAction());
            config.invitePermission(meta.invitePermission());
            config.notAcceptingMembersReason(Integer.toString(meta.notAcceptingMembersReason()));
            config.chatEnabled(Boolean.toString(meta.chatEnabled()));
            config.canJoin(Boolean.toString(meta.canJoin()));
        }
    }


    @PreDeserialize
    private void preDeserialize() {
        // TODO
    }

    public PartyMeta meta() {
        return meta;
    }
}
