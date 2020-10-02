package ch.globaz.pegasus.business.services.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.FraisGardeException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleFraisGarde;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.util.List;

/**
 * @author FHA
 * 
 */
public interface SimpleFraisGardeService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleAutresRevenus
     * 
     * @param SimpleFraisGardeChecker
     *            L'entité SimpleFraisGarde à créer
     * @return L'entité SimpleFraisGarde créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws FraisGardeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleFraisGarde create(SimpleFraisGarde simpleFraisGarde) throws JadePersistenceException,
            FraisGardeException;

    /**
     * Permet la suppression d'une entité SimpleFraisGarde
     * 
     * @param SimpleFraisGardeChecker
     *            L'entité SimpleFraisGarde à supprimer
     * @return L'entité SimpleFraisGarde supprimé
     * @throws FraisGardeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleFraisGarde delete(SimpleFraisGarde simpleFraisGarde) throws FraisGardeException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité simpleAssuranceRenteViagere
     * 
     * @param idFraisGarde
     *            L'identifiant de l'entité SimpleFraisGarde à charger en mémoire
     * @return L'entité SimpleFraisGarde chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws FraisGardeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleFraisGarde read(String idFraisGarde) throws JadePersistenceException, FraisGardeException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleFraisGarde
     * 
     * @param SimpleFraisGardeChecker
     *            L'entité SimpleFraisGarde à mettre à jour
     * @return L'entité SimpleFraisGarde mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws FraisGardeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleFraisGarde update(SimpleFraisGarde simpleFraisGarde) throws JadePersistenceException,
            FraisGardeException;
}
