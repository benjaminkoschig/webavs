package ch.globaz.ij.business.services;

import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.ij.business.models.IJPeriodeControleAbsences;
import ch.globaz.ij.business.models.IJPeriodeControleAbsencesSearchModel;
import ch.globaz.ij.businessimpl.services.Exception.ServiceBusinessException;
import ch.globaz.ij.businessimpl.services.Exception.ServiceTechnicalException;

public interface IJPeriodeControleAbsencesService extends
        JadeCrudService<IJPeriodeControleAbsences, IJPeriodeControleAbsencesSearchModel> {

    public IJPeriodeControleAbsences create(String idDossier) throws ServiceTechnicalException,
            ServiceBusinessException;

    /**
     * V�rifie qu'il n'y � pas de chevauchement dans les p�riodes de contr�le du dossier. Si c'est le cas, le service
     * doit renvoyer false
     * 
     * @param idDossierControle
     *            Id du dossier
     * @return True s'il n'y � pas de chevauchement dans les p�riodes de contr�les
     * @throws ServiceTechnicalException
     * @throws ServiceBusinessException
     */
    public boolean hasChevauchementDePeriode(String idDossierControle) throws ServiceTechnicalException,
            ServiceBusinessException;

    public void calculerJoursPayeEtSoldePourPeriode(IJPeriodeControleAbsences unePeriode)
            throws ServiceTechnicalException, ServiceBusinessException;

}
