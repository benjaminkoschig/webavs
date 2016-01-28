/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.NumeraireException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleNumeraire;

/**
 * @author BSC
 * 
 */
public interface SimpleNumeraireService extends JadeApplicationService {

    /**
     * Permet la création d'une entité simpleNumeraire
     * 
     * @param simpleNumeraire
     *            L'entité simpleNumeraire à créer
     * @return L'entité simpleNumeraire créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleNumeraire create(SimpleNumeraire simpleNumeraire) throws JadePersistenceException, NumeraireException;

    /**
     * Permet la suppression d'une entité simpleNumeraire
     * 
     * @param simpleNumeraire
     *            L'entité simpleNumeraire à supprimer
     * @return L'entité simpleNumeraire supprimé
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleNumeraire delete(SimpleNumeraire simpleNumeraire) throws NumeraireException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleNumeraire en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simpleNumeraire
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité simpleNumeraire
     * 
     * @param idNumeraire
     *            L'identifiant de l'entité simpleNumeraire à charger en mémoire
     * @return L'entité simpleNumeraire chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleNumeraire read(String idNumeraire) throws JadePersistenceException, NumeraireException;

    /**
     * 
     * Permet la mise à jour d'une entité simpleNumeraire
     * 
     * @param simpleNumeraire
     *            L'entité simpleNumeraire à mettre à jour
     * @return L'entité simpleNumeraire mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleNumeraire update(SimpleNumeraire simpleNumeraire) throws JadePersistenceException, NumeraireException;
}
