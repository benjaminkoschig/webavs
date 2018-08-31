package globaz.vulpecula.helpers.ctrlemployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.ctrlemployeur.PTLettreEmployeurActifSansPersonnelViewBean;
import ch.globaz.vulpecula.documents.ctrlemployeur.DocumentLettresEmployeursActifsSansPersonnelPrinter;

/**
 * 
 * @author jwe
 * 
 */
public class PTLettreEmployeurActifSansPersonnelHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTLettreEmployeurActifSansPersonnelViewBean vb = (PTLettreEmployeurActifSansPersonnelViewBean) viewBean;
            DocumentLettresEmployeursActifsSansPersonnelPrinter docs = new DocumentLettresEmployeursActifsSansPersonnelPrinter(
                    vb.getDateReference(), vb.getDateEnvoi());
            docs.setEMailAddress(vb.getEmail());
            BProcessLauncher.start(docs);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
