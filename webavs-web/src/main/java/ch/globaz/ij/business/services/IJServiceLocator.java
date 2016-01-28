package ch.globaz.ij.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class IJServiceLocator {

    /**
     * Retourne le service des '<strong>absences</strong>' des indemnit�s journali�res IJ
     * 
     * @return le service des absences des indemnit�s journali�res IJ
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static IJAbsenceService getAbsenceService() throws JadeApplicationServiceNotAvailableException {
        return (IJAbsenceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(IJAbsenceService.class);
    }

    /**
     * Retourne le service des '<strong>bases d'indemnisation</strong>' des indemnit�s journali�res IJ
     * 
     * @return le service des bases d'indemnisation des indemnit�s journali�res IJ
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static IJBaseIndemnisationService getBaseIndemnisationService()
            throws JadeApplicationServiceNotAvailableException {
        return (IJBaseIndemnisationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                IJBaseIndemnisationService.class);
    }

    /**
     * Retourne le service de '<strong>contr�le des p�riodes</strong>' des indemnit�s journali�res IJ
     * 
     * @return le service de 'contr�le des p�riodes' des indemnit�s journali�res IJ
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static IJPeriodeControleAbsencesService getControlePeriodeService()
            throws JadeApplicationServiceNotAvailableException {
        return (IJPeriodeControleAbsencesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                IJPeriodeControleAbsencesService.class);
    }

    /**
     * Retourne le service des '<strong>dossiers de contr�le des absences</strong>' des indemnit�s journali�res IJ
     * 
     * @return le service des 'dossiers de contr�le des absences' des indemnit�s journali�res IJ
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static IJDossierControleAbsenceService getDossierControleAbsenceService()
            throws JadeApplicationServiceNotAvailableException {
        return (IJDossierControleAbsenceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                IJDossierControleAbsenceService.class);
    }

    /**
     * Retourne le service des '<strong>prononc�s</strong>' des indemnit�s journali�res IJ
     * 
     * @return le service des 'prononc�s' des indemnit�s journali�res IJ
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static IJPrononceService getPrononceService() throws JadeApplicationServiceNotAvailableException {
        return (IJPrononceService) JadeApplicationServiceLocator.getInstance().getServiceImpl(IJPrononceService.class);
    }

}
