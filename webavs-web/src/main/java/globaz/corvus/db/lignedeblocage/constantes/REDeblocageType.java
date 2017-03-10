/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage.constantes;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum REDeblocageType implements CodeSystemEnum<REDeblocageType> {
    CREANCIER(""),
    DETTE_EN_COMPTA(""),
    VERSEMENT_BENEFICIAIRE(""),
    IMPOTS_SOURCE("");

    private String csCode;

    REDeblocageType(String csCode) {
        this.csCode = csCode;
    }

    public static REDeblocageType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, REDeblocageType.class);
    }

    @Override
    public String getValue() {
        return csCode;
    }
}