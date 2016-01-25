package ch.globaz.corvus.domaine;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class BaseCalcul extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PersonneAVS donneurDeDroit;

    public BaseCalcul() {
        super();

        donneurDeDroit = new PersonneAVS();
    }

    /**
     * @return la personne qui donne le droit à une rente au travers de cette base de calcul (souvent appelée
     *         "Tiers base de calcul")
     */
    public PersonneAVS getDonneurDeDroit() {
        return donneurDeDroit;
    }

    /**
     * (re-)défini la personne qui donne le droit à une rente au travers de cette base de calcul (souvent appelée
     * "Tiers base de calcul")
     * 
     * @param donneurDeDroit
     *            le donneur de droit
     * @throws NullPointerException
     *             si la personne est null
     * @throws IllegalArgumentException
     *             si la personne est non initialisée
     */
    public void setDonneurDeDroit(final PersonneAVS donneurDeDroit) {
        Checkers.checkNotNull(donneurDeDroit, "baseCalcul.donneurDeDroit");
        Checkers.checkHasID(donneurDeDroit, "baseCalcul.donneurDeDroit");
        this.donneurDeDroit = donneurDeDroit;
    }
}
