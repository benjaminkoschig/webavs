package globaz.vulpecula.vb.comptabilite;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.vulpecula.business.models.comptabilite.ReferenceRubriqueComplexModel;
import ch.globaz.vulpecula.business.models.comptabilite.ReferenceRubriqueSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;

public class PTReferencesRubriquesAjaxViewBean extends
        JadeAbstractAjaxCrudFindViewBean<ReferenceRubriqueComplexModel, ReferenceRubriqueSearchComplexModel> {
    private static final long serialVersionUID = -2586251765019485908L;

    private ReferenceRubriqueComplexModel currentEntity;
    private ReferenceRubriqueSearchComplexModel searchModel;

    public PTReferencesRubriquesAjaxViewBean() {
        currentEntity = new ReferenceRubriqueComplexModel();
        searchModel = new ReferenceRubriqueSearchComplexModel();
        searchModel.setForLangueNull();
    }

    @Override
    public void initList() {
    }

    @Override
    public ReferenceRubriqueComplexModel getCurrentEntity() {
        return currentEntity;
    }

    @Override
    public ReferenceRubriqueSearchComplexModel getSearchModel() {
        return searchModel;
    }

    @Override
    public JadeCrudService<ReferenceRubriqueComplexModel, ReferenceRubriqueSearchComplexModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return VulpeculaServiceLocator.getReferenceRubriqueServiceCRUD();
    }

    @Override
    public void setCurrentEntity(ReferenceRubriqueComplexModel currentEntity) {
        this.currentEntity = currentEntity;
    }

    @Override
    public void setSearchModel(ReferenceRubriqueSearchComplexModel searchModel) {
        this.searchModel = searchModel;
    }
}
