package ch.globaz.pegasus.business.services.models.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.SubsideAssuranceMaladieException;
import ch.globaz.pegasus.business.models.assurancemaladie.SimpleSubsideAssuranceMaladie;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.util.List;

/**
 * @author FHA
 * 
 */
public interface SimpleSubsideAssuranceMaladieService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleAutresRevenus
     * 
     * @param simpleSubsideAssuranceMaladie
     *            L'entité SimpleFraisGarde à créer
     * @return L'entité SimpleFraisGarde créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SubsideAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleSubsideAssuranceMaladie create(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) throws JadePersistenceException,
            SubsideAssuranceMaladieException;

    /**
     * Permet la suppression d'une entité SimpleFraisGarde
     * 
     * @param simpleSubsideAssuranceMaladie
     *            L'entité SimpleFraisGarde à supprimer
     * @return L'entité SimpleFraisGarde supprimé
     * @throws SubsideAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleSubsideAssuranceMaladie delete(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) throws SubsideAssuranceMaladieException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité simpleAssuranceRenteViagere
     * 
     * @param idSubsideAssuranceMaladie
     *            L'identifiant de l'entité SimpleFraisGarde à charger en mémoire
     * @return L'entité SimpleFraisGarde chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SubsideAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleSubsideAssuranceMaladie read(String idSubsideAssuranceMaladie) throws JadePersistenceException, SubsideAssuranceMaladieException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleFraisGarde
     * 
     * @param simpleSubsideAssuranceMaladie
     *            L'entité SimpleFraisGarde à mettre à jour
     * @return L'entité SimpleFraisGarde mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SubsideAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleSubsideAssuranceMaladie update(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) throws JadePersistenceException,
            SubsideAssuranceMaladieException;
}
