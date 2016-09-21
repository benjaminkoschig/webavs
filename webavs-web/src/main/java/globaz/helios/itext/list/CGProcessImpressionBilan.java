package globaz.helios.itext.list;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.helios.application.CGApplication;
import globaz.helios.db.classifications.CGClasseCompteManager;
import globaz.helios.db.classifications.CGClasseCompteServices;
import globaz.helios.db.classifications.CGClassificationNode;
import globaz.helios.db.classifications.CGDefinitionListe;
import globaz.helios.db.classifications.CGExtendedCompteSoldeManager;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.itext.list.balance.comptes.CGCompteSoldeBean;
import globaz.helios.itext.list.bilan.CGBilanDS;
import globaz.helios.itext.list.bilan.CGBilan_ParameterList;
import globaz.helios.itext.list.utils.CGImpressionUtils;
import globaz.helios.tools.CGXLSContructor;
import globaz.helios.tools.TimeHelper;
import globaz.helios.translation.CodeSystem;
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
 * Process permettant l'impression du bilan en PDF ou Excel.
 * 
 * Creation date: (30.06.2003 16:45:15)
 * 
 * @author: ema
 * @revision SEL mai 2013 - multi niveaux de classification
 */
public class CGProcessImpressionBilan extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String BILAN = "Bilan";
    private static final String NUMERO_REFERENCE_INFOROM = "0051GCF";
    private static final String TEMPLATE_DOC = "cg_bilan_multi_niveau";
    public static final String XLS_DOC_NAME = CGProcessImpressionBilan.BILAN + "Multi";

    private String classesCompteList;
    private String forCodePeriode = "";
    private String idCentreCharge = "0";
    private String idClassification = "";
    private String idComptabilite;
    private String idDefinitionListe = "";
    private String idExerciceComptable;
    private String idMandat;
    private String idPeriodeComptable;
    private Boolean imprimerComptes = true;
    private Boolean imprimerPageGarde = true;
    private Boolean imprimerTitres = true;
    private boolean isFirst = true;

    private int niveauClassification = 0;
    private Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot;
    private Map<String, Comparable<?>> parametres = new HashMap<String, Comparable<?>>();
    private String typeImpression = CGImpressionUtils.TYPE_IMPRESSION_PDF;

    /**
     * @throws Exception
     */
    public CGProcessImpressionBilan() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public CGProcessImpressionBilan(BProcess parent, String arg1, String arg2) throws FWIException {
        super(parent, arg1, arg2);
        init(parent.getSession());
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public CGProcessImpressionBilan(BSession session, String arg1, String arg2) throws FWIException {
        super(session, arg1, arg2);
        init(session);
    }

    /**
     * CGBilan_Doc constructor comment.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CGProcessImpressionBilan(globaz.globall.db.BSession session) throws FWIException {
        this(session, CGApplication.APPLICATION_HELIOS_REP, CGProcessImpressionBilan.BILAN);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
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
        getDocumentInfo().setDocumentTypeNumber(CGProcessImpressionBilan.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        try {
            super.setParametres(CGBilan_ParameterList.PARAM_XLS_MODE, new Boolean(isTypeImpressionXls()).toString());

            // Assignation des paramètres
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_COMPTABILITE,
                    getSession().getLabel("IMPRESSION_BILAN_COMPTABILITE"));
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_DEFLISTE,
                    getSession().getLabel("IMPRESSION_BILAN_DEFLISTE"));
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_PERIODE,
                    getSession().getLabel("IMPRESSION_BILAN_PERIODE"));
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_COL1,
                    getSession().getLabel("IMPRESSION_BILAN_COL1"));
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_COL2,
                    getSession().getLabel("IMPRESSION_BILAN_COL2"));
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_COL3,
                    getSession().getLabel("IMPRESSION_BILAN_COL3"));
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_COL4,
                    getSession().getLabel("IMPRESSION_BILAN_COL4"));
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_TOTAL,
                    getSession().getLabel("IMPRESSION_BILAN_TOTAL"));
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_TOTALFINAL,
                    getSession().getLabel("IMPRESSION_BILAN_TOTALFINAL"));
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_TITRE,
                    getSession().getLabel("IMPRESSION_BILAN_TITRE"));
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_BENEFICE,
                    getSession().getLabel("PARSER_RESULTAT"));
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_PERTE, getSession().getLabel("PARSER_RESULTAT"));

            super.setParametres("P_DATE_TIME", TimeHelper.getCurrentTime());
            parametres.put("P_DATE_TIME", TimeHelper.getCurrentTime());
            // bug 3007
            parametres.put("P_DATE_TIME_USER", TimeHelper.getCurrentTime() + " - " + getSession().getUserName());
            // Description comptabilité
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_COMPTABILITE,
                    CodeSystem.getLibelle(getSession(), idComptabilite));
            parametres.put(CGBilan_ParameterList.PARAM_BILAN_COMPTABILITE,
                    CodeSystem.getLibelle(getSession(), idComptabilite));

            // Impression détails page de garde
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_IMPRIMER_PAGE_GARDE, imprimerPageGarde);
            // Impression titres
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_IMPRIMER_TITRES, imprimerTitres);
            // Impression comptes
            super.setParametres(CGBilan_ParameterList.PARAM_BILAN_IMPRIMER_COMPTES, imprimerComptes);

            super.setParametres("NUMERO_INFOROM", CGProcessImpressionBilan.NUMERO_REFERENCE_INFOROM);
            parametres.put("NUMERO_INFOROM", CGProcessImpressionBilan.NUMERO_REFERENCE_INFOROM);

            this.setParametres(CGBilan_ParameterList.PARAM_BILAN_REFERENCE,
                    CGImpressionUtils.getReference(getSession(), this.getClass().toString()));

            // Assignation template
            setTemplateFile(CGProcessImpressionBilan.TEMPLATE_DOC);
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            JadeLogger.error(this, e);
        }
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() {
        // Doit être placé avant loadClassification et findCompteSolde car l'on initialise idClassification
        prepareParameter(parametres);

        try {
            CGClasseCompteManager classeCompteManager = CGClasseCompteServices.findClasseCompte(getTransaction(),
                    idClassification);
            CGExtendedCompteSoldeManager compteSoldeManager = findCompteSolde(getTransaction(), idClassification);

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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforePrintDocument()
     */
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
        List<CGCompteSoldeBean> listBeanDocument = sortAndDisplayResult(nodesDepot);

        // Si aucune donnée, on sort
        if (listBeanDocument.size() == 0) {
            getMemoryLog().logMessage("GLOBAL_AUCUN_DOCUMENT_A_IMPRIMER", null, FWMessage.FATAL,
                    this.getClass().getName());
        }

        CGBilanDS ds = new CGBilanDS(listBeanDocument);

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
    private boolean createExcel(CGBilanDS ds) throws Exception {
        CGXLSContructor xlsc = new CGXLSContructor();
        xlsc.setDocumentFileName(CGProcessImpressionBilan.XLS_DOC_NAME);
        xlsc.setSession(getSession());

        beforeBuildReport();
        xlsc.process(ds.getListBeanDocument(), parametres);

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(CGApplication.DEFAULT_APPLICATION_HELIOS);
        docInfoExcel.setDocumentTitle(CGProcessImpressionBilan.XLS_DOC_NAME);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(CGProcessImpressionBilan.NUMERO_REFERENCE_INFOROM);
        this.registerAttachedDocument(docInfoExcel, xlsc.getExportPath());

        return true;
    }

    /**
     * @param transaction
     * @param forIdClassification
     * @return the full manager or null if Exception
     */
    private CGExtendedCompteSoldeManager findCompteSolde(BTransaction transaction, String forIdClassification) {
        // Construction de la source
        CGExtendedCompteSoldeManager manager = new CGExtendedCompteSoldeManager();
        manager.setSession(transaction.getSession());
        manager.changeManagerSize(BManager.SIZE_NOLIMIT);

        manager.setForCodePeriode(forCodePeriode);
        manager.setForIdClassification(forIdClassification);
        // Paramétrage de la source
        manager.setForIdMandat(idMandat);
        manager.setForIdExerciceComptable(idExerciceComptable);
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
     * Creation date: (02.07.2003 09:39:53)
     * 
     * @return String
     */
    public String getClassesCompteList() {
        return classesCompteList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.FWIDocumentManager#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_BILAN_EMAIL_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_BILAN_EMAIL_OK");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2003 16:18:43)
     * 
     * @return String
     */
    public String getIdCentreCharge() {
        return idCentreCharge;
    }

    /**
     * Creation date: (01.07.2003 17:43:35)
     * 
     * @return String
     */
    public String getIdComptabilite() {
        return idComptabilite;
    }

    /**
     * Creation date: (02.07.2003 10:26:13)
     * 
     * @return String
     */
    public String getIdDefinitionListe() {
        return idDefinitionListe;
    }

    /**
     * Creation date: (02.07.2003 09:38:36)
     * 
     * @return String
     */
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Creation date: (02.07.2003 09:38:08)
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * Creation date: (02.07.2003 08:42:24)
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
     * @return the imprimerPageGarde
     */
    public Boolean getImprimerPageGarde() {
        return imprimerPageGarde;
    }

    /**
     * @return
     */
    public Boolean getImprimerTitres() {
        return imprimerTitres;
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
        return (nodeValue.computeSoldeActif() != null) || (nodeValue.computeSoldePassif() != null)
                || (nodeValue.getTotalActif() != null) || (nodeValue.getTotalPassif() != null);
    }

    /**
     * @param session
     */
    private void init(BSession session) {
        super.setDocumentTitle(session.getLabel("IMPRESSION_BILAN_TITLE"));
        nodesDepot = new HashMap<String, CGClassificationNode<CGCompteSoldeBean>>();
    }

    /**
     * Creation date: (07.07.2003 17:14:22)
     * 
     * @return Boolean
     */
    public Boolean isImprimerComptes() {
        return imprimerComptes;
    }

    /**
     * Creation date: (02.07.2003 11:44:45)
     * 
     * @return boolean
     */
    public Boolean isImprimerPageGarde() {
        return imprimerPageGarde;
    }

    /**
     * Creation date: (07.07.2003 17:24:10)
     * 
     * @return Boolean
     */
    public Boolean isImprimerTitres() {
        return imprimerTitres;
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
     * Préparation des parametres globaux
     * 
     * @param parametres
     */
    private void prepareParameter(Map<String, Comparable<?>> parametres) {
        try {
            // Description exercice comptable
            CGExerciceComptable exerComptable = retrieveExerciceComptable();
            if (!exerComptable.isNew()) {
                // Set default value, if no periode comptable
                super.setParametres(CGBilan_ParameterList.PARAM_BILAN_PERIODE, exerComptable.getFullDescription());
                this.parametres.put(CGBilan_ParameterList.PARAM_BILAN_PERIODE, exerComptable.getFullDescription());
                super.setParametres(CGBilan_ParameterList.PARAM_BILAN_EXERCICE, exerComptable.getFullDescription());
                this.parametres.put(CGBilan_ParameterList.PARAM_BILAN_EXERCICE, exerComptable.getFullDescription());
                // Date du bilan à la fin de l'exercice
                super.setParametres(CGBilan_ParameterList.PARAM_BILAN_DATE, exerComptable.getDateFin());
                this.parametres.put(CGBilan_ParameterList.PARAM_BILAN_DATE, exerComptable.getDateFin());
            }

            // Description période comptable
            if (!JadeStringUtil.isIntegerEmpty(idPeriodeComptable)) {
                CGPeriodeComptable periodeComptable = retrievePeriodeComptable();
                if (!periodeComptable.isNew()) {
                    forCodePeriode = periodeComptable.getCode();
                    if (JACalendar.getMonth(periodeComptable.getDateFin()) != 1) {
                        super.setParametres(CGBilan_ParameterList.PARAM_BILAN_PERIODE,
                                getSession().getLabel("JANVIER_A") + " " + periodeComptable.getFullDescription());
                        this.parametres.put(CGBilan_ParameterList.PARAM_BILAN_PERIODE,
                                getSession().getLabel("JANVIER_A") + " " + periodeComptable.getFullDescription());
                    } else {
                        super.setParametres(CGBilan_ParameterList.PARAM_BILAN_PERIODE,
                                periodeComptable.getFullDescription());
                        this.parametres.put(CGBilan_ParameterList.PARAM_BILAN_PERIODE,
                                periodeComptable.getFullDescription());
                    }
                    // Date du bilan à la fin de la période
                    super.setParametres(CGBilan_ParameterList.PARAM_BILAN_DATE, periodeComptable.getDateFin());
                    this.parametres.put(CGBilan_ParameterList.PARAM_BILAN_DATE, periodeComptable.getDateFin());
                }
            } else {
                // Si l'option "Tout l'exercice" est selectionnée, on l'affiche sur la liste
                if ("0".equals(idPeriodeComptable)) {
                    super.setParametres(CGBilan_ParameterList.PARAM_BILAN_PERIODE,
                            getSession().getLabel("TOUT_LEXERCICE"));
                    this.parametres.put(CGBilan_ParameterList.PARAM_BILAN_PERIODE,
                            getSession().getLabel("TOUT_LEXERCICE"));
                }
            }

            // Description definition de liste
            CGDefinitionListe definitionListe = retrieveDefinitionListe();
            if (!definitionListe.isNew()) {
                super.setParametres(CGBilan_ParameterList.PARAM_BILAN_DEFLISTE, definitionListe.getLibelle());
                this.parametres.put(CGBilan_ParameterList.PARAM_BILAN_DEFLISTE, definitionListe.getLibelle());
                idClassification = definitionListe.getIdClassification();
            }

            // Description du mandat
            if (!JadeStringUtil.isIntegerEmpty(idMandat)) {
                CGMandat mandat = retrieveMandat();
                if (!mandat.isNew()) {
                    super.setParametres(CGBilan_ParameterList.PARAM_BILAN_LABEL_COMPANYNAME, mandat.getLibelle());
                    this.parametres.put(CGBilan_ParameterList.PARAM_BILAN_LABEL_COMPANYNAME, mandat.getLibelle());
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

                // Ecriture de la ligne de titre
                if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, nodeValue.getNiveau())) {
                    if ((nodeValue.getTotalActif() != null) || (nodeValue.getTotalPassif() != null)) {
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

            // Ecriture de la ligne de TOTAUX
            if (hasMontant(nodeValue)) {

                if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, nodeValue.getNiveau())) {
                    if ((nodeValue.getTotalActif() != null) || (nodeValue.getTotalPassif() != null)) {
                        CGCompteSoldeBean bean = new CGCompteSoldeBean(nodeValue);
                        bean.resetMvt();
                        // HACK pour iReport afin de gérer correctement les groupes
                        bean.setNiveau(bean.getNiveau() + bean.getNoClasse());
                        bean.setNoClasseLibelle(getSession().getLabel("IMPRESSION_BALANCE_COMPTES_TOTAL") + " "
                                + bean.getNoClasseLibelle());
                        listBeanDocument.add(bean);
                    }
                }
            }

        } else if (hasMontant(nodeValue)) {
            if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, node.getValue().getNiveau())) {
                CGCompteSoldeBean bean = nodeValue;
                bean.resetTotaux();
                listBeanDocument.add(bean);
            }
        }
    }

    /**
     * @return
     * @throws Exception
     */
    private CGDefinitionListe retrieveDefinitionListe() throws Exception {
        CGDefinitionListe definitionListe = new CGDefinitionListe();
        definitionListe.setIdDefinitionListe(idDefinitionListe);
        definitionListe.setSession(getSession());
        definitionListe.retrieve();
        return definitionListe;
    }

    /**
     * @return
     * @throws Exception
     */
    private CGExerciceComptable retrieveExerciceComptable() throws Exception {
        CGExerciceComptable exerComptable = new CGExerciceComptable();
        exerComptable.setIdExerciceComptable(idExerciceComptable);
        exerComptable.setSession(getSession());
        exerComptable.retrieve();
        return exerComptable;
    }

    /**
     * @return
     * @throws Exception
     */
    private CGMandat retrieveMandat() throws Exception {
        CGMandat mandat = new CGMandat();
        mandat.setIdMandat(idMandat);
        mandat.setSession(getSession());
        mandat.retrieve();
        return mandat;
    }

    /**
     * @return
     * @throws Exception
     */
    private CGPeriodeComptable retrievePeriodeComptable() throws Exception {
        CGPeriodeComptable periodeComptable = new CGPeriodeComptable();
        periodeComptable.setIdPeriodeComptable(idPeriodeComptable);
        periodeComptable.setSession(getSession());
        periodeComptable.retrieve();
        return periodeComptable;
    }

    /**
     * Creation date: (02.07.2003 09:39:53)
     * 
     * @param newListeIdClasseCompte
     *            String
     */
    public void setClassesCompteList(String newListeIdClasseCompte) {
        classesCompteList = newListeIdClasseCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2003 16:18:43)
     * 
     * @param newIdCentreCharge
     *            String
     */
    public void setIdCentreCharge(String newIdCentreCharge) {
        idCentreCharge = newIdCentreCharge;
    }

    /**
     * Creation date: (01.07.2003 17:43:35)
     * 
     * @param newIdComptabilite
     *            String
     */
    public void setIdComptabilite(String newIdComptabilite) {
        idComptabilite = newIdComptabilite;
    }

    /**
     * Creation date: (02.07.2003 10:26:13)
     * 
     * @param newIdDefinitionListe
     *            String
     */
    public void setIdDefinitionListe(String newIdDefinitionListe) {
        idDefinitionListe = newIdDefinitionListe;
    }

    /**
     * Creation date: (02.07.2003 09:38:36)
     * 
     * @param newIdExerciceComptable
     *            String
     */
    public void setIdExerciceComptable(String newIdExerciceComptable) {
        idExerciceComptable = newIdExerciceComptable;
    }

    /**
     * Creation date: (02.07.2003 09:38:08)
     * 
     * @param newIdMandat
     *            String
     */
    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Creation date: (02.07.2003 08:42:24)
     * 
     * @param newIdPeriodeComptable
     *            String
     */
    public void setIdPeriodeComptable(String newIdPeriodeComptable) {
        idPeriodeComptable = newIdPeriodeComptable;
    }

    /**
     * Creation date: (07.07.2003 17:14:22)
     * 
     * @param newImprimerComptes
     *            Boolean
     */
    public void setImprimerComptes(Boolean newImprimerComptes) {
        imprimerComptes = newImprimerComptes;
    }

    /**
     * Creation date: (02.07.2003 11:44:45)
     * 
     * @param newImprimerPageGarde
     *            boolean
     */
    public void setImprimerPageGarde(Boolean newImprimerPageGarde) {
        imprimerPageGarde = newImprimerPageGarde;
    }

    /**
     * Creation date: (07.07.2003 17:24:10)
     * 
     * @param newImprimerTitres
     *            Boolean
     */
    public void setImprimerTitres(Boolean newImprimerTitres) {
        imprimerTitres = newImprimerTitres;
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
    private List<CGCompteSoldeBean> sortAndDisplayResult(Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot) {
        List<CGCompteSoldeBean> listBeanDocument = new ArrayList<CGCompteSoldeBean>();
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

        return listBeanDocument;
    }

}
