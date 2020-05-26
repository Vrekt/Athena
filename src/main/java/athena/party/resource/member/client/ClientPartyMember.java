package athena.party.resource.member.client;

import athena.context.DefaultAthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.party.resource.member.meta.PartyMemberMeta;
import athena.party.resource.member.meta.assignments.MemberSquadAssignmentRequest;
import athena.party.resource.member.meta.banner.AthenaBanner;
import athena.party.resource.member.meta.battlepass.BattlePass;
import athena.party.resource.member.meta.challenges.AssistedChallenge;
import athena.party.resource.member.meta.cosmetic.CosmeticLoadout;
import athena.party.resource.member.meta.cosmetic.variant.CosmeticVariant;
import athena.party.resource.member.meta.emote.FrontendEmote;
import athena.party.resource.member.meta.hero.CampaignHero;
import athena.party.resource.member.meta.readiness.GameReadiness;
import athena.party.service.PartyService;
import athena.types.Input;
import athena.types.Platform;
import athena.util.json.builder.JsonObjectBuilder;
import athena.util.request.Requests;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.time.Instant;
import java.util.List;

/**
 * Represents a client party member.
 * This class acts a controller
 */
public final class ClientPartyMember {

    /**
     * Essentials
     */
    private final DefaultAthenaContext context;
    private final PartyService service;
    private final Gson gson;

    /**
     * Holds the meta information that will be updated.
     */
    private PartyMemberMeta updateMeta = new PartyMemberMeta();

    /**
     * Holds cosmetic information.
     */
    private final CosmeticLoadout cosmeticLoadout = new CosmeticLoadout();
    private final CampaignHero campaignHero = new CampaignHero();

    /**
     * Party ID and revision information
     */
    private String partyId;
    private int revision;

    /**
     * Keeps track of emoting.
     */
    private boolean isEmoting;

    public ClientPartyMember(DefaultAthenaContext context) {
        this.context = context;
        this.service = context.partyService();
        this.gson = context.gson();
        initializeBaseMeta();
    }

    /**
     * Set this clients party ID and revision.
     *
     * @param partyId the party ID.
     */
    public void set(String partyId) {
        this.partyId = partyId;
        revision = 0;
    }

    /**
     * Initialize our meta with base default values.
     */
    public void initializeBaseMeta() {
        updateMeta.featDefinition("None");
        updateMeta.memberSquadAssignmentRequest(new MemberSquadAssignmentRequest());
        updateMeta.zoneInstanceId("");
        updateMeta.spectateAPartyMemberAvailable("false");
        updateMeta.platformUniqueId("INVALID");
        updateMeta.challengeInfo(new AssistedChallenge());
        updateMeta.voiceChatMuted("false");
        updateMeta.battlePass(new BattlePass());
        updateMeta.displayName(context.displayName());
        updateMeta.hiddenMatchmakingDelayMax("0");
        updateMeta.homeBaseVersion("1");
        updateMeta.currentInputType(Input.KEYBOARD_AND_MOUSE);
        updateMeta.frontendEmote(new FrontendEmote());
        updateMeta.platformSessionId("");
        updateMeta.matchmakingLevel("0");
        updateMeta.banner(new AthenaBanner());
        updateMeta.hasPreloaded("false");
        updateMeta.voiceChatStatus("Enabled");
        updateMeta.gameReadiness("NotReady");
        updateMeta.platform(context.platform());
        updateMeta.timeStartedMatch(Instant.parse("0001-01-01T00:00:00.000Z"));
        updateMeta.location("PreLobby");
        updateMeta.crossplayPreference("OptedIn");
        updateMeta.readyInputType("Count");
        updateMeta.playersLeft("0");
    }

    /**
     * Sets the desired skin/character.
     *
     * @param character the character value.
     */
    public void setCharacter(String character) {
        // set our hero and character definition.
        final var asHero = character.replace("CID", "HID");
        cosmeticLoadout.characterDef("/Game/Athena/Items/Cosmetics/Characters/" + character + "." + character);
        campaignHero.heroType("/Game/Athena/Heroes/" + asHero + "." + asHero);

        // set these values in the update meta.
        updateMeta.campaignHero(campaignHero);
        updateMeta.cosmeticLoadout(cosmeticLoadout);
    }

    /**
     * Sets the desired backpack.
     *
     * @param backpack the backpack value.
     */
    public void setBackpack(String backpack) {
        cosmeticLoadout.backpackDef("/Game/Athena/Items/Cosmetics/Backpacks/" + backpack + "." + backpack);
        updateMeta.cosmeticLoadout(cosmeticLoadout);
    }

    /**
     * Sets the desired pickaxe.
     *
     * @param pickaxe the pickaxe value.
     */
    public void setPickaxe(String pickaxe) {
        cosmeticLoadout.pickaxeDef("/Game/Athena/Items/Cosmetics/Pickaxes/" + pickaxe + "." + pickaxe);
        updateMeta.cosmeticLoadout(cosmeticLoadout);
    }

    /**
     * Sets the desired contrail
     *
     * @param contrail the contrail value.
     */
    public void setContrail(String contrail) {
        cosmeticLoadout.contrailDef("/Game/Athena/Items/Cosmetics/Contrails/" + contrail + "." + contrail);
        updateMeta.cosmeticLoadout(cosmeticLoadout);
    }

    /**
     * Add a variant
     *
     * @param variant the variant
     */
    public void addVariant(CosmeticVariant variant) {
        cosmeticLoadout.variants().add(variant);
    }

