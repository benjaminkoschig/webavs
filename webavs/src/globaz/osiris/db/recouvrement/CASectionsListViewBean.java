package globaz.osiris.db.recouvrement;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CASectionManagerListViewBean;
import globaz.osiris.db.comptes.CASectionViewBean;

/**
 * @author SEL <br>
 *         Date : 15 mai 08
 */
public class CASectionsListViewBean extends CASectionManagerListViewBean implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getOrder(BStatement statement) {
        return getOrderBy();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASectionViewBean();
    }
}
