package athena.stats.resource;

import athena.platform.Input;
import athena.stats.resource.type.StatisticType;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;

/**
 * Holds data for a single statistic value.
 */
final class StatisticValue {

    private final StatisticType type;
    private final Input input;
    private final String playlist;
    private final long value;

    StatisticValue(String key, JsonElement element) {
        // the type of statistic, ex: placetop1
        final var typeRaw = key.startsWith("s11") ? "s11_social_bp_level" : StringUtils.substringBetween(key, "br_", "_");
        // If typeRaw == null we hit a weird ass value like 'crucible2_timezone4_keyboardmouse_m0_playlist_crucible_solo'
        // TODO: Once I figure out whatever those values are (there are different timezone values too) support it?
        type = typeRaw == null ? StatisticType.UNKNOWN.identifier(key) : StatisticType.typeOf(typeRaw);

        // within unknown values, all other types will be null.

        // the input, ex: touch
        final var inputRaw = StringUtils.substringBetween(key, "br_" + type.identifier() + "_", "_m0_");
        input = Input.typeOf(inputRaw);

        // the playlist, ex: 'playlist_defaultsolo'
        playlist = StringUtils.substringAfter(key, inputRaw + "_m0_");
        value = element.getAsLong();
    }

    /**
     * @return the type of statistic.
     */
    StatisticType type() {
        return type;
    }

    /**
     * @return the input.
     */
    Input input() {
        return input;
    }

    /**
     * @return the playlist.
     */
    String playlist() {
        return playlist;
    }

    /**
     * @return the value.
     */
    Long value() {
        return value;
    }

}
