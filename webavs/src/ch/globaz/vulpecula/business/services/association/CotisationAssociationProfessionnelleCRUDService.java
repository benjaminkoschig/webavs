package ch.globaz.vulpecula.business.services.association;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleComplexModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleSearchComplexModel;

public interface CotisationAssociationProfessionnelleCRUDService
        extends
        JadeCrudService<CotisationAssociationProfessionnelleComplexModel, CotisationAssociationProfessionnelleSearchComplexModel> {
    @Override
    public int count(CotisationAssociationProfessionnelleSearchComplexModel searchModel)
            throws JadeApplicationException, JadePersistenceException;

    @Override
    public CotisationAssociationProfessionnelleSearchComplexModel search(
            CotisationAssociationProfessionnelleSearchComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException;
}
