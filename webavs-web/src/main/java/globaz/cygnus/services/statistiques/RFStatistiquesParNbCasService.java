/*
 * Créé le 16 janvier 2012
 */
package globaz.cygnus.services.statistiques;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.mappingXmlml.IRFStatistiquesParNbCasListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingStatistiquesParNbCas;
import globaz.cygnus.process.RFStatistiquesParNbCasProcess;
import globaz.cygnus.utils.RFExcelmlUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MBO
 */
public class RFStatistiquesParNbCasService {

    private String adresseMail = null;
    private String dateDebutStat = null;
    private String dateFinStat = null;
    private String gestionnaire = null;
    private FWMemoryLog log;
    private List<String[]> logsList = new ArrayList<String[]>();
    private RFStatistiquesParNbCasProcess process = null;
    private BSession session = null;
    private BITransaction transaction = null;

    public RFStatistiquesParNbCasService(String gestionnaire, BSession session, BITransaction transaction,
            RFStatistiquesParNbCasProcess process) {
        super();
        this.gestionnaire = gestionnaire;
        this.session = session;
        this.transaction = transaction;
        this.process = process;
    }

    /**
     * Initialisation du contexte
     * 
     * @throws Exception
     */
    public void executerStatistiquesParNbCas() throws Exception {

        RFXmlmlMappingStatistiquesParNbCas.setSession(session);
        RFXmlmlMappingStatistiquesParNbCas.setDateDebutStat(dateDebutStat);
        RFXmlmlMappingStatistiquesParNbCas.setDateFinStat(dateFinStat);
        RFXmlmlMappingStatistiquesParNbCas.setGestionnaire(gestionnaire);
        RFXmlmlContainer container = RFXmlmlMappingStatistiquesParNbCas.loadResults(process);

        String nomDoc = getSession().getLabel("PROCESS_SUCCES_STATISTIQUES_PAR_NB_CAS");
        String docPath = RFExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + IRFStatistiquesParNbCasListeColumns.MODEL_NAME, nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = new JadePublishDocumentInfo();
        docInfo.setApplicationDomain(RFApplication.DEFAULT_APPLICATION_CYGNUS);
        docInfo.setOwnerEmail(adresseMail);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setDocumentSubject(nomDoc);
        docInfo.setDocumentNotes(getSession().getLabel("PROCESS_MESSAGE_STATISTIQUES_PAR_NB_CAS"));
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        process.registerAttachedDocument(docInfo, docPath);

    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public String getDateDebutStat() {
        return dateDebutStat;
    }

    public String getDateFinStat() {
        return dateFinStat;
    }

    public String getGestionnaire() {
        return gestionnaire;
    }

    public BSession getSession() {
        return session;
    }

    public BITransaction getTransaction() {
        return transaction;
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public void setDateDebutStat(String dateDebutStat) {
        this.dateDebutStat = dateDebutStat;
    }

    public void setDateFinStat(String dateFinStat) {
        this.dateFinStat = dateFinStat;
    }

    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    public void setLogsList(List<String[]> logsList) {
        this.logsList = logsList;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTransaction(BITransaction transaction) {
        this.transaction = transaction;
    }

}