package globaz.al.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.models.droit.EnfantComplexModel;
import ch.globaz.al.business.models.droit.EnfantComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean g�rant le mod�le de recherche d'enfants, utilis� dans une recherche ajax pour voir si un enfant correspond
 * au nss
 * 
 * @author GMO
 * 
 */
public class ALEnfantListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Le mod�le de recherche d'enfants
     */
    private EnfantComplexSearchModel enfantComplexSearchModel = null;
    /**
     * Liste indiquant si l'/les enfant/s trouv�/s sont d�j� actifs dans un autre dossier 1er �l�ment de la liste
     * correspond au 1er r�sultat du mod�le de recherche.
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
     * Retourne le mod�le se trouvant � la position idx parmi les r�sultats de la recherche
     * 
     * @param idx
     *            Position du mod�le dans la liste des r�sultats
     * @return le mod�le EnfantComplexModel voulu ou un mod�le EnfantComplexModel vide si position non trouv�e
     */
    public EnfantComplexModel getEnfantComplexResult(int idx) {
        return idx < getCount() ? (EnfantComplexModel) getManagerModel().getSearchResults()[idx]
                : new EnfantComplexModel();
    }

    /**
     * @return enfantComplexSearchModel Le mod�le de recherche enfant
     */
    public EnfantComplexSearchModel getEnfantComplexSearchModel() {
        return enfantComplexSearchModel;
    }

    /**
     * @return isActifListResults La liste indiquant si les allocataires r�sultant sont actifs dans d'autres dossiers
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
     *            Le mod�le de recherche enfants
     * 
     */
    public void setEnfantComplexSearchModel(EnfantComplexSearchModel enfantComplexSearchModel) {
        this.enfantComplexSearchModel = enfantComplexSearchModel;
    }

    /**
     * @param isActifListResults
     *            La liste indiquant si les enfants r�sultant sont actifs dans d'autres droits
     */
    public void setIsActifListResults(List isActifListResults) {
        this.isActifListResults = isActifListResults;
    }

}
