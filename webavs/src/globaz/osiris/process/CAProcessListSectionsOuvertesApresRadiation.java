package globaz.osiris.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.db.sections.CASectionsOuvertesApresRadiationManager;
import globaz.osiris.print.list.parser.CAListSectionsOuvertesApresRadiationParser;

/**
 * @author sel Créé le 19 sept. 06
 */
public class CAProcessListSectionsOuvertesApresRadiation extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCategorie;
    private String forSelectionRole;
    private String forSelectionSequence;

    public CAProcessListSectionsOuvertesApresRadiation() {
        super();
    }

    public CAProcessListSectionsOuvertesApresRadiation(BSession session) {
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CASectionsOuvertesApresRadiationManager manager = new CASectionsOuvertesApresRadiationManager();
            manager.setForSelectionRole(getForSelectionRole());
            manager.setForIdCategorie(getForIdCategorie());
            manager.setForSelectionSequence(getForSelectionSequence());

            // Création du document
            if (!isAborted()) {
                CAListSectionsOuvertesApresRadiationParser excelDoc = new CAListSectionsOuvertesApresRadiationParser(
                        getSession(), this);
                excelDoc.setDocumentInfo(createDocumentInfo());
                excelDoc.populateSheetListe(manager, getTransaction());

                if (!isAborted()) {
                    this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
                }
            }
        } catch (Exception e) {
            this._addError(e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            return false;
        }
        if (isAborted()) {
            return false;
        }
        return true;
    }

    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("IMPRESSION_LISTE_SECTIONS_OUVERTES_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_LISTE_SECTIONS_OUVERTES_OK");
        }
    }

    public String getForIdCategorie() {
        return forIdCategorie;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    public String getForSelectionSequence() {
        return forSelectionSequence;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

    public void setForSelectionSequence(String forSelectionSequence) {
        this.forSelectionSequence = forSelectionSequence;
    }

}
