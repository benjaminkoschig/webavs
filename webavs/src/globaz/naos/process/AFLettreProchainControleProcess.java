/*
 * Créé le 14 févr. 07
 */
package globaz.naos.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFReviseur;
import globaz.naos.itext.controleEmployeur.AFLettreProchainControle_Doc;

/**
 * @author hpe
 * 
 */

public class AFLettreProchainControleProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFControleEmployeur controle;
    private String dateHeure = new String();
    private String idControle = new String();
    private String idDocument = new String();
    private String idDocumentDefaut = new String();
    private String reviseur = new String();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public AFLettreProchainControleProcess() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {

            // Création du document
            AFLettreProchainControle_Doc doc = new AFLettreProchainControle_Doc(getSession());

            doc.setIdDocument(getIdDocument());
            doc.setIdDocumentDefaut(getIdDocumentDefaut());
            doc.setIdControleEmployeur(getIdControle());
            doc.setReviseur(getReviseur());
            doc.setDateHeure(getDateHeure());
            doc.setParent(this);
            // doc.setParentWithCopy(this);
            // doc.executeProcess();
            // BProcessLauncher.start(doc);
            doc.executeProcess();

            return true;
        } catch (Exception e) {
            abort();
            e.printStackTrace();
            return false;
        }
    }

    public AFControleEmployeur _getControle() {
        if (controle == null) {
            controle = new AFControleEmployeur();
            controle.setSession(getSession());
            controle.setControleEmployeurId(getIdControle());
            try {
                controle.retrieve();
            } catch (Exception e) {
                this._addError("Erreur lors du retriev du controle dans AFLettreProchainControleProcess: "
                        + e.getMessage());
            }
        }
        return controle;
    }

    public String getDateHeure() {
        if (JadeStringUtil.isBlank(dateHeure)) {
            dateHeure = JACalendar.format(_getControle().getDatePrevue(), JACalendar.FORMAT_DMMMYYYY);
        }
        return dateHeure;
    }

    // protected void _validate() throws Exception {
    // setControleTransaction(true);
    // setSendCompletionMail(true);
    // setSendMailOnError(true);
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        // if (isAborted() || getSession().hasErrors()) {
        // return
        // getSession().getLabel("NAOS_EMAIL_OBJECT_CONTROLES_PROCHAIN_ERROR");
        // } else {
        // return getSession().getLabel("NAOS_EMAIL_OBJECT_CONTROLES_PROCHAIN");
        // }
        return null;
    }

    public String getIdControle() {
        return idControle;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdDocumentDefaut() {
        return idDocumentDefaut;
    }

    public String getReviseur() {
        if (JadeStringUtil.isBlank(reviseur)) {
            AFReviseur rev = _getControle().getReviseur();
            if (rev != null) {
                reviseur = rev.getNomReviseur();
            }
        }
        return reviseur;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setDateHeure(String dateHeure) {
        this.dateHeure = dateHeure;
    }

    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdDocumentDefaut(String idDocumentDefaut) {
        this.idDocumentDefaut = idDocumentDefaut;
    }

    public void setReviseur(String reviseur) {
        this.reviseur = reviseur;
    }

}
