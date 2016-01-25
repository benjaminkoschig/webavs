package globaz.osiris.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.db.sections.CASectionsOuvertesManager;
import globaz.osiris.print.list.parser.CAListSectionsOuvertesParser;

/**
 * @author sel Créé le 19 sept. 06
 */
public class CAProcessListSectionsOuvertes extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDate;
    private String forSelectionRole;

    public CAProcessListSectionsOuvertes() {
        super();
    }

    public CAProcessListSectionsOuvertes(BSession session) {
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CASectionsOuvertesManager manager = new CASectionsOuvertesManager();
            manager.setForSelectionRole(getForSelectionRole());
            manager.setForDate(getForDate());

            // Création du document
            if (!isAborted()) {
                CAListSectionsOuvertesParser excelDoc = new CAListSectionsOuvertesParser(getSession(), this);
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

    public String getForDate() {
        return forDate;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
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

    public void setForDate(String forDateValeur) {
        forDate = forDateValeur;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

}
