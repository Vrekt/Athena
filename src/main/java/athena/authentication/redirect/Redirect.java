package athena.authentication.redirect;

/**
 * Represents the redirect response.
 */
public final class Redirect {

    /**
     * The redirect URL and SID.
     */
    private String redirectUrl, sid;

    /**
     * @return the redirect URL.
     */
    public String redirectUrl() {
        return redirectUrl;
    }

    /**
     * @return the SID, sometimes {@code null}
     */
    public String sid() {
        return sid;
    }
}
