package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleAllocationsFamiliales;

public interface SimpleAllocationsFamilialesService extends JadeApplicationService {

    /**
     * Permet la création d'une entité simpleAllocationsFamiliales
     * 
     * @param simpleAllocationsFamiliales
     *            L'entité simpleAllocationsFamiliales à créer
     * @return L'entité simpleAllocationsFamiliales créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAllocationsFamiliales create(SimpleAllocationsFamiliales simpleAllocationsFamiliales)
            throws JadePersistenceException, AllocationsFamilialesException;

    /**
     * Permet la suppression d'une entité SimpleAllocationsFamiliales
     * 
     * @param SimpleAllocationsFamiliales
     *            L'entité SimpleAllocationsFamiliales à supprimer
     * @return L'entité SimpleAllocationsFamiliales supprimé
     * @throws SimpleAllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleAllocationsFamiliales delete(SimpleAllocationsFamiliales simpleAllocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleAllocationsFamiliales
     * 
     * @param idAllocationsFamiliales
     *            L'identifiant de l'entité SimpleAllocationsFamiliales à charger en mémoire
     * @return L'entité SimpleAllocationsFamiliales chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAllocationsFamiliales read(String idAllocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleAllocationsFamiliales
     * 
     * @param SimpleAllocationsFamiliales
     *            L'entité SimpleAllocationsFamiliales à mettre à jour
     * @return L'entité SimpleAllocationsFamiliales mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAllocationsFamiliales update(SimpleAllocationsFamiliales simpleAllocationsFamiliales)
            throws JadePersistenceException, AllocationsFamilialesException;

}
