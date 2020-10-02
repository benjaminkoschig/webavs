package ch.globaz.pegasus.businessimpl.utils.calcul;

public class ForfaitPrimeMoyenneAssuranceMaladie {

    /**
     * Enumeration des types de prime
     */
    enum TypesForfait {
        FORFAIT_ADULTE,
        FORFAIT_ENFANT,
        FORFAIT_JEUNE_ADULTE
    };

    private String csTypePersonne = null;
    private float montantPrimeMoy;
    private float montantPrimeReductionMaxCanton;

    /**
     * Constructeur
     * 
     * @param csTypePersonne
     *            le type de prime
     * @param montantPrimeMoy
     *            le montant de la prime moyenne
     * @param montantPrimeReductionMaxCanton le montant de la prime de réduction max cantonale
     */
    public ForfaitPrimeMoyenneAssuranceMaladie(String csTypePersonne, float montantPrimeMoy, float montantPrimeReductionMaxCanton) {
        super();
        this.csTypePersonne = csTypePersonne;
        this.montantPrimeMoy = montantPrimeMoy;
        this.montantPrimeReductionMaxCanton = montantPrimeReductionMaxCanton;
    }

    /**
     * Constructeur
     * 
     * @param csTypePrime
     *            le type de prime
     * @param montantPrimeMoy
     *            le montant de la prime, en texte
     *
     * @param montantPrimeReductionMaxCanton le montant de la prime de réduction max cantonale
     *
     */
    public ForfaitPrimeMoyenneAssuranceMaladie(String csTypePrime, String montantPrimeMoy, String montantPrimeReductionMaxCanton) {
        this(csTypePrime, Float.parseFloat(montantPrimeMoy), Float.parseFloat(montantPrimeReductionMaxCanton));
    }

    /**
     * Constructeur
     *
     * @param csTypePersonne
     *            le type de prime
     * @param montantPrimeMoy
     *            le montant de la prime moyenne
     */
    public ForfaitPrimeMoyenneAssuranceMaladie(String csTypePersonne, float montantPrimeMoy) {
        super();
        this.csTypePersonne = csTypePersonne;
        this.montantPrimeMoy = montantPrimeMoy;
    }

    /**
     * Constructeur
     *
     * @param csTypePrime
     *            le type de prime
     * @param montantPrimeMoy
     *            le montant de la prime, en texte
     *
     */
    public ForfaitPrimeMoyenneAssuranceMaladie(String csTypePrime, String montantPrimeMoy) {
        this(csTypePrime, Float.parseFloat(montantPrimeMoy));
    }

    /**
     * @return the csTypePersonne
     */
    public String getCsTypePersonne() {
        return csTypePersonne;
    }

    /**
     * @return the montant
     */
    public float getMontantPrimeMoy() {
        return montantPrimeMoy;
    }

    /**
     * @param csTypePersonne
     *            the csTypePersonne to set
     */
    public void setCsTypePersonne(String csTypePersonne) {
        this.csTypePersonne = csTypePersonne;
    }

    /**
     * @param montantPrimeMoy
     *            the montant to set
     */
    public void setMontantPrimeMoy(float montantPrimeMoy) {
        this.montantPrimeMoy = montantPrimeMoy;
    }

    public float getMontantPrimeReductionMaxCanton() {
        return montantPrimeReductionMaxCanton;
    }

    public void setMontantPrimeReductionMaxCanton(float montantPrimeReductionMaxCanton) {
        this.montantPrimeReductionMaxCanton = montantPrimeReductionMaxCanton;
    }
}
