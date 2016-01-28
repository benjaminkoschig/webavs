package ch.globaz.vulpecula.facturation;

import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.prestations.Etat;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.repositories.servicemilitaire.ServiceMilitaireRepository;

/***
 * Processus de comptabilisation des services militaires
 * 
 */
public class PTProcessFacturationServiceMilitaireComptabiliser extends PTProcessFacturation {
    private static final long serialVersionUID = 3386277607204232761L;

    private final ServiceMilitaireRepository serviceMilitaireRepository;

    public PTProcessFacturationServiceMilitaireComptabiliser() {
        serviceMilitaireRepository = VulpeculaRepositoryLocator.getServiceMilitaireRepository();
    }

    @Override
    protected boolean launch() {
        List<ServiceMilitaire> serviceMilitaires = serviceMilitaireRepository.findForFacturation(getIdPassage());
        for (ServiceMilitaire serviceMilitaire : serviceMilitaires) {
            serviceMilitaire.setEtat(Etat.COMPTABILISEE);
            serviceMilitaireRepository.update(serviceMilitaire);
        }
        return true;
    }

    @Override
    protected void clean() {
    }
}
