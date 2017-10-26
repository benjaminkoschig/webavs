package ch.globaz.pegasus.rpc.process;

public class ToleranceDifferenceAnnonce {
    private boolean depasserTolerance;
    private double toleranceAnnonce;

    public ToleranceDifferenceAnnonce() {
        depasserTolerance = false;
        toleranceAnnonce = 0.2;
    }

    public ToleranceDifferenceAnnonce(boolean depasserTolerance, double toleranceAnnonce) {
        this.depasserTolerance = depasserTolerance;
        this.toleranceAnnonce = toleranceAnnonce;
    }

    public double getToleranceAnnonce() {
        return toleranceAnnonce;
    }

    public boolean isDepasserTolerance() {
        return depasserTolerance;
    }

    public void setDepasserTolerance(boolean depasserTolerance) {
        this.depasserTolerance = depasserTolerance;
    }

    public void setToleranceAnnonce(double toleranceAnnonce) {
        this.toleranceAnnonce = toleranceAnnonce;
    }
}
