package globaz.osiris.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAOperation;

/**
 * Insérez la description du type ici. Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public class CAProcessExtournerOperation extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String comment = new String();
    private java.lang.String idOperation = new String();
    private CAOperation operation = null;

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CAProcessExtournerOperation() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessExtournerOperation(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        // Vérifier le journal
        if (JadeStringUtil.isIntegerEmpty(getIdOperation()) && (operation == null)) {
            getMemoryLog().logMessage("EXTOURNE_OPERATION_ID_NOT_FOUND", null, FWMessage.FATAL,
                    this.getClass().getName());
            return false;
        }

        // Sous controle d'exceptions
        try {

            // Instancier un journal
            operation = getOperation();

            // Sortie si abort
            if (isAborted()) {
                return false;
            }

            operation.extourner(getTransaction(), null, getComment());

            // Sortie si abort
            if (isAborted()) {
                return false;
            }

            // Récupérer les exceptions
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            e.printStackTrace();
        }

        // Fin de la procédure
        return !isOnError();
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        super._validate();

        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
            } else {
                setSendCompletionMail(true);
            }
            setControleTransaction(getTransaction() == null);
        }

        if (getSession().hasErrors()) {
            abort();
        }

    }

    /**
     * Returns the comment.
     * 
     * @return String
     */
    public String getComment() {
        return comment;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {

        // Déterminer l'objet du message en fonction du code erreur
        String obj;

        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("EXTOURNE_OPERATION_MAIL_ERROR") + " " + getIdOperation();
        } else {
            obj = getSession().getLabel("EXTOURNE_OPERATION_MAIL_OK") + " " + getIdOperation();
        }

        // Restituer l'objet
        return obj;

    }

    /**
     * Returns the idOperation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdOperation() {
        return idOperation;
    }

    public CAOperation getOperation() {
        // Si pas déjà chargé
        if (operation == null) {
            try {
                CAOperation tmp = new CAOperation();
                tmp.setSession(getSession());
                tmp.setIdOperation(getIdOperation());
                tmp.retrieve(getTransaction());
                if ((tmp == null) || tmp.isNew()) {
                    getMemoryLog().logMessage("EXTOURNE_OPERATION_OPER_NOT_FOUND", getIdOperation(), FWMessage.FATAL,
                            this.getClass().getName());
                    operation = null;
                } else {
                    operation = tmp.getOperationFromType(getTransaction());
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                operation = null;
            }
        }
        return operation;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * Sets the comment.
     * 
     * @param comment
     *            The comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Sets the idOperation.
     * 
     * @param idOperation
     *            The idOperation to set
     */
    public void setIdOperation(java.lang.String idSection) {
        idOperation = idSection;
    }

}
