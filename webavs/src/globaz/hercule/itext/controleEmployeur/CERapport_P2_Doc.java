package globaz.hercule.itext.controleEmployeur;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.pyxis.api.ITIRole;
import java.util.Map;

/**
 * @author ald
 * @since Créé le 14 mars 05
 */
public class CERapport_P2_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Numéro du document
    private static final String DOC_NO = "0282CCE";
    public static final String PROP_SIGN_NOM_CAISSE = "signature.nom.caisse.";
    public static final String TEMPLATE_FILENAME = "HERCULE_RAPPORT2_CE";

    public static String getTemplateFilename() {
        return CERapport_P2_Doc.TEMPLATE_FILENAME;
    }

    protected java.lang.String adresseDomicile;
    protected String adressePaiement;
    protected String adressePrincipale;

    CEControleEmployeur controle = new CEControleEmployeur();
    ICTDocument document = null;
    ICTDocument[] documents = null;
    private String emailAdress = new String();
    private Boolean envoyerGed = new Boolean(false);
    private boolean hasNext = true;
    private String idControle = new String();
    private String idPersRef = new String();
    int index = 1;
    CEControleEmployeurManager manager = new CEControleEmployeurManager();
    int nbNiveaux = 0;

    protected java.lang.String numeroCompte;

    /**
     * Constructeur de CERapport_P2_Doc
     */
    public CERapport_P2_Doc() throws Exception {
        this(new BSession(CEApplication.DEFAULT_APPLICATION_HERCULE));
    }

    /**
     * Constructeur de CERapport_P2_Doc
     */
    public CERapport_P2_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, CEApplication.APPLICATION_HERCULE_ROOT, "CaisseDeCompensation");
        super.setDocumentTitle(getSession().getLabel("CONTROLES_TITRE_P2"));
    }

    /**
     * Constructeur de CERapport_P2_Doc
     */
    public CERapport_P2_Doc(BSession session) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, "CaisseDeCompensation");
        super.setDocumentTitle(getSession().getLabel("CONTROLES_TITRE_P2"));
    }

    /**
     * Constructeur de CERapport_P2_Doc
     */
    public CERapport_P2_Doc(BSession session, String idControle) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, "CaisseDeCompensation");
        super.setDocumentTitle(getSession().getLabel("CONTROLES_TITRE_P2"));
        this.idControle = idControle;
    }

    protected void _summaryText() {

        try {
            String autre = getSession().getApplication().getLabel("RAPPORT_AUTRE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_AUTRE, autre);
            String affLaa = getSession().getApplication().getLabel("RAPPORT_AFF_LAA", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_AFF_LAA, affLaa);
            String affLpp = getSession().getApplication().getLabel("RAPPORT_AFF_LPP", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_AFF_LPP, affLpp);
            String afOui = (getSession().getApplication().getLabel("LIBELLE_OUI", controle.getLangueTiers()));
            super.setParametres(CERapport_Param.L_OUI, afOui);
            String afNon = (getSession().getApplication().getLabel("LIBELLE_NON", controle.getLangueTiers()));
            super.setParametres(CERapport_Param.L_NON, afNon);
            String complet = getSession().getApplication().getLabel("RAPPORT_COMPLET", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_COMPLET, complet);
            String sondage = getSession().getApplication().getLabel("RAPPORT_SONDAGE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_SONDAGE, sondage);
            String compta = getSession().getApplication().getLabel("RAPPORT_COMPTA", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_COMPTA, compta);
            String bilan = getSession().getApplication().getLabel("RAPPORT_BILAN", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_BILAN, bilan);
            String apg = getSession().getApplication().getLabel("RAPPORT_APG", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_APG, apg);
            String alloc = getSession().getApplication().getLabel("RAPPORT_ALLOC", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_ALLOC, alloc);
            String af = getSession().getApplication().getLabel("RAPPORT_AF", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_AF, af);
            String afSepare = getSession().getApplication().getLabel("RAPPORT_AF_SEPARE", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_AF_SEPARE, afSepare);
            String remConstate = getSession().getApplication().getLabel("RAPPORT_REM_CONSTATE",
                    controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_REM_CONSTATE, remConstate);
            String remConseil = getSession().getApplication()
                    .getLabel("RAPPORT_REM_CONSEIL", controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_REM_CONSEIL, remConseil);
            String remRemarque = getSession().getApplication().getLabel("RAPPORT_REM_REMARQUE",
                    controle.getLangueTiers());
            super.setParametres(CERapport_Param.L_REM_REMARQUE, remRemarque);

        } catch (Exception e) {
            this._addError(null, e.getMessage());
        }

        super.setParametres(CERapport_Param.P_APG_COM, controle.getDocAllocPerteComplet());
        super.setParametres(CERapport_Param.P_APG_SON, controle.getDocAllocPerteSondage());
        super.setParametres(CERapport_Param.P_ALLOC_COM, controle.getDocAllocMiliComplet());
        super.setParametres(CERapport_Param.P_ALLOC_SON, controle.getDocAllocMiliSondage());
        super.setParametres(CERapport_Param.P_AF_COM, controle.getDocDroitAllocComplet());
        super.setParametres(CERapport_Param.P_AF_SON, controle.getDocDroitAllocSondage());
        if (controle.getRapportAFSepare().booleanValue()) {
            super.setParametres(CERapport_Param.P_AF_OUI, "X");
            super.setParametres(CERapport_Param.P_AF_NON, "");
        } else {
            super.setParametres(CERapport_Param.P_AF_OUI, "");
            super.setParametres(CERapport_Param.P_AF_NON, "X");
        }
        super.setParametres(CERapport_Param.P_REM_CONSTATE, controle.getChampConstate());
        super.setParametres(CERapport_Param.P_REM_CONSEIL, controle.getChampConseil());
        super.setParametres(CERapport_Param.P_REM_REMARQUE, controle.getChampRemarque());
        super.setParametres(
                CERapport_Param.P_SIGNATURE,
                getTemplateProperty(getDocumentInfo(),
                        CERapport_P2_Doc.PROP_SIGN_NOM_CAISSE + JadeStringUtil.toUpperCase(controle.getLangueTiers())));

    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#afterPrintDocument()
     */
    @Override
    public void afterPrintDocument() {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", controle.getNumAffilie());

        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(controle.getNumAffilie()));
            TIDocumentInfoHelper.fill(getDocumentInfo(), controle.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    controle.getNumAffilie(), affilieFormater.unformat(controle.getNumAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", controle.getNumAffilie());
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        getDocumentInfo().setArchiveDocument(true);
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() {
        super.setDocumentTitle(controle.getNumAffilie() + " -2- " + controle.getNomTiers());
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeExecuteReport()
     */
    @Override
    public final void beforeExecuteReport() {
        setImpressionParLot(true);
        setTemplateFile(CERapport_P2_Doc.TEMPLATE_FILENAME);
        if (getEnvoyerGed().booleanValue()) {
            setTailleLot(1);
        } else {
            setTailleLot(500);
        }
        // Initialise le document pour le catalogue de texte
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        getDocumentInfo().setDocumentTypeNumber(CERapport_P2_Doc.DOC_NO);

        // super.setTemplateFile(getTemplateFilename(getEntity()));
        // setTemplateFile(TEMPLATE_FILENAME);
        manager.setSession(getSession());
        manager.setForControleEmployeurId(idControle);
        manager.find();
        if (manager.size() > 0) {
            controle = (CEControleEmployeur) manager.getFirstEntity();
        }

        // initialiser les variables d'aide

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), controle.getLangueTiers());
        CaisseHeaderReportBean hb = new CaisseHeaderReportBean();
        hb.setNomCollaborateur(getSession().getUserFullName());
        hb.setUser(getSession().getUserInfo());

        _summaryText();
        String reviseur = "";
        if (!JadeStringUtil.isEmpty(controle.getControleurNom())) {
            reviseur = controle.getControleurNom();
        }
        caisseReportHelper.addHeaderParameters(this, hb);
        caisseReportHelper.addSignatureParameters(this);

        String nomReviseurSign = (getSession().getApplication()).getProperty("nomReviseurSignature");

        if (!JadeStringUtil.isBlank(nomReviseurSign) && nomReviseurSign.equals("true")) {
            Map map = getImporter().getParametre();

            String signataires = map.get(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE) + "";

            this.setParametres(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE, signataires + "       " + reviseur);
        }

        // if(user!=null){
        // caisseReportHelper.addFooterParameters(this, user);
        // }

    }

    /**
     * @return
     */
    public Boolean getEnvoyerGed() {
        return envoyerGed;
    }

    /**
     * @return
     */
    public String getIdControle() {
        return idControle;
    }

    /**
     * @return
     */
    public String getIdPersRef() {
        return idPersRef;
    }

    /**
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        boolean retValue = hasNext;

        if (hasNext) {
            hasNext = false;
        }

        return retValue;
    }

    /**
     * @param boolean1
     */
    public void setEnvoyerGed(Boolean boolean1) {
        envoyerGed = boolean1;
    }

    /**
     * @param string
     */
    public void setIdControle(String string) {
        idControle = string;
    }

    /**
     * @param string
     */
    public void setIdPersRef(String string) {
        idPersRef = string;
    }

    /**
     * @param i
     */
    public void setIndex(int i) {
        index = i;
    }

}
