package globaz.perseus.helpers.donneesfinancieres;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.perseus.vb.donneesfinancieres.PFDonneefinanciereViewBean;

public class PFDonneeFinanciereHelper extends FWHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);

        if (viewBean instanceof PFDonneefinanciereViewBean) {
            ((PFDonneefinanciereViewBean) viewBean).init();
        }
    }

}
