package globaz.lynx.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.helios.db.comptes.CGJournal;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.journal.LXJournal;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;

public class LXJournalProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String eMailObject = "";
    private String idJournal;
    private String idSociete;

    private LXJournal journal = null;
    private CGJournal journalCG = null;
    private LXSocieteDebitrice societe = null;

    /**
     * Commentaire relatif au constructeur LXJournalProcess.
     */
    public LXJournalProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur LXJournalProcess.
     * 
     * @param parent
     *            BProcess
     */
    public LXJournalProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur LXJournalProcess.
     * 
     * @param session
     *            BSession
     */
    public LXJournalProcess(BSession session) {
        super(session);
    }

    /**
     * @see BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // Do nothing
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        return false;
    }

    @Override
    protected String getEMailObject() {
        return eMailObject;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdSociete() {
        return idSociete;
    }

    /**
     * Charge et return le journal fournisseur.
     * 
     * @return
     */
    public LXJournal getJournal() throws Exception {
        if (journal == null) {
            journal = new LXJournal();
            journal.setSession(getSession());
            journal.setIdJournal(getIdJournal());

            journal.retrieve(getTransaction());

            if (journal.hasErrors()) {
                throw new Exception(journal.getErrors().toString());
            }

            if (journal.isNew()) {
                throw new Exception(getSession().getLabel("FACTURE_VALIDATE_JOURNAL_INCONNU"));
            }
        }

        return journal;
    }

    /**
     * Retrouve et return le journal CG
     * 
     * @return
     * @throws Exception
     */
    public CGJournal getJournalCG() throws Exception {
        if (journalCG == null && !JadeStringUtil.isIntegerEmpty(getJournal().getIdJournalCG())) {
            journalCG = new CGJournal();
            journalCG.setSession(getSession());

            journalCG.setIdJournal(getJournal().getIdJournalCG());

            journalCG.retrieve(getTransaction());

            if (journalCG.hasErrors()) {
                throw new Exception(journalCG.getErrors().toString());
            }

            if (journalCG.isNew()) {
                throw new Exception(getSession().getLabel("COMPTABILISER_JOURNAL_CG_INCONNU"));
            }
        }

        return journalCG;
    }

    /**
     * Retrouve la societe si pas encore chargée.
     * 
     * @return
     */
    public LXSocieteDebitrice getSociete() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
        }

        if (societe == null) {
            societe = new LXSocieteDebitrice();
            societe.setSession(getSession());
            societe.setIdSociete(getIdSociete());
            societe.retrieve(getTransaction());

            if (societe.hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }

            if (societe.isNew()) {
                throw new Exception(getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
            }
        }

        return societe;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setEMailObject(String mailObject) {
        eMailObject = mailObject;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdSociete(String idSocieteDebitrice) {
        idSociete = idSocieteDebitrice;
    }

    public void setJournalCG(CGJournal journalCG) {
        this.journalCG = journalCG;
    }

    /**
     * Permet le changement d'etat de l'ordre groupe
     * 
     * @param csEtatOrdreGroupe
     * @throws Exception
     */
    protected void updateEtatJournal(String csEtatJournal) throws Exception {

        getJournal().setCsEtat(csEtatJournal);
        getJournal().update(getTransaction());

        if (getJournal().hasErrors() || getTransaction().hasErrors()) {
            getTransaction().rollback();
            throw new Exception(getSession().getLabel("MAJ_JOURNAL_IMPOSSIBLE"));
        } else {
            getTransaction().commit();
        }
    }

    /**
     * Permet le changement d'etat d'une opération
     * 
     * @param csEtatOrdreGroupe
     * @throws Exception
     */
    protected void updateEtatOperation(LXOperation ope, String csEtatOperation) throws Exception {

        ope.setCsEtatOperation(csEtatOperation);
        ope.update(getTransaction());

        if (getTransaction().hasErrors() || getSession().hasErrors()) {
            getTransaction().rollback();
            throw new Exception(getSession().getLabel("MAJ_OPERATION_IMPOSSIBLE"));
        } else {
            getTransaction().commit();
        }
    }
}
