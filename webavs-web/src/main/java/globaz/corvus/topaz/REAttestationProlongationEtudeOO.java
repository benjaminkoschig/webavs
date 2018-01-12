package globaz.corvus.topaz;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import ch.globaz.topaz.datajuicer.DocumentData;

public class REAttestationProlongationEtudeOO {

    private static final String CDT_BENEFICIAIRE = "{beneficiaire}";
    private static final String CDT_DATEECHEANCE = "{dateEcheance}";
    private static final String CDT_DATENAISSANCE = "{dateNaissance}";
    private static final String CDT_GENTREPREST = "{GenrePrestation}";
    private static final String CDT_MONTANTPREST = "{MontantPrestation}";
    private static final String CDT_TITRETIERS = "{titreTiers}";

    public static final String FICHIER_MODELE_ENTETE_CORVUS = "RE_LETTRE_ATTESTATION_PROLONGATION_ETUDE";

    private TIAdressePaiementData adr;
    private String adresseTiers;
    private ICaisseReportHelperOO caisseHelper;
    private String codeIsoLangue;
    private DocumentData data;
    private String dateEcheance;
    private ICTDocument document;
    private DocumentData documentData;
    private ICTDocument documentHelper;
    private StringBuffer errorBuffer = new StringBuffer("");
    private String idTiers;
    private RERenteAccJoinTblTiersJoinDemandeRente ra;
    private RERenteAccJoinTblTiersJoinDemandeRente ra2 = null;
    private BSession session;
    private PRTiersWrapper tiers;
    private PRTiersWrapper tiersAdresse;
    private PRTiersWrapper tiersBeneficiaire;
    private PRTiersWrapper tiersAdressePaiement;
    private String titreTiers;

    private void chargementCatalogueTexte() throws Exception {

        // Chargement du catalogue de texte

        if (tiersAdresse != null) {
            codeIsoLangue = getSession().getCode(tiersAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
        } else if (tiersAdressePaiement != null) {
            codeIsoLangue = getSession().getCode(tiersAdressePaiement.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
        } else {
            codeIsoLangue = getSession().getCode(tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
        }

        codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

        // creation du helper pour les catalogues de texte
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_ATTESTATION_PROLONGATION_ETUDE);
        documentHelper.setNom("openOffice");
        documentHelper.setDefault(Boolean.FALSE);
        documentHelper.setActif(Boolean.TRUE);
        documentHelper.setCodeIsoLangue(codeIsoLangue);

        ICTDocument[] documents = documentHelper.load();

        if ((documents == null) || (documents.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            document = documents[0];
        }
    }

    private void ChargementDonneesLettre() throws Exception {

        String concerne;

        // Traitement du concerne, j'insere les valeurs type de rente, montant, prenom/nom et date anniversaire en
        // fonction du sexe de l'enfant

        tiers = PRTiersHelper.getTiersParId(getSession(), getIdTiers());
        String nomBeneficiaire = tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " "
                + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM);
        concerne = buildDocumentObject(nomBeneficiaire, getRa());
        // dans le cas d'une seconde rente accordée pour la même adresse de paiement, on double l'objet
        if (getRa2() != null) {

            concerne += " / " + buildDocumentObject(nomBeneficiaire, getRa2());
        }

        data.addData("LETTRE_CONCERNE", concerne);

        if (codeIsoLangue.equals("DE")) {
            data.addData("titreTiers", titreTiers);
        } else {
            data.addData("titreTiers", titreTiers + ",");
        }

        data.addData("LETTRE_PARA1", document.getTextes(3).getTexte(1).getDescription());
        data.addData("LETTRE_PARA2", PRStringUtils.replaceString(document.getTextes(4).getTexte(1).getDescription(),
                REAttestationProlongationEtudeOO.CDT_DATEECHEANCE, getDateEcheance()));
        data.addData("LETTRE_PARA3", document.getTextes(5).getTexte(1).getDescription());
        data.addData("LETTRE_PARA4", document.getTextes(6).getTexte(1).getDescription());
        data.addData("LETTRE_PARA5", PRStringUtils.replaceString(document.getTextes(7).getTexte(1).getDescription(),
                REAttestationProlongationEtudeOO.CDT_TITRETIERS, titreTiers));

        setDocumentData(data);
    }

    private String buildDocumentObject(String nomBeneficiaire, RERenteAccJoinTblTiersJoinDemandeRente ra)
            throws Exception {
        String concerne;
        if (PRACORConst.CS_HOMME.equals(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE))) {
            concerne = PRStringUtils.replaceString(document.getTextes(2).getTexte(1).getDescription(),
                    REAttestationProlongationEtudeOO.CDT_BENEFICIAIRE, nomBeneficiaire);
        } else {
            concerne = PRStringUtils.replaceString(document.getTextes(2).getTexte(2).getDescription(),
                    REAttestationProlongationEtudeOO.CDT_BENEFICIAIRE, nomBeneficiaire);
        }

        String pourRechercheCodeSysteme = ra.getCodePrestation();

        if (JadeStringUtil.isEmpty(ra.getFractionRente())) {
            pourRechercheCodeSysteme += ".0";
        } else {
            pourRechercheCodeSysteme += "." + ra.getFractionRente();
        }

        // Recuperation du code système en fonction de codeIsoLangue et non en fonction de la langue de l'utilisateur
        FWParametersUserCode userCode = new FWParametersUserCode();
        userCode.setSession(getSession());
        userCode.setIdCodeSysteme(getSession().getSystemCode("REGENRPRST", pourRechercheCodeSysteme));

        if (codeIsoLangue.equals("IT")) {
            userCode.setIdLangue("I");
        } else if (codeIsoLangue.equals("DE")) {
            userCode.setIdLangue("D");
        } else {
            userCode.setIdLangue("F");
        }

        userCode.retrieve();

        concerne = PRStringUtils.replaceString(concerne, REAttestationProlongationEtudeOO.CDT_GENTREPREST,
                userCode.getLibelle());

        concerne = PRStringUtils.replaceString(concerne, REAttestationProlongationEtudeOO.CDT_MONTANTPREST,
                ra.getMontantPrestation());
        concerne = PRStringUtils.replaceString(concerne, REAttestationProlongationEtudeOO.CDT_DATENAISSANCE,
                JACalendar.format(ra.getDateNaissanceBenef(), codeIsoLangue));
        return concerne;
    }

    public void ChargementEnTeteEtSalutationLettre() {
        try {
            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            crBean.setAdresse(adresseTiers);

            crBean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), codeIsoLangue));

            // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du document
            if ("true".equals(getSession().getApplication().getProperty(REApplication.PROPERTY_DOC_CONFIDENTIEL))) {
                crBean.setConfidentiel(true);
            }

            BSession bs = new BSession();
            bs.setApplication("CORVUS");
            if (("true").equals(bs.getApplication().getProperty("isAfficherDossierTraitePar"))) {
                crBean.setNomCollaborateur(document.getTextes(1).getTexte(1).getDescription() + " "
                        + getSession().getUserFullName());
                crBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
            }

            // Ajout du numero NSS dans l'entête de la lettre
            crBean.setNoAvs(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName(REAttestationProlongationEtudeOO.FICHIER_MODELE_ENTETE_CORVUS);

            data.addData("idEntete", "CAISSE");

            data = caisseHelper.addSignatureParameters(data, crBean);

            caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

            setDocumentData(data);

        } catch (Exception e) {
            String msgError = getSession().getLabel("ERREUR_ENTETE_SIGNATURE");
            getErrorBuffer().append(msgError);
        }
    }

    public void chargerAdresseEtTitre(RERenteAccJoinTblTiersJoinDemandeRente ra) {

        try {
            BTransaction transaction = (BTransaction) getSession().newTransaction();

            tiersBeneficiaire = PRTiersHelper.getTiersParId(getSession(), ra.getIdTiersBeneficiaire());
            if (tiersBeneficiaire == null) {
                tiersBeneficiaire = PRTiersHelper.getAdministrationParId(getSession(), ra.getIdTiersBeneficiaire());
            }

            setTiersAdressePaiement(PRTiersHelper.getTiersParId(getSession(), ra.getIdTiersAdressePmt()));

            // Recherche d'une adresse pour le tiers beneficaire

            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            if (tiersAdressePaiement != null) {

                adresseTiers = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                        tiersAdressePaiement.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS),
                        REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

            } else {
                adresseTiers = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                        tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS),
                        REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");
            }

            // Si le tiers beneficiaire n'a pas d'adresse, je recherche toutes les rentes accordées dans un état valide
            // de la famille
            if (JadeStringUtil.isEmpty(adresseTiers)) {

                RERenteAccJoinTblTiersJoinDemRenteManager renteAccManager = new RERenteAccJoinTblTiersJoinDemRenteManager();
                renteAccManager.setSession(getSession());

                String navs = tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                if (navs.length() > 14) {
                    renteAccManager.setLikeNumeroAVSNNSS("true");
                } else {
                    renteAccManager.setLikeNumeroAVSNNSS("false");
                }
                // ce manager qui cherche dans l'idTiers parent en boucle !
                renteAccManager.setLikeNumeroAVS(navs);
                renteAccManager.wantCallMethodBeforeFind(true);
                renteAccManager.setRechercheFamille(true);
                renteAccManager.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE);
                renteAccManager.find();

                // Liste Pour les rentes accordées non principale
                List<RERenteAccJoinTblTiersJoinDemandeRente> listeMemeAdPmtNonPrincipale = new ArrayList<RERenteAccJoinTblTiersJoinDemandeRente>();

                for (int i = 0; i < renteAccManager.size(); i++) {
                    RERenteAccJoinTblTiersJoinDemandeRente elm = (RERenteAccJoinTblTiersJoinDemandeRente) renteAccManager
                            .get(i);

                    REInformationsComptabilite ic = new REInformationsComptabilite();
                    ic.setSession(getSession());
                    ic.setIdInfoCompta(ra.getIdInfoCompta());
                    ic.retrieve();

                    // Uniquement les rentes accorédes dont l'adresse de paiement est identique à l'écheance courante et
                    // sans date de fin de droit
                    if (elm.getIdTierAdressePmt().equals(ic.getIdTiersAdressePmt())
                            && JadeStringUtil.isEmpty(elm.getDateFinDroit())) {

                        RERenteAccordee rentea = new RERenteAccordee();
                        rentea.setSession(getSession());
                        rentea.setIdPrestationAccordee(elm.getIdPrestationAccordee());
                        rentea.retrieve();

                        // Si la rente est principale je l'utilise, sinon je l'insere dans la liste pour les rentes
                        // accordées non principale
                        if (rentea.getCodePrestation().equals("10") || rentea.getCodePrestation().equals("20")
                                || rentea.getCodePrestation().equals("13") || rentea.getCodePrestation().equals("23")
                                || rentea.getCodePrestation().equals("50") || rentea.getCodePrestation().equals("70")
                                || rentea.getCodePrestation().equals("72")) {

                            REInformationsComptabilite infoCompt = rentea.loadInformationsComptabilite();
                            tiersAdresse = PRTiersHelper.getTiersParId(getSession(), infoCompt.getIdTiersAdressePmt());
                            if (tiersAdresse == null) {
                                tiersAdresse = PRTiersHelper.getAdministrationParId(getSession(),
                                        infoCompt.getIdTiersAdressePmt());
                            }
                            // je recherche une adresse de courrier pour le tiers de l'adresse de paiement
                            adr = PRTiersHelper.getAdressePaiementData(getSession(), transaction,
                                    infoCompt.getIdTiersAdressePmt(),
                                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                                    JACalendar.todayJJsMMsAAAA());

                            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
                            // se trouvant dans le fichier corvus.properties
                            adresseTiers = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                                    adr.getIdTiers(), REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

                            // Si une adresse est trouvée, je recherche le titre du tiers del'adresse de paiement
                            if (!JadeStringUtil.isEmpty(adresseTiers)) {
                                ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
                                Hashtable<String, String> params = new Hashtable<String, String>();
                                params.put(ITITiers.FIND_FOR_IDTIERS, adr.getIdTiers());
                                ITITiers[] t = tiersTitre.findTiers(params);
                                if ((t != null) && (t.length > 0)) {
                                    tiersTitre = t[0];
                                }

                                titreTiers = tiersTitre.getFormulePolitesse(tiersAdresse
                                        .getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                                break;
                            }
                        } else {
                            listeMemeAdPmtNonPrincipale.add(ra);
                        }
                    }
                }

                // Si aucune adresse n'est trouvée, je recherche dans la liste des rentes accrodées non principale
                if (JadeStringUtil.isEmpty(adresseTiers)) {
                    if (!listeMemeAdPmtNonPrincipale.isEmpty()) {
                        for (RERenteAccordee renteac : listeMemeAdPmtNonPrincipale) {
                            REInformationsComptabilite infoCompt = renteac.loadInformationsComptabilite();
                            tiersAdresse = PRTiersHelper.getTiersParId(getSession(), infoCompt.getIdTiersAdressePmt());
                            if (tiersAdresse == null) {
                                tiersAdresse = PRTiersHelper.getAdministrationParId(getSession(),
                                        infoCompt.getIdTiersAdressePmt());
                            }

                            // je recherche une adresse de courrier pour le tiers de l'adresse de paiement

                            adr = PRTiersHelper.getAdressePaiementData(getSession(), transaction,
                                    infoCompt.getIdTiersAdressePmt(),
                                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                                    JACalendar.todayJJsMMsAAAA());

                            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
                            // se trouvant dans le fichier corvus.properties
                            adresseTiers = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                                    adr.getIdTiers(), REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

                            // Si une adresse est trouvée, je recherche le titre du tiers de l'adresse de paiement
                            if (!JadeStringUtil.isEmpty(adresseTiers)) {
                                ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
                                Hashtable<String, String> params = new Hashtable<String, String>();
                                params.put(ITITiers.FIND_FOR_IDTIERS, adr.getIdTiers());
                                ITITiers[] t = tiersTitre.findTiers(params);
                                if ((t != null) && (t.length > 0)) {
                                    tiersTitre = t[0];
                                }

                                titreTiers = tiersTitre.getFormulePolitesse(tiersAdresse
                                        .getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                                break;
                            }
                        }
                    }
                }
            } else {
                // Comme une adresse a été trouvée, je recherche le titre du tiers beneficiaire
                ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
                Hashtable<String, String> params = new Hashtable<String, String>();
                PRTiersWrapper tiersForTitre = tiersBeneficiaire;
                if (tiersAdressePaiement != null) {
                    tiersForTitre = tiersAdressePaiement;
                }
                params.put(ITITiers.FIND_FOR_IDTIERS, tiersForTitre.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                ITITiers[] t = tiersTitre.findTiers(params);
                if ((t != null) && (t.length > 0)) {
                    tiersTitre = t[0];
                }
                titreTiers = tiersTitre.getFormulePolitesse(tiersForTitre.getProperty(PRTiersWrapper.PROPERTY_LANGUE));

                if (JadeStringUtil.isEmpty(titreTiers)) {
                    TITiers tiers = new TITiers();
                    tiers.setSession(getSession());
                    tiers.setIdTiers(ra.getIdTiersAdressePmt());
                    tiers.retrieve();

                    TIAdresseDataSource Ads = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(),
                            true);

                    if (null != Ads) {
                        titreTiers = Ads.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_TITRE);
                    }
                }
            }

            // Si aucun titre n'est trouvé, je force un titre par défaut (celui de la caisse)
            if (JadeStringUtil.isEmpty(titreTiers)) {

                TIAdministrationManager tiAdminCaisseMgr = new TIAdministrationManager();
                tiAdminCaisseMgr.setSession(session);
                tiAdminCaisseMgr.setForCodeAdministration(CaisseHelperFactory.getInstance().getNoCaisseFormatee(
                        getSession().getApplication()));
                tiAdminCaisseMgr.setForGenreAdministration(CaisseHelperFactory.CS_CAISSE_COMPENSATION);
                tiAdminCaisseMgr.find();

                TIAdministrationViewBean tiAdminCaisse = (TIAdministrationViewBean) tiAdminCaisseMgr.getFirstEntity();

                String idTiersCaisse = "";
                if (tiAdminCaisse != null) {
                    idTiersCaisse = tiAdminCaisse.getIdTiersAdministration();
                }

                // retrieve du tiers
                PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(getSession(), idTiersCaisse);

                if (null == tier) {
                    tier = PRTiersHelper.getAdministrationParId(getSession(), idTiersCaisse);
                }

                // Insertion du titre du tiers
                ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
                Hashtable<String, String> params = new Hashtable<String, String>();
                params.put(ITITiers.FIND_FOR_IDTIERS, tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                ITITiers[] t = tiersTitre.findTiers(params);
                if ((t != null) && (t.length > 0)) {
                    tiersTitre = t[0];
                }
                titreTiers = tiersTitre.getFormulePolitesse(tier.getProperty(PRTiersWrapper.PROPERTY_LANGUE));

            }
        } catch (Exception e) {
            String msgError = getSession().getLabel("ERREUR_TIER_ADRESSE") + " "
                    + tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " "
                    + tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NOM);
            getErrorBuffer().append(msgError);
        }
    }

    public void generationLettre() throws Exception {

        data = new DocumentData();

        data.addData("idProcess", "REAttestationProlongationEtudetOO");

        chargerAdresseEtTitre(getRa());

        if (errorBuffer.length() == 0) {
            chargementCatalogueTexte();

            ChargementDonneesLettre();

            ChargementEnTeteEtSalutationLettre();

        }
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    public StringBuffer getErrorBuffer() {
        return errorBuffer;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public RERenteAccJoinTblTiersJoinDemandeRente getRa() {
        return ra;
    }

    public BSession getSession() {
        return session;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    public void setErrorBuffer(StringBuffer errorBuffer) {
        this.errorBuffer = errorBuffer;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setRa(RERenteAccJoinTblTiersJoinDemandeRente ra) {
        this.ra = ra;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public RERenteAccJoinTblTiersJoinDemandeRente getRa2() {
        return ra2;
    }

    public void setRa2(RERenteAccJoinTblTiersJoinDemandeRente ra2) {
        this.ra2 = ra2;
    }

    public PRTiersWrapper getTiersAdressePaiement() {
        return tiersAdressePaiement;
    }

    public void setTiersAdressePaiement(PRTiersWrapper tiersAdressePaiement) {
        this.tiersAdressePaiement = tiersAdressePaiement;
    }
}