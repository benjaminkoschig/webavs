package globaz.amal.vb.primesassurance;

import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.primesassurance.SimplePrimesAssurance;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author cbu
 * 
 */
public class AMPrimesAssuranceAjaxViewBean extends JadeAbstractAjaxFindViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient AMPrimesAssuranceAjaxListViewBean listPrimesAssuranceAjaxListViewBean;
    private SimplePrimesAssurance simplePrimesAssurance = null;

    public AMPrimesAssuranceAjaxViewBean() {
        super();
        simplePrimesAssurance = new SimplePrimesAssurance();
        listPrimesAssuranceAjaxListViewBean = new AMPrimesAssuranceAjaxListViewBean();
    }

    public AMPrimesAssuranceAjaxViewBean(SimplePrimesAssurance simplePrimesAssurance) {
        this();
        this.simplePrimesAssurance = simplePrimesAssurance;
    }

    @Override
    public void add() throws Exception {
        simplePrimesAssurance = AmalServiceLocator.getPrimesAssuranceService().create(simplePrimesAssurance);
    }

    @Override
    public void delete() throws Exception {
        simplePrimesAssurance = AmalServiceLocator.getPrimesAssuranceService().delete(simplePrimesAssurance);
    }

    @Override
    public void find() throws Exception {
        listPrimesAssuranceAjaxListViewBean.find();
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return simplePrimesAssurance;
    }

    public AMPrimesAssuranceAjaxListViewBean getListPrimesAssuranceAjaxListViewBean() {
        return listPrimesAssuranceAjaxListViewBean;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return listPrimesAssuranceAjaxListViewBean.getSimplePrimesAssuranceSearch();
    }

    public SimplePrimesAssurance getSimplePrimesAssurance() {
        return simplePrimesAssurance;
    }

    @Override
    public void initList() {
        listPrimesAssuranceAjaxListViewBean = new AMPrimesAssuranceAjaxListViewBean();
    }

    @Override
    public void retrieve() throws Exception {
        simplePrimesAssurance = AmalServiceLocator.getPrimesAssuranceService().read(simplePrimesAssurance.getId());
    }

    public void setListPrimesAssuranceAjaxListViewBean(
            AMPrimesAssuranceAjaxListViewBean listPrimesAssuranceAjaxListViewBean) {
        this.listPrimesAssuranceAjaxListViewBean = listPrimesAssuranceAjaxListViewBean;
    }

    public void setSimplePrimesAssurance(SimplePrimesAssurance simplePrimesAssurance) {
        this.simplePrimesAssurance = simplePrimesAssurance;
    }

    @Override
    public void update() throws Exception {
        simplePrimesAssurance = AmalServiceLocator.getPrimesAssuranceService().update(simplePrimesAssurance);
    }

}
