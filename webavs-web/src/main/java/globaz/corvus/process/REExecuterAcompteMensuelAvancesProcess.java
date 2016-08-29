package globaz.corvus.process;

import globaz.corvus.api.avances.IREAvances;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.avances.REAvance;
import globaz.corvus.db.avances.REAvanceManager;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
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
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import ch.globaz.common.util.prestations.MotifVersementUtil;

/**
 * 
 * @author SCR <br/>
 *         Modification INFOROM 547, 02.2013 -- SCE <br/>
 *         Processus de paiement des avances mensuelles, pour tous les domaines d'avances, à savoir: Rentes, PC, RFM
 * 
 * <br/>
 *         <b>Règles métier: </b> <br/>
 *         <b>- date de valeur comptable</b>: est la date de dernier paiement rente, ou date du jour si date du jour
 *         après date dernier paiement rente <br/>
 *         - le paiement mensuel des rentes doit avoir été effectué pour le mois comptable <br/>
 *         - un seul lot de type avance doit exister <br/>
 *         - pour être payé une avance ne doit pas être dans l'état terminé et avoir une date de fin postérieur (après)
 *         à la date de valeur comptable
 */

public class REExecuterAcompteMensuelAvancesProcess extends BProcess {

    private static final long serialVersionUID = 1L;

    /** date d'échéance pour l'ordre groupé */
    private String dateEcheancePaiement = null;
    /** objet du mail envoyé suite au process */
    private String emailObject = "";
    /** organe d'exécution pour l'ordre groupé */
    private String idOrganeExecution = null;
    /** numero d'ordre groupé */
    private String noOg = null;
    /** manager des lots */
    private RELotManager lotManager = null;
    /** session osiris */
    private BSession sessionOsiris = null;
    /** transaction */
    private BTransaction transaction = null;
    /** Date daernier paiement */
    private JADate dateDernierPaiementMensuel = null;
    /** date de paieemnt avance, défini avec valeur par defaut si pas delot existant --> mise en prod */
    private String dateDernierPaiementAvance = "01.01.2000";
    /** objet lot de type avance */
    private RELot lotAvance = null;
    /** manager des avances */
    private REAvanceManager manager = null;
    /** api compta */
    private APIGestionComptabiliteExterne compta = null;
    /** etat success ou error du process */
    private boolean processOnSuccess = true;
    /** Date de fin fictive pour les test sur les dates */
    private static final String DATE_FIN = "31.12.2999";
    // SEPA iso20002
    private String isoCsTypeAvis;
    private String isoGestionnaire;
    private String isoHightPriority;
    private Boolean isIso = null;

    /**
     * Validation des paramètres non null
     * nécessaires à la bonne création de l'objet
     * 
     */
    private void validateNotNullParameters() {

        if (getSession() == null) {
            getSession().addError("PMT_AVANCE_SESSION_NULL");
        }

        if (getEMailAddress() == null) {
            getSession().addError("PMT_AVANCE_EMAIL_NULL");
        }

        if (!isIso20022(idOrganeExecution, getSession())) {
            if (noOg == null) {
                getSession().addError("PMT_AVANCE_NOOG_NULL");
            }
        }

        if (idOrganeExecution == null) {
            getSession().addError("PMT_AVANCE_IDORGEX_NULL");
        }

        if (dateEcheancePaiement == null) {
            getSession().addError("PMT_AVANCE_DATEECHEANCE_NULL");
        }

    }

