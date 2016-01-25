package globaz.osiris.print.itext.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CARole;
import globaz.osiris.db.comptes.CARoleManager;
import globaz.osiris.db.comptes.CARoleViewBean;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CATypeSection;
import globaz.osiris.db.comptes.CATypeSectionManager;
import globaz.osiris.utils.CAOsirisContainer;
import globaz.webavs.common.CommonExcelmlUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author: Administrator
 */
public class CAIListComptesALettrer extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUMERO_REFERENCE_INFOROM = "0148GCA";
    private static final String TEMPLATE_FILE_NAME = "CAIListComptesALettrer";
    private static final String XLS_DOC_NAME = "ListeComptesALettrer";

    private String aLettrer = new String();
    private int compteur = 0;
    private String forIdCategorie = new String();
    private String forIdGenreCompte = new String();
    private String forIdTypeSection = new String();
    private String forSelectionRole = new String();
    private String forSelectionSigne = new String();
    private String forSelectionTriCA = new String();
    private String forSelectionTriSection = new String();
    private boolean hasNext = true;

    private String idSection = new String();
    private ArrayList<Map> liste = new ArrayList<Map>();
    private Map m_container = new Hashtable();
    private String typeImpression = "pdf";
    private CAOsirisContainer xlsContainer = new CAOsirisContainer();

    /**
     * Commentaire relatif au constructeur CAIListComptesALettrer.
     */
    public CAIListComptesALettrer() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS), CAApplication.DEFAULT_OSIRIS_ROOT,
                "ListComptesALettrer");
        super.setDocumentTitle(getSession().getLabel("TITLE_COMPTE_ANNEXE_LETTRER"));
    }

    /**
     * Commentaire relatif au constructeur CAIListComptesALettrer.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListComptesALettrer(BProcess parent) throws FWIException {
        this(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "ListComptesALettrer");
        super.setDocumentTitle(getSession().getLabel("TITLE_COMPTE_ANNEXE_LETTRER"));
    }

    /**
     * Constructor for CAIListComptesALettrer.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAIListComptesALettrer(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    /**
     * Commentaire relatif au constructeur CAIListComptesALettrer.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListComptesALettrer(BSession session) throws FWIException {
        this(session, CAApplication.DEFAULT_OSIRIS_ROOT, "ListComptesALettrer");
        super.setDocumentTitle(session.getLabel("TITLE_COMPTE_ANNEXE_LETTRER"));
    }

    /**
     * Constructor for CAIListComptesALettrer.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAIListComptesALettrer(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    // Création du paramètre de référence pour les documents de type liste
    private String _getRefParam() {
        try {
            SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy ' - ' HH:mm");
            StringBuffer refBuffer = new StringBuffer(getSession().getLabel("REFERENCE") + " ");
            refBuffer.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1));
            refBuffer.append(" (");
            refBuffer.append(formater.format(new Date()));
            refBuffer.append(")");
            refBuffer.append(" - ");
            refBuffer.append(getSession().getUserId());
            return refBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    protected void _header() {
        try {
            this.setParametres(
                    FWIImportParametre.PARAM_COMPANYNAME,
                    FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                            ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

            // Exercice (date)
            super.setParametres(FWIImportParametre.PARAM_EXERCICE, globaz.globall.util.JACalendar.todayJJsMMsAAAA());

            // Titre du document
            super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("TITRE_COMPTE_LETTRER"));

            // Rôle
            CARole tempRole;
            CARoleManager manRole = new CARoleManager();
            manRole.setSession(getSession());
            try {
                manRole.find();

                if ((getParent() != null) && getParent().isAborted()) {
                    return;
                }

                super.setParametres(CAIListComptesALettrerParam.LABEL_ROLE, getSession().getLabel("ROLE"));
                if (!getForSelectionRole().equals(CARoleViewBean.getTousOptions(getSession()))) {
                    for (int i = 0; i < manRole.size(); i++) {
                        tempRole = (CARole) manRole.getEntity(i);
                        if (getForSelectionRole().indexOf(tempRole.getIdRole()) != -1) {
                            super.setParametres(CAIListComptesALettrerParam.PARAM_ROLE, tempRole.getDescription());
                        }
                    }
                } else {
                    super.setParametres(CAIListComptesALettrerParam.PARAM_ROLE, getSession().getLabel("TOUS"));
                }
            } catch (Exception e) {
            }

            // Type de la section
            CATypeSection tempTypeSection;
            CATypeSectionManager manTypeSection = new CATypeSectionManager();
            manTypeSection.setSession(getSession());
            super.setParametres(CAIListComptesALettrerParam.LABEL_TYPE_DE_SECTION,
                    getSession().getLabel("TYPEDESECTION"));
            try {
                manTypeSection.find();

                if ((getParent() != null) && getParent().isAborted()) {
                    return;
                }

                if (!getForIdTypeSection().equalsIgnoreCase("1000")) {
                    for (int i = 0; i < manTypeSection.size(); i++) {
                        tempTypeSection = (CATypeSection) manTypeSection.getEntity(i);
                        if (tempTypeSection.getIdTypeSection().equalsIgnoreCase(getForIdTypeSection())) {
                            super.setParametres(CAIListComptesALettrerParam.PARAM_TYPE_DE_SECTION,
                                    tempTypeSection.getDescription());
                        }
                    }
                } else {
                    super.setParametres(CAIListComptesALettrerParam.PARAM_TYPE_DE_SECTION, getSession()
                            .getLabel("TOUS"));
                }
            } catch (Exception e) {
            }

            // Premier niveau de tri
            super.setParametres(CAIListComptesALettrerParam.LABEL_PREMIER_TRI, getSession().getLabel("PREMIERTRI"));
            if (getForSelectionTriCA().equalsIgnoreCase("1")) {
                super.setParametres(CAIListComptesALettrerParam.PARAM_PREMIER_TRI,
                        getSession().getLabel("TRICOMPTEANNEXENUMERO"));
            } else {
                super.setParametres(CAIListComptesALettrerParam.PARAM_PREMIER_TRI,
                        getSession().getLabel("TRICOMPTEANNEXENOM"));
            }

            // Deuxième niveau de tri
            super.setParametres(CAIListComptesALettrerParam.LABEL_DEUXIEME_TRI, getSession().getLabel("DEUXIEMETRI"));
            if (getForSelectionTriSection().equalsIgnoreCase("1")) {
                super.setParametres(CAIListComptesALettrerParam.PARAM_DEUXIEME_TRI,
                        getSession().getLabel("TRISECTIONNUMERO"));
            } else {
                super.setParametres(CAIListComptesALettrerParam.PARAM_DEUXIEME_TRI,
                        getSession().getLabel("TRISECTIONDATE"));
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return;
        }

        return;
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la dernière page Utiliser super.setParametres(Key, Value)
     */
    protected void _summary() {
        // Texte total en bas de liste
        super.setParametres(CAIListComptesALettrerParam.LABEL_TOTAUX, getSession().getLabel("TOTAL"));
    }

    /**
     * Methode pour insérer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {
        // Entêtes de colonnes
        this.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("COMPTEANNEXE"));
        this.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("SECTION"));
        this.setParametres(FWIImportParametre.getCol(3), getSession().getLabel("DATE"));
        this.setParametres(FWIImportParametre.getCol(7), getSession().getLabel("SOLDESECTION"));
        this.setParametres(FWIImportParametre.getCol(8), getSession().getLabel("SOLDECOMPTEANNEXE"));
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

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        // Ajout des références en bas de liste
        super.setParametres(FWIImportParametre.PARAM_REFERENCE, _getRefParam());
        getDocumentInfo().setTemplateName(CAIListComptesALettrer.TEMPLATE_FILE_NAME);
        // Référence Inforom
        getDocumentInfo().setDocumentTypeNumber(CAIListComptesALettrer.NUMERO_REFERENCE_INFOROM);
        _header();
        _tableHeader();
        _summary();
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() {
        try {
            super.setTemplateFile(CAIListComptesALettrer.TEMPLATE_FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean beforePrintDocument() {
        if ("xls".equals(getTypeImpression())) {
            return false;
        }

        return super.beforePrintDocument();
    }

    /**
     * Date de création : (25.02.2003 13:59:41)
     * 
     * @param id
     *            String
     * @exception Exception
     *                La description de l'exception.
     */
    public void bindData(String id) throws Exception {
        setIdSection(id);
    }

    /**
     * Methode appelé pour la création des valeurs pour le document
     */
    @Override
    public void createDataSource() throws Exception {

        liste.add(m_container);

        if (liste.size() > 0) {
            super.setDataSource(liste);
        }
    }

    private void extractValues(CASection entity) throws Exception {
        try {
            // Données
            CAIListComptesALettrerBean bean = new CAIListComptesALettrerBean(entity);
            compteur++;
            m_container.put(new Integer(compteur), bean);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
    }

    /**
     * @return
     */
    public String getALettrer() {
        return aLettrer;
    }

    /**
     * @return
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * Returns the forIdTypeSection.
     * 
     * @return String
     */
    public String getForIdTypeSection() {
        return forIdTypeSection;
    }

    /**
     * Returns the forSelectionRole.
     * 
     * @return String
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Returns the forSelectionSigne.
     * 
     * @return String
     */
    public String getForSelectionSigne() {
        return forSelectionSigne;
    }

    /**
     * Returns the forSelectionTriCA.
     * 
     * @return String
     */
    public String getForSelectionTriCA() {
        return forSelectionTriCA;
    }

    /**
     * Returns the forSelectionTriSection.
     * 
     * @return String
     */
    public String getForSelectionTriSection() {
        return forSelectionTriSection;
    }

    /**
     * Returns the idSection.
     * 
     * @return String
     */
    public String getIdSection() {
        return idSection;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (!hasNext) {
            return hasNext;
        }

        if ((getParent() != null) && getParent().isAborted()) {
            return false;
        }

        CAIListComptesALettrerManager manager = new CAIListComptesALettrerManager();
        CASection entity;
        BTransaction transaction = null;
        BStatement statement = null;
        try {
            manager.setSession(super.getSession());
            manager.setForSelectionRole(getForSelectionRole());
            manager.setForIdTypeSection(getForIdTypeSection());
            manager.setForSelectionTriCA(getForSelectionTriCA());
            manager.setForSelectionTriSection(getForSelectionTriSection());
            manager.setForSelectionSigne(getForSelectionSigne());
            manager.setForIdGenreCompte(getForIdGenreCompte());
            manager.setForIdCategorie(getForIdCategorie());
            manager.setALettrer(getALettrer());

            transaction = new globaz.globall.db.BTransaction(getSession());
            transaction.openTransaction();
            statement = manager.cursorOpen(transaction);

            getParent().setProgressScaleValue(manager.getCount());

            // lit le nouveau entity
            while ((entity = (CASection) manager.cursorReadNext(statement)) != null) {
                getParent().incProgressCounter();
                if ((getParent() != null) && getParent().isAborted()) {
                    return false;
                }
                extractValues(entity);
            }
            return true;
            // fin de la transaction on ferme le curseur
        } catch (Exception e) {
            e.printStackTrace();
            JadeLogger.error(this, e);
            return false;
        } finally {
            try {
                if (statement != null) {
                    try {
                        manager.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } catch (Exception ex) {
            } finally {
                if (transaction != null) {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e) {
                    }
                }
            }
            // Il y a un seul document a créer on place hasNext a false
            hasNext = false;
        }
    }

    /**
     * Préparation des données pour le document excel
     */
    private void prepareDataForXLSDoc() {

        BigDecimal totalSection = new BigDecimal(0);
        BigDecimal totalCompte = new BigDecimal(0);

        // Entetes du fichier excel
        xlsContainer.addValue("numeroInforom", CAIListComptesALettrer.NUMERO_REFERENCE_INFOROM);
        this.remplirColumn("TOTAL", getSession().getLabel("TOTAL"), "Total");

        xlsContainer.addMap(getImporter().getParametre());

        // liste
        int size = compteur;
        for (int i = 1; i <= size; i++) {

            CAIListComptesALettrerBean bean = (CAIListComptesALettrerBean) m_container.get(new Integer(i));

            this.remplirColumn("COL_1_VALUE", bean.getCOL_1(), "");
            this.remplirColumn("COL_2_VALUE", bean.getCOL_2(), "");
            this.remplirColumn("COL_3_VALUE", bean.getCOL_3(), "");
            this.remplirColumn("COL_4_VALUE", bean.getCOL_7(), "0.00");
            this.remplirColumn("COL_5_VALUE", bean.getCOL_8(), "0.00");

            if (bean.getCOL_7() != null) {
                totalSection = totalSection.add(bean.getCOL_7());
            }
            if (bean.getCOL_8() != null) {
                totalCompte = totalCompte.add(bean.getCOL_8());
            }
        }

        this.remplirColumn("TOTAL_SECTION", totalSection, "0.00");
        this.remplirColumn("TOTAL_COMPTE", totalCompte, "0.00");
    }

    public void printXlsDocument() throws Exception {

        try {

            String xmlModelPath = Jade.getInstance().getExternalModelDir() + CAApplication.DEFAULT_OSIRIS_ROOT
                    + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                    + CAIListComptesALettrer.XLS_DOC_NAME + "Modele.xml";

            String xlsDocPath = Jade.getInstance().getPersistenceDir()
                    + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(CAIListComptesALettrer.XLS_DOC_NAME + ".xml");

            prepareDataForXLSDoc();

            xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, xlsContainer);

            JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
            docInfoExcel.setApplicationDomain(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            docInfoExcel.setDocumentTitle(CAIListComptesALettrer.XLS_DOC_NAME);
            docInfoExcel.setPublishDocument(true);
            docInfoExcel.setArchiveDocument(false);
            docInfoExcel.setDocumentTypeNumber(CAIListComptesALettrer.NUMERO_REFERENCE_INFOROM);
            this.registerAttachedDocument(docInfoExcel, xlsDocPath);
        } catch (Exception e) {
            throw new Exception("Error generating excel file", e);
        }
    }

    public void remplirColumn(String column, BigDecimal value, String defaultValue) {
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

    /**
     * Méthode appelé pour lancer l'exportation du document Par défaut ne pas utiliser car déjà implémenté par la
     * superClass Utile si on ne veut pas exporter en fichier temporaire Date de création : (17.02.2003 14:44:15)
     */
    @Override
    public void returnDocument() throws FWIException {
        if (!getParent().isAborted()) {
            super.imprimerListDocument();
        }
    }

    /**
     * @param string
     */
    public void setALettrer(String string) {
        aLettrer = string;
    }

    /**
     * @param string
     */
    public void setForIdCategorie(String s) {
        forIdCategorie = s;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String s) {
        forIdGenreCompte = s;
    }

    /**
     * Sets the forIdTypeSection.
     * 
     * @param forIdTypeSection
     *            The forIdTypeSection to set
     */
    public void setForIdTypeSection(String forIdTypeSection) {
        this.forIdTypeSection = forIdTypeSection;
    }

    /**
     * Sets the forSelectionRole.
     * 
     * @param forSelectionRole
     *            The forSelectionRole to set
     */
    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

    /**
     * Sets the forSelectionSigne.
     * 
     * @param forSelectionSigne
     *            The forSelectionSigne to set
     */
    public void setForSelectionSigne(String forSelectionSigne) {
        this.forSelectionSigne = forSelectionSigne;
    }

    /**
     * Sets the forSelectionTriCA.
     * 
     * @param forSelectionTriCA
     *            The forSelectionTriCA to set
     */
    public void setForSelectionTriCA(String forSelectionTriCA) {
        this.forSelectionTriCA = forSelectionTriCA;
    }

    /**
     * Sets the forSelectionTriSection.
     * 
     * @param forSelectionTriSection
     *            The forSelectionTriSection to set
     */
    public void setForSelectionTriSection(String forSelectionTriSection) {
        this.forSelectionTriSection = forSelectionTriSection;
    }

    /**
     * Sets the idSection.
     * 
     * @param idSection
     *            The idSection to set
     */
    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

}
