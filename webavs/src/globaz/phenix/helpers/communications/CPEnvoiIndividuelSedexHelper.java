package globaz.phenix.helpers.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.phenix.process.communications.CPProcessXMLSedexWriter;
import globaz.phenix.vb.communications.CPEnvoiIndividuelSedexViewBean;

public class CPEnvoiIndividuelSedexHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof CPEnvoiIndividuelSedexViewBean) {
            CPProcessXMLSedexWriter process = new CPProcessXMLSedexWriter();
            process.setEMailAddress(((CPEnvoiIndividuelSedexViewBean) viewBean).getEMailAddress());
            process.setEnvoiImmediat(((CPEnvoiIndividuelSedexViewBean) viewBean).getEnvoiImmediat());
            process.setDonneesCommerciales(((CPEnvoiIndividuelSedexViewBean) viewBean).getDonneesCommerciales());
            process.setDonneesPrivees(((CPEnvoiIndividuelSedexViewBean) viewBean).getDonneesPrivees());
            process.setIdCommunication(((CPEnvoiIndividuelSedexViewBean) viewBean).getId());
            process.setLifd(((CPEnvoiIndividuelSedexViewBean) viewBean).getLifd());
            process.setEnvoiIndividuel(true);
            process.setSession((BSession) session);
            try {
                BProcessLauncher.start(process);
            } catch (Exception e) {
                viewBean.setMessage(process.getSession().getLabel("ERREUR_TECHNIQUE") + "\n"
                        + "CPEnvoiIndividuelSedexHelper" + e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

        } else {
            super._start(viewBean, action, session);
        }

    }

}
