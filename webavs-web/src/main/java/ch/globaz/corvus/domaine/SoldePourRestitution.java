package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.corvus.domaine.constantes.TypeSoldePourRestitution;

/**
 * Gestion du solde d'une {@link Decision décision} lorsqu'il est négatif (en faveur de la caisse). Permet de choisir si
 * cette dette doit être écrite directement en comptabilité auxiliaire (type
 * {@link TypeSoldePourRestitution#RESTITUTION} réstitution) ou si un certain montant doit être déduit de ce qui sera
 * payé à l'assuré tous les mois (type {@link TypeSoldePourRestitution#RETENUES retenue mensuelle})
 */
public class SoldePourRestitution extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal montantRestitution;
    private BigDecimal montantRetenueMensuelle;
    private OrdreVersement ordreVersement;
    private TypeSoldePourRestitution type;

    public SoldePourRestitution() {
        super();

        montantRetenueMensuelle = BigDecimal.ZERO;
        montantRestitution = BigDecimal.ZERO;
        ordreVersement = new OrdreVersement();
        type = TypeSoldePourRestitution.RESTITUTION;
    }

    /**
     * @return le montant de la restitution (le montant total de la dette)
     */
    public BigDecimal getMontantRestitution() {
        return montantRestitution;
    }

    /**
     * @return le montant devant être retenu tous les mois sur le paiement mensuelle, dans le cas où le type de cette
     *         entité est {@link TypeSoldePourRestitution#RETENUES}
     */
    public BigDecimal getMontantRetenueMensuelle() {
        return montantRetenueMensuelle;
    }

    /**
     * @return l'ordre de versement sur lequel est attaché cette gestion de la restitution
     */
    public OrdreVersement getOrdreVersement() {
        return ordreVersement;
    }

    /**
     * @return le type de resitution qui doit être appliqué
     */
    public TypeSoldePourRestitution getType() {
        return type;
    }

    /**
     * (re-)défini le montant total de cette restitution (la dette totale)
     * 
     * @param montantRestitution
     *            un montant non null
     * @throws NullPointerException
     *             si le montant passé en paramètre est null
     */
    public void setMontantRestitution(final BigDecimal montantRestitution) {
        Checkers.checkNotNull(montantRestitution, "montantRestitution");
        this.montantRestitution = montantRestitution;
    }

    /**
     * <p>
     * (re-)défini le montant à retenir mensuelle.
     * </p>
     * <p>
     * Ne peut être défini à un montant différent de zéro que si le type de cette restitution est
     * {@link TypeSoldePourRestitution#RETENUES}
     * </p>
     * 
     * @param montantRetenueMensuelle
     *            un montant non-null
     * @throws NullPointerException
     *             si le montant passé en paramètre est null
     * @throws IllegalArgumentException
     *             si le montant passé en paramètre est différent de zéro et que le type de ce solde pour restitution
     *             n'est pas {@link TypeSoldePourRestitution#RETENUES}
     */
    public void setMontantRetenueMensuelle(final BigDecimal montantRetenueMensuelle) {
        Checkers.checkNotNull(montantRetenueMensuelle, "montantRetenueMensuelle");
        if ((montantRetenueMensuelle.abs().compareTo(BigDecimal.ZERO) > 0)
                && (type != TypeSoldePourRestitution.RETENUES)) {
            throw new IllegalArgumentException(
                    "Le montant de la retenue mensuelle ne peut pas être différent de zéro si ce n'est pas une retenue mensuelle");
        }
        this.montantRetenueMensuelle = montantRetenueMensuelle;
    }

    /**
     * (re-)défini l'ordre de versement sur lequel est lié cette gestion de la restitution
     * 
     * @param ordreVersement
     *            un ordre de versement non null et initialisé
     * @throws NullPointerException
     *             si l'ordre de versement passé en paramètre est null
     * @throws IllegalArgumentException
     *             si l'ordre de versement passé en paramètre n'est pas initialisé
     */
    public void setOrdreVersement(final OrdreVersement ordreVersement) {
        Checkers.checkNotNull(ordreVersement, "ordreVersement");
        Checkers.checkHasID(ordreVersement, "ordreVersement");
        this.ordreVersement = ordreVersement;
    }

    /**
     * <p>
     * (re-)défini le type de cette gestion du solde de restitution
     * </p>
     * <p>
     * Si le nouveau type n'est pas {@link TypeSoldePourRestitution#RETENUES une retenue mensuelle}, le
     * {@link #getMontantRetenueMensuelle() montant mensuel} sera remis à zéro
     * </p>
     * 
     * @param type
     *            un type no null
     * @throws NullPointerException
     *             si le type passé en paramètre est null
     */
    public void setType(final TypeSoldePourRestitution type) {
        Checkers.checkNotNull(type, "type");
        this.type = type;

        if (this.type != TypeSoldePourRestitution.RETENUES) {
            setMontantRetenueMensuelle(BigDecimal.ZERO);
        }
    }
}
