package globaz.perseus.vb.parametres;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.parametres.SimpleZone;
import ch.globaz.perseus.business.models.parametres.SimpleZoneSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DMA
 * @date 11 nov. 2010
 */
public class PFSimpleZoneListViewBean extends BJadePersistentObjectListViewBean {
    SimpleZoneSearchModel simpleZoneSearchModel = null;

    public PFSimpleZoneListViewBean() {
        super();
        simpleZoneSearchModel = new SimpleZoneSearchModel();
    }

    @Override
    public void find() throws Exception {
        simpleZoneSearchModel = PerseusServiceLocator.getSimpleZoneService().search(simpleZoneSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < simpleZoneSearchModel.getSize() ? new PFSimpleZoneViewBean(
                (SimpleZone) simpleZoneSearchModel.getSearchResults()[idx]) : new PFSimpleZoneViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return simpleZoneSearchModel;
    }

    /**
     * @return the simpleZoneSearchModel
     */
    public SimpleZoneSearchModel getSimpleZoneSearchModel() {
        return simpleZoneSearchModel;
    }

    /**
     * @param simpleZoneSearchModel
     *            the simpleZoneSearchModel to set
     */
    public void setSimpleZoneSearchModel(SimpleZoneSearchModel simpleZoneSearchModel) {
        this.simpleZoneSearchModel = simpleZoneSearchModel;
    }

}