    private boolean isIso20022(String idOrganeExecution, BSession session) {
        if (isIso == null) {
            CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
            mgr.setSession(session);
            mgr.setForIdOrganeExecution(idOrganeExecution);
            try {
                mgr.find();
                if (mgr.size() != 1) {
                    throw new Exception();
                }
            } catch (Exception e) {
                getSession().addError("PMT_AVANCE_IDORGANEEXEC_NULL");
            }

            isIso = ((CAOrganeExecution) mgr.getEntity(0)).getIdTypeTraitementOG().equals(
                    APIOrganeExecution.OG_ISO_20022);
        }
        return isIso.booleanValue();
    }

    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Recherche du lot de type avance
     * Un seul lot de type avance possible
     */
    private void searchLotAvance(BITransaction transaction) throws Exception {
        RELotManager lotMgr = new RELotManager();
        lotMgr.setSession(getSession());
        lotMgr.setForCsType(IRELot.CS_TYP_LOT_PMT_AVANCES);
        lotMgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
        lotMgr.find(transaction);
        lotManager = lotMgr;
    }

    /**
     * Initialisation de la transaction de base
     * 
     * @throws Exception exception potentiellement générée
     */
    private void initTransaction() throws Exception {
        transaction = getTransaction();

        if (!transaction.isOpened()) {
            transaction.openTransaction();
        }
    }

    /**
     * Initialisation de la session osiris pour la compta auxilliarie
     * 
     * @throws Exception exception potentiellement générée
     */
    private void initSessionOsiris() throws Exception {
        sessionOsiris = (BSession) PRSession.connectSession(getSession(), CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    /**
     * Définition du lot de type avance
     * Un seul lot doit exister, si pas de lot création d'un nouveau lot de type avance
     * 
     * @throws Exception exception potentiellement générée
     */
    private void setLotAvance() throws Exception {

        JACalendar cal = new JACalendarGregorian();
        // si le lot est existant la date de dernier paiement des avances
        // est setter avec celle du lot
        if (!lotManager.isEmpty()) {
            lotAvance = (RELot) lotManager.get(0);
            lotAvance.retrieve(transaction);
        } else {
            lotAvance = new RELot();
            lotAvance.setSession(getSession());
            lotAvance.setCsTypeLot(IRELot.CS_TYP_LOT_PMT_AVANCES);
            JADate moisPrecedent = cal.addMonths(dateDernierPaiementMensuel, -1);
            lotAvance.setDateEnvoiLot(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(moisPrecedent.toStrAMJ()));
            lotAvance.setDateCreationLot(JACalendar.todayJJsMMsAAAA());
            lotAvance.setCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
            lotAvance.add(transaction);
        }
        dateDernierPaiementAvance = lotAvance.getDateEnvoiLot();
    }

    /**
     * On s'assure que la de de dernier paiement mensuel n'est pas egal
     * a la date de dernier paiement des avances
     * 
     * @throws Exception exception potentiellement générée
     */
    private void checkIsPaiementRentePasEffectue() throws Exception {
        JACalendar cal = new JACalendarGregorian();

        if (cal.compare(dateDernierPaiementMensuel, new JADate(dateDernierPaiementAvance)) == JACalendar.COMPARE_EQUALS) {
            throw new Exception(getSession().getLabel("PMT_AVANCES_RENTES_DEJA_EFFECTUE") + " : "
                    + dateDernierPaiementMensuel.toStr("."));
        }
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        BITransaction transaction = getTransaction();

        try {

            initTransaction();

            initSessionOsiris();

            dateDernierPaiementMensuel = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));

            searchLotAvance(transaction);

            setLotAvance();

            checkIsPaiementRentePasEffectue();

            findAvancesToExecute();

            // Si manager vide, on quitte
            if (manager.isEmpty()) {
                return true;
            }
            initCompta((BTransaction) transaction);

            dealPaiementAvances();

            updateLot();

            return true;
        } catch (Exception e) {
            dealProcessException(e);
            return false;
        } finally {
            finalizeProcess();
        }
    }

