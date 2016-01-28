package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.models.droit.DroitSearchModel;

/**
 * service de gestion de la persitance des données liées au droit
 * 
 * @author PTA
 */
public interface DroitModelService extends JadeApplicationService {

    /**
     * Créer un nouveau <code>droitModel</code> sur la base de celui passé en paramètre
     * 
     * /!\ ATTENTION /!\ : NE PAS UTILISER DANS UN AUTRE CADRE QUE LA COPIE DE DOSSIER ACTUELLEMENT EN PLACE, CETTE
     * METHODE NE CREE PAS UN VERITABLE CLONE, IL Y A UN RISQUE DE
     * MODIFICATION DU MODELE DE REFERENCE, UTILISER {@link DroitModelService#copie(DroitModel)}
     * 
     * @param droitModel
     *            - la référence du clone
     * @param idDossier
     *            - le dossier auquel le droit appartient
     * @return - le droitModel cloné
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @deprecated NE PAS UTILISER CETTE METHODE NE CREE PAS UN VERITABLE CLONE, IL Y A UN RISQUE DE
     *             MODIFICATION DU MODELE PAR REFERENCE, UTILISER {@link DroitModelService#copie(DroitModel)}
     */
    @Deprecated
    public DroitModel clone(DroitModel droitModel, String idDossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Copie les tous les champs d'un droit excepté les champs spy et l'id du droit
     * 
     * @param droitModel
     *            correspond au droit à copier
     * @return droitModel copié
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public DroitModel copie(DroitModel droitModel) throws JadeApplicationException;

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param droitSearch
     *            le modèle de recherche contenant les critères de sélection
     * @return le nombre de droits retournés par le modèle de recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */

    public int count(DroitSearchModel droitSearch) throws JadePersistenceException, JadeApplicationException;

    /**
     * Création d'un droit en persistance
     * 
     * @param droitModel
     *            le droit à enregistrer
     * @return DroitModel le droit enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DroitModel create(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Suppression d'un droit selon le modèle passé en paramètre
     * 
     * @param droitModel
     *            le modèle à supprimer
     * @return DroitModel le modèle supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DroitModel delete(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche des droits actifs par rapport à un dossier et une date spécifique
     * 
     * @param idDossier
     *            id du dossier dans lequel les droits devront être recherchés
     * @param date
     *            date à laquelle les droits sont contrôlés afin de définir si ils sont actifs
     * @return List<String> des droits actifs
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public List<DroitModel> findActifs(String idDossier, String date)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException;

    /**
     * Initialisation d'un droit
     * 
     * @param droitModel
     *            le droit à initialiser
     * @return le droit initialisé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DroitModel initModel(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Lecture d'un droit en fonction de son id passé en paramètre
     * 
     * @param idDroitModel
     *            id du droit à charger
     * @return DroitModel le droit chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DroitModel read(String idDroitModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche de droits
     * 
     * @param droitSearchModel
     *            le modèle de recherche
     * @return les résultats chargés dans le modèle de recherche DroitSearchModel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DroitSearchModel search(DroitSearchModel droitSearchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mise à jour d'un droit en persitance
     * 
     * @param droitModel
     *            le modèle à mettre à jour
     * @return DroitModel le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DroitModel update(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException;
}
