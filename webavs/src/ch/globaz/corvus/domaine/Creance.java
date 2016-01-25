package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.pyxis.domaine.Tiers;

/**
 * Un montant li� � un cr�ancier
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
     * @return le tiers � qui l'argent est d�
     */
    public Tiers getCreancier() {
        return creancier;
    }

    /**
     * @return le montant de la cr�ance
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * (re-)d�fini le tiers � qui l'argent est d�
     * 
     * @param creancier
     *            un tiers
     * @throws NullPointerException
     *             si le tiers pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le tiers pass� en param�tre n'est pas initialis�
     */
    public void setCreancier(final Tiers creancier) {
        Checkers.checkNotNull(creancier, "creance.creancier");
        Checkers.checkHasID(creancier, "creance.creancier");
        this.creancier = creancier;
    }

    /**
     * (re-)d�fini le montant de la cr�ance
     * 
     * @param montant
     *            un montant
     * @throws NullPointerException
     *             si le montant pass� en param�tre est null
     */
    public void setMontant(final BigDecimal montant) {
        Checkers.checkNotNull(montant, "creance.montant");
        this.montant = montant;
    }
}
