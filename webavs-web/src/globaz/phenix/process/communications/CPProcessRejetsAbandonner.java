package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPRejets;

/**
 * Dévalide une décision - Enlève l'état validation et remet l'idPassage à blanc Ne peut se faire que si la décision
 * n'est pas en état "facturé" ou "reprise" Date de création : (25.02.2002 13:41:13)
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
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessRejetsAbandonner(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Calcul des montants de cotisation Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        // Vérifier la décision
        if (JadeStringUtil.isIntegerEmpty(getIdRejet())) {
            getMemoryLog().logMessage("5032", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }
        // Sous controle d'exceptions
        try {
            // Remise du passage à l'état "ouvert"
            CPRejets rejet = new CPRejets();
            // Rechercher les données de la décision
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
                // Ne pas traiter si le rejet est à l'état envoyé
                if (!CPRejets.CS_ETAT_ENVOYE.equalsIgnoreCase(rejet.getEtat())) {
                    rejet.setEtat(CPRejets.CS_ETAT_ABANDONNE);
                    rejet.update(getTransaction());
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } // Fin de la procédure
        return !isOnError();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 10:59:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDescriptionTiers() {
        return descriptionTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {

        // Déterminer l'objet du message en fonction du code erreur
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
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 13:55:43)
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
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 10:59:30)
     * 
     * @param newDescriptionTiers
     *            java.lang.String
     */
    public void setDescriptionTiers(java.lang.String newDescriptionTiers) {
        descriptionTiers = newDescriptionTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 13:55:43)
     * 
     * @param newIdJournal
     *            java.lang.String
     */
    public void setIdRejet(java.lang.String newIdDecision) {
        idRejet = newIdDecision;
    }

}
