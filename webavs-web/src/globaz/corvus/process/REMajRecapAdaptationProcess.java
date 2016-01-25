package globaz.corvus.process;

import globaz.corvus.api.adaptation.IREAdaptationRente;
import globaz.corvus.api.recap.IRERecapMensuelle;
import globaz.corvus.db.adaptation.REPmtFictif;
import globaz.corvus.db.adaptation.REPmtFictifManager;
import globaz.corvus.db.recap.access.RERecapElement;
import globaz.corvus.db.recap.access.RERecapElementManager;
import globaz.corvus.db.recap.access.RERecapInfo;
import globaz.corvus.db.recap.access.RERecapInfoManager;
import globaz.corvus.db.recap.access.RERecapMensuelle;
import globaz.corvus.db.recap.access.RERecapMensuelleManager;
import globaz.corvus.helpers.recap.REChargerRecapMensuelleHelper;
import globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;

/**
 * 
 * @author HPE
 * 
 */
public class REMajRecapAdaptationProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String moisAnnee = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public REMajRecapAdaptationProcess() {
        super();
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        BITransaction transaction = null;
        try {
            transaction = ((BSession) getISession()).newTransaction();
            transaction.openTransaction();

            JACalendar cal = new JACalendarGregorian();

            // Mise à jour de la récap de l'adaptation
            REPmtFictifManager pmtFicMgr = new REPmtFictifManager();
            pmtFicMgr = new REPmtFictifManager();
            pmtFicMgr.setSession(getSession());
            pmtFicMgr.setForCsTypeDonnee(IREAdaptationRente.CS_TYPE_RECAP_AUGMENTATION);
            pmtFicMgr.setForMoisAnnee(getMoisAnnee());
            pmtFicMgr.find();

            if (pmtFicMgr.isEmpty()) {
                throw new Exception(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_ERR_3") + " " + getMoisAnnee());
            }

            REPmtFictif pmtFicRecapAda = (REPmtFictif) pmtFicMgr.getFirstEntity();

            pmtFicMgr = new REPmtFictifManager();
            pmtFicMgr = new REPmtFictifManager();
            pmtFicMgr.setSession(getSession());
            pmtFicMgr.setForCsTypeDonnee(IREAdaptationRente.CS_TYPE_PAIEMENT_FICTIF);
            pmtFicMgr.setForMoisAnnee(getMoisAnnee());
            pmtFicMgr.find();

            if (pmtFicMgr.isEmpty()) {
                throw new Exception(getSession().getLabel("PROCESS_LISTE_RECAP_ADA_ERR_3") + " " + getMoisAnnee());
            }

            RERecapMensuelle recapDeJanvier = new RERecapMensuelle();
            recapDeJanvier.setSession(getSession());
            recapDeJanvier.setAlternateKey(RERecapMensuelle.DATE_RAPPORT_KEY);
            recapDeJanvier.setDateRapportMensuel(getMoisAnnee());

            recapDeJanvier.retrieve();
            if (recapDeJanvier.isNew()) {
                // TODO traduire
                throw new Exception("Impossible de récupérer la récap de janvier");
            }
            if (JadeStringUtil.isBlankOrZero(recapDeJanvier.getIdRecapMensuelle())) {
                throw new Exception("Impossible de récupérer l'id de la récap de janvier");
            }

            REDetailRecapMensuelleViewBean recapDeJanvierVB = new REDetailRecapMensuelleViewBean();
            recapDeJanvierVB.setIdRecapMensuelle(recapDeJanvier.getIdRecapMensuelle());
            recapDeJanvierVB.setSession(getSession());
            recapDeJanvierVB.retrieve();

            REPmtFictif pmtFicJanvier = (REPmtFictif) pmtFicMgr.getFirstEntity();

            // La différence entre la récap après augm. et le pmt fictif de
            // janvier
            FWCurrency ir500003 = new FWCurrency(pmtFicRecapAda.getMontantAVSOrdinaires());

            // On va récupérer le montant de l'augmentation des AVS_RO de janvier et on l'additionne
            FWCurrency ir500002 = new FWCurrency(recapDeJanvierVB.getElem500002().getMontant());
            ir500003.add(ir500002);
            ir500003.sub(pmtFicJanvier.getMontantAVSOrdinaires());

            FWCurrency ir501003 = new FWCurrency(pmtFicRecapAda.getMontantAVSExtraordinaires());
            FWCurrency ir501002 = new FWCurrency(recapDeJanvierVB.getElem501002().getMontant());
            ir501003.add(ir501002);
            ir501003.sub(pmtFicJanvier.getMontantAVSExtraordinaires());

            FWCurrency ir503003 = new FWCurrency(pmtFicRecapAda.getMontantAPIAVS());
            FWCurrency ir503002 = new FWCurrency(recapDeJanvierVB.getElem503002().getMontant());
            ir503003.add(ir503002);
            ir503003.sub(pmtFicJanvier.getMontantAPIAVS());

            FWCurrency ir510003 = new FWCurrency(pmtFicRecapAda.getMontantAIOrdinaires());
            FWCurrency ir510002 = new FWCurrency(recapDeJanvierVB.getElem510002().getMontant());
            ir510003.add(ir510002);
            ir510003.sub(pmtFicJanvier.getMontantAIOrdinaires());

            FWCurrency ir511003 = new FWCurrency(pmtFicRecapAda.getMontantAIExtraordinaires());
            FWCurrency ir511002 = new FWCurrency(recapDeJanvierVB.getElem511002().getMontant());
            ir511003.add(ir511002);
            ir511003.sub(pmtFicJanvier.getMontantAIExtraordinaires());

            FWCurrency ir513003 = new FWCurrency(pmtFicRecapAda.getMontantAPIAI());
            FWCurrency ir513002 = new FWCurrency(recapDeJanvierVB.getElem513002().getMontant());
            ir513003.add(ir513002);
            ir513003.sub(pmtFicJanvier.getMontantAPIAI());

            // Si récap existante, supprimer les anciens montants qu'on va
            // modifier
            RERecapInfoManager mgr = new RERecapInfoManager();
            mgr.setForDatePmt(getMoisAnnee());
            mgr.setSession(getSession());
            mgr.setForIdTiers("1");
            mgr.setForCodeRecapIn("500003,501003,503003,510003,511003,513003");
            mgr.find();

            for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
                RERecapInfo ir = (RERecapInfo) iterator.next();
                ir.delete(transaction);
            }

            RERecapInfo ir = new RERecapInfo();
            ir.setSession(getSession());
            ir.setCodeRecap("500003");
            ir.setDatePmt(getMoisAnnee());
            ir.setIdTiers("1");
            ir.setMontant(ir500003.toString());
            ir.setRestoreTag("");
            ir.add(transaction);

            getMemoryLog().logMessage(
                    getSession().getLabel("ERREUR_ECRITURE_RECAP") + ir.getCodeRecap()
                            + getSession().getLabel("MONTANT_ARE") + ir.getMontant(), FWMessage.INFORMATION,
                    this.getClass().getName());

            ir = new RERecapInfo();
            ir.setSession(getSession());
            ir.setCodeRecap("501003");
            ir.setDatePmt(getMoisAnnee());
            ir.setIdTiers("1");
            ir.setMontant(ir501003.toString());
            ir.setRestoreTag("");
            ir.add(transaction);

            getMemoryLog().logMessage(
                    getSession().getLabel("ERREUR_ECRITURE_RECAP") + ir.getCodeRecap()
                            + getSession().getLabel("MONTANT_ARE") + ir.getMontant(), FWMessage.INFORMATION,
                    this.getClass().getName());

            ir = new RERecapInfo();
            ir.setSession(getSession());
            ir.setCodeRecap("503003");
            ir.setDatePmt(getMoisAnnee());
            ir.setIdTiers("1");
            ir.setMontant(ir503003.toString());
            ir.setRestoreTag("");
            ir.add(transaction);

            getMemoryLog().logMessage(
                    getSession().getLabel("ERREUR_ECRITURE_RECAP") + ir.getCodeRecap()
                            + getSession().getLabel("MONTANT_ARE") + ir.getMontant(), FWMessage.INFORMATION,
                    this.getClass().getName());

            ir = new RERecapInfo();
            ir.setSession(getSession());
            ir.setCodeRecap("510003");
            ir.setDatePmt(getMoisAnnee());
            ir.setIdTiers("1");
            ir.setMontant(ir510003.toString());
            ir.setRestoreTag("");
            ir.add(transaction);

            getMemoryLog().logMessage(
                    getSession().getLabel("ERREUR_ECRITURE_RECAP") + ir.getCodeRecap()
                            + getSession().getLabel("MONTANT_ARE") + ir.getMontant(), FWMessage.INFORMATION,
                    this.getClass().getName());

            ir = new RERecapInfo();
            ir.setSession(getSession());
            ir.setCodeRecap("511003");
            ir.setDatePmt(getMoisAnnee());
            ir.setIdTiers("1");
            ir.setMontant(ir511003.toString());
            ir.setRestoreTag("");
            ir.add(transaction);

            getMemoryLog().logMessage(
                    getSession().getLabel("ERREUR_ECRITURE_RECAP") + ir.getCodeRecap()
                            + getSession().getLabel("MONTANT_ARE") + ir.getMontant(), FWMessage.INFORMATION,
                    this.getClass().getName());

            ir = new RERecapInfo();
            ir.setSession(getSession());
            ir.setCodeRecap("513003");
            ir.setDatePmt(getMoisAnnee());
            ir.setIdTiers("1");
            ir.setMontant(ir513003.toString());
            ir.setRestoreTag("");
            ir.add(transaction);

            // Essayer de charger la récap
            String vdate = getMoisAnnee(); // format MMxAAAA
            String existRecapMensuelleEtatsNonEnvoye = idRecapMensuelle(vdate, getSession());

            if (JadeStringUtil.isBlankOrZero(existRecapMensuelleEtatsNonEnvoye)) {

                RELoadRecapMensuelleProcess process = new RELoadRecapMensuelleProcess();
                process.setSession(getSession());
                process.setTransaction(transaction);
                process.setDateRapportMensuel(vdate);

                process.executeProcess();
                if (process.isOnError() || process.getMemoryLog().size() > 0) {
                    throw new Exception(process.getMemoryLog().getMessagesInString());
                }

            } else {

                if (REChargerRecapMensuelleHelper.RECAP_ENVOYE.equals(existRecapMensuelleEtatsNonEnvoye)) {
                    throw new Exception(getSession().getLabel("RECAP_ENVOYE"));
                } else {
                    // Suppression des anciennes Recap.
                    RERecapMensuelleManager recapMensMgr = new RERecapMensuelleManager();
                    recapMensMgr.setSession(getSession());
                    recapMensMgr.setForIdRecapMensuelle(existRecapMensuelleEtatsNonEnvoye);
                    recapMensMgr.find(transaction);

                    Iterator iterRecapMens = recapMensMgr.iterator();

                    while (iterRecapMens.hasNext()) {
                        RERecapMensuelle recapMens = (RERecapMensuelle) iterRecapMens.next();
                        recapMens.delete(transaction);
                    }

                    RERecapElementManager recapElementMgr = new RERecapElementManager();
                    recapElementMgr.setSession(getSession());
                    recapElementMgr.setForIdRecapMensuelle(existRecapMensuelleEtatsNonEnvoye);
                    recapElementMgr.find(transaction);

                    Iterator iterRecapElem = recapElementMgr.iterator();

                    while (iterRecapElem.hasNext()) {
                        RERecapElement recapElem = (RERecapElement) iterRecapElem.next();
                        recapElem.delete(transaction);
                    }

                    // Chargement des nouvelles recap. depuis REINFREC
                    RELoadRecapMensuelleProcess process = new RELoadRecapMensuelleProcess();
                    process.setSession(getSession());
                    process.setDateRapportMensuel(vdate);
                    process.setTransaction(transaction);
                    process.executeProcess();

                    if (process.isOnError() || process.getMemoryLog().size() > 0) {
                        throw new Exception(process.getMemoryLog().getMessagesInString());
                    }
                }

            }

            getMemoryLog().logMessage(
                    getSession().getLabel("ERREUR_ECRITURE_RECAP") + ir.getCodeRecap()
                            + getSession().getLabel("MONTANT_ARE") + ir.getMontant(), FWMessage.INFORMATION,
                    this.getClass().getName());

            getMemoryLog().logMessage("Mise à jour de la récapitulation effectuée avec succès.", FWMessage.INFORMATION,
                    "REMajRecapAdaptationProcess");

            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
                getAttachedDocuments().clear();
                getMemoryLog().logMessage("Erreur dans le traitement : " + e.toString(), FWMessage.ERREUR,
                        "REMajRecapAdaptationProcess");
            }
            return false;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_GENERAL_ADAPTATION_TITRE_MAIL") + " - "
                + getSession().getLabel("PROCESS_MAJ_RECAP_ADA_OBJET_MAIL");
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    /**
     * Recherche l'existance d'un enregistrement de récap mensuel pour le mois donne //dans l'état ENVOYE//
     * 
     * @param vdate
     * @param session
     * @return IdRecap si en attente, RECAP_ENVOYE si envoyé, "" si n'a jamais été chargé
     */
    private String idRecapMensuelle(String vdate, BSession session) {

        RERecapMensuelle recap = new RERecapMensuelle();
        recap.setSession(session);
        recap.setAlternateKey(RERecapMensuelle.DATE_RAPPORT_KEY);
        recap.setDateRapportMensuel(vdate);
        try {
            recap.retrieve();
            if (recap.isNew()) {
                return "";
            } else {
                if (IRERecapMensuelle.CS_ETAT_ATTENTE.equals(recap.getCsEtat())) {
                    return recap.getIdRecapMensuelle();
                } else {
                    return REChargerRecapMensuelleHelper.RECAP_ENVOYE;
                }
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

}
