/**
 * 
 */
package ch.globaz.amal.business.services.models.revenu;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistorique;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistoriqueSearch;

/**
 * @author dhi
 * 
 */
public interface SimpleRevenuHistoriqueService extends JadeApplicationService {
    /**
     * Permet la cr�ation d'un revenu historique
     * 
     * @param simpleRevenuHistorique
     *            le revenu � cr�er
     * @return le revenu cr�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuHistorique create(SimpleRevenuHistorique simpleRevenuHistorique)
            throws JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'un revenu historique
     * 
     * @param simpleRevenuHistorique
     * @return le revenu supprim�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuHistorique delete(SimpleRevenuHistorique simpleRevenuHistorique) throws RevenuException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire un revenu historique
     * 
     * @param idRevenu
     * @return le revenu historique
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuHistorique read(String idRevenu) throws RevenuException, JadePersistenceException;

    /**
     * Permet la recherche d'un revenu simple historique
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le renseign�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuHistoriqueSearch search(SimpleRevenuHistoriqueSearch search) throws JadePersistenceException,
            RevenuException;

    /**
     * Permet la mise � jour d'un revenu historique
     * 
     * @param simpleRevenuHistorique
     * @return le revenu mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuHistorique update(SimpleRevenuHistorique simpleRevenuHistorique) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

}
