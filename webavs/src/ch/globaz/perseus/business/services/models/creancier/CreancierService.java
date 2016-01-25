package ch.globaz.perseus.business.services.models.creancier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.models.creancier.Creancier;
import ch.globaz.perseus.business.models.creancier.CreancierSearchModel;

/**
 * 
 * @author MBO
 * 
 */

public interface CreancierService extends JadeCrudService<Creancier, CreancierSearchModel> {

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
    @Override
    public int count(CreancierSearchModel search) throws CreancierException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une creancier PF
     * 
     * @param idcreancier
     *            L'identifiant de creancier à charger en mémoire
     * @return creancier chargée en mémoire
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */

    @Override
    public Creancier create(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PerseusException;

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
    @Override
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

    @Override
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
    @Override
    public CreancierSearchModel search(CreancierSearchModel creancierSearch) throws CreancierException,
            JadePersistenceException;

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
    @Override
    public Creancier update(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PerseusException;
}
