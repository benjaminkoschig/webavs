/*
 * Créé le 23 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.print.list.CAListTaxesSommationEnSuspens;

/**
 * @author jts 23 mars 05 10:31:12
 */
public class CAProcessListTaxesSommationEnSuspens extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAProcessListTaxesSommationEnSuspens() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CASectionManager manager = new CASectionManager();
            manager.setSession(getSession());
            manager.setForSelectionSections("1"); // différent de 0
            manager.setForSoldeEgalTaxes(true);
            manager.find();

            // Création du document
            CAListTaxesSommationEnSuspens excelDoc = new CAListTaxesSommationEnSuspens(getSession());
            excelDoc.setProcessAppelant(this);
            excelDoc.setDocumentInfo(createDocumentInfo());
            excelDoc.populateSheetListe(manager, getTransaction());

            this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {
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
            return getSession().getLabel("TAXES_SOMMATION_SUSPENS_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("TAXES_SOMMATION_SUSPENS_SUJETMAIL_OK");
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
