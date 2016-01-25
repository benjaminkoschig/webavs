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
     *            CalculDroitEditingModel � enregistrer
     * @return CalculDroitBusinessModel calculDroitModel enregistr�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     *            calcul droit � supprimer
     * @return CalculDroitEditingModel calculDroitModel supprim�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     *            mod�le pour la recherche
     * @return mod�le de recherche de calcul d'un droit
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CalculDroitEditingSearchModel search(CalculDroitEditingSearchModel calculDroitSearchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met � jour les donn�es de <code>calculDroitModel</code> en persistance
     * 
     * @param CalculDroitEditingModel
     *            Calcul dossier mettre � jour
     * @return CalculDroitEditingModel copie mise � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.CalculDroitEditingModel.CalculDroitBusinessModel
     */
    public CalculDroitEditingModel update(CalculDroitEditingModel calculDroitModel) throws JadeApplicationException,
            JadePersistenceException;

}
