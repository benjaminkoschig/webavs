package ch.globaz.pegasus.business.models.avance;

import globaz.jade.persistence.model.JadeAbstractModel;

public class AvanceVo extends JadeAbstractModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDomaine = "";
    private String csDomaineAvance = "";

    private String csEtat1erAcompte = "";

    private String csEtatAcomptes = "";
    private String dateDebutAcompte = "";
    private String dateDebutPmt1erAcompte = "";
    private String dateFinAcompte = "";
    private String dateNaissance = "";
    private String datePmt1erAcompte = "";
    private String idAffilie = "";
    private String idAvance = "";
    private String idTiersAdrPmt = "";
    private String idTiersBeneficiaire = "";
    private String libelle = "";
    private String montant1erAcompte = "";
    private String montantMensuel = "";
    private String nationalite = "";
    private String nom = "";
    protected String nss = "";
    private String prenom = "";
    private String sexe = "";

    public AvanceVo() {

    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getCsDomaineAvance() {
        return csDomaineAvance;
    }

    public String getCsEtat1erAcompte() {
        return csEtat1erAcompte;
    }

    public String getCsEtatAcomptes() {
        return csEtatAcomptes;
    }

    public String getDateDebutAcompte() {
        return dateDebutAcompte;
    }

    public String getDateDebutPmt1erAcompte() {
        return dateDebutPmt1erAcompte;
    }

    public String getDateFinAcompte() {
        return dateFinAcompte;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDatePmt1erAcompte() {
        return datePmt1erAcompte;
    }

    @Override
    public String getId() {
        return getIdAvance();
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdAvance() {
        return idAvance;
    }

    public String getIdTiersAdrPmt() {
        return idTiersAdrPmt;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMontant1erAcompte() {
        return montant1erAcompte;
    }

    public String getMontantMensuel() {
        return montantMensuel;
    }

    public String getNationalite() {
        return nationalite;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSexe() {
        return sexe;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setCsDomaineAvance(String csDomaineAvance) {
        this.csDomaineAvance = csDomaineAvance;
    }

    public void setCsEtat1erAcompte(String csEtat1erAcompte) {
        this.csEtat1erAcompte = csEtat1erAcompte;
    }

    public void setCsEtatAcomptes(String csEtatAcomptes) {
        this.csEtatAcomptes = csEtatAcomptes;
    }

    public void setDateDebutAcompte(String dateDebutAcompte) {
        this.dateDebutAcompte = dateDebutAcompte;
    }

    public void setDateDebutPmt1erAcompte(String dateDebutPmt1erAcompte) {
        this.dateDebutPmt1erAcompte = dateDebutPmt1erAcompte;
    }

    public void setDateFinAcompte(String dateFinAcompte) {
        this.dateFinAcompte = dateFinAcompte;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDatePmt1erAcompte(String datePmt1erAcompte) {
        this.datePmt1erAcompte = datePmt1erAcompte;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdAvance(String idAvance) {
        this.idAvance = idAvance;
    }

    public void setIdTiersAdrPmt(String idTiersAdrPmt) {
        this.idTiersAdrPmt = idTiersAdrPmt;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant1erAcompte(String montant1erAcompte) {
        this.montant1erAcompte = montant1erAcompte;
    }

    public void setMontantMensuel(String montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

}
