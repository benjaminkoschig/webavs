/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.PretEnversTiersException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers;

/**
 * @author BSC
 * 
 */
public interface SimplePretEnversTiersService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simplePretEnversTiers
     * 
     * @param simplePretEnversTiers
     *            L'entit� simplePretEnversTiers � cr�er
     * @return L'entit� simplePretEnversTiers cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PretEnversTiersException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePretEnversTiers create(SimplePretEnversTiers simplePretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException;

    /**
     * Permet la suppression d'une entit� simplePretEnversTiers
     * 
     * @param simplePretEnversTiers
     *            L'entit� simplePretEnversTiers � supprimer
     * @return L'entit� simplePretEnversTiers supprim�
     * @throws PretEnversTiersException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePretEnversTiers delete(SimplePretEnversTiers simplePretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException;

    /**
     * Permet l'effacement de simplePretEnversTiers en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simplePretEnversTiers
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simplePretEnversTiers
     * 
     * @param idPretEnversTiers
     *            L'identifiant de l'entit� simplePretEnversTiers � charger en m�moire
     * @return L'entit� simplePretEnversTiers charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PretEnversTiersException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePretEnversTiers read(String idPretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException;

    /**
     * 
     * Permet la mise � jour d'une entit� simplePretEnversTiers
     * 
     * @param simplePretEnversTiers
     *            L'entit� simplePretEnversTiers � mettre � jour
     * @return L'entit� simplePretEnversTiers mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PretEnversTiersException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePretEnversTiers update(SimplePretEnversTiers simplePretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException;
}
