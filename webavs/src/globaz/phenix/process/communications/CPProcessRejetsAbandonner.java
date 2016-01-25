package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPRejets;

/**
 * D�valide une d�cision - Enl�ve l'�tat validation et remet l'idPassage � blanc Ne peut se faire que si la d�cision
 * n'est pas en �tat "factur�" ou "reprise" Date de cr�ation : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public final class CPProcessRejetsAbandonner extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String descriptionTiers = "";
    private java.lang.String idRejet = "";

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessRejetsAbandonner() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessRejetsAbandonner(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage apr�s erreur ou ex�cution Date de cr�ation : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Calcul des montants de cotisation Date de cr�ation : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        // V�rifier la d�cision
        if (JadeStringUtil.isIntegerEmpty(getIdRejet())) {
            getMemoryLog().logMessage("5032", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }
        // Sous controle d'exceptions
        try {
            // Remise du passage � l'�tat "ouvert"
            CPRejets rejet = new CPRejets();
            // Rechercher les donn�es de la d�cision
            rejet.setSession(getSession());
            rejet.setIdRejets(getIdRejet());
            rejet.retrieve(getTransaction());
            if (rejet.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), rejet.getClass().getName());
                getMemoryLog().logMessage("5029", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            } else {
                setDescriptionTiers(rejet.getIdRejets() + ": " + rejet.getNumContribuable() + " - "
                        + rejet.getNomReference());
                // Ne pas traiter si le rejet est � l'�tat envoy�
                if (!CPRejets.CS_ETAT_ENVOYE.equalsIgnoreCase(rejet.getEtat())) {
                    rejet.setEtat(CPRejets.CS_ETAT_ABANDONNE);
                    rejet.update(getTransaction());
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } // Fin de la proc�dure
        return !isOnError();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 10:59:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDescriptionTiers() {
        return descriptionTiers;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {

        // D�terminer l'objet du message en fonction du code erreur
        String obj = "";

        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("CP_MSG_0163") + " " + getIdRejet() + " "
                    + getSession().getLabel("CP_MSG_0135A") + " " + getDescriptionTiers();
            // else
            // obj = FWMessage.getMessageFromId("5030")+ " " + getIdDecision();
        }

        // Restituer l'objet
        return obj;

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (25.02.2002 13:55:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdRejet() {
        return idRejet;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 10:59:30)
     * 
     * @param newDescriptionTiers
     *            java.lang.String
     */
    public void setDescriptionTiers(java.lang.String newDescriptionTiers) {
        descriptionTiers = newDescriptionTiers;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (25.02.2002 13:55:43)
     * 
     * @param newIdJournal
     *            java.lang.String
     */
    public void setIdRejet(java.lang.String newIdDecision) {
        idRejet = newIdDecision;
    }

}
