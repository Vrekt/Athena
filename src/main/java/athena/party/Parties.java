package athena.party;

import athena.context.DefaultAthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.party.resource.Party;
import athena.party.resource.authentication.PartyJoinRequest;
import athena.party.resource.configuration.PartyConfiguration;
import athena.party.resource.member.PartyMember;
import athena.party.resource.member.client.ClientPartyMember;
import athena.party.resource.member.meta.PartyMemberMeta;
import athena.party.resource.member.meta.banner.AthenaBanner;
import athena.party.resource.member.meta.battlepass.BattlePass;
import athena.party.resource.member.meta.challenges.AssistedChallenge;
import athena.party.resource.member.meta.cosmetic.variant.CosmeticVariant;
import athena.party.resource.member.meta.readiness.GameReadiness;
import athena.party.resource.member.role.PartyRole;
import athena.party.resource.meta.PartyMeta;
import athena.party.service.PartyService;
import athena.types.Input;
import athena.types.Platform;
import athena.util.cleanup.AfterRefresh;
import athena.util.cleanup.BeforeRefresh;
import athena.util.cleanup.Shutdown;
import athena.util.json.builder.JsonObjectBuilder;
import athena.util.request.Requests;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Provides easy access and management for the party system and {@link athena.party.service.PartyService}
 */
public final class Parties {

    /**
     * The party service.
     */
    private final PartyService service;

    /**
     * The context
     */
    private final DefaultAthenaContext context;

    /**
     * The XMPP notifier.
     */
    private final PartyNotifier notifier;

    /**
     * Athena GSON instance
     */
    private final Gson gson;

    /**
     * The current party.
     */
    private Party party;

    /**
     * Our client
     */
    private ClientPartyMember client;

    /**
     * Used to update the party.
     */
    private PartyMeta updateMeta;

    /**
     * Used to update the party.
     */
    private List<String> deleteMeta;

    /**
     * Keeps track of the update revision
     */
    private int revision;

    public Parties(DefaultAthenaContext context) {
        this.context = context;
        this.gson = context.gson();
        this.service = context.partyService();
        this.notifier = new PartyNotifier(context, this);
        this.client = new ClientPartyMember(context);
        updateMeta = new PartyMeta();
        deleteMeta = new ArrayList<>();
        // TODO: Create our own party later.
    }

    /**
     * Attempts to join the provided party.
     *
     * @param partyId the party ID.
     * @throws EpicGamesErrorException if an error occurred while joining
     */
    public Party joinParty(String partyId) throws EpicGamesErrorException {
        // leave our current party.
        if (party != null) party.leave();
        // retrieve the new parties information
        party = Requests.executeCall(service.getParty(partyId));
        // craft our join payload
        final var payload = PartyJoinRequest.forUser(context.localAccountId(), context.displayName(), context.connectionManager().connection().getUser(), context.platform());
        // reset our client
        client.set(partyId);
        // join the party.
        Requests.executeCall(service.joinParty(partyId, context.localAccountId(), payload));
        // send our meta
        client.update();
        return party;
    }

    /**
     * Leave the current party and reset our state.
     *
     * @throws EpicGamesErrorException if an error occurred.
     */
    public void leaveParty() throws EpicGamesErrorException {
        if (party != null) {
            Requests.executeCall(service.leaveParty(party.partyId(), context.localAccountId()));

            party = null;
            client.set(null);
        }
    }

    /**
     * Disband the current party.
     *
     * @throws EpicGamesErrorException if an error occurred.
     */
    public void disbandParty() throws EpicGamesErrorException {
        if (party != null) {
            Requests.executeCall(service.disbandParty(party.partyId()));
            party = null;
            client.set(null);
        }
    }

    /**
     * Promote a member in the party
     *
     * @param accountId the account ID.
     * @return this instance
     */
    public Parties promote(String accountId) {
        if (party != null) Requests.executeCall(service.promote(party.partyId(), accountId));
        return this;
    }

    /**
     * Promote a member in the party
     *
     * @param member the member
     * @return this instance
     */
    public Parties promote(PartyMember member) {
        return promote(member.accountId());
    }

    /**
     * Kick a member in the party
     *
     * @param accountId the account ID.
     * @return this instance
     */
    public Parties kick(String accountId) {
        if (party != null) Requests.executeCall(service.kick(party.partyId(), accountId));
        return this;
    }

    /**
     * Kick a member in the party
     *
     * @param member the member
     * @return this instance
     */
    public Parties kick(PartyMember member) {
        return kick(member.accountId());
    }

