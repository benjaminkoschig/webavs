package globaz.corvus.helpers.process;

import globaz.corvus.process.REAnnulerDiminutionRenteAccordeeProcess;
import globaz.corvus.vb.process.REAnnulerDiminutionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * 
 * @author SCR
 * 
 */
public class REAnnulerDiminutionHelper extends PRAbstractHelper {

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REAnnulerDiminutionViewBean vb = (REAnnulerDiminutionViewBean) viewBean;

        REAnnulerDiminutionRenteAccordeeProcess process = null;

        try {
            process = new REAnnulerDiminutionRenteAccordeeProcess();
            process.setSession((BSession) session);
            process.setIdRenteAccordee(vb.getIdRenteAccordee());
            process.setCodeMutation(vb.getCodeMutation());

            process.setDateDebutDroit(vb.getDateDebutDroitRA());
            process.setDateFinDroit(vb.getDateFinDroitRA());
            process.setGenreRente(vb.getGenreRente());
            process.setDateFinDroitModifiee(vb.getDateFinDroitModifiee());
            process.setIdTiersBeneficiaire(vb.getIdTiersBeneficiaire());
            process.setMontant(vb.getMontant());
            process.setEMailAddress(session.getUserEMail());
            process.setErrorMsg(vb.getErrorMsg());
            process.executeProcess();

        } catch (Exception e) {
            vb.setMsgType(FWViewBeanInterface.ERROR);
            vb.setMessage(e.toString());
        } finally {
            try {
                if (process != null) {
                    JadeSmtpClient.getInstance().sendMail(process.getEMailAddress(), process.getEMailObject(),
                            process.getMemoryLog().getMessagesInString(), null);
                }
            } catch (Exception e) {
                vb.setMsgType(FWViewBeanInterface.ERROR);
                vb.setMessage(e.toString());
            }

        }
    }
}
