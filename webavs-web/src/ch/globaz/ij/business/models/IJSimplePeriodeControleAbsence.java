package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class IJSimplePeriodeControleAbsence extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDeDebut;
    private String dateDeFin;
    private String delaisAttente;
    private String droitIj;
    private String idDossierControleAbsence;
    private String idPeriodeControleAbsence;
    private String joursPayesSolde;
    private String ordre;

    public final String getDateDeDebut() {
        return dateDeDebut;
    }

    public final String getDateDeFin() {
        return dateDeFin;
    }

    public final String getDelaisAttente() {
        return delaisAttente;
    }

    public final String getDroitIj() {
        return droitIj;
    }

    /**
     * Retourne l'id de la période de contrôle des absences
     * 
     * @return l'id de la période de contrôle des absences
     */
    @Override
    public String getId() {
        return idPeriodeControleAbsence;
    }

    public String getIdDossierControleAbsence() {
        return idDossierControleAbsence;
    }

    public String getIdPeriodeControleAbsence() {
        return idPeriodeControleAbsence;
    }

    public final String getJoursPayesSolde() {
        return joursPayesSolde;
    }

    public final String getOrdre() {
        return ordre;
    }

    public final void setDateDeDebut(String dateDeDebut) {
        this.dateDeDebut = dateDeDebut;
    }

    public final void setDateDeFin(String dateDeFin) {
        this.dateDeFin = dateDeFin;
    }

    public final void setDelaisAttente(String delaisAttente) {
        this.delaisAttente = delaisAttente;
    }

    public final void setDroitIj(String droitIj) {
        this.droitIj = droitIj;
    }

    /**
     * Définit l'id de la période de contrôle des absences
     * 
     * @param id
     *            de la période de contrôle des absences
     */
    @Override
    public void setId(String id) {
        idPeriodeControleAbsence = id;
    }

    public void setIdDossierControleAbsence(String idDossierControleAbsence) {
        this.idDossierControleAbsence = idDossierControleAbsence;
    }

    public void setIdPeriodeControleAbsence(String idPeriodeControleAbsence) {
        this.idPeriodeControleAbsence = idPeriodeControleAbsence;
    }

    public final void setJoursPayesSolde(String joursPayesSolde) {
        this.joursPayesSolde = joursPayesSolde;
    }

    public final void setOrdre(String ordre) {
        this.ordre = ordre;
    }
}
