package ch.globaz.pegasus.businessimpl.services.determineSousCodePrestation;

import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;

public class DefaultSousCodePrestation extends DetermineSousCodePrestation {

    @Override
    public String determineSousCode(PeriodePCAccordee periode, boolean isConjoint) throws CalculException {
        String sousTypeGenrePrestation = null;

        if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(periode.getTypePc())) {
            sousTypeGenrePrestation = REGenresPrestations.PC_SOUS_TYPE_AI_DOMICILE;
        } else if (IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(periode.getTypePc())
                || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(periode.getTypePc())) {
            sousTypeGenrePrestation = REGenresPrestations.PC_SOUS_TYPE_AVS_DOMICILE;
        } else {
            throw new CalculException("The type of this pcs is not deal for the 'sous type genre presation'"
                    + periode.getTypePc());
        }
        return sousTypeGenrePrestation;
    }
}
