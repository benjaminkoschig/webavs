package ch.globaz.pegasus.business.services.models.creancier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordeeSearch;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordeeSearch;

public interface CreanceAccordeeService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(CreanceAccordeeSearch search) throws CreancierException, JadePersistenceException;

    /**
     * Permet la création d'une entité CreanceAccordee
     * 
     * @param CreanceAccordee
     *            La creanceAccordee à créer
     * @return creanceAccordee créé
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public CreanceAccordee create(CreanceAccordee creanceAccordee) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entité creanceAccordee
     * 
     * @param CreanceAccordee
     *            La creanceAccordee à supprimer
     * @return supprimé
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public CreanceAccordee delete(CreanceAccordee creanceAccordee) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    public int deleteWithSearchModele(SimpleCreanceAccordeeSearch simpleCreanceAccordeeSearch)
            throws CreancierException, JadePersistenceException;

    /**
     * Retourne le montant rembourse, pour un creancier, pour une demande Effectue l'addition des crenaces versé pour la
     * demande(toute les versions de droits)
     * 
     * @param idDemande
     *            , l'id de la demande
     * @param idCreancier
     *            , l'id du creancier
     * @return montantRebourse
     * @throws JadePersistenceException
     */
    public BigDecimal findTotalCreanceVerseByDemandeForCreancier(String idDemande, String idCreancier)
            throws JadePersistenceException, CreancierException;

    /**
     * Retourne le montant rembourse, pour un creancier, pour une demande Effectue l'addition des crenaces versé pour la
     * demande(toute les versions de droits)
     * 
     * @param idDemande
     *            , l'id de la demande
     * @param idCreancier
     *            , l'id du creancier
     * @return montantRebourse
     * @throws JadePersistenceException
     */
    public BigDecimal findTotalCreanceVerseByVersionDroitForCreancier(String idDemande, String idVersioNDroit,
            String idCreancier) throws JadePersistenceException, CreancierException;

    /**
     * Permet de charger en mémoire une creanceAccordee PC
     * 
     * @param idcreanceAccordee
     *            L'identifiant de creanceAccordee à charger en mémoire
     * @return creanceAccordee chargée en mémoire
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CreanceAccordee read(String idCreanceAccordee) throws CreancierException, JadePersistenceException;

    /**
     * Permet de chercher des CreanceAccordee selon un modèle de critères.
     * 
     * @param creanceAccordeeSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CreanceAccordeeSearch search(CreanceAccordeeSearch creanceAccordeeSearch) throws CreancierException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité CreanceAccordee
     * 
     * @param CreanceAccordee
     *            Le modele à mettre à jour
     * @return creanceAccordee mis à jour
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public CreanceAccordee update(CreanceAccordee creanceAccordee) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;
}
