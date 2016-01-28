package globaz.pegasus.vb.revenusdepenses;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViagerSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViagerSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCContratEntretienViagerAjaxListViewBean extends BJadePersistentObjectListViewBean {

    private SimpleLibelleContratEntretienViagerSearch searchLibelle = null;
    private ContratEntretienViagerSearch searchModel = null;

    /**
	 * 
	 */
    public PCContratEntretienViagerAjaxListViewBean() {
        super();
        searchModel = new ContratEntretienViagerSearch();
        searchLibelle = new SimpleLibelleContratEntretienViagerSearch();
    }

    @Override
    public void find() throws Exception {
        searchModel = PegasusServiceLocator.getDroitService().searchContratEntretienViager(searchModel);

        List<String> listIdContrat = new ArrayList<String>();

        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            ContratEntretienViager contratEntretienViager = (ContratEntretienViager) model;
            listIdContrat.add(contratEntretienViager.getSimpleContratEntretienViager().getIdContratEntretienViager());
        }

        if (!listIdContrat.isEmpty()) {
            searchLibelle.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchLibelle.setInIdContratEntretienViager(listIdContrat);
            searchLibelle = PegasusServiceLocator.getDroitService().searchSimpleLibelleContratEntretienViager(
                    searchLibelle);
        }

    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new PCContratEntretienViagerAjaxViewBean(
                (ContratEntretienViager) searchModel.getSearchResults()[idx])
                : new PCContratEntretienViagerAjaxViewBean();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    public SimpleLibelleContratEntretienViagerSearch getSearchLibelle() {
        return searchLibelle;
    }

    /**
     * @return the searchModel
     */
    public ContratEntretienViagerSearch getSearchModel() {
        return searchModel;
    }

    public void setSearchLibelle(SimpleLibelleContratEntretienViagerSearch searchLibelle) {
        this.searchLibelle = searchLibelle;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(ContratEntretienViagerSearch searchModel) {
        this.searchModel = searchModel;
    }

}