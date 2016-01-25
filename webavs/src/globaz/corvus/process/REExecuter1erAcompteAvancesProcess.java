package globaz.corvus.process;

import globaz.corvus.api.avances.IREAvances;
import globaz.corvus.db.avances.REAvance;
import globaz.corvus.db.avances.REAvanceManager;
import globaz.corvus.module.compta.avance.REModuleComptablePmtAvance;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JATime;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import globaz.prestation.tools.PRStringUtils;

/**
 * 
 * @author SCR
 *         modification INFOROM 547
 * 
 */

public class REExecuter1erAcompteAvancesProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String csDomaineApplicatif = null;
    private String dateEcheance = null;
    String emailObject = "";
    private String idOrganeExecution = null;

    private String noOg = null;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        BITransaction transaction = getTransaction();
        boolean succes = true;

        try {
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            BSession sessionOsiris = (BSession) PRSession.connectSession(getSession(),
                    CAApplication.DEFAULT_APPLICATION_OSIRIS);

            // Recherche des avances du domaine rente non terminé et antérieur ou egale a aujourd'hui, non terminé et
            // non annulé
            REAvanceManager mgr = new REAvanceManager();
            mgr.setSession(getSession());
            // mgr.setForCsEtat1erAcomptesDifferentDe(IREAvances.CS_ETAT_1ER_ACOMPTE_TERMINE);
            String inCsEtatDifferentDe = IREAvances.CS_ETAT_1ER_ACOMPTE_TERMINE + ","
                    + IREAvances.CS_ETAT_1ER_ACOMPTE_ANNULE;
            mgr.setForCsEtat1erAcomptesDifferentDeIn(inCsEtatDifferentDe);
            mgr.setUntilDateDebut1erAcompte(JACalendar.todayJJsMMsAAAA());
            mgr.setForCsDomaineAvance(csDomaineApplicatif);
            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            if (mgr.isEmpty()) {
                getMemoryLog().logMessage((getSession()).getLabel("PMT_AVANCE_EMPTY"), FWMessage.INFORMATION,
                        this.getClass().getName());
                return true;
            }

            JACalendar cal = new JACalendarGregorian();
            APIGestionComptabiliteExterne compta = initCompta(this, sessionOsiris, (BTransaction) transaction, cal);
            String dateValeurComptable = getDateEcheance();

            // iteration sur les résultats
            for (int i = 0; i < mgr.size(); i++) {
                REAvance avance = (REAvance) mgr.get(i);
                avance.retrieve(transaction);
                avance.setCsEtat1erAcompte(IREAvances.CS_ETAT_1ER_ACOMPTE_TERMINE);
                avance.setDatePmt1erAcompte(JACalendar.todayJJsMMsAAAA());
                avance.wantCallValidate(false);
                avance.update(transaction);

                PRTiersWrapper tw = PRTiersHelper.getTiersParId(getSession(), avance.getIdTiersBeneficiaire());

                // Création de l'ov !!!
                // this.getMemoryLog().logMessage(
                // REModuleComptablePmtAvance.getInstance(sessionOsiris).payerAvance(
                // this,
                // this.getSession(),
                // (BTransaction) transaction,
                // compta,
                // avance.getIdTiersBeneficiaire(),
                // avance.getIdTiersAdrPmt(),
                // avance.getCsDomaine(),
                // new FWCurrency(avance.getMontant1erAcompte()),
                // tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " "
                // + this.getSession().getLabel("AVANCE_RENTE"), dateValeurComptable,
                // this.csDomaineApplicatif));

                // Constrcution desc OV
                String ovDesc = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " "
                        + getOvDescriptionForDomaine(avance.getCsDomaineAvance());

                getMemoryLog().logMessage(
                        REModuleComptablePmtAvance.getInstance(sessionOsiris).payerAvance(this, getSession(),
                                (BTransaction) transaction, compta, avance.getIdTiersBeneficiaire(),
                                avance.getIdTiersAdrPmt(), avance.getCsDomaine(),
                                new FWCurrency(avance.getMontant1erAcompte()), ovDesc, dateValeurComptable,
                                avance.getCsDomaineAvance()));
            }

            transaction.commit();
            if (!transaction.hasErrors()) {
                compta.comptabiliser();

                // Description ordre groupé
                String descOg = getOgDescriptionForDomaine(csDomaineApplicatif) + " - OG " + noOg;

                doPreparerOG(getIdOrganeExecution(), getNoOg(), getDateEcheance(), csDomaineApplicatif, compta, descOg);
            }

            return true;
        } catch (Exception e) {
            succes = false;
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            if (transaction.hasErrors()) {
                getMemoryLog().logMessage(transaction.getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }
            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(getSession().getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }
            try {
                transaction.rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            }
            return false;
        } finally {
            try {
                if (succes) {
                    emailObject = getObjectMailMessageForDomaine(csDomaineApplicatif,
                            getSession().getLabel("PMT_AVANCES_RENTE_1ER_ACPT_OK"));
                } else {
                    emailObject = getObjectMailMessageForDomaine(csDomaineApplicatif,
                            getSession().getLabel("PMT_AVANCES_RENTE_1ER_ACPT_KO"));
                }
            } finally {
                try {
                    if (transaction != null) {
                        transaction.closeTransaction();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
                setSendMailOnError(false);
            } else {
                setSendCompletionMail(true);
                setSendMailOnError(true);
            }

            setControleTransaction(getTransaction() == null);
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    // Préparation de l'OG...
    private void doPreparerOG(String idOG, String numeroOG, String dateEcheancePaiement, String csDomaineApplicatif,
            APIGestionComptabiliteExterne compta, String desc) throws Exception {

        if (compta != null) {
            getMemoryLog().logMessage("Préparation de l'OG : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");
            int n = Integer.parseInt(numeroOG);

            compta.preparerOrdreGroupe(idOG, String.valueOf(n), dateEcheancePaiement, CAOrdreGroupe.VERSEMENT,
                    CAOrdreGroupe.NATURE_RENTES_AVS_AI, desc);
        }
    }

    public String getCsDomaineApplicatif() {
        return csDomaineApplicatif;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    /**
     * Ex.
     * 
     * today = 10.08.2007 date dernier pmt = 08.2007 return 10.08.2007
     * 
     * 
     * today = 31.08.2007 date dernier pmt = 09.2007 return 01.09.2007
     * 
     * 
     * @param session
     * @param cal
     * @return Date valeur comptable au format jj.mm.aaaa
     * @throws JAException
     */
    private String getDateValeurComptable(BSession session, JACalendar cal) throws JAException {

        JADate todayMMxAAAA = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(JACalendar.todayJJsMMsAAAA()));
        JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt(session));

        if (cal.compare(todayMMxAAAA, dateDernierPmt) == JACalendar.COMPARE_FIRSTLOWER) {
            return "01." + PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateDernierPmt.toStrAMJ());
        } else {
            return JACalendar.todayJJsMMsAAAA();
        }

    }

    public String getEmailObject() {
        return emailObject;
    }

    @Override
    protected String getEMailObject() {
        return emailObject;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getNoOg() {
        return noOg;
    }

    /**
     * Retourne l'objet du message en fonction du domaine applicatif
     * 
     * @param csDomaineApplicatif2
     * @return
     */
    private String getObjectMailMessageForDomaine(String csDomaineApplicatifAvance, String mainLabel) {

        String domaineApplicatif = null;

        // si le domaine est différent de null, sinon on met la description rente par defaut
        if (csDomaineApplicatifAvance != null) {
            if (csDomaineApplicatifAvance.equals(IREAvances.CS_DOMAINE_AVANCE_PC)) {
                domaineApplicatif = getSession().getLabel("AVANCE_PC");
            } else if (csDomaineApplicatifAvance.equals(IREAvances.CS_DOMAINE_AVANCE_RENTE)) {
                domaineApplicatif = getSession().getLabel("AVANCE_RENTE");
            } else {
                domaineApplicatif = getSession().getLabel("AVANCE_RFM");
            }
        } else {
            domaineApplicatif = getSession().getLabel("AVANCE_RENTE");
        }

        String message = PRStringUtils.replaceString(mainLabel, "{0}", domaineApplicatif);
        return message;

    }

    /**
     * Retourne le libelle de description de l'OV
     * Si le domaine null, ou pas trouvé, on retourne le libellé du domaine rente
     * 
     * @param csDomaineApplicatifAvance
     * @return
     */
    private String getOgDescriptionForDomaine(String csDomaineApplicatifAvance) {

        String suffixe = " - " + getSession().getLabel("PMT_UNIQUE_OG_LIBELLE");

        // si le domaine est différent de null, sinon on met la description rente par defaut
        if (csDomaineApplicatifAvance != null) {
            if (csDomaineApplicatifAvance.equals(IREAvances.CS_DOMAINE_AVANCE_PC)) {
                return getSession().getLabel("AVANCE_PC") + suffixe;
            } else if (csDomaineApplicatifAvance.equals(IREAvances.CS_DOMAINE_AVANCE_RENTE)) {
                return getSession().getLabel("AVANCE_RENTE") + suffixe;
            } else {
                return getSession().getLabel("AVANCE_RFM") + suffixe;
            }
        } else {
            return getSession().getLabel("AVANCE_RENTE") + suffixe;
        }

    }

    /**
     * Retourne le libelle de description de l'OV
     * Si le domaine null, ou pas trouvé, on retourne le libellé du domaine rente
     * 
     * @param csDomaineApplicatifAvance
     * @return
     */
    private String getOvDescriptionForDomaine(String csDomaineApplicatifAvance) {

        // si le domaine est différent de null, sinon on met la description rente par defaut
        if (csDomaineApplicatifAvance != null) {
            if (csDomaineApplicatifAvance.equals(IREAvances.CS_DOMAINE_AVANCE_PC)) {
                return getSession().getLabel("AVANCE_PC");
            } else if (csDomaineApplicatifAvance.equals(IREAvances.CS_DOMAINE_AVANCE_RENTE)) {
                return getSession().getLabel("AVANCE_RENTE");
            } else {
                return getSession().getLabel("AVANCE_RFM");
            }
        } else {
            return getSession().getLabel("AVANCE_RENTE");
        }

    }

    private APIGestionComptabiliteExterne initCompta(BProcess process, BSession sessionOsiris,
            BTransaction transaction, JACalendar cal) throws Exception {
        APIGestionComptabiliteExterne compta = null;

        // instanciation du processus de compta
        compta = (APIGestionComptabiliteExterne) sessionOsiris.getAPIFor(APIGestionComptabiliteExterne.class);
        String dateValeurComptable = getDateValeurComptable(getSession(), cal);
        compta.setDateValeur(dateValeurComptable);
        compta.setEMailAddress(process.getEMailAddress());

        FWMemoryLog comptaMemoryLog = new FWMemoryLog();
        comptaMemoryLog.setSession(sessionOsiris);
        compta.setMessageLog(comptaMemoryLog);

        compta.setSendCompletionMail(false);
        compta.setTransaction(transaction);
        String libelle = getSession().getLabel("JOURNAL_PMT_AVANCES_RENTES_1ER_ACPT");
        if (libelle.length() > 40) {
            libelle = libelle.substring(0, 40);
        }
        compta.setLibelle(libelle);
        compta.setProcess(process);
        compta.createJournal();
        return compta;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setCsDomaineApplicatif(String csDomaineApplicatif) {
        this.csDomaineApplicatif = csDomaineApplicatif;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setEmailObject(String emailObject) {
        this.emailObject = emailObject;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setNoOg(String noOg) {
        this.noOg = noOg;
    }

}
