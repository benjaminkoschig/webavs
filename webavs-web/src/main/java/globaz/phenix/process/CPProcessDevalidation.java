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
 * D�valide une d�cision - Enl�ve l'�tat validation et remet l'idPassage � blanc Ne peut se faire que si la d�cision
 * n'est pas en �tat "factur�" ou "reprise" Date de cr�ation : (25.02.2002 13:41:13)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessDevalidation(BProcess parent) {
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
        if (JadeStringUtil.isIntegerEmpty(getIdDecision())) {
            getMemoryLog().logMessage("5032", null, FWMessage.FATAL, getClass().getName());
            return false;
        }
        // Sous controle d'exceptions
        try {
            // Remise du passage � l'�tat "ouvert"
            CPDecision decis = new CPDecision();
            // Rechercher les donn�es de la d�cision
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
                // Ne pas traiter si la d�cision � l'�tat facturer ou reprise
                // Aller rechercher le dernier �tat
                if ((!CPDecision.CS_FACTURATION.equalsIgnoreCase(decis.getDernierEtat()))
                        && (!CPDecision.CS_REPRISE.equalsIgnoreCase(decis.getDernierEtat()))
                        && (!CPDecision.CS_PB_COMPTABILISATION.equalsIgnoreCase(decis.getDernierEtat()))
                        && (!CPDecision.CS_SORTIE.equalsIgnoreCase(decis.getDernierEtat()))) {
                    // Traitement
                    // Recherche donn�es du tiers pour info message
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
                            // Remettre l'idPassage � blanc et l'�tat � calcul
                            decis.setDernierEtat(CPDecision.CS_CALCUL);
                            decis.setIdPassage("");
                            decis.setMiseEnGEDValidationRetour(Boolean.FALSE);
                            decis.update(getTransaction());
                            // Mettre �tat g�n�r� dans la communication fiscale
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
                                    // Mise � jour du journal
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
            obj = getSession().getLabel("CP_MSG_0163") + " " + getIdDecision() + " "
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
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 10:57:26)
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
    public void setIdDecision(java.lang.String newIdDecision) {
        idDecision = newIdDecision;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 10:57:26)
     * 
     * @param newIdTiers
     *            java.lang.String
     */
    public void setIdTiers(java.lang.String newIdTiers) {
        idTiers = newIdTiers;
    }

}
