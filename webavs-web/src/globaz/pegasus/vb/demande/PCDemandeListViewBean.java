package globaz.pegasus.vb.demande;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.demande.ListDemandes;
import ch.globaz.pegasus.business.models.demande.ListDemandesSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCDemandeListViewBean extends BJadePersistentObjectListViewBean {

    private ListDemandesSearch listDemandesSearch = null;

    public PCDemandeListViewBean() {
        super();
        listDemandesSearch = new ListDemandesSearch();
    }

    @Override
    public void find() throws Exception {
        listDemandesSearch = PegasusServiceLocator.getDemandeService().findDemandeForList(listDemandesSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < listDemandesSearch.getSize() ? new PCDemandeViewBean(
                (ListDemandes) listDemandesSearch.getSearchResults()[idx]) : new PCDemandeViewBean();
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return listDemandesSearch.getForDateNaissance();
    }

    /**
     * @return the demandeSearch
     */
    public ListDemandesSearch getListDemandesSearch() {
        return listDemandesSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return listDemandesSearch;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        listDemandesSearch.setForDateNaissance(forDateNaissance);
    }

    /**
     * @param demandeSearch
     *            the demandeSearch to set
     */
    public void setListDemandesSearch(ListDemandesSearch listDemandesSearch) {
        this.listDemandesSearch = listDemandesSearch;
    }

}
