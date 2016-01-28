package globaz.osiris.print.itext.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.api.FWIImporterInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CARole;
import globaz.osiris.db.comptes.CARoleManager;
import globaz.osiris.db.comptes.CARoleViewBean;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CATypeSection;
import globaz.osiris.db.comptes.CATypeSectionManager;
import globaz.osiris.parser.CASelectBlockParser;
import globaz.osiris.translation.CACodeSystem;
import globaz.osiris.utils.CAOsirisContainer;
import globaz.webavs.common.CommonExcelmlUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Insérez la description du type ici. Date de création : (03.06.2003 15:46:19)
 * 
 * @author: Administrator
 */
public class CAIListSoldeSection extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUMERO_REFERENCE_INFOROM = "0149GCA";
    public static final String XLS_DOC_NAME = "ListeSoldeSection";
    private int compteur = 0;
    private String forIdCategorie = new String();
    private String forIdGenreCompte = new String();
    private java.lang.String forIdTypeSection = new String();
    private java.lang.String forSelectionRole = new String();
    private java.lang.String forSelectionSigne = new String();
    private java.lang.String forSelectionTriCA = new String();
    private java.lang.String forSelectionTriSection = new String();
    private boolean hasNext = true;
    private java.lang.String idSection = new String();
    FWIImporterInterface importDoc = null;
    private List listBeans;

    private Map m_container = new Hashtable();
    private Map m_containerRecapContentieux = new Hashtable();
    private Map m_containerRecapType = new Hashtable();

    private CAOsirisContainer xlsContainer = new CAOsirisContainer();

    /**
     * Commentaire relatif au constructeur CAIListSoldeSection.
     */
    public CAIListSoldeSection() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS), CAApplication.DEFAULT_OSIRIS_ROOT,
                "ListSoldeSection");
        super.setDocumentTitle(getSession().getLabel("TITLE_LIST_SOLDE_SECTION"));
    }

    /**
     * Commentaire relatif au constructeur CAIListSoldeSection.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListSoldeSection(BProcess parent) throws FWIException {
        this(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "ListSoldeSection");
        super.setDocumentTitle(getSession().getLabel("TITLE_LIST_SOLDE_SECTION"));
    }

    /**
     * Constructor for CAIListSoldeSection.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAIListSoldeSection(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    /**
     * Commentaire relatif au constructeur CAIListSoldeSection.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListSoldeSection(BSession session) throws FWIException {
        this(session, CAApplication.DEFAULT_OSIRIS_ROOT, "ListSoldeSection");
        super.setDocumentTitle(session.getLabel("TITLE_LIST_SOLDE_SECTION"));
    }

    /**
     * Constructor for CAIListSoldeSection.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAIListSoldeSection(BSession session, String rootApplication, String fileName) throws FWIException {
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
            super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6010"));
            // Rôle
            CARole tempRole;
            CARoleManager manRole = new CARoleManager();
            manRole.setSession(getSession());
            try {
                manRole.find();
                if (getParent().isAborted()) {
                    return;
                }
                super.setParametres(CAIListSoldeSectionParam.LABEL_ROLE, getSession().getLabel("ROLE") + " :");
                if (!getForSelectionRole().equals(CARoleViewBean.getTousOptions(getSession()))) {
                    for (int i = 0; i < manRole.size(); i++) {
                        tempRole = (CARole) manRole.getEntity(i);
                        if (getForSelectionRole().indexOf(tempRole.getIdRole()) != -1) {
                            super.setParametres(CAIListSoldeSectionParam.PARAM_ROLE, tempRole.getDescription());
                        }
                    }
                } else {
                    super.setParametres(CAIListSoldeSectionParam.PARAM_ROLE, getSession().getLabel("TOUS"));
                }
            } catch (Exception e) {
            }
            // Type de la section
            CATypeSection tempTypeSection;
            CATypeSectionManager manTypeSection = new CATypeSectionManager();
            manTypeSection.setSession(getSession());
            super.setParametres(CAIListSoldeSectionParam.LABEL_TYPE_DE_SECTION, getSession().getLabel("TYPEDESECTION")
                    + " :");
            try {
                manTypeSection.find();
                if (getParent().isAborted()) {
                    return;
                }
                if (!getForIdTypeSection().equalsIgnoreCase("1000")) {
                    for (int i = 0; i < manTypeSection.size(); i++) {
                        tempTypeSection = (CATypeSection) manTypeSection.getEntity(i);
                        if (tempTypeSection.getIdTypeSection().equalsIgnoreCase(getForIdTypeSection())) {
                            super.setParametres(CAIListSoldeSectionParam.PARAM_TYPE_DE_SECTION,
                                    tempTypeSection.getDescription());
                        }
                    }

                } else {
                    super.setParametres(CAIListSoldeSectionParam.PARAM_TYPE_DE_SECTION, getSession().getLabel("TOUS"));
                }
            } catch (Exception e) {
            }
            // Premier niveau de tri
            super.setParametres(CAIListSoldeSectionParam.LABEL_PREMIER_TRI, getSession().getLabel("PREMIERTRI") + " :");
            if (getForSelectionTriCA().equalsIgnoreCase("1")) {
                super.setParametres(CAIListSoldeSectionParam.PARAM_PREMIER_TRI,
                        getSession().getLabel("TRICOMPTEANNEXENUMERO"));
            } else {
                super.setParametres(CAIListSoldeSectionParam.PARAM_PREMIER_TRI,
                        getSession().getLabel("TRICOMPTEANNEXENOM"));
            }
            // Deuxième niveau de tri
            super.setParametres(CAIListSoldeSectionParam.LABEL_DEUXIEME_TRI, getSession().getLabel("DEUXIEMETRI")
                    + " :");
            if (getForSelectionTriSection().equalsIgnoreCase("1")) {
                super.setParametres(CAIListSoldeSectionParam.PARAM_DEUXIEME_TRI,
                        getSession().getLabel("TRISECTIONNUMERO"));
            } else {
                super.setParametres(CAIListSoldeSectionParam.PARAM_DEUXIEME_TRI, getSession()
                        .getLabel("TRISECTIONDATE"));
            }
            // Signe
            super.setParametres(CAIListSoldeSectionParam.LABEL_SIGNE, getSession().getLabel("SIGNE") + " :");
            if (getForSelectionSigne().equalsIgnoreCase("1")) {
                super.setParametres(CAIListSoldeSectionParam.PARAM_SIGNE, getSession().getLabel("POSITIF_ET_NEGATIF"));
            } else if (getForSelectionSigne().equalsIgnoreCase("2")) {
                super.setParametres(CAIListSoldeSectionParam.PARAM_SIGNE, getSession().getLabel("POSITIF"));
            } else {
                super.setParametres(CAIListSoldeSectionParam.PARAM_SIGNE, getSession().getLabel("NEGATIF"));
            }

            if (!getParent().isAborted()) {
                addPrintParametersGenre();
            }
            if (!getParent().isAborted()) {
                addPrintParametersCategorie();
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
        super.setParametres(CAIListSoldeSectionParam.LABEL_TOTAUX, getSession().getLabel("TOTAL"));
    }

    /**
     * Methode pour insérer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {
        // Entêtes de colonnes
        this.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("COMPTEANNEXE"));
        this.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("SECTION"));
        this.setParametres(FWIImportParametre.getCol(3), getSession().getLabel("DATE"));
        this.setParametres(FWIImportParametre.getCol(4), getSession().getLabel("CONTENTIEUX"));
        this.setParametres(FWIImportParametre.getCol(5), getSession().getLabel("MONTANTBASE"));
        this.setParametres(FWIImportParametre.getCol(6), getSession().getLabel("PAIEMENTCOMPENSATION"));
        this.setParametres(FWIImportParametre.getCol(7), getSession().getLabel("SOLDESECTION"));
        this.setParametres(FWIImportParametre.getCol(8), getSession().getLabel("CUMUL_SECTIONS"));
        this.setParametres(FWIImportParametre.getCol(10), getSession().getLabel("RECAP_PAR_TYPE_DE_SECTION"));
        this.setParametres(FWIImportParametre.getCol(11), getSession().getLabel("NOMBRE"));
        this.setParametres(FWIImportParametre.getCol(12), getSession().getLabel("MONTANTBASE"));
        this.setParametres(FWIImportParametre.getCol(13), getSession().getLabel("PAIEMENTCOMPENSATION"));
        this.setParametres(FWIImportParametre.getCol(14), getSession().getLabel("SOLDESECTION"));
        this.setParametres(FWIImportParametre.getCol(16), getSession().getLabel("RECAP_CONTENTIEUX"));
    }

    /**
     * Si la catégorie est présente ajout des paramètres pour l'impression.
     */
    private void addPrintParametersCategorie() {
        if (!JadeStringUtil.isBlank(getForIdCategorie())) {
            super.setParametres(CAIListSoldeCompteAnnexeParam.LABEL_CATEGORIE, getSession().getLabel("CATEGORIE")
                    + " :");
            try {
                if (getForIdCategorie().equals(CACompteAnnexeManager.ALL_CATEGORIE)) {
                    super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_CATEGORIE,
                            getSession().getLabel(CASelectBlockParser.LABEL_TOUS));
                } else if (getForIdCategorie().equals(CACompteAnnexe.CATEGORIE_COMPTE_STANDARD)) {
                    super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_CATEGORIE,
                            getSession().getLabel(CASelectBlockParser.LABEL_COMPTE_ANNEXE_CATEGORIE_STANDARD));
                } else {
                    FWParametersSystemCodeManager manager = CACodeSystem.getCategories(getSession());

                    for (int i = 0; (i < manager.size()) && !getParent().isAborted(); i++) {
                        FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                        if (code.getIdCode().equals(getForIdCategorie())) {
                            super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_CATEGORIE, code
                                    .getCurrentCodeUtilisateur().getLibelle());
                            return;
                        }
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Si le genre est présente ajout des paramètres pour l'impression.
     */
    private void addPrintParametersGenre() {
        if (!JadeStringUtil.isBlank(getForIdGenreCompte())) {
            super.setParametres(CAIListSoldeCompteAnnexeParam.LABEL_GENRE, getSession().getLabel("GENRE") + " :");
            try {
                if (getForIdGenreCompte().equals(CACompteAnnexe.GENRE_COMPTE_STANDARD)) {
                    super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_GENRE,
                            getSession().getLabel(CASelectBlockParser.LABEL_COMPTE_AUXILIAIRE_STANDARD));
                } else {
                    FWParametersSystemCodeManager manager = CACodeSystem.getGenreComptes(getSession());

                    for (int i = 0; (i < manager.size()) && !getParent().isAborted(); i++) {
                        FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                        if (code.getIdCode().equals(getForIdGenreCompte())) {
                            super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_GENRE, code
                                    .getCurrentCodeUtilisateur().getLibelle());
                            return;
                        }
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Cette Méthode est exécutée une fois le rapport créé. On prépare ici les sous-rapports qui viendront également sur
     * la liste. On procède de cette manière suite à un problème de saut de page dans Itext2 (Saut de page sur un
     * sous-rapport)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#afterBuildReport()
     */
    @Override
    public void afterBuildReport() {
        Map globalMap = importDoc.getParametre();
        String currTemplate = importDoc.getDocumentTemplate();
        importDoc.setDocumentTemplate("CAIListSoldeSection_sub");
        int numeroPage = importDoc.getDocument().getPages().size();
        // Premiere recap
        // Definir subsource 1 --> récapitulation par type
        if (!getParent().isAborted()) {
            try {
                JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(
                        (new TreeMap(m_containerRecapType)).values());
                importDoc.setParametre(CAIListSoldeSectionParam.PARAM_PAGENUMERO, new Integer(numeroPage));
                importDoc.setDataSource(source);
                importDoc.createDocument();
            } catch (FWIException e) {
                JadeLogger.fatal(this, e);
                e.printStackTrace();
            }
            numeroPage += importDoc.getDocument().getPages().size();
        }

        if (!getParent().isAborted()) {
            // Deuxieme Recap
            // Definir subsource 2 --> Récapitulation par contentieux
            try {
                JRBeanCollectionDataSource source2 = new JRBeanCollectionDataSource((new TreeMap(
                        m_containerRecapContentieux)).values());
                importDoc.setParametre(CAIListJournalEcrituresParam.PARAM_TYPE_SUB, new Boolean(false));
                importDoc.setParametre(CAIListJournalEcrituresParam.PARAM_PAGENUMERO, new Integer(numeroPage));
                importDoc.setDataSource(source2);
                importDoc.createDocument();
            } catch (FWIException e) {
                JadeLogger.fatal(this, e);
                e.printStackTrace();
            }
        }

        if (!getParent().isAborted()) {
            // On redonne les infos d'origine
            importDoc.setParametre(globalMap);
            importDoc.setDocumentTemplate(currTemplate);

        }
    }

    @Override
    public void afterPrintDocument() throws FWIException {

        try {

            String xmlModelPath = Jade.getInstance().getExternalModelDir() + CAApplication.DEFAULT_OSIRIS_ROOT
                    + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                    + CAIListSoldeSection.XLS_DOC_NAME + "Modele.xml";

            String xlsDocPath = Jade.getInstance().getPersistenceDir()
                    + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(CAIListSoldeSection.XLS_DOC_NAME + ".xml");

            prepareDataForXLSDoc();

            xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, xlsContainer);

            JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
            docInfoExcel.setApplicationDomain(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            docInfoExcel.setDocumentTitle(CAIListSoldeSection.XLS_DOC_NAME);
            docInfoExcel.setPublishDocument(true);
            docInfoExcel.setArchiveDocument(false);
            docInfoExcel.setDocumentTypeNumber(CAIListSoldeSection.NUMERO_REFERENCE_INFOROM);
            this.registerAttachedDocument(docInfoExcel, xlsDocPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("XLS ListeSoldeSection contruction processed");
        }
    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        // Numéro de référence Inforom
        getDocumentInfo().setDocumentTypeNumber(CAIListSoldeSection.NUMERO_REFERENCE_INFOROM);
        importDoc = super.getImporter();
        getDocumentInfo().setTemplateName(importDoc.getDocumentTemplate());
        // Ajout des références en bas de liste
        super.setParametres(FWIImportParametre.PARAM_REFERENCE, _getRefParam());
        if (!getParent().isAborted()) {
            _header();
        }
        if (!getParent().isAborted()) {
            _tableHeader();
        }
        if (!getParent().isAborted()) {
            _summary();
        }
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() {
        try {
            super.setTemplateFile("CAIListSoldeSection");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 13:59:41)
     * 
     * @param id
     *            java.lang.String
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public void bindData(String id) throws Exception {
        setIdSection(id);
    }

    /**
     * Methode appelé pour la création des valeurs pour le document
     */
    @Override
    public void createDataSource() {
        List myList = new ArrayList((new TreeMap(m_container)).values());
        super.setDataSource(myList);
        listBeans = myList;
    }

    /**
     * @param entity
     * @throws Exception
     */
    private void extractValues(CASection entity) throws Exception {
        try {
            // Données
            CAIListSoldeSectionBean bean = new CAIListSoldeSectionBean(entity);
            compteur++;
            m_container.put(new Integer(compteur), bean);

            // Récapitulation par type
            String sIdTypeSection = entity.getIdTypeSection();
            CAIListSoldeSectionRecapTypeBean recapTypeBean = (CAIListSoldeSectionRecapTypeBean) m_containerRecapType
                    .get(sIdTypeSection);
            if (recapTypeBean == null) {
                recapTypeBean = new CAIListSoldeSectionRecapTypeBean(entity);
            } else {
                recapTypeBean.add(entity);
            }
            m_containerRecapType.put(sIdTypeSection, recapTypeBean);

            // Récapitulation par contentieux
            String sEtapeContentieux = entity.getResumeContentieux();
            CAIListSoldeSectionRecapContentieuxBean recapContentieuxBean = (CAIListSoldeSectionRecapContentieuxBean) m_containerRecapContentieux
                    .get(sEtapeContentieux);
            if (recapContentieuxBean == null) {
                recapContentieuxBean = new CAIListSoldeSectionRecapContentieuxBean(entity);
            } else {
                recapContentieuxBean.add(entity);
            }

            m_containerRecapContentieux.put(sEtapeContentieux, recapContentieuxBean);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

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
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeSection() {
        return forIdTypeSection;
    }

    /**
     * Returns the forSelectionRole.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Returns the forSelectionSigne.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionSigne() {
        return forSelectionSigne;
    }

    /**
     * Returns the forSelectionTriCA.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionTriCA() {
        return forSelectionTriCA;
    }

    /**
     * Returns the forSelectionTriSection.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionTriSection() {
        return forSelectionTriSection;
    }

    /**
     * Returns the idSection.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdSection() {
        return idSection;
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

        CAIListSoldeSectionManager manager = new CAIListSoldeSectionManager();
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

            transaction = new globaz.globall.db.BTransaction(getSession());
            transaction.openTransaction();
            statement = manager.cursorOpen(transaction);

            getParent().setProgressScaleValue(manager.getCount());

            // lit le nouveau entity
            while ((entity = (CASection) manager.cursorReadNext(statement)) != null) {
                if (getParent().isAborted()) {
                    return false;
                }
                getParent().incProgressCounter();
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

    private void prepareDataForXLSDoc() {

        int size = listBeans.size();

        for (int i = 0; i < size; i++) {
            xlsContainer.addValue("COL_1_VALUE", ((CAIListSoldeSectionBean) listBeans.get(i)).getCOL_1());
            xlsContainer.addValue("COL_2_VALUE", ((CAIListSoldeSectionBean) listBeans.get(i)).getCOL_2());
            xlsContainer.addValue("COL_3_VALUE", ((CAIListSoldeSectionBean) listBeans.get(i)).getCOL_3());
            xlsContainer.addValue("COL_8_VALUE", ((CAIListSoldeSectionBean) listBeans.get(i)).getCOL_4());
            xlsContainer.addValue("COL_4_VALUE", ((CAIListSoldeSectionBean) listBeans.get(i)).getCOL_5().toString());
            xlsContainer.addValue("COL_5_VALUE", ((CAIListSoldeSectionBean) listBeans.get(i)).getCOL_6().toString());
            xlsContainer.addValue("COL_6_VALUE", ((CAIListSoldeSectionBean) listBeans.get(i)).getCOL_7().toString());
            xlsContainer.addValue("COL_7_VALUE",
                    JANumberFormatter.deQuote(((CAIListSoldeSectionBean) listBeans.get(i)).getCOL_8()));
        }
    }

    /**
     * Méthode appelé pour lancer l'exportation du document Par défaut ne pas utiliser car déjà implémenté par la
     * superClass Utile si on ne veut pas exporter en fichier temporaire Date de création : (17.02.2003 14:44:15)
     */
    @Override
    public void returnDocument() throws FWIException {
        super.imprimerListDocument();
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
    public void setForIdTypeSection(java.lang.String forIdTypeSection) {
        this.forIdTypeSection = forIdTypeSection;
    }

    /**
     * Sets the forSelectionRole.
     * 
     * @param forSelectionRole
     *            The forSelectionRole to set
     */
    public void setForSelectionRole(java.lang.String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

    /**
     * Sets the forSelectionSigne.
     * 
     * @param forSelectionSigne
     *            The forSelectionSigne to set
     */
    public void setForSelectionSigne(java.lang.String forSelectionSigne) {
        this.forSelectionSigne = forSelectionSigne;
    }

    /**
     * Sets the forSelectionTriCA.
     * 
     * @param forSelectionTriCA
     *            The forSelectionTriCA to set
     */
    public void setForSelectionTriCA(java.lang.String forSelectionTriCA) {
        this.forSelectionTriCA = forSelectionTriCA;
    }

    /**
     * Sets the forSelectionTriSection.
     * 
     * @param forSelectionTriSection
     *            The forSelectionTriSection to set
     */
    public void setForSelectionTriSection(java.lang.String forSelectionTriSection) {
        this.forSelectionTriSection = forSelectionTriSection;
    }

    /**
     * Sets the idSection.
     * 
     * @param idSection
     *            The idSection to set
     */
    public void setIdSection(java.lang.String idSection) {
        this.idSection = idSection;
    }

}
