package globaz.hercule.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.itext.controleEmployeur.CELettreProchainControle_Doc;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author hpe
 * @since Créé le 14 févr. 07
 * @revision SCO 13 déc. 2010
 */
public class CELettreProchainControleProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CEControleEmployeur controle;
    private String dateHeure = new String();
    private String idControle = new String();
    private String idDocument = new String();
    private String idDocumentDefaut = new String();
    private String reviseur = "";

    /**
     * Constructeur de CELettreProchainControleProcess
     */
    public CELettreProchainControleProcess() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {

        boolean status = true;

        try {

            // Création du document
            CELettreProchainControle_Doc doc = new CELettreProchainControle_Doc(getSession());

            doc.setIdDocument(getIdDocument());
            doc.setIdDocumentDefaut(getIdDocumentDefaut());
            doc.setIdControleEmployeur(getIdControle());
            doc.setReviseur(getReviseur());
            doc.setDateHeure(getDateHeure());
            doc.setParent(this);
            doc.executeProcess();

        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("IMPRESSION_PROCHAIN_CONTROLE_ERREUR"));

            String messageInformation = "\n";
            messageInformation += "idControle : " + getIdControle() + "\n";
            messageInformation += "IdDocument : " + getIdDocument() + "\n";
            messageInformation += "dateHeure : " + getDateHeure() + "\n";
            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            status = false;
        }

        return status;
    }

    /**
     * Retourne le controle suivant l'identifiant d'un controle
     * 
     * @return
     */
    public CEControleEmployeur _getControle() {
        if (controle == null) {
            controle = new CEControleEmployeur();
            controle.setSession(getSession());
            controle.setIdControleEmployeur(getIdControle());
            try {
                controle.retrieve();
            } catch (Exception e) { // TODO SCO : Traduction
                this._addError("Erreur lors du retriev du controle dans CELettreProchainControleProcess: "
                        + e.getMessage());
            }
        }
        return controle;
    }

    public String _getReviseurPreRempli() {
        return _getControle().getControleurNom();
    }

    public String getDateHeure() {
        if (JadeStringUtil.isBlank(dateHeure)) {
            dateHeure = JACalendar.format(_getControle().getDatePrevue(), _getControle().getLangueTiers());
        }
        return dateHeure;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("OBJECT_PROCESS_IMPRESSION_ERREUR");
        } else {
            return null;
        }
    }

    public String getIdControle() {
        return idControle;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdDocumentDefaut() {
        return idDocumentDefaut;
    }

    public String getReviseur() {
        return reviseur;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    // *******************************************************
    // Setter
    // *******************************************************

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
