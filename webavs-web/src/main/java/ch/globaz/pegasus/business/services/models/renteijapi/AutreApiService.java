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
     * Permet la cr�ation d'une entit� autreRente
     * 
     * @param AutreApi
     *            AutreApi � cr�er
     * @return AutreApi cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreApiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public AutreApi create(AutreApi autreApi) throws AutreApiException, JadePersistenceException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� autreRente
     * 
     * @param AutreApi
     *            AutreApi � supprimer
     * @return autreApi supprim�
     * @throws AutreApiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DonneeFinanciereException
     */
    public AutreApi delete(AutreApi autreApi) throws AutreApiException, JadePersistenceException,
            DonneeFinanciereException;

    /**
     * Permet de charger en m�moire une autreRente PC
     * 
     * @param idAutreApi
     *            L'identifiant de la autreApi � charger en m�moire
     * @return autreApi charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreApiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
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
     * Permet la mise � jour d'une entit� variableMetier
     * 
     * @param AutreApi
     *            AutreApi � mettre � jour
     * @return simpleAutreRente mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreApiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public AutreApi update(AutreApi autreApi) throws AutreApiException, JadePersistenceException,
            DonneeFinanciereException;

}
