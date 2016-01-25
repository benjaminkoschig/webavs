/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.MarchandisesStockException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStock;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStockSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface MarchandisesStockService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws MarchandisesStockException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(MarchandisesStockSearch search) throws MarchandisesStockException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� marchandisesStock
     * 
     * @param marchandisesStock
     *            L'entit� marchandisesStock � cr�er
     * @return L'entit� marchandisesStock cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MarchandisesStockException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public MarchandisesStock create(MarchandisesStock marchandisesStock) throws JadePersistenceException,
            MarchandisesStockException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� marchandisesStock
     * 
     * @param marchandisesStock
     *            L'entit� marchandisesStock � supprimer
     * @return L'entit� marchandisesStock supprim�
     * @throws MarchandisesStockException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public MarchandisesStock delete(MarchandisesStock marchandisesStock) throws MarchandisesStockException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� marchandisesStock
     * 
     * @param idMarchandisesStock
     *            L'identifiant de l'entit� marchandisesStock � charger en m�moire
     * @return L'entit� marchandisesStock charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MarchandisesStockException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public MarchandisesStock read(String idMarchandisesStock) throws JadePersistenceException,
            MarchandisesStockException;

    /**
     * Chargement d'une MarchandisesStoc via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws MarchandisesStocException
     * @throws JadePersistenceException
     */
    public MarchandisesStock readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws MarchandisesStockException, JadePersistenceException;

    /**
     * Permet de chercher des marchandisesStock selon un mod�le de crit�res.
     * 
     * @param marchandisesStockSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MarchandisesStockException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public MarchandisesStockSearch search(MarchandisesStockSearch marchandisesStockSearch)
            throws JadePersistenceException, MarchandisesStockException;

    /**
     * 
     * Permet la mise � jour d'une entit� marchandisesStock
     * 
     * @param marchandisesStock
     *            L'entit� marchandisesStock � mettre � jour
     * @return L'entit� marchandisesStock mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MarchandisesStockException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public MarchandisesStock update(MarchandisesStock marchandisesStock) throws JadePersistenceException,
            MarchandisesStockException, DonneeFinanciereException;
}
