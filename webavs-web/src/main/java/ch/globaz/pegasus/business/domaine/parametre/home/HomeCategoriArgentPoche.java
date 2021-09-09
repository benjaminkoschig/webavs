package ch.globaz.pegasus.business.domaine.parametre.home;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum HomeCategoriArgentPoche implements CodeSystemEnum<HomeCategoriArgentPoche> {
    HANDICAP_PHYSIQUE("64072001"),
    EMS_NON_MEDICALISE_PSY("64072002"),
    EMS_NON_MEDICALISE_AGE("64072003"),
    NON_DEFINIE("64072004"),
    EPS("64072005"),
    SPEN("64072006"),
    INDEFINIT("");

    private String value;

    HomeCategoriArgentPoche(String value) {
        this.value = value;
    }

    public static HomeCategoriArgentPoche fromValue(String value) {
        if (value != null && value.trim().length() > 0 && !"0".equals(value)) {
            return CodeSystemEnumUtils.valueOfById(value, HomeCategoriArgentPoche.class);
        } else {
            return INDEFINIT;
        }
    }

    public boolean isHandicapPhysique() {
        return HANDICAP_PHYSIQUE.equals(this);
    }

    public boolean isEmsNonMedicalisePsy() {
        return EMS_NON_MEDICALISE_PSY.equals(this);
    }

    public boolean isEmsNonMedicaliseAge() {
        return EMS_NON_MEDICALISE_AGE.equals(this);
    }

    public boolean isIndefinit() {
        return INDEFINIT.equals(this);
    }

    @Override
    public String getValue() {
        return value;
    }
}
