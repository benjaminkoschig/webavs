package globaz.corvus.vb.historiques;

import globaz.corvus.db.historiques.REHistoriqueRentesJoinTiersManager;
import globaz.globall.db.BEntity;

/**
 * 
 * @author SCR
 * 
 */
public class REHistoriqueRentesJoinTiersListViewBean extends REHistoriqueRentesJoinTiersManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REHistoriqueRentesJoinTiersViewBean();
    }

}
