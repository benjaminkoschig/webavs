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
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws DecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(DecisionRefusSearch search) throws DecisionException, JadePersistenceException;

    /**
     * Permet la création d'une entité decisionRefus
     * 
     * @param demande
     *            La décsionsRefus à créer
     * @return La décision créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DossierException
     * @throws DemandeException
     */
    public DecisionRefus create(DecisionRefus decision) throws JadePersistenceException, DecisionException,
            DemandeException, DossierException;

    /**
     * Permet la suppression d'une entité decisionRefus
     * 
     * @param decisionRefus
     *            a supprimer
     * @return La decisionRefus supprimé
     * @throws DecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public DecisionRefus delete(DecisionRefus decision) throws DecisionException, JadePersistenceException;

    /**
     * Retourne l'id de la decision lié avec l'id de la demande passé en paramètres.
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
     * Permet de charger en mémoire une decisionRefus
     * 
     * @param idDecision
     *            L'identifiant de la decisionRefus à charger en mémoire
     * @return La decisionRefus chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DecisionRefus read(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * Permet de chercher des demandes selon un modèle de critères.
     * 
     * @param decisionRefusSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DecisionRefusSearch search(DecisionRefusSearch decisionRefusSearch) throws JadePersistenceException,
            DecisionException;

    /**
     * 
     * Permet la mise à jour d'une entité decision
     * 
     * @param decision
     *            La decisionRefus à mettre à jour
     * @return La decisionRefus mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DossierException
     */
    public DecisionRefus update(DecisionRefus decision) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Mise à jour de l'entité pour la prévalidation
     * 
     * @param decision
     * @return L'entité mise à jour
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public DecisionRefus updateForPrevalidation(DecisionRefus decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Peemet la mise à jour d'une entité pour la validation
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
