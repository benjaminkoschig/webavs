/*
 * Créé le 16 janvier 2012
 */
package globaz.cygnus.services.statistiques;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.mappingXmlml.IRFStatistiquesMontantsSashListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingStatistiquesMontantsSASH;
import globaz.cygnus.process.RFStatistiquesParMontantsSashProcess;
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
public class RFStatistiquesParMontantsSashService {

    private String adresseMail = null;
    private String dateDebutStat = null;
    private String dateFinStat = null;
    private String gestionnaire = null;
    private FWMemoryLog log;
    private List<String[]> logsList = new ArrayList<String[]>();
    private RFStatistiquesParMontantsSashProcess process = null;
    private BSession session = null;
    private BITransaction transaction = null;

    public RFStatistiquesParMontantsSashService(String gestionnaire, BSession session, BITransaction transaction,
            RFStatistiquesParMontantsSashProcess process) {
        super();
        this.gestionnaire = gestionnaire;
        this.session = session;
        this.transaction = transaction;
        this.process = process;
    }

    /**
     * Methode d'execution de la statistique
     * 
     * @throws Exception
     */
    public void executerStatistiquesParMontantSash() throws Exception {

        String nomDoc = "";

        RFXmlmlMappingStatistiquesMontantsSASH.setSession(session);
        RFXmlmlMappingStatistiquesMontantsSASH.setDateDebutStat(dateDebutStat);
        RFXmlmlMappingStatistiquesMontantsSASH.setDateFinStat(dateFinStat);
        RFXmlmlMappingStatistiquesMontantsSASH.setGestionnaire(gestionnaire);
        RFXmlmlContainer container = RFXmlmlMappingStatistiquesMontantsSASH.loadResults(process);

        nomDoc = getSession().getLabel("PROCESS_SUCCES_STATISTIQUES_PAR_MONTANTS_SASH");
        String docPath = RFExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + IRFStatistiquesMontantsSashListeColumns.MODEL_NAME, nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = new JadePublishDocumentInfo();
        docInfo.setApplicationDomain(RFApplication.DEFAULT_APPLICATION_CYGNUS);
        docInfo.setOwnerEmail(adresseMail);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setDocumentSubject(nomDoc);
        docInfo.setDocumentNotes(getSession().getLabel("PROCESS_MESSAGE_STATISTIQUES_PAR_MONTANTS"));
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