package ch.globaz.pegasus.business.vo.adresse;

import java.io.Serializable;

public class AdressePaiement implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Banque banque;
    private Tiers tiers;

    public Banque getBanque() {
        return banque;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setBanque(Banque banque) {
        this.banque = banque;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }
}
