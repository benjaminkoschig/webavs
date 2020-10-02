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
     * Permet la cr�ation d'une entit� SimpleAutresRevenus
     * 
     * @param SimpleFraisGardeChecker
     *            L'entit� SimpleFraisGarde � cr�er
     * @return L'entit� SimpleFraisGarde cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FraisGardeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleFraisGarde create(SimpleFraisGarde simpleFraisGarde) throws JadePersistenceException,
            FraisGardeException;

    /**
     * Permet la suppression d'une entit� SimpleFraisGarde
     * 
     * @param SimpleFraisGardeChecker
     *            L'entit� SimpleFraisGarde � supprimer
     * @return L'entit� SimpleFraisGarde supprim�
     * @throws FraisGardeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleFraisGarde delete(SimpleFraisGarde simpleFraisGarde) throws FraisGardeException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simpleAssuranceRenteViagere
     * 
     * @param idFraisGarde
     *            L'identifiant de l'entit� SimpleFraisGarde � charger en m�moire
     * @return L'entit� SimpleFraisGarde charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FraisGardeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleFraisGarde read(String idFraisGarde) throws JadePersistenceException, FraisGardeException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleFraisGarde
     * 
     * @param SimpleFraisGardeChecker
     *            L'entit� SimpleFraisGarde � mettre � jour
     * @return L'entit� SimpleFraisGarde mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FraisGardeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleFraisGarde update(SimpleFraisGarde simpleFraisGarde) throws JadePersistenceException,
            FraisGardeException;
}
