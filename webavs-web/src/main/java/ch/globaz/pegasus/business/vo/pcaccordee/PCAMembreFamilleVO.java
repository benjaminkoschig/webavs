package ch.globaz.pegasus.business.vo.pcaccordee;

public class PCAMembreFamilleVO {
    private String csNationalite = null;
    private String csRoleFamillePC = null;
    private String csSexe = null;
    private String dateNaissance = null;
    private String idTiers = null;
    private Boolean isComprisDansCalcul = null;
    private Boolean isRentier = null;
    private Boolean isRequerantEnfant = Boolean.FALSE;
    private String nom = null;
    private String nss = null;
    private String prenom = null;

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsRoleFamillePC() {
        return csRoleFamillePC;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsComprisDansCalcul() {
        return isComprisDansCalcul;
    }

    public Boolean getIsRequerantEnfant() {
        return isRequerantEnfant;
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

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsRoleFamillePC(String csRoleFamillePC) {
        this.csRoleFamillePC = csRoleFamillePC;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsComprisDansCalcul(Boolean isComprisDansCalcul) {
        this.isComprisDansCalcul = isComprisDansCalcul;
    }

    public void setIsRequerantEnfant(Boolean isRequerantEnfant) {
        this.isRequerantEnfant = isRequerantEnfant;
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

    public Boolean getIsRentier() {
        return isRentier;
    }

    public void setIsRentier(Boolean rentier) {
        isRentier = rentier;
    }
}
