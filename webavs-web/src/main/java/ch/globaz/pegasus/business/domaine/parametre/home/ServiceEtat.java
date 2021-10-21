package ch.globaz.pegasus.business.domaine.parametre.home;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum ServiceEtat implements CodeSystemEnum<ServiceEtat> {
    SASH("64025001"),
    SPAS("64025002"),
    SPJ("64025003"),
    SAS("64025004"),
    SSP("64025005"),
    EMS("64025006"),
    LITS_ATTENTE("64025007"),
    INSTITUTION("64025008"),
    EPS("64025009"),
    SPEN("64025010"),
    DGEJ_SESAF("64025011"),
    DGEJ_FOYER("64025012"),
    DGEJ_FA("64025013"),
    INDEFINIT("");

    private String value;

    ServiceEtat(String value) {
        this.value = value;
    }

    public static ServiceEtat fromValue(String value) {
        if (value != null && value.trim().length() > 0 && !"0".equals(value)) {
            return CodeSystemEnumUtils.valueOfById(value, ServiceEtat.class);
        } else {
            return INDEFINIT;
        }
    }

    public boolean isEms() {
        return EMS.equals(this);
    }

    public boolean isInstitution() {
        return INSTITUTION.equals(this);
    }

    public boolean isListAttente() {
        return LITS_ATTENTE.equals(this);
    }

    public boolean isIndefinit() {
        return INDEFINIT.equals(this);
    }

    public boolean isSpen() {
        return SPEN.equals(this);
    }

    @Override
    public String getValue() {
        return value;
    }
}