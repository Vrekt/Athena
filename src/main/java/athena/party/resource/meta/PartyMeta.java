package athena.party.resource.meta;

import athena.party.resource.assignment.SquadAssignment;
import athena.party.resource.configuration.privacy.PartyPrivacy;
import athena.party.resource.playlist.PartyPlaylistData;
import athena.util.json.wrapped.annotation.FortniteArray;
import athena.util.json.wrapped.annotation.FortniteObject;
import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;

/**
 * Represents meta of a party.
 */
public final class PartyMeta {

    /**
     * Matchmaking information for this party.
     * Build, hotfix, region and playlist information.
     */
    @SerializedName("PartyMatchmakingInfo_j")
    @FortniteObject("PartyMatchmakingInfo")
    private PartyMatchmakingInfo partyMatchmakingInfo;

    /**
     * An empty array []
     */
    @SerializedName("PlatformSessions_j")
    @FortniteArray(value = "PlatformSessions", type = PlatformSessions.class, isConstant = true, isNotList = true)
    private PlatformSessions platformSessions;

    /**
     * {@code true} if matchmaking connection has started.
     */
    @SerializedName("LobbyConnectionStarted_b")
    private String lobbyConnectionStarted;

    @SerializedName("CustomMatchKey_s")
    private String customMatchKey;

    /**
     * ""
     */
    @SerializedName("ZoneInstanceId_s")
    private String zoneInstanceId;

    /**
     * {@code true} if the party is joined while in a game.
     */
    @SerializedName("PartyIsJoinedInProgress_b")
    private String partyIsJoinedInProgress;

    /**
     * "DEFAULT"
     */
    @SerializedName("urn:epic:cfg:party-type-id_s")
    private String partyTypeId;

    /**
     * {@code false}
     */
    @SerializedName("SessionIsCriticalMission_b")
    private String isCriticalMission;

    /**
     * The current build ID.
     */
    @SerializedName("urn:epic:cfg:build-id_s")
    private String buildId;

    /**
     * Presence permission related to privacy settings.
     */
    @SerializedName("urn:epic:cfg:presence-perm_s")
    private String presencePerm;

    /**
     * {@code true} if this party is accepting members.
     */
    @SerializedName("urn:epic:cfg:accepting-members_b")
    private String acceptingMembers;

    /**
     * {@code true} if the game the party is in is allowed to join in progress.
     */
    @SerializedName("AllowJoinInProgress_b")
    private String allowJoinInProgress;

    /**
     * The current matchmaking result when q'ing for a match.
     * Default is "NoResults"
     */
    @SerializedName("MatchmakingResult_s")
    private String matchmakingResult;

    /**
     * The party privacy settings.
     */
    @SerializedName("PrivacySettings_j")
    @FortniteObject("PrivacySettings")
    private PartyPrivacy privacySettings;

    /**
     * The join request action, ex: "Manual"
     */
    @SerializedName("urn:epic:cfg:join-request-action_s")
    private String joinRequestAction;

    /**
     * The looking for game time (i assumed when it started?)
     */
    @SerializedName("LFGTime_s")
    private Instant lfgTime;

    /**
     * The invite permission related to privacy settings.
     */
    @SerializedName("urn:epic:cfg:invite-perm_s")
    private String invitePermission;

    /**
     * Tile states usually []
     */
    @SerializedName("TileStates_j")
    @FortniteArray(value = "TileStates", type = TileStates.class, isConstant = true, isNotList = true)
    @Expose(deserialize = false)
    private TileStates tileStates;

    /**
     * Squad assignments within the party.
     * Basically its how each member is arranged in an index.
     */
    @SerializedName("RawSquadAssignments_j")
    @FortniteArray(value = "RawSquadAssignments", type = SquadAssignment.class)
    private List<SquadAssignment> squadAssignments;

    /**
     * ""
     */
    @SerializedName("TheaterId_s")
    private String theaterId;

    /**
     * The not accepting members reason, related to privacy settings.
     */
    @SerializedName("urn:epic:cfg:not-accepting-members-reason_i")
    private String notAcceptingMembersReason;

    /**
     * The matchmaking state, ex: "NotMatchmaking"
     */
    @SerializedName("MatchmakingState_s")
    private String matchmakingState;

