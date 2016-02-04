package globaz.perseus.process.facture;

import ch.globaz.perseus.business.models.qd.Facture;

public class FactureWrapper {
    private Facture facture;
    private String errorMessage;

    public FactureWrapper(Facture facture, String errorMsg) {
        this.facture = facture;
        errorMessage = errorMsg;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
