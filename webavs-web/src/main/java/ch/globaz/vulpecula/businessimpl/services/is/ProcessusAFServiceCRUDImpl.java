package ch.globaz.vulpecula.businessimpl.services.is;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.Collection;
import ch.globaz.vulpecula.business.models.is.ProcessusAFComplexModel;
import ch.globaz.vulpecula.business.models.is.ProcessusAFSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.is.ProcessusAFServiceCRUD;

public class ProcessusAFServiceCRUDImpl implements ProcessusAFServiceCRUD {

    @Override
    public int count(ProcessusAFSearchComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        return JadePersistenceManager.count(searchModel);
    }

    @Override
    public ProcessusAFComplexModel create(ProcessusAFComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        return null;
    }

    @Override
    public ProcessusAFComplexModel delete(ProcessusAFComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        return null;
    }

    @Override
    public ProcessusAFComplexModel read(String searchModel) throws JadeApplicationException, JadePersistenceException {
        return null;
    }

    @Override
    public ProcessusAFSearchComplexModel search(ProcessusAFSearchComplexModel searchModel)
            throws JadeApplicationException, JadePersistenceException {
        Collection<String> idsAlreadyUsed = VulpeculaRepositoryLocator.getHistoriqueProcessusAfRepository()
                .findIdProcessusByType(searchModel.getForBusinessProcessus());
        searchModel.setForIdsNotIn(idsAlreadyUsed);
        return (ProcessusAFSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public ProcessusAFComplexModel update(ProcessusAFComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        return null;
    }

}
