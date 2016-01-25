/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.business.models.decision.DecisionRefusSearch;

/**
 * @author SCE
 * 
 *         21 juil. 2010
 */
public interface DecisionRefusService extends JadeApplicationService {
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
    public int count(DecisionRefusSearch search) throws DecisionException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� decisionRefus
     * 
     * @param demande
     *            La d�csionsRefus � cr�er
     * @return La d�cision cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DecisionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DossierException
     * @throws DemandeException
     */
    public DecisionRefus create(DecisionRefus decision) throws JadePersistenceException, DecisionException,
            DemandeException, DossierException;

    /**
     * Permet la suppression d'une entit� decisionRefus
     * 
     * @param decisionRefus
     *            a supprimer
     * @return La decisionRefus supprim�
     * @throws DecisionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public DecisionRefus delete(DecisionRefus decision) throws DecisionException, JadePersistenceException;

    /**
     * Retourne l'id de la decision li� avec l'id de la demande pass� en param�tres.
     * 
     * @param idDemande
     * @return idDecision, identifiant de la decision
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public String getIdDecisionByIdDemande(String idDemande) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire une decisionRefus
     * 
     * @param idDecision
     *            L'identifiant de la decisionRefus � charger en m�moire
     * @return La decisionRefus charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DecisionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DecisionRefus read(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * Permet de chercher des demandes selon un mod�le de crit�res.
     * 
     * @param decisionRefusSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DecisionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DecisionRefusSearch search(DecisionRefusSearch decisionRefusSearch) throws JadePersistenceException,
            DecisionException;

    /**
     * 
     * Permet la mise � jour d'une entit� decision
     * 
     * @param decision
     *            La decisionRefus � mettre � jour
     * @return La decisionRefus mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DecisionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DossierException
     */
    public DecisionRefus update(DecisionRefus decision) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Mise � jour de l'entit� pour la pr�validation
     * 
     * @param decision
     * @return L'entit� mise � jour
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public DecisionRefus updateForPrevalidation(DecisionRefus decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Peemet la mise � jour d'une entit� pour la validation
     * 
     * @param decision
     * @return
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public DecisionRefus updateForValidation(DecisionRefus decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DemandeException, DossierException;
}
