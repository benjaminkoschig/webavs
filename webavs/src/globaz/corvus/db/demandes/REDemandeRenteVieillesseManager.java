package globaz.corvus.db.demandes;

import globaz.globall.db.BEntity;

/**
 * @author BSC
 */
public class REDemandeRenteVieillesseManager extends REDemandeRenteManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDemandeRenteVieillesse();
    }
}
