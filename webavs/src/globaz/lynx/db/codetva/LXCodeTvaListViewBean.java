package globaz.lynx.db.codetva;

import globaz.globall.db.BEntity;

public class LXCodeTvaListViewBean extends LXCodeTvaManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public LXCodeTvaListViewBean() {
        super();
    }

    /**
     * @see globaz.lynx.db.codetva.LXCodeTvaManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXCodeTvaViewBean();
    }
}
