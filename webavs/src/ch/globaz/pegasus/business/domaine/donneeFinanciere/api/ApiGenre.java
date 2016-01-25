package ch.globaz.pegasus.business.domaine.donneeFinanciere.api;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum ApiGenre implements CodeSystemEnum<ApiGenre> {
    VIELLESSE("64015001"),
    INVALIDITE("64015002");

    private String value;

    ApiGenre(String value) {
        this.value = value;
    }

    public static ApiGenre fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, ApiGenre.class);
    }

    @Override
    public String getValue() {
        return value;
    }

}
