package ch.globaz.ij.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class IJServiceLocator {

    /**
     * Retourne le service des '<strong>absences</strong>' des indemnités journalières IJ
     * 
     * @return le service des absences des indemnités journalières IJ
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static IJAbsenceService getAbsenceService() throws JadeApplicationServiceNotAvailableException {
        return (IJAbsenceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(IJAbsenceService.class);
    }

    /**
     * Retourne le service des '<strong>bases d'indemnisation</strong>' des indemnités journalières IJ
     * 
     * @return le service des bases d'indemnisation des indemnités journalières IJ
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static IJBaseIndemnisationService getBaseIndemnisationService()
            throws JadeApplicationServiceNotAvailableException {
        return (IJBaseIndemnisationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                IJBaseIndemnisationService.class);
    }

    /**
     * Retourne le service de '<strong>contrôle des périodes</strong>' des indemnités journalières IJ
     * 
     * @return le service de 'contrôle des périodes' des indemnités journalières IJ
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static IJPeriodeControleAbsencesService getControlePeriodeService()
            throws JadeApplicationServiceNotAvailableException {
        return (IJPeriodeControleAbsencesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                IJPeriodeControleAbsencesService.class);
    }

    /**
     * Retourne le service des '<strong>dossiers de contrôle des absences</strong>' des indemnités journalières IJ
     * 
     * @return le service des 'dossiers de contrôle des absences' des indemnités journalières IJ
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static IJDossierControleAbsenceService getDossierControleAbsenceService()
            throws JadeApplicationServiceNotAvailableException {
        return (IJDossierControleAbsenceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                IJDossierControleAbsenceService.class);
    }

    /**
     * Retourne le service des '<strong>prononcés</strong>' des indemnités journalières IJ
     * 
     * @return le service des 'prononcés' des indemnités journalières IJ
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static IJPrononceService getPrononceService() throws JadeApplicationServiceNotAvailableException {
        return (IJPrononceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(IJPrononceService.class);
    }

}
