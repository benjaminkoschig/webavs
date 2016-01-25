package globaz.hercule.process.statOfas;

import globaz.framework.util.FWCurrency;

public class CEStatOfasResultObject {
    private FWCurrency masseSalariale;
    private int nbCas;

    public CEStatOfasResultObject() {
        nbCas = 0;
        masseSalariale = new FWCurrency(0);
    }

    public void addCas() {
        nbCas++;
    }

    public void addMasseSalariale(FWCurrency value) {
        masseSalariale.add(value);
    }

    public FWCurrency getMasseSalariale() {
        return masseSalariale;
    }

    public int getNbCas() {
        return nbCas;
    }

    public void setMasseSalariale(FWCurrency masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public void setNbCas(int nbCas) {
        this.nbCas = nbCas;
    }
}
