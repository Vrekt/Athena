package athena.account;

import athena.Athena;
import athena.account.resource.Account;
import athena.account.resource.EpicGamesProfile;
import athena.account.resource.device.Device;
import athena.account.resource.device.DeviceAuth;
import athena.account.service.AccountPublicService;
import athena.context.AthenaContext;
import athena.context.DefaultAthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.util.request.Requests;
import athena.util.request.Result;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Provides easy access to {@link AccountPublicService}
 *
 * @author Vrekt
 */
public final class Accounts extends AthenaContext {

    /**
     * The service that handles the requests.
     */
    private final AccountPublicService service;
    private final String accountId;

    public Accounts(DefaultAthenaContext context) {
        this.service = context.account();
        this.accountId = context.localAccountId();
    }

    /**
     * @return the service
     */
    public AccountPublicService service() {
        return service;
    }

    /**
     * Find an account by display name.
     *
     * @param displayName the display name of the account
     * @return a {@link Optional} that will contain the account if found.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public Account findByDisplayName(String displayName) throws EpicGamesErrorException {
        if (displayName == null) throw new NullPointerException("displayName is null.");
        final var call = service.findByDisplayName(displayName);
        return Requests.executeCall(call);
    }

    /**
     * Find an account by display name.
     *
     * @param displayName the display name of the account
     * @param callback    the callback
     */
    public void findByDisplayName(String displayName, Result<Account> callback) {
        if (displayName == null || callback == null) throw new NullPointerException("displayName or callback is null.");
        final var call = service.findByDisplayName(displayName);
        Requests.executeCallAsync(call, callback);
    }

    /**
     * Finds an account by account ID.
     *
     * @param accountId the ID of the account.
     * @return a {@link Optional} that will contain the account if found.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public Account findByAccountId(String accountId) throws EpicGamesErrorException {
        if (accountId == null) throw new NullPointerException("accountId is null.");
        final var call = service.findOneByAccountId(accountId);
        final var result = Requests.executeCall(call);
        if (result.length == 0) throw EpicGamesErrorException.create("Failed to find account " + accountId);
        return result[0];
    }

    /**
     * Find an account by ID.
     *
     * @param accountId the ID of the account
     * @param callback  the callback
     */
    public void findByAccountId(String accountId, Result<Account[]> callback) {
        if (accountId == null || callback == null) throw new NullPointerException("accountId or callback is null.");
        final var call = service.findOneByAccountId(accountId);
        Requests.executeCallAsync(call, callback);
    }

    /**
     * Find multiple accounts by account ID.
     *
     * @param accounts an array of accounts
     * @return a {@link List} that will NOT be empty if any accounts are found.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public List<Account> findManyByAccountId(String... accounts) throws EpicGamesErrorException {
        if (accounts == null) throw new NullPointerException("accounts is null.");
        if (accounts.length > 100) throw new IllegalArgumentException("accounts array cannot be greater than 100.");
        final var call = service.findManyByAccountId(accounts);
        return Requests.executeCall(call);
    }

    /**
     * Find multiple accounts async.
     *
     * @param callback the callback
     * @param accounts an array of accounts
     */
    public void findManyByAccountId(Result<List<Account>> callback, String... accounts) {
        if (callback == null || accounts == null) throw new NullPointerException("callback or accounts is null.");
        if (accounts.length > 100) throw new IllegalArgumentException("accounts array cannot be greater than 100.");
        final var call = service.findManyByAccountId(accounts);
        Requests.executeCallAsync(call, callback);
    }

    /**
     * Find multiple accounts by account ID.
     *
     * @param accounts a collection of accounts
     * @return a {@link List} that will NOT be empty if any accounts are found.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public List<Account> findManyByAccountId(Collection<String> accounts) throws EpicGamesErrorException {
        return findManyByAccountId(accounts.toArray(String[]::new));
    }

    /**
     * Find multiple accounts async.
     *
     * @param accounts a collection of accounts
     * @param callback the callback
     */
    public void findManyByAccountId(Collection<String> accounts, Result<List<Account>> callback) {
        findManyByAccountId(callback, accounts.toArray(String[]::new));
    }

    /**
     * Retrieve the profile of current authenticated account
     *
     * @return the {@link EpicGamesProfile}
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public EpicGamesProfile profile() throws EpicGamesErrorException {
        final var call = service.profile(accountId);
        return Requests.executeCall(call);
    }

    /**
     * Creates a new device-auth.
     *
     * @param device the device
     * @return a new {@link DeviceAuth}
     */
    public DeviceAuth createDeviceAuth(Device device) {
        final var toJson = Athena.GSON.toJson(device);
        final var call = service.createDeviceAuth(accountId, toJson);
        return Requests.executeCall(call);
    }

    /**
     * Delete a device auth.
     *
     * @param deviceId the device ID.
     */
    public void deleteDeviceAuth(String deviceId) {
        Requests.executeVoidCall(service.deleteDeviceAuth(accountId, deviceId));
    }

    /**
     * Deletes all device auths.
     */
    public void deleteAllDeviceAuths() {
        Requests.executeVoidCall(service.deviceAuths(accountId));
    }


}
