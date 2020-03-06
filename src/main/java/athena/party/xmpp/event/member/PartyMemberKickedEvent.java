package athena.party.xmpp.event.member;

import athena.account.resource.Account;
import athena.context.AthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.friend.resource.summary.Profile;
import athena.party.resource.Party;
import athena.party.resource.member.meta.PartyMemberMeta;
import athena.util.request.Requests;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Represents an event for when a member is kicked
 */
public final class PartyMemberKickedEvent extends AthenaContext {

    /**
     * When this event was sent.
     */
    private Instant sent;
    /**
     * The revision
     */
    private int revision;
    /**
     * The party ID.
     */
    @SerializedName("party_id")
    private String partyId;
    /**
     * The account ID of who got kicked
     */
    @SerializedName("account_id")
    private String accountId;
    /**
     * Their updated state.
     */
    @SerializedName("member_state_updated")
    private PartyMemberMeta updated;

    /**
     * The party
     */
    private Party party;

    /**
     * @return when it was sent
     */
    public Instant sent() {
        return sent;
    }

    /**
     * @return the revision
     */
    public int revision() {
        return revision;
    }

    /**
     * @return the party ID.
     */
    public String partyId() {
        return partyId;
    }

    /**
     * @return The account ID of who got kicked
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return Their updated state.
     */
    public PartyMemberMeta updated() {
        return updated;
    }

    /**
     * @return the party of this event
     */
    public Party party() {
        return party;
    }

    /**
     * Sets the party for this event
     *
     * @param party the party.
     */
    public void party(Party party) {
        this.party = party;
    }

    /**
     * @return the account of who got kicked
     */
    public Account account() {
        final var call = accountPublicService.findOneByAccountId(accountId);
        final var result = Requests.executeCall(call);
        if (result.length == 0) throw EpicGamesErrorException.create("Failed to find account " + accountId);
        return result[0];
    }

    /**
     * Retrieves the friend profile for this member.
     * They must be a friend for this to succeed.
     *
     * @return the friend profile
     */
    public Profile friendProfile() {
        return Requests.executeCall(friendsPublicService.profile(localAccountId, accountId, true));
    }

}
