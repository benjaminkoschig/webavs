package ch.globaz.libra.business.model;

public class Echeance {

    private String csDomaine = null;
    private String csEtat = null;
    private String csGroupe = null;
    private String csNationalite = null;
    private String csSexe = null;
    private String dateDeces = null;
    private String dateNaissance = null;
    private String dateRappel = null;
    private String dateReception = null;
    // TABLE Dossiers (LIDOSSI)
    private String idDossier = null;
    private String idExterne = null;
    private String idGestionnaire = null;
    private String idJournalisation = null;
    private String idTiers = null;
    private String libelle = null;
    private String libelleAffichage = null;
    private String libelleGroupe = null;
    // Autres champs nécessaires
    private String noAVS = null;
    private String nom = null;
    private String prenom = null;
    private String visaGestionnaire = null;

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsGroupe() {
        return csGroupe;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateRappel() {
        return dateRappel;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idExterne
     */
    public String getIdExterne() {
        return idExterne;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdJournalisation() {
        return idJournalisation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getLibelleAffichage() {
        return libelleAffichage;
    }

    public String getLibelleGroupe() {
        return libelleGroupe;
    }

    public String getNoAVS() {
        return noAVS;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getVisaGestionnaire() {
        return visaGestionnaire;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsGroupe(String csGroupe) {
        this.csGroupe = csGroupe;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateRappel(String dateRappel) {
        this.dateRappel = dateRappel;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idExterne
     *            the idExterne to set
     */
    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdJournalisation(String idJournalisation) {
        this.idJournalisation = idJournalisation;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setLibelleAffichage(String libelleAffichage) {
        this.libelleAffichage = libelleAffichage;
    }

    public void setLibelleGroupe(String libelleGroupe) {
        this.libelleGroupe = libelleGroupe;
    }

    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setVisaGestionnaire(String visaGestionnaire) {
        this.visaGestionnaire = visaGestionnaire;
    }
}
