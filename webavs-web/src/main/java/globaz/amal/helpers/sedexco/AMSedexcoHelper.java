package globaz.amal.helpers.sedexco;

import globaz.amal.vb.sedexco.AMSedexcoViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.File;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.businessimpl.services.sedexCO.AnnoncesCOReceptionMessage5234_000401_1;

public class AMSedexcoHelper extends FWHelper {
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("imprimerListe".equals(action.getActionPart())) {
            AMSedexcoViewBean vb = (AMSedexcoViewBean) viewBean;
            vb.setISession(session);
            try {
                vb.retrieve();

                if (!vb.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().isNew()) {
                    SimpleAnnonceSedexCO simpleAnnonceSedexCO = new SimpleAnnonceSedexCO();
                    if ("000401".equals(simpleAnnonceSedexCO.getMessageSubType())) {
                        AnnoncesCOReceptionMessage5234_000401_1 reception401 = new AnnoncesCOReceptionMessage5234_000401_1();
                        File file = reception401.generationList(simpleAnnonceSedexCO);

                        JadeSmtpClient.getInstance().sendMail(session.getUserEMail(), "Liste 401", "", filenames);
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(vb.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO());
            // CAExtournerOperationViewBean oper = (CAExtournerOperationViewBean) viewBean;
            // oper.setSession((BSession) session);
            // // Retrieve the operation
            // oper.getOperation();
        } else {
            super.execute(viewBean, action, session);
        }

        return viewBean;
    }

}
