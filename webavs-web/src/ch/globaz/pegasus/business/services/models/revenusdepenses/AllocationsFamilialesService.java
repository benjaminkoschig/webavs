package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamiliales;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface AllocationsFamilialesService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws AllocationsFamilialesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(AllocationsFamilialesSearch search) throws AllocationsFamilialesException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� AllocationsFamiliales
     * 
     * @param AllocationsFamiliales
     *            L'entit� AllocationsFamiliales � cr�er
     * @return L'entit� AllocationsFamiliales cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AllocationsFamiliales create(AllocationsFamiliales allocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� AllocationsFamiliales
     * 
     * @param AllocationsFamiliales
     *            L'entit� AllocationsFamiliales � supprimer
     * @return L'entit� AllocationsFamiliales supprim�
     * @throws AllocationsFamilialesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public AllocationsFamiliales delete(AllocationsFamiliales allocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� AllocationsFamiliales
     * 
     * @param idAllocationsFamiliales
     *            L'identifiant de l'entit� AllocationsFamiliales � charger en m�moire
     * @return L'entit� AllocationsFamiliales charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AllocationsFamiliales read(String idAllocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException;

    /**
     * Chargement d'une AllocationsFamiliales via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AllocationsFamilialesException
     * @throws JadePersistenceException
     */
    public AllocationsFamiliales readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AllocationsFamilialesException, JadePersistenceException;

    /**
     * Permet de chercher des AllocationsFamiliales selon un mod�le de crit�res.
     * 
     * @param AllocationsFamilialesSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AllocationsFamilialesSearch search(AllocationsFamilialesSearch allocationsFamilialesSearch)
            throws JadePersistenceException, AllocationsFamilialesException;

    /**
     * 
     * Permet la mise � jour d'une entit� AllocationsFamiliales
     * 
     * @param AllocationsFamiliales
     *            L'entit� AllocationsFamiliales � mettre � jour
     * @return L'entit� AllocationsFamiliales mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public AllocationsFamiliales update(AllocationsFamiliales allocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException, DonneeFinanciereException;
}