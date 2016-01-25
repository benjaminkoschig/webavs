package globaz.lynx.db.paiement;

import globaz.globall.db.BEntity;

public class LXPaiementListViewBean extends LXPaiementManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public LXPaiementListViewBean() {
        super();
    }

    /**
     * @see globaz.lynx.db.paiement.LXPaiementManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXPaiementViewBean();
    }
}
