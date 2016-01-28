package globaz.cygnus.helpers.process;

import globaz.corvus.utils.REPmtMensuel;
import globaz.cygnus.process.RFListeContributionsAssistanceAIProcess;
import globaz.cygnus.vb.process.RFListeContributionsAssistanceAIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author PBA
 */
public class RFListeContributionsAssistanceAIHelper extends FWHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        RFListeContributionsAssistanceAIViewBean processViewBean = (RFListeContributionsAssistanceAIViewBean) viewBean;

        if (JadeStringUtil.isEmpty(processViewBean.getAdresseEmail())) {
            processViewBean.setAdresseEmail(session.getUserEMail());
        }

        processViewBean.setDateEnCoursAuLigne1("01." + REPmtMensuel.getDateDernierPmt((BSession) session));
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        RFListeContributionsAssistanceAIViewBean vb = (RFListeContributionsAssistanceAIViewBean) viewBean;

        if (validate((BSession) session, vb)) {

            try {
                RFListeContributionsAssistanceAIProcess process = new RFListeContributionsAssistanceAIProcess();
                process.setSession((BSession) session);
                process.setEMailAddress(vb.getAdresseEmail());

                switch (vb.getChoixTypePeriode()) {
                    case EnCoursAu:
                        process.setEnCoursLe(vb.getDateEnCoursAuLigne1());
                        break;
                    case EnCoursDurant:
                        process.setDateDebut(vb.getDateEnCoursDu());
                        process.setDateFin(vb.getDateEnCoursAuLigne2());
                        break;
                    default:
                        break;
                }

                BProcessLauncher.start(process, false);
            } catch (Exception e) {
                vb.setMessage(e.getMessage());
                vb.setMsgType(FWViewBeanInterface.ERROR);
            }
        }
    }

    private boolean validate(BSession session, RFListeContributionsAssistanceAIViewBean viewBean) {

        if (JadeStringUtil.isBlank(viewBean.getAdresseEmail())) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(session.getLabel("JSP_ERREUR_CHAMP_ADRESSE_MAIL_OBLIGATOIRE"));
            return false;
        }

        if (viewBean.getChoixTypePeriode() == null) {
            return false;
        }

        switch (viewBean.getChoixTypePeriode()) {

            case SansPeriode:
                return true;

            case EnCoursAu:
                if (!JadeDateUtil.isGlobazDate(viewBean.getDateEnCoursAuLigne1())) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(session.getLabel("PROCESS_CAAI_ERREUR_DATE_MANQUANTE"));
                    return false;
                }
                break;

            case EnCoursDurant:
                if (!JadeDateUtil.isGlobazDate(viewBean.getDateEnCoursDu())
                        || !JadeDateUtil.isGlobazDate(viewBean.getDateEnCoursAuLigne2())) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(session.getLabel("PROCESS_CAAI_ERREUR_DATE_MANQUANTE"));
                    return false;
                }
                break;
        }
        return true;
    }
}
