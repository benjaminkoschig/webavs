package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;

public class IJAbsence extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CS_ABSENCE_ACCIDENT_AVEC_RAPPORT_REA = "52433003";
    public static final String CS_ABSENCE_ACCIDENT_SANS_RAPPORT_REA = "52433001";
    public static final String CS_ABSENCE_ACCOUCHEMENT = "52433006";
    public static final String CS_ABSENCE_AUTRE_MOTIF = "52433007";
    public static final String CS_ABSENCE_GROSSESSE = "52433005";
    public static final String CS_ABSENCE_INJUSTIFIE = "52433008";
    public static final String CS_ABSENCE_MALADIE_AVEC_RAPPORT_REA = "52433004";
    public static final String CS_ABSENCE_MALADIE_SANS_RAPPORT_REA = "52433002";

    private IJSimpleAbsence absence;
    private IJSimpleDossierControleAbsences dossier;
    private String joursInterruption;
    private String joursNonPaye;
    private String nombreDeJours;
    private JadeCodeSysteme typeAbsenceCS;

    public IJAbsence() {
        super();

        absence = new IJSimpleAbsence();
        dossier = new IJSimpleDossierControleAbsences();
    }

    public final IJSimpleAbsence getAbsence() {
        return absence;
    }

    public String getCodeAbsence() {
        return absence.getCodeAbsence();
    }

    public final String getCreationSpy() {
        return absence.getCreationSpy();
    }

    public String getDateDeDebut() {
        return absence.getDateDeDebut();
    }

    public String getDateDeFin() {
        return absence.getDateDeFin();
    }

    public IJSimpleDossierControleAbsences getDossier() {
        return dossier;
    }

    @Override
    public String getId() {
        return absence.getId();
    }

    public String getIdAbsence() {
        return absence.getIdAbsence();
    }

    public String getIdBaseIndemnisation() {
        return absence.getIdBaseIndemnisation();
    }

    public final String getIdDossierControleAbsences() {
        return dossier.getIdDossierControleAbsences();
    }

    public final String getJoursInterruption() {
        return joursInterruption;
    }

    public final String getJoursNonPaye() {
        return joursNonPaye;
    }

    public String getJoursNonPayeSaisis() {
        return absence.getJoursNonPayeSaisis();
    }

    public String getJoursSaisis() {
        return absence.getJoursSaisis();
    }

    public final String getNombreDeJours() {
        return nombreDeJours;
    }

    @Override
    public String getSpy() {
        return absence.getSpy();
    }

    public JadeCodeSysteme getTypeAbsenceCS() {
        return typeAbsenceCS;
    }

    public final void setAbsence(IJSimpleAbsence absence) {
        this.absence = absence;
    }

    public void setCodeAbsence(String codeAbsence) {
        absence.setCodeAbsence(codeAbsence);
    }

    public final void setCreationSpy(String creationSpy) {
        absence.setCreationSpy(creationSpy);
    }

    public void setDateDeDebut(String dateDeDebut) {
        absence.setDateDeDebut(dateDeDebut);
    }

    public void setDateDeFin(String dateDeFin) {
        absence.setDateDeFin(dateDeFin);
    }

    public void setDossier(IJSimpleDossierControleAbsences dossier) {
        this.dossier = dossier;
    }

    @Override
    public void setId(String id) {
        absence.setId(id);
    }

    public void setIdAbsence(String idAbsence) {
        absence.setIdAbsence(idAbsence);
    }

    public void setIdBaseIndemnisation(String idBaseIndemnisation) {
        absence.setIdBaseIndemnisation(idBaseIndemnisation);
    }

    public void setIdDossierControleAbsences(String idDossierControleAbsences) {
        absence.setIdDossierControle(idDossierControleAbsences);
        dossier.setIdDossierControleAbsences(idDossierControleAbsences);
    }

    public final void setJoursInterruption(String joursInterruption) {
        this.joursInterruption = joursInterruption;
    }

    public final void setJoursNonPaye(String joursNonPaye) {
        this.joursNonPaye = joursNonPaye;
    }

    public void setJoursNonPayeSaisis(String joursNonPayeSaisis) {
        absence.setJoursNonPayeSaisis(joursNonPayeSaisis);
    }

    public void setJoursSaisis(String joursSaisis) {
        absence.setJoursSaisis(joursSaisis);
    }

    public final void setNombreDeJours(String nombreDeJours) {
        this.nombreDeJours = nombreDeJours;
    }

    @Override
    public void setSpy(String spy) {
        absence.setSpy(spy);
    }

    public void setTypeAbsenceCS(JadeCodeSysteme typeAbsenceCS) {
        this.typeAbsenceCS = typeAbsenceCS;
    }
}