    /**
     * The party state, can change depending on lobby, game, etc
     * "BattleRoyaleView"
     */
    @SerializedName("PartyState_s")
    private String partyState;

    /**
     * {@code true} if party chat is enabled.
     */
    @SerializedName("urn:epic:cfg:chat-enabled_b")
    private String chatEnabled;

    /**
     * The game session ID.
     */
    @SerializedName("PrimaryGameSessionId_s")
    private String sessionId;

    /**
     * 0
     */
    @SerializedName("ZoneTileIndex_U")
    private String zoneTileIndex;

    /**
     * The game session key used to join the game.
     */
    @SerializedName("GameSessionKey_s")
    private String sessionKey;

    @SerializedName("urn:epic:cfg:can-join_b")
    private String canJoin;

    /**
     * The playlist data of the party.
     * Playlist name, tournament info, region.
     */
    @SerializedName("PlaylistData_j")
    @FortniteObject("PlaylistData")
    private PartyPlaylistData playlistData;

    /**
     * {@code true} if squad fill is enabled.
     */
    @SerializedName("AthenaSquadFill_b")
    private String squadFill;

    /**
     * Updates this meta.
     * TODO: Make this better
     *
     * @param other the other meta
     */
    public void updateMeta(PartyMeta other) {
        if (other.partyMatchmakingInfo != null) partyMatchmakingInfo = other.partyMatchmakingInfo;
        if (other.platformSessions != null) platformSessions = other.platformSessions;
        if (other.lobbyConnectionStarted != null) lobbyConnectionStarted = other.lobbyConnectionStarted;
        if (other.customMatchKey != null) customMatchKey = other.customMatchKey;
        if (other.zoneInstanceId != null) zoneInstanceId = other.zoneInstanceId;
        if (other.partyIsJoinedInProgress != null) partyIsJoinedInProgress = other.partyIsJoinedInProgress;
        if (other.partyTypeId != null) partyTypeId = other.partyTypeId;
        if (other.isCriticalMission != null) isCriticalMission = other.isCriticalMission;
        if (other.buildId != null) buildId = other.buildId;
        if (other.presencePerm != null) presencePerm = other.presencePerm;
        if (other.acceptingMembers != null) acceptingMembers = other.acceptingMembers;
        if (other.allowJoinInProgress != null) allowJoinInProgress = other.allowJoinInProgress;
        if (other.matchmakingResult != null) matchmakingResult = other.matchmakingResult;
        if (other.privacySettings != null) privacySettings = other.privacySettings;
        if (other.joinRequestAction != null) joinRequestAction = other.joinRequestAction;
        if (other.lfgTime != null) lfgTime = other.lfgTime;
        if (other.invitePermission != null) invitePermission = other.invitePermission;
        if (other.tileStates != null) tileStates = other.tileStates;
        if (other.squadAssignments != null) squadAssignments = other.squadAssignments;
        if (other.theaterId != null) theaterId = other.theaterId;
        if (other.notAcceptingMembersReason != null) notAcceptingMembersReason = other.notAcceptingMembersReason;
        if (other.matchmakingState != null) matchmakingState = other.matchmakingState;
        if (other.partyState != null) partyState = other.partyState;
        if (other.chatEnabled != null) chatEnabled = other.chatEnabled;
        if (other.sessionId != null) sessionId = other.sessionId;
        if (other.zoneTileIndex != null) zoneTileIndex = other.zoneTileIndex;
        if (other.sessionKey != null) sessionKey = other.sessionKey;
        if (other.playlistData != null) playlistData = other.playlistData;
        if (other.squadFill != null) squadFill = other.squadFill;
    }


    /**
     * @return the party matchmaking info
     */
    public PartyMatchmakingInfo partyMatchmakingInfo() {
        return partyMatchmakingInfo;
    }

    /**
     * @return {@code true} if the lobby connection has been started
     */
    public boolean lobbyConnectionStarted() {
        if (lobbyConnectionStarted == null) return false;
        return Boolean.parseBoolean(lobbyConnectionStarted);
    }

    /**
     * @return the custom match key, or {@code ""}
     */
    public String customMatchKey() {
        return customMatchKey;
    }

