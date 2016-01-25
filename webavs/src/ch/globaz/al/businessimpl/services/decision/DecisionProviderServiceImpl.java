package ch.globaz.al.businessimpl.services.decision;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.decision.DecisionAgricoleInterCantonalService;
import ch.globaz.al.business.services.decision.DecisionAgricoleService;
import ch.globaz.al.business.services.decision.DecisionInterCantonalService;
import ch.globaz.al.business.services.decision.DecisionProviderService;
import ch.globaz.al.business.services.decision.DecisionSalarieService;

/**
 * Sur le plan métier, défini quel service va remplir les données en fonction du type de décisions
 * 
 * @author JER
 */
public class DecisionProviderServiceImpl implements DecisionProviderService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.decision.DecisionProviderService# getDecisionService
     * (ch.globaz.al.business.models.dossier.DossierComplexModel)
     */
    @Override
    public Class<?> getDecisionService(DossierComplexModel dossierComplex) throws JadeApplicationException {
        if (ALServiceLocator.getDossierBusinessService().isAgricole(
                dossierComplex.getDossierModel().getActiviteAllocataire())
                && ALCSDossier.STATUT_CS.equals(dossierComplex.getDossierModel().getStatut())) {
            return DecisionAgricoleInterCantonalService.class;
        } else if (ALCSDossier.STATUT_CS.equals(dossierComplex.getDossierModel().getStatut())) {
            return DecisionInterCantonalService.class;
        } else if (ALServiceLocator.getDossierBusinessService().isAgricole(
                dossierComplex.getDossierModel().getActiviteAllocataire())) {
            return DecisionAgricoleService.class;
        } else {
            return DecisionSalarieService.class;
        }
    }

}
