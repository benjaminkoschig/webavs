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
     * Creation d'une entité en base de donnée
     * 
     * @param simplePrimeAvantageuse
     * @return l'entité crée
     * @throws PrimeMoyenneException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimplePrimeAvantageuse create(SimplePrimeAvantageuse simplePrimeAvantageuse)
            throws PrimeAvantageuseException, JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entité
     * 
     * @param simplePrimeAvantageuse
     * @return l'entité supprimé
     * @throws JadePersistenceException
     * @throws PrimeMoyenneException
     */
    public SimplePrimeAvantageuse delete(SimplePrimeAvantageuse simplePrimeAvantageuse)
            throws JadePersistenceException, PrimeAvantageuseException;

    /**
     * Permet le chargement d'une entité
     * 
     * @param idPrimeMoyenne
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws PrimeMoyenneException
     */
    public SimplePrimeAvantageuse read(String idPrimeMoyenne) throws JadePersistenceException,
            PrimeAvantageuseException;

    /**
     * Permet la recherche d'entités
     * 
     * @param primeMoyenneSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws PrimeMoyenneException
     */
    public SimplePrimeAvantageuseSearch search(SimplePrimeAvantageuseSearch primeMoyenneSearch)
            throws JadePersistenceException, PrimeAvantageuseException;

    /**
     * Update d'une entité en base de donnée
     * 
     * @param simplePrimeAvantageuse
     * @return l'entité crée
     * @throws PrimeMoyenneException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimplePrimeAvantageuse update(SimplePrimeAvantageuse simplePrimeAvantageuse)
            throws PrimeAvantageuseException, JadePersistenceException, JadeApplicationServiceNotAvailableException;
}
