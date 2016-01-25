package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AutresRevenusException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleAutresRevenus;

/**
 * @author FHA
 * 
 */
public interface SimpleAutresRevenusService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleAutresRevenus
     * 
     * @param SimpleAutresRevenusChecker
     *            L'entité SimpleAutresRevenus à créer
     * @return L'entité SimpleAutresRevenus créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutresRevenus create(SimpleAutresRevenus simpleAutresRevenus) throws JadePersistenceException,
            AutresRevenusException;

    /**
     * Permet la suppression d'une entité SimpleAutresRevenus
     * 
     * @param SimpleAutresRevenusChecker
     *            L'entité SimpleAutresRevenus à supprimer
     * @return L'entité SimpleAutresRevenus supprimé
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleAutresRevenus delete(SimpleAutresRevenus simpleAutresRevenus) throws AutresRevenusException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité simpleAssuranceRenteViagere
     * 
     * @param idAutresRevenus
     *            L'identifiant de l'entité SimpleAutresRevenus à charger en mémoire
     * @return L'entité SimpleAutresRevenus chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutresRevenus read(String idAutresRevenus) throws JadePersistenceException, AutresRevenusException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleAutresRevenus
     * 
     * @param SimpleAutresRevenusChecker
     *            L'entité SimpleAutresRevenus à mettre à jour
     * @return L'entité SimpleAutresRevenus mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutresRevenus update(SimpleAutresRevenus simpleAutresRevenus) throws JadePersistenceException,
            AutresRevenusException;
}
