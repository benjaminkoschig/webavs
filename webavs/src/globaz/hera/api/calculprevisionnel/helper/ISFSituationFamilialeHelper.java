package globaz.hera.api.calculprevisionnel.helper;

import globaz.hera.api.ISFSituationFamiliale;

/**
 * Implémentation des méthodes de l'interface du domaine d'application Rentes,
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
    public static final String ENTITY_CLASS_NAME = "globaz.hera.impl.calculprevisionnel.SFSituationFamiliale";

    /**
     * Crée une nouvelle instance de la classe ISFSituationFamilialeHelper.
     */
    public ISFSituationFamilialeHelper() {
        super(ENTITY_CLASS_NAME);
    }

    /**
     * Crée une nouvelle instance de la classe ISFSituationFamilialeHelper.
     * 
     * @param entity_class_name
     *            DOCUMENT ME!
     */
    public ISFSituationFamilialeHelper(String entity_class_name) {
        super(entity_class_name);
    }
}
