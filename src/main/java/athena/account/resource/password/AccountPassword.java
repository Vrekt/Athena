package athena.account.resource.password;

/**
 * Represents a request to change an account password.
 * @author Vrekt, Armisto
 */
public final class AccountPassword {

    /**
     * The current password and then the new one to set it to.
     */
    private final String currentPassword, password;

    public AccountPassword(String currentPassword, String password) {
        this.currentPassword = currentPassword;
        this.password = password;
    }
}
