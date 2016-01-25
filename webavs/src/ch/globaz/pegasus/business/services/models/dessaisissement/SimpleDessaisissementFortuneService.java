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
     * Permet la création d'une entité simpleDessaisissementFortune
     * 
     * @param simpleDessaisissementFortune
     *            L'entité simpleDessaisissementFortune à créer
     * @return L'entité simpleDessaisissementFortune créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleDessaisissementFortune create(SimpleDessaisissementFortune simpleDessaisissementFortune)
            throws JadePersistenceException, DessaisissementFortuneException;

    /**
     * Permet la suppression d'une entité simpleDessaisissementFortune
     * 
     * @param simpleDessaisissementFortune
     *            L'entité simpleDessaisissementFortune à supprimer
     * @return L'entité simpleDessaisissementFortune supprimé
     * @throws DessaisissementFortuneException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
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
     * Permet de charger en mémoire d'une entité simpleDessaisissementFortune
     * 
     * @param idDessaisissementFortune
     *            L'identifiant de l'entité simpleDessaisissementFortune à charger en mémoire
     * @return L'entité simpleDessaisissementFortune chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleDessaisissementFortune read(String idDessaisissementFortune) throws JadePersistenceException,
            DessaisissementFortuneException;

    /**
     * 
     * Permet la mise à jour d'une entité simpleDessaisissementFortune
     * 
     * @param simpleDessaisissementFortune
     *            L'entité simpleDessaisissementFortune à mettre à jour
     * @return L'entité simpleDessaisissementFortune mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleDessaisissementFortune update(SimpleDessaisissementFortune simpleDessaisissementFortune)
            throws JadePersistenceException, DessaisissementFortuneException;
}
