package ch.globaz.vulpecula.business.services.comptabilite;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.vulpecula.business.models.comptabilite.ReferenceRubriqueComplexModel;
import ch.globaz.vulpecula.business.models.comptabilite.ReferenceRubriqueSearchComplexModel;

public interface ReferenceRubriqueServiceCRUD extends
        JadeCrudService<ReferenceRubriqueComplexModel, ReferenceRubriqueSearchComplexModel> {
    @Override
    public ReferenceRubriqueSearchComplexModel search(ReferenceRubriqueSearchComplexModel search)
            throws JadeApplicationException, JadePersistenceException;

    @Override
    public int count(ReferenceRubriqueSearchComplexModel search) throws JadeApplicationException,
            JadePersistenceException;
}
