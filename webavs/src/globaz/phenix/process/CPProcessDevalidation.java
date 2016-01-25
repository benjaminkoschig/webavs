package globaz.phenix.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.musca.db.facturation.FAPassage;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.toolbox.CPToolBox;

/**
 * Dévalide une décision - Enlève l'état validation et remet l'idPassage à blanc Ne peut se faire que si la décision
 * n'est pas en état "facturé" ou "reprise" Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public final class CPProcessDevalidation extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String descriptionTiers = "";
    private java.lang.String idDecision = "";
    private java.lang.String idTiers = "";

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessDevalidation() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessDevalidation(BProcess parent) {
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
        if (JadeStringUtil.isIntegerEmpty(getIdDecision())) {
            getMemoryLog().logMessage("5032", null, FWMessage.FATAL, getClass().getName());
            return false;
        }
        // Sous controle d'exceptions
        try {
            // Remise du passage à l'état "ouvert"
            CPDecision decis = new CPDecision();
            // Rechercher les données de la décision
            decis.setSession(getSession());
            decis.setIdDecision(getIdDecision());
            decis.retrieve(getTransaction());
            if (decis.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), decis.getClass().getName());
                getMemoryLog().logMessage("5029", null, FWMessage.FATAL, getClass().getName());
                return false;
            } else {
                if (Integer.parseInt((decis.getAnneeDecision())) <= JACalendar.getYear(JACalendar.today().toString())) { // PO
                    // 4563
                    FAPassage passage = new FAPassage();
                    passage.setSession(getSession());
                    passage.setIdPassage(decis.getIdPassage());
                    passage.retrieve(getTransaction());
                    if (!passage.isNew()) {
                        if ((passage.isEstVerrouille().equals(new Boolean(true)))
                                || (passage.getStatus().equalsIgnoreCase(FAPassage.CS_ETAT_COMPTABILISE))) {
                            getMemoryLog().logMessage(
                                    getSession().getLabel("CP_MSG_0161") + " " + passage.getLibelle()
                                            + getSession().getLabel("CP_MSG_0161A"), FWMessage.FATAL,
                                    decis.getIdDecision());
                            return false;
                        }
                    }
                }
                // Ne pas traiter si la décision à l'état facturer ou reprise
                // Aller rechercher le dernier état
                if ((!CPDecision.CS_FACTURATION.equalsIgnoreCase(decis.getDernierEtat()))
                        && (!CPDecision.CS_REPRISE.equalsIgnoreCase(decis.getDernierEtat()))
                        && (!CPDecision.CS_PB_COMPTABILISATION.equalsIgnoreCase(decis.getDernierEtat()))
                        && (!CPDecision.CS_SORTIE.equalsIgnoreCase(decis.getDernierEtat()))) {
                    // Traitement
                    // Recherche données du tiers pour info message
                    AFAffiliation affiliation = new AFAffiliation();
                    affiliation.setSession(getSession());
                    affiliation.setIdTiers(decis.getIdTiers());
                    affiliation.retrieve(getTransaction());
                    if (affiliation.hasErrors()) {
                        getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0162") + " ", FWMessage.FATAL,
                                decis.getIdTiers());
                        return false;
                    } else {
                        setIdTiers(decis.getIdTiers());
                        setDescriptionTiers(decis.loadAffiliation().getAffilieNumero() + " "
                                + decis.loadTiers().getNom());
                        // Partager le log
                        // decision.setMemoryLog(getMemoryLog());
                        try {
                            // Suppression entete facture et ligne de facture
                            // (afact)
                            if (Integer.parseInt((decis.getAnneeDecision())) <= JACalendar.getYear(JACalendar.today()
                                    .toString())) {
                                CPToolBox.suppressionFacture(getSession(), getTransaction(), decis);
                            }

                            if (getTransaction().hasErrors()) {
                                getMemoryLog().logMessage(getTransaction().getErrors().toString(), FWMessage.FATAL,
                                        CPToolBox.class.getName());
                                sendEmail(getTransaction().getErrors().toString());
                                return false;
                            }
                            // Remettre l'idPassage à blanc et l'état à calcul
                            decis.setDernierEtat(CPDecision.CS_CALCUL);
                            decis.setIdPassage("");
                            decis.setMiseEnGEDValidationRetour(Boolean.FALSE);
                            decis.update(getTransaction());
                            // Mettre état généré dans la communication fiscale
                            // retour
                            if (!JadeStringUtil.isIntegerEmpty(decis.getIdCommunication())) {
                                CPCommunicationFiscaleRetourViewBean comRetour = new CPCommunicationFiscaleRetourViewBean();
                                comRetour.setSession(getSession());
                                comRetour.setIdRetour(decis.getIdCommunication());
                                comRetour.retrieve(getTransaction());
                                if (!comRetour.isNew()) {
                                    if (JadeStringUtil.isEmpty(comRetour.getIdLog())) {
                                        comRetour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE);
                                    } else {
                                        comRetour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_A_CONTROLER);
                                    }
                                    comRetour.update(getTransaction());
                                    // Mise à jour du journal
                                    CPJournalRetour jrnRetour = new CPJournalRetour();
                                    jrnRetour.setSession(getSession());
                                    jrnRetour.setIdJournalRetour(comRetour.getIdJournalRetour());
                                    jrnRetour.retrieve(getTransaction());
                                    if (!jrnRetour.isNew()) {
                                        jrnRetour.update(getTransaction());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            getMemoryLog().logMessage(
                                    getSession().getLabel("CP_MSG_0163") + " " + decis.getAnneeDecision()
                                            + getSession().getLabel("CP_MSG_0135A") + " "
                                            + affiliation.getAffilieNumero() + " - "
                                            + decis.loadTiers().getDescriptionTiers(), FWMessage.FATAL,
                                    decis.getIdDecision());
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, getClass().getName());
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
            obj = getSession().getLabel("CP_MSG_0163") + " " + getIdDecision() + " "
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
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 10:57:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private void sendEmail(String emailContent) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(emailContent)) {
            JadeSmtpClient.getInstance().sendMail(JadeSmtpClient.getInstance().getSenderEmailAddress(),
                    getEMailAddress(), getEMailObject(), emailContent, null);
        }

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
    public void setIdDecision(java.lang.String newIdDecision) {
        idDecision = newIdDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 10:57:26)
     * 
     * @param newIdTiers
     *            java.lang.String
     */
    public void setIdTiers(java.lang.String newIdTiers) {
        idTiers = newIdTiers;
    }

}
