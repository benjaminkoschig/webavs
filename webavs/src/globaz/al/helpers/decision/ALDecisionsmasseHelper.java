package globaz.al.helpers.decision;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.process.decision.ALDecisionsMasseListeProcess;
import globaz.al.process.decision.ALDecisionsMasseProcess;
import globaz.al.vb.decision.ALDecisionsmasseViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;

public class ALDecisionsmasseHelper extends ALAbstractHelper {

    /**
     * Constructeur
     */
    public ALDecisionsmasseHelper() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#execute(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        ALDecisionsmasseViewBean decisionsmasseViewBean = (ALDecisionsmasseViewBean) viewBean;

        if (JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getEmail())) {
            JadeThread.logError(this.getClass().getName(), "globaz.al.vb.decision.ALDecisionsMasseViewBean.email");
        }
        if ("importer".equals(action.getActionPart())) {
            if (!JadeDateUtil.isGlobazDate(decisionsmasseViewBean.getDateDebutValidite())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.dateDebutValidite");
            }
            if (JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getFileName())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.fichierListeDossiers");
            }
            if (!JadeDateUtil.isGlobazDate(decisionsmasseViewBean.getDateImpression())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.dateImpression");
            }
            if (JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getEmail())) {
                JadeThread.logError(this.getClass().getName(), "globaz.al.vb.decision.ALDecisionsMasseViewBean.email");
            }

            if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {

                try {
                    ALDecisionsMasseProcess process = new ALDecisionsMasseProcess();
                    process.setSession((BSession) session);

                    process.setDateDebutValidite(decisionsmasseViewBean.getDateDebutValidite());
                    process.setEtatFilter(DossierComplexSearchModel.ETATACTIF);
                    process.setFileName(JadeStringUtil.change(decisionsmasseViewBean.getFileName(), "\\", "/"));
                    process.setOriginalFileName(decisionsmasseViewBean.getOriginalFileName());

                    process.setTexteLibre(decisionsmasseViewBean.getTexteLibre());
                    process.setGestionCopie(decisionsmasseViewBean.getGestionCopie());
                    process.setGestionTexteLibre(decisionsmasseViewBean.getGestionTexteLibre());
                    process.setDateImpression(decisionsmasseViewBean.getDateImpression());
                    process.setInsertionGED(decisionsmasseViewBean.getInsertionGED());
                    process.setTriImpression(decisionsmasseViewBean.getTriImpression());
                    process.setEmail(decisionsmasseViewBean.getEmail());

                    BProcessLauncher.start(process, false);

                } catch (Exception e) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(e.getMessage());
                }
            } else {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).toString());
            }

            return viewBean;
        } else if ("searchactif".equals(action.getActionPart()) || "searchradie".equals(action.getActionPart())) {
            if ("searchactif".equals(action.getActionPart())
                    && !JadeDateUtil.isGlobazDate(decisionsmasseViewBean.getDateDebutValidite())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.dateDebutValidite");
            }
            if (!JadeDateUtil.isGlobazDate(decisionsmasseViewBean.getDateImpression())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.dateImpression");
            }
            if (!JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getDateValiditeGREAT())
                    && !JadeDateUtil.isGlobazDate(decisionsmasseViewBean.getDateValiditeGREAT())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.getDateValidite");
            }
            if (!JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getDateValiditeLESS())
                    && !JadeDateUtil.isGlobazDate(decisionsmasseViewBean.getDateValiditeLESS())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.getDateValidite");
            }
            if (!JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getDateFinValiditeGREAT())
                    && !JadeDateUtil.isGlobazDate(decisionsmasseViewBean.getDateFinValiditeGREAT())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.getDateFinValidite");
            }
            if (!JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getDateFinValiditeLESS())
                    && !JadeDateUtil.isGlobazDate(decisionsmasseViewBean.getDateFinValiditeLESS())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.getDateFinValidite");
            }
            if (JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getEmail())) {
                JadeThread.logError(this.getClass().getName(), "globaz.al.vb.decision.ALDecisionsMasseViewBean.email");
            }
            if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {
                try {
                    ALDecisionsMasseProcess process = new ALDecisionsMasseProcess();
                    process.setSession((BSession) session);

                    process.setDateDebutValidite(decisionsmasseViewBean.getDateDebutValidite());

                    process.setTexteLibre(decisionsmasseViewBean.getTexteLibre());
                    process.setGestionCopie(decisionsmasseViewBean.getGestionCopie());
                    process.setGestionTexteLibre(decisionsmasseViewBean.getGestionTexteLibre());
                    process.setDateImpression(decisionsmasseViewBean.getDateImpression());
                    process.setInsertionGED(decisionsmasseViewBean.getInsertionGED());
                    process.setTriImpression(decisionsmasseViewBean.getTriImpression());
                    process.setEmail(decisionsmasseViewBean.getEmail());

                    process.setInNumeroAffilie(decisionsmasseViewBean.getInAffilie());
                    process.setInActivites(decisionsmasseViewBean.getInActivites());
                    process.setInStatut(decisionsmasseViewBean.getInStatut());
                    process.setInTarif(decisionsmasseViewBean.getInTarif());
                    process.setTypeDroit(decisionsmasseViewBean.getInTypeDroit());
                    process.setEtatFilter(decisionsmasseViewBean.getEtatFilter());
                    process.setDateValiditeGREAT(decisionsmasseViewBean.getDateValiditeGREAT());
                    process.setDateValiditeLESS(decisionsmasseViewBean.getDateValiditeLESS());
                    process.setDateFinValiditeGREAT(decisionsmasseViewBean.getDateFinValiditeGREAT());
                    process.setDateFinValiditeLESS(decisionsmasseViewBean.getDateFinValiditeLESS());

                    BProcessLauncher.start(process, false);

                } catch (Exception e) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(e.getMessage());
                }
            } else {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).toString());
            }
            return viewBean;
            // génération de liste
        } else if ("listeimporter".equals(action.getActionPart())) {

            if (JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getFileName())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.fichierListeDossiers");
            }

            if (JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getEmail())) {
                JadeThread.logError(this.getClass().getName(), "globaz.al.vb.decision.ALDecisionsMasseViewBean.email");
            }

            if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {

                try {
                    ALDecisionsMasseListeProcess process = new ALDecisionsMasseListeProcess();
                    process.setSession((BSession) session);

                    process.setDateDebutValidite(decisionsmasseViewBean.getDateDebutValidite());

                    process.setFileName(JadeStringUtil.change(decisionsmasseViewBean.getFileName(), "\\", "/"));
                    process.setOriginalFileName(decisionsmasseViewBean.getOriginalFileName());

                    process.setTexteLibre(decisionsmasseViewBean.getTexteLibre());
                    process.setGestionCopie(decisionsmasseViewBean.getGestionCopie());
                    process.setGestionTexteLibre(decisionsmasseViewBean.getGestionTexteLibre());
                    process.setDateImpression(decisionsmasseViewBean.getDateImpression());
                    process.setInsertionGED(decisionsmasseViewBean.getInsertionGED());
                    process.setTriImpression(decisionsmasseViewBean.getTriImpression());
                    process.setEmail(decisionsmasseViewBean.getEmail());

                    BProcessLauncher.start(process, false);

                } catch (Exception e) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(e.getMessage());
                }
            } else {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).toString());
            }

            return viewBean;
        } else if ("listeactif".equals(action.getActionPart()) || "listeradie".equals(action.getActionPart())) {

            if (!JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getDateValiditeGREAT())
                    && !JadeDateUtil.isGlobazDate(decisionsmasseViewBean.getDateValiditeGREAT())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.getDateValidite");
            }
            if (!JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getDateValiditeLESS())
                    && !JadeDateUtil.isGlobazDate(decisionsmasseViewBean.getDateValiditeLESS())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.getDateValidite");
            }
            if (!JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getDateFinValiditeGREAT())
                    && !JadeDateUtil.isGlobazDate(decisionsmasseViewBean.getDateFinValiditeGREAT())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.getDateFinValidite");
            }
            if (!JadeStringUtil.isBlankOrZero(decisionsmasseViewBean.getDateFinValiditeLESS())
                    && !JadeDateUtil.isGlobazDate(decisionsmasseViewBean.getDateFinValiditeLESS())) {
                JadeThread.logError(this.getClass().getName(),
                        "globaz.al.vb.decision.ALDecisionsMasseViewBean.getDateFinValidite");
            }
            if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {
                try {

                    ALDecisionsMasseListeProcess process = new ALDecisionsMasseListeProcess();
                    process.setSession((BSession) session);

                    process.setDateDebutValidite(decisionsmasseViewBean.getDateDebutValidite());
                    process.setTexteLibre(decisionsmasseViewBean.getTexteLibre());
                    process.setGestionCopie(decisionsmasseViewBean.getGestionCopie());
                    process.setGestionTexteLibre(decisionsmasseViewBean.getGestionTexteLibre());
                    process.setDateImpression(decisionsmasseViewBean.getDateImpression());
                    process.setInsertionGED(decisionsmasseViewBean.getInsertionGED());
                    process.setTriImpression(decisionsmasseViewBean.getTriImpression());
                    process.setEmail(decisionsmasseViewBean.getEmail());

                    process.setInNumeroAffilie(decisionsmasseViewBean.getInAffilie());
                    process.setInActivites(decisionsmasseViewBean.getInActivites());
                    process.setInStatut(decisionsmasseViewBean.getInStatut());
                    process.setInTarif(decisionsmasseViewBean.getInTarif());
                    process.setTypeDroit(decisionsmasseViewBean.getInTypeDroit());
                    process.setEtatFilter(decisionsmasseViewBean.getEtatFilter());
                    process.setDateValiditeGREAT(decisionsmasseViewBean.getDateValiditeGREAT());
                    process.setDateValiditeLESS(decisionsmasseViewBean.getDateValiditeLESS());
                    process.setDateFinValiditeGREAT(decisionsmasseViewBean.getDateFinValiditeGREAT());
                    process.setDateFinValiditeLESS(decisionsmasseViewBean.getDateFinValiditeLESS());

                    BProcessLauncher.start(process, false);

                } catch (Exception e) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(e.getMessage());
                }
            } else {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR).toString());
            }
            return viewBean;
        } else {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage("in custom action \"" + action.getActionPart() + "\" not implemented ");

            return super.execute(viewBean, action, session);
        }
    }
}
