package athena.party.resource.member.meta.banner;

/**
 * Represents banner information for a member.
 */
public final class AthenaBanner {

    /**
     * The banner icon ID, eg "standardbanner15"
     * The banner color ID, eg "defaultcolor17"
     */
    private String bannerIconId = "standardbanner15", bannerColorId = "defaultcolor17";
    /**
     * The current season level.
     */
    private int seasonLevel = 1;

    public AthenaBanner() {
    }

    public AthenaBanner(String bannerIconId, String bannerColorId, int seasonLevel) {
        this.bannerIconId = bannerIconId;
        this.bannerColorId = bannerColorId;
        this.seasonLevel = seasonLevel;
    }

    /**
     * @return the banner icon ID.
     */
    public String bannerIconId() {
        return bannerIconId;
    }

    /**
     * Set the banner icon ID.
     *
     * @param bannerIconId the banner icon ID.
     */
    public void bannerIconId(String bannerIconId) {
        this.bannerIconId = bannerIconId;
    }

    /**
     * @return the banner color ID.
     */
    public String bannerColorId() {
        return bannerColorId;
    }

    /**
     * Set the banner color ID.
     *
     * @param bannerColorId the banner color ID.
     */
    public void bannerColorId(String bannerColorId) {
        this.bannerColorId = bannerColorId;
    }

    /**
     * @return the season level.
     */
    public int seasonLevel() {
        return seasonLevel;
    }

    /**
     * Set the season level
     *
     * @param seasonLevel the season level
     */
    public void seasonLevel(int seasonLevel) {
        this.seasonLevel = seasonLevel;
    }
}
