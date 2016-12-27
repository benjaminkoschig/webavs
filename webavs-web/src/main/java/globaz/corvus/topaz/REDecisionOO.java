package globaz.corvus.topaz;

import globaz.babel.utils.CatalogueText;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.ordresversements.IRESoldePourRestitution;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.decisions.REAnnexeDecision;
import globaz.corvus.db.decisions.REAnnexeDecisionManager;
import globaz.corvus.db.decisions.RECopieDecision;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.decisions.REValidationDecisionsManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.demandes.REPeriodeInvaliditeManager;
import globaz.corvus.db.ordresversements.RECompensationInterDecisions;
import globaz.corvus.db.ordresversements.RECompensationInterDecisionsManager;
import globaz.corvus.db.ordresversements.REOrdreVersementJoinRenteVerseeATort;
import globaz.corvus.db.ordresversements.REOrdreVersementJoinRenteVerseeATortManager;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.REOrdresVersementsManager;
import globaz.corvus.db.ordresversements.RESoldePourRestitution;
import globaz.corvus.db.ordresversements.RESoldePourRestitutionManager;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.corvus.vb.decisions.KeyPeriodeInfo;
import globaz.corvus.vb.decisions.REBeneficiaireInfoVO;
import globaz.corvus.vb.decisions.RECopieDecisionListViewBean;
import globaz.corvus.vb.decisions.RECopieDecisionViewBean;
import globaz.corvus.vb.decisions.REDecisionInfoContainer;
import globaz.corvus.vb.decisions.REDecisionsContainer;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater02;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater03;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater05;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.api.ITIPersonne;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Création de la décision de rentes avec la solution Topaz
 * 
 * @author HPE
 */
public class REDecisionOO extends REAbstractJobOO {

    private static final String API_INV = "API-INV";
    private static final String API_AVS = "API-AVS";
    private static final String AVS_EXT = "AVS-EXT";
    private static final String AVS_ORD = "AVS-ORD";
    private static final String INV_EXT = "INV-EXT";
    private static final String INV_ORD = "INV-ORD";
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CDT_MONTANTARETENIR = "{montant}";
    private static final String REGEX_IBAN = "CH[0-9A-Z]{2}( [0-9A-Z]{4}){4} [0-9A-Z]";

    private ICaisseReportHelperOO caisseHelper;
    private CatalogueText catalogeDeTexteDecision;
    private CatalogueText catalogeDeTexteRemarquesDecision;
    private String codeIsoLangue;
    private RECopieDecision copieDecision;
    private DocumentData data;
    private String dateDocument;
    private REDecisionEntity decision;
    private REDecisionsContainer decisionsContainer;
    private REDemandeRente demandeRente;
    private DocumentData documentData;
    private String documentTypeNumber;
    private StringBuffer errorBuffer;
    private JADate firstDateDebutRADecision;
    private String idLastBCDecision;
    private String idTiersAdressePmt;
    private String idTiersSiAdresseVide;
    private boolean isAdresseCourrierDiffTiersDecision;
    private boolean isAdresseVide;
    private boolean isAnnexeBTA;
    private boolean isAvecMotivation;
    private boolean isCopie;
    private boolean isCopieFiscTronquee;
    private boolean isCopieOAI;
    public boolean isEnteteAI;
    private boolean isIdTiersBCEqualsIdTiersReqDemande;
    private JADate lastDateDebutRADecision;
    private BigDecimal montantCompensationDepuisCID;
    private BigDecimal montantTotal;
    public String officeAI;
    private final List<REOrdreVersementJoinRenteVerseeATort> ovCompenser;
    private final List<REOrdreVersementJoinRenteVerseeATort> rentesDejaVersees;
    private RESoldePourRestitution soldePourRestitution;
    private String texteCopie;
    private TIAdministrationViewBean tiAdministration;
    private PRTiersWrapper tiers;
    private String typeDecision;

    public REDecisionOO() {
        super(false);

        caisseHelper = null;
        codeIsoLangue = "FR";
        copieDecision = new RECopieDecision();
        data = null;
        dateDocument = "";
        decision = null;
        decisionsContainer = null;
        demandeRente = new REDemandeRente();
        documentData = null;
        documentTypeNumber = "";
        errorBuffer = new StringBuffer("");
        firstDateDebutRADecision = new JADate();
        idLastBCDecision = "";
        idTiersAdressePmt = "";
        idTiersSiAdresseVide = "";
        isAdresseCourrierDiffTiersDecision = false;
        isAdresseVide = false;
        isAnnexeBTA = false;
        isAvecMotivation = false;
        isCopie = false;
        isCopieFiscTronquee = false;
        isCopieOAI = false;
        isEnteteAI = false;
        isIdTiersBCEqualsIdTiersReqDemande = false;
        lastDateDebutRADecision = new JADate();
        montantCompensationDepuisCID = BigDecimal.ZERO;
        montantTotal = BigDecimal.ZERO;
        officeAI = "";
        ovCompenser = new ArrayList<REOrdreVersementJoinRenteVerseeATort>();
        rentesDejaVersees = new ArrayList<REOrdreVersementJoinRenteVerseeATort>();
        soldePourRestitution = null;
        texteCopie = "";
        tiAdministration = null;
        tiers = null;
        typeDecision = "";
    }

    public void chargerDonneesEnTete() throws Exception {

        CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

        caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                codeIsoLangue);
        caisseHelper.setTemplateName("RE_DECISION");

