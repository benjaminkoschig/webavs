package globaz.amal.helpers.sedexco;

import globaz.amal.vb.sedexco.AMSedexcoViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.businessimpl.services.sedexCO.AnnoncesCOReceptionMessage5234_000401_1;
import ch.globaz.amal.businessimpl.services.sedexCO.AnnoncesCOReceptionMessage5234_000402_1;

public class AMSedexcoHelper extends FWHelper {
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("imprimerListe".equals(action.getActionPart())) {
            AMSedexcoViewBean vb = (AMSedexcoViewBean) viewBean;
            vb.setISession(session);
            try {
                vb.retrieve();
                if (!vb.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().isNew()) {
                    SimpleAnnonceSedexCO simpleAnnonceSedexCO = vb.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO();
                    if ("401".equals(simpleAnnonceSedexCO.getMessageSubType())) {
                        AnnoncesCOReceptionMessage5234_000401_1 reception401 = new AnnoncesCOReceptionMessage5234_000401_1();
                        reception401.setSenderId(simpleAnnonceSedexCO.getMessageEmetteur());
                        reception401.generationList(simpleAnnonceSedexCO);
                    } else if ("402".equals(simpleAnnonceSedexCO.getMessageSubType())) {
                        AnnoncesCOReceptionMessage5234_000402_1 reception402 = new AnnoncesCOReceptionMessage5234_000402_1();
                        reception402.setSenderId(simpleAnnonceSedexCO.getMessageEmetteur());
                        reception402.generationList(simpleAnnonceSedexCO);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            super.execute(viewBean, action, session);
        }
        return viewBean;
    }
}
