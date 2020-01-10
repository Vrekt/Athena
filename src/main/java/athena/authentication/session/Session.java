package athena.authentication.session;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Represents an authenticated session.
 */
public final class Session {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("account_id")
    private String accountId;
    @SerializedName("expires_at")
    private Instant accessTokenExpiresAt;
    @SerializedName("refresh_expires_at")
    private Instant refreshTokenExpiresAt;
    @SerializedName("client_id")
    private String clientId;
    @SerializedName("in_app_id")
    private String inAppId;
    @SerializedName("device_id")
    private String deviceId;

    /**
     * @return the access token.
     */
    public String accessToken() {
        return accessToken;
    }

    /**
     * @return the refresh token.
     */
    public String refreshToken() {
        return refreshToken;
    }

    /**
     * @return the account ID this session belongs to.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return when the access token expires.
     */
    public Instant accessTokenExpiresAt() {
        return accessTokenExpiresAt;
    }

    /**
     * @return when the refresh token expires.
     */
    public Instant refreshTokenExpiresAt() {
        return refreshTokenExpiresAt;
    }

    /**
     * @return the client ID.
     */
    public String clientId() {
        return clientId;
    }

    /**
     * @return the in-app ID.
     */
    public String inAppId() {
        return inAppId;
    }

    /**
     * @return the device ID.
     */
    public String deviceId() {
        return deviceId;
    }
}
