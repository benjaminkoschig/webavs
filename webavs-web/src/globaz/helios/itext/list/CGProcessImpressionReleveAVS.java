package globaz.helios.itext.list;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.export.FWIExportManager;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.printing.itext.types.FWIDocumentType;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.helios.application.CGApplication;
import globaz.helios.db.avs.CGExtendedCompteOfas;
import globaz.helios.db.avs.CGExtendedCompteOfasManager;
import globaz.helios.db.avs.CGExtendedContrePartieCpteAff;
import globaz.helios.db.avs.CGExtendedContrePartieCpteAffManager;
import globaz.helios.db.bouclement.CGBouclement;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.itext.list.releveAVS.CGIReleveAVS_Bean;
import globaz.helios.itext.list.releveAVS.CGReleveAVSBalance_Bean;
import globaz.helios.itext.list.releveAVS.CGReleveAVSBilan_Bean;
import globaz.helios.itext.list.releveAVS.CGReleveAVSCptAdmin_Bean;
import globaz.helios.itext.list.releveAVS.CGReleveAVSCptAffilie_Bean;
import globaz.helios.itext.list.releveAVS.CGReleveAVSCptExpl_Bean;
import globaz.helios.itext.list.releveAVS.CGReleveAVS_ParameterList;
import globaz.helios.itext.list.utils.CGImpressionUtils;
import globaz.helios.tools.CGXLSContructor;
import globaz.helios.tools.TimeHelper;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexandre Cuva
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CGProcessImpressionReleveAVS extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String NUMERO_REFERENCE_INFOROM = "0052GCF";

    public static final String TEMPLATE_COMPTE = "cg_compte";
    public static final String TEMPLATE_COMPTEEXP = "cg_compte_expl";
    private CGBouclement bouclement = null;
    private FWIExportManager exportManager = new FWIExportManager();
    private String idComptabilite = "";
    private java.lang.String idExerciceComptable;
    private java.lang.String idMandat;
    private java.lang.String idPeriodeComptable;
    private FWIImportManager importManager = new FWIImportManager();
    private Boolean isComptaDefinitive = new Boolean(true);
    private List listBeanBalance = new ArrayList();
    private List listBeanBilan = new ArrayList();
    private List listBeanCptAdmin = new ArrayList();
    private List listBeanCptAffilie = new ArrayList();
    private List listBeanCptExpl = new ArrayList();

    private List listBeanDocument = new ArrayList();
    private Map parametres = new HashMap();
    private String typeImpression = CGImpressionUtils.TYPE_IMPRESSION_PDF;

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     */
    public CGProcessImpressionReleveAVS() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     * 
     * @param parent
     */
    public CGProcessImpressionReleveAVS(BProcess parent) throws Exception {
        super(parent);
        init();
    }

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     * 
     * @param session
     */
    public CGProcessImpressionReleveAVS(BSession session) throws Exception {
        super(session);
        init();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        boolean result = false;

        BStatement statement = null;
        CGExtendedCompteOfasManager ds = null;
        try {
            // On prepare les parametres globaux
            prepareParameter();
            // On prepare les dataSources
            ds = new CGExtendedCompteOfasManager();
            ds.setSession(getSession());
            ds.setForIdMandat(getIdMandat());
            // Bilan
            buildBilan(ds);
            // Compte d'exploitation
            buildExploitation(ds);
            // Compte d'administration
            buildAdministration(ds);

            CGPeriodeComptable periode = new CGPeriodeComptable();
            periode.setSession(getSession());
            periode.setIdPeriodeComptable(getIdPeriodeComptable());
            periode.retrieve(getTransaction());
            if ((periode != null) && !periode.isNew() && periode.isAnnuel()) {
                // Balance
                buildBalance(ds);
            }

            // Comptabilite Affilie
            buildDocument();
            // Il y a des documents a imprimer
            if (result = (importManager.size() > 0)) {
                exportManager.addAll(importManager.getList());
                exportManager.exportReport();

                if (isAborted()) {
                    return false;
                }

                if (isTypeImpressionXls()) {
                    createFileExcel();
                } else {
                    JadePublishDocumentInfo documentInfo = createDocumentInfo();
                    documentInfo.setDocumentTypeNumber(CGProcessImpressionReleveAVS.NUMERO_REFERENCE_INFOROM);
                    this.registerAttachedDocument(documentInfo, exportManager.getExportNewFilePath());
                }
            } else {
                getMemoryLog().logMessage("GLOBAL_AUCUN_DOCUMENT_A_IMPRIMER", null, FWMessage.FATAL,
                        this.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (ds != null) {
                try {
                    ds.cursorClose(statement);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            statement = null;
        }
        return result;
    }

    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isBlank(idExerciceComptable)) {
            throw new Exception(getSession().getLabel("IMPRESSION_RELEVEAVS_ERR_1"));
        }
        if (JadeStringUtil.isBlank(idMandat)) {
            throw new Exception(getSession().getLabel("IMPRESSION_RELEVEAVS_ERR_2"));
        }
        if (JadeStringUtil.isBlank(idPeriodeComptable)) {
            throw new Exception(getSession().getLabel("IMPRESSION_RELEVEAVS_ERR_3"));
        }
        if (getParent() == null) {
            setSendCompletionMail(true);
            setControleTransaction(true);
        }

        CGPeriodeComptable periode = new CGPeriodeComptable();
        periode.setSession(getSession());
        periode.setIdPeriodeComptable(idPeriodeComptable);
        periode.retrieve(getTransaction());

        bouclement = new CGBouclement();
        bouclement.setSession(getSession());
        bouclement.setIdBouclement(periode.getIdBouclement());
        bouclement.retrieve(getTransaction());
        if (bouclement.isNew()) {
            throw new Exception(getSession().getLabel("IMPRESSION_RELEVEAVS_ERR_4"));
        }

        if (getSession().hasErrors()) {
            abort();
        }

        // if (!bouclement.isImprimerReleveAvs().booleanValue()) {
        // throw new
        // Exception("Impression du relevé AVS non authorisée pour ce type de bouclement");
        // }
    }

    protected void buildAdministration(CGExtendedCompteOfasManager ds) {
        BStatement statement = null;
        CGExtendedCompteOfas entity = null;
        try {
            ds.setTypeRapport(CGExtendedCompteOfasManager.RAPPORT_ADMINISTRATION);
            statement = ds.cursorOpen(getTransaction());
            while ((entity = (CGExtendedCompteOfas) ds.cursorReadNext(statement)) != null) {
                createRow(CGReleveAVSCptAdmin_Bean.class.getName(), entity, listBeanCptAdmin);
            }
            if (listBeanCptAdmin.size() == 0) {
                return;
            }
            importManager.clearParam();
            importManager.setDocumentName(getSession().getLabel("IMPRESSION_RELEVEAVS_ADMINISTRATION"));
            importManager.setParametre(parametres);
            importManager.setParametre(FWIImportParametre.getCol(1),
                    getSession().getLabel("IMPRESSION_RELEVEAVS_ADMINISTRATION"));
            importManager.setParametre(FWIImportParametre.getCol(2),
                    getSession().getLabel("IMPRESSION_RELEVEAVS_CHARGE"));
            importManager.setParametre(FWIImportParametre.getCol(3),
                    getSession().getLabel("IMPRESSION_RELEVEAVS_PRODUIT"));
            importManager.setDocumentTemplate(CGProcessImpressionReleveAVS.TEMPLATE_COMPTE);
            importManager.setBeanCollectionDataSource(listBeanCptAdmin);
            importManager.createDocument();
        } catch (FWIException e) {
            JadeLogger.fatal(this, e);
        } catch (Exception e) {
            JadeLogger.fatal(this, e);
        } finally {
            try {
                ds.cursorClose(statement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void buildBalance(CGExtendedCompteOfasManager ds) {
        BStatement statement = null;
        CGExtendedCompteOfas entity = null;
        try {
            ds.setTypeRapport(CGExtendedCompteOfasManager.RAPPORT_BALANCE_ANNUELLE);
            statement = ds.cursorOpen(getTransaction());
            while ((entity = (CGExtendedCompteOfas) ds.cursorReadNext(statement)) != null) {
                createRow(CGReleveAVSBalance_Bean.class.getName(), entity, listBeanBalance);
            }
            if (listBeanBalance.size() == 0) {
                return;
            }

            importManager.clearParam();
            importManager.setDocumentName(getSession().getLabel("IMPRESSION_RELEVEAVS_BALANCE_MOUVEMENTS"));
            importManager.setParametre(parametres);
            importManager.setParametre(FWIImportParametre.getCol(1),
                    getSession().getLabel("IMPRESSION_RELEVEAVS_BALANCE_MOUVEMENTS"));
            importManager
                    .setParametre(FWIImportParametre.getCol(2), getSession().getLabel("IMPRESSION_RELEVEAVS_DOIT"));
            importManager.setParametre(FWIImportParametre.getCol(3), getSession()
                    .getLabel("IMPRESSION_RELEVEAVS_AVOIR"));
            importManager.setDocumentTemplate(CGProcessImpressionReleveAVS.TEMPLATE_COMPTE);
            importManager.setBeanCollectionDataSource(listBeanBalance);
            importManager.createDocument();
        } catch (FWIException e) {
            JadeLogger.fatal(this, e);
        } catch (Exception e) {
            JadeLogger.fatal(this, e);
        } finally {
            try {
                ds.cursorClose(statement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void buildBilan(CGExtendedCompteOfasManager ds) {
        BStatement statement = null;
        CGExtendedCompteOfas entity = null;
        try {
            ds.setTypeRapport(CGExtendedCompteOfasManager.RAPPORT_BILAN);
            statement = ds.cursorOpen(getTransaction());
            while ((entity = (CGExtendedCompteOfas) ds.cursorReadNext(statement)) != null) {
                createRow(CGReleveAVSBilan_Bean.class.getName(), entity, listBeanBilan);
            }
            if (listBeanBilan.size() == 0) {
                return;
            }

            importManager.clearParam();
            importManager.setDocumentName(getSession().getLabel("IMPRESSION_RELEVEAVS_BILAN"));
            importManager.setParametre(parametres);
            importManager.setParametre(FWIImportParametre.getCol(1), getSession()
                    .getLabel("IMPRESSION_RELEVEAVS_BILAN"));
            importManager.setParametre(FWIImportParametre.getCol(2), getSession()
                    .getLabel("IMPRESSION_RELEVEAVS_ACTIF"));
            importManager.setParametre(FWIImportParametre.getCol(3),
                    getSession().getLabel("IMPRESSION_RELEVEAVS_PASSIF"));
            importManager.setDocumentTemplate(CGProcessImpressionReleveAVS.TEMPLATE_COMPTE);
            importManager.setBeanCollectionDataSource(listBeanBilan);
            importManager.createDocument();
        } catch (FWIException e) {
            JadeLogger.fatal(this, e);
        } catch (Exception e) {
            JadeLogger.fatal(this, e);
        } finally {
            try {
                ds.cursorClose(statement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void buildDocument() {
        CGExtendedContrePartieCpteAff entityAff = null;
        CGExtendedContrePartieCpteAffManager dsa = null;
        BStatement statement = null;
        try {
            dsa = new CGExtendedContrePartieCpteAffManager();
            dsa.setSession(getSession());
            dsa.changeManagerSize(BManager.SIZE_NOLIMIT);
            dsa.setForIdExerciceComptable(getIdExerciceComptable());
            dsa.setForIdMandat(getIdMandat());
            dsa.setForIdPeriodeComptable(getIdPeriodeComptable());
            dsa.setForIsProvisoire(new Boolean(!isComptaDefinitive().booleanValue()));
            dsa.setForIsActive(new Boolean(true));
            statement = dsa.cursorOpen(getTransaction());
            while ((entityAff = (CGExtendedContrePartieCpteAff) dsa.cursorReadNext(statement)) != null) {
                // Comptabilité affilié
                createRow(CGReleveAVSCptAffilie_Bean.class.getName(), entityAff, listBeanCptAffilie);
            }
            if (listBeanCptAffilie.size() == 0) {
                return;
            }

            importManager.clearParam();
            importManager.setDocumentName(getSession().getLabel("IMPRESSION_RELEVEAVS_COMPTA_AFFILIES"));
            importManager.setParametre(parametres);
            importManager.setParametre(FWIImportParametre.getCol(1),
                    getSession().getLabel("IMPRESSION_RELEVEAVS_COMPTA_AFFILIES"));
            importManager
                    .setParametre(FWIImportParametre.getCol(2), getSession().getLabel("IMPRESSION_RELEVEAVS_DOIT"));
            importManager.setParametre(FWIImportParametre.getCol(3), getSession()
                    .getLabel("IMPRESSION_RELEVEAVS_AVOIR"));
            importManager.setDocumentTemplate(CGProcessImpressionReleveAVS.TEMPLATE_COMPTE);
            importManager.setBeanCollectionDataSource(listBeanCptAffilie);
            importManager.createDocument();
        } catch (FWIException e) {
            JadeLogger.fatal(this, e);
        } catch (Exception e2) {
            JadeLogger.fatal(this, e2);
        } finally {
            try {
                dsa.cursorClose(statement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void buildExploitation(CGExtendedCompteOfasManager ds) {
        BStatement statement = null;
        CGExtendedCompteOfas entity = null;
        try {
            ds.setTypeRapport(CGExtendedCompteOfasManager.RAPPORT_EXPLOITATION);
            statement = ds.cursorOpen(getTransaction());
            while ((entity = (CGExtendedCompteOfas) ds.cursorReadNext(statement)) != null) {
                createRow(CGReleveAVSCptExpl_Bean.class.getName(), entity, listBeanCptExpl);
            }
            if (listBeanCptExpl.size() == 0) {
                return;
            }

            importManager.clearParam();
            importManager.setDocumentName(getSession().getLabel("IMPRESSION_RELEVEAVS_EXPLOITATION"));
            importManager.setParametre(parametres);
            importManager.setParametre(FWIImportParametre.getCol(1),
                    getSession().getLabel("IMPRESSION_RELEVEAVS_EXPLOITATION"));
            importManager.setParametre(FWIImportParametre.getCol(2),
                    getSession().getLabel("IMPRESSION_RELEVEAVS_DEPENSE"));
            importManager.setParametre(FWIImportParametre.getCol(3),
                    getSession().getLabel("IMPRESSION_RELEVEAVS_RECETTE"));
            importManager.setParametre(FWIImportParametre.getCol(4), getSession()
                    .getLabel("IMPRESSION_RELEVEAVS_CUMUL"));
            importManager.setDocumentTemplate(CGProcessImpressionReleveAVS.TEMPLATE_COMPTEEXP);
            importManager.setBeanCollectionDataSource(listBeanCptExpl);
            importManager.createDocument();
        } catch (FWIException e) {
            JadeLogger.fatal(this, e);
        } catch (Exception e) {
            JadeLogger.fatal(this, e);
        } finally {
            try {
                ds.cursorClose(statement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createFileExcel() throws Exception {

        CGXLSContructor xlsc = new CGXLSContructor();
        xlsc.setSession(getSession());
        listBeanDocument.addAll(listBeanBilan);
        xlsc.setDocumentFileName("ReleveAVS");
        xlsc.process(listBeanDocument, parametres);

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(CGApplication.DEFAULT_APPLICATION_HELIOS);
        docInfoExcel.setDocumentTitle(xlsc.getDocumentFileName());
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(CGProcessImpressionReleveAVS.NUMERO_REFERENCE_INFOROM);
        super.registerAttachedDocument(docInfoExcel, xlsc.getExportPath());

    }

    protected void createRow(String className, BEntity entity, List list) {
        try {
            CGIReleveAVS_Bean bean = (CGIReleveAVS_Bean) Class.forName(className).newInstance();
            bean.setIdExerciceComptable(getIdExerciceComptable());
            bean.setIdMandat(getIdMandat());
            bean.setIdPeriodeComptable(getIdPeriodeComptable());
            bean.setIsProvisoire(!isComptaDefinitive.booleanValue());
            if (bean.prepareValue(entity, getTransaction(), getSession())) {
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_RELEVEAVS_EMAIL_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_RELEVEAVS_EMAIL_OK");
        }
    }

    /**
     * Returns the idComptabilite.
     * 
     * @return String
     */
    public String getIdComptabilite() {
        return idComptabilite;
    }

    /**
     * Returns the idExerciceComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Insert the method's description here. Creation date: (03.07.2003 09:53:52)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdMandat() {
        return idMandat;
    }

    /**
     * Insert the method's description here. Creation date: (03.07.2003 09:53:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    private void init() throws FWIException {
        // import
        importManager.setImportPath(CGApplication.APPLICATION_HELIOS_REP);
        // export
        exportManager.setExportApplicationRoot(CGApplication.APPLICATION_HELIOS_REP);
        exportManager.setExportFileName("ReleveAVS");
        exportManager.setExportFileType(FWIDocumentType.PDF);
    }

    /**
     * Returns the isComptaDefinitive.
     * 
     * @return Boolean
     */
    public Boolean isComptaDefinitive() {
        return isComptaDefinitive;
    }

    private boolean isTypeImpressionXls() {
        return getTypeImpression().equals(CGImpressionUtils.TYPE_IMPRESSION_XLS);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {

        try {
            CGPeriodeComptable periode = new CGPeriodeComptable();
            periode.setSession(getSession());
            periode.setIdPeriodeComptable(getIdPeriodeComptable());
            periode.retrieve(getTransaction());
            if ((periode != null) && !periode.isNew() && periode.isAnnuel()) {
                return GlobazJobQueue.READ_LONG;
            } else {
                return GlobazJobQueue.READ_SHORT;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GlobazJobQueue.READ_SHORT;
        }
    }

    private void prepareParameter() {
        try {
            // Assignation des paramètres
            parametres.put(CGReleveAVS_ParameterList.PARAM_RELEVEAVS_MODELPATH, getSession().getApplication()
                    .getExternalModelPath() + "\\heliosRoot\\model\\");
            // + "\\model\\");

            parametres.put(CGReleveAVS_ParameterList.PARAM_RELEVEAVS_LABEL_TOTAUX,
                    getSession().getLabel("IMPRESSION_RELEVEAVS_TOTAUX"));

            parametres.put("P_DATE_TIME", TimeHelper.getCurrentTime());

            globaz.helios.db.comptes.CGPeriodeComptable periodeComptable = new globaz.helios.db.comptes.CGPeriodeComptable();
            periodeComptable.setIdPeriodeComptable(idPeriodeComptable);
            periodeComptable.setSession(getSession());
            periodeComptable.retrieve();

            // Description période comptable
            if (!JadeStringUtil.isIntegerEmpty(idPeriodeComptable)) {
                if (!periodeComptable.isNew()) {
                    // Date du bilan à la fin de l'exercice
                    parametres.put(CGReleveAVS_ParameterList.PARAM_RELEVEAVS_DATE,
                            periodeComptable.getFullDescription());

                }
            }
            // Précision titre
            if (periodeComptable.isAnnuel()) {
                parametres.put(
                        CGReleveAVS_ParameterList.PARAM_RELEVEAVS_LABEL_TITRE,
                        getSession().getLabel("IMPRESSION_RELEVEAVS_TITRE") + " "
                                + getSession().getLabel("IMPRESSION_RELEVEAVS_ANNUEL"));
            } else {
                parametres.put(
                        CGReleveAVS_ParameterList.PARAM_RELEVEAVS_LABEL_TITRE,
                        getSession().getLabel("IMPRESSION_RELEVEAVS_TITRE") + " "
                                + getSession().getLabel("IMPRESSION_RELEVEAVS_MENSUEL"));
            }
            // Description du mandat
            if (!JadeStringUtil.isIntegerEmpty(idMandat)) {
                globaz.helios.db.comptes.CGMandat mandat = new globaz.helios.db.comptes.CGMandat();
                mandat.setIdMandat(idMandat);
                mandat.setSession(getSession());
                mandat.retrieve();
                if (!mandat.isNew()) {
                    parametres.put(CGReleveAVS_ParameterList.PARAM_RELEVEAVS_LABEL_COMPANYNAME, mandat.getLibelle());
                }
            }
            parametres.put(CGReleveAVS_ParameterList.PARAM_RELEVEAVS_LABEL_TYPE_COMPTA,
                    CodeSystem.getLibelle(getSession(), getIdComptabilite()));
            if (periodeComptable.isMensuel()) {
                parametres.put(CGReleveAVS_ParameterList.PARAM_RELEVEAVS_PERIODE_MENSUELLE, new Boolean(true));
            } else {
                parametres.put(CGReleveAVS_ParameterList.PARAM_RELEVEAVS_PERIODE_MENSUELLE, new Boolean(false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the idComptabilite.
     * 
     * @param idComptabilite
     *            The idComptabilite to set
     */
    public void setIdComptabilite(String idComptabilite) {
        this.idComptabilite = idComptabilite;
        if (CodeSystem.CS_PROVISOIRE.equals(idComptabilite)) {
            setIsComptaDefinitive(new Boolean(false));
        } else {
            setIsComptaDefinitive(new Boolean(true));
        }

    }

    /**
     * Sets the idExerciceComptable.
     * 
     * @param idExerciceComptable
     *            The idExerciceComptable to set
     */
    public void setIdExerciceComptable(java.lang.String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

    /**
     * Insert the method's description here. Creation date: (03.07.2003 09:53:52)
     * 
     * @param newIdMandat
     *            java.lang.String
     */
    public void setIdMandat(java.lang.String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Insert the method's description here. Creation date: (03.07.2003 09:53:36)
     * 
     * @param newIdPeriodeComptable
     *            java.lang.String
     */
    public void setIdPeriodeComptable(java.lang.String newIdPeriodeComptable) {
        idPeriodeComptable = newIdPeriodeComptable;
    }

    /**
     * Sets the isComptaDefinitive.
     * 
     * @param isComptaDefinitive
     *            The isComptaDefinitive to set
     */
    public void setIsComptaDefinitive(Boolean isComptaDefinitive) {
        this.isComptaDefinitive = isComptaDefinitive;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

}
