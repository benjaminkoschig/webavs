package globaz.ij.helpers.controleAbsences;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.regles.IJBaseIndemnisationRegles;
import globaz.ij.vb.controleAbsences.IJBaseIndemnisationAjaxViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.utils.PRDateUtils;
import ch.globaz.ij.business.models.IJAbsence;
import ch.globaz.ij.business.models.IJAbsenceSearchModel;
import ch.globaz.ij.business.models.IJSimpleBaseIndemnisation;
import ch.globaz.ij.business.services.IJAbsenceService;
import ch.globaz.ij.business.services.IJBaseIndemnisationService;
import ch.globaz.ij.business.services.IJServiceLocator;

public class IJBaseIndemnisationAjaxHelper extends PRAbstractHelper {

    /**
     * Recalcul les jours externes, les jours d'interruption et le motif
     * 
     * @param idBaseIndemnisation
     * @throws ServiceTechnicalException
     */
    public static IJSimpleBaseIndemnisation actualise(String idBaseIndemnisation) throws Exception {
        IJBaseIndemnisationService baseIndemnisationService;
        baseIndemnisationService = IJServiceLocator.getBaseIndemnisationService();
        IJSimpleBaseIndemnisation baseIndemnisation = baseIndemnisationService.read(idBaseIndemnisation);
        if (baseIndemnisation == null) {
            throw new Exception("Unable to found entity IJBaseIndemnisationService with id : " + idBaseIndemnisation);
        }
        return IJBaseIndemnisationAjaxHelper.calculerValeurs(baseIndemnisation);
    }

