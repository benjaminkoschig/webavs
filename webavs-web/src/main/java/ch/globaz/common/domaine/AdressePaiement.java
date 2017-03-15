/*
 * Globaz SA
 */
package ch.globaz.common.domaine;

import java.io.Serializable;

public class AdressePaiement implements Serializable {

    private static final long serialVersionUID = 1L;
    private Banque banque;
    private Tiers tiers;

    public AdressePaiement(Banque banque, Tiers tiers) {
        super();
        this.banque = banque;
        this.tiers = tiers;
    }

    public Banque getBanque() {
        return banque;
    }

    public Tiers getTiers() {
        return tiers;
    }
}
