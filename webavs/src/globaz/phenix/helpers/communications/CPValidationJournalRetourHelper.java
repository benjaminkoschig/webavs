package globaz.phenix.helpers.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPValidationCalculCommunication;
import globaz.phenix.db.communications.CPValidationJournalRetourViewBean;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.phenix.process.CPProcessCalculCotisation;
import globaz.phenix.process.communications.CPProcessAbandonSelectionJournalRetourViewBean;
import globaz.phenix.process.communications.CPProcessDevalidationSelectionJournalRetourViewBean;
import globaz.phenix.process.communications.CPProcessValidationSelectionJournalRetourViewBean;

public class CPValidationJournalRetourHelper extends FWHelper {

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        if (viewBean instanceof CPDecisionViewBean) {
            ((CPDecisionViewBean) viewBean).update();

            CPProcessCalculCotisation calcul = new CPProcessCalculCotisation();
            calcul.setIdDecision(((CPDecisionViewBean) viewBean).getIdDecision());
            calcul.setISession(((CPDecisionViewBean) viewBean).getSession());
            calcul.setSendMailOnError(true);
            calcul.setSendCompletionMail(false);
            calcul.executeProcess();
        }
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if ("modifierDecision".equals(action.getActionPart()) && viewBean instanceof CPDecisionViewBean) {
            CPDecisionViewBean vb = (CPDecisionViewBean) viewBean;

            CPValidationJournalRetourViewBean validation = new CPValidationJournalRetourViewBean();
            validation.setSession(vb.getSession());
            validation.setIdValidation(vb.getIdValidation());
            try {
                validation.retrieve();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            String idDecision = validation.getIdDecision();
            String idTiers = validation.getIdTiers();

            vb.setIdDecision(idDecision);
            vb.setIdTiers(idTiers);
            vb.setIdValidation("");
            try {
                vb.retrieve();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            viewBean = vb;

        }

        if ("devalider".equals(action.getActionPart()) && viewBean instanceof CPValidationJournalRetourViewBean) {
            CPValidationJournalRetourViewBean vb = (CPValidationJournalRetourViewBean) viewBean;

            CPProcessDevalidationSelectionJournalRetourViewBean devalidation = new CPProcessDevalidationSelectionJournalRetourViewBean();
            devalidation.setSession(vb.getSession());
            devalidation.setForIdValidationCommunication(vb.getIdValidation());
            devalidation.start();

            if (devalidation.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(devalidation.getMsgType());
                viewBean.setMessage(devalidation.getMessage());
            }

        }

        if ("processValiderSelection".equals(action.getActionPart())
                && viewBean instanceof CPValidationJournalRetourViewBean) {
            CPValidationJournalRetourViewBean vb = (CPValidationJournalRetourViewBean) viewBean;

            CPProcessValidationSelectionJournalRetourViewBean process = new CPProcessValidationSelectionJournalRetourViewBean();
            process.setSession(vb.getSession());
            process.setForAnnee(vb.getForAnnee());
            process.setLikeNumAffilie(vb.getLikeNumAffilie());
            process.setForGrpTaxation(vb.getForGrpTaxation());
            process.setForGrpExtraction(vb.getForGrpExtraction());
            process.setForGenreAffilie(vb.getGenreAffilie());
            process.setForJournal(vb.getForJournal());
            process.setForTypeDecision(vb.getTypeDecision());
            process.setEMailAddress(vb.getEMailAdress());
            process.setIdJournalFacturation(vb.getIdJournal());
            process.setSendCompletionMail(true);
            process.setSendMailOnError(true);
            process.start();

            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(viewBean.getMsgType());
                viewBean.setMessage(viewBean.getMessage());
            }
        }

        if ("processDevaliderSelection".equals(action.getActionPart())
                && viewBean instanceof CPValidationJournalRetourViewBean) {
            CPValidationJournalRetourViewBean vb = (CPValidationJournalRetourViewBean) viewBean;

            CPProcessDevalidationSelectionJournalRetourViewBean process = new CPProcessDevalidationSelectionJournalRetourViewBean();
            process.setSession(vb.getSession());
            process.setForAnnee(vb.getForAnnee());
            process.setLikeNumAffilie(vb.getLikeNumAffilie());
            process.setForGrpTaxation(vb.getForGrpTaxation());
            process.setForGrpExtraction(vb.getForGrpExtraction());
            process.setForGenreAffilie(vb.getGenreAffilie());
            process.setForJournal(vb.getForJournal());
            process.setForTypeDecision(vb.getTypeDecision());
            process.setEMailAddress(vb.getEMailAdress());
            process.setIdJournalFacturation(vb.getIdJournal());
            process.setSendCompletionMail(true);
            process.setSendMailOnError(true);
            process.start();
            // viewBean.executeProcess();
            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }

        if ("executerSuppression".equals(action.getActionPart())
                && viewBean instanceof CPValidationJournalRetourViewBean) {
            CPValidationJournalRetourViewBean vb = (CPValidationJournalRetourViewBean) viewBean;

            if (!JadeStringUtil.isBlank(vb.getIdValidation())) {
                try {
                    vb.retrieve();
                    CPCommunicationFiscaleRetourViewBean communication = new CPCommunicationFiscaleRetourViewBean();
                    communication.setSession(vb.getSession());
                    communication.setIdRetour(vb.getIdRetour());
                    communication.retrieve();
                    communication.delete();
                } catch (Exception e) {
                    viewBean.setMessage(e.toString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }
            }
        }

        if ("valider".equals(action.getActionPart()) && viewBean instanceof CPValidationJournalRetourViewBean) {
            try {
                ((CPValidationJournalRetourViewBean) viewBean).retrieve();

                CPValidationCalculCommunication validation = new CPValidationCalculCommunication();
                validation.setSession(((CPValidationJournalRetourViewBean) viewBean).getSession());
                validation.setIdValidationCommunication(((CPValidationJournalRetourViewBean) viewBean)
                        .getIdValidation());
                validation.retrieve();

                ((CPValidationJournalRetourViewBean) viewBean).setIdDecision(validation.getIdDecision());
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        if ("abandonner".equals(action.getActionPart()) && viewBean instanceof CPValidationJournalRetourViewBean) {
            CPValidationJournalRetourViewBean vb = (CPValidationJournalRetourViewBean) viewBean;

            CPProcessAbandonSelectionJournalRetourViewBean abandon = new CPProcessAbandonSelectionJournalRetourViewBean();
            abandon.setSession(vb.getSession());
            abandon.setForIdValidationCommunication(vb.getIdValidation());
            abandon.setSendMailOnError(true);
            abandon.setSendCompletionMail(false);
            abandon.start();

            if (abandon.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(abandon.getMsgType());
                viewBean.setMessage(abandon.getMessage());
            }
        }

        if ("processAbandonnerSelection".equals(action.getActionPart())
                && viewBean instanceof CPValidationJournalRetourViewBean) {
            CPValidationJournalRetourViewBean vb = (CPValidationJournalRetourViewBean) viewBean;

            CPProcessAbandonSelectionJournalRetourViewBean process = new CPProcessAbandonSelectionJournalRetourViewBean();
            process.setSession(vb.getSession());
            process.setForAnnee(vb.getForAnnee());
            process.setLikeNumAffilie(vb.getLikeNumAffilie());
            process.setForGrpTaxation(vb.getForGrpTaxation());
            process.setForGenreAffilie(vb.getGenreAffilie());
            process.setForGrpExtraction(vb.getForGrpExtraction());
            process.setForTypeDecision(vb.getTypeDecision());
            process.setForJournal(vb.getForJournal());
            process.setEMailAddress(vb.getEMailAdress());
            process.setIdJournalFacturation(vb.getIdJournal());
            process.setSendCompletionMail(true);
            process.setSendMailOnError(true);
            process.start();
            // viewBean.executeProcess();
            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }
        return super.execute(viewBean, action, session);
    }
}
