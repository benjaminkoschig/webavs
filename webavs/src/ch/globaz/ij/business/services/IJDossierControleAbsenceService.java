package ch.globaz.ij.business.services;

import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.ij.business.models.IJSimpleDossierControleAbsences;
import ch.globaz.ij.business.models.IJSimpleDossierControleAbsencesSearchModel;
import ch.globaz.ij.businessimpl.services.Exception.ServiceBusinessException;
import ch.globaz.ij.businessimpl.services.Exception.ServiceTechnicalException;

public interface IJDossierControleAbsenceService extends
        JadeCrudService<IJSimpleDossierControleAbsences, IJSimpleDossierControleAbsencesSearchModel> {

    public IJSimpleDossierControleAbsences historiser(String idDossier) throws ServiceTechnicalException,
            ServiceBusinessException;
}
