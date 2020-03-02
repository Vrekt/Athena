package athena.party.resource.member.meta.battlepass;

/**
 * Represents member battle pass information.
 */
public final class BattlePass {

    /**
     * {@code true} if the battle-pass is purchased.
     */
    private boolean bHasPurchasedPass = false;
    /**
     * Level, and other related XP.
     */
    private int passLevel = 1, selfBoostXp = 0, friendBoostXp = 0;

    /**
     * @return {@code true} if the battle pass has been purchased.
     */
    public boolean hasPurchasedPass() {
        return bHasPurchasedPass;
    }

    public BattlePass() {
    }

    public BattlePass(boolean bHasPurchasedPass, int passLevel, int selfBoostXp, int friendBoostXp) {
        this.bHasPurchasedPass = bHasPurchasedPass;
        this.passLevel = passLevel;
        this.selfBoostXp = selfBoostXp;
        this.friendBoostXp = friendBoostXp;
    }

    /**
     * Set has purchased the battle pass.
     *
     * @param bHasPurchasedPass the value
     */
    public void hasPurchasedPass(boolean bHasPurchasedPass) {
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
     * @return the client boost XP.
     */
    public int selfBoostXp() {
        return selfBoostXp;
    }

    /**
     * Set the client boost XP.
     *
     * @param selfBoostXp the client boost XP.
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
