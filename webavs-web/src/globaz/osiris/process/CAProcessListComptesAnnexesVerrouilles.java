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
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.print.list.CAListComptesAnnexesVerrouilles;

/**
 * @author jts 23 mars 05 10:31:12
 */
public class CAProcessListComptesAnnexesVerrouilles extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdGenreCompte = new String();
    private String forSelectionRole = new String();
    private String forSelectionTri = new String();

    public CAProcessListComptesAnnexesVerrouilles() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CACompteAnnexeManager manager = new CACompteAnnexeManager();
            manager.setSession(getSession());

            manager.setForSelectionTri(getForSelectionTri());
            manager.setForSelectionRole(getForSelectionRole());
            manager.setForIdGenreCompte(getForIdGenreCompte());
            manager.setVerrouille(true);
            manager.find();

            // Création du document
            CAListComptesAnnexesVerrouilles excelDoc = new CAListComptesAnnexesVerrouilles(getSession());
            excelDoc.setForSelectionTri(getForSelectionTri());
            excelDoc.setForSelectionRole(getForSelectionRole());
            excelDoc.setForIdGenreCompte(getForIdGenreCompte());
            excelDoc.setDocumentInfo(createDocumentInfo());
            excelDoc.setProcessAppelant(this);
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
            return getSession().getLabel("CAV_SUJETMAIL_KO");
        } else {
            return getSession().getLabel("CAV_SUJETMAIL_OK");
        }
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @return
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @return
     */
    public String getForSelectionTri() {
        return forSelectionTri;
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

    /**
     * @param string
     */
    public void setForIdGenreCompte(String string) {
        forIdGenreCompte = string;
    }

    /**
     * @param string
     */
    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * @param string
     */
    public void setForSelectionTri(String string) {
        forSelectionTri = string;
    }

}
