package ch.globaz.al.business.services.models.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.rafam.ErreurAnnonceRafamModel;
import ch.globaz.al.business.models.rafam.ErreurAnnonceRafamSearchModel;

/**
 * Services liés à la persistance des annonces RAFAM
 * 
 * @author jts
 */
public interface ErreurAnnonceRafamModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param search
     *            Modèle de recherche contenant les critères
     * @return nombre de copies correspondant aux critères de recherche
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public int count(ErreurAnnonceRafamSearchModel search) throws JadePersistenceException, JadeApplicationException;

    /**
     * Enregistre <code>model</code> en persistance
     * 
     * @param model
     *            Annonce à enregistrer
     * @return Annonce enregistrée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.rafam.ErreurAnnonceRafamModel
     */
    public ErreurAnnonceRafamModel create(ErreurAnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime <code>model</code> de la persistance
     * 
     * @param model
     *            Annonce à supprimer
     * @return l'erreur d'annonce supprimée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.rafam.ErreurAnnonceRafamModel
     */
    public ErreurAnnonceRafamModel delete(ErreurAnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException;

    public void deleteForIdAnnonce(String idAnnonce) throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère les données de l'erreur d'annonce correspondant à <code>idErreurAnnonce</code>
     * 
     * @param idAnnonce
     *            Id de l'erreur d'annonce à charger
     * @return Annonce chargée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.rafam.ErreurAnnonceRafamModel
     */
    public ErreurAnnonceRafamModel read(String idErreurAnnonce) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche les annonces correspondant aux critère contenus dans le modèle de recherche passé en paramètre
     * 
     * @param search
     *            modèle contenant les critères de recherche
     * @return résultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ErreurAnnonceRafamSearchModel search(ErreurAnnonceRafamSearchModel search) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Met à jour les données de <code>ErreurAnnonceRafamModel</code> en persistance
     * 
     * @param model
     *            Annonce à mettre à jour
     * @return Annonce mise à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.rafam.ErreurAnnonceRafamModel
     */
    public ErreurAnnonceRafamModel update(ErreurAnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException;
}
