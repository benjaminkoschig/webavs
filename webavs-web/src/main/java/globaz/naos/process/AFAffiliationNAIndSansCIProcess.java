package globaz.naos.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliationJTiersCI;
import globaz.naos.db.affiliation.AFAffiliationJTiersCIManager;
import globaz.naos.itext.affiliation.AFAffiliationNAIndSansCI_DocListe;
import globaz.naos.listes.excel.AFXmlmlAffiliationNAIndSansCI;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.alternate.TIPAvsAdrLienAdmin;
import globaz.pyxis.db.alternate.TIPAvsAdrLienAdminManager;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.CommonExcelmlUtils;
import java.io.IOException;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFAffiliationNAIndSansCIProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String typeImpression = "pdf";
    private AFXmlmlAffiliationNAIndSansCI xmlml = null;

    /**
     * Crée une nouvelle instance de la classe AFAffiliationNAIndSansCIProcess.
     */
    public AFAffiliationNAIndSansCIProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe AFAffiliationNAIndSansCIProcess.
     * 
     * @param parent
     */
    public AFAffiliationNAIndSansCIProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe AFAffiliationNAIndSansCIProcess.
     * 
     * @param session
     */
    public AFAffiliationNAIndSansCIProcess(BSession session) {
        super(session);
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

        boolean status = true;

        if ("pdf".equals(getTypeImpression())) {

            status = createPdf();

        } else {

            // Création des données
            createDataForExcel();

            // Création du fichier excel
            status = createListExcel();
        }

        return status;
    }

    /**
     * Création des données pour la liste
     * 
     * @throws Exception
     */
    private boolean createDataForExcel() {

        xmlml = new AFXmlmlAffiliationNAIndSansCI();

        // Création des informations sur le document
        xmlml.putData(AFXmlmlAffiliationNAIndSansCI.DATA_EXCEL_DATE_DOC, JACalendar.todayJJsMMsAAAA());
        xmlml.putData(AFXmlmlAffiliationNAIndSansCI.DATA_EXCEL_TITRE_DOC,
                getSession().getLabel("NAOS_AFFILIES_IND_NA_SANS_CI_TITRE"));

        // Création des données de la liste
        AFAffiliationJTiersCIManager manager = new AFAffiliationJTiersCIManager();
        manager.setSession(getSession());
        manager.forAucunCIOuvert(Boolean.TRUE);
        manager.setForTypeAffiliation(new String[] { CodeSystem.TYPE_AFFILI_INDEP, CodeSystem.TYPE_AFFILI_INDEP_EMPLOY,
                CodeSystem.TYPE_AFFILI_NON_ACTIF, CodeSystem.TYPE_AFFILI_SELON_ART_1A, CodeSystem.TYPE_AFFILI_TSE,
                CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE });

        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            return false;
        }

        Object[] obj_listAf = manager.getContainer().toArray();

        // Pour chaque ligne de la requete
        for (Object obj_af : obj_listAf) {
            AFAffiliationJTiersCI aff = (AFAffiliationJTiersCI) obj_af;

            String agence = retrieveAgence(aff.getIdTiers());
            String numAffilie = aff.getAffilieNumero();
            String numAvs = aff.getNumAVS();
            String nom = aff.getPrenomNom();
            String type = getSession().getCodeLibelle(aff.getTypeAffiliation());
            String dateDebut = aff.getDateDebut();

            xmlml.createLigne(numAffilie, numAvs, nom, type, dateDebut, agence);
        }

        return true;
    }

    private boolean createListExcel() {
        CommonExcelmlContainer container = xmlml.loadResults();

        if (isAborted()) {
            return false;
        }

        String xmlModelPath = Jade.getInstance().getExternalModelDir() + AFApplication.DEFAULT_APPLICATION_NAOS_REP
                + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                + AFXmlmlAffiliationNAIndSansCI.XLS_DOC_NAME + "Modele.xml";

        String xlsDocPath = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(AFXmlmlAffiliationNAIndSansCI.XLS_DOC_NAME + ".xml");

        try {
            xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, container);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            return false;
        }

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfoExcel.setDocumentTitle(AFXmlmlAffiliationNAIndSansCI.XLS_DOC_NAME);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(AFXmlmlAffiliationNAIndSansCI.NUMERO_INFOROM);
        try {
            this.registerAttachedDocument(docInfoExcel, xlsDocPath);
        } catch (IOException e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            return false;
        }

        return true;
    }

    private boolean createPdf() throws Exception {

        AFAffiliationNAIndSansCI_DocListe doc = new AFAffiliationNAIndSansCI_DocListe(getSession());
        doc.setEMailAddress(getEMailAddress());
        doc.setParent(this);

        try {

            doc.executeProcess();

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            abort();

            return false;
        }

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("IMP_LISTE_NA_SANS_CI_ERREUR");
        } else {
            return getSession().getLabel("IMP_LISTE_NA_SANS_CI_OK");
        }
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Récupération de l'agence de l'affilié
     * 
     * @param idTiers
     * @return
     */
    private String retrieveAgence(String idTiers) {
        String nomAgence = "";
        try {
            TIPAvsAdrLienAdminManager lienAgence = new TIPAvsAdrLienAdminManager();
            lienAgence.setSession(getSession());
            lienAgence.setForIdTiers(idTiers);
            lienAgence.setForGenreAdministration(AFAffiliationNAIndSansCI_DocListe.AGENCE_COMMUNALE);

            lienAgence.find();
            if (lienAgence.size() != 0) {
                TIPAvsAdrLienAdmin agence = (TIPAvsAdrLienAdmin) lienAgence.getFirstEntity();
                nomAgence = agence.getDesignation2Admin();
            }
        } catch (Exception exception) {
        }

        return nomAgence;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }
}
