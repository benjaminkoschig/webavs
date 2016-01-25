/**
 * 
 */
package ch.globaz.al.business.services.models.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.allocataire.AgricoleModel;
import ch.globaz.al.business.models.allocataire.AgricoleSearchModel;

/**
 * Service de gestion de persistance des données de Agricol de allocataire
 * 
 * @author PTA
 */
public interface AgricoleModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param agricoleSearch
     *            modèle contenant les critères de recherche
     * @return nombre d'enregistrement correspondant à la recherche
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(AgricoleSearchModel agricoleSearch) throws JadePersistenceException;

    /**
     * Crée d'un allocataire agricole selon le modèle agricole passé en paramètre
     * 
     * @param agricoleModel
     *            modèle à enregistrer
     * @return le modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AgricoleModel create(AgricoleModel agricoleModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * suppression d'un allocataire agricole selon le modèle agricoles passé en paramètre
     * 
     * @param agricoleModel
     *            le modèle à supprimer
     * @return le modèle supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AgricoleModel delete(AgricoleModel agricoleModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Permet la suppression des données agriculteur liées à un allocataire dont l'id est passé en paramètre
     * 
     * @param idAllocataire
     *            id de l'allocataire pour lequel supprimer les données
     * @return modèle contenant les données supprimées
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AgricoleModel deleteForIdAllocataire(String idAllocataire) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Lecture d'un allocataire agricole en fonction de son id passé en paramètre
     * 
     * @param idAgricoleModel
     *            id du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AgricoleModel read(String idAgricoleModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche selon le modèle de recherche passé en paramètre
     * 
     * @param searchModel
     *            modèle de recherche de dossier
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AgricoleSearchModel search(AgricoleSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * 
     * Mise à jour d'un allocataire agricole selon l'allocataire agricole passé en paramètre
     * 
     * @param agricoleModel
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AgricoleModel update(AgricoleModel agricoleModel) throws JadeApplicationException, JadePersistenceException;

}
