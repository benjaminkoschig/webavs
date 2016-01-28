/**
 * 
 */
package globaz.al.vb.parametres;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModelSearch;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean g�rant la recherche sur les �l�ments EnvoiTemplateComplexModel
 * 
 * @author dhi
 * 
 */
public class ALFormulesListViewBean extends BJadePersistentObjectListViewBean {

    /**
     * Mod�le de recherche pour les templates envoi
     */
    private EnvoiTemplateComplexModelSearch searchModel = null;

    /**
     * Default constructor
     */
    public ALFormulesListViewBean() {
        super();
        searchModel = new EnvoiTemplateComplexModelSearch();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        searchModel = ALServiceLocator.getEnvoiTemplateComplexService().search(searchModel);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new ALFormulesViewBean(
                (EnvoiTemplateComplexModel) searchModel.getSearchResults()[idx]) : new ALFormulesViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public EnvoiTemplateComplexModelSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(EnvoiTemplateComplexModelSearch searchModel) {
        this.searchModel = searchModel;
    }

}
