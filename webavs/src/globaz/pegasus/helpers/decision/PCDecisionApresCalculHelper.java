package globaz.pegasus.helpers.decision;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.process.decision.PCImprimerDecisionsProcess;
import globaz.pegasus.vb.decision.PCDecisionApresCalculViewBean;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.decision.DecisionTypes;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;

public class PCDecisionApresCalculHelper extends PegasusHelper {
    /**
     * detailler - custom action , affichage du détail de la décision
     * 
     * @param viewBean
     *            PCDecisionAporesCAlculViewBean
     * @param action
     *            l'action appelé
     * @param session
     *            la session courante
     * @return FWViewBEanInterface, le viewBean traité
     * @throws Exception
     */
    private FWViewBeanInterface _detaillerDecision(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {
        ((PCDecisionApresCalculViewBean) viewBean).detail();
        return viewBean;
    }

    /**
     * Prévalidation - custom action
     * 
     * @param viewBean
     *            FWViewBEanInterface, le viewBEan traité
     * @param action
     *            l'action appelé
     * @param session
     *            la session courante
     * @return FWViewBEanInterface, le viewBEan traité
     * @throws Exception
     */
    private FWViewBeanInterface _prevaliderDecision(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {

        PCDecisionApresCalculViewBean vb = (PCDecisionApresCalculViewBean) viewBean;
        // Prévalisation du viewBean
        vb.prevalider();

        if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)
                && !FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            // Si propriétés envoi auto decision (sans copie)
            if (PCproperties.getBoolean(EPCProperties.PUBLICATION_FTP_AUTO_DAC_PREVALIDATION)) {

                PCImprimerDecisionsProcess process = new PCImprimerDecisionsProcess();
                process.setSession((BSession) session);
                process.addDecisionToPrint(vb.getDecisionApresCalcul().getSimpleDecisionApresCalcul()
                        .getIdDecisionApresCalcul());
                process.setDecisionType(DecisionTypes.DECISION_APRES_CALCUL);
                String persRef = vb.getDecisionApresCalcul().getDecisionHeader().getSimpleDecisionHeader()
                        .getPreparationPar();

                process.setMailGest(getSession().getUserEMail());
                process.setDateDoc(vb.getDecisionApresCalcul().getDecisionHeader().getSimpleDecisionHeader()
                        .getDatePreparation());
                process.setPersref(persRef);
                process.setIsForFtp(Boolean.TRUE);

                BProcessLauncher.startJob(process);

            }
        }

        return viewBean;
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            String actionPart = action.getActionPart();
            if (actionPart.equals("detail")) {
                viewBean = _detaillerDecision(viewBean, action, session);
            } else if (actionPart.equals("prevalider")) {
                viewBean = _prevaliderDecision(viewBean, action, session);
            }

            else {
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
