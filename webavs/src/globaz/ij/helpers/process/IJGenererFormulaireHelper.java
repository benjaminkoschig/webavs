package globaz.ij.helpers.process;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.itext.IJFormulaires;
import globaz.ij.vb.process.IJGenererFormulaireViewBean;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJGenererFormulaireHelper extends PRAbstractHelper {

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
                if (s != null && s.startsWith(IPRConstantesExternes.FORMULAIRE_BASE_INDEMNI_IJ)) {
                    ((IJGenererFormulaireViewBean) viewBean).setDisplaySendToGed("1");
                    break;
                } else {
                    ((IJGenererFormulaireViewBean) viewBean).setDisplaySendToGed("0");
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
        IJGenererFormulaireViewBean gfViewBean = (IJGenererFormulaireViewBean) viewBean;

        try {
            IJFormulaires document = new IJFormulaires((BSession) session);

            document.setEMailAddress(gfViewBean.getEmail());
            document.setIdFormulaire(gfViewBean.getIdFormulaire());
            document.setIdPrononce(gfViewBean.getIdPrononce());
            document.setCsTypeIJ(gfViewBean.getCsTypeIJ());
            document.setDate(gfViewBean.getDate());
            document.setDateRetour(gfViewBean.getDateRetour());
            document.setIsSendToGed(gfViewBean.getIsSendToGed());
            document.setPublishDocument(Boolean.TRUE);
            document.start();
        } catch (FWIException e) {
            gfViewBean.setMessage(e.getMessage());
            gfViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
