package globaz.helios.process;

import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.helios.api.ICGJournal;
import globaz.helios.db.comptes.CGEcritureListViewBean;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import globaz.helios.process.journal.CAJournalProcessUtils;

/**
 * Process d'annulation du journal et écritures.
 * 
 * @author: Administrator
 */
public class CGJournalAnnulerProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final static String LABEL_PREFIXE = "ANNULER_JOURNAL_";

    private String eMailObject = "";
    private String idJournal = "";
    private CGJournal journal = null;

    FWMemoryLog journalLog = new FWMemoryLog();
    int nbEntity = 0;
    int nbEntityAnnule = 0;
    int nbErrors = 0;

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     */
    public CGJournalAnnulerProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     * 
     * @param parent
     *            BProcess
     */
    public CGJournalAnnulerProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     * 
     * @param session
     *            BSession
     */
    public CGJournalAnnulerProcess(BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_ANNULER_CRITIQUE"));

        if (!testJournal()) {
            return false;
        }

        if (!CAJournalProcessUtils.resetJournalLog(getSession(), getTransaction(), getJournal())) {
            return false;
        }

        if (!CAJournalProcessUtils.setEtatJournalToTraitement(getSession(), getTransaction(), getJournal())) {
            return false;
        }

        if (!annulerEcritures()) {
            return false;
        }

        if (!updateJournal()) {
            return false;
        }

        updateEmailSubject();

        return true;
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        if (!testJournal()) {

            // getMemoryLog().logMessage(label("ERROR_ETAT_ANNULE"),
            // FWMessage.INFORMATION, label("MSG_JOURNAL") +
            // getJournal().getNumero());

            return;
        }

        if (testPeriodeCloture()) {
            _addError(getSession().getLabel("ANNULER_JOURNAL_PERIODE_CLOTUREE_ERROR"));
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
    private boolean annulerEcritures() {
        nbErrors = 0;
        nbEntity = 0;
        nbEntityAnnule = 0;

        journalLog = new FWMemoryLog();
        journalLog.setSession(getSession());

        CGEcritureListViewBean manager = new CGEcritureListViewBean();
        manager.setForIdJournal(getJournal().getIdJournal());
        manager.wantForEstActive(true);
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
                            warn("WARN_ECR_NON_ACTIVE", ecriture.getIdEcriture());
                        } else {
                            ecriture.wantEstActive(new Boolean(false));
                            ecriture.setSession(getSession());
                            ecriture.update(getTransaction());

                            // Mise à jour des soldes provisoires et definitifs.
                            CGGestionEcritureUtils.updateSoldesDefinitif(getSession(), getTransaction(), getJournal(),
                                    ecriture);

                            nbEntityAnnule++;

                            if (getTransaction().hasErrors()) {
                                throw (new Exception(getTransaction().getErrors().toString()));
                            } else {
                                getTransaction().commit();
                            }
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
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 15:05:25)
     * 
     * @return globaz.helios.db.comptes.CGModeleEcriture
     */
    public globaz.helios.db.comptes.CGJournal getJournal() {
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
                getSession().getLabel("GLOBAL_JOURNAL") + " N°" + getJournal().getNumero());
    }

    /**
     * @see BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */

    private String label(String code) {
        return getSession().getLabel(LABEL_PREFIXE + code);
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
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 15:05:25)
     * 
     * @param newJournal
     *            globaz.helios.db.comptes.CGModeleEcriture
     */
    public void setJournal(globaz.helios.db.comptes.CGJournal newJournal) {
        journal = newJournal;
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

        if (ICGJournal.CS_ETAT_PARTIEL.equals(getJournal().getIdEtat())) {
            _addError(label("ERROR_8"));
            return false;
        }

        if (ICGJournal.CS_ETAT_ANNULE.equals(getJournal().getIdEtat())) {
            _addError(label("ERROR_ETAT_ANNULE"));
            return false;
        }

        return true;
    }

    /**
     * La periode est-elle cloture ?
     * 
     * @return
     * @throws Exception
     */
    private boolean testPeriodeCloture() throws Exception {
        CGPeriodeComptable periode = new CGPeriodeComptable();
        periode.setSession(getSession());
        periode.setIdPeriodeComptable(getJournal().getIdPeriodeComptable());
        periode.retrieve();

        return (periode.isNew() || periode.isEstCloture().booleanValue());
    }

    /**
     * Mise à jour du sujet de l'email.
     */
    private void updateEmailSubject() {
        if (journalLog.getErrorLevel() == FWMessage.AVERTISSEMENT) {
            setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_ANNULER_AVERTISSEMENT"));
        } else if (journalLog.getErrorLevel() == FWMessage.ERREUR) {
            setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_ANNULER_ERREUR"));
        } else if (journalLog.getErrorLevel() == FWMessage.INFORMATION || !journalLog.hasMessages()) {
            setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_ANNULER_OK"));
        }
    }

    /**
     * Mise à jour du journal après traitement.
     * 
     * @return
     */
    private boolean updateJournal() {
        if (nbEntity == 0) {
            warn("WARN_1", "");
        }

        if (nbErrors == 0) {
            // si toutes les ecritures ont été passée sans erreurs :
            // l'etat du journal passe a 'annule'
            getMemoryLog().logMessage(nbEntityAnnule + " " + label("MSG_5") + " " + nbEntity + " " + label("MSG_6"),
                    FWMessage.INFORMATION, " " + label("MSG_JOURNAL") + " " + getJournal().getNumero());
            info("INFO_2", "");
            try {
                CAJournalProcessUtils.updateEtatJournal(getSession(), getTransaction(), getJournal(),
                        ICGJournal.CS_ETAT_ANNULE);
            } catch (Exception e) {

                try {
                    getTransaction().rollback();
                } catch (Exception ee) {
                    return false;
                }
                return false;
            }
            journalLog.logMessage(nbEntity + " " + label("MSG_7") + " " + nbEntityAnnule + " " + label("MSG_8"),
                    FWMessage.INFORMATION, label("MSG_EFFACEMENT"));
        } else {
            getMemoryLog().logMessage(nbEntity + " " + label("MSG_8") + " " + nbErrors + " " + label("MSG_9"),
                    FWMessage.ERREUR, label("MSG_JOURNAL") + " " + getJournal().getNumero());
            getMemoryLog().logMessage(label("ERROR_4"), FWMessage.ERREUR,
                    label("MSG_JOURNAL") + " " + getJournal().getNumero());
            try {
                CAJournalProcessUtils.updateEtatJournal(getSession(), getTransaction(), getJournal(),
                        ICGJournal.CS_ETAT_ERREUR);
            } catch (Exception e) {

                try {
                    getTransaction().rollback();
                } catch (Exception ee) {
                    return false;
                }
                return false;
            }
            journalLog.logMessage(nbEntity + " " + label("MSG_7") + " " + nbErrors + " " + label("MSG_10"),
                    FWMessage.ERREUR, label("MSG_EFFACEMENT"));
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
                    getSession().getLabel("GLOBAL_JOURNAL") + " N°" + getJournal().getNumero());
        }
    }

}
