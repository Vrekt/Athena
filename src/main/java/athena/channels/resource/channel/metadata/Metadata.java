package athena.channels.resource.channel.metadata;

/**
 * A key/value pair system for storing metadata in entities and messages.
 */
public final class Metadata {

    /**
     * K/V
     */
    private String key, value;

    public Metadata(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return the key.
     */
    public String key() {
        return key;
    }

    /**
     * @return the value.
     */
    public String value() {
        return value;
    }

}
