package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.dossier.DossierSearchModel;

/**
 * Service de gestion de la persistance des données des dossiers
 * 
 * @author jts
 */
public interface DossierModelService extends JadeApplicationService {

    /**
     * Créer un nouveau <code>dossierModel</code> sur la base de celui passé en paramètre
     * 
     * @param dossierModel
     *            la référence du clone
     * @return le dossierModel cloné
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierModel
     */
    public DossierModel clone(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Enregistre <code>dossierModel</code> en persistance
     * 
     * @param dossierModel
     *            Dossier à enregistrer en persistance
     * 
     * @return DossierModel Le dossier enregistré
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierModel
     */
    public DossierModel create(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime <code>dossierModel</code> de la persistance
     * 
     * @param dossierModel
     *            Dossier à supprimer de la persistance
     * 
     * @return DossierModel Le dossier supprimé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierModel
     */
    public DossierModel delete(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un nouveau modèle de dossier
     * 
     * @param dossierModel
     *            Le dossier à initialiser
     * @return Le dossier initialisé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @see ch.globaz.al.business.models.dossier.DossierModel
     */
    public DossierModel initModel(DossierModel dossierModel) throws JadeApplicationException;

    /**
     * Récupère les données du dossier correspondant à <code>idDossierModel</code>
     * 
     * @param idDossierModel
     *            Id du dossier à charger
     * 
     * @return DossierModel Le modèle du dossier chargé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierModel
     */
    public DossierModel read(String idDossierModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche selon le modèle de recherche passé en paramètre
     * 
     * @param searchModel
     *            modèle de recherche de dossier
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierSearchModel search(DossierSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Met à jour <code>dossierModel</code> en persistance
     * 
     * @param dossierModel
     *            Dossier à mettre à jour
     * 
     * @return DossierModel Le dossier mis à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierModel
     */
    public DossierModel update(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException;

}