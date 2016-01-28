package ch.globaz.perseus.business.services.models.creancier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.models.creancier.Creancier;
import ch.globaz.perseus.business.models.creancier.CreancierSearchModel;

/**
 * 
 * @author MBO
 * 
 */

public interface CreancierService extends JadeCrudService<Creancier, CreancierSearchModel> {

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
    @Override
    public int count(CreancierSearchModel search) throws CreancierException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une creancier PF
     * 
     * @param idcreancier
     *            L'identifiant de creancier � charger en m�moire
     * @return creancier charg�e en m�moire
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */

    @Override
    public Creancier create(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PerseusException;

    /**
     * Permet la suppression d'une entit� creancier
     * 
     * @param Creance
     *            Le creancier � supprimer
     * @return supprim�
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    @Override
    public Creancier delete(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * 
     * Test si le creancier est r�f�renc� dans la table des cr�ances accord�es
     * 
     * @param idCreancier
     * @return boolean
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */

    @Override
    public Creancier read(String idCreancier) throws CreancierException, JadePersistenceException;

    /**
     * Permet de chercher des Creancier selon un mod�le de crit�res.
     * 
     * @param creancierSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    @Override
    public CreancierSearchModel search(CreancierSearchModel creancierSearch) throws CreancierException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� Creancier
     * 
     * @param Creance
     *            Le modele � mettre � jour
     * @return creancier mis � jour
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    @Override
    public Creancier update(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PerseusException;
}
