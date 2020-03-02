package athena.party.resource.member.meta.cosmetic;

import athena.party.resource.member.meta.cosmetic.variant.CosmeticVariant;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a members cosmetic loadout.
 */
public final class CosmeticLoadout {

    /**
     * Character
     */
    private String characterDef = "None", characterEKey = "";
    /**
     * Backpack
     */
    private String backpackDef = "None", backpackEKey = "";
    /**
     * Pickaxe
     */
    private String pickaxeDef = "None", pickaxeEKey = "";
    /**
     * Contrail
     */
    private String contrailDef = "None", contrailEKey = "";
    /**
     * Unknown yet.
     */
    private JsonArray scratchpad = new JsonArray();
    /**
     * List of cosmetic variants/stages.
     */
    private List<CosmeticVariant> variants = new ArrayList<>();

    /**
     * @return the character definition
     */
    public String characterDef() {
        return characterDef;
    }

    /**
     * Set the character definition
     *
     * @param characterDef the character definition
     */
    public void characterDef(String characterDef) {
        this.characterDef = characterDef;
    }

    /**
     * @return the character encryption key
     */
    public String characterEKey() {
        return characterEKey;
    }

    /**
     * Set the character encryption key
     *
     * @param characterEKey the character encryption key
     */
    public void characterEKey(String characterEKey) {
        this.characterEKey = characterEKey;
    }

    /**
     * @return the backpack definition
     */
    public String backpackDef() {
        return backpackDef;
    }

    /**
     * Set the backpack definition
     *
     * @param backpackDef the backpack definition
     */
    public void backpackDef(String backpackDef) {
        this.backpackDef = backpackDef;
    }

    /**
     * @return the backpack encryption key
     */
    public String backpackEKey() {
        return backpackEKey;
    }

    /**
     * Set the backpack encryption key
     *
     * @param backpackEKey the backpack encryption key
     */
    public void backpackEKey(String backpackEKey) {
        this.backpackEKey = backpackEKey;
    }

    /**
     * @return the pickaxe definition
     */
    public String pickaxeDef() {
        return pickaxeDef;
    }

    /**
     * Set the pickaxe definition
     *
     * @param pickaxeDef the pickaxe definition
     */
    public void pickaxeDef(String pickaxeDef) {
        this.pickaxeDef = pickaxeDef;
    }

    /**
     * @return the pickaxe encryption key
     */
    public String pickaxeEKey() {
        return pickaxeEKey;
    }

    /**
     * Set the pickaxe encryption key
     *
     * @param pickaxeEKey the pickaxe encryption key
     */
    public void pickaxeEKey(String pickaxeEKey) {
        this.pickaxeEKey = pickaxeEKey;
    }

    /**
     * @return the contrail definition
     */
    public String contrailDef() {
        return contrailDef;
    }

    /**
     * Set the contrail definition
     *
     * @param contrailDef the contrail definition
     */
    public void contrailDef(String contrailDef) {
        this.contrailDef = contrailDef;
    }

    /**
     * @return the contrail encryption key
     */
    public String contrailEKey() {
        return contrailEKey;
    }

    /**
     * Set the contrail encryption key
     *
     * @param contrailEKey the contrail encryption key
     */
    public void contrailEKey(String contrailEKey) {
        this.contrailEKey = contrailEKey;
    }

    /**
     * @return A json array.
     * TODO:
     */
    public JsonArray scratchpad() {
        return scratchpad;
    }

    /**
     * Set the scratchpad (for now just an empty array) {@code new JsonArray()}
     *
     * @param scratchpad the array
     */
    public void scratchpad(JsonArray scratchpad) {
        this.scratchpad = scratchpad;
    }

    /**
     * @return List of cosmetic variants
     */
    public List<CosmeticVariant> variants() {
        return variants;
    }

    /**
     * Set the list of cosmetic variants
     *
     * @param variants List of cosmetic variants
     */
    public void variants(List<CosmeticVariant> variants) {
        this.variants = variants;
    }
}
