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
     * Permet la création d'une entité simpleMarchandisesStock
     * 
     * @param simpleMarchandisesStock
     *            L'entité simpleMarchandisesStock à créer
     * @return L'entité simpleMarchandisesStock créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleMarchandisesStock create(SimpleMarchandisesStock simpleMarchandisesStock)
            throws JadePersistenceException, MarchandisesStockException;

    /**
     * Permet la suppression d'une entité simpleMarchandisesStock
     * 
     * @param simpleMarchandisesStock
     *            L'entité simpleMarchandisesStock à supprimer
     * @return L'entité simpleMarchandisesStock supprimé
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
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
     * Permet de charger en mémoire d'une entité simpleMarchandisesStock
     * 
     * @param idMarchandisesStock
     *            L'identifiant de l'entité simpleMarchandisesStock à charger en mémoire
     * @return L'entité simpleMarchandisesStock chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleMarchandisesStock read(String idMarchandisesStock) throws JadePersistenceException,
            MarchandisesStockException;

    /**
     * 
     * Permet la mise à jour d'une entité simpleMarchandisesStock
     * 
     * @param simpleMarchandisesStock
     *            L'entité simpleMarchandisesStock à mettre à jour
     * @return L'entité simpleMarchandisesStock mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MarchandisesStockException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleMarchandisesStock update(SimpleMarchandisesStock simpleMarchandisesStock)
            throws JadePersistenceException, MarchandisesStockException;
}
