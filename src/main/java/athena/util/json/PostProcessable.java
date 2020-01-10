package athena.util.json;

import athena.account.service.AccountPublicService;
import athena.context.AthenaContext;
import athena.friend.service.FriendsPublicService;
import com.google.gson.Gson;

/**
 * A utility class used to set the context after JSON parsing.
 */
public abstract class PostProcessable {

    /**
     * The athena context.
     */
    protected AthenaContext context;

    /**
     * Post process
     *
     * @param context the context
     */
    public void postProcess(AthenaContext context) {
        if (this.context == null) this.context = context;
    }

    /**
     * @return the local account ID.
     */
    protected String localAccountId() {
        return context.accountId();
    }

    /**
     * @return the {@link AccountPublicService}
     */
    protected AccountPublicService accountService() {
        return context.account();
    }

    /**
     * @return the {@link FriendsPublicService}
     */
    protected FriendsPublicService friendService() {
        return context.friendsService();
    }

    /**
     * @return athena GSON instance.
     */
    protected Gson gson() {
        return context.gson();
    }

}
