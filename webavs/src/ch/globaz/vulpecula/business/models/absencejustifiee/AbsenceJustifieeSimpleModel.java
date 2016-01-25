package ch.globaz.vulpecula.business.models.absencejustifiee;

import globaz.jade.persistence.model.JadeSimpleModel;

public class AbsenceJustifieeSimpleModel extends JadeSimpleModel {

    private static final long serialVersionUID = 1L;

    private String id;
    private String idPosteTravail;
    private String type;
    private String etat;
    private String dateDebutAbsence;
    private String dateFinAbsence;
    private String montantBrut;
    private String montantVerse;
    private String tauxAVS;
    private String tauxAC;
    private String beneficiaire;
    private String lienParente;
    private String idPassageFacturation;
    private String nombreDeJours;
    private String nombreHeuresParJour;
    private String salaireHoraire;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getDateDebutAbsence() {
        return dateDebutAbsence;
    }

    public void setDateDebutAbsence(String dateDebutAbsence) {
        this.dateDebutAbsence = dateDebutAbsence;
    }

    public String getDateFinAbsence() {
        return dateFinAbsence;
    }

    public void setDateFinAbsence(String dateFinAbsence) {
        this.dateFinAbsence = dateFinAbsence;
    }

    public String getMontantBrut() {
        return montantBrut;
    }

    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

    public String getMontantVerse() {
        return montantVerse;
    }

    public void setMontantVerse(String montantVerse) {
        this.montantVerse = montantVerse;
    }

    public String getTauxAVS() {
        return tauxAVS;
    }

    public void setTauxAVS(String tauxAVS) {
        this.tauxAVS = tauxAVS;
    }

    public String getTauxAC() {
        return tauxAC;
    }

    public void setTauxAC(String tauxAC) {
        this.tauxAC = tauxAC;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public String getLienParente() {
        return lienParente;
    }

    public void setLienParente(String lienParente) {
        this.lienParente = lienParente;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public String getNombreDeJours() {
        return nombreDeJours;
    }

    public void setNombreDeJours(String nombreDeJours) {
        this.nombreDeJours = nombreDeJours;
    }

    public String getNombreHeuresParJour() {
        return nombreHeuresParJour;
    }

    public void setNombreHeuresParJour(String nombreHeuresParJour) {
        this.nombreHeuresParJour = nombreHeuresParJour;
    }

    public String getSalaireHoraire() {
        return salaireHoraire;
    }

    public void setSalaireHoraire(String salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }
}
