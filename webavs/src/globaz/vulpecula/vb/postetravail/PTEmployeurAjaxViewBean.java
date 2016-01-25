package globaz.vulpecula.vb.postetravail;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.globall.db.BSessionUtil;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;

/**
 * @author sel
 * 
 */
public class PTEmployeurAjaxViewBean extends
        JadeAbstractAjaxCrudFindViewBean<EmployeurComplexModel, EmployeurSearchComplexModel> {

    private EmployeurComplexModel currentEntity = null;
    private String personnaliteJuridiqueLibelle = null;
    private transient EmployeurSearchComplexModel searchModel = null;

    /**
     *
     */
    public PTEmployeurAjaxViewBean() {
        initList();
        currentEntity = new EmployeurComplexModel();
    }

    @Override
    public EmployeurComplexModel getCurrentEntity() {
        return currentEntity;
    }

    /**
     * @return
     */
    public String getPersonnaliteJuridiqueLibelle() {
        return personnaliteJuridiqueLibelle;
    }

    @Override
    public EmployeurSearchComplexModel getSearchModel() {
        return searchModel;
    }

    @Override
    public JadeCrudService<EmployeurComplexModel, EmployeurSearchComplexModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return VulpeculaServiceLocator.getEmployeurServiceCRUD();
    }

    @Override
    public void initList() {
        searchModel = new EmployeurSearchComplexModel();
    }

    @Override
    public void retrieve() throws Exception {
        super.retrieve();
        personnaliteJuridiqueLibelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                currentEntity.getAffiliationTiersComplexModel().getAffiliation().getPersonnaliteJuridique());
    }

    @Override
    public void setCurrentEntity(EmployeurComplexModel entite) {
        currentEntity = entite;
    }

    /**
     * @param personnaliteJuridiqueLibelle
     *            the personnaliteJuridiqueLibelle to set
     */
    public void setPersonnaliteJuridiqueLibelle(String personnaliteJuridiqueLibelle) {
        this.personnaliteJuridiqueLibelle = personnaliteJuridiqueLibelle;
    }

    @Override
    public void setSearchModel(EmployeurSearchComplexModel jadeAbstractSearchModel) {
        searchModel = jadeAbstractSearchModel;
    }
}
