package globaz.musca.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.util.FAUtil;
import globaz.osiris.utils.CAOsirisContainer;
import globaz.webavs.common.CommonExcelmlUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author: btc
 */
public class FAListCompensation_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String NUM_REF_INFOROM_LISTE_COMP = "0097CFA";
    private static final String XLS_DOC_NAME = "ListeCompensation";
    private globaz.musca.db.facturation.FAAfact afact;
    private java.lang.String idAfact = new String();
    private java.lang.String idModuleFacturation = new String();
    private java.lang.String idPassage = new String();
    private java.lang.String idTri = new String();
    private java.lang.String idTypeModule = new String(FAModuleFacturation.CS_MODULE_AFACT);
    private Iterator<String> itPassage;
    private java.lang.String libelle = new String();
    private List<String> listPassage = new ArrayList<String>();
    private globaz.musca.db.facturation.FAPassage passage;
    private List<Map<?, ?>> source = new ArrayList<Map<?, ?>>();
    public final String TEMPLATE_FILENAME = new String("MUSCA_PCOMP_LST");
    private String typeImpression = "pdf";
    private CAOsirisContainer xlsContainer = new CAOsirisContainer();

    /**
     * Constructeur
     */
    public FAListCompensation_Doc() throws Exception {
        this(new BSession(globaz.musca.application.FAApplication.DEFAULT_APPLICATION_MUSCA));
    }

    /**
     * Constructeur
     */
    public FAListCompensation_Doc(BProcess parent) throws FWIException {
        this(parent, globaz.musca.application.FAApplication.APPLICATION_MUSCA_REP, "listCompensation");
    };

    /**
     * Constructeur
     */
    public FAListCompensation_Doc(BProcess arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle("Liste des propositions de compensation");
    }

    /**
     * Constructeur
     */
    public FAListCompensation_Doc(BSession session) throws FWIException {
        this(session, globaz.musca.application.FAApplication.APPLICATION_MUSCA_REP, "listCompensation");
    }

    /**
     * Constructeur
     */
    public FAListCompensation_Doc(BSession arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle("Liste des propositions de compensation");
    }

    protected void _headerText() {

        super.setParametres(
                FWIImportParametre.PARAM_COMPANYNAME,
                getTemplateProperty(
                        getDocumentInfo(),
                        ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                                + JadeStringUtil.toUpperCase(getSession().getIdLangueISO())));
        super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("LISPCOMPTITLE"));

        super.setParametres(FAiTextParameterList.LABEL_NUMERO, getSession().getLabel("NUMERO"));
        super.setParametres(FAiTextParameterList.PARAM_NUMERO, getPassage().getIdPassage());

        super.setParametres(FAiTextParameterList.LABEL_LIBELLE, getSession().getLabel("LIBELLE"));
        super.setParametres(FAiTextParameterList.PARAM_LIBELLE, getPassage().getLibelle());

        super.setParametres(FAiTextParameterList.LABEL_DATE_FACT, getSession().getLabel("DATEFACT"));
        super.setParametres(FAiTextParameterList.PARAM_DATE_FACT, getPassage().getDateFacturation());

        super.setParametres(FAiTextParameterList.LABEL_MODULE, getSession().getLabel("MODULE"));
        super.setParametres(FAiTextParameterList.PARAM_MODULE, getModuleLibelle(getIdModuleFacturation()));

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2003 14:31:37)
     */
    protected void _summaryText() {
        super.setParametres(FAiTextParameterList.LABEL_TOTAUX, getSession().getLabel("TOTAUX"));
        super.setParametres(FAiTextParameterList.PARAM_REFERENCE,
                getSession().getUserId() + " (" + JACalendar.todayJJsMMsAAAA() + ")");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2003 14:33:40)
     */
    protected void _tableHeader() {
        super.setColumnHeader(1, getSession().getLabel("DEBITEUR"));
        super.setColumnHeader(2, getSession().getLabel("DECOMPTE"));
        super.setColumnHeader(3, getSession().getLabel("FACTURE_S_COMPEN"));
        super.setColumnHeader(4, getSession().getLabel("MONTANT"));
        super.setColumnHeader(5, getSession().getLabel("DEBITEURCOMP"));
        super.setColumnHeader(6, getSession().getLabel("DECOMPTECOMP"));
        super.setColumnHeader(7, getSession().getLabel("AQUITTANCER"));
    }

    @Override
    protected void _validate() throws java.lang.Exception {
        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError("Le champ email doit être renseigné.");
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
    }

    @Override
    public void afterBuildReport() {
        if ("xls".equals(getTypeImpression())) {
            try {
                printXlsDocument();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        getDocumentInfo().setDocumentTypeNumber(FAListCompensation_Doc.NUM_REF_INFOROM_LISTE_COMP);
    }

    @Override
    public void beforeExecuteReport() throws FWIException {

        itPassage = listPassage.iterator();
        // nom du template
        super.setTemplateFile(TEMPLATE_FILENAME);
        itPassage = listPassage.iterator();

    }

    @Override
    public boolean beforePrintDocument() {
        if ("xls".equals(getTypeImpression())) {
            return false;
        }

        return super.beforePrintDocument();
    }

    public void bindData(String id) throws java.lang.Exception {
        setIdPassage(id);
        super._executeProcess();
    }

    @Override
    public void createDataSource() throws Exception {
        FAListCompensation_DS manager = null;

        // Sous contrôle d'exceptions
        try {
            manager = new FAListCompensation_DS();
            manager.setSession(getSession());

            // Vérifier l'id du passage
            if (JadeStringUtil.isIntegerEmpty(getIdPassage())) {
                getMemoryLog().logMessage("à remplir", FWMessage.FATAL, this.getClass().getName());
                return;
            }

            // instancier le passage en cours
            passage = new FAPassage();
            passage.setSession(getSession());
            passage.setIdPassage(getIdPassage());
            passage.retrieve(getTransaction());
            if (passage.hasErrors()) {
                getTransaction().addErrors(getSession().getErrors().toString());
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), passage.getClass().getName());
                getMemoryLog().logMessage("a remplir", FWMessage.FATAL, this.getClass().getName());
                return;
            }

            FAUtil.fillDocInfoWithPassageInfo(getDocumentInfo(), getPassage());

            // S'il n'y a pas d'erreur lors du rapatriement des données, passer
            // aux traitements
            if (!isAborted()) {

                // Where clause
                manager.setForIdPassage(getIdPassage());
                manager.setForIdModuleFacturation(getIdModuleFacturation());
                manager.setForIsAfactCompensation(true);
                // Order by
                if (!JadeStringUtil.isBlankOrZero(getIdTri())) {
                    manager.setOrderBy(getIdTri());
                } else {
                    manager.setOrderBy(FAEnteteFacture.CS_TRI_DEBITEUR);
                }
                int compt = 0;
                while (manager.next()) {
                    compt++;
                    setProgressDescription(compt + "/" + manager.size() + "<br>");
                    if (isAborted()) {
                        setProgressDescription("Traitement interrompu<br> sur la ligne : " + compt + "/"
                                + manager.size() + "<br>");
                        if ((getParent() != null) && getParent().isAborted()) {
                            getParent()
                                    .setProcessDescription(
                                            "Traitement interrompu<br> sur la ligne : " + compt + "/" + manager.size()
                                                    + "<br>");
                        }
                        break;
                    } else {
                        source.add(manager.getFieldValues());
                    }
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            throw new FWIException(e);
        }

        _headerText();
        _summaryText();
        _tableHeader();

        if (!source.isEmpty()) {
            super.setDataSource(source);
        }
    }

    public globaz.musca.db.facturation.FAAfact getAfact() {
        return afact;
    }

    public java.lang.String getIdAfact() {
        return idAfact;
    }

    public java.lang.String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public java.lang.String getIdPassage() {
        return idPassage;
    }

    public java.lang.String getIdTri() {
        return idTri;
    }

    public java.lang.String getIdTypeModule() {
        return idTypeModule;
    }

    public java.lang.String getLibelle() {
        return libelle;
    }

    public String getModuleLibelle(String idModuleFacturation) {
        FAModuleFacturation entity = new FAModuleFacturation();
        entity.setSession(getSession());
        entity.setIdModuleFacturation(getIdModuleFacturation());
        try {
            entity.retrieve(getTransaction());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }
        if (entity.isNew()) {
            return "Module empty...";
        } else {
            return entity.getLibelle();
        }
    }

    public globaz.musca.db.facturation.FAPassage getPassage() {
        return passage;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        if (itPassage.hasNext()) {
            idPassage = itPassage.next();
            return true;
        }
        return false;
    }

    /**
     * Préparation des données pour le document excel
     */
    private void prepareDataForXLSDoc() {

        double total = 0;

        // Entetes du fichier excel
        xlsContainer.addValue("numeroInforom", FAListCompensation_Doc.NUM_REF_INFOROM_LISTE_COMP);
        xlsContainer.addValue("DATE_DOC", JACalendar.todayJJsMMsAAAA());

        xlsContainer.addMap(getImporter().getParametre());

        // liste
        int size = source.size();
        for (int i = 0; i < size; i++) {

            Map<?, ?> bean = source.get(new Integer(i));

            this.remplirColumn("COL_1_VALUE", (String) bean.get(FWIImportParametre.getCol(1)), "");
            this.remplirColumn("COL_2_VALUE", (String) bean.get(FWIImportParametre.getCol(2)), "");
            this.remplirColumn("COL_3_VALUE", (Double) bean.get(FWIImportParametre.getCol(3)), "0.00");
            this.remplirColumn("COL_4_VALUE", (Double) bean.get(FWIImportParametre.getCol(4)), "0.00");
            this.remplirColumn("COL_5_VALUE", (String) bean.get(FWIImportParametre.getCol(5)), "");
            this.remplirColumn("COL_6_VALUE", (String) bean.get(FWIImportParametre.getCol(6)), "");
            this.remplirColumn("COL_7_VALUE", (String) bean.get(FWIImportParametre.getCol(7)), "");

            Double montant = (Double) bean.get(FWIImportParametre.getCol(4));
            if (montant != null) {
                total += montant;
            }
        }

        this.remplirColumn("TOTAL_MONTANT", total, "0.00");
    }

    public void printXlsDocument() throws Exception {

        try {

            String xmlModelPath = Jade.getInstance().getExternalModelDir() + FAApplication.APPLICATION_MUSCA_REP
                    + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                    + FAListCompensation_Doc.XLS_DOC_NAME + "Modele.xml";

            String xlsDocPath = Jade.getInstance().getPersistenceDir()
                    + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(FAListCompensation_Doc.XLS_DOC_NAME + ".xml");

            prepareDataForXLSDoc();

            xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, xlsContainer);

            JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
            docInfoExcel.setApplicationDomain(FAApplication.APPLICATION_MUSCA_REP);
            docInfoExcel.setDocumentTitle(FAListCompensation_Doc.XLS_DOC_NAME);
            docInfoExcel.setPublishDocument(true);
            docInfoExcel.setArchiveDocument(false);
            docInfoExcel.setDocumentTypeNumber(FAListCompensation_Doc.NUM_REF_INFOROM_LISTE_COMP);
            this.registerAttachedDocument(docInfoExcel, xlsDocPath);

        } catch (Exception e) {
            throw new Exception("Error generating excel file", e);
        }
    }

    public void remplirColumn(String column, Double value, String defaultValue) {
        if (value != null) {
            xlsContainer.addValue(column, value.toString());
        } else {
            xlsContainer.addValue(column, defaultValue);
        }
    }

    public void remplirColumn(String column, String value, String defaultValue) {
        if (!JadeStringUtil.isEmpty(value)) {
            xlsContainer.addValue(column, value);
        } else {
            xlsContainer.addValue(column, defaultValue);
        }
    }

    public void setAfact(globaz.musca.db.facturation.FAAfact newAfact) {
        afact = newAfact;
    }

    public void setIdAfact(java.lang.String newIdAfact) {
        idAfact = newIdAfact;
    }

    public void setIdModuleFacturation(java.lang.String newIdModuleFacturation) {
        idModuleFacturation = newIdModuleFacturation;
    }

    public void setIdPassage(java.lang.String idPassage) {
        if (!listPassage.contains(idPassage)) {
            listPassage.add(idPassage);
        }
        this.idPassage = idPassage;
    }

    public void setIdTri(java.lang.String newIdTri) {
        idTri = newIdTri;
    }

    public void setIdTypeModule(java.lang.String newIdTypeModule) {
        idTypeModule = newIdTypeModule;
    }

    public void setLibelle(java.lang.String libelle) {
        this.libelle = libelle;
    }

    public void setPassage(globaz.musca.db.facturation.FAPassage newPassage) {
        passage = newPassage;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

}
