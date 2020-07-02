package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation;

import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCBienImmoPrincipal;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

public class UtilStrategieBienImmobillier {

    public static Boolean isHomeEtDroitHabitation(Float sommeHomes, TupleDonneeRapport tupleHabitat) {

        if (tupleHabitat != null) {
            return (sommeHomes > 0) && tupleHabitat.getLegende().equals(IPCBienImmoPrincipal.CS_TYPE_DROIT_HABITATION);
        }
        return false;
    }
}
