package globaz.vulpecula.vb.postetravail;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;

public class PTTravailleurAjaxViewBean extends
        JadeAbstractAjaxCrudFindViewBean<TravailleurComplexModel, TravailleurSearchComplexModel> {

    private TravailleurComplexModel currentEntity = null;
    private String descriptionTiers = null;

    private transient TravailleurSearchComplexModel searchModel = null;

    public PTTravailleurAjaxViewBean() {
        initList();
        currentEntity = new TravailleurComplexModel();
    }

    @Override
    public TravailleurComplexModel getCurrentEntity() {
        return currentEntity;
    }

    public String getDescriptionTier() {
        return descriptionTiers;
    }

    @Override
    public TravailleurSearchComplexModel getSearchModel() {
        return searchModel;
    }

    @Override
    public JadeCrudService<TravailleurComplexModel, TravailleurSearchComplexModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return VulpeculaServiceLocator.getTravailleurServiceCRUD();
    }

    @Override
    public void initList() {
        searchModel = new TravailleurSearchComplexModel();
    }

    @Override
    public void retrieve() throws Exception {
        super.retrieve();
        descriptionTiers = currentEntity.getPersonneEtendueComplexModel().getTiers().getDesignation1() + " "
                + currentEntity.getPersonneEtendueComplexModel().getTiers().getDesignation2();
    }

    @Override
    public void setCurrentEntity(final TravailleurComplexModel entite) {
        currentEntity = entite;
    }

    @Override
    public void setSearchModel(final TravailleurSearchComplexModel jadeAbstractSearchModel) {
        searchModel = jadeAbstractSearchModel;
    }
}
