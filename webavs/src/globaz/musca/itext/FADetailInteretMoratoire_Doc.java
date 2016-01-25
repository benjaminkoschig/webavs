package globaz.musca.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JABVR;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.process.FAImpressionFactureProcess;
import globaz.musca.util.FAUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.util.AFIDEUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.interets.CAGenreInteret;
import globaz.osiris.db.interets.CAGenreInteretManager;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.db.tiers.TITiers;
import java.text.MessageFormat;

public class FADetailInteretMoratoire_Doc extends FAImpressionFacturation {

    private static final long serialVersionUID = 1L;
    protected static final String COT_ARRIERES_NOMDOC = "Cotisations arriérées";
    protected static final String COT_PERS_NOMDOC = "Cotisations personnelles";
    protected static final String DEC_FINAL_NOMDOC = "Décompte final";
    protected static final String INT_REMUN_NOMDOC = "Intérêts rémunératoires";
    protected static final String INT_TARDIF_NOMDOC = "Intérêts tardifs";
    public final static String NUM_REF_INFOROM_IM_25_POUR = "0129CFA";

    public final static String NUM_REF_INFOROM_IM_COT_ARR = "0094CFA";
    public final static String NUM_REF_INFOROM_IM_PAI_TAR = "0095CFA";
    public final static String NUM_REF_INFOROM_IM_REM_TAR = "0128CFA";
    public final static String NUM_REF_INFOROM_INT_REMUN = "0096CFA";
    protected final static String TEMPLATE_FILENAME = "MUSCA_INTMOR_DEC";

    protected java.lang.String adresseDomicile;
    protected String adressePrincipalePaiement;
    private Boolean callEcran = new Boolean(false);
    ICTDocument[] document = null;
    protected FAEnteteFacture enteteFacture = null;
    private Boolean envoyerGed = new Boolean(false);
    private String genreAffilie = "";
    private String genreDecompte = "";
    String genreInteret = "";
    private String idInteretMoratoire;
    protected CAInteretMoratoire interetMoratoire;
    private String libelleInteret;

    private String numControle = "";
    protected java.lang.String numeroCompte;

    private BSession sessionMusca = null;
    private String signataire1 = new String();

    private String signataire2 = new String();
    protected TITiers tiers = null;

    private boolean wantReferenceFacture = false;

    public FADetailInteretMoratoire_Doc() throws Exception {
        this(new BSession(FAApplication.DEFAULT_APPLICATION_MUSCA));
    }

    public FADetailInteretMoratoire_Doc(BSession session) throws java.lang.Exception {
        super(session, FAApplication.APPLICATION_MUSCA_REP, "DECISIONSINTERET");
        super.setDocumentTitle(getSession().getLabel("TITRE_DOC_DECISION_INTERET"));
        super.setSendCompletionMail(false);
    }

    public FADetailInteretMoratoire_Doc(FAImpressionFactureProcess parent) throws java.lang.Exception {
        super(parent, FAApplication.APPLICATION_MUSCA_REP, "DECISIONSINTERET");
        super.setDocumentTitle(getSession().getLabel("TITRE_DOC_CAISSE_COMP"));
        super.setSendCompletionMail(false);
        setEnvoyerGed(parent.getEnvoyerGed());
        setCallEcran(parent.getCallEcran());

    }

    @Override
    public void _executeCleanUp() {
        super._executeCleanUp();
    }

