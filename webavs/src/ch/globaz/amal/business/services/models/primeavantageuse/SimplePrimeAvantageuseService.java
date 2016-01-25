/**
 * 
 */
package ch.globaz.amal.business.services.models.primeavantageuse;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.primeavantageuse.PrimeAvantageuseException;
import ch.globaz.amal.business.exceptions.models.primemoyenne.PrimeMoyenneException;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuse;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuseSearch;

/**
 * @author CBU
 * 
 */
public interface SimplePrimeAvantageuseService extends JadeApplicationService {
    /**
     * Creation d'une entit� en base de donn�e
     * 
     * @param simplePrimeAvantageuse
     * @return l'entit� cr�e
     * @throws PrimeMoyenneException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimplePrimeAvantageuse create(SimplePrimeAvantageuse simplePrimeAvantageuse)
            throws PrimeAvantageuseException, JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit�
     * 
     * @param simplePrimeAvantageuse
     * @return l'entit� supprim�
     * @throws JadePersistenceException
     * @throws PrimeMoyenneException
     */
    public SimplePrimeAvantageuse delete(SimplePrimeAvantageuse simplePrimeAvantageuse)
            throws JadePersistenceException, PrimeAvantageuseException;

    /**
     * Permet le chargement d'une entit�
     * 
     * @param idPrimeMoyenne
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws PrimeMoyenneException
     */
    public SimplePrimeAvantageuse read(String idPrimeMoyenne) throws JadePersistenceException,
            PrimeAvantageuseException;

    /**
     * Permet la recherche d'entit�s
     * 
     * @param primeMoyenneSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws PrimeMoyenneException
     */
    public SimplePrimeAvantageuseSearch search(SimplePrimeAvantageuseSearch primeMoyenneSearch)
            throws JadePersistenceException, PrimeAvantageuseException;

    /**
     * Update d'une entit� en base de donn�e
     * 
     * @param simplePrimeAvantageuse
     * @return l'entit� cr�e
     * @throws PrimeMoyenneException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimplePrimeAvantageuse update(SimplePrimeAvantageuse simplePrimeAvantageuse)
            throws PrimeAvantageuseException, JadePersistenceException, JadeApplicationServiceNotAvailableException;
}
