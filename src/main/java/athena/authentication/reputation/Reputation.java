package athena.authentication.reputation;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Represents the reputation response.
 */
public final class Reputation {

    /**
     * The verdict.
     */
    private String verdict;

    /**
     * The captcha data.
     * "arkose_data":{"blob":"xxx"}}
     */
    @SerializedName("arkose_data")
    private JsonObject arkoseData;

    /**
     * @return the verdict
     */
    public String verdict() {
        return verdict;
    }

    /**
     * @return the arkose data.
     */
    public JsonObject arkoseData() {
        return arkoseData;
    }
}
