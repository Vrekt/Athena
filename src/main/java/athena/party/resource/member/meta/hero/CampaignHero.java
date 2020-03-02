package athena.party.resource.member.meta.hero;

/**
 * Represents a hero definition.
 */
public final class CampaignHero {

    /**
     * The instance ID is ?? usually ""
     * Hero type is HID skin type.
     */
    private String heroItemInstanceId = "", heroType;

    /**
     * @return ""
     */
    public String heroItemInstanceId() {
        return heroItemInstanceId;
    }

    /**
     * Set the hero item instance ID.
     *
     * @param heroItemInstanceId the instance ID.
     */
    public void heroItemInstanceId(String heroItemInstanceId) {
        this.heroItemInstanceId = heroItemInstanceId;
    }

    /**
     * @return the hero type HID.
     */
    public String heroType() {
        return heroType;
    }

    /**
     * Set the hero type.
     *
     * @param heroType the hero type.
     */
    public void heroType(String heroType) {
        this.heroType = heroType;
    }
}
