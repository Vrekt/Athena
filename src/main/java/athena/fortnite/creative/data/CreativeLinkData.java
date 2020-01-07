package athena.fortnite.creative.data;

/**
 * Represents link data inside a {@link athena.fortnite.creative.CreativeHistoryResult}
 */
public final class CreativeLinkData {

    /**
     * "7981-5279-7029",
     * "Creative:Island",
     * "xxx",
     * "name"
     */
    private String mnemonic, linkType, accountId, creatorName;
    /**
     * {@code true} if this creative is active?
     */
    private boolean active;
    /**
     * The version of this island/gamemode?
     */
    private int version;
    /**
     * Meta-data for this creative
     */
    private CreativeMetadata metadata;

    /**
     * @return the code/mnemonic
     */
    public String mnemonic() {
        return mnemonic;
    }

    /**
     * @return the link type, ex: Creative:Island
     */
    public String linkType() {
        return linkType;
    }

    /**
     * @return account ID owner of the island
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return name of who made/owns the island
     */
    public String creatorName() {
        return creatorName;
    }

    /**
     * @return {@code true} if this creative is active?
     */
    public boolean active() {
        return active;
    }

    /**
     * @return the version, sometimes -1
     */
    public int version() {
        return version;
    }

    /**
     * @return the meta-data.
     */
    public CreativeMetadata metadata() {
        return metadata;
    }
}
