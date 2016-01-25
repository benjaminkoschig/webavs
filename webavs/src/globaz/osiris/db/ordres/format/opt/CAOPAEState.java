package globaz.osiris.db.ordres.format.opt;

import globaz.framework.util.FWCurrency;

public class CAOPAEState {
    private int nbTransation = 0;
    private FWCurrency somme = new FWCurrency(0);

    public void addToSomme(FWCurrency montant) {
        getSomme().add(montant);

    }

    public int getNbTransation() {
        return nbTransation;
    }

    public FWCurrency getSomme() {
        return somme;
    }

    public void incNbTransation() {
        nbTransation++;

    }

    public void setNbTransation(int nbTransation) {
        this.nbTransation = nbTransation;
    }

    public void setSomme(FWCurrency somme) {
        this.somme = somme;
    }
}
