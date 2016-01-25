package globaz.aquila.db.irrecouvrables;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CASectionManagerListViewBean;

/**
 * @author sch
 */
public class CORecouvrementSectionsListViewBean extends CASectionManagerListViewBean implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CORecouvrementSectionsListViewBean() {
        changeManagerSize(BManager.SIZE_NOLIMIT);
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return getOrderBy();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CORecouvrementSectionsViewBean();
    }
}
