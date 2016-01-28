/**
 * 
 */
package ch.globaz.amal.business.services.models.primemoyenne;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.primemoyenne.PrimeMoyenneException;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenne;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenneSearch;

/**
 * @author CBU
 * 
 */
public interface SimplePrimeMoyenneService extends JadeApplicationService {
    /**
     * Creation d'une entit� en base de donn�e
     * 
     * @param simplePrimeMoyenne
     * @return l'entit� cr�e
     * @throws PrimeMoyenneException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimplePrimeMoyenne create(SimplePrimeMoyenne simplePrimeMoyenne) throws PrimeMoyenneException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit�
     * 
     * @param simplePrimeMoyenne
     * @return l'entit� supprim�
     * @throws JadePersistenceException
     * @throws PrimeMoyenneException
     */
    public SimplePrimeMoyenne delete(SimplePrimeMoyenne simplePrimeMoyenne) throws JadePersistenceException,
            PrimeMoyenneException;

    /**
     * Permet le chargement d'une entit�
     * 
     * @param idPrimeMoyenne
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws PrimeMoyenneException
     */
    public SimplePrimeMoyenne read(String idPrimeMoyenne) throws JadePersistenceException, PrimeMoyenneException;

    /**
     * Permet la recherche d'entit�s
     * 
     * @param primeMoyenneSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws PrimeMoyenneException
     */
    public SimplePrimeMoyenneSearch search(SimplePrimeMoyenneSearch primeMoyenneSearch)
            throws JadePersistenceException, PrimeMoyenneException;

    /**
     * Update d'une entit� en base de donn�e
     * 
     * @param simplePrimeMoyenne
     * @return l'entit� cr�e
     * @throws PrimeMoyenneException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimplePrimeMoyenne update(SimplePrimeMoyenne simplePrimeMoyenne) throws PrimeMoyenneException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;
}
