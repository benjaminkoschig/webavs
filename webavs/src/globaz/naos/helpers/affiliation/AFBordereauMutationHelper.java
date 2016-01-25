package globaz.naos.helpers.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFBordereauMutationViewBean;
import globaz.naos.itext.affiliation.AFBordereauMutation_Doc;

/**
 * <H1>Description</H1>
 * 
 * @author sda
 */
public class AFBordereauMutationHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        AFBordereauMutationViewBean afViewBean = (AFBordereauMutationViewBean) viewBean;
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
        AFBordereauMutationViewBean afViewBean = (AFBordereauMutationViewBean) viewBean;
        AFBordereauMutation_Doc doc = new AFBordereauMutation_Doc();

        doc.setISession(session);
        doc.setAffiliationId(afViewBean.getAffiliation().getAffiliationId());
        doc.setObservations(afViewBean.getObservations());
        // doc.setAffiliation(afViewBean.getAffiliation());
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
