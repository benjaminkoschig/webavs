package globaz.eform.vb.suivi;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class GFSuiviListViewBean  extends BJadePersistentObjectListViewBean {

    private GFDaDossierSearch suiviSearch;

    public GFSuiviListViewBean() {
        super();
        suiviSearch = new GFDaDossierSearch();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return suiviSearch;
    }

    @Override
    public void find() throws Exception {
        suiviSearch.setWhereKey("suivi");
        suiviSearch = GFEFormServiceLocator.getGFDaDossierDBService().search(suiviSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < suiviSearch.getSize() ? new GFSuiviViewBean(
                (GFDaDossierModel) suiviSearch.getSearchResults()[idx]) : new GFSuiviViewBean();
    }

    public String getLikeNss() {
        return suiviSearch.getLikeNss();
    }

    public void setLikeNss(String likeNss) {
        suiviSearch.setLikeNss(likeNss);
    }

    public String getByCaisse() {
        return suiviSearch.getByCaisse();
    }

    public void setByCaisse(String byCaisse) {
        suiviSearch.setByCaisse(byCaisse);
    }

    public String getByType() {
        return suiviSearch.getByType();
    }

    public void setByType(String byType) {
        suiviSearch.setByType(byType);
    }

    public String getByStatus() {
        return suiviSearch.getByStatus();
    }

    public void setByStatus(String byStatus) {
        suiviSearch.setByStatus(byStatus);
    }

    public String getByGestionnaire() {
        return suiviSearch.getByGestionnaire();
    }

    public void setByGestionnaire(String byGestionnaire) {
        suiviSearch.setByGestionnaire(byGestionnaire);
    }
}