    protected void _headerText(CaisseHeaderReportBean headerBean) {

        try {
            genreInteret = getInteretMoratoire().getIdGenreInteret();
            document = getICTDocument(genreInteret);
            JadeUser user;
            if (getPassage() == null) {
                passage = new FAPassage();
                passage.setISession(getSession());
                passage.setIdPassage(entity.getIdPassage());
                passage.retrieve(getTransaction());
            }
            JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
            if (wantReferenceFacture) {
                user = service.loadForVisa(getEntity().getReferenceFacture());
            } else {
                user = service.loadForVisa(passage.getPersonneRef());
            }
            if (user == null) {
                user = getSession().getUserInfo();
            }
            // adresse du tiers
            headerBean.setAdresse(adressePrincipalePaiement);
            // Ajout du confidentiel
            if (CAApplication.getApplicationOsiris().getCAParametres().isConfidentiel()) {
                headerBean.setConfidentiel(true);
            }
            // texte de la date
            headerBean.setDate(JACalendar.format(getPassage().getDateFacturation(), entity.getISOLangueTiers()));

            // numéro AVS
            headerBean.setNoAvs("");
            if (!CaisseHelperFactory.CS_AFFILIE_PARITAIRE.equals(getEntity().getIdRole())) {
                if (!"".equals(getEntity().getNumeroAVSTiers(getTransaction()))) {
                    headerBean.setNoAvs(getEntity().getNumeroAVSTiers(getTransaction()));
                }
            }

            if (user != null) {
                headerBean.setNomCollaborateur(user.getFirstname() + " " + user.getLastname());
                headerBean.setTelCollaborateur(user.getPhone());
                headerBean.setUser(user);
            }

            // No affilié
            headerBean.setNoAffilie(getEntity().getIdExterneRole());
            // Renseinge le Numéro IDE
            AFIDEUtil.addNumeroIDEInDoc(getSession(), headerBean, getEntity().getIdTiers(), getEntity()
                    .getIdExterneRole(), getEntity().getIdExterneFacture(), getEntity().getIdRole());

            super.setParametres(
                    FWIImportParametre.PARAM_TITLE,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(1, document), false),
                            FAUtil.prepareQuotes(getLibelleInteret(), false),
                            FAUtil.prepareQuotes(getGenreAffilie(), false),
                            FAUtil.prepareQuotes(getGenreDecompte(), false),
                            FAUtil.prepareQuotes(getNumControle(), false)));

            String numeroFacture = "";
            if (getInteretMoratoire().getSection() != null) {
                numeroFacture = getInteretMoratoire().getSection().getIdExterne();
            }

