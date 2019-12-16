package globaz.al.vb.impotsource;

import ch.globaz.al.business.services.ALServiceLocator;
import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.al.impotsource.models.TauxImpositionSearchSimpleModel;
import ch.globaz.al.impotsource.models.TauxImpositionSimpleModel;
import ch.globaz.al.impotsource.domain.TauxImposition;
import ch.globaz.al.impotsource.persistence.TauxImpositionConverter;

public class ALTauxImpositionAjaxViewBean extends
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
        return ALServiceLocator.getTauxImpositionServiceCRUD();
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
        ALServiceLocator.getTauxImpositionService().create(tauxImposition);
    }

    @Override
    public void update() throws Exception {
        TauxImposition tauxImposition = TauxImpositionConverter.getInstance().convertToDomain(currentEntity);
        ALServiceLocator.getTauxImpositionService().update(tauxImposition);
    }
}
