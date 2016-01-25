/**
 * 
 */
package ch.globaz.pegasus.business.services.models.dessaisissement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementRevenuException;
import ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementRevenu;

/**
 * @author BSC
 * 
 */
public interface SimpleDessaisissementRevenuService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleDessaisissementRevenu
     * 
     * @param simpleDessaisissementRevenu
     *            L'entit� simpleDessaisissementRevenu � cr�er
     * @return L'entit� simpleDessaisissementRevenu cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleDessaisissementRevenu create(SimpleDessaisissementRevenu simpleDessaisissementRevenu)
            throws JadePersistenceException, DessaisissementRevenuException;

    /**
     * Permet la suppression d'une entit� simpleDessaisissementRevenu
     * 
     * @param simpleDessaisissementRevenu
     *            L'entit� simpleDessaisissementRevenu � supprimer
     * @return L'entit� simpleDessaisissementRevenu supprim�
     * @throws DessaisissementRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDessaisissementRevenu delete(SimpleDessaisissementRevenu simpleDessaisissementRevenu)
            throws DessaisissementRevenuException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleDessaisissementRevenu en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les
     *            simpleDessaisissementRevenu
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simpleDessaisissementRevenu
     * 
     * @param idDessaisissementRevenu
     *            L'identifiant de l'entit� simpleDessaisissementRevenu � charger en m�moire
     * @return L'entit� simpleDessaisissementRevenu charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleDessaisissementRevenu read(String idDessaisissementRevenu) throws JadePersistenceException,
            DessaisissementRevenuException;

    /**
     * 
     * Permet la mise � jour d'une entit� simpleDessaisissementRevenu
     * 
     * @param simpleDessaisissementRevenu
     *            L'entit� simpleDessaisissementRevenu � mettre � jour
     * @return L'entit� simpleDessaisissementRevenu mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleDessaisissementRevenu update(SimpleDessaisissementRevenu simpleDessaisissementRevenu)
            throws JadePersistenceException, DessaisissementRevenuException;
}
