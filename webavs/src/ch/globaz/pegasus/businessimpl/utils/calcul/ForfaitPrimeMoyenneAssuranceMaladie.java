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

    private float montant;

    /**
     * Constructeur
     * 
     * @param csTypePersonne
     *            le type de prime
     * @param montant
     *            le montant de la prime
     */
    public ForfaitPrimeMoyenneAssuranceMaladie(String csTypePersonne, float montant) {
        super();
        this.csTypePersonne = csTypePersonne;
        this.montant = montant;
    }

    /**
     * Constructeur
     * 
     * @param csTypePrime
     *            le type de prime
     * @param montant
     *            le montant de la prime, en texte
     */
    public ForfaitPrimeMoyenneAssuranceMaladie(String csTypePrime, String montant) {
        this(csTypePrime, Float.parseFloat(montant));
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
    public float getMontant() {
        return montant;
    }

    /**
     * @param csTypePersonne
     *            the csTypePersonne to set
     */
    public void setCsTypePersonne(String csTypePersonne) {
        this.csTypePersonne = csTypePersonne;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(float montant) {
        this.montant = montant;
    }

}
