package athena.party.resource.member.meta.assignments;

/**
 * Represents the squad assignment request of a member.
 */
public final class MemberSquadAssignmentRequest {

    /**
     * The starting index, target index
     * The swap target member ID to change with, sometimes is "INVALID" if no request.
     */
    private int startingAbsoluteIdx = -1, targetAbsoluteIdx = -1;
    private String swapTargetMemberId = "INVALID";
    /**
     * Version?
     */
    private int version = 0;

    /**
     * @return The starting index.
     */
    public int startingAbsoluteIdx() {
        return startingAbsoluteIdx;
    }

    /**
     * Set the starting index.
     *
     * @param startingAbsoluteIdx The starting index.
     */
    public MemberSquadAssignmentRequest startingAbsoluteIdx(int startingAbsoluteIdx) {
        this.startingAbsoluteIdx = startingAbsoluteIdx;
        return this;
    }

    /**
     * @return The target index
     */
    public int targetAbsoluteIdx() {
        return targetAbsoluteIdx;
    }

    /**
     * Set the target index.
     *
     * @param targetAbsoluteIdx The target index
     */
    public MemberSquadAssignmentRequest targetAbsoluteIdx(int targetAbsoluteIdx) {
        this.targetAbsoluteIdx = targetAbsoluteIdx;
        return this;
    }

    /**
     * @return the version (?)
     */
    public int version() {
        return version;
    }

    /**
     * Set the version
     *
     * @param version the version
     */
    public MemberSquadAssignmentRequest version(int version) {
        this.version = version;
        return this;
    }

    /**
     * @return the account ID of who to swap with.
     */
    public String swapTargetMemberId() {
        return swapTargetMemberId;
    }

    /**
     * Set the account ID of who to swap with
     *
     * @param swapTargetMemberId the account ID.
     */
    public MemberSquadAssignmentRequest swapTargetMemberId(String swapTargetMemberId) {
        this.swapTargetMemberId = swapTargetMemberId;
        return this;
    }
}