    /**
     * @return ""
     */
    public String zoneInstanceId() {
        return zoneInstanceId;
    }

    /**
     * @return {@code true} if the party is joined in progress
     */
    public boolean partyIsJoinedInProgress() {
        if (partyIsJoinedInProgress == null) return false;
        return Boolean.parseBoolean(partyIsJoinedInProgress);
    }

    /**
     * @return "default"
     */
    public String partyTypeId() {
        return partyTypeId;
    }

    /**
     * @return if its a critical mission
     */
    public boolean isCriticalMission() {
        if (isCriticalMission == null) return false;
        return Boolean.parseBoolean(isCriticalMission);
    }

    /**
     * @return the build ID
     */
    public String buildId() {
        return buildId;
    }

    /**
     * @return the presence permission
     */
    public String presencePerm() {
        return presencePerm;
    }

    /**
     * @return {@code true} if accepting members
     */
    public boolean acceptingMembers() {
        if (acceptingMembers == null) return false;
        return Boolean.parseBoolean(acceptingMembers);
    }

    /**
     * @return {@code true} if allowed to join in progress
     */
    public boolean allowJoinInProgress() {
        if (allowJoinInProgress == null) return false;
        return Boolean.parseBoolean(allowJoinInProgress);
    }

    /**
     * @return the matchmaking result, ex: "NoResults"
     */
    public String matchmakingResult() {
        return matchmakingResult;
    }

    /**
     * @return party privacy settings
     */
    public PartyPrivacy privacySettings() {
        return privacySettings;
    }

    /**
     * @return "manual"
     */
    public String joinRequestAction() {
        return joinRequestAction;
    }

    /**
     * @return the "looking-for-game" time
     */
    public Instant lfgTime() {
        return lfgTime;
    }

    /**
     * @return the invite permission
     */
    public String invitePermission() {
        return invitePermission;
    }

    /**
     * @return list of squad assignments
     */
    public List<SquadAssignment> squadAssignments() {
        return squadAssignments;
    }

    /**
     * @return ""
     */
    public String theaterId() {
        return theaterId;
    }

    /**
     * @return 7 for private parties.
     */
    public int notAcceptingMembersReason() {
        if (notAcceptingMembersReason == null) return -1;
        return Integer.parseInt(notAcceptingMembersReason);
    }

    /**
     * @return the matchmaking state, ex: "NotMatchmaking"
     */
    public String matchmakingState() {
        return matchmakingState;
    }

    /**
     * @return the party state, ex: "BattleRoyaleView"
     */
    public String partyState() {
        return partyState;
    }

    /**
     * @return {@code true} if chat is enabled.
     */
    public boolean chatEnabled() {
        if (chatEnabled == null) return false;
        return Boolean.parseBoolean(chatEnabled);
    }

    /**
     * @return the session ID.
     */
    public String sessionId() {
        return sessionId;
    }

    /**
     * @return -1
     */
    public int zoneTileIndex() {
        if (zoneTileIndex == null) return -1;
        return Integer.parseInt(zoneTileIndex);
    }

    /**
     * @return the game session key.
     */
    public String sessionKey() {
        return sessionKey;
    }

    /**
     * @return {@code true} if joinable.
     */
    public boolean canJoin() {
        if (canJoin == null) return false;
        return Boolean.parseBoolean(canJoin);
    }

    /**
     * @return the playlist data
     */
    public PartyPlaylistData playlistData() {
        return playlistData;
    }

    /**
     * @return {@code true} if squad fill is enabled.
     */
    public boolean squadFill() {
        if (squadFill == null) return false;
        return Boolean.parseBoolean(squadFill);
    }

    /**
     * Set playlist matchmaking info
     *
     * @param partyMatchmakingInfo the matchmaking info
     * @return this
     */
    public PartyMeta partyMatchmakingInfo(PartyMatchmakingInfo partyMatchmakingInfo) {
        this.partyMatchmakingInfo = partyMatchmakingInfo;
        return this;
    }

    /**
     * Create new platform sessions
     *
     * @return this
     */
    public PartyMeta platformSessions() {
        this.platformSessions = new PlatformSessions();
        return this;
    }

