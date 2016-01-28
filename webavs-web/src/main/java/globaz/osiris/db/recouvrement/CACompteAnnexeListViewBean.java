package globaz.osiris.db.recouvrement;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexeManagerListViewBean;
import globaz.osiris.db.comptes.CACompteAnnexeViewBean;

/**
 * @author sel <br>
 *         Date : 15 mai 08
 */
public class CACompteAnnexeListViewBean extends CACompteAnnexeManagerListViewBean implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return getOrderBy();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CACompteAnnexeViewBean();
    }

    /**
     * @see globaz.osiris.db.comptes.CACompteAnnexeManager#getOrderBy()
     */
    @Override
    public String getOrderBy() {
        return super.getOrderBy();
    }

    /**
     * @see globaz.osiris.db.comptes.CACompteAnnexeManager#setOrderBy(java.lang.String)
     */
    @Override
    public void setOrderBy(String newOrderBy) {
        super.setOrderBy(newOrderBy);
    }

}
