package globaz.apg.module.calcul.salaire;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APMontantVerse extends APSalaire {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private boolean pourcent;
    private String pourcentage;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APMontantVerse.
     * 
     * @param montant
     *            DOCUMENT ME!
     * @param pourcentage
     *            DOCUMENT ME!
     * @param pourcent
     *            DOCUMENT ME!
     * @param csPeriodiciteSalaire
     *            DOCUMENT ME!
     */
    public APMontantVerse(String montant, String pourcentage, boolean pourcent, String csPeriodiciteSalaire) {
        super(montant, csPeriodiciteSalaire);
        this.pourcentage = pourcentage;
        this.pourcent = pourcent;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut pourcentage
     * 
     * @return la valeur courante de l'attribut pourcentage
     */
    public String getPourcentage() {
        return pourcentage;
    }

    /**
     * getter pour l'attribut pourcent
     * 
     * @return la valeur courante de l'attribut pourcent
     */
    public boolean isPourcent() {
        return pourcent;
    }
}
