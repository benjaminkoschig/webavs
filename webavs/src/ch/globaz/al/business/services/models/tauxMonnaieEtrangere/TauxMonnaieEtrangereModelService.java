package ch.globaz.al.business.services.models.tauxMonnaieEtrangere;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereSearchModel;

/**
 * Service de gestion de la persistance des données de TauxMonnaieEtrangereModel
 * 
 * @author PTA
 * 
 */
public interface TauxMonnaieEtrangereModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param tauxMonnaieSearch
     *            Modèle de recherche contenant les critères
     * @return nombre de copies correspondant aux critères de recherche
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @see ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel
     */

    public int count(TauxMonnaieEtrangereSearchModel tauxMonnaieSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Enregistre <code>TauxMonnaieEtrangereModel</code> en persistance
     * 
     * @param tauxMonnaieEtrangere
     *            taux à enregistrer
     * @return TauxMonnaieEtrangereModel enregistrée
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel
     * 
     */
    public TauxMonnaieEtrangereModel create(TauxMonnaieEtrangereModel tauxMonnaieEtrangere)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime le modèle <code>TauxMonnaieEtrangereModel</code> en persistance
     * 
     * @param tauxMonnaieEtrangere
     *            tau à supprimer
     * @return TauxMonnaieEtrangereMode taux supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel
     */
    public TauxMonnaieEtrangereModel delete(TauxMonnaieEtrangereModel tauxMonnaieEtrangere)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère les données du taux de monnaie étrangère correspondant à <code>idDossierModel</code>
     * 
     * @param idTauxMonnaierEtrangere
     *            identifiant du taux de la monnaie étrangère
     * @return TauxMonnaieEtrangerModel le modèle du taux chargée
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel
     */
    public TauxMonnaieEtrangereModel read(String idTauxMonnaierEtrangere) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effectue une recherche selon le modèle <code>TauxMonnaieEtrangereSearchModel</code> de recherche passé en
     * paramètre
     * 
     * @param tauxMonnaieEtrangereSearch
     *            modèle de recherche du taux
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TauxMonnaieEtrangereSearchModel search(TauxMonnaieEtrangereSearchModel tauxMonnaieEtrangereSearch)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met à jour le modèle <code>TauxMonnaieEtrangereModel</code> en persistance
     * 
     * @param tauxMonnaieEtrangere
     *            taux à mettre jour
     * @return TauxMonnaieEtrangereModel model du taux mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel
     */
    public TauxMonnaieEtrangereModel update(TauxMonnaieEtrangereModel tauxMonnaieEtrangere)
            throws JadeApplicationException, JadePersistenceException;

}
