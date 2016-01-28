package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;

/**
 * Une répartition d'une {@link Creance créance}. Ce qui veut dire qu'une partie du montant réclamé par le créancier est
 * payé par le montant rétroactif de la prestation accordée liée à cette répartition
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
     * @return la créance dont découle cette répartition de créance
     */
    public Creance getCreance() {
        return creance;
    }

    /**
     * @return le montant qui sera compensé sur la {@link #getCreance() créance} par cette répartition
     */
    public BigDecimal getMontantReparti() {
        return montantReparti;
    }

    /**
     * (re-)défini la créance dont découle cette répartition
     * 
     * @param creance
     *            une créance non-null et initialisée
     * @throws NullPointerException
     *             si la créance passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la créance passée en paramètre n'est pas initialisée
     */
    public void setCreance(final Creance creance) {
        Checkers.checkNotNull(creance, "creance");
        Checkers.checkHasID(creance, "creance");
        this.creance = creance;
    }

    /**
     * <p>
     * (re-)défini le montant ponctionné sur le rétroactif de la prestation accordée pour payer le créancier
     * </p>
     * <p>
     * Ce montant ne peut pas être plus grand que le rétroactif à disposition (donc le montant rétroactif de la
     * prestation accordée moins les potentiels autres réparations de créance)
     * </p>
     * 
     * @param montantReparti
     *            un montant non-null et plus grand que zéro
     * @throws NullPointerException
     *             si le montant passé en paramètre est null
     * @throws IllegalArgumentException
     *             si le montant passé en paramètre est négatif, ou si la somme des montant réparti de la prestation
     *             accordée dépasse le montant rétroactif disponible
     */
    public void setMontantReparti(final BigDecimal montantReparti) {
        Checkers.checkNotNull(montantReparti, "montantReparti");
        // TODO : implémenter le test pour le montant
        this.montantReparti = montantReparti;
    }
}
