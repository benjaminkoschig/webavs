/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AutreFortuneMobiliereException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAutreFortuneMobiliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAutreFortuneMobiliereSearch;

/**
 * @author BSC
 * 
 */
public interface SimpleAutreFortuneMobiliereService extends JadeApplicationService {

    /**
     * Permet la création d'une entité simpleAutreFortuneMobiliere
     * 
     * @param simpleAutreFortuneMobiliere
     *            L'entité simpleAutreFortuneMobiliere à créer
     * @return L'entité simpleAutreFortuneMobiliere créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutreFortuneMobiliere create(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere)
            throws JadePersistenceException, AutreFortuneMobiliereException;

    /**
     * Permet la suppression d'une entité simpleAutreFortuneMobiliere
     * 
     * @param simpleAutreFortuneMobiliere
     *            L'entité simpleAutreFortuneMobiliere à supprimer
     * @return L'entité simpleAutreFortuneMobiliere supprimé
     * @throws AutreFortuneMobiliereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleAutreFortuneMobiliere delete(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere)
            throws AutreFortuneMobiliereException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleAutreFortuneMobiliere en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les
     *            simpleAutreFortuneMobiliere
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité simpleAutreFortuneMobiliere
     * 
     * @param idAutreFortuneMobiliere
     *            L'identifiant de l'entité simpleAutreFortuneMobiliere à charger en mémoire
     * @return L'entité simpleAutreFortuneMobiliere chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutreFortuneMobiliere read(String idAutreFortuneMobiliere) throws JadePersistenceException,
            AutreFortuneMobiliereException;

    /**
     * Permet de chercher des simpleAutreFortuneMobiliere selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     * @throws AutreFortuneMobiliereException
     */
    public SimpleAutreFortuneMobiliereSearch search(SimpleAutreFortuneMobiliereSearch searchModel)
            throws JadePersistenceException, AutreFortuneMobiliereException;

    /**
     * 
     * Permet la mise à jour d'une entité simpleAutreFortuneMobiliere
     * 
     * @param simpleAutreFortuneMobiliere
     *            L'entité simpleAutreFortuneMobiliere à mettre à jour
     * @return L'entité simpleAutreFortuneMobiliere mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutreFortuneMobiliere update(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere)
            throws JadePersistenceException, AutreFortuneMobiliereException;
}
