package ch.globaz.prestation.domaine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;

/**
 * <p>
 * Regroupement des montants bloqu�s pour une prestation accord�e.
 * </p>
 * <p>
 * Un blocage est en cours si le montant d�bloqu� est diff�rent du montant total bloqu�.
 * </p>
 */
public class EnTeteBlocage extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal montantDebloque;
    private final Map<String, BigDecimal> montantsBloquesParMois;

    public EnTeteBlocage() {
        super();

        montantDebloque = BigDecimal.ZERO;
        montantsBloquesParMois = new HashMap<String, BigDecimal>();
    }

    /**
     * Ajoute un montant bloqu� pour le mois donn� (il ne peut y avoir qu'un blocage par mois pour une prestation
     * accord�e)
     * 
     * @param mois
     *            un mois (format MM.AAAA)
     * @param montantBlocage
     *            un montant
     * @throws NullPointerException
     *             si le mois ou le montant pass�s en param�tre sont null
     * @throws IllegalArgumentException
     *             si le mois n'est pas au bon format, ou si le mois est d�j� d�fini dans les blocages pr�sents
     */
    public final void ajouterUnBlocagePourLeMois(final String mois, final BigDecimal montantBlocage) {
        Checkers.checkNotNull(mois, "mois");
        Checkers.checkNotNull(montantBlocage, "montantBlocage");
        Checkers.checkDateMonthYear(mois, "mois", false);

        if (montantsBloquesParMois.containsKey(mois)) {
            throw new IllegalArgumentException(mois + " can't be added twice");
        }

        montantsBloquesParMois.put(mois, montantBlocage);
    }

    /**
     * @param mois
     *            un mois (format MM.AAAA
     * @return vrai s'il y a un blocage pour le mois pass� en param�tre
     * @throws NullPointerException
     *             si le mois pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le mois pass� en param�tre n'est pas au bon format
     */
    public final boolean contientUnBlocagePourLeMois(final String mois) {
        Checkers.checkNotNull(mois, "mois");
        Checkers.checkDateMonthYear(mois, "mois", false);
        return montantsBloquesParMois.containsKey(mois);
    }

    /**
     * @return le montant total d�bloqu� pour cette prestation accord�e
     */
    public final BigDecimal getMontantDebloque() {
        return montantDebloque;
    }

    /**
     * @return le montant total bloqu� pour cette prestation accord�e
     */
    public final BigDecimal getMontantTotalBloque() {

        BigDecimal montantTotal = BigDecimal.ZERO;

        for (BigDecimal unMontant : montantsBloquesParMois.values()) {
            montantTotal = montantTotal.add(unMontant);
        }

        return montantTotal;
    }

    /**
     * (re-)d�fini le montant total d�bloqu� pour cette prestation accord�e
     * 
     * @param montantDebloque
     *            un montant
     * @throws NullPointerException
     *             si le montant pass� en param�tre est null
     */
    public final void setMontantDebloque(final BigDecimal montantDebloque) {
        Checkers.checkNotNull(montantDebloque, "enTeteBlocage.montantDebloque");
        this.montantDebloque = montantDebloque;
    }

    /**
     * @return vrai si le montant total bloqu� n'est pas �gal au montant d�bloqu�
     */
    public final boolean unBlocageEstEnCours() {
        return getMontantDebloque().compareTo(getMontantTotalBloque()) != 0;
    }
}
