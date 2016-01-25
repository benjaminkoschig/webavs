package ch.globaz.vulpecula.business.models.servicemilitaire;

import globaz.jade.persistence.model.JadeSimpleModel;

public class ServiceMilitaireSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 1L;

    private String id;
    private String idPosteTravail;
    private String idPassageFacturation;
    private String genre;
    private String dateDebut;
    private String dateFin;
    private String beneficiaire;
    private String etat;
    private String nbJours;
    private String nbHeuresJour;
    private String salaireHoraire;
    private String couvertureAPG;
    private String versementAPG;
    private String compensationAPG;
    private String montantBrut;
    private String montantAVerser;
    private String baseSalaire;
    private String tauxCP;
    private String tauxGratification;

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

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getNbJours() {
        return nbJours;
    }

    public void setNbJours(String nbJours) {
        this.nbJours = nbJours;
    }

    public String getNbHeuresJour() {
        return nbHeuresJour;
    }

    public void setNbHeuresJour(String nbHeuresJour) {
        this.nbHeuresJour = nbHeuresJour;
    }

    public String getSalaireHoraire() {
        return salaireHoraire;
    }

    public void setSalaireHoraire(String salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public String getCouvertureAPG() {
        return couvertureAPG;
    }

    public void setCouvertureAPG(String couvertureAPG) {
        this.couvertureAPG = couvertureAPG;
    }

    public String getVersementAPG() {
        return versementAPG;
    }

    public void setVersementAPG(String versementAPG) {
        this.versementAPG = versementAPG;
    }

    public String getCompensationAPG() {
        return compensationAPG;
    }

    public void setCompensationAPG(String compensationAPG) {
        this.compensationAPG = compensationAPG;
    }

    public String getMontantBrut() {
        return montantBrut;
    }

    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

    public String getMontantAVerser() {
        return montantAVerser;
    }

    public void setMontantAVerser(String montantAVerser) {
        this.montantAVerser = montantAVerser;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBaseSalaire() {
        return baseSalaire;
    }

    public void setBaseSalaire(String baseSalaire) {
        this.baseSalaire = baseSalaire;
    }

    public String getTauxCP() {
        return tauxCP;
    }

    public void setTauxCP(String tauxCP) {
        this.tauxCP = tauxCP;
    }

    public String getTauxGratification() {
        return tauxGratification;
    }

    public void setTauxGratification(String tauxGratification) {
        this.tauxGratification = tauxGratification;
    }
}
