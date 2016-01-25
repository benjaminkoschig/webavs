package globaz.helios.itext.list;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.types.FWIDocumentType;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.helios.application.CGApplication;
import globaz.helios.db.classifications.CGClasseCompteManager;
import globaz.helios.db.classifications.CGClasseCompteServices;
import globaz.helios.db.classifications.CGClassificationNode;
import globaz.helios.db.classifications.CGExtendedCompteSoldeManager;
import globaz.helios.itext.list.balance.comptes.CGCompteSoldeBean;
import globaz.helios.itext.list.pertesprofits.CGPertesProfitsDS;
import globaz.helios.itext.list.pertesprofits.CGPertesProfits_ParameterList;
import globaz.helios.itext.list.utils.CGImpressionUtils;
import globaz.helios.tools.CGXLSContructor;
import globaz.helios.tools.TimeHelper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @revision SEL mai 2013 - multi niveaux de classification
 */
public class CGProcessImpressionPertesProfits extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUMERO_REFERENCE_INFOROM = "0056GCF";
    private static final String PERTE_PROFITS = "PertesProfits";
    private static final String TEMPLATE_DOC = "cg_pertes_profits_multi";
    public static final String XLS_DOC_NAME = CGProcessImpressionPertesProfits.PERTE_PROFITS + "Multi";

    private String classesCompteList;
    private String forCodePeriode = "";
    private String idCentreCharge = "0";
    private String idClassification = "";
    private String idComptabilite;
    private String idDefinitionListe = "";
    private String idDomaine;
    private String idExerciceComptable;
    private String idMandat;
    private String idPeriodeComptable;
    private Boolean imprimerComptes = true;
    private Boolean imprimerPageGarde = true;
    private Boolean imprimerTitres = true;
    private Boolean inclurePeriodePrecedente = true;
    private boolean isFirst = true;
    private int niveauClassification = 0;
    private Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot = new HashMap<String, CGClassificationNode<CGCompteSoldeBean>>();
    private String typeImpression = CGImpressionUtils.TYPE_IMPRESSION_PDF;

    /**
     * @throws Exception
     */
    public CGProcessImpressionPertesProfits() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public CGProcessImpressionPertesProfits(BProcess parent, String arg1, String arg2) throws FWIException {
        super(parent, arg1, arg2);
        super.setDocumentTitle(parent.getSession().getLabel("IMPRESSION_PERTESPROFITS_TITLE"));
    }

    /**
     * CGPertesProfits_Doc constructor comment.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CGProcessImpressionPertesProfits(BSession session) throws FWIException {
        this(session, CGApplication.APPLICATION_HELIOS_REP, CGProcessImpressionPertesProfits.PERTE_PROFITS);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public CGProcessImpressionPertesProfits(BSession session, String arg1, String arg2) throws FWIException {
        super(session, arg1, arg2);
        super.setDocumentTitle(session.getLabel("IMPRESSION_PERTESPROFITS_TITLE"));
    }

    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isBlank(idExerciceComptable)) {
            throw new Exception(getSession().getLabel("IMPRESSION_PERTESPROFITS_ERR_1"));
        }

        if (JadeStringUtil.isBlank(idMandat)) {
            throw new Exception(getSession().getLabel("IMPRESSION_PERTESPROFITS_ERR_3"));
        }
        if (getParent() == null) {
            setSendCompletionMail(true);
            setControleTransaction(true);
        }

        if (getSession().hasErrors()) {
            abort();
        }

    }

    /**
     * @see FWIDocumentManager#afterBuildReport()
     */
    @Override
    public void afterBuildReport() {
        super.afterBuildReport();
        getDocumentInfo().setDocumentTypeNumber(CGProcessImpressionPertesProfits.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            getExporter().setExportFileType(FWIDocumentType.PDF);

            super.setParametres(CGPertesProfits_ParameterList.PARAM_XLS_MODE, "false");

            // Assignation des paramètres
            // super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_COMPANYNAME,
            // getSession().getLabel("IMPRESSION_PERTESPROFITS_COMPANYNAME"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_COMPTABILITE, getSession()
                    .getLabel("IMPRESSION_PERTESPROFITS_COMPTABILITE"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_DEFLISTE, getSession()
                    .getLabel("IMPRESSION_PERTESPROFITS_DEFLISTE"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_PERIODE,
                    getSession().getLabel("IMPRESSION_PERTESPROFITS_PERIODE"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_COL1,
                    getSession().getLabel("IMPRESSION_PERTESPROFITS_COL1"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_COL2,
                    getSession().getLabel("IMPRESSION_PERTESPROFITS_COL2"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_COL3,
                    getSession().getLabel("IMPRESSION_PERTESPROFITS_COL3"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_COL4,
                    getSession().getLabel("IMPRESSION_PERTESPROFITS_COL4"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_TOTAL,
                    getSession().getLabel("IMPRESSION_PERTESPROFITS_TOTAL"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_TOTALFINAL, getSession()
                    .getLabel("IMPRESSION_PERTESPROFITS_TOTALFINAL"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_TITRE,
                    getSession().getLabel("IMPRESSION_PERTESPROFITS_TITRE"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_BENEFICE, getSession()
                    .getLabel("PARSER_RESULTAT"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_PERTE,
                    getSession().getLabel("PARSER_RESULTAT"));
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_CENTRECHARGE, getSession()
                    .getLabel("IMPRESSION_PERTESPROFITS_CENTRECHARGE"));
            // Bug 3007
            super.setParametres("NUMERO_INFOROM", CGProcessImpressionPertesProfits.NUMERO_REFERENCE_INFOROM);

            // Assignation template
            setTemplateFile(CGProcessImpressionPertesProfits.TEMPLATE_DOC);
        } catch (Exception e) {
            throw new FWIException(e);
        }
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        // Doit être placé avant loadClassification et findCompteSolde car l'on initialise idClassification
        prepareParameter();

        try {
            CGClasseCompteManager classeCompteManager = CGClasseCompteServices.findClasseCompte(getTransaction(),
                    idClassification);
            CGExtendedCompteSoldeManager compteSoldeManager = findCompteSolde(getTransaction(), idClassification,
                    idExerciceComptable, forCodePeriode);

            /** Critère des classifications à afficher */
            List<String> classeListe = Arrays.asList(classesCompteList.trim().split(" "));

            CGClasseCompteServices.loadClassification(getSession().getIdLangueISO(), classeCompteManager, nodesDepot,
                    classeListe);

            CGClasseCompteServices.loadCompteSoldeLevel(compteSoldeManager, nodesDepot, getSession().getIdLangueISO(),
                    CGClasseCompteServices.isProvisoire(getIdComptabilite()), classeListe);

        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), e.getMessage());
        }
    }

    @Override
    public boolean beforePrintDocument() {
        if (isTypeImpressionXls()) {
            return false;
        }

        return super.beforePrintDocument();
    }

    /**
     * Methode appelé pour la création des valeurs pour le document 1) addRow (si nécessaire) 2) Appèle des méthodes
     * pour la création des paramètres
     */
    @Override
    public void createDataSource() throws Exception {
        // Remplit la liste de beans
        List<CGCompteSoldeBean> listBeanDocument = new ArrayList<CGCompteSoldeBean>();
        sortAndDisplayResult(nodesDepot, listBeanDocument);

        // Si aucune donnée, on sort
        if (listBeanDocument.size() == 0) {
            getMemoryLog().logMessage("GLOBAL_AUCUN_DOCUMENT_A_IMPRIMER", null, FWMessage.FATAL,
                    this.getClass().getName());
        }

        CGPertesProfitsDS ds = new CGPertesProfitsDS(listBeanDocument);

        if (isTypeImpressionXls()) {
            createExcel(ds);
        } else {
            this.setDataSource(ds);
        }
    }

    /**
     * @param ds
     * @return
     * @throws Exception
     */
    private boolean createExcel(CGPertesProfitsDS ds) throws Exception {
        CGXLSContructor xlsc = new CGXLSContructor();
        xlsc.setDocumentFileName(CGProcessImpressionPertesProfits.XLS_DOC_NAME);
        xlsc.setSession(getSession());

        beforeBuildReport();
        xlsc.process(ds.getListBeanDocument(), super.getImporter().getParametre());

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(CGApplication.DEFAULT_APPLICATION_HELIOS);
        docInfoExcel.setDocumentTitle(xlsc.getDocumentFileName());
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(CGProcessImpressionPertesProfits.NUMERO_REFERENCE_INFOROM);
        super.registerAttachedDocument(docInfoExcel, xlsc.getExportPath());

        return true;
    }

    /**
     * @param transaction
     * @param forIdClassification
     * @return the full manager or null if Exception
     */
    private CGExtendedCompteSoldeManager findCompteSolde(BTransaction transaction, String forIdClassification,
            String forIdExerciceComptable, String forCodePeriode) {
        // Construction de la source
        CGExtendedCompteSoldeManager manager = new CGExtendedCompteSoldeManager();
        manager.setSession(transaction.getSession());
        manager.changeManagerSize(BManager.SIZE_NOLIMIT);
        manager.setInclurePeriode(getInclurePeriodePrecedente());
        manager.setForCodePeriode(forCodePeriode);
        manager.setForIdClassification(forIdClassification);
        // Paramétrage de la source
        manager.setForIdMandat(idMandat);
        manager.setForIdExerciceComptable(forIdExerciceComptable);
        manager.setForIdCentreCharge(idCentreCharge);

        try {
            manager.find(transaction);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return null;
        }
        return manager;
    }

    /**
     * Returns the classesCompteList.
     * 
     * @return String
     */
    public String getClassesCompteList() {
        return classesCompteList;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_PERTESPROFITS_EMAIL_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_PERTESPROFITS_EMAIL_OK");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2003 16:20:25)
     * 
     * @return String
     */
    public String getIdCentreCharge() {
        return idCentreCharge;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:04:40)
     * 
     * @return String
     */
    public String getIdComptabilite() {
        return idComptabilite;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:57:58)
     * 
     * @return String
     */
    public String getIdDefinitionListe() {
        return idDefinitionListe;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:04:40)
     * 
     * @return String
     */
    public String getIdDomaine() {
        return idDomaine;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:57:18)
     * 
     * @return String
     */
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:58:23)
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:04:40)
     * 
     * @return String
     */
    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    /**
     * @return the imprimerComptes
     */
    public Boolean getImprimerComptes() {
        return imprimerComptes;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:00:21)
     * 
     * @return Boolean
     */
    public Boolean getImprimerPageGarde() {
        return imprimerPageGarde;
    }

    /**
     * @return the imprimerTitres
     */
    public Boolean getImprimerTitres() {
        return imprimerTitres;
    }

    public Boolean getInclurePeriodePrecedente() {
        return inclurePeriodePrecedente;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:59:45)
     * 
     * @return String
     */
    public String getListeIdClasseCompte() {
        return classesCompteList;
    }

    /**
     * @return the niveauClassification
     */
    public int getNiveauClassification() {
        return niveauClassification;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    /**
     * @param nodeValue
     * @return
     */
    private boolean hasMontant(CGCompteSoldeBean nodeValue) {
        return (nodeValue.computeSoldeProduit() != null) || (nodeValue.computeSoldeCharge() != null)
                || (nodeValue.getTotalCharges() != null) || (nodeValue.getTotalProduits() != null);
    }

    /**
     * Insert the method's description here. Creation date: (07.07.2003 17:14:22)
     * 
     * @return Boolean
     */
    public Boolean isImprimerComptes() {
        return imprimerComptes;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 11:44:45)
     * 
     * @return boolean
     */
    public Boolean isImprimerPageGarde() {
        return imprimerPageGarde;
    }

    /**
     * Insert the method's description here. Creation date: (07.07.2003 17:24:10)
     * 
     * @return Boolean
     */
    public Boolean isImprimerTitres() {
        return imprimerTitres;
    }

    public boolean isInclurePeriodePrecedente() {
        return getInclurePeriodePrecedente().booleanValue();
    }

    private boolean isTypeImpressionXls() {
        return getTypeImpression().equals(CGImpressionUtils.TYPE_IMPRESSION_XLS);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (isFirst) {
            isFirst = false;
            return true;
        }
        return false;
    }

    /**
     * @throws Exception
     * @throws JAException
     */
    private void prepareParameter() {
        try {
            // Description comptabilité
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_COMPTABILITE,
                    globaz.helios.translation.CodeSystem.getLibelle(getSession(), idComptabilite));

            super.setParametres("P_DATE_TIME", TimeHelper.getCurrentTime());
            // Bug 3007
            super.setParametres("P_DATE_TIME_USER", TimeHelper.getCurrentTime() + " - " + getSession().getUserName());
            // Description exercice comptable
            globaz.helios.db.comptes.CGExerciceComptable exerComptable = new globaz.helios.db.comptes.CGExerciceComptable();
            exerComptable.setIdExerciceComptable(idExerciceComptable);
            exerComptable.setSession(getSession());
            exerComptable.retrieve();
            if (!exerComptable.isNew()) {
                // Set default value if no periode provided
                super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_EXERCICE,
                        exerComptable.getFullDescription());
                // Date des P&P à la fin de l'exercice
                super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_DATE, exerComptable.getDateFin());
                super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_PERIODE,
                        exerComptable.getFullDescription());
            }

            // Description période comptable
            if (!JadeStringUtil.isIntegerEmpty(idPeriodeComptable)) {
                globaz.helios.db.comptes.CGPeriodeComptable periodeComptable = new globaz.helios.db.comptes.CGPeriodeComptable();
                periodeComptable.setIdPeriodeComptable(idPeriodeComptable);
                periodeComptable.setSession(getSession());
                periodeComptable.retrieve();
                if (!periodeComptable.isNew()) {
                    forCodePeriode = periodeComptable.getCode();
                    if (isInclurePeriodePrecedente() && (JACalendar.getMonth(periodeComptable.getDateFin()) != 1)) {
                        super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_PERIODE, getSession()
                                .getLabel("JANVIER_A") + " " + periodeComptable.getFullDescription());
                    } else {
                        super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_PERIODE,
                                periodeComptable.getFullDescription());
                    }
                    // Date des P&P à la fin de la période
                    super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_DATE,
                            periodeComptable.getDateFin());
                }
            } else {
                // Si l'option "Tout l'exercice" est selectionnée, on l'affiche sur la liste
                if ("0".equals(idPeriodeComptable)) {
                    super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_PERIODE, getSession()
                            .getLabel("TOUT_LEXERCICE"));
                }
            }

            // Description definition de liste
            globaz.helios.db.classifications.CGDefinitionListe definitionListe = new globaz.helios.db.classifications.CGDefinitionListe();
            definitionListe.setIdDefinitionListe(idDefinitionListe);
            definitionListe.setSession(getSession());
            definitionListe.retrieve();
            if (!definitionListe.isNew()) {
                super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_DEFLISTE,
                        definitionListe.getLibelle());
                idClassification = definitionListe.getIdClassification();
            }

            // Impression détails page de garde
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_IMPRIMER_PAGE_GARDE,
                    imprimerPageGarde);
            // Impression titres
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_IMPRIMER_TITRES, imprimerTitres);
            // Impression comptes
            super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_IMPRIMER_COMPTES, imprimerComptes);

            // Description du mandat
            if (!JadeStringUtil.isIntegerEmpty(getIdMandat())) {
                globaz.helios.db.comptes.CGMandat mandat = new globaz.helios.db.comptes.CGMandat();
                mandat.setIdMandat(idMandat);
                mandat.setSession(getSession());
                mandat.retrieve();
                if (!mandat.isNew()) {
                    super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_LABEL_COMPANYNAME,
                            mandat.getLibelle());
                }
            }

            // Description du centre de charge
            if (!JadeStringUtil.isIntegerEmpty(getIdCentreCharge())) {
                globaz.helios.db.comptes.CGCentreCharge centreCharge = new globaz.helios.db.comptes.CGCentreCharge();
                centreCharge.setIdCentreCharge(getIdCentreCharge());
                centreCharge.setIdMandat(getIdMandat());
                centreCharge.setSession(getSession());
                centreCharge.retrieve(getTransaction());
                if (!centreCharge.isNew()) {
                    super.setParametres(CGPertesProfits_ParameterList.PARAM_PERTESPROFITS_CENTRECHARGE,
                            centreCharge.getLibelle());
                }
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            JadeLogger.error(this, e);
        }
    }

    /**
     * Crée un bean avec les informations que l'on souhaite afficher dans la liste. Cela dépend si c'est un titre, un
     * compte avec solde ou un total.
     * 
     * @param node
     *            : noeud a traiter
     * @param listBeanDocument
     *            : liste de bean pour le Jasper. Un bean est soit un titre, soit un compte, soit un total.
     */
    private void recurseDisplayNode(CGClassificationNode<CGCompteSoldeBean> node,
            List<CGCompteSoldeBean> listBeanDocument) {

        if (node == null) {
            throw new IllegalArgumentException("Error : CGClassificationNode<CGBalanceComptesBean> is null !");
        }
        if (listBeanDocument == null) {
            throw new IllegalArgumentException("Error : List<CGBalanceComptesBean> is null !");
        }

        CGCompteSoldeBean nodeValue = node.getValue();
        if (!node.isLeaf()) {
            if (hasMontant(nodeValue)) {
                if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, nodeValue.getNiveau())) {
                    if ((nodeValue.getTotalCharges() != null) || (nodeValue.getTotalProduits() != null)) {
                        CGCompteSoldeBean bean = new CGCompteSoldeBean(nodeValue);
                        bean.resetMvt();
                        bean.resetTotaux();
                        // HACK pour iReport afin de gérer correctement les groupes
                        bean.setNiveau(bean.getNiveau() + bean.getNoClasse());
                        listBeanDocument.add(bean);
                    }
                }
            }

            if (node.hasChildren()) {
                for (CGClassificationNode<CGCompteSoldeBean> child : node.getChildren().values()) {
                    recurseDisplayNode(child, listBeanDocument);
                }
            }

            // TOTAUX
            if (hasMontant(nodeValue)) {
                if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, nodeValue.getNiveau())) {
                    if ((nodeValue.getTotalCharges() != null) || (nodeValue.getTotalProduits() != null)) {
                        CGCompteSoldeBean bean = new CGCompteSoldeBean(nodeValue);
                        bean.resetMvt();
                        // HACK pour iReport afin de gérer correctement les groupes
                        bean.setNiveau(bean.getNiveau() + bean.getNoClasse());
                        bean.setNoClasseLibelle(getSession().getLabel("IMPRESSION_BALANCE_COMPTES_TOTAL") + " "
                                + bean.getNoClasseLibelle());

                        Double totalCharges = bean.getTotalCharges() != null ? bean.getTotalCharges() : 0;
                        Double totalProduits = bean.getTotalProduits() != null ? bean.getTotalProduits() : 0;
                        bean.setResultat(totalCharges - totalProduits);
                        listBeanDocument.add(bean);
                    }
                }
            }
        } else if (hasMontant(nodeValue)) {
            if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, nodeValue.getNiveau())) {
                CGCompteSoldeBean bean = nodeValue;
                bean.resetTotaux();
                listBeanDocument.add(bean);
            }
        }
    }

    /**
     * Sets the classesCompteList.
     * 
     * @param classesCompteList
     *            The classesCompteList to set
     */
    public void setClassesCompteList(String classesCompteList) {
        this.classesCompteList = classesCompteList;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2003 16:20:25)
     * 
     * @param newIdCentreCharge
     *            String
     */
    public void setIdCentreCharge(String newIdCentreCharge) {
        idCentreCharge = newIdCentreCharge;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:04:40)
     * 
     * @param newIdComptabilite
     *            String
     */
    public void setIdComptabilite(String newIdComptabilite) {
        idComptabilite = newIdComptabilite;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:57:58)
     * 
     * @param newIdDefinitionListe
     *            String
     */
    public void setIdDefinitionListe(String newIdDefinitionListe) {
        idDefinitionListe = newIdDefinitionListe;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:04:40)
     * 
     * @param newIdDomaine
     *            String
     */
    public void setIdDomaine(String newIdDomaine) {
        idDomaine = newIdDomaine;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:57:18)
     * 
     * @param newIdExerciceComptable
     *            String
     */
    public void setIdExerciceComptable(String newIdExerciceComptable) {
        idExerciceComptable = newIdExerciceComptable;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:58:23)
     * 
     * @param newIdMandat
     *            String
     */
    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:04:40)
     * 
     * @param newIdPeriodeComptable
     *            String
     */
    public void setIdPeriodeComptable(String newIdPeriodeComptable) {
        idPeriodeComptable = newIdPeriodeComptable;
    }

    /**
     * Insert the method's description here. Creation date: (07.07.2003 17:14:22)
     * 
     * @param newImprimerComptes
     *            Boolean
     */
    public void setImprimerComptes(Boolean newImprimerComptes) {
        imprimerComptes = newImprimerComptes;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:00:21)
     * 
     * @param newImprimerPageGarde
     *            Boolean
     */
    public void setImprimerPageGarde(Boolean newImprimerPageGarde) {
        imprimerPageGarde = newImprimerPageGarde;
    }

    /**
     * Insert the method's description here. Creation date: (07.07.2003 17:24:10)
     * 
     * @param newImprimerTitres
     *            Boolean
     */
    public void setImprimerTitres(Boolean newImprimerTitres) {
        imprimerTitres = newImprimerTitres;
    }

    public void setInclurePeriodePrecedente(Boolean inclurePeriodePrecedente) {
        this.inclurePeriodePrecedente = inclurePeriodePrecedente;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:59:45)
     * 
     * @param newListeIdClasseCompte
     *            String
     */
    public void setListeIdClasseCompte(String newListeIdClasseCompte) {
        classesCompteList = newListeIdClasseCompte;
    }

    /**
     * @param niveauClassification
     *            the niveauClassification to set
     */
    public void setNiveauClassification(int niveauClassification) {
        this.niveauClassification = niveauClassification;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

    /**
     * Recherche les noeuds racines afin d'en afficher l'arbre
     * 
     * @param nodesDepot
     *            : dépot de noeuds
     * @param listBeanDocument
     *            : list de bean soumise à JasperReport
     */
    private void sortAndDisplayResult(Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot,
            List<CGCompteSoldeBean> listBeanDocument) {
        TreeMap<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepotTrie = new TreeMap<String, CGClassificationNode<CGCompteSoldeBean>>();

        // Trie dans l'ordre croissant des numéros de classification
        for (CGClassificationNode<CGCompteSoldeBean> node : nodesDepot.values()) {
            nodesDepotTrie.put(node.getValue().getNoClasse(), node);
        }

        for (CGClassificationNode<CGCompteSoldeBean> node : nodesDepotTrie.values()) {
            if (node.isRoot()) {
                recurseDisplayNode(node, listBeanDocument);
            }
        }
    }

}
