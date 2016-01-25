package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;

/**
 * service de gestion des données inhérentes à RecapitulatifEntreprise dans Prestation
 * 
 * @author PTA
 */
public interface RecapitulatifEntrepriseModelService extends JadeApplicationService {

    /**
     * Retourne le nombre de récap trouvées par le modèle de recherche
     * 
     * @param recapSearch
     *            modèle contenant les critères de recherche
     * @return nombre d'enregistrement correspondant à la recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public int count(RecapitulatifEntrepriseSearchModel recapSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Création d'un récapitulatif entreprise selon le modèle passé en paramètre
     * 
     * @param recapEntrepriseModel
     *            modèle à enregistrer
     * @return le modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseModel create(RecapitulatifEntrepriseModel recapEntrepriseModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Suppression d'un récapitulatif entreprise selon le modèle passé en paramètre
     * 
     * @param recapEntrepriseModel
     *            le modèle à supprimer
     * @return le modèle supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseModel delete(RecapitulatifEntrepriseModel recapEntrepriseModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Efface un récapitulatif entreprise selon le modèle passé en paramètre si il ne contient n prestations où n=
     * <code>size</code>
     * 
     * @param idRecap
     *            le modèle à (éventuellement) effacer
     * @param size
     *            le nombre de prestations contenues dans la récap
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deleteIfSizeEquals(String idRecap, int size) throws JadeApplicationException, JadePersistenceException;

    /**
     * Lecture d'un récapitulatif entreprise selon son identifiant passé en paramètre
     * 
     * @param idRecapEntrepriseModel
     *            id du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseModel read(String idRecapEntrepriseModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effectue une recherche sur <code>recapSearch</code>
     * 
     * @param recapSearch
     *            modèle contenant les critères de recherche
     * @return modèle contenant le résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseSearchModel search(RecapitulatifEntrepriseSearchModel recapSearch)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * Mise à jour d'un récapitulatif entreprise selon le modèle passé en paramètre
     * 
     * @param recapEntrepriseModel
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseModel update(RecapitulatifEntrepriseModel recapEntrepriseModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche sur <code>recapSearch</code>, à appeler uniquement depuis les écrans dans tablib
     * <ct:widget
     * 
     * @param recapSearch
     *            modèle contenant les critères de recherche
     * @return modèle contenant le résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseSearchModel widgetFind(RecapitulatifEntrepriseSearchModel recapSearch)
            throws JadeApplicationException, JadePersistenceException;
}
