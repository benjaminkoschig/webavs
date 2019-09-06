package globaz.corvus.servlet;

import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.attestationsFiscales.REAttestationFiscale;
import globaz.corvus.db.attestationsFiscales.REAttestationFiscaleDecision;
import globaz.corvus.db.attestationsFiscales.REAttestationFiscaleDecisionManager;
import globaz.corvus.db.attestationsFiscales.REAttestationFiscaleManager;
import globaz.corvus.db.attestationsFiscales.REAttestationFiscaleRenteAccordee;
import globaz.corvus.db.attestationsFiscales.REAttestationFiscaleRenteAccordeeManager;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REDecisionsManager;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.REOrdresVersementsManager;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.RERentesToCompare;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.corvus.vb.process.REGenererAttestationFiscaleUniqueViewBean;
import globaz.corvus.vb.process.REGenererAttestationFiscaleViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.http.JSPUtils;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.utils.ged.PRGedUtils;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiers;

/**
 * @author PCA
 */
public class REGenererAttestationFiscaleAction extends REDefaultProcessAction {

    private static final String VERS_ECRAN_DE = "_de.jsp?";
    private TIAdressePaiementData adr;
    private String adresse = "";
    private final Map<String, String> assure = new HashMap<>();
    private final Map<String,String> codePrestation = new HashMap<>();
    private REAttestationFiscale att;
    private final JACalendar cal = new JACalendarGregorian();
    private ICTDocument document;
    private ICTDocument documentDecision;
    private ICTDocument documentHelper;
    private ICTDocument documentHelperDecision;
    private String idTiersAdresseSiDeces = "";
    private final Map<String, String> libelleRente = new HashMap<>();
    private final Map<String, String> montant = new HashMap<>();
    private final FWCurrency montantTotal = new FWCurrency("0.00");
    private final Map<String, String> OVDesignation = new HashMap<>();
    private final Map<String, String> OVMontant = new HashMap<>();
    private final Map<String, String> OVType = new HashMap<>();
    private final Map<String, String> periode = new HashMap<>();
    private PRTiersWrapper tiersAdresse;
    private PRTiersWrapper tiersBeneficiaire;
    private String titre = "";
    private String idTiers = "";
    private String idTiersBaseCalcul = "";
    private String idAdressePaiement;

    public REGenererAttestationFiscaleAction(final FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher)
            throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            REGenererAttestationFiscaleViewBean viewBean = (REGenererAttestationFiscaleViewBean) FWViewBeanActionFactory
                    .newInstance(action, mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());

            String isDepuisDemande = request.getParameter("isDepuisDemande");
            String numNss = request.getParameter("numNSS");

            if (!JadeStringUtil.isNull(isDepuisDemande) && !JadeStringUtil.isEmpty(isDepuisDemande)) {
                viewBean.setIsDepuisDemande(Boolean.TRUE);
                viewBean.setNSS(numNss);
            }

