package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.pyxis.domaine.Tiers;

/**
 * Un montant lié à un créancier
 */
public class Creance extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Tiers creancier;
    private BigDecimal montant;

    public Creance() {
        super();

        creancier = new Tiers();
        montant = BigDecimal.ZERO;
    }

    /**
     * @return le tiers à qui l'argent est dû
     */
    public Tiers getCreancier() {
        return creancier;
    }

    /**
     * @return le montant de la créance
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * (re-)défini le tiers à qui l'argent est dû
     * 
     * @param creancier
     *            un tiers
     * @throws NullPointerException
     *             si le tiers passé en paramètre est null
     * @throws IllegalArgumentException
     *             si le tiers passé en paramètre n'est pas initialisé
     */
    public void setCreancier(final Tiers creancier) {
        Checkers.checkNotNull(creancier, "creance.creancier");
        Checkers.checkHasID(creancier, "creance.creancier");
        this.creancier = creancier;
    }

    /**
     * (re-)défini le montant de la créance
     * 
     * @param montant
     *            un montant
     * @throws NullPointerException
     *             si le montant passé en paramètre est null
     */
    public void setMontant(final BigDecimal montant) {
        Checkers.checkNotNull(montant, "creance.montant");
        this.montant = montant;
    }
}
