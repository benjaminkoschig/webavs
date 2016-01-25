package globaz.aries.helpers.renouvellementdecisionmasse;

import globaz.aries.process.renouvellementdecisionmasse.ARRenouvellementDecisionMasseProcess;
import globaz.aries.vb.renouvellementdecisionmasse.ARRenouvellementDecisionMasseViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModulePassageManager;
import ch.globaz.aries.businessimpl.checkers.RenouvellementDecisionCGASChecker;
import ch.globaz.common.business.exceptions.CommonJobException;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.business.services.FABusinessServiceLocator;

public class ARRenouvellementDecisionMasseHelper extends FWHelper {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (ARRenouvellementDecisionMasseViewBean.class.isAssignableFrom(viewBean.getClass())) {
            try {

                ARRenouvellementDecisionMasseViewBean renouvellementDecisionMasseViewBean = (ARRenouvellementDecisionMasseViewBean) viewBean;

                RenouvellementDecisionCGASChecker.checkSaisieEcranLancementTraitementMasse(
                        renouvellementDecisionMasseViewBean.getNumeroPassage(),
                        renouvellementDecisionMasseViewBean.getAnnee(),
                        renouvellementDecisionMasseViewBean.getNumeroAffilieDebut(),
                        renouvellementDecisionMasseViewBean.getNumeroAffilieFin(),
                        renouvellementDecisionMasseViewBean.getEmail());

                PassageModel thePassage = FABusinessServiceLocator.getPassageModelService().read(
                        renouvellementDecisionMasseViewBean.getNumeroPassage());

                FAModulePassageManager modulePassageManager = new FAModulePassageManager();
                modulePassageManager.setSession(BSessionUtil.getSessionFromThreadContext());
                modulePassageManager.setForIdPassage(renouvellementDecisionMasseViewBean.getNumeroPassage());
                modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_DECISION_CAP_CGAS);

                RenouvellementDecisionCGASChecker.checkPassageFacturation(thePassage,
                        modulePassageManager.getCount() >= 1);

                ARRenouvellementDecisionMasseProcess process = new ARRenouvellementDecisionMasseProcess();
                process.setNumeroPassageFacturation(renouvellementDecisionMasseViewBean.getNumeroPassage());
                process.setNumeroAffilieDebut(renouvellementDecisionMasseViewBean.getNumeroAffilieDebut());
                process.setNumeroAffilieFin(renouvellementDecisionMasseViewBean.getNumeroAffilieFin());
                process.setAnnee(renouvellementDecisionMasseViewBean.getAnnee());
                process.setAdresseEmail(renouvellementDecisionMasseViewBean.getEmail());
                process.setSession((BSession) session);

                BProcessLauncher.startJob(process);
            } catch (CommonJobException exceptionMetier) {
                // rien à faire ici car RenouvellementDecisionCGASChecker effectue les JadeThread.logError
            } catch (Exception e) {
                JadeThread.logError(this.getClass().getName(), "aries.renouvellement.decision.masse.erreur.technique");
            }

        } else {
            JadeThread.logError(this.getClass().getName(), "aries.fonction.non.implementee");
        }
    }
}
