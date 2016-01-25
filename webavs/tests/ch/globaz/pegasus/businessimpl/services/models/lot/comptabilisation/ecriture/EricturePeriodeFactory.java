package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import ch.globaz.pegasus.business.constantes.IPCDroits;

public class EricturePeriodeFactory {
    public static EricturePeriode generateEcriturePeriode() {
        EricturePeriode ericturePeriode = new EricturePeriode();
        ericturePeriode.setBeneficiaire(EcritureFactory.generateEcrituresBeneficiaire(200,
                IPCDroits.CS_ROLE_FAMILLE_REQUERANT, "1"));
        ericturePeriode.setRestitution(EcritureFactory.generateEcrituresResitution(200,
                IPCDroits.CS_ROLE_FAMILLE_REQUERANT, "1"));
        return ericturePeriode;
    }

    public static EricturePeriode generateEcriturePeriode(int montantBeneficiaire, int montantRestitution) {
        EricturePeriode ericturePeriode = new EricturePeriode();
        ericturePeriode.setBeneficiaire(EcritureFactory.generateEcrituresBeneficiaire(montantBeneficiaire,
                IPCDroits.CS_ROLE_FAMILLE_REQUERANT, "1"));
        ericturePeriode.setRestitution(EcritureFactory.generateEcrituresResitution(montantRestitution,
                IPCDroits.CS_ROLE_FAMILLE_REQUERANT, "1"));
        return ericturePeriode;
    }
}
