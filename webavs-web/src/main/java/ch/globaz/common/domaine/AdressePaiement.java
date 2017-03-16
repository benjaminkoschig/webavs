/*
 * Globaz SA
 */
package ch.globaz.common.domaine;

import java.io.Serializable;

public class AdressePaiement implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Banque banque;
    private final Tiers tiers;
    private final String idAvoirPaiementUnique;

    public AdressePaiement(Banque banque, Tiers tiers, String idAvoirPaiementUnique) {
        this.banque = banque;
        this.tiers = tiers;
        this.idAvoirPaiementUnique = idAvoirPaiementUnique;
    }

    public Banque getBanque() {
        return banque;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public String getIdAvoirPaiementUnique() {
        return idAvoirPaiementUnique;
    }

}
