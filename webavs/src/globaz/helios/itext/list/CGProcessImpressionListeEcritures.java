package globaz.helios.itext.list;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGExtendedEcriture;
import globaz.helios.db.comptes.CGExtendedEcritureManager;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.itext.list.listeecritures.CGListeEcrituresBean;
import globaz.helios.itext.list.listeecritures.CGListeEcritures_ParameterList;
import globaz.helios.itext.list.plancomptable.CGPlanComptable_ParameterList;
import globaz.helios.tools.TimeHelper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Insérez la description du type ici. Date de création : (26.03.2003 09:10:05)
 * 
 * @author: scr
 */
public class CGProcessImpressionListeEcritures extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String comptabilite;
    private Map container = new HashMap();
    private BProcess context = null;
    private String critereTri;
    private String idExerciceComptable;
    private String idMandat;
    private Boolean inclurePeriodePrecedente;
    private boolean isFirst = true;
    private boolean isUtiliserLivre = false;
    private String livre;
    private String periodeComptable;
    private String piece;
    private String selectionComptes;
    private BStatement statement = null;
    private BTransaction transaction = null;

    /**
     * Constructor for CGListeEcritures_Doc.
     */
    public CGProcessImpressionListeEcritures() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS), CGApplication.APPLICATION_HELIOS_REP,
                "ListEcriture");
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public CGProcessImpressionListeEcritures(BProcess parent, String arg1, String arg2) throws FWIException {
        super(parent, arg1, arg2);
        super.setDocumentTitle(parent.getSession().getLabel("IMPRESSION_LISTEECRITURE_TITRE"));
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public CGProcessImpressionListeEcritures(BSession session, String arg1, String arg2) throws FWIException {
        super(session, arg1, arg2);
        super.setDocumentTitle(session.getLabel("IMPRESSION_LISTEECRITURE_TITRE"));
        // TODO Auto-generated constructor stub
    }

    /**
     * Constructor for CGListeEcritures_Doc.
     * 
     * @param session
     */
    public CGProcessImpressionListeEcritures(globaz.globall.db.BSession session) throws Exception {
        this(session, CGApplication.APPLICATION_HELIOS_REP, "ListEcriture");
    }

    protected void _footers() {
    }

    protected void _header() {
        super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("IMPRESSION_LISTEECRITURES_TITRE"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL1,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL1"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL2,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL2"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL3,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL3"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL4,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL4"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL5,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL5"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL6,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL6"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL7,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL7"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL8,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL8"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL9,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL9"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL10,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL10"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL9Italique,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL9"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL10Italique,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL10"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL11,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL11"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__COL12,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_COL12"));
        super.setParametres(CGListeEcritures_ParameterList.PARAM__TOTAUX,
                getSession().getLabel("IMPRESSION_LISTEECRITURES_TOTAUX"));
        super.setParametres("P_DATE_TIME", TimeHelper.getCurrentTime());
        try {
            // Description exercice comptable
            globaz.helios.db.comptes.CGExerciceComptable exerComptable = new globaz.helios.db.comptes.CGExerciceComptable();
            exerComptable.setIdExerciceComptable(idExerciceComptable);
            exerComptable.setSession(getSession());
            exerComptable.retrieve();
            if (!exerComptable.isNew()) {
                super.setParametres(FWIImportParametre.PARAM_EXERCICE, exerComptable.getFullDescription());
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
        } catch (Exception e) {
            e.printStackTrace();
            JadeLogger.error(this, e);
        }
    }

    protected void _summary() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.03.2003 09:12:50)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isBlank(getIdExerciceComptable())) {
            throw new Exception(getSession().getLabel("IMPRESSION_LISTEECRITURE_ERR_1"));
        }
        if (JadeStringUtil.isBlank(getIdMandat())) {
            throw new Exception(getSession().getLabel("IMPRESSION_LISTEECRITURE_ERR_2"));
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
     * @see globaz.itext.print.FWIDocument#_beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
    }

    /**
     * @see globaz.itext.print.FWIDocument#_beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.setTemplateFile("cg_liste_ecritures");
    }

    /**
     * @see globaz.globall.api.BIDocument#bindData(String)
     */
    public void bindData(String arg0) throws Exception {
        setIdMandat(arg0);
        super.executeProcess();
    }

    /**
     * @see globaz.itext.print.FWIDocument#_createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        CGExtendedEcritureManager manager = new CGExtendedEcritureManager();
        CGExtendedEcriture entity = null;
        try {
            CGMandat mandat = new CGMandat();
            mandat.setSession(getSession());
            mandat.setIdMandat(getIdMandat());
            mandat.retrieve(getTransaction());
            if (mandat != null && !mandat.isNew()) {
                if (mandat.isUtiliseLivres().booleanValue()) {
                    isUtiliserLivre = true;
                }
            }
            manager.setSession(getSession());
            if (getIdMandat() != null && getIdMandat().trim().length() != 0) {
                manager.setForIdMandat(getIdMandat());
            }
            if (getIdExerciceComptable() != null && getIdExerciceComptable().trim().length() != 0) {
                manager.setForIdExerciceComptable(getIdExerciceComptable());
            }
            transaction = new globaz.globall.db.BTransaction(getSession());
            transaction.openTransaction();
            statement = manager.cursorOpen(transaction);
            // lit le nouveau entity
            while ((entity = (CGExtendedEcriture) manager.cursorReadNext(statement)) != null) {
                extractValues(entity);
            }
            // fin de la transaction on ferme le curseur
        } catch (Exception e) {
            e.printStackTrace();
            JadeLogger.error(this, e);
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
                ex.printStackTrace();
            } finally {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            }
        }
        JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(container.values());
        super.setDataSource(source);
        _header();
        _footers();
        _summary();
    }

    /**
     * Method extractValues.
     * 
     * @param entity
     * @throws Exception
     */
    private void extractValues(CGExtendedEcriture entity) throws Exception {
        try {
            CGListeEcrituresBean bean = (CGListeEcrituresBean) container.get(entity.getIdEcriture());
            if (bean == null) {
                bean = new CGListeEcrituresBean(entity, getSession().getIdLangueISO(), isUtiliserLivre);
            } else {
                bean.initValues(entity, getSession().getIdLangueISO(), isUtiliserLivre);
            }
            container.put(entity.getIdEcriture(), bean);
        } catch (Exception ex) {
            ex.printStackTrace();
            JadeLogger.error(this, ex);
        }
    }

    /**
     * Returns the comptabilite.
     * 
     * @return String
     */
    public String getComptabilite() {
        return comptabilite;
    }

    /**
     * Returns the context.
     * 
     * @return BProcess
     */
    public BProcess getContext() {
        return context;
    }

    /**
     * Returns the critereTri.
     * 
     * @return String
     */
    public String getCritereTri() {
        return critereTri;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_LISTEECRITURE_EMAIL_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_LISTEECRITURE_EMAIL_OK");
        }
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
     * @return java.lang.String
     */
    public java.lang.String getIdMandat() {
        return idMandat;
    }

    /**
     * Returns the livre.
     * 
     * @return String
     */
    public String getLivre() {
        return livre;
    }

    /**
     * Returns the periodeComptable.
     * 
     * @return String
     */
    public String getPeriodeComptable() {
        return periodeComptable;
    }

    /**
     * Returns the piece.
     * 
     * @return String
     */
    public String getPiece() {
        return piece;
    }

    /**
     * Returns the selectionComptes.
     * 
     * @return String
     */
    public String getSelectionComptes() {
        return selectionComptes;
    }

    /**
     * Returns the inclurePeriodePrecedente.
     * 
     * @return Boolean
     */
    public Boolean isInclurePeriodePrecedente() {
        return inclurePeriodePrecedente;
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
     * Sets the comptabilité.
     * 
     * @param comptabilité
     *            The comptabilité to set
     */
    public void setComptabilite(String comptabilite) {
        this.comptabilite = comptabilite;
    }

    /**
     * Sets the context.
     * 
     * @param context
     *            The context to set
     */
    public void setContext(BProcess context) {
        this.context = context;
    }

    /**
     * Sets the critereTri.
     * 
     * @param critereTri
     *            The critereTri to set
     */
    public void setCritereTri(String critereTri) {
        this.critereTri = critereTri;
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
    public void setIdMandat(java.lang.String idMandat) {
        this.idMandat = idMandat;
    }

    /**
     * Sets the inclurePeriodePrecedente.
     * 
     * @param inclurePeriodePrecedente
     *            The inclurePeriodePrecedente to set
     */
    public void setInclurePeriodePrecedente(Boolean inclurePeriodePrecedente) {
        this.inclurePeriodePrecedente = inclurePeriodePrecedente;
    }

    /**
     * Sets the livre.
     * 
     * @param livre
     *            The livre to set
     */
    public void setLivre(String livre) {
        this.livre = livre;
    }

    /**
     * Sets the periodeComptable.
     * 
     * @param periodeComptable
     *            The periodeComptable to set
     */
    public void setPeriodeComptable(String periodeComptable) {
        this.periodeComptable = periodeComptable;
    }

    /**
     * Sets the piece.
     * 
     * @param piece
     *            The piece to set
     */
    public void setPiece(String piece) {
        this.piece = piece;
    }

    /**
     * Sets the selectionComptes.
     * 
     * @param selectionComptes
     *            The selectionComptes to set
     */
    public void setSelectionComptes(String selectionComptes) {
        this.selectionComptes = selectionComptes;
    }
}
