package globaz.pegasus.vb.home;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.models.home.PrixChambreSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCPrixChambreListViewBean extends BJadePersistentObjectListViewBean {
    private PrixChambreSearch prixChambreSearch = null;

    public PCPrixChambreListViewBean() {
        super();
        prixChambreSearch = new PrixChambreSearch();
    }

    @Override
    public void find() throws Exception {
        prixChambreSearch = PegasusServiceLocator.getHomeService().findPrixChambre(prixChambreSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < prixChambreSearch.getSize() ? new PCPrixChambreViewBean(
                (PrixChambre) prixChambreSearch.getSearchResults()[idx]) : new PCPrixChambreViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return prixChambreSearch;
    }

    /**
     * @return the prixChambreSearch
     */
    public PrixChambreSearch getPrixChambreSearch() {
        return prixChambreSearch;
    }

    /**
     * @param prixChambreSearch
     *            the prixChambreSearch to set
     */
    public void setPrixChambreSearch(PrixChambreSearch prixChambreSearch) {
        this.prixChambreSearch = prixChambreSearch;
    }

}
