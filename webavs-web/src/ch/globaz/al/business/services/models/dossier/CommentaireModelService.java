package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.CommentaireModel;
import ch.globaz.al.business.models.dossier.CommentaireSearchModel;

/**
 * Service de gestion de la persistance des données des commentaires de dossier
 * 
 * @author jts
 */
public interface CommentaireModelService extends JadeApplicationService {

    /**
     * Créer un nouveau <code>CommentaireModel</code> sur la base de celui passé en paramètre
     * 
     * @param commentaireModel
     *            Commentaire à cloner
     * @param idDossier
     *            Id du dossier auquel lier le clone du commentaire
     * @return Clone du commentaire
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public CommentaireModel clone(CommentaireModel commentaireModel, String idDossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param commentaireSearch
     *            Modèle de recherche contenant les critères de filtrage
     * @return nombre de commentaire correspondant au modèle de recherche
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public int count(CommentaireSearchModel commentaireSearch) throws JadePersistenceException;

    /**
     * Enregistre un commentaire en persistance
     * 
     * @param commentaireModel
     *            Commentaire à enregistrer
     * @return CommentaireModel Commentaire enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public CommentaireModel create(CommentaireModel commentaireModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * suppression d'un commentaire
     * 
     * @param commentaireModel
     *            Commentaire à supprimer
     * @return CommentaireModel commentaire supprimé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public CommentaireModel delete(CommentaireModel commentaireModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge les données du commentaire correspondant à <code>idCommentaire</code>
     * 
     * @param idCommentaire
     *            Id du commentaire à charger
     * @return CommentaireModel Modèle du commentaire chargé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public CommentaireModel read(String idCommentaire) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche et retourne les commentaires correspondant au critère de recherche
     * 
     * @param commentaireSearch
     *            Modèle de recherche contenant les critères
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public CommentaireSearchModel search(CommentaireSearchModel commentaireSearch) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mets à jour un commentaire en persistance
     * 
     * @param commentaireModel
     *            Commentaire à mettre à jour
     * @return CommentaireModel Commentaire mis à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public CommentaireModel update(CommentaireModel commentaireModel) throws JadeApplicationException,
            JadePersistenceException;
}