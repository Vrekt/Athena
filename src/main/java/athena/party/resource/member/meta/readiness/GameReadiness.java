package athena.party.resource.member.meta.readiness;

/**
 * Represents game readiness values.
 */
public enum GameReadiness {

    READY("Ready"), NOT_READY("NotReady"), SITTING_OUT("SittingOut");

    private final String name;

    GameReadiness(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
