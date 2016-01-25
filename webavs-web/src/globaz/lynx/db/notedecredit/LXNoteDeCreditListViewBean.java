package globaz.lynx.db.notedecredit;

import globaz.globall.db.BEntity;

public class LXNoteDeCreditListViewBean extends LXNoteDeCreditManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LXNoteDeCreditListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXNoteDeCreditViewBean();
    }
}
