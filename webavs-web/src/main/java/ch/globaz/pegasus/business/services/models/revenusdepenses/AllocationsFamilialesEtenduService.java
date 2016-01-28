package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesEtendu;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesEtenduSearch;

public interface AllocationsFamilialesEtenduService extends JadeApplicationService {
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
    public int count(AllocationsFamilialesEtenduSearch search) throws AllocationsFamilialesException,
            JadePersistenceException;

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
    public AllocationsFamilialesEtendu read(String idAllocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException;

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
    public AllocationsFamilialesEtenduSearch search(AllocationsFamilialesEtenduSearch allocationsFamilialesSearch)
            throws JadePersistenceException, AllocationsFamilialesException;

}