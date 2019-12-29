package athena.util.request;

import athena.exception.EpicGamesErrorException;

/**
 * Represents an asynchronous result.
 *
 * @param <T> TYPE.
 */
public interface Result<T> {

    /**
     * Invoked when the call is completed/executed.
     *
     * @param result the result, will be {@code null} if {@code failed} == true.
     * @param failed {@code true} if the request failed.
     * @param error  the error, will be {@code null} if {@code failed} == false.
     */
    void result(T result, boolean failed, EpicGamesErrorException error);

}
