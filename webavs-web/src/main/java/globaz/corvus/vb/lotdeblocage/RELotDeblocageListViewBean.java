package globaz.corvus.vb.lotdeblocage;

import globaz.corvus.db.deblocage.REDeblocageVersementManager;
import globaz.globall.db.BEntity;

public class RELotDeblocageListViewBean extends REDeblocageVersementManager {

    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RELotDeblocageViewBean();
    }
}
