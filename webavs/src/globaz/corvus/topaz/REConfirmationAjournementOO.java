package globaz.corvus.topaz;

import globaz.babel.db.copies.CTCopies;
import globaz.babel.utils.CatalogueText;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.process.REImprimerDecisionProcess;
import globaz.corvus.properties.REProperties;
import globaz.corvus.utils.REGedUtils;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.itext.PRLettreEnTete;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.topaz.PRLettreEnTeteOO;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class REConfirmationAjournementOO extends REAbstractJobOO {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NSS_ASSURE = "NSS_ASSURE";
    private static final String PARAGRAPHE_1_DATE_DEPOT = "PARAGRAPHE_1_DATE_DEPOT";
    private static final String PARAGRAPHE_1_DEBUT = "PARAGRAPHE_1_DEBUT";
    private static final String PARAGRAPHE_1_FIN = "PARAGRAPHE_1_FIN";
    private static final String PARAGRAPHE_2 = "PARAGRAPHE_2";
    private static final String PARAGRAPHE_3_DEBUT = "PARAGRAPHE_3_DEBUT";
    private static final String PARAGRAPHE_3_FIN = "PARAGRAPHE_3_FIN";
    private static final String PARAGRAPHE_3_GRAS = "PARAGRAPHE_3_GRAS";
    private static final String PARAGRAPHE_4 = "PARAGRAPHE_4";
    private static final String POLITESSE_TIERS = "{politesseTiers}";
    private static final String TITRE_ANNEXE = "TITRE_ANNEXE";
    private static final String TITRE_COPIE = "TITRE_COPIE";
    private static final String TITRE_DOCUMENT = "TITRE_DOCUMENT";
    private static final String TITRE_NSS = "TITRE_NSS";
    private static final String TITRE_TIERS = "TITRE_TIERS";
    private static final String VALEUR_ANNEXE = "VALEUR_ANNEXE";
    private static final String VALEUR_COPIE = "VALEUR_COPIE";

    private String adresseCourrier;
    private CatalogueText catalogueDecision;
    private CatalogueText catalogueLettreConfirmationAjournement;
    private String codeIsoLangue;
    private String dateDepotDemande;
    private String dateDuDocument;
    private String dateDuDocumentFormattee;
    private DemandeRente demande;
    private boolean faireUneCopiePourAgenceCommunale = false;
    private boolean miseEnGed = false;
    private PRTiersWrapper tierAdministration = null;

    public REConfirmationAjournementOO() {
        super(false);
    }

    private void checkData() {
        Checkers.checkNotNull(demande, "demande");
        Checkers.checkHasID(demande, "demande");
        Checkers.checkFullDate(demande.getDateDepot(), "demande.dateDepot", false);
        Checkers.checkHasID(demande.getRequerant(), "demande.requerant");
        Checkers.checkNotNull(adresseCourrier, "adresseCourrier");
        Checkers.checkNotNull(codeIsoLangue, "codeIsoLangue");
        Checkers.checkNotNull(dateDuDocument, "dateDuDocument");
        Checkers.checkFullDate(dateDuDocument, "dateDuDocument", false);
    }

    /**
     * <p>
     * Génère les DocumentData pour la création d'un en-tête<br/>
     * Copie de la méthode "createLettreEntete" de {@link REImprimerDecisionProcess}
     * </p>
     * 
     * @param session
     * @param idTier
     * @param idProcedureCommunication
     * @param dateDuDocument
     * @return
     * @throws Exception
     */
    private DocumentData createPRLettreEnTeteOO(final BSession session, final PRTiersWrapper tier,
            final String idProcedureCommunication, final String dateDuDocument) throws Exception {

        PRLettreEnTeteOO lettreEnTete = new PRLettreEnTeteOO();
        lettreEnTete.setSession(session);

        // BZ 5536
        // retrieve de la procédure de communication si elle est définie
        CTCopies procedureCommunication = null;
        if (!JadeStringUtil.isBlankOrZero(idProcedureCommunication)) {
            procedureCommunication = new CTCopies();
            procedureCommunication.setSession(session);
            procedureCommunication.setIdCopie(idProcedureCommunication);
            procedureCommunication.retrieve();
        }

        lettreEnTete.setTierAdresse(tier);
        lettreEnTete.setSession(session);
        lettreEnTete.setDomaineLettreEnTete(PRLettreEnTete.DOMAINE_CORVUS);
        lettreEnTete.setDateDocument(dateDuDocument);

        // BZ 5536
        if ((procedureCommunication != null) && !procedureCommunication.isNew()) {
            lettreEnTete.setReferenceProcedureComunication(procedureCommunication.getReference());
        }
        lettreEnTete.generationLettre();

        return lettreEnTete.getDocumentData();
    }

    protected void creerDocument(final JadePrintDocumentContainer allDoc, final boolean isCopie) throws Exception {

        DocumentData docData = new DocumentData();

        // 1 - Validation des paramètres d'entrées du process
        // Vérification de l'integrité des données
        checkData();

        // 2 - Récupération des information nécessaire à la génération du document
        // Chargement du modèle de document
        loadTemplateDocument(docData);

        // 3 - Formattage des dates
        formatDates();

        // 4 - Préparation des DocInfo et autres
        remplirCorpsDocument(docData);

        // 5 - En-tête et signature
        remplirEnteteEtSignature(docData, isCopie);

        // Récupération du nss/nom+prenom de l'assuré, pour l'afficher dans le mail
        String infoAssure = demande.getRequerant().getNss() + " / " + demande.getRequerant().getNom() + " "
                + demande.getRequerant().getPrenom();

        JadePublishDocumentInfo lettreDocInfo = JadePublishDocumentInfoProvider.newInstance(this);
        lettreDocInfo.setOwnerEmail(getAdresseEmail());
        lettreDocInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getAdresseEmail());
        lettreDocInfo.setDocumentTitle(getSession().getLabel("LETTRE_CONFIRMATION_AJOURNEMENT_TITRE_MAIL"));
        lettreDocInfo.setDocumentSubject(getSession().getLabel("LETTRE_CONFIRMATION_AJOURNEMENT_TITRE_MAIL"));
        lettreDocInfo.setDocumentNotes(getSession().getLabel("LETTRE_CONFIRMATION_AJOURNEMENT_SUBJECT_MAIL"));
        lettreDocInfo.setPublishDocument(false);
        lettreDocInfo.setArchiveDocument(false);
        lettreDocInfo.setDocumentType(IRENoDocumentInfoRom.LETTRE_CONFIRMATION_AJOURNEMENT);
        lettreDocInfo.setDocumentTypeNumber(IRENoDocumentInfoRom.LETTRE_CONFIRMATION_AJOURNEMENT);
        lettreDocInfo.setDocumentProperty(REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                REGedUtils.getCleGedPourTypeRente(getSession(), REGedUtils.getTypeRentePourCetteDemandeRente(demande)));
        // Mise en GED
        if (miseEnGed && !isCopie) {
            lettreDocInfo.setArchiveDocument(true);
        }

        // 4 - ajout du doc
        allDoc.addDocument(docData, lettreDocInfo);

        TIDocumentInfoHelper.fill(lettreDocInfo, String.valueOf(demande.getRequerant().getId()), getSession(), null,
                null, null);

    }

    @Override
    protected List<CatalogueText> definirCataloguesDeTextes() {

        catalogueLettreConfirmationAjournement = new CatalogueText();
        catalogueLettreConfirmationAjournement.setCodeIsoLangue(codeIsoLangue);
        catalogueLettreConfirmationAjournement.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        catalogueLettreConfirmationAjournement.setCsTypeDocument(IRECatalogueTexte.CS_LETTRE_CONFIRMATION_AJOURNEMENT);
        catalogueLettreConfirmationAjournement.setNomCatalogue("openOffice");

        catalogueDecision = new CatalogueText();
        catalogueDecision.setCodeIsoLangue(codeIsoLangue);
        catalogueDecision.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        catalogueDecision.setCsTypeDocument(IRECatalogueTexte.CS_DECISION);
        catalogueDecision.setNomCatalogue("openOffice");

        return Arrays.asList(catalogueLettreConfirmationAjournement, catalogueDecision);
    }

    private void formatDates() {
        if (!codeIsoLangue.equals("DE")) {
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRENCH);
            Date d = JadeDateUtil.getGlobazDate(dateDuDocument);
            dateDuDocumentFormattee = df.format(d);

            d = JadeDateUtil.getGlobazDate(demande.getDateDepot());
            dateDepotDemande = df.format(d);

        } else {
            dateDuDocumentFormattee = dateDuDocument;
            dateDepotDemande = demande.getDateDepot();
        }
    }

    @Override
    protected void genererDocument() throws Exception {

        JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

        // chargement de l'agence communale seulement si on doit faire une copie pour elle
        if (faireUneCopiePourAgenceCommunale) {
            getTiersAdministration();
        }

        // Récupération du nss/nom+prenom de l'assuré, pour l'afficher dans le mail
        String infoAssure = demande.getRequerant().getNss() + " / " + demande.getRequerant().getNom() + " "
                + demande.getRequerant().getPrenom();

        JadePublishDocumentInfo publishDestination = JadePublishDocumentInfoProvider.newInstance(this);
        publishDestination.setOwnerEmail(getAdresseEmail());
        publishDestination.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getAdresseEmail());
        publishDestination.setDocumentTitle(getSession().getLabel("LETTRE_CONFIRMATION_AJOURNEMENT_TITRE_MAIL")
                + infoAssure);
        publishDestination.setDocumentSubject(getSession().getLabel("LETTRE_CONFIRMATION_AJOURNEMENT_TITRE_MAIL")
                + infoAssure);
        publishDestination.setDocumentNotes(getSession().getLabel("LETTRE_CONFIRMATION_AJOURNEMENT_SUBJECT_MAIL")
                + infoAssure);
        publishDestination.setPublishDocument(true);
        publishDestination.setArchiveDocument(false);
        publishDestination.setDocumentType(IRENoDocumentInfoRom.LETTRE_CONFIRMATION_AJOURNEMENT);
        publishDestination.setDocumentTypeNumber(IRENoDocumentInfoRom.LETTRE_CONFIRMATION_AJOURNEMENT);
        publishDestination.setDocumentProperty(REGedUtils.PROPRIETE_GED_TYPE_DEMANDE_RENTE,
                REGedUtils.getCleGedPourTypeRente(getSession(), REGedUtils.getTypeRentePourCetteDemandeRente(demande)));

        allDoc.setMergedDocDestination(publishDestination);

        creerDocument(allDoc, false);

        // Gestion des copies
        if (faireUneCopiePourAgenceCommunale) {

            if (REProperties.LETTRE_EN_TETE_DECISION_AJOURNEMENT.getBooleanValue()) {
                // Création de la page de garde pour la copie envoyée à la commune d'origine
                JadePublishDocumentInfo pageDeGardeInfo = JadePublishDocumentInfoProvider.newInstance(this);
                pageDeGardeInfo.setOwnerEmail(getAdresseEmail());
                pageDeGardeInfo.setDocumentDate(dateDuDocument);
                pageDeGardeInfo.setPublishDocument(false);
                pageDeGardeInfo.setArchiveDocument(false);

                DocumentData pageDeGardeData = createPRLettreEnTeteOO(getSession(), tierAdministration, null,
                        dateDuDocument);
                allDoc.addDocument(pageDeGardeData, pageDeGardeInfo);
            }

            creerDocument(allDoc, true);

        }
        this.createDocuments(allDoc);
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("LETTRE_CONFIRMATION_AJOURNEMENT_TITRE_MAIL");
    }

    /**
     * Recherche le code de l'administration ou est domicilié le tier principale en fonction d'un type de lien 507007
     * 
     * @param transaction
     * @param session
     * @param idTierPrincipale
     * @return IdTier de la commune ou est établis l'idTier principal
     * @throws Exception
     */
    private String getIdTiersAdministrationComunale(final BTransaction transaction, final BSession session,
            final String idTierPrincipale) throws Exception {

        String idTierEnfant = null;
        TICompositionTiersManager compTiersMgr = new TICompositionTiersManager();
        compTiersMgr.setForIdTiersParent(demande.getRequerant().getId().toString());
        compTiersMgr.setForTypeLien("507007");
        compTiersMgr.setSession(session);

        compTiersMgr.find(transaction);
        if (compTiersMgr.isEmpty()) {
            return null;
        }

        for (int i = 0; i < compTiersMgr.size(); i++) {
            TICompositionTiers entity = (TICompositionTiers) compTiersMgr.get(i);

            TIAdministrationViewBean administrationCommunale = new TIAdministrationViewBean();
            administrationCommunale.setSession(session);
            administrationCommunale.setIdTiersAdministration(entity.getIdTiersEnfant());
            administrationCommunale.retrieve(transaction);

            if (!administrationCommunale.isNew()) {
                idTierEnfant = entity.getIdTiersEnfant();
            }
        }
        return idTierEnfant;
    }

    @Override
    public String getName() {
        return getSession().getLabel("LETTRE_CONFIRMATION_AJOURNEMENT_SUBJECT_MAIL");
    }

    /**
     * Methode pour retourner la politesse du tiers, cas échéant son titre
     */
    private String getPolitesseTiers() throws Exception {

        StringBuilder titreTiers = new StringBuilder();
        ITITiers tiersCorrespondance = null;

        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put(ITITiers.FIND_FOR_IDTIERS, demande.getRequerant().getId().toString());

        tiersCorrespondance = (ITITiers) getSession().getAPIFor(ITITiers.class);

        ITITiers[] tiersHelper = tiersCorrespondance.findTiers(params);
        if ((tiersHelper != null) && (tiersHelper.length > 0)) {
            tiersCorrespondance = tiersHelper[0];
        }

        // Retourne la politesse du tiers si non vide
        if (tiersCorrespondance != null) {

            String csLangue = null;

            switch (Langues.getLangueDepuisCodeIso(codeIsoLangue)) {
                case Allemand:
                    csLangue = IConstantes.CS_TIERS_LANGUE_ALLEMAND;
                    break;

                case Francais:
                    csLangue = IConstantes.CS_TIERS_LANGUE_FRANCAIS;
                    break;

                case Italien:
                    csLangue = IConstantes.CS_TIERS_LANGUE_ITALIEN;
                    break;

                case Anglais:
                    csLangue = IConstantes.CS_TIERS_LANGUE_ANGLAIS;
                    break;

                case Romanche:
                    csLangue = IConstantes.CS_TIERS_LANGUE_ROMANCHE;
                    break;

                default:
                    break;
            }

            titreTiers.append(tiersCorrespondance.getFormulePolitesse(csLangue));
        } else {
            // Sinon, retourne le titre du tiers
            titreTiers.append(demande.getRequerant().getTitreTraduit(Langues.getLangueDepuisCodeIso(codeIsoLangue)));
        }

        return titreTiers.toString();
    }

    protected void getTiersAdministration() throws Exception {

        String idTierAdmin = null;
        idTierAdmin = getIdTiersAdministrationComunale(getTransaction(), getSession(), demande.getRequerant().getId()
                .toString());
        tierAdministration = PRTiersHelper.getAdministrationParId(getSession(), idTierAdmin);
        // Si on ne trouve pas l'admin, ce n'est pas normal ==> Exception
        if (tierAdministration == null) {
            throw new Exception(FWMessageFormat.format(getSession().getLabel("WARNING_AGENCE_COMM"), idTierAdmin));
        }

    }

    public boolean getMiseEnGed() {
        return miseEnGed;
    }

    private void loadTemplateDocument(final DocumentData docData) {
        docData.addData("idProcess", "REConfirmationAjournementOO");
        docData.addData("idEntete", "CAISSE");
        docData.addData("idSignature", "Signature_Caisse");
    }

    private void remplirCorpsDocument(final DocumentData docData) throws Exception {

        String titreTiers = getPolitesseTiers();

        // Insertion du numéro AVS assuré
        docData.addData(REConfirmationAjournementOO.TITRE_NSS, getTexte(catalogueLettreConfirmationAjournement, 1, 2));
        docData.addData(REConfirmationAjournementOO.NSS_ASSURE, demande.getRequerant().getNss().toString());

        // Insertion du titre du document
        docData.addData(REConfirmationAjournementOO.TITRE_DOCUMENT,
                getTexte(catalogueLettreConfirmationAjournement, 1, 1));

        // Insertion de la 'politesse' du tiers
        docData.addData(REConfirmationAjournementOO.TITRE_TIERS, PRStringUtils.replaceString(
                getTexte(catalogueLettreConfirmationAjournement, 1, 3), REConfirmationAjournementOO.POLITESSE_TIERS,
                titreTiers));

        // Insertion du paragraphe 1
        docData.addData(REConfirmationAjournementOO.PARAGRAPHE_1_DEBUT,
                getTexte(catalogueLettreConfirmationAjournement, 2, 1));
        docData.addData(REConfirmationAjournementOO.PARAGRAPHE_1_DATE_DEPOT, dateDepotDemande);
        docData.addData(REConfirmationAjournementOO.PARAGRAPHE_1_FIN,
                getTexte(catalogueLettreConfirmationAjournement, 2, 2));

        // Insertion du paragraphe 2
        docData.addData(REConfirmationAjournementOO.PARAGRAPHE_2,
                getTexte(catalogueLettreConfirmationAjournement, 3, 1));

        // Insertion du paragraphe 3
        docData.addData(REConfirmationAjournementOO.PARAGRAPHE_3_DEBUT,
                getTexte(catalogueLettreConfirmationAjournement, 4, 1));
        docData.addData(REConfirmationAjournementOO.PARAGRAPHE_3_GRAS,
                getTexte(catalogueLettreConfirmationAjournement, 4, 2));
        docData.addData(REConfirmationAjournementOO.PARAGRAPHE_3_FIN,
                getTexte(catalogueLettreConfirmationAjournement, 4, 3));

        // Insertion du paragraphe 4 (salutations)
        docData.addData(REConfirmationAjournementOO.PARAGRAPHE_4,
                getTexte(catalogueLettreConfirmationAjournement, 5, 1).replace("{politesse}", titreTiers));

        // Insertion de l'annexe en bas de page.

        docData.addData(REConfirmationAjournementOO.TITRE_ANNEXE,
                getTexte(catalogueLettreConfirmationAjournement, 6, 1));
        docData.addData(REConfirmationAjournementOO.VALEUR_ANNEXE,
                getTexte(catalogueLettreConfirmationAjournement, 6, 2));

        Collection tabCopie = new Collection("tabCopie");
        // Insertion de la copie à l'agence communale si besoin
        if (faireUneCopiePourAgenceCommunale) {
            DataList copieAgenceCommunale = new DataList("copie");

            copieAgenceCommunale.addData(REConfirmationAjournementOO.TITRE_COPIE,
                    getTexte(catalogueLettreConfirmationAjournement, 6, 3));
            copieAgenceCommunale.addData(
                    REConfirmationAjournementOO.VALEUR_COPIE,
                    tierAdministration.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tierAdministration.getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            tabCopie.add(copieAgenceCommunale);
        }
        docData.add(tabCopie);
    }

    private void remplirEnteteEtSignature(DocumentData docData, final boolean isCopie) throws Exception {

        ICaisseReportHelperOO caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(
                getSession().getApplication(), codeIsoLangue);
        caisseHelper.setTemplateName("RE_DECISION");

        CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

        crBean.setAdresse(adresseCourrier);

        // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du document
        if ("true".equals(getSession().getApplication().getProperty(REApplication.PROPERTY_DOC_CONFIDENTIEL))) {
            crBean.setConfidentiel(true);
        }

        // Ajoute dans l'entête de la lettre qui a traité le dossier si nécessaire
        if ("true".equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
            crBean.setNomCollaborateur(getSession().getUserFullName());
            crBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        }

        crBean.setDate(dateDuDocumentFormattee);

        if (isCopie) {
            docData = caisseHelper.addHeaderParameters(docData, crBean, true);
        } else {
            docData = caisseHelper.addHeaderParameters(docData, crBean, false);
        }
        docData = caisseHelper.addSignatureParameters(docData, crBean);
    }

    public void setAdresseCourrier(final String adresseCourrier) {
        this.adresseCourrier = adresseCourrier;
    }

    public void setCodeIsoLangue(final String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    public void setDateDuDocument(final String dateDuDocument) {
        this.dateDuDocument = dateDuDocument;
    }

    public void setDemande(final DemandeRente demande) {
        this.demande = demande;
    }

    public void setFaireUneCopiePourAgenceCommunale(final boolean faireUneCopiePourAgenceCommunale) {
        this.faireUneCopiePourAgenceCommunale = faireUneCopiePourAgenceCommunale;
    }

    public void setMiseEnGed(final boolean miseEnGed) {
        this.miseEnGed = miseEnGed;
    }
}
