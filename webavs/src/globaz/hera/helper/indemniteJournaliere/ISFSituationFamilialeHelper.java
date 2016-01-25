/*
 * mmu Créé le 13 oct. 05
 */
package globaz.hera.helper.indemniteJournaliere;

import globaz.hera.api.ISFMembreFamilleRequerant;

/**
 * @author mmu
 * 
 *         13 oct. 05
 */
public class ISFSituationFamilialeHelper extends globaz.hera.api.helper.ISFSituationFamilialeHelper {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**  **/
    public static final String ENTITY_CLASS_NAME = "globaz.hera.impl.indemniteJournaliere.SFSituationFamiliale";

    /**
     * @param implementationClassName
     */
    public ISFSituationFamilialeHelper() {
        super(ENTITY_CLASS_NAME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getMembresFamille(java.lang.String)
     */
    public ISFMembreFamilleRequerant[] getMembreFamilleRequerant(String idTiers) throws Exception {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getMembresFamille(java.lang.String, java.lang.String)
     */
    public ISFMembreFamilleRequerant[] getMembreFamilleRequerant(String idTiers, String date) throws Exception {
        // TODO Raccord de méthode auto-généré
        return null;
    }

}
