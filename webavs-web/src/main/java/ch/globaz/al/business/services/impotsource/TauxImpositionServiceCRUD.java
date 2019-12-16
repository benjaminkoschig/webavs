package ch.globaz.al.business.services.impotsource;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.al.impotsource.models.TauxImpositionSearchSimpleModel;
import ch.globaz.al.impotsource.models.TauxImpositionSimpleModel;

public interface TauxImpositionServiceCRUD extends
        JadeCrudService<TauxImpositionSimpleModel, TauxImpositionSearchSimpleModel> {
    @Override
    int count(TauxImpositionSearchSimpleModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    @Override
    TauxImpositionSearchSimpleModel search(TauxImpositionSearchSimpleModel searchModel)
            throws JadeApplicationException, JadePersistenceException;
}