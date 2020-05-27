package athena.party;

import athena.context.DefaultAthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.party.resource.ClientParty;
import athena.party.resource.Party;
import athena.party.resource.assignment.SquadAssignment;
import athena.party.resource.chat.PartyChat;
import athena.party.resource.configuration.PartyConfiguration;
import athena.party.resource.configuration.privacy.PartyPrivacy;
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
import athena.party.resource.playlist.PartyPlaylistData;
import athena.party.resource.requests.PartyCreateRequest;
import athena.party.resource.requests.PartyInvitationRequest;
import athena.party.resource.requests.PartyJoinRequest;
import athena.party.service.PartyService;
import athena.party.xmpp.event.invite.PartyInviteEvent;
import athena.party.xmpp.event.invite.PartyPingEvent;
import athena.types.Input;
import athena.types.Platform;
import athena.util.cleanup.AfterRefresh;
import athena.util.cleanup.BeforeRefresh;
import athena.util.cleanup.Shutdown;
import athena.util.request.Requests;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Provides easy access and management for the party system and {@link athena.party.service.PartyService}
 */
public final class Parties {

    /**
     * The current build ID.
     */
    public static final String BUILD_ID = "1:1:";

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
    private final PartyEventNotifier notifier;

    /**
     * The current party.
     */
    private Party party;

    /**
     * Our client
     */
    private ClientPartyMember client;

    /**
     * The client party controller.
     */
    private ClientParty clientParty;

    /**
     * The party chat
     */
    private PartyChat chat;

    public Parties(DefaultAthenaContext context) {
        this.context = context;
        this.service = context.partyService();
        this.notifier = new PartyEventNotifier(context, this);
        this.client = new ClientPartyMember(context);
        this.clientParty = new ClientParty(service, null, context.gson());
        this.chat = new PartyChat(MultiUserChatManager.getInstanceFor(context.connectionManager().connection()));
    }

    public void onPing(Consumer<PartyPingEvent> event) {
        // TODO:
    }

    public void onInvite(Consumer<PartyInviteEvent> event) {
        // TODO:
    }

    /**
     * Attempts to join the provided party.
     *
     * @param partyId the party ID.
     * @throws EpicGamesErrorException if an error occurred while joining
     */
    public Party joinParty(String partyId) throws EpicGamesErrorException {
        // leave our current party.
        if (party != null) leaveParty();
        // retrieve the new parties information
        party = Requests.executeCall(service.getParty(partyId));
        // craft our join payload
        final var payload = PartyJoinRequest.forUser(context.localAccountId(), context.displayName(), context.connectionManager().connection().getUser(), context.platform());
        // reset our client
        client.set(partyId);
        client.initializeBaseMeta();
        client.updateCosmetic();
        // join the party.
        Requests.executeCall(service.joinParty(partyId, context.localAccountId(), payload));
        // send our meta
        client.update();
        // join the party chat
        chat.joinNewChat(partyId, context.displayName(), context.localAccountId(), context.connectionManager().connection().getUser().getResourceOrEmpty().toString());
        // set when we last joined
        chat.setLastJoinTimeInternal(System.currentTimeMillis());
        return party;
    }

