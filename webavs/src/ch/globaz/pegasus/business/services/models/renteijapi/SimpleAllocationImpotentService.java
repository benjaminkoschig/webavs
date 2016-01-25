package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AllocationImpotentException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAllocationImpotent;

/**
 * Interface pour le service des allocation impotents AI 6.2010
 * 
 * @author SCE
 * 
 */
public interface SimpleAllocationImpotentService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� allocationImpotent
     * 
     * @param allocationImpotent
     *            L' allocationImpotent � cr�er
     * @return L' allocationImpotent cr��
     * @throws AllocationImpotentException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleAllocationImpotent create(SimpleAllocationImpotent allocationImpotent)
            throws AllocationImpotentException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� allocationImpotent
     * 
     * @param allocationImpotent
     *            L' allocationImpotent � supprimer
     * @return L' allocationImpotent supprim�
     * @throws AllocationImpotentException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleAllocationImpotent delete(SimpleAllocationImpotent allocationImpotent)
            throws AllocationImpotentException, JadePersistenceException;

    /**
     * Permet la suppression r�ele de la donn�e financiere
     * 
     * @param listeIDString
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire une allocationImpotent
     * 
     * @param idAllocationImpotent
     *            L'identifiant de la renteAvsAi � charger en m�moire
     * @return L' allocationImpotent charg� en m�moire
     * @throws AllocationImpotentException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleAllocationImpotent read(String idAllocationImpotent) throws AllocationImpotentException,
            JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� allocationImpotent
     * 
     * @param allocationImpotent
     *            L' allocationImpotent � mettre � jour
     * @return L' allocationImpotent mis � jour
     * @throws AllocationImpotentException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleAllocationImpotent update(SimpleAllocationImpotent allocationImpotent)
            throws AllocationImpotentException, JadePersistenceException;

}
