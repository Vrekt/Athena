package athena.presence.resource;

import athena.Athena;
import athena.account.resource.Account;
import athena.exception.EpicGamesErrorException;
import athena.friend.resource.summary.Profile;
import athena.util.request.Requests;
import athena.context.AthenaContext;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jxmpp.jid.Jid;

/**
 * Represents a Fortnite presence.
 */
public final class FortnitePresence extends AthenaContext {

    /**
     * The JSON status.
     */
    @SerializedName("Status")
    private String status;
    /**
     * The session ID.
     */
    @SerializedName("SessionId")
    private String sessionId;
    /**
     * {@code true} if they are playing.
     */
    @SerializedName("bIsPlaying")
    private boolean playing;
    /**
     * {@code true} if the game is joinable?
     * I don't think this is always set to the correct value.
     */
    @SerializedName("bIsJoinable")
    private boolean joinable;
    /**
     * ??
     */
    @SerializedName("bHasVoiceSupport")
    private boolean voiceSupport;
    /**
     * Properties
     */
    @SerializedName("Properties")
    private Properties properties;

    /**
     * The account
     */
    private Account account;

    /**
     * Who its from
     */
    private Jid from;

    private FortnitePresence() {
    }

    /**
     * @return the JSON status
     */
    public String status() {
        return status;
    }

    /**
     * @return the session ID.
     */
    public String sessionId() {
        return sessionId;
    }

    /**
     * @return {@code true} if they are playing.
     */
    public boolean playing() {
        return playing;
    }

    /**
     * @return {@code true} if joinable, I don't think this is always set to the correct value.
     */
    public boolean joinable() {
        return joinable;
    }

    /**
     * @return ??
     */
    public boolean voiceSupport() {
        return voiceSupport;
    }

    /**
     * @return the avatar
     */
    public String avatar() {
        return properties.profile().avatar;
    }

    /**
     * @return the avatar background
     */
    public String[] avatarBackground() {
        return properties.profile().avatarBackground;
    }

    /**
     * @return the playlist
     */
    public String playlist() {
        return properties.playlist;
    }

    /**
     * @return the amount of players alive.
     */
    public int eventPlayersAlive() {
        return Integer.parseInt(properties.eventPlayersAlive);
    }

    /**
     * @return the party size.
     */
    public int eventPartySize() {
        return Integer.parseInt(properties.eventPartySize);
    }

    /**
     * @return the max party size.
     */
    public int eventMaxPartySize() {
        return Integer.parseInt(properties.eventMaxPartySize);
    }

    /**
     * @return the session key.
     */
    public String sessionKey() {
        return properties.sessionKey;
    }

    /**
     * @return ""
     */
    public String state() {
        return properties.gameplayStats.state;
    }

    /**
     * @return amount of kills
     */
    public int kills() {
        return properties.gameplayStats.kills;
    }

    /**
     * @return {@code true} if they fell to death.
     */
    public boolean fellToDeath() {
        return properties.gameplayStats.fellToDeath;
    }

    /**
     * @return the account ID of who owns the party, or {@code ""} if the party is private.
     */
    public String sourceId() {
        return properties.partyJoinInfo.isPrivate ? "" : properties.partyJoinInfo.sourceId;
    }

    /**
     * @return the display name of who owns the party, or {@code ""} if the party is private.
     */
    public String sourceDisplayName() {
        if (properties == null || properties.partyJoinInfo == null) return "";
        return properties.partyJoinInfo.isPrivate ? "" : properties.partyJoinInfo.sourceDisplayName;
    }

    /**
     * @return the party ID, or {@code ""} if the party is private.
     */
    public String partyId() {
        return properties.partyJoinInfo.isPrivate ? "" : properties.partyJoinInfo.partyId;
    }

    /**
     * @return the app ID.
     */
    public String appId() {
        return "Fortnite";
    }

    /**
     * @return the build ID, or {@code ""} if the party is private.
     */
    public String buildId() {
        return properties.partyJoinInfo.isPrivate ? "" : properties.partyJoinInfo.buildId;
    }

    /**
     * @return party flags, or {@code 0} if the party is private.
     */
    public int partyFlags() {
        return properties.partyJoinInfo.isPrivate ? 0 : properties.partyJoinInfo.partyFlags;
    }

    /**
     * @return the not accepting reason, or {@code 0} if the party is private.
     */
    public int notAcceptingReason() {
        return properties.partyJoinInfo.isPrivate ? 0 : properties.partyJoinInfo.notAcceptingReason;
    }

