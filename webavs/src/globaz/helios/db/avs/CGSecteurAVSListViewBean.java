package globaz.helios.db.avs;

import globaz.helios.db.comptes.CGMandatViewBean;
import globaz.jade.client.util.JadeStringUtil;

public class CGSecteurAVSListViewBean extends CGSecteurAVSManager implements
        globaz.framework.bean.FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CGMandatViewBean mandat = new CGMandatViewBean();

    public CGSecteurAVSListViewBean() {
        super();
    }

    public CGMandatViewBean getMandat() {
        if (!JadeStringUtil.isIntegerEmpty(getForIdMandat()) && mandat.isNew()) {
            mandat.setSession(getSession());
            mandat.setIdMandat(getForIdMandat());

            try {
                mandat.retrieve();
            } catch (Exception e) {
                // do nothing
            }
        }

        return mandat;
    }

}
