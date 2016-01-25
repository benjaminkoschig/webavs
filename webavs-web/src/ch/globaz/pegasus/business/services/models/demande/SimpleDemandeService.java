/**
 * 
 */
package ch.globaz.pegasus.business.services.models.demande;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.demande.SimpleDemandeSearch;

/**
 * @author ECO
 * 
 */
public interface SimpleDemandeService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws DemandeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleDemandeSearch search) throws DemandeException, JadePersistenceException;

    /**
     * Permet la création d'une entité demande
     * 
     * @param demande
     *            La demande PC à créer
     * @return La demande PC créé
     * @throws DemandeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DossierException
     */
    public SimpleDemande create(SimpleDemande demande) throws DemandeException, JadePersistenceException,
            DossierException;

    /**
     * Permet la suppression d'une entité demande PC
     * 
     * @param demande
     *            La demande PC à supprimer
     * @return La demande PC supprimé
     * @throws DemandeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDemande delete(SimpleDemande demande) throws DemandeException, JadePersistenceException;

    /**
     * Recherche si une demande initial (imédiatement contigue existe pour ce dossier)
     * 
     * @param simpleDemande
     * @return
     * @throws DemandeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public boolean isDemandeInitial(SimpleDemande simpleDemande, String dateDemandeToCheck) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une demande
     * 
     * @param idDemande
     *            L'identifiant de la demande à charger en mémoire
     * @return La demande chargé en mémoire
     * @throws DemandeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDemande read(String idDemande) throws DemandeException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité demande
     * 
     * @param demande
     *            La demande PC à mettre à jour
     * @return La demande PC mis à jour
     * @throws DemandeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DossierException
     */
    public SimpleDemande update(SimpleDemande demande) throws DemandeException, JadePersistenceException,
            DossierException;

}
