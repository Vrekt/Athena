package athena.party.resource.member.meta.challenges;

/**
 * Represents assisted challenges.
 */
public final class AssistedChallengeInfo {

    /**
     * The quest item definition.
     */
    private String questItemDef;
    /**
     * How many objectives have been completed.
     */
    private int objectivesCompleted;

    /**
     * @return the quest item definition.
     */
    public String questItemDef() {
        return questItemDef;
    }

    /**
     * Set the quest item definition
     *
     * @param questItemDef the item definition
     */
    public void questItemDef(String questItemDef) {
        this.questItemDef = questItemDef;
    }

    /**
     * @return how many objectives have been completed.
     */
    public int objectivesCompleted() {
        return objectivesCompleted;
    }

    /**
     * Set how many objectives have been completed.
     *
     * @param objectivesCompleted how many objectives have been completed.
     */
    public void objectivesCompleted(int objectivesCompleted) {
        this.objectivesCompleted = objectivesCompleted;
    }
}
