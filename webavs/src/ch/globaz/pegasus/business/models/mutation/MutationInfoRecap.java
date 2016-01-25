package ch.globaz.pegasus.business.models.mutation;

public class MutationInfoRecap {
    private String montantTotalPCAI;
    private String montantTotalPCAVS;
    private String nbPrestationPCAI;
    private String nbPrestationPCAvs;

    public String getMontantTotalPCAI() {
        return montantTotalPCAI;
    }

    public String getMontantTotalPCAVS() {
        return montantTotalPCAVS;
    }

    public String getNbPrestationPCAI() {
        return nbPrestationPCAI;
    }

    public String getNbPrestationPCAvs() {
        return nbPrestationPCAvs;
    }

    public void setMontantTotalPCAI(String montantTotalPCAI) {
        this.montantTotalPCAI = montantTotalPCAI;
    }

    public void setMontantTotalPCAVS(String montantTotalPCAVS) {
        this.montantTotalPCAVS = montantTotalPCAVS;
    }

    public void setNbPrestationPCAI(String nbPrestationPCAI) {
        this.nbPrestationPCAI = nbPrestationPCAI;
    }

    public void setNbPrestationPCAvs(String nbPrestationPCAvs) {
        this.nbPrestationPCAvs = nbPrestationPCAvs;
    }
}
