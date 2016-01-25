package globaz.helios.itext.list;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.helios.application.CGApplication;
import globaz.helios.db.classifications.CGClasseCompteManager;
import globaz.helios.db.classifications.CGClasseCompteServices;
import globaz.helios.db.classifications.CGClassificationNode;
import globaz.helios.db.classifications.CGExtendedCompteManager;
import globaz.helios.itext.list.balance.comptes.CGCompteSoldeBean;
import globaz.helios.itext.list.plancomptable.CGPlanComptable_ParameterList;
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
public class CGProcessImpressionPlanComptable extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUMERO_REFERENCE_INFOROM = "0054GCF";
    private static final String PLAN_COMPTABLE = "PlanComptable";
    public static final String XLS_DOC_NAME = CGProcessImpressionPlanComptable.PLAN_COMPTABLE + "Multi";

    private String classesCompteList;
    private String idClassification = "";
    private String idDefinitionListe = "";
    private String idExerciceComptable;
    private String idMandat;
    private Boolean imprimerComptes = new Boolean(true);
    private Boolean imprimerPageGarde = new Boolean(true);
    private Boolean imprimerTitres = new Boolean(true);
    private boolean isFirst = true;
    private int niveauClassification = 0;
    private Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot = new HashMap<String, CGClassificationNode<CGCompteSoldeBean>>();

    /**
     * @throws Exception
     */
    public CGProcessImpressionPlanComptable() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public CGProcessImpressionPlanComptable(BProcess parent, String arg1, String arg2) throws FWIException {
        super(parent, arg1, arg2);
        super.setDocumentTitle(parent.getSession().getLabel("IMPRESSION_PLANCOMPTABLE_TITLE"));
    }

    /**
     * Constructor for CGPlanComptable_Doc.
     * 
     * @param session
     */
    public CGProcessImpressionPlanComptable(BSession session) throws FWIException {
        this(session, CGApplication.APPLICATION_HELIOS_REP, CGProcessImpressionPlanComptable.PLAN_COMPTABLE);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public CGProcessImpressionPlanComptable(BSession session, String arg1, String arg2) throws FWIException {
        super(session, arg1, arg2);
        super.setDocumentTitle(session.getLabel("IMPRESSION_PLANCOMPTABLE_TITLE"));
    }

    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isBlank(idExerciceComptable)) {
            throw new Exception(getSession().getLabel("IMPRESSION_PLANCOMPTABLE_ERR_1"));
        }
        if (JadeStringUtil.isBlank(classesCompteList)) {
            throw new Exception(getSession().getLabel("IMPRESSION_PLANCOMPTABLE_ERR_2"));
        }
        if (JadeStringUtil.isBlank(idMandat)) {
            throw new Exception(getSession().getLabel("IMPRESSION_PLANCOMPTABLE_ERR_3"));
        }
        if (JadeStringUtil.isBlank(idDefinitionListe)) {
            throw new Exception(getSession().getLabel("IMPRESSION_PLANCOMPTABLE_ERR_4"));
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
        getDocumentInfo().setDocumentTypeNumber(CGProcessImpressionPlanComptable.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * @see globaz.itext.print.FWIAbstractDocument#_beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            // Assignation des paramètres
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_DEFLISTE, getSession()
                    .getLabel("IMPRESSION_PLANCOMPTABLE_DEFLISTE"));
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_COL1,
                    getSession().getLabel("IMPRESSION_PLANCOMPTABLE_COL1"));
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_COL2,
                    getSession().getLabel("IMPRESSION_PLANCOMPTABLE_COL2"));
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_COL3,
                    getSession().getLabel("IMPRESSION_PLANCOMPTABLE_COL3"));
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_COL4,
                    getSession().getLabel("IMPRESSION_PLANCOMPTABLE_COL4"));
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_COL5,
                    getSession().getLabel("IMPRESSION_PLANCOMPTABLE_COL5"));
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_COL6,
                    getSession().getLabel("IMPRESSION_PLANCOMPTABLE_COL6"));
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_COL7,
                    getSession().getLabel("IMPRESSION_PLANCOMPTABLE_COL7"));
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_COL8,
                    getSession().getLabel("IMPRESSION_PLANCOMPTABLE_COL8"));
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_TOTAUX,
                    getSession().getLabel("IMPRESSION_PLANCOMPTABLE_TOTAUX"));
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_COMPTES,
                    getSession().getLabel("IMPRESSION_PLANCOMPTABLE_COMPTES"));
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_TITRE,
                    getSession().getLabel("IMPRESSION_PLANCOMPTABLE_TITRE"));

            super.setParametres("P_DATE_TIME", TimeHelper.getCurrentTime());
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            JadeLogger.error(this, e);
        }
    }

    /**
     * @see globaz.itext.print.FWIAbstractDocument#_beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        // Doit être placé avant loadClassification et findCompteSolde car l'on initialise idClassification
        prepareParameter();

        try {
            CGClasseCompteManager classeCompteManager = CGClasseCompteServices.findClasseCompte(getTransaction(),
                    idClassification);

            CGExtendedCompteManager manager = new CGExtendedCompteManager();
            manager.setSession(getTransaction().getSession());
            manager.changeManagerSize(BManager.SIZE_NOLIMIT);
            manager.setIdMandat(idMandat);
            manager.setIdExerciceComptable(idExerciceComptable);
            manager.find(getTransaction());

            /** Critère des classifications à afficher */
            List<String> classeListe = Arrays.asList(classesCompteList.trim().split(" "));

            CGClasseCompteServices.loadClassification(getSession().getIdLangueISO(), classeCompteManager, nodesDepot,
                    classeListe);

            CGClasseCompteServices.loadCompteLevel(manager, nodesDepot, getSession().getIdLangueISO(), false,
                    classeListe);

        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), e.getMessage());
        }
    }

    /**
     * @see globaz.itext.print.FWIAbstractDocument#_createDataSource()
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

        createExcel(listBeanDocument);
    }

    /**
     * @param ds
     * @return
     * @throws Exception
     */
    private boolean createExcel(List<CGCompteSoldeBean> listBeanDocument) throws Exception {
        CGXLSContructor xlsc = new CGXLSContructor();
        xlsc.setDocumentFileName(CGProcessImpressionPlanComptable.XLS_DOC_NAME);
        xlsc.setSession(getSession());

        beforeBuildReport();
        xlsc.process(listBeanDocument, super.getImporter().getParametre());

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(CGApplication.DEFAULT_APPLICATION_HELIOS);
        docInfoExcel.setDocumentTitle(xlsc.getDocumentFileName());
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(CGProcessImpressionPlanComptable.NUMERO_REFERENCE_INFOROM);
        super.registerAttachedDocument(docInfoExcel, xlsc.getExportPath());

        return true;
    }

    /**
     * Returns the classesCompteList.
     * 
     * @return String
     */
    public String getClassesCompteList() {
        return classesCompteList;
    }

    /**
     * Returns the idDefinitionListe.
     * 
     * @return String
     */
    public String getIdDefinitionListe() {
        return idDefinitionListe;
    }

    /**
     * Returns the idExerciceComptable.
     * 
     * @return String
     */
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Returns the idMandat.
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * @return the niveauClassification
     */
    public int getNiveauClassification() {
        return niveauClassification;
    }

    /**
     * Returns the imprimerComptes.
     * 
     * @return Boolean
     */
    public Boolean isImprimerComptes() {
        return imprimerComptes;
    }

    /**
     * Returns the imprimerPageGarde.
     * 
     * @return Boolean
     */
    public Boolean isImprimerPageGarde() {
        return imprimerPageGarde;
    }

    /**
     * Returns the imprimerTitres.
     * 
     * @return Boolean
     */
    public Boolean isImprimerTitres() {
        return imprimerTitres;
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
    private void prepareParameter() {
        try {
            // Description exercice comptable
            globaz.helios.db.comptes.CGExerciceComptable exerComptable = new globaz.helios.db.comptes.CGExerciceComptable();
            exerComptable.setIdExerciceComptable(idExerciceComptable);
            exerComptable.setSession(getSession());
            exerComptable.retrieve();
            if (!exerComptable.isNew()) {
                super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_EXERCICE,
                        exerComptable.getFullDescription());
                // Date du bilan à la fin de l'exercice
                super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_DATE, exerComptable.getDateFin());
            }

            // Description definition de liste
            globaz.helios.db.classifications.CGDefinitionListe definitionListe = new globaz.helios.db.classifications.CGDefinitionListe();
            definitionListe.setIdDefinitionListe(idDefinitionListe);
            definitionListe.setSession(getSession());
            definitionListe.retrieve();
            if (!definitionListe.isNew()) {
                idClassification = definitionListe.getIdClassification();
                super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_DEFLISTE,
                        definitionListe.getLibelle());
            }

            // Description du mandat
            if (!JadeStringUtil.isIntegerEmpty(idMandat)) {
                globaz.helios.db.comptes.CGMandat mandat = new globaz.helios.db.comptes.CGMandat();
                mandat.setIdMandat(idMandat);
                mandat.setSession(getSession());
                mandat.retrieve();
                if (!mandat.isNew()) {
                    super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_LABEL_COMPANYNAME,
                            mandat.getLibelle());
                }
            }

            // Impression détails page de garde
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_IMPRIMER_PAGE_GARDE,
                    imprimerPageGarde);
            // Impression titres
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_IMPRIMER_TITRES, imprimerTitres);
            // Impression comptes
            super.setParametres(CGPlanComptable_ParameterList.PARAM_PLANCOMPTABLE_IMPRIMER_COMPTES, imprimerComptes);
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
            if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, node.getValue().getNiveau())) {
                CGCompteSoldeBean bean = new CGCompteSoldeBean(nodeValue);
                bean.setNiveau(bean.getNiveau() + bean.getNoClasse());
                listBeanDocument.add(bean);
            }

            if (node.hasChildren()) {
                for (CGClassificationNode<CGCompteSoldeBean> child : node.getChildren().values()) {
                    recurseDisplayNode(child, listBeanDocument);
                }
            }
        } else {
            if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, node.getValue().getNiveau())) {
                CGCompteSoldeBean bean = nodeValue;
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
     * Sets the idDefinitionListe.
     * 
     * @param idDefinitionListe
     *            The idDefinitionListe to set
     */
    public void setIdDefinitionListe(String idDefinitionListe) {
        this.idDefinitionListe = idDefinitionListe;
    }

    /**
     * Sets the idExerciceComptable.
     * 
     * @param idExerciceComptable
     *            The idExerciceComptable to set
     */
    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

    /**
     * Sets the idMandat.
     * 
     * @param idMandat
     *            The idMandat to set
     */
    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    /**
     * Sets the imprimerComptes.
     * 
     * @param imprimerComptes
     *            The imprimerComptes to set
     */
    public void setImprimerComptes(Boolean imprimerComptes) {
        this.imprimerComptes = imprimerComptes;
    }

    /**
     * Sets the imprimerPageGarde.
     * 
     * @param imprimerPageGarde
     *            The imprimerPageGarde to set
     */
    public void setImprimerPageGarde(Boolean imprimerPageGarde) {
        this.imprimerPageGarde = imprimerPageGarde;
    }

    /**
     * Sets the imprimerTitres.
     * 
     * @param imprimerTitres
     *            The imprimerTitres to set
     */
    public void setImprimerTitres(Boolean imprimerTitres) {
        this.imprimerTitres = imprimerTitres;
    }

    /**
     * @param niveauClassification
     *            the niveauClassification to set
     */
    public void setNiveauClassification(int niveauClassification) {
        this.niveauClassification = niveauClassification;
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
