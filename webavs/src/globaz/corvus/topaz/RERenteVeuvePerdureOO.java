package globaz.corvus.topaz;

import globaz.babel.utils.BabelContainer;
import globaz.babel.utils.CatalogueText;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater02;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.pyxis.api.ITITiers;
import java.util.Hashtable;
import ch.globaz.topaz.datajuicer.DocumentData;

public class RERenteVeuvePerdureOO {

    private Boolean annexeParDefaut = Boolean.FALSE;
    private String annexes = null;
    private BabelContainer babelContainer = null;
    private CatalogueText catalogueTextDecision = null;
    private CatalogueText catalogueTextRenteVeuvePerdure = null;
    private String codeIsoLangue = null;
    private Boolean copie = Boolean.FALSE;
    private DocumentData data = null;
    private String dateDebutRenteVieillesse = null;
    private String dateDocument = null;
    private String idDemandeRente = null;
    private FWCurrency montantRenteVeuve = null;
    private FWCurrency montantRenteVieillesse = null;
    private BSession session = null;
    private PRTiersWrapper tiers = null;
    /** Utilisé lorsqu'une copie est envoyée à la commune d'origine */
    private String tiersAdministrationCommunale;

    public RERenteVeuvePerdureOO(BSession session, String dateDocument, String idDemandeRente,
            String montantRenteVeuve, String montantRenteVieillesse, String dateDebutRenteVieillesse, String idTiers,
            boolean copie) throws Exception {
        super();

        this.session = session;

        this.copie = copie;

        tiers = PRTiersHelper.getTiersParId(session, idTiers);
        if (tiers != null) {
            codeIsoLangue = getSession().getCode(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
        }

        this.dateDocument = JACalendar.format(dateDocument, codeIsoLangue);
        this.dateDebutRenteVieillesse = JACalendar.format(dateDebutRenteVieillesse, codeIsoLangue);
        this.idDemandeRente = idDemandeRente;
        this.montantRenteVeuve = new FWCurrency(montantRenteVeuve);
        this.montantRenteVieillesse = new FWCurrency(montantRenteVieillesse);

        data = new DocumentData();

        babelContainer = new BabelContainer();
        catalogueTextDecision = new CatalogueText();
        catalogueTextRenteVeuvePerdure = new CatalogueText();

        if (this.session == null) {
            throw new NullPointerException("No session");
        }
        if (tiers == null) {
            throw new NullPointerException(session.getLabel("ERREUR_TIERS_OBLIGATOIRE"));
        }
        if (this.dateDocument == null) {
            throw new NullPointerException(session.getLabel("ERREUR_DATE_DOCUMENT_OBLIGATOIRE"));
        }
        if ((this.dateDebutRenteVieillesse == null) || (this.montantRenteVieillesse == null)) {
            throw new NullPointerException(session.getLabel("ERREUR_RENTE_VIEILLESSE_INTROUVABLE"));
        }
        if (this.montantRenteVeuve == null) {
            throw new NullPointerException(session.getLabel("ERREUR_RENTE_VEUVE_INTROUVABLE"));
        }
        if (this.montantRenteVeuve.doubleValue() < this.montantRenteVieillesse.doubleValue()) {
            throw new Exception(session
                    .getLabel("ERREUR_RENTE_VEUVE_PLUS_PETITE_RENTE_VIEILLESSE")
                    .replace("{montantRenteVeuve}", this.montantRenteVeuve.toStringFormat().replace(".00", ".-"))
                    .replace("{montantRenteVieillesse}",
                            this.montantRenteVieillesse.toStringFormat().replace(".00", ".-")));
        }
    }

    private void addData(String key, String value) {
        if (data == null) {
            data = new DocumentData();
        }
        data.addData(key, value);
    }

    private void chargementCatalogueTexte() throws Exception {

        catalogueTextDecision.setCodeIsoLangue(codeIsoLangue);
        catalogueTextDecision.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        catalogueTextDecision.setCsTypeDocument(IRECatalogueTexte.CS_DECISION);
        catalogueTextDecision.setNomCatalogue("openOffice");
        babelContainer.addCatalogueText(catalogueTextDecision);

        catalogueTextRenteVeuvePerdure.setCodeIsoLangue(codeIsoLangue);
        catalogueTextRenteVeuvePerdure.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        catalogueTextRenteVeuvePerdure.setCsTypeDocument(IRECatalogueTexte.CS_DECISION);
        catalogueTextRenteVeuvePerdure.setNomCatalogue("renteVeuvePerdure");
        babelContainer.addCatalogueText(catalogueTextRenteVeuvePerdure);

        babelContainer.setSession(getSession());
        babelContainer.load();
    }

    private void chargerDonneesLettre() throws Exception {
        data.addData("titre_document", babelContainer.getTexte(catalogueTextRenteVeuvePerdure, 1, 1));

        data.addData(
                "titre_tiers",
                babelContainer.getTexte(catalogueTextRenteVeuvePerdure, 1, 2).replace("{titreTiers}",
                        chargerTitreTiers()));
        data.addData(
                "paragraphe1",
                babelContainer
                        .getTexte(catalogueTextRenteVeuvePerdure, 2, 1)
                        .replace("{montantRenteVieillesse}",
                                montantRenteVieillesse.toStringFormat().replace(".00", ".-"))
                        .replace("{dateDebutRenteVieillesse}", dateDebutRenteVieillesse));
        data.addData("paragraphe2_partie1", babelContainer.getTexte(catalogueTextRenteVeuvePerdure, 3, 1));
        data.addData(
                "montant_rente_veuve",
                babelContainer.getTexte(catalogueTextRenteVeuvePerdure, 3, 2).replace("{montantRenteVeuve}",
                        montantRenteVeuve.toStringFormat().replace(".00", ".-")));
        data.addData("paragraphe2_partie2", babelContainer.getTexte(catalogueTextRenteVeuvePerdure, 3, 3));
        data.addData("paragraphe3", babelContainer.getTexte(catalogueTextRenteVeuvePerdure, 4, 1));
        data.addData("paragraphe4", babelContainer.getTexte(catalogueTextRenteVeuvePerdure, 5, 1));
        data.addData(
                "paragraphe5",
                babelContainer.getTexte(catalogueTextRenteVeuvePerdure, 6, 1).replace("{titreTiers}",
                        session.getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_TITRE))));
        if (codeIsoLangue.equalsIgnoreCase("de")) {
            data.addData("salutations_speciales", babelContainer.getTexte(catalogueTextRenteVeuvePerdure, 7, 1));
        }

