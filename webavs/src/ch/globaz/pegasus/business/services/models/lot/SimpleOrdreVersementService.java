package ch.globaz.pegasus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersementSearch;

public interface SimpleOrdreVersementService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws OrdreVersementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleOrdreVersementSearch search) throws OrdreVersementException, JadePersistenceException;

    /**
     * Permet la création d'une entité SimpleOrdreVersement
     * 
     * @param SimpleOrdreVersement
     *            La simpleOrdreVersement à créer
     * @return simpleOrdreVersement créé
     * @throws OrdreVersementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleOrdreVersement create(SimpleOrdreVersement simpleOrdreVersement) throws OrdreVersementException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleOrdreVersement
     * 
     * @param SimpleOrdreVersement
     *            La simpleOrdreVersement à supprimer
     * @return supprimé
     * @throws OrdreVersementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleOrdreVersement delete(SimpleOrdreVersement simpleOrdreVersement) throws OrdreVersementException,
            JadePersistenceException;

    /**
     * Permet la suppression de plusieurs entités simpleOrdreVersement
     * 
     * @param SimpleOrdreVersementSearch
     *            Les critères des simpleOrdreVersements à supprimer
     * @return nombre d'éléments supprimés
     * @throws OrdreVersementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int delete(SimpleOrdreVersementSearch simpleOrdreVersementSearch) throws OrdreVersementException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire une simpleOrdreVersement PC
     * 
     * @param idsimpleOrdreVersement
     *            L'identifiant de simpleOrdreVersement à charger en mémoire
     * @return simpleOrdreVersement chargée en mémoire
     * @throws OrdreVersementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleOrdreVersement read(String idSimpleOrdreVersement) throws OrdreVersementException,
            JadePersistenceException;

    /**
     * Permet de chercher des SimpleOrdreVersement selon un modèle de critères.
     * 
     * @param simpleOrdreVersementSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws OrdreVersementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleOrdreVersementSearch search(SimpleOrdreVersementSearch simpleOrdreVersementSearch)
            throws OrdreVersementException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleOrdreVersement
     * 
     * @param SimpleOrdreVersement
     *            Le modele à mettre à jour
     * @return simpleOrdreVersement mis à jour
     * @throws OrdreVersementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleOrdreVersement update(SimpleOrdreVersement simpleOrdreVersement) throws OrdreVersementException,
            JadePersistenceException;

}