package athena.party.resource.configuration;

import athena.party.resource.configuration.types.Joinability;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a Party configuration.
 */
public final class PartyConfiguration {

    /**
     * A public party configuration.
     */
    public static PartyConfiguration PUBLIC_PARTY = PartyConfiguration.open();
    /**
     * A friends only configuration
     */
    public static PartyConfiguration FRIENDS_ONLY_PARTY = PartyConfiguration.friendsOnly();
    /**
     * A closed/private party configuration.
     */
    public static PartyConfiguration PRIVATE_PARTY = PartyConfiguration.closed();

    /**
     * Invite time to live - when it expires.
     */
    public static final int INVITE_TTL = 14400;

    /**
     * The type of party, always "DEFAULT"
     */
    private String type;

    /**
     * The joinability of the party.
     */
    private Joinability joinability;

    /**
     * The max size of the party.
     */
    @SerializedName("max_size")
    private int maxSize;

    /**
     * The discoverability of the party.
     */
    private String discoverability;

    /**
     * The sub type of the party, always "default"
     */
    @SerializedName("sub_type")
    private String subType;

    /**
     * The invite time to live.
     */
    @SerializedName("invite_ttl")
    private int inviteTtl;

    /**
     * {@code true} if join confirmation is required.
     */
    @SerializedName("join_confirmation")
    private boolean joinConfirmation;

    /**
     * Static factory method for a public party.
     *
     * @return a new {@link PartyConfiguration}
     */
    public static PartyConfiguration open() {
        return new PartyConfiguration(Joinability.OPEN, "ALL", 16, true);
    }

    /**
     * Static factory method for a friends only party.
     *
     * @return a new {@link PartyConfiguration}
     */
    public static PartyConfiguration friendsOnly() {
        return new PartyConfiguration(Joinability.OPEN, "ALL", 16, true);
    }

    /**
     * Static factory method for a private party.
     *
     * @return a new {@link PartyConfiguration}
     */
    public static PartyConfiguration closed() {
        return new PartyConfiguration(Joinability.INVITE_AND_FORMER, "ALL", 16, true);
    }

    /**
     * Initialize this party configuration.
     *
     * @param joinability      the joinability.
     * @param discoverability  the discoverability.
     * @param maxSize          the max size.
     * @param joinConfirmation {@code true} if join confirmation is enabled/required.
     */
    private PartyConfiguration(Joinability joinability, String discoverability, int maxSize, boolean joinConfirmation) {
        this.type = "DEFAULT";
        this.joinability = joinability;
        this.discoverability = discoverability;
        this.subType = "default";
        this.maxSize = maxSize;
        this.inviteTtl = INVITE_TTL;
        this.joinConfirmation = joinConfirmation;
    }

    /**
     * @return type of party - "DEFAULT"
     */
    public String type() {
        return type;
    }

    /**
     * @return the joinability of the party
     */
    public Joinability joinability() {
        return joinability;
    }

    /**
     * @return the max size of the party.
     */
    public int maxSize() {
        return maxSize;
    }

    /**
     * @return the discoverability of the party - "ALL"
     */
    public String discoverability() {
        return discoverability;
    }

    /**
     * @return the sub_type - "default"
     */
    public String subType() {
        return subType;
    }

    /**
     * @return the invite time to live - when it expires.
     */
    public int inviteTtl() {
        return inviteTtl;
    }

    /**
     * @return {@code true} if join confirmation is required.
     */
    public boolean joinConfirmation() {
        return joinConfirmation;
    }

    /**
     * @return {@code true} if the party is open.
     */
    public boolean isOpen() {
        return joinability == Joinability.OPEN;
    }

    /**
     * @return {@code true} if the party is private.
     */
    public boolean isPrivate() {
        return joinability == Joinability.INVITE_AND_FORMER;
    }

}
