package globaz.lynx.db.recherche;

import globaz.globall.db.BEntity;

public class LXRechercheGeneraleListViewBean extends LXRechercheGeneraleManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public LXRechercheGeneraleListViewBean() {
        super();
    }

    /**
     * @see globaz.lynx.db.recherche.LXRechercheGeneraleManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXRechercheGeneraleViewBean();
    }
}
