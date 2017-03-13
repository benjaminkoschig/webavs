/*
 * Globaz SA.
 */
package ch.globaz.pegasus.business.constantes;

import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDeblocage;

public enum EPCEtatDeblocage {
    COMPTABILISE(IPCDeblocage.CS_ETAT_COMPTABILISE),
    ENREGISTRE(IPCDeblocage.CS_ETAT_ENREGISTRE),
    VALIDE(IPCDeblocage.CS_ETAT_VALIDE);

    private String csCode;

    EPCEtatDeblocage(String csCode) {
        this.csCode = csCode;
    }

    public static EPCEtatDeblocage getEnumByCsCode(String csCode) {
        if (COMPTABILISE.csCode.equals(csCode)) {
            return COMPTABILISE;
        } else if (ENREGISTRE.csCode.equals(csCode)) {
            return ENREGISTRE;
        } else if (VALIDE.csCode.equals(csCode)) {
            return VALIDE;
        }
        throw new IllegalArgumentException("No Enum specified for this string " + csCode);
    }

    public String getCsCode() {
        return csCode;
    }
}
