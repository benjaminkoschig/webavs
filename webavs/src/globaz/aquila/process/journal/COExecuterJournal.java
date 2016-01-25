package globaz.aquila.process.journal;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.transition.CO005ExecuterRDP;
import globaz.aquila.db.access.batch.transition.COAbstractDelaiPaiementAction;
import globaz.aquila.db.access.batch.transition.COAbstractEnvoyerDocument;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.journal.COElementJournalBatchManager;
import globaz.aquila.db.access.journal.COJournalBatch;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.traitspec.COTraitementSpecifique;
import globaz.aquila.db.access.traitspec.COTraitementSpecifiqueManager;
import globaz.aquila.print.journal.COImprimerRecapitulatifJournal;
import globaz.aquila.print.journal.COTSOPGEImprimerRequisitionPoursuiteJournal;
import globaz.aquila.process.journal.utils.COUtilsJournal;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.taxes.ICOTaxeProducer;
import globaz.aquila.ts.COTSExecutor;
import globaz.aquila.ts.COTSFileUtil;
import globaz.aquila.util.COJournalAdapter;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class COExecuterJournal extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_ELEMENT_NON_OUVERT = "ELEMENT_NON_OUVERT";
    private static final String LABEL_JOURNAL_EXECUTION_ERROR = "JOURNAL_EXECUTION_ERROR";
    private static final String LABEL_JOURNAL_EXECUTION_OK = "JOURNAL_EXECUTION_OK";
    private static final String LABEL_JOURNAL_NON_OUVERT = "JOURNAL_NON_OUVERT";
    private static final int NOMBRE_DE_CAS_MAX_PAR_FICHIER = 400;

    private String idJournal;
    private COJournalBatch journal;

    private COJournalAdapter journalComptaAux;

    /**
     * Constructor for COExecuterJournal.
     */
    public COExecuterJournal() throws Exception {
        this(new BSession(ICOApplication.DEFAULT_APPLICATION_AQUILA));
    }

    /**
     * Constructor for COExecuterJournal.
     * 
     * @param parent
     */
    public COExecuterJournal(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for COExecuterJournal.
     * 
     * @param session
     */
    public COExecuterJournal(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {

    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        printRecapitulatif(getSession(), getTransaction());

        COTraitementSpecifiqueManager manager = new COTraitementSpecifiqueManager();
        manager.setSession(getSession());
        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; (i < manager.size()) && !isAborted(); i++) {
            COTraitementSpecifique traitement = (COTraitementSpecifique) manager.get(i);
            setProgressDescription(traitement.getIdTraitementSpecifique());

            executeTraitementSpecifique(traitement);
        }

        if (!isAborted()) {
            COUtilsJournal.updateEtatJournal(getSession(), getTransaction(), getIdJournal(), COJournalBatch.TRAITE);
            if (!isAborted()) {
                getJournalComptabiliteAuxilliaire().comptabiliser(getTransaction(), this);
                if (!isAborted()) {
                    printRecapitulatifSpecifique(getSession(), getTransaction());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        COUtilsJournal.isIdJournalEmpty(getSession(), getIdJournal());

        COJournalBatch journal = COUtilsJournal.getJournal(getSession(), getTransaction(), getIdJournal());
        if (!journal.isOuvert()) {
            this._addError(getSession().getLabel(COExecuterJournal.LABEL_JOURNAL_NON_OUVERT));
            throw new Exception(getSession().getLabel(COExecuterJournal.LABEL_JOURNAL_NON_OUVERT));
        }
    }

    /**
     * Si fichier généré (content != vide), attache ce fichier à l'email principal.
     * 
     * @param content
     * @param outputFileNameUtil
     * @throws Exception
     */
    private void attachFile(StringBuffer content, COTSFileUtil outputFileNameUtil) throws Exception {
        if (content.length() > 0) {
            JADate dateCreation = new JADate(getJournal().getDateCreation());

            String outputFileName = Jade.getInstance().getHomeDir() + COTSFileUtil.OUTPUT_FILE_WORK_DIR + "/"
                    + outputFileNameUtil.getOutputFileName(dateCreation);

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFileName, false),
                    "ISO8859_1"));
            pw.write(content.toString());
            pw.close();

            this.registerAttachedDocument(outputFileName);
        }
    }

    /**
     * Exécution de la transition.
     * 
     * @param element
     * @throws Exception
     */
    private void executerTransition(COElementJournalBatch element) throws Exception {
        COTransitionAction action = COServiceLocator.getActionService().getTransitionAction(element.getTransition());

        action.setDateExecution(getJournal().getDateCreation());
        action.setParent(this);
        ((COAbstractEnvoyerDocument) action).wantEnvoyerDocument(false);

        if (action instanceof COAbstractDelaiPaiementAction) {
            ((COAbstractDelaiPaiementAction) action).setDateDelaiPaiement(getJournal().getDateCreation());
        }
        if (action instanceof CO005ExecuterRDP) {
            ((CO005ExecuterRDP) action).setTraitementSpecifique(true);
        }

        setTaxes(element.getContentieux(), element.getTransition(), action);

        COServiceLocator.getTransitionService().executerTransition(getSession(), getTransaction(),
                element.getContentieux(), action, getJournalComptabiliteAuxilliaire());

    }

    /**
     * Execute le traitement spécifique pour tous les éléments du journal correspondants.
     * 
     * @param traitement
     * @throws Exception
     */
    private void executeTraitementSpecifique(COTraitementSpecifique traitement) throws Exception {
        COElementJournalBatchManager manager = new COElementJournalBatchManager();
        manager.setSession(getSession());

        manager.setForIdJournal(getIdJournal());
        manager.setForIdTraitementSpecifique(traitement.getIdTraitementSpecifique());

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        setProgressScaleValue(manager.size());

        StringBuffer sb = new StringBuffer();
        int countElement = 0;
        for (int i = 0; (i < manager.size()) && !isAborted(); i++) {
            COElementJournalBatch element = (COElementJournalBatch) manager.get(i);
            element.setSession(getSession());
            setProgressDescription(element.getContentieux().getCompteAnnexe().getIdExterneRole() + " "
                    + element.getContentieux().getSection().getIdExterne());

            if (!element.isOuvert()) {
                throw new Exception(getSession().getLabel(COExecuterJournal.LABEL_ELEMENT_NON_OUVERT));
            }

            COTSExecutor executor = element.getTraitementSpecifique().getExecutor();
            try {
                String result = executor.execute(getSession(), getTransaction(), element);

                if ((countElement % COExecuterJournal.NOMBRE_DE_CAS_MAX_PAR_FICHIER == 0) && (sb.length() > 0)) {
                    attachFile(sb, traitement.getFileUtil());
                    sb = new StringBuffer();
                }

                if (!JadeStringUtil.isBlank(result)) {
                    sb.append(result);
                }

                countElement++;

                executerTransition(element);

                element.setEtat(COElementJournalBatch.TRAITE);
                element.update(getTransaction());
            } catch (Exception e) {
                this._addError(getTransaction(), e.getMessage());
                getAttachedDocuments().clear();
                throw new Exception(e.getMessage());
            }
            incProgressCounter();
        }

        if (!isAborted()) {
            attachFile(sb, traitement.getFileUtil());
        }
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel(COExecuterJournal.LABEL_JOURNAL_EXECUTION_ERROR);
        } else {
            return getSession().getLabel(COExecuterJournal.LABEL_JOURNAL_EXECUTION_OK);
        }
    }

    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Retourne le journal de contentieux aquila.
     * 
     * @return
     * @throws Exception
     */
    private COJournalBatch getJournal() throws Exception {
        if (journal == null) {
            journal = COUtilsJournal.getJournal(getSession(), getTransaction(), getIdJournal());
        }

        return journal;
    }

    /**
     * Retourne le journal de comptabilité auxiliaire.
     * 
     * @return
     * @throws Exception
     */
    private COJournalAdapter getJournalComptabiliteAuxilliaire() throws Exception {
        if (journalComptaAux == null) {
            journalComptaAux = new COJournalAdapter(getSession());
            journalComptaAux.creerJournal(getTransaction(), false, getJournal().getDateCreation(), getJournal()
                    .getLibelle());
        }

        return journalComptaAux;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Imprime un récapitulatif du journal.
     * 
     * @param session
     * @param transaction
     * @throws Exception
     */
    private void printRecapitulatif(BSession session, BTransaction transaction) throws Exception {
        COImprimerRecapitulatifJournal imprimer = new COImprimerRecapitulatifJournal(getSession());
        imprimer.setSession(getSession());
        imprimer.setParent(this);
        imprimer.setForIdJournal(getIdJournal());
        imprimer.setEMailAddress(getEMailAddress());

        imprimer.start();
    }

    /**
     * Imprimer un recapitulatif specifique pour FER. <br/>
     * A optimiser si plusieurs caisses utilisent des processus spécifiques.
     * 
     * @param session
     * @param transaction
     * @throws Exception
     */
    private void printRecapitulatifSpecifique(BSession session, BTransaction transaction) throws Exception {
        COTSOPGEImprimerRequisitionPoursuiteJournal imprimer = new COTSOPGEImprimerRequisitionPoursuiteJournal(
                getSession());
        imprimer.setSession(getSession());
        imprimer.setParent(this);
        imprimer.setForIdJournal(getIdJournal());
        imprimer.setEMailAddress(getEMailAddress());

        imprimer.start();
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    /**
     * Mise à jour des taxes.
     * 
     * @param contentieux
     * @param transition
     * @param action
     * @throws Exception
     */
    private void setTaxes(COContentieux contentieux, COTransition transition, COTransitionAction action)
            throws Exception {
        ICOTaxeProducer producer = COServiceLocator.getTaxeService().getTaxeProducer(transition.getEtapeSuivante());

        List taxes = producer.getListeTaxes(getSession(), contentieux, transition.getEtapeSuivante());
        action.setTaxes(taxes);
    }

}
