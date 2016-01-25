package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreApiException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAutreApi;

public interface SimpleAutreApiService extends JadeApplicationService {

    /**
     * Permet la création d'une entité autreRente
     * 
     * @param SimpleIjApg
     *            SimpleAutreApi à créer
     * @return SimpleAutreApi créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreApiException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutreApi create(SimpleAutreApi simpleAutreApi) throws AutreApiException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité autreRente
     * 
     * @param SimpleAutreApiService
     *            SimpleAutreApi à supprimer
     * @return simpleAutreApi supprimé
     * @throws AutreApiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleAutreApi delete(SimpleAutreApi simpleAutreApi) throws AutreApiException, JadePersistenceException;

    /**
     * Permet la suppression réele de la donnée financiere
     * 
     * @param listeIDString
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une autreRente PC
     * 
     * @param idSimpleAutreApi
     *            L'identifiant de la simpleAutreApi à charger en mémoire
     * @return simpleAutreApi chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreApiException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutreApi read(String idSimpleAutreApi) throws AutreApiException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité variableMetier
     * 
     * @param SimpleAutreApi
     *            SimpleAutreApi à mettre à jour
     * @return simpleAutreRente mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreApiException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutreApi update(SimpleAutreApi simpleAutreApi) throws AutreApiException, JadePersistenceException;

}
