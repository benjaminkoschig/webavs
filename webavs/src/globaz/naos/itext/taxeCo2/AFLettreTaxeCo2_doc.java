package globaz.naos.itext.taxeCo2;

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
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.itext.FADetailInteretMoratoire_Param;
import globaz.musca.itext.FAImpressionFacturation;
import globaz.musca.process.FAImpressionFactureProcess;
import globaz.musca.util.FAUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.taxeCo2.AFLettreTaxeCo2;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.osiris.application.CAApplication;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.db.tiers.TITiers;

public class AFLettreTaxeCo2_doc extends FAImpressionFacturation {

    private static final long serialVersionUID = 1L;

    public final static String NUM_REF_INFOROM_LETTRE_TAXECO2 = "0237CAF";

    protected final static String TEMPLATE_FILENAME = "NAOS_LETTRE_TAXE_CO2";
    protected final static String TEMPLATE_FILENAME_PARAM = "template.taxeco2.filename";

    protected java.lang.String adresseDomicile;
    protected String adressePrincipalePaiement;
    ICTDocument[] document = null;
    protected FAEnteteFacture enteteFacture = null;
    private BSession sessionMusca = null;
    private String signataire1 = new String();
    private String signataire2 = new String();

    protected AFLettreTaxeCo2 taxe = null;

    protected TITiers tiers = null;

    public AFLettreTaxeCo2_doc() throws Exception {
        this(new BSession(AFApplication.DEFAULT_APPLICATION_NAOS));
    }

