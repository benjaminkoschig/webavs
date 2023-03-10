package globaz.hera.api.rentes.helper;

import globaz.hera.api.ISFSituationFamiliale;

/**
 * Impl?mentation des m?thodes de l'interface du domaine d'application Rentes,
 * 
 * @author scr *
 */

public class ISFSituationFamilialeHelper extends globaz.hera.api.helper.ISFSituationFamilialeHelper implements
        ISFSituationFamiliale {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * *
     */
    public static final String ENTITY_CLASS_NAME = "globaz.hera.impl.rentes.SFSituationFamiliale";

    /**
     * Cr?e une nouvelle instance de la classe ISFSituationFamilialeHelper.
     */
    public ISFSituationFamilialeHelper() {
        super(ENTITY_CLASS_NAME);
    }

    /**
     * Cr?e une nouvelle instance de la classe ISFSituationFamilialeHelper.
     * 
     * @param entity_class_name
     *            DOCUMENT ME!
     */
    public ISFSituationFamilialeHelper(String entity_class_name) {
        super(entity_class_name);
    }
}
