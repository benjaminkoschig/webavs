package ch.globaz.pegasus.business.domaine.parametre.home;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum HomeCategorie implements CodeSystemEnum<HomeCategorie> {
    MEDICALISE("64010001"),
    NON_MEDICALISE("64010002"),
    INDEFINIT("");

    private String value;

    HomeCategorie(String value) {
        this.value = value;
    }

    public static HomeCategorie fromValue(String value) {
        if (value != null && value.trim().length() > 0 && !"0".equals(value)) {
            return CodeSystemEnumUtils.valueOfById(value, HomeCategorie.class);
        } else {
            return INDEFINIT;
        }
    }

    public boolean isMedicalise() {
        return MEDICALISE.equals(this);
    }

    public boolean isNonMedicalise() {
        return NON_MEDICALISE.equals(this);
    }

    public boolean isIndefinit() {
        return INDEFINIT.equals(this);
    }

    @Override
    public String getValue() {
        return value;
    }
}
