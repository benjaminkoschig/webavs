package globaz.pegasus.helpers.decision;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.pegasus.helpers.PegasusHelper;
import globaz.pegasus.process.decision.PCImprimerDecisionsProcess;
import globaz.pegasus.vb.decision.PCImprimerDecisionsViewBean;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.decision.DecisionTypes;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.businessimpl.utils.PCGedUtils;

public class PCImprimerDecisionsProcessHelper extends PegasusHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (viewBean instanceof PCImprimerDecisionsViewBean) {
            PCImprimerDecisionsProcess process = new PCImprimerDecisionsProcess();
            process.setSession((BSession) session);
            process.setIdDecisionsIdToPrint(((PCImprimerDecisionsViewBean) viewBean).getDecisionsId());
            process.setDecisionType(DecisionTypes.valueOf(((PCImprimerDecisionsViewBean) viewBean).getTypeDecision()));
            process.setMailGest(((PCImprimerDecisionsViewBean) viewBean).getMailGest());
            process.setDateDoc(((PCImprimerDecisionsViewBean) viewBean).getDateDoc());
            process.setPersref(((PCImprimerDecisionsViewBean) viewBean).getPersref());

            try {
                enforceGedCheck(((PCImprimerDecisionsViewBean) viewBean).getToGed());

                process.setForGed(((PCImprimerDecisionsViewBean) viewBean).getToGed());

                BProcessLauncher.startJob(process);
            } catch (Exception e) {
                putTransactionInError(viewBean, e);
            }
        } else {
            super._start(viewBean, action, session);
        }
    }

    private void enforceGedCheck(boolean isForGedValue) throws PropertiesException, JadeServiceLocatorException,
            JadeServiceActivatorException, NullPointerException, ClassCastException, JadeClassCastException,
            DecisionException {
        if (isForGedValue) {
            if (!PCGedUtils.isDocumentInGed(IPRConstantesExternes.PC_REF_INFOROM_DECISION_APRES_CALCUL,
                    BSessionUtil.getSessionFromThreadContext())) {
                String warnLabel = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_IMPRESSION_GED_WARN");
                JadeLogger.info(this, warnLabel);
            }
        }
    }

}
