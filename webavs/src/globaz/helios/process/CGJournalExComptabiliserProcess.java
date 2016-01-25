package globaz.helios.process;

import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
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
 * Insérez la description du type ici. Date de création : (20.03.2003 14:48:16)
 * 
 * @author: Administrator
 */
public class CGJournalExComptabiliserProcess extends globaz.globall.db.BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String LABEL_PREFIXE = "EXCOMPTABILISER_JOURNAL_";
    private java.lang.String eMailObject = "";
    private java.lang.String idJournal = "";

    private globaz.helios.db.comptes.CGJournal journal = null;
    FWMemoryLog journalLog = new FWMemoryLog();
    int nbEntity = 0;

    int nbEntityExComptabilisee = 0;

    int nbErrors = 0;

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     */
    public CGJournalExComptabiliserProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     * 
     * @param parent
     *            globaz.globall.db.BProcess
     */
    public CGJournalExComptabiliserProcess(globaz.globall.db.BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CGJournalExComptabiliserProcess(globaz.globall.db.BSession session) {
        super(session);
    }

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
        setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_EXCOMPTABILISER_CRITIQUE"));

        if (!testJournal()) {
            return false;
        }

        if (!testPeriodeComptable()) {
            return false;
        }

        if (!CAJournalProcessUtils.resetJournalLog(getSession(), getTransaction(), getJournal())) {
            return false;
        }

        if (!CAJournalProcessUtils.setEtatJournalToTraitement(getSession(), getTransaction(), getJournal())) {
            return false;
        }

        if (!exComptabiliserEcritures()) {
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
            return;
        }

        if (!testPeriodeComptable()) {
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
     * ExComptabilisation des écritures.
     * 
     * @return
     */
    private boolean exComptabiliserEcritures() {
        nbErrors = 0;
        nbEntity = 0;
        nbEntityExComptabilisee = 0;

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
                        warn("WARN_ECR_ERREUR", "");

                        journalLog.logMessage(label("MSG_1") + ecriture.getExternalReference() + label("MSG_2"),
                                FWMessage.AVERTISSEMENT, label("MSG_EXCOMPTABILISATION"));
                        nbErrors++;
                    } else {
                        if (ecriture.isEstProvisoire().booleanValue()) {
                            warn("WARN_ECR_DEJA_COMPTABILISE", ecriture.getIdEcriture());
                        } else {
                            if (!ecriture.isEstActive().booleanValue()) {
                                ecriture.wantEstActive(new Boolean(true));
                                ecriture.update(getTransaction());

                                warn("WARN_ECR_NON_ACTIVE", ecriture.getIdEcriture());
                            }

                            ecriture.wantEstProvisoire(new Boolean(true));
                            ecriture.setSession(getSession());
                            ecriture.update(getTransaction());
                        }

                        CGGestionEcritureUtils.updateSoldesDefinitif(getSession(), getTransaction(), getJournal(),
                                ecriture);

                        nbEntityExComptabilisee++;

                        if (getTransaction().hasErrors()) {
                            throw (new Exception(getTransaction().getErrors().toString()));
                        } else {
                            getTransaction().commit();
                        }
                    }
                } catch (Exception e) {
                    warn("WARN_ECR_ECHEC", " " + ecriture.getIdEcriture());
                    warn(null, e.getMessage());

                    journalLog.logMessage(label("MSG_1") + ecriture.getExternalReference() + label("MSG_3"),
                            FWMessage.AVERTISSEMENT, label("MSG_EXCOMPTABILISATION"));
                    nbErrors++;

                    try {
                        getTransaction().rollback();
                    } catch (Exception ee) {
                        warn(null, ee.getMessage());
                        return false;
                    }
                }
            }

            manager.cursorClose(statement);
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
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        return eMailObject;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 14:53:42)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdJournal() {
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
     *            java.lang.String
     */

    protected void info(String codeLabel, String msg) {
        getMemoryLog().logMessage(label(codeLabel) + msg, FWMessage.INFORMATION,
                getSession().getLabel("GLOBAL_JOURNAL") + " N°" + getJournal().getNumero());
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */

    private String label(String code) {
        return getSession().getLabel(LABEL_PREFIXE + code);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    protected void setEMailObject(String object) {
        eMailObject = object;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 14:53:42)
     * 
     * @param newIdJournal
     *            java.lang.String
     */
    public void setIdJournal(java.lang.String newIdJournal) {
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

        if (ICGJournal.CS_ETAT_ANNULE.equals(getJournal().getIdEtat())
                || (ICGJournal.CS_ETAT_TRAITEMENT.equals(getJournal().getIdEtat()))) {
            _addError(label("ERROR_8"));
            return false;
        }

        return true;
    }

    /**
     * La période comptable est-elle ouverte ?
     * 
     * @return
     * @throws Exception
     */
    private boolean testPeriodeComptable() {
        CGPeriodeComptable periode = new CGPeriodeComptable();
        periode.setSession(getSession());
        periode.setIdPeriodeComptable(getJournal().getIdPeriodeComptable());

        try {
            periode.retrieve(getTransaction());
        } catch (Exception e) {
            _addError(getSession().getLabel("EXCOMPTABILISER_JOURNAL_PERIODE_CLOTUREE_ERROR"));
            return false;
        }

        if (periode.isNew() || periode.isEstCloture().booleanValue()) {
            _addError(getSession().getLabel("EXCOMPTABILISER_JOURNAL_PERIODE_CLOTUREE_ERROR"));
            return false;
        }

        return true;
    }

    /**
     * Mise à jour du sujet de l'email après traitement.
     */
    private void updateEmailSubject() {
        if (journalLog.getErrorLevel() == FWMessage.AVERTISSEMENT) {
            setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_EXCOMPTABILISER_AVERTISSEMENT"));
        } else if (journalLog.getErrorLevel() == FWMessage.ERREUR) {
            setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_EXCOMPTABILISER_ERREUR"));
        } else if (journalLog.getErrorLevel() == FWMessage.INFORMATION || !journalLog.hasMessages()) {
            setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_EXCOMPTABILISER_OK"));
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
            journalLog.logMessage(label("MSG_J_VIDE"), FWMessage.AVERTISSEMENT, label("MSG_EXCOMPTABILISATION"));
        } else {
            if (nbErrors == 0) {
                try {
                    getMemoryLog().logMessage(
                            nbEntityExComptabilisee + " " + label("MSG_4") + " " + nbEntity + " " + label("MSG_5"),
                            FWMessage.INFORMATION, label("MSG_JOURNAL") + " " + getJournal().getNumero());
                    info("INFO_FIN_COMPTA", "");
                    CAJournalProcessUtils.updateEtatJournal(getSession(), getTransaction(), getJournal(),
                            ICGJournal.CS_ETAT_OUVERT);
                } catch (Exception e) {
                    _addError(e.getMessage());

                    try {
                        getTransaction().rollback();
                    } catch (Exception ee) {
                        return false;
                    }
                    return false;
                }
                journalLog.logMessage(nbEntityExComptabilisee + " " + label("MSG_4") + " " + nbEntity + " "
                        + label("MSG_5"), FWMessage.INFORMATION, label("MSG_EXCOMPTABILISATION"));
            } else {
                try {
                    getMemoryLog().logMessage(nbEntity + " " + label("MSG_6") + " " + nbErrors + " " + label("MSG_7"),
                            FWMessage.ERREUR, label("MSG_JOURNAL") + getJournal().getNumero());
                    info("INFO_ERROR_1", "");
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
                        FWMessage.ERREUR, label("MSG_EXCOMPTABILISATION"));
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
                    getSession().getLabel("GLOBAL_JOURNAL") + " N°" + getJournal().getNumero());
        }
    }

}
