package ch.globaz.prestation.domaine;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Objet représentant la demande de prestation
 */
public class DossierPrestation extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PersonneAVS requerant;

    /**
     * @return le requérant de cette demande de prestation
     */
    public PersonneAVS getRequerant() {
        return requerant;
    }

    /**
     * (re-)défini le requérant de cette demande de prestation
     * 
     * @param requerant
     * @throws NullPointerException
     *             si le requérant passé en paramètre est null
     * @throws IllegalArgumentException
     *             si le requérant passé en paramètre n'est pas initialisé
     */
    public void setRequerant(final PersonneAVS requerant) {
        Checkers.checkNotNull(requerant, "requerant");
        Checkers.checkHasID(requerant, "requerant");
        this.requerant = requerant;
    }
}
