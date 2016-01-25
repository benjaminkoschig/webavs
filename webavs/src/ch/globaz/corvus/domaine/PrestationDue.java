package ch.globaz.corvus.domaine;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.domaine.constantes.TypePrestationDue;

/**
 * <p>
 * Une prestation due est un historique des montants d'une rente acord�e. Il sert � garder trace du montant pay�
 * mensuellement durant une certaine p�riode ainsi que de ce qui a �t� pay� une seule fois comme arri�r�s (r�troactif).
 * Il sert �galement d'historique pour l'adaptation des rentes (rencherissement).
 * </p>
 * <p>
 * Le montant d'une prestation due exprime une donn�e diff�rente selon le type de cette prestation d�e, il en va de m�me
 * pour la p�riode. Veuillez-vous r�f�rez aux commentaires de {@link #getPeriode()} et {@link #getMontant()} pour plus
 * d'informations � ce sujet.
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
     * Le montant de la prestation due peut �tre un montant pay� une seule fois (dans le cas d'une prestation due de
     * type {@link TypePrestationDue#MONTANT_RETROACTIF_TOTALL montant total}) ou un montant qui sera pay� tous les mois
     * durant la {@link #getPeriode() p�riode} (type {@link TypePrestationDue#PAIEMENT_MENSUEL paiement mensuel})
     * 
     * @return le montant de cette prestation due
     */
    public final BigDecimal getMontant() {
        return montant;
    }

    /**
     * La p�riode reprs�sente une donn�e diff�rente selon le type de prestation due :
     * <ul>
     * <li>Type {@link TypePrestationDue#MONTANT_RETROACTIF_TOTAL montant total} : la p�riode durant laquelle le montant
     * total a �t� calcul� (ne sera pay� qu'une seule fois, au moment de la prise de d�cision)
     * <li>Type {@link TypePrestationDue#PAIEMENT_MENSUEL paiement mensuel} : la p�riode durant laquelle, tous les mois,
     * le montant sera pay�
     * </ul>
     * 
     * @return la p�riode de cette prestation due
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
     * (re-)d�fini le montant de cette prestation due.
     * 
     * @param montant
     *            un montant non-null
     * @throws NullPointerException
     *             si le montant pass� en param�tre est null
     * @see #getMontant()
     */
    public final void setMontant(final BigDecimal montant) {
        Checkers.checkNotNull(montant, "montant");
        this.montant = montant;
    }

    /**
     * (re-)d�fini la p�riode de cette prestation due.
     * 
     * @param periode
     *            une p�riode non-null, exprim�e par des date au format MM.AAAA
     * @throws NullPointerException
     *             si la p�riode pass�e en param�tre est null
     * @throws IllegalArgumentException
     *             si la p�riode pass�e en param�tre n'est pas dans le bon format
     */
    public final void setPeriode(final Periode periode) {
        Checkers.checkNotNull(periode, "periode");
        Checkers.checkPeriodMonthYear(periode, "periode");
        this.periode = periode;
    }

    /**
     * (re-)d�fini la type de cette prestation due
     * 
     * @param type
     *            un type non-null
     * @throws NullPointerException
     *             si le type pass� en param�tre est null
     */
    public final void setType(final TypePrestationDue type) {
        Checkers.checkNotNull(type, "type");
        this.type = type;
    }
}
