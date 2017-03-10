/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage.constantes;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum REEtatDeblocage implements CodeSystemEnum<REEtatDeblocage> {
    COMPTABILISE(""),
    ENREGISTRE(""),
    VALIDE("");

    private String csCode;

    REEtatDeblocage(String csCode) {
        this.csCode = csCode;
    }

    public static REEtatDeblocage fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, REEtatDeblocage.class);
    }

    @Override
    public String getValue() {
        return csCode;
    }
}
