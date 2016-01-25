package globaz.osiris.process.journal;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAJournal;

/**
 * Insérez la description du type ici. Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public class CAProcessAnnulerJournal extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJournal = new String();
    private CAJournal journal = null;

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CAProcessAnnulerJournal() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessAnnulerJournal(BProcess parent) {
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
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            getMemoryLog().logMessage("5032", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // Sous controle d'exceptions
        try {

            // Instancier un journal
            journal = new CAJournal();

            // Charger l'ordre
            journal.setSession(getSession());
            journal.setIdJournal(getIdJournal());
            journal.retrieve(getTransaction());
            if (journal.isNew() || journal.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), journal.getClass().getName());
                getMemoryLog().logMessage("5029", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // Partager le log
            journal.setMemoryLog(getMemoryLog());

            // Sortie si abort
            if (isAborted()) {
                return false;
            }

            new CAAnnulerJournal().annuler(this, journal);

            // Vérifier les erreurs
            if (journal.isNew() || journal.hasErrors() || getTransaction().hasErrors()) {
                getMemoryLog().logMessage("5029", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // Sortie si abort
            if (isAborted()) {
                return false;
            }

            journal.update(getTransaction());

            // Vérifier les erreurs
            if (journal.isNew() || journal.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), journal.getClass().getName());
                getMemoryLog().logMessage("5029", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // Récupérer les exceptions
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        // Fin de la procédure
        return !isOnError();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {

        // Déterminer l'objet du message en fonction du code erreur
        String obj;

        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("5031") + " " + getIdJournal();
        } else {
            obj = getSession().getLabel("5030") + " " + getIdJournal();
        }

        // Restituer l'objet
        return obj;

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 13:55:43)
     * 
     * @return String
     */
    public String getIdJournal() {
        return idJournal;
    }

    public CAJournal getJournal() {
        // Si pas déjà chargé
        if (journal == null) {
            try {
                journal = new CAJournal();
                journal.setSession(getSession());
                journal.setIdJournal(getIdJournal());
                journal.retrieve(getTransaction());
                if (journal.isNew() || journal.hasErrors()) {
                    getMemoryLog().logMessage("5157", getIdJournal(), FWMessage.FATAL, this.getClass().getName());
                    journal = null;
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                journal = null;
            }
        }

        // Forcer la transaction
        if (getTransaction() != null) {
            journal.setSession(getSession());
        }

        return journal;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 13:55:43)
     * 
     * @param newIdJournal
     *            String
     */
    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }
}
