package globaz.aquila.db.irrecouvrables;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexeManagerListViewBean;

/**
 * @author dostes, 4 janv. 05
 */
public class CORecouvrementCompteAnnexeListViewBean extends CACompteAnnexeManagerListViewBean implements
        FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // CS
    public final static String CS_AFFILIE = "517002";

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
        return new CORecouvrementCompteAnnexeViewBean();
    }

    @Override
    public String getOrderBy() {
        return super.getOrderBy();
    }

    @Override
    public void setOrderBy(String newOrderBy) {
        super.setOrderBy(newOrderBy);
    }

}
