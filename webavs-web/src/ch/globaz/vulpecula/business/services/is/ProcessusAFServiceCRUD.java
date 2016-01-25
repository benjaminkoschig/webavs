package ch.globaz.vulpecula.business.services.is;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.vulpecula.business.models.is.ProcessusAFComplexModel;
import ch.globaz.vulpecula.business.models.is.ProcessusAFSearchComplexModel;

public interface ProcessusAFServiceCRUD extends JadeCrudService<ProcessusAFComplexModel, ProcessusAFSearchComplexModel> {
    @Override
    public ProcessusAFSearchComplexModel search(ProcessusAFSearchComplexModel arg0) throws JadeApplicationException,
            JadePersistenceException;

    @Override
    public int count(ProcessusAFSearchComplexModel arg0) throws JadeApplicationException, JadePersistenceException;
}
