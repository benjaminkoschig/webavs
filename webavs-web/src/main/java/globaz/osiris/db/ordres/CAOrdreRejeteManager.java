package globaz.osiris.db.ordres;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;

public class CAOrdreRejeteManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = -8546248011838964175L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAOrdreRejete();
    }

}
