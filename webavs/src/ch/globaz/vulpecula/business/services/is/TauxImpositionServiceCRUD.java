package ch.globaz.vulpecula.business.services.is;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.vulpecula.business.models.is.TauxImpositionSearchSimpleModel;
import ch.globaz.vulpecula.business.models.is.TauxImpositionSimpleModel;

public interface TauxImpositionServiceCRUD extends
        JadeCrudService<TauxImpositionSimpleModel, TauxImpositionSearchSimpleModel> {
    @Override
    public int count(TauxImpositionSearchSimpleModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    @Override
    public TauxImpositionSearchSimpleModel search(TauxImpositionSearchSimpleModel searchModel)
            throws JadeApplicationException, JadePersistenceException;
}