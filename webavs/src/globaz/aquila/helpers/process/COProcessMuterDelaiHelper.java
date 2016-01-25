package globaz.aquila.helpers.process;

import globaz.aquila.print.COMuterDelaiDoc;
import globaz.aquila.vb.process.COProcessMuterDelaiViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COProcessMuterDelaiHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param viewBean
     * @param action
     * @param session
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        COProcessMuterDelaiViewBean muBean = (COProcessMuterDelaiViewBean) viewBean;

        // lancer la creation du document
        COMuterDelaiDoc mDoc = new COMuterDelaiDoc();

        mDoc.setProchaineDateDeclenchement(muBean.getProchaineDateDeclenchement());
        mDoc.setDateDocument(muBean.getDateDocument());
        mDoc.setDateExecution(muBean.getDateDocument());
        mDoc.setParaEcheanceAffiche(muBean.getParaEcheanceAffiche());
        mDoc.setParaInteretsMoratoiresAffiche(muBean.getParaInteretsMoratoiresAffiche());
        mDoc.setPrevisionnel(false);
        mDoc.setEMailAddress(muBean.getEmail());
        mDoc.addContentieux(muBean.getContentieux());
        mDoc.setISession(session);

        try {
            mDoc.setUserIdCollaborateur(session.getUserId());
            BProcessLauncher.start(mDoc);
        } catch (Exception e) {
            ((BSession) session).addError(e.getMessage());
        }
    }
}
