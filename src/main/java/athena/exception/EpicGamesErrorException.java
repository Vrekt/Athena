package athena.exception;

import athena.util.json.JsonFind;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An exception that is thrown due to an error being returned by an endpoint.
 */
public final class EpicGamesErrorException extends RuntimeException {

    private int numericErrorCode;
    private JsonObject jsonObject;
    private String url, errorCode, errorMessage, service, intent;
    private List<String> messageVars = new ArrayList<>();

    /**
     * Initialize this exception.
     *
     * @param cause      the cause
     * @param url        the url of the request
     * @param jsonObject the json response.
     */
    private EpicGamesErrorException(String cause, String url, String errorCode, String errorMessage, JsonObject jsonObject) {
        super(cause);

        this.url = url;
        this.jsonObject = jsonObject;

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        final var vars = JsonFind.findArray(jsonObject, "messageVars");
        vars.forEach(element -> messageVars.add(element.getAsJsonPrimitive().getAsString()));
        this.numericErrorCode = JsonFind.findInt(jsonObject, "numericErrorCode");
        this.service = JsonFind.findString(jsonObject, "originatingService");
        this.intent = JsonFind.findString(jsonObject, "intent");
    }

    /**
     * Initialize this exception.
     *
     * @param cause the cause
     * @param url   the url of the request
     */
    private EpicGamesErrorException(String cause, String url) {
        super(cause);
        this.url = url;
    }

    private EpicGamesErrorException(String cause, Throwable other) {
        super(cause, other);
    }

    private EpicGamesErrorException(String cause) {
        super(cause);
    }

    /**
     * Create a new exception from another one.
     *
     * @param error the error
     * @return a new {@link EpicGamesErrorException}
     */
    public static EpicGamesErrorException createFromOther(Throwable error) {
        return new EpicGamesErrorException(error.getMessage(), error);
    }

    /**
     * Creates a new exception to be thrown.
     *
     * @param url    the URL that returned the error.
     * @param object the error object.
     * @return a new {@link EpicGamesErrorException}
     */
    public static EpicGamesErrorException create(final String url, final JsonObject object) {
        // make sure we have an actual error code and message.
        final var errorCode = JsonFind.findStringOptional(object, "errorCode").orElse("No Error Code?");
        final var errorMessage = JsonFind.findStringsOptional(object, "errorMessage", "message").orElse("No Error Message?");
        final var cause = "Failed to execute a request to: [" + url + "]\nError: " + errorCode + "\nMessage: " + errorMessage;
        return new EpicGamesErrorException(cause, url, errorCode, errorMessage, object);
    }

    /**
     * Creates a new exception to be thrown.
     *
     * @param url  the URL that returned the error
     * @param code the HTTP code.
     * @return a new {@link EpicGamesErrorException}
     */
    public static EpicGamesErrorException createFromHttpError(final String url, final int code) {
        final var cause = "Failed to execute a request to: [" + url + "]\nError: HTTP " + code;
        return new EpicGamesErrorException(cause, url);
    }

    /**
     * Create a super general error.
     *
     * @param cause the cause of this exception
     * @return a new {@link EpicGamesErrorException}
     */
    public static EpicGamesErrorException create(String cause) {
        return new EpicGamesErrorException(cause);
    }

    /**
     * @return the URL of the request
     */
    public String url() {
        return url;
    }

    /**
     * @return the error code, usually something like "com.epicgames.errors.xxx.xxx.etc"
     */
    public String errorCode() {
        return errorCode;
    }

    /**
     * @return the reason for the error
     */
    public String errorMessage() {
        return errorMessage;
    }

    /**
     * @return the variables that gave the error, for example two account IDs
     */
    public List<String> messageVars() {
        return messageVars;
    }

    /**
     * @return the service
     */
    public String service() {
        return service;
    }

    /**
     * @return the intent, usually prod
     */
    public String intent() {
        return intent;
    }

    /**
     * @return the error code
     */
    public int numericErrorCode() {
        return numericErrorCode;
    }

    /**
     * @return the json object of the error.
     */
    public JsonObject jsonObject() {
        return jsonObject;
    }
}
