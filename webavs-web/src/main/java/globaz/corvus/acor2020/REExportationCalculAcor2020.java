package globaz.corvus.acor2020;

import acor.ch.admin.zas.rc.annoncesRente.types.*;
import ch.admin.zas.pool.PoolMeldungZurZAS;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.ObjectFactory;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.*;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.hera.business.constantes.ISFMembreFamille;
import ch.globaz.hera.business.constantes.ISFRelationConjoint;
import globaz.commons.nss.NSUtil;
import globaz.corvus.acor.adapter.plat.REACORDemandeAdapter;
import globaz.corvus.acor2020.business.*;
import globaz.corvus.acor2020.parser.ParserUtils;
import globaz.corvus.api.ci.IRERassemblementCI;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.ci.RERassemblementCI;
import globaz.corvus.db.ci.RERassemblementCIManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.historiques.REHistoriqueRentes;
import globaz.corvus.db.historiques.REHistoriqueRentesJoinTiersManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.ci.REInscriptionCIListViewBean;
import globaz.corvus.vb.ci.REInscriptionCIViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.externe.IPTConstantesExternes;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.*;
import globaz.hera.enums.TypeDeDetenteur;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.hera.wrapper.SFPeriodeWrapper;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.impl.PRNSS13ChiffresUtils;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressecourrier.TIPaysManager;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.util.TIAdresseResolver;
import globaz.webavs.common.CommonProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class REExportationCalculAcor2020 {

    private static final Logger LOG = LoggerFactory.getLogger(REExportationCalculAcor2020.class);


    public BSession session;
    public String idDemande;
    public BTransaction transaction;
    public REDemandeRente demandeRente;
    public PRTiersWrapper tiersRequerant;
    private ISFMembreFamilleRequerant membreRequerant;
    private HashMap relations = new HashMap();

    private HashMap idNoAVSBidons = new HashMap();
    private HashMap idNSSBidons = new HashMap();
    private static final String IS_WANT_ADRESSE_COURRIER = "isWantAdresseCourrier";

    private transient Marshaller marshaller;
    private static final String XSD_FOLDER = "/xsd/acor/xsd/";
    private static final String XSD_NAME = "acor-rentes-in-host.xsd";
    private static final String DD_MM_YYYY_FORMAT = "dd.MM.yyyy";
    public static final String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";
    private static final int ANTICIPATION_OR_REVOCATION = 100;
    private static final int AJOURNEMENT = 0;

    public REExportationCalculAcor2020(BSession bSession, String idDemandeRente) {
        session = bSession;
        idDemande = idDemandeRente;
        transaction = session.getCurrentThreadTransaction();
    }

    public InHostType createInHost() {
        LOG.info("Création du inHost.");
        InHostType inHost = new InHostType();
        try {
            demandeRente = REDemandeRente.loadDemandeRente(session, transaction, idDemande, null);
            tiersRequerant = demandeRente.loadDemandePrestation(null).loadTiers();

            inHost.setDemande(createDemande(demandeRente, tiersRequerant, session));
            List<ISFMembreFamilleRequerant> membresFamille = getToutesLesMembresFamillesEtEtendue();
            trouverRequerant(membresFamille);
            List<ISFMembreFamilleRequerant> membresCatAssures = filterListMembresAssures(membresFamille);
            List<ISFMembreFamilleRequerant> membresCatConjoints = filterListMembresConjoints(membresFamille);
            List<ISFMembreFamilleRequerant> membresCatExConjointsConjoints = filterListMembresExConjointConjoint(membresFamille);
            List<ISFMembreFamilleRequerant> membresCatEnfants = filterListMembresEnfants(membresFamille);

            inHost.getAssure().addAll(createListAssures(membresCatAssures));
            inHost.getEnfant().addAll(createListEnfants(membresCatEnfants));
            inHost.getFamille().addAll(createListFamilles(membresCatConjoints, membresCatExConjointsConjoints));
        } catch (Exception e) {
            LOG.error("Erreur lors de la construction du inHost.", e);
        }
        return inHost;
    }

    private List<ISFMembreFamilleRequerant> filterListMembresAssures(List<ISFMembreFamilleRequerant> membresFamille) {
        List<ISFMembreFamilleRequerant> assures = new ArrayList<>();
        for (ISFMembreFamilleRequerant membre : membresFamille) {
            if (!StringUtils.equals(membre.getRelationAuRequerant(), ISFMembreFamille.CS_TYPE_RELATION_ENFANT)) {
                assures.add(membre);
            }
        }
        return assures;
    }

    private List<ISFMembreFamilleRequerant> filterListMembresConjoints(List<ISFMembreFamilleRequerant> membresFamille) {
        List<ISFMembreFamilleRequerant> conjoints = new ArrayList<>();
        for (ISFMembreFamilleRequerant membre : membresFamille) {
            if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT, membre.getRelationAuRequerant())
                    && !StringUtils.equals(ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU, membre.getIdMembreFamille())) {
                conjoints.add(membre);
            }
        }
        return conjoints;
    }

    private List<ISFMembreFamilleRequerant> filterListMembresExConjointConjoint(List<ISFMembreFamilleRequerant> membresFamille) {
        List<ISFMembreFamilleRequerant> exConjointsConjoints = new ArrayList<>();
        for (ISFMembreFamilleRequerant membre : membresFamille) {
            if (StringUtils.equals(REACORDemandeAdapter.ImplMembreFamilleRequerantWrapper.NO_CS_RELATION_EX_CONJOINT_DU_CONJOINT,
                    membre.getRelationAuRequerant())) {
                exConjointsConjoints.add(membre);
            }
        }
        return exConjointsConjoints;
    }

    private List<ISFMembreFamilleRequerant> filterListMembresEnfants(List<ISFMembreFamilleRequerant> membresFamille) {
        List<ISFMembreFamilleRequerant> enfants = new ArrayList<>();
        for (ISFMembreFamilleRequerant membre : membresFamille) {
            if (StringUtils.equals(membre.getRelationAuRequerant(), ISFMembreFamille.CS_TYPE_RELATION_ENFANT)) {
                enfants.add(membre);
            }
        }
        return enfants;
    }

    public DemandeType createDemande(REDemandeRente demandeRente, PRTiersWrapper tiersRequerant, BSession session) {
        DemandeType demandeType = new DemandeType();

        // DONNEES FICHIER DEMANDES
        demandeType.setNavs(getNumAvsFromDemande(demandeRente, tiersRequerant, session));
        demandeType.setTypeDemande(formatTypeDemande(demandeRente.getCsTypeDemandeRente()));
        demandeType.setDateTraitement(getDateTraitement());
        demandeType.setDateDepot(getDateDepot(demandeRente.getDateDepot()));

        // NOUVELLES DONNES XSD OBLIGATOIRES
        demandeType.setCaisseAgence(getCaisseAgence(session));
        //TODO adresse de la caisse par des propriétés applications à créer
        demandeType.setAdresseCaisse(createAdresseCaisse());
        demandeType.setMoisRapport(0);
        demandeType.setLangue(StringUtils.lowerCase(session.getIdLangue()));
        demandeType.setTypeCalcul(getTypeCalcul(demandeRente.getCsTypeCalcul()));

        return demandeType;
    }

    private OrganisationAdresseType createAdresseCaisse() {
        OrganisationAdresseType adresseCaisse = new OrganisationAdresseType();
        //TODO
        adresseCaisse.setAdresse("test");
        adresseCaisse.setLocalite("test");
        adresseCaisse.setCodePostal("2300");
        adresseCaisse.setPays(100);
        adresseCaisse.setNom("");
        return adresseCaisse;
    }

    private Integer getCaisseAgence(BSession session) {
        Integer caisseAgence = 0;
        try {
            String noCaisse = session.getApplication().getProperty(CommonProperties.KEY_NO_CAISSE);
            String noAgence = session.getApplication().getProperty(CommonProperties.KEY_NO_AGENCE);
            caisseAgence = Integer.valueOf(noCaisse + noAgence);
        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération du numéro de la caisse.", e);
        }
        return caisseAgence;
    }

    private List<AssureType> createListAssures(List<ISFMembreFamilleRequerant> membresFamille) {
        List<AssureType> listAssures = new ArrayList<>();
        for (ISFMembreFamilleRequerant membre : membresFamille) {
            AssureType assure = createAssureType(membre);
            listAssures.add(assure);
        }
        return listAssures;
    }

    /**
     * @param membre
     * @return
     */
    private AssureType createAssureType(ISFMembreFamilleRequerant membre) {
        AssureType assure = new AssureType();
        assure.setNavs(getNssMembre(membre));
        assure.setNom(membre.getNom());
        assure.setPrenom(membre.getPrenom());
        assure.setDateNaissance(getDateNaissanceMembreAssure(membre));
        if (!JadeStringUtil.isBlankOrZero(membre.getDateDeces())) {
            assure.setDateDeces(ParserUtils.formatDate(membre.getDateDeces(), DD_MM_YYYY_FORMAT));
        }
        assure.setNationalite(getCodePays(membre.getCsNationalite()));
        assure.setDomicile(getDomicile(membre.getCsCantonDomicile(), membre.getPays(), tiersRequerant));
        assure.setRefugie(false);
        assure.setSexe(PRACORConst.csSexeToAcor2020(membre.getCsSexe()));
        // RENTES
        addRentesAssures(assure, membre);

        // Donnees AI
        if (StringUtils.equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE, demandeRente.getCsTypeDemandeRente())) {
            assure.setDonneesAI(createAiInformations((REDemandeRenteInvalidite) demandeRente));
            assure.setReductionFauteGrave(Short.valueOf(((REDemandeRenteInvalidite) demandeRente).getPourcentRedFauteGrave()));
        }

        // Anticipation ou ajournement
        if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT, membre.getRelationAuRequerant()) && demandeRente instanceof REDemandeRenteVieillesse) {
            FlexibilisationType flexibilisationType = createFlexibilisationType();
            if (Objects.nonNull(flexibilisationType)) {
                assure.setFlexibilisation(flexibilisationType);
            }
        }

        // EURO_FORM
        assure.setDonneesPostales(createDonneesPostales());

        // CI
        if (!JadeStringUtil.isBlankOrZero(membre.getIdTiers())) {
            assure.getCi().addAll(createListCI(membre.getIdTiers()));
        }

        // PERIODES
        addPeriodesAssure(assure, membre);
        return assure;
    }

    /**
     * Création d'une flexibisation si on est sur un ajournement, une anticipation ou une révocation (rente vieillesse)
     *
     * @return la flexibilisation s'il y a un ajournement, une anticipation ou une révocation. Null sinon.
     */
    private FlexibilisationType createFlexibilisationType() {
        FlexibilisationType flexibilisationType = null;
        String anticipation = ((REDemandeRenteVieillesse) demandeRente).getCsAnneeAnticipation();
        // Anticipation
        if (StringUtils.equals(IREDemandeRente.CS_ANNEE_ANTICIPATION_2ANNEES, anticipation) || StringUtils.equals(IREDemandeRente.CS_ANNEE_ANTICIPATION_1ANNEE, anticipation)) {
            flexibilisationType = new FlexibilisationType();
            flexibilisationType.setDebut(ParserUtils.formatDate(demandeRente.getDateDebut(), DD_MM_YYYY_FORMAT));
            flexibilisationType.setPartPercue(ANTICIPATION_OR_REVOCATION); // Pour une anticipation
        }
        boolean ajournement = ((REDemandeRenteVieillesse) demandeRente).getIsAjournementRequerant();
        String dateRevocation = ((REDemandeRenteVieillesse) demandeRente).getDateRevocationRequerant();
        if (ajournement) {
            flexibilisationType = new FlexibilisationType();
            // Revocation
            if (StringUtils.isNotEmpty(dateRevocation)) {
                flexibilisationType.setDebut(ParserUtils.formatDate(dateRevocation, DD_MM_YYYY_FORMAT));
                flexibilisationType.setPartPercue(ANTICIPATION_OR_REVOCATION); // Pour un ajournement révoqué
            } else {
                // Ajournement
                flexibilisationType.setDebut(ParserUtils.formatDate(demandeRente.getDateDebut(), DD_MM_YYYY_FORMAT));
                flexibilisationType.setPartPercue(AJOURNEMENT); // Pour un ajournement demandé
            }
        }
        return flexibilisationType;
    }

    private void addRentesAssures(AssureType assure, ISFMembreFamilleRequerant membre) {
        try {
            for (REHistoriqueRentes rente : getRentesEnCours(membre.getIdTiers())) {
                if (StringUtils.equals(IREDemandeRente.REVISION_10EME_REVISION, rente.getDroitApplique())) {
                    if (isRenteExtraordinaire(rente.getCodePrestation())) {
                        assure.getRenteExtraordinaire10().add(createRenteExtraordinaire10(rente));
                    } else {
                        assure.getRenteOrdinaire10().add(createRenteOrdinaire10(rente));
                    }
                } else if (StringUtils.equals(IREDemandeRente.REVISION_9EME_REVISION, rente.getDroitApplique())) {
                    if (isRenteExtraordinaire(rente.getCodePrestation())) {
                        assure.getRenteExtraordinaire9().add(createRenteExtraordinaire9(rente));
                    } else {
                        assure.getRenteOrdinaire9().add(createRenteOrdinaire9(rente));
                    }
                } else {
                    LOG.info("Révision différente de 10 ou 9 (IDRente: " + rente.getIdRenteAccordee() + "/ Révision : " + rente.getDroitApplique() + ")");
                }
            }
        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération des rentes en cours.", e);
        }
    }

    private boolean isRenteExtraordinaire(String codePrestation) {
        Integer intCodePrestation = ParserUtils.formatRequiredInteger(codePrestation);
        if (intCodePrestation >= 20 && intCodePrestation <= 29) {
            return true;
        }
        if (intCodePrestation >= 40 && intCodePrestation <= 49) {
            return true;
        }
        if (intCodePrestation >= 70 && intCodePrestation <= 79) {
            return true;
        }
        return false;
    }

    private CommonRenteType createCommonRenteType(REHistoriqueRentes rente, boolean is10emeRevision, boolean isOrdinaire) {
        CommonRenteType commonRente;
        if (is10emeRevision && isOrdinaire) {
            commonRente = new RenteOrdinaire10Type();
        } else if (is10emeRevision && !isOrdinaire) {
            commonRente = new RenteExtraordinaire10Type();
        } else if (!is10emeRevision && isOrdinaire) {
            commonRente = new RenteOrdinaire9Type();
        } else {
            commonRente = new RenteExtraordinaire9Type();
        }

        // 2. genre de prestation
        commonRente.setGenre(ParserUtils.formatRequiredInteger(rente.getCodePrestation()));
        // Non mappé -> mettre false par défaut
        commonRente.setIndemniteForfaitaire(false);
        // 3. fraction de rente
        commonRente.setFraction(FractionRente.getValueFromConst((rente.getFractionRente())));
        // 4. date début du droit
        commonRente.setDebutDroit(ParserUtils.formatDate(rente.getDateDebutDroit(), "MM.yyyy"));
        // 5. date fin du droit
        commonRente.setFinDroit(ParserUtils.formatDate(rente.getDateFinDroit(), "MM.yyyy"));
        if (commonRente.getFinDroit() != null) {
            // Non utilisé
            // TODO : valeur 0 non valide (Enum Mutationscode_Type dans Prestations-types.xsd) ->
//          // TODO : information contenu dans la rente accordée : RERenteAccordee --> YLLCMU
//            commonRente.setCodeMutation(0);
        }
        // 6. montant de la prestation
        commonRente.setMontant(ParserUtils.formatRequiredBigDecimalNoDecimal(rente.getMontantPrestation()));
        // 38. année du montant du ram
        commonRente.setAnneeEtat(ParserUtils.formatDate(rente.getAnneeMontantRAM(), "yyyy"));
//        // 17. code cas spécial
        if (!JadeStringUtil.isBlankOrZero((rente.getCs1()))) {
            commonRente.getCasSpecial().add(ParserUtils.formatRequiredShort(rente.getCs1()));
        }
        if (!JadeStringUtil.isBlankOrZero((rente.getCs2()))) {
            commonRente.getCasSpecial().add(ParserUtils.formatRequiredShort(rente.getCs2()));
        }
        if (!JadeStringUtil.isBlankOrZero((rente.getCs3()))) {
            commonRente.getCasSpecial().add(ParserUtils.formatRequiredShort(rente.getCs3()));
        }
        if (!JadeStringUtil.isBlankOrZero((rente.getCs4()))) {
            commonRente.getCasSpecial().add(ParserUtils.formatRequiredShort(rente.getCs4()));
        }
        if (!JadeStringUtil.isBlankOrZero((rente.getCs5()))) {
            commonRente.getCasSpecial().add(ParserUtils.formatRequiredShort(rente.getCs5()));
        }

        return commonRente;
    }

    private RenteOrdinaire10Type createRenteOrdinaire10(REHistoriqueRentes rente) {
        RenteOrdinaire10Type renteOrdinaire10 = (RenteOrdinaire10Type) createCommonRenteType(rente, true, true);
        renteOrdinaire10.setBases(createOrdinaireBase10(rente));
        // 32. code survivant
        renteOrdinaire10.setCodeSurvivantInvalide(rente.getIsSurvivantInvalid());
        // 41. transféré
        renteOrdinaire10.setTransferee(rente.getIsTransfere());
        return renteOrdinaire10;
    }

    private RenteExtraordinaire10Type createRenteExtraordinaire10(REHistoriqueRentes rente) {
        RenteExtraordinaire10Type renteExtraordinaire10 = (RenteExtraordinaire10Type) createCommonRenteType(rente, true, false);
        renteExtraordinaire10.setBases(createExtraordinaireBase10(rente));
        // 32. code survivant
        renteExtraordinaire10.setCodeSurvivantInvalide(rente.getIsSurvivantInvalid());
        // 41. transféré
        renteExtraordinaire10.setTransferee(rente.getIsTransfere());
        return renteExtraordinaire10;
    }

    private RenteOrdinaire9Type createRenteOrdinaire9(REHistoriqueRentes rente) {
        RenteOrdinaire9Type renteOrdinaire9 = (RenteOrdinaire9Type) createCommonRenteType(rente, false, true);
        renteOrdinaire9.setBases(createOrdinaireBase9(rente));
        return renteOrdinaire9;
    }

    private RenteExtraordinaire9Type createRenteExtraordinaire9(REHistoriqueRentes rente) {
        RenteExtraordinaire9Type renteExtraordinaire9 = (RenteExtraordinaire9Type) createCommonRenteType(rente, false, false);
        renteExtraordinaire9.setBases(createExtraordinaireBase9(rente));
        return renteExtraordinaire9;
    }

    private OrdinaireBase10Type createOrdinaireBase10(REHistoriqueRentes rente) {
        OrdinaireBase10Type base = new OrdinaireBase10Type();
        // 7 + 8 + 31
        base.setDonneesRam(createRam10(rente));
        // 10 - 15 + 39 + 40
        base.setDonneesEchelle(createDonnesEchelleType(rente));
        // 16. année de niveau
        String anneeNiveau = formatAnneeNiveau(rente.getAnneeNiveau());
        base.setAnneeNiveau(ParserUtils.formatDate(anneeNiveau, "yyyy"));
        // 19 - 23
        base.setDonneesAI(createAiType(rente));
        // 24 - 26 + 33 - 35
        base.setDonneesAgeFlexible(createAgeFlexible10(rente));
        // 27 - 30
        base.setDonneesBonifications(createDonneesBonification10(rente));
        return base;
    }

    private OrdinaireBase9Type createOrdinaireBase9(REHistoriqueRentes rente) {
        OrdinaireBase9Type base = new OrdinaireBase9Type();
        // 7 + 8 + 31
        base.setDonneesRam(createRam9(rente));
        // 10 - 15 + 39 + 40
        base.setDonneesEchelle(createDonnesEchelleType(rente));
        // 16. année de niveau
        String anneeNiveau = formatAnneeNiveau(rente.getAnneeNiveau());
        base.setAnneeNiveau(ParserUtils.formatDate(anneeNiveau, "yyyy"));
        // 19 - 23
        base.setDonneesAI(createAiType(rente));
        // 24 - 26 + 33 - 35
        base.setDonneesAgeFlexible(createCommonAgeFlexible(rente));
        // 27 - 30
        base.setDonneesBonifications(createDonneesBonification9(rente, base.getDonneesRam().getDurchschnittlichesJahreseinkommen()));
        return base;
    }

    private ExtraordinaireBase10Type createExtraordinaireBase10(REHistoriqueRentes rente) {
        ExtraordinaireBase10Type base = new ExtraordinaireBase10Type();
        // 16. année de niveau
        String anneeNiveau = formatAnneeNiveau(rente.getAnneeNiveau());
        base.setAnneeNiveau(ParserUtils.formatDate(anneeNiveau, "yyyy"));
        // 19 - 23
        base.setDonneesAI(createAiType(rente));
        return base;
    }

    private ExtraordinaireBase9Type createExtraordinaireBase9(REHistoriqueRentes rente) {
        ExtraordinaireBase9Type base = new ExtraordinaireBase9Type();
        // 7 + 8 + 31
        base.setDonneesRam(createRam9(rente));
        // 10 - 15 + 39 + 40
        base.setDonneesEchelle(createDonnesEchelleType(rente));
        // 16. année de niveau
        String anneeNiveau = formatAnneeNiveau(rente.getAnneeNiveau());
        base.setAnneeNiveau(ParserUtils.formatDate(anneeNiveau, "yyyy"));
        // 19 - 23
        base.setDonneesAI(createAiType(rente));
        // 27 - 30
        base.setDonneesBonifications(createDonneesBonification9(rente, base.getDonneesRam().getDurchschnittlichesJahreseinkommen()));
        return base;
    }

    private AgeFlexible10 createAgeFlexible10(REHistoriqueRentes rente) {
        AgeFlexible10 ageFlexible = new AgeFlexible10();
        // 24 - 26
        ageFlexible.setDonneesAjournement(createAjournement(rente));
        //33 - 35
        ageFlexible.setDonneesAnticipation(createAnticipation(rente));
        if (isToutesDonneesManquantes(ageFlexible.getDonneesAjournement(), ageFlexible.getDonneesAnticipation())) {
            return null;
        }
        return ageFlexible;
    }

    private CommonAgeFlexible createCommonAgeFlexible(REHistoriqueRentes rente) {
        CommonAgeFlexible ageFlexible = new CommonAgeFlexible();
        // 24 - 26
        ageFlexible.setDonneesAjournement(createAjournement(rente));
        if (isDonneeManquante(ageFlexible.getDonneesAjournement())) {
            return null;
        }
        return ageFlexible;
    }

    private DJE10BeschreibungType createRam10(REHistoriqueRentes rente) {
        DJE10BeschreibungType ram = new DJE10BeschreibungType();
        // 7. revenu annuel moyen
        ram.setDurchschnittlichesJahreseinkommen(ParserUtils.formatRequiredBigDecimalNoDecimal(rente.getRam()));
        // 8. durée cotisation ram
        ram.setBeitragsdauerDurchschnittlichesJahreseinkommen(ParserUtils.formatRequiredBigDecimal(rente.getDureeCotRam()));
        // 31. code rev. splitté
        ram.setGesplitteteEinkommen(rente.getIsRevenuSplitte());
        return ram;
    }

    private DJE9BeschreibungType createRam9(REHistoriqueRentes rente) {
        DJE9BeschreibungType ram = new DJE9BeschreibungType();
        // 7. revenu annuel moyen
        ram.setDurchschnittlichesJahreseinkommen(ParserUtils.formatRequiredBigDecimalNoDecimal(rente.getRam()));
        // 8. durée cotisation ram
        ram.setBeitragsdauerDurchschnittlichesJahreseinkommen(ParserUtils.formatRequiredBigDecimal(rente.getDureeCotRam()));
//        TODO : valeur 0 non valide -> enum  1,2,3 (code revenu pris dans Prestations9-types.xsd) Le code revenu ne devrait pas être vide.
        ram.setAngerechneteEinkommen((short) 1);
//        ram.setAngerechneteEinkommen(formatRequiredShort(rente.getCodeRevenu()));
        return ram;
    }

    private RentenvorbezugType createAnticipation(REHistoriqueRentes rente) {
        RentenvorbezugType anticipation = new RentenvorbezugType();
        // 33. nbr. année anticipation
        if (!JadeStringUtil.isBlankOrZero(rente.getNbrAnneeAnticipation())) {
            anticipation.setAnzahlVorbezugsjahre(ParserUtils.formatRequiredInteger(rente.getNbrAnneeAnticipation()));
        }
        // 34. montant redic. pour anticipation
        if (!JadeStringUtil.isBlankOrZero(rente.getMontantReducAnticipation())) {
            anticipation.setVorbezugsreduktion(ParserUtils.formatRequiredBigDecimalNoDecimal(rente.getMontantReducAnticipation()));
        }
        // 35. date déb. anticipation
        anticipation.setVorbezugsdatum(ParserUtils.formatDate(rente.getDateDebutAnticipation(), "MM.yyyy"));
        if (isAuMoinsUneDonneeObligatoireManquante(anticipation.getAnzahlVorbezugsjahre(), anticipation.getVorbezugsreduktion(), anticipation.getVorbezugsdatum())) {
            return null;
        }
        return anticipation;
    }

    private Gutschriften10Type createDonneesBonification10(REHistoriqueRentes rente) {
        Gutschriften10Type donneesBonification = new Gutschriften10Type();
        // 28. nbr. année bonif. bte
        donneesBonification.setAnzahlErziehungsgutschrift(ParserUtils.formatRequiredBigDecimal(rente.getNbrAnneeBTE()));
        // 29. nbr. année bonif. bta
        donneesBonification.setAnzahlBetreuungsgutschrift(ParserUtils.formatRequiredBigDecimal(rente.getNbrAnneeBTA()));
        // 30. nbr. année bonif. transitoire
        donneesBonification.setAnzahlUebergangsgutschrift(ParserUtils.formatRequiredBigDecimal(rente.getNbrAnneeBTR()));
        return donneesBonification;
    }

    private Gutschriften9Type createDonneesBonification9(REHistoriqueRentes rente, BigDecimal durchschnittlichesJahreseinkommen) {
        Gutschriften9Type donneesBonification = new Gutschriften9Type();
        // 27. montant bonus éducatif
        BigDecimal montantBTE = ParserUtils.formatRequiredBigDecimal(rente.getMontantBTE());
        donneesBonification.setAngerechneteErziehungsgutschrift(montantBTE.setScale(0, BigDecimal.ROUND_DOWN));
        // 28. nbr. année bonif. bte
        if (!JadeStringUtil.isBlankOrZero(rente.getNbrAnneeBTE())) {
            donneesBonification.setAnzahlErziehungsgutschrift(ParserUtils.formatRequiredShort(rente.getNbrAnneeBTE().substring(0, 2)));
        } else {
            donneesBonification.setAnzahlErziehungsgutschrift(new Short("0"));
        }
        // Numéro 7 - Numéro 27
        BigDecimal montant = durchschnittlichesJahreseinkommen.subtract(donneesBonification.getAngerechneteErziehungsgutschrift());
        donneesBonification.setDJEohneErziehungsgutschrift(montant.setScale(0,BigDecimal.ROUND_DOWN));
        return donneesBonification;
    }

    private RentenaufschubType createAjournement(REHistoriqueRentes rente) {
        RentenaufschubType ajournement = new RentenaufschubType();
        // 24. durée ajournement
        if (!JadeStringUtil.isBlankOrZero(rente.getDureeAjournement())) {
            ajournement.setAufschubsdauer(ParserUtils.formatRequiredBigDecimal(rente.getDureeAjournement()));
        }
        // 25. supplément ajournement
        if (!JadeStringUtil.isBlankOrZero(rente.getSupplementAjournement())) {
            ajournement.setAufschubszuschlag(ParserUtils.formatRequiredBigDecimalNoDecimal(rente.getSupplementAjournement()));
        }
        // 26. date de révocation
        String date = rente.getDateRevocationAjournement();
        if (JadeStringUtil.isBlank(date) && rente.getIsRenteAjournee()) {
            date = "99.9999";
        }
        if (date.length() < 6) {
            date = 0 + date;
        }
        ajournement.setAbrufdatum(ParserUtils.formatDate(date, "MM.yyyy"));

        if (isAuMoinsUneDonneeObligatoireManquante(ajournement.getAufschubsdauer(), ajournement.getAbrufdatum(), ajournement.getAufschubszuschlag())) {
            return null;
        }
        return ajournement;
    }

    private boolean isDonneeManquante(Object obj1) {
        return Objects.isNull(obj1);
    }

    private boolean isToutesDonneesManquantes(Object obj1, Object obj2) {
        return Objects.isNull(obj1) && Objects.isNull(obj2);
    }

    private boolean isAuMoinsUneDonneeObligatoireManquante(Object obj1, Object obj2, Object obj3) {
        return Objects.isNull(obj1) || Objects.isNull(obj2) || Objects.isNull(obj3);
    }

    private AiType createAiType(REHistoriqueRentes rente) {
        AiType donnesAi = new AiType();
        // 19. degré d'invalidité
        donnesAi.setInvaliditaetsgrad(ParserUtils.formatRequiredShort(rente.getDegreInvalidite()));
        // 20. clé infirmité + atteinte fonctionnelle
        Integer cleInfirmite = getCleInfirmite(rente.getCleInfirmiteAtteinteFct());
        if (cleInfirmite != null) {
            donnesAi.setGebrechensschluessel(cleInfirmite);
        }
        Short atteinteFct = getAtteinteFct(rente.getCleInfirmiteAtteinteFct());
        if (atteinteFct != null) {
            donnesAi.setFunktionsausfallcode(atteinteFct);
        }
        // 21. survenance évén. assure
        donnesAi.setDatumVersicherungsfall(ParserUtils.formatDate(rente.getSurvenanceEvenementAssure(), "MM.yyyy"));
        // 22. invalide précoce
        donnesAi.setIstFruehInvalid(rente.getIsInvaliditePrecoce());
        // 23. office ai
        donnesAi.setIVStelle(rente.getOfficeAI());
        //TODO faut-il tester tous les champs pour savoir si donneesAI doit être remplies ?
        if (JadeStringUtil.isBlankOrZero(donnesAi.getIVStelle())) {
            return null;
        }
        return donnesAi;
    }

    /**
     * Créations des informations AI.
     *
     * @param demandeRenteInvalidite : demande de rente invalidité.
     * @return les informations AI.
     */
    private AiInformations createAiInformations(REDemandeRenteInvalidite demandeRenteInvalidite) {
        AiInformations aiInformations = new AiInformations();
        try {
            for (REPeriodeInvalidite periode : demandeRenteInvalidite.getPeriodesInvalidite()) {
                AiCurrentType periodeAI = new AiCurrentType();
                periodeAI.setIVStelle(demandeRenteInvalidite.getCodeOfficeAI());
                periodeAI.setInvaliditaetsgrad(Short.valueOf(periode.getDegreInvalidite())); // degré invalidité
                periodeAI.setGebrechensschluessel(Integer.valueOf(getSession().getCode(demandeRenteInvalidite.getCsInfirmite()))); // Genre infirmité
                periodeAI.setFunktionsausfallcode(Short.valueOf(getSession().getCode(demandeRenteInvalidite.getCsAtteinte()))); // atteinte fonctionnelle
                periodeAI.setDebutInvalidite(ParserUtils.formatDate(periode.getDateDebutInvalidite(), DD_MM_YYYY_FORMAT));
                periodeAI.setFinInvalidite(ParserUtils.formatDate(periode.getDateFinInvalidite(), DD_MM_YYYY_FORMAT));
                aiInformations.getPeriodeAI().add(periodeAI);
            }
        } catch (Exception e) {
            LOG.error("Impossible de récupérer les périodes d'invalidité.", e);
        }
        if (!JadeStringUtil.isBlankOrZero(demandeRenteInvalidite.getPourcentRedNonCollaboration())) {
            AiInformations.DonneesNonCollaboration donneesNonCollaboration = new AiInformations.DonneesNonCollaboration();
            donneesNonCollaboration.setPartReduction(Integer.valueOf(demandeRenteInvalidite.getPourcentRedNonCollaboration()));
            donneesNonCollaboration.setDebut(ParserUtils.formatDate(demandeRenteInvalidite.getDateDebutRedNonCollaboration(), DD_MM_YYYY_FORMAT));
            donneesNonCollaboration.setFin(ParserUtils.formatDate(demandeRenteInvalidite.getDateFinRedNonCollaboration(), DD_MM_YYYY_FORMAT));
            aiInformations.getDonneesNonCollaboration().add(donneesNonCollaboration);
        }
        return aiInformations;
    }

    private Integer getCleInfirmite(String cleInfirmiteAtteinteFct) {
        if (!JadeStringUtil.isBlankOrZero(cleInfirmiteAtteinteFct) && cleInfirmiteAtteinteFct.length() >= 3) {
            return ParserUtils.formatRequiredInteger(cleInfirmiteAtteinteFct.substring(0, 3));
        }
        return null;
    }

    private Short getAtteinteFct(String cleInfirmiteAtteinteFct) {
        if (!JadeStringUtil.isBlankOrZero(cleInfirmiteAtteinteFct) && cleInfirmiteAtteinteFct.length() == 5) {
            return ParserUtils.formatRequiredShort(cleInfirmiteAtteinteFct.substring(3, 5));
        }
        return null;
    }

    private String formatAnneeNiveau(String anneeNiveau) {
        if ("00".equals(anneeNiveau)) {
            anneeNiveau = "2000";
        } else if (JadeStringUtil.isBlankOrZero(anneeNiveau)) {
            anneeNiveau = "0000";
        } else {
            if (Integer.parseInt(anneeNiveau) < 60) {
                anneeNiveau = "20" + anneeNiveau;
            } else {
                anneeNiveau = "19" + anneeNiveau;
            }
        }
        return anneeNiveau;
    }

    private DonneesEchelleType createDonnesEchelleType(REHistoriqueRentes rente) {
        DonneesEchelleType donnesEchelle = new DonneesEchelleType();
        // 10. echelle de rente
        donnesEchelle.setSkala(ParserUtils.formatRequiredShort(rente.getEchelle()));
        // 11. durée cotisation avant 73
        donnesEchelle.setDureeEtrangereAvant73(ParserUtils.formatRequiredBigDecimalDuree(rente.getDureeCotiEtrangereAv73()));
        // 12. durée cotisation après 73
        donnesEchelle.setDureeEtrangereApres73(ParserUtils.formatRequiredBigDecimalDuree(rente.getDureeCotiEtrangereAp73()));
        // 13. mois appoint avant 73
        donnesEchelle.setAnrechnungVor1973FehlenderBeitragsmonate(ParserUtils.formatRequiredInteger(rente.getMoisAppointAv73()));
        // 14. mois appoint après 73
        donnesEchelle.setAnrechnungAb1973Bis1978FehlenderBeitragsmonate(ParserUtils.formatRequiredInteger(rente.getMoisAppointAp73()));
        // 15. durée cotis. de la classe d'age
        donnesEchelle.setBeitragsjahreJahrgang(ParserUtils.formatRequiredInteger(rente.getDureeCotiClasseAge()));
        // 39. durée cotis av. 73
        donnesEchelle.setBeitragsdauerVor1973(ParserUtils.formatRequiredBigDecimalDuree(rente.getDureeCotAv73()));
        // 40.durée cotis ap. 73
        donnesEchelle.setBeitragsdauerAb1973(ParserUtils.formatRequiredBigDecimalDuree(rente.getDureeCotAp73()));
        return donnesEchelle;
    }

    private void addPeriodesAssure(AssureType assure, ISFMembreFamilleRequerant membre) {
        for (ISFPeriode isfPeriode : recupererPeriodesMembre(membre)) {
            if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_IJ, isfPeriode.getType())) {
                assure.getPeriodeIJ().add(createPeriodeIJ(isfPeriode));
            } else if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_TRAVAILLE, isfPeriode.getType())) {
                assure.getPeriodeTravail().add(createPeriodeTravailType(isfPeriode));
            } else if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ASSURANCE_ETRANGERE, isfPeriode.getType())) {
                assure.getPeriodeEtrangere().add(createPeriodeEtrangerType(isfPeriode));
            } else if (getEnumPeriodeType(isfPeriode) != null) {
                assure.getPeriode().add(createPeriodeType(isfPeriode));
            }
        }
    }

    private PeriodeTypeEnum getEnumPeriodeType(ISFPeriode isfPeriode) {
        switch (isfPeriode.getType()) {
            case ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE:
                return PeriodeTypeEnum.DOMICILE;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_NATIONALITE:
                return PeriodeTypeEnum.NATIONALITE;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_INCARCERATION:
                return PeriodeTypeEnum.INCARCERATION;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE:
                return PeriodeTypeEnum.ETUDE;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_AFFILIATION:
                return PeriodeTypeEnum.AFAC;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_COTISATION:
                return PeriodeTypeEnum.EXEMPTION;
            default:
                return null;
        }
    }

    private PeriodeEnfantTypeEnum getEnumPeriodeEnfantType(ISFPeriode isfPeriode) {
        switch (isfPeriode.getType()) {
            case ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE:
                return PeriodeEnfantTypeEnum.DOMICILE;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_NATIONALITE:
                return PeriodeEnfantTypeEnum.NATIONALITE;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE:
                return PeriodeEnfantTypeEnum.ETUDE;
            default:
                return null;
        }
    }

    private PeriodeBTEType createPeriodeBTE(ISFPeriode isfPeriode) {
        PeriodeBTEType periode = new PeriodeBTEType();
        periode.setDebut(ParserUtils.formatDate(isfPeriode.getDateDebut(), DD_MM_YYYY_FORMAT));
        periode.setFin(ParserUtils.formatDate(isfPeriode.getDateFin(), DD_MM_YYYY_FORMAT));
        if (StringUtils.isNotEmpty(isfPeriode.getNoAvsDetenteurBTE())) {
            periode.setEducateur(Long.valueOf(NSUtil.unFormatAVS(isfPeriode.getNoAvsDetenteurBTE())));
        } else {
            periode.setTiers(true);
        }
        return periode;
    }

    private PeriodeRecueilliGType createPeriodeRecueilliG(ISFPeriode isfPeriode) {
        PeriodeRecueilliGType periode = new PeriodeRecueilliGType();
        periode.setDebut(ParserUtils.formatDate(isfPeriode.getDateDebut(), DD_MM_YYYY_FORMAT));
        periode.setFin(ParserUtils.formatDate(isfPeriode.getDateFin(), DD_MM_YYYY_FORMAT));
        if (StringUtils.equals(String.valueOf(TypeDeDetenteur.TUTEUR_LEGAL.getCodeSystem()), isfPeriode.getCsTypeDeDetenteur())) {
            periode.setTuteur(true);
        } else {
            periode.setTuteur(false);
        }
        return periode;
    }

    private PeriodeRecueilliCType createPeriodeRecueilliC(ISFPeriode isfPeriode) {
        // TODO : identifier quand on est sur un cas recueilliC
        PeriodeRecueilliCType periode = new PeriodeRecueilliCType();
        periode.setDebut(ParserUtils.formatDate(isfPeriode.getDateDebut(), DD_MM_YYYY_FORMAT));
        periode.setFin(ParserUtils.formatDate(isfPeriode.getDateFin(), DD_MM_YYYY_FORMAT));
        //TODO voir quoi mettre
//        periode.setParentNonBiologique();
        return periode;
    }

    private PeriodeTravailType createPeriodeTravailType(ISFPeriode isfPeriode) {
        PeriodeTravailType periode = new PeriodeTravailType();
        periode.setDebut(ParserUtils.formatDate(isfPeriode.getDateDebut(), DD_MM_YYYY_FORMAT));
        periode.setFin(ParserUtils.formatDate(isfPeriode.getDateFin(), DD_MM_YYYY_FORMAT));
        //Mettre à 0 car il n'existe pas sur WebAVS
        periode.setMontantDebut(BigDecimal.ZERO);
        return periode;
    }

    private PeriodeEtrangereType createPeriodeEtrangerType(ISFPeriode isfPeriode) {
        PeriodeEtrangereType periode = new PeriodeEtrangereType();
        periode.setDebut(ParserUtils.formatDate(isfPeriode.getDateDebut(), DD_MM_YYYY_FORMAT));
        periode.setFin(ParserUtils.formatDate(isfPeriode.getDateFin(), DD_MM_YYYY_FORMAT));
        periode.setEtat(ParserUtils.formatRequiredInteger(isfPeriode.getPays()));
        return periode;
    }

    private PeriodeType createPeriodeType(ISFPeriode isfPeriode) {
        PeriodeType periode = new PeriodeType();
        periode.setDebut(ParserUtils.formatDate(isfPeriode.getDateDebut(), DD_MM_YYYY_FORMAT));
        periode.setFin(ParserUtils.formatDate(isfPeriode.getDateFin(), DD_MM_YYYY_FORMAT));
        periode.setType(getEnumPeriodeType(isfPeriode));
        return periode;
    }

    private PeriodeIJType createPeriodeIJ(ISFPeriode isfPeriode) {
        PeriodeIJType periode = new PeriodeIJType();
        periode.setDebut(ParserUtils.formatDate(isfPeriode.getDateDebut(), DD_MM_YYYY_FORMAT));
        periode.setFin(ParserUtils.formatDate(isfPeriode.getDateFin(), DD_MM_YYYY_FORMAT));
        //Par défaut false
        periode.setMesureNouvelle(false);
        return periode;
    }

    private PeriodeEnfantType createPeriodeEnfantType(ISFPeriode isfPeriode) {
        PeriodeEnfantType periode = new PeriodeEnfantType();
        periode.setDebut(ParserUtils.formatDate(isfPeriode.getDateDebut(), DD_MM_YYYY_FORMAT));
        periode.setFin(ParserUtils.formatDate(isfPeriode.getDateFin(), DD_MM_YYYY_FORMAT));
        periode.setType(getEnumPeriodeEnfantType(isfPeriode));
        return periode;
    }


    private ISFPeriode[] recupererPeriodesMembre(ISFMembreFamilleRequerant membre) {
        ISFPeriode[] periodes;
        try {
            ISFPeriode[] periodesToFilre = situationFamiliale().getPeriodes(membre.getIdMembreFamille());

            // On filtre les période qui ne sont pas connues d'ACOR
            List<ISFPeriode> listPeriode = new ArrayList<ISFPeriode>();
            for (int i = 0; i < periodesToFilre.length; i++) {
                if (!getTypePeriode(periodesToFilre[i]).isEmpty()) {
                    listPeriode.add(periodesToFilre[i]);
                }
            }
            periodes = listPeriode.toArray(new ISFPeriode[listPeriode.size()]);

            // si demande survivant
            if (getTypeDemande().equals(PRACORConst.CA_TYPE_DEMANDE_SURVIVANT)) {
                // si membre = requérant
                PRDemande demandePrest = new PRDemande();
                demandePrest.setSession(getSession());
                demandePrest.setIdDemande(demandeRente.getIdDemandePrestation());
                demandePrest.retrieve();

                if (demandePrest.getIdTiers().equals(membre.getIdTiers())) {

                    // Workaround ACOR
                    // Si personne décédée sans période de domicile en
                    // suisse,
                    // Il faut la crééer pour l'envoyer à ACOR autrement
                    // ACOR ne calcul pas.
                    // si requérant avec date décès
                    if (!JadeStringUtil.isBlankOrZero(membre.getDateDeces())) {
                        // si suisse

                        if (membre.getCsNationalite().equals("100")) {
                            // voir si une période de domicile en suisse
                            // dans la liste
                            boolean isPeriodeDomicileSuisse = false;
                            for (int i = 0; i < periodes.length; i++) {
                                ISFPeriode periode = periodes[i];
                                if (periode.getNoAvs().equals(((ISFMembreFamilleRequerant) membre).getNss())
                                        && ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE.equals(periode
                                        .getType())) {
                                    isPeriodeDomicileSuisse = true;
                                }
                            }

                            if (!isPeriodeDomicileSuisse) {
                                // Créer une période de domicile en
                                // suisse pour cet assuré
                                SFPeriodeWrapper periode = new SFPeriodeWrapper();
                                periode.setDateFin(membre.getDateDeces());
                                periode.setDateDebut(membre.getDateNaissance());
                                periode.setNoAvs(membre.getNss());
                                periode.setPays(membre.getCsNationalite());
                                periode.setType(ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE);

                                List lperiodes = new ArrayList();
                                for (int i = 0; i < periodes.length; i++) {
                                    lperiodes.add(periodes[i]);
                                }
                                lperiodes.add(periode);
                                periodes = (ISFPeriode[]) lperiodes.toArray(new ISFPeriode[lperiodes.size()]);
                            }

                        }

                    }

                }

            }

        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération des périodes par membres.", e);
            periodes = new ISFPeriode[0];
        }
        return periodes;
    }

    public String getTypeDemande() {
        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(demandeRente.getCsTypeDemandeRente())) {
            return "i";
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(demandeRente.getCsTypeDemandeRente())) {
            return "s";
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(demandeRente.getCsTypeDemandeRente())) {
            return "v";
        } else {
            return "";
        }

    }

    private String getTypePeriode(ISFPeriode periode) {

        if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE)) {
            return "do";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_TRAVAILLE)) {
            return "tr";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_NATIONALITE)) {
            return "na";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_AFFILIATION)) {
            return "af";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_COTISATION)) {
            return "ex";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ASSURANCE_ETRANGERE)) {
            return "ae";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT)) {
            return "rc";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE)) {
            return "et";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_IJ)) {
            return "ij";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE)) {
            return "be";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_INCARCERATION)) {
            return "in";
        } else {
            return "";
        }
    }

    private List<REHistoriqueRentes> getRentesEnCours(String idTiers) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            return new ArrayList<>();
        }
        // On récupère tous les historiques pour chaque assuré
        // Les RA de type API n'ont pas d'historique
        REHistoriqueRentesJoinTiersManager mgr = new REHistoriqueRentesJoinTiersManager();
        mgr.setSession(getSession());
        mgr.setForIdTiersIn(idTiers);
        mgr.setForIsEnvoyerAcor(Boolean.TRUE);
        mgr.find(BManager.SIZE_NOLIMIT);

        // On met la liste des rentes accordées ds une table de wrapper
        List<REHistoriqueRentes> historiques = new ArrayList<>();
        historiques.addAll(mgr.getContainerAsList());
        return historiques;
    }

    private List<CiType> createListCI(String idTiers) {
        List<CiType> ciTypes = new ArrayList<>();
        try {
            List<REInscriptionCIViewBean> listCIs = recupererInscriptionsCI(idTiers);
            for (REInscriptionCIViewBean inscription : listCIs) {
                CiType ci = createCi(inscription);
                ciTypes.add(ci);
            }
        } catch (PRACORException e) {
            LOG.error("Erreur lors de la récupération des inscriptions CI.", e);
        }
        return ciTypes;
    }

    private CiType createCi(REInscriptionCIViewBean inscription) {
        CiType ci = new CiType();
        ci.setBrancheEconomique(ParserUtils.formatRequiredLong(getSession().getCode(inscription.getBrancheEconomique())));

        // 3. Code diminution
        ci.setCodeDiminution(ParserUtils.formatRequiredShort(inscription.getCodeExtourne()));

        // 4. Genre cotisation
        ci.setGenreCotisation(ParserUtils.formatRequiredInteger(inscription.getGenreCotisation()));

        // 5. Code particulier
        ci.setCodeParticulier(ParserUtils.formatOptionalShort(inscription.getCodeParticulier()));

        // 6. Mois début période de cotisation
        if (!JadeStringUtil.isBlankOrZero(inscription.getMoisDebutCotisations())) {
            ci.setMoisDebut(ParserUtils.formatRequiredInteger(inscription.getMoisDebutCotisations()));
        }

        // 7. Mois de fin de période de cotisation
        if (!JadeStringUtil.isBlankOrZero(inscription.getMoisFinCotisations())) {
            ci.setMoisFin(ParserUtils.formatRequiredInteger(inscription.getMoisFinCotisations()));
        }

        // 8. Année de cotisation
        ci.setAnnee(convertstrAnneeToXmlGreg(inscription.getAnneeCotisations()));

        // 9. Montant du revenu en francs
        ci.setMontant(ParserUtils.formatRequiredBigDecimalNoDecimal(inscription.getRevenu()));

        // 10. Caisse + agence
        ci.setCaisseAgence(Integer.valueOf(inscription.getNumeroCaisse() + inscription.getNumeroAgence()));

        // 11. No de relevé
        if (JadeStringUtil.isBlankOrZero(inscription.getNoAffilie())) {
            ci.setNumeroAffilie("0");
        } else {
            ci.setNumeroAffilie(inscription.getNoAffilie());
        }

        // 12. Code amortissement
        if (!JadeStringUtil.isBlankOrZero(inscription.getCodeADS())) {
            ci.setCodeAmortissement(inscription.getCodeADS());
        }

        // 13. Part. aux bonif. assistance
        ci.setParticipationBonificationAssistance(ParserUtils.formatRequiredInteger(inscription.getPartBonifAssist()));

        // 14. Code provenance
        ci.setCodeProvenance(ParserUtils.formatRequiredInteger(inscription.getProvenance()));

        return ci;
    }

    private XMLGregorianCalendar convertstrAnneeToXmlGreg(String anneeCotisations) {
        DateFormat format = new SimpleDateFormat("yyyy");
        Date dateFormat = null;
        try {
            dateFormat = format.parse(anneeCotisations);

            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(dateFormat);

            return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (ParseException | DatatypeConfigurationException e) {
            LOG.error("Erreur lors de la conversion des années de cotisations.", e);
        }
        return null;
    }

    private List<REInscriptionCIViewBean> recupererInscriptionsCI(String idTiers) throws PRACORException {
        List<REInscriptionCIViewBean> inscriptionsCI = new ArrayList<>();
        BTransaction transaction = null;
        try {

            transaction = (BTransaction) getSession().newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            /*
             * CI en envoyer a ACOR :
             *
             * Tous les rassemblements 'définitif', ainsi que les extraits de CI postérieur au dernier
             * rassemblement. Si plusieurs extraits postérieur au dernier rassemblement sont trouvé, seul le
             * plus récent sera pris. Egalement inclure tous les CI additionnels de tous les rassemblement
             * trouvé.
             */

            RERassemblementCIManager mgrRCI = new RERassemblementCIManager();
            mgrRCI.setSession(getSession());
            mgrRCI.setForIdTiers(idTiers);
            mgrRCI.setForCsEtatDiffentDe(IRERassemblementCI.CS_ETAT_REVOQUE);
            mgrRCI.setForNoDateRevocation(Boolean.TRUE);
            mgrRCI.setForRassWithoutParent(Boolean.TRUE);
            // Tri par date de rassemblement (plus récente à plus
            // ancienne) et motif (ordre croissant)
            mgrRCI.setOrderBy(RERassemblementCI.FIELDNAME_DATE_RASSEMBLEMENT + " DESC, "
                    + RERassemblementCI.FIELDNAME_MOTIF + " ASC ");

            mgrRCI.find(transaction, BManager.SIZE_NOLIMIT);
            RCIContainer rciCo = new RCIContainer();

            // Traitement des RCI sans les CI additionnel
            if (mgrRCI.getSize() > 0) {
                for (Iterator<RERassemblementCI> iterator = mgrRCI.iterator(); iterator.hasNext(); ) {
                    RERassemblementCI rci = iterator.next();
                    // Les rassemblements sans motif ne sont pas envoyé
                    // à ACOR.
                    // Contrôler avec RJE, car avec la reprise, il y a
                    // qqes rassemblements sans motif (~3%)
                    if (JadeStringUtil.isBlankOrZero(rci.getMotif())) {
                        continue;
                    }
                    rciCo.addRCI(ParserUtils.formatRequiredInteger(rci.getMotif()).intValue(), rci.getIdRCI());
                }
            }

            if (!rciCo.getIdsRCI().isEmpty()) {

                // Rajout des CI Additionnel....
                mgrRCI.setForRassWithoutParent(null);
                mgrRCI.setForCIAdditionnelOnly(Boolean.TRUE);
                List<String> idsRCICopy = new ArrayList<String>(rciCo.getIdsRCI());
                mgrRCI.find(transaction, BManager.SIZE_NOLIMIT);
                if (mgrRCI.getSize() > 0) {
                    for (Iterator<RERassemblementCI> iterator = mgrRCI.iterator(); iterator.hasNext(); ) {
                        RERassemblementCI rciAdd = iterator.next();
                        // Les rassemblements sans motif ne sont pas
                        // considéré !!!
                        if (JadeStringUtil.isBlankOrZero(rciAdd.getMotif())) {
                            continue;
                        }
                        if (idsRCICopy.contains(rciAdd.getIdParent())) {
                            rciCo.addRCIAdditionnel(ParserUtils.formatRequiredInteger(rciAdd.getMotif()).intValue(),
                                    rciAdd.getIdRCI());
                        }
                    }
                }
            }

            for (Iterator<String> iterator = rciCo.getIdsRCI().iterator(); iterator.hasNext(); ) {
                String idRCI = iterator.next();

                // Récupération des inscriptions CI
                REInscriptionCIListViewBean mgr = new REInscriptionCIListViewBean();
                mgr.setSession(getSession());
                mgr.setForIdRCI(idRCI);
                mgr.setIsCITraiteExclu(true);
                mgr.find(transaction, BManager.SIZE_NOLIMIT);

                for (int i = 0; i < mgr.size(); i++) {
                    inscriptionsCI.add((REInscriptionCIViewBean) mgr.get(i));
                }

            }

            // Tous rassemblement de CI additionnel seront marqués comme étant traité avec la date du jour
            String dateTraitementCIAdditionnel = JadeDateUtil.getGlobazFormattedDate(new Date());
            for (Iterator<String> iterator = rciCo.getIdsRCI().iterator(); iterator.hasNext(); ) {
                String idRCI = iterator.next();
                RERassemblementCI rassemblementCI = new RERassemblementCI();
                rassemblementCI.setSession(getSession());
                rassemblementCI.setId(idRCI);
                rassemblementCI.retrieve(transaction);
                if (rassemblementCI.isNew()) {
                    throw new Exception("Unable to retrieve the RERassemblementCI with id [" + idRCI + "]");
                }
                // Pour savoir si le rassemblement contient des CI additionnel, on test l'id parent qui
                // référence un rassemblement contenant des CI en attente de CIs additionnels
                if (!JadeStringUtil.isBlankOrZero(rassemblementCI.getIdParent())) {
                    if (JadeStringUtil.isBlankOrZero(rassemblementCI.getDateTraitement())) {
                        // Si la date de traitement est déjà renseigné, on la conserve
                        rassemblementCI.setDateTraitement(dateTraitementCIAdditionnel);
                        rassemblementCI.update(transaction);
                    }
                }
            }
            if ((transaction != null) && !transaction.hasErrors() && !transaction.isRollbackOnly()) {
                transaction.commit();
            }
        } catch (Exception e) {
            String message = getSession().getLabel("ERREUR_INSCRIPTIONS_CI_ENFANT");
            message += " Parent Exception message : " + e.getMessage();
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    message += " Exception during transaction rollback. Error message : " + e1.getMessage();
                }
            }
            throw new PRACORException(message, e);
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                }
            }
        }
        return inscriptionsCI;
    }

    private DonneesPostalesType createDonneesPostales() {
        DonneesPostalesType donneesPostalesType = new DonneesPostalesType();
        try {

            TIAdresseDataSource adresse = getAdresseRequerant();
            if (adresse != null) {
                AdresseType adresseType = new AdresseType();
                // 2. Numéro et rue
                adresseType.setAdresse(adresse.rue + " " + adresse.numeroRue);
                // 3. Localite
                adresseType.setLocalite(adresse.localiteNom);
                // 4. Code postal
                adresseType.setCodePostal(adresse.localiteNpa);
                // 5. Récupération du code pays
                String codePays = "0";
                if (!JadeStringUtil.isBlankOrZero(adresse.paysIso)) {
                    TIPaysManager paysManager = new TIPaysManager();
                    paysManager.setSession(session);
                    paysManager.setForCodeIso(adresse.paysIso);
                    paysManager.find(BManager.SIZE_NOLIMIT);
                    if (!paysManager.getContainer().isEmpty()) {
                        codePays = ((TIPays) paysManager.getContainer().get(0)).getCodeCentrale();
                    }
                }
                adresseType.setPays(ParserUtils.formatRequiredInteger(codePays));
                donneesPostalesType.setAdresse(adresseType);
            }

            // Informations sur le domicile
            TIAdressePaiementData adressePaiement = PRTiersHelper.getAdressePaiementData(session, transaction, tiersRequerant.getIdTiers(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());
            if (adressePaiement != null) {
                BanqueAdresseType banqueAdresseType = new BanqueAdresseType();
                // 6. Nom du tiers
                banqueAdresseType.setNomTitulaire(adressePaiement.getNomTiers1() + adressePaiement.getNomTiers2());

                String idTiersBanque = adressePaiement.getIdTiersBanque();
                TIAbstractAdresseData banque = null;
                if (!JadeStringUtil.isBlankOrZero(idTiersBanque)) {
                    banque = TIAdresseResolver.dataSourceAdr(session, idTiersBanque,
                            IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                            IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "", JACalendar.todayJJsMMsAAAA(), true);
                }

                // Adresse de la banque
                if (banque != null) {
                    // 7. Nom banque
                    StringBuilder banqueDesignation = new StringBuilder();
                    banqueDesignation.append(banque.getDesignation1_tiers()).append(PRACORConst.CA_CHAINE_VIDE);
                    banqueDesignation.append(banque.getDesignation2_tiers()).append(PRACORConst.CA_CHAINE_VIDE);
                    banqueDesignation.append(banque.getDesignation3_tiers()).append(PRACORConst.CA_CHAINE_VIDE);
                    banqueDesignation.append(banque.getDesignation4_tiers());
                    banqueAdresseType.setNom(banqueDesignation.toString().trim());

                    // 8. Numéro et rue
                    StringBuilder banqueAdresse = new StringBuilder();
                    banqueAdresse.append(banque.getRue()).append(PRACORConst.CA_CHAINE_VIDE);
                    banqueAdresse.append(banque.getNumero());
                    banqueAdresseType.setAdresse(banqueAdresse.toString().trim());
                    // 9. Localité
                    banqueAdresseType.setLocalite(banque.getLocalite());
                    // 10. Code postal
                    banqueAdresseType.setCodePostal(banque.getNpa());
                    // 11. Code pays
                    banqueAdresseType.setPays(ParserUtils.formatRequiredInteger(banque.getIdPays()));
                }

                // 12. swift
                banqueAdresseType.setBic(adressePaiement.getSwift());
                // Apparemment pour récupérer l'IBAN il ne faut pas utiliser la méthode getIban() mais getCompte()... ->
                // trop facile sinon
                // 13. IBAN
                // On doit supprimer les espaces pour respecter le xsd ACOR
                banqueAdresseType.setIban(adressePaiement.getCompte().replace(" ", ""));
                donneesPostalesType.setBanque(banqueAdresseType);
            }
        } catch (Exception e) {
            LOG.error("Erreur lors de la création des données postales.", e);
        }
        return donneesPostalesType;
    }

    private TIAdresseDataSource getAdresseRequerant() throws Exception {
        TIAdresseDataSource adresse;
        TITiers t = new TITiers();
        t.setIdTiers(tiersRequerant.getIdTiers());
        t.setSession(session);
        String prop = getWantAdresseCourrierProperties();
        if ("true".equals(prop)) {
            adresse = t.getAdresseAsDataSource(IPTConstantesExternes.TIERS_ADRESSE_TYPE_COURRIER,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), true);
        } else {
            adresse = t.getAdresseAsDataSource(IPTConstantesExternes.TIERS_ADRESSE_TYPE_DOMICILE,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), true);
        }
        return adresse;
    }

    private String getWantAdresseCourrierProperties() {
        String prop = null;
        try {
            if ("corvus".equalsIgnoreCase(session.getApplicationId())) {
                prop = session.getApplication().getProperty(IS_WANT_ADRESSE_COURRIER);
            } else {
                prop = null;
            }
        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération des propriétées d'adresse courrier.", e);
            prop = null;
        }
        return prop;
    }

    private Integer getDomicile(String csCantonDomicile, String codePays, PRTiersWrapper tiersRequerant) {
        /**
         * 1) On prend le canton
         * 2) Sinon le pays
         * 3) Sinon prendre le pays du requérant
         */
        if (!JadeStringUtil.isIntegerEmpty(csCantonDomicile)) {
            // Le canton peut désigner 'Etranger', dans ce cas il faut reprendre le pays
            if (PRACORConst.CODE_CANTON_ETRANGER.equals(csCantonDomicile)) {
                if (JadeStringUtil.isIntegerEmpty(codePays)) {
                    // On retourne un code 999 si le canton et pays sont inconnus
                    return ParserUtils.formatRequiredInteger(PRACORConst.CANTON_ET_PAYS_INCONNU);
                } else {
                    return ParserUtils.formatRequiredInteger(codePays);
                }
            } else {
                return ParserUtils.formatRequiredInteger(PRACORConst.csCantonToAcor(csCantonDomicile));
            }
        } else {
            if (!JadeStringUtil.isIntegerEmpty(codePays)) {
                return ParserUtils.formatRequiredInteger(codePays);
            } else {
                return ParserUtils.formatRequiredInteger(PRACORConst.csCantonToAcor(tiersRequerant.getCanton()));
            }
        }
    }

    private Integer getCodePays(String csNationalite) {
        if (!JadeStringUtil.isIntegerEmpty(csNationalite)) {
            return ParserUtils.formatRequiredInteger(PRACORConst.csEtatToAcor(csNationalite));
        } else {
            return ParserUtils.formatRequiredInteger(PRACORConst.CA_ORIGINE_INCONNU);
        }
    }

    private List<EnfantType> createListEnfants(List<ISFMembreFamilleRequerant> enfantsFamilles) throws PRACORException {
        List<EnfantType> enfants = new ArrayList<>();
        for (ISFMembreFamilleRequerant membre : enfantsFamilles) {
            EnfantType enfant = createEnfantType(membre);
            enfants.add(enfant);
        }
        return enfants;
    }

    private EnfantType createEnfantType(ISFMembreFamilleRequerant membre) throws PRACORException {
        EnfantType enfant = new EnfantType();
        ISFEnfant detail;
        try {
            detail = situationFamiliale().getEnfant(membre.getIdMembreFamille());
        } catch (Exception e) {
            throw new PRACORException(getSession().getLabel("ERREUR_DETAILS_ENFANTS"), e);
        }

        enfant.setNavs(getNssMembre(membre));
        enfant.setNom(membre.getNom());
        enfant.setPrenom(membre.getPrenom());
        enfant.setDateNaissance(ParserUtils.formatDate(membre.getDateNaissance(), DD_MM_YYYY_FORMAT));
        if (!JadeStringUtil.isBlankOrZero(membre.getDateDeces())) {
            enfant.setDateDeces(ParserUtils.formatDate(membre.getDateDeces(), DD_MM_YYYY_FORMAT));
        }
        //TODO demande un int dans le JSON sinon l'appli client met une erreur
        enfant.setNationalite(getCodePays(membre.getCsNationalite()));
        enfant.setDomicile(getDomicile(membre.getCsCantonDomicile(), membre.getPays(), tiersRequerant));
        enfant.setRefugie(false);
        enfant.setSexe(PRACORConst.csSexeEnfantToAcor2020(membre.getCsSexe()));
        if (!JadeStringUtil.isBlankOrZero(detail.getNoAvsPere())) {
            enfant.setNavsPere(ParserUtils.formatNssToLong(detail.getNoAvsPere()));
        } else {
            enfant.setPereInconnu(true);
        }
        if (!JadeStringUtil.isBlankOrZero(detail.getNoAvsMere())) {
            enfant.setNavsMere(ParserUtils.formatNssToLong(detail.getNoAvsMere()));
        } else {
            enfant.setMereInconnue(true);
        }
        // TODO rechercher etat civil et mapper selon EtatCivil-types.xsd
        enfant.setEtatCivil(Short.valueOf(PRACORConst.csTypeLienToACOR(session, membre.getCsEtatCivil())));
        if (!JadeStringUtil.isBlankOrZero(detail.getDateAdoption())) {
            enfant.setDateAdoption(ParserUtils.formatDate(detail.getDateAdoption(), YYYY_MM_DD_FORMAT));
        }
        // RENTES
        addRentesEnfant(enfant, membre);

//        // EURO_FORM
        enfant.setDonneesPostales(createDonneesPostales());

//        // PERIODES
        addPeriodesEnfant(enfant, membre);
        return enfant;
    }

    private void addPeriodesEnfant(EnfantType enfant, ISFMembreFamilleRequerant membre) {
        for (ISFPeriode isfPeriode : recupererPeriodesMembre(membre)) {
            // TODO ajouter les période recueilli C et G (G = recueilli gratuitement avec ou sans tuteur)
            if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE, isfPeriode.getType())) {
                enfant.getPeriodeBTE().add(createPeriodeBTE(isfPeriode));
            } else if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT, isfPeriode.getType())) {
                enfant.getPeriodeRecueilliG().add(createPeriodeRecueilliG(isfPeriode));
            } else if (getEnumPeriodeEnfantType(isfPeriode) != null) {
                enfant.getPeriode().add(createPeriodeEnfantType(isfPeriode));
            }
        }
    }

    private void addRentesEnfant(EnfantType enfant, ISFMembreFamilleRequerant membre) {
        try {
            for (REHistoriqueRentes rente : getRentesEnCours(membre.getIdTiers())) {
                if (StringUtils.equals(IREDemandeRente.REVISION_10EME_REVISION, rente.getDroitApplique())) {
                    if (isRenteExtraordinaire(rente.getCodePrestation())) {
                        enfant.getRenteExtraordinaire10().add(createRenteExtraordinaire10(rente));
                    } else {
                        enfant.getRenteOrdinaire10().add(createRenteOrdinaire10(rente));
                    }
                } else if (StringUtils.equals(IREDemandeRente.REVISION_9EME_REVISION, rente.getDroitApplique())) {
                    if (isRenteExtraordinaire(rente.getCodePrestation())) {
                        enfant.getRenteExtraordinaire9().add(createRenteExtraordinaire9(rente));
                    } else {
                        enfant.getRenteOrdinaire9().add(createRenteOrdinaire9(rente));
                    }
                } else {
                    LOG.info("Révision différente de 10 ou 9 (IDRente: " + rente.getIdRenteAccordee() + "/ Révision : " + rente.getDroitApplique() + ")");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getNssMembre(ISFMembreFamilleRequerant membre) {
        String nss = membre.getNss();
        if (JadeStringUtil.isBlank(membre.getNss()) || JadeStringUtil.isIntegerEmpty(membre.getNss())) {
            nss = nssBidon(membre.getNss(), membre.getCsSexe(), membre.getNom() + membre.getPrenom(), !membre
                    .getRelationAuRequerant().equals(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT));
        }
        return ParserUtils.formatNssToLong(nss);
    }

    private XMLGregorianCalendar getDateNaissanceMembreAssure(ISFMembreFamilleRequerant membre) {
        String dn;
        if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(membre.getIdMembreFamille())) {
            dn = "01.01.1970";
        } else {
            dn = membre.getDateNaissance();
        }
        return ParserUtils.formatDate(dn, DD_MM_YYYY_FORMAT);
    }

    private List<FamilleType> createListFamilles(List<ISFMembreFamilleRequerant> membresCatConjoints, List<ISFMembreFamilleRequerant> membresCatExConjointsConjoints) {
        List<FamilleType> listfamillesType = new ArrayList<>();
        try {
            List<Ligne> lignesConjoints = new ArrayList<>();
            List<Ligne> lignesExConjoints = new ArrayList<>();
            for (ISFMembreFamilleRequerant conjoints : membresCatConjoints) {
                lignesConjoints.addAll(creerLignes(conjoints));
            }
            for (Ligne ligne : lignesConjoints) {
                listfamillesType.add(createFamille(ligne));
            }
            for (ISFMembreFamilleRequerant exConjoints : membresCatExConjointsConjoints) {
                lignesExConjoints.addAll(creerLignes(exConjoints));
            }
            for (Ligne ligne : lignesExConjoints) {
                listfamillesType.add(createFamille(ligne));
            }
            return listfamillesType;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listfamillesType;
    }


    private FamilleType createFamille(Ligne ligne) {
        FamilleType famille = new FamilleType();
        if ((ligne.getConjoint() instanceof REACORDemandeAdapter.ImplMembreFamilleRequerantWrapper)
                && REACORDemandeAdapter.ImplMembreFamilleRequerantWrapper.NO_CS_RELATION_EX_CONJOINT_DU_CONJOINT
                .equals(ligne.getConjoint().getRelationAuRequerant())) {

            REACORDemandeAdapter.ImplMembreFamilleRequerantWrapper exConjointDuConjoint = (REACORDemandeAdapter.ImplMembreFamilleRequerantWrapper) ligne
                    .getConjoint();
        // TODO : cas d'un ex conjoint de conjoint qui n'a pas de NSS --> générer un NSS bidon ?
            famille.getNavs().add(ParserUtils.formatNssToLong(exConjointDuConjoint.getNssConjoint()));
            famille.getNavs().add(getNssMembre(exConjointDuConjoint));
        } else {
            famille.getNavs().add(ParserUtils.formatNssToLong(tiersRequerant.getNSS()));
            famille.getNavs().add(getNssMembre(ligne.getConjoint()));
        }
        famille.setDebut(ParserUtils.formatDate(ligne.getDateMariage(), DD_MM_YYYY_FORMAT));
        if (ParserUtils.formatDate(ligne.getDateFin(), DD_MM_YYYY_FORMAT) != null) {
            FamilleType.DonneesFin donnesFin = new FamilleType.DonneesFin();
            donnesFin.setFin(ParserUtils.formatDate(ligne.getDateFin(), DD_MM_YYYY_FORMAT));
            donnesFin.setType(ParserUtils.formatRequiredShort(PRACORConst.csTypeLienToACOR(getSession(), ligne.getTypeLien())));
            //TODO
//        famille.getPeriodeSeparation().addAll()
            famille.setDonneesFin(donnesFin);
        }
        famille.setLien(ParserUtils.formatRequiredShort(PRACORConst.csTypeLienFamilleToACOR(getSession(), ligne.getTypeLien())));
        famille.setPensionAlimentaire(false);
        return famille;
    }

    private void trouverRequerant(List<ISFMembreFamilleRequerant> membres) {
        String noAVS = tiersRequerant.getNSS();
        for (ISFMembreFamilleRequerant membre : membres) {
            ImplMembreFamilleRequerantWrapper membreWrapper = (ImplMembreFamilleRequerantWrapper) membre;

            if (noAVS.equals(membreWrapper.getNssDirect())) {
                membreRequerant = membre;
                break;
            }
        }
    }

    private List<Ligne> creerLignes(ISFMembreFamilleRequerant conjoint) throws Exception {
        List<Ligne> lignesList = new ArrayList<>();

        // Les relations sont ordonnées de la plus ancienne à la plus récente.
        ISFRelationFamiliale[] relationsAll = null;
        // Cas des ex-conjoints du conjoint du requérant....
        if ((conjoint instanceof ImplMembreFamilleRequerantWrapper)
                && ImplMembreFamilleRequerantWrapper.NO_CS_RELATION_EX_CONJOINT_DU_CONJOINT
                .equals(((ImplMembreFamilleRequerantWrapper) conjoint).getRelationAuRequerant())) {

            relationsAll = relations(((ImplMembreFamilleRequerantWrapper) conjoint).getIdMFDuConjoint(),
                    conjoint.getIdMembreFamille());

        } else {
            relationsAll = relations(membreRequerant.getIdMembreFamille(), conjoint.getIdMembreFamille());
        }

        // regroup
        String dateMariage = null;

        for (int idRelation = 0; idRelation < relationsAll.length; ++idRelation) {

            ISFRelationFamiliale relation = relationsAll[idRelation];

            // Ce cas apparait pour les types de relations ENFANT_COMMUN ou RELATION_INDEFINIE.
            if (relation.getTypeLien() == null) {
                continue;
            }

            if (ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(relation.getTypeLien())
                    || ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relation
                    .getTypeLien())
                    || ISFSituationFamiliale.CS_TYPE_LIEN_VEUF.equals(relation.getTypeLien())
                    || ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES.equals(relation.getTypeLien())) {

                dateMariage = relation.getDateDebutRelation();
            }

            boolean isLastElement = idRelation == (relationsAll.length - 1) ? true : false;

            String csTypeLienNextElem = null;
            if (!isLastElement) {
                csTypeLienNextElem = relationsAll[idRelation + 1].getTypeLien();
            }

            if (isLastElement || ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(csTypeLienNextElem)
                    || ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(csTypeLienNextElem)) {

                // BZ-5083
                // On stocke le cas en cours
                Ligne l = null;

                // Le type de lien séparé de fait doit être considéré comme marié ou lpart_enregistré
                if (ISFSituationFamiliale.CS_TYPE_LIEN_SEPARE.equals(relation.getTypeLien())) {
                    if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relation
                            .getTypeRelation())) {
                        l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                ISFSituationFamiliale.CS_TYPE_LIEN_MARIE, dateMariage);
                    } else {
                        l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                relation.getTypeLien(), dateMariage);
                    }
                } else if (ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DISSOUT.equals(relation
                        .getTypeLien())) {
                    if (ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relation
                            .getTypeRelation())) {
                        l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE, dateMariage);
                    } else {
                        l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                relation.getTypeLien(), dateMariage);
                    }
                } else {
                    l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                            relation.getTypeLien(), dateMariage);
                }


                // TODO : la gestion des dates de fin de relation reste un mystère.
                if (!ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(relation.getTypeLien())
                        && !ISFSituationFamiliale.CS_TYPE_LIEN_VEUF.equals(relation.getTypeLien())
                        && !ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relation
                        .getTypeLien()) && !ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relation
                        .getTypeRelation())) {

                    l.setDateFin(relation.getDateDebut());
                } else {
                    l.setDateFin(relation.getDateFin());
                }
                lignesList.add(l);
            }
        }

        return lignesList;
    }

    /**
     * DOCUMENT ME!
     *
     * @param idM1 DOCUMENT ME!
     * @param idM2 DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws PRACORException DOCUMENT ME!
     */
    public ISFRelationFamiliale[] relations(String idM1, String idM2) throws PRACORException {
        String id = idM1 + "_" + idM2;
        ISFRelationFamiliale[] retValue = (ISFRelationFamiliale[]) relations.get(id);

        if (retValue == null) {
            try {
                retValue = situationFamiliale().getToutesRelationsConjoints(idM1, idM2, Boolean.FALSE);
                relations.put(id, retValue);
            } catch (PRACORException e) {
                throw e;
            } catch (Exception e) {
                throw new PRACORException(session.getLabel("ERREUR_CHARGEMENT_RELATIONS_CONJOINTS"), e);
            }
        }

        return retValue;
    }

    private Long getNumAvsFromDemande(REDemandeRente demandeRente, PRTiersWrapper tiers, BSession session) {
        Long navs = new Long(0);
        String strNss = "";
        try {
            // Traitement particulier que les demandes de survivants
            if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(demandeRente.getCsTypeDemandeRente())) {
                strNss = getNumAvsFromDemandeSurvivant(tiers, session);
            } else if (!Objects.isNull(tiers)) {
                strNss = tiers.getNSS();
            }
            navs = ParserUtils.formatNssToLong(strNss);
        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération du numéro AVS de la demande.", e);
        }
        return navs;
    }

    private String getNumAvsFromDemandeSurvivant(PRTiersWrapper tiers, BSession session) {
        String nssDemande =  tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        try {
            String idTiersRequerant = tiers.getIdTiers();
            // Gestion de cas spéciaux pour ACOR
            ISFMembreFamilleRequerant[] membreFamille = getToutesLesMembresFamilles(idTiersRequerant, session);
            if (hasUniquementRelationEnfantCommun(membreFamille, idTiersRequerant, session)) {
                // On prend le NSS su conjoint si il existe
                nssDemande = recupererNSSConjoint(membreFamille, idTiersRequerant);
                // Sinon on prend le NSS de l'enfant le plus jeune
                if (JadeStringUtil.isBlankOrZero(nssDemande)) {
                    nssDemande = recupererNSSEnfantPlusJeune(membreFamille, idTiersRequerant);
                }
            }

        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération du numéro AVS de la demande du survivant.", e);
        }
        return nssDemande;
    }

    /**
     * Recherche les membres de la famille du tiers requérant
     *
     * @return
     * @throws Exception
     */
    protected List<ISFMembreFamilleRequerant> getToutesLesMembresFamillesEtEtendue() throws Exception {
        List<ISFMembreFamilleRequerant> membres = new LinkedList();
        ISFSituationFamiliale sf = situationFamiliale();
        try {
            List antiDoublon = new ArrayList();
            ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(tiersRequerant.getIdTiers(),
                    getDateTraimentFormat());

            for (int i = 0; i < membresFamille.length; ++i) {
                if (!antiDoublon.contains(getKey(membresFamille[i]))) {
                    membres.add(new ImplMembreFamilleRequerantWrapper(membresFamille[i], tiersRequerant));
                    antiDoublon.add(getKey(membresFamille[i]));

                    if (ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(membresFamille[i]
                            .getRelationAuRequerant())) {

                        // On récupére également les éventuelles conjoints des conjoints

                        ISFMembreFamilleRequerant membresFamilleEtendue[] = sf
                                .getMembresFamilleRequerantParMbrFamille(membresFamille[i].getIdMembreFamille());

                        if (membresFamilleEtendue != null) {
                            for (int j = 0; j < membresFamilleEtendue.length; j++) {
                                if (membresFamilleEtendue[j] != null) {
                                    if (!antiDoublon.contains(getKey(membresFamilleEtendue[j]))) {

                                        ImplMembreFamilleRequerantWrapper m = new ImplMembreFamilleRequerantWrapper(
                                                membresFamilleEtendue[j], tiersRequerant);

                                        // On parle du (ex)conjoint du conjoint du requérant.
                                        if (ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(m
                                                .getRelationAuRequerant())
                                                && !tiersRequerant.getIdTiers().equals(m.getIdTiers())) {
                                            m.setRelationAuRequerant(REACORDemandeAdapter.ImplMembreFamilleRequerantWrapper.NO_CS_RELATION_EX_CONJOINT_DU_CONJOINT);
                                            m.setIdMFDuConjoint(membresFamille[i].getIdMembreFamille());
                                            m.setNssConjoint(membresFamille[i].getNss());
                                        } else {

                                            m.setIdMFDuConjoint(null);
                                            if (tiersRequerant.getIdTiers().equals(m.getIdTiers())) {
                                                m.setRelationAuRequerant(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT);
                                            } else if (ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT.equals(m
                                                    .getRelationAuRequerant())) {
                                                ;
                                            } else {
                                                m.setRelationAuRequerant(REACORDemandeAdapter.ImplMembreFamilleRequerantWrapper.NO_CS_RELATION_BLANK);
                                            }
                                        }
                                        membres.add(m);
                                        antiDoublon.add(getKey(membresFamilleEtendue[j]));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new PRACORException(session.getLabel("ERREUR_CHARGEMENT_SF"), e);
        }
        return membres;
    }

    String getKey(ISFMembreFamilleRequerant mf) {
        if (JadeStringUtil.isBlankOrZero(mf.getIdTiers())) {
            return "imf-" + mf.getIdMembreFamille();
        } else {
            return "iti-" + mf.getIdTiers();
        }
    }

    public ISFSituationFamiliale situationFamiliale() throws PRACORException {
        ISFSituationFamiliale sf;
        try {
            String csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
            try {
                if (IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(demandeRente.getCsTypeCalcul())) {
                    csDomaine = ISFSituationFamiliale.CS_DOMAINE_CALCUL_PREVISIONNEL;
                }
            } catch (Exception e) {
                csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
            }
            sf = SFSituationFamilialeFactory.getSituationFamiliale(session, csDomaine, tiersRequerant.getIdTiers());
        } catch (Exception e) {
            throw new PRACORException(session.getLabel("ERREUR_CHARGEMENT_SF"), e);
        }

        return sf;
    }

    /**
     * Recherche les membres de la famille du tiers requérant
     *
     * @param idTiersRequerant
     * @param session
     * @return
     * @throws Exception
     */
    protected ISFMembreFamilleRequerant[] getToutesLesMembresFamilles(String idTiersRequerant, BSession session) throws Exception {
        Map<ISFMembreFamilleRequerant, ISFRelationFamiliale[]> relations = new HashMap<>();

        // On recherche la sit famille du tiers requérant
        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);

        // On récupère tous les membres de la famille
        ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(idTiersRequerant);
        return membresFamille;
    }

    /**
     * Analyse les relations avec les conjoints du requérant et retourne <code>true</code> s'il ne possède que des
     * relations de types enfants communs
     *
     * @param membreFamille
     * @param idTiersRequerant
     * @param session
     * @return
     * @throws Exception
     */
    protected boolean hasUniquementRelationEnfantCommun(ISFMembreFamilleRequerant[] membreFamille,
                                                        String idTiersRequerant, BSession session) throws Exception {

        boolean result = false;
        String idMembreFamilleRequerant = null;
        // On récupère tous les conjoints
        List<ISFMembreFamilleRequerant> conjoints = new ArrayList<ISFMembreFamilleRequerant>();
        for (ISFMembreFamilleRequerant tiers : membreFamille) {
            // On ne veut pas traiter le tiers requérant
            if (idTiersRequerant.equals(tiers.getIdTiers())) {
                idMembreFamilleRequerant = tiers.getIdMembreFamille();
                continue;
            }
            if (ISFMembreFamille.CS_TYPE_RELATION_CONJOINT.equals(tiers.getRelationAuRequerant())) {
                conjoints.add(tiers);
            }
        }

        // On poursuit uniquement si on à trouver l'idMembreFamilleRequerant et que la liste des conjoints n'est pas
        // vide
        if (!conjoints.isEmpty() && !JadeStringUtil.isEmpty(idMembreFamilleRequerant)) {

            // On recherche la sit famille du tiers requérant
            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);

            // On récupère les relations au tiers pour chaque conjoint
            for (ISFMembreFamilleRequerant conjoint : conjoints) {
                ISFRelationFamiliale[] relations = sf.getToutesRelationsConjoints(idMembreFamilleRequerant,
                        conjoint.getIdMembreFamille(), false);
                for (ISFRelationFamiliale relation : relations) {
                    // On anaylse ses relations par rapport au tiers requerant
                    if (ISFRelationConjoint.CS_REL_CONJ_ENFANT_COMMUN.equals(relation.getTypeRelation())) {
                        result = true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return result;
    }

    private String recupererNSSConjoint(ISFMembreFamilleRequerant[] membresFamille, String idTiersRequerant) {

        String nssConjoint = null;
        List<ISFMembreFamilleRequerant> conjoints = new ArrayList<ISFMembreFamilleRequerant>();

        for (ISFMembreFamilleRequerant tiers : membresFamille) {
            // On ne veut pas traiter le tiers requérant
            if (idTiersRequerant.equals(tiers.getIdTiers())) {
                continue;
            }
            if (ISFMembreFamille.CS_TYPE_RELATION_CONJOINT.equals(tiers.getRelationAuRequerant())) {
                conjoints.add(tiers);
            }
        }

        if (!conjoints.isEmpty()) {
            // Dans tous les cas on prend le premier conjoint
            nssConjoint = conjoints.get(0).getNss();
        }

        if (!JadeStringUtil.isEmpty(nssConjoint)) {
            nssConjoint = null;
        }
        return nssConjoint;
    }

    private String recupererNSSEnfantPlusJeune(ISFMembreFamilleRequerant[] membresFamille, String idTiersRequerant)
            throws Exception {

        String nss = null;
        // On récupère tous les enfants
        List<ISFMembreFamilleRequerant> enfants = recupererEnfants(membresFamille, idTiersRequerant);

        if (!enfants.isEmpty()) {
            Collections.sort(enfants, new REDateNaissanceComparator());
            nss = enfants.get(0).getNss();
        }
        return nss;
    }

    private List<ISFMembreFamilleRequerant> recupererEnfants(ISFMembreFamilleRequerant[] membresFamille, String idTiersRequerant) {
        List<ISFMembreFamilleRequerant> enfants = new ArrayList<>();
        for (ISFMembreFamilleRequerant tiers : membresFamille) {
            // On ne veut pas traiter le tiers requérant
            if (!idTiersRequerant.equals(tiers.getIdTiers())
                    && ISFMembreFamille.CS_TYPE_RELATION_ENFANT.equals(tiers.getRelationAuRequerant())) {
                enfants.add(tiers);
            }
        }
        return enfants;
    }

    private TypeDemandeEnum formatTypeDemande(String csTypeDemandeRente) {
        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(csTypeDemandeRente)) {
            return TypeDemandeEnum.I;
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(csTypeDemandeRente)) {
            return TypeDemandeEnum.S;
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(csTypeDemandeRente)) {
            return TypeDemandeEnum.V;
        } else {
            // TODO : Que faut-il renvoyer si cs type demande est différent des trois tests
            return TypeDemandeEnum.V;
        }
    }

    public int getTypeCalcul(String csTypeCalcul) {
        switch (csTypeCalcul) {
            case IREDemandeRente.CS_TYPE_CALCUL_STANDARD:
                return 0;
            case IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL:
                return 1;
            case IREDemandeRente.CS_TYPE_CALCUL_PMT_PROVISOIRE:
                return 2;
            default:
                return 0;
        }
    }

    public String getDateTraimentFormat() {
        JADate dateTraitement;
        JADate datePmt;
        try {
            // -- BZ6830 --//
            if (JadeDateUtil.isGlobazDate(demandeRente.getDateTraitement())) {
                String dateDuJour = JadeDateUtil.getGlobazFormattedDate(new Date());
                // Si date traitement < date du jour ==> On prend la date du jour
                if (JadeDateUtil.isDateBefore(demandeRente.getDateTraitement(), dateDuJour)) {
                    dateTraitement = new JADate(dateDuJour);
                } else {
                    dateTraitement = new JADate(demandeRente.getDateTraitement());
                }
            } else {
                dateTraitement = new JADate(JadeDateUtil.getGlobazFormattedDate(new Date()));
            }
            // -- BZ6830 --//

            datePmt = new JADate(REPmtMensuel.getDateDernierPmt(session));

            JACalendarGregorian cal = new JACalendarGregorian();
            if (cal.compare(datePmt, dateTraitement) == JACalendar.COMPARE_FIRSTUPPER) {
                return PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(datePmt.toStrAMJ());
            } else {
                return PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateTraitement.toStrAMJ());
            }

        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération de la date de traitement formaté.", e);
            return null;
        }
    }

    public XMLGregorianCalendar getDateTraitement() {
        JADate dateTraitement = null;
        JADate datePmt = null;
        try {
            // -- BZ6830 --//
            if (JadeDateUtil.isGlobazDate(demandeRente.getDateTraitement())) {
                String dateDuJour = JadeDateUtil.getGlobazFormattedDate(new Date());
                // Si date traitement < date du jour ==> On prend la date du jour
                if (JadeDateUtil.isDateBefore(demandeRente.getDateTraitement(), dateDuJour)) {
                    dateTraitement = new JADate(dateDuJour);
                } else {
                    dateTraitement = new JADate(demandeRente.getDateTraitement());
                }
            } else {
                dateTraitement = new JADate(JadeDateUtil.getGlobazFormattedDate(new Date()));
            }
            // -- BZ6830 --//

            datePmt = new JADate(REPmtMensuel.getDateDernierPmt(session));

            JACalendarGregorian cal = new JACalendarGregorian();
            if (cal.compare(datePmt, dateTraitement) == JACalendar.COMPARE_FIRSTUPPER) {
                return ParserUtils.formatDate(datePmt.toXMLDate(), YYYY_MM_DD_FORMAT);
            } else {
                return ParserUtils.formatDate(dateTraitement.toXMLDate(), YYYY_MM_DD_FORMAT);
            }

        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération de la date de traitement.", e);
            return null;
        }
    }

    public XMLGregorianCalendar getDateDepot(String date) {
        if (JadeStringUtil.isBlankOrZero(date)) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(DD_MM_YYYY_FORMAT);
        Date dateFormat = null;
        try {
            dateFormat = format.parse(date);

            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(dateFormat);

            return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (ParseException | DatatypeConfigurationException e) {
            LOG.error("Erreur lors de la récupération de la date de dépôt.", e);
        }
        return null;
    }

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public String nssBidon(String nss, String csSexe, String nomPrenom, boolean conjoint) {

        if (!JadeStringUtil.isEmpty(nss)) {
            return nss;
        }

        try {
            String nssRequerant = NSUtil.unFormatAVS(tiersRequerant.getNSS());

            // NNSS
            if (nssRequerant.length() > 11) {
                return _nssBidon(nss, csSexe, nomPrenom, conjoint);
            }
            // NAVS
            else {
                return _noAVSBidon(nss, csSexe, nomPrenom, conjoint);
            }

        } catch (Exception e) {
            return _nssBidon(nss, csSexe, nomPrenom, conjoint);
        }

    }

    /**
     * @param noAVS     DOCUMENT ME!
     * @param csSexe    DOCUMENT ME!
     * @param nomPrenom DOCUMENT ME!
     * @param conjoint  DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private String _noAVSBidon(String noAVS, String csSexe, String nomPrenom, boolean conjoint) {

        if (!JadeStringUtil.isEmpty(noAVS)) {
            return noAVS;
        }

        String retValue;

        if (JadeStringUtil.isIntegerEmpty(csSexe)) {
            /*
             * le sexe n'est pas stocke dans la situation familliale, par defaut on va prendre le sexe oppose au
             * requerant, de cette manière, les eventuelles relations de conjoint seront acceptees par ACOR
             */
            retValue = PRACORConst.CS_FEMME.equals(tiersRequerant.getSexe()) ? PRACORConst.CA_NO_AVS_BIDON_HOMME
                    : PRACORConst.CA_NO_AVS_BIDON_FEMME;
        } else {
            // le sexe est defini dans la situation familiale.
            retValue = PRACORConst.CS_FEMME.equals(csSexe) ? PRACORConst.CA_NO_AVS_BIDON_FEMME
                    : PRACORConst.CA_NO_AVS_BIDON_HOMME;
        }

        /*
         * comme a la fois les conjoints et les enfants peuvent avoir un no avs vide, il est possible qu'un enfant et un
         * conjoint ait le meme no AVS dans le fichier ACOR, ce qui fait qu'ACOR ne pourra pas determiner qui est
         * l'enfant et qui est le conjoint. Pour regler ce probleme, on differencie les no AVS bidon en se basant sur le
         * type de relation et le nomPrenom
         */
        String idNoAVSBidon = conjoint + "_" + nomPrenom;
        String noUnique = (String) idNoAVSBidons.get(idNoAVSBidon);

        if (noUnique == null) {
            noUnique = String.valueOf(idNoAVSBidons.size() + 1);
            idNoAVSBidons.put(idNoAVSBidon, noUnique);
        }

        return noUnique + retValue.substring(noUnique.length());
    }

    /**
     * @param csSexe    DOCUMENT ME!
     * @param nomPrenom DOCUMENT ME!
     * @param conjoint  DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private String _nssBidon(String nss, String csSexe, String nomPrenom, boolean conjoint) {

        if (!JadeStringUtil.isEmpty(nss)) {
            return nss;
        }

        String idNssBidon = conjoint + "_" + nomPrenom;

        // Prendre un nss de la liste des 25 et voir s'il existe déjà dans la map (itérer),
        // s'il existe, prendre un autre et retest, s'il existe pas, le retourner et l'insérer.

        boolean isOK = false;
        boolean isEqual = false;
        String nss13 = "";
        int increment = 0;

        while (!isOK) {

            nss13 = PRNSS13ChiffresUtils.getNSSErrone(increment);

            Set keys = idNSSBidons.keySet();

            for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                String nssKey = (String) idNSSBidons.get(key);

                if (nssKey.equals(nss13)) {
                    isEqual = true;
                    break;
                }

            }

            if (!isEqual) {
                isOK = true;
                idNoAVSBidons.put(idNssBidon, nss13);
            } else {
                increment++;
                isEqual = false;
            }

        }

        // le sexe est defini dans la situation familiale.
        // String nss13 = PRNSS13ChiffresUtils.getRandomNSS(getSession());

        /*
         * comme a la fois les conjoints et les enfants peuvent avoir un no avs vide, il est possible qu'un enfant et un
         * conjoint ait le meme no AVS dans le fichier ACOR, ce qui fait qu'ACOR ne pourra pas determiner qui est
         * l'enfant et qui est le conjoint. Pour regler ce probleme, on differencie les no AVS bidon en se basant sur le
         * type de relation et le nomPrenom
         */

        String noUnique = (String) idNSSBidons.get(idNssBidon);

        if (noUnique == null) {
            noUnique = nss13;
            idNSSBidons.put(idNssBidon, noUnique);
        }
        return noUnique;
    }

    private Marshaller initMarshaller(Object element) throws SAXException, JAXBException {
        if (marshaller == null) {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            URL url = getClass().getResource(XSD_FOLDER + XSD_NAME);
            Schema schema = sf.newSchema(url);

            JAXBContext jc = JAXBContext.newInstance(element.getClass());

            marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setSchema(schema);
        }
        return marshaller;
    }

    /**
     * CAUTION, only element choice of a PoolMeldungZurZAS.Lot
     * <p>
     * possible object are element of Lot {@link PoolMeldungZurZAS.Lot }
     *
     * @param element : must be an element to put on a PoolMeldungZurZAS.Lot
     * @throws ValidationException
     * @throws SAXException
     * @throws JAXBException
     */
    public void validateUnitMessage(InHostType element) throws ValidationException, JAXBException {
        final List<String> validationErrors = new LinkedList<>();
        ObjectFactory myRootFactory = new ObjectFactory();
        try {
            initMarshaller(element);
            JAXBElement<InHostType> inHostElement = myRootFactory.createInHost(element);

            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    LOG.warn("JAXB validation error : " + event.getMessage(), this);
                    validationErrors.add(event.getMessage());
                    return true;
                }

            });

            marshaller.marshal(inHostElement, System.out);

        } catch (JAXBException exception) {
            LOG.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            throw exception;
        } catch (Exception e) {
            LOG.error("impossible d'initialier un PoolMeldungZurZAS", e);
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

    }
}
