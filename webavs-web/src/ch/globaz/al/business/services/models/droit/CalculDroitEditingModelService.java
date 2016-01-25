/**
 * 
 */
package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.droit.CalculDroitEditingModel;
import ch.globaz.al.business.models.droit.CalculDroitEditingSearchModel;

/**
 * @author pta
 * 
 */
public interface CalculDroitEditingModelService extends JadeApplicationService {

    /**
     * Enregistre <code>calculDroitModel</code> en persistance
     * 
     * @param calculDroitModel
     *            CalculDroitEditingModel à enregistrer
     * @return CalculDroitBusinessModel calculDroitModel enregistrée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.CalculDroitEditingModel.CalculDroitBusinessModel
     */
    public CalculDroitEditingModel create(CalculDroitEditingModel calculDroitModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime <code>calculDroitModel</code> de la persistance
     * 
     * @param calculDossierModel
     *            calcul droit à supprimer
     * @return CalculDroitEditingModel calculDroitModel supprimée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CalculDossierModel
     */
    public CalculDroitEditingModel delete(CalculDroitEditingModel calculDroitModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche
     * 
     * @param calculDroitSearchModel
     *            modèle pour la recherche
     * @return modèle de recherche de calcul d'un droit
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CalculDroitEditingSearchModel search(CalculDroitEditingSearchModel calculDroitSearchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met à jour les données de <code>calculDroitModel</code> en persistance
     * 
     * @param CalculDroitEditingModel
     *            Calcul dossier mettre à jour
     * @return CalculDroitEditingModel copie mise à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.CalculDroitEditingModel.CalculDroitBusinessModel
     */
    public CalculDroitEditingModel update(CalculDroitEditingModel calculDroitModel) throws JadeApplicationException,
            JadePersistenceException;

}
