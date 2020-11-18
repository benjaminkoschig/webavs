package globaz.pegasus.process.decision;

import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pegasus.process.PCAbstractJob;
import globaz.pegasus.process.lot.ComptabiliserProcessMailHandler;
import globaz.pegasus.process.lot.ComptabiliserProcessMailHandler.PROCESS_TYPE;

import java.util.ArrayList;
import java.util.List;

import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.pegasus.business.constantes.decision.DecisionTypes;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.decision.DecisionBuilder;
import ch.globaz.pegasus.businessimpl.services.models.decision.DACPublishHandler;
import ch.globaz.pegasus.businessimpl.services.models.decision.ged.DACGedHandler;

public class PCImprimerDecisionsProcess extends PCAbstractJob {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /* Date docment */
    private String dateDoc = null;
    /* Type de la decision */
    private DecisionTypes decisionType = null;
    private Boolean forGed = Boolean.FALSE;

    private DACGedHandler gedHandler = null;

    private ArrayList<String> idDecisionsToPrint;

    private String idLot = null;

    /* for ftp */
    private Boolean isForFtp = Boolean.FALSE;
    private Boolean isForFtpValid = Boolean.FALSE;

    private Boolean isForLot = Boolean.FALSE;
    private Boolean isFromAdaptation = Boolean.FALSE;
    private String idProcessusPC = null;

    /* email gestionnaire */
    private String mailGest = null;

    /* personne de référence */
    private String persref = null;

    public void addDecisionToPrint(String idDecision) {
        if (idDecisionsToPrint == null) {
            idDecisionsToPrint = new ArrayList<String>();
        }
        idDecisionsToPrint.add(idDecision);
    }

    /**
     * Génération des documents présents dans le container. Le but est de créer une publication, donc une tâche
     * d'impression par décisions présente dans le handler
     *
     * @param publisherHandler le handler contenant les documents
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     */
    private void createDecisionsByLot(DACPublishHandler publisherHandler) throws JadeServiceLocatorException,
            JadeServiceActivatorException, JadeClassCastException {
        for (JadePrintDocumentContainer containerGed : publisherHandler.getContainersGed()) {
            this.createDocuments(containerGed);
        }
    }

    private void generateDecisionsForLot() throws Exception {
        DACGedHandler globalLotGedHandler = DACGedHandler.getInstanceForTraitementPourLot(idLot, persref, getSession(),
                idDecisionsToPrint);
        // globalLotGedHandler.setLot(CorvusServiceLocator.getLotService().read(this.idLot));

        DecisionBuilder decBuilder = null;
        ArrayList<String> idsDecisions;

        for (String idDecs : idDecisionsToPrint) {
            decBuilder = PegasusServiceLocator.getDecisionBuilderProvderService().getBuilderFor(decisionType);

            idsDecisions = new ArrayList<String>();
            idsDecisions.add(idDecs);

            DACGedHandler gedHandler = DACGedHandler.getInstanceForTraitementPourLot(idLot, persref, getSession(),
                    idsDecisions);
            DACPublishHandler publisherHandlerForLot = DACPublishHandler.getInstanceForGedPrintOnly(idsDecisions,
                    mailGest, dateDoc, persref, gedHandler);
            decBuilder.build(publisherHandlerForLot);
            // Création des décisions
            createDecisionsByLot(publisherHandlerForLot);

        }
        sendProcessMail(PROCESS_TYPE.MISE_EN_GED, globalLotGedHandler);
    }

    public String getDateDoc() {
        return dateDoc;
    }

