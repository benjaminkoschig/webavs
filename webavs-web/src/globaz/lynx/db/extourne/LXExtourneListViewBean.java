package globaz.lynx.db.extourne;

import globaz.globall.db.BEntity;

public class LXExtourneListViewBean extends LXExtourneManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.lynx.db.extourne.LXExtourneManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXExtourneViewBean();
    }

}
