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
}
