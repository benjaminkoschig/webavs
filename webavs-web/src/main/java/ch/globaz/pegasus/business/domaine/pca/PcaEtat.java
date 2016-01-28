package ch.globaz.pegasus.business.domaine.pca;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum PcaEtat implements CodeSystemEnum<PcaEtat> {
    CALCULE("64029001"),
    VALIDE("64029002"),
    PARTIEL("64029003"),
    HISTORISEE("64029004");

    private String value;

    PcaEtat(String value) {
        this.value = value;
    }

    public static PcaEtat fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, PcaEtat.class);
    }

    @Override
    public String getValue() {
        return value;
    }
}
