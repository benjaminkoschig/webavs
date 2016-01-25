package ch.globaz.al.business.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tarif.CritereTarifModel;
import ch.globaz.al.business.models.tarif.CritereTarifSearchModel;

/**
 * 
 * Service de gestion de persistance des données de CritereTarif
 * 
 * @author PTA
 */
public interface CritereTarifModelService extends JadeApplicationService {

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
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(CritereTarifSearchModel search) throws JadePersistenceException, JadeApplicationException;

    /**
     * Création de critère tarif selon le modèle passé en paramètre
     * 
     * @param critereTarifModel
     *            modèle à enregistrer
     * @return modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CritereTarifModel create(CritereTarifModel critereTarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'un critère tarif selon l'id passé en paramètre
     * 
     * @param idCritereTarifModel
     *            Id du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CritereTarifModel read(String idCritereTarifModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche d'un critère tarif selon le modèle passé en paramètre
     * 
     * @param critereTarifSearch
     *            modèle contenant les critères de recherche
     * @return modèle contenant le résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CritereTarifSearchModel search(CritereTarifSearchModel critereTarifSearch) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mise à jour d'un critère tarif selon le modèle passé en paramètre
     * 
     * @param critereTarifModel
     *            contenant les données mise à jour
     * @return le modèle après mise à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CritereTarifModel update(CritereTarifModel critereTarifModel) throws JadeApplicationException,
            JadePersistenceException;
}
