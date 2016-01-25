package ch.globaz.al.business.services.models.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.rafam.ErrorPeriodModel;
import ch.globaz.al.business.models.rafam.ErrorPeriodSearchModel;

/**
 * Services liés à la persistance des annonces RAFAM
 * 
 * @author jts
 */
public interface ErrorPeriodModelService extends JadeApplicationService {

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
    public int count(ErrorPeriodSearchModel search) throws JadePersistenceException, JadeApplicationException;

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
     * @see ch.globaz.al.business.models.rafam.ErrorPeriodModel
     */
    public ErrorPeriodModel create(ErrorPeriodModel model) throws JadeApplicationException, JadePersistenceException;

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
     * @see ch.globaz.al.business.models.rafam.ErrorPeriodModel
     */
    public ErrorPeriodModel delete(ErrorPeriodModel model) throws JadeApplicationException, JadePersistenceException;

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
     * @see ch.globaz.al.business.models.rafam.ErrorPeriodModel
     */
    public ErrorPeriodModel read(String idErreurAnnonce) throws JadeApplicationException, JadePersistenceException;

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
    public ErrorPeriodSearchModel search(ErrorPeriodSearchModel search) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Met à jour les données de <code>ErrorPeriodModel</code> en persistance
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
     * @see ch.globaz.al.business.models.rafam.ErrorPeriodModel
     */
    public ErrorPeriodModel update(ErrorPeriodModel model) throws JadeApplicationException, JadePersistenceException;
}
