package athena.util.json;

import athena.account.service.AccountPublicService;
import athena.friend.service.FriendsPublicService;

/**
 * A utility class used to post process objects with the necessary services.
 */
public abstract class PostProcessable {

    protected AccountPublicService accountPublicService;
    protected FriendsPublicService friendsPublicService;
    protected String localAccountId;

    /**
     * Post process
     *
     * @param accountPublicService the {@link AccountPublicService}
     * @param friendsPublicService the {@link FriendsPublicService}
     * @param localAccountId       the local account ID.
     */
    public void postProcess(AccountPublicService accountPublicService, FriendsPublicService friendsPublicService, String localAccountId) {
        this.accountPublicService = accountPublicService;
        this.friendsPublicService = friendsPublicService;
        this.localAccountId = localAccountId;
    }
}
