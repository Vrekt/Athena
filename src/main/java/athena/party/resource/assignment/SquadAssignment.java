package athena.party.resource.assignment;

import com.google.gson.annotations.SerializedName;

/**
 * Represents squad assignments inside a party.
 */
public final class SquadAssignment {

    /**
     * The account ID.
     */
    private String memberId;

    /**
     * The index or position they are in.
     */
    @SerializedName("absoluteMemberIdx")
    private int memberIndex;

    public SquadAssignment(String memberId, int memberIndex) {
        this.memberId = memberId;
        this.memberIndex = memberIndex;
    }

    /**
     * @return the member ID.
     */
    public String memberId() {
        return memberId;
    }

    /**
     * @return the member index or position.
     */
    public int memberIndex() {
        return memberIndex;
    }
}
