package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum ProprieteType implements CodeSystemEnum<ProprieteType> {
    PROPRIETAIRE("64009001"),
    USUFRUITIER("64009002"),
    DROIT_HABITATION("64009003"),
    NU_PROPRIETAIRE("64009004"),
    CO_PROPRIETAIRE("64009005");

    private String value;

    ProprieteType(String value) {
        this.value = value;
    }

    public static ProprieteType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, ProprieteType.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isProprietaire() {
        return equals(PROPRIETAIRE);
    }

    public boolean isUsufruit() {
        return equals(USUFRUITIER);
    }

    public boolean isDroitHabitation() {
        return equals(DROIT_HABITATION);
    }

    public boolean isNuProprietaire() {
        return equals(NU_PROPRIETAIRE);
    }

    public boolean isCoProprietaire() {
        return equals(CO_PROPRIETAIRE);
    }

}
