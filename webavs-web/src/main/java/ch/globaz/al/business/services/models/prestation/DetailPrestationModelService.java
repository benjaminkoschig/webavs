package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.DetailPrestationSearchModel;

/**
 * Service de gestion de persistance des données des detailPrestation de prestation
 * 
 * @author PTA
 */
public interface DetailPrestationModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param detailPrestSearch
     *            modèle contenant les critères de recherche
     * @return nombre d'enregistrement correspondant à la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(DetailPrestationSearchModel detailPrestSearch) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * 
     * Création d'un détail de prestation selon le modèle passé en paramètre
     * 
     * @param detailPrestationModel
     *            modèle à enregistrer
     * @return le modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationModel create(DetailPrestationModel detailPrestationModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Suppression d'un détail de prestation selon le modèle passé en paramètre
     * 
     * @param detailPrestationModel
     *            le modèle à supprimer
     * @return le modèle supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationModel delete(DetailPrestationModel detailPrestationModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les détails liés à l'en-tête dont l'id est passé en paramètre
     * 
     * @param idEntete
     *            id de l'en-tête
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deleteForIdEntete(String idEntete) throws JadePersistenceException, JadeApplicationException;

    /**
     * Lecture d'un détail de prestation selon son id passé en paramètre
     * 
     * @param idDetailPrestationModel
     *            id du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationModel read(String idDetailPrestationModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche les détail de prestation correspondant aux critères définis dans le modèle de recherche
     * 
     * @param searchModel
     *            Le modèle de recherche
     * @return Le modèle de recherche contenant les résultats
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationSearchModel search(DetailPrestationSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mise d'un détail de prestation selon le modèle passé en paramètre
     * 
     * @param detailPrestationModel
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationModel update(DetailPrestationModel detailPrestationModel) throws JadeApplicationException,
            JadePersistenceException;
}
