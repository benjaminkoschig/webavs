package globaz.vulpecula.vb.is;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.vulpecula.business.models.is.TauxImpositionSearchSimpleModel;
import ch.globaz.vulpecula.business.models.is.TauxImpositionSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.is.TauxImposition;
import ch.globaz.vulpecula.repositoriesjade.is.converters.TauxImpositionConverter;

public class PTTauxImpositionAjaxViewBean extends
        JadeAbstractAjaxCrudFindViewBean<TauxImpositionSimpleModel, TauxImpositionSearchSimpleModel> {
    private static final long serialVersionUID = -3753494825638691444L;

    private TauxImpositionSimpleModel currentEntity = new TauxImpositionSimpleModel();
    private TauxImpositionSearchSimpleModel searchModel = new TauxImpositionSearchSimpleModel();

    @Override
    public void initList() {
    }

    @Override
    public TauxImpositionSimpleModel getCurrentEntity() {
        return currentEntity;
    }

    @Override
    public TauxImpositionSearchSimpleModel getSearchModel() {
        return searchModel;
    }

    @Override
    public JadeCrudService<TauxImpositionSimpleModel, TauxImpositionSearchSimpleModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return VulpeculaServiceLocator.getTauxImpositionServiceCRUD();
    }

    @Override
    public void setCurrentEntity(TauxImpositionSimpleModel currentEntity) {
        this.currentEntity = currentEntity;
    }

    @Override
    public void setSearchModel(TauxImpositionSearchSimpleModel tauxImpositionSearchSimpleModel) {
        searchModel = tauxImpositionSearchSimpleModel;
    }

    @Override
    public void add() throws Exception {
        TauxImposition tauxImposition = TauxImpositionConverter.getInstance().convertToDomain(currentEntity);
        VulpeculaServiceLocator.getTauxImpositionService().create(tauxImposition);
    }

    @Override
    public void update() throws Exception {
        TauxImposition tauxImposition = TauxImpositionConverter.getInstance().convertToDomain(currentEntity);
        VulpeculaServiceLocator.getTauxImpositionService().update(tauxImposition);
    }
}
