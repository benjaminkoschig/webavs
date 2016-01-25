package ch.globaz.pegasus.business.domaine.donneeFinanciere.api;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum ApiDegre implements CodeSystemEnum<ApiDegre> {
    FAIBLE("64016001"),
    MOYEN("64016002"),
    GRAVE("64016003"),
    INDEFINIT("0");

    private String value;

    ApiDegre(String value) {
        this.value = value;
    }

    public static ApiDegre fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, ApiDegre.class);
    }

    @Override
    public String getValue() {
        return value;
    }
}
