package ch.globaz.pegasus.business.services.models.creancier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.Creancier;
import ch.globaz.pegasus.business.models.creancier.CreancierSearch;

public interface CreancierService extends JadeApplicationService {
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
    public int count(CreancierSearch search) throws CreancierException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� Creancier
     * 
     * @param Creance
     *            Le creancier � cr�er
     * @return creancier cr��
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public Creancier create(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

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
    public boolean hasCreanceAccordee(String idCreancier) throws CreancierException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une creancier PC
     * 
     * @param idcreancier
     *            L'identifiant de creancier � charger en m�moire
     * @return creancier charg�e en m�moire
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
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
    public CreancierSearch search(CreancierSearch creancierSearch) throws CreancierException, JadePersistenceException;

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
    public Creancier update(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}