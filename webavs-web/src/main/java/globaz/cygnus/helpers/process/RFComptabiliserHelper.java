/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.helpers.process;

import globaz.cygnus.process.RFComptabiliserProcess;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.process.RFComptabiliserViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFComptabiliserHelper extends PRAbstractHelper {

    private List<String> listeMessagesErreurs = new ArrayList<String>();

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

        RFComptabiliserViewBean vb = (RFComptabiliserViewBean) viewBean;
        try {
            // impression d'une décision ou validation des décisions
            // if (IRFDecisions.TYPE_VALIDATION_DECISION_NORMAL.equals(vb.getTypeValidation())) {

            RFComptabiliserProcess process = new RFComptabiliserProcess();

            if (!hasErreur(vb, (BSession) session)) {
                initialiserProcessValidationDecision(process, vb, (BSession) session);
                BProcessLauncher.start(process, false);
            } else {
                checkMessagesErreurs(vb);
            }

            // }
            // NE PAS DECOMMENTER
            /*
             * else if (IRFDecisions.TYPE_VALIDATION_DECISION_SIMULATION.equals(vb.getTypeValidation())) {
             * 
             * RFSimulerValidationDecisionsProcess process = new RFSimulerValidationDecisionsProcess( (BSession)
             * session); process.setEMailAddress(vb.getEMailAddress()); process.setSendMailOnError(true);
             * process.setIdGestionnaire(vb.getIdGestionnaire()); // indiquer qu'on ne vient pas de la validation mais
             * du lot pour comptabilisation process.setIsPourValidation(Boolean.FALSE); process.start(); }
             */
        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Methode qui parcours les messages présent dans la liste pour les remonter dans un popup
     * 
     * @param vb
     */
    private void checkMessagesErreurs(RFComptabiliserViewBean vb) {
        for (String message : listeMessagesErreurs) {
            RFUtils.setMsgErreurViewBean(vb, message);
        }
    }

    /**
     * Methode qui remplie la liste des messages d'erreurs pour les paramètres concernés
     * 
     * @param vb
     * @return boolean
     */
    private Boolean hasErreur(RFComptabiliserViewBean vb, BSession session) {

        if (JadeStringUtil.isEmpty(vb.getDateEcheancePaiement())
                || !JadeDateUtil.isGlobazDate(vb.getDateEcheancePaiement())
                || isDatePassee(vb.getDateEcheancePaiement())) {
            listeMessagesErreurs.add("ERREUR_RF_VALIDER_DECISION_DATE_ECHEANCE");
        }

        if (JadeStringUtil.isEmpty(vb.getIdOrganeExecution())) {
            listeMessagesErreurs.add("ERREUR_RF_VALIDER_DECISION_ORGANE_EXECUTION");
        }
        if (isIso20022(vb.getIdOrganeExecution(), session)) {
            if (JadeStringUtil.isEmpty(vb.getIsoGestionnaire())) {
                listeMessagesErreurs.add("ERREUR_RF_VALIDER_DECISION_ISO_GESTIONNAIRE");
            }
        } else {
            if (!JadeNumericUtil.isInteger(vb.getNumeroOG()) || (JadeStringUtil.toLong(vb.getNumeroOG()) >= 100)) {
                listeMessagesErreurs.add("ERREUR_RF_VALIDER_DECISION_NUMERO_OG");
            }
        }

        return !listeMessagesErreurs.isEmpty();
    }

    private boolean isIso20022(String idOrganeExecutionm, BSession session) {
        CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
        mgr.setSession(session);
        mgr.setForIdOrganeExecution(idOrganeExecutionm);
        try {
            mgr.find();
            if (mgr.size() != 1) {
                throw new Exception();
            }
        } catch (Exception e) {
            listeMessagesErreurs.add("ERREUR_RF_VALIDER_DECISION_ORGANE_EXECUTION_UNKNOW");
        }

        return ((CAOrganeExecution) mgr.getEntity(0)).getIdTypeTraitementOG().equals(APIOrganeExecution.OG_ISO_20022);
    }

    /**
     * Methode qui initialise le process, des paramètres reçus du viewBean
     * 
     * @param process
     * @param vb
     * @param session
     * @throws Exception
     */
    private void initialiserProcessValidationDecision(RFComptabiliserProcess process, RFComptabiliserViewBean vb,
            BSession session) throws Exception {

        process.setDateEcheancePaiement(vb.getDateEcheancePaiement());
        process.setDescriptionLot(vb.getDescriptionLot());
        process.setIdOrganeExecution(vb.getIdOrganeExecution());
        process.setNumeroOG(vb.getNumeroOG());
        process.setSession(session);
        process.setSendCompletionMail(true);
        process.setSendMailOnError(true);
        process.setDateComptable(vb.getDateComptable());
        process.setEMailAddress(vb.getEMailAddress());
        process.setIdLot(vb.getIdLot());
        process.setIsoGestionnaire(vb.getIsoGestionnaire());
        process.setIsoHighPriority(vb.getIsoHighPriority());
    }

    /**
     * 
     * @param dateEcheance
     * @return
     */
    private Boolean isDatePassee(String dateEcheance) {
        return JadeDateUtil.isDateMonthYearAfter(JACalendar.todayJJsMMsAAAA().substring(3), dateEcheance.substring(3));
    }

    /**
     * Methode qui controle si l'un des paramètres est en erreur
     * 
     * @param vb
     * @return boolean
     */
    private Boolean paramsIsErreur(RFComptabiliserViewBean vb) {
        return (JadeStringUtil.isEmpty(vb.getDateEcheancePaiement())
                || !JadeDateUtil.isGlobazDate(vb.getDateEcheancePaiement())
                || isDatePassee(vb.getDateEcheancePaiement()) || JadeStringUtil.isEmpty(vb.getIdOrganeExecution())
                || !JadeNumericUtil.isInteger(vb.getNumeroOG()) || (JadeStringUtil.toLong(vb.getNumeroOG()) >= 100));
    }
}
