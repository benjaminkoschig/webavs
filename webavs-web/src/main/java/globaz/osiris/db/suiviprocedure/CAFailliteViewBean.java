package globaz.osiris.db.suiviprocedure;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.db.comptes.CACompteAnnexeViewBean;
import globaz.osiris.db.suiviprocedure.util.CASuiviProcedureUtil;

public class CAFailliteViewBean extends CAFaillite implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CACompteAnnexeViewBean getCompteAnnexe() {
        return CASuiviProcedureUtil.getCompteAnnexe(getSession(), getIdCompteAnnexe());
    }
}
