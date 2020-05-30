package athena.party.xmpp.event.party;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.party.resource.Party;
import athena.party.resource.assignment.SquadAssignment;
import athena.party.resource.configuration.privacy.PartyPrivacy;
import athena.party.resource.meta.PartyMeta;
import athena.party.resource.playlist.PartyPlaylistData;
import athena.util.json.request.Request;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;

/**
 * Represents an event for when the party is updated
 */
public final class PartyUpdatedEvent {

    /**
     * When this event was sent
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
     * The account ID of who is captain
     */
    @SerializedName("captain_id")
    private String captainId;

    /**
     * The list of removed meta elements
     */
    @SerializedName("party_state_removed")
    private List<String> removed;
    /**
     * The updated meta
     */
    @SerializedName("party_state_updated")
    private PartyMeta updated;
    /**
     * The overridden meta
     */
    @SerializedName("party_state_overridden")
    private PartyMeta overridden;
    /**
     * The privacy type {@link athena.party.resource.configuration.types.Joinability}
     */
    @SerializedName("party_privacy_type")
    private String privacyType;
    /**
     * "DEFAULT"
     */
    @SerializedName("party_type")
    private String partyType;
    /**
     * "default"
     */
    @SerializedName("party_sub_type")
    private String partySubtype;
    /**
     * the max number of members
     */
    @SerializedName("max_number_of_members")
    private int maxSize;
    /**
     * When the party was created
     */
    @SerializedName("created_at")
    private Instant createdAt;
    /**
     * When the party was updated
     */
    @SerializedName("updated_at")
    private Instant updatedAt;

    /**
     * The accounts provider
     */
    @Request(item = Accounts.class)
    private Accounts accounts;

    /**
     * The party
     */
    private Party party;

    /**
     * @return when this event was sent
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
     * @return the party ID
     */
    public String partyId() {
        return partyId;
    }

    /**
     * @return the account ID of who is captain
     */
    public String captainId() {
        return captainId;
    }

    /**
     * @return The account of who is captain
     */
    public Account captainAccount() {
        return accounts.findByAccountId(captainId);
    }

    /**
     * @return list of removed meta elements
     */
    public List<String> removed() {
        return removed;
    }

    /**
     * @return the updated meta
     */
    public PartyMeta updated() {
        return updated;
    }

    /**
     * @return the overridden meta
     */
    public PartyMeta overridden() {
        return overridden;
    }

    /**
     * @return the privacy type {@link athena.party.resource.configuration.types.Joinability}
     */
    public String privacyType() {
        return privacyType;
    }

    /**
     * @return "DEFAULT"
     */
    public String partyType() {
        return partyType;
    }

    /**
     * @return "default"
     */
    public String partySubtype() {
        return partySubtype;
    }

    /**
     * @return the max party size
     */
    public int maxSize() {
        return maxSize;
    }

    /**
     * @return when the party was created
     */
    public Instant createdAt() {
        return createdAt;
    }

    /**
     * @return when the party was updated
     */
    public Instant updatedAt() {
        return updatedAt;
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
     * @return {@code true} if the custom key was updated
     */
    public boolean didCustomKeyChange() {
        return updated.customMatchKey() != null;
    }

    /**
     * @return the custom key or {@code null} if none
     */
    public String customKey() {
        return didCustomKeyChange() ? updated.customMatchKey() : null;
    }

    /**
     * @return {@code true} if the lobby connection was started.
     */
    public boolean isLobbyConnectionStarted() {
        return updated.lobbyConnectionStarted();
    }

    /**
     * @return {@code true} if the privacy settings changed
     */
    public boolean didPrivacySettingsChange() {
        return updated.privacySettings() != null;
    }

    /**
     * @return the new privacy settings
     */
    public PartyPrivacy privacySettings() {
        return updated.privacySettings();
    }

    /**
     * @return {@code true} if the squad assignments changed
     */
    public boolean didSquadAssignmentsChange() {
        return updated.squadAssignments() != null;
    }

    /**
     * @return the new squad assignments
     */
    public List<SquadAssignment> squadAssignments() {
        return updated.squadAssignments();
    }

    /**
     * @return {@code true} if the session key changed
     */
    public boolean didSessionKeyChange() {
        return updated.sessionKey() != null;
    }

    /**
     * @return the new session key
     */
    public String sessionKey() {
        return updated.sessionKey();
    }

    /**
     * @return {@code true} if any playlist data changed.
     */
    public boolean didPlaylistChange() {
        return updated.playlistData() != null;
    }

    /**
     * @return the playlist data
     */
    public PartyPlaylistData playlist() {
        return updated.playlistData();
    }

    /**
     * @return squad fill
     */
    public boolean squadFill() {
        return updated.squadFill();
    }

}
