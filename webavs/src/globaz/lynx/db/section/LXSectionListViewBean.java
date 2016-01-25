package globaz.lynx.db.section;

import globaz.globall.db.BEntity;

public class LXSectionListViewBean extends LXSectionManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LXSectionListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXSectionViewBean();
    }
}
