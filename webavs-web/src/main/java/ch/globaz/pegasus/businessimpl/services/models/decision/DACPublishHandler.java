package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.print.server.JadePrintDocumentContainer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.globaz.pegasus.business.constantes.decision.DecisionTypes;
import ch.globaz.pegasus.businessimpl.services.models.decision.ged.DACGedHandler;

public class DACPublishHandler {

    private DACPublishHandler() {
    }

    public static DACPublishHandler getInstanceForStandardPrint(ArrayList<String> decisionsId, String mailGest,
            String dateDoc, String persRef, Boolean isForFtp, Boolean includeGed) {

        DACPublishHandler handler = new DACPublishHandler();
        handler.setDecisionsId(decisionsId);
        handler.setMailGest(mailGest);
        handler.setDateDoc(dateDoc);
        handler.setPersref(persRef);
        handler.setIsForFtp(isForFtp);
        handler.setForGed(includeGed);
        return handler;
    }

    public static DACPublishHandler getInstanceForGedPrintOnly(ArrayList<String> decisionsId, String mailGest,
            String dateDoc, String persRef, DACGedHandler gedHandler) {

        DACPublishHandler handler = new DACPublishHandler();
        handler.setDecisionsId(decisionsId);
        handler.setMailGest(mailGest);
        handler.setDateDoc(dateDoc);
        handler.setPersref(persRef);
        handler.setIsForFtp(false);
        handler.setForGed(true);
        handler.setGedHandler(gedHandler);
        return handler;
    }

    public static DACPublishHandler getInstanceForFTPPrintOnly(ArrayList<String> decisionsId, String mailGest,
            String dateDoc, String persRef) {

        DACPublishHandler handler = new DACPublishHandler();
        handler.setDecisionsId(decisionsId);
        handler.setMailGest(mailGest);
        handler.setDateDoc(dateDoc);
        handler.setPersref(persRef);
        handler.setIsForFtp(true);
        handler.setForGed(false);
        return handler;
    }
    public static DACPublishHandler getInstanceForAdaptationAnnuel(ArrayList<String> decisionsId, String mailGest,
                                                                String dateDoc, String persRef, Boolean isForGed,Boolean isForFtp, Boolean isFromAdaptation,String idProcessPC, DACGedHandler gedHandler) {

        DACPublishHandler handler = new DACPublishHandler();
        handler.setDecisionsId(decisionsId);
        handler.setMailGest(mailGest);
        handler.setDateDoc(dateDoc);
        handler.setPersref(persRef);
        handler.setIsForFtp(isForFtp);
        handler.setForGed(isForGed);
        handler.setGedHandler(gedHandler);
        handler.setFromAdaptation(isFromAdaptation);
        handler.setIdProcessusPC(idProcessPC);
        return handler;
    }

    private DACGedHandler gedHandler = null;

    public DACGedHandler getGedHandler() {
        return gedHandler;
    }

    public void setGedHandler(DACGedHandler gedHandler) {
        this.gedHandler = gedHandler;
    }

    /* Date docment */
    private String dateDoc = null;
    /* Liste des décisions (id) */
    private ArrayList<String> decisionsId = null;
    /* Type de la decision */
    private DecisionTypes decisionType = null;
    private Boolean forGed = false;
    /* for ftp */
    private Boolean isForFtp = false;

    /* email gestionnaire */
    private String mailGest = null;

    /* personne de référence */
    private String persref = null;

    /* ou id seul pour impression seul */
    private String uniqueDecisionId = null;

    private String idProcessusPC = null;

    private Boolean isFromAdaptation = false;

    private JadePrintDocumentContainer containerPublication = null;
    private ArrayList<JadePrintDocumentContainer> conrtainersGed = new ArrayList<JadePrintDocumentContainer>();

    public String getDateDoc() {
        return dateDoc;
    }

    public void setDateDoc(String dateDoc) {
        this.dateDoc = dateDoc;
    }

    public ArrayList<String> getDecisionsId() {
        return decisionsId;
    }

    public void setDecisionsId(ArrayList<String> decisionsId) {
        this.decisionsId = decisionsId;
    }

    public DecisionTypes getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(DecisionTypes decisionType) {
        this.decisionType = decisionType;
    }

    public Boolean getForGed() {
        return forGed;
    }

    public void setForGed(Boolean forGed) {
        this.forGed = forGed;
    }

    public Boolean getIsForFtp() {
        return isForFtp;
    }

    public void setIsForFtp(Boolean isForFtp) {
        this.isForFtp = isForFtp;
    }

    public String getMailGest() {
        return mailGest;
    }

    public void setMailGest(String mailGest) {
        this.mailGest = mailGest;
    }

    public String getPersref() {
        return persref;
    }

    public void setPersref(String persref) {
        this.persref = persref;
    }

    public String getUniqueDecisionId() {
        return uniqueDecisionId;
    }

    public void setUniqueDecisionId(String uniqueDecisionId) {
        this.uniqueDecisionId = uniqueDecisionId;
    }

    public JadePrintDocumentContainer getContainerPublication() {
        return containerPublication;
    }

    public void setContainerPublication(JadePrintDocumentContainer containerPublication) {
        this.containerPublication = containerPublication;
    }

    public List<JadePrintDocumentContainer> getContainersGed() {
        return Collections.unmodifiableList(conrtainersGed);
    }

    public void addGedContainer(JadePrintDocumentContainer containerGed) {
        conrtainersGed.add(containerGed);
    }
    public String getIdProcessusPC() {
        return idProcessusPC;
    }

    public void setIdProcessusPC(String idProcessusPC) {
        this.idProcessusPC = idProcessusPC;
    }
    public Boolean getFromAdaptation() {
        return isFromAdaptation;
    }

    public void setFromAdaptation(Boolean fromAdaptation) {
        isFromAdaptation = fromAdaptation;
    }

}
