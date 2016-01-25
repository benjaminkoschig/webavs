package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum AutreRenteType implements CodeSystemEnum<AutreRenteType> {

    VIEILLESSE("64017001"),
    SURVIVANT("64017002"),
    INVALIDITE("64017003"),
    INDEFINI("0");

    private String value;

    AutreRenteType(String value) {
        this.value = value;
    }

    public static AutreRenteType fromValue(String value) {
        if (value != null && value.trim().length() > 0) {
            return CodeSystemEnumUtils.valueOfById(value, AutreRenteType.class);
        } else {
            return INDEFINI;
        }
    }

    @Override
    public String getValue() {
        return value;
    }
}