    /**
     * Set connection started.
     *
     * @param lobbyConnectionStarted {@code "true" or "false"}
     * @return this
     */
    public PartyMeta lobbyConnectionStarted(String lobbyConnectionStarted) {
        this.lobbyConnectionStarted = lobbyConnectionStarted;
        return this;
    }

    /**
     * Set the custom match key.
     *
     * @param customMatchKey the custom key
     * @return this
     */
    public PartyMeta customMatchKey(String customMatchKey) {
        this.customMatchKey = customMatchKey;
        return this;
    }

    /**
     * Set the zone instance ID, usually empty.
     *
     * @param zoneInstanceId the zone instance ID.
     * @return this
     */
    public PartyMeta zoneInstanceId(String zoneInstanceId) {
        this.zoneInstanceId = zoneInstanceId;
        return this;
    }

    /**
     * Set party joined in progress.
     *
     * @param partyIsJoinedInProgress {@code "true" or "false"}
     * @return this
     */
    public PartyMeta partyIsJoinedInProgress(String partyIsJoinedInProgress) {
        this.partyIsJoinedInProgress = partyIsJoinedInProgress;
        return this;
    }

    /**
     * Set the party type ID - "default"
     *
     * @param partyTypeId the party type ID.
     * @return this
     */
    public PartyMeta partyTypeId(String partyTypeId) {
        this.partyTypeId = partyTypeId;
        return this;
    }

    /**
     * Set if critical mission.
     *
     * @param isCriticalMission {@code "true" or "false"}
     * @return this
     */
    public PartyMeta isCriticalMission(String isCriticalMission) {
        this.isCriticalMission = isCriticalMission;
        return this;
    }

    /**
     * Set the party build ID.
     *
     * @param buildId the build ID.
     * @return this
     */
    public PartyMeta buildId(String buildId) {
        this.buildId = buildId;
        return this;
    }

    /**
     * Set the presence permission
     *
     * @param presencePerm the presence permission
     * @return this
     */
    public PartyMeta presencePerm(String presencePerm) {
        this.presencePerm = presencePerm;
        return this;
    }

    /**
     * Set accepting members.
     *
     * @param acceptingMembers {@code "true" or "false"}
     * @return this
     */
    public PartyMeta acceptingMembers(String acceptingMembers) {
        this.acceptingMembers = acceptingMembers;
        return this;
    }

    /**
     * Set allow join in progress
     *
     * @param allowJoinInProgress {@code "true" or "false"}
     * @return this
     */
    public PartyMeta allowJoinInProgress(String allowJoinInProgress) {
        this.allowJoinInProgress = allowJoinInProgress;
        return this;
    }

    /**
     * Set the matchmaking result.
     *
     * @param matchmakingResult the result
     * @return this
     */
    public PartyMeta matchmakingResult(String matchmakingResult) {
        this.matchmakingResult = matchmakingResult;
        return this;
    }

    /**
     * Set the privacy settings
     *
     * @param privacySettings the privacy settings
     * @return this
     */
    public PartyMeta privacySettings(PartyPrivacy privacySettings) {
        this.privacySettings = privacySettings;
        return this;
    }

    /**
     * Set the join request action, usually "Manual"
     *
     * @param joinRequestAction the action
     * @return this
     */
    public PartyMeta joinRequestAction(String joinRequestAction) {
        this.joinRequestAction = joinRequestAction;
        return this;
    }

    /**
     * Set the 'looking for game' time.
     *
     * @param lfgTime the LFG time
     * @return this
     */
    public PartyMeta lfgTime(Instant lfgTime) {
        this.lfgTime = lfgTime;
        return this;
    }

    /**
     * Set the invite permission
     *
     * @param invitePermission the invite permission
     * @return this
     */
    public PartyMeta invitePermission(String invitePermission) {
        this.invitePermission = invitePermission;
        return this;
    }

    /**
     * Create new tile states
     *
     * @return this
     */
    public PartyMeta tileStates() {
        this.tileStates = new TileStates();
        return this;
    }

