package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class IJSimpleAbsence extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeAbsence;
    // private String codeAbsenceDescription;
    private String dateDeDebut;
    private String dateDeFin;
    private String idAbsence;
    private String idBaseIndemnisation;
    private String idDossierControle;
    private String joursNonPayeSaisis;

    private String joursSaisis;

    // private String nombreDeJoursCalcule;
    // private String nombreDeJoursInterruptionCalcule;
    // private String nombreDeJoursNonPayesCalcule;

    /**
     * Retourne le code de l'absence
     * 
     * @return le code de l'absence
     */
    public String getCodeAbsence() {
        return codeAbsence;
    }

    // public final String getCodeAbsenceDescription() {
    // return this.codeAbsenceDescription;
    // }

    /**
     * Retourne la date de début de l'absence
     * 
     * @return la date de début de l'absence
     */
    public String getDateDeDebut() {
        return dateDeDebut;
    }

    /**
     * Retourne la date de fin de l'absence
     * 
     * @return la date de fin de l'absence
     */
    public String getDateDeFin() {
        return dateDeFin;
    }

    /**
     * Retourne l'id de l'absence Similaire à <code>getAbsenceId()</code>
     * 
     * @return id de l'absence
     */
    @Override
    public String getId() {
        return idAbsence;
    }

    /**
     * Retourne l'id de l'absence Similaire à <code>getId()</code>
     * 
     * @return l'id de l'absence
     */
    public String getIdAbsence() {
        return idAbsence;
    }

    /**
     * Retourne l'id de la base d'indemnisation associée à cette absence
     * 
     * @return l'id de la base d'indemnisation associée à cette absence
     */
    public String getIdBaseIndemnisation() {
        return idBaseIndemnisation;
    }

    /**
     * Retourne l'id du dossier de contrôle des absences
     * 
     * @return id du dossier de contrôle des absences
     */
    public String getIdDossierControle() {
        return idDossierControle;
    }

    /**
     * Retourne le nombre de jours non-payé saisit par l'utilisateur
     * 
     * @return le nombre de jours non-payé saisit par l'utilisateur
     */
    public String getJoursNonPayeSaisis() {
        return joursNonPayeSaisis;
    }

    /**
     * Nombre de jours saisit par l'utilisateur
     * 
     * @return nombre de jours saisit par l'utilisateur
     */
    public String getJoursSaisis() {
        return joursSaisis;
    }

    //
    // public final String getNombreDeJoursCalcule() {
    // return this.nombreDeJoursCalcule;
    // }
    //
    // public final String getNombreDeJoursInterruptionCalcule() {
    // return this.nombreDeJoursInterruptionCalcule;
    // }
    //
    // public final String getNombreDeJoursNonPayesCalcule() {
    // return this.nombreDeJoursNonPayesCalcule;
    // }

    /**
     * Définit le code de l'absence.
     * 
     * @param codeAbsence
     *            le code de l'absence
     */
    public void setCodeAbsence(String codeAbsence) {
        this.codeAbsence = codeAbsence;
    }

    // public final void setCodeAbsenceDescription(String codeAbsenceDescription) {
    // this.codeAbsenceDescription = codeAbsenceDescription;
    // }

    /**
     * Définit la date de début de l'absence
     * 
     * @param dateDeDebut
     *            la date de début de l'absence
     */
    public void setDateDeDebut(String dateDeDebut) {
        this.dateDeDebut = dateDeDebut;
    }

    /**
     * Définit la date de fin de l'absence
     * 
     * @param dateDeFin
     *            la date de fin de l'absence
     */
    public void setDateDeFin(String dateDeFin) {
        this.dateDeFin = dateDeFin;
    }

    /**
     * Définit l'id de l'absence.<br>
     * Similaire à <code>setAbsenceId(String idAbsence)</code>
     * 
     * @param id
     *            l'id de l'absence
     */
    @Override
    public void setId(String id) {
        idAbsence = id;
    }

    /**
     * Définit l'id de l'absence.<br>
     * Similaire à <code>setId(String id)</code>
     * 
     * @param absenceId
     *            id de l'absence
     */
    public void setIdAbsence(String idAbsence) {
        this.idAbsence = idAbsence;
    }

    /**
     * Définit l'id de la base d'indemnisation associée à cette absence
     * 
     * @param baseIndemnisationId
     *            l'id de la base d'indemnisation associée à cette absence
     */
    public void setIdBaseIndemnisation(String idBaseIndemnisation) {
        this.idBaseIndemnisation = idBaseIndemnisation;
    }

    /**
     * Définit l'id du dossier de contrôle des absences.
     * 
     * @param id
     *            id du dossier de contrôle des absences
     */
    public void setIdDossierControle(String idDossierControle) {
        this.idDossierControle = idDossierControle;
    }

    /**
     * Définit le nombre de jours non-payé saisit par l'utilisateur
     * 
     * @param joursNonPayeSaisis
     *            nombre de jours non-payé saisit par l'utilisateur
     */
    public void setJoursNonPayeSaisis(String joursNonPayeSaisis) {
        this.joursNonPayeSaisis = joursNonPayeSaisis;
    }

    /**
     * Définit le nombre de jours saisit par l'utilisateur
     * 
     * @param joursSaisis
     *            nombre de jours saisit par l'utilisateur
     */
    public void setJoursSaisis(String joursSaisis) {
        this.joursSaisis = joursSaisis;
    }

    // public final void setNombreDeJoursCalcule(String nombreDeJours) {
    // this.nombreDeJoursCalcule = nombreDeJours;
    // }
    //
    // public final void setNombreDeJoursInterruptionCalcule(String nombreDeJoursInterruption) {
    // this.nombreDeJoursInterruptionCalcule = nombreDeJoursInterruption;
    // }
    //
    // public final void setNombreDeJoursNonPayesCalcule(String nombreDeJoursNonPayes) {
    // this.nombreDeJoursNonPayesCalcule = nombreDeJoursNonPayes;
    // }

}
