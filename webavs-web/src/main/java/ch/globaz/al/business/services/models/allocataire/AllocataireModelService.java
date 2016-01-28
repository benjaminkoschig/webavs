/**
 * 
 */
package ch.globaz.al.business.services.models.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.allocataire.AllocataireSearchModel;

/**
 * Service de gestion de persistance des données des allocataire de allocataire
 * 
 * @author PTA
 */
public interface AllocataireModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param allocataireSearch
     *            modèle contenant les critères de recherche
     * @return nombre d'enregistrement correspondant à la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(AllocataireSearchModel allocataireSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * création d'un allocataire selon le modèle allocataire passé en paramètre
     * 
     * @param allocataireModel
     *            modèle à enregistrer
     * @return le modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireModel create(AllocataireModel allocataireModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Suppression d'un allocataire selon le modèle allocataire passé en paramètre
     * 
     * @param allocataireModel
     *            le modèle à supprimer
     * @return le modèle supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireModel delete(AllocataireModel allocataireModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * initialisation d'un modèle allocataire selon le modèle passée en paramètre
     * 
     * @param allocataireModel
     *            le modèle à initialiser
     * @return le modèle initialisé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireModel initModel(AllocataireModel allocataireModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'un allocataire selon l'identifiant de l'allocataire passé en paramètre
     * 
     * @param idAllocataireModel
     *            id du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireModel read(String idAllocataireModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise à jour d'un allocataire selon le modèle allocataire passé en paramètre
     * 
     * @param allocataireModel
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireModel update(AllocataireModel allocataireModel) throws JadeApplicationException,
            JadePersistenceException;
}
