package athena.account.resource;

import java.util.List;
import java.util.Optional;

/**
 * Holds data about external auths, the platform, id and display name.
 */
public final class ExternalAuth {

    private  String type, externalAuthIdType, accountId, externalDisplayName;

    private final ExternalPlatform platform;
    private final String externalAuthId, externalDisplayName;

    /**
     * Initialize this external auth.
     *
     * @param platform            the platform
     * @param externalAuthId      the ID
     * @param externalDisplayName the display name of the account on this platform
     */
    ExternalAuth(final ExternalPlatform platform, final String externalAuthId, final String externalDisplayName) {
        this.platform = platform;
        this.externalAuthId = externalAuthId;
        this.externalDisplayName = externalDisplayName;
    }

    /**
     * @return the platform of this external auth
     */
    public ExternalPlatform platform() {
        return platform;
    }

    /**
     * @return the ID of this external auth
     */
    public String externalAuthId() {
        return externalAuthId;
    }

    /**
     * @return the external display name
     */
    public String externalDisplayName() {
        return externalDisplayName;
    }

    /**
     * Represents platforms that are present in external platforms.
     */
    public enum ExternalPlatform {
        PSN("psn", "ps4"), XBL("xbl", "xb1");

        private final List<String> codes;

        ExternalPlatform(final String... codes) {
            this.codes = List.of(codes);
        }

        /**
         * Get the {@link ExternalPlatform} that corresponds to the given {@code code}
         *
         * @param code the API code.
         * @return a {@link Optional} that will contain the platform if valid.
         */
        public static Optional<ExternalPlatform> get(final String code) {
            if (code.equalsIgnoreCase("psn") || code.equalsIgnoreCase("ps4")) return Optional.of(PSN);
            if (code.equalsIgnoreCase("xb1") || code.equalsIgnoreCase("xbl")) return Optional.of(XBL);
            return Optional.empty();
        }

        /**
         * @return a list of codes for this platform
         */
        public List<String> codes() {
            return codes;
        }

    }

}
