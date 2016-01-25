package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;

/**
 * Service de gestion de la persistance d'un dossier complexe
 * 
 * @author jts
 * @see ch.globaz.al.business.models.dossier.DossierComplexModel
 */
public interface DossierComplexModelService extends JadeApplicationService {
    /**
     * Créer un nouveau <code>dossierComplexModel</code> sur la base de celui passé en paramètre
     * 
     * @param dossierComplexModel
     *            la référence du clone
     * @return le dossierComplexModel cloné
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierComplexModel clone(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Enregistre <code>dossierComplexModel</code> en persistance
     * 
     * @param dossierComplexModel
     *            Dossier à enregistrer en persistance
     * @return Le modèle ajouté en persistance
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierComplexModel create(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime <code>dossierComplexModel</code> de la persistance
     * 
     * @param dossierComplexModel
     *            Dossier à supprimer de la persistance
     * @return Le dossier supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierComplexModel delete(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise le modèle
     * 
     * @param dossierComplexModel
     *            Le modèle à initialiser
     * @return Le modèle initialiser
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierComplexModel initModel(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Récupère les données du dossier correspondant à <code>idDossier</code>
     * 
     * @param idDossier
     *            Id du dossier à charger
     * @return Le modèle du dossier chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierComplexModel read(String idDossier) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche selon le modèle de recherche passé en paramètre
     * 
     * @param searchModel
     *            modèle de recherche de dossier
     * @return Résultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexSearchModel search(DossierComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effectue une recherche selon le modèle de recherche passé en paramètre, les valeurs de codes système sont
     * remplacées par leur libellé
     * 
     * @param searchModel
     *            modèle de recherche de lien dossier
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexSearchModel searchWithCsTranslated(DossierComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mets à jour les données de <code>dossierComplexModel</code> en persistance
     * 
     * @param dossierComplexModel
     *            Dossier à mettre à jour
     * @return Le dossier mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierComplexModel update(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException;
}
