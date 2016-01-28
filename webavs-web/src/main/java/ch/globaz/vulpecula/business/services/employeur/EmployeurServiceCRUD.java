package ch.globaz.vulpecula.business.services.employeur;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurSearchComplexModel;

public interface EmployeurServiceCRUD extends JadeCrudService<EmployeurComplexModel, EmployeurSearchComplexModel> {
    @Override
    public EmployeurSearchComplexModel search(EmployeurSearchComplexModel search) throws JadeApplicationException,
            JadePersistenceException;

    @Override
    public int count(EmployeurSearchComplexModel search) throws JadeApplicationException, JadePersistenceException;
}
