package athena.util.json;

import athena.account.service.AccountPublicService;
import athena.friend.service.FriendsPublicService;

/**
 * A utility class used to post process objects with the necessary services.
 */
public interface PostProcessable {

    /**
     * Post process
     *
     * @param accountPublicService the {@link AccountPublicService}
     * @param friendsPublicService the {@link FriendsPublicService}
     * @param localAccountId       the local account ID.
     */
    default void postProcess(AccountPublicService accountPublicService, FriendsPublicService friendsPublicService, String localAccountId) {
        //
    }
}
