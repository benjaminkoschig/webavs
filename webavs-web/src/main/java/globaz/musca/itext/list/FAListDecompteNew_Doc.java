package globaz.musca.itext.list;

// ITEXT
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.itext.FAiTextParameterList;
import globaz.musca.util.FAUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;

/**
 * Insérez la description du type ici. Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class FAListDecompteNew_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String NUM_REF_INFOROM_LISTE_DECOMPTE = "0098CFA";
    private globaz.musca.db.facturation.FAEnteteFacture enteteFacture;
    private java.lang.String idPassage = new String();
    private java.lang.String idSousType = new String();
    private java.lang.String idTri = new String();
    private java.lang.String idTriDecompte = new String();
    private Iterator<String> itPassage;
    private java.lang.String libelle = new String();
    private List<String> listPassage = new ArrayList<String>();
    private DecompteRecap decompteRecap = new DecompteRecap();
    private globaz.musca.db.facturation.FAPassage passage;
    private String outPutType;

    // Nombre total de documents que le manager devrait contenir
    private long shouldNbImprimer = 0;
    public final String TEMPLATE_FILENAME = new String("MUSCA_DECOMPTE_LST");
    public final String TEMPLATE_FILENAME_RECAP = new String("MUSCA_DECOMPTE_LST_RECAP");

    public FAListDecompteNew_Doc() throws Exception {
        this(new BSession(globaz.musca.application.FAApplication.DEFAULT_APPLICATION_MUSCA));
    }

    public FAListDecompteNew_Doc(BProcess parent) throws FWIException {
        this(parent, globaz.musca.application.FAApplication.APPLICATION_MUSCA_REP, "listCompensation");
    }

    public FAListDecompteNew_Doc(BProcess arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle("Impression des listes de décomptes");
    }

    public FAListDecompteNew_Doc(BSession session) throws FWIException {
        this(session, globaz.musca.application.FAApplication.APPLICATION_MUSCA_REP, "listCompensation");
    }

    public FAListDecompteNew_Doc(BSession arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle("Impression des listes de décomptes");
    }

    protected void _headerText() {
        super.setParametres(
                FWIImportParametre.PARAM_COMPANYNAME,
                getTemplateProperty(
                        getDocumentInfo(),
                        ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                                + JadeStringUtil.toUpperCase(getSession().getIdLangueISO())));
        super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("LISDECTITLE"));
        super.setParametres(FAiTextParameterList.LABEL_NUMERO, getSession().getLabel("NUMERO"));
        super.setParametres(FAiTextParameterList.PARAM_NUMERO, getPassage().getIdPassage());
        super.setParametres(FAiTextParameterList.LABEL_LIBELLE, getSession().getLabel("LIBELLE"));
        super.setParametres(FAiTextParameterList.PARAM_LIBELLE, getPassage().getLibelle());
        super.setParametres(FAiTextParameterList.LABEL_DATE_FACT, getSession().getLabel("DATEFACT"));
        super.setParametres(FAiTextParameterList.PARAM_DATE_FACT, getPassage().getDateFacturation());
        super.setParametres(FAiTextParameterList.LABEL_DATE_ECHEANCE, getSession().getLabel("DATEECHEANCE"));
        super.setParametres(FAiTextParameterList.PARAM_DATE_ECHEANCE, getPassage().getDateEcheance());
    };

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2003 14:31:37)
     */
    protected void _summaryText() {
        super.setParametres(FAiTextParameterList.LABEL_TOTAUX, getSession().getLabel("TOTAUX"));
        super.setParametres(FAiTextParameterList.PARAM_REFERENCE,
                getSession().getUserId() + " (" + JACalendar.todayJJsMMsAAAA() + ")");
        super.setParametres(FAiTextParameterList.PARAM_MONTANT_TOTAL, decompteRecap.getMontantTotal().getValueDouble());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2003 14:33:40)
     */
    protected void _tableHeader() {
        super.setColumnHeader(1, getSession().getLabel("DEBITEUR"));
        super.setColumnHeader(2, getSession().getLabel("DECOMPTE"));
        super.setColumnHeader(3, getSession().getLabel("MONTANT"));
        super.setColumnHeader(4, getSession().getLabel("ADRPAIEM"));
        super.setColumnHeader(5, getSession().getLabel("REMARQUE"));
        super.setColumnHeader(6, getSession().getLabel("IMPRESSION"));
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
    public void beforeBuildReport() throws FWIException {
        getDocumentInfo().setDocumentTypeNumber(FAListDecompteNew_Doc.NUM_REF_INFOROM_LISTE_DECOMPTE);
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() throws FWIException {

        itPassage = listPassage.iterator();
        // nom du template
        super.setTemplateFile(TEMPLATE_FILENAME);
        itPassage = listPassage.iterator();

    }

    @Override
    public final boolean beforePrintDocument() {
        boolean isOk = super.beforePrintDocument();
        if (isOk) {
            // On lance la construction de la recap
            FWIImportManager recapImport = super.getImporter();
            recapImport.setDataSource(new JREmptyDataSource());
            recapImport.setDocumentTemplate(TEMPLATE_FILENAME_RECAP);
            recapImport.setDocumentName(" Recapitulatif");
            recapImport.setParametre(FAiTextParameterList.LABEL_TYPE_DECOMPTE, getSession()
                    .getLabel("LISDEC_RECAPTYPE"));
            recapImport.setParametre(FAiTextParameterList.LABEL_CREDIT, getSession().getLabel("LISDEC_RECAPCREDIT"));
            recapImport.setParametre(FAiTextParameterList.LABEL_POSITIF, getSession().getLabel("LISDEC_RECAPPOSITIF"));
            recapImport
                    .setParametre(FAiTextParameterList.LABEL_RECAP_TITRE, getSession().getLabel("LISDEC_RECAPTITLE"));
            recapImport.setParametre(FAiTextParameterList.LABEL_NUM_DECOMPTE,
                    getSession().getLabel("LISDEC_RECAPNDECOMPTE"));

            recapImport.setParametre(FAiTextParameterList.PARAM_CREDIT, decompteRecap.getMontantNegatif()
                    .getValueDouble());
            recapImport.setParametre(FAiTextParameterList.PARAM_POSITIF, decompteRecap.getMontantPositif()
                    .getValueDouble());
            recapImport.setParametre(FAiTextParameterList.PARAM_NUM_DECOMPTE, decompteRecap.getCompteur()
                    .getValueDouble());
            recapImport.setParametre(FAiTextParameterList.PARAM_MONTANT_TOTAL, decompteRecap.getMontantTotal()
                    .getValueDouble());
            try {
                recapImport.createDocument();
            } catch (FWIException e) {
                // Il y a eu une erreur, on informe l'utilisateur
                getMemoryLog().logMessage(
                        getSession().getLabel("La recapitulation du document a eu une erreur : " + e.getMessage()),
                        globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
            }
        }
        return isOk;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 11:53:46)
     * 
     * @param id
     *            java.lang.String
     */
    public void bindData(String id) throws java.lang.Exception {
        setIdPassage(id);
        super.executeProcess();
    }

    private FAPassage retrivePassage() throws Exception {
        FAPassage passage = new FAPassage();
        passage.setSession(getSession());
        passage.setIdPassage(idPassage);
        passage.retrieve(getTransaction());
        return passage;
    }

    private List<Map<?, ?>> generateSource(List<FaDecompteBeanXls> list) {
        List<Map<?, ?>> source = new ArrayList<Map<?, ?>>();

        for (FaDecompteBeanXls bean : list) {
            Map<String, Object> row = new HashMap<String, Object>();
            row.put(FWIImportParametre.getCol(1), bean.getDebiteur());
            row.put(FWIImportParametre.getCol(2), bean.getDecompte());
            // Description du décompte
            row.put(FWIImportParametre.getCol(3), bean.getMontant().getValueDouble());
            row.put(FWIImportParametre.getCol(4), bean.getAddressPaiement());
            // Montant
            row.put(FWIImportParametre.getCol(5), bean.getRecRem());
            // Débiteur compensé
            row.put(FWIImportParametre.getCol(6), bean.getImprimable());
            source.add(row);
        }
        return source;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource ()
     */
    @Override
    public void createDataSource() throws Exception {

        // Sous contrôle d'exceptions
        try {
            // Vérifier l'id du passage
            if (JadeStringUtil.isIntegerEmpty(getIdPassage())) {
                getMemoryLog().logMessage("à remplir", FWMessage.FATAL, this.getClass().getName());
                return;
            }
            List<FaDecompteBeanXls> list = load();

            if (passage.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), passage.getClass().getName());
                getMemoryLog().logMessage("a remplir", FWMessage.FATAL, this.getClass().getName());
                return;
            }

            FAUtil.fillDocInfoWithPassageInfo(getDocumentInfo(), getPassage());

            if (!list.isEmpty()) {
                super.setDataSource(generateSource(list));
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            throw new FWIException(e);
        }

        _headerText();
        _summaryText();
        _tableHeader();

    }

    private List<FaDecompteBeanXls> load() throws Exception {
        passage = retrivePassage();

        List<FaDecompteBeanXls> list = new ArrayList<FaDecompteBeanXls>();
        DecomptesLoader loader = new DecomptesLoader(getSession(), idPassage, idTriDecompte, idSousType, idTri);
        loader.load();
        int compt = 0;
        while (loader.next()) {
            compt++;
            FaDecompteBeanXls decompte = loader.getBean();
            list.add(decompte);
            setProgressDescription(compt + "/" + loader.size() + "<br>");
            if (isAborted()) {
                setProgressDescription("Traitement interrompu<br> sur la ligne : " + compt + "/" + loader.size()
                        + "<br>");
                if (getParent() != null && getParent().isAborted()) {
                    getParent().setProcessDescription(
                            "Traitement interrompu<br> sur la ligne : " + compt + "/" + loader.size() + "<br>");
                }
                break;
            } else {
                decompteRecap.addMontant(decompte.getMontant());
            }
        }

        shouldNbImprimer = loader.size();
        return list;
    }

    public void generateXsl() {
        // FilenameUtils
        _createDocumentInfo();

        File file;
        try {
            file = this.generateXsl(load(), decompteRecap);
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
        try {
            this.registerAttachedDocument(getDocumentInfo(), file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File generateXsl(List<FaDecompteBeanXls> list, DecompteRecap decompteRecap) {
        String nomCaise = FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase());

        Details details = new Details();
        details.add(nomCaise, "");
        details.add("N°", NUM_REF_INFOROM_LISTE_DECOMPTE);
        details.newLigne();

        details.add(getSession().getLabel("NUMERO"), idPassage);
        details.add(getSession().getLabel("LIBELLE"), getPassage().getLibelle());
        details.newLigne();

        details.add(getSession().getLabel("DATEFACT"), getPassage().getDateFacturation());
        details.add(getSession().getLabel("DATEECHEANCE"), getPassage().getDateEcheance());
        details.newLigne();

        List<DecompteInfos> decomtpeInfos = new ArrayList<DecompteInfos>();

        decomtpeInfos.add(new DecompteInfos(toLabel("LISDEC_RECAPNDECOMPTE"), decompteRecap.getCompteur()));
        decomtpeInfos.add(new DecompteInfos(toLabel("LISDEC_RECAPPOSITIF"), decompteRecap.getMontantPositif()));
        decomtpeInfos.add(new DecompteInfos(toLabel("LISDEC_RECAPCREDIT"), decompteRecap.getMontantNegatif()));
        decomtpeInfos.add(new DecompteInfos(toLabel("TOTAUX"), decompteRecap.getMontantTotal()));

        SimpleOutputListBuilderJade builder = null;
        File file = null;
        try {
            builder = SimpleOutputListBuilderJade.newInstance().session(getSession()).globazTheme().addTranslater()
                    .outputNameAndAddPath(NUM_REF_INFOROM_LISTE_DECOMPTE + "_" + toLabel("DECOMPTE"));

            file = builder.addList(list).classElementList(FaDecompteBeanXls.class).addHeaderDetails(details)
                    .addTitle(toLabel("LISDECTITLE"), Align.CENTER).addSubTitle(toLabel("DECOMPTE")).jump()
                    .addList(decomtpeInfos).addSubTitle(toLabel("RECAPITULATIF"))
                    .addTitle(toLabel("LISDECTITLE"), Align.CENTER).addHeaderDetails(details).asXls().build();
        } finally {
            if (builder != null) {
                builder.close();
            }
        }
        return file;
    }

    public globaz.musca.db.facturation.FAEnteteFacture getEnteteFacture() {
        return enteteFacture;
    }

    private String toLabel(String label) {
        return getSession().getLabel(label);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:47:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdPassage() {
        return idPassage;
    }

    public java.lang.String getIdSousType() {
        return idSousType;
    }

    public java.lang.String getIdTri() {
        return idTri;
    }

    public java.lang.String getIdTriDecompte() {
        return idTriDecompte;
    }

    public Iterator<String> getItPassage() {
        return itPassage;
    }

    public java.lang.String getLibelle() {
        return libelle;
    }

    public List<String> getListPassage() {
        return listPassage;
    }

    public globaz.musca.db.facturation.FAPassage getPassage() {
        return passage;
    }

    public long getShouldNbImprimer() {
        return shouldNbImprimer;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
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

    public void setEnteteFacture(globaz.musca.db.facturation.FAEnteteFacture enteteFacture) {
        this.enteteFacture = enteteFacture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:47:34)
     * 
     * @param newIdPassage
     *            java.lang.String
     */
    public void setIdPassage(java.lang.String idPassage) {
        if (!listPassage.contains(idPassage)) {
            listPassage.add(idPassage);
        }
        this.idPassage = idPassage;
    }

    public void setIdSousType(java.lang.String idSousType) {
        this.idSousType = idSousType;
    }

    public void setIdTri(java.lang.String idTri) {
        this.idTri = idTri;
    }

    public void setIdTriDecompte(java.lang.String idTriDecompte) {
        this.idTriDecompte = idTriDecompte;
    }

    public void setItPassage(Iterator<String> itPassage) {
        this.itPassage = itPassage;
    }

    public void setLibelle(java.lang.String libelle) {
        this.libelle = libelle;
    }

    public void setListPassage(List<String> listPassage) {
        this.listPassage = listPassage;
    }

    public String getOutPutType() {
        return outPutType;
    }

    public void setOutPutType(String outPutType) {
        this.outPutType = outPutType;
    }

}
