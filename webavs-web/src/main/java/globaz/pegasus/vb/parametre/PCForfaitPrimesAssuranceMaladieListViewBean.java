package globaz.pegasus.vb.parametre;

import ch.globaz.pegasus.business.constantes.EPCForfaitType;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.parametre.ForfaitsPrimesAssuranceMaladie;
import ch.globaz.pegasus.business.models.parametre.ForfaitsPrimesAssuranceMaladieSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author DMA
 * @date 11 nov. 2010
 */
public class PCForfaitPrimesAssuranceMaladieListViewBean extends BJadePersistentObjectListViewBean {
    final EPCForfaitType type = EPCForfaitType.LAMAL;
    ForfaitsPrimesAssuranceMaladieSearch forfaitPrimesAssuranceMaladieSearch = null;

    public PCForfaitPrimesAssuranceMaladieListViewBean() {
        super();
        forfaitPrimesAssuranceMaladieSearch = new ForfaitsPrimesAssuranceMaladieSearch();
        forfaitPrimesAssuranceMaladieSearch.setForType(type.getCode().toString());
    }

    @Override
    public void find() throws Exception {
        forfaitPrimesAssuranceMaladieSearch = PegasusServiceLocator.getParametreServicesLocator()
                .getForfaitsPrimesAssuranceMaladieService().search(forfaitPrimesAssuranceMaladieSearch);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < forfaitPrimesAssuranceMaladieSearch.getSize() ? new PCForfaitPrimesAssuranceMaladieViewBean(
                (ForfaitsPrimesAssuranceMaladie) forfaitPrimesAssuranceMaladieSearch.getSearchResults()[idx])
                : new PCForfaitPrimesAssuranceMaladieViewBean();
    }

    /**
     * @return the forfaitPrimesAssuranceMaladieSearch
     */
    public ForfaitsPrimesAssuranceMaladieSearch getForfaitPrimesAssuranceMaladieSearch() {
        return forfaitPrimesAssuranceMaladieSearch;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return forfaitPrimesAssuranceMaladieSearch;
    }

    /**
     * @param forfaitPrimesAssuranceMaladieSearch
     *            the forfaitPrimesAssuranceMaladieSearch to set
     */
    public void setForfaitPrimesAssuranceMaladieSearch(
            ForfaitsPrimesAssuranceMaladieSearch forfaitPrimesAssuranceMaladieSearch) {
        this.forfaitPrimesAssuranceMaladieSearch = forfaitPrimesAssuranceMaladieSearch;
    }

}