            if (PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.ATTESTATIONS_FISCALES_ANNUELLE,
                    (BSession) mainDispatcher.getSession())) {
                viewBean.setDisplaySendToGed("1");
            } else {
                viewBean.setDisplaySendToGed("0");
            }

            JSPUtils.setBeanProperties(request, viewBean);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionExecuter(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher)
            throws ServletException, IOException {

        String isAttestationUnique = request.getParameter("isAttestationUnique");
        String nss = request.getParameter("NSS");
        String destination = "/corvusRoot/process/genererAttestationFiscaleUnique"
                + REGenererAttestationFiscaleAction.VERS_ECRAN_DE;
        String codeIsoLangue = "";
        String anneeAttestation = request.getParameter("anneeAttestations");
        String mail = request.getParameter("eMailAddress");
        String dateAttJJMMAAAA = request.getParameter("dateImpressionAttJJMMAAA");

        if ("false".equals(isAttestationUnique)) {
            super.actionExecuter(session, request, response, mainDispatcher);
        } else {
            try {

                BTransaction transaction = (BTransaction) ((BSession) mainDispatcher.getSession()).newTransaction();
                REGenererAttestationFiscaleUniqueViewBean viewBean = new REGenererAttestationFiscaleUniqueViewBean();

                // Chargement du catalogue de texte
                BSession bSession = (BSession) mainDispatcher.getSession();
                PRTiersWrapper tierswrap = PRTiersHelper.getTiers(bSession, nss);

                viewBean.setISession(bSession);

                if (null == tierswrap) {
                    viewBean.setNSS(nss);
                    session.setAttribute(FWServlet.VIEWBEAN, viewBean);

                } else {
                    codeIsoLangue = bSession.getCode(tierswrap.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                    codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
                    // creation du helper pour les catalogues de texte
                    documentHelper = PRBabelHelper.getDocumentHelper(bSession);
                    documentHelper.setCsDomaine(IRECatalogueTexte.CS_RENTES);
                    documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_ATTESTATION_FISCALE);
                    documentHelper.setNom("openOffice");
                    documentHelper.setDefault(Boolean.FALSE);
                    documentHelper.setActif(Boolean.TRUE);
                    documentHelper.setCodeIsoLangue(codeIsoLangue);

                    REAttestationFiscaleManager attmgr = new REAttestationFiscaleManager();
                    attmgr.setSession(bSession);
                    attmgr.setForAnnee(anneeAttestation);
                    attmgr.setFornss(nss);
                    attmgr.setControleDateFinDansAnnee(false);
                    attmgr.find(transaction);

                    // recherche les rentes accordées de la famille
                    if (attmgr.isEmpty()) {
                        RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager renteAccManager = new RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager();

                        if (nss.length() > 14) {
                            renteAccManager.setLikeNumeroAVSNNSS("true");
                        } else {
                            renteAccManager.setLikeNumeroAVSNNSS("false");
                        }

                        renteAccManager.wantCallMethodBeforeFind(true);
                        renteAccManager.setLikeNumeroAVS(nss);
                        renteAccManager.setRechercheFamille(true);
                        renteAccManager.setSession(bSession);
                        // renteAccManager.setDateDecesVide(true);
                        renteAccManager.setOrderBy(RERenteAccordee.FIELDNAME_ID_TIERS_BASE_CALCUL);
                        renteAccManager.setForEnCoursAtMois("12." + anneeAttestation);
                        renteAccManager.setOrderBy(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
                        renteAccManager.find();

                        if (!renteAccManager.isEmpty()) {
                            RERenteAccordee renteacc = (RERenteAccordee) renteAccManager.getFirstEntity();
                            attmgr.setSession(bSession);
                            attmgr.setForAnnee(anneeAttestation);
                            PRTiersWrapper tier = PRTiersHelper.getTiersParId(bSession,
                                    renteacc.getIdTiersBeneficiaire());
                            attmgr.setFornss(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                            attmgr.setControleDateFinDansAnnee(false);
                            attmgr.find(transaction);
                        }
                    }

                    // K161215_001 -> recherche de la langue du tiers Bénéficiaire
                    codeIsoLangue = getCodeLangue(codeIsoLangue, anneeAttestation, transaction, bSession, tierswrap,
                            attmgr);

                    ICTDocument[] documents = documentHelper.load();

                    if ((documents == null) || (documents.length == 0)) {
                        throw new Exception(bSession.getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
                    } else {
                        document = documents[0];
                    }

                    for (int i = 0; i < attmgr.size(); i++) {
                        att = (REAttestationFiscale) attmgr.getEntity(i);

                        REInformationsComptabilite infocompta = new REInformationsComptabilite();
                        infocompta.setSession(bSession);
                        infocompta.setIdInfoCompta(att.getIdInfoCompta());
                        infocompta.retrieve();

                        if (REGenresPrestations.GENRE_81.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_85.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_89.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_84.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_91.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_94.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_95.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_82.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_86.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_88.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_92.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_96.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_83.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_87.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_93.equals(att.getCodePrestation())
                                || REGenresPrestations.GENRE_97.equals(att.getCodePrestation())) {
                            continue;
                        } else {
                            REAttestationFiscaleRenteAccordeeManager mgr2 = new REAttestationFiscaleRenteAccordeeManager();
                            mgr2.setSession(bSession);

                            if (att.getNss().length() > 14) {
                                mgr2.setLikeNumeroAVSNNSS("true");
                            } else {
                                mgr2.setLikeNumeroAVSNNSS("false");
                            }

                            mgr2.wantCallMethodBeforeFind(true);
                            mgr2.setLikeNumeroAVS(att.getNss());
                            mgr2.setRechercheFamille(true);
                            // ddDroit <= DATE
                            mgr2.setFromDateDebutDroit("12." + anneeAttestation);
                            // dfDroit >= DATE
                            mgr2.setToDateFinDroit("01." + anneeAttestation);
                            mgr2.setForCsEtatNotIn(IREPrestationAccordee.CS_ETAT_CALCULE + ", "
                                    + IREPrestationAccordee.CS_ETAT_AJOURNE);
                            mgr2.setForOrderBy(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION + ", "
                                    + RERenteAccJoinTblTiersJoinDemandeRente.FIELDNAME_NOM + ", "
                                    + RERenteAccJoinTblTiersJoinDemandeRente.FIELDNAME_PRENOM + ", "
                                    + REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);

                            mgr2.find(transaction);
                            /*
                             * K160602_004 - attestation fiscale - rente survivant, afficher les rentes versées à la
                             * même
                             * adresse
                             */
                            for (int k = 0; k < mgr2.size(); k++) {
                                REAttestationFiscaleRenteAccordee raccoNSSConcerne = (REAttestationFiscaleRenteAccordee) mgr2
                                        .getEntity(k);
                                if (raccoNSSConcerne.getNumeroAvsBenef().equals(att.getNss())) {
                                    idAdressePaiement = infocompta.getIdTiersAdressePmt();
                                    break;
                                }
                            }
                            /*
                             * Si le NSS choisi n'a pas de rente alors on prends la 1ère rente comme adresse de paiement
                             * de référence
                             */
                            if (JadeStringUtil.isBlank(idAdressePaiement)) {
                                REAttestationFiscaleRenteAccordee Rente1ereTrouve = (REAttestationFiscaleRenteAccordee) mgr2
                                        .getFirstEntity();
                                infocompta.setIdInfoCompta(Rente1ereTrouve.getIdInfoCompta());
                                infocompta.retrieve();
                                idAdressePaiement = infocompta.getIdTiersAdressePmt();
                            }

                            for (int j = 0; j < mgr2.size(); j++) {
                                REAttestationFiscaleRenteAccordee ra = (REAttestationFiscaleRenteAccordee) mgr2
                                        .getEntity(j);

                                if (REGenresPrestations.GENRE_10.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_20.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_50.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_70.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_72.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_13.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_23.equals(ra.getCodePrestation())) {
                                    if (!att.getNss().equals(ra.getNumeroAvsBenef())) {
                                        if (!JadeStringUtil.isEmpty(att.getDateDeces())) {
                                            ISFSituationFamiliale sf = SFSituationFamilialeFactory
                                                    .getSituationFamiliale(bSession,
                                                            ISFSituationFamiliale.CS_DOMAINE_RENTES,
                                                            att.getIdTiersBeneficiaire());
                                            ISFMembreFamilleRequerant[] mbrsFam = sf
                                                    .getMembresFamille(att.getIdTiersBeneficiaire());
                                            for (ISFMembreFamilleRequerant mbrs : mbrsFam) {
                                                if (att.getNss().equals(mbrs.getNss())
                                                        && !ISFSituationFamiliale.CS_ETAT_CIVIL_DIVORCE
                                                                .equals(mbrs.getCsEtatCivil())) {
                                                    idTiersAdresseSiDeces = ra.getIdTiersBeneficiaire();
                                                    break;
                                                }
                                            }

                                        }
                                    }
                                }
                                if (REGenresPrestations.GENRE_13.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_14.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_15.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_16.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_23.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_24.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_25.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_26.equals(ra.getCodePrestation())) {
                                    infocompta.setIdInfoCompta(ra.getIdInfoCompta());
                                    infocompta.retrieve();
                                    if (!IsMemeAdressePaiement(idAdressePaiement, ra.getIdTierAdressePmt())) {
                                        continue;
                                    }
                                } else {
                                    if (!att.getIdTiersBaseCalcul().equals(ra.getIdTiersBaseCalcul())) {
                                        continue;
                                    }
                                }

                                if (REGenresPrestations.GENRE_81.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_85.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_89.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_84.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_91.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_94.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_95.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_82.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_86.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_88.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_92.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_96.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_83.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_87.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_93.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_97.equals(ra.getCodePrestation())) {
                                    continue;
                                } else {
                                    String infoTiers = ra.getNumeroAvsBenef() + " " + ra.getNomBenef() + " "
                                            + ra.getPrenomBenef();

                                    JADate dd = new JADate("01.01." + anneeAttestation);

                                    // format mm.aaaa
                                    String dateDernierPmt = REPmtMensuel.getDateDernierPmt(bSession);
                                    JADate df;
                                    if (PRDateFormater.convertDate_MMxAAAA_to_AAAA(dateDernierPmt)
                                            .equals(anneeAttestation)) {
                                        df = new JADate(dateDernierPmt);
                                        df.setDay(cal.daysInMonth(df.getMonth(), df.getYear()));
                                    } else {
                                        df = new JADate("31.12." + anneeAttestation);
                                    }

                                    if (!JadeStringUtil.isBlankOrZero(ra.getDateDebutDroit())) {
                                        JADate d = new JADate(ra.getDateDebutDroit());

                                        if (cal.compare(dd, d) == JACalendar.COMPARE_FIRSTLOWER) {
                                            dd = new JADate(ra.getDateDebutDroit());
                                        }
                                    }

                                    if (!JadeStringUtil.isBlankOrZero(ra.getDateFinDroit())) {
                                        JADate d = new JADate(ra.getDateFinDroit());

                                        if (cal.compare(df, d) == JACalendar.COMPARE_FIRSTUPPER) {
                                            df = new JADate(ra.getDateFinDroit());
                                            df.setDay(cal.daysInMonth(df.getMonth(), df.getYear()));
                                        }
                                    }

                                    REPrestationsDuesManager prm = new REPrestationsDuesManager();
                                    prm.setSession(bSession);
                                    prm.setForIdRenteAccordes(ra.getIdRenteAccordee());
                                    prm.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
                                    prm.setOrderBy(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT);
                                    prm.find();
                                    FWCurrency montant = new FWCurrency();
                                    for (int a = 0; a < prm.size(); a++) {

                                        REPrestationDue pa = (REPrestationDue) prm.get(a);

                                        String dateDebutPrestation = PRDateFormater
                                                .convertDate_MMxAAAA_to_AAAA(pa.getDateDebutPaiement());
                                        String dateFinPrestation = PRDateFormater
                                                .convertDate_MMxAAAA_to_AAAA(pa.getDateFinPaiement());

                                        if (((cal.compare(dateDebutPrestation,
                                                anneeAttestation) == JACalendar.COMPARE_FIRSTLOWER)
                                                || (cal.compare(dateDebutPrestation,
                                                        anneeAttestation) == JACalendar.COMPARE_EQUALS))
                                                && ((cal.compare(dateFinPrestation,
                                                        anneeAttestation) == JACalendar.COMPARE_FIRSTUPPER)
                                                        || (cal.compare(dateFinPrestation,
                                                                anneeAttestation) == JACalendar.COMPARE_EQUALS)
                                                        || JadeStringUtil.isEmpty(dateFinPrestation))) {
                                            JADate dateDebut = new JADate("01.01." + anneeAttestation);

                                            JADate dateFin;

                                            if (!JadeStringUtil.isBlankOrZero(ra.getDateFinDroit())
                                                    && PRDateFormater.convertDate_MMxAAAA_to_AAAA(ra.getDateFinDroit())
                                                            .equals(anneeAttestation)) {
                                                dateFin = new JADate(ra.getDateFinDroit());
                                                dateFin.setDay(cal.daysInMonth(dateFin.getMonth(), dateFin.getYear()));
                                            } else if (PRDateFormater.convertDate_MMxAAAA_to_AAAA(dateDernierPmt)
                                                    .equals(anneeAttestation)) {
                                                dateFin = new JADate(dateDernierPmt);
                                                dateFin.setDay(cal.daysInMonth(dateFin.getMonth(), dateFin.getYear()));
                                            } else {
                                                dateFin = new JADate("31.12." + anneeAttestation);
                                            }

                                            if (!JadeStringUtil.isBlankOrZero(pa.getDateDebutPaiement())) {
                                                JADate d = new JADate(pa.getDateDebutPaiement());

                                                if (cal.compare(dateDebut, d) == JACalendar.COMPARE_FIRSTLOWER) {
                                                    dateDebut = new JADate(pa.getDateDebutPaiement());
                                                }
                                            }

                                            if (!JadeStringUtil.isBlankOrZero(pa.getDateFinPaiement())) {
                                                JADate d = new JADate(pa.getDateFinPaiement());

                                                if (cal.compare(dateFin, d) == JACalendar.COMPARE_FIRSTUPPER) {
                                                    dateFin = new JADate(pa.getDateFinPaiement());
                                                    dateFin.setDay(
                                                            cal.daysInMonth(dateFin.getMonth(), dateFin.getYear()));
                                                }
                                            }

                                            int moisD = dateDebut.getMonth();
                                            int moisF = dateFin.getMonth();
                                            int difference = (moisF - moisD) + 1;

                                            FWCurrency montantPrest = new FWCurrency(pa.getMontant());
                                            float f = difference * montantPrest.floatValue();
                                            montant.add(Float.toString(f));
                                        }
                                    }

                                    FWCurrency montantAfficher = new FWCurrency("0.00");
                                    montantAfficher.add(montant);
                                    montantTotal.add(montant);

                                    // BZ 5177 Dans le cas d'une rente 13 pour un homme, je ne dois pas aller chercher
                                    // la valeur du code système mais le texte niveau 2 position 11 dans le catalogue de
                                    // texte des décisions.
                                    if (REGenresPrestations.GENRE_13.equals(ra.getCodePrestation())
                                            && PRACORConst.CS_HOMME.equals(ra.getSexe())) {

                                        documentHelperDecision = PRBabelHelper.getDocumentHelper(bSession);
                                        documentHelperDecision.setCsDomaine(IRECatalogueTexte.CS_RENTES);
                                        documentHelperDecision.setCsTypeDocument(IRECatalogueTexte.CS_DECISION);
                                        documentHelperDecision.setNom("openOffice");
                                        documentHelperDecision.setDefault(Boolean.FALSE);
                                        documentHelperDecision.setActif(Boolean.TRUE);
                                        documentHelperDecision.setCodeIsoLangue(codeIsoLangue);

                                        ICTDocument[] documentsDecis = documentHelperDecision.load();

                                        if ((documentsDecis == null) || (documentsDecis.length == 0)) {
                                            throw new Exception(bSession.getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
                                        } else {
                                            documentDecision = documentsDecis[0];
                                        }

                                        String index = Integer.toString(j);
                                        assure.put(index, infoTiers);
                                        periode.put(index, dd.toStr(".") + " - " + df.toStr("."));
                                        this.montant.put(index, montantAfficher.toStringFormat());
                                        libelleRente.put(index,
                                                documentDecision.getTextes(2).getTexte(11).getDescription());
                                        codePrestation.put(index, ra.getCodePrestation());
                                    } else {
                                        String pourRechercheCodeSysteme = ra.getCodePrestation();

                                        if (JadeStringUtil.isEmpty(ra.getFractionRente())) {
                                            pourRechercheCodeSysteme += ".0";
                                        } else {
                                            pourRechercheCodeSysteme += "." + ra.getFractionRente();
                                        }

                                        // Recuperation du code système en fonction
                                        // de codeIsoLangue et non en fonction de la
                                        // langue de l'utilisateur
                                        FWParametersUserCode userCode = new FWParametersUserCode();
                                        userCode.setSession(bSession);
                                        userCode.setIdCodeSysteme(
                                                bSession.getSystemCode("REGENRPRST", pourRechercheCodeSysteme));

                                        if (codeIsoLangue.equals("IT")) {
                                            userCode.setIdLangue("I");
                                        } else if (codeIsoLangue.equals("DE")) {
                                            userCode.setIdLangue("D");
                                        } else {
                                            userCode.setIdLangue("F");
                                        }

                                        userCode.retrieve();
                                        String index = Integer.toString(j);
                                        assure.put(index, infoTiers);
                                        periode.put(index, dd.toStr(".") + " - " + df.toStr("."));
                                        this.montant.put(index, montantAfficher.toStringFormat());
                                        libelleRente.put(index, userCode.getLibelle());
                                        codePrestation.put(index, ra.getCodePrestation());
                                    }

                                    REDecisionsManager decismgr = new REDecisionsManager();
                                    decismgr.setSession(bSession);
                                    decismgr.setForIdTiersBeneficaire(ra.getIdTiersBeneficiaire());
                                    decismgr.setForValideDes("0101" + anneeAttestation);
                                    decismgr.setForValideJusqua("3112" + anneeAttestation);
                                    decismgr.find();

                                    for (int x = 0; x < decismgr.size(); x++) {
                                        REDecisionEntity decis = (REDecisionEntity) decismgr.get(x);

                                        REOrdresVersementsManager ovmgr = new REOrdresVersementsManager();
                                        ovmgr.setSession(bSession);
                                        ovmgr.setForIdPrestation(decis.getPrestation(transaction).getIdPrestation());
                                        ovmgr.find();

                                        for (int l = 0; l < ovmgr.size(); l++) {
                                            REOrdresVersements ov = (REOrdresVersements) ovmgr.get(l);
                                            String ovIndex = Integer.toString(l);
                                            String designation = "";

                                            if (ov.getCsType().equals(IREOrdresVersements.CS_TYPE_TIERS)
                                                    || ov.getCsType()
                                                            .equals(IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE)
                                                    || ov.getCsType()
                                                            .equals(IREOrdresVersements.CS_TYPE_INTERET_MORATOIRE)
                                                    || ov.getCsType()
                                                            .equals(IREOrdresVersements.CS_TYPE_DETTE_RENTE_AVANCES)
                                                    || ov.getCsType()
                                                            .equals(IREOrdresVersements.CS_TYPE_DETTE_RENTE_DECISION)
                                                    || ov.getCsType()
                                                            .equals(IREOrdresVersements.CS_TYPE_DETTE_RENTE_PRST_BLOQUE)
                                                    || ov.getCsType()
                                                            .equals(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION)
                                                    || ov.getCsType()
                                                            .equals(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RETOUR)
                                                    || ov.getCsType().equals(IREOrdresVersements.CS_TYPE_DETTE)) {

                                                PRTiersWrapper tier;
                                                try {
                                                    tier = PRTiersHelper.getTiersParId(bSession, ov.getIdTiers());

                                                    if (tier == null) {
                                                        tier = PRTiersHelper.getAdministrationParId(bSession,
                                                                ov.getIdTiers());
                                                    }

                                                    if (tier != null) {
                                                        designation = tier.getProperty(PRTiersWrapper.PROPERTY_NOM)
                                                                + " "
                                                                + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                if (ov.getCsType().equals(IREOrdresVersements.CS_TYPE_TIERS)
                                                        || ov.getCsType()
                                                                .equals(IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE)
                                                        || ov.getCsType().equals(
                                                                IREOrdresVersements.CS_TYPE_INTERET_MORATOIRE)) {

                                                } else {

                                                    if (ov.getCsType().equals(IREOrdresVersements.CS_TYPE_DETTE)) {
                                                        // BZ 4877, changement du format d'affichage de la dette
                                                        FWParametersUserCode csOv = new FWParametersUserCode();
                                                        csOv.setSession(bSession);
                                                        csOv.setIdCodeSysteme(ov.getCsType());
                                                        if (codeIsoLangue.equals("IT")) {
                                                            csOv.setIdLangue("I");
                                                        } else if (codeIsoLangue.equals("DE")) {
                                                            csOv.setIdLangue("D");
                                                        } else {
                                                            csOv.setIdLangue("F");
                                                        }
                                                        csOv.retrieve();

                                                        designation = csOv.getLibelle() + " - " + designation;
                                                    } else {

                                                        String no = ov.getNoFacture().substring(
                                                                ov.getNoFacture().lastIndexOf("-") + 2,
                                                                ov.getNoFacture().length());

                                                        designation = no + " - " + designation;
                                                    }
                                                }
                                            } else if (ov.getCsType()
                                                    .equals(IREOrdresVersements.CS_TYPE_IMPOT_SOURCE)) {

                                                designation = document.getTextes(8).getTexte(1).getDescription();

                                            } else if (ov.getCsType()
                                                    .equals(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)) {
                                                continue;
                                            } else {
                                                designation = "";
                                            }

                                            if (IREOrdresVersements.CS_TYPE_INTERET_MORATOIRE.equals(ov.getCsType())) {
                                                // BZ 4877, changement du format d'affichage de l'intérêt moratoire
                                                FWParametersUserCode csOv = new FWParametersUserCode();
                                                csOv.setSession(bSession);
                                                csOv.setIdCodeSysteme(ov.getCsType());
                                                if (codeIsoLangue.equals("IT")) {
                                                    csOv.setIdLangue("I");
                                                } else if (codeIsoLangue.equals("DE")) {
                                                    csOv.setIdLangue("D");
                                                } else {
                                                    csOv.setIdLangue("F");
                                                }
                                                csOv.retrieve();
                                                OVDesignation.put(ovIndex,
                                                        "+ " + csOv.getLibelle() + " (" + designation + ")");
                                            } else {
                                                OVDesignation.put(ovIndex, "./. " + designation);
                                            }

                                            OVType.put(ovIndex, "");
                                            FWCurrency montantOV = new FWCurrency(ov.getMontant());
                                            OVMontant.put(ovIndex, montantOV.toStringFormat());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    viewBean.setOVDesignation(OVDesignation);
                    viewBean.setOVMontant(OVMontant);
                    viewBean.setOVType(OVType);

                    this.sortRentes();

                    viewBean.setMapassure(assure);
                    viewBean.setMapperiode(periode);
                    viewBean.setMapmontant(montant);
                    viewBean.setMaplibelleRente(libelleRente);
                    viewBean.setMontantTotal(montantTotal);

                    REAttestationFiscaleDecisionManager attdecismgr = new REAttestationFiscaleDecisionManager();
                    attdecismgr.setSession(bSession);

                    attdecismgr.setFornss(nss);

                    attdecismgr.setForEtatDecisions(IREDecision.CS_ETAT_VALIDE);
                    attdecismgr.setForCodesPrestationsIn("'" + REGenresPrestations.GENRE_81 + "', " + "'"
                            + REGenresPrestations.GENRE_85 + "', " + "'" + REGenresPrestations.GENRE_89 + "', " + "'"
                            + REGenresPrestations.GENRE_84 + "', " + "'" + REGenresPrestations.GENRE_91 + "', " + "'"
                            + REGenresPrestations.GENRE_94 + "', " + "'" + REGenresPrestations.GENRE_95 + "', " + "'"
                            + REGenresPrestations.GENRE_82 + "', " + "'" + REGenresPrestations.GENRE_86 + "', " + "'"
                            + REGenresPrestations.GENRE_88 + "', " + "'" + REGenresPrestations.GENRE_92 + "', " + "'"
                            + REGenresPrestations.GENRE_96 + "', " + "'" + REGenresPrestations.GENRE_83 + "', " + "'"
                            + REGenresPrestations.GENRE_87 + "', " + "'" + REGenresPrestations.GENRE_93 + "', " + "'"
                            + REGenresPrestations.GENRE_97 + "' ");
                    attdecismgr.find();

                    if (!attdecismgr.isEmpty()) {
                        for (int i = 0; i < attdecismgr.size(); i++) {
                            REAttestationFiscaleDecision rd = (REAttestationFiscaleDecision) attdecismgr.getEntity(i);
                            JADate jDd = new JADate(
                                    PRDateFormater.convertDate_AAAAMM_to_MMAAAA(rd.getDateDebutDroit()));
                            JADate jDf = new JADate(PRDateFormater.convertDate_AAAAMM_to_MMAAAA(rd.getDateFinDroit()));
                            JACalendar cal = new JACalendarGregorian();
                            int cDd = cal.compare(Integer.toString(jDd.getYear()), anneeAttestation);
                            int cDf = cal.compare(Integer.toString(jDf.getYear()), anneeAttestation);
                            // BZ4974 Modification du if ci-dessous, je ne contrôle que l'année mais plus le mois car,
                            // si date de fin api dans l'année, je dois afficher le texte pour l'api
                            if (JadeStringUtil.isBlankOrZero(rd.getDateFinDroit())
                                    || (((cDd == JACalendar.COMPARE_EQUALS) || (cDd == JACalendar.COMPARE_FIRSTLOWER))
                                            && ((cDf == JACalendar.COMPARE_EQUALS)
                                                    || (cDf == JACalendar.COMPARE_FIRSTUPPER)))) {
                                if (REGenresPrestations.GENRE_81.equals(rd.getCodePrestation())
                                        || REGenresPrestations.GENRE_85.equals(rd.getCodePrestation())
                                        || REGenresPrestations.GENRE_89.equals(rd.getCodePrestation())
                                        || REGenresPrestations.GENRE_84.equals(rd.getCodePrestation())
                                        || REGenresPrestations.GENRE_91.equals(rd.getCodePrestation())
                                        || REGenresPrestations.GENRE_95.equals(rd.getCodePrestation())) {
                                    viewBean.setTitreAPI(document.getTextes(4).getTexte(1).getDescription());
                                    viewBean.setParaAPI(document.getTextes(4).getTexte(2).getDescription());

                                } else if (REGenresPrestations.GENRE_82.equals(rd.getCodePrestation())
                                        || REGenresPrestations.GENRE_86.equals(rd.getCodePrestation())
                                        || REGenresPrestations.GENRE_88.equals(rd.getCodePrestation())
                                        || REGenresPrestations.GENRE_92.equals(rd.getCodePrestation())
                                        || REGenresPrestations.GENRE_96.equals(rd.getCodePrestation())) {
                                    viewBean.setTitreAPI(document.getTextes(4).getTexte(1).getDescription());
                                    viewBean.setParaAPI(document.getTextes(4).getTexte(3).getDescription());

                                } else if (REGenresPrestations.GENRE_83.equals(rd.getCodePrestation())
                                        || REGenresPrestations.GENRE_87.equals(rd.getCodePrestation())
                                        || REGenresPrestations.GENRE_93.equals(rd.getCodePrestation())
                                        || REGenresPrestations.GENRE_97.equals(rd.getCodePrestation())) {
                                    viewBean.setTitreAPI(document.getTextes(4).getTexte(1).getDescription());
                                    viewBean.setParaAPI(document.getTextes(4).getTexte(4).getDescription());
                                }
                            }
                        }
                    }

                    viewBean.setIdTiers(idTiers);
                    viewBean.setIdTiersBaseCalcul(idTiersBaseCalcul);

                    viewBean.setAdresse(adresse);
                    if (!codeIsoLangue.equals("DE")) {
                        viewBean.setTitre(titre + ", ");
                    } else {
                        viewBean.setTitre(titre);
                    }

                    viewBean.setTraiterPar(document.getTextes(6).getTexte(1).getDescription());
                    viewBean.setEMailAddress(mail);
                    viewBean.setDateImpressionAttJJMMAAA(dateAttJJMMAAAA);
                    viewBean.setCodeIsoLangue(codeIsoLangue);
                    viewBean.setPara1(document.getTextes(2).getTexte(1).getDescription());
                    viewBean.setSalutation(PRStringUtils.replaceString(
                            document.getTextes(5).getTexte(1).getDescription(), "{TitreSalutation}", titre));
                    viewBean.setAssure(document.getTextes(3).getTexte(1).getDescription());
                    viewBean.setPeriode(document.getTextes(3).getTexte(2).getDescription());
                    viewBean.setMontant(document.getTextes(3).getTexte(3).getDescription());
                    viewBean.setChf(document.getTextes(3).getTexte(5).getDescription());
                    viewBean.setTotal(document.getTextes(3).getTexte(4).getDescription());
                    viewBean.setConcerne(document.getTextes(1).getTexte(1).getDescription() + " " + anneeAttestation);
                    viewBean.setSousConcerne(tierswrap.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " "
                            + tierswrap.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tierswrap.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                    viewBean.setSignature(document.getTextes(7).getTexte(1).getDescription());
                    viewBean.setIsSendToGed("on".equals(request.getParameter("isSendToGed")));
                    viewBean.setAnneeAttestations(anneeAttestation);
                    session.setAttribute(FWServlet.VIEWBEAN, viewBean);
                }
            } catch (Exception e) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            }

            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        }

    }

    /**
     * Permet de trier les rentes.
     */
    private void sortRentes() {
        // On récupère toutes les infos nécessaires au tri.
        List<RERentesToCompare> rentes = new ArrayList<>();
        for (int i = 0 ; i < assure.size() ; i++) {
            String key = String.valueOf(i);
            RERentesToCompare eachRentes = new RERentesToCompare(assure.get(key), periode.get(key), montant.get(key), libelleRente.get(key), codePrestation.get(key));
            rentes.add(eachRentes);
        }
        // On trie les rentes.
        Collections.sort(rentes);

        // On réinitialise les maps.
        assure.clear();
        periode.clear();
        montant.clear();
        libelleRente.clear();
        codePrestation.clear();

        // On repeuple les maps suite au tri.
        for (int i = 0 ; i < rentes.size() ; i++) {
            String key = String.valueOf(i);
            assure.put(key,rentes.get(i).getAssure());
            periode.put(key,rentes.get(i).getPeriode());
            montant.put(key,rentes.get(i).getMontant());
            libelleRente.put(key,rentes.get(i).getLibelleRente());
        }
    }

    private String getCodeLangue(String codeIsoLangue, String anneeAttestation, BTransaction transaction,
            BSession bSession, PRTiersWrapper tierswrap, REAttestationFiscaleManager attmgr) throws Exception {
        String newCodeIsoLangue = codeIsoLangue;
        if (attmgr.getSize() > 0) {
            att = (REAttestationFiscale) attmgr.getEntity(0);

            REAttestationFiscaleRenteAccordeeManager mgr2 = new REAttestationFiscaleRenteAccordeeManager();
            mgr2.setSession(bSession);

            if (att.getNss().length() > 14) {
                mgr2.setLikeNumeroAVSNNSS("true");
            } else {
                mgr2.setLikeNumeroAVSNNSS("false");
            }

            mgr2.wantCallMethodBeforeFind(true);
            mgr2.setLikeNumeroAVS(att.getNss());
            mgr2.setRechercheFamille(true);
            // ddDroit <= DATE
            mgr2.setFromDateDebutDroit("12." + anneeAttestation);
            // dfDroit >= DATE
            mgr2.setToDateFinDroit("01." + anneeAttestation);
            mgr2.setForCsEtatNotIn(
                    IREPrestationAccordee.CS_ETAT_CALCULE + ", " + IREPrestationAccordee.CS_ETAT_AJOURNE);
            mgr2.setForOrderBy(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION + ", "
                    + RERenteAccJoinTblTiersJoinDemandeRente.FIELDNAME_NOM + ", "
                    + RERenteAccJoinTblTiersJoinDemandeRente.FIELDNAME_PRENOM + ", "
                    + REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);

            mgr2.find(transaction, BManager.SIZE_USEDEFAULT);
            for (int j = 0; j < mgr2.size(); j++) {
                REAttestationFiscaleRenteAccordee ra = (REAttestationFiscaleRenteAccordee) mgr2.getEntity(j);

                if (REGenresPrestations.GENRE_10.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_20.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_50.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_70.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_72.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_13.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_23.equals(ra.getCodePrestation())) {
                    // BZ 4507, je memorise l'adresse de paiement pour le tiers qui a une rente
                    // principale et qui n'est pas décédée
                    if (!att.getNss().equals(ra.getNumeroAvsBenef())) {
                        if (!JadeStringUtil.isEmpty(att.getDateDeces())) {
                            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(bSession,
                                    ISFSituationFamiliale.CS_DOMAINE_RENTES, att.getIdTiersBeneficiaire());
                            ISFMembreFamilleRequerant[] mbrsFam = sf.getMembresFamille(att.getIdTiersBeneficiaire());
                            for (ISFMembreFamilleRequerant mbrs : mbrsFam) {
                                if (att.getNss().equals(mbrs.getNss())
                                        && !ISFSituationFamiliale.CS_ETAT_CIVIL_DIVORCE.equals(mbrs.getCsEtatCivil())) {
                                    idTiersAdresseSiDeces = ra.getIdTiersBeneficiaire();
                                    break;
                                }
                            }

                        }
                    }
                }
            }

            // Chargement de l'idTiers et de l'adresse
            // BZ 4507, si la personne pour qui on génère une attestation
            // est décédée, j'utilise l'idtiers de la personne qui a la rente de
            // survivant pour autant qu'il y en ai une
            if (attmgr.getSize() > 0) {
                idTiers = att.getIdTiersBeneficiaire();
                idTiersBaseCalcul = att.getIdTiersBaseCalcul();
                if (JadeStringUtil.isEmpty(att.getDateDeces())) {
                    chargerAdresseEtTitre(idTiers, bSession, transaction);
                } else {
                    if (JadeStringUtil.isEmpty(idTiersAdresseSiDeces)) {
                        chargerAdresseEtTitre(idTiers, bSession, transaction);
                    } else {
                        chargerAdresseEtTitre(idTiersAdresseSiDeces, bSession, transaction);
                    }
                }
                newCodeIsoLangue = bSession.getCode(tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                newCodeIsoLangue = PRUtil.getISOLangueTiers(newCodeIsoLangue);
                documentHelper.setCodeIsoLangue(newCodeIsoLangue);
            }
        } else {
            ITITiers tiersTitre = (ITITiers) bSession.getAPIFor(ITITiers.class);
            Hashtable<String, String> params = new Hashtable<>();
            params.put(ITITiers.FIND_FOR_IDTIERS, tierswrap.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }
            idTiers = tiersTitre.getIdTiers();
            idTiersBaseCalcul = idTiers;
            chargerAdresseEtTitre(idTiers, bSession, transaction);

        }
        return newCodeIsoLangue;
    }

    private boolean IsMemeAdressePaiement(String idAdressePaiement1erRenteSurvivant, String idADressePaiement) {
        if (idAdressePaiement1erRenteSurvivant.equals(idADressePaiement)) {
            return true;
        }
        return false;
    }

    private void chargerAdresseEtTitre(final String idTiers, final BSession session, final BTransaction transaction)
            throws Exception {

        // Recherche de l'adresse et du titre du tiers. Si aucune adresse, un mail est envoyé à l'utilisateur
        tiersBeneficiaire = PRTiersHelper.getTiersAdresseParId(session, idTiers);
        if (tiersBeneficiaire == null) {
            tiersBeneficiaire = PRTiersHelper.getAdministrationParId(session, idTiers);
        }
        // BZ 5220, recherche de l'adresse en cascade en fonction du paramètre isWantAdresseCourrier se trouvant dans le
        // fichier corvus.properties
        adresse = PRTiersHelper.getAdresseCourrierFormateeRente(session, idTiers,
                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

        if (JadeStringUtil.isEmpty(adresse)) {
            RERenteAccJoinTblTiersJoinDemRenteManager renteAccManager = new RERenteAccJoinTblTiersJoinDemRenteManager();
            renteAccManager.setSession(session);

            String navs = tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

            if (navs.length() > 14) {
                renteAccManager.setLikeNumeroAVSNNSS("true");
            } else {
                renteAccManager.setLikeNumeroAVSNNSS("false");
            }

            renteAccManager.setLikeNumeroAVS(navs);
            renteAccManager.wantCallMethodBeforeFind(true);
            renteAccManager.setRechercheFamille(true);
            renteAccManager.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE);
            renteAccManager.find();

            // Liste Pour les rentes accordées non principale
            List<RERenteAccordee> listeMemeAdPmtNonPrincipale = new ArrayList<RERenteAccordee>();

            for (int i = 0; i < renteAccManager.size(); i++) {
                RERenteAccJoinTblTiersJoinDemandeRente elm = (RERenteAccJoinTblTiersJoinDemandeRente) renteAccManager
                        .get(i);

                REInformationsComptabilite ic = new REInformationsComptabilite();
                ic.setSession(session);
                ic.setIdInfoCompta(elm.getIdInfoCompta());
                ic.retrieve();

                // Uniquement les rentes accordées dont l'adresse de paiement est identique à l'écheance courante et
                // sans date de fin de droit
                if (elm.getIdTierAdressePmt().equals(ic.getIdTiersAdressePmt())
                        && JadeStringUtil.isEmpty(elm.getDateFinDroit())) {

                    RERenteAccordee ra = new RERenteAccordee();
                    ra.setSession(session);
                    ra.setIdPrestationAccordee(elm.getIdPrestationAccordee());
                    ra.retrieve();

                    // Si la rente est principale je l'utilise, sinon je l'insere dans la liste pour les rentes
                    // accordées non principale
                    if (ra.getCodePrestation().equals("10") || ra.getCodePrestation().equals("20")
                            || ra.getCodePrestation().equals("13") || ra.getCodePrestation().equals("23")
                            || ra.getCodePrestation().equals("50") || ra.getCodePrestation().equals("70")
                            || ra.getCodePrestation().equals("72")) {

                        REInformationsComptabilite infoCompt = ra.loadInformationsComptabilite();
                        tiersAdresse = PRTiersHelper.getTiersParId(session, infoCompt.getIdTiersAdressePmt());
                        if (tiersAdresse == null) {
                            tiersAdresse = PRTiersHelper.getAdministrationParId(session,
                                    infoCompt.getIdTiersAdressePmt());
                        }
                        // je recherche une adresse de courrier pour le tiers de l'adresse de paiement
                        adr = PRTiersHelper.getAdressePaiementData(session, null, infoCompt.getIdTiersAdressePmt(),
                                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                                JACalendar.todayJJsMMsAAAA());
                        // BZ 5220, recherche de l'adresse en cascade en fonction du paramètre isWantAdresseCourrier se
                        // trouvant dans le fichier corvus.properties
                        adresse = PRTiersHelper.getAdresseCourrierFormateeRente(session, adr.getIdTiers(),
                                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

                        // Si une adresse est trouvée, je recherche le titre du tiers del'adresse de paiement
                        if (!JadeStringUtil.isEmpty(adresse)) {
                            ITITiers tiersTitre = (ITITiers) session.getAPIFor(ITITiers.class);
                            Hashtable<String, String> params = new Hashtable<String, String>();
                            params.put(ITITiers.FIND_FOR_IDTIERS, adr.getIdTiers());
                            ITITiers[] t = tiersTitre.findTiers(params);
                            if ((t != null) && (t.length > 0)) {
                                tiersTitre = t[0];
                            }

                            titre = tiersTitre
                                    .getFormulePolitesse(tiersAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                            break;
                        }
                    } else {
                        listeMemeAdPmtNonPrincipale.add(ra);
                    }
                }
            }

            // Si aucune adresse n'est trouvée, je recherche dans la liste des rentes accordées non principale
            if (JadeStringUtil.isEmpty(adresse)) {
                if (!listeMemeAdPmtNonPrincipale.isEmpty()) {
                    for (RERenteAccordee ra : listeMemeAdPmtNonPrincipale) {
                        REInformationsComptabilite infoCompt = ra.loadInformationsComptabilite();
                        tiersAdresse = PRTiersHelper.getTiersParId(session, infoCompt.getIdTiersAdressePmt());
                        if (tiersAdresse == null) {
                            tiersAdresse = PRTiersHelper.getAdministrationParId(session,
                                    infoCompt.getIdTiersAdressePmt());
                        }

                        // je recherche une adresse de courrier pour le tiers de l'adresse de paiement
                        adr = PRTiersHelper.getAdressePaiementData(session, transaction,
                                infoCompt.getIdTiersAdressePmt(),
                                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                                JACalendar.todayJJsMMsAAAA());

                        // BZ 5220, recherche de l'adresse en cascade en fonction du paramètre isWantAdresseCourrier se
                        // trouvant dans le fichier corvus.properties
                        adresse = PRTiersHelper.getAdresseCourrierFormateeRente(session, adr.getIdTiers(),
                                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

                        // Si une adresse est trouvée, je recherche le titre du tiers de l'adresse de paiement
                        if (!JadeStringUtil.isEmpty(adresse)) {
                            ITITiers tiersTitre = (ITITiers) session.getAPIFor(ITITiers.class);
                            Hashtable<String, String> params = new Hashtable<String, String>();
                            params.put(ITITiers.FIND_FOR_IDTIERS, adr.getIdTiers());
                            ITITiers[] t = tiersTitre.findTiers(params);
                            if ((t != null) && (t.length > 0)) {
                                tiersTitre = t[0];
                            }

                            titre = tiersTitre
                                    .getFormulePolitesse(tiersAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                            break;
                        }
                    }
                }
            }
        } else {
            // Comme une adresse a été trouvée, je recherche le titre du tiers bénéficiaire
            ITITiers tiersTitre = (ITITiers) session.getAPIFor(ITITiers.class);
            Hashtable<String, String> params = new Hashtable<String, String>();
            params.put(ITITiers.FIND_FOR_IDTIERS, tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }
            titre = tiersTitre.getFormulePolitesse(tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_LANGUE));

            if (JadeStringUtil.isEmpty(titre)) {
                TITiers tiers = new TITiers();
                tiers.setSession(session);
                tiers.setIdTiers(idTiers);
                tiers.retrieve();

                TIAdresseDataSource Ads = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        IConstantes.CS_APPLICATION_DEFAUT, JACalendar.todayJJsMMsAAAA(), true);

                if (null != Ads) {
                    titre = Ads.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_TITRE);
                }
            }
        }

        // Si aucun titre n'est trouvé, je force un titre par défaut
        if (JadeStringUtil.isEmpty(titre)) {

            TIAdministrationManager tiAdminCaisseMgr = new TIAdministrationManager();
            tiAdminCaisseMgr.setSession(session);
            tiAdminCaisseMgr.setForCodeAdministration(
                    CaisseHelperFactory.getInstance().getNoCaisseFormatee(session.getApplication()));
            tiAdminCaisseMgr.setForGenreAdministration(CaisseHelperFactory.CS_CAISSE_COMPENSATION);
            tiAdminCaisseMgr.find();

            TIAdministrationViewBean tiAdminCaisse = (TIAdministrationViewBean) tiAdminCaisseMgr.getFirstEntity();

            String idTiersCaisse = "";
            if (tiAdminCaisse != null) {
                idTiersCaisse = tiAdminCaisse.getIdTiersAdministration();
            }

            // retrieve du tiers
            PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(session, idTiersCaisse);

            if (null == tier) {
                tier = PRTiersHelper.getAdministrationParId(session, idTiersCaisse);
            }

            // Insertion du titre du tiers
            ITITiers tiersTitre = (ITITiers) session.getAPIFor(ITITiers.class);
            Hashtable<String, String> params = new Hashtable<String, String>();
            params.put(ITITiers.FIND_FOR_IDTIERS, tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }
            titre = tiersTitre.getFormulePolitesse(tier.getProperty(PRTiersWrapper.PROPERTY_LANGUE));

        }
    }
}
