/*
 * Créé le Apr 20, 2005
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.application.CAApplication;

/**
 * @author dda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CAJournalOperationLettrer extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String MODE_AUTOMATIC = "automatic";
    public final static String MODE_MANUAL = "manual";

    private String idCompteAnnexe = null;
    private String idSourceSection = null;
    private String manualSection = "";

    private String mode = CAJournalOperationLettrer.MODE_AUTOMATIC;
    private String montantMaxALettrer = "";

    /**
     * Constructor for CALettrage.
     */
    public CAJournalOperationLettrer() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CALettrage.
     * 
     * @param parent
     */
    public CAJournalOperationLettrer(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CALettrage.
     * 
     * @param session
     */
    public CAJournalOperationLettrer(BSession session) throws Exception {
        super(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        CASection sourceSection = getSourceSection();

        if (sourceSection != null) {
            FWCurrency montantMax = new FWCurrency(getMontantMaxALettrer());
            if (!montantMax.isNegative()) {
                getMemoryLog().logMessage(
                        getSession().getLabel("LETTRAGE_ERROR_MONTANT_NOT_NEGATIF") + " "
                                + getCompteAnnexeDescription(), FWMessage.FATAL, this.getClass().getName());
                return false;
            }
            if (getSourceSection().getSoldeToCurrency().compareTo(montantMax) > 0) {
                getMemoryLog()
                        .logMessage(
                                getSession().getLabel("LETTRAGE_ERROR_MONTANT_TROP_GRAND") + " "
                                        + getCompteAnnexeDescription(), FWMessage.FATAL, this.getClass().getName());
                return false;
            }
            if (mode.equals(CAJournalOperationLettrer.MODE_AUTOMATIC)) {
                sourceSection.automaticLettrage(null, getCompteAnnexe(), null, getMontantMaxALettrer());
            } else if (mode.equals(CAJournalOperationLettrer.MODE_MANUAL)) {
                CASection section = getSection(getManualSection());

                if (section == null) {
                    return false;
                }

                sourceSection.manualLettrage(null, getCompteAnnexe(), section, null, getMontantMaxALettrer());
            } else {
                return false;
            }

            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(getSession().getErrors().toString() + " " + getCompteAnnexeDescription(),
                        FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            if (sourceSection.hasErrors()) {
                this._addError(sourceSection.getMessage());
                getMemoryLog().logMessage(sourceSection.getMessage() + " " + getCompteAnnexeDescription(),
                        FWMessage.FATAL, this.getClass().getName());
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * Return le compte annexe.
     * 
     * @return
     */
    public CACompteAnnexe getCompteAnnexe() {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());
        compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());

        try {
            compteAnnexe.retrieve();
        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        return compteAnnexe;
    }

    /**
     * Return la description du compte annexe (à parser dans l'email lors d'erreurs).
     * 
     * @return
     */
    private String getCompteAnnexeDescription() {
        return "(" + getCompteAnnexe().getDescription() + ")";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("LETTRAGE_ERROR");
        } else {
            return getSession().getLabel("LETTRAGE_OK");
        }
    }

    /**
     * Return l'id du compte annexe.
     * 
     * @return
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * Return l'id de la section de base sur laquelle le lettrage sera effectué.
     * 
     * @return
     */
    public String getIdSourceSection() {
        return idSourceSection;
    }

    /**
     * Retourne la section à lettrer dans le cas du mode manuel.
     * 
     * @return
     */
    public String getManualSection() {
        return manualSection;
    }

    /**
     * @return
     */
    public String getMode() {
        return mode;
    }

    /**
     * @return the montantMaxALettrer
     */
    public String getMontantMaxALettrer() {
        if (JadeStringUtil.isBlankOrZero(montantMaxALettrer)) {
            return getSourceSection().getSolde();
        }
        return montantMaxALettrer;
    }

    /**
     * Retourne un section correspondant l'id paramètre (idSection).
     * 
     * @param idSection
     * @return
     */
    public CASection getSection(String idSection) {
        CASectionManager managerSection = new CASectionManager();
        managerSection.setSession(getSession());
        managerSection.setForIdSection(idSection);
        managerSection.setForIdCompteAnnexe(getIdCompteAnnexe());

        try {
            managerSection.find();
        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        if (managerSection.size() == 0) {
            return null;
        }

        return (CASection) managerSection.getEntity(0);
    }

    /**
     * Retourne la section de base à lettrer.
     * 
     * @return
     */
    public CASection getSourceSection() {
        CASection section = new CASection();
        section.setSession(getSession());
        section.setIdSection(getIdSourceSection());
        section.setIdCompteAnnexe(getIdCompteAnnexe());

        try {
            section.retrieve();
        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        return section;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Set l'id du compte annexe.
     * 
     * @param string
     */
    public void setIdCompteAnnexe(String string) {
        idCompteAnnexe = string;
    }

    /**
     * Set l'id de la section de base à lettrer.
     * 
     * @param string
     */
    public void setIdSourceSection(String string) {
        idSourceSection = string;
    }

    /**
     * Set la valeur de la section manuel que l'on doit lettrer.
     * 
     * @param string
     */
    public void setManualSection(String string) {
        manualSection = string;
    }

    /**
     * @param string
     */
    public void setMode(String s) {
        mode = s;
    }

    /**
     * @param montantMaxALettrer
     *            the montantMaxALettrer to set
     */
    public void setMontantMaxALettrer(String montantMaxALettrer) {
        this.montantMaxALettrer = montantMaxALettrer;
    }

}
