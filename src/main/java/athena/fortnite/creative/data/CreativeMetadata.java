package athena.fortnite.creative.data;

import athena.fortnite.creative.matchmaking.CreativeMatchmaking;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Represents meta-data for
 */
public final class CreativeMetadata {

    /**
     * Alternate titles for this creative.
     * The key is the language and the value is the title in that language.
     */
    @SerializedName("alt_title")
    private Map<String, String> alternateTitles;
    /**
     * Alternate introductions for this creative.
     * The key is the language and the value is the introduction in that language.
     */
    @SerializedName("alt_introduction")
    private Map<String, String> alternateIntroductions;
    /**
     * Map of image URLs for this creative in different sizes I believe.
     */
    @SerializedName("generated_image_urls")
    private Map<String, String> generatedImageUrls;

    /**
     * "",
     * "CreativePlot:flatgrid_large",
     * "title",
     * "en",
     * "xxxx"
     * "intro"
     */
    private String tagLine, islandType, title, locale, supportCode, introduction;

    /**
     * Matchmaking info for this creative.
     */
    private CreativeMatchmaking matchmaking;

    /**
     * @return Alternate titles for this creative.
     * The key is the language and the value is the title in that language.
     */
    public Map<String, String> alternateTitles() {
        return alternateTitles;
    }

    /**
     * @return Alternate introductions for this creative.
     * The key is the language and the value is the introduction in that language.
     */
    public Map<String, String> alternateIntroductions() {
        return alternateIntroductions;
    }

    /**
     * @return Map of image URLs for this creative in different sizes I believe.
     */
    public Map<String, String> generatedImageUrls() {
        return generatedImageUrls;
    }

    /**
     * @return the tag line for this creative
     */
    public String tagLine() {
        return tagLine;
    }

    /**
     * @return the island type, ex: "CreativePlot:flatgrid_large"
     */
    public String islandType() {
        return islandType;
    }

    /**
     * @return the title
     */
    public String title() {
        return title;
    }

    /**
     * @return the locale, ex: "en"
     */
    public String locale() {
        return locale;
    }

    /**
     * @return support a creator code for this island.
     */
    public String supportCode() {
        return supportCode;
    }

    /**
     * @return introduction text
     */
    public String introduction() {
        return introduction;
    }

    /**
     * @return the matchmaking information
     */
    public CreativeMatchmaking matchmaking() {
        return matchmaking;
    }
}
