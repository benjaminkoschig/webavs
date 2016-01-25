/**
 * 
 */
package ch.globaz.pegasus.business.services.models.dessaisissement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementFortuneException;
import ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementFortune;

/**
 * @author BSC
 * 
 */
public interface SimpleDessaisissementFortuneService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleDessaisissementFortune
     * 
     * @param simpleDessaisissementFortune
     *            L'entit� simpleDessaisissementFortune � cr�er
     * @return L'entit� simpleDessaisissementFortune cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleDessaisissementFortune create(SimpleDessaisissementFortune simpleDessaisissementFortune)
            throws JadePersistenceException, DessaisissementFortuneException;

    /**
     * Permet la suppression d'une entit� simpleDessaisissementFortune
     * 
     * @param simpleDessaisissementFortune
     *            L'entit� simpleDessaisissementFortune � supprimer
     * @return L'entit� simpleDessaisissementFortune supprim�
     * @throws DessaisissementFortuneException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDessaisissementFortune delete(SimpleDessaisissementFortune simpleDessaisissementFortune)
            throws DessaisissementFortuneException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleDessaisissementFortune en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les
     *            simpleDessaisissementFortune
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simpleDessaisissementFortune
     * 
     * @param idDessaisissementFortune
     *            L'identifiant de l'entit� simpleDessaisissementFortune � charger en m�moire
     * @return L'entit� simpleDessaisissementFortune charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleDessaisissementFortune read(String idDessaisissementFortune) throws JadePersistenceException,
            DessaisissementFortuneException;

    /**
     * 
     * Permet la mise � jour d'une entit� simpleDessaisissementFortune
     * 
     * @param simpleDessaisissementFortune
     *            L'entit� simpleDessaisissementFortune � mettre � jour
     * @return L'entit� simpleDessaisissementFortune mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleDessaisissementFortune update(SimpleDessaisissementFortune simpleDessaisissementFortune)
            throws JadePersistenceException, DessaisissementFortuneException;
}
