package ch.globaz.al.business.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tarif.PrestationTarifModel;
import ch.globaz.al.business.models.tarif.PrestationTarifSearchModel;

/**
 * Service de gestion de persistance des données de PrestationTarif
 * 
 * @author PTA
 */
public interface PrestationTarifModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param search
     *            modèle contenant les critères de recherche
     * @return nombre de lignes retourné par la recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public int count(PrestationTarifSearchModel search) throws JadePersistenceException, JadeApplicationException;

    /**
     * Création d'une prestation de tarif selon le modèle passé en paramètre
     * 
     * @param prestationTarifModel
     *            modèle à enregistrer
     * @return modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PrestationTarifModel create(PrestationTarifModel prestationTarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'une prestation de tarif selon l'identifiant passé en paramètre
     * 
     * @param idPrestationTarifModel
     *            Id du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PrestationTarifModel read(String idPrestationTarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche d'une prestation de tarif selon le modèle passé en paramètre
     * 
     * @param search
     *            modèle contenant les critères de recherche
     * @return modèle contenant le résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PrestationTarifSearchModel search(PrestationTarifSearchModel search) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mis à jour d'une prestation de tarif selon le modèle passé en paramètre
     * 
     * @param prestationTarifModel
     *            modèle contenant les données mise à jour
     * @return le modèle après mise à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PrestationTarifModel update(PrestationTarifModel prestationTarifModel) throws JadeApplicationException,
            JadePersistenceException;

}
