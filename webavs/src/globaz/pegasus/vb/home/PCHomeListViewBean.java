package globaz.pegasus.vb.home;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.home.HomeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCHomeListViewBean extends BJadePersistentObjectListViewBean {
    private HomeSearch homeSearch = null;

    public PCHomeListViewBean() {
        super();
        homeSearch = new HomeSearch();
    }

    @Override
    public void find() throws Exception {
        homeSearch = PegasusServiceLocator.getHomeService().search(homeSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < homeSearch.getSize() ? new PCHomeViewBean((Home) homeSearch.getSearchResults()[idx])
                : new PCHomeViewBean();
    }

    /**
     * @return the homeSearch
     */
    public HomeSearch getHomeSearch() {
        return homeSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return homeSearch;
    }

    /**
     * @param homeSearch
     *            the homeSearch to set
     */
    public void setHomeSearch(HomeSearch homeSearch) {
        this.homeSearch = homeSearch;
    }

}
