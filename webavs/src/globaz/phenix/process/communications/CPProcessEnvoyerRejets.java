package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPLienSedexCommunicationFiscale;
import globaz.phenix.db.communications.CPLienSedexCommunicationFiscaleManager;
import globaz.phenix.db.communications.CPRejets;
import java.util.ArrayList;

/**
 * TODO Commentaire de la classe
 * 
 * @author SCO 12 nov. 2009
 */
public class CPProcessEnvoyerRejets extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean envoiMail = true;
    private ArrayList<String> listeNonRetrouvee = null;
    private String[] listIdRetour = null;
    private int nbIdRetour = 0;

    /**
     * Constructeur
     */
    public CPProcessEnvoyerRejets() {
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
        setTransaction(getSession().getCurrentThreadTransaction());
        listeNonRetrouvee = new ArrayList<String>();
        try {
            if (getListIdRetour() != null) {
                CPProcessXMLSedexWriter process = null;
                for (int i = 0; i < getListIdRetour().length; i++) {
                    String idRejet = getListIdRetour()[i];
                    CPRejets rejets = new CPRejets();
                    rejets.setSession(getSession());
                    rejets.setIdRejets(idRejet);
                    rejets.retrieve(getTransaction());

                    CPCommunicationFiscale comm = new CPCommunicationFiscale();
                    comm.setSession(getSession());
                    comm.setAlternateKey(CPCommunicationFiscale.AK_ID_MESSAGE_SEDEX);
                    comm.setIdMessageSedex(rejets.getReferenceMessageId());
                    comm.retrieve(getTransaction());

                    if (comm.isNew()) {
                        // regarder dans la table historique
                        CPLienSedexCommunicationFiscaleManager mng = new CPLienSedexCommunicationFiscaleManager();
                        mng.setSession(getSession());
                        mng.setForIdMessageSedex(rejets.getReferenceMessageId());
                        mng.find();
                        if (mng.size() > 0) {
                            String idComm = ((CPLienSedexCommunicationFiscale) mng.getFirstEntity())
                                    .getIdCommunication();
                            comm = new CPCommunicationFiscale();
                            comm.setIdCommunication(idComm);
                            comm.retrieve(getTransaction());
                        }
                    }
                    if (!comm.isNew()) {
                        process = new CPProcessXMLSedexWriter();
                        process.setEMailAddress(getEMailAddress());
                        process.setEnvoiImmediat(true);
                        process.setDonneesCommerciales(false);
                        process.setDonneesPrivees(false);
                        process.setIdCommunication(comm.getIdCommunication());
                        process.setEnvoiIndividuel(true);
                        process.setSession(getSession());
                        process.setTransaction(getTransaction());
                        try {
                            process.executeProcess();
                            getMemoryLog().logMessage(process.getMemoryLog());
                            if (!getTransaction().hasErrors() && !getSession().hasErrors()) {
                                nbIdRetour++;
                            }
                        } catch (Exception e) {
                            JadeLogger.error(this, e);
                            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                        }
                        if (!getTransaction().hasErrors() && !getSession().hasErrors()) {
                            String format = "ddMMyyyy";
                            java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
                            java.util.Date date = new java.util.Date();
                            rejets.setDateRenvoi(formater.format(date));
                            rejets.setEtat(CPRejets.CS_ETAT_ENVOYE);
                            rejets.update(getTransaction());
                        }
                    } else {
                        listeNonRetrouvee.add(rejets.getYourBusinessReferenceId() + " " + rejets.getNom() + " "
                                + rejets.getPrenom() + " : " + rejets.getAnnee());
                        // this.getTransaction().addWarnings(
                        // "Demande non retrouvée : " + rejets.getYourBusinessReferenceId() + " "
                        // + rejets.getNom() + " " + rejets.getPrenom());
                    }

                    if (!getTransaction().hasErrors() && !getSession().hasErrors()) {
                        getTransaction().commit();
                    } else {
                        getMemoryLog().logMessage(getTransaction().getErrors().toString(), FWMessage.FATAL,
                                this.getClass().getName());
                        getTransaction().rollback();
                    }
                    getTransaction().clearErrorBuffer();
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } finally {
            try {
                // Envoi par mail des informations du process
                addMailInformations();
                // this.sendEmail();
                if (getTransaction() != null) {
                    getTransaction().closeTransaction();
                }
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
            getMemoryLog().logMessage(getSession().getLabel("REJETS_NBCOM_TRAITE") + ": " + getNbIdRetour(),
                    FWMessage.INFORMATION, this.getClass().getName());
            getMemoryLog().logMessage(getSession().getLabel("REJETS_NBCOM_NONTRAITE") + ": " + nbNonTraite,
                    FWMessage.INFORMATION, this.getClass().getName());
            if (listeNonRetrouvee.size() > 0) {
                for (int i = 0; i < listeNonRetrouvee.size(); i++) {
                    getMemoryLog().logMessage(getSession().getLabel("CP_REJET_MSG2") + ": " + listeNonRetrouvee.get(i),
                            FWMessage.INFORMATION, this.getClass().getName());
                }
            }
        }
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("REJETS_MAIL_OBJECT_ERROR");
        } else {
            return getSession().getLabel("REJETS_MAIL_OBJECT_SUCCES");
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
