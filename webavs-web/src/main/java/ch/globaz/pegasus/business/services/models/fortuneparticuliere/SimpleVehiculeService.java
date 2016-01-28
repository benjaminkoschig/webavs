/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.VehiculeException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleVehicule;

/**
 * @author BSC
 * 
 */
public interface SimpleVehiculeService extends JadeApplicationService {

    /**
     * Permet la création d'une entité simpleVehicule
     * 
     * @param simpleVehicule
     *            L'entité simpleVehicule à créer
     * @return L'entité simpleVehicule créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws VehiculeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleVehicule create(SimpleVehicule simpleVehicule) throws JadePersistenceException, VehiculeException;

    /**
     * Permet la suppression d'une entité simpleVehicule
     * 
     * @param simpleVehicule
     *            L'entité simpleVehicule à supprimer
     * @return L'entité simpleVehicule supprimé
     * @throws VehiculeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleVehicule delete(SimpleVehicule simpleVehicule) throws VehiculeException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleVehicule en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simpleVehicule
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité simpleVehicule
     * 
     * @param idVehicule
     *            L'identifiant de l'entité simpleVehicule à charger en mémoire
     * @return L'entité simpleVehicule chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws VehiculeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleVehicule read(String idVehicule) throws JadePersistenceException, VehiculeException;

    /**
     * 
     * Permet la mise à jour d'une entité simpleVehicule
     * 
     * @param simpleVehicule
     *            L'entité simpleVehicule à mettre à jour
     * @return L'entité simpleVehicule mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws VehiculeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleVehicule update(SimpleVehicule simpleVehicule) throws JadePersistenceException, VehiculeException;
}
