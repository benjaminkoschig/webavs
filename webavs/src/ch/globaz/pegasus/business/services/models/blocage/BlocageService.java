package ch.globaz.pegasus.business.services.models.blocage;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.osiris.business.model.SoldeCompteCourant;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;
import ch.globaz.pegasus.business.vo.blocage.DeblocageDetail;

public interface BlocageService extends JadeApplicationService {

    /**
     * Permet de trouve le compte courant de déblocage lié à la pca
     * 
     * @param pcaBloque
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SoldeCompteCourant determineLeCompteCouranAUtiliser(PcaBloque pcaBloque) throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException;

    public void devaliderLiberation(String idPca) throws JadePersistenceException, JadeApplicationException;

    /**
     * Permet de trouver le motant des compte courant de type blocage en fonction d'une pcaBloqué
     * 
     * @param pcaBloque
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public List<SoldeCompteCourant> findSoldeCompteCourant(PcaBloque pcaBloque) throws JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException;

    void libererBlocage(String idPca) throws BlocageException, DecisionException, PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException;

    /**
     * Retourne un objet permettant de traiter une PCA bloqué
     * 
     * @param idPca
     * @return
     * @throws BlocageException
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PCAccordeeException
     * @throws JadeApplicationException
     */
    public DeblocageDetail readForDeblocage(String idPca) throws BlocageException, JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException, PCAccordeeException,
            JadeApplicationException;

}
