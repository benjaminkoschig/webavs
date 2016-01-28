package globaz.lynx.db.escompte;

import globaz.globall.db.BEntity;

public class LXEscompteListViewBean extends LXEscompteManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public LXEscompteListViewBean() {
        super();
    }

    /**
     * @see globaz.lynx.db.paiementLXEscompteManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXEscompteViewBean();
    }
}
