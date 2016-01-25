/**
 * 
 */
package ch.globaz.amal.business.models.contribuable;

/**
 * @author CBU
 * 
 */
public class ContribuableFamilleRCListeSearch extends ContribuableRCListeSearch {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Class whichModelClass() {
        return ContribuableFamilleRCListe.class;
    }
}
