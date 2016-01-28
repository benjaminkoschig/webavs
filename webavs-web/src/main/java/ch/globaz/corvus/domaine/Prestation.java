package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;

public final class Prestation extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal montant;
    private Set<OrdreVersement> ordresVersement;

    public Prestation() {
        super();

        montant = BigDecimal.ZERO;
        ordresVersement = new HashSet<OrdreVersement>();
    }

    /**
     * @return le montant pour cette prestation
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * @return la liste des ordres de versement dans un conteneur invariable
     */
    public final Set<OrdreVersement> getOrdresVersement() {
        return Collections.unmodifiableSet(ordresVersement);
    }

    /**
     * (re-)d�fini le montant de cette prestation
     * 
     * @param montant
     * @throws NullPointerException
     *             si le montant pass� en param�tre est null
     */
    public void setMontant(final BigDecimal montant) {
        Checkers.checkNotNull(montant, "prestation.montant");
        this.montant = montant;
    }

    /**
     * (re-)d�fini la liste des ordres de versement li�s � cette prestation
     * 
     * @param ordresVersement
     *            la liste des ordres de versement (ne peut pas �tre null)
     * @throws NullPointerException
     *             si la liste pass�e en param�tre est null
     */
    public final void setOrdresVersement(final Set<OrdreVersement> ordresVersement) {
        Checkers.checkNotNull(ordresVersement, "prestation.ordresVersement");
        this.ordresVersement = ordresVersement;
    }
}