    /**
     * @return the party
     */
    public Party party() {
        return party;
    }

    /**
     * Set your character.
     *
     * @param character the character.
     * @return this instance
     */
    public Parties setCharacter(String character) {
        client.setCharacter(character);
        client.update();
        return this;
    }

    /**
     * Sets your backpack.
     *
     * @param backpack the backpack
     * @return this instance
     */
    public Parties setBackpack(String backpack) {
        client.setBackpack(backpack);
        client.update();
        return this;
    }

    /**
     * Sets the desired pickaxe.
     *
     * @param pickaxe the pickaxe value.
     * @return this instance
     */
    public Parties setPickaxe(String pickaxe) {
        client.setPickaxe(pickaxe);
        client.update();
        return this;
    }

    /**
     * Sets the desired contrail
     *
     * @param contrail the contrail value.
     * @return this instance
     */
    public Parties setContrail(String contrail) {
        client.setContrail(contrail);
        client.update();
        return this;
    }

    /**
     * Add a variant
     *
     * @param variant the variant
     * @return this instance
     */
    public Parties addVariant(CosmeticVariant variant) {
        client.addVariant(variant);
        client.update();
        return this;
    }

    /**
     * Add a list of variants
     *
     * @param variants the variants
     * @return this instance
     */
    public Parties addVariants(List<CosmeticVariant> variants) {
        client.addVariants(variants);
        client.update();
        return this;
    }

    /**
     * Set the assisted challenge
     *
     * @param assistedChallenge the assisted challenge.
     * @return this instance
     */
    public Parties setAssistedChallenge(AssistedChallenge assistedChallenge) {
        client.setAssistedChallenge(assistedChallenge);
        client.update();
        return this;
    }

    /**
     * Set the assisted challenge.
     *
     * @param questItem           the quest item.
     * @param objectivesCompleted how many objectives have been completed.
     * @return this instance
     */
    public Parties setAssistedChallenge(String questItem, int objectivesCompleted) {
        setAssistedChallenge(new AssistedChallenge(questItem, objectivesCompleted));
        return this;
    }

    /**
     * Set if the voice chat is muted or not.
     *
     * @param muted {@code true} if the voice chat is muted
     * @return this instance
     */
    public Parties setVoiceChatMuted(boolean muted) {
        client.setVoiceChatMuted(muted);
        return this;
    }

