package globaz.naos.helpers.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFFicheCartothequeViewBean;
import globaz.naos.itext.affiliation.AFFicheCartotheque_Doc;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFFicheCartothequeHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        AFFicheCartothequeViewBean afViewBean = (AFFicheCartothequeViewBean) viewBean;
        AFAffiliation affiliation = new AFAffiliation();

        affiliation.setAffiliationId(afViewBean.getAffiliationId());
        affiliation.setISession(session);
        affiliation.retrieve();

        afViewBean.setAffiliation(affiliation);
        afViewBean.setEmail(session.getUserEMail());
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFFicheCartothequeViewBean afViewBean = (AFFicheCartothequeViewBean) viewBean;
        AFFicheCartotheque_Doc doc = new AFFicheCartotheque_Doc();

        doc.setISession(session);
        doc.setAffiliationId(afViewBean.getAffiliationId());
        doc.setEMailAddress(afViewBean.getEmail());
        doc.setDateSituation(afViewBean.getDateSituation());

        try {
            BProcessLauncher.start(doc);
        } catch (Exception e) {
            afViewBean.setMessage(e.getMessage());
            afViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