    /**
     * Set the squad assignments
     *
     * @param squadAssignments the list of squad assignments
     * @return this
     */
    public PartyMeta squadAssignments(List<SquadAssignment> squadAssignments) {
        this.squadAssignments = squadAssignments;
        return this;
    }

    /**
     * Set the theater ID, usually ""
     *
     * @param theaterId the theater ID.
     * @return this
     */
    public PartyMeta theaterId(String theaterId) {
        this.theaterId = theaterId;
        return this;
    }

    /**
     * Set the not accepting members reason. {@code "7"} for private
     *
     * @param notAcceptingMembersReason the reason
     * @return this
     */
    public PartyMeta notAcceptingMembersReason(String notAcceptingMembersReason) {
        this.notAcceptingMembersReason = notAcceptingMembersReason;
        return this;
    }

    /**
     * Set the matchmaking state, ex: "BattleRoyaleView"
     *
     * @param matchmakingState the state
     * @return this
     */
    public PartyMeta matchmakingState(String matchmakingState) {
        this.matchmakingState = matchmakingState;
        return this;
    }

    /**
     * Set the party state, ex: "BattleRoyaleView"
     *
     * @param partyState the state
     * @return this
     */
    public PartyMeta partyState(String partyState) {
        this.partyState = partyState;
        return this;
    }

    /**
     * Set chat enabled.
     *
     * @param chatEnabled {@code "Enabled" or "Disabled"}
     * @return this
     */
    public PartyMeta chatEnabled(String chatEnabled) {
        this.chatEnabled = chatEnabled;
        return this;
    }

    /**
     * Set the game session ID.
     *
     * @param sessionId the session ID.
     * @return this
     */
    public PartyMeta sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    /**
     * Usually -1
     *
     * @param zoneTileIndex the index
     * @return this
     */
    public PartyMeta zoneTileIndex(String zoneTileIndex) {
        this.zoneTileIndex = zoneTileIndex;
        return this;
    }

    /**
     * Set the game session join key
     *
     * @param sessionKey the key
     * @return this
     */
    public PartyMeta sessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
        return this;
    }

    /**
     * Set if can join.
     *
     * @param canJoin {@code "true" or "false}
     * @return this
     */
    public PartyMeta canJoin(String canJoin) {
        this.canJoin = canJoin;
        return this;
    }

    /**
     * Set the playlist data
     *
     * @param playlistData the playlist data
     * @return this
     */
    public PartyMeta playlistData(PartyPlaylistData playlistData) {
        this.playlistData = playlistData;
        return this;
    }

    /**
     * Set the squad fill status.
     *
     * @param squadFill the squad fill state
     * @return this
     */
    public PartyMeta squadFill(String squadFill) {
        this.squadFill = squadFill;
        return this;
    }

    /**
     * Represents matchmaking information.
     */
    public static final class PartyMatchmakingInfo {
        /**
         * The build ID or -1
         * The hotfix version or -1
         */
        private int buildId = -1, hotfixVersion = -1;

        /**
         * Region, playlist, tournament, eventWindow and linkCode??
         * see {@link athena.events.Events}
         */
        private String regionId = "", playlistName = "None", tournamentId = "", eventWindowId = "", linkCode = "";

        /**
         * @return the build ID.
         */
        public int buildId() {
            return buildId;
        }

        /**
         * @return the hotfix version
         */
        public int hotfixVersion() {
            return hotfixVersion;
        }

        /**
         * @return region ID.
         */
        public String regionId() {
            return regionId;
        }

        /**
         * @return playlist name or "None"
         */
        public String playlistName() {
            return playlistName;
        }

        /**
         * @return tournament ID.
         */
        public String tournamentId() {
            return tournamentId;
        }

        /**
         * @return event window ID.
         */
        public String eventWindowId() {
            return eventWindowId;
        }

        /**
         * @return ??
         */
        public String linkCode() {
            return linkCode;
        }
    }

    /**
     * Platform sessions.
     * Usually empty.
     */
    private static final class PlatformSessions {
        @SerializedName("PlatformSessions")
        private JsonArray platformSessions = new JsonArray();
    }

    /**
     * Tile states.
     * Usually empty.
     */
    private static final class TileStates {
        @SerializedName("TileStates")
        private JsonArray tileStates = new JsonArray();
    }

}
