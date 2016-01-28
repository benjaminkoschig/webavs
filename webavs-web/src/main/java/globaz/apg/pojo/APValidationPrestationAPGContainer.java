package globaz.apg.pojo;

import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APPrestation;
import java.util.List;

public class APValidationPrestationAPGContainer {
    private APDroitLAPG droit;
    private APPrestation prestation;
    private APAnnonceAPG annonce;
    private List<ViolatedRule> validationErrors;

    public final APDroitLAPG getDroit() {
        return droit;
    }

    public final void setDroit(APDroitLAPG droit) {
        this.droit = droit;
    }

    public final APPrestation getPrestation() {
        return prestation;
    }

    public final void setPrestation(APPrestation prestation) {
        this.prestation = prestation;
    }

    public final APAnnonceAPG getAnnonce() {
        return annonce;
    }

    public final void setAnnonce(APAnnonceAPG annonce) {
        this.annonce = annonce;
    }

    public final List<ViolatedRule> getValidationErrors() {
        return validationErrors;
    }

    public final void setValidationErrors(List<ViolatedRule> validationErrors) {
        this.validationErrors = validationErrors;
    }

    /**
     * Informe si, lors de la validation des prestation, des erreurs sont survenue
     * 
     * @return si des erreurs de validation sont survenue lors de la validation des prestation
     */
    public boolean getHasValidationError() {
        if ((validationErrors != null) && (validationErrors.size() > 0)) {
            return true;
        }
        return false;
    }

    /**
     * Informe si, lors de la validation de la prestation, des erreurs non 'quittançable' sont survenue
     * 
     * @return si des erreurs de validation non 'quittançable' sont survenue lors de la validation de la prestation
     */
    public boolean getHasUnbreakableValidationError() {
        if ((validationErrors != null) && (validationErrors.size() > 0)) {
            for (ViolatedRule violatedRule : validationErrors) {
                if (!violatedRule.getBreakable()) {
                    return true;
                }
            }
        }
        return false;
    }
}
