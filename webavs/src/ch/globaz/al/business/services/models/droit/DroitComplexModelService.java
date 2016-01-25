package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.droit.DroitModel;

/**
 * Service de gestion de la persistance des droits
 * 
 * @author jts
 * @see ch.globaz.al.business.models.droit.DroitComplexModel
 */
public interface DroitComplexModelService extends JadeApplicationService {

    /**
     * Créer un nouveau <code>droitComplexModel</code> sur la base de celui passé en paramètre
     * 
     * @param droitComplexModel
     *            la référence du clone
     * @param idDossier
     *            le dossier auquel le droit cloné appartiendra
     * @return le droitComplexModel cloné
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexModel clone(DroitComplexModel droitComplexModel, String idDossier)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Crée une copie du modèle passé en paramètre. Seul le {@link DroitModel} contenu dans modèle complexe est
     * réellement copié. Le modèle de l'enfant est une référence du modèle d'origine. Le tiers bénéficiaire n'est pas
     * copié
     * 
     * @param droit
     *            droit à copier
     * @return la copie du droit
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public DroitComplexModel copie(DroitComplexModel droit) throws JadeApplicationException;

    /**
     * Enregistre <code>droitComplexModel</code> en persistance
     * 
     * @param droitComplexModel
     *            Droit à enregistrer en persistance
     * @return Le droit enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexModel create(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime <code>droitComplexModel</code> de la persistance
     * 
     * @param droitComplexModel
     *            Droit à supprimer de la persistance
     * @return Le droit supprimé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexModel delete(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise <code>droitComplexModel</code>
     * 
     * @param droitComplexModel
     *            Le droit à initialiser
     * @return Le droit initialisé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexModel initModel(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Récupère les données correspondant au droit <code>idDroit</code>
     * 
     * @param idDroit
     *            Id du droit à charger
     * @return Le modèle du droit chargé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexModel read(String idDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les droits correspondant au modèle de recherche <code>droitComplexComplexSearchModel</code>
     * 
     * @param droitComplexSearchModel
     *            Modèle de recherche de droit contenant les critères de recherche souhaités
     * @return résultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexSearchModel search(DroitComplexSearchModel droitComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met à jour <code>droitComplexModel</code> de la persistance
     * 
     * @param droitComplexModel
     *            Le droit à mettre à jour
     * @return Le droit mis à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexModel update(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException;

}
