package globaz.apg.process;

import globaz.framework.util.FWCurrency;

public class MontantsPorterEnCompte {
    private FWCurrency montantTotal = null;
    private FWCurrency montantPorterEnCompte = null;

    public MontantsPorterEnCompte() {
        montantTotal = new FWCurrency();
        montantPorterEnCompte = new FWCurrency();
    }

    public void setMontantTotal(FWCurrency montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setMontantPorterEnCompte(FWCurrency montantPorterEnCompte) {
        this.montantPorterEnCompte = montantPorterEnCompte;
    }

    public FWCurrency getMontantPorterEnCompte() {
        return montantPorterEnCompte;
    }

    public FWCurrency getMontantTotal() {
        return montantTotal;
    }
}
