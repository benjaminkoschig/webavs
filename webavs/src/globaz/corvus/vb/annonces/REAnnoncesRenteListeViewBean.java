package globaz.corvus.vb.annonces;

import globaz.corvus.db.annonces.REAnnoncesRentePourEcranManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;

public class REAnnoncesRenteListeViewBean extends REAnnoncesRentePourEcranManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public REAnnoncesRenteListeViewBean() {
        this(null);
    }

    public REAnnoncesRenteListeViewBean(BSession session) {
        super();

        setSession(session);
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAnnoncesRenteViewBean();
    }

}
