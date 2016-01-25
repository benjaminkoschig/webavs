package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.LienDossierModel;
import ch.globaz.al.business.models.dossier.LienDossierSearchModel;

public interface LienDossierModelService extends JadeApplicationService {

    /**
     * Enregistre <code>lienDossierModel</code> en persistance
     * 
     * @param lienDossierModel
     *            lien à enregistrer en persistance
     * 
     * @return LienDossierModel Le lien enregistré
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.LienDossierModel
     */
    public LienDossierModel create(LienDossierModel lienDossierModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime <code>lienDossierModel</code> de la persistance
     * 
     * @param lienDossierModel
     *            lien à supprimer de la persistance
     * 
     * @return LienDossierModel Le lien supprimé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.LienDossierModel
     */
    public LienDossierModel delete(LienDossierModel lienDossierModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Récupère les données du lien correspondant à <code>idLienDossierModel</code>
     * 
     * @param idLienDossierModel
     *            Id du lien dossier à charger
     * 
     * @return LienDossierModel Le modèle du lien chargé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.LienDossierModel
     */
    public LienDossierModel read(String idLienDossierModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche selon le modèle de recherche passé en paramètre
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
    public LienDossierSearchModel search(LienDossierSearchModel searchModel) throws JadeApplicationException,
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
     * @see ch.globaz.al.business.models.dossier.LienDossierModel
     */
    public LienDossierModel update(LienDossierModel lienDossierModel) throws JadeApplicationException,
            JadePersistenceException;

}
