package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaitsSearch;

public interface SimpleZoneForfaitsService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleZoneForfaitsSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� SimpleZoneForfaits
     * 
     * @param SimpleZoneForfaits
     *            La simpleZoneForfaits m�tier � cr�er
     * @return simpleZoneForfaits cr��
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleZoneForfaits create(SimpleZoneForfaits simpleZoneForfaits)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError;

    /**
     * Permet la suppression d'une entit� simpleZoneForfaits
     * 
     * @param SimpleZoneForfaits
     *            La simpleZoneForfaits m�tier � supprimer
     * @return supprim�
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleZoneForfaits delete(SimpleZoneForfaits simpleZoneForfaits)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError;

    /**
     * Permet de charger en m�moire une simpleZoneForfaits PC
     * 
     * @param idsimpleZoneForfaits
     *            L'identifiant de simpleZoneForfaits � charger en m�moire
     * @return simpleZoneForfaits charg�e en m�moire
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleZoneForfaits read(String idSimpleZoneForfaits) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet de chercher des SimpleZoneForfaits selon un mod�le de crit�res.
     * 
     * @param simpleZoneForfaitsSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleZoneForfaitsSearch search(SimpleZoneForfaitsSearch simpleZoneForfaitsSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleZoneForfaits
     * 
     * @param SimpleZoneForfaits
     *            Le modele � mettre � jour
     * @return simpleZoneForfaits mis � jour
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleZoneForfaits update(SimpleZoneForfaits simpleZoneForfaits)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}