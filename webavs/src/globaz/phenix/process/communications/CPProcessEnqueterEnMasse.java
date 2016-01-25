package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import java.util.Enumeration;

/**
 * TODO Commentaire de la classe
 * 
 * @author SCO 12 nov. 2009
 */
public class CPProcessEnqueterEnMasse extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean envoiMail = false;
    private String[] listIdRetour = null;
    private int nbIdRetour = 0;

    /**
     * Constructeur
     */
    public CPProcessEnqueterEnMasse() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            if (getListIdRetour() != null) {

                for (int i = 0; i < getListIdRetour().length; i++) {

                    String idRetour = getListIdRetour()[i];

                    CPCommunicationFiscaleRetourViewBean comRetour = null;
                    // Manager
                    CPCommunicationFiscaleRetourManager comRetourMng = new CPCommunicationFiscaleRetourManager();
                    comRetourMng.setSession(getSession());
                    comRetourMng.setForIdRetour(idRetour);

                    comRetourMng.find(getTransaction());

                    for (int j = 0; j < comRetourMng.getSize(); j++) {

                        comRetour = (CPCommunicationFiscaleRetourViewBean) comRetourMng.getEntity(j);

                        // On ne peut passer en enquete que sur les status
                        // suivant :
                        if (CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE.equals(comRetour.getStatus())
                                || CPCommunicationFiscaleRetourViewBean.CS_ERREUR.equals(comRetour.getStatus())
                                || CPCommunicationFiscaleRetourViewBean.CS_AVERTISSEMENT.equals(comRetour.getStatus())
                                || CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE.equals(comRetour.getStatus())
                                || CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE.equals(comRetour.getStatus())
                                || CPCommunicationFiscaleRetourViewBean.CS_A_CONTROLER.equals(comRetour.getStatus())) {
                            comRetour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_ENQUETE);
                            comRetour.update(getTransaction());
                            if (comRetour.hasErrors()) {
                                getMemoryLog().logStringBuffer(getTransaction().getErrors(),
                                        comRetour.getClass().getName());
                                getMemoryLog().logMessage("5029", null, FWMessage.FATAL, this.getClass().getName());
                                getMemoryLog().logMessage(
                                        getSession().getLabel("ENQUETER_COM_RETOUR_PROBLEME") + " : "
                                                + comRetour.getIdRetour(), FWMessage.INFORMATION,
                                        this.getClass().getName());
                                return false;
                            }
                            incNbIdRetour(); // On incrémente le compteur du
                            // nombre de com traité
                        } else { // Si on peut pas, on le log
                            getMemoryLog()
                                    .logMessage(
                                            getSession().getLabel("ENQUETER_NBCOM_RETOUR_IMPOSSIBLE") + " "
                                                    + comRetour.getIdRetour(), FWMessage.INFORMATION,
                                            this.getClass().getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } finally {
            try {
                // Envoi par mail des informations du process
                addMailInformations();
                sendEmail();
            } catch (Exception e) {
                JadeLogger.warn(this + "_Envoi_du_mail", e);
            }
        }

        return !isOnError();
    }

    /**
     * Ajoute des informations dans l'email.
     */
    private void addMailInformations() throws Exception {
        int nbNonTraite = 0;

        if (getListIdRetour() != null) {
            nbNonTraite = getListIdRetour().length - getNbIdRetour();

            getMemoryLog().logMessage(getSession().getLabel("ENQUETER_NBCOM_RETOUR_TRAITE") + " " + getNbIdRetour(),
                    FWMessage.INFORMATION, this.getClass().getName());
            getMemoryLog().logMessage(getSession().getLabel("ENQUETER_NBCOM_RETOUR_NONTRAITE") + " " + nbNonTraite,
                    FWMessage.INFORMATION, this.getClass().getName());
        }
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("ENQUETER_MAIL_OBJECT_ERROR");
        } else {
            return getSession().getLabel("ENQUETER_MAIL_OBJECT_SUCCESS");
        }
    }

    public String[] getListIdJournalRetour() {
        return null;
    }

    public String[] getListIdRetour() {
        return listIdRetour;
    }

    public int getNbIdRetour() {
        return nbIdRetour;
    }

    // **************************************
    // * Getter
    // **************************************

    /**
     * Incrémente le compteur d'id retour traité
     */
    private void incNbIdRetour() {
        nbIdRetour += 1;
    }

    public boolean isEnvoiMail() {
        return envoiMail;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Permet l'envoi de l'email
     * 
     * @throws Exception
     */
    private void sendEmail() throws Exception {
        if (isEnvoiMail()) {
            StringBuffer emailContent = new StringBuffer();

            Enumeration<?> e = getMemoryLog().enumMessages();
            while (e.hasMoreElements()) {
                emailContent.append(((FWMessage) e.nextElement()).getFullMessage()).append("\n");
            }

            JadeSmtpClient.getInstance().sendMail(getEMailAddress(), getEMailObject(), emailContent.toString(), null);
        }
    }

    // **************************************
    // * Setter
    // **************************************

    public void setEnvoiMail(boolean envoiMail) {
        this.envoiMail = envoiMail;
    }

    public void setListIdRetour(String[] listIdRetour) {
        this.listIdRetour = listIdRetour;
    }

    public void setNbIdRetour(int nbIdRetour) {
        this.nbIdRetour = nbIdRetour;
    }
}