        // BZ 5220, recherche de l'adresse en cascade en fonction du paramètre isWantAdresseCourrier
        // se trouvant dans le fichier corvus.properties
        String adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), decision.getIdTiersAdrCourrier(),
                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

        if (!decision.getIdTiersAdrCourrier().equals(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))) {
            isAdresseCourrierDiffTiersDecision = true;
        }

        if (JadeStringUtil.isBlankOrZero(adresse)) {

            isAdresseVide = true;

            REValidationDecisionsManager mgr = new REValidationDecisionsManager();
            mgr.setSession(getSession());
            mgr.setForIdDecision(getDecision().getIdDecision());
            mgr.find();

            REPrestationDue pd = new REPrestationDue();
            pd.setSession(getSession());
            pd.setIdPrestationDue(((REValidationDecisions) mgr.getFirstEntity()).getIdPrestationDue());
            pd.retrieve();

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(getSession());
            ra.setIdPrestationAccordee(pd.getIdRenteAccordee());
            ra.retrieve();

            REInformationsComptabilite infoCom = new REInformationsComptabilite();
            infoCom.setSession(getSession());
            infoCom.setIdInfoCompta(ra.getIdInfoCompta());
            infoCom.retrieve();

            // BZ 5220, recherche de l'adresse en cascade en fonction du paramètre isWantAdresseCourrier
            // se trouvant dans le fichier corvus.properties
            adresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), infoCom.getIdTiersAdressePmt(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "", null, "");

            idTiersSiAdresseVide = infoCom.getIdTiersAdressePmt();

        }

        crBean.setAdresse(adresse);

        // Ajoute le libellé CONFIDENTIEL dans l'adresse de l'entête du document
        if ("true".equals(getSession().getApplication().getProperty(REApplication.PROPERTY_DOC_CONFIDENTIEL))) {
            crBean.setConfidentiel(true);
        }

        // Ajouter la mention traité par :
        if (getSession().getApplication().getProperty("isTraitePar").equals("true")) {
            try {
                JadeUser userName = JadeAdminServiceLocatorProvider.getLocator().getUserService()
                        .loadForVisa(decision.getTraitePar());

                String user = userName.getFirstname() + " " + userName.getLastname();
                crBean.setNomCollaborateur(getTexte(catalogeDeTexteDecision, 1, 7) + " " + user);
                crBean.setTelCollaborateur(userName.getPhone());
            } catch (Exception e) {
                if (!isCopie()) {
                    String msgError = "Erreur : La personne de référence n'est pas saisie";
                    errorBuffer.append(msgError);
                }
            }
        }

        if (decision.getCsGenreDecision().equals(IREDecision.CS_GENRE_DECISION_COMMUNICATION)) {
            data.addData("enTeteDesDeuxiemePage",
                    getTexte(catalogeDeTexteDecision, 1, 5) + " " + JACalendar.format(getDateDocument(), codeIsoLangue));

        } else if (decision.getCsGenreDecision().equals(IREDecision.CS_GENRE_DECISION_DECISION_SUR_OPPOSITION)) {
            data.addData("enTeteDesDeuxiemePage",
                    getTexte(catalogeDeTexteDecision, 1, 6) + " " + JACalendar.format(getDateDocument(), codeIsoLangue));
        } else {
            data.addData("enTeteDesDeuxiemePage",
                    getTexte(catalogeDeTexteDecision, 1, 2) + " " + JACalendar.format(getDateDocument(), codeIsoLangue));
        }

        data.addData(
                "detailAssureEnTete",
                PRStringUtils.replaceString(
                        getTexte(catalogeDeTexteDecision, 9, 3),
                        "{nssNomPrenomPage2}",
                        tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " - "
                                + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM)));

        data.addData("enTetePageNb", getTexte(catalogeDeTexteDecision, 9, 5));

        // Afficher un en-tête ou un autre (AVS-AI ou normal)
        /*
         * Si AI --> En-tête AI Si API / AI --> En-tête AI Si API / AVS --> En-tête Caisse Si AVS --> En-tête Caisse Si
         * Survivant --> En-tête Caisse
         * 
         * + Si en-tête office AI copie automatique à la caisse + Si API / AVS copie automatique à l'office AI
         */

        if (typeDecision.startsWith("INV")) {
            isEnteteAI = true;
            isCopieFiscTronquee = true;

            REDemandeRenteInvalidite demInv = new REDemandeRenteInvalidite();
            demInv.setSession(getSession());
            demInv.setIdDemandeRente(demandeRente.getIdDemandeRente());
            demInv.retrieve();

            officeAI = demInv.getCodeOfficeAI();

            if (JadeStringUtil.isBlankOrZero(officeAI)) {

                Set<Long> keys = decisionsContainer.getDecisionIC().getIdsPrstDuesParIdsRA().keySet();

                for (Long idRA : keys) {
                    RERenteAccordee ra = new RERenteAccordee();
                    ra.setSession(getSession());
                    ra.setIdPrestationAccordee(idRA.toString());
                    ra.retrieve();

                    REBasesCalcul bc = new REBasesCalcul();
                    bc.setSession(getSession());
                    bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                    bc.retrieve();

                    officeAI = bc.getCodeOfficeAi();
                }
            }
        } else if (typeDecision.startsWith("API")) {

            REDemandeRenteAPI demAPI = new REDemandeRenteAPI();
            demAPI.setSession(getSession());
            demAPI.setIdDemandeRente(demandeRente.getIdDemandeRente());
            demAPI.retrieve();

            if (typeDecision.endsWith("INV")) {
                isEnteteAI = true;
            }

            officeAI = demAPI.getCodeOfficeAI();
            if (JadeStringUtil.isBlankOrZero(officeAI)) {

                Set<Long> keys = decisionsContainer.getDecisionIC().getIdsPrstDuesParIdsRA().keySet();

                for (Long idRA : keys) {
                    RERenteAccordee ra = new RERenteAccordee();
                    ra.setSession(getSession());
                    ra.setIdPrestationAccordee(idRA.toString());
                    ra.retrieve();

                    REBasesCalcul bc = new REBasesCalcul();
                    bc.setSession(getSession());
                    bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                    bc.retrieve();

                    officeAI = bc.getCodeOfficeAi();
                }
            }
        } else {
            isEnteteAI = false;
        }

        // Recherche de l'adresse selon code Administration
        String adresseOfficeAi = "";

        TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();
        tiAdministrationMgr.setSession(getSession());
        tiAdministrationMgr.setForCodeAdministration(officeAI);
        tiAdministrationMgr.setForGenreAdministration("509004");
        tiAdministrationMgr.find();

        tiAdministration = PRTiersHelper.resolveAdminFromTiersLanguage(tiers, tiAdministrationMgr);

        // BZ 5220, recherche de l'adresse en cascade en fonction du paramètre isWantAdresseCourrier
        // se trouvant dans le fichier corvus.properties
        adresseOfficeAi = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(), tiAdministration.getIdTiers(),
                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", new PRTiersAdresseCopyFormater03(),
                decision.getDateDecision());

        if (isEnteteAI) {

            // BZ 4820 - Spécifique CCJU
            // Si n° de caisse = 150 et officeAI = 350, en-tête spécifique OAIJU
            String noCaisse = CaisseHelperFactory.getInstance().getNoCaisse(getSession().getApplication());
            String noOfficeAI = officeAI;

            if (noCaisse.equals("150") && noOfficeAI.equals("350")) {
                data.addData("idEntete", "AVS_AI_SPEC");
            } else {
                data.addData("idEntete", "AVS_AI");
            }

            data.addData("idFooter", "AVS_AI");
            data.addData("HEADER_ADRESSE_CAISSE", adresseOfficeAi);
            caisseHelper.addHeaderParameters(data, crBean, new Boolean(isCopie()));

        } else {
            data.addData("idEntete", "CAISSE");
            data.addData("idFooter", "CAISSE");
            caisseHelper.addHeaderParameters(data, crBean, new Boolean(isCopie()));

            String signatureElectroniqueProperty = getSession().getApplication().getProperty(
                    "corvus.decision.rente.signature.electronique");
            if ((null != signatureElectroniqueProperty) && "true".equals(signatureElectroniqueProperty)) {
                // Si signature électronique, je redirige vers le modèle contenant la signature
                data.addData("idProcess", "REDecisionOOWithElectronicSignature");
            }
        }

    }

    public void chargerTypeDecision() throws Exception {

        // Définir le type de décision
        REDecisionInfoContainer decision = decisionsContainer.getDecisionIC();
        Set<KeyPeriodeInfo> keys = null;

        if ((decisionsContainer != null) && (decision != null)) {

            // On reprend toutes les clés de la map des bénéficiaires info
            keys = decision.getMapBeneficiairesInfo().keySet();

            if (keys != null) {
                Iterator<KeyPeriodeInfo> iter = keys.iterator();

                firstDateDebutRADecision = new JADate("31.12.9999");

                // Pour chaque clé (ra), mais on passe une seule fois ! HACK pour gagner du temps
                int nbPassage = 0;

                REDemandeRenteJointDemande demJointDem = new REDemandeRenteJointDemande();
                demJointDem.setSession(getSession());
                demJointDem.setIdDemandeRente(decisionsContainer.getIdDemandeRente());
                demJointDem.retrieve();

                while ((iter != null) && iter.hasNext() && (nbPassage == 0)) {

                    KeyPeriodeInfo keyPeriode = iter.next();
                    REBeneficiaireInfoVO[] benefs = decision.getBeneficiaires(keyPeriode);

                    for (int inc = 0; inc < benefs.length; inc++) {

                        RERenteAccordee ra = new RERenteAccordee();
                        ra.setSession(getSession());
                        ra.setIdPrestationAccordee(benefs[inc].getIdRenteAccordee().toString());
                        ra.retrieve();

                        int genrePrestation = Integer.parseInt(ra.getCodePrestation());
                        // Résolution du type de rente
                        // AI
                        if ((genrePrestation >= 50) && (genrePrestation <= 59)) {
                            typeDecision = INV_ORD;
                        } else if ((genrePrestation >= 70) && (genrePrestation <= 79)) {
                            typeDecision = INV_EXT;
                            // AVS
                        } else if (((genrePrestation >= 10) && (genrePrestation <= 19))
                                || ((genrePrestation >= 30) && (genrePrestation <= 39))) {
                            typeDecision = AVS_ORD;
                        } else if (((genrePrestation >= 20) && (genrePrestation <= 29))
                                || ((genrePrestation >= 40) && (genrePrestation <= 49))) {
                            typeDecision = AVS_EXT;
                            // API
                        } else if (((genrePrestation >= 85) && (genrePrestation <= 87))
                                || ((genrePrestation >= 94) && (genrePrestation <= 97)) || (genrePrestation == 89)) {
                            typeDecision = API_AVS;
                        } else {
                            typeDecision = API_INV;
                        }

                        // Inforom 529 : gestion du document type number en fonction du type de décision
                        if (((genrePrestation >= 50) && (genrePrestation <= 59))
                                || ((genrePrestation >= 70) && (genrePrestation <= 79))) {
                            documentTypeNumber = IRENoDocumentInfoRom.DECISION_DE_RENTES_INVALIDITE;
                        } else if (((genrePrestation >= 10) && (genrePrestation <= 12))
                                || ((genrePrestation >= 20) && (genrePrestation <= 22))
                                || ((genrePrestation >= 33) && (genrePrestation <= 36))
                                || ((genrePrestation >= 43) && (genrePrestation <= 46))) {
                            documentTypeNumber = IRENoDocumentInfoRom.DECISION_DE_RENTES_VIEILLESSE;
                        } else if (((genrePrestation >= 13) && (genrePrestation <= 16))
                                || ((genrePrestation >= 23) && (genrePrestation <= 26))) {
                            documentTypeNumber = IRENoDocumentInfoRom.DECISION_DE_RENTES_SURVIVANT;
                            // API
                            // BZ 8325 API commencent au numéro 81
                        } else if (((genrePrestation >= 81) && (genrePrestation <= 97))) {
                            documentTypeNumber = IRENoDocumentInfoRom.DECISION_DE_RENTES_API;
                        } else {
                            // Dans le cas tout pourris ou l'on ne trouve pas le document type number, on met des 0
                            documentTypeNumber = "0000PRE";
                        }

                        nbPassage++;

                        if (ra.getIdTiersBaseCalcul().equals(demJointDem.getIdTiersRequerant())) {
                            isIdTiersBCEqualsIdTiersReqDemande = true;
                        }
                    }
                }
            }
        }

        // Charger la demande de rente
        REDecisionEntity decisions = new REDecisionEntity();
        decisions.setSession(getSession());
        decisions.setIdDecision(getDecision().getIdDecision());
        decisions.retrieve();

        demandeRente = new REDemandeRente();
        demandeRente.setSession(getSession());
        demandeRente.setIdDemandeRente(decisions.getIdDemandeRente());
        demandeRente.retrieve();

        // Charger motivations ou non
        // --> si avec motivation, jamais imprimer les moyens de droit (API ou AI)

        boolean isRetro = false;
        if (decisions.getCsTypeDecision().equals(IREDecision.CS_TYPE_DECISION_RETRO)) {
            isRetro = true;
        }

        if (typeDecision.startsWith("API")) {

            if (typeDecision.equals(API_INV)) {
                setCopieOAI(true);
            }

            REDemandeRenteAPI demandeApi = new REDemandeRenteAPI();
            demandeApi.setSession(getSession());
            demandeApi.setIdDemandeRente(getDecision().getIdDemandeRente());
            demandeApi.retrieve();

            if (!isRetro
                    && demandeApi.getCsGenrePrononceAI().equals(
                            IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_AVEC_MOTIVATION)) {
                isAvecMotivation = true;
            }
        }

        if (typeDecision.startsWith("INV")) {
            setCopieOAI(true);
            REDemandeRenteInvalidite demandeAI = new REDemandeRenteInvalidite();
            demandeAI.setSession(getSession());
            demandeAI.setIdDemandeRente(getDecision().getIdDemandeRente());
            demandeAI.retrieve();

            if (!isRetro
                    && demandeAI.getCsGenrePrononceAI().equals(
                            IREDemandeRente.CS_GENRE_PRONONCE_INVALIDITE_AVEC_MOTIVATION)) {

                // Si le requérant de la demande n'est pas égal au requérant de la base calcul, isAvecMotivation = false
                if (isIdTiersBCEqualsIdTiersReqDemande) {
                    isAvecMotivation = true;
                } else {
                    isAvecMotivation = false;
                }
            }
        }
    }

    @Override
    protected List<CatalogueText> definirCataloguesDeTextes() {

        // Retrieve d'informations pour la création de la décision
        String idTiersPrincipal = "";
        idTiersPrincipal = decision.getIdTiersBeneficiairePrincipal();
        try {
            tiers = PRTiersHelper.getTiersParId(getSession(), idTiersPrincipal);

            if (null == tiers) {
                tiers = PRTiersHelper.getAdministrationParId(getSession(), idTiersPrincipal);
            }
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        codeIsoLangue = getSession().getCode(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
        codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

        catalogeDeTexteDecision = new CatalogueText();
        catalogeDeTexteDecision.setCodeIsoLangue(codeIsoLangue);
        catalogeDeTexteDecision.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        catalogeDeTexteDecision.setCsTypeDocument(IRECatalogueTexte.CS_DECISION);
        catalogeDeTexteDecision.setNomCatalogue("openOffice");

        catalogeDeTexteRemarquesDecision = new CatalogueText();
        catalogeDeTexteRemarquesDecision.setCodeIsoLangue(codeIsoLangue);
        catalogeDeTexteRemarquesDecision.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        catalogeDeTexteRemarquesDecision.setCsTypeDocument(IRECatalogueTexte.CS_REMARQUES_DECISION);
        catalogeDeTexteRemarquesDecision.setNomCatalogue("openOffice");

        return Arrays.asList(catalogeDeTexteDecision, catalogeDeTexteRemarquesDecision);
    }

    @Override
    protected void genererDocument() throws Exception {

        // 1) Chargement informations principales
        data = new DocumentData();
        data.addData("idProcess", "REDecisionOO");
        chargerTypeDecision();

        // 2) Création des paramètres pour l'en-tête
        chargerDonneesEnTete();

        // 3) Remplissage des données de base
        remplirDonneesBase();

        // 4) Remplissage de la première partie de la décision
        remplirPremierePartie();

        // 5) Remplissage des rentes accordées
        remplirRentesAccordees();

        // 6) Remplissage de la partie "Versement"
        remplirDonneesVersement();

        // 7) Remplissage de la partie "Bases de calcul"
        if (!isCopie() || (isCopie() && getCopieDecision().getIsBaseCalcul().booleanValue())) {
            if (!decision.getCsTypeDecision().equals(IREDecision.CS_TYPE_DECISION_RETRO)) {
                remplirBasesCalcul();
            } else {
                Collection table = new Collection("baseCalcul");
                data.add(table);
            }
        } else {
            Collection table = new Collection("baseCalcul");
            data.add(table);
        }

        // 8) Remplissage de la partie "Décompte"
        if (!isCopie() || (isCopie() && getCopieDecision().getIsDecompte().booleanValue())) {
            remplirDecompte();
        } else {
            Collection table = new Collection("decompte");
            data.add(table);
        }

        // 9) Remplissage de la partie "Remarques"
        if (!isCopie() || (isCopie() && getCopieDecision().getIsRemarques().booleanValue())) {
            remplirRemarques();
        }

        // 10) Remplissage de la partie "Informations"
        remplirInformations();

        // 11) Remplissage de la partie "Obligation de payer des cotisations...."
        remplirObligationPayer();

        // 12) Remplissage de la partie "Moyens de droit"
        boolean isRemplirMoyensDeDroit = false;

        if (!isAvecMotivation && !isCopie()) {
            isRemplirMoyensDeDroit = true;
        }

        if (isCopie() && getCopieDecision().getIsMoyensDroit().booleanValue()) {
            isRemplirMoyensDeDroit = true;
        }

        if (isRemplirMoyensDeDroit) {
            remplirMoyensDroit();
        }

        // 13) Remplissage de la partie "Obligation de renseigner"
        if (!isAvecMotivation) {
            remplirObligationRenseigner();
        }

        // 14) Remplissage salutations et signature
        remplirSalutationsSignature();

        // 15) Remplissage des annexes et copies
        remplirAnnexesCopies();

        setDocumentData(data);

    }

    public RECopieDecision getCopieDecision() {
        return copieDecision;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public REDecisionEntity getDecision() {
        return decision;
    }

    public REDecisionsContainer getDecisionsContainer() {
        return decisionsContainer;
    }

    @Override
    public String getDescription() {
        return "Impression de la décision de rente";
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    /**
     * Retourne le document type number en fonction du type de décision.
     * 
     * @see IPRConstantesExternes.DECISION_RENTES_......._TYPE_NUMBER
     * @return le document type number
     */
    public String getDocumentTypeNumber() {
        return documentTypeNumber;
    }

    public StringBuffer getErrorBuffer() {
        return errorBuffer;
    }

    @Override
    public String getName() {
        return getSession().getLabel("MENU_OPTION_IMPRIMER_DECISION");
    }

    public String getTexteCopie() {
        return texteCopie;
    }

    public TIAdministrationViewBean getTiAdministration() {
        return tiAdministration;
    }

    public String getTypeDecision() {
        return typeDecision;
    }

    public boolean isCopie() {
        return isCopie;
    }

    public boolean isCopieFiscTronquee() {
        return isCopieFiscTronquee;
    }

    public boolean isCopieOAI() {
        return isCopieOAI;
    }

    /**
     * Methode de tri des bénéficiaires pour les lister par "genre" + "dateNaissance" dans le document
     * 
     * @param benefs
     */
    private REBeneficiaireInfoVO[] ordrerParGenreEtDateNaissance(final REBeneficiaireInfoVO[] benefs) {

        ArrayList<REBeneficiaireInfoVO> list = new ArrayList<REBeneficiaireInfoVO>();
        for (REBeneficiaireInfoVO vo : benefs) {
            list.add(vo);
        }

        // Tri des élément du tableau par genre de prestation et par date de naissance
        Collections.sort(list, new Comparator<REBeneficiaireInfoVO>() {

            @Override
            public int compare(final REBeneficiaireInfoVO o1, final REBeneficiaireInfoVO o2) {
                if (o1.getGenrePrestation().compareTo(o2.getGenrePrestation()) != 0) {
                    // Tri par genre de prestation
                    return o1.getGenrePrestation().compareTo(o2.getGenrePrestation());
                } else {
                    // Tri par date de naissance (année)
                    if (JadeDateUtil.isDateBefore(o1.getDateNaissanceBeneficiaire(), o2.getDateNaissanceBeneficiaire())) {
                        return -1;
                    } else if (JadeDateUtil.isDateAfter(o1.getDateNaissanceBeneficiaire(),
                            o2.getDateNaissanceBeneficiaire())) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        });

        return list.toArray(new REBeneficiaireInfoVO[list.size()]);

    }

    private void remplirAnnexesCopies() throws Exception {

        Collection newTable = new Collection("annexesCopies");
        DataList line1 = new DataList(null);

        boolean isTitreAnnexe = false;

        // Affichage des annexes sur la décision
        REAnnexeDecisionManager annMgr = new REAnnexeDecisionManager();
        annMgr.setSession(getSession());
        annMgr.setForIdDecision(getDecision().getIdDecision());
        annMgr.find(getSession().getCurrentThreadTransaction());

        // Affichage des copies sur la décision, on test si un des tiers en copie est décédé.
        // Si c'est le cas, le ne le met pas en copie
        List<RECopieDecisionViewBean> listeCopies = new ArrayList<RECopieDecisionViewBean>();
        RECopieDecisionListViewBean copieMgr1 = new RECopieDecisionListViewBean();
        copieMgr1.setSession(getSession());
        copieMgr1.setForIdDecision(getDecision().getIdDecision());
        copieMgr1.find(getSession().getCurrentThreadTransaction());

        for (int i = 0; i < copieMgr1.size(); i++) {
            RECopieDecisionViewBean copie = (RECopieDecisionViewBean) copieMgr1.get(i);
            PRTiersWrapper tw = PRTiersHelper.getTiersParId(getSession(), copie.getIdTiersCopie());
            if (tw != null) {
                // On ajoute le tiers uniquement s'il n'est pas décédé
                if (!JadeDateUtil.isGlobazDate(tw.getDateDeces())) {
                    copie.setIdProvisoire(copie.getIdDecisionCopie());
                    listeCopies.add(copie);
                }
            } else {
                // Si on n'arrive pas à récupérer le tiers, cela ne signifie pas forcément qu'il n'existe pas
                // Les administrations n'ont pas de n° AVS et du coup ne sont pas retourné par la méthode
                // getTiersParId
                copie.setIdProvisoire(copie.getIdDecisionCopie());
                listeCopies.add(copie);
            }
        }
        // -------------------------------------

        int nbAnnexes = 0;

        if (isAnnexeBTA) {
            nbAnnexes++;
        }

        StringBuilder valeurAnnexe = new StringBuilder();
        StringBuilder valeurCopie = new StringBuilder();

        if (!isCopie() || (isCopie() && copieDecision.getIsAnnexes().booleanValue())) {
            if (annMgr.size() > 0) {

                if ((annMgr.size() == 1) && ((annMgr.size() + nbAnnexes) == 1)) {
                    line1 = new DataList(null);
                    line1.addData("TITRE_ANNEXE_COPIE", getTexte(catalogeDeTexteDecision, 8, 1));
                    isTitreAnnexe = true;
                } else {
                    line1 = new DataList(null);
                    line1.addData("TITRE_ANNEXE_COPIE", getTexte(catalogeDeTexteDecision, 8, 2));
                    isTitreAnnexe = true;
                }

                for (int i = 0; i < annMgr.size(); i++) {
                    REAnnexeDecision annexe = (REAnnexeDecision) annMgr.get(i);
                    valeurAnnexe.append(annexe.getLibelle()).append("\r");
                }
            }

            if (isAnnexeBTA) {

                if (!isTitreAnnexe) {
                    line1 = new DataList(null);
                    line1.addData("TITRE_ANNEXE_COPIE", getTexte(catalogeDeTexteDecision, 8, 1));
                    isTitreAnnexe = true;
                }

                valeurAnnexe.append(getTexte(catalogeDeTexteDecision, 8, 5)).append("\r");
            }

            if (valeurAnnexe.length() > 0) {
                line1.addData("VALEUR_ANNEXE_COPIE", valeurAnnexe.toString());
                newTable.add(line1);
            }

        }

        if (!isCopie() || (isCopie() && copieDecision.getIsCopies().booleanValue())) {
            if (listeCopies.size() > 0) {

                if (listeCopies.size() == 1) {
                    line1 = new DataList(null);
                    line1.addData("TITRE_ANNEXE_COPIE", getTexte(catalogeDeTexteDecision, 8, 3));
                } else {
                    line1 = new DataList(null);
                    line1.addData("TITRE_ANNEXE_COPIE", getTexte(catalogeDeTexteDecision, 8, 4));
                }

                for (int i = 0; i < listeCopies.size(); i++) {
                    RECopieDecisionViewBean copie = listeCopies.get(i);

                    // chargement de la ligne de copie avec le formater

                    // BZ 5220, recherche de l'adresse en cascade en fonction du paramètre isWantAdresseCourrier
                    // se trouvant dans le fichier corvus.properties
                    String tiersAdresse = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                            copie.getIdTiersCopie(), REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "",
                            new PRTiersAdresseCopyFormater02(), decision.getDateDecision());

                    valeurCopie.append(tiersAdresse).append("\r");
                }

                if (valeurCopie.length() > 0) {
                    line1.addData("VALEUR_ANNEXE_COPIE", valeurCopie.toString());
                    newTable.add(line1);
                }

            }
        }

        data.add(newTable);
    }

    private void remplirBasesCalcul() throws Exception {

        Collection newTable = new Collection("baseCalcul");

        if (!decision.getCsGenreDecision().equals(IREDecision.CS_GENRE_DECISION_COMMUNICATION)) {

            // Si API
            if (typeDecision.startsWith("API")) {

            } else if (typeDecision.endsWith("ORD")) {

                data.addData("TITRE_BASE_CALCUL", getTexte(catalogeDeTexteDecision, 4, 1));

                DataList line1 = new DataList("bc");

                // Retrieve de la base de calcul
                String dureeRevenuAnnuelMoyen = "";
                String montantRAM = "";

                REBasesCalcul baseCalcul = new REBasesCalcul();
                REBasesCalcul bc = new REBasesCalcul();
                bc.setSession(getSession());
                bc.setIdBasesCalcul(idLastBCDecision);
                bc.retrieve();

                dureeRevenuAnnuelMoyen = bc.getDureeRevenuAnnuelMoyen();
                montantRAM = bc.getRevenuAnnuelMoyen();
                baseCalcul = bc;

                // Revenu annuel moyen déterminant basé sur X années et X mois de cotisations
                // Format dans les bases de calcul : AA.MM
                String nbAnnees = dureeRevenuAnnuelMoyen.length() == 5 ? dureeRevenuAnnuelMoyen.substring(0, 2) : "0";
                String nbMois = dureeRevenuAnnuelMoyen.length() == 5 ? dureeRevenuAnnuelMoyen.substring(3) : "0";

                if (Integer.parseInt(nbAnnees) < 10) {
                    nbAnnees = String.valueOf(Integer.parseInt(nbAnnees));
                }

                if (Integer.parseInt(nbMois) < 10) {
                    nbMois = String.valueOf(Integer.parseInt(nbMois));
                }

                String nbAnneesMois = "";

                if (!nbAnnees.equals("0") && !nbAnnees.equals("00")) {
                    nbAnneesMois = nbAnnees + " " + getTexte(catalogeDeTexteDecision, 4, 12);

                    if (!nbMois.equals("0") && !nbMois.equals("00")) {
                        nbAnneesMois += " " + getTexte(catalogeDeTexteDecision, 4, 18) + " ";

                        if (nbMois.equals("1") || nbMois.equals("01")) {
                            nbAnneesMois += nbMois + " " + getTexte(catalogeDeTexteDecision, 4, 19);
                        } else {
                            nbAnneesMois += nbMois + " " + getTexte(catalogeDeTexteDecision, 4, 14);
                        }

                    }

                } else if (!nbMois.equals("0") && !nbMois.equals("00")) {

                    if (nbMois.equals("1") || nbMois.equals("01")) {
                        nbAnneesMois += nbMois + " " + getTexte(catalogeDeTexteDecision, 4, 19);
                    } else {
                        nbAnneesMois += nbMois + " " + getTexte(catalogeDeTexteDecision, 4, 14);
                    }

                }

                if (nbAnneesMois.length() > 0) {
                    line1.addData("baseCalculLigne", PRStringUtils.replaceString(
                            getTexte(catalogeDeTexteDecision, 4, 3), "{nbAnneesMois}", nbAnneesMois));
                    line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                    line1.addData("montantValeur", JANumberFormatter.format(montantRAM));
                    newTable.add(line1);
                }

                // Nombre d'années prises en compte pour les tâches éducatives
                // Format : pris tel quel

                // 1) S'il y une valeur dans les champs getNombreAnneeBTE1() ou getNombreAnneeBTE2() ou
                // getNombreAnneeBTE4()
                // on additionne toutes les valeurs et on les ajoute dans le champ
                if (!JadeStringUtil.isBlankOrZero(baseCalcul.getNombreAnneeBTE1())
                        || !JadeStringUtil.isBlankOrZero(baseCalcul.getNombreAnneeBTE2())
                        || !JadeStringUtil.isBlankOrZero(baseCalcul.getNombreAnneeBTE4())) {

                    int nombre = 0;

                    if (!JadeStringUtil.isBlankOrZero(baseCalcul.getNombreAnneeBTE1())) {
                        nombre = nombre + Integer.parseInt(baseCalcul.getNombreAnneeBTE1());
                    }

                    if (!JadeStringUtil.isBlankOrZero(baseCalcul.getNombreAnneeBTE2())) {
                        nombre = nombre + Integer.parseInt(baseCalcul.getNombreAnneeBTE2());
                    }

                    if (!JadeStringUtil.isBlankOrZero(baseCalcul.getNombreAnneeBTE4())) {
                        nombre = nombre + Integer.parseInt(baseCalcul.getNombreAnneeBTE4());
                    }

                    line1 = new DataList("bc");
                    line1.addData("baseCalculLigne", getTexte(catalogeDeTexteDecision, 4, 4));
                    line1.addData("CHF", "");
                    line1.addData("montantValeur", String.valueOf(nombre));
                    newTable.add(line1);

                    // 2) Sinon, et s'il y a une valeur dans le champ getAnneeBonifTacheEduc(), on ajoute ce chiffre
                    // dans le champ
                } else if (!JadeStringUtil.isIntegerEmpty(baseCalcul.getAnneeBonifTacheEduc())) {

                    line1 = new DataList("bc");
                    line1.addData("baseCalculLigne", getTexte(catalogeDeTexteDecision, 4, 4));
                    line1.addData("CHF", "");
                    line1.addData("montantValeur", baseCalcul.getAnneeBonifTacheEduc());
                    newTable.add(line1);

                    // 3) Sinon, on ajoute rien...
                }

                // Nombre d'années prises en compte pour les tâches d'assistance
                // Format : pris tel quel
                if (!JadeStringUtil.isIntegerEmpty(baseCalcul.getAnneeBonifTacheAssistance())) {
                    line1 = new DataList("bc");
                    line1.addData("baseCalculLigne", getTexte(catalogeDeTexteDecision, 4, 5));
                    line1.addData("CHF", "");
                    line1.addData("montantValeur", baseCalcul.getAnneeBonifTacheAssistance());
                    newTable.add(line1);
                }

                // Durée de cotisations de la classe d'âge
                // Format : pris tel quel
                if (!JadeStringUtil.isBlankOrZero(baseCalcul.getAnneeCotiClasseAge())) {
                    if (Integer.parseInt(baseCalcul.getAnneeCotiClasseAge()) > 0) {
                        line1 = new DataList("bc");
                        line1.addData("baseCalculLigne", getTexte(catalogeDeTexteDecision, 4, 6));
                        line1.addData("CHF", "");
                        line1.addData("montantValeur", baseCalcul.getAnneeCotiClasseAge());
                        newTable.add(line1);
                    }
                }

                // Nombre d'années de cotisations prises en compte pour l'échelle
                // Format : Pour les deux champs (dureeAvant73 et dureeAprès73) : AA.MM
                if ((baseCalcul.getDureeCotiAvant73().length() == 5) && (baseCalcul.getDureeCotiDes73().length() == 5)) {

                    String valeurCalculee = "";
                    String nbAnneesAv73 = baseCalcul.getDureeCotiAvant73().length() == 5 ? baseCalcul
                            .getDureeCotiAvant73().substring(0, 2) : "0";
                    String nbMoisAv73 = baseCalcul.getDureeCotiAvant73().length() == 5 ? baseCalcul
                            .getDureeCotiAvant73().substring(3) : "0";
                    String nbAnneesAp73 = baseCalcul.getDureeCotiDes73().length() == 5 ? baseCalcul.getDureeCotiDes73()
                            .substring(0, 2) : "0";
                    String nbMoisAp73 = baseCalcul.getDureeCotiDes73().length() == 5 ? baseCalcul.getDureeCotiDes73()
                            .substring(3) : "0";

                    int nbAnneesTotal = Integer.parseInt(nbAnneesAv73) + Integer.parseInt(nbAnneesAp73);
                    int nbMoisTotal = Integer.parseInt(nbMoisAv73) + Integer.parseInt(nbMoisAp73);

                    if (nbMoisTotal > 11) {
                        int annees = nbMoisTotal / 12;
                        nbAnneesTotal = nbAnneesTotal + annees;
                        nbMoisTotal = nbMoisTotal - (annees * 12);
                    }

                    // Prendre en compte également les mois d'appoints
                    String nbMoisAppointAv73 = baseCalcul.getMoisAppointsAvant73();
                    String nbMoisAppointAp73 = baseCalcul.getMoisAppointsDes73();

                    int nbMoisAppointTotal = 0;

                    if (!JadeStringUtil.isBlankOrZero(nbMoisAppointAv73)) {
                        nbMoisAppointTotal += Integer.parseInt(nbMoisAppointAv73);
                    }

                    if (!JadeStringUtil.isBlankOrZero(nbMoisAppointAp73)) {
                        nbMoisAppointTotal += Integer.parseInt(nbMoisAppointAp73);
                    }

                    if (nbMoisAppointTotal > 11) {
                        int annees = nbMoisAppointTotal / 12;
                        nbAnneesTotal = nbAnneesTotal + annees;
                        nbMoisAppointTotal = nbMoisAppointTotal - (annees * 12);
                        nbMoisTotal = nbMoisTotal + nbMoisAppointTotal;
                        if (nbMoisTotal > 11) {
                            annees = nbMoisTotal / 12;
                            nbAnneesTotal = nbAnneesTotal + annees;
                            nbMoisTotal = nbMoisTotal - (annees * 12);
                        }
                    } else {
                        nbMoisTotal = nbMoisTotal + nbMoisAppointTotal;
                        if (nbMoisTotal > 11) {
                            int annees = nbMoisTotal / 12;
                            nbAnneesTotal = nbAnneesTotal + annees;
                            nbMoisTotal = nbMoisTotal - (annees * 12);
                        }
                    }

                    if (nbAnneesTotal > 0) {

                        String nbAnnees1 = "";

                        if (nbAnneesTotal < 10) {
                            nbAnnees1 += "0" + String.valueOf(nbAnneesTotal);
                        } else {
                            nbAnnees1 += String.valueOf(nbAnneesTotal);
                        }

                        valeurCalculee = nbAnnees1;
                    } else {
                        valeurCalculee = "00";
                    }

                    valeurCalculee += ".";

                    if (nbMoisTotal > 0) {

                        String nbMois1 = "";

                        if (nbMoisTotal < 10) {
                            nbMois1 += "0" + String.valueOf(nbMoisTotal);
                        } else {
                            nbMois1 += String.valueOf(nbMoisTotal);
                        }

                        valeurCalculee += nbMois1;
                    } else {
                        valeurCalculee += "00";
                    }

                    if (!valeurCalculee.equals("00.00")) {
                        line1 = new DataList("bc");
                        line1.addData("baseCalculLigne", getTexte(catalogeDeTexteDecision, 4, 7));
                        line1.addData("CHF", "");
                        line1.addData("montantValeur", valeurCalculee);
                        newTable.add(line1);
                    }

                }

                // Échelle de rente applicable
                // Format : tel quel
                if (!JadeStringUtil.isBlankOrZero(baseCalcul.getEchelleRente())) {
                    if (Integer.parseInt(baseCalcul.getEchelleRente()) > 0) {
                        line1 = new DataList("bc");
                        line1.addData("baseCalculLigne", getTexte(catalogeDeTexteDecision, 4, 8));
                        line1.addData("CHF", "");
                        line1.addData("montantValeur", baseCalcul.getEchelleRente());
                        newTable.add(line1);
                    }
                }

                // si AI
                if (typeDecision.startsWith("INV")) {

                    // Degré d'invalidité
                    REPeriodeInvaliditeManager periodeAIMgr = new REPeriodeInvaliditeManager();
                    periodeAIMgr.setSession(getSession());
                    periodeAIMgr.setForIdDemandeRente(demandeRente.getIdDemandeRente());
                    periodeAIMgr.setOrderBy(REPeriodeInvalidite.FIELDNAME_DATE_DEBUT_INVALIDITE);
                    periodeAIMgr.find(getSession().getCurrentThreadTransaction());

                    String degreInvalidite = "";

                    REBasesCalcul baseC = new REBasesCalcul();
                    baseC.setSession(getSession());
                    baseC.setIdBasesCalcul(idLastBCDecision);
                    baseC.retrieve();

                    if (!baseC.isNew()) {
                        degreInvalidite = baseC.getDegreInvalidite();
                    }

                    line1 = new DataList("bc");
                    line1.addData("baseCalculLigne", getTexte(catalogeDeTexteDecision, 4, 9));
                    line1.addData("CHF", "");
                    line1.addData("montantValeur", degreInvalidite + " %");
                    newTable.add(line1);
                }

                // Durant les années de mariage, les revenus des conjoints sont partagés
                if (baseCalcul.isRevenuSplitte().booleanValue()) {
                    line1 = new DataList("bc");
                    line1.addData("baseCalculLigne", getTexte(catalogeDeTexteDecision, 4, 10));
                    line1.addData("CHF", "");
                    line1.addData("montantValeur", "");
                    newTable.add(line1);
                }

                // Pour les anticipations et les ajournements --> Voir dans toutes les RA !!
                // Charger toutes les RAs
                // Toutes les clés représentent toutes les RA de la décision
                Set<Long> keys = getDecisionsContainer().getDecisionIC().getIdsPrstDuesParIdsRA().keySet();
                StringBuilder listeIdRAPourManager = new StringBuilder();

                for (Long idRA : keys) {
                    if (listeIdRAPourManager.length() > 0) {
                        listeIdRAPourManager.append(", ");
                    }
                    listeIdRAPourManager.append(idRA);
                }

                // Il nous faut la dernière RA pour les ajournements et anticipations

                RERenteAccordeeManager raMgr = new RERenteAccordeeManager();
                raMgr.setSession(getSession());
                raMgr.setForIdsRentesAccordees(listeIdRAPourManager.toString());
                raMgr.setOrderBy(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + " DESC");
                raMgr.find(getSession().getCurrentThreadTransaction());

                String dateDebutDerPeriode = ((RERenteAccordee) raMgr.getFirstEntity()).getDateDebutDroit();

                // Pour anticipation
                int nbAnneesAnticipation = 0;
                FWCurrency montantAnticipation = new FWCurrency(0);

                String pourcentAnticipation = "";
                // Explication pour le pour-cent d'anticipation
                //
                // Homme : si 1 an anticipation : 6,8%
                // si 2 ans anticipation : 13,6%
                //
                // Femme : si née en 1947 et avant :
                // --> si 1 an anticipation : 3,4%
                // --> si 2 ans anticipation : 6,8%
                //
                // si née en 1948 et après :
                // --> si 1 an anticipation : 6,8%
                // --> si 2 ans anticipation : 13,6%

                // Pour ajournement
                int nbMoisAjournement = 0;
                int nbAnneesAjournement = 0;
                FWCurrency montantAjournement = new FWCurrency(0);

                String idTierBC = "";
                RERenteAccordee rentePrincipale = null;

                for (int i = 0; i < raMgr.size(); i++) {
                    RERenteAccordee ra = (RERenteAccordee) raMgr.get(i);

                    // Rechercher la RA principale
                    if (ra.getCodePrestation().equals("10") || ra.getCodePrestation().equals("20")
                            || ra.getCodePrestation().equals("13") || ra.getCodePrestation().equals("23")
                            || ra.getCodePrestation().equals("50") || ra.getCodePrestation().equals("70")
                            || ra.getCodePrestation().equals("72")) {
                        rentePrincipale = ra;
                    }

                    // Seulement si dernière période !
                    if (ra.getDateDebutDroit().equals(dateDebutDerPeriode)) {
                        // Retrouver les infos pour les champs suivants (anticipation et ajournement)
                        if (!JadeStringUtil.isIntegerEmpty(ra.getAnneeAnticipation())) {
                            nbAnneesAnticipation = Integer.parseInt(ra.getAnneeAnticipation());
                            montantAnticipation.add(ra.getMontantReducationAnticipation());
                            idTierBC = ra.getIdTiersBaseCalcul();
                        }

                        if (!JadeStringUtil.isIntegerEmpty(ra.getDureeAjournement())) {
                            nbAnneesAjournement = Integer.parseInt(ra.getDureeAjournement().length() == 4 ? ra
                                    .getDureeAjournement().substring(0, 1) : "0");
                            nbMoisAjournement = Integer.parseInt(ra.getDureeAjournement().length() == 4 ? ra
                                    .getDureeAjournement().substring(2) : "0");
                            montantAjournement.add(ra.getSupplementAjournement());
                        }
                    }

                }

                if (nbAnneesAnticipation > 0) {
                    // Réduction pour anticipation de XX années : CHF XX.XX
                    line1 = new DataList("bc");

                    String nbAnneesMoisAnticipationValeur = "";
                    PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(), idTierBC);

                    if (null == tier) {
                        throw new Exception("Tiers introuvable pour id=" + idTierBC);
                    }

                    if (nbAnneesAnticipation == 1) {

                        // si homme : pour-cent = 6,8%
                        // si femme née en 1947 et avant = 3,4%
                        // si femme née en 1948 et après = 6,8%
                        if (tier.getProperty(PRTiersWrapper.PROPERTY_SEXE).equals(ITIPersonne.CS_HOMME)) {
                            pourcentAnticipation = "6,8";
                        } else if (tier.getProperty(PRTiersWrapper.PROPERTY_SEXE).equals(ITIPersonne.CS_FEMME)) {
                            JADate dateNaissance = new JADate(tier.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
                            if (dateNaissance.getYear() <= 1947) {
                                pourcentAnticipation = "3,4";
                            } else if (dateNaissance.getYear() >= 1948) {
                                pourcentAnticipation = "6,8";
                            }
                        }

                        nbAnneesMoisAnticipationValeur = String.valueOf(nbAnneesAnticipation);
                        nbAnneesMoisAnticipationValeur += " " + getTexte(catalogeDeTexteDecision, 4, 11);
                    } else if (nbAnneesAnticipation > 1) {

                        // si homme : pour-cent = 13,6%
                        // si femme née en 1947 et avant = 6,8%
                        // si femme née en 1948 et après = 13,6%
                        if (tier.getProperty(PRTiersWrapper.PROPERTY_SEXE).equals(ITIPersonne.CS_HOMME)) {
                            pourcentAnticipation = "13,6";
                        } else if (tier.getProperty(PRTiersWrapper.PROPERTY_SEXE).equals(ITIPersonne.CS_FEMME)) {
                            JADate dateNaissance = new JADate(tier.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
                            if (dateNaissance.getYear() <= 1947) {
                                pourcentAnticipation = "6,8";
                            } else if (dateNaissance.getYear() >= 1948) {
                                pourcentAnticipation = "13,6";
                            }
                        }

                        nbAnneesMoisAnticipationValeur = String.valueOf(nbAnneesAnticipation);
                        nbAnneesMoisAnticipationValeur += " " + getTexte(catalogeDeTexteDecision, 4, 12);
                    }

                    String ligneComplete = "";

                    if (null != rentePrincipale) {
                        if (!rentePrincipale.getDateDebutAnticipation().equals(rentePrincipale.getDateDebutDroit())) {
                            ligneComplete = PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 4, 21),
                                    "{nbAnnées}", nbAnneesMoisAnticipationValeur);
                        } else {
                            ligneComplete = PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 4, 13),
                                    "{nbAnnées}", nbAnneesMoisAnticipationValeur);

                            ligneComplete = PRStringUtils.replaceString(ligneComplete, "{nbPourcentage}",
                                    pourcentAnticipation);
                        }
                    } else {
                        ligneComplete = PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 4, 13),
                                "{nbAnnées}", nbAnneesMoisAnticipationValeur);

                        ligneComplete = PRStringUtils.replaceString(ligneComplete, "{nbPourcentage}",
                                pourcentAnticipation);
                    }

                    line1 = new DataList("bc");
                    line1.addData("baseCalculLigne", ligneComplete);
                    line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                    line1.addData("montantValeur", montantAnticipation.toStringFormat());
                    newTable.add(line1);
                }

                if ((nbMoisAjournement > 0) || (nbAnneesAjournement > 0)) {

                    String nbAnneesMoisAjournementValeur = "";

                    if (nbAnneesAjournement == 1) {
                        nbAnneesMoisAjournementValeur = String.valueOf(nbAnneesAjournement);
                        nbAnneesMoisAjournementValeur += " " + getTexte(catalogeDeTexteDecision, 4, 11);
                    } else if (nbAnneesAjournement > 1) {
                        nbAnneesMoisAjournementValeur = String.valueOf(nbAnneesAjournement);
                        nbAnneesMoisAjournementValeur += " " + getTexte(catalogeDeTexteDecision, 4, 12);
                    }

                    if (nbMoisAjournement > 0) {
                        nbAnneesMoisAjournementValeur += " " + String.valueOf(nbMoisAjournement);
                        if (nbMoisAjournement > 1) {
                            nbAnneesMoisAjournementValeur += " " + getTexte(catalogeDeTexteDecision, 4, 14);
                        } else {
                            nbAnneesMoisAjournementValeur += " " + getTexte(catalogeDeTexteDecision, 4, 19);
                        }

                    }

                    // Supplément pour ajournement de XX années XX mois compris dans le total mensuel
                    line1 = new DataList("bc");
                    line1.addData("baseCalculLigne", PRStringUtils.replaceString(
                            getTexte(catalogeDeTexteDecision, 4, 15), "{nbAnneesMois}", nbAnneesMoisAjournementValeur));
                    line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                    line1.addData("montantValeur", montantAjournement.toStringFormat());
                    newTable.add(line1);

                }
            } else if (typeDecision.endsWith("EXT")) {

                // ajouter une ligne fixe (4.20)
                data.addData("TITRE_BASE_CALCUL", getTexte(catalogeDeTexteDecision, 4, 1));

                // Ajout des lignes pour les autres types de rentes
                DataList line1 = new DataList("bc");
                line1.addData("baseCalculLigne", getTexte(catalogeDeTexteDecision, 4, 16));
                line1.addData("CHF", "");
                line1.addData("montantValeur", "");
                newTable.add(line1);

                // si AI
                if (demandeRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE)) {
                    // Degré d'invalidité

                    REPeriodeInvaliditeManager periodeAIMgr = new REPeriodeInvaliditeManager();
                    periodeAIMgr.setSession(getSession());
                    periodeAIMgr.setForIdDemandeRente(demandeRente.getIdDemandeRente());
                    periodeAIMgr.setOrderBy(REPeriodeInvalidite.FIELDNAME_DATE_DEBUT_INVALIDITE);
                    periodeAIMgr.find(getSession().getCurrentThreadTransaction());

                    String degreInvalidite = "";

                    REBasesCalcul baseC = new REBasesCalcul();
                    baseC.setSession(getSession());
                    baseC.setIdBasesCalcul(idLastBCDecision);
                    baseC.retrieve();

                    if (!baseC.isNew()) {
                        degreInvalidite = baseC.getDegreInvalidite();
                    }

                    line1 = new DataList("bc");
                    line1.addData("baseCalculLigne", getTexte(catalogeDeTexteDecision, 4, 17));
                    line1.addData("CHF", "");
                    line1.addData("montantValeur", degreInvalidite + " %");
                    newTable.add(line1);
                }

            }

        }

        data.add(newTable);

    }

    private void remplirDecompte() throws Exception {

        // Itération sur tous les ordres de versement pour chargement des données
        BigDecimal montantInteretsMoratoires = BigDecimal.ZERO;
        BigDecimal montantRentesDejaVersees = BigDecimal.ZERO;
        BigDecimal montantImpotSource = BigDecimal.ZERO;
        BigDecimal montantFactureACompenser = BigDecimal.ZERO;

        Map<TypeRenteVerseeATort, BigDecimal> montantParTypeRenteVerseeATort = new HashMap<TypeRenteVerseeATort, BigDecimal>();
        // contient la description à mettre dans le décompte comme clé
        Map<String, BigDecimal> montantPourLesRentesVerseesATortDeTypeSaisieManuelle = new HashMap<String, BigDecimal>();
        Map<Long, BigDecimal> creanciers = new HashMap<Long, BigDecimal>();

        // Retrieve des ordres de versements
        REOrdreVersementJoinRenteVerseeATortManager ovMgr = new REOrdreVersementJoinRenteVerseeATortManager();
        ovMgr.setSession(getSession());
        ovMgr.setForIdPrestation(Long.parseLong(getDecision().getPrestation(getSession().getCurrentThreadTransaction())
                .getIdPrestation()));
        ovMgr.find(getSession().getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);

        // si pas d'OV ou si date tout début dans le futur, pas de décompte
        JACalendar cal = new JACalendarGregorian();

        boolean isDepartFutur = false;

        Collection newTable = new Collection("decompte");
        DataList line1 = new DataList("ligneVide");

        if (cal.compare(firstDateDebutRADecision, new JADate(decision.getDateDecision())) == JACalendar.COMPARE_FIRSTUPPER) {
            isDepartFutur = true;
        }

        if (!ovMgr.isEmpty()) {
            if (!isDepartFutur) {

                data.addData("TITRE_DECOMPTE", getTexte(catalogeDeTexteDecision, 5, 1));

                REDecisionInfoContainer decisionInfoContainer = decisionsContainer.getDecisionIC();

                Set<KeyPeriodeInfo> keys = null;

                if ((decisionsContainer != null) && (decisionInfoContainer != null)) {

                    // On reprend toutes les clés de la map des bénéficiaires info
                    keys = decisionInfoContainer.getMapBeneficiairesInfo().keySet();

                    if (keys != null) {
                        Iterator<KeyPeriodeInfo> iter = keys.iterator();

                        // Pour chaque clé, on remplit les droits
                        while ((iter != null) && iter.hasNext()) {
                            KeyPeriodeInfo keyPeriode = iter.next();

                            String dateDebut = PRDateFormater.format_MMMYYYY(
                                    new JADate(PRDateFormater.convertDate_AAAAMM_to_MMAAAA(keyPeriode.dateDebut)),
                                    codeIsoLangue);

                            if (IREDecision.CS_TYPE_DECISION_RETRO.equals(decision.getCsTypeDecision())
                                    && JadeStringUtil.isEmpty(keyPeriode.dateFin)) {
                                keyPeriode.dateFin = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(decision
                                        .getDateFinRetro());
                            }

                            String dateFin = PRDateFormater.format_MMMYYYY(
                                    new JADate(PRDateFormater.convertDate_AAAAMM_to_MMAAAA(keyPeriode.dateFin)),
                                    codeIsoLangue);

                            if (JadeStringUtil.isEmpty(keyPeriode.dateFin)) {

                                // Si pas de date de fin, on multiplie le nombre de mois entre la date de début
                                // et le mois avant la date du dernier paiement, puis on détail le mois du dernier
                                // paiement

                                // mm.aaaa
                                String dateDernierPaiement = REPmtMensuel.getDateDernierPmt(getSession());

                                // On prend la plus petite date entre la date du dernier pmt et la date de validation
                                // !!!
                                if (!JadeStringUtil.isBlankOrZero(decision.getDateDecision())) {
                                    // mm.aaaa
                                    String dateDecision = PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(decision
                                            .getDateDecision());
                                    cal = new JACalendarGregorian();
                                    if (cal.compare(new JADate(dateDernierPaiement), new JADate(dateDecision)) == JACalendar.COMPARE_SECONDLOWER) {
                                        dateDernierPaiement = dateDecision;
                                    }
                                }

                                // mm.aaaa
                                String dateDepuis = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateDebut);
                                int nbMois = PRDateFormater.nbrMoisEntreDates(new JADate(dateDepuis), new JADate(
                                        dateDernierPaiement));

                                // on retire le mois en cours (mois du dernier paiement)
                                nbMois = nbMois - 1;

                                BigDecimal montantMensuel = BigDecimal.ZERO;

                                REBeneficiaireInfoVO[] benefs = decisionInfoContainer.getBeneficiaires(keyPeriode);
                                for (int inc = 0; inc < benefs.length; inc++) {
                                    montantMensuel = montantMensuel.add(new BigDecimal(benefs[inc].getMontant()));
                                }

                                if (nbMois == 0) {

                                    nbMois = 1;

                                    // On insert la ligne si le montant mensuel est supérieur a zero
                                    if (montantMensuel.compareTo(new BigDecimal(0)) > 0) {

                                        line1 = new DataList("lignePeriode");
                                        line1.addData("detailPeriode",
                                                PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 5, 3),
                                                        "{dateDebut}", PRDateFormater.format_MMMYYYY(new JADate(
                                                                dateDernierPaiement), codeIsoLangue)));

                                        // scr --> added
                                        if (!JadeStringUtil.isBlankOrZero(decision.getDateFinRetro())) {
                                            nbMois = PRDateFormater.nbrMoisEntreDates(
                                                    new JADate(PRDateFormater
                                                            .convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateDebut)),
                                                    new JADate(decision.getDateFinRetro()));

                                        }

                                        if (nbMois == 0) {
                                            nbMois = 1;
                                        }
                                        // scr >-- end add

                                        String nbMoisA = PRStringUtils.replaceString(
                                                getTexte(catalogeDeTexteDecision, 5, 13), "{nbMois}",
                                                String.valueOf(nbMois));

                                        line1.addData("nbMois", nbMoisA);
                                        line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                                        line1.addData("montantMensuel",
                                                new FWCurrency(montantMensuel.toString()).toStringFormat());

                                        line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                                        line1.addData("montantPeriode", new FWCurrency(montantMensuel.intValue()
                                                * nbMois).toStringFormat());
                                        montantTotal = montantTotal
                                                .add(montantMensuel.multiply(new BigDecimal(nbMois)));

                                        newTable.add(line1);
                                    }
                                } else if (nbMois > 0) {

                                    // 01.04.2008
                                    JADate dateFinAvantDernierPmt = new JADate(dateDernierPaiement);
                                    // 01.03.2008
                                    dateFinAvantDernierPmt = cal.addMonths(dateFinAvantDernierPmt, -1);
                                    // 31.03.2008
                                    String date = cal.lastInMonth(dateFinAvantDernierPmt.toString());

                                    String ligneDebutFin = PRStringUtils.replaceString(
                                            getTexte(catalogeDeTexteDecision, 5, 2), "{dateDebut}", dateDebut);

                                    ligneDebutFin = PRStringUtils.replaceString(ligneDebutFin, "{dateFin}",
                                            PRDateFormater.format_MMMYYYY(new JADate(date), codeIsoLangue));

                                    if (montantMensuel.compareTo(new BigDecimal(0)) > 0) {
                                        line1 = new DataList("lignePeriode");
                                        line1.addData("detailPeriode", ligneDebutFin);

                                        String nbMoisA = "";
                                        if (nbMois > 1) {
                                            nbMoisA = PRStringUtils.replaceString(
                                                    getTexte(catalogeDeTexteDecision, 5, 4), "{nbMois}",
                                                    String.valueOf(nbMois));
                                        } else {
                                            nbMoisA = PRStringUtils.replaceString(
                                                    getTexte(catalogeDeTexteDecision, 5, 13), "{nbMois}",
                                                    String.valueOf(nbMois));
                                        }

                                        line1.addData("nbMois", nbMoisA);
                                        line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                                        line1.addData("montantMensuel",
                                                new FWCurrency(montantMensuel.toString()).toStringFormat());

                                        line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                                        line1.addData("montantPeriode", new FWCurrency(montantMensuel.intValue()
                                                * nbMois).toStringFormat());
                                        montantTotal = montantTotal
                                                .add(montantMensuel.multiply(new BigDecimal(nbMois)));
                                        newTable.add(line1);

                                        line1 = new DataList("lignePeriode");
                                        line1.addData("detailPeriode",
                                                PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 5, 3),
                                                        "{dateDebut}", PRDateFormater.format_MMMYYYY(new JADate(
                                                                dateDernierPaiement), codeIsoLangue)));

                                        nbMoisA = PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 5, 13),
                                                "{nbMois}", "1");

                                        line1.addData("nbMois", nbMoisA);
                                        line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                                        line1.addData("montantMensuel",
                                                new FWCurrency(montantMensuel.toString()).toStringFormat());

                                        line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                                        line1.addData("montantPeriode",
                                                new FWCurrency(montantMensuel.intValue()).toStringFormat());
                                        montantTotal = montantTotal.add(montantMensuel);
                                        newTable.add(line1);
                                    }
                                }

                            } else {

                                String df = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateFin);
                                // On prend la plus petite date entre la date du dernier pmt et la date de validation
                                // !!!
                                if (!JadeStringUtil.isBlankOrZero(decision.getDateDecision())) {
                                    // mm.aaaa
                                    String dateDecision = PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(decision
                                            .getDateDecision());
                                    String dfp = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateFin);

                                    cal = new JACalendarGregorian();
                                    if (cal.compare(new JADate(dfp), new JADate(dateDecision)) == JACalendar.COMPARE_SECONDLOWER) {
                                        df = dateDecision;
                                    }
                                }

                                dateFin = PRDateFormater.format_MMMYYYY(
                                        new JADate(PRDateFormater.convertDate_MMxAAAA_to_MMAAAA(df)), codeIsoLangue);

                                BigDecimal montantMensuel = BigDecimal.ZERO;
                                REBeneficiaireInfoVO[] benefs = decisionInfoContainer.getBeneficiaires(keyPeriode);
                                for (int inc = 0; inc < benefs.length; inc++) {
                                    montantMensuel = montantMensuel.add(new BigDecimal(benefs[inc].getMontant()));
                                }

                                int nbMois = 0;

                                // On insert la ligne si le montant mensuel est supérieur a zero
                                if (montantMensuel.compareTo(new BigDecimal(0)) > 0) {

                                    line1 = new DataList("lignePeriode");

                                    String ligneDebutFin = PRStringUtils.replaceString(
                                            getTexte(catalogeDeTexteDecision, 5, 2), "{dateDebut}", dateDebut);

                                    ligneDebutFin = PRStringUtils.replaceString(ligneDebutFin, "{dateFin}", dateFin);

                                    line1.addData("detailPeriode", ligneDebutFin);

                                    nbMois = PRDateFormater.nbrMoisEntreDates(
                                            new JADate(PRDateFormater
                                                    .convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateDebut)), new JADate(
                                                    df));

                                    String nbMoisA = "";

                                    if (nbMois > 1) {
                                        nbMoisA = PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 5, 4),
                                                "{nbMois}", String.valueOf(nbMois));
                                    } else {
                                        nbMoisA = PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 5, 13),
                                                "{nbMois}", String.valueOf(nbMois));
                                    }

                                    line1.addData("nbMois", nbMoisA);
                                    line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                                    line1.addData("montantMensuel",
                                            new FWCurrency(montantMensuel.toString()).toStringFormat());

                                    line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                                    line1.addData(
                                            "montantPeriode",
                                            new FWCurrency(String.valueOf(montantMensuel.doubleValue()
                                                    * Double.parseDouble(String.valueOf(nbMois)))).toStringFormat());
                                    newTable.add(line1);
                                }
                                montantTotal = montantTotal.add(montantMensuel.multiply(new BigDecimal(String
                                        .valueOf(nbMois))));
                            }

                        }
                        for (REOrdreVersementJoinRenteVerseeATort ov : ovMgr.getContainerAsList()) {

                            switch (ov.getTypeOrdreVersement()) {

                                case INTERET_MORATOIRE:
                                    montantInteretsMoratoires = montantInteretsMoratoires.add(ov
                                            .getMontantCompenseOrdreVersement());
                                    break;

                                case DETTE:
                                    if (ov.isCompensationInterDecision()) {
                                        // si dette compensée inter-décision
                                        rentesDejaVersees.add(ov);
                                    } else if (ov.isCompense()) {
                                        if (ov.getIdRenteVerseeATort() != null) {
                                            // pour les saisies manuelles, traitement particulier car on doit se
                                            // rappeler de
                                            // la description que l'utilisateur a entré sur la rente versée à tort et
                                            // qui
                                            // doit figurer sur le décompte
                                            BigDecimal montantPourCeTypeDeRenteVerseeATort = BigDecimal.ZERO;
                                            if (TypeRenteVerseeATort.SAISIE_MANUELLE.equals(ov
                                                    .getTypeRenteVerseeATort())) {
                                                if (montantPourLesRentesVerseesATortDeTypeSaisieManuelle.containsKey(ov
                                                        .getDescriptionSaisieManuelleRenteVerseeATort())) {
                                                    montantPourCeTypeDeRenteVerseeATort = montantPourLesRentesVerseesATortDeTypeSaisieManuelle
                                                            .get(ov.getDescriptionSaisieManuelleRenteVerseeATort());
                                                }
                                                montantPourLesRentesVerseesATortDeTypeSaisieManuelle.put(ov
                                                        .getDescriptionSaisieManuelleRenteVerseeATort(),
                                                        montantPourCeTypeDeRenteVerseeATort.add(ov
                                                                .getMontantCompenseOrdreVersement()));
                                            } else {
                                                if (montantParTypeRenteVerseeATort.containsKey(ov
                                                        .getTypeRenteVerseeATort())) {
                                                    montantPourCeTypeDeRenteVerseeATort = montantParTypeRenteVerseeATort
                                                            .get(ov.getTypeRenteVerseeATort());
                                                }
                                                montantParTypeRenteVerseeATort.put(ov.getTypeRenteVerseeATort(),
                                                        montantPourCeTypeDeRenteVerseeATort.add(ov
                                                                .getMontantCompenseOrdreVersement()));
                                            }
                                        } else {
                                            montantRentesDejaVersees = montantRentesDejaVersees.add(ov
                                                    .getMontantCompenseOrdreVersement());
                                        }
                                        ovCompenser.add(ov);

                                        // BZ6338
                                        // si dette compensée inter-décision
                                        RECompensationInterDecisionsManager cmpMgr = new RECompensationInterDecisionsManager();
                                        cmpMgr.setSession(getSession());
                                        cmpMgr.setForIdOV(ov.getIdOrdreVersement().toString());
                                        cmpMgr.find();

                                        for (int j = 0; j < cmpMgr.size(); j++) {
                                            RECompensationInterDecisions cid = (RECompensationInterDecisions) cmpMgr
                                                    .getEntity(j);
                                            montantCompensationDepuisCID = montantCompensationDepuisCID
                                                    .add(new BigDecimal(cid.getMontant()));
                                        }
                                    }
                                    break;

                                case CREANCIER:
                                case ASSURANCE_SOCIALE:
                                    // on ajoute les lignes, seulement si montant > 0
                                    if (ov.getMontantCompenseOrdreVersement().compareTo(BigDecimal.ZERO) > 0) {
                                        BigDecimal montantPourCeCreancier = BigDecimal.ZERO;
                                        if (creanciers.containsKey(ov.getIdTiersOrdreVersement())) {
                                            montantPourCeCreancier = creanciers.get(ov.getIdTiersOrdreVersement());
                                        }
                                        creanciers.put(ov.getIdTiersOrdreVersement(),
                                                montantPourCeCreancier.add(ov.getMontantCompenseOrdreVersement()));
                                    }
                                    break;

                                case IMPOT_A_LA_SOURCE:
                                    montantImpotSource = montantImpotSource.add(ov.getMontantCompenseOrdreVersement());
                                    break;

                                case DETTE_RENTE_AVANCES:
                                case DETTE_RENTE_DECISION:
                                case DETTE_RENTE_PRST_BLOQUE:
                                case DETTE_RENTE_RESTITUTION:
                                case DETTE_RENTE_RETOUR:
                                    if (ov.isCompense()) {
                                        montantFactureACompenser = montantFactureACompenser.add(ov
                                                .getMontantCompenseOrdreVersement());
                                    }
                                    break;

                                default:
                                    break;
                            }
                        }

                        // Intérêts moratoires
                        if (montantInteretsMoratoires.compareTo(BigDecimal.ZERO) > 0) {
                            line1 = new DataList("ligneSupp");
                            line1.addData("detailDiminution", getTexte(catalogeDeTexteDecision, 5, 5));
                            line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                            line1.addData("montantDiminution",
                                    new FWCurrency(montantInteretsMoratoires.toString()).toStringFormat());
                            montantTotal = montantTotal.add(montantInteretsMoratoires);
                            newTable.add(line1);
                        }

                        // Si montant total intermédiaire = montant total
                        if ((montantParTypeRenteVerseeATort.size() == 0)
                                && (montantPourLesRentesVerseesATortDeTypeSaisieManuelle.size() == 0)
                                && (rentesDejaVersees.size() == 0)
                                && (montantRentesDejaVersees.compareTo(BigDecimal.ZERO) == 0)
                                && (creanciers.keySet().size() == 0)
                                && (montantImpotSource.compareTo(BigDecimal.ZERO) == 0)
                                && (montantFactureACompenser.compareTo(BigDecimal.ZERO) == 0)) {

                        } else {
                            // Montant total (intermédiaire)
                            if (montantTotal.compareTo(BigDecimal.ZERO) > 0) {
                                line1 = new DataList("lignePreTotal");
                                line1.addData("TEXTE_TOTAL", getTexte(catalogeDeTexteDecision, 5, 6));
                                line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                                line1.addData("montantPreTotal",
                                        new FWCurrency(montantTotal.toString()).toStringFormat());
                                newTable.add(line1);

                                line1 = new DataList("ligneVide");
                                line1.addData("VIDE", "");
                                newTable.add(line1);
                            }
                        }

                        // Rentes déjà versées

                        // 1) Traiter les rentes de type : Rentes versées à tort à [nomPrenom]
                        if (rentesDejaVersees.size() > 0) {
                            for (REOrdreVersementJoinRenteVerseeATort ov : rentesDejaVersees) {

                                String texte = getTexte(catalogeDeTexteDecision, 5, 15);

                                RECompensationInterDecisions compIntDec = new RECompensationInterDecisions();
                                compIntDec.setSession(getSession());
                                compIntDec
                                        .setAlternateKey(RECompensationInterDecisions.ALTERNATE_KEY_ID_OV_COMPENSATION);
                                compIntDec.setIdOVCompensation(ov.getIdOrdreVersement().toString());
                                compIntDec.retrieve();

                                REOrdresVersements ordreV = new REOrdresVersements();
                                ordreV.setSession(getSession());
                                ordreV.setIdOrdreVersement(compIntDec.getIdOrdreVersement());
                                ordreV.retrieve();

                                PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), ordreV.getIdTiers());

                                String nomPrenom = "";
                                if (null != tiers) {
                                    nomPrenom = tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                                }

                                // BZ 4944 - Si montant à zéro, ne pas insérer la ligne
                                FWCurrency mntLigne = new FWCurrency(compIntDec.getMontant());

                                if (!mntLigne.isZero()) {
                                    line1 = new DataList("ligneSupp");
                                    line1.addData("detailDiminution",
                                            PRStringUtils.replaceString(texte, "{nomPrenom}", nomPrenom));
                                    line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));

                                    BigDecimal montant = new BigDecimal(compIntDec.getMontant()).negate();

                                    line1.addData("montantDiminution",
                                            new FWCurrency(montant.toString()).toStringFormat());

                                    montantTotal = montantTotal.add(montant);
                                    newTable.add(line1);
                                }
                            }
                        }

                        // 2) Traiter les rentes de type : Rentes déjà versées
                        if (montantRentesDejaVersees.compareTo(BigDecimal.ZERO) > 0) {
                            line1 = new DataList("ligneSupp");
                            line1.addData("detailDiminution", getTexte(catalogeDeTexteDecision, 5, 7));
                            line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));

                            montantRentesDejaVersees = montantRentesDejaVersees.negate();

                            line1.addData("montantDiminution",
                                    new FWCurrency(montantRentesDejaVersees.toString()).toStringFormat());
                            montantTotal = montantTotal.add(montantRentesDejaVersees);
                            newTable.add(line1);
                        }

                        // pour chaque type de rente versée à tort (sauf saisie manuelle car traité de manière
                        // spécifique plus bas)
                        for (Entry<TypeRenteVerseeATort, BigDecimal> uneEntree : montantParTypeRenteVerseeATort
                                .entrySet()) {
                            line1 = new DataList("ligneSupp");

                            switch (uneEntree.getKey()) {

                                case AVANCE_DEJA_PERCUE:
                                    line1.addData("detailDiminution", getTexte(catalogeDeTexteDecision, 5, 20));
                                    break;

                                case DETTES:
                                    line1.addData("detailDiminution", getTexte(catalogeDeTexteDecision, 5, 17));
                                    break;

                                case PRESTATION_DEJA_VERSEE:
                                    line1.addData("detailDiminution", getTexte(catalogeDeTexteDecision, 5, 7));
                                    break;

                                case PRESTATION_EN_SUSPENS:
                                    line1.addData("detailDiminution", getTexte(catalogeDeTexteDecision, 5, 18));
                                    break;

                                case PRESTATION_NON_VERSEE:
                                    line1.addData("detailDiminution", getTexte(catalogeDeTexteDecision, 5, 21));
                                    break;

                                case PRESTATION_TOUCHEE_INDUMENT:
                                    line1.addData("detailDiminution", getTexte(catalogeDeTexteDecision, 5, 19));
                                    break;

                                default:
                                    break;
                            }

                            BigDecimal montantPourCeTypeDeRenteVerseeATort = uneEntree.getValue().negate();
                            line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                            line1.addData("montantDiminution",
                                    new FWCurrency(montantPourCeTypeDeRenteVerseeATort.toString()).toStringFormat());
                            montantTotal = montantTotal.add(montantPourCeTypeDeRenteVerseeATort);
                            newTable.add(line1);
                        }

                        for (Entry<String, BigDecimal> uneEntree : montantPourLesRentesVerseesATortDeTypeSaisieManuelle
                                .entrySet()) {
                            line1 = new DataList("ligneSupp");
                            line1.addData("detailDiminution", uneEntree.getKey());

                            BigDecimal montantPourCeTypeDeSaisieManuelleRenteVerseeATort = uneEntree.getValue()
                                    .negate();
                            line1.addData("montantDiminution", new FWCurrency(
                                    montantPourCeTypeDeSaisieManuelleRenteVerseeATort.toString()).toStringFormat());
                            line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                            montantTotal = montantTotal.add(montantPourCeTypeDeSaisieManuelleRenteVerseeATort);
                            newTable.add(line1);
                        }

                        // Voir s'il y a une restitution
                        RESoldePourRestitutionManager soldeMgr = new RESoldePourRestitutionManager();
                        soldeMgr.setSession(getSession());
                        soldeMgr.setForIdPrestation(getDecision().getPrestation(
                                getSession().getCurrentThreadTransaction()).getIdPrestation());
                        soldeMgr.find();

                        if (!soldeMgr.isEmpty()) {

                            // BZ 5340
                            for (int i = 0; i < soldeMgr.size(); i++) {
                                if (soldePourRestitution == null) {
                                    soldePourRestitution = (RESoldePourRestitution) soldeMgr.get(i);
                                } else {
                                    RESoldePourRestitution unSolde = (RESoldePourRestitution) soldeMgr.get(i);

                                    FWCurrency montantMensuelARetenir = new FWCurrency(
                                            soldePourRestitution.getMontantMensuelARetenir());
                                    montantMensuelARetenir.add(Double.parseDouble(unSolde.getMontantMensuelARetenir()));
                                    soldePourRestitution.setMontantMensuelARetenir(montantMensuelARetenir.toString());
                                }
                            }
                        }

                        // traiter les CID compensant cette décision
                        if (montantCompensationDepuisCID.compareTo(BigDecimal.ZERO) > 0) {

                            line1 = new DataList("ligneSupp");

                            // Création de la liste des personnes qui vont être ponctionnés pour le remboursement de
                            // la dette. Pour ça, on regarde pour chaque ov si une compensation inter-décision
                            // existe
                            // Attention : on ne veut pas voir 2 fois le même nom dans la liste des personnes qui
                            // vont compenser la dette
                            HashMap<String, String> personneSoumiseACompensationDette = new HashMap<String, String>();
                            for (REOrdreVersementJoinRenteVerseeATort ov : ovCompenser) {

                                RECompensationInterDecisionsManager compIntMgr = new RECompensationInterDecisionsManager();
                                compIntMgr.setSession(getSession());
                                compIntMgr.setForIdOV(ov.getIdOrdreVersement().toString());
                                compIntMgr.find();

                                //
                                for (int i = 0; i < compIntMgr.size(); i++) {
                                    RECompensationInterDecisions compInt = (RECompensationInterDecisions) compIntMgr
                                            .get(i);

                                    tiers = PRTiersHelper.getTiersParId(getSession(), compInt.getIdTiers());

                                    if (null != tiers) {
                                        // Si la personne est deja dans la map on ne la rajoute pas
                                        if (!personneSoumiseACompensationDette.containsKey(tiers.getIdTiers())) {
                                            personneSoumiseACompensationDette.put(
                                                    tiers.getIdTiers(),
                                                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                                        }
                                    }
                                }
                            }
                            // Génère la String qui contient les nom-prénom des personne qui vont compensser
                            String nomsPrenoms = "";
                            Iterator<String> it = personneSoumiseACompensationDette.values().iterator();
                            while (it.hasNext()) {
                                nomsPrenoms += it.next();
                                if (it.hasNext()) {
                                    nomsPrenoms += ", ";
                                }
                            }

                            String remarque = PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 5, 16),
                                    "{nomPrenom}", nomsPrenoms);

                            line1.addData("detailDiminution", remarque);
                            line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                            line1.addData("montantDiminution",
                                    new FWCurrency(montantCompensationDepuisCID.toString()).toStringFormat());
                            montantTotal = montantTotal.add(montantCompensationDepuisCID);
                            newTable.add(line1);
                        }

                        // Créanciers
                        Set<Long> keysCreanciers = creanciers.keySet();

                        boolean isLigneVideOK = false;

                        if (keysCreanciers.size() > 0) {
                            if (!isLigneVideOK) {
                                line1 = new DataList("ligneVide");
                                line1.addData("VIDE", "");
                                newTable.add(line1);
                                isLigneVideOK = true;
                            }

                            for (Long idTiersCreancier : keysCreanciers) {

                                // BZ 5220, recherche de l'adresse en cascade en fonction du paramètre
                                // isWantAdresseCourrier se trouvant dans le fichier corvus.properties
                                String nomCreancier = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                                        idTiersCreancier.toString(), REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "",
                                        new PRTiersAdresseCopyFormater05(), decision.getDateDecision());

                                BigDecimal montantCreancier = creanciers.get(idTiersCreancier).negate();

                                line1 = new DataList("ligneSupp");
                                line1.addData("detailDiminution", nomCreancier);
                                line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));

                                line1.addData("montantDiminution",
                                        new FWCurrency(montantCreancier.toString()).toStringFormat());

                                montantTotal = montantTotal.add(montantCreancier);
                                newTable.add(line1);

                            }
                        }

                        // Impôts à la source
                        if (montantImpotSource.compareTo(BigDecimal.ZERO) > 0) {

                            if (!isLigneVideOK) {
                                line1 = new DataList("ligneVide");
                                line1.addData("VIDE", "");
                                newTable.add(line1);
                                isLigneVideOK = true;
                            }

                            line1 = new DataList("ligneSupp");
                            line1.addData("detailDiminution", getTexte(catalogeDeTexteDecision, 5, 8));
                            line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                            montantImpotSource = montantImpotSource.negate();
                            line1.addData("montantDiminution",
                                    new FWCurrency(montantImpotSource.toString()).toStringFormat());
                            montantTotal = montantTotal.add(montantImpotSource);
                            newTable.add(line1);
                        }

                        // Facture à compenser
                        if (montantFactureACompenser.compareTo(BigDecimal.ZERO) > 0) {

                            if (!isLigneVideOK) {
                                line1 = new DataList("ligneVide");
                                line1.addData("VIDE", "");
                                newTable.add(line1);
                                isLigneVideOK = true;
                            }

                            line1 = new DataList("ligneSupp");
                            line1.addData("detailDiminution", getTexte(catalogeDeTexteDecision, 5, 9));
                            line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                            montantFactureACompenser = montantFactureACompenser.negate();
                            line1.addData("montantDiminution",
                                    new FWCurrency(montantFactureACompenser.toString()).toStringFormat());
                            montantTotal = montantTotal.add(montantFactureACompenser);
                            newTable.add(line1);
                        }

                        // Montant total (final)
                        line1 = new DataList("ligneTotalFinal");
                        switch (montantTotal.compareTo(BigDecimal.ZERO)) {
                            case 1:
                                // montant positif
                                line1.addData("TEXTE_TOT_FINAL", getTexte(catalogeDeTexteDecision, 5, 10));
                                break;
                            case 0:
                                // montant à zéro
                                line1.addData("TEXTE_TOT_FINAL", getTexte(catalogeDeTexteDecision, 5, 11));
                                break;
                            case -1:
                                // montant négatif
                                line1.addData("TEXTE_TOT_FINAL", getTexte(catalogeDeTexteDecision, 5, 12));
                                break;

                            default:
                                break;
                        }

                        line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                        line1.addData("montantTotalFinal", new FWCurrency(montantTotal.toString()).toStringFormat());

                        newTable.add(line1);
                    }
                }

            }
        }

        data.add(newTable);

    }

    private void remplirDonneesBase() throws Exception {

        // Ajout du tag "COPIE" si c'est une copie, sinon, vide
        if (isCopie) {
            data.addData("TEXTE_COPIE", getTexte(catalogeDeTexteDecision, 1, 3));
        } else {
            data.addData("TEXTE_COPIE", "");
        }

        setTexteCopie(getTexte(catalogeDeTexteDecision, 1, 3));

        // Pour tous les CHF du document
        data.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));

    }

    private void remplirDonneesVersement() throws Exception {

        data.addData("TITRE_VERSEMENT", getTexte(catalogeDeTexteDecision, 3, 1));

        Collection table1 = new Collection("versement");
        Collection table2 = new Collection("titulaire");
        boolean isTitulaire = false;

        // Afficher seulement si pas de décompte
        // Retrieve des ordres de versements
        REOrdresVersementsManager ovMgr = new REOrdresVersementsManager();
        ovMgr.setSession(getSession());
        ovMgr.setForIdPrestation(getDecision().getPrestation(getSession().getCurrentThreadTransaction())
                .getIdPrestation());
        ovMgr.find(BManager.SIZE_NOLIMIT);

        // si pas d'OV ou si date tout début dans le futur, pas de décompte
        JACalendar cal = new JACalendarGregorian();

        boolean isDepartFutur = false;

        if (cal.compare(firstDateDebutRADecision, new JADate(REPmtMensuel.getDateDernierPmt(getSession()))) == JACalendar.COMPARE_FIRSTUPPER) {
            isDepartFutur = true;
        }

        if (ovMgr.isEmpty() || isDepartFutur) {
            data.addData("TEXTE_VERSEMENT", getTexte(catalogeDeTexteDecision, 3, 2));
        }

        DataList line1 = new DataList("par");
        line1.addData("TEXTE_PAR", getTexte(catalogeDeTexteDecision, 3, 3));
        line1.addData("caisseAdr", getTexte(catalogeDeTexteDecision, 3, 4));

        if (!isCopie() || (isCopie() && getCopieDecision().getIsVersementA().booleanValue())) {

            DataList line2 = new DataList("a");
            line2.addData("TEXTE_A", getTexte(catalogeDeTexteDecision, 3, 5));

            DataList line3 = new DataList("tit");

            // Recherche de l'adresse de paiement
            try {
                TIAdressePaiementData adressePmt = PRTiersHelper.getAdressePaiementData(getSession(), getSession()
                        .getCurrentThreadTransaction(), idTiersAdressePmt,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "");

                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
                source.setSession(getSession());

                // Si pas d'adresse de paiement, adresse de courrier (mandat)
                if (null == adressePmt) {

                    // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
                    // se trouvant dans le fichier corvus.properties
                    String adressePmtMandat = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                            idTiersAdressePmt, REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

                    // On ajoute l'adresse dans la première colonne
                    line2.addData("nomBanque", adressePmtMandat);
                    // On ajoute le type (Par mandat postal) dans la deuxième colonne
                    line2.addData("noCompte", getTexte(catalogeDeTexteDecision, 3, 8));

                } else {
                    adressePmt.setSession(getSession());
                    source.load(adressePmt);
                    // Si c'est une adresse bancaire
                    if (!JadeStringUtil.isEmpty(adressePmt.getCompte())) {

                        // Colonne 1
                        // Nom de la banque
                        //
                        // Titulaire du compte
                        StringBuffer colonne1 = new StringBuffer();

                        // Colonne 2
                        // N° de compte
                        // N° de clearing
                        // Nom prénom titulaire
                        StringBuffer colonne2 = new StringBuffer();

                        // Trouver le nom de la banque
                        String nomBanque = adressePmt.getDesignation1_banque() + " "
                                + adressePmt.getDesignation2_banque();
                        colonne1.append(nomBanque);

                        // Trouver le numéro de compte
                        String noCompte = adressePmt.getCompte().toUpperCase();

                        // BZ 5274, si c'est un IBAN, le mettre en forme
                        // + BZ 6121 si l'IBAN est déjà dans le bon format on ne le modifie pas
                        if (noCompte.startsWith("CH") && !noCompte.matches(REDecisionOO.REGEX_IBAN)) {
                            String trimedNoCompte = noCompte.trim();
                            StringBuilder ibanBuilder = new StringBuilder();
                            for (int i = 0; i < trimedNoCompte.length(); i++) {
                                if ((i % 4) == 0) {
                                    ibanBuilder.append(" ");
                                }
                                ibanBuilder.append(trimedNoCompte.charAt(i));
                            }
                            noCompte = ibanBuilder.toString();
                        }

                        colonne2.append(getTexte(catalogeDeTexteDecision, 3, 10) + " " + noCompte + "\r");

                        // Trouver le numéro de clearing
                        String noClearing = adressePmt.getClearing();
                        if (!JadeStringUtil.isEmpty(noClearing)) {
                            colonne2.append(getTexte(catalogeDeTexteDecision, 3, 11) + " " + noClearing);
                        }

                        // Trouver le nom + prénom du titulaire
                        line3.addData("TEXTE_TITULAIRE", getTexte(catalogeDeTexteDecision, 3, 9));
                        String nomPrenomTitulaire = adressePmt.getDesignation1_adr() + " "
                                + adressePmt.getDesignation2_adr();

                        if (JadeStringUtil.isBlankOrZero(adressePmt.getDesignation1_adr())) {
                            nomPrenomTitulaire = adressePmt.getDesignation1_tiers() + " "
                                    + adressePmt.getDesignation2_tiers();
                        }

                        line3.addData("valeurTitulaire", nomPrenomTitulaire);

                        // On ajoute les paramètres
                        line2.addData("nomBanque", colonne1.toString());
                        line2.addData("noCompte", colonne2.toString());

                        table2.add(line3);
                        isTitulaire = true;

                        // Si c'est un CCP
                    } else if (!JadeStringUtil.isEmpty(adressePmt.getCcp())) {

                        // On retrouve l'adresse du ccp
                        String adresseCCP = "";

                        if (!JadeStringUtil.isEmpty(adressePmt.getDesignation1_adr())) {
                            adresseCCP = adressePmt.getDesignation1_adr();
                        } else {
                            adresseCCP = adressePmt.getDesignation1_tiers() + " " + adressePmt.getDesignation2_tiers();
                        }

                        adresseCCP += "\r";
                        adresseCCP += adressePmt.getRue() + " " + adressePmt.getNumero() + "\r";
                        adresseCCP += adressePmt.getNpa() + " " + adressePmt.getLocalite();

                        // On retrouve le n° de ccp
                        String noCCP = getTexte(catalogeDeTexteDecision, 3, 12) + " " + adressePmt.getCcp();

                        // On ajoute les paramères
                        line2.addData("nomBanque", adresseCCP);
                        line2.addData("noCompte", noCCP);

                        // Sinon un suppose le mandat
                    } else {

                        // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
                        // se trouvant dans le fichier corvus.properties
                        String adressePmtMandat = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                                idTiersAdressePmt, REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

                        // On ajoute l'adresse dans la première colonne
                        if (JadeStringUtil.isBlankOrZero(adressePmtMandat)) {

                            // BZ 5220, recherche de l'adresse en cascade en fonction du parametre isWantAdresseCourrier
                            // se trouvant dans le fichier corvus.properties
                            line2.addData("nomBanque", PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS),
                                    REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, ""));
                        } else {
                            line2.addData("nomBanque", adressePmtMandat);
                        }

                        // On ajoute le type (Par mandat postal) dans la deuxième colonne
                        line2.addData("noCompte", getTexte(catalogeDeTexteDecision, 3, 8));
                    }

                }
                // si aucun idTiersAdressePmt dans les rentes accordées, abort et log dans le mail
            } catch (Exception e) {
                if (!isCopie()) {
                    String msgError = e.toString() + "\n";
                    errorBuffer.append(msgError);
                }
            }

            table1.add(line1);
            table1.add(line2);
        } else {
            table1.add(line1);
        }

        data.add(table1);
        data.add(table2);

        if (!isTitulaire) {
            data.addData("isTitulaire", "stayLign");
        }

    }

    private void remplirInformations() throws Exception {

        StringBuffer buffer = new StringBuffer();

        // Insertion des informations
        data.addData("TITRE_INFORMATION", getTexte(catalogeDeTexteDecision, 7, 1));

        // si c'est une demande AI
        if (demandeRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE)) {
            buffer.append(getTexte(catalogeDeTexteDecision, 7, 2));
            buffer.append("\r\r");
        }

        // pour tous les type de demandes
        buffer.append(getTexte(catalogeDeTexteDecision, 7, 3));

        // si c'est une demande API
        if (demandeRente.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {
            buffer.append("\r");
            buffer.append(getTexte(catalogeDeTexteDecision, 7, 4));
            buffer.append("\r");
            buffer.append(getTexte(catalogeDeTexteDecision, 7, 5));

            // Ajouter l'annexe automatique pour 7.4 (memento sur les bonifications pour tâches d'assistance)
            isAnnexeBTA = true;
        }

        data.addData("TEXTE_INFORMATION", buffer.toString());

    }

    private void remplirMoyensDroit() throws Exception {

        StringBuffer buffer = new StringBuffer();

        // Moyens de droit
        if (!decision.getCsGenreDecision().equals(IREDecision.CS_GENRE_DECISION_COMMUNICATION)) {

            String donnesTribunal = null;

            // La recherche du tribunal est différente dans le cas d'un décision AVS ou AI/API

            // INV
            if (INV_ORD.equals(typeDecision) || INV_EXT.equals(typeDecision)) {
                donnesTribunal = PRTiersHelper.getAdresseTribunalPourOfficeAI(getSession(), officeAI,
                        tiers.getIdTiers(), decision.getDateDecision());
            }
            // API
            else if (API_AVS.equals(typeDecision) || API_INV.equals(typeDecision)) {
                donnesTribunal = PRTiersHelper.getAdresseTribunalPourOfficeAI(getSession(), officeAI,
                        tiers.getIdTiers(), decision.getDateDecision());
            }
            // AVS
            else if (AVS_ORD.equals(typeDecision) || AVS_EXT.equals(typeDecision)) {
                donnesTribunal = PRTiersHelper.getAdresseTribunalPourTiers(getSession(), tiers);
            }
            // La, ce n'est pas normal...
            else {
                String message = getSession().getLabel("IMPOSSIBLE_RETORUVER_TRIBUNAL_POUR_MOYEN_RECOURS");
                throw new IllegalArgumentException(message);
            }

            data.addData("TITRE_MOYENS_DROIT", getTexte(catalogeDeTexteDecision, 7, 20));

            // si décision sur opposition --> TOUJOURS 7.21
            if (decision.getCsGenreDecision().equals(IREDecision.CS_GENRE_DECISION_DECISION_SUR_OPPOSITION)) {

                buffer.append(PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 7, 21)
                        .replaceAll("\\n", "") + "\r", "{donneesTribunal}", donnesTribunal));

            } else {

                // si rentes AI --> 7.21
                if (typeDecision.startsWith("INV")) {
                    buffer.append(PRStringUtils.replaceString(
                            getTexte(catalogeDeTexteDecision, 7, 21).replaceAll("\\n", "") + "\r", "{donneesTribunal}",
                            donnesTribunal));

                }

                // si rentes AVS --> 7.22
                if (typeDecision.startsWith("AVS")) {
                    buffer.append(getTexte(catalogeDeTexteDecision, 7, 22) + "\r");
                }

                REDemandeRenteAPI demandeApi = new REDemandeRenteAPI();
                demandeApi.setSession(getSession());
                demandeApi.setIdDemandeRente(getDecision().getIdDemandeRente());
                demandeApi.retrieve(getSession().getCurrentThreadTransaction());

                // si rentes API -->
                if (!demandeApi.isNew()) {
                    // si api/ai --> 7.21
                    if (API_INV.equalsIgnoreCase(typeDecision)) {

                        buffer.append(PRStringUtils.replaceString(
                                getTexte(catalogeDeTexteDecision, 7, 21).replaceAll("\\n", "") + "\r",
                                "{donneesTribunal}", donnesTribunal));

                        // si api/avs --> 7.22
                    } else if (API_AVS.equalsIgnoreCase(typeDecision)) {

                        buffer.append(getTexte(catalogeDeTexteDecision, 7, 22) + "\r");

                    }
                }
            }

            if (montantTotal.compareTo(BigDecimal.ZERO) < 0) {
                buffer.append("\r");
                buffer.append(getTexte(catalogeDeTexteDecision, 7, 23) + "\r");
            }

            data.addData("TEXTE_MOYENS_DROIT", buffer.toString().trim());
        }

    }

    private void remplirObligationPayer() throws Exception {

        if (decision.getIsObliPayerCoti().booleanValue()) {
            data.addData("TITRE_OBLIGATION_PAYER_COTISATIONS", getTexte(catalogeDeTexteDecision, 7, 10));
            data.addData("TEXTE_OBLIGATION_PAYER_COTISATIONS", getTexte(catalogeDeTexteDecision, 7, 11));
        }

    }

    private void remplirObligationRenseigner() throws Exception {

        // Insertion de Votre obligation de renseigner
        StringBuffer buffer = new StringBuffer();

        data.addData("TITRE_OBLIGATION_RENSEIGNER", getTexte(catalogeDeTexteDecision, 7, 30));

        // --> si api, 7.32
        if (typeDecision.startsWith("API")) {

            data.addData("TEXTE_DEBUT_OBLIGATION_RENSEIGNER", getTexte(catalogeDeTexteDecision, 7, 32));

            // --> sinon, 7.31
        } else {

            data.addData("TEXTE_DEBUT_OBLIGATION_RENSEIGNER", getTexte(catalogeDeTexteDecision, 7, 31));

            data.addData("OBLIGATION_PAYER_1", getTexte(catalogeDeTexteDecision, 7, 33));
            data.addData("OBLIGATION_PAYER_2", getTexte(catalogeDeTexteDecision, 7, 34));
            data.addData("OBLIGATION_PAYER_3", getTexte(catalogeDeTexteDecision, 7, 35));
            data.addData("OBLIGATION_PAYER_4", getTexte(catalogeDeTexteDecision, 7, 36));
            data.addData("OBLIGATION_PAYER_5", getTexte(catalogeDeTexteDecision, 7, 37));
            data.addData("OBLIGATION_PAYER_6", getTexte(catalogeDeTexteDecision, 7, 38));
            data.addData("OBLIGATION_PAYER_7", getTexte(catalogeDeTexteDecision, 7, 39));

        }

        data.addData("", buffer.toString());

    }

    private void remplirPremierePartie() throws Exception {

        // Remplir le NSS
        data.addData("TEXTE_NSS", getTexte(catalogeDeTexteDecision, 1, 1));
        data.addData("nssTiersBeneficiaire", tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

        // Titre de la décision
        if (decision.getCsGenreDecision().equals(IREDecision.CS_GENRE_DECISION_COMMUNICATION)) {
            data.addData("DECISION_DU", getTexte(catalogeDeTexteDecision, 1, 5));
            data.addData("dateImpression", JACalendar.format(getDateDocument(), codeIsoLangue));

        } else if (decision.getCsGenreDecision().equals(IREDecision.CS_GENRE_DECISION_DECISION_SUR_OPPOSITION)) {
            data.addData("DECISION_DU", getTexte(catalogeDeTexteDecision, 1, 6));
            data.addData("dateImpression", JACalendar.format(getDateDocument(), codeIsoLangue));

        } else {
            data.addData("DECISION_DU", getTexte(catalogeDeTexteDecision, 1, 2));
            data.addData("dateImpression", JACalendar.format(getDateDocument(), codeIsoLangue));

        }

        if (decision.getIsRemAnnDeci().booleanValue()) {
            data.addData("TEXTE_ANNULE_REMPLACE", getTexte(catalogeDeTexteDecision, 1, 4));
        }

        // Insertion du titre du tiers
        ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
        Hashtable<String, String> params = new Hashtable<String, String>();

        PRTiersWrapper tiersT = tiers;

        if (isAdresseCourrierDiffTiersDecision) {
            tiersT = PRTiersHelper.getTiersParId(getSession(), decision.getIdTiersAdrCourrier());
            if (null == tiersT) {
                tiersT = PRTiersHelper.getAdministrationParId(getSession(), decision.getIdTiersAdrCourrier());
            }
        }

        if (isAdresseVide) {
            tiersT = PRTiersHelper.getTiersParId(getSession(), idTiersSiAdresseVide);
            if (null == tiersT) {
                tiersT = PRTiersHelper.getAdministrationParId(getSession(), idTiersSiAdresseVide);
            }
        }

        params.put(ITITiers.FIND_FOR_IDTIERS, tiersT.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        ITITiers[] t = tiersTitre.findTiers(params);
        if ((t != null) && (t.length > 0)) {
            tiersTitre = t[0];
        }

        data.addData(
                "titreTiers",
                PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 1, 8), "{titreTiers}",
                        tiersTitre.getFormulePolitesse(tiersT.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));

    }

    private void remplirRemarques() throws Exception {

        StringBuffer buffer = new StringBuffer();

        boolean isTitreOK = false;
        boolean isRemarque = false;

        // Insertion de la remarque générale de la décision

        // si restitution, on ajoute les textes pour mauvaise et bonne foi
        if (montantTotal.compareTo(BigDecimal.ZERO) < 0) {
            if (soldePourRestitution != null) {

                if (!isTitreOK) {
                    isTitreOK = true;
                }

                if (buffer.length() > 0) {
                    buffer.append("\r\r");
                }
                // Insérer une remarque pour indiquer le montant et le type de la restitution
                if (soldePourRestitution.getCsTypeRestitution().equals(IRESoldePourRestitution.CS_RETENUES)) {
                    if (!soldePourRestitution.getMontantMensuelARetenir().equals(soldePourRestitution.getMontant())) {
                        String texteAvecMontant = PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 6, 8),
                                REDecisionOO.CDT_MONTANTARETENIR, soldePourRestitution.getMontantMensuelARetenir());
                        buffer.append(texteAvecMontant);
                    } else {
                        buffer.append(getTexte(catalogeDeTexteDecision, 6, 3));
                    }
                } else if (soldePourRestitution.getCsTypeRestitution().equals(IRESoldePourRestitution.CS_EDITIONBVR)) {
                    buffer.append(getTexte(catalogeDeTexteDecision, 6, 4));
                } else {
                    buffer.append(getTexte(catalogeDeTexteDecision, 6, 5));
                }

                // Ajouter les remarques après 6/
                if ((null != getDecision().getRemarqueDecision())
                        && !"null".equals(getDecision().getRemarqueDecision())) {
                    if (!JadeStringUtil.isEmpty(getDecision().getRemarqueDecision())) {
                        if (buffer.length() > 0) {
                            buffer.append("\r\r");
                        }
                        buffer.append(getDecision().getRemarqueDecision().replaceAll("\\n", ""));
                        isRemarque = true;
                    }
                }
            }

            if (!isTitreOK) {
                isTitreOK = true;
            }

            // Remarque avant point 6/6 et 6/7 si pas ajoutée avant
            if (!isRemarque) {
                if ((null != getDecision().getRemarqueDecision())
                        && !"null".equals(getDecision().getRemarqueDecision())) {
                    if (!JadeStringUtil.isEmpty(getDecision().getRemarqueDecision())) {
                        if (buffer.length() > 0) {
                            buffer.append("\r\r");
                        }
                        buffer.append(getDecision().getRemarqueDecision().replaceAll("\\n", ""));
                        isRemarque = true;
                    }
                }
            }

            if (getDecision().getIsAvecBonneFoi().booleanValue()) {
                // si bonne foi
                if (buffer.length() > 0) {
                    buffer.append("\r\r");
                }
                buffer.append(getTexte(catalogeDeTexteDecision, 6, 6));
            }
            if (getDecision().getIsSansBonneFoi().booleanValue()) {
                // si pas bonne foi
                if (buffer.length() > 0) {
                    buffer.append("\r\r");
                }
                buffer.append(getTexte(catalogeDeTexteDecision, 6, 7));
            }
        }

        if (!isTitreOK) {
            isTitreOK = true;
        }

        // Remarque si on passe pas dans le grand if precedent !
        if (!isRemarque) {
            if ((null != getDecision().getRemarqueDecision()) && !"null".equals(getDecision().getRemarqueDecision())) {
                if (!JadeStringUtil.isEmpty(getDecision().getRemarqueDecision())) {
                    if (buffer.length() > 0) {
                        buffer.append("\r\r");
                    }
                    buffer.append(getDecision().getRemarqueDecision().replaceAll("\\n", ""));
                    isRemarque = true;
                }
            }
        }

        // Supplément pour personne veuve
        if (getDecision().getIsRemSuppVeuf().booleanValue()) {
            if (buffer.length() > 0) {
                buffer.append("\r\r");
            }
            buffer.append(getTexte(catalogeDeTexteRemarquesDecision, 1, 1));
        }

        // Rente réduite par plafonnement
        if (getDecision().getIsRemRedPlaf().booleanValue()) {
            if (buffer.length() > 0) {
                buffer.append("\r\r");
            }
            buffer.append(getTexte(catalogeDeTexteRemarquesDecision, 1, 2));
        }

        // INFOROM 500 : ajout d'une remarque s'il y a des intérêts moratoires
        if (decision.getIsRemInteretMoratoires()) {
            if (buffer.length() > 0) {
                buffer.append("\r\r");
            }
            buffer.append(getTexte(catalogeDeTexteRemarquesDecision, 1, 3));
        }

        // InfoRom D0112 : Ajout remarque dans décisions si rentes limitées

        // Rente de VEUF limitée
        if (decision.getIsRemarqueRenteDeVeufLimitee()) {
            if (buffer.length() > 0) {
                buffer.append("\r\r");
            }
            buffer.append(getTexte(catalogeDeTexteRemarquesDecision, 1, 4));
        }

        // Rente de VEUVE limitée
        if (decision.getIsRemarqueRenteDeVeuveLimitee()) {
            if (buffer.length() > 0) {
                buffer.append("\r\r");
            }
            buffer.append(getTexte(catalogeDeTexteRemarquesDecision, 1, 5));
        }

        // Remariage d'une rente de survivant
        if (decision.getIsRemarqueRemariageRenteDeSurvivant()) {
            if (buffer.length() > 0) {
                buffer.append("\r\r");
            }
            buffer.append(getTexte(catalogeDeTexteRemarquesDecision, 1, 6));
        }

        // Rente pour enfants
        /**
         * Information Cette remarque complique la gestion des remarques dans la décision du fait que la chaine est
         * découpée sur plusieurs éléments du catalogue de textes
         */
        boolean isRemarqueRentePourEnfant = decision.getIsRemarqueRentePourEnfant();
        String texteRemarque1 = "";
        String texteRemarque2Indice = "";
        String texteRemarque3 = "";
        String texteRemarque4Indice = "";
        String texteRemarque5 = "";

        if (isRemarqueRentePourEnfant) {
            if (buffer.length() > 0) {
                buffer.append("\r\r");
            }

            // On récupère ce qu'il y a déjà dans le buffer et on vide le buffer
            texteRemarque1 = buffer.toString();
            buffer = new StringBuffer();

            texteRemarque1 += getTexte(catalogeDeTexteRemarquesDecision, 1, 7);
            texteRemarque2Indice = getTexte(catalogeDeTexteRemarquesDecision, 1, 10);
            texteRemarque3 = getTexte(catalogeDeTexteRemarquesDecision, 1, 8);
            texteRemarque4Indice = texteRemarque2Indice;
            texteRemarque5 = getTexte(catalogeDeTexteRemarquesDecision, 1, 9);
        }

        // Début du droit 5 ans avant la date de dépôt de la demande
        if (decision.getIsRemarqueRenteAvecDebutDroit5AnsAvantDepotDemande()) {
            if (buffer.length() > 0) {
                buffer.append("\r\r");
            }
            buffer.append(getTexte(catalogeDeTexteRemarquesDecision, 1, 11));
        }

        // Montant minimum majoré pour invalidité précoce
        if (decision.getIsRemarqueRenteAvecMontantMinimumMajoreInvalidite()) {
            if (buffer.length() > 0) {
                buffer.append("\r\r");
            }
            buffer.append(getTexte(catalogeDeTexteRemarquesDecision, 1, 12));
        }

        // Début du droit 5 ans avant la date de dépôt de la demande
        if (decision.getIsRemarqueRenteReduitePourSurassurance()) {
            if (buffer.length() > 0) {
                buffer.append("\r\r");
            }
            buffer.append(getTexte(catalogeDeTexteRemarquesDecision, 1, 13));
        }

        if (decision.getIsRemarqueIncarceration()) {
            if (buffer.length() > 0) {
                buffer.append("\r\r");
            }
            buffer.append(getTexte(catalogeDeTexteRemarquesDecision, 1, 14));
        }

        if (isRemarqueRentePourEnfant) {
            data.addData("TITRE_REMARQUE", getTexte(catalogeDeTexteDecision, 6, 1));
            data.addData("TEXTE_REMARQUE_1", texteRemarque1);
            data.addData("TEXTE_REMARQUE_2_INDICE", texteRemarque2Indice);
            data.addData("TEXTE_REMARQUE_3", texteRemarque3);
            data.addData("TEXTE_REMARQUE_4_INDICE", texteRemarque4Indice);
            StringBuilder remarque5 = new StringBuilder(texteRemarque5);
            if (buffer.length() > 0) {
                remarque5.append("\r\r");
                remarque5.append(buffer.toString());
            }
            data.addData("TEXTE_REMARQUE_5", remarque5.toString());
        } else {
            if (buffer.length() > 0) {
                data.addData("TITRE_REMARQUE", getTexte(catalogeDeTexteDecision, 6, 1));
                data.addData("TEXTE_REMARQUE_1", buffer.toString());
            }
        }

        // Fin InfoRom D0112
    }

    private void remplirRentesAccordees() throws Exception {

        FWCurrency sommeTotalPrestationsRenteAccordee = new FWCurrency(0);
        Collection newTable = new Collection("listeRentes");
        DataList line1 = new DataList("enTeteRA");

        // Reprise des informations sur la décision
        REDecisionInfoContainer decision = decisionsContainer.getDecisionIC();

        // 1. Gestion du détail (Affichage des rentes accordées et détail)
        Set<KeyPeriodeInfo> keys = null;

        if ((decisionsContainer != null) && (decision != null)) {

            // On reprend toutes les clés de la map des bénéficiaires info
            keys = decision.getMapBeneficiairesInfo().keySet();

            if (keys != null) {
                Iterator<KeyPeriodeInfo> iter = keys.iterator();
                firstDateDebutRADecision = new JADate("31.12.9999");

                // Pour chaque clé
                while ((iter != null) && iter.hasNext()) {
                    KeyPeriodeInfo keyPeriode = iter.next();

                    JACalendar cal = new JACalendarGregorian();

                    if ((cal.compare(firstDateDebutRADecision,
                            new JADate(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateDebut)))) == JACalendar.COMPARE_SECONDLOWER) {
                        firstDateDebutRADecision = new JADate(
                                PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(keyPeriode.dateDebut));
                    }

                    FWCurrency totalMensuel = new FWCurrency();

                    REBeneficiaireInfoVO[] benefs = decision.getBeneficiaires(keyPeriode);

                    // Retrouver le tiers de la RA
                    PRTiersWrapper tierRA = PRTiersHelper.getTiersParId(getSession(), benefs[0]
                            .getIdTiersBeneficiaire().toString());

                    // Pour les bases de calcul, il faut garder en mémoire l'id de la dernière rente accordée de la
                    // décision
                    RERenteAccordee ra = new RERenteAccordee();
                    ra.setSession(getSession());
                    ra.setIdPrestationAccordee(benefs[0].getIdRenteAccordee().toString());
                    ra.retrieve();

                    if ((cal.compare(new JADate(keyPeriode.dateDebut), lastDateDebutRADecision)) == JACalendar.COMPARE_FIRSTUPPER) {
                        lastDateDebutRADecision = new JADate(keyPeriode.dateDebut);
                        idLastBCDecision = ra.getIdBaseCalcul();
                    }

                    if (JadeStringUtil.isEmpty(idTiersAdressePmt)) {
                        REInformationsComptabilite infoCompta = new REInformationsComptabilite();
                        infoCompta.setIdInfoCompta(ra.getIdInfoCompta());
                        infoCompta.setSession(getSession());
                        infoCompta.retrieve(getSession().getCurrentThreadTransaction());

                        idTiersAdressePmt = infoCompta.getIdTiersAdressePmt();
                    }

                    if (null != tierRA) {

                        String dateDebutFormatee = JACalendar.format(
                                PRDateFormater.convertDate_AAAAMM_to_MMAAAA(keyPeriode.dateDebut), codeIsoLangue);

                        if (IREDecision.CS_TYPE_DECISION_RETRO.equals(decision.getCsTypeDecision())
                                && JadeStringUtil.isEmpty(keyPeriode.dateFin)) {
                            keyPeriode.dateFin = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(this.decision
                                    .getDateFinRetro());
                        }

                        // pour date de fin, prendre le dernier jour du mois
                        String dateFinFormatee = JACalendar.format(
                                cal.lastInMonth(PRDateFormater.convertDate_AAAAMM_to_MMAAAA(keyPeriode.dateFin)),
                                codeIsoLangue);

                        // Créer la première phrase avec la période et la remarque
                        String texteDebut = "";

                        if (JadeStringUtil.isEmpty(keyPeriode.dateFin)) {
                            texteDebut = PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 2, 2),
                                    "{dateDebut}", dateDebutFormatee);

                            // isPeriodeInfinie = true;
                        } else {
                            texteDebut = PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 2, 1),
                                    "{dateDebut}", dateDebutFormatee);

                            texteDebut = PRStringUtils.replaceString(texteDebut, "{dateFin}", dateFinFormatee);
                        }

                        // si c'est le bénéficiaire principal
                        if (benefs[0].getIdTiersBeneficiaire().equals(
                                Long.parseLong(tierRA.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS)))) {

                            // Inforom 499 : Prestation transitoire
                            if (IREDemandeRente.CS_TYPE_CALCUL_TRANSITOIRE.equals(demandeRente.getCsTypeCalcul())) {
                                texteDebut += " " + getTexte(catalogeDeTexteDecision, 2, 13);
                            } else {
                                // Modification de la remarque selon le genre de prestation
                                if (typeDecision.equals(AVS_ORD)) {
                                    texteDebut += " " + getTexte(catalogeDeTexteDecision, 2, 5);
                                } else if (typeDecision.equals(AVS_EXT)) {
                                    texteDebut += " " + getTexte(catalogeDeTexteDecision, 2, 6);
                                } else if (typeDecision.equals(API_AVS)) {
                                    texteDebut += " " + getTexte(catalogeDeTexteDecision, 2, 7);
                                } else if (typeDecision.equals(INV_ORD)) {
                                    texteDebut += " " + getTexte(catalogeDeTexteDecision, 2, 9);
                                } else if (typeDecision.equals(INV_EXT)) {
                                    texteDebut += " " + getTexte(catalogeDeTexteDecision, 2, 10);
                                } else {
                                    texteDebut += " " + getTexte(catalogeDeTexteDecision, 2, 8);
                                }
                            }
                        }

                        // On somme toutes les prestations pour s'assurer que le montant total reste positif.
                        sommeTotalPrestationsRenteAccordee = new FWCurrency(0);
                        for (int inc = 0; inc < benefs.length; inc++) {
                            sommeTotalPrestationsRenteAccordee.add(new FWCurrency(benefs[inc].getMontant()));
                        }

                        // On affchie pas le texte uniquement si la somme de la rente est à Frs 0.-
                        if (sommeTotalPrestationsRenteAccordee.isPositive()) {
                            // Ajout de la ligne de début
                            line1 = new DataList("enTeteRA");
                            line1.addData("texteDebutRentes", texteDebut);
                            newTable.add(line1);
                        }

                        // Ajout de la ligne de remarque
                        line1 = new DataList("ligneRemarque");
                        if (!JadeStringUtil.isBlankOrZero(keyPeriode.remarque)) {
                            line1.addData("valeurRemarque", keyPeriode.remarque);
                            newTable.add(line1);
                        }

                        // Ajout ligne vide
                        if (sommeTotalPrestationsRenteAccordee.isPositive()) {
                            line1 = new DataList("ligneRemarque");
                            line1.addData("valeurRemarque", " ");
                            newTable.add(line1);
                        }

                    }

                    benefs = ordrerParGenreEtDateNaissance(benefs);

                    for (int inc = 0; inc < benefs.length; inc++) {

                        // On insert pas la ligne si le montant de la rente est a zero
                        if (!JadeStringUtil.isBlankOrZero(benefs[inc].getMontant())) {

                            // Retrouver le tiers de la RA
                            tierRA = PRTiersHelper.getTiersParId(getSession(), benefs[inc].getIdTiersBeneficiaire()
                                    .toString());

                            if (null != tierRA) {

                                // Ajout le détail de l'assuré
                                line1 = new DataList("ligneDonnees");
                                line1.addData("nssAssure", tierRA.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                                line1.addData("detailAssure", tierRA.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                        + tierRA.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                                newTable.add(line1);

                                // Ajout de la date de naissance et le genre de rente
                                line1 = new DataList("ligneGenreRente");
                                line1.addData("dateNaissance",
                                        tierRA.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
                                line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));

                                // Retrouver le type de rente accordée grâce au code système (avec genre prestation +
                                // fraction (10.1))
                                String pourRechercheCodeSysteme = benefs[inc].getGenrePrestation();

                                if (JadeStringUtil.isEmpty(benefs[inc].getFraction())) {
                                    pourRechercheCodeSysteme += ".0";
                                } else {
                                    pourRechercheCodeSysteme += "." + benefs[inc].getFraction();
                                }

                                // Retrouver le sexe du bénéficiaire
                                Long idTiers = benefs[inc].getIdTiersBeneficiaire();
                                String csSexe = "";

                                PRTiersWrapper tierBen = PRTiersHelper.getTiersParId(getSession(), idTiers.toString());
                                if (null != tierBen) {
                                    csSexe = tierBen.getProperty(PRTiersWrapper.PROPERTY_SEXE);
                                }

                                // Tip pour afficher le texte rente de veuve et veuf selon genre pour les rentes 13 et
                                // 23
                                if (benefs[inc].getGenrePrestation().equals(REGenresPrestations.GENRE_13)
                                        && csSexe.equals(ITIPersonne.CS_HOMME)) {

                                    line1.addData("genreRente", getTexte(catalogeDeTexteDecision, 2, 11));

                                } else if (benefs[inc].getGenrePrestation().equals(REGenresPrestations.GENRE_23)
                                        && csSexe.equals(ITIPersonne.CS_HOMME)) {

                                    line1.addData("genreRente", getTexte(catalogeDeTexteDecision, 2, 12));

                                } else {

                                    FWParametersUserCode userCode = new FWParametersUserCode();
                                    userCode.setSession(getSession());
                                    userCode.setIdCodeSysteme(getSession().getSystemCode("REGENRPRST",
                                            pourRechercheCodeSysteme));

                                    if (codeIsoLangue.equals("IT")) {
                                        userCode.setIdLangue("I");
                                    } else if (codeIsoLangue.equals("DE")) {
                                        userCode.setIdLangue("D");
                                    } else {
                                        userCode.setIdLangue("F");
                                    }

                                    userCode.retrieve();

                                    line1.addData("genreRente", userCode.getLibelle());
                                }

                                line1.addData("montantLigne", JANumberFormatter.format(benefs[inc].getMontant()));
                                newTable.add(line1);

                                totalMensuel.add(benefs[inc].getMontant());

                            } else {
                                throw new FWIException("Le tiers de la rente accordée n'a pas été trouvé");
                            }

                            // Ajout d'une ligne vide
                            if (inc < (benefs.length - 1)) {
                                line1 = new DataList("ligneRemarque");
                                line1.addData("valeurRemarque", " ");
                                newTable.add(line1);
                            }
                        }
                    }

                    // Ajouter la ligne du total mensuel uniquement si la somme est supérieur à Frs 0.-
                    if (sommeTotalPrestationsRenteAccordee.isPositive()) {
                        line1 = new DataList("ligneTotal");
                        line1.addData("TEXTE_TOTAL", getTexte(catalogeDeTexteDecision, 2, 4));
                        line1.addData("CHF", getTexte(catalogeDeTexteDecision, 2, 3));
                        line1.addData("montantTotal", totalMensuel.toStringFormat());
                        newTable.add(line1);
                    }

                    if (iter.hasNext()) {
                        if (sommeTotalPrestationsRenteAccordee.isPositive()) {
                            line1 = new DataList("ligneRemarque");
                            line1.addData("valeurRemarque", " ");
                            newTable.add(line1);
                        }
                    }

                }

                data.add(newTable);

            }
        }
    }

    private void remplirSalutationsSignature() throws Exception {

        int nbPageMotivation = 0;
        boolean isAPIAVS = false;

        if (typeDecision.startsWith("INV")) {
            REDemandeRenteInvalidite demRenteAI = new REDemandeRenteInvalidite();
            demRenteAI.setSession(getSession());
            demRenteAI.setIdDemandeRente(demandeRente.getIdDemandeRente());
            demRenteAI.retrieve();

            if (!JadeStringUtil.isBlankOrZero(demRenteAI.getNbPageMotivation())) {
                nbPageMotivation = Integer.parseInt(demRenteAI.getNbPageMotivation());
            }

        } else if (typeDecision.startsWith("API")) {
            REDemandeRenteAPI demRenteAPI = new REDemandeRenteAPI();
            demRenteAPI.setSession(getSession());
            demRenteAPI.setIdDemandeRente(demandeRente.getIdDemandeRente());
            demRenteAPI.retrieve();

            if (API_AVS.equalsIgnoreCase(typeDecision)) {
                isAPIAVS = true;
            }

            if (!JadeStringUtil.isBlankOrZero(demRenteAPI.getNbPageMotivation())) {
                nbPageMotivation = Integer.parseInt(demRenteAPI.getNbPageMotivation());
            }

        }

        decision.getCsGenreDecision();

        // Correction du bug 5178
        // test si le code système correspond du genre de décision correspond à "Communication"
        if ("52850001".equalsIgnoreCase(decision.getCsGenreDecision())) {
            // si c'est le cas, le pied de page contiendra "Cette communication comporte..."
            data.addData("piedPremierePage1", getTexte(catalogeDeTexteDecision, 9, 6));
        } else {
            // sinon "cette décision comporte..."
            data.addData("piedPremierePage1", getTexte(catalogeDeTexteDecision, 9, 2));
        }

        if ((nbPageMotivation == 0) || !isAvecMotivation) {
            data.addData("COUNT_ADD", "0");
        } else {
            data.addData("COUNT_ADD", Integer.toString(nbPageMotivation));
        }

        data.addData("piedPremierePage2", getTexte(catalogeDeTexteDecision, 9, 4));

        // si pas de pages de motivation ou si api avs
        if (!isAvecMotivation || (isAvecMotivation && isAPIAVS)) {

            // Insertion des salutations et de la signature
            if (!isCopie() || (isCopie() && getCopieDecision().getIsSignature().booleanValue())) {

                // Insertion du titre du tiers
                ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
                Hashtable<String, String> params = new Hashtable<String, String>();

                PRTiersWrapper tiersT = PRTiersHelper.getTiersParId(getSession(),
                        decision.getIdTiersBeneficiairePrincipal());
                if (null == tiersT) {
                    tiersT = PRTiersHelper.getAdministrationParId(getSession(),
                            decision.getIdTiersBeneficiairePrincipal());
                }

                if (isAdresseCourrierDiffTiersDecision) {
                    tiersT = PRTiersHelper.getTiersParId(getSession(), decision.getIdTiersAdrCourrier());
                    if (null == tiersT) {
                        tiersT = PRTiersHelper.getAdministrationParId(getSession(), decision.getIdTiersAdrCourrier());
                    }
                }

                if (isAdresseVide) {
                    tiersT = PRTiersHelper.getTiersParId(getSession(), idTiersSiAdresseVide);
                    if (null == tiersT) {
                        tiersT = PRTiersHelper.getAdministrationParId(getSession(), idTiersSiAdresseVide);
                    }
                }

                params.put(ITITiers.FIND_FOR_IDTIERS, tiersT.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                ITITiers[] t = tiersTitre.findTiers(params);
                if ((t != null) && (t.length > 0)) {
                    tiersTitre = t[0];
                }

                data.addData("TEXTE_SALUTATIONS", PRStringUtils.replaceString(getTexte(catalogeDeTexteDecision, 7, 40),
                        "{titreTiers}",
                        tiersTitre.getFormulePolitesse(tiersT.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));

                if (isEnteteAI) {

                    if (null != tiAdministration) {
                        data.addData("TEXTE_SIGNATURE", tiAdministration.getNomPrenom());
                        // BZ 4282
                        try {
                            if (!isAvecMotivation && !JadeStringUtil.isEmpty(getTexte(catalogeDeTexteDecision, 7, 42))) {
                                data.addData("AVIS_VAL_SANS_SIGN", getTexte(catalogeDeTexteDecision, 7, 42));
                            }
                        } catch (Exception e) {
                            // Ne rien faire car, ne doit pas générer une erreur dans le cas ou le ligne 7/42
                            // n'existe pas dans le catalogue de texte
                        }
                    } else {
                        data.addData("TEXTE_SIGNATURE", "Office AI introuvable");
                    }
                } else {
                    data.addData("TEXTE_SIGNATURE", getTexte(catalogeDeTexteDecision, 7, 41));
                }
            }
        }
    }

    public void setCopieDecision(final RECopieDecision copieDecision) {
        this.copieDecision = copieDecision;
    }

    public void setCopieFiscTronquee(final boolean isCopieFiscTronquee) {
        this.isCopieFiscTronquee = isCopieFiscTronquee;
    }

    public void setCopieOAI(final boolean isCopieOAI) {
        this.isCopieOAI = isCopieOAI;
    }

    public void setDateDocument(final String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDecision(final REDecisionEntity decision) {
        this.decision = decision;
    }

    public void setDecisionsContainer(final REDecisionsContainer decisionsContainer) {
        this.decisionsContainer = decisionsContainer;
    }

    public void setDocumentData(final DocumentData documentData) {
        this.documentData = documentData;
    }

    public void setErrorBuffer(final StringBuffer errorBuffer) {
        this.errorBuffer = errorBuffer;
    }

    public void setIsCopie(final boolean isCopie) {
        this.isCopie = isCopie;
    }

    public void setTexteCopie(final String texteCopie) {
        this.texteCopie = texteCopie;
    }

    public void setTiAdministration(final TIAdministrationViewBean tiAdministration) {
        this.tiAdministration = tiAdministration;
    }

}