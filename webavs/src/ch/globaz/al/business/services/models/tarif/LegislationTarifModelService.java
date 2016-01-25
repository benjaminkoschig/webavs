package ch.globaz.al.business.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tarif.LegislationTarifModel;
import ch.globaz.al.business.models.tarif.LegislationTarifSearchModel;

/**
 * Service de gestion de persistance des données de LegislationTarif
 * 
 * @author PTA
 */
public interface LegislationTarifModelService extends JadeApplicationService {

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
    public int count(LegislationTarifSearchModel search) throws JadePersistenceException, JadeApplicationException;

    /**
     * Création d'une législation de tarif selon le modèle passé en paramètre
     * 
     * @param legislationTarifModel
     *            modèle à enregistrer
     * @return modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public LegislationTarifModel create(LegislationTarifModel legislationTarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * 
     * Lecture d'une législation de tarif selon l'id passé en paramètre
     * 
     * @param idLegislationtarifModel
     *            Id du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public LegislationTarifModel read(String idLegislationtarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche d'une législation de tarif selon le modèle passé en paramètre
     * 
     * @param legislationTarifSearch
     *            modèle contenant les critères de recherche
     * @return modèle contenant le résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public LegislationTarifSearchModel search(LegislationTarifSearchModel legislationTarifSearch)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise à jour d'une législation de tarif selon le modèle passé en paramètre
     * 
     * @param legislationTarifModel
     *            contenant les données mise à jour
     * @return le modèle après mise à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public LegislationTarifModel update(LegislationTarifModel legislationTarifModel) throws JadeApplicationException,
            JadePersistenceException;
}
