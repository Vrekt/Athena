package athena.party.resource.member.meta.assignments;

/**
 * Represents the squad assignment request of a member.
 */
public final class MemberSquadAssignmentRequest {

    /**
     * The starting index, target index and version(?)
     * The swap target member ID to change with, sometimes is "INVALID" if no request.
     */
    private int startingAbsoluteIdx, targetAbsoluteIdx, version;
    private String swapTargetMemberId;

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
    public void startingAbsoluteIdx(int startingAbsoluteIdx) {
        this.startingAbsoluteIdx = startingAbsoluteIdx;
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
    public void targetAbsoluteIdx(int targetAbsoluteIdx) {
        this.targetAbsoluteIdx = targetAbsoluteIdx;
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
    public void version(int version) {
        this.version = version;
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
    public void swapTargetMemberId(String swapTargetMemberId) {
        this.swapTargetMemberId = swapTargetMemberId;
    }
}
