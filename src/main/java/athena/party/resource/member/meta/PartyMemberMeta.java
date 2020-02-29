package athena.party.resource.member.meta;

import athena.party.resource.member.meta.assignments.MemberSquadAssignmentRequest;
import athena.party.resource.member.meta.banner.BannerInfo;
import athena.party.resource.member.meta.battlepass.BattlePass;
import athena.party.resource.member.meta.challenges.AssistedChallengeInfo;
import athena.party.resource.member.meta.cosmetic.CosmeticLoadout;
import athena.party.resource.member.meta.emote.FrontendEmote;
import athena.party.resource.member.meta.hero.CampaignHero;
import athena.types.Input;
import athena.types.Platform;
import athena.util.json.hooks.annotation.PostDeserialize;
import athena.util.json.wrapped.annotation.WrappedObject;
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
    @WrappedObject("CampaignHero")
    private CampaignHero campaignHero;

    /**
     * The squad assignment request.
     */
    @SerializedName("MemberSquadAssignmentRequest_j")
    @WrappedObject("MemberSquadAssignmentRequest")
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
    @WrappedObject("AssistedChallengeInfo")
    private AssistedChallengeInfo challengeInfo;

    /**
     * {@code "false"} if voice chat is not muted
     */
    @SerializedName("urn:epic:member:voicechatmuted_b")
    private String voiceChatMuted;

    /**
     * Battle pass information
     */
    @SerializedName("BattlePassInfo_j")
    @WrappedObject("BattlePassInfo")
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
    @WrappedObject("FrontendEmote")
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
    @WrappedObject("AthenaBannerInfo")
    private BannerInfo bannerInfo;

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
     * The raw platform
     */
    @SerializedName("Platform_j")
    @WrappedObject("Platform")
    private PlatformObject platformObject;

    /**
     * The platform, set after deserializing.
     */
    private Platform platform;

    /**
     * The cosmetics
     */
    @SerializedName("AthenaCosmeticLoadout_j")
    @WrappedObject("AthenaCosmeticLoadout")
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
     * Set the platform.
     */
    @PostDeserialize
    private void postDeserialize() {
        platform = platformObject.platform;
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
    public AssistedChallengeInfo challengeInfo() {
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
    public BannerInfo bannerInfo() {
        return bannerInfo;
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
        return platform;
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

    /**
     * Represents the platform object that contains their platform.
     */
    private static final class PlatformObject {
        @SerializedName("platformStr")
        private Platform platform;
    }

}
