package ch.globaz.pegasus.business.domaine.membreFamille;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum StatusRefugieApatride implements CodeSystemEnum<StatusRefugieApatride> {
    REFUGIE("64005001"),
    APATRIDE("64005002"),
    INDEFINIT("0");

    private String value;

    StatusRefugieApatride(String value) {
        this.value = value;
    }

    public static StatusRefugieApatride fromValue(String value) {
        if (value != null && value.isEmpty()) {
            return INDEFINIT;
        }
        return CodeSystemEnumUtils.valueOfById(value, StatusRefugieApatride.class);
    }

    @Override
    public String getValue() {
        return value;
    }
}
