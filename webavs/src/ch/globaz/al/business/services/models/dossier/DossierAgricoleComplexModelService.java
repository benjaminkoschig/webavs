package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel;

/**
 * Service de gestion de la persistance d'un dossier complexe agricole
 * 
 * @author jts
 */
public interface DossierAgricoleComplexModelService extends JadeApplicationService {

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
     * @see ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel
     */
    public DossierAgricoleComplexModel create(DossierAgricoleComplexModel dossierComplexModel)
            throws JadeApplicationException, JadePersistenceException;

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
     * @see ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel
     */
    public DossierAgricoleComplexModel delete(DossierAgricoleComplexModel dossierComplexModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère le dossier correspondant à <code>idDossier</code> depuis la persistance
     * 
     * @param idDossier
     *            Id du dossier à charger
     * 
     * @return Le modèle du dossier chargé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel
     */
    public DossierAgricoleComplexModel read(String idDossier) throws JadeApplicationException, JadePersistenceException;

    /**
     * Met à jour <code>dossierComplexModel</code> en persistance
     * 
     * @param dossierComplexModel
     *            Dossier à mettre à jour
     * 
     * @return Le dossier mis à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel
     */
    public DossierAgricoleComplexModel update(DossierAgricoleComplexModel dossierComplexModel)
            throws JadeApplicationException, JadePersistenceException;
}
