package globaz.pegasus.vb.home;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.models.home.TypeChambreSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCTypeChambreListViewBean extends BJadePersistentObjectListViewBean {
    private TypeChambreSearch typeChambreSearch = null;

    public PCTypeChambreListViewBean() {
        super();
        typeChambreSearch = new TypeChambreSearch();
    }

    @Override
    public void find() throws Exception {
        typeChambreSearch.setForHomeTypeAdresse(PRTiersHelper.CS_ADRESSE_DOMICILE);
        typeChambreSearch = PegasusServiceLocator.getHomeService().searchTypeChambre(typeChambreSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < typeChambreSearch.getSize() ? new PCHomeTypeChambreViewBean(
                (TypeChambre) typeChambreSearch.getSearchResults()[idx]) : new PCHomeTypeChambreViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return typeChambreSearch;
    }

    /**
     * @return the typeChambreSearch
     */
    public TypeChambreSearch getTypeChambreSearch() {
        return typeChambreSearch;
    }

    /**
     * @param typeChambreSearch the typeChambreSearch to set
     */
    public void setTypeChambreSearch(TypeChambreSearch typeChambreSearch) {
        this.typeChambreSearch = typeChambreSearch;
    }

}
