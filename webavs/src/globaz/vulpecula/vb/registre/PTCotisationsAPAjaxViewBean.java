package globaz.vulpecula.vb.registre;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleComplexModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;

public class PTCotisationsAPAjaxViewBean
        extends
        JadeAbstractAjaxCrudFindViewBean<CotisationAssociationProfessionnelleComplexModel, CotisationAssociationProfessionnelleSearchComplexModel> {
    private static final long serialVersionUID = 1L;

    private CotisationAssociationProfessionnelleComplexModel currentEntity = new CotisationAssociationProfessionnelleComplexModel();
    private CotisationAssociationProfessionnelleSearchComplexModel searchModel;

    public PTCotisationsAPAjaxViewBean() {
        initList();
    }

    @Override
    public void initList() {
        searchModel = new CotisationAssociationProfessionnelleSearchComplexModel();
    }

    @Override
    public CotisationAssociationProfessionnelleComplexModel getCurrentEntity() {
        return currentEntity;
    }

    @Override
    public CotisationAssociationProfessionnelleSearchComplexModel getSearchModel() {
        return searchModel;
    }

    @Override
    public JadeCrudService<CotisationAssociationProfessionnelleComplexModel, CotisationAssociationProfessionnelleSearchComplexModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return VulpeculaServiceLocator.getCotisationAssociationProfessionnelleCrudService();
    }

    @Override
    public void setCurrentEntity(CotisationAssociationProfessionnelleComplexModel entity) {
        currentEntity = entity;
    }

    @Override
    public void setSearchModel(CotisationAssociationProfessionnelleSearchComplexModel searchModel) {
        this.searchModel = searchModel;
    }
}
