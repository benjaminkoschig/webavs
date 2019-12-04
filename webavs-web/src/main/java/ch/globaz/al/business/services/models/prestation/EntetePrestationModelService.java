package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.List;

/**
 * Service de gestion de persistance des données des en-tête de prestation
 * 
 * @author PTA
 */
public interface EntetePrestationModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param enteteSearch
     *            Modèle contenant les critères de recherche
     * @return Nombre d'en-têtes correspondant aux critères de recherche du modèle
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public int count(EntetePrestationSearchModel enteteSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Création d'un entête de prestation selon le modèle passée en paramètre
     * 
     * @param entetePrestModel
     *            Le modèle à enregistrer
     * @return Le modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationModel create(EntetePrestationModel entetePrestModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Suppression d'une entête de prestation selon le modèle passé en paramètre
     * 
     * @param entetePrestModel
     *            Le modèle à supprimer
     * @return Le modèle supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationModel delete(EntetePrestationModel entetePrestModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les prestations liées à la récap dont l'id est passé en param La récap elle-même ne sera pas supprimée,
     * puisque ce service est appelé depuis le service delete de récap
     * 
     * @param idRecap
     *            id de la récap
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deleteForIdRecap(String idRecap) throws JadePersistenceException, JadeApplicationException;

    /**
     * Lecture d'une entête de prestation selon son id passé en paramètre
     * 
     * @param idEntetePrestModel
     *            Id de l'en-tête à charger
     * @return EntetePrestaonModel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationModel read(String idEntetePrestModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche les entêtes de prestation répondant aux critères définis dans le modèle de recherche
     * 
     * @param entetePrestationSearchModel
     *            Le modèle de recherche
     * @return Le modèle de recherche contenant les résultats
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationSearchModel search(EntetePrestationSearchModel entetePrestationSearchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise à jour d'une entête de prestation selon le modèle passé en paramètre
     * 
     * @param entetePrestModel
     *            Le modèle à mettre à jour
     * @return EntetePrestationModel Le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationModel update(EntetePrestationModel entetePrestModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Métohde permettant de rechercher les prestations comptabilisées d'un dossier pour une période donnée
     *
     * @param idDossier L'ID du dossier contenant les prestations
     * @param periode La période de la prestation (MM.yyyy)
     * @return Le modèle de recherche contentant les prestations d'une période donné d'un dossier
     */
    public List<EntetePrestationModel> searchEntetesPrestationsComptabilisees(String idDossier, String periode) throws JadeApplicationException, JadePersistenceException;
}
