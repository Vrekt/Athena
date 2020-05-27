package athena.party.resource.configuration;

import athena.party.resource.configuration.types.Joinability;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a Party configuration.
 */
public final class PartyConfiguration {

    /**
     * A public party configuration.
     */
    public static PartyConfiguration PUBLIC_PARTY = PartyConfiguration.open();
    /**
     * A friends only configuration
     */
    public static PartyConfiguration FRIENDS_ONLY_PARTY = PartyConfiguration.friendsOnly();
    /**
     * A closed/private party configuration.
     */
    public static PartyConfiguration PRIVATE_PARTY = PartyConfiguration.closed();

    /**
     * Invite time to live - when it expires.
     */
    public static final int INVITE_TTL = 14400;

    /**
     * The type of party, always "DEFAULT"
     */
    private String type;

    /**
     * The joinability of the party.
     */
    private Joinability joinability;

    /**
     * The max size of the party.
     */
    @SerializedName("max_size")
    private int maxSize;

    /**
     * The discoverability of the party.
     */
    private String discoverability;

    /**
     * The sub type of the party, always "default"
     */
    @SerializedName("sub_type")
    private String subType;

    /**
     * The invite time to live.
     */
    @SerializedName("invite_ttl")
    private int inviteTtl;

    /**
     * {@code true} if join confirmation is required.
     */
    @SerializedName("join_confirmation")
    private boolean joinConfirmation;

    /**
     * Related to the party meta cfg settings.
     * Presence permission is related to presence publishing of party join data.
     * Accepting members is a boolean field.
     * Join request action is usually "manual"
     * Invite permission is usually always "Anyone"
     * For a private party not accepting members reason is 7.
     * Chat enabled is a boolean
     * can join is a boolean.
     */
    private transient String presencePermission, acceptingMembers, joinRequestAction, invitePermission, notAcceptingMembersReason, chatEnabled, canJoin;

    /**
     * Static factory method for a public party.
     *
     * @return a new {@link PartyConfiguration}
     */
    public static PartyConfiguration open() {
        return new PartyConfiguration(Joinability.OPEN, "ALL", 16, true);
    }

    /**
     * Static factory method for a friends only party.
     *
     * @return a new {@link PartyConfiguration}
     */
    public static PartyConfiguration friendsOnly() {
        return new PartyConfiguration(Joinability.OPEN, "ALL", 16, true);
    }

    /**
     * Static factory method for a private party.
     *
     * @return a new {@link PartyConfiguration}
     */
    public static PartyConfiguration closed() {
        return new PartyConfiguration(Joinability.INVITE_AND_FORMER, "INVITED_ONLY", 16, true);
    }

    /**
     * Initialize this party configuration.
     *
     * @param joinability      the joinability.
     * @param discoverability  the discoverability.
     * @param maxSize          the max size.
     * @param joinConfirmation {@code true} if join confirmation is enabled/required.
     */
    private PartyConfiguration(Joinability joinability, String discoverability, int maxSize, boolean joinConfirmation) {
        this.type = "DEFAULT";
        this.joinability = joinability;
        this.discoverability = discoverability;
        this.subType = "default";
        this.maxSize = maxSize;
        this.inviteTtl = 14440;
        this.joinConfirmation = joinConfirmation;

        // party meta related configuration.
        joinRequestAction = "Manual";
        invitePermission = "Anyone";
        chatEnabled = "true";
        canJoin = "true";

        if (joinability == Joinability.OPEN) {
            presencePermission = "Anyone";
            acceptingMembers = "true";
            notAcceptingMembersReason = null;
        } else {
            presencePermission = "Noone";
            acceptingMembers = "false";
            notAcceptingMembersReason = "7";
        }

    }

    /**
     * @return type of party - "DEFAULT"
     */
    public String type() {
        return type;
    }

    /**
     * @return the joinability of the party
     */
    public Joinability joinability() {
        return joinability;
    }

    /**
     * @return the max size of the party.
     */
    public int maxSize() {
        return maxSize;
    }

    /**
     * @return the discoverability of the party - "ALL"
     */
    public String discoverability() {
        return discoverability;
    }

    /**
     * @return the sub_type - "default"
     */
    public String subType() {
        return subType;
    }

    /**
     * @return the invite time to live - when it expires.
     */
    public int inviteTtl() {
        return inviteTtl;
    }

    /**
     * @return {@code true} if join confirmation is required.
     */
    public boolean joinConfirmation() {
        return joinConfirmation;
    }

    /**
     * @return {@code true} if the party is open.
     */
    public boolean isOpen() {
        return joinability == Joinability.OPEN;
    }

    /**
     * @return {@code true} if the party is private.
     */
    public boolean isPrivate() {
        return joinability == Joinability.INVITE_AND_FORMER;
    }

    /**
     * @return the presence permission
     */
    public String presencePermission() {
        return presencePermission;
    }

    /**
     * @return if this party is accepting members
     */
    public boolean acceptingMembers() {
        return Boolean.parseBoolean(acceptingMembers);
    }

    /**
     * @return the join request action, usually "manual"
     */
    public String joinRequestAction() {
        return joinRequestAction;
    }

    /**
     * @return the invite permission
     */
    public String invitePermission() {
        return invitePermission;
    }

    /**
     * @return the not accepting members reason integer value
     */
    public int notAcceptingMembersReason() {
        return Integer.parseInt(notAcceptingMembersReason);
    }

    /**
     * @return if chat is enabled.
     */
    public boolean chatEnabled() {
        return Boolean.parseBoolean(chatEnabled);
    }

    /**
     * @return if members can join.
     */
    public boolean canJoin() {
        return Boolean.parseBoolean(canJoin);
    }

    /**
     * The presence permission setter.
     *
     * @param presencePermission the value
     */
    public void presencePermission(String presencePermission) {
        this.presencePermission = presencePermission;
    }

    /**
     * Sets the accepting members value.
     * Requires a boolean wrapped in a string.
     *
     * @param acceptingMembers the value
     */
    public void acceptingMembers(String acceptingMembers) {
        this.acceptingMembers = acceptingMembers;
    }

    /**
     * Sets the join request action, usually "manual"
     *
     * @param joinRequestAction the join request action.
     */
    public void joinRequestAction(String joinRequestAction) {
        this.joinRequestAction = joinRequestAction;
    }

    /**
     * The invite permission.
     *
     * @param invitePermission the invite permission
     */
    public void invitePermission(String invitePermission) {
        this.invitePermission = invitePermission;
    }

    /**
     * The not accepting members reason.
     * Requires a integer wrapped in a string.
     *
     * @param notAcceptingMembersReason the reason.
     */
    public void notAcceptingMembersReason(String notAcceptingMembersReason) {
        this.notAcceptingMembersReason = notAcceptingMembersReason;
    }

    /**
     * If chat is enabled or not.
     * Must be a boolean wrapped in a string.
     *
     * @param chatEnabled if chat is enabled.
     */
    public void chatEnabled(String chatEnabled) {
        this.chatEnabled = chatEnabled;
    }

    /**
     * If members can join.
     * Must be a boolean wrapped in a string.
     *
     * @param canJoin if members can join.
     */
    public void canJoin(String canJoin) {
        this.canJoin = canJoin;
    }
}
