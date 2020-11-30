/*
 * Créé le 20 nov. 06
 */
package globaz.apg.helpers.process;

import globaz.apg.process.APEnvoyerCIProcess;
import globaz.apg.vb.process.APEnvoyerCIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.api.IPRDemande;
import org.apache.commons.lang.StringUtils;

/**
 * @author hpe
 * 
 */
public class APInscrireCIHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        APEnvoyerCIViewBean cpViewBean = (APEnvoyerCIViewBean) viewBean;

        APEnvoyerCIProcess process = new APEnvoyerCIProcess((BSession) session);

        process.setEMailAddress(cpViewBean.getEMailAddress());
        process.setNoPassage(cpViewBean.getNoPassageFinal());
        if (StringUtils.equals(IPRDemande.CS_TYPE_PANDEMIE,cpViewBean.getTypePrestation())) {
            process.setPandemie(true);
        }

        if (cpViewBean.isRegeneration()) {
            process.setIsRegen(Boolean.TRUE);
        } else {
            process.setIsRegen(Boolean.FALSE);
        }

        process.start();
    }
}
