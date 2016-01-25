package globaz.pavo.print.itext;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.splitting.CIAnnonceSplitting;
import globaz.pyxis.api.ITIAdministration;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CI_Spliting extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseAssure = "";
    private String adresseExConjoint = "";
    // private String m_commentaire = "";
    private CIAnnonceSplitting annonce = null;

    private boolean check = false;

    private String idAnnonceAssure = "";

    private String idAnnonceConjoint = "";

    private Iterator<CIAnnonceSplitting> itAnnonces = null;

    private String langueISO = "";
    private String langueISOConjoint = "";

    private List<CIAnnonceSplitting> listAnnoncesSplitting = new ArrayList<CIAnnonceSplitting>();

    private BProcess m_context = null;

    private String m_entete = "";

    private String numeroAVSAssure = "";

    private String numeroAVSExConjoint = "";

    // private List listRefUnique = new ArrayList();
    // private Iterator itRefUni = null;
    private ByteArrayOutputStream out = null;

    public CI_Spliting() throws Exception {
        this(new BSession(CIApplication.DEFAULT_APPLICATION_PAVO));

    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public CI_Spliting(BProcess arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle(getSession().getLabel("MSG_SLIT_CI"));
    }

    public CI_Spliting(BSession session) throws FWIException {
        this(session, CIApplication.APPLICATION_PAVO_REP, session.getLabel("MSG_SLIT_CI"));
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public CI_Spliting(BSession arg0, String arg1, String arg2) throws FWIException {
        super(arg0, arg1, arg2);
        super.setFileTitle(getSession().getLabel("MSG_SLIT_CI"));
    }

    protected void _footers() {
        super.setParametres(CIItextParam.getFooter(1), getSession().getLabel("PARAM_SPL_REPORT"));
        super.setParametres(CIItextParam.getFooter(2), getSession().getLabel("PARAM_SPL_TOTAL"));
    }

    protected ITIAdministration _getCaisseCompensation() throws Exception {

        CIApplication applic = (CIApplication) getSession().getApplication();
        ITIAdministration caisse = applic.getAdministrationLocale(getSession());

        return caisse;
    }

    /**
     * Sets the transaction.
     * 
     * @param transaction
     *            The transaction to set
     */
    // public void setTransaction(BTransaction transaction) {
    // this.transaction = transaction;
    // }

    protected java.lang.String _getDateLieux() {
        String dateLieux = "";
        try {
            // todo getLocaliteCourt de PYXIS
            /*
             * dateLieux = _getCaisseCompensation().getLocalite(); if(dateLieux == null || "null".equals(dateLieux)) {
             * dateLieux = "n/a"; }
             */
            dateLieux = JACalendar.format(JACalendar.todayJJsMMsAAAA(), getLangueISO());
        } catch (Exception e) {
            dateLieux = "n/a";
        }

        return dateLieux;
    }

    protected void _header() {
        super.setParametres(CIItextParam.getHeader(1), getSession().getLabel("ENTETE_CAISSE"));
        super.setParametres(CIItextParam.getHeader(8), getSession().getLabel("ADRESSE_CAISSE"));
        super.setParametres(CIItextParam.getHeader(2), getEntete());
        super.setParametres(CIItextParam.getHeader(3), getSession().getLabel("PARAM_SPL_TITLE"));
        super.setParametres(CIItextParam.getHeader(4), getSession().getLabel("PARAM_SPL_AVS"));
        super.setParametres(CIItextParam.getHeader(5), getSession().getLabel("PARAM_SPL_NAME"));
        super.setParametres(CIItextParam.getHeader(6), getSession().getLabel("PARAM_SPL_REPORT"));
        super.setParametres(CIItextParam.getHeader(7), getSession().getLabel("PARAM_SPL_PAGE"));
        super.setParametres(CIItextParam.getHeaderLabel(4), getAvs());
        super.setParametres(CIItextParam.getHeaderLabel(5), getNomPrenom());

        super.setParametres("P_" + FWIImportParametre.getCol(1), getSession().getLabel("PARAM_SPL_YEAR"));
        super.setParametres("P_" + FWIImportParametre.getCol(2), getSession().getLabel("PARAM_SPL_REVENU"));
        // super.setParametres("P_" + CIItextParam.getCol(3),
        // getSession().getLabel("PARAM_SPL_COMM"));
        super.setParametres("P_" + FWIImportParametre.getCol(3), getSession().getLabel("PARAM_SPL_MNT_DONNE"));
        super.setParametres("P_" + FWIImportParametre.getCol(4), getSession().getLabel("PARAM_SPL_MNT_RECU"));
        super.setParametres("P_" + FWIImportParametre.getCol(5), getSession().getLabel("PARAM_SPL_MNT_PART"));
    }

    protected void _summary() {
        super.setParametres(CIItextParam.getSummary(1), "");
        super.setParametres(CIItextParam.getSummary(2), _getDateLieux());
    }

    /**
     * @see globaz.itext.print.FWIDocument#_beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        setDocumentTitle(annonce.getAvs() + " " + annonce.getNom());
    }

    /**
     * @see globaz.itext.print.FWIDocument#_beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {

        super.setTemplateFile("PAVO_CI_SPLITING");
        // itRefUni = listRefUnique.iterator();
        CIAnnonceSplitting annonceAssu = null;
        CIAnnonceSplitting annonceConj = null;
        try {
            if (!JadeStringUtil.isIntegerEmpty(idAnnonceAssure)) {
                annonceAssu = new CIAnnonceSplitting(getTransaction(), idAnnonceAssure, true);
                listAnnoncesSplitting.add(annonceAssu);
            }
            if (!JadeStringUtil.isIntegerEmpty(idAnnonceConjoint)) {
                annonceConj = new CIAnnonceSplitting(getTransaction(), idAnnonceConjoint, true);
                listAnnoncesSplitting.add(annonceConj);
            }
        } catch (Exception e) {
        }
        if (check && (annonceAssu != null) && (annonceConj != null)) {
            annonceAssu.checkSplitting(annonceConj);
        }

        itAnnonces = listAnnoncesSplitting.iterator();
    }

    /**
     * @see globaz.itext.print.FWIDocument#_createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        // HEOutputAnnonceListViewBean manager = new
        // HEOutputAnnonceListViewBean();
        // HEOutputAnnonceViewBean entity;
        // annonce = new
        // CIAnnonceSplitting(getTransaction(),this.getReferenceUnique());

        // BTransaction transaction = new
        // globaz.globall.db.BTransaction(getSession());
        // transaction.openTransaction();
        getDocumentInfo().setDocumentTypeNumber("0060CCI");
        Hashtable<?, ?> container = annonce.getCumuls();
        // if (transaction != null)
        // transaction.closeTransaction();
        JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(
                (new TreeMap<Object, Object>(container)).values());
        super.setDataSource(source);

        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);

        String theAdresse = "";
        if (annonce != null) {
            // on set les adresses (assuré / ex-conjoint) pour les aperçus de splitting selon les adresses saisies dans
            // le dossier de splitting
            String theUnformattedNumeroAVSAssure = NSUtil.unFormatAVS(getNumeroAVSAssure());
            String theUnformattedNumeroAVSExConjoint = NSUtil.unFormatAVS(getNumeroAVSExConjoint());
            String theUnformattedNumeroAVSAnnonce = NSUtil.unFormatAVS(annonce.getAvs());

            if (!JadeStringUtil.isBlankOrZero(theUnformattedNumeroAVSAnnonce)) {
                if (!JadeStringUtil.isBlankOrZero(theUnformattedNumeroAVSAssure)
                        && theUnformattedNumeroAVSAnnonce.equalsIgnoreCase(theUnformattedNumeroAVSAssure)) {
                    theAdresse = getAdresseAssure();
                } else if (!JadeStringUtil.isBlankOrZero(theUnformattedNumeroAVSExConjoint)
                        && theUnformattedNumeroAVSAnnonce.equalsIgnoreCase(theUnformattedNumeroAVSExConjoint)) {

                    theAdresse = getAdresseExConjoint();
                    setLangueISO(getLangueISOConjoint());
                }
            }
        }

        // TODO : Header Caisse
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), getLangueISO());
        //
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        headerBean.setNoAffilie(" ");
        headerBean.setNoAvs(" ");
        headerBean.setAdresse(theAdresse);
        headerBean.setEmailCollaborateur(" ");
        headerBean.setConfidentiel(true);

        headerBean.setDate(" ");

        headerBean.setNomCollaborateur(" ");
        caisseReportHelper.addHeaderParameters(this, headerBean);
        // local path
        super.setParametres("P_LOCAL_PATH", application.getExternalModelPath());
        _header();
        _footers();
        _summary();

        // caisseReportHelper.addHeaderParameters(this, headerBean);
    }

    public String getAdresseAssure() {
        return adresseAssure;
    }

    public String getAdresseExConjoint() {
        return adresseExConjoint;
    }

    /**
     * Returns the m_referenceUnique.
     * 
     * @return String
     */
    /*
     * public String getReferenceUnique() { return m_referenceUnique; }
     */
    public CIAnnonceSplitting getAnnonceSplitting() {
        return annonce;
    }

    /**
     * Returns the m_avs.
     * 
     * @return String
     */
    public String getAvs() {
        return annonce != null ? annonce.getAvs() : "";
    }

    /**
     * Returns the m_context.
     * 
     * @return BProcess
     */
    public BProcess getContext() {
        return m_context;
    };

    /**
     * Returns the m_entete.
     * 
     * @return String
     */
    public String getEntete() {
        return m_entete;
    }

    /**
     * @return
     */
    public String getIdAnnonceAssure() {
        return idAnnonceAssure;
    }

    /**
     * @return
     */
    public String getIdAnnonceConjoint() {
        return idAnnonceConjoint;
    }

    public String getLangueISO() {
        return langueISO;
    }

    public String getLangueISOConjoint() {
        return langueISOConjoint;
    }

    /**
     * Returns the m_nomPrenom.
     * 
     * @return String
     */
    public String getNomPrenom() {
        return annonce != null ? annonce.getNom() : "";
    }

    public String getNumeroAVSAssure() {
        return numeroAVSAssure;
    }

    /**
     * Sets the m_referenceUnique.
     * 
     * @param m_referenceUnique
     *            The m_referenceUnique to set
     */
    /*
     * public void setReferenceUnique(String referenceUnique) { this.m_referenceUnique = referenceUnique;
     * this.listRefUnique.add(referenceUnique); }
     */

    /*
     * public void addListReferenceUnique(List list) { this.listRefUnique = list; }
     */

    public String getNumeroAVSExConjoint() {
        return numeroAVSExConjoint;
    }

    /**
     * @return
     */
    public ByteArrayOutputStream getOut() {
        return out;
    }

    /**
     * @return
     */
    public boolean isCheck() {
        return check;
    }

    /**
     * Returns the m_commentaire.
     * 
     * @return String
     */
    /*
     * public String getCommentaire() { return m_commentaire; }
     */

    /**
     * Sets the m_commentaire.
     * 
     * @param m_commentaire
     *            The m_commentaire to set
     */
    /*
     * public void setCommentaire(String commentaire) { this.m_commentaire = commentaire; }
     */

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Sets the m_nomPrenom.
     * 
     * @param m_nomPrenom
     *            The m_nomPrenom to set
     */
    // public void setNomPrenom(String nomPrenom) {
    // this.m_nomPrenom = nomPrenom;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        // boolean hasNext = itRefUni.hasNext();
        boolean hasNext = itAnnonces.hasNext();
        if (hasNext) {
            // this.m_referenceUnique = (String) itRefUni.next();
            try {
                annonce = itAnnonces.next();
            } catch (Exception e) {
            }
        }
        return hasNext;
    }

    /**
     * Sets the m_avs.
     * 
     * @param m_avs
     *            The m_avs to set
     */
    // public void setAvs(String avs) {
    // this.m_avs = avs;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#returnDocument()
     */
    @Override
    public void returnDocument() throws FWIException {
        if (out == null) {
            super.returnDocument();
        } else {
            super.imprimerListDocument(out);
        }

    }

    public void setAdresseAssure(String adresseAssure) {
        this.adresseAssure = adresseAssure;
    }

    public void setAdresseExConjoint(String adresseExConjoint) {
        this.adresseExConjoint = adresseExConjoint;
    }

    /**
     * @param b
     */
    public void setCheck(boolean b) {
        check = b;
    }

    /**
     * Sets the m_context.
     * 
     * @param m_context
     *            The m_context to set
     */
    public void setContext(BProcess context) {
        m_context = context;
    }

    /**
     * Sets the m_entete.
     * 
     * @param m_entete
     *            The m_entete to set
     */
    public void setEntete(String entete) {
        m_entete = entete;
    }

    /**
     * @param string
     */
    public void setIdAnnonceAssure(String string) {
        idAnnonceAssure = string;
    }

    /**
     * @param string
     */
    public void setIdAnnonceConjoint(String string) {
        idAnnonceConjoint = string;
    }

    public void setLangueISO(String langueISO) {
        this.langueISO = langueISO;
    }

    public void setLangueISOConjoint(String langueISOConjoint) {
        this.langueISOConjoint = langueISOConjoint;
    }

    public void setNumeroAVSAssure(String numeroAVSAssure) {
        this.numeroAVSAssure = numeroAVSAssure;
    }

    public void setNumeroAVSExConjoint(String numeroAVSExConjoint) {
        this.numeroAVSExConjoint = numeroAVSExConjoint;
    }

    /**
     * @param stream
     */
    public void setOut(ByteArrayOutputStream stream) {
        out = stream;
    }

}