    /**
     * Gestion des exceptions du process. Gestion du bloc catch
     * 
     * @throws Exception exception potentiellement générée
     */
    private void dealProcessException(Exception exception) {
        processOnSuccess = false;
        getMemoryLog().logMessage(exception.getMessage(), FWMessage.ERREUR, this.getClass().toString());
        if (transaction.hasErrors()) {
            getMemoryLog().logMessage(transaction.getErrors().toString(), FWMessage.ERREUR, this.getClass().toString());
        }
        if (getSession().hasErrors()) {
            getMemoryLog()
                    .logMessage(getSession().getErrors().toString(), FWMessage.ERREUR, this.getClass().toString());
        }
        try {
            transaction.rollback();
        } catch (Exception transactionException) {
            getMemoryLog().logMessage(transactionException.getMessage(), FWMessage.ERREUR, this.getClass().toString());
        }

    }

    /**
     * Gestion du bloc finnally
     * 
     * @throws Exception exception potentiellement générée
     */
    private void finalizeProcess() throws Exception {
        try {
            if (processOnSuccess) {
                emailObject = getSession().getLabel("PMT_AVANCES_RENTE_OK");
            } else {
                emailObject = getSession().getLabel("PMT_AVANCES_RENTE_KO");
            }
        } finally {
            try {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            } catch (Exception closeTransactionException) {
                JadeLogger.error(this, closeTransactionException.getMessage());
                throw new Exception(closeTransactionException.getCause());
            }
        }
    }

    /**
     * Mise à jour du lot de type avance après le paiement des avances
     * Création de l'ordre groupé
     * 
     * @throws Exception exception potentiellement générée
     */
    private void updateLot() throws Exception {
        lotAvance.retrieve(transaction);
        lotAvance
                .setDateEnvoiLot(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateDernierPaiementMensuel.toStrAMJ()));
        lotAvance.update(transaction);
        transaction.commit();

