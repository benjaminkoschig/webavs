package ch.globaz.al.business.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tarif.CategorieTarifModel;
import ch.globaz.al.business.models.tarif.CategorieTarifSearchModel;

/**
 * Service de gestion de persistance des données de CategorieTarif
 * 
 * @author PTA
 */
public interface CategorieTarifModelService extends JadeApplicationService {

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

    public int count(CategorieTarifSearchModel search) throws JadePersistenceException, JadeApplicationException;

    /**
     * Création d'une catégorie tarif selon le modèle passé en paramètre
     * 
     * @param categorieTarifModel
     *            modèle à enregistrer
     * @return modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CategorieTarifModel create(CategorieTarifModel categorieTarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'une catégorie de tarif passé en paramètre selon l'id passé en paramètre
     * 
     * @param idCategorieTarifModel
     *            Id du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CategorieTarifModel read(String idCategorieTarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche d'une catégorie tarif selon le modèle passé en paramètre
     * 
     * @param categorieTarifSearch
     *            modèle contenant les critères de recherche
     * @return modèle contenant le résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CategorieTarifSearchModel search(CategorieTarifSearchModel categorieTarifSearch)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise à jour d'une catégorie de tarif selon le modèle passé en paramètre
     * 
     * @param categorieTarifModel
     *            contenant les données mise à jour
     * @return le modèle après mise à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CategorieTarifModel update(CategorieTarifModel categorieTarifModel) throws JadeApplicationException,
            JadePersistenceException;

}