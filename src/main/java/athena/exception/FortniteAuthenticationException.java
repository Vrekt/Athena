package athena.exception;

/**
 * An exception that is thrown when Athena fails to authenticate (or login) to the Fortnite service.
 */
public final class FortniteAuthenticationException extends Exception {

    public FortniteAuthenticationException(String message) {
        super(message);
    }

    public FortniteAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