    /**
     * @return pc ?? , or {@code 0} if the party is private.
     */
    public int pc() {
        return properties.partyJoinInfo.isPrivate ? 0 : properties.partyJoinInfo.pc;
    }

    /**
     * @return the party type ID, or {@code 0} if the party is private.
     */
    public long partyTypeId() {
        return properties.partyJoinInfo.isPrivate ? 0 : properties.partyJoinInfo.partyTypeId;
    }

    /**
     * @return {@code true} if this party is private.
     */
    public boolean isPartyPrivate() {
        return properties.partyJoinInfo.isPrivate;
    }

    /**
     * @return {@code true} if the presence is valid and they are playing fortnite.
     */
    public boolean isPlayingFortnite() {
        return properties != null && !status.isEmpty() && playing;
    }

    /**
     * @return {@code true} if they have a party.
     */
    public boolean hasParty() {
        return properties.partyJoinInfo != null && !properties.partyJoinInfo.isPrivate;
    }

    /**
     * @return the account for this presence sender.
     */
    public Account account() {
        if (account == null) {
            final var call = accountPublicService.findOneByAccountId(from.getLocalpartOrNull().asUnescapedString());
            final var result = Requests.executeCall(call);
            if (result.length == 0) throw EpicGamesErrorException.create("Cannot find account " + from.getLocalpartOrNull().asUnescapedString());
            account = result[0];
            return account;
        }
        return account;
    }

    /**
     * @return the profile for this presence sender.
     */
    public Profile profile() {
        final var call = friendsPublicService.profile(localAccountId, from.getLocalpartOrNull().asUnescapedString(), true);
        return Requests.executeCall(call);
    }

    /**
     * @return who its from.
     */
    public Jid from() {
        return from;
    }

    /**
     * Set who its from, always set internally.
     *
     * @param from who its from.
     */
    public void setFrom(Jid from) {
        this.from = from;
    }

    private static final class Properties {
        /**
         * The kairos profile JSON as a string.
         */
        private String KairosProfile_s;
        @Expose(deserialize = false)
        private KairosProfile kairosProfile;

        /**
         * The playlist
         */
        @SerializedName("GamePlaylistName_s")
        private String playlist;
        /**
         * How many players are alive.
         */
        @SerializedName("Event_PlayersAlive_s")
        private String eventPlayersAlive;
        /**
         * The party size
         */
        @SerializedName("Event_PartySize_s")
        private String eventPartySize;
        /**
         * The max party size.
         */
        @SerializedName("Event_PartyMaxSize_s")
        private String eventMaxPartySize;
        /**
         * The game session join key.
         */
        @SerializedName("GameSessionJoinKey_s")
        private String sessionKey;

        /**
         * Gameplay stats
         */
        @SerializedName("FortGameplayStats_j")
        private GameplayStats gameplayStats;

        /**
         * Party join info.
         */
        @SerializedName("party.joininfodata.286331153_j")
        private PartyJoinInfo partyJoinInfo;

        /**
         * Convert the string to a {@link KairosProfile} object.
         *
         * @return a {@link KairosProfile}
         */
        private KairosProfile profile() {
            if (kairosProfile == null) kairosProfile = Athena.GSON.fromJson(KairosProfile_s, KairosProfile.class);
            return kairosProfile;
        }

    }

    /**
     * A kairos profile.
     */
    private static final class KairosProfile {
        /**
         * The avatar ID.
         */
        private String avatar;
        /**
         * List of colors.
         */
        private String[] avatarBackground;
    }

    /**
     * Gameplat stats.
     */
    private static final class GameplayStats {
        /**
         * The state, seems to always be ""
         * the playlist
         */
        private String state, playlist;
        /**
         * Number of kills
         */
        @SerializedName("numKills")
        private int kills;
        /**
         * {@code true} if they fell to death.
         */
        @SerializedName("bFellToDeath")
        private boolean fellToDeath;
    }

    /**
     * Party join info.
     */
    private static final class PartyJoinInfo {

        /**
         * Account ID,
         * Display Name,
         * party Id
         * "Fortnite"
         * build ID
         */
        private String sourceId, sourceDisplayName, partyId, appId, buildId;
        /**
         * Flags,
         * Reason for not accepting
         * pc ??
         */
        private int partyFlags, notAcceptingReason, pc;
        /**
         * The party type ID.
         */
        private long partyTypeId;

        @SerializedName("bIsPrivate")
        private boolean isPrivate;
    }

}
