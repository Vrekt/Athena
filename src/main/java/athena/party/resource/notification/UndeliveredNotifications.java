package athena.party.resource.notification;

/**
 * The count of undelivered party notifications.
 */
public final class UndeliveredNotifications {

    /**
     * Number of undelivered invites and pings.
     */
    private int invites, pings;

    /**
     * @return number of invites
     */
    public int invites() {
        return invites;
    }

    /**
     * @return number of pings
     */
    public int pings() {
        return pings;
    }
}
