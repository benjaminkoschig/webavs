/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.process;

import globaz.corvus.process.REGenererDemandeCalculProvisoireProcess;
import globaz.cygnus.services.RFSetEtatProcessService;
import globaz.cygnus.services.genererDecision.RFGenererDecisionRestitutionService;
import globaz.cygnus.services.validerDecision.RFDecisionDocumentData;
import globaz.cygnus.services.validerDecision.RFSimulerValidationService;
import globaz.cygnus.services.validerDecision.RFValiderDecisionServiceFactory;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BTransaction;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFValiderDecisionsNonValideesProcess extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateSurDocument = "";
    private DocumentData docData = new DocumentData();
    private String emailAdress = "";
    private String idGestionnaire = "";
    private Boolean isMiseEnGed = Boolean.FALSE;
    private Boolean isSimulationValidation = Boolean.FALSE;
    private FWMemoryLog memoryLog = new FWMemoryLog();
    private StringBuffer pdfDecisionURL = new StringBuffer("null");

    /**
     * Crée une nouvelle instance de la classe RFValiderDecisionsProcess.
     */
    public RFValiderDecisionsNonValideesProcess() {
        super();
    }

    private JadePublishDocumentInfo createDocInfoDecompte() {

        JadePublishDocumentInfo documentInfo = JadePublishDocumentInfoProvider.newInstance(this);
        documentInfo.setOwnerEmail(getEmailAdress());
        documentInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdress());
        documentInfo.setDocumentTitle(getSession().getLabel("PROCESS_SIMULER_VALIDATION_DECISION"));
        documentInfo.setArchiveDocument(false);
        documentInfo.setPublishDocument(false);
        documentInfo.setDocumentSubject(getSession().getLabel("PROCESS_SIMULER_VALIDATION_DECISION"));

        return documentInfo;
    }

    /**
     * Methode pour envoyer un mail supplémentaire avec les décisions n'ayant pas de modèle.
     * 
     * Ce message ne peut être contenu dans le même mail que les PDF, car si il y a uniquement des décisions sans
     * modèle(template), aucun mail n'est envoyé.
     * 
     * 
     * @throws Exception
     */
    private void envoyerMailDecisionsSansModeles() throws Exception {

        String titre = getSession().getLabel("PROCESS_VALIDER_DECISION_TITRE_DECISIONS_REFUSEES") + "\n"
                + getSession().getLabel("PROCESS_VALIDER_DECISION_TEXTE_DECISIONS_REFUSEES") + "\n";

        getLogSession().addMessage(
                new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, REGenererDemandeCalculProvisoireProcess.class
                        .getName(), titre + getMessagesDecisionsSansModel()));

        List<String> list = new ArrayList<String>();
        list.add(emailAdress);
        sendCompletionMail(list);
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public DocumentData getDocData() {
        return docData;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public Boolean getIsMiseEnGed() {
        return isMiseEnGed;
    }

    public Boolean getIsSimulationValidation() {
        return isSimulationValidation;
    }

    public FWMemoryLog getMemoryLog() {
        return memoryLog;
    }

    /**
     * Methode pour itérer sur les FWMessage contenus dans le memoryLog. Les FWMessage, contiennent les infos sur les
     * décisions qui n'ont pas de modèle de document.
     * 
     * @return
     */
    private String getMessagesDecisionsSansModel() {
        String message = "";
        Iterator<FWMessage> itr = memoryLog.getMessagesToVector().iterator();
        while (itr.hasNext()) {
            FWMessage fwMessage = itr.next();
            message = message + fwMessage.getComplement();
        }

        return message;
    }

    @Override
    public String getName() {
        return null;
    }

    public String getObjetMail() {

        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("PROCESS_VALIDER_DECISIONS_FAILED");
        } else {
            if (getIsSimulationValidation()) {
                return getSession().getLabel("PROCESS_SIMULER_DECISIONS_SUCCESS");
            } else {
                return getSession().getLabel("PROCESS_VALIDER_DECISIONS_SUCCESS");
            }
        }
    }

    public StringBuffer getPdfDecisionURL() {
        return pdfDecisionURL;
    }

    private void initTransaction() throws Exception {
        BITransaction transaction = new BTransaction(getSession());
        transaction.openTransaction();
        setTransaction((BTransaction) transaction);
    }

    @Override
    public void run() {

        try {

            initTransaction();
            RFSetEtatProcessService.setEtatProcessValiderDecision(true, getSession());
            ArrayList<RFDecisionDocumentData> decisionArray = new ArrayList<RFDecisionDocumentData>();

            this.createDocuments(RFValiderDecisionServiceFactory.getRFValiderDecisionNonValideeService()
                    .genererDecisionDocument(getDateSurDocument(), getDocData(), getEmailAdress(), getIdGestionnaire(),
                            getIsMiseEnGed(), getIsSimulationValidation(), getMemoryLog(), getPdfDecisionURL(),
                            decisionArray, this, getSession()));

            // Après génération des documents, suppression des décisions de restitution dans decisionArray
            RFGenererDecisionRestitutionService rfDecisionRestitutionService = new RFGenererDecisionRestitutionService();
            decisionArray = rfDecisionRestitutionService.removeDecisionsRestitutionListe(decisionArray);

            RFSimulerValidationService.lancerSimulationValidation(getEmailAdress(), getIdGestionnaire(),
                    getIsSimulationValidation(), createDocInfoDecompte(), "", getSession(), getTransaction(),
                    Boolean.TRUE, RFPropertiesUtils.getIdTiersAvanceSas(), "");

            RFValiderDecisionServiceFactory.getRFValiderDecisionNonValideeService().miseAJourEntites(decisionArray,
                    getDateSurDocument(), getIsSimulationValidation(), getIdGestionnaire(), getSession(),
                    getTransaction());

            RFValiderDecisionServiceFactory.getRFValiderDecisionNonValideeService().envoyerMail(getEmailAdress(),
                    getObjetMail(),
                    RFSimulerValidationService.getMemoryLog().getMessagesInHtml().replaceAll("</br>", "<br>"),
                    RFSimulerValidationService.getDocPath());

            // MessageToVector est renseigné par les décisions sans modèle de documents.
            if (memoryLog.getMessagesToVector().size() > 0) {
                envoyerMailDecisionsSansModeles();
            }

        } catch (Exception e1) {
            RFValiderDecisionServiceFactory.getRFValiderDecisionNonValideeService().logErreurs(getEmailAdress(),
                    getMemoryLog(), e1, this, getSession(), getTransaction());

        } finally {
            RFValiderDecisionServiceFactory.getRFValiderDecisionNonValideeService().commitTransaction(getTransaction(),
                    getIsSimulationValidation());

            try {
                RFSetEtatProcessService.setEtatProcessValiderDecision(false, getSession());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setDocData(DocumentData docData) {
        this.docData = docData;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIsMiseEnGed(Boolean isMiseEnGed) {
        this.isMiseEnGed = isMiseEnGed;
    }

    public void setIsSimulationValidation(Boolean isSimulationValidation) {
        this.isSimulationValidation = isSimulationValidation;
    }

    public void setMemoryLog(FWMemoryLog memoryLog) {
        this.memoryLog = memoryLog;
    }

    public void setPdfDecisionURL(StringBuffer pdfDecisionURL) {
        this.pdfDecisionURL = pdfDecisionURL;
    }
}