        if (!transaction.hasErrors()) {
            // comptabilisation
            compta.comptabiliser();
            // Creation de l'og
            doPreparerOG();
        }
    }

    /**
     * Methode gérant le paiement des avances retournées
     * Etat pas terminé, et date de debut plus petite ou egale à la date de
     * 
     * @throws Exception
     */
    private void dealPaiementAvances() throws Exception {

        JACalendar cal = new JACalendarGregorian();
        String dateValeurComptable = getDateEcheancePaiement();

        for (Object oAvance : manager.getContainer()) {

            REAvance avance = (REAvance) oAvance;
            avance.retrieve(transaction);

            JADate dateFin = null;
            if (JadeStringUtil.isBlankOrZero(avance.getDateFinAcompte())) {
                dateFin = new JADate(DATE_FIN);
            } else {
                dateFin = new JADate(avance.getDateFinAcompte());
            }

            if ((cal.compare(new JADate(dateValeurComptable), dateFin) == JACalendar.COMPARE_FIRSTUPPER)
                    || (cal.compare(new JADate(dateValeurComptable), dateFin) == JACalendar.COMPARE_EQUALS)) {
                dealAvanceTermine(avance);
            } else {
                dealAvanceAPAyer(avance, dateValeurComptable);
            }
        }
    }

    private void dealAvanceAPAyer(REAvance avance, String dateValeurComptable) throws Exception {
        avance.setCsEtatAcomptes(IREAvances.CS_ETAT_ACOMPTE_EN_COURS);
        updateAvance(avance);

        // Création de l'ov
        PRTiersWrapper tw = PRTiersHelper.getTiersParId(getSession(), avance.getIdTiersBeneficiaire());

        final String nss = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        final String nomPrenom = tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

        String idTiersPrincipal = avance.getIdTiersAdrPmt();

        if (JadeStringUtil.isBlankOrZero(idTiersPrincipal) || Long.parseLong(idTiersPrincipal) < 0) {
            idTiersPrincipal = avance.getIdTiersBeneficiaire();
        }

        String isoLangFromIdTiers = PRTiersHelper.getIsoLangFromIdTiers(getSession(), idTiersPrincipal);

        final String msgAvance = getOvDescriptionForDomaine(avance.getCsDomaineAvance(), isoLangFromIdTiers);

        final String motifVersement = MotifVersementUtil.formatAvance(nss, nomPrenom, msgAvance);

        // paiement des avances
        getMemoryLog().logMessage(
                REModuleComptablePmtAvance.getInstance(sessionOsiris).payerAvance(this, getSession(), transaction,
                        compta, avance.getIdTiersBeneficiaire(), avance.getIdTiersAdrPmt(), avance.getCsDomaine(),
                        new FWCurrency(avance.getMontantMensuel()), motifVersement, dateValeurComptable,
                        avance.getCsDomaineAvance()));

    }

    /**
     * Gestion des avances qui sont passés dans l'état terminé.
     * CàD si la date de fin d el'avance est antérieur à la date de valeur comptable
     * 
     * @throws Exception
     */
    private void dealAvanceTermine(REAvance avance) throws Exception {
        avance.setCsEtatAcomptes(IREAvances.CS_ETAT_ACOMPTE_TERMINE);
        updateAvance(avance);
    }

    private void updateAvance(REAvance avance) throws Exception {
        avance.setCsEtat1erAcompte(IREAvances.CS_ETAT_1ER_ACOMPTE_ANNULE);
        avance.wantCallValidate(false);
        avance.update(transaction);
    }

    /**
     * Recherche des avances de tous domaine non terminé et antérieur ou egale
     * au dernier jour de la date du prochain paiement mensuel
     * 
     * @return
     * @throws Exception
     */
    private void findAvancesToExecute() throws Exception {
        manager = new REAvanceManager();
        manager.setSession(getSession());
        manager.setForCsEtatAcomptesDifferentDe(IREAvances.CS_ETAT_ACOMPTE_TERMINE);
        // date prochain paiement -1
        String dateLimite = JadeDateUtil.addDays("01." + REPmtMensuel.getDateProchainPmt(getSession()), -1);
        manager.setUntilDateDebutAcompte(dateLimite);
        manager.find(transaction, BManager.SIZE_NOLIMIT);

        // si manager vide on loggue
        if (manager.isEmpty()) {
            getMemoryLog().logMessage((getSession()).getLabel("PMT_AVANCE_EMPTY"), FWMessage.INFORMATION,
                    this.getClass().getName());
        }
    }

    /**
     * Methode de validation appelé par le BProcess
     * Si la session est mise en erreur, arret du process
     * 
     * @throws Exception exception potentiellement générée
     * 
     */
    @Override
    protected void _validate() throws Exception {

        if ((getParent() == null)) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
                setSendMailOnError(false);
            } else {
                setSendCompletionMail(true);
                setSendMailOnError(true);
            }

            setControleTransaction(getTransaction() == null);
        }
        // check de paramètres non null et mandatory
        validateNotNullParameters();
        validateMandatoryParameters();

        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * Check des paramètres obligatoires
     */
    private void validateMandatoryParameters() {
        // Check noOg
        if (!isIso20022(idOrganeExecution, getSession())) {
            if (JadeStringUtil.isBlank(getNoOg())) {
                getSession().addError(getSession().getLabel("PMT_AVANCE_NOOG_VIDE"));
            }
        }
        // check idOrganeexecution
        if (JadeStringUtil.isBlank(getIdOrganeExecution())) {
            getSession().addError(getSession().getLabel("PMT_AVANCE_IDORGEX_VIDE"));
        }

        if (JadeStringUtil.isBlank(getDateEcheancePaiement())) {
            getSession().addError(getSession().getLabel("PMT_AVANCE_DATEECHEANCE_VIDE"));
        }
    }

    /**
     * Préparation de l'ordre groupé
     * 
     * @throws Exception exception potentiellement générée
     */
    private void doPreparerOG() throws Exception {

        if (Integer.parseInt(noOg) < 10) {
            noOg = "0" + noOg;
        }

        if (compta != null) {
            getMemoryLog().logMessage(
                    getSession().getLabel("PMT_AVANCE_PREPARATION") + " " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");

            String libelleOG = getSession().getLabel("PMT_AVANCE_MENSUEL_RENTE_DESC_OG") + noOg;

            compta.preparerOrdreGroupe(idOrganeExecution, String.valueOf(noOg), dateEcheancePaiement,
                    CAOrdreGroupe.VERSEMENT, CAOrdreGroupe.NATURE_RENTES_AVS_AI, libelleOG, isoCsTypeAvis,
                    isoGestionnaire, isoHightPriority);
        }
    }

    public String getDateEcheancePaiement() {
        return dateEcheancePaiement;
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
     * @param session la session
     * @param cal l'instance de JACalendar
     * @return Date valeur comptable au format jj.mm.aaaa
     * @throws JAException l'exception généré si problème avec JACalendar
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
     * Retourne le libelle de description de l'OV
     * Si le domaine null, ou pas trouvé, on retourne le libellé du domaine rente
     * 
     * @param csDomaineApplicatifAvance
     * @return
     */
    private String getOvDescriptionForDomaine(String csDomaineApplicatifAvance, String isoLangue) {
        String idMessage = "";

        // si le domaine est différent de null, sinon on met la description rente par defaut
        if (csDomaineApplicatifAvance != null) {
            if (csDomaineApplicatifAvance.equals(IREAvances.CS_DOMAINE_AVANCE_PC)) {
                idMessage = "AVANCE_PC";
            } else if (csDomaineApplicatifAvance.equals(IREAvances.CS_DOMAINE_AVANCE_RENTE)) {
                idMessage = "AVANCE_PC";
            } else {
                idMessage = "AVANCE_RFM";
            }
        } else {
            idMessage = "AVANCE_RENTE";
        }

        return MotifVersementUtil.getTranslatedLabelFromIsolangue(isoLangue, idMessage, getSession());
    }

    /**
     * Initialisation de l'API de la compta auxilliaire
     * 
     * @param transaction la transaction de base
     * @param cal l'instance de JACalendar
     * @throws Exception exception potentiellement générée
     */
    private void initCompta(BTransaction transaction) throws Exception {
        compta = null;

        // instanciation du processus de compta
        compta = (APIGestionComptabiliteExterne) sessionOsiris.getAPIFor(APIGestionComptabiliteExterne.class);
        String dateValeurComptable = getDateValeurComptable(getSession(), new JACalendarGregorian());

        compta.setDateValeur(dateValeurComptable);

        compta.setEMailAddress(getEMailAddress());

        FWMemoryLog comptaMemoryLog = new FWMemoryLog();
        comptaMemoryLog.setSession(sessionOsiris);
        compta.setMessageLog(comptaMemoryLog);

        compta.setSendCompletionMail(false);
        compta.setTransaction(transaction);
        String libelle = getSession().getLabel("JOURNAL_PMT_AVANCES_RENTES");
        if (libelle.length() > 40) {
            libelle = libelle.substring(0, 40);
        }
        compta.setLibelle(libelle);
        compta.setProcess(this);
        compta.createJournal();

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setNoOg(String noOg) {
        this.noOg = noOg;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;

    }

    public void setDateEcheancePaiement(String dateEcheance) {
        dateEcheancePaiement = dateEcheance;
    }

    public String getIsoCsTypeAvis() {
        return isoCsTypeAvis;
    }

    public void setIsoCsTypeAvis(String isoCsTypeAvis) {
        this.isoCsTypeAvis = isoCsTypeAvis;
    }

    public String getIsoGestionnaire() {
        return isoGestionnaire;
    }

    public void setIsoGestionnaire(String isoGestionnaire) {
        this.isoGestionnaire = isoGestionnaire;
    }

    public String getIsoHightPriority() {
        return isoHightPriority;
    }

    public void setIsoHightPriority(String isoHightPriority) {
        this.isoHightPriority = isoHightPriority;
    }

}
