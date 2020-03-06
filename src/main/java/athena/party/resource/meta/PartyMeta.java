package athena.party.resource.meta;

import athena.party.resource.assignment.SquadAssignments;
import athena.party.resource.configuration.privacy.PartyPrivacy;
import athena.party.resource.playlist.PartyPlaylistData;
import athena.util.json.wrapped.annotation.WrappedArray;
import athena.util.json.wrapped.annotation.WrappedObject;
import com.google.gson.JsonArray;
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
    @WrappedObject("PartyMatchmakingInfo")
    private PartyMatchmakingInfo partyMatchmakingInfo;

    /**
     * An empty array []
     */
    @SerializedName("PlatformSessions_j")
    @WrappedArray(value = "PlatformSessions", type = PlatformSessions.class, useRawType = true)
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
    @WrappedObject("PrivacySettings")
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
    @WrappedArray(value = "TileStates", type = TileStates.class, useRawType = true)
    private TileStates tileStates;

    /**
     * Squad assignments within the party.
     * Basically its how each member is arranged in an index.
     */
    @SerializedName("RawSquadAssignments_j")
    @WrappedArray(value = "RawSquadAssignments", type = SquadAssignments.class)
    private List<SquadAssignments> squadAssignments;

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
    @WrappedObject("PlaylistData")
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
     * @return platform sessions
     */
    public PlatformSessions platformSessions() {
        return platformSessions;
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
     * @return []
     */
    public TileStates tileStates() {
        return tileStates;
    }

    /**
     * @return list of squad assignments
     */
    public List<SquadAssignments> squadAssignments() {
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

    public void partyMatchmakingInfo(PartyMatchmakingInfo partyMatchmakingInfo) {
        this.partyMatchmakingInfo = partyMatchmakingInfo;
    }

    public void platformSessions(PlatformSessions platformSessions) {
        this.platformSessions = platformSessions;
    }

    public void lobbyConnectionStarted(String lobbyConnectionStarted) {
        this.lobbyConnectionStarted = lobbyConnectionStarted;
    }

    public void customMatchKey(String customMatchKey) {
        this.customMatchKey = customMatchKey;
    }

    public void zoneInstanceId(String zoneInstanceId) {
        this.zoneInstanceId = zoneInstanceId;
    }

    public void partyIsJoinedInProgress(String partyIsJoinedInProgress) {
        this.partyIsJoinedInProgress = partyIsJoinedInProgress;
    }

    public void partyTypeId(String partyTypeId) {
        this.partyTypeId = partyTypeId;
    }

    public void isCriticalMission(String isCriticalMission) {
        this.isCriticalMission = isCriticalMission;
    }

    public void buildId(String buildId) {
        this.buildId = buildId;
    }

    public void presencePerm(String presencePerm) {
        this.presencePerm = presencePerm;
    }

    public void acceptingMembers(String acceptingMembers) {
        this.acceptingMembers = acceptingMembers;
    }

    public void allowJoinInProgress(String allowJoinInProgress) {
        this.allowJoinInProgress = allowJoinInProgress;
    }

    public void matchmakingResult(String matchmakingResult) {
        this.matchmakingResult = matchmakingResult;
    }

    public void privacySettings(PartyPrivacy privacySettings) {
        this.privacySettings = privacySettings;
    }

    public void joinRequestAction(String joinRequestAction) {
        this.joinRequestAction = joinRequestAction;
    }

    public void lfgTime(Instant lfgTime) {
        this.lfgTime = lfgTime;
    }

    public void invitePermission(String invitePermission) {
        this.invitePermission = invitePermission;
    }

    public void tileStates(TileStates tileStates) {
        this.tileStates = tileStates;
    }

    public void squadAssignments(List<SquadAssignments> squadAssignments) {
        this.squadAssignments = squadAssignments;
    }

    public void theaterId(String theaterId) {
        this.theaterId = theaterId;
    }

    public void notAcceptingMembersReason(String notAcceptingMembersReason) {
        this.notAcceptingMembersReason = notAcceptingMembersReason;
    }

    public void matchmakingState(String matchmakingState) {
        this.matchmakingState = matchmakingState;
    }

    public void partyState(String partyState) {
        this.partyState = partyState;
    }

    public void chatEnabled(String chatEnabled) {
        this.chatEnabled = chatEnabled;
    }

    public void sessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void zoneTileIndex(String zoneTileIndex) {
        this.zoneTileIndex = zoneTileIndex;
    }

    public void sessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public void canJoin(String canJoin) {
        this.canJoin = canJoin;
    }

    public void playlistData(PartyPlaylistData playlistData) {
        this.playlistData = playlistData;
    }

    public void squadFill(String squadFill) {
        this.squadFill = squadFill;
    }

    /**
     * Represents matchmaking information.
     */
    public static final class PartyMatchmakingInfo {
        /**
         * The build ID or -1
         * The hotfix version or -1
         */
        private int buildId, hotfixVersion;

        /**
         * Region, playlist, tournament, eventWindow and linkCode??
         * see {@link athena.events.Events}
         */
        private String regionId, playlistName, tournamentId, eventWindowId, linkCode;

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
        private JsonArray platformSessions;
    }

    /**
     * Tile states.
     * Usually empty.
     */
    private static final class TileStates {
        @SerializedName("TileStates")
        private JsonArray tileStates;
    }

}
