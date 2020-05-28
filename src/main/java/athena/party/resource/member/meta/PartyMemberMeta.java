package athena.party.resource.member.meta;

import athena.party.resource.member.meta.assignments.MemberSquadAssignmentRequest;
import athena.party.resource.member.meta.banner.AthenaBanner;
import athena.party.resource.member.meta.battlepass.BattlePass;
import athena.party.resource.member.meta.challenges.AssistedChallenge;
import athena.party.resource.member.meta.cosmetic.CosmeticLoadout;
import athena.party.resource.member.meta.emote.FrontendEmote;
import athena.party.resource.member.meta.hero.CampaignHero;
import athena.types.Input;
import athena.types.Platform;
import athena.util.json.wrapped.annotation.FortniteObject;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Represents metadata of a party member.
 */
public final class PartyMemberMeta {

    /**
     * ?? - "None"
     */
    @SerializedName("FeatDefinition_s")
    private String featDefinition;

    /**
     * The campaign hero skin HID.
     */
    @SerializedName("CampaignHero_j")
    @FortniteObject("CampaignHero")
    private CampaignHero campaignHero;

    /**
     * The squad assignment request.
     */
    @SerializedName("MemberSquadAssignmentRequest_j")
    @FortniteObject("MemberSquadAssignmentRequest")
    private MemberSquadAssignmentRequest memberSquadAssignmentRequest;

    /**
     * ""
     */
    @SerializedName("ZoneInstanceId_s")
    private String zoneInstanceId;

    /**
     * {@code "true"} if spectating a member is available (this member?)
     */
    @SerializedName("SpectateAPartyMemberAvailable_b")
    private String spectateAPartyMemberAvailable;

    /**
     * ""
     */
    @SerializedName("PlatformUniqueId_s")
    private String platformUniqueId;

    /**
     * Assisted challenge information
     */
    @SerializedName("AssistedChallengeInfo_j")
    @FortniteObject("AssistedChallengeInfo")
    private AssistedChallenge challengeInfo;

    /**
     * {@code "false"} if voice chat is not muted
     */
    @SerializedName("urn:epic:member:voicechatmuted_b")
    private String voiceChatMuted;

    /**
     * Battle pass information
     */
    @SerializedName("BattlePassInfo_j")
    @FortniteObject("BattlePassInfo")
    private BattlePass battlePass;

    /**
     * The display name
     */
    @SerializedName("urn:epic:member:dn_s")
    private String displayName;

    /**
     * The matchmaking delay max
     */
    @SerializedName("HiddenMatchmakingDelayMax_U")
    private String hiddenMatchmakingDelayMax;

    /**
     * 1
     */
    @SerializedName("HomeBaseVersion_U")
    private String homeBaseVersion;

    /**
     * The current input type
     */
    @SerializedName("CurrentInputType_s")
    private Input currentInputType;

    /**
     * Emote information
     */
    @SerializedName("FrontendEmote_j")
    @FortniteObject("FrontendEmote")
    private FrontendEmote frontendEmote;

    /**
     * ""
     */
    @SerializedName("PlatformSessionId_s")
    private String platformSessionId;

    /**
     * 0
     */
    @SerializedName("MatchmakingLevel_U")
    private String matchmakingLevel;

    /**
     * Banner information
     */
    @SerializedName("AthenaBannerInfo_j")
    @FortniteObject("AthenaBannerInfo")
    private AthenaBanner banner;

    /**
     * {@code "true"} if content is preloaded
     */
    @SerializedName("HasPreloadedAthena_b")
    private String hasPreloaded;

    /**
     * {@code "Enabled"} if voice chat is enabled.
     */
    @SerializedName("VoiceChatStatus_s")
    private String voiceChatStatus;

    /**
     * "NotReady", "SittingOut" .... ETC
     */
    @SerializedName("GameReadiness_s")
    private String gameReadiness;

    /**
     * Represents the platform
     */
    @SerializedName("Platform_j")
    @FortniteObject("Platform")
    private FortnitePlatform platform;

    /**
     * The cosmetics
     */
    @SerializedName("AthenaCosmeticLoadout_j")
    @FortniteObject("AthenaCosmeticLoadout")
    private CosmeticLoadout cosmeticLoadout;

    /**
     * When the match started.
     */
    @SerializedName("UtcTimeStartedMatchAthena_s")
    private Instant timeStartedMatch;

    /**
     * The current location, ex: "PreLobby"
     */
    @SerializedName("Location_s")
    private String location;

    /**
     * "OptedIn"
     */
    @SerializedName("CrossplayPreference_s")
    private String crossplayPreference;

    /**
     * "Count"
     */
    @SerializedName("ReadyInputType_s")
    private String readyInputType;

    /**
     * Number of players left in the game if one is started.
     */
    @SerializedName("NumAthenaPlayersLeft_U")
    private String playersLeft;