    public AFLettreTaxeCo2_doc(BSession session) throws java.lang.Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "REDISTRIBUTIONCO2");
        super.setDocumentTitle(getSession().getLabel("TITRE_LETTRE_REDISTRIBUTION"));
        super.setSendCompletionMail(false);
    }

    public AFLettreTaxeCo2_doc(FAImpressionFactureProcess parent) throws java.lang.Exception {
        super(parent, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "REDISTRIBUTIONCO2");
        super.setDocumentTitle(getSession().getLabel("TITRE_LETTRE_REDISTRIBUTION"));
        super.setSendCompletionMail(false);
    }

    @Override
    public void _executeCleanUp() {
        super._executeCleanUp();
    }

    protected void _headerText(CaisseHeaderReportBean headerBean) {
        try {
            document = getICTDocument();
            if (getPassage() == null) {
                passage = new FAPassage();
                passage.setISession(getSession());
                passage.setIdPassage(enteteFacture.getIdPassage());
                passage.retrieve(getTransaction());
            }

            if (JadeStringUtil.isBlank(passage.getPersonneRef())) {
                headerBean.setUser(getSession().getUserInfo());
                headerBean.setNomCollaborateur(getSession().getUserFullName());
                headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
            } else {
                JadeUser user;
                JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
                user = service.loadForVisa(passage.getPersonneRef());
                if (user != null) {
                    headerBean.setNomCollaborateur(user.getFirstname() + " " + user.getLastname());
                    headerBean.setTelCollaborateur(user.getPhone());
                    headerBean.setUser(user);
                }
            }

            // adresse du tiers
            headerBean.setAdresse(adressePrincipalePaiement);
            // Ajout du confidentiel
            if (CAApplication.getApplicationOsiris().getCAParametres().isConfidentiel()) {
                headerBean.setConfidentiel(true);
            }
            // texte de la date
            headerBean.setDate(JACalendar.format(getPassage().getDateFacturation(), enteteFacture.getISOLangueTiers()));

            // numéro AVS
            headerBean.setNoAvs("");
            if (!CaisseHelperFactory.CS_AFFILIE_PARITAIRE.equals(enteteFacture.getIdRole())) {
                if (!"".equals(enteteFacture.getNumeroAVSTiers(getTransaction()))) {
                    headerBean.setNoAvs(enteteFacture.getNumeroAVSTiers(getTransaction()));
                }
            }

            // No affilié
            headerBean.setNoAffilie(enteteFacture.getIdExterneRole());
            // Renseinge le Numéro IDE
            AFIDEUtil.addNumeroIDEInDoc(getSession(), headerBean, getEntity().getIdTiers(), getEntity()
                    .getIdExterneRole(), getEntity().getIdExterneFacture(), getEntity().getIdRole());

            super.setParametres(AFLettreTaxeCo2_Param.P_TITLE, getTexte(1, document));
            super.setParametres(
                    AFLettreTaxeCo2_Param.P_TITLE2,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(2, document), false),
                            FAUtil.prepareQuotes(tiers.getFormulePolitesse(tiers.getLangue()), false)));

        } catch (Exception e) {
            this._addError("Erreur lors de la création du Header de la lettre Taxe Co2: "
                    + enteteFacture.getIdExterneRole() + e.getMessage());
        }
    }

    protected void _letterBody() {
        try {

            FWCurrency montantTaxe = new FWCurrency(taxe.getMontant());
            montantTaxe.abs();
            super.setParametres(
                    AFLettreTaxeCo2_Param.P_TEXT,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(3, document), false),
                            JANumberFormatter.fmt(montantTaxe.toString(), true, true, false, 2),
                            FAUtil.prepareQuotes(tiers.getFormulePolitesse(tiers.getLangue()), false)));
        } catch (Exception e) {
            this._addError("Erreur lors de la création du Header du détail d'un intérêt: "
                    + enteteFacture.getIdExterneRole() + e.getMessage());
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
                    + enteteFacture.getIdExterneRole() + e.getMessage());
        }
    }

    @Override
    public void beforeBuildReport() {
        super.setSendMailOnError(false);
        super.setDocumentTitle(enteteFacture.getIdExterneRole() + " - " + enteteFacture.getNomTiers());
    }

    @Override
    public final void beforeExecuteReport() {
        super.setTemplateFile(AFLettreTaxeCo2_doc.TEMPLATE_FILENAME);
        setImpressionParLot(true);
        setTailleLot(1);
        // Initialise le document pour le catalogue de texte
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() throws Exception {
        fillDocInfo();

        // si un autre template est définit dans les params des docs, on l'utilise
        String templateName = getImporter().getTemplateProperty(getDocumentInfo(),
                AFLettreTaxeCo2_doc.TEMPLATE_FILENAME_PARAM);
        if (!JadeStringUtil.isEmpty(templateName)) {
            super.setTemplateFile(templateName);
        }

        // Vérifier l'id de l'entête
        if (JadeStringUtil.isIntegerEmpty(taxe.getIdEnteteFacture())) {
            return;
        }

        // initialiser les variables d'aide
        adressePrincipalePaiement = enteteFacture.getAdressePrincipale(getTransaction(), globaz.globall.util.JACalendar
                .today().toStr("."));
        adresseDomicile = enteteFacture.getAdresseDomicile(getTransaction(), JACalendar.today().toStr("."));

        // Get Parameters
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), enteteFacture.getISOLangueTiers());

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        tiers = getTiers();

        _headerText(headerBean);
        _letterBody();

        caisseReportHelper.addHeaderParameters(this, headerBean);
        caisseReportHelper.addSignatureParameters(this);
    }

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
            getDocumentInfo().setDocumentDate(getPassage().getDateFacturation());

        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", numAff);
        }

        getDocumentInfo().setDocumentTypeNumber(AFLettreTaxeCo2_doc.NUM_REF_INFOROM_LETTRE_TAXECO2);
        getDocumentInfo().setDocumentType(AFLettreTaxeCo2_doc.NUM_REF_INFOROM_LETTRE_TAXECO2);

        try {
            // GED
            if (getSessionMusca().getApplication().getProperty("mettreGed").equals("true")) {
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

    // Retourne le document à utiliser
    public ICTDocument[] getICTDocument() {
        ICTDocument res[] = null;
        ICTDocument document = null;

        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, "Error while api for document");
        }
        // On charge le document
        document.setISession(getSession());
        document.setCsDomaine(CodeSystem.DOMAINE_CAT_AFF);
        document.setCsTypeDocument(CodeSystem.TYPE_LETTRE_REDISTRIBUTION_TAXECO2);
        // document.setNom(TITRE_LETTRE);

        document.setCodeIsoLangue(enteteFacture.getISOLangueTiers());
        document.setActif(new Boolean(true));

        try {
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWViewBeanInterface.ERROR, "Error while getting document");
        }
        return res;
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
                    if ((i + 1) < listeTextes.size()) {
                        // Sinon c'est que le niveau doit être séparé par un
                        // paragraphe
                        resString = resString.concat(texte.getDescription() + "\n\n");
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
            this._addError("Erreur lors du retrieve du tiers pour la lettre sur la taxe CO2: "
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
        if (hasNext = getEntityList().hasNext()) {
            // while ((hasNext = getEntityList().hasNext())) {
            taxe = (AFLettreTaxeCo2) getEntityList().next();
            try {
                // retrouver l'entete de facture associé à la décision
                enteteFacture = new FAEnteteFacture();
                enteteFacture.setSession(getSession());
                enteteFacture.setIdEntete(taxe.getIdEnteteFacture());
                enteteFacture.retrieve(getTransaction());

                // si l'entete existe pour la décision d'intérêt, imprimer le
                // document
                if (!enteteFacture.isNew() && !getTransaction().hasErrors()) {
                    super.setEntity(enteteFacture);
                    factureImpressionNo++;
                    setFactureImpressionNo(factureImpressionNo);
                    // break;
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                hasNext = false;
                // break;
            }
        }
        return hasNext;
    }

    public void setSignataire1(String string) {
        signataire1 = string;
    }

    public void setSignataire2(String string) {
        signataire2 = string;
    }

}
