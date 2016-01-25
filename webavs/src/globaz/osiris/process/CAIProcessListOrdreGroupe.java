package globaz.osiris.process;

import globaz.framework.printing.FWDocumentListener;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.print.itext.list.CAIListOrdreGroupe;

/**
 * Cette classe permet de lancer l'impression de la liste des ordres group�s
 * 
 * @author: Administrator
 */
public class CAIProcessListOrdreGroupe extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FWDocumentListener documentListener = new FWDocumentListener();
    private boolean erreur = false;
    private String idOrdreGroupe = new String();
    private String idTypeOperation = new String();
    private CAOrdreGroupe ordreGroupe;
    private String typeImpression = "pdf";

    /**
     * Commentaire relatif au constructeur CAIProcessListOrdreGroupe.
     */
    public CAIProcessListOrdreGroupe() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CAIProcessListOrdreGroupe
     * 
     * @param parent
     *            BProcess
     */
    public CAIProcessListOrdreGroupe(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur CAIProcessListOrdreGroupe.
     */
    public CAIProcessListOrdreGroupe(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage apr�s erreur ou ex�cution
     */
    @Override
    protected void _executeCleanUp() {
        // Terminer le listener de document en cas d'erreur
        if (isOnError() && isErreur()) {
            getDocumentListener().abort();
        }
    }

    /**
     * Execution du processus
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            // Variable pour le document Itext
            CAIListOrdreGroupe documentIt = new CAIListOrdreGroupe(this);
            documentIt.setIdOrdreGroupe(getIdOrdreGroupe());
            documentIt.setIdTypeOperation(getOrdreGroupe().getTypeOrdreGroupe());
            documentIt.bindData(getIdOrdreGroupe());
            documentIt.setEMailAddress(getEMailAddress());
            documentIt.setTypeImpression(getTypeImpression());
            documentIt.executeProcess();
        } catch (Exception e) {
            setErreur(true);
        } finally {
            if (!isAborted()) {
                // Fin de registration
                getDocumentListener().endRegistering();
            } else {
                setErreur(true);
            }
        }
        return isErreur();
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.08.2002 14:04:44)
     * 
     * @return FWDocumentListener
     */
    public FWDocumentListener getDocumentListener() {
        return documentListener;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:22:21)
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {
        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("5501");
        } else {
            return getSession().getLabel("5500");
        }
    }

    /**
     * Returns the idOrdreGroupe.
     * 
     * @return String
     */
    public String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    /**
     * @return
     */
    public String getIdTypeOperation() {
        return idTypeOperation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2002 11:49:57)
     * 
     * @return CAOrdreGroupe
     */
    public CAOrdreGroupe getOrdreGroupe() {
        // Si pas d�j� charg�
        if (ordreGroupe == null) {
            try {
                ordreGroupe = new CAOrdreGroupe();
                ordreGroupe.setSession(getSession());
                ordreGroupe.setIdOrdreGroupe(getIdOrdreGroupe());
                ordreGroupe.retrieve(getTransaction());
                if (ordreGroupe.isNew() || ordreGroupe.hasErrors()) {
                    getMemoryLog().logMessage("5147", getIdOrdreGroupe(), FWMessage.FATAL, this.getClass().getName());
                    ordreGroupe = null;
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                ordreGroupe = null;
            }
        }

        return ordreGroupe;

    }

    public String getTypeImpression() {
        return typeImpression;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.08.2002 14:16:13)
     * 
     * @return boolean
     */
    public boolean isErreur() {
        return erreur;
    }

    /**
     * Method jobQueue. Cette m�thode d�finit la nature du traitement s'il s'agit d'un processus qui doit-�tre lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.08.2002 14:04:44)
     * 
     * @param newDocumentListener
     *            FWDocumentListener
     */
    public void setDocumentListener(FWDocumentListener newDocumentListener) {
        documentListener = newDocumentListener;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.08.2002 14:16:13)
     * 
     * @param newErreur
     *            boolean
     */
    public void setErreur(boolean newErreur) {
        erreur = newErreur;
    }

    /**
     * Sets the idOrdreGroupe.
     * 
     * @param idOrdreGroupe
     *            The idOrdreGroupe to set
     */
    public void setIdOrdreGroupe(String idOrdreGroupe) {
        this.idOrdreGroupe = idOrdreGroupe;
    }

    /**
     * @param string
     */
    public void setIdTypeOperation(String string) {
        idTypeOperation = string;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

}
