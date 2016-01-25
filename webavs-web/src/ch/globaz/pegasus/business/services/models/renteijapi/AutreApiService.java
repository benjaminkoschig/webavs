package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreApiException;
import ch.globaz.pegasus.business.models.renteijapi.AutreApi;
import ch.globaz.pegasus.business.models.renteijapi.AutreApiSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface AutreApiService extends JadeApplicationService, AbstractDonneeFinanciereService {

    public int count(AutreApiSearch search) throws AutreApiException, JadePersistenceException;

    /**
     * Permet la création d'une entité autreRente
     * 
     * @param AutreApi
     *            AutreApi à créer
     * @return AutreApi créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreApiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public AutreApi create(AutreApi autreApi) throws AutreApiException, JadePersistenceException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité autreRente
     * 
     * @param AutreApi
     *            AutreApi à supprimer
     * @return autreApi supprimé
     * @throws AutreApiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DonneeFinanciereException
     */
    public AutreApi delete(AutreApi autreApi) throws AutreApiException, JadePersistenceException,
            DonneeFinanciereException;

    /**
     * Permet de charger en mémoire une autreRente PC
     * 
     * @param idAutreApi
     *            L'identifiant de la autreApi à charger en mémoire
     * @return autreApi chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreApiException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutreApi read(String idAutreApi) throws AutreApiException, JadePersistenceException;

    /**
     * Chargement d'une AutreApi via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutreApiException
     * @throws JadePersistenceException
     */
    public AutreApi readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws AutreApiException,
            JadePersistenceException;

    public AutreApiSearch search(AutreApiSearch autreApiSearch) throws JadePersistenceException, AutreApiException;

    /**
     * 
     * Permet la mise à jour d'une entité variableMetier
     * 
     * @param AutreApi
     *            AutreApi à mettre à jour
     * @return simpleAutreRente mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreApiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public AutreApi update(AutreApi autreApi) throws AutreApiException, JadePersistenceException,
            DonneeFinanciereException;

}
