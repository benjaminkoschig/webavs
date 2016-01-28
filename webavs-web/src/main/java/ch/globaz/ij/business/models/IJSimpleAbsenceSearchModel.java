package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class IJSimpleAbsenceSearchModel extends JadeAbstractSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCodeAbsence;
    private String forDateDeDebut;
    private String forDateDeFin;
    private String forIdAbsence;
    private String forIdBaseIndemnisation;
    private String forIdDossierControleAbsence;
    private String forJoursNonPayeSaisis;
    private String forJoursSaisis;

    public IJSimpleAbsenceSearchModel() {
        forIdAbsence = "";
        forIdBaseIndemnisation = "";
        forIdDossierControleAbsence = "";
        forCodeAbsence = "";
        forDateDeDebut = "";
        forDateDeFin = "";
        forJoursNonPayeSaisis = "";
        forJoursSaisis = "";
    }

    public String getForCodeAbsence() {
        return forCodeAbsence;
    }

    public String getForDateDeDebut() {
        return forDateDeDebut;
    }

    public String getForDateDeFin() {
        return forDateDeFin;
    }

    public String getForIdAbsence() {
        return forIdAbsence;
    }

    public String getForIdBaseIndemnisation() {
        return forIdBaseIndemnisation;
    }

    public String getForIdDossierControleAbsence() {
        return forIdDossierControleAbsence;
    }

    public String getForJoursNonPayeSaisis() {
        return forJoursNonPayeSaisis;
    }

    public String getForJoursSaisis() {
        return forJoursSaisis;
    }

    public void setForCodeAbsence(String forCodeAbsence) {
        this.forCodeAbsence = forCodeAbsence;
    }

    public void setForDateDeDebut(String forDateDeDebut) {
        this.forDateDeDebut = forDateDeDebut;
    }

    public void setForDateDeFin(String forDateDeFin) {
        this.forDateDeFin = forDateDeFin;
    }

    public void setForIdAbsence(String forIdAbsence) {
        this.forIdAbsence = forIdAbsence;
    }

    public void setForIdBaseIndemnisation(String forIdBaseIndemnisation) {
        this.forIdBaseIndemnisation = forIdBaseIndemnisation;
    }

    public void setForIdDossierControleAbsence(String forIdDossierControleAbsence) {
        this.forIdDossierControleAbsence = forIdDossierControleAbsence;
    }

    public void setForJoursNonPayeSaisis(String forJoursNonPayeSaisis) {
        this.forJoursNonPayeSaisis = forJoursNonPayeSaisis;
    }

    public void setForJoursSaisis(String forJoursSaisis) {
        this.forJoursSaisis = forJoursSaisis;
    }

    @Override
    public Class<?> whichModelClass() {
        return IJSimpleAbsence.class;
    }

}
