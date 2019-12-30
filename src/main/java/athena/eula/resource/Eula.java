package athena.eula.resource;

/**
 * Represents a fortnite EULA.
 */
public final class Eula {

    /**
     * Key is usually FN, title of the EULA, body, locale and username/status.
     */
    private String key, title, body, locale, agentUserName, status;

    /**
     * Version and revision
     */
    private int version, revision;

    /**
     * Unknown values really
     */
    private boolean custom, wasDeclined, hasResponse;

    /**
     * @return the key of the EULA, "FN"
     */
    public String key() {
        return key;
    }

    /**
     * @return title of the EULA.
     */
    public String title() {
        return title;
    }

    /**
     * @return the body of the EULA.
     */
    public String body() {
        return body;
    }

    /**
     * @return the locale of the EULA.
     */
    public String locale() {
        return locale;
    }

    /**
     * @return username who requested EULA.
     */
    public String agentUserName() {
        return agentUserName;
    }

    /**
     * @return the status, usually "ACTIVE"?
     */
    public String status() {
        return status;
    }

    /**
     * @return current EULA version.
     */
    public int version() {
        return version;
    }

    /**
     * @return current EULA revision.
     */
    public int revision() {
        return revision;
    }

    /**
     * @return unknown.
     */
    public boolean custom() {
        return custom;
    }

    /**
     * @return {@code true} if the EULA was declined?
     */
    public boolean wasDeclined() {
        return wasDeclined;
    }

    /**
     * @return unknown.
     */
    public boolean hasResponse() {
        return hasResponse;
    }
}
