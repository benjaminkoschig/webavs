package globaz.pegasus.vb.parametre;

import ch.globaz.pegasus.business.constantes.EPCForfaitType;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaitsSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

/**
 * @author DMA
 * @date 11 nov. 2010
 */
public class PCZoneForfaitsLoyerListViewBean extends BJadePersistentObjectListViewBean {
    final EPCForfaitType type = EPCForfaitType.LOYER;
    SimpleZoneForfaitsSearch simpleZoneForfaitsSearch = null;

    public PCZoneForfaitsLoyerListViewBean() {
        super();
        simpleZoneForfaitsSearch = new SimpleZoneForfaitsSearch();
        simpleZoneForfaitsSearch.setForType(type.getCode().toString());
    }

    @Override
    public void find() throws Exception {
        simpleZoneForfaitsSearch = PegasusServiceLocator.getParametreServicesLocator().getSimpleZoneForfaitsService()
                .search(simpleZoneForfaitsSearch);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < simpleZoneForfaitsSearch.getSize() ? new PCZoneForfaitsLoyerViewBean(
                (SimpleZoneForfaits) simpleZoneForfaitsSearch.getSearchResults()[idx]) : new PCZoneForfaitsLoyerViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return simpleZoneForfaitsSearch;
    }

    /**
     * @return the simpleZoneForfaitsSearch
     */
    public SimpleZoneForfaitsSearch getSimpleZoneForfaitsSearch() {
        return simpleZoneForfaitsSearch;
    }

    /**
     * @param simpleZoneForfaitsSearch
     *            the simpleZoneForfaitsSearch to set
     */
    public void setSimpleZoneForfaitsSearch(SimpleZoneForfaitsSearch simpleZoneForfaitsSearch) {
        this.simpleZoneForfaitsSearch = simpleZoneForfaitsSearch;
    }

}
