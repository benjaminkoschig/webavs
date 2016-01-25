/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.BetailException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleBetail;

/**
 * @author BSC
 * 
 */
public interface SimpleBetailService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleBetail
     * 
     * @param simpleBetail
     *            L'entit� simpleBetail � cr�er
     * @return L'entit� simpleBetail cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BetailException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBetail create(SimpleBetail simpleBetail) throws JadePersistenceException, BetailException;

    /**
     * Permet la suppression d'une entit� simpleBetail
     * 
     * @param simpleBetail
     *            L'entit� simpleBetail � supprimer
     * @return L'entit� simpleBetail supprim�
     * @throws BetailException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleBetail delete(SimpleBetail simpleBetail) throws BetailException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleBetail en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simpleBetail
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simpleBetail
     * 
     * @param idBetail
     *            L'identifiant de l'entit� simpleBetail � charger en m�moire
     * @return L'entit� simpleBetail charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BetailException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBetail read(String idBetail) throws JadePersistenceException, BetailException;

    /**
     * 
     * Permet la mise � jour d'une entit� simpleBetail
     * 
     * @param simpleBetail
     *            L'entit� simpleBetail � mettre � jour
     * @return L'entit� simpleBetail mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BetailException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBetail update(SimpleBetail simpleBetail) throws JadePersistenceException, BetailException;
}
