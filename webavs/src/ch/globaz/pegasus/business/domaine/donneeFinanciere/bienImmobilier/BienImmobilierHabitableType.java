package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum BienImmobilierHabitableType implements CodeSystemEnum<BienImmobilierHabitableType> {
    MAISON_INDIVIDUELLE("64023001"),
    IMMEUBLE_LOCATIF("64023002"),
    APPARTEMENT("64023003"),
    RURAL("64023004"),
    AUTRE("64023005");

    private String value;

    BienImmobilierHabitableType(String value) {
        this.value = value;
    }

    public static BienImmobilierHabitableType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, BienImmobilierHabitableType.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isMaisonIndividuelle() {
        return equals(MAISON_INDIVIDUELLE);
    }

    public boolean isImmeubleLocatif() {
        return equals(IMMEUBLE_LOCATIF);
    }

    public boolean isAppartement() {
        return equals(APPARTEMENT);
    }

    public boolean isRural() {
        return equals(RURAL);
    }

    public boolean isAutre() {
        return equals(AUTRE);
    }
}
