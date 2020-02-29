package athena.party.resource.member.meta.emote;

/**
 * Represents the emote playing.
 */
public final class FrontendEmote {

    /**
     * The emote item definition and encryption key.
     */
    private String emoteItemDef, emoteEKey;
    /**
     * -1 = stop, -2 = start
     */
    private int emoteSection;

    /**
     * @return the emote item definition
     */
    public String emoteItemDef() {
        return emoteItemDef;
    }

    /**
     * Set the emote item definition
     *
     * @param emoteItemDef the emote item definition.
     */
    public void emoteItemDef(String emoteItemDef) {
        this.emoteItemDef = emoteItemDef;
    }

    /**
     * @return the emote encryption key
     */
    public String emoteEKey() {
        return emoteEKey;
    }

    /**
     * Set the emote encryption key
     *
     * @param emoteEKey the key
     */
    public void emoteEKey(String emoteEKey) {
        this.emoteEKey = emoteEKey;
    }

    /**
     * @return the section, -1 = stopped, -2 = started
     */
    public int emoteSection() {
        return emoteSection;
    }

    /**
     * Set the emote section
     *
     * @param emoteSection the emote section
     */
    public void emoteSection(int emoteSection) {
        this.emoteSection = emoteSection;
    }
}
