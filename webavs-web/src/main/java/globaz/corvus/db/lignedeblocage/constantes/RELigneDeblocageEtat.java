/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage.constantes;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum RELigneDeblocageEtat implements CodeSystemEnum<RELigneDeblocageEtat> {
    COMPTABILISE("52862003"),
    ENREGISTRE("52862001"),
    VALIDE("52862002");

    private String csCode;

    RELigneDeblocageEtat(String csCode) {
        this.csCode = csCode;
    }

    public static RELigneDeblocageEtat fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, RELigneDeblocageEtat.class);
    }

    @Override
    public String getValue() {
        return csCode;
    }

    public boolean isComptabilise() {
        return COMPTABILISE.equals(this);
    }

    public boolean isEnregistre() {
        return ENREGISTRE.equals(this);
    }

    public boolean isValide() {
        return VALIDE.equals(this);
    }

}
