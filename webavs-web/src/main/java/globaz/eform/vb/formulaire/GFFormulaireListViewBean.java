package globaz.eform.vb.formulaire;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFEFormModel;
import ch.globaz.eform.business.search.GFEFormSearch;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class GFFormulaireListViewBean extends BJadePersistentObjectListViewBean {

    private GFEFormSearch formulaireSearch;

    public GFFormulaireListViewBean() {
        super();
        formulaireSearch = new GFEFormSearch();
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return formulaireSearch;
    }


    @Override
    public void find() throws Exception {
        formulaireSearch = GFEFormServiceLocator.getGFEFormService().search(formulaireSearch);
    }

    @Override
    public BIPersistentObject get(int idx) {
        return idx < formulaireSearch.getSize() ? new GFFormulaireViewBean(
                (GFEFormModel) formulaireSearch.getSearchResults()[idx]) : new GFFormulaireViewBean();
    }

}
