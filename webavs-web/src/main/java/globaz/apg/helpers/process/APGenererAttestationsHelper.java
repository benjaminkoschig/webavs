/*
 * Crée le 21 septembre 2006
 */
package globaz.apg.helpers.process;

import globaz.apg.process.APGenererAttestationsProcess;
import globaz.apg.vb.process.APGenererAttestationsViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * Helper pour la génération des attestations fiscales
 * 
 * @author hpe
 */
public class APGenererAttestationsHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        super._retrieve(viewBean, action, session);
        if (JadeGedFacade.isInstalled()) {
            List l = JadeGedFacade.getDocumentNamesList();
            for (Iterator iterator = l.iterator(); iterator.hasNext();) {
                String s = (String) iterator.next();
                if (s != null && (s.startsWith(IPRConstantesExternes.ATTESTATION_FISCALE_APG)
                    || s.startsWith(IPRConstantesExternes.ATTESTATION_FISCALE_MATERNITE)
                    || s.startsWith(IPRConstantesExternes.ATTESTATION_FISCALE_PANDEMIE)
                )) {
                    ((APGenererAttestationsViewBean) viewBean).setDisplaySendToGed("1");
                    break;
                } else {
                    ((APGenererAttestationsViewBean) viewBean).setDisplaySendToGed("0");
                }
            }
        }
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        BSession bSession = (BSession) session;

        APGenererAttestationsViewBean gaViewBean = (APGenererAttestationsViewBean) viewBean;

        APGenererAttestationsProcess process = new APGenererAttestationsProcess(bSession);

        process.setIsGenerationUnique(gaViewBean.getIsGenerationUnique());
        process.setNSS(gaViewBean.getNSS());
        process.setAnnee(gaViewBean.getAnnee());
        process.setEMailAddress(gaViewBean.getEmail());
        process.setIsSendToGed(gaViewBean.getIsSendToGed());
        process.setTypePrestation(gaViewBean.getTypePrestation());
        process.start();
    }
}
