package ch.globaz.pegasus.business.domaine.donneeFinanciere.api.autreApi;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum AutreApiType implements CodeSystemEnum<AutreApiType> {
    API_ACCIDENT("64014001"),
    API_MILITAIRE("64014002"),
    AUTRES("64014003");

    private String value;

    AutreApiType(String value) {
        this.value = value;
    }

    public static AutreApiType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, AutreApiType.class);
    }

    @Override
    public String getValue() {
        return value;
    }
}