        StringBuilder annexes = new StringBuilder();

        if (annexeParDefaut) {
            annexes.append(babelContainer.getTexte(catalogueTextRenteVeuvePerdure, 1, 5));
        }

        if (this.annexes.length() > 0) {
            if (annexes.length() > 0) {
                annexes.append("\n");
            }
            annexes.append(this.annexes);
        }

        if ((annexes != null) && (annexes.length() > 0)) {
            String titreAnnexes = babelContainer.getTexte(catalogueTextRenteVeuvePerdure, 1, 4);
            data.addData("titre_annexe", titreAnnexes);
            String spacer = getSpacer(15 - titreAnnexes.length());
            data.addData("espace_deux_points", spacer + ":");

            data.addData("annexes", annexes.toString());
        }

        if (!JadeStringUtil.isEmpty(tiersAdministrationCommunale)) {
            String titreCopie = babelContainer.getTexte(catalogueTextDecision, 8, 3);
            data.addData("titre_copie", titreCopie);
            String spacer = getSpacer(15 - titreCopie.length());

            data.addData("espace_deux_points2", spacer + " :");

            String adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), tiersAdministrationCommunale,
                    REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", new PRTiersAdresseCopyFormater02(), "");

            data.addData("copie_pour", adresse);
        }
    }

    private void chargerEnTeteSignature() throws Exception {
        ICaisseReportHelperOO caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(
                getSession().getApplication(), codeIsoLangue);

        CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

        REDemandeRente demandeRente = new REDemandeRente();
        demandeRente.setSession(getSession());
        demandeRente.setIdDemandeRente(idDemandeRente);
        demandeRente.retrieve();

        crBean.setAdresse(PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "",
                null, ""));

        // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du document
        if ("true".equals(getSession().getApplication().getProperty(REApplication.PROPERTY_DOC_CONFIDENTIEL))) {
            crBean.setConfidentiel(true);
        }

        // Ajoute dans l'entête de la lettre qui a traité le dossier si nécessaire
        if ("true".equals(getSession().getApplication().getProperty("isAfficherDossierTraitePar"))) {
            crBean.setNomCollaborateur(babelContainer.getTexte(catalogueTextRenteVeuvePerdure, 1, 3) + " "
                    + getSession().getUserFullName());
            crBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        }

        // Ajoute la date dans l'entête de la lettre sauf pour les documents IJ
        crBean.setDate(dateDocument);

        // Ajout du numéro NSS dans l'entête de la lettre
        crBean.setNoAvs(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

        caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                codeIsoLangue);
        caisseHelper.setTemplateName("RE_DECISION");

        data = caisseHelper.addHeaderParameters(data, crBean, isCopie());
        data = caisseHelper.addSignatureParameters(data, crBean);
    }

    private String chargerTitreTiers() throws Exception {
        // Insertion du titre du tiers
        ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
        Hashtable<String, String> params = new Hashtable<String, String>();

        params.put(ITITiers.FIND_FOR_IDTIERS, tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        ITITiers[] t = tiersTitre.findTiers(params);
        if ((t != null) && (t.length > 0)) {
            tiersTitre = t[0];
        }

        return tiersTitre.getFormulePolitesse(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
    }

    public void genererLettre() throws Exception {
        addData("idProcess", "RERenteVeuvePerdureOO");
        addData("idEntete", "CAISSE");
        addData("idSignature", "Signature_Caisse");

        try {
            chargementCatalogueTexte();
        } catch (Exception ex) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        }

        try {
            chargerDonneesLettre();
        } catch (Exception ex) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_DONNEES_LETTRE"), ex);
        }

        try {
            chargerEnTeteSignature();
        } catch (Exception ex) {
            throw new Exception(getSession().getLabel("ERREUR_ENTETE_SIGNATURE"), ex);
        }
    }

    public Boolean getAnnexeParDefaut() {
        return annexeParDefaut;
    }

    public String getAnnexes() {
        return annexes;
    }

    public Boolean getCopie() {
        return copie;
    }

    public DocumentData getData() {
        return data;
    }

    public BSession getSession() {
        return session;
    }

    private String getSpacer(int lenght) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < lenght) {
            sb.append(" ");
        }
        return sb.toString();
    }

    public boolean isAnnexeParDefaut() {
        return annexeParDefaut;
    }

    public boolean isCopie() {
        return copie;
    }

    public void setAnnexeParDefaut(Boolean annexeParDefaut) {
        this.annexeParDefaut = annexeParDefaut;
    }

    public void setAnnexes(String annexes) {
        this.annexes = annexes;
    }

    /**
     * <p>
     * <b> Par défaut aucune copie n'est envoyée.
     * </p>
     * </b>
     * <p>
     * Si une copie est envoyée à l'agence communale, il suffit de fournir le tier de l'administration communale et
     * l'inscription
     * 
     * </br><b> 'copie : Agence communale AVS UneVille, xxxx UneVille </b></br> sera ajoutée sous les annexes
     * 
     * @param tiersAdministrationCommunale
     *            Le tier de l'administration communale à qui la copie sera envoyée
     */
    public void setCopiePourAgenceAvs(String tiersAdministrationCommunale) {
        this.tiersAdministrationCommunale = tiersAdministrationCommunale;
    }
}
