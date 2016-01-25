package ch.globaz.vulpecula.business.services.registre;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationComplexModel;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSearchComplexModel;

/**
 * @author JPA
 * 
 */
public interface ParametreCotisationAssociationServiceCRUD extends
        JadeCrudService<ParametreCotisationAssociationComplexModel, ParametreCotisationAssociationSearchComplexModel> {
    @Override
    int count(ParametreCotisationAssociationSearchComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    @Override
    ParametreCotisationAssociationSearchComplexModel search(ParametreCotisationAssociationSearchComplexModel searchModel)
            throws JadePersistenceException;
}
