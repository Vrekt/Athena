package athena.account.resource.address;

import com.google.gson.annotations.SerializedName;

/**
 * Represents an address inside an account.
 * @author Vrekt
 */
public final class AccountAddress {

    /**
     * The account ID.
     */
    @SerializedName("id")
    private String accountId;

    /**
     * The address information.
     */
    private String line1, line2, city, region, postalCode, country;
    /**
     * {@code true} if this address is the default.
     */
    private boolean defaultAddress;

    /**
     * @return the account ID.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the line 1 address.
     */
    public String line1() {
        return line1;
    }

    /**
     * @return the line 2 address.
     */
    public String line2() {
        return line2;
    }

    /**
     * @return the city
     */
    public String city() {
        return city;
    }

    /**
     * @return the region
     */
    public String region() {
        return region;
    }

    /**
     * @return the postal code.
     */
    public String postalCode() {
        return postalCode;
    }

    /**
     * @return the country
     */
    public String country() {
        return country;
    }

    /**
     * @return {@code true} if this address is the default.
     */
    public boolean defaultAddress() {
        return defaultAddress;
    }
}
