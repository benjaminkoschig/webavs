package ch.globaz.vulpecula.domain.models.communicationsalaires;

public class CommunicationSalairesRetaval {

    private String codeConvention;
    private String numAffilie;
    private String raisonSocial;
    private String idTiers;
    private String idPosteTravail;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String nss;
    private String masse;

    public String getCodeConvention() {
        return codeConvention;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getRaisonSocial() {
        return raisonSocial;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getNss() {
        return nss;
    }

    public String getMasse() {
        return masse;
    }

    public void setCodeConvention(String codeConvention) {
        this.codeConvention = codeConvention;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setMasse(String masse) {
        this.masse = masse;
    }
}