    /**
     * The join request users.
     */
    //@SerializedName("urn:epic:member:joinrequestusers_j")
    //  private JoinRequestUsers joinRequestUsers;


    /**
     * Updates this meta.
     * TODO: Make this better
     *
     * @param other the other meta
     */
    public void updateMeta(PartyMemberMeta other) {
        if (other.featDefinition != null) featDefinition = other.featDefinition;
        if (other.campaignHero != null) campaignHero = other.campaignHero;
        if (other.memberSquadAssignmentRequest != null) memberSquadAssignmentRequest = other.memberSquadAssignmentRequest;
        if (other.zoneInstanceId != null) zoneInstanceId = other.zoneInstanceId;
        if (other.spectateAPartyMemberAvailable != null) spectateAPartyMemberAvailable = other.spectateAPartyMemberAvailable;
        if (other.platformUniqueId != null) platformUniqueId = other.platformUniqueId;
        if (other.challengeInfo != null) challengeInfo = other.challengeInfo;
        if (other.voiceChatMuted != null) voiceChatMuted = other.voiceChatMuted;
        if (other.battlePass != null) battlePass = other.battlePass;
        if (other.displayName != null) displayName = other.displayName;
        if (other.hiddenMatchmakingDelayMax != null) hiddenMatchmakingDelayMax = other.hiddenMatchmakingDelayMax;
        if (other.homeBaseVersion != null) homeBaseVersion = other.homeBaseVersion;
        if (other.currentInputType != null) currentInputType = other.currentInputType;
        if (other.frontendEmote != null) frontendEmote = other.frontendEmote;
        if (other.platformSessionId != null) platformSessionId = other.platformSessionId;
        if (other.matchmakingLevel != null) matchmakingLevel = other.matchmakingLevel;
        if (other.banner != null) banner = other.banner;
        if (other.hasPreloaded != null) hasPreloaded = other.hasPreloaded;
        if (other.voiceChatStatus != null) voiceChatStatus = other.voiceChatStatus;
        if (other.gameReadiness != null) gameReadiness = other.gameReadiness;
        if (other.platform != null) platform = other.platform;
        if (other.timeStartedMatch != null) timeStartedMatch = other.timeStartedMatch;
        if (other.location != null) location = other.location;
        if (other.crossplayPreference != null) crossplayPreference = other.crossplayPreference;
        if (other.readyInputType != null) readyInputType = other.readyInputType;
        if (other.playersLeft != null) playersLeft = other.playersLeft;
    }

    /**
     * @return "None"
     */
    public String featDefinition() {
        return featDefinition;
    }

    /**
     * @return the hero
     */
    public CampaignHero campaignHero() {
        return campaignHero;
    }

    /**
     * @return the squad assignment request.
     */
    public MemberSquadAssignmentRequest memberSquadAssignmentRequest() {
        return memberSquadAssignmentRequest;
    }

    /**
     * @return ""
     */
    public String zoneInstanceId() {
        return zoneInstanceId;
    }

    /**
     * @return {@code true} if spectating a party member is available.
     */
    public boolean spectateAPartyMemberAvailable() {
        return Boolean.parseBoolean(spectateAPartyMemberAvailable);
    }

    /**
     * @return "INVALID"
     */
    public String platformUniqueId() {
        return platformUniqueId;
    }

    /**
     * @return assisted challenge information
     */
    public AssistedChallenge challengeInfo() {
        return challengeInfo;
    }

    /**
     * @return {@code true} if voice chat is muted
     */
    public boolean voiceChatMuted() {
        return Boolean.parseBoolean(voiceChatMuted);
    }

    /**
     * @return battle pass information
     */
    public BattlePass battlePass() {
        return battlePass;
    }

    /**
     * @return the display name
     */
    public String displayName() {
        return displayName;
    }

    /**
     * @return the max matchmaking delay
     */
    public int hiddenMatchmakingDelayMax() {
        return Integer.parseInt(hiddenMatchmakingDelayMax);
    }

    /**
     * @return 1
     */
    public int homeBaseVersion() {
        return Integer.parseInt(homeBaseVersion);
    }

    /**
     * @return the current input type
     */
    public Input currentInputType() {
        return currentInputType;
    }

    /**
     * @return emote information
     */
    public FrontendEmote frontendEmote() {
        return frontendEmote;
    }

    /**
     * @return {@code true} if this member is emoting.
     */
    public boolean isEmoting() {
        return frontendEmote != null && !frontendEmote.emoteItemDef().equals("None");
    }

    /**
     * @return the emote name or {@code null} if no emote definition is present.
     */
    public String emoteName() {
        return frontendEmote == null ? null : frontendEmote.emoteItemDef();
    }

    /**
     * @return ""
     */
    public String platformSessionId() {
        return platformSessionId;
    }

