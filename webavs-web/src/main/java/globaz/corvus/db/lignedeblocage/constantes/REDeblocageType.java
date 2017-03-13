/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage.constantes;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum REDeblocageType implements CodeSystemEnum<REDeblocageType> {
    CREANCIER("52863001"),
    DETTE_EN_COMPTA("52863002"),
    VERSEMENT_BENEFICIAIRE("52863003"),
    IMPOTS_SOURCE("52863004");

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