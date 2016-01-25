package globaz.hera.impl.rentes;

import globaz.hera.api.ISFSituationFamiliale;

/**
 * @author scr
 */
public class SFSituationFamiliale extends globaz.hera.impl.standard.SFSituationFamiliale {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SFSituationFamiliale() {
        super();
        domaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
    }
}
