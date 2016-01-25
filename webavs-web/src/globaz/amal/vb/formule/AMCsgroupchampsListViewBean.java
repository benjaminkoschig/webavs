/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.CodeSystemSearch;
import ch.globaz.envoi.business.models.parametrageEnvoi.CodeSysteme;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMCsgroupchampsListViewBean extends BJadePersistentObjectListViewBean {
    private CodeSystemSearch codeSystemSearch = null;

    /**
	  *
	  */
    public AMCsgroupchampsListViewBean() {
        super();
        codeSystemSearch = new CodeSystemSearch();
    }

    @Override
    public void find() throws Exception {
        codeSystemSearch.setForGroupe("AMGPSIG");
        codeSystemSearch.setForType("0");
        codeSystemSearch = ENServiceLocator.getCodeSystemListService().search(codeSystemSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return (idx < codeSystemSearch.getSize() ? new AMCsgroupchampsViewBean(
                (CodeSysteme) codeSystemSearch.getSearchResults()[idx]) : new AMCsgroupchampsViewBean());

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return codeSystemSearch;
    }

}
