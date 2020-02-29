package athena.party.resource.member.meta.battlepass;

/**
 * Represents member battle pass information.
 */
public final class BattlePass {

    /**
     * {@code true} if the battle-pass is purchased.
     */
    private boolean bHasPurchasedPass;
    /**
     * Level, and other related XP.
     */
    private int passLevel, selfBoostXp, friendBoostXp;

    /**
     * @return {@code true} if the battle pass has been purchased.
     */
    public boolean bHasPurchasedPass() {
        return bHasPurchasedPass;
    }

    /**
     * Set has purchased the battle pass.
     *
     * @param bHasPurchasedPass the value
     */
    public void bHasPurchasedPass(boolean bHasPurchasedPass) {
        this.bHasPurchasedPass = bHasPurchasedPass;
    }

    /**
     * @return the battle pass level
     */
    public int passLevel() {
        return passLevel;
    }

    /**
     * Set the battle pass level
     *
     * @param passLevel the pass level
     */
    public void passLevel(int passLevel) {
        this.passLevel = passLevel;
    }

    /**
     * @return the self boost XP.
     */
    public int selfBoostXp() {
        return selfBoostXp;
    }

    /**
     * Set the self boost XP.
     *
     * @param selfBoostXp the self boost XP.
     */
    public void selfBoostXp(int selfBoostXp) {
        this.selfBoostXp = selfBoostXp;
    }

    /**
     * @return the friend boost XP.
     */
    public int friendBoostXp() {
        return friendBoostXp;
    }

    /**
     * Set the friend boost XP.
     *
     * @param friendBoostXp the friend boost XP.
     */
    public void friendBoostXp(int friendBoostXp) {
        this.friendBoostXp = friendBoostXp;
    }
}
