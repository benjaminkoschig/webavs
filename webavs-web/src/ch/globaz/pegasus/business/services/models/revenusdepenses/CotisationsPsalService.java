package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsal;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsalSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface CotisationsPsalService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(CotisationsPsalSearch search) throws CotisationsPsalException, JadePersistenceException;

    /**
     * Permet la création d'une entité CotisationsPsal
     * 
     * @param CotisationsPsal
     *            L'entité CotisationsPsal à créer
     * @return L'entité CotisationsPsal créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CotisationsPsal create(CotisationsPsal cotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité CotisationsPsal
     * 
     * @param CotisationsPsal
     *            L'entité CotisationsPsal à supprimer
     * @return L'entité CotisationsPsal supprimé
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CotisationsPsal delete(CotisationsPsal cotisationsPsal) throws CotisationsPsalException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité CotisationsPsal
     * 
     * @param idCotisationsPsal
     *            L'identifiant de l'entité CotisationsPsal à charger en mémoire
     * @return L'entité CotisationsPsal chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CotisationsPsal read(String idCotisationsPsal) throws JadePersistenceException, CotisationsPsalException;

    /**
     * Chargement d'un CotisationsPsal via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws CotisationsPsalException
     * @throws JadePersistenceException
     */
    public CotisationsPsal readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws CotisationsPsalException, JadePersistenceException;

    /**
     * Permet de chercher des CotisationsPsal selon un modèle de critères.
     * 
     * @param CotisationsPsalSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CotisationsPsalSearch search(CotisationsPsalSearch cotisationsPsalSearch) throws JadePersistenceException,
            CotisationsPsalException;

    /**
     * 
     * Permet la mise à jour d'une entité CotisationsPsal
     * 
     * @param CotisationsPsal
     *            L'entité CotisationsPsal à mettre à jour
     * @return L'entité CotisationsPsal mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public CotisationsPsal update(CotisationsPsal cotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException, DonneeFinanciereException;
}