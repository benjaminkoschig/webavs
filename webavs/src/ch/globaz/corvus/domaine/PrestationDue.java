package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.domaine.constantes.TypePrestationDue;

/**
 * <p>
 * Une prestation due est un historique des montants d'une rente acordée. Il sert à garder trace du montant payé
 * mensuellement durant une certaine période ainsi que de ce qui a été payé une seule fois comme arriérés (rétroactif).
 * Il sert également d'historique pour l'adaptation des rentes (rencherissement).
 * </p>
 * <p>
 * Le montant d'une prestation due exprime une donnée différente selon le type de cette prestation dûe, il en va de même
 * pour la période. Veuillez-vous référez aux commentaires de {@link #getPeriode()} et {@link #getMontant()} pour plus
 * d'informations à ce sujet.
 * </p>
 */
public class PrestationDue extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal montant;
    private Periode periode;
    private TypePrestationDue type;

    public PrestationDue() {
        super();

        montant = BigDecimal.ZERO;
        periode = new Periode("", "");
        type = TypePrestationDue.PAIEMENT_MENSUEL;
    }

    /**
     * Le montant de la prestation due peut être un montant payé une seule fois (dans le cas d'une prestation due de
     * type {@link TypePrestationDue#MONTANT_RETROACTIF_TOTALL montant total}) ou un montant qui sera payé tous les mois
     * durant la {@link #getPeriode() période} (type {@link TypePrestationDue#PAIEMENT_MENSUEL paiement mensuel})
     * 
     * @return le montant de cette prestation due
     */
    public final BigDecimal getMontant() {
        return montant;
    }

    /**
     * La période reprsésente une donnée différente selon le type de prestation due :
     * <ul>
     * <li>Type {@link TypePrestationDue#MONTANT_RETROACTIF_TOTAL montant total} : la période durant laquelle le montant
     * total a été calculé (ne sera payé qu'une seule fois, au moment de la prise de décision)
     * <li>Type {@link TypePrestationDue#PAIEMENT_MENSUEL paiement mensuel} : la période durant laquelle, tous les mois,
     * le montant sera payé
     * </ul>
     * 
     * @return la période de cette prestation due
     */
    public final Periode getPeriode() {
        return periode;
    }

    /**
     * @return le type de cette prestation due
     */
    public final TypePrestationDue getType() {
        return type;
    }

    /**
     * (re-)défini le montant de cette prestation due.
     * 
     * @param montant
     *            un montant non-null
     * @throws NullPointerException
     *             si le montant passé en paramètre est null
     * @see #getMontant()
     */
    public final void setMontant(final BigDecimal montant) {
        Checkers.checkNotNull(montant, "montant");
        this.montant = montant;
    }

    /**
     * (re-)défini la période de cette prestation due.
     * 
     * @param periode
     *            une période non-null, exprimée par des date au format MM.AAAA
     * @throws NullPointerException
     *             si la période passée en paramètre est null
     * @throws IllegalArgumentException
     *             si la période passée en paramètre n'est pas dans le bon format
     */
    public final void setPeriode(final Periode periode) {
        Checkers.checkNotNull(periode, "periode");
        Checkers.checkPeriodMonthYear(periode, "periode");
        this.periode = periode;
    }

    /**
     * (re-)défini la type de cette prestation due
     * 
     * @param type
     *            un type non-null
     * @throws NullPointerException
     *             si le type passé en paramètre est null
     */
    public final void setType(final TypePrestationDue type) {
        Checkers.checkNotNull(type, "type");
        this.type = type;
    }
}
