package globaz.lynx.db.facture;

import globaz.globall.db.BEntity;

public class LXFactureListViewBean extends LXFactureManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LXFactureListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXFactureViewBean();
    }
}
