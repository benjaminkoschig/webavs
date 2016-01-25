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
     * Permet la création d'une entité simpleDessaisissementRevenu
     * 
     * @param simpleDessaisissementRevenu
     *            L'entité simpleDessaisissementRevenu à créer
     * @return L'entité simpleDessaisissementRevenu créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleDessaisissementRevenu create(SimpleDessaisissementRevenu simpleDessaisissementRevenu)
            throws JadePersistenceException, DessaisissementRevenuException;

    /**
     * Permet la suppression d'une entité simpleDessaisissementRevenu
     * 
     * @param simpleDessaisissementRevenu
     *            L'entité simpleDessaisissementRevenu à supprimer
     * @return L'entité simpleDessaisissementRevenu supprimé
     * @throws DessaisissementRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
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
     * Permet de charger en mémoire d'une entité simpleDessaisissementRevenu
     * 
     * @param idDessaisissementRevenu
     *            L'identifiant de l'entité simpleDessaisissementRevenu à charger en mémoire
     * @return L'entité simpleDessaisissementRevenu chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleDessaisissementRevenu read(String idDessaisissementRevenu) throws JadePersistenceException,
            DessaisissementRevenuException;

    /**
     * 
     * Permet la mise à jour d'une entité simpleDessaisissementRevenu
     * 
     * @param simpleDessaisissementRevenu
     *            L'entité simpleDessaisissementRevenu à mettre à jour
     * @return L'entité simpleDessaisissementRevenu mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleDessaisissementRevenu update(SimpleDessaisissementRevenu simpleDessaisissementRevenu)
            throws JadePersistenceException, DessaisissementRevenuException;
}
