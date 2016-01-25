package ch.globaz.pegasus.business.services.models.creancier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordeeSearch;

public interface SimpleCreanceAccordeeService extends JadeApplicationService {
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
    public int count(SimpleCreanceAccordeeSearch search) throws CreancierException, JadePersistenceException;

    /**
     * Permet la création d'une entité SimpleCreanceAccordee
     * 
     * @param SimpleCreanceAccordee
     *            La simpleCreanceAccordee à créer
     * @return simpleCreanceAccordee créé
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCreanceAccordee create(SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleCreanceAccordee
     * 
     * @param SimpleCreanceAccordee
     *            La simpleCreanceAccordee à supprimer
     * @return supprimé
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCreanceAccordee delete(SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException,
            JadePersistenceException;

    /**
     * 
     * Permet de supprimer des creances par une version de droit
     * 
     * @param SimpleCreanceAccordee
     *            Le modele à mettre à jour
     * @return simpleCreanceAccordee mis à jour
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public void deleteByIdVersionDroit(String idVersionDroit) throws CreancierException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une simpleCreanceAccordee PC
     * 
     * @param idsimpleCreanceAccordee
     *            L'identifiant de simpleCreanceAccordee à charger en mémoire
     * @return simpleCreanceAccordee chargée en mémoire
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCreanceAccordee read(String idSimpleCreanceAccordee) throws CreancierException,
            JadePersistenceException;

    /**
     * Permet de chercher des SimpleCreanceAccordee selon un modèle de critères.
     * 
     * @param simpleCreanceAccordeeSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCreanceAccordeeSearch search(SimpleCreanceAccordeeSearch simpleCreanceAccordeeSearch)
            throws CreancierException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleCreanceAccordee
     * 
     * @param SimpleCreanceAccordee
     *            Le modele à mettre à jour
     * @return simpleCreanceAccordee mis à jour
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCreanceAccordee update(SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException,
            JadePersistenceException;
}