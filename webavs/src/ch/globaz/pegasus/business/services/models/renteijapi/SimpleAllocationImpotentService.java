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
     * Permet la création d'une entité allocationImpotent
     * 
     * @param allocationImpotent
     *            L' allocationImpotent à créer
     * @return L' allocationImpotent créé
     * @throws AllocationImpotentException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleAllocationImpotent create(SimpleAllocationImpotent allocationImpotent)
            throws AllocationImpotentException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité allocationImpotent
     * 
     * @param allocationImpotent
     *            L' allocationImpotent à supprimer
     * @return L' allocationImpotent supprimé
     * @throws AllocationImpotentException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleAllocationImpotent delete(SimpleAllocationImpotent allocationImpotent)
            throws AllocationImpotentException, JadePersistenceException;

    /**
     * Permet la suppression réele de la donnée financiere
     * 
     * @param listeIDString
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une allocationImpotent
     * 
     * @param idAllocationImpotent
     *            L'identifiant de la renteAvsAi à charger en mémoire
     * @return L' allocationImpotent chargé en mémoire
     * @throws AllocationImpotentException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleAllocationImpotent read(String idAllocationImpotent) throws AllocationImpotentException,
            JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité allocationImpotent
     * 
     * @param allocationImpotent
     *            L' allocationImpotent à mettre à jour
     * @return L' allocationImpotent mis à jour
     * @throws AllocationImpotentException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleAllocationImpotent update(SimpleAllocationImpotent allocationImpotent)
            throws AllocationImpotentException, JadePersistenceException;

}
