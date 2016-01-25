package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;

/**
 * Une r�partition d'une {@link Creance cr�ance}. Ce qui veut dire qu'une partie du montant r�clam� par le cr�ancier est
 * pay� par le montant r�troactif de la prestation accord�e li�e � cette r�partition
 */
public class RepartitionCreance extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Creance creance;
    private BigDecimal montantReparti;

    public RepartitionCreance() {
        super();

        creance = new Creance();
        montantReparti = BigDecimal.ZERO;
    }

    /**
     * @return la cr�ance dont d�coule cette r�partition de cr�ance
     */
    public Creance getCreance() {
        return creance;
    }

    /**
     * @return le montant qui sera compens� sur la {@link #getCreance() cr�ance} par cette r�partition
     */
    public BigDecimal getMontantReparti() {
        return montantReparti;
    }

    /**
     * (re-)d�fini la cr�ance dont d�coule cette r�partition
     * 
     * @param creance
     *            une cr�ance non-null et initialis�e
     * @throws NullPointerException
     *             si la cr�ance pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la cr�ance pass�e en param�tre n'est pas initialis�e
     */
    public void setCreance(final Creance creance) {
        Checkers.checkNotNull(creance, "creance");
        Checkers.checkHasID(creance, "creance");
        this.creance = creance;
    }

    /**
     * <p>
     * (re-)d�fini le montant ponctionn� sur le r�troactif de la prestation accord�e pour payer le cr�ancier
     * </p>
     * <p>
     * Ce montant ne peut pas �tre plus grand que le r�troactif � disposition (donc le montant r�troactif de la
     * prestation accord�e moins les potentiels autres r�parations de cr�ance)
     * </p>
     * 
     * @param montantReparti
     *            un montant non-null et plus grand que z�ro
     * @throws NullPointerException
     *             si le montant pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le montant pass� en param�tre est n�gatif, ou si la somme des montant r�parti de la prestation
     *             accord�e d�passe le montant r�troactif disponible
     */
    public void setMontantReparti(final BigDecimal montantReparti) {
        Checkers.checkNotNull(montantReparti, "montantReparti");
        // TODO : impl�menter le test pour le montant
        this.montantReparti = montantReparti;
    }
}