    /**
     * Recalcule les jours externes, les jours d'interruption et le motif
     * 
     * @param idBaseIndemnisation
     * @throws ServiceTechnicalException
     */
    public static IJSimpleBaseIndemnisation calculerValeurs(IJSimpleBaseIndemnisation baseIndemnisation)
            throws Exception {
        // try {
        IJAbsenceService absenceService = IJServiceLocator.getAbsenceService();
        IJAbsenceSearchModel asm = new IJAbsenceSearchModel();
        int totalJoursNonPayeSaisit = 0;
        int totalJoursInterruption = 0;

        if (!JadeStringUtil.isBlankOrZero(baseIndemnisation.getIdBaseIndemnisation())) {
            asm.setForIdBaseIndemnisation(baseIndemnisation.getIdBaseIndemnisation());
            asm = absenceService.search(asm);
            JadeAbstractModel[] absences = asm.getSearchResults();
            JadePeriodWrapper periodeBaseIndemnisation = new JadePeriodWrapper(baseIndemnisation.getDateDeDebut(),
                    baseIndemnisation.getDateDeFin());

            for (JadeAbstractModel model : absences) {
                IJAbsence absence = (IJAbsence) model;
                // Si l'absence est dans la période de la base d'indemnisation
                if ((periodeBaseIndemnisation.isDateDansLaPeriode(absence.getDateDeDebut()))
                        && (periodeBaseIndemnisation.isDateDansLaPeriode(absence.getDateDeFin()))) {

                    // Si l'absence contient des jours d'interruption
                    if (!JadeStringUtil.isBlankOrZero(absence.getJoursInterruption())) {
                        totalJoursInterruption += Integer.valueOf(absence.getJoursInterruption());
                    }

                    // Si l'absence contient des jours non payés saisit
                    if (!JadeStringUtil.isBlankOrZero(absence.getJoursNonPayeSaisis())) {
                        totalJoursNonPayeSaisit += Integer.valueOf(absence.getJoursNonPayeSaisis());
                    }
                }
            }

            // Recherche du motif
            if ((absences.length == 0) || (absences[0] == null)) {
                baseIndemnisation.setMotifInterruption("");
            } else {
                IJAbsence absence = (IJAbsence) absences[0];
                // Si pas de code d'absence, on ne set pas de motif dans la base
                if (JadeStringUtil.isBlankOrZero(absence.getCodeAbsence())) {
                    baseIndemnisation.setMotifInterruption("");
                }
                // Si aucun jours d'interruption, on ne set pas le motif dans la base d'indemnisation
                else if (JadeStringUtil.isBlankOrZero(absence.getJoursInterruption())) {
                    baseIndemnisation.setMotifInterruption("");
                } else {
                    if (IJAbsence.CS_ABSENCE_ACCIDENT_SANS_RAPPORT_REA.equals(absence.getCodeAbsence())) {
                        baseIndemnisation.setMotifInterruption(IIJBaseIndemnisation.CS_MAL_ACC_SANS_RAP_READAPT);
                    } else if (IJAbsence.CS_ABSENCE_MALADIE_SANS_RAPPORT_REA.equals(absence.getCodeAbsence())) {
                        baseIndemnisation.setMotifInterruption(IIJBaseIndemnisation.CS_MAL_ACC_SANS_RAP_READAPT);
                    } else if (IJAbsence.CS_ABSENCE_ACCIDENT_AVEC_RAPPORT_REA.equals(absence.getCodeAbsence())) {
                        baseIndemnisation.setMotifInterruption(IIJBaseIndemnisation.CS_MAL_ACC_EN_RAP_READAPT);
                    } else if (IJAbsence.CS_ABSENCE_MALADIE_AVEC_RAPPORT_REA.equals(absence.getCodeAbsence())) {
                        baseIndemnisation.setMotifInterruption(IIJBaseIndemnisation.CS_MAL_ACC_EN_RAP_READAPT);
                    } else if (IJAbsence.CS_ABSENCE_GROSSESSE.equals(absence.getCodeAbsence())) {
                        baseIndemnisation.setMotifInterruption(IIJBaseIndemnisation.CS_GROSSESSE);
                    } else if (IJAbsence.CS_ABSENCE_ACCOUCHEMENT.equals(absence.getCodeAbsence())) {
                        baseIndemnisation.setMotifInterruption(IIJBaseIndemnisation.CS_ACCOUCHEMENT);
                    } else if (IJAbsence.CS_ABSENCE_AUTRE_MOTIF.equals(absence.getCodeAbsence())) {
                        baseIndemnisation.setMotifInterruption(IIJBaseIndemnisation.CS_AUTRES_MOTIFS);
                    } else if (IJAbsence.CS_ABSENCE_INJUSTIFIE.equals(absence.getCodeAbsence())) {
                        baseIndemnisation.setMotifInterruption("");
                    }
                }
            }
        }
        baseIndemnisation.setJoursInterruption(String.valueOf(totalJoursInterruption));

        // Calcul du nombre de jours externes
        int nbJoursPeriodeBI = PRDateUtils.getNbDayBetween(baseIndemnisation.getDateDeDebut(),
                baseIndemnisation.getDateDeFin()) + 1;

        int totalJoursInternes = 0;
        if (!JadeStringUtil.isBlankOrZero(baseIndemnisation.getJoursInternes())) {
            totalJoursInternes += Integer.valueOf(baseIndemnisation.getJoursInternes());
        }
        int totalJoursExternes = nbJoursPeriodeBI - (totalJoursNonPayeSaisit + totalJoursInternes);

        if (totalJoursExternes < 0) {
            throw new Exception(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "ERROR_BASE_INDEMNISATION_JOURS_EXTERNES_INFERIEUR_ZERO"));
        }

