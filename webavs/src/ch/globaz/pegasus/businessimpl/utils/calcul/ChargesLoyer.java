package ch.globaz.pegasus.businessimpl.utils.calcul;

public class ChargesLoyer {

    private String cleDepenseMontantCharges = null;
    private float montantCharges = 0.0f;

    public ChargesLoyer(String cleDepenseMontantCharges, float montantCharges) {
        super();
        this.cleDepenseMontantCharges = cleDepenseMontantCharges;
        this.montantCharges = montantCharges;
    }

    public String getCleDepenseMontantCharges() {
        return cleDepenseMontantCharges;
    }

    public float getMontantCharges() {
        return montantCharges;
    }

    public void setCleDepenseMontantCharges(String cleDepenseMontantCharges) {
        this.cleDepenseMontantCharges = cleDepenseMontantCharges;
    }

    public void setMontantCharges(float montantCharges) {
        this.montantCharges = montantCharges;
    }

}
