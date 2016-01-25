package globaz.aquila.db.irrecouvrables;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CASectionManagerListViewBean;

/**
 * @author dostes, 3 janv. 05
 */
public class COSectionsListViewBean extends CASectionManagerListViewBean implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public COSectionsListViewBean() {
        changeManagerSize(BManager.SIZE_NOLIMIT);
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return getOrderBy();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COSectionsViewBean();
    }
}
