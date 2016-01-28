package globaz.apg.module.calcul.salaire;

import globaz.apg.module.calcul.APBaseCalculSalaireJournalier;
import globaz.framework.util.FWCurrency;
import globaz.prestation.api.IPRSituationProfessionnelle;
import java.math.BigDecimal;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APSalaire {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String PERIODICITE_AUTRE = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csPeriodiciteSalaire;
    private String montant;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APSalaire.
     * 
     * @param montant
     *            DOCUMENT ME!
     * @param csPeriodiciteSalaire
     *            DOCUMENT ME!
     */
    public APSalaire(String montant, String csPeriodiciteSalaire) {
        this.montant = montant;
        this.csPeriodiciteSalaire = csPeriodiciteSalaire;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut cs periodicite salaire
     * 
     * @return la valeur courante de l'attribut cs periodicite salaire
     */
    public String getCsPeriodiciteSalaire() {
        return csPeriodiciteSalaire;
    }

    /**
     * getter pour l'attribut montant
     * 
     * @return la valeur courante de l'attribut montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @param bcj
     *            DOCUMENT ME!
     * @param nbHeuresSemaines
     *            DOCUMENT ME!
     */
    public void updateBaseCalculSalaire(APBaseCalculSalaireJournalier bcj, String nbHeuresSemaines) {
        if (IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES.equals(csPeriodiciteSalaire)) {
            bcj.setSalaire4Semaines(new FWCurrency(getMontant()));
        } else if (IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE.equals(csPeriodiciteSalaire)) {
            bcj.setSalaireAnnuel(new FWCurrency(getMontant()));
        } else if (IPRSituationProfessionnelle.CS_PERIODICITE_HEURE.equals(csPeriodiciteSalaire)) {
            bcj.setNbrHeuresSemaine(new BigDecimal(nbHeuresSemaines));
            bcj.setSalaireHoraire(new FWCurrency(getMontant()));
        } else if (IPRSituationProfessionnelle.CS_PERIODICITE_MOIS.equals(csPeriodiciteSalaire)) {
            bcj.setSalaireMensuel(new FWCurrency(getMontant()));
        }
    }
}
