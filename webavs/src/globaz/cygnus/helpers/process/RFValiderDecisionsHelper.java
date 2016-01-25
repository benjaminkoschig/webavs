/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.helpers.process;

import globaz.corvus.utils.REPmtMensuel;
import globaz.cygnus.api.decisions.IRFDecisions;
import globaz.cygnus.process.RFValiderDecisionUniqueProcess;
import globaz.cygnus.process.RFValiderDecisionsNonValideesProcess;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.process.RFValiderDecisionsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 * @Revision FHA
 */
public class RFValiderDecisionsHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        RFValiderDecisionsViewBean vb = (RFValiderDecisionsViewBean) viewBean;
        try {
            if (validate(viewBean)) {
                // impression d'une décision ou validation des décisions
                if (JadeStringUtil.isEmpty(vb.getTypeValidation())
                        || IRFDecisions.TYPE_VALIDATION_DECISION_NORMAL.equals(vb.getTypeValidation())) {

                    if (JadeStringUtil.isEmpty(vb.getTypeValidation())) {

                        RFValiderDecisionUniqueProcess process = new RFValiderDecisionUniqueProcess();
                        initialiserProcessValiderDecisionUnique(process, vb, (BSession) session);
                        BProcessLauncher.start(process, false);

                    } else {

                        RFValiderDecisionsNonValideesProcess process = new RFValiderDecisionsNonValideesProcess();
                        initialiserProcessValiderDecisionsNonValidees(process, vb, Boolean.FALSE, (BSession) session);
                        BProcessLauncher.start(process, false);

                    }
                } else if (IRFDecisions.TYPE_VALIDATION_DECISION_SIMULATION.equals(vb.getTypeValidation())) {

                    // Appel du process pour générer les documents sans modifier leur état
                    RFValiderDecisionsNonValideesProcess processSimulationValidation = new RFValiderDecisionsNonValideesProcess();
                    initialiserProcessValiderDecisionsNonValidees(processSimulationValidation, vb, Boolean.TRUE,
                            (BSession) session);
                    BProcessLauncher.start(processSimulationValidation, false);
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            e.printStackTrace();
        }
    }

    private void initialiserProcessValiderDecisionsNonValidees(RFValiderDecisionsNonValideesProcess process,
            RFValiderDecisionsViewBean vb, Boolean isSimulation, BSession session) throws Exception {

        process.setSession(session);
        process.setEmailAdress(vb.getEMailAddress());
        process.setIsSimulationValidation(isSimulation);

        if (RFPropertiesUtils.utiliserGestionnaireViewBean()) {
            process.setIdGestionnaire(vb.getIdGestionnaire());
        } else {
            process.setIdGestionnaire(session.getUserId());
        }

        process.setDateSurDocument(vb.getDateSurDocument());
        process.setIsMiseEnGed(vb.getMiseEnGed());
    }

    private void initialiserProcessValiderDecisionUnique(RFValiderDecisionUniqueProcess process,
            RFValiderDecisionsViewBean vb, BSession session) throws Exception {

        process.setSession(session);
        if (RFPropertiesUtils.utiliserGestionnaireViewBean()) {
            process.setIdGestionnaire(vb.getIdGestionnaire());
        } else {
            process.setIdGestionnaire(session.getUserId());
        }

        process.setDateSurDocument(vb.getDateSurDocument());
        process.setIsMiseEnGed(vb.getMiseEnGed());

        process.setEmailAdresse(vb.getEMailAddress());
        process.setIdDecision(vb.getIdDecision());
        process.setNumeroDecision(vb.getNumeroDecision());

    }

    /**
     * Methode qui permet de controler que les champs obligatoires soient renseignés
     * 
     * @param viewBean
     * @return boolean
     * @throws Exception
     */
    private boolean validate(FWViewBeanInterface viewBean) throws Exception {

        boolean isValidation = true;

        // Adresse e-mail obligatoire
        if (JadeStringUtil.isBlankOrZero(((RFValiderDecisionsViewBean) viewBean).getEMailAddress())) {
            RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_ADRESSE_MAIL_OBLIGATOIRE");
            isValidation = false;
        }
        // Date sur document obligatoire
        if (JadeStringUtil.isBlankOrZero(((RFValiderDecisionsViewBean) viewBean).getDateSurDocument())) {
            RFUtils.setMsgErreurViewBean(viewBean, "JSP_ERREUR_CHAMP_DATE_SUR_DOCUMENT_OBLIGATOIRE");
            isValidation = false;
        }
        // Contrôle si la validation est bloquée depuis l'application des rentes.
        if (REPmtMensuel.isValidationDecisionAuthorise((BSession) viewBean.getISession()) == false) {
            RFUtils.setMsgErreurViewBean(viewBean, "JSP_RF_CHECK_VALIDATION_DECISION");
            isValidation = false;
        }

        return isValidation;
    }

}
