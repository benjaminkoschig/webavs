package globaz.corvus.vb.creances;

import globaz.corvus.db.creances.RECreancierManager;
import globaz.globall.db.BEntity;

/**
 * @author BSC
 */
public class RECreancierListViewBean extends RECreancierManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RECreancierViewBean();
    }
}
