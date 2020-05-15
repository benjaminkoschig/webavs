/*
 *
 */
package globaz.apg.helpers.process;

import globaz.apg.enums.APPandemieServiceCalcul;
import globaz.apg.process.APGenererDroitPandemieMensuelProcess;
import globaz.apg.vb.process.APGenererDroitPandemieMensuelViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APGenererDroitPandemieMensuelHelper extends FWHelper {

    /**
     * (non javadoc)
     *
     * @see FWHelper#_start(FWViewBeanInterface,
     *      FWAction, BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        APGenererDroitPandemieMensuelViewBean apDroitPandemieMensuelviewBean = (APGenererDroitPandemieMensuelViewBean) viewBean;
        APGenererDroitPandemieMensuelProcess process = new APGenererDroitPandemieMensuelProcess((BSession) session);
        process.setEMailAddress(apDroitPandemieMensuelviewBean.getEMailAddress());
        process.setDateArrivee(apDroitPandemieMensuelviewBean.getDateArrivee());
        process.setDateDepart(apDroitPandemieMensuelviewBean.getDateDepart());
        process.setDateFin(apDroitPandemieMensuelviewBean.getDateFin());
        process.setGenreService(APPandemieServiceCalcul.resolveEnum(apDroitPandemieMensuelviewBean.getGenreService()));
        process.setCategorie(apDroitPandemieMensuelviewBean.getCategorieEntreprise());
        process.setIsDefinitif(apDroitPandemieMensuelviewBean.getIsDefinitif());
        process.setIdDroit(null);
        process.start();
    }
}
