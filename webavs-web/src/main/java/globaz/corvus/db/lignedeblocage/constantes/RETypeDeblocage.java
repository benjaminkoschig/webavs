package globaz.corvus.db.lignedeblocage.constantes;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum RETypeDeblocage implements CodeSystemEnum<RETypeDeblocage> {
    CREANCIER(""),
    DETTE_EN_COMPTA(""),
    VERSEMENT_BENEFICIAIRE(""),
    IMPOTS_SOURCE("");

    private String csCode;

    RETypeDeblocage(String csCode) {
        this.csCode = csCode;
    }

    public static RETypeDeblocage fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, RETypeDeblocage.class);
    }

    @Override
    public String getValue() {
        return csCode;
    }
}
