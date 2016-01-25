package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreApiException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAutreApi;

public interface SimpleAutreApiService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� autreRente
     * 
     * @param SimpleIjApg
     *            SimpleAutreApi � cr�er
     * @return SimpleAutreApi cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreApiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutreApi create(SimpleAutreApi simpleAutreApi) throws AutreApiException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� autreRente
     * 
     * @param SimpleAutreApiService
     *            SimpleAutreApi � supprimer
     * @return simpleAutreApi supprim�
     * @throws AutreApiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleAutreApi delete(SimpleAutreApi simpleAutreApi) throws AutreApiException, JadePersistenceException;

    /**
     * Permet la suppression r�ele de la donn�e financiere
     * 
     * @param listeIDString
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire une autreRente PC
     * 
     * @param idSimpleAutreApi
     *            L'identifiant de la simpleAutreApi � charger en m�moire
     * @return simpleAutreApi charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreApiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutreApi read(String idSimpleAutreApi) throws AutreApiException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� variableMetier
     * 
     * @param SimpleAutreApi
     *            SimpleAutreApi � mettre � jour
     * @return simpleAutreRente mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreApiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutreApi update(SimpleAutreApi simpleAutreApi) throws AutreApiException, JadePersistenceException;

}