    /**
     * Create a new party.
     *
     * @return the party
     */
    public Party createParty(PartyPrivacy privacy) {
        // create a new config from the privacy
        final var configuration = privacy.isPrivate() ? PartyConfiguration.closed() : privacy.partyType().equals("Public") ? PartyConfiguration.open() : PartyConfiguration.friendsOnly();
        // initialize our meta
        final var base = clientParty.initializeBaseMeta(privacy);
        // create the party.
        party = Requests.executeCall(service.createParty(
                PartyCreateRequest.forParty(configuration, context.connectionManager().connection().getUser().asUnescapedString(), context.platform())));
        // set our client party.
        clientParty.resetParty(party);
        // send our base meta
        clientParty.update(base);
        // reset our client
        client.set(party.partyId());
        client.initializeBaseMeta();
        client.updateCosmetic();
        client.update();
        // join the party chat
        chat.joinNewChat(party.partyId(), context.displayName(), context.localAccountId(), context.connectionManager().connection().getUser().getResourceOrEmpty().toString());
        // finally, update the party information
        updatePartyInformation();
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

            // TODO: Rearrange squad assignments on leave

            party = null;
            client.set(null);
            chat.leave();
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
     * @return The party chat.
     */
    public PartyChat chat() {
        return chat;
    }

    /**
     * Invite a member to this party.
     * TODO: May error with certain configurations not sure
     *
     * @param accountId the account ID.
     */
    public void invite(String accountId) {
        if (party == null) return;
        Requests.executeCall(service.invite(party.partyId(), accountId, new PartyInvitationRequest(context.displayName(), context.platform())));
    }

    /**
     * Updates/refreshes squad assignments.
     * Only updates if the current account is leader.
     */
    public void refreshSquadAssignments() {
        if (!party.leader().accountId().equals(context.localAccountId())) return;
        final var assignments = createSquadAssignments();
        clientParty.setSquadAssignments(assignments);
    }

    /**
     * Create a list of squad assignments
     *
     * @return the squad assignments
     */
    private List<SquadAssignment> createSquadAssignments() {
        final var leader = party.leader();
        final var list = new ArrayList<SquadAssignment>();
        final var index = new AtomicInteger(1);
        // add our leader first.
        list.add(new SquadAssignment(leader.accountId(), 0));
        // add each member next, filtering out the leader.
        party.members().stream().filter(member -> !member.accountId().equals(leader.accountId()))
                .forEach(member -> {
                    list.add(new SquadAssignment(member.accountId(), index.get()));
                    index.addAndGet(1);
                });
        return list;
    }

    /**
     * Set the custom key.
     *
     * @param customKey the custom key
     * @return this
     */
    public Parties setCustomKey(String customKey) {
        clientParty.setCustomKey(customKey);
        return this;
    }

    /**
     * Set the privacy
     *
     * @param privacy the privacy
     * @return this
     */
    public Parties setPrivacy(PartyPrivacy privacy) {
        party.updateConfiguration(privacy.isPrivate() ? PartyConfiguration.PRIVATE_PARTY
                : privacy.partyType().equals("Public") ? PartyConfiguration.PUBLIC_PARTY
                : PartyConfiguration.FRIENDS_ONLY_PARTY);

        clientParty.setPrivacy(privacy);
        return this;
    }

    /**
     * Set the squad assignments
     *
     * @param squadAssignments the squad assignments
     */
    public Parties setSquadAssignments(List<SquadAssignment> squadAssignments) {
        clientParty.setSquadAssignments(squadAssignments);
        return this;
    }

    /**
     * Set the playlist
     *
     * @param playlist the playlist
     */
    public Parties setPlaylist(PartyPlaylistData playlist) {
        clientParty.setPlaylist(playlist);
        return this;
    }

    /**
     * Set the playlist
     *
     * @param playlistName  the playlist name, ex: "Playlist_DefaultSolo"
     * @param tournamentId  the tournament ID or {@code ""}
     * @param eventWindowId the event window ID or {@code ""}
     * @param regionId      the region ID, ex: "NAE"
     */
    public Parties setPlaylist(String playlistName, String tournamentId, String eventWindowId, String regionId) {
        return setPlaylist(new PartyPlaylistData(playlistName, tournamentId, eventWindowId, regionId));
    }

    /**
     * Set squad fill
     *
     * @param squadFill the squad fill status
     * @return this
     */
    public Parties setSquadFill(boolean squadFill) {
        clientParty.setSquadFill(squadFill);
        return this;
    }

    /**
     * Update the meta.
     *
     * @param meta the meta
     * @return this
     */
    public Parties updateMetaFromEvent(PartyMeta meta) {
        party.meta().updateMeta(meta);
        return this;
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
            clientParty.resetParty(party);
        }

        return this;
    }

    /**
     * Register an event listener.
     *
     * @param listener the listener.
     */
    public void registerEventListener(Object listener) {
        notifier.registerEventListener(listener);
    }

    /**
     * Unregister an event listener
     *
     * @param listener the listener
     */
    public void unregisterEventListener(Object listener) {
        notifier.unregisterEventListener(listener);
    }

    @AfterRefresh
    private void after(DefaultAthenaContext context) {
        notifier.afterRefresh(context);
        chat = new PartyChat(MultiUserChatManager.getInstanceFor(context.connectionManager().connection()));
    }

    @BeforeRefresh
    private void before() {
        notifier.beforeRefresh();
        chat.leave();
    }

    @Shutdown
    private void shutdown() {
        leaveParty();
        notifier.shutdown();
    }

}
