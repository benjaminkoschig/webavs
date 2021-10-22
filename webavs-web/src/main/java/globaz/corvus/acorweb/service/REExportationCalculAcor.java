package globaz.corvus.acorweb.service;

import acor.ch.admin.zas.rc.annonces.rente.rc.DJE10BeschreibungType;
import acor.ch.admin.zas.rc.annonces.rente.rc.DJE9BeschreibungType;
import acor.ch.admin.zas.rc.annonces.rente.rc.Gutschriften10Type;
import acor.ch.admin.zas.rc.annonces.rente.rc.Gutschriften9WeakType;
import acor.ch.admin.zas.rc.annonces.rente.rc.RentenaufschubType;
import acor.ch.admin.zas.rc.annonces.rente.rc.RentenvorbezugType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.AgeFlexible10;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.AiCurrentType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.AiInformations;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.AiType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.AssureType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.CiType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.CommonAgeFlexible;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.CommonRenteType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.DemandeType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.DonneesEchelleType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.EnfantType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.ExtraordinaireBase10Type;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.ExtraordinaireBase9Type;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.FlexibilisationType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.OrdinaireBase10Type;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.OrdinaireBase9Type;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.RenteExtraordinaire10Type;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.RenteExtraordinaire9Type;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.RenteOrdinaire10Type;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.RenteOrdinaire9Type;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.TypeDemandeEnum;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.common.util.Dates;
import ch.globaz.hera.business.constantes.ISFMembreFamille;
import ch.globaz.hera.business.constantes.ISFRelationConjoint;
import globaz.corvus.acor.adapter.plat.REACORDemandeAdapter;
import globaz.corvus.acorweb.business.FractionRente;
import globaz.corvus.acorweb.business.ImplMembreFamilleRequerantWrapper;
import globaz.corvus.acorweb.business.RCIContainer;
import globaz.corvus.acorweb.business.REDateNaissanceComparator;
import globaz.corvus.api.ci.IRERassemblementCI;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.ci.RERassemblementCI;
import globaz.corvus.db.ci.RERassemblementCIManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.historiques.REHistoriqueRentes;
import globaz.corvus.db.historiques.REHistoriqueRentesJoinTiersManager;
import globaz.corvus.helpers.historiques.REHistoriqueRentesJoinTiersHelper;
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
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFPeriode;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.hera.wrapper.SFPeriodeWrapper;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorTechnicalException;
import globaz.prestation.acor.web.mapper.PRAcorAssureTypeMapper;
import globaz.prestation.acor.web.mapper.PRAcorDemandeTypeMapper;
import globaz.prestation.acor.web.mapper.PRAcorEnfantTypeMapper;
import globaz.prestation.acor.web.mapper.PRAcorFamilleTypeMapper;
import globaz.prestation.acor.web.mapper.PRAcorMapper;
import globaz.prestation.acor.web.mapper.PRConverterUtils;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class REExportationCalculAcor {

    private static final Logger LOG = LoggerFactory.getLogger(REExportationCalculAcor.class);


    private BSession session;
    private String idDemande;
    private BTransaction transaction;
    private REDemandeRente demandeRente;
    private PRTiersWrapper tiersRequerant;
    private ISFMembreFamilleRequerant membreRequerant;
    private static final String IS_WANT_ADRESSE_COURRIER = "isWantAdresseCourrier";
    private EntityService entityService;
    public static final String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";
    private static final int ANTICIPATION_OR_REVOCATION = 100;
    private static final int AJOURNEMENT = 0;
    private PRAcorMapper prAcorMapper;

    public REExportationCalculAcor(BSession bSession, String idDemandeRente) {
        session = bSession;
        idDemande = idDemandeRente;
        transaction = session.getCurrentThreadTransaction();
        this.entityService = EntityService.of(session);
    }

    public InHostType createInHost() {
        LOG.info("Création du inHost.");
        InHostType inHost = new InHostType();
        try {
            demandeRente = REDemandeRente.loadDemandeRente(session, transaction, idDemande, null);
            tiersRequerant = demandeRente.loadDemandePrestation(null).loadTiers();

            String typeAdressePourRequerant = IPTConstantesExternes.TIERS_ADRESSE_TYPE_DOMICILE;
            if ("true".equals(getWantAdresseCourrierProperties())) {
                typeAdressePourRequerant = IPTConstantesExternes.TIERS_ADRESSE_TYPE_COURRIER;
            }
            this.prAcorMapper = new PRAcorMapper(typeAdressePourRequerant, this.tiersRequerant,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                    this.session);
            ISFSituationFamiliale situationFamiliale = this.situationFamiliale();
            inHost.setDemande(createDemande(demandeRente, tiersRequerant, session));
            List<ISFMembreFamilleRequerant> membresFamille = getToutesLesMembresFamillesEtEtendue();
            trouverRequerant(membresFamille);
            List<ISFMembreFamilleRequerant> membresCatAssures = filterListMembresAssures(membresFamille);
            List<ISFMembreFamilleRequerant> membresCatEnfants = filterListMembresEnfants(membresFamille);
            List<ISFMembreFamilleRequerant> conjoints = new ArrayList<>();
            conjoints.addAll(filterListMembresExConjointConjoint(membresFamille));
            conjoints.addAll(filterListMembresConjoints(membresFamille));

            PRAcorFamilleTypeMapper familleTypeMapper = new PRAcorFamilleTypeMapper(this.membreRequerant, situationFamiliale, conjoints, this.prAcorMapper);

            inHost.getAssure().addAll(createListAssures(membresCatAssures, situationFamiliale));
            inHost.getFamille().addAll(familleTypeMapper.map());

            PRAcorEnfantTypeMapper prAcorEnfantTypeMapper = new PRAcorEnfantTypeMapper(situationFamiliale, membresCatEnfants, this.prAcorMapper);

            List<EnfantType> enfantTypes = createListEnfants(prAcorEnfantTypeMapper);
            inHost.getEnfant().addAll(enfantTypes);
        } catch (Exception e) {
            LOG.error("Erreur lors de la construction du inHost.", e);
        }
        return inHost;
    }

    private List<EnfantType> createListEnfants(PRAcorEnfantTypeMapper prAcorEnfantTypeMapper) throws PRACORException {
        return prAcorEnfantTypeMapper.map(this::addRentesEnfant);
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
            if (StringUtils.equals(
                    REACORDemandeAdapter.ImplMembreFamilleRequerantWrapper.NO_CS_RELATION_EX_CONJOINT_DU_CONJOINT,
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
        PRAcorDemandeTypeMapper demandeTypeAcorMapper = new PRAcorDemandeTypeMapper(this.session, tiersRequerant);
        DemandeType demandeType = demandeTypeAcorMapper.map();

        // DONNEES FICHIER DEMANDES
        demandeType.setNavs(getNumAvsFromDemande(demandeRente, tiersRequerant, session));
        demandeType.setTypeDemande(formatTypeDemande(demandeRente.getCsTypeDemandeRente()));
        demandeType.setDateTraitement(getDateTraitement());
        demandeType.setDateDepot(Dates.toXMLGregorianCalendar(demandeRente.getDateDepot()));
        // NOUVELLES DONNES XSD OBLIGATOIRES
        demandeType.setTypeCalcul(getTypeCalcul(demandeRente.getCsTypeCalcul()));

        return demandeType;
    }

    private List<AssureType> createListAssures(List<ISFMembreFamilleRequerant> membresFamille, final ISFSituationFamiliale situationFamiliale) {
        return new PRAcorAssureTypeMapper(membresFamille, situationFamiliale, this.prAcorMapper)
                .setAddPeriodeFunction(this::addPeriodeForSurvivant)
                .map(this::addInformationInAssreType);
    }

    private AssureType addInformationInAssreType(final ISFMembreFamilleRequerant membre, final AssureType assureType) {
        PRInfoCompl infoCompl = new PRInfoCompl();
        try {
            infoCompl.setSession(getSession());
            infoCompl.setIdInfoCompl(demandeRente.getIdInfoComplementaire());
            infoCompl.retrieve();
        } catch (Exception e) {
            getSession().addError(getSession().getLabel("ERREUR_INFOS_COMP"));
        }
        assureType.setRefugie(infoCompl.getIsRefugie());
        assureType.setDonneesVeto(createDonnesVeto(infoCompl));

        // RENTES
        addRentesAssures(assureType, membre);

        // Donnees AI
        if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT, membre.getRelationAuRequerant()) && StringUtils.equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE, demandeRente.getCsTypeDemandeRente())) {
            assureType.setDonneesAI(createAiInformations((REDemandeRenteInvalidite) demandeRente));
            assureType.setReductionFauteGrave(Short.valueOf(((REDemandeRenteInvalidite) demandeRente).getPourcentRedFauteGrave()));
        } else if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT, membre.getRelationAuRequerant())) {
            REDemandeRente demandeRenteConjoint = rechercheDemandeAiConjoint(membre);
            if (demandeRenteConjoint != null) {
                assureType.setDonneesAI(createAiInformations((REDemandeRenteInvalidite) demandeRenteConjoint));
                assureType.setReductionFauteGrave(Short.valueOf(((REDemandeRenteInvalidite) demandeRenteConjoint).getPourcentRedFauteGrave()));
            }
        }

        // Anticipation ou ajournement
        if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT, membre.getRelationAuRequerant()) && demandeRente instanceof REDemandeRenteVieillesse) {
            FlexibilisationType flexibilisationType = createFlexibilisationType();
            if (Objects.nonNull(flexibilisationType)) {
                assureType.setFlexibilisation(flexibilisationType);
            }
        }

        // CI
        if (!JadeStringUtil.isBlankOrZero(membre.getIdTiers())) {
            assureType.getCi().addAll(createListCI(membre.getIdTiers()));
        }

        return assureType;
    }

    private AssureType.DonneesVeto createDonnesVeto(PRInfoCompl infoCompl) {
        AssureType.DonneesVeto donneesVeto = new AssureType.DonneesVeto();

        // TODO : voir avec French : date de veto et impact acorV3
        // Actuellement, autre raison = 2 dans WebAVS -> faut-il modifier le Code systèmes ? attention aux impacts sur l'ancien acor.
        // De plus s'il y a un veto, il faut renseigner une date de veto --> quelle date doit être prise en compte.
        String codeVeto = PRACORConst.csVetoToAcor(getSession(), infoCompl.getCsVetoPrestation());
        if (StringUtils.isNotEmpty(codeVeto)) {
            donneesVeto.setVetoPrestation(PRConverterUtils.formatRequiredInteger(codeVeto));
            donneesVeto.setDateVeto(Dates.toXMLGregorianCalendar(infoCompl.getDateInfoCompl()));
            return donneesVeto;
        }
        return null;
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
            flexibilisationType.setDebut(Dates.toXMLGregorianCalendar(demandeRente.getDateDebut()));
            flexibilisationType.setPartPercue(ANTICIPATION_OR_REVOCATION); // Pour une anticipation
        }
        boolean ajournement = ((REDemandeRenteVieillesse) demandeRente).getIsAjournementRequerant();
        String dateRevocation = ((REDemandeRenteVieillesse) demandeRente).getDateRevocationRequerant();
        if (ajournement) {
            flexibilisationType = new FlexibilisationType();
            // Revocation
            if (StringUtils.isNotEmpty(dateRevocation)) {
                LocalDate date = Dates.toDate(dateRevocation);
                date = date.plusMonths(1);
                date = date.withDayOfMonth(1);
                flexibilisationType.setDebut(Dates.toXMLGregorianCalendar(date));
                flexibilisationType.setPartPercue(ANTICIPATION_OR_REVOCATION); // Pour un ajournement révoqué
            } else {
                // Ajournement
                flexibilisationType.setDebut(Dates.toXMLGregorianCalendar(demandeRente.getDateDebut()));
                flexibilisationType.setPartPercue(AJOURNEMENT); // Pour un ajournement demandé
            }
        }
        return flexibilisationType;
    }

    private void addRentesAssures(AssureType assure, ISFMembreFamilleRequerant membre) {
        try {
            if (ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(membre.getRelationAuRequerant())) {
                reloadHistorique(membre.getIdTiers());
            }
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

    private void reloadHistorique(String idTiers) throws PRACORException {
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) getSession().newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            REHistoriqueRentesJoinTiersHelper.doReloadHistorique(getSession(), transaction, idTiers);
            if ((transaction != null) && !transaction.hasErrors() && !transaction.isRollbackOnly()) {
                transaction.commit();
            }
        } catch (Exception e) {
            String message = " Parent Exception message : " + e.getMessage();
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
    }

    private boolean isRenteExtraordinaire(String codePrestation) {
        Integer intCodePrestation = PRConverterUtils.formatRequiredInteger(codePrestation);
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

    private CommonRenteType createCommonRenteType(REHistoriqueRentes rente, boolean is10emeRevision,
                                                  boolean isOrdinaire) {
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
        commonRente.setGenre(PRConverterUtils.formatRequiredInteger(rente.getCodePrestation()));
        // Non mappé -> mettre false par défaut
        commonRente.setIndemniteForfaitaire(false);
        // 3. fraction de rente
        commonRente.setFraction(FractionRente.getValueFromConst((rente.getFractionRente())));
        if (StringUtils.isNotEmpty(rente.getQuotiteRente())) {
            commonRente.setQuotite(Double.valueOf(rente.getQuotiteRente()));
        }
        // 4. date début du droit
        commonRente.setDebutDroit(Dates.toXMLGregorianCalendar(rente.getDateDebutDroit(), "MM.yyyy"));
        // 5. date fin du droit
        commonRente.setFinDroit(Dates.toXMLGregorianCalendar(rente.getDateFinDroit(), "MM.yyyy"));

        if (commonRente.getFinDroit() != null && StringUtils.isNotEmpty(rente.getCodeMutation())) {
            commonRente.setCodeMutation(PRConverterUtils.formatRequiredInteger(rente.getCodeMutation()));
        }
        // 6. montant de la prestation
        commonRente.setMontant(PRConverterUtils.formatRequiredBigDecimalNoDecimal(rente.getMontantPrestation()));
        // 38. année du montant du ram
        commonRente.setAnneeEtat(Dates.toXMLGregorianCalendar(rente.getAnneeMontantRAM(), "yyyy"));
//        // 17. code cas spécial
        if (!JadeStringUtil.isBlankOrZero((rente.getCs1()))) {
            commonRente.getCasSpecial().add(PRConverterUtils.formatRequiredShort(rente.getCs1()));
        }
        if (!JadeStringUtil.isBlankOrZero((rente.getCs2()))) {
            commonRente.getCasSpecial().add(PRConverterUtils.formatRequiredShort(rente.getCs2()));
        }
        if (!JadeStringUtil.isBlankOrZero((rente.getCs3()))) {
            commonRente.getCasSpecial().add(PRConverterUtils.formatRequiredShort(rente.getCs3()));
        }
        if (!JadeStringUtil.isBlankOrZero((rente.getCs4()))) {
            commonRente.getCasSpecial().add(PRConverterUtils.formatRequiredShort(rente.getCs4()));
        }
        if (!JadeStringUtil.isBlankOrZero((rente.getCs5()))) {
            commonRente.getCasSpecial().add(PRConverterUtils.formatRequiredShort(rente.getCs5()));
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
        base.setAnneeNiveau(Dates.toXMLGregorianCalendar(anneeNiveau, "yyyy"));
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
        base.setAnneeNiveau(Dates.toXMLGregorianCalendar(anneeNiveau, "yyyy"));
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
        base.setAnneeNiveau(Dates.toXMLGregorianCalendar(anneeNiveau, "yyyy"));
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
        base.setAnneeNiveau(Dates.toXMLGregorianCalendar(anneeNiveau, "yyyy"));
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
        ram.setDurchschnittlichesJahreseinkommen(PRConverterUtils.formatRequiredBigDecimalNoDecimal(rente.getRam()));
        // 8. durée cotisation ram
        ram.setBeitragsdauerDurchschnittlichesJahreseinkommen(PRConverterUtils.formatRequiredBigDecimal(rente.getDureeCotRam()));
        // 31. code rev. splitté
        ram.setGesplitteteEinkommen(rente.getIsRevenuSplitte());
        return ram;
    }

    private DJE9BeschreibungType createRam9(REHistoriqueRentes rente) {
        DJE9BeschreibungType ram = new DJE9BeschreibungType();
        // 7. revenu annuel moyen
        ram.setDurchschnittlichesJahreseinkommen(PRConverterUtils.formatRequiredBigDecimalNoDecimal(rente.getRam()));
        // 8. durée cotisation ram
        ram.setBeitragsdauerDurchschnittlichesJahreseinkommen(PRConverterUtils.formatRequiredBigDecimal(rente.getDureeCotRam()));
        // 9. code revenu
        ram.setAngerechneteEinkommen(PRConverterUtils.formatRequiredShort(rente.getCodeRevenu()));
        return ram;
    }

    private RentenvorbezugType createAnticipation(REHistoriqueRentes rente) {
        RentenvorbezugType anticipation = new RentenvorbezugType();
        // 33. nbr. année anticipation
        if (!JadeStringUtil.isBlankOrZero(rente.getNbrAnneeAnticipation())) {
            anticipation.setAnzahlVorbezugsjahre(PRConverterUtils.formatRequiredInteger(rente.getNbrAnneeAnticipation()));
        }
        // 34. montant redic. pour anticipation
        if (!JadeStringUtil.isBlankOrZero(rente.getMontantReducAnticipation())) {
            anticipation.setVorbezugsreduktion(PRConverterUtils.formatRequiredBigDecimalNoDecimal(rente.getMontantReducAnticipation()));
        }
        // 35. date déb. anticipation
        anticipation.setVorbezugsdatum(Dates.toXMLGregorianCalendar(rente.getDateDebutAnticipation(), "MM.yyyy"));
        if (isAuMoinsUneDonneeObligatoireManquante(anticipation.getAnzahlVorbezugsjahre(), anticipation.getVorbezugsreduktion(), anticipation.getVorbezugsdatum())) {
            return null;
        }
        return anticipation;
    }

    private Gutschriften10Type createDonneesBonification10(REHistoriqueRentes rente) {
        Gutschriften10Type donneesBonification = new Gutschriften10Type();
        // 28. nbr. année bonif. bte
        donneesBonification.setAnzahlErziehungsgutschrift(PRConverterUtils.formatRequiredBigDecimal(rente.getNbrAnneeBTE()));
        // 29. nbr. année bonif. bta
        donneesBonification.setAnzahlBetreuungsgutschrift(PRConverterUtils.formatRequiredBigDecimal(rente.getNbrAnneeBTA()));
        // 30. nbr. année bonif. transitoire
        donneesBonification.setAnzahlUebergangsgutschrift(PRConverterUtils.formatRequiredBigDecimal(rente.getNbrAnneeBTR()));
        return donneesBonification;
    }

    private Gutschriften9WeakType createDonneesBonification9(REHistoriqueRentes rente, BigDecimal
            durchschnittlichesJahreseinkommen) {
        Gutschriften9WeakType donneesBonification = new Gutschriften9WeakType();
        // 27. montant bonus éducatif
        BigDecimal montantBTE = PRConverterUtils.formatRequiredBigDecimal(rente.getMontantBTE());
        donneesBonification.setAngerechneteErziehungsgutschrift(montantBTE.setScale(0, BigDecimal.ROUND_DOWN));
        // 28. nbr. année bonif. bte
        if (!JadeStringUtil.isBlankOrZero(rente.getNbrAnneeBTE())) {
            donneesBonification.setAnzahlErziehungsgutschrift(PRConverterUtils.formatRequiredShort(rente.getNbrAnneeBTE().substring(0, 2)));
        } else {
            donneesBonification.setAnzahlErziehungsgutschrift(new Short("0"));
        }
        // Numéro 7 - Numéro 27
        BigDecimal montant = durchschnittlichesJahreseinkommen.subtract(donneesBonification.getAngerechneteErziehungsgutschrift());
        donneesBonification.setDJEohneErziehungsgutschrift(montant.setScale(0, BigDecimal.ROUND_DOWN));
        return donneesBonification;
    }

    private RentenaufschubType createAjournement(REHistoriqueRentes rente) {
        RentenaufschubType ajournement = new RentenaufschubType();
        // 24. durée ajournement
        if (!JadeStringUtil.isBlankOrZero(rente.getDureeAjournement())) {
            ajournement.setAufschubsdauer(PRConverterUtils.formatRequiredBigDecimal(rente.getDureeAjournement()));
        }
        // 25. supplément ajournement
        if (!JadeStringUtil.isBlankOrZero(rente.getSupplementAjournement())) {
            ajournement.setAufschubszuschlag(PRConverterUtils.formatRequiredBigDecimalNoDecimal(rente.getSupplementAjournement()));
        }
        // 26. date de révocation
        String date = rente.getDateRevocationAjournement();
        if (JadeStringUtil.isBlank(date) && rente.getIsRenteAjournee()) {
            date = "99.9999";
        }
        if (date.length() < 6) {
            date = 0 + date;
        }
        ajournement.setAbrufdatum(Dates.toXMLGregorianCalendar(date, "MM.yyyy"));

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
        donnesAi.setInvaliditaetsgrad(PRConverterUtils.formatRequiredShort(rente.getDegreInvalidite()));
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
        donnesAi.setDatumVersicherungsfall(Dates.toXMLGregorianCalendar(rente.getSurvenanceEvenementAssure(), "MM.yyyy"));
        // 22. invalide précoce
        donnesAi.setIstFruehInvalid(rente.getIsInvaliditePrecoce());
        // 23. office ai
        donnesAi.setIVStelle(rente.getOfficeAI());

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
                periodeAI.setDebutInvalidite(Dates.toXMLGregorianCalendar(periode.getDateDebutInvalidite()));
                periodeAI.setFinInvalidite(Dates.toXMLGregorianCalendar(periode.getDateFinInvalidite()));
                aiInformations.getPeriodeAI().add(periodeAI);
            }
        } catch (Exception e) {
            LOG.error("Impossible de récupérer les périodes d'invalidité.", e);
        }
        if (!JadeStringUtil.isBlankOrZero(demandeRenteInvalidite.getPourcentRedNonCollaboration())) {
            AiInformations.DonneesNonCollaboration donneesNonCollaboration = new AiInformations.DonneesNonCollaboration();
            donneesNonCollaboration.setPartReduction(Integer.valueOf(demandeRenteInvalidite.getPourcentRedNonCollaboration()));
            donneesNonCollaboration.setDebut(Dates.toXMLGregorianCalendar(demandeRenteInvalidite.getDateDebutRedNonCollaboration()));
            donneesNonCollaboration.setFin(Dates.toXMLGregorianCalendar(demandeRenteInvalidite.getDateFinRedNonCollaboration()));
            aiInformations.getDonneesNonCollaboration().add(donneesNonCollaboration);
        }
        return aiInformations;
    }

    private Integer getCleInfirmite(String cleInfirmiteAtteinteFct) {
        if (!JadeStringUtil.isBlankOrZero(cleInfirmiteAtteinteFct) && cleInfirmiteAtteinteFct.length() >= 3) {
            return PRConverterUtils.formatRequiredInteger(cleInfirmiteAtteinteFct.substring(0, 3));
        }
        return null;
    }

    private Short getAtteinteFct(String cleInfirmiteAtteinteFct) {
        if (!JadeStringUtil.isBlankOrZero(cleInfirmiteAtteinteFct) && cleInfirmiteAtteinteFct.length() == 5) {
            return PRConverterUtils.formatRequiredShort(cleInfirmiteAtteinteFct.substring(3, 5));
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
        donnesEchelle.setSkala(PRConverterUtils.formatRequiredShort(rente.getEchelle()));
        // TODO : valider le résultat fonctionnel de la méthode de formattage
        // 11. durée cotisation avant 73
        donnesEchelle.setDureeEtrangereAvant73(PRConverterUtils.formatRequiredBigDecimalDuree(rente.getDureeCotiEtrangereAv73()));
        // 12. durée cotisation après 73
        donnesEchelle.setDureeEtrangereApres73(PRConverterUtils.formatRequiredBigDecimalDuree(rente.getDureeCotiEtrangereAp73()));
        // 13. mois appoint avant 73
        donnesEchelle.setAnrechnungVor1973FehlenderBeitragsmonate(PRConverterUtils.formatRequiredInteger(rente.getMoisAppointAv73()));
        // 14. mois appoint après 73
        donnesEchelle.setAnrechnungAb1973Bis1978FehlenderBeitragsmonate(PRConverterUtils.formatRequiredInteger(rente.getMoisAppointAp73()));
        // 15. durée cotis. de la classe d'age
        donnesEchelle.setBeitragsjahreJahrgang(PRConverterUtils.formatRequiredInteger(rente.getDureeCotiClasseAge()));
        // 39. durée cotis av. 73
        donnesEchelle.setBeitragsdauerVor1973(PRConverterUtils.formatRequiredBigDecimalDuree(rente.getDureeCotAv73()));
        // 40.durée cotis ap. 73
        donnesEchelle.setBeitragsdauerAb1973(PRConverterUtils.formatRequiredBigDecimalDuree(rente.getDureeCotAp73()));
        return donnesEchelle;
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
        long brancheEco = PRConverterUtils.formatRequiredLong(getSession().getCode(inscription.getBrancheEconomique()));
        if (brancheEco != 0) {
            ci.setBrancheEconomique(brancheEco);
        }

        // 3. Code diminution
        ci.setCodeDiminution(PRConverterUtils.formatRequiredShort(inscription.getCodeExtourne()));

        // 4. Genre cotisation
        ci.setGenreCotisation(PRConverterUtils.formatRequiredInteger(inscription.getGenreCotisation()));

        // 5. Code particulier
        ci.setCodeParticulier(PRConverterUtils.formatOptionalShort(inscription.getCodeParticulier()));

        // 6. Mois début période de cotisation
        if (!JadeStringUtil.isBlankOrZero(inscription.getMoisDebutCotisations())) {
            ci.setMoisDebut(PRConverterUtils.formatRequiredInteger(inscription.getMoisDebutCotisations()));
        }

        // 7. Mois de fin de période de cotisation
        if (!JadeStringUtil.isBlankOrZero(inscription.getMoisFinCotisations())) {
            ci.setMoisFin(PRConverterUtils.formatRequiredInteger(inscription.getMoisFinCotisations()));
        }

        // 8. Année de cotisation
        ci.setAnnee(convertstrAnneeToXmlGreg(inscription.getAnneeCotisations()));

        // 9. Montant du revenu en francs
        ci.setMontant(PRConverterUtils.formatRequiredBigDecimalNoDecimal(inscription.getRevenu()));

        // 10. Caisse + agence
        ci.setCaisseAgence(Integer.valueOf(inscription.getNumeroCaisse() + inscription.getNumeroAgence()));

        // 11. No de relevé
        if (JadeStringUtil.isBlankOrZero(inscription.getNoAffilie())) {
            ci.setNumeroAffilie("0");
        } else {
            ci.setNumeroAffilie(PRConverterUtils.formatStringWithoutDots(inscription.getNoAffilie()));
        }

        // 12. Code amortissement
        if (!JadeStringUtil.isBlankOrZero(inscription.getCodeADS())) {
            ci.setCodeAmortissement(inscription.getCodeADS());
        }

        // 13. Part. aux bonif. assistance
        ci.setParticipationBonificationAssistance(PRConverterUtils.formatRequiredInteger(inscription.getPartBonifAssist()));

        // 14. Code provenance
        ci.setCodeProvenance(PRConverterUtils.formatRequiredInteger(inscription.getProvenance()));

        // Code spécial
        if (!JadeStringUtil.isBlankOrZero(inscription.getCodeSpecial())) {
            ci.setCodeSpecial(inscription.getCodeSpecial());
        }
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
                    rciCo.addRCI(PRConverterUtils.formatRequiredInteger(rci.getMotif()).intValue(), rci.getIdRCI());
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
                            rciCo.addRCIAdditionnel(
                                    PRConverterUtils.formatRequiredInteger(rciAdd.getMotif()).intValue(),
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


    private EnfantType addRentesEnfant(ISFMembreFamilleRequerant membre, EnfantType enfant) {
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
            LOG.error("Erreur lors de l'ajout des rentes enfants", e);
        }
        return enfant;
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


    private Long getNumAvsFromDemande(REDemandeRente demandeRente, PRTiersWrapper tiers, BSession session) {
        Long navs = 0L;
        String strNss = "";
        try {
            // Traitement particulier que les demandes de survivants
            if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(demandeRente.getCsTypeDemandeRente())) {
                strNss = getNumAvsFromDemandeSurvivant(tiers, session);
            } else if (!Objects.isNull(tiers)) {
                strNss = tiers.getNSS();
            }
            navs = PRConverterUtils.formatNssToLong(strNss);
        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération du numéro AVS de la demande.", e);
        }
        return navs;
    }

    private String getNumAvsFromDemandeSurvivant(PRTiersWrapper tiers, BSession session) {
        String nssDemande = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
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
            ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(
                    tiersRequerant.getIdTiers(),
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

    private ISFSituationFamiliale situationFamiliale() {
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
            throw new PRAcorTechnicalException(session.getLabel("ERREUR_CHARGEMENT_SF"), e);
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
    protected ISFMembreFamilleRequerant[] getToutesLesMembresFamilles(String idTiersRequerant, BSession session) throws
            Exception {
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

    private List<ISFMembreFamilleRequerant> recupererEnfants(ISFMembreFamilleRequerant[] membresFamille, String
            idTiersRequerant) {
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
        }
        return null;
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
                return Dates.toXMLGregorianCalendar(datePmt.toXMLDate(), YYYY_MM_DD_FORMAT);
            } else {
                return Dates.toXMLGregorianCalendar(dateTraitement.toXMLDate(), YYYY_MM_DD_FORMAT);
            }

        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération de la date de traitement.", e);
            return null;
        }
    }

    /**
     * Recherche d'une demande AI du conjoint, non validée
     *
     * @param membre
     * @return la demande AI du conjoint
     */
    public REDemandeRente rechercheDemandeAiConjoint(ISFMembreFamilleRequerant membre) {
        String idTiersConjoint = membre.getIdTiers();
        REDemandeRenteJointDemandeManager mgr = new REDemandeRenteJointDemandeManager();
        mgr.setSession(getSession());
        mgr.setForIdTiersRequ(idTiersConjoint);
        mgr.setForCsEtatDemandeIn(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL + ", "
                + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE + ", "
                + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);

        mgr.setForCsTypeDemande(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE);
        mgr.setOrderBy(REDemandeRente.FIELDNAME_DATE_DEBUT + " DESC ");
        mgr.setForCsTypeCalcul(IREDemandeRente.CS_TYPE_CALCUL_STANDARD);

        try {
            mgr.find(1);
            // ---------------------------------------------------------
            // Traitement des paramètres du conjoint s'il possède une demande de
            // type AI
            // ---------------------------------------------------------
            if (!mgr.isEmpty()) {
                REDemandeRenteJointDemande elm = (REDemandeRenteJointDemande) mgr.getFirstEntity();
                REDemandeRente demConjoint = null;
                demConjoint = REDemandeRente.loadDemandeRente(getSession(), null,
                        elm.getIdDemandeRente(), elm.getCsTypeDemande());
                return demConjoint;
            }
        } catch (Exception e) {
            LOG.error("Impossible de récupérer la demande du conjoint.", e);
        }
        return null;
    }

    private ISFPeriode[] addPeriodeForSurvivant(final ISFMembreFamilleRequerant membre, ISFPeriode[] periodes) {
        // si demande survivant
        if (getTypeDemande().equals(PRACORConst.CA_TYPE_DEMANDE_SURVIVANT)) {
            // si membre = requérant
            PRDemande demandePrest = this.entityService.load(PRDemande.class, demandeRente.getIdDemandePrestation());

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
                            if (periode.getNoAvs().equals(membre.getNss())
                                    && ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE.equals(periode.getType())) {
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
        return periodes;
    }


    private BSession getSession() {
        return session;
    }
}
