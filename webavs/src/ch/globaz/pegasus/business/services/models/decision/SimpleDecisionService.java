/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.decision.DecisionHeader;
import ch.globaz.pegasus.business.models.decision.DecisionHeaderSearch;

/**
 * @author SCE
 * 
 *         15 juil. 2010
 */
public interface SimpleDecisionService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws DecisionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(DecisionHeaderSearch search) throws DecisionException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� demande
     * 
     * @param demande
     *            La d�csions � cr�er
     * @return La d�cision cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DecisionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DecisionHeader create(DecisionHeader decision) throws JadePersistenceException, DecisionException;

    /**
     * Permet la suppression d'une entit� decision
     * 
     * @param decision
     *            a supprimer
     * @return La decision supprim�
     * @throws DecisionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public DecisionHeader delete(DecisionHeader decision) throws DecisionException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une decision
     * 
     * @param idDecision
     *            L'identifiant de la decision � charger en m�moire
     * @return La decision charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DecisionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DecisionHeader read(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * Permet de chercher des demandes selon un mod�le de crit�res.
     * 
     * @param decisionSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DecisionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DecisionHeaderSearch search(DecisionHeaderSearch decisionSearch) throws JadePersistenceException,
            DecisionException;

    /**
     * 
     * Permet la mise � jour d'une entit� decision
     * 
     * @param decision
     *            La decision � mettre � jour
     * @return La deecision mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DecisionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DossierException
     */
    public DecisionHeader update(DecisionHeader decision) throws JadePersistenceException, DecisionException;

}