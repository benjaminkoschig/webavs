package globaz.osiris.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.ordres.CAOrdreGroupe;

/**
 * G�rer l'attachement d'un ordre de Date de cr�ation : (22.02.2002 09:08:40)
 * 
 * @author: Administrator
 * @revision SCO 19 mars 2010
 */
public class CAProcessAttacherOrdre extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJournalSource = null;
    private String idOrdreGroupe = "";
    private CAOrdreGroupe ordreGroupe = null;

    /**
     * Commentaire relatif au constructeur CAProcessAttacherOrdre.
     */
    public CAProcessAttacherOrdre() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2002 11:11:15)
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessAttacherOrdre(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage apr�s erreur ou ex�cution Date de cr�ation : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        // V�rifier l'ordre group�
        if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            getMemoryLog().logMessage("5200", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // Sous controle d'exceptions
        try {

            // Charger l'ordre
            ordreGroupe = getOrdreGroupe();
            if (ordreGroupe == null) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), ordreGroupe.getClass().getName());
                getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // Partager le log
            ordreGroupe.setMemoryLog(getMemoryLog());

            // Indiquer que l'on d�marre la cr�ation d'un ordre
            ordreGroupe.beforeExecuteAttacherOrdre(this);

            // Sortie si abort
            if (isAborted()) {
                return false;
            }

            // Mise � jour
            ordreGroupe.update(getTransaction());
            if (ordreGroupe.isNew() || ordreGroupe.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), ordreGroupe.getClass().getName());
                getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // Sortie si abort
            if (isAborted()) {
                return false;
            }

            // Ex�cuter l'attachement de l'ordre group�
            ordreGroupe.executeAttacherOrdre(this, idJournalSource);
            if (ordreGroupe.isNew() || ordreGroupe.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), ordreGroupe.getClass().getName());
                getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // Sortie si abort
            if (isAborted()) {
                return false;
            }

            // Mise � jour
            ordreGroupe.update(getTransaction());
            if (ordreGroupe.isNew() || ordreGroupe.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), ordreGroupe.getClass().getName());
                getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // R�cup�rer les exceptions
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        // Fin de la proc�dure
        return !isOnError();

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:22:21)
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {

        // D�terminer l'objet du message en fonction du code erreur
        String obj;

        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("5401");
        } else {
            obj = getSession().getLabel("5400");
        }

        // Restituer l'objet
        return obj;

    }

    public String getIdJournalSource() {
        return idJournalSource;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.02.2002 09:21:41)
     * 
     * @return String
     */
    public String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (02.04.2002 11:43:20)
     */
    public CAOrdreGroupe getOrdreGroupe() {
        if (ordreGroupe == null) {
            ordreGroupe = new CAOrdreGroupe();
            ordreGroupe.setISession(getSession());
            ordreGroupe.setIdOrdreGroupe(getIdOrdreGroupe());
            try {
                ordreGroupe.retrieve(getTransaction());
                if (ordreGroupe.isNew()) {
                    ordreGroupe = null;
                }
            } catch (Exception e) {
                ordreGroupe = null;
            }
        }

        return ordreGroupe;
    }

    /**
     * Method jobQueue. Cette m�thode d�finit la nature du traitement s'il s'agit d'un processus qui doit-�tre lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setIdJournalSource(String idJournalSource) {
        this.idJournalSource = idJournalSource;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.02.2002 09:21:41)
     * 
     * @param newIdOrdreGroupe
     *            String
     */
    public void setIdOrdreGroupe(String newIdOrdreGroupe) {
        idOrdreGroupe = newIdOrdreGroupe;
    }
}
