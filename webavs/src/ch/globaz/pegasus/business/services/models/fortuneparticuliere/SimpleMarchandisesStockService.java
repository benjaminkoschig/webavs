/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.MarchandisesStockException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleMarchandisesStock;

/**
 * @author BSC
 * 
 */
public interface SimpleMarchandisesStockService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleMarchandisesStock
     * 
     * @param simpleMarchandisesStock
     *            L'entit� simpleMarchandisesStock � cr�er
     * @return L'entit� simpleMarchandisesStock cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MarchandisesStockException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleMarchandisesStock create(SimpleMarchandisesStock simpleMarchandisesStock)
            throws JadePersistenceException, MarchandisesStockException;

    /**
     * Permet la suppression d'une entit� simpleMarchandisesStock
     * 
     * @param simpleMarchandisesStock
     *            L'entit� simpleMarchandisesStock � supprimer
     * @return L'entit� simpleMarchandisesStock supprim�
     * @throws MarchandisesStockException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleMarchandisesStock delete(SimpleMarchandisesStock simpleMarchandisesStock)
            throws MarchandisesStockException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleMarchandisesStock en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simpleMarchandisesStock
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simpleMarchandisesStock
     * 
     * @param idMarchandisesStock
     *            L'identifiant de l'entit� simpleMarchandisesStock � charger en m�moire
     * @return L'entit� simpleMarchandisesStock charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MarchandisesStockException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleMarchandisesStock read(String idMarchandisesStock) throws JadePersistenceException,
            MarchandisesStockException;

    /**
     * 
     * Permet la mise � jour d'une entit� simpleMarchandisesStock
     * 
     * @param simpleMarchandisesStock
     *            L'entit� simpleMarchandisesStock � mettre � jour
     * @return L'entit� simpleMarchandisesStock mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MarchandisesStockException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleMarchandisesStock update(SimpleMarchandisesStock simpleMarchandisesStock)
            throws JadePersistenceException, MarchandisesStockException;
}
