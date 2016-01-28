package globaz.helios.itext.list;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.export.FWIExportManager;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.types.FWIDocumentType;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExtendedGrandLivre;
import globaz.helios.db.comptes.CGExtendedGrandLivreManager;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.itext.list.grand.livre.CGGrandLivre_Bean;
import globaz.helios.itext.list.grand.livre.CGGrandLivre_ParameterList;
import globaz.helios.tools.TimeHelper;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Sylvain Crelier
 */
public class CGProcessImpressionGrandLivre extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String NUMERO_REFERENCE_INFOROM = "0055GCF";

    public static final String SEARCH_PAR_DATE = "SEARCH_PAR_DATE";
    public static final String SEARCH_TOUT_EXERCICE = "SEARCH_TOUT_EXERCICE";
    public static final String TEMPLATE_DOC = "cg_grand_livre";

    private String compteA;
    private String compteDe;

    private FWIExportManager exportManager = new FWIExportManager();
    private String fromPeriodeDate;

    private String idComptabilite;
    private String idExerciceComptable;

    private String idMandat;
    private FWIImportManager importManager = new FWIImportManager();

    private List listBeanDocument = new ArrayList();

    private Map parametres = new HashMap();
    private String searchPeriode = CGProcessImpressionGrandLivre.SEARCH_TOUT_EXERCICE;

    private String untilPeriodeDate;

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     */
    public CGProcessImpressionGrandLivre() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     * 
     * @param parent
     */
    public CGProcessImpressionGrandLivre(BProcess parent) throws Exception {
        super(parent);
        init();
    }

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     * 
     * @param session
     */
    public CGProcessImpressionGrandLivre(BSession session) throws Exception {
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
        boolean status = true;
        try {
            // On prepare les parametres globaux
            prepareParameter();

            if (isAborted()) {
                return false;
            }

            // Create datasource
            buildDocument();

            if (isAborted()) {
                return false;
            }

            // CGXLSContructor xlsc = new CGXLSContructor();
            // try {
            // xlsc.setDocumentFileName("GrandLivre");
            // xlsc.setSession(this.getSession());
            // xlsc.process(this.listBeanDocument, this.parametres);
            // JadePublishDocumentInfo docInfoExcel = this.createDocumentInfo();
            // docInfoExcel.setApplicationDomain(CGApplication.DEFAULT_APPLICATION_HELIOS);
            // docInfoExcel.setDocumentTitle(xlsc.getDocumentFileName());
            // docInfoExcel.setPublishDocument(true);
            // docInfoExcel.setArchiveDocument(false);
            // docInfoExcel.setDocumentTypeNumber(CGProcessImpressionGrandLivre.NUMERO_REFERENCE_INFOROM);
            // super.registerAttachedDocument(docInfoExcel, xlsc.getExportPath());
            // } catch (Exception e) {
            // e.printStackTrace();
            // } finally {
            // System.out.println("XLS Grand Livre contruction processed");
            // }

            // Il y a des documents a imprimer
            if ((status = (importManager.size() > 0))) {
                exportManager.addAll(importManager.getList());
                exportManager.exportReport();
                if (isAborted()) {
                    return false;
                }

                JadePublishDocumentInfo documentInfo = createDocumentInfo();
                documentInfo.setDocumentTypeNumber(CGProcessImpressionGrandLivre.NUMERO_REFERENCE_INFOROM);
                this.registerAttachedDocument(documentInfo, exportManager.getExportNewFilePath());
            } else {
                getMemoryLog().logMessage("GLOBAL_AUCUN_DOCUMENT_A_IMPRIMER", null, FWMessage.FATAL,
                        this.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isBlank(idExerciceComptable)) {
            throw new Exception(getSession().getLabel("IMPRESSION_GRANDLIVRE_ERROR_1"));
        }
        if (JadeStringUtil.isBlank(idMandat)) {
            throw new Exception(getSession().getLabel("IMPRESSION_GRANDLIVRE_ERROR_2"));
        }
        if (getParent() == null) {
            setSendCompletionMail(true);
            setControleTransaction(true);
        }

        if (getSession().hasErrors()) {
            abort();
        }

    }

    protected void buildDocument() {

        CGExtendedGrandLivre entity = null;
        CGExtendedGrandLivreManager ds = null;

        BStatement statement = null;
        try {
            // Paramétrage du datasource
            ds = new CGExtendedGrandLivreManager();
            ds.setSession(getSession());
            ds.setForIsActive(new Boolean(true));
            ds.changeManagerSize(BManager.SIZE_NOLIMIT);
            ds.setForIdExerciceComptable(getIdExerciceComptable());

            // Si provisoire, on prends tout : provisoire + definitif --> on
            // sette le flag à null
            if (CodeSystem.CS_DEFINITIF.equals(getIdComptabilite())) {
                ds.setForIsProvisoire(new Boolean(false));
            } else {
                ds.setForIsProvisoire(null);
            }

            ds.setForNumeroCompteMax(getCompteA());
            ds.setForNumeroCompteMin(getCompteDe());

            String idPeriodeComptableSoldeANouveau;
            String lastIdPeriodeComptable;
            List listId = new ArrayList();
            if (getSearchPeriode().equals(CGProcessImpressionGrandLivre.SEARCH_TOUT_EXERCICE)) {
                idPeriodeComptableSoldeANouveau = CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE;
                lastIdPeriodeComptable = CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE;
            } else {
                JADate from = new JADate(getFromPeriodeDate());
                from.setDay(1);

                JACalendarGregorian calendar = new JACalendarGregorian();
                JADate until = new JADate(getUntilPeriodeDate());
                until.setDay(calendar.daysInMonth(until.getMonth(), until.getYear()));

                CGPeriodeComptableManager manager = getPeriodeManager(from, until);

                idPeriodeComptableSoldeANouveau = ((CGPeriodeComptable) manager.get(manager.size() - 1))
                        .getIdPeriodeComptable();

                listId = CGSolde.getListIdPreviousPeriodeComptables(getSession(), getIdExerciceComptable(),
                        ((CGPeriodeComptable) manager.getFirstEntity()), from.toString());

                String periodes = "";
                if (listId != null) {
                    Iterator iter = listId.iterator();
                    while (iter.hasNext() && !isAborted()) {
                        periodes += (String) iter.next() + ",";
                    }
                }

                periodes += ((CGPeriodeComptable) manager.getFirstEntity()).getIdPeriodeComptable();
                lastIdPeriodeComptable = ((CGPeriodeComptable) manager.getFirstEntity()).getIdPeriodeComptable();
                ds.setForIdListePeriodeComptable(periodes);
            }

            statement = ds.cursorOpen(getTransaction());
            setProgressScaleValue(ds.getCount());
            while ((entity = (CGExtendedGrandLivre) ds.cursorReadNext(statement)) != null) {
                // Gestion de l'abort
                if (isAborted()) {
                    return;
                }
                createRow(CGGrandLivre_Bean.class.getName(), entity, idPeriodeComptableSoldeANouveau, listId,
                        listBeanDocument);
                incProgressCounter();
            }

            if (listBeanDocument.size() == 0) {
                return;
            }

            preparePeriodeParameters(idPeriodeComptableSoldeANouveau, lastIdPeriodeComptable);

            importManager.clearParam();
            importManager.setDocumentName("GrandLivre");
            importManager.setParametre(parametres);
            importManager.setDocumentTemplate(CGProcessImpressionGrandLivre.TEMPLATE_DOC);
            importManager.setBeanCollectionDataSource(listBeanDocument);
            importManager.createDocument();
        } catch (FWIException e) {
            JadeLogger.fatal(this, e);
        } catch (Exception e2) {
            JadeLogger.fatal(this, e2);
        } finally {
            try {
                ds.cursorClose(statement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void createRow(String className, BEntity entity, String idPeriodeComptableForSoldeANouveau,
            List listPreviousPeriode, List list) {
        try {
            CGGrandLivre_Bean bean = (CGGrandLivre_Bean) Class.forName(className).newInstance();
            bean.setIdPeriodeForSoldeANouveau(idPeriodeComptableForSoldeANouveau);

            if (bean.prepareValue(entity, getTransaction(), getSession())) {
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the compteA.
     * 
     * @return String
     */
    public String getCompteA() {
        return compteA;
    }

    /**
     * Returns the compteDe.
     * 
     * @return String
     */
    public String getCompteDe() {
        return compteDe;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_GRANDLIVRE_EMAIL_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_GRANDLIVRE_EMAIL_OK");
        }
    }

    private CGExerciceComptable getExerciceComptable() throws Exception {
        CGExerciceComptable exerComptable = new CGExerciceComptable();
        exerComptable.setIdExerciceComptable(getIdExerciceComptable());
        exerComptable.setSession(getSession());
        exerComptable.retrieve(getTransaction());
        return exerComptable;
    }

    public String getFromPeriodeDate() {
        return fromPeriodeDate;
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
     * @return String
     */
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Insert the method's description here. Creation date: (03.07.2003 09:53:52)
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    private CGPeriodeComptable getPeriode(String idPeriode) throws Exception {
        CGPeriodeComptable periode = new CGPeriodeComptable();
        periode.setSession(getSession());
        periode.setIdPeriodeComptable(idPeriode);
        periode.retrieve(getTransaction());
        return periode;
    }

    /**
     * Retrouve les périodes pour les dates sélectionnées.
     * 
     * @param from
     * @param until
     * @return
     * @throws Exception
     */
    private CGPeriodeComptableManager getPeriodeManager(JADate from, JADate until) throws Exception {
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());

        manager.setForIdExerciceComptable(getIdExerciceComptable());

        manager.setFromDateDebut(from.toString());
        manager.setUntilDateFin(until.toString());

        manager.setOrderBy(CGPeriodeComptableManager.TRI_DATE_FIN_AND_TYPE_DESC);

        manager.find(getTransaction());

        if (manager.isEmpty()) {
            throw new Exception(getSession().getLabel("PERIODE_COMPTABLE_NO_PERIODE_MATCH"));
        }
        return manager;
    }

    public String getSearchPeriode() {
        return searchPeriode;
    }

    public String getUntilPeriodeDate() {
        return untilPeriodeDate;
    }

    private void init() throws FWIException {
        // import
        importManager.setImportPath(CGApplication.APPLICATION_HELIOS_REP);
        // export
        exportManager.setExportApplicationRoot(CGApplication.APPLICATION_HELIOS_REP);
        exportManager.setExportFileName("GrandLivre");
        exportManager.setExportFileType(FWIDocumentType.PDF);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private void prepareParameter() throws Exception {
        // Assignation des paramètres
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_COL1,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_COL_DATE"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_COL2,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_NUMERO"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_COL3,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_CONTRE_ECRITURE"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_COL4,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_LIBELLE_ECRITURE"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_COL5,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_PIECE"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_COL7,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_P"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_COL8,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_MONNAIE_ETRANGERE"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_COL9,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_COURS"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_COL10,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_DEBIT"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_COL11,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_CREDIT"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_COL12,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_SOLDE"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_TITRE,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_TITRE"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_COMPTABILITE,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_COMPTABILITE"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_PERIODE,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_PERIODE"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_DATE,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_DATE"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_SELECTION,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_SELECTION"));
        parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_LABEL_TOTAL,
                getSession().getLabel("IMPRESSION_GRANDLIVRE_TOTAL"));

        parametres.put("P_DATE_TIME", TimeHelper.getCurrentTime());
        // Description comptabilité

        if ((idComptabilite == null) || "".equals(idComptabilite)) {
            parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_COMPTABILITE, "");
        } else {
            parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_COMPTABILITE,
                    globaz.helios.translation.CodeSystem.getLibelle(getSession(), idComptabilite));
        }

        // Description exercice comptable
        CGExerciceComptable exerComptable = getExerciceComptable();
        if (!exerComptable.isNew()) {
            parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_EXERCICE, exerComptable.getFullDescription());
            // Date du bilan à la fin de l'exercice
            parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_DATE, exerComptable.getDateDebut() + " - "
                    + exerComptable.getDateFin());
        }

        // Description du mandat
        if (!JadeStringUtil.isIntegerEmpty(idMandat)) {
            CGMandat mandat = new CGMandat();
            mandat.setIdMandat(idMandat);
            mandat.setSession(getSession());
            mandat.retrieve(getTransaction());
            if (!mandat.isNew()) {
                parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_COMPANYNAME, mandat.getLibelle());
            }
        }

    }

    private void preparePeriodeParameters(String idPeriodeBegin, String idPeriodeLast) throws Exception {
        if (!CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE.equals(idPeriodeBegin)) {
            CGPeriodeComptable periodeBegin = getPeriode(idPeriodeBegin);
            CGPeriodeComptable periodeLast = getPeriode(idPeriodeLast);

            parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_PERIODE, periodeBegin.getFullDescription()
                    + " - " + periodeLast.getFullDescription());
            // Date du bilan à la fin de la période
            parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_DATE, periodeBegin.getDateDebut() + " - "
                    + periodeLast.getDateFin());
        } else if (!getExerciceComptable().isNew()) {
            parametres.put(CGGrandLivre_ParameterList.PARAM_GRANDLIVRE_PERIODE, getExerciceComptable()
                    .getFullDescription());
        }
    }

    /**
     * Sets the compteA.
     * 
     * @param compteA
     *            The compteA to set
     */
    public void setCompteA(String compteA) {
        this.compteA = compteA;
    }

    /**
     * Sets the compteDe.
     * 
     * @param compteDe
     *            The compteDe to set
     */
    public void setCompteDe(String compteDe) {
        this.compteDe = compteDe;
    }

    public void setFromPeriodeDate(String fromPeriodeDate) {
        this.fromPeriodeDate = fromPeriodeDate;
    }

    /**
     * Sets the idComptabilite.
     * 
     * @param idComptabilite
     *            The idComptabilite to set
     */
    public void setIdComptabilite(String idComptabilite) {
        this.idComptabilite = idComptabilite;
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
     * Insert the method's description here. Creation date: (03.07.2003 09:53:52)
     * 
     * @param newIdMandat
     *            String
     */
    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

    public void setSearchPeriode(String searchPeriode) {
        this.searchPeriode = searchPeriode;
    }

    public void setUntilPeriodeDate(String untilPeriodeDate) {
        this.untilPeriodeDate = untilPeriodeDate;
    }
}
