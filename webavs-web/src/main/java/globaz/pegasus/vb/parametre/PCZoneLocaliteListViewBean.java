package globaz.pegasus.vb.parametre;

import ch.globaz.pegasus.business.constantes.EPCForfaitType;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.parametre.ZoneLocalite;
import ch.globaz.pegasus.business.models.parametre.ZoneLocaliteSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class PCZoneLocaliteListViewBean extends BJadePersistentObjectListViewBean {
    final EPCForfaitType type = EPCForfaitType.LAMAL;
    ZoneLocaliteSearch zoneLocaliteSearch = null;

    public PCZoneLocaliteListViewBean() {
        super();
        zoneLocaliteSearch = new ZoneLocaliteSearch();
        zoneLocaliteSearch.setForType(type.getCode().toString());
    }

    @Override
    public void find() throws Exception {

        zoneLocaliteSearch = PegasusServiceLocator.getParametreServicesLocator().getZoneLocaliteService()
                .search(zoneLocaliteSearch);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < zoneLocaliteSearch.getSize() ? new PCZoneLocaliteViewBean(
                (ZoneLocalite) zoneLocaliteSearch.getSearchResults()[idx]) : new PCZoneLocaliteViewBean();
    }

    public String getDescLocalite(ZoneLocalite zoneLocalite) {
        String desc = "";
        if (zoneLocalite != null) {
            desc = zoneLocalite.getLocaliteSimpleModel().getNumPostal() + " - "
                    + zoneLocalite.getLocaliteSimpleModel().getLocalite();
        }
        return desc;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return zoneLocaliteSearch;
    }

    /**
     * @return the zoneLocaliteSearch
     */
    public ZoneLocaliteSearch getZoneLocaliteSearch() {
        return zoneLocaliteSearch;
    }

    /**
     * @param zoneLocaliteSearch
     *            the zoneLocaliteSearch to set
     */
    public void setZoneLocaliteSearch(ZoneLocaliteSearch zoneLocaliteSearch) {
        this.zoneLocaliteSearch = zoneLocaliteSearch;
    }
}
