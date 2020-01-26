package athena.account.resource.device;

import java.time.Instant;

/**
 * Represents a device-auth.
 */
public final class DeviceAuth {

    /**
     * Device ID, account ID, secret and user agent registered with.
     * Secret is used to login with device_auth, its only present after creating one - not retrieving.
     */
    private String deviceId, accountId, secret, userAgent;

    /**
     * The device info of this auth.
     */
    private Device deviceInfo;

    /**
     * Location, IP and created time of this device-auth.
     */
    private Created created;

    /**
     * @return the device ID.
     */
    public String deviceId() {
        return deviceId;
    }

    /**
     * @return the account ID.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the secret, only present after the response from creating a device auth.
     */
    public String secret() {
        return secret;
    }

    /**
     * @return the user-agent.
     */
    public String userAgent() {
        return userAgent;
    }

    /**
     * @return type/manufacturer
     */
    public String type() {
        return deviceInfo.type();
    }

    /**
     * @return device model
     */
    public String model() {
        return deviceInfo.model();
    }

    /**
     * @return device OS.
     */
    public String os() {
        return deviceInfo.os();
    }

    /**
     * @return the location of who created this device-auth.
     */
    public String location() {
        return created.location;
    }

    /**
     * @return the IP address of who created this device-auth.
     */
    public String ipAddress() {
        return created.ipAddress;
    }

    /**
     * @return when this device-auth was created.
     */
    public Instant created() {
        return created.dateTime;
    }

    /**
     * @return the device info
     */
    public Device deviceInfo() {
        return deviceInfo;
    }

    /**
     * Information about when this device-auth was created.
     */
    private static final class Created {

        /**
         * Location and IP.
         */
        private String location, ipAddress;
        /**
         * When it was created.
         */
        private Instant dateTime;

    }

}
