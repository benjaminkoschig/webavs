package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.common.Jade;
import globaz.osiris.application.CAApplication;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public abstract class CAAbstractListProcess extends BProcess implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    FWIDocumentInterface _doc = null;
    private boolean m_batch = true;
    private boolean m_deleteOnServerRestart = true;
    private String m_fileName = "ListComptabiliteAux";
    private String m_path = Jade.getInstance().getHomeDir() + "/" + CAApplication.DEFAULT_OSIRIS_ROOT + "/work/";

    /**
     * Constructor for CAListProcess.
     */
    public CAAbstractListProcess() {
        super();
    }

    /**
     * Constructor for CAListProcess.
     * 
     * @param parent
     */
    public CAAbstractListProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Constructor for CAListProcess.
     * 
     * @param session
     */
    public CAAbstractListProcess(BSession session) {
        super(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected final boolean _executeProcess() {
        try {
            _doc = getDocument();
            _doc.execute();
            // ALD : pas besoin d'attacher le fichier généré, le process le fait
            // déjà
            // super.registerAttachedDocument(_doc.getExporter().getExportNewFilePath());
            return true;
        } catch (Exception e) {
            super.setMsgType(FWViewBeanInterface.ERROR);
            super.setMessage("Echec de l'impression");
            getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
            getMemoryLog().logMessage("5504", null, FWMessage.FATAL, this.getClass().getName());
        }
        return false;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:44:29)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate() throws java.lang.Exception {
        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError("Le champ email doit être renseigné.");
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
    }

    abstract FWIDocumentInterface getDocument();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected final String getEMailObject() {
        String documentTitle = (_doc != null) ? _doc.getDocumentTitle() : "";
        StringBuffer buffer = new StringBuffer("L'impression du document " + documentTitle);
        if (isOnError()) {
            buffer.append(" s'est terminée en erreur");
        } else {
            buffer.append(" s'est terminée avec succès");
        }
        return buffer.toString();
    }

    /**
     * Returns the fileName.
     * 
     * @return String
     */
    public String getFileName() {
        return m_fileName;
    }

    /**
     * Returns the path.
     * 
     * @return String
     */
    public String getPath() {
        return m_path;
    }

    /**
     * Returns the batch.
     * 
     * @return boolean
     */
    public boolean isBatch() {
        return m_batch;
    }

    /**
     * Returns the deleteOnServerRestart.
     * 
     * @return boolean
     */
    public boolean isDeleteOnServerRestart() {
        return m_deleteOnServerRestart;
    }

    /**
     * Sets the batch.
     * 
     * @param batch
     *            The batch to set
     */
    public void setBatch(boolean batch) {
        m_batch = batch;
    }

    /**
     * Sets the deleteOnServerRestart.
     * 
     * @param deleteOnServerRestart
     *            The deleteOnServerRestart to set
     */
    public void setDeleteOnServerRestart(boolean deleteOnServerRestart) {
        m_deleteOnServerRestart = deleteOnServerRestart;
    }

    /**
     * Sets the fileName.
     * 
     * @param fileName
     *            The fileName to set
     */
    public void setFileName(String fileName) {
        m_fileName = fileName;
    }

    /**
     * Sets the path.
     * 
     * @param path
     *            The path to set
     */
    public void setPath(String path) {
        m_path = path;
    }

}
