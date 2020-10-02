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
     * Permet la cr�ation d'une entit� SimpleAutresRevenus
     * 
     * @param simplePrimeAssuranceMaladie
     *            L'entit� SimpleFraisGarde � cr�er
     * @return L'entit� SimpleFraisGarde cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrimeAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePrimeAssuranceMaladie create(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) throws JadePersistenceException,
            PrimeAssuranceMaladieException;

    /**
     * Permet la suppression d'une entit� SimpleFraisGarde
     * 
     * @param simplePrimeAssuranceMaladie
     *            L'entit� SimpleFraisGarde � supprimer
     * @return L'entit� SimpleFraisGarde supprim�
     * @throws PrimeAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePrimeAssuranceMaladie delete(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) throws PrimeAssuranceMaladieException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simpleAssuranceRenteViagere
     * 
     * @param idPrimeAssuranceMaladie
     *            L'identifiant de l'entit� SimpleFraisGarde � charger en m�moire
     * @return L'entit� SimpleFraisGarde charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrimeAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePrimeAssuranceMaladie read(String idPrimeAssuranceMaladie) throws JadePersistenceException, PrimeAssuranceMaladieException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleFraisGarde
     * 
     * @param simplePrimeAssuranceMaladie
     *            L'entit� SimpleFraisGarde � mettre � jour
     * @return L'entit� SimpleFraisGarde mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrimeAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePrimeAssuranceMaladie update(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) throws JadePersistenceException,
            PrimeAssuranceMaladieException;
}
