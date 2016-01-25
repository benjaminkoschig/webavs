package globaz.libra.process;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.journalisation.access.JOReferenceDestination;
import globaz.journalisation.db.journalisation.access.JOReferenceDestinationManager;
import globaz.libra.db.formules.LIComplementFormuleAction;
import globaz.libra.db.formules.LIComplementFormuleActionManager;
import globaz.libra.vb.formules.LIFormulesDetailViewBean;
import globaz.libra.vb.journalisations.LIJournalisationsDetailViewBean;
import ch.globaz.libra.constantes.ILIConstantesExternes;

public class LIReceptionProcess extends BProcess {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJournalisation = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIReceptionProcess.
     */
    public LIReceptionProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe LIReceptionProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public LIReceptionProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe LIReceptionProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public LIReceptionProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        BITransaction transaction = null;
        try {
            transaction = ((BSession) getISession()).newTransaction();
            transaction.openTransaction();

            setSendMailOnError(true);
            setSendCompletionMail(false);

            JOReferenceDestinationManager refDestMgr = new JOReferenceDestinationManager();
            refDestMgr.setSession(getSession());
            refDestMgr.setForIdJournalisation(getIdJournalisation());
            refDestMgr.setForTypeReferenceDestination(ILIConstantesExternes.CS_TYPE_FORM_PRESTATIONS);
            refDestMgr.find();

            if (!refDestMgr.isEmpty()) {
                JOReferenceDestination refDest = (JOReferenceDestination) refDestMgr.getFirstEntity();

                LIFormulesDetailViewBean formule = new LIFormulesDetailViewBean();
                formule.setISession(getISession());
                formule.setId(refDest.getIdCleReferenceDestination());
                formule.retrieve();

                LIComplementFormuleActionManager compMgr = new LIComplementFormuleActionManager();
                compMgr.setSession((BSession) getISession());
                compMgr.setForIdFormule(formule.getFormulePDF().getIdFormule());
                compMgr.setForCsTypeFormule(ILIConstantesExternes.CS_ACTION_FORMULE_GRP);
                compMgr.find();

                if (!compMgr.isEmpty()) {

                    LIComplementFormuleAction compForm = (LIComplementFormuleAction) compMgr.getFirstEntity();

                    int action = Integer.parseInt(compForm.getCsValeur());

                    switch (action) {
                        case ILIConstantesExternes.CS_ACTION_REC_STANDARD_INT:

                            majJournalisationReception();
                            break;

                        // // A faire lors de la mise en place de Web@PC
                        //
                        // case ILIConstantes.CS_ACTION_MAJ_DOSSIER_PC_INT:
                        //
                        // // Mise à jour du dossier PC
                        //
                        // break;

                        default:
                            break;
                    }

                }

                // Si l'échéance n'est pas liée à une formule
            } else {
                majJournalisationReception();
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
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
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return true;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_RECEPTION");
    }

    public String getIdJournalisation() {
        return idJournalisation;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    protected void majJournalisationReception() throws Exception {

        // Processus de réception d'une échéance

        // 0) Retrieve complet de l'échéance
        LIJournalisationsDetailViewBean joDetail = new LIJournalisationsDetailViewBean();
        joDetail.setISession(getISession());
        joDetail.setId(getIdJournalisation());
        joDetail.retrieve();

        // 1) Mise à jour du complémentJournal
        joDetail.getComplementJournal().setValeurCodeSysteme(JOConstantes.CS_JO_FMT_MANUELLE_RECEPTION);

        // 2) Modification du groupeJournal
        joDetail.getGroupeJournal().setDateRappel("");
        joDetail.getGroupeJournal().setDateReception(JACalendar.todayJJsMMsAAAA());

        // 3) Modification de la journalisation
        joDetail.getJournalisation().setDate(JACalendar.todayJJsMMsAAAA());
        joDetail.getJournalisation().setIdUtilisateur(getSession().getUserId());
        joDetail.getJournalisation().setCsTypeJournal(JOConstantes.CS_JO_JOURNALISATION);
        joDetail.setDateRappel("");
        joDetail.setLibelle(joDetail.getJournalisation().getLibelle());

        if (!JadeStringUtil.isBlankOrZero(joDetail.getJournalisation().getIdInitial())) {
            // 4) Reprise la journalisation initiale pour mise à jour du libellé
            LIJournalisationsDetailViewBean joPere = new LIJournalisationsDetailViewBean();
            joPere.setISession(getSession());
            joPere.setId(joDetail.getJournalisation().getIdInitial());
            joPere.retrieve();

            joDetail.getJournalisation().setLibelle(joPere.getLibelle());
        }

        // 5) Update
        joDetail.update();

    }

    public void setIdJournalisation(String idJournalisation) {
        this.idJournalisation = idJournalisation;
    }

}