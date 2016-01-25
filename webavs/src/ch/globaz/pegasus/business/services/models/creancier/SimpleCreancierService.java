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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleCreancierSearch search) throws CreancierException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� SimpleCreancier
     * 
     * @param SimpleCreancier
     *            La simpleCreancier m�tier � cr�er
     * @return simpleCreancier cr��
     * @throws creancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleCreancier create(SimpleCreancier simpleCreancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError;

    /**
     * Permet la suppression d'une entit� simpleCreancier
     * 
     * @param SimpleCreancier
     *            La simpleCreancier m�tier � supprimer
     * @return supprim�
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCreancier delete(SimpleCreancier simpleCreancier) throws CreancierException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une simpleCreancier PC
     * 
     * @param idsimpleCreancier
     *            L'identifiant de simpleCreancier � charger en m�moire
     * @return simpleCreancier charg�e en m�moire
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCreancier read(String idSimpleCreancier) throws CreancierException, JadePersistenceException;

    /**
     * Permet de chercher des SimpleCreancier selon un mod�le de crit�res.
     * 
     * @param simpleCreancierSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCreancierSearch search(SimpleCreancierSearch simpleCreancierSearch) throws CreancierException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleCreancier
     * 
     * @param SimpleCreancier
     *            Le modele � mettre � jour
     * @return simpleCreancier mis � jour
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleCreancier update(SimpleCreancier simpleCreancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}