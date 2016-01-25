package ch.globaz.prestation.domaine;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Objet repr�sentant la demande de prestation
 */
public class DossierPrestation extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PersonneAVS requerant;

    /**
     * @return le requ�rant de cette demande de prestation
     */
    public PersonneAVS getRequerant() {
        return requerant;
    }

    /**
     * (re-)d�fini le requ�rant de cette demande de prestation
     * 
     * @param requerant
     * @throws NullPointerException
     *             si le requ�rant pass� en param�tre est null
     * @throws IllegalArgumentException
     *             si le requ�rant pass� en param�tre n'est pas initialis�
     */
    public void setRequerant(final PersonneAVS requerant) {
        Checkers.checkNotNull(requerant, "requerant");
        Checkers.checkHasID(requerant, "requerant");
        this.requerant = requerant;
    }
}
