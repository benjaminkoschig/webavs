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
     * Permet la création d'une entité simplePretEnversTiers
     * 
     * @param simplePretEnversTiers
     *            L'entité simplePretEnversTiers à créer
     * @return L'entité simplePretEnversTiers créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePretEnversTiers create(SimplePretEnversTiers simplePretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException;

    /**
     * Permet la suppression d'une entité simplePretEnversTiers
     * 
     * @param simplePretEnversTiers
     *            L'entité simplePretEnversTiers à supprimer
     * @return L'entité simplePretEnversTiers supprimé
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
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
     * Permet de charger en mémoire d'une entité simplePretEnversTiers
     * 
     * @param idPretEnversTiers
     *            L'identifiant de l'entité simplePretEnversTiers à charger en mémoire
     * @return L'entité simplePretEnversTiers chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePretEnversTiers read(String idPretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException;

    /**
     * 
     * Permet la mise à jour d'une entité simplePretEnversTiers
     * 
     * @param simplePretEnversTiers
     *            L'entité simplePretEnversTiers à mettre à jour
     * @return L'entité simplePretEnversTiers mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PretEnversTiersException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePretEnversTiers update(SimplePretEnversTiers simplePretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException;
}
