package globaz.libra.process;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.journalisation.constantes.JOConstantes;
import globaz.libra.vb.journalisations.LIJournalisationsDetailViewBean;

public class LIAnnulerReceptionProcess extends BProcess {

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
     * Crée une nouvelle instance de la classe LIAnnulerReceptionProcess.
     */
    public LIAnnulerReceptionProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe LIAnnulerReceptionProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public LIAnnulerReceptionProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe LIAnnulerReceptionProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public LIAnnulerReceptionProcess(BSession session) {
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

            // Processus de réception d'une échéance

            // 0) Retrieve complet de l'échéance
            LIJournalisationsDetailViewBean joDetail = new LIJournalisationsDetailViewBean();
            joDetail.setISession(getISession());
            joDetail.setId(getIdJournalisation());
            joDetail.retrieve();

            // 1) Mise à jour du complémentJournal
            joDetail.getComplementJournal().setValeurCodeSysteme(JOConstantes.CS_JO_AVS_FMT_RAPPEL);

            // 2) Modification du groupeJournal
            joDetail.getGroupeJournal().setDateRappel(JACalendar.todayJJsMMsAAAA());
            joDetail.getGroupeJournal().setDateReception("");

            // 3) Modification de la journalisation
            joDetail.getJournalisation().setDate(JACalendar.todayJJsMMsAAAA());
            joDetail.getJournalisation().setIdUtilisateur(getSession().getUserId());
            joDetail.getJournalisation().setCsTypeJournal(JOConstantes.CS_JO_RAPPEL);
            joDetail.setDateRappel(JACalendar.todayJJsMMsAAAA());
            joDetail.setLibelle(joDetail.getJournalisation().getLibelle());

            if (!JadeStringUtil.isBlankOrZero(joDetail.getJournalisation().getIdInitial())) {
                // 4) Reprise la journalisation initiale pour mise à jour du
                // libellé
                LIJournalisationsDetailViewBean joPere = new LIJournalisationsDetailViewBean();
                joPere.setISession(getSession());
                joPere.setId(joDetail.getJournalisation().getIdInitial());
                joPere.retrieve();

                joDetail.getJournalisation().setLibelle(joPere.getLibelle());
            }

            // 5) Update
            joDetail.update();

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
        return getSession().getLabel("PROCESS_ANN_RECEPTION");
    }

    public String getIdJournalisation() {
        return idJournalisation;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setIdJournalisation(String idJournalisation) {
        this.idJournalisation = idJournalisation;
    }

}