package ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum PensionAlimentaireType implements CodeSystemEnum<PensionAlimentaireType> {
    DUE("64031001"),
    VERSEE("64031002");

    private String value;

    PensionAlimentaireType(String value) {
        this.value = value;
    }

    public static PensionAlimentaireType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, PensionAlimentaireType.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isDue() {
        return equals(DUE);
    }

    public boolean isVersee() {
        return equals(VERSEE);
    }
}
