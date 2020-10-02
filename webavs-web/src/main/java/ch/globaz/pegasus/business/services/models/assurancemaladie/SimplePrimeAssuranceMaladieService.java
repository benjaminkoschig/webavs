package ch.globaz.pegasus.business.services.models.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.PrimeAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.FraisGardeException;
import ch.globaz.pegasus.business.models.assurancemaladie.SimplePrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleFraisGarde;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.util.List;

/**
 * @author FHA
 * 
 */
public interface SimplePrimeAssuranceMaladieService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleAutresRevenus
     * 
     * @param simplePrimeAssuranceMaladie
     *            L'entité SimpleFraisGarde à créer
     * @return L'entité SimpleFraisGarde créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrimeAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePrimeAssuranceMaladie create(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) throws JadePersistenceException,
            PrimeAssuranceMaladieException;

    /**
     * Permet la suppression d'une entité SimpleFraisGarde
     * 
     * @param simplePrimeAssuranceMaladie
     *            L'entité SimpleFraisGarde à supprimer
     * @return L'entité SimpleFraisGarde supprimé
     * @throws PrimeAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePrimeAssuranceMaladie delete(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) throws PrimeAssuranceMaladieException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité simpleAssuranceRenteViagere
     * 
     * @param idPrimeAssuranceMaladie
     *            L'identifiant de l'entité SimpleFraisGarde à charger en mémoire
     * @return L'entité SimpleFraisGarde chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrimeAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePrimeAssuranceMaladie read(String idPrimeAssuranceMaladie) throws JadePersistenceException, PrimeAssuranceMaladieException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleFraisGarde
     * 
     * @param simplePrimeAssuranceMaladie
     *            L'entité SimpleFraisGarde à mettre à jour
     * @return L'entité SimpleFraisGarde mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrimeAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePrimeAssuranceMaladie update(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) throws JadePersistenceException,
            PrimeAssuranceMaladieException;
}
