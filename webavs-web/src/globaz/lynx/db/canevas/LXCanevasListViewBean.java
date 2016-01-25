package globaz.lynx.db.canevas;

import globaz.globall.db.BEntity;

public class LXCanevasListViewBean extends LXCanevasManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Le constructeur
     */
    public LXCanevasListViewBean() {
        super();
    }

    /**
     * @see globaz.lynx.db.canevas.LXCanevasManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXCanevasViewBean();
    }
}
