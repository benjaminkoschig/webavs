/*
 * Crée le 6 septembre 2006
 */
package globaz.ij.helpers.process;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.process.IJGenererAttestationsProcess;
import globaz.ij.vb.process.IJGenererAttestationsViewBean;
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
public class IJGenererAttestationsHelper extends PRAbstractHelper {

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
                if (s != null && s.startsWith(IPRConstantesExternes.ATTESTATION_FISCALE_IJ)) {
                    ((IJGenererAttestationsViewBean) viewBean).setDisplaySendToGed("1");
                    break;
                } else {
                    ((IJGenererAttestationsViewBean) viewBean).setDisplaySendToGed("0");
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

        IJGenererAttestationsViewBean gaViewBean = (IJGenererAttestationsViewBean) viewBean;

        IJGenererAttestationsProcess process = new IJGenererAttestationsProcess(bSession);

        process.setIsGenerationUnique(gaViewBean.getIsGenerationUnique());
        process.setNSS(gaViewBean.getNSS());
        process.setAnnee(gaViewBean.getAnnee());
        process.setEMailAddress(gaViewBean.getEmail());
        process.setIsSendToGed(gaViewBean.getIsSendToGed());
        process.start();

    }

}
