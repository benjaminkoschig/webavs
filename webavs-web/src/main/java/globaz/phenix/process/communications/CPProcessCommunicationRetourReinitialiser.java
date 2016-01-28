package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.interfaces.ICommunicationrRetourManager;
import java.util.ArrayList;

/**
 * Processus de réinitialisation des communications en retour
 * 
 * @author btc
 * @author SCO 08-12-09
 */
public class CPProcessCommunicationRetourReinitialiser extends BProcess {

    private static final long serialVersionUID = 8514525917255629722L;
    private String descriptionTiers = "";
    private String forIdPlausibilite = "";
    private String forStatus = "";
    private String fromNumAffilie = "";
    private String idJournalRetour = "";
    private CPJournalRetour journal = null;
    private String[] listIdJournalRetour = null;
    private ArrayList<String> listIdNonTraite = null;

    private String[] listIdRetour = null;
    private ArrayList<String> listIdTraite = null;

    private boolean progressBarInJournalOn = false;
    private ICommunicationRetour retour = null;

    private String tillNumAffilie = "";

    /**
     * Constructeur.
     */
    public CPProcessCommunicationRetourReinitialiser() {
        super();
    }

    /**
     * Constructeur du type BProcess
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessCommunicationRetourReinitialiser(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BSession.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessCommunicationRetourReinitialiser(BSession session) {
        super(session);
    }

    /**
     * @param manager
     * @return
     */
    public boolean _executeBoucleRetour(ICommunicationrRetourManager manager) {

        BStatement statement = null;
        // Sous controle d'exceptions
        try {
            // disabler le spy
            getTransaction().disableSpy();
            // itérer sur toutes les affiliations
            statement = manager.cursorOpen(getTransaction());
            initProgressCounter(manager.getCount(getTransaction()));
            // Recherche si revenuAutre = revenu AF (ex cas spéciaux AF pour GE)
            while (((retour = (ICommunicationRetour) manager.cursorReadNext(statement)) != null) && (!retour.isNew())
                    && !isAborted()) {
                if (!getTransaction().hasErrors()) {
                    // Si etat = générer => suppression des décisions
                    retour.setJournalRetour(journal);
                    retour._suppressionDecision(getTransaction(), "");
                    // Repasser dans les plausibilités avant génération (fait
                    // dans le validate)
                    retour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE);
                    retour.updateCas(getTransaction());
                }
                if (!getTransaction().hasErrors()) {
                    getTransaction().commit();
                } else {
                    getTransaction().rollback();
                }
                incProgressCounter();
            }
            // rétablir le contrôle du spy
            getTransaction().enableSpy();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } // Fin de la procédure
        catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                manager.cursorClose(statement);
                manager = null;
                statement = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return !isOnError();
    }

    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Calcul des montants de cotisation Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        listIdTraite = new ArrayList<String>();
        listIdNonTraite = new ArrayList<String>();

        try {
            // Soit on donne une liste d'id retour
            if ((getListIdRetour() != null) && (getListIdRetour().length != 0)) {
                reinitByIdRetour();

                // Soit on donne un id de journal
            } else if ((getListIdJournalRetour() != null) && (getListIdJournalRetour().length != 0)) {
                reinitByIdJournalRetour();

                // On trace un message dans le mail
            } else {
                this._addError(getTransaction(), getSession().getLabel("COM_RETOUR_SELECTIONNE"));
            }

            // Ajout du corps de l'email
            addMailInformations();

            if (getTransaction().hasErrors()) {
                return false;
            }

        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("PROCRECEPTINIT_EMAIL_OBJECT_FAILED"));

            if (!JadeStringUtil.isBlank(e.getMessage())) {
                this._addError(getTransaction(), e.getMessage());
            } else {
                this._addError(getTransaction(), e.toString());
            }

            return false;
        }

        return !isOnError();
    }

    @Override
    protected void _validate() throws java.lang.Exception {

        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0101"));
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
    }

    /**
     * Ajoute des informations dans l'email.
     */
    private void addMailInformations() throws Exception {
        StringBuffer buff = new StringBuffer();

        getMemoryLog().logMessage(getSession().getLabel("PROCESS_DONNEES_TRAITEES") + " : " + getListIdTraite().size(),
                FWMessage.INFORMATION, this.getClass().getName());
        getMemoryLog().logMessage(
                getSession().getLabel("PROCESS_DONNEES_NON_TRAITEES") + " : " + getListIdNonTraite().size(),
                FWMessage.INFORMATION, this.getClass().getName());
        if (getListIdTraite().size() > 0) {
            getMemoryLog().logMessage(
                    "------------------------- " + getSession().getLabel("PROCESS_DONNEES_TRAITEES")
                            + " -------------------" + getListIdNonTraite().size(), FWMessage.INFORMATION,
                    this.getClass().getName());
            for (int i = 0; i < getListIdTraite().size(); i++) {
                if (buff.length() != 0) {
                    buff.append(";");
                }
                buff.append(getListIdTraite().get(i));
            }
            getMemoryLog().logMessage(buff.toString(), FWMessage.INFORMATION, this.getClass().getName());
        }
        if (getListIdNonTraite().size() > 0) {
            getMemoryLog().logMessage(
                    "------------------------- " + getSession().getLabel("PROCESS_DONNEES_NON_TRAITEES")
                            + " -------------------" + getListIdNonTraite().size(), FWMessage.INFORMATION,
                    this.getClass().getName());
            buff = new StringBuffer();
            for (int i = 0; i < getListIdNonTraite().size(); i++) {
                if (buff.length() != 0) {
                    buff.append(";");
                }
                buff.append(getListIdNonTraite().get(i));
            }
            getMemoryLog().logMessage(buff.toString(), FWMessage.INFORMATION, this.getClass().getName());
        }
    }

    /**
     * @param idRetour
     * @return
     */
    protected boolean checkReinitAllowed(String idRetour) {
        try {
            CPCommunicationFiscaleRetourViewBean retour = new CPCommunicationFiscaleRetourViewBean();
            retour.setIdRetour(idRetour);
            retour.setSession(getSession());
            retour.retrieve();
            if (!retour.isNew()
                    && CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE.equalsIgnoreCase(retour.getStatus())) {
                return false;
            }

            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public String getDescriptionTiers() {
        return descriptionTiers;
    }

    @Override
    protected String getEMailObject() {
        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("PROCRECEPTINIT_EMAIL_OBJECT_FAILED");
        }

        return getSession().getLabel("SUJET_EMAIL_RECEPTION_INIT");
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

    public String getIdJournalRetour() {
        return idJournalRetour;
    }

    public CPJournalRetour getJournal() {
        return journal;
    }

    public String[] getListIdJournalRetour() {
        return listIdJournalRetour;
    }

    public ArrayList<String> getListIdNonTraite() {
        return listIdNonTraite;
    }

    public String[] getListIdRetour() {
        return listIdRetour;
    }

    public ArrayList<String> getListIdTraite() {
        return listIdTraite;
    }

    public ICommunicationRetour getRetour() {
        return retour;
    }

    public String getTillNumAffilie() {
        return tillNumAffilie;
    }

    /**
     * Permet d'initialiser la progress bar
     * 
     * @param nbOccurence
     */
    protected void initProgressCounter(int nbOccurence) {

        if (nbOccurence > 0) {
            setProgressScaleValue(nbOccurence);
        } else {
            setProgressScaleValue(1);
        }
    }

    public boolean isProgressBarInJournalOn() {
        return progressBarInJournalOn;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Récupération du journal
     * 
     * @param idRetour
     * @return
     * @throws Exception
     */
    protected CPJournalRetour recuperationJournal(String idRetour, String idJournalRetour) throws Exception {

        if (!JadeStringUtil.isEmpty(idRetour)) {
            CPCommunicationFiscaleRetourViewBean communication = new CPCommunicationFiscaleRetourViewBean();
            communication.setSession(getSession());
            communication.setIdRetour(idRetour);
            communication.retrieve();
            idJournalRetour = communication.getIdJournalRetour();
        }

        // Si aucun journal, on retourne null
        if (JadeStringUtil.isEmpty(idJournalRetour)) {
            return null;
        }

        CPJournalRetour jrn = new CPJournalRetour();
        jrn.setSession(getSession());
        jrn.setIdJournalRetour(idJournalRetour);
        jrn.retrieve(getTransaction());
        if ((jrn == null) || jrn.isNew()) { // Si aucun journal, on retourne null
            // avec un message
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0138") + " " + idJournalRetour);
            return null;
        }

        return jrn;
    }

    /**
     * Permet de lancer le process a partir d'un id de journal de retour
     * 
     * @return
     * @throws Exception
     */
    protected boolean reinitByIdJournalRetour() {

        // Init du compteur de progression
        initProgressCounter(getListIdJournalRetour().length);

        // Pour chaque id journal retour donné
        for (int i = 0; i < getListIdJournalRetour().length; i++) {

            String idJournalRetour = getListIdJournalRetour()[i];

            try {
                if (!JadeStringUtil.isEmpty(idJournalRetour)) {
                    setIdJournalRetour(idJournalRetour);
                    reinitialiserCommunication(null);
                    setIdJournalRetour(null);
                    if (!getTransaction().hasErrors()) {
                        getListIdTraite().add(idJournalRetour);
                    }
                }

            } catch (Exception e) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0139") + retour.getIdRetour() + " - "
                        + e.getMessage());
                getListIdNonTraite().add(idJournalRetour);
            }

            if (isAborted()) {
                return false;
            }

            // On incremente le compteur
            incProgressCounter();
        }

        return true;
    }

    /**
     * Permet de lancer le process avec une liste d'id retour
     * 
     * @return
     */
    protected boolean reinitByIdRetour() {

        // Init du compteur de progression
        initProgressCounter(getListIdRetour().length);

        // Pour chaque idRetour donné
        for (int i = 0; i < getListIdRetour().length; i++) {
            String idRetour = getListIdRetour()[i];

            try {
                if (!JadeStringUtil.isEmpty(idRetour)) {
                    if (checkReinitAllowed(idRetour)) {
                        reinitialiserCommunication(idRetour);
                        if (!getTransaction().hasErrors()) {
                            getListIdTraite().add(idRetour);
                        }
                    } else {
                        this._addError(getTransaction(), getSession().getLabel("CP_MSG_0176") + " " + idRetour);
                        getListIdNonTraite().add(idRetour);
                    }
                }
            } catch (Exception e) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0139") + retour.getIdRetour() + " - "
                        + e.getMessage());
                getListIdNonTraite().add(idRetour);
            }

            if (isAborted()) {
                return false;
            }

            // On incremente le compteur
            incProgressCounter();
        }

        return true;
    }

    /**
     * @param idRetour
     * @return
     * @throws Exception
     */
    protected boolean reinitialiserCommunication(String idRetour) throws Exception {

        ICommunicationrRetourManager manager = null;

        // Récupération du journal
        setJournal(recuperationJournal(idRetour, getIdJournalRetour()));

        // Définition du manager
        manager = getJournal().determinationManager();
        if (!JadeStringUtil.isEmpty(idRetour)) {
            manager.setForIdRetour(idRetour);
        }
        manager.setSession(getSession());
        manager.setForIdJournalRetour(getIdJournalRetour());
        if (JadeStringUtil.isEmpty(idRetour)) {
            manager.setForStatus(getForStatus());
            manager.setNotInStatus(CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE);
            manager.setFromNumAffilie(getFromNumAffilie());
            manager.setTillNumAffilie(getTillNumAffilie());
        }
        manager.setWhitPavsAffilie(true);
        manager.setWhitAffiliation(true);
        manager.orderByNumIFD();
        manager.orderByNumContribuable();
        manager.orderByIdCommunicationRetour();
        manager.setForIdPlausibilite(getForIdPlausibilite());

        _executeBoucleRetour(manager);

        if (!getTransaction().hasErrors()) {
            getJournal().update(getTransaction());
        }
        if (!getTransaction().hasErrors()) {
            getTransaction().commit();
        }

        return true;
    }

    public void setDescriptionTiers(String newDescriptionTiers) {
        descriptionTiers = newDescriptionTiers;
    }

    public void setForIdPlausibilite(String forIdPlausibilite) {
        this.forIdPlausibilite = forIdPlausibilite;
    }

    public void setForStatus(String string) {
        forStatus = string;
    }

    public void setFromNumAffilie(String string) {
        fromNumAffilie = string;
    }

    public void setIdJournalRetour(String string) {
        idJournalRetour = string;
    }

    public void setJournal(CPJournalRetour journal) {
        this.journal = journal;
    }

    public void setListIdJournalRetour(String[] listIdJournalRetour) {
        this.listIdJournalRetour = listIdJournalRetour;
    }

    public void setListIdNonTraite(ArrayList<String> listIdNonTraite) {
        this.listIdNonTraite = listIdNonTraite;
    }

    public void setListIdRetour(String[] listIdRetour) {
        this.listIdRetour = listIdRetour;
    }

    public void setListIdTraite(ArrayList<String> listIdTraite) {
        this.listIdTraite = listIdTraite;
    }

    public void setProgressBarInJournalOn(boolean progressBarInJournalOn) {
        this.progressBarInJournalOn = progressBarInJournalOn;
    }

    public void setRetour(ICommunicationRetour retour) {
        this.retour = retour;
    }

    public void setTillNumAffilie(String string) {
        tillNumAffilie = string;
    }

}
