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
     * Permet la cr�ation d'une entit� SimpleAutresRevenus
     * 
     * @param SimpleAutresRevenusChecker
     *            L'entit� SimpleAutresRevenus � cr�er
     * @return L'entit� SimpleAutresRevenus cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresRevenusException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutresRevenus create(SimpleAutresRevenus simpleAutresRevenus) throws JadePersistenceException,
            AutresRevenusException;

    /**
     * Permet la suppression d'une entit� SimpleAutresRevenus
     * 
     * @param SimpleAutresRevenusChecker
     *            L'entit� SimpleAutresRevenus � supprimer
     * @return L'entit� SimpleAutresRevenus supprim�
     * @throws AutresRevenusException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleAutresRevenus delete(SimpleAutresRevenus simpleAutresRevenus) throws AutresRevenusException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simpleAssuranceRenteViagere
     * 
     * @param idAutresRevenus
     *            L'identifiant de l'entit� SimpleAutresRevenus � charger en m�moire
     * @return L'entit� SimpleAutresRevenus charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresRevenusException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutresRevenus read(String idAutresRevenus) throws JadePersistenceException, AutresRevenusException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleAutresRevenus
     * 
     * @param SimpleAutresRevenusChecker
     *            L'entit� SimpleAutresRevenus � mettre � jour
     * @return L'entit� SimpleAutresRevenus mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresRevenusException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutresRevenus update(SimpleAutresRevenus simpleAutresRevenus) throws JadePersistenceException,
            AutresRevenusException;
}
