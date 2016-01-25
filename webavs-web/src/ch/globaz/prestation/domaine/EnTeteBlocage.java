package ch.globaz.prestation.domaine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;

/**
 * <p>
 * Regroupement des montants bloqués pour une prestation accordée.
 * </p>
 * <p>
 * Un blocage est en cours si le montant débloqué est différent du montant total bloqué.
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
     * Ajoute un montant bloqué pour le mois donné (il ne peut y avoir qu'un blocage par mois pour une prestation
     * accordée)
     * 
     * @param mois
     *            un mois (format MM.AAAA)
     * @param montantBlocage
     *            un montant
     * @throws NullPointerException
     *             si le mois ou le montant passés en paramètre sont null
     * @throws IllegalArgumentException
     *             si le mois n'est pas au bon format, ou si le mois est déjà défini dans les blocages présents
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
     * @return vrai s'il y a un blocage pour le mois passé en paramètre
     * @throws NullPointerException
     *             si le mois passé en paramètre est null
     * @throws IllegalArgumentException
     *             si le mois passé en paramètre n'est pas au bon format
     */
    public final boolean contientUnBlocagePourLeMois(final String mois) {
        Checkers.checkNotNull(mois, "mois");
        Checkers.checkDateMonthYear(mois, "mois", false);
        return montantsBloquesParMois.containsKey(mois);
    }

    /**
     * @return le montant total débloqué pour cette prestation accordée
     */
    public final BigDecimal getMontantDebloque() {
        return montantDebloque;
    }

    /**
     * @return le montant total bloqué pour cette prestation accordée
     */
    public final BigDecimal getMontantTotalBloque() {

        BigDecimal montantTotal = BigDecimal.ZERO;

        for (BigDecimal unMontant : montantsBloquesParMois.values()) {
            montantTotal = montantTotal.add(unMontant);
        }

        return montantTotal;
    }

    /**
     * (re-)défini le montant total débloqué pour cette prestation accordée
     * 
     * @param montantDebloque
     *            un montant
     * @throws NullPointerException
     *             si le montant passé en paramètre est null
     */
    public final void setMontantDebloque(final BigDecimal montantDebloque) {
        Checkers.checkNotNull(montantDebloque, "enTeteBlocage.montantDebloque");
        this.montantDebloque = montantDebloque;
    }

    /**
     * @return vrai si le montant total bloqué n'est pas égal au montant débloqué
     */
    public final boolean unBlocageEstEnCours() {
        return getMontantDebloque().compareTo(getMontantTotalBloque()) != 0;
    }
}
