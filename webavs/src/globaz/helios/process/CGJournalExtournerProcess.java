package globaz.helios.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.helios.api.ICGJournal;
import globaz.helios.db.comptes.CGEcritureListViewBean;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGEnteteEcritureListViewBean;
import globaz.helios.db.comptes.CGEnteteEcritureViewBean;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.helpers.ecritures.CGGestionEcritureAdd;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import java.util.ArrayList;

/**
 * Insérez la description du type ici. Date de création : (20.03.2003 14:48:16)
 * 
 * @author: Administrator
 */
public class CGJournalExtournerProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String LABEL_PREFIXE = "EXTOURNER_JOURNAL_";
    private String dateExtourne = "";
    private String eMailObject = "";
    private String idJournal = "";

    private CGJournal journal = null;

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     */
    public CGJournalExtournerProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     * 
     * @param parent
     *            BProcess
     */
    public CGJournalExtournerProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     * 
     * @param session
     *            BSession
     */
    public CGJournalExtournerProcess(BSession session) {
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
        setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_EXTOURNER_CRITIQUE"));

        if (!testJournal()) {
            return false;
        }

        if (!extournerEcritures()) {
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

        if (!testDateExtourne()) {
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
     * Return le journal de destination.
     * 
     * @param periode
     * @return
     * @throws Exception
     */
    private CGJournal createJournalDestination(CGPeriodeComptable periode) throws Exception {
        CGJournal journalJournalier = new CGJournal();

        journalJournalier.setSession(getSession());
        journalJournalier.setDate(JACalendar.todayJJsMMsAAAA());
        journalJournalier.setDateValeur(getDateExtourne());
        journalJournalier.setIdEtat(ICGJournal.CS_ETAT_OUVERT);
        journalJournalier.setIdExerciceComptable(getJournal().getExerciceComptable().getIdExerciceComptable());
        journalJournalier.setIdPeriodeComptable(periode.getIdPeriodeComptable());
        journalJournalier.setIdTypeJournal(CGJournal.CS_TYPE_AUTOMATIQUE);
        journalJournalier.setEstPublic(new Boolean(false));
        journalJournalier.setEstConfidentiel(new Boolean(false));
        journalJournalier.setProprietaire(getSession().getUserId());
        journalJournalier.setLibelle(getSession().getLabel("EXTOURNE") + " " + journal.getNumero());

        journalJournalier.add(getTransaction());

        return journalJournalier;
    }

    /**
     * Permet d'extourner les écritures de l'entête dans le journal et la période passée en paramètre.
     * 
     * @param entete
     * @param periode
     * @param journalJournalier
     * @throws Exception
     */
    protected void extourneEcriture(CGEnteteEcritureViewBean entete, CGPeriodeComptable periode,
            CGJournal journalJournalier) throws Exception {
        if (CGEcritureViewBean.CS_TYPE_ECRITURE_DETTE_AVOIR.equals(entete.getIdTypeEcriture())) {
            throw new Exception("Les écriture de type Dette & Avoir ne doivent pas être extournées.");
        }

        CGEcritureListViewBean manager = new CGEcritureListViewBean();
        manager.setSession(getSession());
        manager.setForIdEnteteEcriture(entete.getIdEnteteEcriture());
        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (manager.isEmpty()) {
            return;
        }

        CGGestionEcritureViewBean ecritures = new CGGestionEcritureViewBean();
        ecritures.setSession(getSession());

        ecritures.setIdExerciceComptable(journalJournalier.getIdExerciceComptable());
        ecritures.setIdJournal(journalJournalier.getIdJournal());
        ecritures.setDateValeur(journalJournalier.getDateValeur());

        ecritures.setRemarque(entete.getRemarque());
        ecritures.setPiece(entete.getPiece());

        ecritures.setIdFournisseur(entete.getIdFournisseur());
        ecritures.setIdSection(entete.getIdSection());

        ArrayList ecrituresList = new ArrayList();
        for (int i = 0; i < manager.size(); i++) {
            CGEcritureViewBean ecritureAExtourner = ((CGEcritureViewBean) manager.getEntity(i));

            if (!ecritureAExtourner.isEstActive().booleanValue()) {
                throw new Exception("Il n'est pas possible d'extourner une écriture non active!!!");
            }

            CGEcritureViewBean ecritureExtourne = new CGEcritureViewBean();
            ecritureExtourne.setSession(getSession());
            ecritureExtourne.setIdJournal(journalJournalier.getIdJournal());
            ecritureExtourne.setIdExerciceComptable(journalJournalier.getExerciceComptable().getIdExerciceComptable());
            ecritureExtourne.setCodeDebitCredit(ecritureAExtourner.getCodeDebitCredit());
            ecritureExtourne.setDate(ecritureAExtourner.getDateValeur());
            ecritureExtourne.setDateValeur(ecritureAExtourner.getDateValeur());
            ecritureExtourne.setLibelle(ecritureAExtourner.getLibelle());

            // On inverse le signe pour l'extourne
            FWCurrency montant = new FWCurrency(ecritureAExtourner.getMontantBase());
            montant.negate();
            ecritureExtourne.setMontantBase(montant.toString());
            FWCurrency montantME = new FWCurrency(ecritureAExtourner.getMontantBaseMonnaie());
            montantME.negate();

            ecritureExtourne.setMontantMonnaieBase(montantME.toString());
            ecritureExtourne.setCoursMonnaie(ecritureAExtourner.getCoursMonnaie());
            ecritureExtourne.setPiece(ecritureAExtourner.getPiece());
            ecritureExtourne.setIdLivre(ecritureAExtourner.getIdLivre());
            ecritureExtourne.setRemarque(ecritureAExtourner.getRemarque());
            ecritureExtourne.setIdMandat(ecritureAExtourner.getIdMandat());
            ecritureExtourne.setIdCompte(ecritureAExtourner.getIdCompte());
            ecritureExtourne.setIdCentreCharge(ecritureAExtourner.getIdCentreCharge());
            ecritureExtourne.wantEstActive(new Boolean(true));

            ecritureExtourne.setIdExterneCompte(CGGestionEcritureUtils.getIdExterneCompte(getSession(),
                    ecritureExtourne));

            ecrituresList.add(ecritureExtourne);
        }

        ecritures.setEcritures(ecrituresList);

        CGGestionEcritureAdd.addEcritures(getSession(), getTransaction(), ecritures, true);
    }

    /**
     * Extourne toutes les écritures du journal sauf dette et avoir.
     * 
     * @return
     */
    private boolean extournerEcritures() {
        CGEnteteEcritureListViewBean manager = new CGEnteteEcritureListViewBean();
        manager.setForIdJournal(getJournal().getIdJournal());
        manager.setSession(getSession());

        BTransaction readTransaction = null;
        BStatement statement = null;
        try {
            if (!testDateExtourne()) {
                return false;
            }

            readTransaction = (BTransaction) getSession().newTransaction();
            readTransaction.openTransaction();

            statement = manager.cursorOpen(readTransaction);

            CGPeriodeComptable periode = getPeriodeComptableForExtourne();

            CGJournal journalJournalier = createJournalDestination(periode);

            CGEnteteEcritureViewBean entete = null;
            while ((entete = (CGEnteteEcritureViewBean) manager.cursorReadNext(statement)) != null) {
                if (!CGEcritureViewBean.CS_TYPE_ECRITURE_DETTE_AVOIR.equals(entete.getIdTypeEcriture())) {
                    extourneEcriture(entete, periode, journalJournalier);
                }

                if (getTransaction().hasErrors()) {
                    throw (new Exception(getTransaction().getErrors().toString()));
                } else {
                    getTransaction().commit();
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
     * Returns the dateExtourne.
     * 
     * @return String
     */
    public String getDateExtourne() {
        return dateExtourne;
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
     * @return CGModeleEcriture
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
     * Return la periode comptable pour l'extourne.
     * 
     * @return
     * @throws Exception
     */
    private CGPeriodeComptable getPeriodeComptableForExtourne() throws Exception {
        CGPeriodeComptableManager perMgr = new CGPeriodeComptableManager();
        perMgr.setSession(getSession());
        perMgr.setForIdExerciceComptable(journal.getExerciceComptable().getIdExerciceComptable());
        perMgr.setForDateInPeriode(getDateExtourne());
        perMgr.setForPeriodeOuverte(true);
        perMgr.find(getTransaction(), 2);

        if (perMgr.isEmpty()) {
            throw new Exception(label("NO_PERIODE_OUVERTE_POUR_DATE"));
        } else {
            return (CGPeriodeComptable) perMgr.getFirstEntity();
        }
    }

    /**
     * @see BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private String label(String code) {
        return getSession().getLabel(LABEL_PREFIXE + code);
    }

    /**
     * Sets the dateExtourne.
     * 
     * @param dateExtourne
     *            The dateExtourne to set
     */
    public void setDateExtourne(String dateExtourne) {
        this.dateExtourne = dateExtourne;
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
     *            CGModeleEcriture
     */
    public void setJournal(CGJournal newJournal) {
        journal = newJournal;
    }

    /**
     * Test la date d'extourne qui doit être comprise dans l'exercice comptable en cours.
     * 
     * @return
     * @throws Exception
     */
    private boolean testDateExtourne() throws Exception {
        if (JAUtil.isDateEmpty(getDateExtourne())) {
            _addError(label("ERROR_3"));
            return false;
        }

        CGExerciceComptableManager manager = new CGExerciceComptableManager();
        manager.setSession(getSession());
        manager.setBetweenDateDebutDateFin(getDateExtourne());
        manager.setForExerciceOuvert(new Boolean(true));
        manager.setForIdMandat(getJournal().getExerciceComptable().getIdMandat());

        manager.find(getTransaction());

        if (manager.hasErrors() || manager.isEmpty()) {
            _addError(getSession().getLabel("EXER_COMPTABLE_AUCUN_EXERCICE_COMPTABLE"));
            return false;
        }

        if (!getJournal().getExerciceComptable().getIdExerciceComptable()
                .equals(((CGExerciceComptable) manager.getFirstEntity()).getIdExerciceComptable())) {
            _addError(label("ERROR_4"));
            return false;
        }

        return true;
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

        if (!ICGJournal.CS_ETAT_COMPTABILISE.equals(getJournal().getIdEtat())) {
            _addError(label("ERROR_2"));
            return false;
        }

        return true;
    }

    /**
     * Mise à jour du sujet de l'email après traitement.
     */
    private void updateEmailSubject() {
        if (getTransaction().hasErrors()) {
            setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_EXTOURNER_ERREUR"));
        } else {
            setEMailObject(getSession().getLabel("OBJET_MAIL_JOURNAL_EXTOURNER_OK"));
        }
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