    /**
     * Add a list of variants
     *
     * @param variants the variants
     */
    public void addVariants(List<CosmeticVariant> variants) {
        cosmeticLoadout.variants().addAll(variants);
    }

    /**
     * Set the assisted challenge.
     *
     * @param assistedChallenge the assisted challenge.
     */
    public void setAssistedChallenge(AssistedChallenge assistedChallenge) {
        updateMeta.challengeInfo(assistedChallenge);
    }

    /**
     * Set the assisted challenge.
     *
     * @param questItem           the quest item.
     * @param objectivesCompleted how many objectives have been completed.
     */
    public void setAssistedChallenge(String questItem, int objectivesCompleted) {
        setAssistedChallenge(new AssistedChallenge(questItem, objectivesCompleted));
    }

    /**
     * Set if the voice chat is muted or not.
     *
     * @param muted {@code true} if the voice chat is muted
     */
    public void setVoiceChatMuted(boolean muted) {
        updateMeta.voiceChatMuted(Boolean.toString(muted));
    }

    /**
     * Set the battle pass
     *
     * @param battlePass the battlepass
     */
    public void setBattlePass(BattlePass battlePass) {
        updateMeta.battlePass(battlePass);
    }

    /**
     * Set the battle pass information
     *
     * @param hasPurchased  {@code true} if the battlepass has been purchased
     * @param level         the battlepass level
     * @param selfBoostXp   the self boost XP
     * @param friendBoostXp the friend boost XP
     */
    public void setBattlePass(boolean hasPurchased, int level, int selfBoostXp, int friendBoostXp) {
        setBattlePass(new BattlePass(hasPurchased, level, selfBoostXp, friendBoostXp));
    }

    /**
     * Set the input type.
     *
     * @param inputType the input type.
     */
    public void setInputType(Input inputType) {
        updateMeta.currentInputType(inputType);
    }

    /**
     * Play an emote or a dance.
     *
     * @param emote   the emote or dance
     * @param isEmote {@code true} if the emote is not a dance.
     */
    public void playEmote(String emote, boolean isEmote) {
        isEmoting = true;
        updateMeta.frontendEmote(new FrontendEmote(isEmote ? "/Game/Athena/Items/Cosmetics/Dances/Emoji/" + emote + "." + emote : "/Game/Athena/Items/Cosmetics/Dances/" + emote + "." + emote, "", -2));
    }

    /**
     * Play an emote or a dance.
     *
     * @param fullEmoteDefinition the emote or dance including its path, (example: /Game/Athena/Items/Cosmetics/Dances/EID_SnowGlobe.EID_SnowGlobe)
     */
    public void playEmote(String fullEmoteDefinition) {
        isEmoting = true;
        updateMeta.frontendEmote(new FrontendEmote(fullEmoteDefinition, "", -2));
    }

    /**
     * @return {@code true} if the client is emoting.
     */
    public boolean isEmoting() {
        return isEmoting;
    }

    /**
     * Stop the emote or dance
     *
     * @param emote the emote or dance
     */
    public void stopEmote(String emote) {
        isEmoting = false;
        updateMeta.frontendEmote(new FrontendEmote(emote, "", -1));
    }

    /**
     * Set the banner
     *
     * @param banner the banner information
     */
    public void setBanner(AthenaBanner banner) {
        updateMeta.banner(banner);
    }

    /**
     * Set the banner information
     *
     * @param bannerIconId  the icon ID
     * @param bannerColorId the color ID
     * @param seasonLevel   the level
     */
    public void setBanner(String bannerIconId, String bannerColorId, int seasonLevel) {
        setBanner(new AthenaBanner(bannerIconId, bannerColorId, seasonLevel));
    }

    /**
     * Set if content has been preloaded
     *
     * @param preloaded {@code true} if content has been preloaded
     */
    public void setPreloaded(boolean preloaded) {
        updateMeta.hasPreloaded(Boolean.toString(preloaded));
    }

    /**
     * Set the ready status
     *
     * @param readiness the status
     */
    public void setReadiness(GameReadiness readiness) {
        updateMeta.gameReadiness(readiness.getName());
    }

    /**
     * Set the platform
     *
     * @param platform the platform
     */
    public void setPlatform(Platform platform) {
        updateMeta.platform(platform);
    }

    /**
     * Set the location inside the lobby, ex: prelobby, ingame, etc.
     *
     * @param location the location
     */
    public void setLocation(String location) {
        updateMeta.location(location);
    }

    /**
     * Updates this client and bumps the revision.
     */
    public void update() {
        if (partyId == null) return;
        try {
            patch(payload());
        } catch (EpicGamesErrorException exception) {
            if (exception.errorCode().equals("errors.com.epicgames.social.party.stale_revision")) {
                // retry the request, our revision is outdated.
                revision++;
                patch(payload());
            } else {
                throw exception;
            }
        }

        // reset our meta and bump the revision.
        updateMeta = new PartyMemberMeta();
        revision++;
    }

    /**
     * Builds the payload
     *
     * @return the payload.
     */
    private JsonObject payload() {
        return new JsonObjectBuilder()
                .add("delete", new JsonArray())
                .add("revision", revision)
                .add("update", gson.toJsonTree(updateMeta).getAsJsonObject())
                .build();
    }

    /**
     * Dispatches the HTTP request and patches this client.
     *
     * @param payload the payload to send.
     * @throws EpicGamesErrorException if an error occurred
     */
    private void patch(JsonObject payload) throws EpicGamesErrorException {
        final var patch = service.patch(partyId, context.localAccountId(), payload);
        Requests.executeCall(patch);
    }

}
