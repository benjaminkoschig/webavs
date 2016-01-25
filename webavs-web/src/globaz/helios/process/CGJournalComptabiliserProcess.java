package globaz.helios.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.helios.api.ICGJournal;
import globaz.helios.db.comptes.CGEcritureListViewBean;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.mapping.CGMappingProcessComptabiliser;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import globaz.helios.itext.list.CGProcessImpressionJournalEcritures;
import globaz.helios.process.journal.CAJournalProcessUtils;
import globaz.helios.translation.CodeSystem;

/**
 * Insérez la description du type ici. Date de création : (20.03.2003 14:48:16)
 * 
 * @author: Administrator
 */
public class CGJournalComptabiliserProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String LABEL_PREFIXE = "COMPTABILISER_JOURNAL_";
    private String eMailObject = "";
    private String idJournal = "";
    private boolean imprimerJournal = false;

    private CGJournal journal = null;
    FWMemoryLog journalLog = new FWMemoryLog();
    int nbEntity = 0;
    int nbEntityComptabilisee = 0;

    int nbErrors = 0;

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     */
    public CGJournalComptabiliserProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     * 
     * @param parent
     *            globaz.globall.db.BProcess
     */
    public CGJournalComptabiliserProcess(globaz.globall.db.BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CGJournalComptabiliserProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_COMPTABILISER_CRITIQUE"));

        if (!testJournal()) {
            return false;
        }

        testEcrituresEquilibrees();

        if (!CAJournalProcessUtils.resetJournalLog(getSession(), getTransaction(), getJournal())) {
            return false;
        }

        if (!CAJournalProcessUtils.setEtatJournalToTraitement(getSession(), getTransaction(), getJournal())) {
            return false;
        }

        if (!comptabiliserEcritures()) {
            return false;
        }

        if (!updateJournal()) {
            return false;
        }

        updateEmailSubject();

        printJournalDesEcritures();

        return true;
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {

        if (!testJournal()) {
            return;
        }

        setSendCompletionMail(true);
        setControleTransaction(true);
        setSendMailOnError(true);

        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * Parcours les écritures du journal => <br/>
     * 1. Activation des écritures annulées. <br/>
     * 2. Flag provisoire à false. <br/>
     * 3. Contrôle aucune écriture en erreur. <br/>
     * 4. Mise à jour des soldes définitif.
     * 
     * @return
     */
    private boolean comptabiliserEcritures() {
        nbErrors = 0;
        nbEntity = 0;
        nbEntityComptabilisee = 0;

        journalLog = new FWMemoryLog();
        journalLog.setSession(getSession());

        CGEcritureListViewBean manager = new CGEcritureListViewBean();
        manager.setForIdJournal(getJournal().getIdJournal());
        manager.setSession(getSession());

        BTransaction readTransaction = null;
        BStatement statement = null;
        try {
            readTransaction = (BTransaction) getSession().newTransaction();
            readTransaction.openTransaction();

            statement = manager.cursorOpen(readTransaction);

            CGEcritureViewBean ecriture = null;
            while ((ecriture = (CGEcritureViewBean) manager.cursorReadNext(statement)) != null) {
                nbEntity++;
                ecriture.setSession(getSession());

                try {
                    if (ecriture.isEstErreur().booleanValue()) {
                        warn("WARN_ECR_NON_QUITTANCE", ecriture.getIdEcriture());

                        journalLog.logMessage(label("MSG_1") + ecriture.getExternalReference() + label("MSG_2"),
                                FWMessage.AVERTISSEMENT, label("MSG_COMPTABILISATION"));
                        nbErrors++;
                    } else {
                        if (!ecriture.isEstActive().booleanValue()) {
                            ecriture.wantEstActive(new Boolean(true));
                            ecriture.update(getTransaction());

                            warn("WARN_ECR_NON_ACTIVE", ecriture.getIdEcriture());
                        }

                        if (!ecriture.isEstProvisoire().booleanValue()) {
                            warn("WARN_ECR_DEJA_COMPTABILISE", ecriture.getIdEcriture());
                        } else {
                            FWCurrency totalAvoir = new FWCurrency(ecriture.getEntete().getTotalAvoir());
                            totalAvoir.negate();
                            FWCurrency totalDoit = new FWCurrency(ecriture.getEntete().getTotalDoit());
                            if (totalDoit.compareTo(totalAvoir) != 0) {
                                throw (new Exception(getSession().getLabel("ECRITURE_COMPTABILISATION_ERROR_4")
                                        + " idEntete = " + ecriture.getEntete().getIdEnteteEcriture()));
                            }

                            ecriture.wantEstProvisoire(new Boolean(false));
                            ecriture.setSession(getSession());
                            ecriture.update(getTransaction());
                        }

                        CGGestionEcritureUtils.updateSoldesDefinitif(getSession(), getTransaction(), getJournal(),
                                ecriture);

                        CGMappingProcessComptabiliser.execute(getSession(), getTransaction(), getJournal(), ecriture);

                        nbEntityComptabilisee++;
                        if (getTransaction().hasErrors()) {
                            warn("WARN_ECR_ERREUR", ecriture.getIdEcriture());
                            journalLog.logMessage(label("MSG_1") + ecriture.getExternalReference() + label("MSG_2"),
                                    FWMessage.AVERTISSEMENT, label("MSG_COMPTABILISATION"));
                            nbErrors++;
                        }

                        if (getTransaction().hasErrors()) {
                            throw (new Exception(getTransaction().getErrors().toString()));
                        } else {
                            getTransaction().commit();
                        }
                    }
                } catch (Exception e) {
                    warn("WARN_ECR_ECHEC", ecriture.getIdEcriture());
                    warn(null, e.getMessage());

                    journalLog.logMessage(label("MSG_1") + ecriture.getExternalReference() + label("MSG_3"),
                            FWMessage.AVERTISSEMENT, label("MSG_COMPTABILISATION"));
                    nbErrors++;

                    try {
                        getTransaction().rollback();
                    } catch (Exception ee) {
                        warn(null, ee.getMessage());
                        return false;
                    }
                }
            }

        } catch (Exception e) {
            _addError(e.getMessage());
            return false;
        } finally {
            if (readTransaction != null && readTransaction.isOpened()) {
                try {
                    readTransaction.rollback();
                } catch (Exception eTransactionRollback) {
                    _addError(eTransactionRollback.getMessage());
                    return false;
                } finally {
                    try {
                        if (statement != null) {
                            try {
                                manager.cursorClose(statement);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                statement.closeStatement();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            readTransaction.closeTransaction();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return true;
    }

    protected void error(String codeLabel, String msg) {
        getMemoryLog().logMessage(label(codeLabel) + msg, FWMessage.ERREUR,
                getSession().getLabel("GLOBAL_JOURNAL") + " N° " + getJournal().getNumero());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {
        return eMailObject;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 14:53:42)
     * 
     * @return String
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 14:58:29)
     * 
     * @return Boolean
     */
    public boolean getImprimerJournal() {
        return imprimerJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 15:05:25)
     * 
     * @return CGJournal
     */
    public CGJournal getJournal() {
        if (journal == null) {
            try {
                journal = new CGJournal();
                journal.setSession(getSession());
                journal.setIdJournal(getIdJournal());
                journal.retrieve();

                if (journal.isNew()) {
                    journal = null;
                }
            } catch (Exception e) {
                journal = null;
            }
        }
        return journal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     * 
     * @param msg
     *            String
     */

    protected void info(String codeLabel, String msg) {
        getMemoryLog().logMessage(label(codeLabel) + msg, FWMessage.INFORMATION,
                getSession().getLabel("GLOBAL_JOURNAL") + " N° " + getJournal().getNumero());
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private String label(String code) {
        return getSession().getLabel(LABEL_PREFIXE + code);
    }

    /**
     * Si nécessaire impression du journal des écritures.
     */
    private void printJournalDesEcritures() {
        if (getImprimerJournal() && (nbErrors == 0)) {
            try {
                CGProcessImpressionJournalEcritures document = new CGProcessImpressionJournalEcritures(getSession());
                document.setEMailAddress(getEMailAddress());

                document.setSendCompletionMail(false);
                document.setSendMailOnError(false);
                document.setMemoryLog(journalLog);

                document.setIdJournal(getIdJournal());
                document.executeProcess();

                registerAttachedDocument(document.getExportManagerNewFilePath());
            } catch (Exception e) {
                warn("ERROR_3", "");
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return String
     */
    protected void setEMailObject(String object) {
        eMailObject = object;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 14:53:42)
     * 
     * @param newIdJournal
     *            String
     */
    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 14:58:29)
     * 
     * @param newImprimerJournal
     *            Boolean
     */
    public void setImprimerJournal(boolean newImprimerJournal) {
        imprimerJournal = newImprimerJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 15:05:25)
     * 
     * @param newJournal
     *            globaz.helios.db.comptes.CGModeleEcriture
     */
    public void setJournal(globaz.helios.db.comptes.CGJournal newJournal) {
        journal = newJournal;
    }

    /**
     * Test si le total des écritures de débit et de crédit sont équilibrées. <br/>
     * Si non ajoute une erreur à la session/transaction et au memory log.
     */
    private void testEcrituresEquilibrees() {
        BStatement statement = null;
        CGEcritureListViewBean manager = new CGEcritureListViewBean();
        try {
            FWCurrency totalDebit = new FWCurrency();
            FWCurrency totalCredit = new FWCurrency();

            manager.setForIdJournal(journal.getIdJournal());
            manager.setSession(getSession());

            statement = manager.cursorOpen(getTransaction());
            CGEcritureViewBean ecriture;
            while ((ecriture = (CGEcritureViewBean) manager.cursorReadNext(statement)) != null) {
                if (CodeSystem.CS_DEBIT.equals(ecriture.getCodeDebitCredit())
                        || CodeSystem.CS_EXTOURNE_DEBIT.equals(ecriture.getCodeDebitCredit())) {
                    totalDebit.add(ecriture.getMontantBase());
                } else {
                    totalCredit.add(ecriture.getMontantBase());
                }
            }

            totalDebit.abs();
            totalCredit.abs();
            if (totalDebit.compareTo(totalCredit) != 0) {
                _addError(getTransaction(), label("WARN_TOT_DEBIT_CREDIT_DIFF"));
            }
        } catch (Exception e) {
            _addError(e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    manager.cursorClose(statement);
                } catch (Exception e) {
                    if (statement != null) {
                        statement.closeStatement();
                    }
                }
            }
        }
    }

    /**
     * Test journal avant execution.
     * 
     * @return
     */
    private boolean testJournal() {
        if (getJournal() == null) {
            _addError(getSession().getLabel("JOURNAL_INEXISTANT"));
            return false;
        }

        if (ICGJournal.CS_ETAT_TRAITEMENT.equals(getJournal().getIdEtat())) {
            _addError(label("ERROR_8"));
            return false;
        }

        if (ICGJournal.CS_ETAT_COMPTABILISE.equals(getJournal().getIdEtat())) {
            _addError(label("WARN_JOURNAL_DEJA_COMPTABILISE"));
            return false;
        }

        if (getImprimerJournal() && (getEMailAddress() == null || getEMailAddress().equals(""))) {
            _addError(label("ERROR_9"));
            return false;
        }

        return true;
    }

    /**
     * Mise à jour du sujet de l'email après traitement.
     */
    private void updateEmailSubject() {
        if (journalLog.getErrorLevel() == FWMessage.AVERTISSEMENT) {
            setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_COMPTABILISER_AVERTISSEMENT"));
        } else if (journalLog.getErrorLevel() == FWMessage.ERREUR) {
            setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_COMPTABILISER_ERREUR"));
        } else if (journalLog.getErrorLevel() == FWMessage.INFORMATION || !journalLog.hasMessages()) {
            setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_COMPTABILISER_OK"));
        }
    }

    /**
     * Mise à jour du journal après traitement.
     * 
     * @return
     */
    private boolean updateJournal() {
        if (nbEntity == 0) {
            try {
                warn("WARN_JOUR_VIDE", "");
                info("INFO_JOUR_ANNULE", "");
                CAJournalProcessUtils.updateEtatJournal(getSession(), getTransaction(), getJournal(),
                        ICGJournal.CS_ETAT_ANNULE);
            } catch (Exception e) {
                _addError(e.getMessage());

                try {
                    getTransaction().rollback();
                } catch (Exception ee) {
                    return false;
                }
                return false;
            }
            journalLog.logMessage(label("MSG_J_VIDE"), FWMessage.AVERTISSEMENT, label("MSG_COMPTABILISATION"));
        } else {
            if (nbErrors == 0) {
                getMemoryLog().logMessage(
                        nbEntityComptabilisee + " " + label("MSG_4") + " " + nbEntity + " " + label("MSG_5"),
                        FWMessage.INFORMATION, label("MSG_JOURNAL") + " " + getJournal().getNumero());
                info("INFO_FIN_COMPTA", "");
                try {
                    CGMappingProcessComptabiliser.comptabiliserJournauxDestination(getSession(), getTransaction(),
                            journal);

                    CAJournalProcessUtils.updateEtatJournal(getSession(), getTransaction(), getJournal(),
                            ICGJournal.CS_ETAT_COMPTABILISE);
                } catch (Exception e) {

                    _addError(e.getMessage());
                    try {
                        getTransaction().rollback();
                    } catch (Exception ee) {
                        return false;
                    }
                    return false;
                }
                journalLog.logMessage(nbEntityComptabilisee + " " + label("MSG_4") + " " + nbEntity + " "
                        + label("MSG_5"), FWMessage.INFORMATION, label("MSG_JOURNAL"));
            } else if (nbErrors >= nbEntityComptabilisee) {
                getMemoryLog().logMessage(nbEntity + " " + label("MSG_6") + " " + nbErrors + " " + label("MSG_7"),
                        FWMessage.AVERTISSEMENT, label("MSG_JOURNAL") + getJournal().getNumero());
                info("INFO_ERROR_1", "");
                try {
                    CGMappingProcessComptabiliser.comptabiliserJournauxDestination(getSession(), getTransaction(),
                            journal);

                    CAJournalProcessUtils.updateEtatJournal(getSession(), getTransaction(), getJournal(),
                            ICGJournal.CS_ETAT_ERREUR);
                } catch (Exception e) {
                    _addError(e.getMessage());

                    try {
                        getTransaction().rollback();
                    } catch (Exception ee) {
                        return false;
                    }
                    return false;
                }
                journalLog.logMessage(nbEntity + " " + label("MSG_6") + " " + nbErrors + " " + label("MSG_8"),
                        FWMessage.ERREUR, label("MSG_COMPTABILISATION"));
            } else {
                getMemoryLog().logMessage(nbEntity + " " + label("MSG_6") + " " + nbErrors + " " + label("MSG_7"),
                        FWMessage.AVERTISSEMENT, label("MSG_JOURNAL") + getJournal().getNumero());
                info("INFO_ERROR_2", "");
                try {
                    CGMappingProcessComptabiliser.comptabiliserJournauxDestination(getSession(), getTransaction(),
                            journal);

                    CAJournalProcessUtils.updateEtatJournal(getSession(), getTransaction(), getJournal(),
                            ICGJournal.CS_ETAT_PARTIEL);
                } catch (Exception e) {
                    _addError(e.getMessage());

                    try {
                        getTransaction().rollback();
                    } catch (Exception ee) {
                        e.printStackTrace();
                        return false;
                    }
                    return false;
                }
                journalLog.logMessage(nbEntity + " " + label("MSG_6") + " " + nbErrors + " " + label("MSG_8"),
                        FWMessage.AVERTISSEMENT, label("MSG_COMPTABILISATION"));
            }
        }

        return CAJournalProcessUtils.updateLogJournal(getSession(), getTransaction(), getJournal(), journalLog,
                getMemoryLog());
    }

    protected void warn(String codeLabel, String msg) {
        if (codeLabel == null) {
            getMemoryLog().logMessage(msg, FWMessage.AVERTISSEMENT,
                    getSession().getLabel("GLOBAL_JOURNAL") + " N°" + getJournal().getNumero());
        } else {
            getMemoryLog().logMessage(label(codeLabel) + msg, FWMessage.AVERTISSEMENT,
                    getSession().getLabel("GLOBAL_JOURNAL") + " N° " + getJournal().getNumero());
        }
    }

}
