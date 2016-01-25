package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier.bienImmobilierNonHabitable;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum BienImmobilierNonHabitableType implements CodeSystemEnum<BienImmobilierNonHabitableType> {
    ZONE_A_BATIR("64022001"),
    TERRAIN_AGRICOLE("64022002"),
    FORET("64022003"),
    CHEMIN("64022004"),
    AISANCE("64022005"),
    PISCINE("64022006"),
    VIGNE("64022007"),
    MINE("64022008"),
    AUTRES("64022009");

    private String value;

    BienImmobilierNonHabitableType(String value) {
        this.value = value;
    }

    public static BienImmobilierNonHabitableType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, BienImmobilierNonHabitableType.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isZonneABatir() {
        return equals(ZONE_A_BATIR);
    }

    public boolean isTerrainAgricole() {
        return equals(TERRAIN_AGRICOLE);
    }

    public boolean isForet() {
        return equals(FORET);
    }

    public boolean isChemin() {
        return equals(CHEMIN);
    }

    public boolean isAisance() {
        return equals(AISANCE);
    }

    public boolean isPiscine() {
        return equals(PISCINE);
    }

    public boolean isVigne() {
        return equals(VIGNE);
    }

    public boolean isMine() {
        return equals(MINE);
    }

    public boolean isAutre() {
        return equals(AUTRES);
    }
}
