package athena.account.resource;

import java.time.Instant;

/**
 * Represents an epic games profile.
 */
public final class EpicGamesProfile {

    /**
     * Account ID,
     * Display name
     * First name
     * Last name
     * email address
     * age group,
     * country (US, etc)
     * 123-456-7890
     * "en"
     * "UNKNOWN"
     */
    private String id, displayName, name, lastName, email, ageGroup, country, phoneNumber, preferredLanguage, minorStatus;

    /**
     * Number of failed login attempts
     * display name changes
     */
    private int failedLoginAttempts, numberOfDisplayNameChanges;
    /**
     * Last login and last display name change.
     */
    private Instant lastLogin, lastDisplayNameChange;
    /**
     * Headless??
     * {@code true} if display name can be changed
     * TFA?
     * {@code true} if email is verified
     * ??
     * ??
     */
    private boolean headless, canUpdateDisplayName, tfaEnabled, emailVerified, minorVerified, minorExpected;

    private EpicGamesProfile() {
    }

    /**
     * @return the account ID.
     */
    public String id() {
        return id;
    }

    /**
     * @return the display name
     */
    public String displayName() {
        return displayName;
    }

    /**
     * @return first name
     */
    public String name() {
        return name;
    }

    /**
     * @return last name
     */
    public String lastName() {
        return lastName;
    }

    /**
     * @return email address
     */
    public String email() {
        return email;
    }

    /**
     * @return the age group
     */
    public String ageGroup() {
        return ageGroup;
    }

    /**
     * @return the country
     */
    public String country() {
        return country;
    }

    /**
     * @return phone number
     */
    public String phoneNumber() {
        return phoneNumber;
    }

    /**
     * @return preferred language, ex: 'en'
     */
    public String preferredLanguage() {
        return preferredLanguage;
    }

    /**
     * @return "UNKNOWN" in some cases
     */
    public String minorStatus() {
        return minorStatus;
    }

    /**
     * @return number of failed login attempts
     */
    public int failedLoginAttempts() {
        return failedLoginAttempts;
    }

    /**
     * @return number of display name changes
     */
    public int numberOfDisplayNameChanges() {
        return numberOfDisplayNameChanges;
    }

    /**
     * @return last login time
     */
    public Instant lastLogin() {
        return lastLogin;
    }

    /**
     * @return the last display name change
     */
    public Instant lastDisplayNameChange() {
        return lastDisplayNameChange;
    }

    /**
     * @return ??
     */
    public boolean headless() {
        return headless;
    }

    /**
     * @return {@code true} if your display name can be changed
     */
    public boolean canUpdateDisplayName() {
        return canUpdateDisplayName;
    }

    /**
     * @return ??
     */
    public boolean tfaEnabled() {
        return tfaEnabled;
    }

    /**
     * @return {@code true} if the email associated with this account is verified.
     */
    public boolean emailVerified() {
        return emailVerified;
    }

    /**
     * @return ??
     */
    public boolean minorVerified() {
        return minorVerified;
    }

    /**
     * @return ??
     */
    public boolean minorExpected() {
        return minorExpected;
    }
}
