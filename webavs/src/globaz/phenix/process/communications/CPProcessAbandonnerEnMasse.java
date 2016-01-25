package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPValidationCalculCommunication;
import globaz.phenix.db.communications.CPValidationCalculCommunicationManager;
import globaz.phenix.db.communications.CPValidationJournalRetourViewBean;
import java.util.Enumeration;

public class CPProcessAbandonnerEnMasse extends BProcess {

    private static final long serialVersionUID = 945776927145658111L;
    private boolean envoiMail = false;
    private String forIdPlausibilite = "";
    private String forStatus = "";
    private String fromNumAffilie = "";
    private String[] listIdJournalRetour = null;
    private String[] listIdRetour = null;
    private int nbIdJournalRetour = 0;
    private int nbIdRetour = 0;
    private String tillNumAffilie = "";

    /**
     * Commentaire relatif au constructeur CPProcessAbandonnerEnMasse.
     */
    public CPProcessAbandonnerEnMasse() {
        super();
    }

    /**
     * Nettoyage après erreur ou exécution
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Calcul des montants de cotisation
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        try {

            // Point d'entré pour l'abandon en masse suivant une liste d'id
            // retour
            if (getListIdRetour() != null) {
                if (!updateByIdRetour()) {
                    return false;
                }
            }

            // Point d'entré pour l'abandon en masse suivant une liste d'id
            // journal
            if (getListIdJournalRetour() != null) {
                if (!updateByIdJournalRetour()) {
                    return false;
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
        int nbErreur = 0;

        if (getListIdRetour() != null) {
            nbErreur = getListIdRetour().length - getNbIdRetour();

            getMemoryLog().logMessage(getSession().getLabel("ABANDONNER_NBCOM_RETOUR_SUCCESS") + " " + getNbIdRetour(),
                    FWMessage.INFORMATION, this.getClass().getName());
            getMemoryLog().logMessage(getSession().getLabel("ABANDONNER_NBCOM_RETOUR_ERROR") + " " + nbErreur,
                    FWMessage.INFORMATION, this.getClass().getName());
        }
        if (getListIdJournalRetour() != null) {
            nbErreur = getListIdJournalRetour().length - getNbIdJournalRetour();

            getMemoryLog().logMessage(
                    getSession().getLabel("ABANDONNER_NBJOURNAL_RETOUR_SUCCESS") + " " + getNbIdJournalRetour(),
                    FWMessage.INFORMATION, this.getClass().getName());
            getMemoryLog().logMessage(getSession().getLabel("ABANDONNER_NBJOURNAL_RETOUR_SUCCESS") + " " + nbErreur,
                    FWMessage.INFORMATION, this.getClass().getName());
        }
    }

    /**
     * Permet de checker si on est autorisé a lancer un abandon sur l'id retour passé en paramètre
     * 
     * @param session
     * @param idRetour
     * @return
     */
    private boolean checkOptionAllowed(BSession session, String idRetour) {
        try {
            String idValidation = "";
            if (idRetour != null) {
                CPValidationCalculCommunicationManager manager = new CPValidationCalculCommunicationManager();
                manager.setSession(session);
                manager.setForIdCommunicationRetour(idRetour);
                manager.find();
                CPValidationCalculCommunication communication = (CPValidationCalculCommunication) manager
                        .getFirstEntity();
                if (communication != null) {
                    idValidation = communication.getIdValidationCommunication();
                    if (idValidation != null) {
                        CPValidationJournalRetourViewBean validation = new CPValidationJournalRetourViewBean();
                        validation.setSession(session);
                        validation.setIdValidation(idValidation);
                        validation.getIdDecision();
                        validation.retrieve();
                        if (validation.getCodeValidation().equalsIgnoreCase("1")) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    // la communication n'est pas encore dans la table calcul
                    // communication
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("ABANDONNER_MAIL_OBJECT_ERROR");
        } else {
            return getSession().getLabel("ABANDONNER_MAIL_OBJECT_SUCCESS");
        }
    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    public String getForStatus() {
        return forStatus;
    }

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String[] getListIdJournalRetour() {
        return listIdJournalRetour;
    }

    public String[] getListIdRetour() {
        return listIdRetour;
    }

    public int getNbIdJournalRetour() {
        return nbIdJournalRetour;
    }

    public int getNbIdRetour() {
        return nbIdRetour;
    }

    public String getTillNumAffilie() {
        return tillNumAffilie;
    }

    // **************************************
    // * Getter
    // **************************************

    /**
     * Incrémente le compteur d'id journal retour traité
     */
    private void incNbIdJournalRetour() {
        nbIdJournalRetour += 1;
    }

    /**
     * Incrémente le compteur d'id retour traité
     */
    private void incNbIdRetour() {
        nbIdRetour += 1;
    }

    /**
     * Initialisation de l'information de progression
     * 
     * @param length
     */
    private void initProgressCounter(int length) {

        if (length > 0) {
            setProgressScaleValue(length);
        } else {
            setProgressScaleValue(1);
        }
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

    public void setEnvoiMail(boolean envoiMail) {
        this.envoiMail = envoiMail;
    }

    public void setForIdPlausibilite(String forIdPlausibilite) {
        this.forIdPlausibilite = forIdPlausibilite;
    }

    public void setForStatus(String forStatus) {
        this.forStatus = forStatus;
    }

    // **************************************
    // * Setter
    // **************************************

    public void setFromNumAffilie(String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    public void setListIdJournalRetour(String[] listIdJournalRetour) {
        this.listIdJournalRetour = listIdJournalRetour;
    }

    public void setListIdRetour(String[] listIdRetour) {
        this.listIdRetour = listIdRetour;
    }

    public void setNbIdJournalRetour(int nbIdJournalRetour) {
        this.nbIdJournalRetour = nbIdJournalRetour;
    }

    public void setNbIdRetour(int nbIdRetour) {
        this.nbIdRetour = nbIdRetour;
    }

    public void setTillNumAffilie(String tillNumAffilie) {
        this.tillNumAffilie = tillNumAffilie;
    }

    /**
     * @param idRetour
     * @param idJournalRetour
     * @param forStatus
     * @param fromNumAffilie
     * @param TillNumAffilie
     * @param forIdPlausibilite
     * @return
     * @throws Exception
     */
    private boolean update(String idRetour, String idJournalRetour, String forStatus, String fromNumAffilie,
            String TillNumAffilie, String forIdPlausibilite) throws Exception {

        BStatement statement = null;
        CPCommunicationFiscaleRetourViewBean comRetour = null;

        // Manager qui permet de récupérer les communications en retour
        CPCommunicationFiscaleRetourManager comRetourMng = new CPCommunicationFiscaleRetourManager();
        comRetourMng.setSession(getSession());
        comRetourMng.setForIdRetour(idRetour);
        comRetourMng.setForIdJournalRetour(idJournalRetour);
        comRetourMng.setForStatus(forStatus);
        comRetourMng.setFromNumAffilie(fromNumAffilie);
        comRetourMng.setTillNumAffilie(TillNumAffilie);
        comRetourMng.setForIdPlausibilite(forIdPlausibilite);
        comRetourMng.setWhitAffiliation(true);

        // On itére sur toutes les communication en retour
        statement = comRetourMng.cursorOpen(getTransaction());
        while (((comRetour = (CPCommunicationFiscaleRetourViewBean) comRetourMng.cursorReadNext(statement)) != null)
                && (!comRetour.isNew()) && !isAborted()) {
            if (!getTransaction().hasErrors()) {
                idJournalRetour = comRetour.getIdJournalRetour();
                if (!CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE.equalsIgnoreCase(comRetour.getStatus())
                        && !CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE.equalsIgnoreCase(comRetour.getStatus())) {
                    // Si état = validé => contrôler si le journal de
                    // facturation n'est pas en traitement
                    comRetour._suppressionDecision(getTransaction(), "");
                    comRetour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
                    comRetour.update(getTransaction());
                    if (comRetour.hasErrors()) {
                        getMemoryLog().logStringBuffer(getTransaction().getErrors(), comRetour.getClass().getName());
                        getMemoryLog().logMessage("5029", null, FWMessage.FATAL, this.getClass().getName());
                        return false;
                    }
                }
            }
        }

        // Mise à jour de l'état du journal
        if (!JadeStringUtil.isBlank(idJournalRetour)) {
            CPJournalRetour jrn = new CPJournalRetour();
            jrn.setSession(getSession());
            jrn.setIdJournalRetour(idJournalRetour);
            jrn.retrieve(getTransaction());
            if (!jrn.isNew()) {
                jrn.update(getTransaction());
            }
        }

        return true;
    }

    /**
     * Permet dêxecuter l'update des communications en retour en passant une liste d'id journal retour
     * 
     * @return
     * @throws Exception
     */
    private boolean updateByIdJournalRetour() throws Exception {

        // initialisation du %age de progression
        initProgressCounter(getListIdJournalRetour().length);

        // Execute l'abandon sur chaque journal
        for (int i = 0; i < getListIdJournalRetour().length; i++) {

            String idJournalRetour = getListIdJournalRetour()[i];

            if (!JadeStringUtil.isBlank(idJournalRetour)) {
                if (!update("", idJournalRetour, getForStatus(), getFromNumAffilie(), getTillNumAffilie(),
                        getForIdPlausibilite())) {
                    getMemoryLog().logMessage(
                            getSession().getLabel("ABANDONNER_JOURNAL_RETOUR_ERROR") + " : " + idJournalRetour,
                            FWMessage.INFORMATION, this.getClass().getName());
                } else {
                    incNbIdJournalRetour();
                }
            }

            incProgressCounter(); // %age de progression
        }

        return true;
    }

    /**
     * Permet d'executer l'update des communications en retour en passant une liste d'id retour
     * 
     * @return
     * @throws Exception
     */
    private boolean updateByIdRetour() throws Exception {

        // initialisation du %age de progression
        initProgressCounter(getListIdRetour().length);

        // Execute l'abandon sur chaque communication en retour.
        for (int i = 0; i < getListIdRetour().length; i++) {

            String idRetour = getListIdRetour()[i];

            if (!checkOptionAllowed(getSession(), idRetour)) {
                getMemoryLog().logMessage(
                        getSession().getLabel("ABANDONNER_COM_RETOUR_IMPOSSIBLE") + " : " + " ID : " + idRetour,
                        FWMessage.INFORMATION, this.getClass().getName());
            } else {
                if (!update(idRetour, "", "", "", "", "")) {
                    getMemoryLog().logMessage(
                            getSession().getLabel("ABANDONNER_JOURNAL_RETOUR_ERROR") + " : " + idRetour,
                            FWMessage.INFORMATION, this.getClass().getName());
                } else {
                    incNbIdRetour();
                }
            }

            incProgressCounter(); // %age de progression
        }

        return true;
    }

}
