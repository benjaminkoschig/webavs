package globaz.phenix.helpers.suivis;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.suivi.AFSuiviRadiation;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.vb.suivis.CPRevBilanViewBean;

public class CPRadiationAffiliationHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CPRevBilanViewBean vb = (CPRevBilanViewBean) viewBean;
        AFAffiliation aff = new AFAffiliation();
        aff.setISession(vb.getISession());
        aff.setAffiliationId(vb.getAffiliationId());

        try {
            aff.retrieve();

            if (aff.getSession().hasErrors()) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(aff.getSession().getErrors().toString());
            } else {
                if (aff.isAffiliationAVS()) {
                    if (aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP)
                            || aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)
                            || aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)) {
                        AFSuiviRadiation suiviRadiation = new AFSuiviRadiation((BSession) vb.getISession());
                        suiviRadiation.genererControle(vb.getDateFin(), aff, vb.getEMailAddress());
                    }
                }
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
