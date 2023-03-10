/*
 * Cr?? le 29 juin 05
 */
package globaz.apg.helpers.process;

import globaz.apg.process.APEnvoyerAnnoncesSedexProcess;
import globaz.apg.vb.process.APEnvoyerAnnoncesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <H1>Description</H1>
 *
 * @author dvh
 */
public class APEnvoyerAnnoncesHelper extends FWHelper {

    /**
     * (non javadoc)
     *
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        APEnvoyerAnnoncesViewBean eaViewBean = (APEnvoyerAnnoncesViewBean) viewBean;
        APEnvoyerAnnoncesSedexProcess process = new APEnvoyerAnnoncesSedexProcess((BSession) session);
        if (!JadeStringUtil.isBlank(eaViewBean.getForGenreServices())) {
            process.setForGenreServiceIn(Arrays.stream(eaViewBean.getForGenreServices().split(",")).map(String::trim).collect(Collectors.toList()));
        }
        process.setEMailAddress(eaViewBean.getEMailAddress());
        process.setForMoisAnneeComptable(eaViewBean.getForMoisAnneeComptable());
        process.start();
    }
}
