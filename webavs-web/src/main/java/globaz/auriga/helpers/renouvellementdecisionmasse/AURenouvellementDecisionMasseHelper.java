package globaz.auriga.helpers.renouvellementdecisionmasse;

import globaz.auriga.process.renouvellementdecisionmasse.AURenouvellementDecisionMasseProcess;
import globaz.auriga.vb.renouvellementdecisionmasse.AURenouvellementDecisionMasseViewBean;
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
import ch.globaz.auriga.businessimpl.checkers.RenouvellementDecisionCAPChecker;
import ch.globaz.common.business.exceptions.CommonJobException;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.business.services.FABusinessServiceLocator;

public class AURenouvellementDecisionMasseHelper extends FWHelper {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (AURenouvellementDecisionMasseViewBean.class.isAssignableFrom(viewBean.getClass())) {
            try {

                AURenouvellementDecisionMasseViewBean renouvellementDecisionMasseViewBean = (AURenouvellementDecisionMasseViewBean) viewBean;

                RenouvellementDecisionCAPChecker.checkSaisieEcranLancementTraitementMasse(
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

                RenouvellementDecisionCAPChecker.checkPassageFacturation(thePassage,
                        modulePassageManager.getCount() >= 1);

                AURenouvellementDecisionMasseProcess process = new AURenouvellementDecisionMasseProcess();
                process.setNumeroPassageFacturation(renouvellementDecisionMasseViewBean.getNumeroPassage());
                process.setNumeroAffilieDebut(renouvellementDecisionMasseViewBean.getNumeroAffilieDebut());
                process.setNumeroAffilieFin(renouvellementDecisionMasseViewBean.getNumeroAffilieFin());
                process.setAnnee(renouvellementDecisionMasseViewBean.getAnnee());
                process.setAdresseEmail(renouvellementDecisionMasseViewBean.getEmail());
                process.setSession((BSession) session);

                BProcessLauncher.startJob(process);
            } catch (CommonJobException exceptionMetier) {
                // rien à faire ici car RenouvellementDecisionCAPChecker effectue les JadeThread.logError
            } catch (Exception e) {
                JadeThread.logError(this.getClass().getName(), "auriga.renouvellement.decision.masse.erreur.technique");
            }

        } else {
            JadeThread.logError(this.getClass().getName(), "auriga.fonction.non.implementee");
        }
    }
}
