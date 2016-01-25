package globaz.pegasus.vb.revenusdepenses;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependanteSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenuSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCRevenuActiviteLucrativeDependanteAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private SimpleTypeFraisObtentionRevenuSearch searchFrais = null;
    private RevenuActiviteLucrativeDependanteSearch searchModel = null;

    /**
	 * 
	 */
    public PCRevenuActiviteLucrativeDependanteAjaxListViewBean() {
        super();
        searchModel = new RevenuActiviteLucrativeDependanteSearch();
        searchFrais = new SimpleTypeFraisObtentionRevenuSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchRevenuActiviteLucrativeDependante(searchModel);

        List<String> listIdRevenu = new ArrayList<String>();
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante = (RevenuActiviteLucrativeDependante) model;
            listIdRevenu.add(revenuActiviteLucrativeDependante.getSimpleRevenuActiviteLucrativeDependante()
                    .getIdRevenuActiviteLucrativeDependante());
        }
        if (!listIdRevenu.isEmpty()) {
            searchFrais.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchFrais.setInIdRevenuActiviteLucrativeDependante(listIdRevenu);
            searchFrais = PegasusServiceLocator.getDroitService().searchSimpleTypeFraisObtentionRevenu(searchFrais);
        }

    }

    @Override
    public BIPersistentObject get(int idx) {

        return idx < searchModel.getSize() ? new PCRevenuActiviteLucrativeDependanteAjaxViewBean(
                (RevenuActiviteLucrativeDependante) searchModel.getSearchResults()[idx])
                : new PCRevenuActiviteLucrativeDependanteAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    public SimpleTypeFraisObtentionRevenuSearch getSearchFrais() {
        return searchFrais;
    }

    /**
     * @return the searchModel
     */
    public RevenuActiviteLucrativeDependanteSearch getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(RevenuActiviteLucrativeDependanteSearch searchModel) {
        this.searchModel = searchModel;
    }

}