package globaz.naos.helpers.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFLettreBienvenueViewBean;
import globaz.naos.itext.affiliation.AFLettreDeBienvenue;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFLettreBienvenueHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        AFLettreBienvenueViewBean afViewBean = (AFLettreBienvenueViewBean) viewBean;
        AFAffiliation affiliation = new AFAffiliation();

        affiliation.setAffiliationId(afViewBean.getIdAffiliation());
        affiliation.setISession(session);
        affiliation.retrieve();

        // afViewBean.seta(affiliation);
        afViewBean.setEMailAddress(afViewBean.getEMailAddress());
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFLettreBienvenueViewBean afViewBean = null;
        AFLettreDeBienvenue doc = null;
        try {

            afViewBean = (AFLettreBienvenueViewBean) viewBean;
            doc = new AFLettreDeBienvenue();

            doc.setISession(session);
            doc.setIdAffiliation(afViewBean.getIdAffiliation());
            doc.setEMailAddress(afViewBean.getEMailAddress());
            // doc.setDateSituation(afViewBean.getDateSituation());

            BProcessLauncher.start(doc);
        } catch (Exception e) {
            afViewBean.setMessage(e.getMessage());
            afViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
