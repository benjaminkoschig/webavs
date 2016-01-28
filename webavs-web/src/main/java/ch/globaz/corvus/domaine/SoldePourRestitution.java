package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.corvus.domaine.constantes.TypeSoldePourRestitution;

/**
 * Gestion du solde d'une {@link Decision d�cision} lorsqu'il est n�gatif (en faveur de la caisse). Permet de choisir si
 * cette dette doit �tre �crite directement en comptabilit� auxiliaire (type
 * {@link TypeSoldePourRestitution#RESTITUTION} r�stitution) ou si un certain montant doit �tre d�duit de ce qui sera
 * pay� � l'assur� tous les mois (type {@link TypeSoldePourRestitution#RETENUES retenue mensuelle})
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
     * @return le montant devant �tre retenu tous les mois sur le paiement mensuelle, dans le cas o� le type de cette
     *         entit� est {@link TypeSoldePourRestitution#RETENUES}
     */
    public BigDecimal getMontantRetenueMensuelle() {
        return montantRetenueMensuelle;
    }

    /**
     * @return l'ordre de versement sur lequel est attach� cette gestion de la restitution
     */
    public OrdreVersement getOrdreVersement() {
        return ordreVersement;
    }

    /**
     * @return le type de resitution qui doit �tre appliqu�
     */
    public TypeSoldePourRestitution getType() {
        return type;
    }

    /**
     * (re-)d�fini le montant total de cette restitution (la dette totale)
     * 
     * @param montantRestitution
     *            un montant non null
     * @throws NullPointerException
     *             si le montant pass� en param�tre est null
     */
    public void setMontantRestitution(final BigDecimal montantRestitution) {
        Checkers.checkNotNull(montantRestitution, "montantRestitution");
        this.montantRestitution = montantRestitution;
    }

    /**
     * <p>
     * (re-)d�fini le montant � retenir mensuelle.
     * </p>
     * <p>
     * Ne peut �tre d�fini � un montant diff�rent de z�ro que si le type de cette restitution est
     * {@link TypeSoldePourRestitution#RETENUES}
     * </p>
     * 
     * @param montantRetenueMensuelle
     *            un montant non-null
     * @throws NullPointerException
     *             si le montant pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le montant pass� en param�tre est diff�rent de z�ro et que le type de ce solde pour restitution
     *             n'est pas {@link TypeSoldePourRestitution#RETENUES}
     */
    public void setMontantRetenueMensuelle(final BigDecimal montantRetenueMensuelle) {
        Checkers.checkNotNull(montantRetenueMensuelle, "montantRetenueMensuelle");
        if ((montantRetenueMensuelle.abs().compareTo(BigDecimal.ZERO) > 0)
                && (type != TypeSoldePourRestitution.RETENUES)) {
            throw new IllegalArgumentException(
                    "Le montant de la retenue mensuelle ne peut pas �tre diff�rent de z�ro si ce n'est pas une retenue mensuelle");
        }
        this.montantRetenueMensuelle = montantRetenueMensuelle;
    }

    /**
     * (re-)d�fini l'ordre de versement sur lequel est li� cette gestion de la restitution
     * 
     * @param ordreVersement
     *            un ordre de versement non null et initialis�
     * @throws NullPointerException
     *             si l'ordre de versement pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si l'ordre de versement pass� en param�tre n'est pas initialis�
     */
    public void setOrdreVersement(final OrdreVersement ordreVersement) {
        Checkers.checkNotNull(ordreVersement, "ordreVersement");
        Checkers.checkHasID(ordreVersement, "ordreVersement");
        this.ordreVersement = ordreVersement;
    }

    /**
     * <p>
     * (re-)d�fini le type de cette gestion du solde de restitution
     * </p>
     * <p>
     * Si le nouveau type n'est pas {@link TypeSoldePourRestitution#RETENUES une retenue mensuelle}, le
     * {@link #getMontantRetenueMensuelle() montant mensuel} sera remis � z�ro
     * </p>
     * 
     * @param type
     *            un type no null
     * @throws NullPointerException
     *             si le type pass� en param�tre est null
     */
    public void setType(final TypeSoldePourRestitution type) {
        Checkers.checkNotNull(type, "type");
        this.type = type;

        if (this.type != TypeSoldePourRestitution.RETENUES) {
            setMontantRetenueMensuelle(BigDecimal.ZERO);
        }
    }
}
