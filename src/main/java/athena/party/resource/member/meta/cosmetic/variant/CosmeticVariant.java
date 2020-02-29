package athena.party.resource.member.meta.cosmetic.variant;

/**
 * Represents a cosmetic variant/stage.
 */
public final class CosmeticVariant {

    /**
     * AthenaCharacter,
     * "Parts",
     * "Stage1"
     */
    private String item, channel, variant;

    /**
     * @return the item
     */
    public String item() {
        return item;
    }

    /**
     * Set the item
     *
     * @param item the item
     */
    public void item(String item) {
        this.item = item;
    }

    /**
     * @return the channel.
     */
    public String channel() {
        return channel;
    }

    /**
     * Set the channel
     *
     * @param channel the channel
     */
    public void channel(String channel) {
        this.channel = channel;
    }

    /**
     * @return the variant
     */
    public String variant() {
        return variant;
    }

    /**
     * Set the variant
     *
     * @param variant the variant
     */
    public void variant(String variant) {
        this.variant = variant;
    }
}
