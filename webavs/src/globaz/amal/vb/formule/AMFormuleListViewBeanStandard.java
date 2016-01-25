/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMFormuleListViewBeanStandard extends BJadePersistentObjectListViewBean {
    private FormuleListSearch formuleListSearch = null;

    /**
	  *
	  */
    public AMFormuleListViewBeanStandard() {
        super();
        formuleListSearch = new FormuleListSearch();
    }

    @Override
    public void find() throws Exception {
        // this.formuleListSearch = AmalServiceLocator.getFormuleListService().search(this.formuleListSearch);
        formuleListSearch = ENServiceLocator.getFormuleListService().search(formuleListSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return (idx < formuleListSearch.getSize() ? new AMFormuleViewBeanStandard(
                (FormuleList) formuleListSearch.getSearchResults()[idx]) : new AMFormuleViewBeanStandard());
        // return idx < this.contribuableSearch.getSize() ? new AMContribuableViewBean(
        // (Contribuable) this.contribuableSearch.getSearchResults()[idx])
        // : new AMContribuableViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */

    /**
     * @return the dossierSearch
     */
    public FormuleListSearch getFormuleListSearch() {
        return formuleListSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return formuleListSearch;
    }

    /**
     * @param dossierSearch
     *            the dossierSearch to set
     */
    public void setFormuleListSearch(FormuleListSearch formuleListSearch) {
        this.formuleListSearch = formuleListSearch;
    }

}
