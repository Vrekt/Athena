package athena.exception;

/**
 * An exception thrown when the Athena builder is attempting to be built incomplete.
 */
public final class UnsupportedBuildException extends RuntimeException {

    public UnsupportedBuildException(String message) {
        super(message);
    }

}
