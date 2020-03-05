package athena.party.resource.member;

import athena.party.resource.connection.Connection;
import athena.party.resource.member.meta.PartyMemberMeta;
import athena.party.resource.member.role.PartyRole;
import athena.types.Input;
import athena.types.Platform;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;

/**
 * Represents a party member.
 */
public final class PartyMember {

    /**
     * The account ID of this member.
     */
    @SerializedName("account_id")
    private String accountId;

    /**
     * Metadata for this member.
     */
    private PartyMemberMeta meta;

    /**
     * List of connections for this member.
     */
    private List<Connection> connections;

    /**
     * The primary connection.
     */
    private Connection connection;

    /**
     * The revision of this member.
     */
    private int revision;

    /**
     * When this member was updated.
     */
    @SerializedName("updated_at")
    private Instant updatedAt;
    /**
     * When this member joined.
     */
    @SerializedName("joined_at")
    private Instant joinedAt;

    /**
     * The role of this member.
     */
    private PartyRole role;

    /**
     * @return the account ID of this party member.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the display name of this member.
     */
    public String displayName() {
        return meta.displayName();
    }

    /**
     * @return the meta for this member
     */
    public PartyMemberMeta meta() {
        return meta;
    }

    /**
     * @return the current input type
     */
    public Input currentInputType() {
        return meta.currentInputType();
    }

    /**
     * @return the current emote or {@code ""} if no emote is playing
     */
    public String currentEmote() {
        return meta.frontendEmote().emoteItemDef();
    }

    /**
     * @return {@code true} if an emote is playing.
     */
    public boolean isEmotePlaying() {
        return meta.frontendEmote().emoteSection() == -2;
    }

    /**
     * @return {@code true} if this member has preloaded their content.
     */
    public boolean hasPreloaded() {
        return meta.hasPreloaded();
    }

    /**
     * @return {@code "Enabled"} if voice chat is enabled.
     */
    public String voiceChatStatus() {
        return meta.voiceChatStatus();
    }

    /**
     * @return the game readiness, ex: "NotReady"
     */
    public String gameReadiness() {
        return meta.gameReadiness();
    }

    /**
     * @return the platform of this member.
     */
    public Platform platform() {
        return meta.platform();
    }

    /**
     * @return the character or skin of this member.
     */
    public String character() {
        return meta.cosmeticLoadout().characterDef();
    }

    /**
     * @return the backpack of this member
     */
    public String backpack() {
        return meta.cosmeticLoadout().backpackDef();
    }

    /**
     * @return the pickaxe of this member
     */
    public String pickaxe() {
        return meta.cosmeticLoadout().pickaxeDef();
    }

    /**
     * @return the contrail of this member.
     */
    public String contrail() {
        return meta.cosmeticLoadout().contrailDef();
    }

    /**
     * @return when the match was started.
     */
    public Instant timeStartedMatch() {
        return meta.timeStartedMatch();
    }

    /**
     * @return the location of this member, ex: "PreLobby"
     */
    public String location() {
        return meta.location();
    }

    /**
     * @return a list of connections
     */
    public List<Connection> connections() {
        return connections;
    }

    /**
     * @return the first or primary connection for this member.
     */
    public Connection connection() {
        return connection;
    }

    /**
     * @return the current revision
     */
    public int revision() {
        return revision;
    }

    /**
     * @return when this member was updated.
     */
    public Instant updatedAt() {
        return updatedAt;
    }

    /**
     * @return when this member joined.
     */
    public Instant joinedAt() {
        return joinedAt;
    }

    /**
     * @return the role of this member.
     */
    public PartyRole role() {
        return role;
    }

    /**
     * Set the role of this member
     *
     * @param role the role
     */
    public void role(PartyRole role) {
        this.role = role;
    }

    /**
     * @return {@code true} if this member is emoting.
     */
    public boolean isEmoting() {
        return meta != null && meta.isEmoting();
    }

    /**
     * @return {@code true} if this member is readied up.
     */
    public boolean isReady() {
        return meta != null && meta.gameReadiness().equals("Ready");
    }

    /**
     * @return {@code true} if this member is sitting out.
     */
    public boolean isSittingOut() {
        return meta != null && meta.gameReadiness().equals("SittingOut");
    }

}
