package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.CommentaireModel;
import ch.globaz.al.business.models.dossier.CommentaireSearchModel;

/**
 * Service de gestion de la persistance des donn�es des commentaires de dossier
 * 
 * @author jts
 */
public interface CommentaireModelService extends JadeApplicationService {

    /**
     * Cr�er un nouveau <code>CommentaireModel</code> sur la base de celui pass� en param�tre
     * 
     * @param commentaireModel
     *            Commentaire � cloner
     * @param idDossier
     *            Id du dossier auquel lier le clone du commentaire
     * @return Clone du commentaire
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public CommentaireModel clone(CommentaireModel commentaireModel, String idDossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param commentaireSearch
     *            Mod�le de recherche contenant les crit�res de filtrage
     * @return nombre de commentaire correspondant au mod�le de recherche
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public int count(CommentaireSearchModel commentaireSearch) throws JadePersistenceException;

    /**
     * Enregistre un commentaire en persistance
     * 
     * @param commentaireModel
     *            Commentaire � enregistrer
     * @return CommentaireModel Commentaire enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public CommentaireModel create(CommentaireModel commentaireModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * suppression d'un commentaire
     * 
     * @param commentaireModel
     *            Commentaire � supprimer
     * @return CommentaireModel commentaire supprim�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public CommentaireModel delete(CommentaireModel commentaireModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge les donn�es du commentaire correspondant � <code>idCommentaire</code>
     * 
     * @param idCommentaire
     *            Id du commentaire � charger
     * @return CommentaireModel Mod�le du commentaire charg�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public CommentaireModel read(String idCommentaire) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche et retourne les commentaires correspondant au crit�re de recherche
     * 
     * @param commentaireSearch
     *            Mod�le de recherche contenant les crit�res
     * @return R�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public CommentaireSearchModel search(CommentaireSearchModel commentaireSearch) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mets � jour un commentaire en persistance
     * 
     * @param commentaireModel
     *            Commentaire � mettre � jour
     * @return CommentaireModel Commentaire mis � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CommentaireModel
     */
    public CommentaireModel update(CommentaireModel commentaireModel) throws JadeApplicationException,
            JadePersistenceException;
}