package globaz.osiris.process;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.cumulcotisations.CACumulCotisationsParAnnee;
import globaz.osiris.db.cumulcotisations.CACumulCotisationsParAnneeManager;
import globaz.osiris.print.CAXmlmlListCumulCotisationParAnnee;
import globaz.osiris.utils.CAOsirisContainer;
import globaz.webavs.common.CommonExcelmlUtils;
import java.util.StringTokenizer;

public class CAProcessListCumulCotisationParAnnee extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String fromDateValeur = "";
    private String fromIdExterne = "";
    private boolean status = true;
    private FWCurrency sumMasseTotal = new FWCurrency();
    private FWCurrency sumMontantTotal = new FWCurrency();
    private String toDateValeur = "";
    private String toIdExterne = "";
    private String typeImpression = "pdf";
    private CAXmlmlListCumulCotisationParAnnee xmlml = null;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        xmlml = new CAXmlmlListCumulCotisationParAnnee();

        try {
            createEntete();

            if (isAborted()) {
                return false;
            }

            createDataExcel();

            if (isAborted()) {
                return false;
            }

            createListExcel();

        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("EXECUTION_LIST_CUMUL_COTI_ANNEE_ERREUR"));

            String messageInformation = CEUtils.stack2string(e);

            addMailInformationsError(messageInformation, this.getClass().getName());
            status = false;
        }

        return status;
    }

    /**
     * Permet de créer une partie informations pour globaz afin de mieux debugger le process en cas de probleme
     * 
     * @param message
     */
    public void addMailInformationsError(String _message, String source) {

        String message = "\n***** INFORMATIONS POUR GLOBAZ *****\n";
        message += _message;

        // Parcourir des informations
        StringTokenizer str = new StringTokenizer(message, "\n\r");
        while (str.hasMoreElements()) {
            getMemoryLog().logMessage(str.nextToken(), FWMessage.INFORMATION, source);
        }
    }

    private void createDataExcel() throws Exception {

        CACumulCotisationsParAnneeManager manager = new CACumulCotisationsParAnneeManager();
        manager.setSession(getSession());

        manager.setFromIdExterne(getFromIdExterne());
        manager.setToIdExterne(getToIdExterne());

        manager.setFromDateValeur(JACalendar.format(getFromDateValeur(), JACalendar.FORMAT_YYYYMMDD));
        manager.setToDateValeur(JACalendar.format(getToDateValeur(), JACalendar.FORMAT_YYYYMMDD));

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        Object[] list_entity = manager.getContainer().toArray();

        for (Object obj_entity : list_entity) {
            CACumulCotisationsParAnnee cumulCotisations = (CACumulCotisationsParAnnee) obj_entity;

            xmlml.createLigne(cumulCotisations.getIdExterne(), cumulCotisations.getAnneeCotisation(),
                    cumulCotisations.getSumMontant(), cumulCotisations.getSumMasse());

            sumMontantTotal.add(cumulCotisations.getSumMontant());
            sumMasseTotal.add(cumulCotisations.getSumMasse());
        }

        xmlml.setTotalMasse(sumMasseTotal.toString());
        xmlml.setTotalMontant(sumMontantTotal.toString());
    }

    private void createEntete() {
        xmlml.setLabelRubrique(getSession().getLabel("RUBRIQUE"));
        xmlml.setLabelAnnee(getSession().getLabel("ANNEE_COTISATION"));
        xmlml.setLabelMontant(getSession().getLabel("MONTANT"));
        xmlml.setLabelMasse(getSession().getLabel("MASSE"));
        xmlml.setLabelTotal(getSession().getLabel("TOTAL"));
        xmlml.setLabelDateValeur(getSession().getLabel("DATEVALEUR"));
        xmlml.setTitle(getSession().getLabel("LIST_CUMUL_COTISATIONS_PAR_ANNEE"));
        xmlml.setDateValeur(getFromDateValeur() + " - " + getToDateValeur());
        xmlml.setRubValeur(getFromIdExterne() + " - " + getToIdExterne());
    }

    private void createListExcel() throws Exception {

        String xmlModelPath = Jade.getInstance().getExternalModelDir() + CAApplication.DEFAULT_OSIRIS_ROOT
                + "/model/excelml/" + CAXmlmlListCumulCotisationParAnnee.XLS_DOC_NAME + "Modele.xml";

        String xlsDocPath = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(CAXmlmlListCumulCotisationParAnnee.XLS_DOC_NAME
                        + ".xml");

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(CAApplication.DEFAULT_APPLICATION_OSIRIS);
        docInfoExcel.setDocumentTitle(CAXmlmlListCumulCotisationParAnnee.XLS_DOC_NAME);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(CAXmlmlListCumulCotisationParAnnee.NUMERO_INFOROM);

        xmlml.setCompanyName(FWIImportProperties.getInstance().getProperty(docInfoExcel,
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

        CAOsirisContainer container = xmlml.loadResults();

        xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, container);

        this.registerAttachedDocument(docInfoExcel, xlsDocPath);

    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("LIST_CUMUL_COTI_ANNEE_ERREUR");
        } else {
            return getSession().getLabel("LIST_CUMUL_COTI_ANNEE_OK");
        }
    }

    public String getFromDateValeur() {
        return fromDateValeur;
    }

    public String getFromIdExterne() {
        return fromIdExterne;
    }

    public String getToDateValeur() {
        return toDateValeur;
    }

    public String getToIdExterne() {
        return toIdExterne;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setFromDateValeur(String fromDateValeur) {
        this.fromDateValeur = fromDateValeur;
    }

    public void setFromIdExterne(String fromIdExterne) {
        this.fromIdExterne = fromIdExterne;
    }

    public void setToDateValeur(String toDateValeur) {
        this.toDateValeur = toDateValeur;
    }

    public void setToIdExterne(String toIdExterne) {
        this.toIdExterne = toIdExterne;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

}
