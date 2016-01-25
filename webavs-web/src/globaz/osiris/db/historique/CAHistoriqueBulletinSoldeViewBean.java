package globaz.osiris.db.historique;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;

public class CAHistoriqueBulletinSoldeViewBean extends CAHistoriqueBulletinSolde implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getSoldeFormatter() {
        return JANumberFormatter.fmt(getSolde(), true, true, false, 2);
    }
}
