package globaz.al.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.models.droit.EnfantComplexModel;
import ch.globaz.al.business.models.droit.EnfantComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant le modèle de recherche d'enfants, utilisé dans une recherche ajax pour voir si un enfant correspond
 * au nss
 * 
 * @author GMO
 * 
 */
public class ALEnfantListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Le modèle de recherche d'enfants
     */
    private EnfantComplexSearchModel enfantComplexSearchModel = null;
    /**
     * Liste indiquant si l'/les enfant/s trouvé/s sont déjà actifs dans un autre dossier 1er élément de la liste
     * correspond au 1er résultat du modèle de recherche.
     */
    private List isActifListResults = null;

    /**
     * Constructeur du listViewBean
     */
    public ALEnfantListViewBean() {
        super();
        enfantComplexSearchModel = new EnfantComplexSearchModel();
        isActifListResults = new ArrayList();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        enfantComplexSearchModel = ALServiceLocator.getEnfantComplexModelService().search(enfantComplexSearchModel);
        isActifListResults.clear();
        for (int i = 0; i < getEnfantComplexSearchModel().getSize(); i++) {
            isActifListResults.add(new Integer(ALServiceLocator.getEnfantBusinessService().getNombreDroitsActifs(
                    getEnfantComplexResult(i).getId())));
        }
    }

    /**
     * Retourne le modèle se trouvant à la position idx parmi les résultats de la recherche
     * 
     * @param idx
     *            Position du modèle dans la liste des résultats
     * @return le modèle EnfantComplexModel voulu ou un modèle EnfantComplexModel vide si position non trouvée
     */
    public EnfantComplexModel getEnfantComplexResult(int idx) {
        return idx < getCount() ? (EnfantComplexModel) getManagerModel().getSearchResults()[idx]
                : new EnfantComplexModel();
    }

    /**
     * @return enfantComplexSearchModel Le modèle de recherche enfant
     */
    public EnfantComplexSearchModel getEnfantComplexSearchModel() {
        return enfantComplexSearchModel;
    }

    /**
     * @return isActifListResults La liste indiquant si les allocataires résultant sont actifs dans d'autres dossiers
     */
    public List getIsActifListResults() {
        return isActifListResults;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return enfantComplexSearchModel;
    }

    /**
     * @param enfantComplexSearchModel
     *            Le modèle de recherche enfants
     * 
     */
    public void setEnfantComplexSearchModel(EnfantComplexSearchModel enfantComplexSearchModel) {
        this.enfantComplexSearchModel = enfantComplexSearchModel;
    }

    /**
     * @param isActifListResults
     *            La liste indiquant si les enfants résultant sont actifs dans d'autres droits
     */
    public void setIsActifListResults(List isActifListResults) {
        this.isActifListResults = isActifListResults;
    }

}