        baseIndemnisation.setJoursExternes(String.valueOf(totalJoursExternes));
        return baseIndemnisation;
    }

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJBaseIndemnisationAjaxViewBean vb = (IJBaseIndemnisationAjaxViewBean) viewBean;

        String idPrononce = vb.getIdPrononce();
        if (JadeStringUtil.isBlankOrZero(idPrononce)) {
            throw new Exception("ID prononce is empty");
        }

        if (vb.getCurrentEntity() == null) {
            throw new Exception("IJBaseIndemnisationAjaxHelper : vb.getCurrentEntity() is empty");
        }

        // validation des dates
        validate(idPrononce, vb, (BSession) session);

        // Calcul de la base d'indemnisation si besoin
        String value = vb.getCalculerBaseIndemnisation();
        if (Boolean.valueOf(value)) {
            IJBaseIndemnisationAjaxHelper.calculerValeurs(vb.getCurrentEntity());
        }
        super._add(viewBean, action, session);
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        ((BIPersistentObject) viewBean).retrieve();
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJBaseIndemnisationAjaxViewBean vb = (IJBaseIndemnisationAjaxViewBean) viewBean;

        // BZ7847
        String idPrononce = vb.getIdPrononce();
        if (JadeStringUtil.isBlankOrZero(idPrononce)) {
            throw new Exception("ID prononce is empty");
        }

        if (vb.getCurrentEntity() == null) {
            throw new Exception("IJBaseIndemnisationAjaxViewBean : currentEntity is empty");
        }

        // validation des dates
        validate(idPrononce, vb, (BSession) session);

        // Calcul de la base d'indemnisation si besoin
        String value = vb.getCalculerBaseIndemnisation();
        if (Boolean.valueOf(value)) {
            IJBaseIndemnisationAjaxHelper.calculerValeurs(vb.getCurrentEntity());
        }
        super._update(viewBean, action, session);
    }

    public FWViewBeanInterface creerCorrection(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        IJBaseIndemnisationAjaxViewBean vb = (IJBaseIndemnisationAjaxViewBean) viewBean;
        IJBaseIndemnisation baseOrigine = new IJBaseIndemnisation();
        baseOrigine.setIdBaseIndemisation(vb.getIdBaseIndemnisation());
        baseOrigine.setSession(session);
        baseOrigine.retrieve();

        if (IJBaseIndemnisationRegles.isCorrigerPermisExt(baseOrigine)) {
            IJBaseIndemnisationRegles.creerCorrection(session, session.getCurrentThreadTransaction(), baseOrigine);
        }
        return baseOrigine;
    }

    private void validate(String idPrononce, IJBaseIndemnisationAjaxViewBean vb, BSession session) throws Exception {

        // On doit avoir une date de début pour la base d'indemnisation
        if (!JadeDateUtil.isGlobazDate(vb.getDateDeDebut())) {
            throw new Exception("La date de début de la base d'indemnisation est vide");
        }
        // et une date de fin
        if (!JadeDateUtil.isGlobazDate(vb.getDateDeFin())) {
            throw new Exception("La date de fin de la base d'indemnisation est vide");
        }

        // Récupération du prononcé pour comparaison des date de début/fin
        IJPrononce prononce = new IJPrononce();
        prononce.setSession(session);
        prononce.setIdPrononce(idPrononce);
        prononce.retrieve();
        if (prononce.isNew()) {
            throw new Exception("Can not retreive prononce with id : " + idPrononce);
        }
        if (!JadeDateUtil.isGlobazDate(prononce.getDateDebutPrononce())) {
            throw new Exception("La date de début du prononcé est vide");
        }

        // Contrôle que la période de la base d'indemnisation soit dans la période du prononce.
        // La date de fin du prononce peut-être vide !!
        if (!JadeDateUtil.isGlobazDate(prononce.getDateFinPrononce())) {

            // BZ7753 LGA : La date de début de la base d'indemnisation ne peut pas se situer avant la date de début du
            // prononcé
            if (JadeDateUtil.isDateBefore(vb.getDateDeDebut(), prononce.getDateDebutPrononce())) {
                throw new Exception(
                        "La date de début de la base d'indémnisation ne peut pas être située avant la date de début du prononcé");
            }

            // BZ7753 LGA : la date de début de la base d'indémnisation peut être identique à la date de début du
            // prononcé.
            if (!JadeDateUtil.isDateBefore(prononce.getDateDebutPrononce(), vb.getDateDeDebut())
                    && !prononce.getDateDebutPrononce().equals(vb.getDateDeDebut())) {
                throw new Exception(
                        "La date de début de la base d'indémnisation ne peut pas être située après la date de début du prononcé");
            }

        } else {
            JadePeriodWrapper periodePrononce = new JadePeriodWrapper(prononce.getDateDebutPrononce(),
                    prononce.getDateFinPrononce());
            if (!periodePrononce.isDateDansLaPeriode(vb.getDateDeDebut())) {
                throw new Exception(
                        "La date de début de la base d'indémnisation ne se situe pas dans la période du prononcé");
            }
            if (!periodePrononce.isDateDansLaPeriode(vb.getDateDeFin())) {
                throw new Exception(
                        "La date de fin de la base d'indémnisation ne se situe pas dans la période du prononcé");
            }
        }
    }
}
