package globaz.naos.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliationJSection;
import globaz.naos.db.affiliation.AFAffiliationJSectionManager;
import globaz.naos.listes.excel.AFXmlmlSoldesAffiliesRadies;
import globaz.naos.util.AFUtil;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.CommonExcelmlUtils;

public class AFSoldesAffiliesRadiesProcess extends BProcess {

    private static final long serialVersionUID = -7918150287789795921L;
    private String fromDate = "";
    private AFXmlmlSoldesAffiliesRadies xmlml = null;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        boolean status = true;

        try {
            // Création des données
            createDataForExcel();

            // Création du fichier excel
            createListExcel();

        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("EXECUTION_IMP_LISTE_SOLDES_AF_RADIES_ERREUR"));

            AFUtil.addMailInformationsError(getMemoryLog(), AFUtil.stack2string(e), this.getClass().getName());

            status = false;
        }

        return status;
    }

    private void createDataForExcel() throws Exception {

        xmlml = new AFXmlmlSoldesAffiliesRadies();

        // Création des informations sur le document
        xmlml.putData(AFXmlmlSoldesAffiliesRadies.DATA_EXCEL_DATE_DOC, JACalendar.todayJJsMMsAAAA());
        xmlml.putData(AFXmlmlSoldesAffiliesRadies.DATA_EXCEL_DATE_A_PARTIR, getFromDate());
        xmlml.putData(AFXmlmlSoldesAffiliesRadies.DATA_EXCEL_TITRE_DOC,
                getSession().getLabel("NAOS_AFFILIES_SOLDE_AFF_RAD_TITRE"));

        AFAffiliationJSectionManager manager = new AFAffiliationJSectionManager();
        manager.setSession(getSession());
        manager.setForSectionsOuvertes(Boolean.TRUE);
        manager.setFromDateFinNonVide(getFromDate());
        manager.forIsTraitement(false);
        manager.find(BManager.SIZE_NOLIMIT);

        Object[] obj_listAf = manager.getContainer().toArray();

        // Pour chaque ligne de la requete
        for (Object obj_af : obj_listAf) {
            AFAffiliationJSection aff = (AFAffiliationJSection) obj_af;

            String numAffilie = aff.getAffilieNumero();
            String nom = aff.getTiers().getPrenomNom();
            String dateRadiation = aff.getDateFin();
            String periode = "";
            if (!JadeStringUtil.isEmpty(aff.getDateDebutParticularite())
                    || !JadeStringUtil.isEmpty(aff.getDateFinParticularite())) {
                periode = aff.getDateDebutParticularite() + " - " + aff.getDateFinParticularite();
            }

            String numSection = aff.getSection().getIdExterne();
            String dateSection = aff.getSection().getDateSection();
            String descSection = aff.getSection().getDescription();
            String solde = aff.getSection().getSolde();

            xmlml.createLigne(numAffilie, nom, dateRadiation, periode, numSection, dateSection, descSection, solde);
        }

    }

    private boolean createListExcel() throws Exception {
        CommonExcelmlContainer container = xmlml.loadResults();

        if (isAborted()) {
            return false;
        }

        String xmlModelPath = Jade.getInstance().getExternalModelDir() + AFApplication.DEFAULT_APPLICATION_NAOS_REP
                + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                + AFXmlmlSoldesAffiliesRadies.XLS_DOC_NAME + "Modele.xml";

        String xlsDocPath = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(AFXmlmlSoldesAffiliesRadies.XLS_DOC_NAME + ".xml");

        xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, container);

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfoExcel.setDocumentTitle(AFXmlmlSoldesAffiliesRadies.XLS_DOC_NAME);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(AFXmlmlSoldesAffiliesRadies.NUMERO_INFOROM);

        this.registerAttachedDocument(docInfoExcel, xlsDocPath);

        return true;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("IMP_LISTE_SOLDES_AF_RADIES_ERREUR");
        } else {
            return getSession().getLabel("IMP_LISTE_SOLDES_AF_RADIES_OK");
        }
    }

    public String getFromDate() {
        return fromDate;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

}
