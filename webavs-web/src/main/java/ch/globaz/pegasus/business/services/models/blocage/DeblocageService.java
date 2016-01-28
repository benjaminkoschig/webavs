package ch.globaz.pegasus.business.services.models.blocage;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.vo.blocage.Deblocage;

public interface DeblocageService extends JadeApplicationService {

    /**
     * Permet de rechercher les lignes de déblocage saisie. Fait la somme du totale de ligne et fait aussi la somme de
     * ligne enregistré Les ligne de déblocages sont regroupé par type de déblocage. Recherche aussi la pcaBloqué
     * 
     * @param idPca
     * @throws BlocageException
     * @throws DecisionException
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public Deblocage retriveDeblocage(String idPca) throws BlocageException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PCAccordeeException;
}