            super.setParametres(
                    FADetailInteretMoratoire_Param.P_TITLE2,
                    MessageFormat.format(FAUtil.prepareQuotes(getTexte(2, document), false),
                            FAUtil.prepareQuotes(getEntity().getIdExterneFacture(), false),
                            FAUtil.prepareQuotes(getEntity().getIdExterneRole(), false),
                            FAUtil.prepareQuotes(getGenreAffilie(), false),
                            FAUtil.prepareQuotes(getGenreDecompte(), false),
                            FAUtil.prepareQuotes(getNumControle(), false), FAUtil.prepareQuotes(numeroFacture, false)));

        } catch (Exception e) {
            this._addError("Erreur lors de la création du Header du détail d'un intérêt: "
                    + getEntity().getIdExterneRole() + e.getMessage());
        }
    }

    public void _initMontant() {
        String montantFacture = getEntity().getTotalFacture();
        montantFacture = JANumberFormatter.deQuote(montantFacture);
        // convertir le montant en entier (BigInteger)
        montantSansCentime = JAUtil.createBigDecimal(montantFacture).toBigInteger().toString();
        java.math.BigDecimal montantSansCentimeBig = JAUtil.createBigDecimal(montantSansCentime);
        // convertir le montant avec centimes en BigDecimal
        java.math.BigDecimal montantAvecCentimeBig = JAUtil.createBigDecimal(montantFacture);
        // les centimes représentés en entier
        centimes = montantAvecCentimeBig.subtract(montantSansCentimeBig).toString().substring(2, 4);
    }

    protected void _letterBody() {

        try {
            tiers = getTiers();
            // On set le texte du haut du document
            super.setParametres(
                    FADetailInteretMoratoire_Param.PARAM_TEXTTOP,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(3, document), false),
                            FAUtil.prepareQuotes(tiers.getFormulePolitesse(tiers.getLangue()), false),
                            FAUtil.prepareQuotes(getGenreAffilie(), false),
                            FAUtil.prepareQuotes(getGenreDecompte(), false),
                            FAUtil.prepareQuotes(getNumControle(), false)));
            // On sette le texte du bas du document
            super.setParametres(
                    FADetailInteretMoratoire_Param.PARAM_TEXTBOTTOM,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(4, document), false),
                            FAUtil.prepareQuotes(tiers.getFormulePolitesse(tiers.getLangue()), false),
                            FAUtil.prepareQuotes(getGenreAffilie(), false),
                            FAUtil.prepareQuotes(getGenreDecompte(), false),
                            FAUtil.prepareQuotes(getNumControle(), false)));
            // On set le texte du total des intérêts
            super.setParametres(FADetailInteretMoratoire_Param.P_TOTAL,
                    getSession().getApplication().getLabel("FACTEXT_TOTAL_NOCOMMENT", entity.getISOLangueTiers()));

        } catch (Exception e) {
            this._addError("Erreur lors de la création du Header du détail d'un intérêt: "
                    + getEntity().getIdExterneRole() + e.getMessage());
        }

    }

    protected void _signatureText(CaisseSignatureReportBean signBean) {
        try {
            JadeUser user1;
            JadeUser user2;
            JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
            user1 = service.loadForVisa(passage.getPersonneSign1());
            user2 = service.loadForVisa(passage.getPersonneSign2());
            if ((user1 != null) && (user2 != null)) {
                signataire1 = user1.getFirstname() + " " + user1.getLastname();
                signataire2 = user2.getFirstname() + " " + user2.getLastname();
            } else if ((user1 != null) && (user2 == null)) {
                signataire1 = user1.getFirstname() + " " + user1.getLastname();
                signataire2 = "";
            } else if ((user1 == null) && (user2 != null)) {
                signataire1 = "";
                signataire2 = user2.getFirstname() + " " + user2.getLastname();
            } else {
                signataire1 = "";
                signataire2 = "";
            }
            signBean.setSignataire(signataire1);
            signBean.setSignataire2(signataire2);
        } catch (Exception e) {
            this._addError("Erreur lors de la création de la signature du détail d'un intérêt: "
                    + getEntity().getIdExterneRole() + e.getMessage());

        }
    }

    protected void _summaryText() {
        try {
            super.setParametres(FADetailInteretMoratoire_Param.PARAM_TEXTSIGN, getTexte(5, document));
        } catch (Exception e) {
            this._addError("Erreur lors de la création du text du détail d'un intérêt: "
                    + getEntity().getIdExterneRole() + e.getMessage());
        }

    }

    protected void _tableHeader() {
        try {
            setColumnHeader(1,
                    getSession().getApplication().getLabel("INTMOR_MONTANTSOUMIS", entity.getISOLangueTiers()));
            setColumnHeader(2, getSession().getApplication().getLabel("INTMOR_DATEDUAU", entity.getISOLangueTiers()));
            setColumnHeader(3, getSession().getApplication().getLabel("INTMOR_NBJOURS", entity.getISOLangueTiers()));
            setColumnHeader(4, getSession().getApplication().getLabel("INTMOR_TAUX", entity.getISOLangueTiers()));
            setColumnHeader(6, getSession().getApplication().getLabel("INTMOR_ANNEE", entity.getISOLangueTiers()));

            if (CAGenreInteret.CS_TYPE_REMUNERATOIRES.equals(getInteretMoratoire().getIdGenreInteret())) {
                setColumnHeader(5,
                        getSession().getApplication().getLabel("INTREMUN_MONTANTINTERET", entity.getISOLangueTiers()));
            } else {
                setColumnHeader(5,
                        getSession().getApplication().getLabel("INTMOR_MONTANTINTERET", entity.getISOLangueTiers()));
            }
        } catch (Exception e) {
            this._addError("Erreur lors de la création du TableHeader du détail d'un intérêt:"
                    + getEntity().getIdExterneRole() + e.getMessage());
        }
    }

    @Override
    public void beforeBuildReport() {
        super.setSendMailOnError(false);
        super.setDocumentTitle(entity.getIdExterneRole() + " - " + entity.getNomTiers());

    }

    @Override
    public final void beforeExecuteReport() {
        setImpressionParLot(true);
        setTailleLot(1);
        try {
            if ("true".equalsIgnoreCase(FWFindParameter.findParameter(getTransaction(), "1", "FAREFFACTU",
                    JACalendar.todayJJsMMsAAAA(), "", 0))) {
                wantReferenceFacture = true;
            }
        } catch (Exception e) {
            wantReferenceFacture = false;
        }
        // Initialise le document pour le catalogue de texte
    }

    @Override
    public void createDataSource() throws Exception {

        // première chose à faire, initialiser le docInfo
        fillDocInfo();

        FAUtil.fillDocInfoWithPassageInfo(getDocumentInfo(), getPassage());

        super.setParametres(FAImpressionFacture_Param.P_HEADER_EACH_PAGE, new Boolean(wantHeaderOnEachPage()));
        super.setTemplateFile(FADetailInteretMoratoire_Doc.TEMPLATE_FILENAME);

        FADetailInteretMoratoire_DS manager = null;

        // initialiser les variables d'aide
        adressePrincipalePaiement = entity.getAdressePrincipale(getTransaction(), globaz.globall.util.JACalendar
                .today().toStr("."));
        adresseDomicile = entity.getAdresseDomicile(getTransaction(), JACalendar.today().toStr("."));

        numeroCompte = getSession().getLabel("NUMERO_COMPTE"); // numero CC

        // Sous contrôle d'exceptions
        manager = new FADetailInteretMoratoire_DS();
        manager.setISession(((FAApplication) getSession().getApplication()).getSessionOsiris(getSession()));

        // Vérifier l'id de l'entête
        if (JadeStringUtil.isIntegerEmpty(entity.getIdEntete())) {
            return;
        }

        // Where clause

        manager.setForIdInteretMoratoire(getInteretMoratoire().getIdInteretMoratoire());

        super.setDataSource(manager);

        // Get Parameters
        // ALD : 27.08.2010, ajout du docInfo pour gestion des documents
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), entity.getISOLangueTiers());

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        CaisseSignatureReportBean signBean = new CaisseSignatureReportBean();

        if (enteteFacture.getIdRole().equals(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)
                || enteteFacture.getIdRole().equals(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)) {
            setGenreAffilie(getSession().getCodeLibelle(enteteFacture.getIdRole()));
        } else {
            setGenreAffilie("");
        }
        if (!JadeStringUtil.isEmpty(enteteFacture.getIdSousType())) {
            setGenreDecompte(getSession().getCodeLibelle(enteteFacture.getIdSousType()));
        } else {
            setGenreDecompte("");
        }
        if (!JadeStringUtil.isIntegerEmpty(enteteFacture.getIdControle())) {

            if (FAUtil.isNouveauControleEmployeur(getSession())) {
                CEControleEmployeur controle = new CEControleEmployeur();
                controle.setSession(getSession());
                controle.setIdControleEmployeur(enteteFacture.getIdControle());
                controle.retrieve();
                if (!JadeStringUtil.isEmpty(controle.getRapportNumero())) {
                    setNumControle(getSession().getApplication().getLabel("RAPPORT_CONTROLE",
                            entity.getISOLangueTiers())
                            + controle.getRapportNumero());
                }

            } else {
                AFControleEmployeur controle = new AFControleEmployeur();
                controle.setSession(getSession());
                controle.setControleEmployeurId(enteteFacture.getIdControle());
                controle.retrieve();
                if (!JadeStringUtil.isEmpty(controle.getRapportNumero())) {
                    setNumControle(getSession().getApplication().getLabel("RAPPORT_CONTROLE",
                            entity.getISOLangueTiers())
                            + controle.getRapportNumero());
                }
            }
        } else {
            setNumControle("");
        }

        _headerText(headerBean);
        _letterBody();
        _tableHeader();
        _summaryText();
        _signatureText(signBean);

        caisseReportHelper.addHeaderParameters(this, headerBean);
        caisseReportHelper.addSignatureParameters(this, signBean);
    };

    private void fillDocInfo() {

        String numAff = getEntity().getIdExterneRole();
        String idTiers = getEntity().getIdTiers();
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", numAff);
        if (getEntity().isNonImprimable().booleanValue()) {
            getDocumentInfo().setSeparateDocument(true);
        }
        try {
            // prend le rôle du compte annexe
            String role = getEntity().getIdRole();
            if (JadeStringUtil.isIntegerEmpty(role)) {
                role = ITIRole.CS_AFFILIE;
            }
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affilieFormater.unformat(numAff));
            TIDocumentInfoHelper.fill(getDocumentInfo(), idTiers, getSession(), role, numAff,
                    affilieFormater.unformat(numAff));

            getDocumentInfo().setDocumentProperty("annee", getAnneeFromEntete(getEntity()));
            // getDocumentInfo().setDocumentDate(getDateImpression());
            getDocumentInfo().setDocumentDate(getPassage().getDateFacturation());

        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", numAff);
        }

        if (getInteretMoratoire().getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_REMUNERATOIRES)) {
            getDocumentInfo().setDocumentTypeNumber(FADetailInteretMoratoire_Doc.NUM_REF_INFOROM_INT_REMUN);
        } else if (getInteretMoratoire().getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_TARDIF)) {
            getDocumentInfo().setDocumentTypeNumber(FADetailInteretMoratoire_Doc.NUM_REF_INFOROM_IM_PAI_TAR);
        } else if (getInteretMoratoire().getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_DECOMPTE_FINAL)) {
            getDocumentInfo().setDocumentTypeNumber(FADetailInteretMoratoire_Doc.NUM_REF_INFOROM_IM_REM_TAR);
        } else if (getInteretMoratoire().getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_COTISATIONS_ARRIEREES)) {
            getDocumentInfo().setDocumentTypeNumber(FADetailInteretMoratoire_Doc.NUM_REF_INFOROM_IM_COT_ARR);
        } else if (getInteretMoratoire().getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_COTISATIONS_PERSONNELLES)) {
            getDocumentInfo().setDocumentTypeNumber(FADetailInteretMoratoire_Doc.NUM_REF_INFOROM_IM_25_POUR);
        }

        try {
            // GED
            if (getEnvoyerGed().booleanValue()) {
                getDocumentInfo().setPublishDocument(false);
                getDocumentInfo().setArchiveDocument(true);
            } else if (getSessionMusca().getApplication().getProperty("mettreGed").equals("true")
                    && !getCallEcran().booleanValue()) {
                getDocumentInfo().setPublishDocument(false);
                getDocumentInfo().setArchiveDocument(true);
            } else {
                getDocumentInfo().setPublishDocument(true);
                getDocumentInfo().setArchiveDocument(false);
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    public Boolean getCallEcran() {
        return callEcran;
    }

    public Boolean getEnvoyerGed() {
        return envoyerGed;
    }

    public String getGenreAffilie() {
        return genreAffilie;
    }

    public String getGenreDecompte() {
        return genreDecompte;
    }

    // Retourne le document à utiliser
    public ICTDocument[] getICTDocument(String genreInteret) {
        ICTDocument res[] = null;
        ICTDocument document = null;

        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, "Error while api for document");
        }
        // On charge le document
        document.setISession(getSession());
        document.setCsDomaine(FAImpressionFacturation.DOMAINE_FACTURATION);
        document.setCsTypeDocument(FAImpressionFacturation.DECOMPTE_COTISATION);

        // En fonction du genre d'intérêt on va chercher le document portant le
        // nom correspondant
        if (genreInteret.equals(CAGenreInteret.CS_TYPE_REMUNERATOIRES)) {
            document.setNom(FADetailInteretMoratoire_Doc.INT_REMUN_NOMDOC);
        } else if (genreInteret.equals(CAGenreInteret.CS_TYPE_TARDIF)) {
            document.setNomLike(FADetailInteretMoratoire_Doc.INT_TARDIF_NOMDOC);
            document.setDefault(new Boolean(true));
        } else if (genreInteret.equals(CAGenreInteret.CS_TYPE_DECOMPTE_FINAL)) {
            document.setNom(FADetailInteretMoratoire_Doc.DEC_FINAL_NOMDOC);
        } else if (genreInteret.equals(CAGenreInteret.CS_TYPE_COTISATIONS_ARRIEREES)) {
            document.setNom(FADetailInteretMoratoire_Doc.COT_ARRIERES_NOMDOC);
        } else if (genreInteret.equals(CAGenreInteret.CS_TYPE_COTISATIONS_PERSONNELLES)) {
            document.setNom(FADetailInteretMoratoire_Doc.COT_PERS_NOMDOC);
        }

        document.setCodeIsoLangue(entity.getISOLangueTiers());
        document.setActif(new Boolean(true));

        try {
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWViewBeanInterface.ERROR, "Error while getting document");
        }
        return res;
    }

    public String getIdInteretMoratoire() {
        return idInteretMoratoire;
    }

    public CAInteretMoratoire getInteretMoratoire() {
        if (getEntity() == null) {
            return interetMoratoire;
        }

        try {
            if ((interetMoratoire == null) || interetMoratoire.isNew()) {

                // rechercher l'intérêt moratoire depuis l'entete de facture
                interetMoratoire = new CAInteretMoratoire();
                interetMoratoire.setISession(((FAApplication) getSession().getApplication())
                        .getSessionOsiris(getSession()));
                interetMoratoire.setIdJournalFacturation(getEntity().getIdPassage());
                interetMoratoire.setIdSectionFacture(getEntity().getIdEntete());
                interetMoratoire.setAlternateKey(CAInteretMoratoire.AK_FACTURE_JOURNAL);
                interetMoratoire.retrieve(getTransaction());
            }
            CAGenreInteretManager genre = new CAGenreInteretManager();
            genre.setSession(getSession());
            genre.setForIdPlanCalculInteret(interetMoratoire.getIdPlan());
            genre.setForIdTypeInteret(interetMoratoire.getIdGenreInteret());
            genre.find();
            tiers = getTiers();
            if (genre.size() > 0) {
                if (tiers.getLangueIso().equals("fr")) {
                    setLibelleInteret(((CAGenreInteret) genre.getFirstEntity()).getLibelleFR());
                }
                if (tiers.getLangueIso().equals("de")) {
                    setLibelleInteret(((CAGenreInteret) genre.getFirstEntity()).getLibelleDE());
                }
                if (tiers.getLangueIso().equals("it")) {
                    setLibelleInteret(((CAGenreInteret) genre.getFirstEntity()).getLibelleIT());
                }

            }
            if (getTransaction().hasErrors()) {
                // id interet moratoire n'existe pas
                getTransaction().addErrors(getSession().getLabel("7350"));
                return null;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return interetMoratoire;

    }

    public String getLibelleInteret() {
        return libelleInteret;
    }

    public String getNumControle() {
        return numControle;
    }

    public String[] getOCRBText() {
        String numeroCompteWithoutDelim = getTextWithoutDelimiter(numeroCompte, "-");
        // oter les séparations
        StringBuffer noReference = new StringBuffer();
        noReference.append(entity.getIdRole());
        // 6 chars
        noReference.append(getTextWithoutDelimiter(entity.getIdExterneRole(), "."));
        // 6 chars
        noReference.append(JadeStringUtil.rightJustifyInteger(entity.getIdTypeFacture(), 2));
        // 2 chars
        noReference.append(entity.getIdExterneFacture());
        // 9 chars
        // rajouter un 0 à la 3eme pos au numéro de compte sans delimiter
        String noAdherent = numeroCompteWithoutDelim.substring(0, 2) + "0" + numeroCompteWithoutDelim.substring(2);
        JABVR bvr = null;
        try {
            bvr = new JABVR(JANumberFormatter.deQuote(entity.getTotalFacture()), noReference.toString(), noAdherent);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        String bvrArray[] = new String[2];
        bvrArray[0] = new String(bvr.get_ocrb());
        bvrArray[1] = new String(bvr.get_ligneReference());
        return bvrArray;
    }

    public BSession getSessionMusca() {
        if (sessionMusca == null) {
            try {
                sessionMusca = (BSession) GlobazSystem.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA)
                        .newSession(getSession());
            } catch (Exception ex) {
                sessionMusca = getSession();
            }
        }

        return sessionMusca;
    }

    public String getSignataire1() {
        return signataire1;
    }

    public String getSignataire2() {
        return signataire2;
    }

    // Retourne le texte du niveau entré en paramètres
    private String getTexte(int niveau, ICTDocument[] document) throws Exception {
        String resString = "";
        ICTTexte texte = null;
        // Si le document est null, on retourne un message d'erreur
        if (document == null) {
            getMemoryLog().logMessage("Il n'y a pas de document par défaut", FWViewBeanInterface.ERROR, "");
        } else {
            ICTListeTextes listeTextes = null;
            // On charge la liste de textes du niveau donné
            try {
                listeTextes = document[0].getTextes(niveau);
            } catch (Exception e3) {
                getMemoryLog().logMessage(e3.toString(), FWViewBeanInterface.ERROR,
                        "Error while getting listes de textes");
            }
            // S'il n'y a pas de texte on retourne une erreur
            if (listeTextes == null) {
                getMemoryLog().logMessage("Il n'y a pas de texte", FWViewBeanInterface.ERROR, "");
            } else {
                // Dans le cas ou on a un texte, on va parcourir toutes les
                // positions de ce texte
                for (int i = 0; i < listeTextes.size(); i++) {
                    // On charge le texte de la position donnée
                    texte = listeTextes.getTexte(i + 1);
                    // On regarde si c'est la dernière position du niveau
                    if (i + 1 < listeTextes.size()) {
                        // Si ce n'est pas le cas, on regarde si on est au
                        // niveau 2, si le genre d'intérêts est rémunératoire
                        // et qu'on est pas à la première position
                        // (Pour les intérêts rémunératoire le niveau 2 n'est
                        // pas séparé par des paragraphes mais uniquement par
                        // des retours
                        // à la ligne, sauf pour la première position(champ
                        // contenant madame, monsieur) qui sera séparée par un
                        // paragraphe)
                        if ((niveau == 2) && genreInteret.equals(CAGenreInteret.CS_TYPE_REMUNERATOIRES) && (i + 1 != 1)) {
                            // Si la condition est respectée on sépare le niveau
                            // par un retour à la ligne
                            resString = resString.concat(texte.getDescription() + "\n");
                        } else {
                            // Sinon c'est que le niveau doit être séparé par un
                            // paragraphe
                            resString = resString.concat(texte.getDescription() + "\n\n");
                        }
                    } else {
                        // Dans le cas ou c'est la dernière position du niveau,
                        // on n'ajoute aucun retour à la ligne ni paragraphe
                        resString = resString.concat(texte.getDescription());
                    }
                }
            }
        }
        // return format(resString);
        return resString;
    }

    public TITiers getTiers() {
        try {
            tiers = new TITiers();
            tiers.setSession(entity.getSession());
            tiers.setIdTiers(enteteFacture.getIdTiers());
            tiers.retrieve();
        } catch (Exception e) {
            this._addError("Erreur lors du retrieve du tiers pour la décisions d'intérêt: "
                    + enteteFacture.getIdExterneRole() + e.getMessage());
        }
        return tiers;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        boolean hasNext = false;
        while ((hasNext = getEntityList().hasNext())) {
            CAInteretMoratoire imoratoire = (CAInteretMoratoire) getEntityList().next();
            try {
                // retrouver l'entete de facture associé à la décision
                enteteFacture = new FAEnteteFacture();
                enteteFacture.setSession(getSession());
                enteteFacture.setIdEntete(imoratoire.getIdSectionFacture());
                enteteFacture.retrieve(getTransaction());

                // si l'entete existe pour la décision d'intérêt, imprimer le
                // document
                if (!enteteFacture.isNew() && !getTransaction().hasErrors()) {
                    super.setEntity(enteteFacture);
                    setInteretMoratoire(imoratoire);
                    factureImpressionNo++;
                    setFactureImpressionNo(factureImpressionNo);
                    break;
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                hasNext = false;
                break;
            }
        }
        return hasNext;
    }

    public void setCallEcran(Boolean callEcran) {
        this.callEcran = callEcran;
    }

    public void setEnvoyerGed(Boolean envoyerGed) {
        this.envoyerGed = envoyerGed;
    }

    public void setGenreAffilie(String genreAffilie) {
        this.genreAffilie = genreAffilie;
    }

    public void setGenreDecompte(String genreDecompte) {
        this.genreDecompte = genreDecompte;
    }

    public void setIdInteretMoratoire(String idInteretMoratoire) {
        this.idInteretMoratoire = idInteretMoratoire;
    }

    /**
     * Sets the interetMoratoire.
     * 
     * @param interetMoratoire
     *            The interetMoratoire to set
     */
    public void setInteretMoratoire(CAInteretMoratoire interetMoratoire) {
        this.interetMoratoire = interetMoratoire;
    }

    public void setLibelleInteret(String string) {
        libelleInteret = string;
    }

    public void setNumControle(String numControle) {
        this.numControle = numControle;
    }

    public void setSignataire1(String string) {
        signataire1 = string;
    }

    public void setSignataire2(String string) {
        signataire2 = string;
    }

}