    /**
     * Set the battle pass
     *
     * @param battlePass the battlepass
     * @return this instance
     */
    public Parties setBattlePass(BattlePass battlePass) {
        client.setBattlePass(battlePass);
        client.update();
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
    public Parties setBattlePass(boolean hasPurchased, int level, int selfBoostXp, int friendBoostXp) {
        setBattlePass(new BattlePass(hasPurchased, level, selfBoostXp, friendBoostXp));
        return this;
    }

    /**
     * Set the input type.
     *
     * @param inputType the input type.
     * @return this instance
     */
    public Parties setInputType(Input inputType) {
        client.setInputType(inputType);
        return this;
    }

    /**
     * Play an emote or a dance.
     *
     * @param emote   the emote or dance
     * @param isEmote {@code true} if the emote is not a dance.
     * @return this instance
     */
    public Parties playEmote(String emote, boolean isEmote) {
        client.playEmote(emote, isEmote);
        client.update();
        return this;
    }

    /**
     * Play an emote or a dance.
     *
     * @param fullEmoteDefinition the emote or dance including its path, (example: /Game/Athena/Items/Cosmetics/Dances/EID_SnowGlobe.EID_SnowGlobe)
     * @return this instance
     */
    public Parties playEmote(String fullEmoteDefinition) {
        if (client.isEmoting()) return this;
        client.playEmote(fullEmoteDefinition);
        client.update();
        return this;
    }

    /**
     * The amount of seconds to let pass before stopping the emote.
     *
     * @param seconds the seconds
     * @return this instance
     */
    public Parties stopEmoteAfter(int seconds) {
        CompletableFuture.delayedExecutor(seconds, TimeUnit.SECONDS).execute(() -> stopEmote("None"));
        return this;
    }

    /**
     * Stop the emote or dance
     *
     * @param emote the emote or dance
     * @return this instance
     */
    public Parties stopEmote(String emote) {
        client.stopEmote(emote);
        client.update();
        client.stopEmote("None");
        client.update();
        return this;
    }

    /**
     * Set the banner
     *
     * @param banner the banner information
     * @return this instance
     */
    public Parties setBanner(AthenaBanner banner) {
        client.setBanner(banner);
        client.update();
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
    public Parties setBanner(String bannerIconId, String bannerColorId, int seasonLevel) {
        setBanner(new AthenaBanner(bannerIconId, bannerColorId, seasonLevel));
        return this;
    }

    /**
     * Set if content has been preloaded
     *
     * @param preloaded {@code true} if content has been preloaded
     * @return this instace
     */
    public Parties setPreloaded(boolean preloaded) {
        client.setPreloaded(preloaded);
        client.update();
        return this;
    }

    /**
     * Set the ready status
     *
     * @param readiness the status
     * @return this instance
     */
    public Parties setReadiness(GameReadiness readiness) {
        client.setReadiness(readiness);
        client.update();
        return this;
    }

    /**
     * Set the platform
     *
     * @param platform the platform
     * @return this instance
     */
    public Parties setPlatform(Platform platform) {
        client.setPlatform(platform);
        client.update();
        return this;
    }

    /**
     * Set the location inside the lobby, ex: prelobby, ingame, etc.
     *
     * @param location the location
     * @return this instance
     */
    public Parties setLocation(String location) {
        client.setLocation(location);
        client.update();
        return this;
    }

    /**
     * Updates your client.
     *
     * @return this
     */
    public Parties updateClient() {
        client.update();
        return this;
    }

    /**
     * Updates the party information (its members, meta, etc)
     *
     * @return this
     */
    public Parties updatePartyInformation() {
        if (party != null) party = Requests.executeCall(service.getParty(party.partyId()));
        return this;
    }

    /**
     * Updates the party meta, can only be done if you are leader.
     *
     * @return this
     */
    public Parties updateParty() {
        if (party != null) update();
        return this;
    }

    /**
     * Update the party meta
     *
     * @param meta the meta
     * @return this
     */
    public Parties updatePartyMeta(PartyMeta meta) {
        party.meta().updateMeta(meta);
        party.updateConfigurationFromMeta();
        return this;
    }

    /**
     * @return the update meta
     */
    public PartyMeta updateMeta() {
        return updateMeta;
    }

    /**
     * Add something to the delete list.
     *
     * @param deleteMeta the meta to delete.
     */
    public void addDeleteMeta(String deleteMeta) {
        this.deleteMeta.add(deleteMeta);
    }

    /**
     * Update a member.
     *
     * @param accountId the account ID of the member
     * @param meta      the updated meta
     * @return this
     */
    public PartyMember updateMember(String accountId, PartyMemberMeta meta) {
        final var member = party.members().stream().filter(partyMember -> partyMember.accountId().equals(accountId)).findAny().orElseThrow();
        if (member.accountId().equals(accountId)) return member;
        member.meta().updateMeta(meta);
        return member;
    }

    /**
     * Update the captain.
     *
     * @param newCaptain the new captain
     * @return this party.
     */
    public Parties updateCaptain(PartyMember newCaptain) {
        if (party == null) return this;
        party.members().stream().filter(member -> member.role() == PartyRole.CAPTAIN).findFirst().ifPresent(member -> member.role(PartyRole.MEMBER));
        newCaptain.role(PartyRole.CAPTAIN);

        // if we were promoted, get the revision.
        if (newCaptain.accountId().equals(context.localAccountId())) {
            updatePartyInformation();
            revision = party.revision();
        }

        return this;
    }

    /**
     * Updates the party and bumps the revision
     */
    public void update() {
        if (party == null) return;
        try {
            patch(payload());
        } catch (EpicGamesErrorException exception) {
            if (exception.errorCode().equals("errors.com.epicgames.social.party.stale_revision")) {
                System.err.println("Revision is outdated, updating that!");
                // retry the request, our revision is outdated.
                // TODO Get revision from error
                revision++;
                patch(payload());
            } else {
                throw exception;
            }
        }

        // reset our meta and bump the revision.
        updateMeta = new PartyMeta();
        deleteMeta.clear();
        revision++;
    }

    /**
     * Builds the payload
     *
     * @return the payload.
     */
    private JsonObject payload() {
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
        final var patch = service.updateParty(party.partyId(), payload);
        Requests.executeCall(patch);
    }

    public void registerEventListener(Object listener) {
        notifier.registerEventListener(listener);
    }

    public void unregisterEventListener(Object listener) {
        notifier.unregisterEventListener(listener);
    }

    @AfterRefresh
    private void after(DefaultAthenaContext context) {
        // TODO:
    }

    @BeforeRefresh
    private void before() {
        // TODO:
    }

    @Shutdown
    private void shutdown() {
        leaveParty();
    }


}
