package ch.globaz.vulpecula.external.models.pyxis;

/**
 * Repr�sentation m�tier d'une PersonneEtendue (PersonneAvs) selon le module
 * Pyxis
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 23 d�c. 2013
 * 
 */
public class PersonneEtendue extends PersonneSimple {
    private String numAvsActuel;
    private String numContribuableActuel;
    private String ancienNumAvs;

    public PersonneEtendue() {

    }

    public PersonneEtendue(Tiers tiers) {
        super(tiers);
    }

    public PersonneEtendue(PersonneSimple personneSimple) {
        super(personneSimple);
    }

    public PersonneEtendue(PersonneEtendue personneEtendue) {
        super(personneEtendue);
        numAvsActuel = personneEtendue.getNumAvsActuel();
        numContribuableActuel = personneEtendue.getNumContribuableActuel();
        ancienNumAvs = personneEtendue.getAncienNumAvs();
    }

    /**
     * Retourne le numero AVS du tiers
     * 
     * @return String repr�sentant le numero AVS au format XXX.XXXX.XXXX.XX
     */
    public String getNumAvsActuel() {
        return numAvsActuel;
    }

    /**
     * Mise � jour du numero AVS du tiers
     * 
     * @param numAvsActuel
     *            Nouveau numero AVS
     */
    public void setNumAvsActuel(String numAvsActuel) {
        this.numAvsActuel = numAvsActuel;
    }

    /**
     * Retourne le num�ro de contribuable actuel
     * 
     * @return Le num�ro de contribuable actuel
     */
    public String getNumContribuableActuel() {
        return numContribuableActuel;
    }

    /**
     * Le num�ro de contribuable actuel
     * 
     * @param numContribuableActuel
     */
    public void setNumContribuableActuel(String numContribuableActuel) {
        this.numContribuableActuel = numContribuableActuel;
    }

    /**
     * Retourne l'ancien numero AVS du tiers
     * 
     * @return String repr�sentant le numero AVS au format XXX.XXXX.XXXX.XX
     */
    public String getAncienNumAvs() {
        return ancienNumAvs;
    }

    /**
     * Mise � jour de l'ancien numero AVS du tiers
     * 
     * @param ancienNumAvs
     *            Num�ro AVS � mettre � jour
     */
    public void setAncienNumAvs(String ancienNumAvs) {
        this.ancienNumAvs = ancienNumAvs;
    }
}
