package globaz.apg.helpers.process;

import globaz.apg.api.codesystem.IAPCatalogueTexte;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.process.APGenererDecisionCommunicationAMATProcess;
import globaz.apg.vb.process.APGenererDecisionAMATViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.ged.client.JadeGedFacade;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APGenererDecisionAMATHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // vérifier qu'il y a des prestations calculées pour ce droit
        APPrestationManager mgr = new APPrestationManager();

        mgr.setSession((BSession) session);
        mgr.setForIdDroit(((APGenererDecisionAMATViewBean) viewBean).getIdDroit());

        if (mgr.getCount() > 0) {
            ((APGenererDecisionAMATViewBean) viewBean).setCalcule(true);
        }

        if (JadeGedFacade.isInstalled()) {
            List l = JadeGedFacade.getDocumentNamesList();
            for (Iterator iterator = l.iterator(); iterator.hasNext();) {
                String s = (String) iterator.next();
                if (s != null
                        && (s.startsWith(IPRConstantesExternes.DECISION_MATERNITE) || s
                                .startsWith("globaz.apg.itext.APDecisionCommunicationAMAT"))) {
                    ((APGenererDecisionAMATViewBean) viewBean).setDisplaySendToGed("1");
                    break;
                } else {
                    ((APGenererDecisionAMATViewBean) viewBean).setDisplaySendToGed("0");
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

        APGenererDecisionAMATViewBean gdViewBean = (APGenererDecisionAMATViewBean) viewBean;

        try {

            APGenererDecisionCommunicationAMATProcess process = new APGenererDecisionCommunicationAMATProcess(
                    (BSession) session);

            process.setEMailAddress(gdViewBean.getEmail());
            process.setCsTypeDocument(gdViewBean.isDecision() ? IAPCatalogueTexte.CS_DECISION_MAT
                    : IAPCatalogueTexte.CS_COMMUNICATION_MAT);
            process.setDate(gdViewBean.getDate());
            process.setIdDroit(gdViewBean.getIdDroit());
            process.setIsSendToGed(gdViewBean.getIsSendToGed());
            process.setDisplaySendToGed(gdViewBean.getDisplaySendToGed());
            process.start();

        } catch (Exception e) {
            e.printStackTrace();
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
