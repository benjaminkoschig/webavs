package globaz.osiris.db.suiviprocedure;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.osiris.db.comptes.CACompteAnnexeViewBean;
import globaz.osiris.db.suiviprocedure.util.CASuiviProcedureUtil;

public class CAFailliteListViewBean extends CAFailliteManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAFailliteViewBean();
    }

    public CACompteAnnexeViewBean getCompteAnnexe() {
        return CASuiviProcedureUtil.getCompteAnnexe(getSession(), getForIdCompteAnnexe());
    }
}
