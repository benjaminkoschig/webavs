package globaz.osiris.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.ordres.CAOrdreGroupe;

/**
 * Date de création : (29.04.2002 09:09:26)
 * 
 * @author: Administrator
 */
public class CAProcessAnnulerOrdre extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idOrdreGroupe = new String();

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerOrdre.
     */
    public CAProcessAnnulerOrdre() {
        super();
    }

    /**
     * Date de création : (29.04.2002 11:12:00)
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessAnnulerOrdre(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        // TODO Dal if annule do nothign

        // Vérifier l'ordre groupé
        if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
            getMemoryLog().logMessage("5200", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // Sous controle d'exceptions
        try {
            CAOrdreGroupe ordreGroupe = getOrdreGroupe();

            // Charger l'ordre
            if (ordreGroupe == null) {
                getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            ordreGroupe.setEtat(CAOrdreGroupe.TRAITEMENT);

            if (isAborted()) {
                return false;
            }

            // Mise à jour
            ordreGroupe.update(getTransaction());
            if (ordreGroupe.isNew()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), ordreGroupe.getClass().getName());
                getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            ordreGroupe.executeAnnulerOrdre(this);

            if (isAborted()) {
                return false;
            }

            // Mise à jour
            ordreGroupe.update(getTransaction());
            if (ordreGroupe.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), ordreGroupe.getClass().getName());
                getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        return true;

    }

    /**
     * Date de création : (14.02.2002 14:22:21)
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("5409");
        } else {
            return getSession().getLabel("5410");
        }
    }

    /**
     * Date de création : (29.04.2002 09:10:03)
     * 
     * @return String
     */
    public String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    /**
     * Date de création : (02.04.2002 11:43:20)
     */
    public CAOrdreGroupe getOrdreGroupe() {
        CAOrdreGroupe ordreGroupe = new CAOrdreGroupe();
        ordreGroupe.setISession(getSession());
        ordreGroupe.setIdOrdreGroupe(getIdOrdreGroupe());

        try {
            ordreGroupe.retrieve();
            if (ordreGroupe.isNew()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return ordreGroupe;
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

    public void setIdOrdreGroupe(String newIdOrdreGroupe) {
        idOrdreGroupe = newIdOrdreGroupe;
    }
}
