package athena.party.resource;

import athena.context.AthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.party.resource.configuration.PartyConfiguration;
import athena.party.resource.configuration.privacy.PartyPrivacy;
import athena.party.resource.configuration.types.Joinability;
import athena.party.resource.invite.PartyInvitation;
import athena.party.resource.member.PartyMember;
import athena.party.resource.member.meta.banner.AthenaBanner;
import athena.party.resource.member.meta.battlepass.BattlePass;
import athena.party.resource.member.meta.challenges.AssistedChallenge;
import athena.party.resource.member.meta.cosmetic.variant.CosmeticVariant;
import athena.party.resource.member.meta.readiness.GameReadiness;
import athena.party.resource.meta.PartyMeta;
import athena.types.Input;
import athena.types.Platform;
import athena.util.json.hooks.annotation.PostDeserialize;
import athena.util.json.hooks.annotation.PreDeserialize;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;

/**
 * Represents a Fortnite party.
 */
public final class Party extends AthenaContext {

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
     * @return the metadata of this party.
     */
    public PartyMeta meta() {
        return meta;
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
     * Attempts to join this party.
     *
     * @throws EpicGamesErrorException if an error occurred while joining
     */
    public void join() throws EpicGamesErrorException {
        parties.joinParty(partyId);
    }

    /**
     * Leaves this party.
     *
     * @throws EpicGamesErrorException if an error occurred.
     */
    public void leave() throws EpicGamesErrorException {
        parties.leaveParty();
    }

    /**
     * Sets your character model.
     *
     * @param character the character.
     * @return this party
     */
    public Party setCharacter(String character) {
        parties.setCharacter(character);
        return this;
    }

    /**
     * Sets your backpack.
     *
     * @param backpack the backpack
     * @return this party
     */
    public Party setBackpack(String backpack) {
        parties.setBackpack(backpack);
        return this;
    }

    /**
     * Sets the desired pickaxe.
     *
     * @param pickaxe the pickaxe value.
     * @return this party
     */
    public Party setPickaxe(String pickaxe) {
        parties.setPickaxe(pickaxe);
        return this;
    }

    /**
     * Sets the desired contrail
     *
     * @param contrail the contrail value.
     * @return this party
     */
    public Party setContrail(String contrail) {
        parties.setContrail(contrail);
        return this;
    }

    /**
     * Add a variant
     *
     * @param variant the variant
     * @return this party
     */
    public Party addVariant(CosmeticVariant variant) {
        parties.addVariant(variant);
        return this;
    }

    /**
     * Add a list of variants
     *
     * @param variants the variants
     * @return this party
     */
    public Party addVariants(List<CosmeticVariant> variants) {
        parties.addVariants(variants);
        return this;
    }

    /**
     * Set the assisted challenge
     *
     * @param assistedChallenge the assisted challenge.
     * @return this instance
     */
    public Party setAssistedChallenge(AssistedChallenge assistedChallenge) {
        parties.setAssistedChallenge(assistedChallenge);
        return this;
    }

    /**
     * Set the assisted challenge.
     *
     * @param questItem           the quest item.
     * @param objectivesCompleted how many objectives have been completed.
     * @return this instance
     */
    public Party setAssistedChallenge(String questItem, int objectivesCompleted) {
        setAssistedChallenge(new AssistedChallenge(questItem, objectivesCompleted));
        return this;
    }

    /**
     * Set if the voice chat is muted or not.
     *
     * @param muted {@code true} if the voice chat is muted
     * @return this instance
     */
    public Party setVoiceChatMuted(boolean muted) {
        parties.setVoiceChatMuted(muted);
        return this;
    }

    /**
     * Set the battle pass
     *
     * @param battlePass the battlepass
     * @return this instance
     */
    public Party setBattlePass(BattlePass battlePass) {
        parties.setBattlePass(battlePass);
        return this;
    }

    /**
     * Set the battle pass information
     *
     * @param hasPurchased  {@code true} if the battlepass has been purchased
     * @param level         the battlepass level
     * @param selfBoostXp   the self boost XP
     * @param friendBoostXp the friend boost XP
     * @return this instance
     */
    public Party setBattlePass(boolean hasPurchased, int level, int selfBoostXp, int friendBoostXp) {
        setBattlePass(new BattlePass(hasPurchased, level, selfBoostXp, friendBoostXp));
        return this;
    }

    /**
     * Set the input type.
     *
     * @param inputType the input type.
     * @return this instance
     */
    public Party setInputType(Input inputType) {
        parties.setInputType(inputType);
        return this;
    }

    /**
     * Play an emote or a dance.
     *
     * @param emote   the emote or dance
     * @param isEmote {@code true} if the emote is not a dance.
     * @return this instance
     */
    public Party playEmote(String emote, boolean isEmote) {
        parties.playEmote(emote, isEmote);
        return this;
    }

    /**
     * Stop the emote or dance
     *
     * @param emote the emote or dance
     * @return this instance
     */
    public Party stopEmote(String emote) {
        parties.stopEmote(emote);
        return this;
    }

    /**
     * Set the banner
     *
     * @param banner the banner information
     * @return this instance
     */
    public Party setBanner(AthenaBanner banner) {
        parties.setBanner(banner);
        return this;
    }

    /**
     * Set the banner information
     *
     * @param bannerIconId  the icon ID
     * @param bannerColorId the color ID
     * @param seasonLevel   the level
     * @return this instance
     */
    public Party setBanner(String bannerIconId, String bannerColorId, int seasonLevel) {
        setBanner(new AthenaBanner(bannerIconId, bannerColorId, seasonLevel));
        return this;
    }

    /**
     * Set if content has been preloaded
     *
     * @param preloaded {@code true} if content has been preloaded
     * @return this instace
     */
    public Party setPreloaded(boolean preloaded) {
        parties.setPreloaded(preloaded);
        return this;
    }

    /**
     * Set the ready status
     *
     * @param readiness the status
     * @return this instance
     */
    public Party setReadiness(GameReadiness readiness) {
        parties.setReadiness(readiness);
        return this;
    }

    /**
     * Set the platform
     *
     * @param platform the platform
     * @return this instance
     */
    public Party setPlatform(Platform platform) {
        parties.setPlatform(platform);
        return this;
    }

    /**
     * Set the location inside the lobby, ex: prelobby, ingame, etc.
     *
     * @param location the location
     * @return this instance
     */
    public Party setLocation(String location) {
        parties.setLocation(location);
        return this;
    }

    /**
     * Updates your client.
     *
     * @return this party.
     */
    public Party updateClient() {
        parties.updateClient();
        return this;
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
}
