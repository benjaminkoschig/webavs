package ch.globaz.vulpecula.process.comptabilite;

public class SoldesCPPAssociationDTO {
    private String nomCppAssociation;
    private String numSection;
    private String idExterneRole;
    private String idTiers;
    private String description;
    private String solde;

    public String getNomCppAssociation() {
        return nomCppAssociation;
    }

    public void setNomCppAssociation(String nomCppAssociation) {
        this.nomCppAssociation = nomCppAssociation;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSolde() {
        return solde;
    }

    public void setSolde(String solde) {
        this.solde = solde;
    }

    public String getNumSection() {
        return numSection;
    }

    public void setNumSection(String numSection) {
        this.numSection = numSection;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}
