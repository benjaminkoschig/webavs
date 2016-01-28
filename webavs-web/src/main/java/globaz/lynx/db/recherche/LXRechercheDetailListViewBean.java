package globaz.lynx.db.recherche;

import globaz.globall.db.BEntity;

public class LXRechercheDetailListViewBean extends LXRechercheDetailManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public LXRechercheDetailListViewBean() {
        super();
    }

    /**
     * @see globaz.lynx.db.paiement.LXRechercheDetailManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXRechercheDetailViewBean();
    }

}
