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
     * Permet la création d'une entité simpleBetail
     * 
     * @param simpleBetail
     *            L'entité simpleBetail à créer
     * @return L'entité simpleBetail créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BetailException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBetail create(SimpleBetail simpleBetail) throws JadePersistenceException, BetailException;

    /**
     * Permet la suppression d'une entité simpleBetail
     * 
     * @param simpleBetail
     *            L'entité simpleBetail à supprimer
     * @return L'entité simpleBetail supprimé
     * @throws BetailException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
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
     * Permet de charger en mémoire d'une entité simpleBetail
     * 
     * @param idBetail
     *            L'identifiant de l'entité simpleBetail à charger en mémoire
     * @return L'entité simpleBetail chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BetailException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBetail read(String idBetail) throws JadePersistenceException, BetailException;

    /**
     * 
     * Permet la mise à jour d'une entité simpleBetail
     * 
     * @param simpleBetail
     *            L'entité simpleBetail à mettre à jour
     * @return L'entité simpleBetail mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BetailException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBetail update(SimpleBetail simpleBetail) throws JadePersistenceException, BetailException;
}
