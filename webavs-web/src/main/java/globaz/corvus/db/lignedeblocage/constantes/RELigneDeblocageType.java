/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage.constantes;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum RELigneDeblocageType implements CodeSystemEnum<RELigneDeblocageType> {
    CREANCIER("52863001"),
    DETTE_EN_COMPTA("52863002"),
    VERSEMENT_BENEFICIAIRE("52863003"),
    IMPOTS_SOURCE("52863004");

    private String csCode;

    RELigneDeblocageType(String csCode) {
        this.csCode = csCode;
    }

    public static RELigneDeblocageType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, RELigneDeblocageType.class);
    }

    @Override
    public String getValue() {
        return csCode;
    }

    public boolean isCreancier() {
        return CREANCIER.equals(this);
    }

    public boolean isDetteEnCompta() {
        return DETTE_EN_COMPTA.equals(this);
    }

    public boolean isVersementBeneficaire() {
        return VERSEMENT_BENEFICIAIRE.equals(this);
    }

    public boolean isImpotsSource() {
        return IMPOTS_SOURCE.equals(this);
    }
}