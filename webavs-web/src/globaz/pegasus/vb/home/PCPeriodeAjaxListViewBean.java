package globaz.pegasus.vb.home;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtat;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtatSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCPeriodeAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private PeriodeServiceEtatSearch periodeSearch = null;

    /**
	 * 
	 */
    public PCPeriodeAjaxListViewBean() {
        super();
        periodeSearch = new PeriodeServiceEtatSearch();
    }

    @Override
    public void find() throws Exception {
        periodeSearch = PegasusServiceLocator.getHomeService().searchPeriode(periodeSearch);

    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < periodeSearch.getSize() ? new PCPeriodeAjaxViewBean(
                (PeriodeServiceEtat) periodeSearch.getSearchResults()[idx]) : new PCPeriodeAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return periodeSearch;
    }

    /**
     * @return the periodeSearch
     */
    public PeriodeServiceEtatSearch getPeriodeSearch() {
        return periodeSearch;
    }

    /**
     * @param periodeSearch
     *            the periodeSearch to set
     */
    public void setPeriodeSearch(PeriodeServiceEtatSearch periodeSearch) {
        this.periodeSearch = periodeSearch;
    }

}
