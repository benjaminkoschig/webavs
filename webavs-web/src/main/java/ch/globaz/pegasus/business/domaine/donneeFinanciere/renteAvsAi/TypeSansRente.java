package ch.globaz.pegasus.business.domaine.donneeFinanciere.renteAvsAi;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum TypeSansRente implements CodeSystemEnum<TypeSansRente> {

    INVALIDITE("64027003"),
    RENTE_SURVIVANT("64027002"),
    RENTE_VIEILLESSE("64027001"),
    INDEFINIT("0");

    private String value;

    TypeSansRente(String value) {
        this.value = value;
    }

    public static TypeSansRente fromValue(String value) {
        if (value != null && value.trim().length() > 0) {
            return CodeSystemEnumUtils.valueOfById(value, TypeSansRente.class);
        } else {
            return INDEFINIT;
        }
    }

    public boolean isIndefinit() {
        return INDEFINIT.equals(this);
    }

    @Override
    public String getValue() {
        return value;
    }
}
