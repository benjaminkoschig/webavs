/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModelSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author LFO
 * 
 */
public class AMChampformuleListViewBean extends BJadePersistentObjectListViewBean {
    private String idFormule = null;
    // private ChampFormuleSearch champformuleListSearch = null;
    private SignetListModelSearch signetListModelSearch = null;

    /**
	  *
	  */
    public AMChampformuleListViewBean() {
        super();
        signetListModelSearch = new SignetListModelSearch();
    }

    @Override
    public void find() throws Exception {
        signetListModelSearch = ENServiceLocator.getSignetListModelService().search(signetListModelSearch);
        if (signetListModelSearch.getSize() > 0) {
            idFormule = ((SignetListModel) signetListModelSearch.getSearchResults()[0]).getId();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return (idx < signetListModelSearch.getSize() ? new AMChampformuleViewBean(
                (SignetListModel) signetListModelSearch.getSearchResults()[idx]) : new AMChampformuleViewBean());

        /*
         * return (idx < this.champformuleListSearch.getSize() ? new AMChampformuleViewBean( (ChampFormule)
         * this.champformuleListSearch.getSearchResults()[idx]) : new AMChampformuleViewBean());
         */
    }

    public String getIdFormule() {
        return idFormule;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        // TODO Auto-generated method stub
        return signetListModelSearch;
    }

    public SignetListModelSearch getSignetListModelSearch() {
        return signetListModelSearch;
    }

    public void setIdFormule(String idFormule) {
        this.idFormule = idFormule;
    }

    public void setSignetListModelSearch(SignetListModelSearch signetListModelSearch) {
        this.signetListModelSearch = signetListModelSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */

    /**
     * @return the dossierSearch
     */
    /*
     * public ChampFormuleSearch getFormuleListSearch() { return this.champformuleListSearch; }
     * 
     * @Override protected JadeAbstractSearchModel getManagerModel() { return this.champformuleListSearch; }
     * 
     * 
     * public void setFormuleListSearch(ChampFormuleSearch champformuleListSearch) { this.champformuleListSearch =
     * champformuleListSearch; }
     */

}
