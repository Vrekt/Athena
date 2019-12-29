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

}
