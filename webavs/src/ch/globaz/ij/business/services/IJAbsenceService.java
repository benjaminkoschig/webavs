package ch.globaz.ij.business.services;

import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.ij.business.models.IJAbsence;
import ch.globaz.ij.business.models.IJAbsenceSearchModel;
import ch.globaz.ij.businessimpl.services.Exception.ServiceBusinessException;
import ch.globaz.ij.businessimpl.services.Exception.ServiceTechnicalException;

public interface IJAbsenceService extends JadeCrudService<IJAbsence, IJAbsenceSearchModel> {

    /**
     * ATTENTION !!! Ne pas utiliser cette méthode pour la suppression d'une absence. Cette méthode est réservé pour le
     * service @see IJBaseIndemnisationServiceImpl qui l'utilise lors de la supression de la base d'indemnisation (pas
     * besoin de recalculer la base d'inemnisation)
     * 
     * @param entity
     * @return
     * @throws ServiceBusinessException
     * @throws ServiceTechnicalException
     */
    public IJAbsence deleteWithoutBaseIndemnisationUpdate(IJAbsence entity) throws ServiceBusinessException,
            ServiceTechnicalException;

    /**
     * @param idDossierControle
     * @param codeAbsence
     * @param dateDeDebut
     * @param dateDeFin
     * @param jourNonPayeSaisis
     * @param joursSaisis
     * @return
     * @throws ServiceBusinessException
     * @throws ServiceTechnicalException
     */
    public String verifierSiSoldeNegatifApresSaisieAbsence(String idDossierControle, String idBaseIndemnisation,
            String idAbsence, String codeAbsence, String dateDeDebut, String dateDeFin, String jourNonPayeSaisis,
            String joursSaisis, String mode) throws ServiceBusinessException, ServiceTechnicalException;
}
