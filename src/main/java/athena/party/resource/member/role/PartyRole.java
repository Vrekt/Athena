package athena.party.resource.member.role;

/**
 * Represents the role within a party.
 */
public enum PartyRole {

    /**
     * The party leader.
     */
    CAPTAIN,
    /**
     * A party member.
     */
    MEMBER;

    /**
     * @return {@code true} if this role is leader/captain.
     */
    public boolean isLeader() {
        return this == CAPTAIN;
    }

    /**
     * @return {@code true} if this role is member.
     */
    public boolean isMember() {
        return this == MEMBER;
    }

}
