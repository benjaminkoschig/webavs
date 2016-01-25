/**
 *
 */
package globaz.apg.process;

import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.itext.APDecisionCommunicationAMAT;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.itext.PRLettreEnTete;

/**
 * @author FGO
 */
public class APGenererDecisionCommunicationAMATProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DOC_DEC_AMAT_COPIE_ASS = "documents.decision.amat.copie.assure";
    private String csTypeDocument = "";
    private String date = "";
    private PRDemande demande = null;
    private String displaySendToGed = "0";
    private APDroitMaternite droit = null;
    private String idDroit = "";
    private Boolean isSendToGed = Boolean.FALSE;
    private boolean succeed = true;

    /**
     * Constructeur
     */
    public APGenererDecisionCommunicationAMATProcess() {
    }

    /**
     * Constructeur
     * 
     * @param parent
     */
    public APGenererDecisionCommunicationAMATProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur
     * 
     * @param session
     */
    public APGenererDecisionCommunicationAMATProcess(BSession session) {
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
    protected boolean _executeProcess() throws Exception {

        // Chargement des propriétés
        chargeProprietes();
        // Envoi de la décision - communication
        APDecisionCommunicationAMAT decisionOriginale = createDecisionOriginale();
        Boolean paramCopie = new Boolean(getSession().getApplication().getProperty(DOC_DEC_AMAT_COPIE_ASS));
        if (decisionOriginale.isCreateDocumentCopie() && paramCopie.booleanValue()) {
            // Création du document en-tête
            createLettreEntete(demande.getIdTiers());
            // Création de la lettre de copie à l'assuré
            createDecisionCopy();
        }
        // Fusionne les documents ci-dessus
        fusionDocuments();
        if (decisionOriginale.isOnError()) {
            succeed = false;
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_ENVOI_FAILED"), FWMessage.INFORMATION, "-->");
        } else {
            succeed = true;
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_ENVOI_SUCCESS"), FWMessage.INFORMATION, "-->");
        }
        return succeed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);

    }

    /**
     * Chargement de la demande
     * 
     * @throws Exception
     */
    private void chargeDemande() throws Exception {
        try {
            demande = new PRDemande();
            demande.setSession(getSession());
            demande.setIdDemande(droit.getIdDemande());
            demande.retrieve();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                    "APGenererDecisionCommunicationAMATProcess.chargeDemande()");
            abort();
        }
    }

    /**
     * Chargement du droit
     * 
     * @throws Exception
     */
    private void chargeDroit() throws Exception {
        try {
            droit = new APDroitMaternite();
            droit.setSession(getSession());
            droit.setIdDroit(getIdDroit());
            droit.retrieve();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                    "APGenererDecisionCommunicationAMATProcess.chargeDroit()");
            abort();
        }
    }

    /**
     * Chargement des propriétés
     * 
     * @throws Exception
     */
    private void chargeProprietes() throws Exception {
        chargeDroit();
        chargeDemande();
    }

    /**
     * @return
     * @throws FWIException
     * @throws Exception
     */
    private void createDecisionCopy() throws FWIException, Exception {
        APDecisionCommunicationAMAT decisionCopy = new APDecisionCommunicationAMAT();
        decisionCopy.setSession(getSession());
        decisionCopy.setEMailAddress(getEMailAddress());
        decisionCopy.setCsTypeDocument(getCsTypeDocument());
        decisionCopy.setDate(getDate());
        decisionCopy.setIdDroit(getIdDroit());
        decisionCopy.setIsSendToGed(getIsSendToGed());
        decisionCopy.setParent(this);
        decisionCopy.setDocumentCopy(true);
        decisionCopy.executeProcess();
    }

    /**
     * @return
     * @throws FWIException
     * @throws Exception
     */
    private APDecisionCommunicationAMAT createDecisionOriginale() throws FWIException, Exception {
        APDecisionCommunicationAMAT decisionOriginale = new APDecisionCommunicationAMAT();
        decisionOriginale.setSession(getSession());
        decisionOriginale.setEMailAddress(getEMailAddress());
        decisionOriginale.setCsTypeDocument(getCsTypeDocument());
        decisionOriginale.setDate(getDate());
        decisionOriginale.setIdDroit(getIdDroit());
        decisionOriginale.setParent(this);
        decisionOriginale.setDocumentCopy(false);
        decisionOriginale.setIsSendToGed(getIsSendToGed());
        decisionOriginale.executeProcess();

        return decisionOriginale;
    }

    /**
     * Création de la lettre d'entête
     * 
     * @param idTiers
     * @return
     * @throws FWIException
     * @throws Exception
     */
    private PRLettreEnTete createLettreEntete(String idTiers) throws FWIException, Exception {
        PRLettreEnTete lettreEnTete = new PRLettreEnTete();
        lettreEnTete.setSession(getSession());
        // retrieve du tiers
        PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(getSession(), idTiers);
        lettreEnTete.setTierAdresse(tier);
        // pour l'instant, les copies sont uniquement adressées aux assurés,
        // donc pas d'idAffilié
        lettreEnTete.setIdAffilie("");
        lettreEnTete.setEMailAddress(getEMailAddress());
        lettreEnTete.setDomaineLettreEnTete(PRLettreEnTete.DOMAINE_MAT);
        lettreEnTete.setParent(this);
        lettreEnTete.executeProcess();
        return lettreEnTete;
    }

    /**
     * Fusion des documents
     * 
     * @throws Exception
     */
    private void fusionDocuments() throws Exception {
        JadePublishDocumentInfo info = createDocumentInfo();
        info.setPublishDocument(true);
        info.setArchiveDocument(false);
        mergePDF(info, false, 500, false, null);
    }

    /**
     * Récupère le code système du type de document
     * 
     * @return
     */
    public String getCsTypeDocument() {
        return csTypeDocument;
    }

    /**
     * Récupère la date
     * 
     * @return
     */
    public String getDate() {
        return date;
    }

    public String getDisplaySendToGed() {
        return displaySendToGed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        StringBuffer str = new StringBuffer();
        if (getMemoryLog().isOnErrorLevel() && !succeed) {
            str = new StringBuffer(getSession().getLabel("PROCESS_ENVOI_FAILED"));
        } else {
            str = new StringBuffer(getSession().getLabel("PROCESS_ENVOI_SUCCESS"));
        }
        str.append(" - (").append(JACalendar.todayJJsMMsAAAA()).append(")");
        return str.toString();
    }

    /**
     * Récupère l'id du droit
     * 
     * @return
     */
    public String getIdDroit() {
        return idDroit;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * Modifie le code système du type de document
     * 
     * @param newCsTypeDocument
     */
    public void setCsTypeDocument(String newCsTypeDocument) {
        csTypeDocument = newCsTypeDocument;
    }

    /**
     * Modifie la date
     * 
     * @param newDate
     */
    public void setDate(String newDate) {
        date = newDate;
    }

    public void setDisplaySendToGed(String displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    /**
     * Modifie l'id du droit
     * 
     * @param newIdDroit
     */
    public void setIdDroit(String newIdDroit) {
        idDroit = newIdDroit;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

}
