package globaz.lynx.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.journal.LXJournal;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;
import globaz.lynx.db.ventilation.LXVentilation;
import globaz.lynx.db.ventilation.LXVentilationManager;
import globaz.lynx.utils.LXJournalUtil;
import globaz.osiris.process.journal.CAUtilsJournal;

public class LXNoteDeCreditEncaisserProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateValeurCG;
    private String idCompteDebite;
    private String idJournal;
    private String idOperation;
    private String idSection;
    private String idSociete;

    private LXJournal journal = null;
    private LXOperation operation = null;
    private LXSocieteDebitrice societe = null;

    /**
     * Commentaire relatif au constructeur LXOrdreGroupeAnnulerProcess.
     */
    public LXNoteDeCreditEncaisserProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur LXOrdreGroupeAnnulerProcess.
     * 
     * @param parent
     *            BProcess
     */
    public LXNoteDeCreditEncaisserProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur LXOrdreGroupeAnnulerProcess.
     * 
     * @param session
     *            BSession
     */
    public LXNoteDeCreditEncaisserProcess(BSession session) {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // Do nothing
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        LXJournal journalEncaisser = null;
        LXOperation operationEncaisser = null;

        try {

            journalEncaisser = createJournal();
            operationEncaisser = createNoteDeCredit(journalEncaisser.getIdJournal());
            createVentilations(getOperation().getIdOperation(), operationEncaisser.getIdOperation(), getOperation()
                    .getMontant());

            comptabiliseJournal(journalEncaisser.getIdJournal(), journalEncaisser.getIdSociete());

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getClass().getName());
            return false;
        }

        return true;
    }

    /**
     * @see BProcess#_validate() throws Exception
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdSociete())) {
            _addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_SOCIETE"));
            return;
        }

        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            _addError(getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_JOURNAL"));
            return;
        }

        if (JadeStringUtil.isBlank(getDateValeurCG())) {
            _addError(getTransaction(), getSession().getLabel("ENCAISSER_DATEVALEURCG"));
            return;
        }

        if (!new CAUtilsJournal().isPeriodeComptableOuverte(getSession(), getTransaction(), getDateValeurCG(),
                getJournal().getSociete().getIdMandat())) {
            return;
        }

        if (!getJournal().isComptabilise()) {
            _addError(getTransaction(), getSession().getLabel("ENCAISSER_ETAT_JOURNAL"));
            return;
        }

        if (JadeStringUtil.isIntegerEmpty(getIdCompteDebite())) {
            _addError(getTransaction(), getSession().getLabel("ENCAISSER_COMPTE_DEBITE"));
            return;
        }

        if (!LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE.equals(getCsTypeOperation())) {
            _addError(getTransaction(), getSession().getLabel("ENCAISSER_OPERATION_NDC_DE_BASE"));
            return;
        }
    }

    /**
     * Comptabilise le journal suivant l'id journal et l'id société passé en paramètre
     * 
     * @param idJournalEncaisser
     * @param idSocieteEncaisser
     * @throws Exception
     */
    private void comptabiliseJournal(String idJournalEncaisser, String idSocieteEncaisser) throws Exception {
        // Execution de la comptabilisation du nouveau journal
        LXJournalComptabiliserProcess comptabilisationProcess = new LXJournalComptabiliserProcess();
        comptabilisationProcess.setIdJournal(idJournalEncaisser);
        comptabilisationProcess.setIdSociete(idSocieteEncaisser);
        comptabilisationProcess.setEMailAddress(getEMailAddress());
        comptabilisationProcess.setParent(this);
        comptabilisationProcess.executeProcess();
    }

    // *******************************************************
    // Outils
    // *******************************************************

    /**
     * Créé et retourne un journal
     * 
     * @return
     * @throws Exception
     */
    private LXJournal createJournal() throws Exception {
        return LXJournalUtil.fetchJournalJournalier(getSession(), getTransaction(), getIdSociete());
    }

    /**
     * Crée et retourne une note de crédit
     * 
     * @param idJournalEncaisser
     * @return
     * @throws Exception
     */
    private LXOperation createNoteDeCredit(String idJournalEncaisser) throws Exception {

        LXOperation operationEncaisser = (LXOperation) getOperation().clone();

        FWCurrency tmp = new FWCurrency(operationEncaisser.getMontant());
        tmp.negate();

        operationEncaisser.setCsTypeOperation(LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE);
        operationEncaisser.setIdJournal(idJournalEncaisser);
        operationEncaisser.setMontant(tmp.toString());
        operationEncaisser.setIdOperation(null);
        operationEncaisser.setIdOperationSrc(getIdOperation());
        operationEncaisser.setCsEtatOperation(LXOperation.CS_ETAT_OUVERT);
        operationEncaisser.setCsCodeTVA("0");

        operationEncaisser.add(getTransaction());

        if (operationEncaisser.hasErrors()) {
            throw new Exception(operationEncaisser.getErrors().toString());
        }

        if (operationEncaisser.isNew()) {
            throw new Exception(getSession().getLabel("ENCAISSER_NOTEDECREDIT_NON_CREE"));
        }

        return operationEncaisser;
    }

    /**
     * Charge et return les ventilations
     */
    private void createVentilations(String forIdOperation, String idOperationEncaisser, String montant)
            throws Exception {
        LXVentilationManager ventilationManager = new LXVentilationManager();
        ventilationManager.setSession(getSession());
        ventilationManager.setForIdOperation(forIdOperation);
        ventilationManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (!ventilationManager.isEmpty()) {

            for (int i = 0; i < ventilationManager.size(); i++) {
                LXVentilation ventilation = (LXVentilation) ventilationManager.get(i);

                LXVentilation ventilationEncaisser = (LXVentilation) ventilation.clone();
                ventilationEncaisser.setIdVentilation(null);
                ventilationEncaisser.setIdOperation(idOperationEncaisser);

                if (ventilationEncaisser.getCodeDebitCredit().equals(CodeSystem.CS_DEBIT)) {

                    ventilationEncaisser.setCodeDebitCredit(CodeSystem.CS_CREDIT);
                    ventilationEncaisser.add(getTransaction());

                    if (ventilationEncaisser.hasErrors()) {
                        throw new Exception(ventilationEncaisser.getErrors().toString());
                    }

                    if (ventilationEncaisser.isNew()) {
                        throw new Exception(getSession().getLabel("ENCAISSER_VENTILATION_NON_CREE"));
                    }

                }
            }

            LXVentilation ventilationEncaisser = new LXVentilation();
            ventilationEncaisser.setIdOperation(idOperationEncaisser);
            ventilationEncaisser.setIdCompte(getIdCompteDebite());
            ventilationEncaisser.setCodeDebitCredit(CodeSystem.CS_DEBIT);
            ventilationEncaisser.setMontant(montant);
            ventilationEncaisser.add(getTransaction());

            if (ventilationEncaisser.hasErrors()) {
                throw new Exception(ventilationEncaisser.getErrors().toString());
            }

            if (ventilationEncaisser.isNew()) {
                throw new Exception(getSession().getLabel("ENCAISSER_VENTILATION_NON_CREE"));
            }

        } else {
            throw new Exception(getSession().getLabel("ENCAISSER_VENTILATION_NON_TROUVEE"));
        }
    }

    /**
     * Permet de récupérer le type de la note de crédit
     * 
     * @return
     * @throws Exception
     */
    private String getCsTypeOperation() throws Exception {
        return getOperation().getCsTypeOperation();
    }

    public String getDateValeurCG() {
        return dateValeurCG;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors()) {
            return getSession().getLabel("ENCAISSER_ERREUR");
        } else {
            return getSession().getLabel("ENCAISSER_OK");
        }
    }

    public String getIdCompteDebite() {
        return idCompteDebite;
    }

    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Return l'id mandat de la société. Nécessaire pour l'écran de détail, lien vers compta gen recherche compte.
     * 
     * @return
     */
    public String getIdMandat() {
        retrieveSociete();

        if (societe != null) {
            return societe.getIdMandat();
        } else {
            return "";
        }
    }

    public String getIdOperation() {
        return idOperation;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getIdSection() {
        return idSection;
    }

    public String getIdSociete() {
        return idSociete;
    }

    /**
     * Charge et return le journal fournisseur.
     * 
     * @return
     */
    private LXJournal getJournal() throws Exception {
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
     * Charge et return l'operation
     * 
     * @return
     */
    private LXOperation getOperation() throws Exception {
        if (operation == null) {
            operation = new LXOperation();
            operation.setSession(getSession());
            operation.setIdOperation(getIdOperation());

            operation.retrieve(getTransaction());

            if (operation.hasErrors()) {
                throw new Exception(operation.getErrors().toString());
            }

            if (operation.isNew()) {
                throw new Exception(getSession().getLabel("FACTURE_VALIDATE_JOURNAL_INCONNU"));
            }
        }

        return operation;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Retrouve la societe si pas encore chargée.
     */
    private void retrieveSociete() {
        if (!JadeStringUtil.isIntegerEmpty(getIdSociete()) && societe == null) {
            try {
                societe = new LXSocieteDebitrice();
                societe.setSession(getSession());
                societe.setIdSociete(getIdSociete());
                societe.retrieve();

                if (societe.hasErrors() || societe.isNew()) {
                    societe = null;
                    return;
                }
            } catch (Exception e) {
                // nothing
            }
        }
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setDateValeurCG(String dateValeurCG) {
        this.dateValeurCG = dateValeurCG;
    }

    public void setIdCompteDebite(String idCompteDebite) {
        this.idCompteDebite = idCompteDebite;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdOperation(String idOperation) {
        this.idOperation = idOperation;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

}
