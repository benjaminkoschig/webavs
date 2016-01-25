package ch.globaz.prestation.business.models.recap;

import globaz.framework.util.FWCurrency;

public class RecapitulationPcRfm {
    private FWCurrency montantTotalPCAI;
    private FWCurrency montantTotalPCAVS;
    private FWCurrency montantTotalRFMAI;
    private FWCurrency montantTotalRFMAVS;
    private int nbPrestationPCAI;
    private int nbPrestationPCAVS;
    private int nbPrestationRFMAI;
    private int nbPrestationRFMAVS;

    public RecapitulationPcRfm() {
        super();

        montantTotalPCAI = new FWCurrency();
        montantTotalPCAVS = new FWCurrency();
        montantTotalRFMAI = new FWCurrency();
        montantTotalRFMAVS = new FWCurrency();

        nbPrestationPCAI = 0;
        nbPrestationPCAVS = 0;
        nbPrestationRFMAI = 0;
        nbPrestationRFMAVS = 0;
    }

    public FWCurrency getMontantTotalPCAI() {
        return montantTotalPCAI;
    }

    public FWCurrency getMontantTotalPCAVS() {
        return montantTotalPCAVS;
    }

    public FWCurrency getMontantTotalRFMAI() {
        return montantTotalRFMAI;
    }

    public FWCurrency getMontantTotalRFMAVS() {
        return montantTotalRFMAVS;
    }

    public int getNbPrestationPCAI() {
        return nbPrestationPCAI;
    }

    public int getNbPrestationPCAVS() {
        return nbPrestationPCAVS;
    }

    public int getNbPrestationRFMAI() {
        return nbPrestationRFMAI;
    }

    public int getNbPrestationRFMAVS() {
        return nbPrestationRFMAVS;
    }

    public void setMontantTotalPCAI(FWCurrency montantTotalPCAI) {
        this.montantTotalPCAI = montantTotalPCAI;
    }

    public void setMontantTotalPCAVS(FWCurrency montantTotalPCAVS) {
        this.montantTotalPCAVS = montantTotalPCAVS;
    }

    public void setMontantTotalRFMAI(FWCurrency montantTotalRFMAI) {
        this.montantTotalRFMAI = montantTotalRFMAI;
    }

    public void setMontantTotalRFMAVS(FWCurrency montantTotalRFMAVS) {
        this.montantTotalRFMAVS = montantTotalRFMAVS;
    }

    public void setNbPrestationPCAI(int nbPrestationPCAI) {
        this.nbPrestationPCAI = nbPrestationPCAI;
    }

    public void setNbPrestationPCAVS(int nbPrestationPCAVS) {
        this.nbPrestationPCAVS = nbPrestationPCAVS;
    }

    public void setNbPrestationRFMAI(int nbPrestationRFMAI) {
        this.nbPrestationRFMAI = nbPrestationRFMAI;
    }

    public void setNbPrestationRFMAVS(int nbPrestationRFMAVS) {
        this.nbPrestationRFMAVS = nbPrestationRFMAVS;
    }
}
