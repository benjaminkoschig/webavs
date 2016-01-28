package globaz.libra.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.journalisation.db.journalisation.access.JOReferenceDestination;
import globaz.journalisation.db.journalisation.access.JOReferenceDestinationManager;
import globaz.libra.vb.formules.LIFormulesDetailViewBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import ch.globaz.libra.constantes.ILIConstantesExternes;

public class LIDocumentsProcess extends BProcess {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDocuments = new String();
    private Collection idsJournalisation = new ArrayList();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIDocumentsProcess.
     */
    public LIDocumentsProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe LIDocumentsProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public LIDocumentsProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe LIDocumentsProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public LIDocumentsProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        for (Iterator iterator = getIdsJournalisation().iterator(); iterator.hasNext();) {
            String idJO = (String) iterator.next();

            JOReferenceDestinationManager refDestMgr = new JOReferenceDestinationManager();
            refDestMgr.setSession(getSession());
            refDestMgr.setForIdJournalisation(idJO);
            refDestMgr.setForTypeReferenceDestination(ILIConstantesExternes.CS_TYPE_FORM_PRESTATIONS);
            refDestMgr.find();

            if (!refDestMgr.isEmpty()) {
                JOReferenceDestination refDest = (JOReferenceDestination) refDestMgr.getFirstEntity();

                LIFormulesDetailViewBean formule = new LIFormulesDetailViewBean();
                formule.setISession(getISession());
                formule.setId(refDest.getIdCleReferenceDestination());
                formule.retrieve();

                // TODO Exécution du process de chaque formule PDF

            }

        }

        return false;
    }

    public String getDateDocuments() {
        return dateDocuments;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("PROCESS_DOCUMENTS");
    }

    public Collection getIdsJournalisation() {
        return idsJournalisation;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setDateDocuments(String dateDocuments) {
        this.dateDocuments = dateDocuments;
    }

    public void setIdsJournalisation(Collection idsJournalisation) {
        this.idsJournalisation = idsJournalisation;
    }

}
