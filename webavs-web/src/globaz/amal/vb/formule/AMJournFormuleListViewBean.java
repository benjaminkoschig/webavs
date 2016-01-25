package globaz.amal.vb.formule;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.journalisation.HistoriqueImportation;
import ch.globaz.amal.business.models.journalisation.HistoriqueImportationSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMJournFormuleListViewBean extends BJadePersistentObjectListViewBean {
    private HistoriqueImportationSearch historiqueImportationSearch = null;

    /**
	  *
	  */
    public AMJournFormuleListViewBean() {
        super();
        historiqueImportationSearch = new HistoriqueImportationSearch();
    }

    @Override
    public void find() throws Exception {
        // TODO Auto-generated method stub
        historiqueImportationSearch = AmalServiceLocator.getHistoriqueImportationService().search(
                historiqueImportationSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return (idx < historiqueImportationSearch.getSize() ? new AMJournFormuleViewBean(
                (HistoriqueImportation) historiqueImportationSearch.getSearchResults()[idx])
                : new AMJournFormuleViewBean());
        // return idx < this.contribuableSearch.getSize() ? new AMContribuableViewBean(
        // (Contribuable) this.contribuableSearch.getSearchResults()[idx])
        // : new AMContribuableViewBean();
    }

    public HistoriqueImportationSearch getHistoriqueImportationSearch() {
        return historiqueImportationSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        // TODO Auto-generated method stub
        return historiqueImportationSearch;
    }

    public void setHistoriqueImportationSearch(HistoriqueImportationSearch historiqueImportationSearch) {
        this.historiqueImportationSearch = historiqueImportationSearch;
    }

}
