package globaz.corvus.helpers.decisions;

import globaz.framework.util.FWCurrency;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author SCR
 * 
 * 
 */
public class REOVInterDecisionVO {

    /*
     * 
     * Inner Class
     * 
     * Représente la dette d'une REC à compenser d'un autre membre de la famille
     */
    public class ExtRenteEnCoursACompenser {
        private String idOV = "";
        private String idTiers = "";

        // Info pour les compensations inter-decisions
        private String idTiersCompensationInterDecision = "";
        private FWCurrency montantCompense = null;

        // private String idOVCompensation = "";

        public String getIdOV() {
            return idOV;
        }

        public String getIdTiers() {
            return idTiers;
        }

        public String getIdTiersCompensationInterDecision() {
            return idTiersCompensationInterDecision;
        }

        public FWCurrency getMontantCompense() {
            return montantCompense;
        }

        public void setIdOV(String idOV) {
            this.idOV = idOV;
        }

        public void setIdTiers(String idTiers) {
            this.idTiers = idTiers;
        }

        public void setIdTiersCompensationInterDecision(String idTiersCompensationInterDecision) {
            this.idTiersCompensationInterDecision = idTiersCompensationInterDecision;
        }

        public void setMontantCompense(FWCurrency montantCompense) {
            this.montantCompense = montantCompense;
        }

    }

    private List dettesCompenseeExt = null;
    private String idOV = "";
    private String idPrestation = "";
    private String idTiers = "";

    private FWCurrency montantCompense = null;
    private FWCurrency montantDette = null;
    private FWCurrency montantRetro = null;
    private String priority = "";
    private FWCurrency solde = null;

    public void addDettesExtACompenser(String idTiers, String idOV, String montantCompense,
            String idTiersCompensationInterDecision) {
        if (dettesCompenseeExt == null) {
            dettesCompenseeExt = new ArrayList();
        }

        ExtRenteEnCoursACompenser elm = new ExtRenteEnCoursACompenser();
        elm.idTiers = idTiers;
        elm.montantCompense = new FWCurrency(montantCompense);
        elm.idOV = idOV;
        elm.idTiersCompensationInterDecision = idTiersCompensationInterDecision;
        dettesCompenseeExt.add(elm);
    }

    public List getDettesCompenseeExt() {
        return dettesCompenseeExt;
    }

    public String getIdOV() {
        return idOV;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public FWCurrency getMontantCompense() {
        return montantCompense;
    }

    public FWCurrency getMontantDette() {
        return montantDette;
    }

    public FWCurrency getMontantRetro() {
        return montantRetro;
    }

    public String getPriority() {
        return priority;
    }

    public FWCurrency getSolde() {
        return solde;
    }

    public void setIdOV(String idOV) {
        this.idOV = idOV;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantCompense(FWCurrency montantCompense) {
        this.montantCompense = montantCompense;
    }

    public void setMontantDette(FWCurrency montantDette) {
        this.montantDette = montantDette;
    }

    public void setMontantRetro(FWCurrency montantRetro) {
        this.montantRetro = montantRetro;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setSolde(FWCurrency solde) {
        this.solde = solde;
    }

}
