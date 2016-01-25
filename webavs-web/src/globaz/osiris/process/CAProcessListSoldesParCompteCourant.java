package globaz.osiris.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.db.comptecourant.CASoldesParCompteCourantManager;
import globaz.osiris.print.list.parser.CAListSoldesParCompteCourantParser;

/**
 * @author sel Créé le 19 sept. 06
 */
public class CAProcessListSoldesParCompteCourant extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateValeur;
    private String forSelectionRole;

    public CAProcessListSoldesParCompteCourant() {
        super();
    }

    public CAProcessListSoldesParCompteCourant(BSession session) {
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CASoldesParCompteCourantManager manager = new CASoldesParCompteCourantManager();
            manager.setForSelectionRole(getForSelectionRole());
            manager.setForDateValeur(getForDateValeur());

            // Création du document
            CAListSoldesParCompteCourantParser excelDoc = new CAListSoldesParCompteCourantParser(getSession(), this);
            excelDoc.setDocumentInfo(createDocumentInfo());
            excelDoc.populateSheetListe(manager, getTransaction());
            if (isAborted()) {
                return false;
            }
            this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {
            this._addError(e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            return false;
        }
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
            return getSession().getLabel("IMPRESSION_LISTE_SOLDE_PAR_COMPTE_COURANT_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_LISTE_SOLDE_PAR_COMPTE_COURANT_OK");
        }
    }

    public String getForDateValeur() {
        return forDateValeur;
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

    public void setForDateValeur(String forDateValeur) {
        this.forDateValeur = forDateValeur;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

}
