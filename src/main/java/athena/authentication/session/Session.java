package athena.authentication.session;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Represents an authenticated session.
 */
public final class Session {

    @SerializedName("access_token")
    private final String accessToken;
    @SerializedName("refresh_token")
    private final String refreshToken;
    @SerializedName("account_id")
    private final String accountId;
    @SerializedName("expires_at")
    private final Instant accessTokenExpiresAt;
    @SerializedName("refresh_expires_at")
    private final Instant refreshTokenExpiresAt;

    /**
     * Initializes this session instance.
     *
     * @param accessToken           The access token
     * @param refreshToken          The refresh token
     * @param accessTokenExpiresAt  The time when the access token expires as string
     * @param refreshTokenExpiresAt The time when the refresh token expires as string
     * @param accountId             the ID of the account for this session
     */
    private Session(String accessToken, String refreshToken, String accessTokenExpiresAt, String refreshTokenExpiresAt, String accountId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresAt = Instant.parse(accessTokenExpiresAt);
        this.refreshTokenExpiresAt = Instant.parse(refreshTokenExpiresAt);
        this.accountId = accountId;
    }

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

}
