package athena.party.xmpp.event.invite;

import athena.context.AthenaContext;
import athena.party.resource.Party;
import athena.party.resource.meta.invites.PingOrInvitationMeta;
import athena.party.xmpp.event.IPartyEvent;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Represents a ping event.
 */
public final class PartyPingEvent extends AthenaContext implements IPartyEvent {

    /**
     * The account ID of who sent the ping.
     */
    @SerializedName("pinger_id")
    private String fromAccountId;

    /**
     * The display name of who sent the ping.
     */
    @SerializedName("pinger_dn")
    private String fromDisplayName;

    /**
     * When this ping expires and when it was sent.
     */
    private Instant expires, sent;

    /**
     * The metadata of this event.
     */
    private PingOrInvitationMeta meta;

    /**
     * The party for this ping.
     */
    private Party party;

    /**
     * @return The account ID of who sent the ping.
     */
    public String fromAccountId() {
        return fromAccountId;
    }

    /**
     * @return The display name of who sent the ping.
     */
    public String fromDisplayName() {
        return fromDisplayName;
    }

    /**
     * @return When this ping expires.
     */
    public Instant expires() {
        return expires;
    }

    /**
     * @return when this ping was sent.
     */
    public Instant sent() {
        return sent;
    }

    /**
     * @return The metadata of this event.
     */
    public PingOrInvitationMeta meta() {
        return meta;
    }

    /**
     * @return the party of this ping.
     */
    public Party party() {
        return party;
    }

    /**
     * Sets the party for this ping.
     *
     * @param party the party.
     */
    public void party(Party party) {
        this.party = party;
    }

    /**
     * Joins the party that this ping belongs to.
     *
     * @return the party.
     */
    public Party joinParty() {
        if (party == null) throw new IllegalStateException("No party to join!");
        this.party = parties.joinParty(party.partyId());
        return this.party;
    }

}
