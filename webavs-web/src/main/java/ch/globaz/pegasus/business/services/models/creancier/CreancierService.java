package ch.globaz.pegasus.business.services.models.creancier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.Creancier;
import ch.globaz.pegasus.business.models.creancier.CreancierSearch;

public interface CreancierService extends JadeApplicationService {
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
    public int count(CreancierSearch search) throws CreancierException, JadePersistenceException;

    /**
     * Permet la création d'une entité Creancier
     * 
     * @param Creance
     *            Le creancier à créer
     * @return creancier créé
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public Creancier create(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entité creancier
     * 
     * @param Creance
     *            Le creancier à supprimer
     * @return supprimé
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public Creancier delete(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * 
     * Test si le creancier est référencé dans la table des créances accordées
     * 
     * @param idCreancier
     * @return boolean
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public boolean hasCreanceAccordee(String idCreancier) throws CreancierException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une creancier PC
     * 
     * @param idcreancier
     *            L'identifiant de creancier à charger en mémoire
     * @return creancier chargée en mémoire
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Creancier read(String idCreancier) throws CreancierException, JadePersistenceException;

    /**
     * Permet de chercher des Creancier selon un modèle de critères.
     * 
     * @param creancierSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CreancierSearch search(CreancierSearch creancierSearch) throws CreancierException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité Creancier
     * 
     * @param Creance
     *            Le modele à mettre à jour
     * @return creancier mis à jour
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public Creancier update(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}