    public DecisionTypes getDecisionType() {
        return decisionType;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public Boolean getForGed() {
        return forGed;
    }

    public DACGedHandler getGedHandler() {
        return gedHandler;
    }

    public ArrayList<String> getIdDecisionsIdToPrint() {
        return idDecisionsToPrint;
    }

    public String getIdLot() {
        return idLot;
    }

    public Boolean getIsForFtp() {
        return isForFtp;
    }

    public Boolean getIsForFtpValid() {
        return isForFtpValid;
    }

    public Boolean getIsForLot() {
        return isForLot;
    }

    public String getMailGest() {
        return mailGest;
    }

    @Override
    public String getName() {
        return null;
    }

    public String getPersref() {
        return persref;
    }

    @Override
    protected void process() throws Exception {

        DecisionBuilder dec = null;
        DACPublishHandler publisherHandler = null;
        String exceptionMsg = null;
        try {
            if (decisionType == null) {
                decisionType = DecisionTypes.DECISION_APRES_CALCUL;
            }
            // instance du decision builder
            dec = PegasusServiceLocator.getDecisionBuilderProvderService().getBuilderFor(decisionType);


            /** Impression ftp validation et prevalidation automatique */
            if (isFromAdaptation) {
                DecisionBuilder decBuilder = null;
                decBuilder = PegasusServiceLocator.getDecisionBuilderProvderService().getBuilderFor(decisionType);
                ArrayList<String> idsDecisions;
                DACGedHandler globalLotGedHandler = DACGedHandler.getInstanceForTraitementDecisionsAdaptationAnnuel(idDecisionsToPrint, getSession(),persref);
                sendProcessMailAdaptations(PROCESS_TYPE.ADAPTATION_ANNUEL, idProcessusPC,globalLotGedHandler);
                publisherHandler = DACPublishHandler.getInstanceForAdaptationAnnuel(idDecisionsToPrint, mailGest, dateDoc,
                        persref, false,isForFtp, isFromAdaptation, idProcessusPC,gedHandler);
                decBuilder.buildDecisionForAdaptation(publisherHandler);
                this.createDocuments(publisherHandler.getContainerPublication());
                for (String idDecs : idDecisionsToPrint) {


                    idsDecisions = new ArrayList<String>();
                    idsDecisions.add(idDecs);

                    DACGedHandler gedHandler = DACGedHandler.getInstanceForTraitementDecisionsAdaptationAnnuel(idsDecisions, getSession(),persref);
                    DACPublishHandler publisherHandler2 = DACPublishHandler.getInstanceForAdaptationAnnuel(idsDecisions, mailGest, dateDoc,
                            persref, true,isForFtp, isFromAdaptation, idProcessusPC,gedHandler);
                    decBuilder.build(publisherHandler2);
                    // Création des décisions
                    createDecisionsByLot(publisherHandler2);

                }


//                dec.buildDecisionForAdaptation(publisherHandler);
//                // Lancement des task d 'impression, création du document fusionné
//                this.createDocuments(publisherHandler.getContainerPublication());
//
//                // Création des décisions
//                createDecisionsByLot(publisherHandler);

//                generateDecisionsForLot();

            } else if (isForFtp) {
                publisherHandler = DACPublishHandler.getInstanceForFTPPrintOnly(idDecisionsToPrint, mailGest, dateDoc,
                        persref);

                if (isForFtpValid) {
                    dec.buildForFtpValidation(publisherHandler);
                    // Lancement des task d 'impression, création du document fusionné
                    this.createDocuments(publisherHandler.getContainerPublication());

                    // Création des décisions
                    createDecisionsByLot(publisherHandler);
                } else {
                    dec.buildDecisionsForFtp(publisherHandler);

                    // Création des décisions
                    createDecisionsByLot(publisherHandler);

                }

            }
            /** Impression des décisions du lot --> comptabilisation d'un lot de type décisions */
            else if (isForLot) {

                if (idDecisionsToPrint.size() > 0) {

                    generateDecisionsForLot();
                }

            }
            /** Impression standard via l'écran */
            else {
                publisherHandler = DACPublishHandler.getInstanceForStandardPrint(idDecisionsToPrint, mailGest, dateDoc,
                        persref, isForFtp, forGed);
                dec.build(publisherHandler);

                // Lancement des task d 'impression, création du document fusionné
                this.createDocuments(publisherHandler.getContainerPublication());

                // Création des décisions
                createDecisionsByLot(publisherHandler);
            }

        } catch (Exception ex) {
            this.addError(ex);
            JadeLogger.error(this, ex.getMessage());
            exceptionMsg = ex.toString();
        } finally {

            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                JadeSmtpClient.getInstance().sendMail(mailGest, getSession().getLabel("IMPRIMER_DECISION_ERREUR"),
                        exceptionMsg, null);
            }

        }

    }

    /**
     * Envoi du mail suite au process
     *
     * @param process
     * @param lot
     * @param gedHandler
     * @throws Exception
     */
    private final void sendProcessMail(PROCESS_TYPE process, DACGedHandler gedHandler) throws Exception {
        List<String> mails = new ArrayList<String>();
        if (mailGest == null) {
            mails.add(getSession().getUserEMail());
        } else {
            mails.add(mailGest);
        }

        ComptabiliserProcessMailHandler handler = new ComptabiliserProcessMailHandler(process, gedHandler.getLot(),
                getSession(), getLogSession(), gedHandler);

        handler.sendMail(mails);
    }
    private final void sendProcessMailAdaptations(PROCESS_TYPE process, String idProcessus,DACGedHandler gedHandler) throws Exception {
        List<String> mails = new ArrayList<String>();
        if (mailGest == null) {
            mails.add(getSession().getUserEMail());
        } else {
            mails.add(mailGest);
        }

        ComptabiliserProcessMailHandler handler = new ComptabiliserProcessMailHandler(process,idProcessus,
                getSession(), getLogSession(), gedHandler);

        handler.sendMail(mails);
    }

    public void setDateDoc(String dateDoc) {
        this.dateDoc = dateDoc;
    }

    public void setDecisionType(DecisionTypes decisionType) {
        this.decisionType = decisionType;
    }

    public void setForGed(Boolean forGed) {
        this.forGed = forGed;
    }

    public void setGedHandler(DACGedHandler gedHandler) {
        this.gedHandler = gedHandler;
    }

    public void setIdDecisionsIdToPrint(ArrayList<String> decisionsIdToPrint) {
        idDecisionsToPrint = decisionsIdToPrint;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIsForFtp(Boolean isForFtp) {
        this.isForFtp = isForFtp;
    }

    public void setIsForFtpValid(Boolean isForFtpValid) {
        this.isForFtpValid = isForFtpValid;
    }

    public void setIsForLot(Boolean isForLot) {
        this.isForLot = isForLot;
    }

    public void setMailGest(String mailGest) {
        this.mailGest = mailGest;
    }

    public void setPersref(String persref) {
        this.persref = persref;
    }

    public Boolean getFromAdaptation() {
        return isFromAdaptation;
    }

    public void setFromAdaptation(Boolean fromAdaptation) {
        isFromAdaptation = fromAdaptation;
    }

    public String getIdProcessusPC() {
        return idProcessusPC;
    }

    public void setIdProcessusPC(String idProcessusPC) {
        this.idProcessusPC = idProcessusPC;
    }
}
