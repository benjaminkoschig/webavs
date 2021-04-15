package globaz.musca.api.musca;

import java.util.Objects;

public class PaireIdExterneEBill {

    private String idExterneRole;
    private String idExterneFactureCompensation;
    private String montant;

    public PaireIdExterneEBill(String idExterneRole, String idExterneFactureCompensation) {
        this.idExterneRole = idExterneRole;
        this.idExterneFactureCompensation = idExterneFactureCompensation;
    }

    public PaireIdExterneEBill(String idExterneRole, String idExterneFactureCompensation, String montant) {
        this.idExterneRole = idExterneRole;
        this.idExterneFactureCompensation = idExterneFactureCompensation;
        this.montant = montant;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public String getIdExterneFactureCompensation() {
        return idExterneFactureCompensation;
    }

    public void setIdExterneFactureCompensation(String idExterneFactureCompensation) {
        this.idExterneFactureCompensation = idExterneFactureCompensation;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaireIdExterneEBill that = (PaireIdExterneEBill) o;
        return idExterneRole.equals(that.idExterneRole) && idExterneFactureCompensation.equals(that.idExterneFactureCompensation) && Objects.equals(montant, that.montant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idExterneRole, idExterneFactureCompensation, montant);
    }
}