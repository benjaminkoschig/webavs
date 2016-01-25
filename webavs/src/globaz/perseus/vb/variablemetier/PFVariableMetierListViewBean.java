/**
 * 
 */
package globaz.perseus.vb.variablemetier;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.models.variablemetier.VariableMetierSearch;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFVariableMetierListViewBean extends BJadePersistentObjectListViewBean {

    private VariableMetierSearch variableMetierSearch;

    public PFVariableMetierListViewBean() {
        super();
        variableMetierSearch = new VariableMetierSearch();
    }

    /**
     * @throws VariableMetierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void find() throws VariableMetierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        variableMetierSearch = PerseusServiceLocator.getVariableMetierService().search(variableMetierSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < variableMetierSearch.getSize() ? new PFVariableMetierViewBean(
                (VariableMetier) variableMetierSearch.getSearchResults()[idx]) : new PFVariableMetierViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return variableMetierSearch;
    }

    /**
     * @return the variableMetierSearch
     */
    public VariableMetierSearch getVariableMetierSearch() {
        return variableMetierSearch;
    }

    public String replaceBlanc(String str) {
        return (JadeStringUtil.isEmpty(str)) ? "&nbsp" : str;
    }

    public void setFor() {

    }

    /**
     * @param variableMetierSearch
     *            the variableMetierSearch to set
     */
    public void setVariableMetierSearch(VariableMetierSearch variableMetierSearch) {
        this.variableMetierSearch = variableMetierSearch;
    }

}
