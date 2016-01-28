package ch.globaz.al.business.services.models.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexSearchModel;

/**
 * interface de déclaration des services de AllocataireComplexMOdel
 * 
 * @author PTA
 */
public interface AllocataireComplexModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'allocataires correspondant aux critères contenu dans le modèle de recherche
     * <code>allocataireComplexSearch</code>
     * 
     * @param allocataireComplexSearch
     *            selon modèle AllocataireComplexSearchModel
     * @return résultat de la recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public int count(AllocataireComplexSearchModel allocataireComplexSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Enregistre le modèle d'allocataire passé en paramètre en persistance
     * 
     * @param allocataireComplex
     *            modèle à enregistrer
     * 
     * @return modèle enregistré
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireComplexModel create(AllocataireComplexModel allocataireComplex) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * 
     * @param allocataireComplex
     *            le modèle à supprimer
     * @return le modèle supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireComplexModel delete(AllocataireComplexModel allocataireComplex) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un modèle d'allocataire
     * 
     * @param allocataireComplexModel
     *            le modèle à initialisé
     * @return le modèle initialisé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireComplexModel initModel(AllocataireComplexModel allocataireComplexModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge un allocataire
     * 
     * @param idAllocataireComplexModel
     *            identifiant de l'allocataire à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireComplexModel read(String idAllocataireComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effectue une recherche d'allocataires selon les critères contenu dans le modèle de recherche
     * <code>allocataireComplexSearch</code>
     * 
     * @param allocataireComplexModel
     *            selon modèle AllocataireComplexSearchModel
     * @return résultat de la recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AllocataireComplexSearchModel search(AllocataireComplexSearchModel allocataireComplexModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met à jour le modèle d'allocataire passé en paramètre en persistance
     * 
     * @param allocataireComplex
     *            modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireComplexModel update(AllocataireComplexModel allocataireComplex) throws JadeApplicationException,
            JadePersistenceException;
}
