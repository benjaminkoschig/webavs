package globaz.osiris.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.db.controleConcordanceSoldes.CAControleConcordanceSoldeCASEExcelManager;
import globaz.osiris.db.controleConcordanceSoldes.CAControleConcordanceSoldeSEOPExcelManager;
import globaz.osiris.print.list.CAListControleConcordanceSoldesExcel;

/**
 * @author sch
 */
public class CAProcessListControleConcordanceSoldesExcel extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAProcessListControleConcordanceSoldesExcel() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {

            CAControleConcordanceSoldeCASEExcelManager managerCompteAnnexeSections = new CAControleConcordanceSoldeCASEExcelManager();
            managerCompteAnnexeSections.setSession(getSession());
            managerCompteAnnexeSections.find();

            CAControleConcordanceSoldeSEOPExcelManager managerSectionsOperations = new CAControleConcordanceSoldeSEOPExcelManager();
            managerSectionsOperations.setSession(getSession());
            managerSectionsOperations.find();

            if (isAborted()) {
                return false;
            }

            setProgressScaleValue(managerCompteAnnexeSections.size());
            // Création du document
            CAListControleConcordanceSoldesExcel excelDoc = new CAListControleConcordanceSoldesExcel(getSession(), this);
            excelDoc.setDocumentInfo(createDocumentInfo());
            setProgressScaleValue(managerCompteAnnexeSections.size());
            excelDoc.populateSheetListe(managerCompteAnnexeSections, getTransaction());
            setProgressScaleValue(managerSectionsOperations.size());
            excelDoc.populateSheetListe(managerSectionsOperations, getTransaction());

            if (!isAborted()) {
                this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
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
            return getSession().getLabel("LISTE_CONTROLE_CONCORDANCE_SOLDE_EXCEL_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("LISTE_CONTROLE_CONCORDANCE_SOLDE_EXCEL_SUJETMAIL_OK");
        }
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
}
