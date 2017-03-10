/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage.constantes;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum REDeblocageEtat implements CodeSystemEnum<REDeblocageEtat> {
    COMPTABILISE(""),
    ENREGISTRE(""),
    VALIDE("");

    private String csCode;

    REDeblocageEtat(String csCode) {
        this.csCode = csCode;
    }

    public static REDeblocageEtat fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, REDeblocageEtat.class);
    }

    @Override
    public String getValue() {
        return csCode;
    }
}
