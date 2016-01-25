package ch.globaz.pegasus.business.services.models.creancier;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.SimpleCreancier;
import ch.globaz.pegasus.business.models.creancier.SimpleCreancierSearch;

public interface SimpleCreancierService extends JadeApplicationService {
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
    public int count(SimpleCreancierSearch search) throws CreancierException, JadePersistenceException;

    /**
     * Permet la création d'une entité SimpleCreancier
     * 
     * @param SimpleCreancier
     *            La simpleCreancier métier à créer
     * @return simpleCreancier créé
     * @throws creancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleCreancier create(SimpleCreancier simpleCreancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError;

    /**
     * Permet la suppression d'une entité simpleCreancier
     * 
     * @param SimpleCreancier
     *            La simpleCreancier métier à supprimer
     * @return supprimé
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCreancier delete(SimpleCreancier simpleCreancier) throws CreancierException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une simpleCreancier PC
     * 
     * @param idsimpleCreancier
     *            L'identifiant de simpleCreancier à charger en mémoire
     * @return simpleCreancier chargée en mémoire
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCreancier read(String idSimpleCreancier) throws CreancierException, JadePersistenceException;

    /**
     * Permet de chercher des SimpleCreancier selon un modèle de critères.
     * 
     * @param simpleCreancierSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCreancierSearch search(SimpleCreancierSearch simpleCreancierSearch) throws CreancierException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleCreancier
     * 
     * @param SimpleCreancier
     *            Le modele à mettre à jour
     * @return simpleCreancier mis à jour
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleCreancier update(SimpleCreancier simpleCreancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}