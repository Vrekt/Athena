package athena.party.resource.user;

import athena.party.resource.Party;
import athena.party.resource.invite.PartyInvitation;
import athena.party.resource.ping.PartyPing;

import java.util.List;

/**
 * Represents data for a user.
 * Current party, pending, invites and pings.
 */
public final class UserPartyProfile {

    /**
     * The current party.
     * TODO: Pending? I'm assuming for joining parties.
     */
    private List<Party> current, pending;
    /**
     * List of party invitations
     */
    private List<PartyInvitation> invites;
    /**
     * List of party pings
     */
    private List<PartyPing> pings;

    /**
     * @return list of all current parties
     */
    public List<Party> parties() {
        return current;
    }

    /**
     * @return the current party.
     */
    public Party current() {
        return current.size() > 0 ? current.get(0) : null;
    }

    /**
     * @return list of pending parties
     */
    public List<Party> pending() {
        return pending;
    }

    /**
     * @return list of party invites
     */
    public List<PartyInvitation> invites() {
        return invites;
    }

    /**
     * @return list of party pings
     */
    public List<PartyPing> pings() {
        return pings;
    }
}
