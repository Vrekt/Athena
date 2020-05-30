package athena.party.resource.configuration.privacy;

import com.google.gson.annotations.SerializedName;

/**
 * Represents party privacy settings, different from a {@link athena.party.resource.configuration.PartyConfiguration}
 */
public final class PartyPrivacy {

    /**
     * The party type and invite restrictions.
     * For example: "Private" and "AnyMember"
     */
    private String partyType, partyInviteRestriction;

    /**
     * if {@code true} only friends of the leader can join.
     */
    @SerializedName("bOnlyLeaderFriendsCanJoin")
    private boolean onlyLeaderFriendsCanJoin;

    /**
     * Public privacy settings
     */
    public static PartyPrivacy PUBLIC = new PartyPrivacy("Public", "AnyMember", false);
    /**
     * Friends only privacy settings
     */
    public static PartyPrivacy FRIENDS_ONLY = new PartyPrivacy("FriendsOnly", "AnyMember", false);
    /**
     * Private privacy settings
     */
    public static PartyPrivacy PRIVATE = new PartyPrivacy("Private", "NoInvites", false);

    private PartyPrivacy(String partyType, String partyInviteRestriction, boolean onlyLeaderFriendsCanJoin) {
        this.partyType = partyType;
        this.partyInviteRestriction = partyInviteRestriction;
        this.onlyLeaderFriendsCanJoin = onlyLeaderFriendsCanJoin;
    }

    /**
     * @return the privacy type.
     */
    public String partyType() {
        return partyType;
    }

    /**
     * @return the invite restriction.
     */
    public String partyInviteRestriction() {
        return partyInviteRestriction;
    }

    /**
     * @return {@code true} if only friends of the leader can join.
     */
    public boolean onlyLeaderFriendsCanJoin() {
        return onlyLeaderFriendsCanJoin;
    }

    /**
     * @return {@code true} if this privacy is private.
     */
    public boolean isPrivate() {
        return partyType.equalsIgnoreCase("Private");
    }

}
