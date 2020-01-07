package athena.fortnite.creative;

import athena.fortnite.creative.data.CreativeLinkData;
import athena.fortnite.creative.data.CreativeMetadata;
import athena.fortnite.creative.matchmaking.CreativeMatchmaking;
import athena.fortnite.service.FortnitePublicService;

import java.time.Instant;
import java.util.Map;

/**
 * Represents a singular result from "fortnite/api/game/v2/creative/history/{accountId}" {@link FortnitePublicService}
 */
public final class CreativeHistoryResult {

    /**
     * The date this was sorted?
     */
    private Instant sortDate;

    /**
     * Link-data for this creative
     */
    private CreativeLinkData linkData;

    /**
     * {@code true} if this is a favorite.
     */
    private boolean isFavorite;

    /**
     * @return the sort date
     */
    public Instant sortDate() {
        return sortDate;
    }

    /**
     * @return link-data
     */
    public CreativeLinkData linkData() {
        return linkData;
    }

    /**
     * @return {@code true} if this creative is a favorite.
     */
    public boolean isFavorite() {
        return isFavorite;
    }

    /**
     * @return the code/mnemonic
     */
    public String mnemonic() {
        return linkData.mnemonic();
    }

    /**
     * @return the link type, ex: Creative:Island
     */
    public String linkType() {
        return linkData.linkType();
    }

    /**
     * @return account ID owner of the island
     */
    public String accountId() {
        return linkData.accountId();
    }

    /**
     * @return name of who made/owns the island
     */
    public String creatorName() {
        return linkData.creatorName();
    }

    /**
     * @return {@code true} if this creative is active?
     */
    public boolean active() {
        return linkData.active();
    }

    /**
     * @return the version, sometimes -1
     */
    public int version() {
        return linkData.version();
    }

    /**
     * @return the meta-data.
     */
    public CreativeMetadata metadata() {
        return linkData.metadata();
    }

    /**
     * @return Alternate titles for this creative.
     * The key is the language and the value is the title in that language.
     */
    public Map<String, String> alternateTitles() {
        return metadata().alternateTitles();
    }

    /**
     * @return Alternate introductions for this creative.
     * The key is the language and the value is the introduction in that language.
     */
    public Map<String, String> alternateIntroductions() {
        return metadata().alternateIntroductions();
    }

    /**
     * @return Map of image URLs for this creative in different sizes I believe.
     */
    public Map<String, String> generatedImageUrls() {
        return metadata().generatedImageUrls();
    }

    /**
     * @return the tag line for this creative
     */
    public String tagLine() {
        return metadata().tagLine();
    }

    /**
     * @return the island type, ex: "CreativePlot:flatgrid_large"
     */
    public String islandType() {
        return metadata().islandType();
    }

    /**
     * @return the title
     */
    public String title() {
        return metadata().title();
    }

    /**
     * @return the locale, ex: "en"
     */
    public String locale() {
        return metadata().locale();
    }

    /**
     * @return support a creator code for this island.
     */
    public String supportCode() {
        return metadata().supportCode();
    }

    /**
     * @return introduction text
     */
    public String introduction() {
        return metadata().introduction();
    }

    /**
     * @return the matchmaking information
     */
    public CreativeMatchmaking matchmaking() {
        return metadata().matchmaking();
    }

    /**
     * @return the join in progress type, eg "Spectate"
     */
    public String joinInProgressType() {
        return matchmaking().joinInProgressType();
    }

    /**
     * @return mms type ? "off"
     */
    public String mmsType() {
        return matchmaking().mmsType();
    }

    /**
     * @return players per team.
     */
    public int playersPerTeam() {
        return matchmaking().playersPerTeam();
    }

    /**
     * @return max number of players
     */
    public int maximumNumberOfPlayers() {
        return matchmaking().maximumNumberOfPlayers();
    }

    /**
     * @return the player count
     */
    public int playerCount() {
        return matchmaking().playerCount();
    }

    /**
     * @return number of teams
     */
    public int numberOfTeams() {
        return matchmaking().numberOfTeams();
    }

    /**
     * @return min number of players
     */
    public int minimumNumberOfPlayers() {
        return matchmaking().minimumNumberOfPlayers();
    }

    /**
     * @return {@code true} if join in progress is allowed.
     */
    public boolean allowJoinInProgress() {
        return matchmaking().allowJoinInProgress();
    }

}
