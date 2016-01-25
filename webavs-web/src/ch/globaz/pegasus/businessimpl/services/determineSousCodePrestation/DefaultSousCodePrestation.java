package ch.globaz.pegasus.businessimpl.services.determineSousCodePrestation;

import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;

public class DefaultSousCodePrestation extends DetermineSousCodePreation {

    // @Override
    // public String determineCsNormal(SimpleOrdreVersement ov) throws ComptabiliserLotException {
    // String rubrique;
    // if (IPCOrdresVersements.CS_DOMAINE_AI.equals(ov.getCsTypePcAccordee())) {
    // rubrique = APIReferenceRubrique.PC_AI;
    // } else if (IPCOrdresVersements.CS_DOMAINE_AVS.equals(ov.getCsTypePcAccordee())) {
    // rubrique = APIReferenceRubrique.PC_AVS;
    // } else {
    // throw new ComptabiliserLotException("Rubrique: not find with this type PC "
    // + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(ov.getCsTypePcAccordee()) + "(id:"
    // + ov.getCsTypePcAccordee() + ")");
    // }
    //
    // return rubrique;
    // }
    //
    // @Override
    // public String determineCsRestitution(SimpleOrdreVersement ov) throws ComptabiliserLotException {
    // String csDomaine = ov.getCsTypePcAccordee();
    // String csRubriqueRestitution;
    // if (IPCOrdresVersements.CS_DOMAINE_AI.equals(csDomaine)) {
    // csRubriqueRestitution = APIReferenceRubrique.PC_AI_A_RESTITUER;
    // } else if (IPCOrdresVersements.CS_DOMAINE_AVS.equals(csDomaine)) {
    // csRubriqueRestitution = APIReferenceRubrique.PC_AVS_A_RESTITUER;
    // } else {
    // throw new ComptabiliserLotException("Unable to find the csRubriqueRestitution with this value(" + csDomaine
    // + ")");
    // }
    // return csRubriqueRestitution;
    // }

    @Override
    public String detetrmineSousCode(PeriodePCAccordee periode, boolean isConjoint) throws CalculException {
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
