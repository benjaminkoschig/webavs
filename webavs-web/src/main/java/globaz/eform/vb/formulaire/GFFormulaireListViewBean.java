package globaz.eform.vb.formulaire;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.search.GFFormulaireSearch;
import ch.globaz.eform.utils.GFSessionDataContainerHelper;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

import javax.servlet.http.HttpSession;

public class GFFormulaireListViewBean extends BJadePersistentObjectListViewBean {

    private GFFormulaireSearch formulaireSearch;

    public GFFormulaireListViewBean() {
        super();
        formulaireSearch = new GFFormulaireSearch();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return formulaireSearch;
    }


    @Override
    public void find() throws Exception {
        formulaireSearch.setWhereKey(GFFormulaireSearch.WHERE_DEFINITION_FORMULAIRE);
        formulaireSearch.setOrderKey(getOrderBy());
        formulaireSearch = GFEFormServiceLocator.getGFEFormService().search(formulaireSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < formulaireSearch.getSize() ? new GFFormulaireViewBean(
                (GFFormulaireModel) formulaireSearch.getSearchResults()[idx]) : new GFFormulaireViewBean();
    }

    public void putSearchModelOnSession(HttpSession session) {
        GFSessionDataContainerHelper.putGFFormulaireSearchOnSession(session, formulaireSearch);
    }

    public String getByGestionnaire() {
        return formulaireSearch.getByGestionnaire();
    }

    public void setByGestionnaire(String byGestionnaire) {
        formulaireSearch.setByGestionnaire(byGestionnaire);
    }

    public String getByStatus() {
        return formulaireSearch.getByStatus();
    }

    public void setByStatus(String byStatus) {
        formulaireSearch.setByStatus(byStatus);
    }

    public String getByType() {
        return formulaireSearch.getByType();
    }

    public void setByType(String byType) {
        formulaireSearch.setByType(byType);
    }

    public String getByDate() {
        return formulaireSearch.getByDate();
    }

    public void setByDate(String byDate) {
        formulaireSearch.setByDate(byDate);
    }

    public String getByMessageId() {
        return formulaireSearch.getByMessageId();
    }

    public void setByMessageId(String byMessageId) {
        formulaireSearch.setByMessageId(byMessageId);
    }

    public String getByLastName() {
        return formulaireSearch.getByLastName();
    }

    public void setByLastName(String byLastName) {
        formulaireSearch.setByLastName(byLastName);
    }

    public String getByFirstName() {
        return formulaireSearch.getByFirstName();
    }

    public void setByFirstName(String byFirstName) {
        formulaireSearch.setByFirstName(byFirstName);
    }

    public String getLikeNss() {
        return formulaireSearch.getLikeNss();
    }

    public void setLikeNss(String likeNss) {
        formulaireSearch.setLikeNss(NSUtil.unFormatAVS(likeNss));
    }

    public String getOrderBy() {
        return formulaireSearch.getOrderBy();
    }

    public void setOrderBy(String orderBy) {
        formulaireSearch.setOrderKey(orderBy);
    }
}
