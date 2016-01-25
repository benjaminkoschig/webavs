package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class IJSimplePeriodeControleAbsenceSearchModel extends JadeAbstractSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDeDebut;
    private String forDateDeFin;
    private String forDelaisAttente;
    private String forDroitIj;
    private String forIdDossierControleAbsence;
    private String forIdPeriodeControleAbsence;
    private String forJoursPayesSolde;
    private String forOrdre;

    public IJSimplePeriodeControleAbsenceSearchModel() {
        forIdPeriodeControleAbsence = "";
        forIdDossierControleAbsence = "";
        forDelaisAttente = "";
        forDateDeDebut = "";
        forDateDeFin = "";
        forDroitIj = "";
        forJoursPayesSolde = "";
        forOrdre = "";
    }

    public final String getForDateDeDebut() {
        return forDateDeDebut;
    }

    public final String getForDateDeFin() {
        return forDateDeFin;
    }

    public final String getForDelaisAttente() {
        return forDelaisAttente;
    }

    public final String getForDroitIj() {
        return forDroitIj;
    }

    public String getForIdDossierControleAbsence() {
        return forIdDossierControleAbsence;
    }

    public String getForIdPeriodeControleAbsence() {
        return forIdPeriodeControleAbsence;
    }

    public final String getForJoursPayesSolde() {
        return forJoursPayesSolde;
    }

    public final String getForOrdre() {
        return forOrdre;
    }

    public final void setForDateDeDebut(String forDateDeDebut) {
        this.forDateDeDebut = forDateDeDebut;
    }

    public final void setForDateDeFin(String forDateDeFin) {
        this.forDateDeFin = forDateDeFin;
    }

    public final void setForDelaisAttente(String forDelaisAttente) {
        this.forDelaisAttente = forDelaisAttente;
    }

    public final void setForDroitIj(String forDroitIj) {
        this.forDroitIj = forDroitIj;
    }

    public void setForIdDossierControleAbsence(String forIdDossierControleAbsence) {
        this.forIdDossierControleAbsence = forIdDossierControleAbsence;
    }

    public void setForIdPeriodeControleAbsence(String forIdPeriodeControleAbsence) {
        this.forIdPeriodeControleAbsence = forIdPeriodeControleAbsence;
    }

    public final void setForJoursPayesSolde(String forJoursPayesSolde) {
        this.forJoursPayesSolde = forJoursPayesSolde;
    }

    public final void setForOrdre(String forOrdre) {
        this.forOrdre = forOrdre;
    }

    @Override
    public Class<?> whichModelClass() {
        return IJSimplePeriodeControleAbsence.class;
    }

}
