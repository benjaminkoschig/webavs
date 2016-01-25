package ch.globaz.pegasus.business.constantes;

import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDeblocage;

public enum EPCTypeDeblocage {
    CS_CREANCIER(IPCDeblocage.CS_TYPE_DEBLOCAGE_CREANCIER),
    CS_DETTE_EN_COMPTA(IPCDeblocage.CS_TYPE_DEBLOCAGE_DETTE_EN_COMPTA),
    CS_VERSEMENT_BENEFICIAIRE(IPCDeblocage.CS_TYPE_DEBLOCAGE_VERSEMENT_BENEFICIAIRE);

    public static EPCTypeDeblocage getEnumByCsCode(String csCode) {
        if (CS_CREANCIER.csCode.equals(csCode)) {
            return CS_CREANCIER;
        } else if (CS_DETTE_EN_COMPTA.csCode.equals(csCode)) {
            return CS_DETTE_EN_COMPTA;
        } else if (CS_VERSEMENT_BENEFICIAIRE.csCode.equals(csCode)) {
            return CS_VERSEMENT_BENEFICIAIRE;
        }
        throw new IllegalArgumentException("No Enum specified for this string " + csCode);
    }

    private String csCode;

    EPCTypeDeblocage(String csCode) {
        this.csCode = csCode;
    }

    public String getCsCode() {
        return csCode;
    }
}
