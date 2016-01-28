package globaz.orion.helpers.swissdec;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.fs.JadeFsFacade;
import globaz.orion.vb.swissdec.EBPucsValidationDetailViewBean;
import ch.globaz.orion.business.constantes.EBProperties;

public class EBPucsValidationDetailHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (action.getActionPart().equals("accepter")) {
            executeAccepter(viewBean, action, session);
        } else if (action.getActionPart().equals("refuser")) {
            executeRefuser(viewBean, action, session);
        } else if (action.getActionPart().equals("annulerRefus")) {
            executeAnnulerRefuser(viewBean, action, session);
        }

        return super.execute(viewBean, action, session);
    }

    private FWViewBeanInterface executeAccepter(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        EBPucsValidationDetailViewBean vb = (EBPucsValidationDetailViewBean) viewBean;

        try {
            String path = EBProperties.PUCS_SWISS_DEC_DIRECTORY_A_VALIDER.getValue() + "/" + vb.getId() + ".xml";
            String dest = EBProperties.PUCS_SWISS_DEC_DIRECTORY.getValue() + "/" + vb.getId() + ".xml";

            JadeFsFacade.copyFile(path, dest);
            JadeFsFacade.delete(path);

        } catch (Exception e) {
            vb.setMessage(e.getMessage());
            vb.setMsgType(FWViewBeanInterface.ERROR);
        }

        return vb;
    }

    private FWViewBeanInterface executeRefuser(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        EBPucsValidationDetailViewBean vb = (EBPucsValidationDetailViewBean) viewBean;

        try {
            String path = EBProperties.PUCS_SWISS_DEC_DIRECTORY_A_VALIDER.getValue() + "/" + vb.getId() + ".xml";
            String dest = EBProperties.PUCS_SWISS_DEC_DIRECTORY_REFUSER.getValue() + "/" + vb.getId() + ".xml";

            JadeFsFacade.copyFile(path, dest);
            JadeFsFacade.delete(path);

        } catch (Exception e) {
            vb.setMessage(e.getMessage());
            vb.setMsgType(FWViewBeanInterface.ERROR);
        }

        return vb;
    }

    private FWViewBeanInterface executeAnnulerRefuser(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        EBPucsValidationDetailViewBean vb = (EBPucsValidationDetailViewBean) viewBean;

        try {
            String path = EBProperties.PUCS_SWISS_DEC_DIRECTORY_REFUSER.getValue() + "/" + vb.getId() + ".xml";
            String dest = EBProperties.PUCS_SWISS_DEC_DIRECTORY_A_VALIDER.getValue() + "/" + vb.getId() + ".xml";

            JadeFsFacade.copyFile(path, dest);
            JadeFsFacade.delete(path);

        } catch (Exception e) {
            vb.setMessage(e.getMessage());
            vb.setMsgType(FWViewBeanInterface.ERROR);
        }

        return vb;
    }
}
