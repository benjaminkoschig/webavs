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
     * Permet la cr�ation d'une entit� SimpleAutresRevenus
     * 
     * @param simpleSubsideAssuranceMaladie
     *            L'entit� SimpleFraisGarde � cr�er
     * @return L'entit� SimpleFraisGarde cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SubsideAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleSubsideAssuranceMaladie create(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) throws JadePersistenceException,
            SubsideAssuranceMaladieException;

    /**
     * Permet la suppression d'une entit� SimpleFraisGarde
     * 
     * @param simpleSubsideAssuranceMaladie
     *            L'entit� SimpleFraisGarde � supprimer
     * @return L'entit� SimpleFraisGarde supprim�
     * @throws SubsideAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleSubsideAssuranceMaladie delete(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) throws SubsideAssuranceMaladieException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simpleAssuranceRenteViagere
     * 
     * @param idSubsideAssuranceMaladie
     *            L'identifiant de l'entit� SimpleFraisGarde � charger en m�moire
     * @return L'entit� SimpleFraisGarde charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SubsideAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleSubsideAssuranceMaladie read(String idSubsideAssuranceMaladie) throws JadePersistenceException, SubsideAssuranceMaladieException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleFraisGarde
     * 
     * @param simpleSubsideAssuranceMaladie
     *            L'entit� SimpleFraisGarde � mettre � jour
     * @return L'entit� SimpleFraisGarde mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SubsideAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleSubsideAssuranceMaladie update(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) throws JadePersistenceException,
            SubsideAssuranceMaladieException;
}
