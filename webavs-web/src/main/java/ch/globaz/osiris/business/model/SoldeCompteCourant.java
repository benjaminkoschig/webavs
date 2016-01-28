package ch.globaz.osiris.business.model;

public class SoldeCompteCourant {
    private String description;
    private String idExterneCompteCourant;
    private String idExterneRole;
    private String idExterneSection;
    private String idSection;
    private String montant;

    public String getDescription() {
        return description;
    }

    public String getIdExterneCompteCourant() {
        return idExterneCompteCourant;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public String getIdExterneSection() {
        return idExterneSection;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getMontant() {
        return montant;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdExterneCompteCourant(String idExterneCompteCourant) {
        this.idExterneCompteCourant = idExterneCompteCourant;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public void setIdExterneSection(String idExterneSection) {
        this.idExterneSection = idExterneSection;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
