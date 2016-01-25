package ch.globaz.pegasus.process.statistiquesOFAS;

public class StatistiquesOFASIdentification {
    private float montantPc = 0;
    private String numAvs = null;
    private String numAvsConj = null;
    private String numAvsDansHome = null;

    public float getMontantPc() {
        return montantPc;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getNumAvsConj() {
        return numAvsConj;
    }

    public String getNumAvsDansHome() {
        return numAvsDansHome;
    }

    public void setMontantPc(float montantPc) {
        this.montantPc = montantPc;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setNumAvsConj(String numAvsConj) {
        this.numAvsConj = numAvsConj;
    }

    public void setNumAvsDansHome(String numAvsDansHome) {
        this.numAvsDansHome = numAvsDansHome;
    }
}
