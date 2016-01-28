package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleAllocationsFamiliales;

public interface SimpleAllocationsFamilialesService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleAllocationsFamiliales
     * 
     * @param simpleAllocationsFamiliales
     *            L'entit� simpleAllocationsFamiliales � cr�er
     * @return L'entit� simpleAllocationsFamiliales cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAllocationsFamiliales create(SimpleAllocationsFamiliales simpleAllocationsFamiliales)
            throws JadePersistenceException, AllocationsFamilialesException;

    /**
     * Permet la suppression d'une entit� SimpleAllocationsFamiliales
     * 
     * @param SimpleAllocationsFamiliales
     *            L'entit� SimpleAllocationsFamiliales � supprimer
     * @return L'entit� SimpleAllocationsFamiliales supprim�
     * @throws SimpleAllocationsFamilialesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleAllocationsFamiliales delete(SimpleAllocationsFamiliales simpleAllocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleAllocationsFamiliales
     * 
     * @param idAllocationsFamiliales
     *            L'identifiant de l'entit� SimpleAllocationsFamiliales � charger en m�moire
     * @return L'entit� SimpleAllocationsFamiliales charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAllocationsFamiliales read(String idAllocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleAllocationsFamiliales
     * 
     * @param SimpleAllocationsFamiliales
     *            L'entit� SimpleAllocationsFamiliales � mettre � jour
     * @return L'entit� SimpleAllocationsFamiliales mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAllocationsFamiliales update(SimpleAllocationsFamiliales simpleAllocationsFamiliales)
            throws JadePersistenceException, AllocationsFamilialesException;

}
