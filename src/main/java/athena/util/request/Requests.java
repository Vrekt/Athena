package athena.util.request;

import athena.exception.EpicGamesErrorException;
import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import java.io.IOException;
import java.util.Optional;

/**
 * A utility class used to execute requests.
 */
public final class Requests {

    /**
     * The LOGGER.
     */
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    /**
     * The main GSON object.
     */
    private static final Gson GSON = new Gson();

    /**
     * Execute a call.
     *
     * @param call the call
     * @param <T>  the TYPE.
     * @return a {@link Optional} containing the type or empty.
     */
    @SuppressWarnings("ConstantConditions")
    public static <T> Optional<T> executeCallOptional(Call<T> call) {
        Response<T> result;
        try {
            result = call.execute();
        } catch (final Exception exception) {
            LOGGER.atWarning().withCause(exception).log("Failed to execute a request to [" + call.request().url().toString() + "].\n");
            throw EpicGamesErrorException.create("Failed to execute a request to [" + call.request().url().toString() + "].\n");
        }

        if (!result.isSuccessful()) {
            try {
                final var object = GSON.fromJson(result.errorBody().string(), JsonObject.class);
                throw EpicGamesErrorException.create(call.request().url().toString(), object);
            } catch (final IOException | NullPointerException exception) {
                LOGGER.atWarning().withCause(exception).log("Failed to execute a request to [" + call.request().url().toString() + "].\n");
                throw EpicGamesErrorException.create("Failed to execute a request to [" + call.request().url().toString() + "].\n");
            }
        }
        return Optional.ofNullable(result.body());
    }

    /**
     * Execute a call.
     *
     * @param call the call
     * @param <T>  the TYPE.
     * @return the TYPE or {@code null} if an error occurred.
     */
    public static <T> T executeCall(Call<T> call) {
        return executeCallOptional(call).orElseThrow(() -> EpicGamesErrorException.create("Failed to execute a request to [" + call.request().url().toString() + "].\n"));
    }

    /**
     * Executes a call async using {@link Call#enqueue(Callback)}
     *
     * @param call       the call
     * @param resultPost the result callback.
     * @param <T>        the TYPE.
     */
    public static <T> void executeCallAsync(Call<T> call, Result<T> resultPost) {
        call.enqueue(new Callback<>() {
            @Override
            @EverythingIsNonNull
            @SuppressWarnings("ConstantConditions")
            public void onResponse(Call<T> call, Response<T> response) {
                if (!response.isSuccessful()) {
                    try {
                        final var object = GSON.fromJson(response.errorBody().string(), JsonObject.class);
                        resultPost.result(null, true, EpicGamesErrorException.create(call.request().url().toString(), object));
                    } catch (final IOException | NullPointerException exception) {
                        LOGGER.atWarning().withCause(exception).log("Failed to execute a request to [" + call.request().url().toString() + "]");
                        resultPost.result(null, true, EpicGamesErrorException.createFromOther(exception));
                    }
                } else {
                    resultPost.result(response.body(), response.body() == null, null);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<T> call, Throwable t) {
                resultPost.result(null, true, EpicGamesErrorException.createFromOther(t));
            }
        });
    }

    /**
     * Executes a call that has no return.
     *
     * @param call the call
     * @param <T>  the TYPE.
     */
    public static <T> void executeVoidCall(Call<T> call) {
        executeCallOptional(call);
    }

}
