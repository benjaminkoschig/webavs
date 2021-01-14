package globaz.pegasus.helpers.decision;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.process.decision.PCImprimerDecisionsProcess;
import globaz.pegasus.vb.decision.PCValidationDecisionsViewBean;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.decision.DecisionTypes;

/**
 * Helper pour action custom valider
 * 
 * @author sce
 * 
 */
public class PCValidationDecisionsHelper extends PegasusHelper {

    /**
     * Methode de lancement du viewBean et du pricessus d'impression, le cas échéant
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    private FWViewBeanInterface _validerDecision(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {

        PCValidationDecisionsViewBean vb = (PCValidationDecisionsViewBean) viewBean;
        // Prévalidation du viewBean
        vb.update();

        // si pas en erreur
        if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)
                && !FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            if (EPCProperties.PUBLICATION_FTP_AUTO_DAC_VALIDATION.getBooleanValue()) {
                // Instanciation du process d'impression
                PCImprimerDecisionsProcess process = new PCImprimerDecisionsProcess();
                process.setSession((BSession) session);
                process.setIdDecisionsIdToPrint(vb.getDecisionsId());
                process.setDecisionType(DecisionTypes.DECISION_APRES_CALCUL);
                // Personne de référence --> voir VB
                String persRef = vb.getPersonneRef();
                // recup gestionnaire
                JadeUser gestionnaire = getSession().getApplication()._getSecurityManager()
                        .getUserForVisa(getSession(), persRef);

                process.setMailGest(gestionnaire.getEmail());
                process.setDateDoc(vb.getDateDoc());
                process.setPersref(persRef);
                process.setIsForFtp(Boolean.TRUE);
                process.setIsForFtpValid(Boolean.TRUE);
                BProcessLauncher.startJob(process);

            }
        }

        return viewBean;

    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            String actionPart = action.getActionPart();
            if (actionPart.equals("valider")) {
                viewBean = _validerDecision(viewBean, action, session);
            } else {
                viewBean = super.execute(viewBean, action, session);
            }
        } catch (Exception e) {
            putTransactionInError(viewBean, e);
        }

        return viewBean;
    }

    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }
}
