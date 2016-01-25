package globaz.perseus.vb.impotsource;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.impotsource.PeriodeImpotSourceException;
import ch.globaz.perseus.business.models.impotsource.PeriodeImpotSource;
import ch.globaz.perseus.business.models.impotsource.PeriodeImpotSourceSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFPeriodeImpotSourceListViewBean extends BJadePersistentObjectListViewBean {

    private PeriodeImpotSourceSearchModel periodeSearch;

    public PFPeriodeImpotSourceListViewBean() {
        super();
        setPeriodeSearch(new PeriodeImpotSourceSearchModel());
    }

    /**
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void find() throws PeriodeImpotSourceException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        setPeriodeSearch(PerseusServiceLocator.getPeriodeImpotSourceService().search(getPeriodeSearch()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < getPeriodeSearch().getSize() ? new PFPeriodeImpotSourceViewBean(
                (PeriodeImpotSource) getPeriodeSearch().getSearchResults()[idx]) : new PFPeriodeImpotSourceViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return periodeSearch;
    }

    public PeriodeImpotSourceSearchModel getPeriodeSearch() {
        return periodeSearch;
    }

    public String replaceBlanc(String str) {
        return (JadeStringUtil.isEmpty(str)) ? "&nbsp" : str;
    }

    public void setFor() {

    }

    public void setPeriodeSearch(PeriodeImpotSourceSearchModel periodeSearch) {
        this.periodeSearch = periodeSearch;
    }

}