    /**
     * @return 0
     */
    public int matchmakingLevel() {
        return Integer.parseInt(matchmakingLevel);
    }

    /**
     * @return the banner information
     */
    public AthenaBanner banner() {
        return banner;
    }

    /**
     * @return {@code true} if content has been preloaded
     */
    public boolean hasPreloaded() {
        return Boolean.parseBoolean(hasPreloaded);
    }

    /**
     * @return {@code "Enabled"} if enabled.
     */
    public String voiceChatStatus() {
        return voiceChatStatus;
    }

    /**
     * @return the game readiness, ex: "NotReady"
     */
    public String gameReadiness() {
        return gameReadiness;
    }

    /**
     * @return the platform
     */
    public Platform platform() {
        return platform.platform;
    }

    /**
     * @return cosmetic loadout information
     */
    public CosmeticLoadout cosmeticLoadout() {
        return cosmeticLoadout;
    }

    /**
     * @return when the match started (if any, otherwise 0001-01-01T00:00:00.000Z)
     */
    public Instant timeStartedMatch() {
        return timeStartedMatch;
    }

    /**
     * @return the location, ex: "PreLobby"
     */
    public String location() {
        return location;
    }

    /**
     * @return "OptedIn"
     */
    public String crossplayPreference() {
        return crossplayPreference;
    }

    /**
     * @return "Count"
     */
    public String readyInputType() {
        return readyInputType;
    }

    /**
     * @return amount of players left if in a game
     */
    public int playersLeft() {
        return Integer.parseInt(playersLeft);
    }

    public void featDefinition(String featDefinition) {
        this.featDefinition = featDefinition;
    }

    public void campaignHero(CampaignHero campaignHero) {
        this.campaignHero = campaignHero;
    }

    public void memberSquadAssignmentRequest(MemberSquadAssignmentRequest memberSquadAssignmentRequest) {
        this.memberSquadAssignmentRequest = memberSquadAssignmentRequest;
    }

    public void zoneInstanceId(String zoneInstanceId) {
        this.zoneInstanceId = zoneInstanceId;
    }

    public void spectateAPartyMemberAvailable(String spectateAPartyMemberAvailable) {
        this.spectateAPartyMemberAvailable = spectateAPartyMemberAvailable;
    }

    public void platformUniqueId(String platformUniqueId) {
        this.platformUniqueId = platformUniqueId;
    }

    public void challengeInfo(AssistedChallenge challengeInfo) {
        this.challengeInfo = challengeInfo;
    }

    public void voiceChatMuted(String voiceChatMuted) {
        this.voiceChatMuted = voiceChatMuted;
    }

    public void battlePass(BattlePass battlePass) {
        this.battlePass = battlePass;
    }

    public void displayName(String displayName) {
        this.displayName = displayName;
    }

    public void hiddenMatchmakingDelayMax(String hiddenMatchmakingDelayMax) {
        this.hiddenMatchmakingDelayMax = hiddenMatchmakingDelayMax;
    }

    public void homeBaseVersion(String homeBaseVersion) {
        this.homeBaseVersion = homeBaseVersion;
    }

    public void currentInputType(Input currentInputType) {
        this.currentInputType = currentInputType;
    }

    public void frontendEmote(FrontendEmote frontendEmote) {
        this.frontendEmote = frontendEmote;
    }

    public void platformSessionId(String platformSessionId) {
        this.platformSessionId = platformSessionId;
    }

    public void matchmakingLevel(String matchmakingLevel) {
        this.matchmakingLevel = matchmakingLevel;
    }

    public void banner(AthenaBanner banner) {
        this.banner = banner;
    }

    public void hasPreloaded(String hasPreloaded) {
        this.hasPreloaded = hasPreloaded;
    }

    public void voiceChatStatus(String voiceChatStatus) {
        this.voiceChatStatus = voiceChatStatus;
    }

    public void gameReadiness(String gameReadiness) {
        this.gameReadiness = gameReadiness;
    }

    public void platform(Platform platform) {
        this.platform = new FortnitePlatform();
        this.platform.platform = platform;
    }

    public void cosmeticLoadout(CosmeticLoadout cosmeticLoadout) {
        this.cosmeticLoadout = cosmeticLoadout;
    }

    public void timeStartedMatch(Instant timeStartedMatch) {
        this.timeStartedMatch = timeStartedMatch;
    }

    public void location(String location) {
        this.location = location;
    }

    public void crossplayPreference(String crossplayPreference) {
        this.crossplayPreference = crossplayPreference;
    }

    public void readyInputType(String readyInputType) {
        this.readyInputType = readyInputType;
    }

    public void playersLeft(String playersLeft) {
        this.playersLeft = playersLeft;
    }

    /**
     * Represents the platform object that contains their platform.
     */
    private static final class FortnitePlatform {
        @SerializedName("platformStr")
        private Platform platform;
    }

}
