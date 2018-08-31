package globaz.vulpecula.vb.ebusiness;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.vulpecula.business.models.ebusiness.TravailleurEbuSimpleModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurEbuSearchSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;

public class PTNouveauTravailleurAjaxViewBean extends
        JadeAbstractAjaxCrudFindViewBean<TravailleurEbuSimpleModel, TravailleurEbuSearchSimpleModel> {
    private static final long serialVersionUID = 1L;

    private TravailleurEbuSimpleModel currentEntity = null;
    private String descriptionTiers = null;

    private transient TravailleurEbuSearchSimpleModel searchModel = null;

    public PTNouveauTravailleurAjaxViewBean() {
        initList();
        currentEntity = new TravailleurEbuSimpleModel();
    }

    @Override
    public TravailleurEbuSimpleModel getCurrentEntity() {
        return currentEntity;
    }

    public String getDescriptionTier() {
        return descriptionTiers;
    }

    @Override
    public TravailleurEbuSearchSimpleModel getSearchModel() {
        return searchModel;
    }

    @Override
    public JadeCrudService<TravailleurEbuSimpleModel, TravailleurEbuSearchSimpleModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return VulpeculaServiceLocator.getNouveauTravailleurServiceCRUD();
    }

    @Override
    public void initList() {
        searchModel = new TravailleurEbuSearchSimpleModel();
    }

    @Override
    public void setCurrentEntity(final TravailleurEbuSimpleModel entite) {
        currentEntity = entite;
    }

    @Override
    public void setSearchModel(final TravailleurEbuSearchSimpleModel jadeAbstractSearchModel) {
        searchModel = jadeAbstractSearchModel;
    }
}
