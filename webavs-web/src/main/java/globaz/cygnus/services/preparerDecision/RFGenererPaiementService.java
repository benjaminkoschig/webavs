package globaz.cygnus.services.preparerDecision;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.avances.REAvance;
import globaz.corvus.db.avances.REAvanceManager;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.api.ordresversements.IRFOrdresVersements;
import globaz.cygnus.api.paiement.IRFLot;
import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.api.paiement.IRFTypePaiement;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.ordresversements.RFOrdresVersements;
import globaz.cygnus.db.paiement.RFAssDecOv;
import globaz.cygnus.db.paiement.RFLotJointPrestationJointOV;
import globaz.cygnus.db.paiement.RFLotJointPrestationJointOVManager;
import globaz.cygnus.db.paiement.RFLotManager;
import globaz.cygnus.db.paiement.RFPrestation;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeManager;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.db.qds.RFQdPrincipaleJointDossier;
import globaz.cygnus.db.qds.RFQdPrincipaleJointDossierManager;
import globaz.cygnus.utils.RFMyBigDecimal;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIPropositionCompensation;
import globaz.osiris.api.APISection;
import globaz.prestation.enums.codeprestation.soustype.PRSousTypeCodePrestationRFM;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.cygnus.RFApplicationUtils;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;

/**
 * @author fha
 * @revision JJE 02.08.2011,05.08.2011,08.08.2011,08.06.2012,13.08.2012,06.03.2013,13.08.2013
 */
public class RFGenererPaiementService {

    private static final String SEPARATEUR_CLE = "_";
    private Set<RFDecisionData> decisions = null;
    private Map<String, RFImputationDemandesData> demandesAimputerMap = null;
    // Map<idTiers,map<idSection,montant>>
    private Map<String, Map<String, RFMyBigDecimal>> dettesOsirisMap = new HashMap<String, Map<String, RFMyBigDecimal>>();
    private Boolean hasDecisionAvasad = false;
    private Boolean hasDecisionMensuelle = false;
    private Boolean hasDecisionPonctuelle = false;
    private String idDepassementQD = "";
    private String idGestionnaire = "";
    private String idInfoCompta = "";
    private String idLotDecision = "";
    private String idLotMensuel = "";
    private HashMap<String, Set<String[]>> idQdIdsDossierMap = null;
    private String idRfmAccordee = "";
    private String idRfmAccordeeParent = "";
    private boolean isAdaptationAnnuelle = false;
    private transient FWMemoryLog memoryLog = null;
    // Map<idTiers,map<idSection,montant>>
    private Map<String, Map<String, RFMyBigDecimal>> montantDetteExistanteParTiersLotOuvertMap = new HashMap<String, Map<String, RFMyBigDecimal>>();
    // Map<idDecision,Map{idSection,Montant}>
    private Map<String, Map<String, BigDecimal>> montantDetteParDecisionMap = new HashMap<String, Map<String, BigDecimal>>();
    // Map<typePcAccordee_typeRemboursement,{idPrestation}>
    private Map<String, String> prestationsCreeesMap = null;
    private Map<String, RFImputationQdsData> qdsAimputerMap = null;

    private BSession session = null;

    // Set<idTiers>
    private Set<String> tiersAvecAvanceMap = new HashSet<String>();
    private BITransaction transaction = null;

    public RFGenererPaiementService(BITransaction transactionPreparerDecision, BSession sessionPreparerDecision,
            String idGestionnaire, Set<RFDecisionData> decisions,
            Map<String, RFImputationDemandesData> demandesAimputerMap, Map<String, RFImputationQdsData> qdsAimputerMap,
            String idDepassementQd, HashMap<String, Set<String[]>> idQdIdsDossierMap, boolean isAdaptationAnnuelle,
            String idRfmAccordeeParent, boolean hasDecisionAvasad, FWMemoryLog memoryLog) {

        super();

        setTransaction(transactionPreparerDecision);
        setSession(sessionPreparerDecision);
        idDepassementQD = idDepassementQd;
        this.idGestionnaire = idGestionnaire;
        idRfmAccordee = "";
        this.isAdaptationAnnuelle = isAdaptationAnnuelle;
        this.idRfmAccordeeParent = idRfmAccordeeParent;

        dettesOsirisMap = new HashMap<String, Map<String, RFMyBigDecimal>>();
        montantDetteExistanteParTiersLotOuvertMap = new HashMap<String, Map<String, RFMyBigDecimal>>();
        montantDetteParDecisionMap = new HashMap<String, Map<String, BigDecimal>>();

        this.demandesAimputerMap = demandesAimputerMap;
        this.decisions = decisions;
        this.qdsAimputerMap = qdsAimputerMap;
        this.idQdIdsDossierMap = idQdIdsDossierMap;
        this.hasDecisionAvasad = hasDecisionAvasad;

        if (null != memoryLog) {
            this.memoryLog = memoryLog;
        } else {
            this.memoryLog = new FWMemoryLog();
        }
    }

    public final String addRfmAccordeeCascade_noCommit(BSession session, BITransaction transaction, RERenteAccordee ra,
            int validationLevel) throws Exception {

        REInformationsComptabilite ic = new REInformationsComptabilite();
        ic.setSession(session);

        // La création du compte annexe doit se faire lors de la validation de
        // la décision.
        // Ceci pour éviter de créer potentiellement des compte annexes
        // inutiles.

        // Recherche de l'adresse de paiement
        ic.setAdressePaiement(PRTiersHelper.getAdressePaiementData(session, (BTransaction) transaction,
                ra.getIdTiersBeneficiaire(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                JACalendar.todayJJsMMsAAAA()));

        if (JadeStringUtil.isBlankOrZero(ic.getIdTiersAdressePmt())) {

            ic.setAdressePaiement(PRTiersHelper.getAdressePaiementData(session, (BTransaction) transaction,
                    ra.getIdTiersBaseCalcul(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                    JACalendar.todayJJsMMsAAAA()));

        }

        if (JadeStringUtil.isBlankOrZero(ic.getIdTiersAdressePmt())) {
            RERenteAccordeeManager mgr = new RERenteAccordeeManager();
            mgr.setSession(session);
            mgr.setForIdTiersBeneficiaire(ra.getIdTiersBeneficiaire());
            mgr.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);
            mgr.setOrderBy(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + " DESC ");
            mgr.changeManagerSize(0);
            mgr.find(transaction, 1);
            if (!mgr.isEmpty()) {
                RERenteAccordee ancienneRA = (RERenteAccordee) mgr.getFirstEntity();
                REInformationsComptabilite ic2 = new REInformationsComptabilite();
                ic2.setSession(session);
                ic2.setIdInfoCompta(ancienneRA.getIdInfoCompta());
                ic2.retrieve(transaction);
                ic.setIdTiersAdressePmt(ic2.getIdTiersAdressePmt());
            }
        }

        ic.add(transaction);

        ra.setIdInfoCompta(ic.getIdInfoCompta());
        ra.add(transaction);
        return ra.getIdPrestationAccordee();
    }

    private void ajoutAssociationOvRestitDecision(String idRequerant, String idOrdreVersementRestitution,
            Set<RFDecisionData> decisions) throws Exception {
        for (RFDecisionData decisionRestitution : decisions) {
            if (decisionRestitution.isHasTraiterRestitutions()
                    && decisionRestitution.getIdRequerant().equals(idRequerant)) {
                RFAssDecOv rfAssDeOv = new RFAssDecOv();
                rfAssDeOv.setSession(getSession());
                rfAssDeOv.setIdDecision(decisionRestitution.getIdDecision());
                rfAssDeOv.setIdOv(idOrdreVersementRestitution);
                rfAssDeOv.setNumeroRestitution("");
                rfAssDeOv.add(transaction);
            }
        }
    }

    /**
     * Recherche du lot ouvert ou création si celui-ci n'existe pas. CCJU : 1 lot ouvert par gestionnaire, CCVD : 1 lot
     * ouvert pour tous En fonction de la valeur de hasDecisionPonctuelle et hasDecisionMensuelle :
     * 
     * @return String
     * @throws Exception
     */
    private String creationLot(String typeLot) throws Exception {

        RFLotManager lotManager = RFUtils.retrieveLotOuvert(session, typeLot);

        // si pas de lot RFM, on le créé
        if (lotManager.size() == 0) {

            RELot lotRente = RFUtils.addRELot(typeLot, getSession(), (BTransaction) getTransaction());
            RFUtils.addRFLot(lotRente, "", getSession(), (BTransaction) getTransaction());
            return lotRente.getIdLot();

        } else {
            if (lotManager.size() == 1) {
                return ((RELot) lotManager.getFirstEntity()).getIdLot();

            } else {
                throw new Exception(
                        "RFGenererPaiementService.creationLot():Plusieurs lots trouvées (RFGenererPaiementService::creationLot)");
            }
        }
    }

    /**
     * 
     * Création du ou des OV associé(s) à la décision
     * 
     * @param decision
     * @param prestation
     * @param demandesAimputerMap
     * @param idDepassementQD
     * @param qdsAimputerMap
     * @throws Exception
     */
    private void creationOrdresVersementPaiementMensuelCourant(RFDecisionData decision, RFPrestation prestation,
            Map<String, RFImputationDemandesData> demandesAimputerMap, String idDepassementQD,
            Map<String, RFImputationQdsData> qdsAimputerMap, String idRequerant) throws Exception {

        BigDecimal depassementQdDemandeCourante = new BigDecimal(0);

        RFImputationDemandesData demandeCourante = getDemandeMap(demandesAimputerMap,
                getIdDemandeRegimeDecision(decision));

        if (demandeCourante.isForcerPaiement()) {
            // il faut rechercher si il y a un motif de refus dépassement QD (à la charge de la DSAS)
            for (String[] idMotifRefus : demandeCourante.getMotifsDeRefus()) {

                if (idDepassementQD.equals(idMotifRefus[0])) {
                    depassementQdDemandeCourante = new BigDecimal(idMotifRefus[1]);
                }
            }
        }

        creationSimpleOrdreVersement(prestation.getIdPrestation(), idRequerant, decision.getIdAdressePaiement(),
                decision.getMontantCourantPartieRetro(), depassementQdDemandeCourante.toString(),
                IRFOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, decision.getCodeTypeDeSoin(),
                demandeCourante.isForcerPaiement(), Boolean.FALSE, Boolean.FALSE, "",
                demandeCourante.getCodeSousTypeDeSoin());

    }

    /**
     * 
     * Création du ou des OV associé(s) à la décision en paramètre
     * 
     * @param decision
     * @param prestation
     * @param demandesAimputerMap
     * @param idDepassementQD
     * @param qdsAimputerMap
     * @throws Exception
     */
    private void creationOrdresVersementPaiementPonctuel(RFDecisionData decision, RFPrestation prestation,
            Map<String, RFImputationDemandesData> demandesAimputerMap, String idDepassementQD, String idRequerant)
            throws Exception {

        BigDecimal depassementQdDemandeCourante = new BigDecimal(0);

        // ordres de versement correspondant aux demandes de la décision
        for (String idDemande : decision.getIdDemandes()) {
            RFImputationDemandesData demandeCourante = demandesAimputerMap.get(idDemande);

            if (demandeCourante.isForcerPaiement()) {
                // il faut rechercher si il y a un motif de refus dépassement QD (à la charge de la DSAS)
                for (String[] idMotifRefus : demandeCourante.getMotifsDeRefus()) {

                    if (idDepassementQD.equals(idMotifRefus[0])) {
                        depassementQdDemandeCourante = new BigDecimal(idMotifRefus[1]);
                    }
                }
            }

            creationSimpleOrdreVersement(prestation.getIdPrestation(), idRequerant, decision.getIdAdressePaiement(),
                    demandeCourante.getMontantAccepte(), depassementQdDemandeCourante.toString(),
                    IRFOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, demandeCourante.getCodeTypeDeSoin(),
                    demandeCourante.isForcerPaiement(),
                    isSourceImportation(demandeCourante.getCodeTypeDeSoin(), demandeCourante.getCsSource()),
                    Boolean.FALSE, "", demandeCourante.getCodeSousTypeDeSoin());

            depassementQdDemandeCourante = new BigDecimal(0);
        }
    }

    /**
     * 
     * Création de la prestation associée à la décision en paramètre pour un paiement courant (partie rétro)
     * 
     * @param decision
     * @param demandesAimputerMap
     * @param montantMensuel
     * @param typePaiement
     * @param dateDernierPaiement_mm_yyyy
     * @param csGenrePCAccordee
     * @param csTypePCAccordee
     * @throws Exception
     */
    private void creationPrestationAccordee(RFDecisionData decision, RFImputationDemandesData demandeCourante,
            RFImputationQdsData qdPrincipale, String dateAugmentationMMYYYY, String dateDiminutionMMYYYY)
            throws Exception {

        // Créer un enregistrement dans REINCOM
        REInformationsComptabilite ic = new REInformationsComptabilite();
        ic.setSession(session);

        // Recherche de l'adresse de paiement
        ic.setAdressePaiement(PRTiersHelper.getAdressePaiementData(session, (BTransaction) transaction,
                demandeCourante.getIdAdressePaiement(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                JACalendar.todayJJsMMsAAAA()));

        ic.add(transaction);

        REPrestationsAccordees rePrestationAccordee = new REPrestationsAccordees();
        rePrestationAccordee.setSession(session);
        // Montant mensuel
        rePrestationAccordee.setMontantPrestation(decision.getMontantMensuel());

        if (!isAdaptationAnnuelle) {
            rePrestationAccordee.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        } else {
            rePrestationAccordee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
        }

        rePrestationAccordee.setDateDebutDroit(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(demandeCourante
                .getDateDebutTraitement()));

        if (!isAdaptationAnnuelle) {

            if (!JadeStringUtil.isBlankOrZero(demandeCourante.getDateFinTraitement())) {
                rePrestationAccordee.setDateFinDroit(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(demandeCourante
                        .getDateFinTraitement()));
            } else {// Si la date de fin est null, il peut s'agir d'un régime rétroactif ne concernant pas l'année
                    // courante.

                int anneeDateDebut = Integer.valueOf(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(demandeCourante
                        .getDateDebutTraitement()));
                int anneeDateDernierPaiement = Integer.valueOf(PRDateFormater
                        .convertDate_MMxAAAA_to_AAAA(demandeCourante.getDateDernierPaiement_mm_yyyy()));

                if (anneeDateDebut >= anneeDateDernierPaiement) {
                    rePrestationAccordee.setDateFinDroit("");
                } else {
                    rePrestationAccordee.setDateFinDroit("12."
                            + PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(demandeCourante.getDateDebutTraitement()));
                }
            }

        } else {
            rePrestationAccordee.setDateFinDroit("");
        }

        boolean isSousTypesGenrePrestationActif = CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getBooleanValue();

        if (qdPrincipale.getCsTypePcAccordee().equals(IPCPCAccordee.CS_TYPE_PC_INVALIDITE)) {
            rePrestationAccordee.setCodePrestation("250");

            if (isSousTypesGenrePrestationActif) {

                if (RFApplicationUtils.isCantonVS()) {
                    setSousTypeForCCVS(qdPrincipale, rePrestationAccordee, IPCPCAccordee.CS_TYPE_PC_INVALIDITE);
                } else if (demandeCourante.getCodeTypeDeSoin().equals(RFUtils.CODE_TYPE_DE_SOIN_REGIME_2_STR)) {
                    rePrestationAccordee.setSousTypeGenrePrestation("227");
                } else if (demandeCourante.getCodeTypeDeSoin().equals(RFUtils.CODE_TYPE_DE_SOIN_FRQP_STR)) {
                    rePrestationAccordee.setSousTypeGenrePrestation("215");
                } else {
                    throw new Exception(
                            "RFGenererPaiementService.creationPrestationAccordee: Impossible de détérminer le sous type genre de la prestation accordée");
                }
            }

        } else if (qdPrincipale.getCsTypePcAccordee().equals(IPCPCAccordee.CS_TYPE_PC_SURVIVANT)) {
            rePrestationAccordee.setCodePrestation("213");

            if (isSousTypesGenrePrestationActif) {
                if (RFApplicationUtils.isCantonVS()) {
                    setSousTypeForCCVS(qdPrincipale, rePrestationAccordee, IPCPCAccordee.CS_TYPE_PC_SURVIVANT);
                } else if (demandeCourante.getCodeTypeDeSoin().equals(RFUtils.CODE_TYPE_DE_SOIN_REGIME_2_STR)) {
                    rePrestationAccordee.setSousTypeGenrePrestation("213");
                } else if (demandeCourante.getCodeTypeDeSoin().equals(RFUtils.CODE_TYPE_DE_SOIN_FRQP_STR)) {
                    rePrestationAccordee.setSousTypeGenrePrestation("201");
                } else {
                    throw new Exception(
                            "RFGenererPaiementService.creationPrestationAccordee: Impossible de détérminer le sous type genre de la prestation accordée");
                }
            }

        } else if (qdPrincipale.getCsTypePcAccordee().equals(IPCPCAccordee.CS_TYPE_PC_VIELLESSE)) {
            rePrestationAccordee.setCodePrestation("210");

            if (isSousTypesGenrePrestationActif) {
                if (RFApplicationUtils.isCantonVS()) {
                    setSousTypeForCCVS(qdPrincipale, rePrestationAccordee, IPCPCAccordee.CS_TYPE_PC_VIELLESSE);
                } else if (demandeCourante.getCodeTypeDeSoin().equals(RFUtils.CODE_TYPE_DE_SOIN_REGIME_2_STR)) {
                    rePrestationAccordee.setSousTypeGenrePrestation("213");
                } else if (demandeCourante.getCodeTypeDeSoin().equals(RFUtils.CODE_TYPE_DE_SOIN_FRQP_STR)) {
                    rePrestationAccordee.setSousTypeGenrePrestation("201");
                } else {
                    throw new Exception(
                            "RFGenererPaiementService.creationPrestationAccordee: Impossible de détérminer le sous type genre de la prestation accordée");
                }
            }

        } else {
            throw new Exception(
                    "RFGenererPaiementService.creationPrestationAccordee: Impossible de détérminer le type PC de la prestation accordée");
        }

        rePrestationAccordee.setCsGenre(IREPrestationAccordee.CS_GENRE_RFM);
        // adresse du bénéficiaire
        rePrestationAccordee.setIdTiersBeneficiaire(demandeCourante.getIdTiers());
        // important pour l'update du compte annexe à la validation!
        rePrestationAccordee.setIdInfoCompta(ic.getIdInfoCompta());

        rePrestationAccordee.add(transaction);

        RFPrestationAccordee prestAcc = new RFPrestationAccordee();

        prestAcc.setSession(session);
        prestAcc.setIdDecision(decision.getIdDecision());
        prestAcc.setCsTypeRFA(qdPrincipale.getCsTypePcAccordee());
        prestAcc.setIdRFMAccordee(rePrestationAccordee.getIdPrestationAccordee());

        if (isAdaptationAnnuelle) {
            prestAcc.setIsAdaptation(true);
        }

        idInfoCompta = rePrestationAccordee.getIdInfoCompta();

        prestAcc.setIdAdressePaiement(decision.getIdAdressePaiement());

        if (!JadeStringUtil.isBlankOrZero(idRfmAccordeeParent)) {
            prestAcc.setIdParentAdaptation(idRfmAccordeeParent);
        } else {
            prestAcc.setIdParentAdaptation("");
        }

        if (!JadeStringUtil.isBlankOrZero(dateAugmentationMMYYYY)) {
            prestAcc.setDateAugmentation("01." + dateAugmentationMMYYYY);
        } else {
            prestAcc.setDateAugmentation("");
        }

        if (!JadeStringUtil.isBlankOrZero(dateDiminutionMMYYYY)) {
            prestAcc.setDateDiminution("01." + dateDiminutionMMYYYY);
        } else {
            prestAcc.setDateDiminution("");
        }

        if (demandeCourante.getCodeTypeDeSoin().length() == 1) {
            demandeCourante.setCodeTypeDeSoin("0" + demandeCourante.getCodeTypeDeSoin());
        }

        if (demandeCourante.getCodeTypeDeSoin().equals(RFUtils.CODE_TYPE_DE_SOIN_REGIME_2_STR)) {
            if (demandeCourante.getCodeSousTypeDeSoin()
                    .equals(RFUtils.CODE_SOUS_TYPE_DE_SOIN_REGIME_DIABETIQUE_2_2_STR)) {
                prestAcc.setCs_source(IRFPrestations.CS_SOURCE_RFACCORDEES_REGIME_DIABETIQUE);
            } else {
                prestAcc.setCs_source(IRFPrestations.CS_SOURCE_RFACCORDEES_REGIME);
            }
        } else if (demandeCourante.getCodeTypeDeSoin().equals(RFUtils.CODE_TYPE_DE_SOIN_FRQP_STR)) {
            prestAcc.setCs_source(IRFPrestations.CS_SOURCE_RFACCORDEES_FRQP);
        } else {
            prestAcc.setCs_source(IRFPrestations.CS_SOURCE_RFACCORDEES_AUTRE);
        }

        prestAcc.setIdLot(idLotMensuel);

        prestAcc.add(transaction);

        idRfmAccordee = prestAcc.getIdRFMAccordee();

    }

    private void setSousTypeForCCVS(RFImputationQdsData qdPrincipale, REPrestationsAccordees prestation, String typePc)
            throws Exception {

        // AI
        if (typePc.equals(IPCPCAccordee.CS_TYPE_PC_INVALIDITE)) {
            if (qdPrincipale.getCsGenrePcAccordee().equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                prestation.setSousTypeGenrePrestation(PRSousTypeCodePrestationRFM.RFM_AI_DOMICILE_206
                        .getSousTypeCodePrestationAsString());
            } else if (qdPrincipale.getCsGenrePcAccordee().equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {
                prestation.setSousTypeGenrePrestation(PRSousTypeCodePrestationRFM.RFM_AI_HOME_212
                        .getSousTypeCodePrestationAsString());
            } else {
                throw new Exception(
                        "RFGenererPaiementService.creationPrestationAccordee: Impossible de détérminer le sous type genre de la prestation accordée (AI) d'après le genre de pcaccordee: "
                                + qdPrincipale.getCsGenrePcAccordee());
            }

        }
        // AVS
        else if (typePc.equals(IPCPCAccordee.CS_TYPE_PC_VIELLESSE) || typePc.equals(IPCPCAccordee.CS_TYPE_PC_SURVIVANT)) {
            if (qdPrincipale.getCsGenrePcAccordee().equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                prestation.setSousTypeGenrePrestation(PRSousTypeCodePrestationRFM.RFM_AVS_DOMICILE_200
                        .getSousTypeCodePrestationAsString());
            } else if (qdPrincipale.getCsGenrePcAccordee().equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {
                prestation.setSousTypeGenrePrestation(PRSousTypeCodePrestationRFM.RFM_AVS_HOME_214
                        .getSousTypeCodePrestationAsString());
            } else {
                throw new Exception(
                        "RFGenererPaiementService.creationPrestationAccordee: Impossible de détérminer le sous type genre de la prestation accordée (AVS) d'après le genre de pcaccordee: "
                                + qdPrincipale.getCsGenrePcAccordee());
            }
        } else {
            throw new Exception(
                    "RFGenererPaiementService.creationPrestationAccordee: Impossible de détérminer le sous type genre de la prestation accordée (AVS) d'après le genre de pcaccordee: "
                            + qdPrincipale.getCsGenrePcAccordee());
        }

    }

    /**
     * 
     * creation de la prestation dans le cas d'un paiement courant (partie rétroactive)
     * 
     * @param decision
     * @param demandesAimputerMap
     * @param qdsAimputerMap
     * @return
     * @throws Exception
     */
    private RFPrestation creationPrestationPaiementCourant(RFDecisionData decision,
            Map<String, RFImputationQdsData> qdsAimputerMap, String idRequerant,
            Map<String, RFImputationDemandesData> demandesAimputerMap) throws Exception {

        // on ajoute une prestation par décision
        RFPrestation prest = new RFPrestation();
        prest.setSession(session);

        // mois/année courante!
        prest.setDateMoisAnnee(JACalendar.todayJJsMMsAAAA());
        prest.setCsEtatPrestation(IRFPrestations.CS_ETAT_PRESTATION_MIS_EN_LOT);
        prest.setIdDecision(decision.getIdDecision());
        prest.setIdAdressePaiement(decision.getIdAdressePaiement());
        prest.setIdTiersBeneficiaire(idRequerant);

        // recherche du montant à payer de la décision
        prest.setMontantTotal(getMontantTotalPrestationPaiementMensuelCourant(decision, qdsAimputerMap,
                demandesAimputerMap, idRequerant).subtract(retrieveDetteDecision(decision.getIdDecision())).toString());

        RFImputationQdsData qd = getQdPrincipalMap(qdsAimputerMap, decision.getIdQdPrincipale());

        prest.setcsGenrePrestation(qd.getCsGenrePcAccordee());
        prest.setTypePrestation(qd.getCsTypePcAccordee());
        prest.setIdLot(idLotDecision);

        if (RFPropertiesUtils.ajoutDemandesEnComptabiliteSansTenirCompteTypeDeHome()) {
            prest.setRemboursementRequerant("");
        } else {
            if (JadeStringUtil.isBlankOrZero(qd.getRemboursementRequerant())) {
                prest.setRemboursementRequerant(IRFQd.CS_TYPE_REMBOURSEMENT_DOMICILE);
            } else {
                prest.setRemboursementRequerant(qd.getRemboursementRequerant());
            }
        }

        prest.setRemboursementConjoint(qd.getRemboursementConjoint());

        prest.setIsRI(qd.isRI());
        prest.setIsLAPRAMS(qd.isLAPRAMS());

        prest.add(transaction);

        if (JadeStringUtil.isBlankOrZero(prest.getIdPrestation())) {
            decision.setIdPrestation(prest.getIdPrestation());
        }

        prestationsCreeesMap.put(
                genererClefTypesPrestationsMap(prest.getTypePrestation(), prest.getRemboursementRequerant()),
                prest.getIdPrestation());

        return prest;
    }

    private RFPrestation creationPrestationPaiementPonctuel(RFDecisionData decision,
            Map<String, RFImputationQdsData> qdsAimputerMap, BigDecimal montantTotalPlusARembourserDsas,
            Map<String, RFImputationDemandesData> demandesAimputerMap, String idRequerant) throws Exception {

        RFPrestation prest = new RFPrestation();

        prest.setSession(session);
        prest.setDateMoisAnnee(JACalendar.todayJJsMMsAAAA());
        prest.setCsEtatPrestation(IRFPrestations.CS_ETAT_PRESTATION_MIS_EN_LOT);
        prest.setIdDecision(decision.getIdDecision());

        // Màj du montant en fonction du type PC des réstitutions,
        // si réstit même type que prestation courante -> ok
        // si réstit type différent que prestation courante -> Ajout de la réstit au montant
        prest.setMontantTotal(getMontantTotalPrestationPonctuelleSelonTypePcRestitution(decision,
                montantTotalPlusARembourserDsas, qdsAimputerMap, demandesAimputerMap, idRequerant).subtract(
                retrieveDetteDecision(decision.getIdDecision())).toString());

        prest.setIdLot(idLotDecision);
        prest.setIdAdressePaiement(decision.getIdAdressePaiement());
        prest.setIdTiersBeneficiaire(idRequerant);

        RFImputationQdsData qd = getQdPrincipalMap(qdsAimputerMap, decision.getIdQdPrincipale());

        prest.setcsGenrePrestation(qd.getCsGenrePcAccordee());
        prest.setTypePrestation(qd.getCsTypePcAccordee());

        if (RFPropertiesUtils.ajoutDemandesEnComptabiliteSansTenirCompteTypeDeHome()) {
            prest.setRemboursementRequerant("");
        } else {
            if (JadeStringUtil.isBlankOrZero(qd.getRemboursementRequerant())) {
                prest.setRemboursementRequerant(IRFQd.CS_TYPE_REMBOURSEMENT_DOMICILE);
            } else {
                prest.setRemboursementRequerant(qd.getRemboursementRequerant());
            }
        }

        prest.setRemboursementConjoint(qd.getRemboursementConjoint());
        prest.setIsRI(qd.isRI());
        prest.setIsLAPRAMS(qd.isLAPRAMS());

        prest.add(transaction);

        decision.setIdPrestation(prest.getIdPrestation());

        prestationsCreeesMap.put(
                genererClefTypesPrestationsMap(prest.getTypePrestation(), prest.getRemboursementRequerant()),
                prest.getIdPrestation());

        return prest;
    }

    /**
     * 
     * Créer une prestation pour ajouter l'(les) OV(s) de réstitution.
     * 
     * 2 cas peuvent se présenter:
     * 
     * Si la restitution s'effectue depuis une demande concernant un paiement mensuel future, il faut créer la
     * prestation pour ajouter l'(les) OV(s) de restitutions.
     * 
     * Si la décision ne contient que des demandes refusées et que au moins l'une d'elle est une correction, il faut
     * créer la prestation pour ajouter l'(les) OV(s) de restitutions.
     * 
     * @param decision
     * @param demandeCourant
     * @param qdsAimputerMap
     * @return String[idPrestation, csTypePcAccordee]
     * @throws Exception
     */
    private String creationPrestationRestitution(RFDecisionData decision, RFImputationDemandesData demandeCourant,
            Map<String, RFImputationQdsData> qdsAimputerMap, Map<String, RFImputationDemandesData> demandesAimputerMap,
            String idRequerant) throws Exception {

        RFPrestation prest = new RFPrestation();
        prest.setSession(session);
        prest.setDateMoisAnnee(JACalendar.todayJJsMMsAAAA());
        prest.setCsEtatPrestation(IRFPrestations.CS_ETAT_PRESTATION_MIS_EN_LOT);
        prest.setIdDecision(decision.getIdDecision());
        prest.setIdAdressePaiement(decision.getIdAdressePaiement());
        prest.setIdTiersBeneficiaire(idRequerant);

        // On restitue selon les valeurs de la Qd du parent
        String csTypePcAccordee = "";
        String csTypeRemboursement = "";
        if (!JadeStringUtil.isBlankOrZero(demandeCourant.getIdQdPrincipaleParent())) {

            RFQdPrincipale rfQdPriParent = new RFQdPrincipale();
            rfQdPriParent.setSession(getSession());
            rfQdPriParent.setIdQdPrincipale(demandeCourant.getIdQdPrincipaleParent());
            rfQdPriParent.retrieve();

            if (!rfQdPriParent.isNew()) {

                prest.setTypePrestation(rfQdPriParent.getCsTypePCAccordee());
                prest.setcsGenrePrestation(rfQdPriParent.getCsGenrePCAccordee());
                csTypePcAccordee = rfQdPriParent.getCsTypePCAccordee();
                csTypeRemboursement = rfQdPriParent.getRemboursementRequerant();

                if (RFPropertiesUtils.ajoutDemandesEnComptabiliteSansTenirCompteTypeDeHome()) {
                    prest.setRemboursementRequerant("");
                } else {
                    if (JadeStringUtil.isBlankOrZero(rfQdPriParent.getRemboursementRequerant())) {
                        prest.setRemboursementRequerant(IRFQd.CS_TYPE_REMBOURSEMENT_DOMICILE);
                    } else {
                        prest.setRemboursementRequerant(rfQdPriParent.getRemboursementRequerant());
                    }

                }

                prest.setRemboursementConjoint(rfQdPriParent.getRemboursementConjoint());
                prest.setIsRI(rfQdPriParent.getIsRI());
                prest.setIsLAPRAMS(rfQdPriParent.getIsLAPRAMS());

            } else {
                throw new Exception("RFGenererPaiementService.creationPrestationRestitution(): Qd parent introuvable");
            }
        } else {
            throw new Exception(
                    "RFGenererPaiementService.creationPrestationRestitution(): Qd parent introuvable id null");
        }

        prest.setMontantTotal(getMontantTotalPrestationRestitutionSelonTypePcRestitution(decision, qdsAimputerMap,
                demandesAimputerMap, new String[] { csTypePcAccordee, csTypeRemboursement }, idRequerant).negate()
                .toString());
        prest.setIdLot(idLotDecision);
        prest.add(transaction);

        if (JadeStringUtil.isBlankOrZero(decision.getIdPrestation())) {
            decision.setIdPrestation(prest.getIdPrestation());
        }

        prestationsCreeesMap.put(
                genererClefTypesPrestationsMap(prest.getTypePrestation(), prest.getRemboursementRequerant()),
                prest.getIdPrestation());

        return prest.getIdPrestation();

    }

    private String creationSimpleOrdreVersement(String idPrestation, String idTiers, String idTiersAdressePaiement,
            String montantDemande, String montantDepassementQD, String beneficiaire, String codeTypeSoin,
            Boolean isForcerPayement, Boolean isImportation, Boolean isCompense, String idDecisionParent,
            String codeSousTypeSoin) throws Exception {

        if (!(JadeStringUtil.isBlankOrZero(montantDemande) && JadeStringUtil.isBlankOrZero(montantDepassementQD))) {

            RFOrdresVersements ordreVersement = new RFOrdresVersements();
            ordreVersement.setSession(session);
            ordreVersement.setIdPrestation(idPrestation);
            ordreVersement.setTypeVersement(beneficiaire);
            ordreVersement.setMontant(montantDemande);
            ordreVersement.setIdTiersAdressePaiement(idTiersAdressePaiement);
            ordreVersement.setIdTiers(idTiers);
            ordreVersement.setIsImportation(isImportation);
            ordreVersement.setIsCompense(isCompense);

            if (null != codeTypeSoin) {
                ordreVersement.setIdTypeSoin(RFUtils.getIdTypeDeSoin(codeTypeSoin, session));

                if (null != codeSousTypeSoin) {
                    ordreVersement.setIdSousTypeSoin(RFUtils.getIdSousTypeDeSoin(codeTypeSoin, codeSousTypeSoin,
                            session));
                } else {
                    ordreVersement.setIdSousTypeSoin(codeSousTypeSoin);
                }

            } else {
                ordreVersement.setIdTypeSoin(codeTypeSoin);
            }

            ordreVersement.setIsForcerPayement(isForcerPayement);
            // ordreVersement.setReferencePmt("referencePmt");

            ordreVersement.setMontantDepassementQD(montantDepassementQD);
            ordreVersement.setIdDecisionParent(idDecisionParent);

            ordreVersement.add(transaction);

            return ordreVersement.getId();

        }

        return "";
    }

    private String genererClefTypesPrestationsMap(String csTypePrestationPc, String remboursementRequerant)
            throws Exception {

        StringBuffer cleTypesPrestationsMapBfr = new StringBuffer();
        cleTypesPrestationsMapBfr.append(RFUtils.getTypePc_AVS_AI(csTypePrestationPc));
        if (!RFPropertiesUtils.ajoutDemandesEnComptabiliteSansTenirCompteTypeDeHome()) {
            cleTypesPrestationsMapBfr.append(RFGenererPaiementService.SEPARATEUR_CLE);
            cleTypesPrestationsMapBfr.append(remboursementRequerant);
        }

        return cleTypesPrestationsMapBfr.toString();
    }

    /**
     * 
     * Lance la génération du paiement. Créé les prestations, les OVs et les prestations accordées
     * 
     * @param transaction
     * @param session
     * @param idGestionnaire
     * @param decisions
     * @param demandesAimputerMap
     * @param qdsAimputerMap
     * @return idPrestationAccordee (dernière Rfm accordée créée)
     * @throws Exception
     */
    public String[] genererPaiement() throws Exception {

        Set<String> idTiersDecisions = rechercheTypeDecision(decisions, demandesAimputerMap);

        boolean premierPassage = true;

        // Vérification si le lot comporte déja des Ovs de type dette. Ces dettes sont stockées dans la
        // map montantRestitutionsParTiersLotOuvert
        rechercherDettesLotOuvert();

        // Recherche si le tiers à des dettes dans la tables des avances. Ceci permet d'optimiser le traitemement en
        // recherchant le montant de la dette en compta seulement pour les tiers ayant une avance. Les tiers concernés
        // sont stockés dans la map tiersAvecAvance
        rechercherTiersAvecAvance(idTiersDecisions, decisions);

        // Pour chaque décision on recherche le montant total puis on décide si le traitement des restitutions doit
        // s'effectuer. Si c'est le cas on recherche le requérant. Ce traitement est fait en amont pour permettre de
        // renseigner la table association ov restit-décisions, il sert aussi à determiner le montant de la dette pour
        // chaque décision(=prestation)
        for (RFDecisionData decision : decisions) {

            if (null != decision) {

                BigDecimal montantTotalRestitutionBigDec = new BigDecimal(JadeStringUtil.isBlankOrZero(decision
                        .getMontantRestitution()) ? "0" : decision.getMontantRestitution());

                BigDecimal montantTotalDemandeBigDec = new BigDecimal(JadeStringUtil.isBlankOrZero(decision
                        .getMontantTotalAPayer()) ? "0" : decision.getMontantTotalAPayer().replace("'", ""))
                        .add(new BigDecimal(JadeStringUtil.isBlankOrZero(decision.getMontantARembourserDsas()) ? "0"
                                : decision.getMontantARembourserDsas().replace("'", "")));

                BigDecimal montantTotalBigDec = montantTotalDemandeBigDec.add(montantTotalRestitutionBigDec.negate());
                decision.setMontantTotalBigDec(montantTotalBigDec);

                if ((montantTotalDemandeBigDec.compareTo(new BigDecimal("0")) != 0)) {
                    decision.setHasTraiterDecision(true);
                }

                if ((montantTotalRestitutionBigDec.compareTo(new BigDecimal("0")) != 0)) {
                    decision.setHasTraiterRestitutions(true);

                    if (JadeStringUtil.isBlankOrZero(decision.getIdRequerant())) {
                        throw new Exception(
                                "RFGenererPaiementService.traiterRestitutions(): Impossible de retrouver le bénéficiaire de la décision N° "
                                        + decision.getIdDecision());
                    }
                }

                // Recherche du montant de la dette pour la décision courante
                if (!decision.isPaiementMensuel()
                        || (decision.isPaiementMensuel() && !JadeStringUtil.isBlankOrZero(decision
                                .getMontantCourantPartieRetro()))) {
                    rechercherDettesParDecision(decision);
                }
            }
        }

        // traitement des décisions
        for (RFDecisionData decision : decisions) {

            prestationsCreeesMap = new HashMap<String, String>();

            if (null != decision) {

                if (premierPassage) {

                    // pour être sur qu'on arrive pas avec des lots mal initialisés
                    idLotMensuel = "";
                    idLotDecision = "";

                    if (hasDecisionMensuelle) {
                        idLotMensuel = creationLot(IRELot.CS_TYP_LOT_MENSUEL);
                    }
                    if (hasDecisionPonctuelle) {

                        if (!hasDecisionAvasad) {
                            idLotDecision = creationLot(IRELot.CS_TYP_LOT_DECISION);
                        } else {
                            idLotDecision = creationLot(IRFLot.CS_TYP_LOT_AVASAD);
                        }
                    }
                    premierPassage = false;
                }

                traiterDecision(decision, demandesAimputerMap, qdsAimputerMap, decision.getMontantTotalBigDec(),
                        idQdIdsDossierMap);

                if (decision.isHasTraiterRestitutions()) {
                    traiterRestitutions(decision, demandesAimputerMap, qdsAimputerMap,
                            decision.getMontantTotalBigDec(), idQdIdsDossierMap, decisions);
                }

                if (montantDetteParDecisionMap.containsKey(decision.getIdDecision())) {

                    Map<String, BigDecimal> dettesOsirisParSectionDecisionCouranteMap = montantDetteParDecisionMap
                            .get(decision.getIdDecision());

                    for (String sectionKey : dettesOsirisParSectionDecisionCouranteMap.keySet()) {
                        traiterDette(decision, sectionKey, dettesOsirisParSectionDecisionCouranteMap.get(sectionKey)
                                .negate().toString());
                    }

                }

            }
        }

        return new String[] { idRfmAccordee, idInfoCompta };

    }

    /**
     * @param idTiers
     *            l'idTiers de la personne susceptible de devoir compenser des trucs
     * @param montant
     *            montant qu'on peut compenser
     * @return une Collection de CAPropositionCompensation
     * @throws Exception
     *             DOCUMENT ME!
     */
    private Collection<APISection> getCollectionSectionsACompenser(BSession session, String idTiers, FWCurrency montant)
            throws Exception {
        // BISession sessionOsiris = PRSession.connectSession(session, "OSIRIS");

        APIPropositionCompensation propositionCompensation = (APIPropositionCompensation) session
                .getAPIFor(APIPropositionCompensation.class);
        FWCurrency montantTotalNegate = new FWCurrency(montant.toString());
        montantTotalNegate.negate();

        Collection<APISection> compensations = propositionCompensation.propositionCompensation(
                Integer.parseInt(idTiers), montantTotalNegate, APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN);

        return compensations;
    }

    /**
     * 
     * Recherche le type pc de la demande corrigée et le type pc de la demande parent
     * 
     * @param demandeCourante
     * @param qdsAimputerMap
     * @return String[csTypePcAccordeeDemandeCorrigee, csTypePcAccordeeParent, idTiersRequerant, idTiersRequerantParent,
     *         csTypeRemboursementDemandeCorrigee, csTypeRemboursementParent]
     * @throws Exception
     */
    private String[] getCsTypePcTypeRemboursementCourantCorrection(RFImputationDemandesData demandeCourante,
            RFDecisionData decision, Map<String, RFImputationQdsData> qdsAimputerMap, String idTiersRequerant)
            throws Exception {

        String csTypePcAccordeeParent = "";
        String idTiersRequerantParent = "";
        String csTypeRemboursementParent = "";

        RFQdPrincipaleJointDossierManager rfQdPriJoiDosMgrParent = new RFQdPrincipaleJointDossierManager();
        rfQdPriJoiDosMgrParent.setSession(getSession());
        Set<String> idsQdSet = new HashSet<String>();
        idsQdSet.add(demandeCourante.getIdQdPrincipaleParent());
        rfQdPriJoiDosMgrParent.setForIdsQd(idsQdSet);
        rfQdPriJoiDosMgrParent.setForCsTypeRelation(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        rfQdPriJoiDosMgrParent.find();

        if (rfQdPriJoiDosMgrParent.size() == 1) {

            RFQdPrincipaleJointDossier qdParent = (RFQdPrincipaleJointDossier) rfQdPriJoiDosMgrParent.getFirstEntity();

            csTypePcAccordeeParent = qdParent.getCsTypePCAccordee();

            if (RFPropertiesUtils.ajoutDemandesEnComptabiliteSansTenirCompteTypeDeHome()) {
                csTypeRemboursementParent = "";
            } else {
                if (JadeStringUtil.isBlankOrZero(qdParent.getRemboursementRequerant())) {
                    csTypeRemboursementParent = IRFQd.CS_TYPE_REMBOURSEMENT_DOMICILE;
                } else {
                    csTypeRemboursementParent = qdParent.getRemboursementRequerant();
                }
            }

            idTiersRequerantParent = qdParent.getIdTiers();
        } else {
            throw new Exception("RFGenererPaiementService.traiterRestitutions(): Qd parent introuvable");
        }

        String csTypePcAccordeeDemandeCorrigee = "";
        String csTypeRemboursementDemandeCorrigee = "";
        if (!JadeStringUtil.isBlankOrZero(decision.getIdQdPrincipale())) {
            RFImputationQdsData qdDemandeCorrigee = getQdPrincipalMap(qdsAimputerMap, decision.getIdQdPrincipale());
            csTypePcAccordeeDemandeCorrigee = qdDemandeCorrigee.getCsTypePcAccordee();

            if (RFPropertiesUtils.ajoutDemandesEnComptabiliteSansTenirCompteTypeDeHome()) {
                csTypeRemboursementDemandeCorrigee = "";
            } else {
                if (JadeStringUtil.isBlankOrZero(qdDemandeCorrigee.getRemboursementRequerant())) {
                    csTypeRemboursementDemandeCorrigee = IRFQd.CS_TYPE_REMBOURSEMENT_DOMICILE;
                } else {
                    csTypeRemboursementDemandeCorrigee = qdDemandeCorrigee.getRemboursementRequerant();
                }
            }
        }

        return new String[] { csTypePcAccordeeDemandeCorrigee, csTypePcAccordeeParent, idTiersRequerant,
                idTiersRequerantParent, csTypeRemboursementDemandeCorrigee, csTypeRemboursementParent };
    }

    private RFImputationDemandesData getDemandeMap(Map<String, RFImputationDemandesData> demandesAimputerMap,
            String idDemande) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(idDemande)) {

            if (demandesAimputerMap.containsKey(idDemande)) {

                RFImputationDemandesData demandeData = demandesAimputerMap.get(idDemande);

                if (null != demandeData) {
                    return demandeData;
                } else {
                    throw new Exception("RFGenererPaiementService.getDemandeMap(): demande null");
                }
            } else {
                throw new Exception("RFGenererPaiementService.getDemandeMap(): Impossible de retrouver la demande");
            }

        } else {
            throw new Exception("RFGenererPaiementService.getDemandeMap(): id demande null");
        }
    }

    public Boolean getHasDecisionAvasad() {
        return hasDecisionAvasad;
    }

    public Boolean getHasDecisionMensuelle() {
        return hasDecisionMensuelle;
    }

    public Boolean getHasDecisionPonctuelle() {
        return hasDecisionPonctuelle;
    }

    private String getIdDemandeRegimeDecision(RFDecisionData decision) throws Exception {

        if (decision.getIdDemandes().size() == 1) {
            String idDemandeRegime = (String) decision.getIdDemandes().toArray()[0];
            if ((idDemandeRegime != null) && !JadeStringUtil.isBlankOrZero(idDemandeRegime)) {
                return idDemandeRegime;
            } else {
                throw new Exception("RFGenererPaiementService.idDemandeRegimeDecision(): demande de régime null");
            }

        } else {
            throw new Exception("RFGenererPaiementService.idDemandeRegimeDecision(): demande de régime introuvable");
        }

    }

    public String getIdDepassementQD() {
        return idDepassementQD;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdLotDecision() {
        return idLotDecision;
    }

    public String getIdLotMensuel() {
        return idLotMensuel;
    }

    public FWMemoryLog getMemoryLog() {
        return memoryLog;
    }

    private BigDecimal getMontantTotalPrestationPaiementMensuelCourant(RFDecisionData decision,
            Map<String, RFImputationQdsData> qdsAimputerMap, Map<String, RFImputationDemandesData> demandesAimputerMap,
            String idTiersRequerant) throws Exception {

        BigDecimal montantTotalSelonTypePcRestitutionBigDec = new BigDecimal(decision.getMontantCourantPartieRetro());

        for (String idDemande : decision.getIdDemandes()) {

            RFImputationDemandesData demandeCourante = demandesAimputerMap.get(idDemande);

            if (demandeCourante != null) {

                if (!JadeStringUtil.isBlankOrZero(demandeCourante.getRestitutionMontantAPayeParent())
                        || !JadeStringUtil.isBlankOrZero(demandeCourante.getRestitutionMontantDepassementQdParent())) {

                    String[] csTypePcTypeRemboursementCorrectionParent = getCsTypePcTypeRemboursementCourantCorrection(
                            demandeCourante, decision, qdsAimputerMap, idTiersRequerant);

                    // Si le type PC de la décision en cours est indéfini (pas de Qd, toutes les demandes sont
                    // refusées), le montant total de la décision
                    // est à zéro -> pas de màj, car la restitution concernera une nouvelle prestation
                    if (!JadeStringUtil.isBlankOrZero(csTypePcTypeRemboursementCorrectionParent[0])) {

                        if ((RFUtils.getTypePc_AVS_AI(csTypePcTypeRemboursementCorrectionParent[0]) == RFUtils
                                .getTypePc_AVS_AI(csTypePcTypeRemboursementCorrectionParent[1]))
                                && (csTypePcTypeRemboursementCorrectionParent[4]
                                        .equals(csTypePcTypeRemboursementCorrectionParent[5]) || RFPropertiesUtils
                                        .ajoutDemandesEnComptabiliteSansTenirCompteTypeDeHome())) {

                            // Si l'OV est compensée par defaut, on la prend pas en compte
                            if (csTypePcTypeRemboursementCorrectionParent[2]
                                    .equals(csTypePcTypeRemboursementCorrectionParent[3])) {

                                montantTotalSelonTypePcRestitutionBigDec = montantTotalSelonTypePcRestitutionBigDec
                                        .subtract((new BigDecimal(demandeCourante.getRestitutionMontantAPayeParent())
                                                .add(JadeStringUtil.isBlankOrZero(demandeCourante
                                                        .getRestitutionMontantDepassementQdParent()) ? new BigDecimal(
                                                        "0") : new BigDecimal(demandeCourante
                                                        .getRestitutionMontantDepassementQdParent()))));
                            }
                        }

                    } else {
                        return montantTotalSelonTypePcRestitutionBigDec;
                    }
                }
            } else {
                throw new Exception(
                        "RFGenererPaiementService.getMontantTotalPrestationPonctuelleSelonTypePcRestitution(): Qd null");
            }

        }

        return montantTotalSelonTypePcRestitutionBigDec;

    }

    /**
     * 
     * Met à jour le montant de la prestation existante (car autres demandes acceptées dans décision) en ajoutant les
     * montants des restitutions qui feront partie d'une autre prestation
     * 
     * @param decision
     * @param montantBigDec
     * @param qdsAimputerMap
     * @param demandesAimputerMap
     * @return
     * @throws Exception
     */
    private BigDecimal getMontantTotalPrestationPonctuelleSelonTypePcRestitution(RFDecisionData decision,
            BigDecimal montantTotalPlusARembourserDsas, Map<String, RFImputationQdsData> qdsAimputerMap,
            Map<String, RFImputationDemandesData> demandesAimputerMap, String idTiersRequerant) throws Exception {

        BigDecimal montantTotalSelonTypePcRestitutionBigDec = new BigDecimal("0.00");

        montantTotalSelonTypePcRestitutionBigDec = montantTotalSelonTypePcRestitutionBigDec
                .add(montantTotalPlusARembourserDsas);

        for (String idDemande : decision.getIdDemandes()) {

            RFImputationDemandesData demandeCourante = demandesAimputerMap.get(idDemande);

            if (demandeCourante != null) {

                if (!JadeStringUtil.isBlankOrZero(demandeCourante.getRestitutionMontantAPayeParent())
                        || !JadeStringUtil.isBlankOrZero(demandeCourante.getRestitutionMontantDepassementQdParent())) {

                    String[] csTypePcTypeRemboursementCorrectionParent = getCsTypePcTypeRemboursementCourantCorrection(
                            demandeCourante, decision, qdsAimputerMap, idTiersRequerant);

                    // Si le type PC de la décision en cours est indéfini (pas de Qd, toutes les demandes sont
                    // refusées), le montant total de la décision
                    // est à zéro -> pas de màj, car la restitution concernera une nouvelle prestation
                    if (!JadeStringUtil.isBlankOrZero(csTypePcTypeRemboursementCorrectionParent[0])) {

                        if ((RFUtils.getTypePc_AVS_AI(csTypePcTypeRemboursementCorrectionParent[0]) != RFUtils
                                .getTypePc_AVS_AI(csTypePcTypeRemboursementCorrectionParent[1]))
                                || (!csTypePcTypeRemboursementCorrectionParent[4]
                                        .equals(csTypePcTypeRemboursementCorrectionParent[5]) && !RFPropertiesUtils
                                        .ajoutDemandesEnComptabiliteSansTenirCompteTypeDeHome())) {

                            // Le montantTotalSelonTypePcRestitutionBigDec est égale à la somme de tous les ovs. Il
                            // faut donc additionner les Ovs de type restitution qui font l'objet d'une autre prestation
                            montantTotalSelonTypePcRestitutionBigDec = montantTotalSelonTypePcRestitutionBigDec
                                    .add(new BigDecimal(demandeCourante.getRestitutionMontantAPayeParent()).add(demandeCourante
                                            .getRestitutionIsForcerPaiementParent() ? (JadeStringUtil
                                            .isBlankOrZero(demandeCourante.getRestitutionMontantDepassementQdParent()) ? new BigDecimal(
                                            "0") : new BigDecimal(demandeCourante
                                            .getRestitutionMontantDepassementQdParent()))
                                            : new BigDecimal("0")));
                        } else {

                            // Si l'OV n'est pas compensée par defaut, on ne la prend pas en compte
                            if (!csTypePcTypeRemboursementCorrectionParent[2]
                                    .equals(csTypePcTypeRemboursementCorrectionParent[3])) {

                                montantTotalSelonTypePcRestitutionBigDec = montantTotalSelonTypePcRestitutionBigDec
                                        .add(new BigDecimal(demandeCourante.getRestitutionMontantAPayeParent()).add(demandeCourante
                                                .getRestitutionIsForcerPaiementParent() ? JadeStringUtil
                                                .isBlankOrZero(demandeCourante
                                                        .getRestitutionMontantDepassementQdParent()) ? new BigDecimal(
                                                "0") : new BigDecimal(demandeCourante
                                                .getRestitutionMontantDepassementQdParent()) : new BigDecimal("0")));
                            }
                        }

                    } else {
                        // de toute façon = 0.00 ...
                        return montantTotalPlusARembourserDsas;
                    }
                }
            } else {
                throw new Exception(
                        "RFGenererPaiementService.getMontantTotalPrestationPonctuelleSelonTypePcRestitution(): Qd null");
            }

        }

        return montantTotalSelonTypePcRestitutionBigDec;

    }

    private BigDecimal getMontantTotalPrestationRestitutionSelonTypePcRestitution(RFDecisionData decision,
            Map<String, RFImputationQdsData> qdsAimputerMap, Map<String, RFImputationDemandesData> demandesAimputerMap,
            String[] csTypePcAccordeeTypeRemboursementRestitution, String idTiersRequerant) throws Exception {

        // Recherche du montant total en tenant compte des restitutions
        BigDecimal montantTotalSelonTypePcRestitutionBigDec = new BigDecimal("0.00");
        for (String idDemande : decision.getIdDemandes()) {

            RFImputationDemandesData demandeCourante = demandesAimputerMap.get(idDemande);

            if (demandeCourante != null) {
                if (!JadeStringUtil.isBlankOrZero(demandeCourante.getRestitutionMontantAPayeParent())
                        || !JadeStringUtil.isBlankOrZero(demandeCourante.getRestitutionMontantDepassementQdParent())) {

                    String[] csTypePcTypeRemboursementCorrectionParent = getCsTypePcTypeRemboursementCourantCorrection(
                            demandeCourante, decision, qdsAimputerMap, idTiersRequerant);

                    // Si le type pc de l'ov de restitution est égale au type pc de la prestation de restitution et que
                    // cette Ov sera compensée par défaut (idTiers restit = idTiers prest) alors on la prend en compte
                    if ((RFUtils.getTypePc_AVS_AI(csTypePcAccordeeTypeRemboursementRestitution[0]) == RFUtils
                            .getTypePc_AVS_AI(csTypePcTypeRemboursementCorrectionParent[1]))
                            && csTypePcTypeRemboursementCorrectionParent[2]
                                    .equals(csTypePcTypeRemboursementCorrectionParent[3])
                            && (csTypePcTypeRemboursementCorrectionParent[5]
                                    .equals(csTypePcAccordeeTypeRemboursementRestitution[1]) || RFPropertiesUtils
                                    .ajoutDemandesEnComptabiliteSansTenirCompteTypeDeHome())) {

                        BigDecimal restitutionMontantAPayeParentBigDec = new BigDecimal("0");
                        if (!JadeStringUtil.isBlankOrZero(demandeCourante.getRestitutionMontantAPayeParent())) {
                            restitutionMontantAPayeParentBigDec = restitutionMontantAPayeParentBigDec
                                    .add(new BigDecimal(demandeCourante.getRestitutionMontantAPayeParent()));
                        }

                        montantTotalSelonTypePcRestitutionBigDec = montantTotalSelonTypePcRestitutionBigDec
                                .add(restitutionMontantAPayeParentBigDec.add(demandeCourante
                                        .getRestitutionIsForcerPaiementParent() ? (JadeStringUtil
                                        .isBlankOrZero(demandeCourante.getRestitutionMontantDepassementQdParent()) ? new BigDecimal(
                                        "0") : new BigDecimal(demandeCourante
                                        .getRestitutionMontantDepassementQdParent()))
                                        : new BigDecimal("0")));
                    }

                }
            } else {
                throw new Exception(
                        "RFGenererPaiementService.getMontantTotalPrestationRestitutionSelonTypePcRestitution(): Qd null");
            }
        }

        return montantTotalSelonTypePcRestitutionBigDec;

    }

    private RFImputationQdsData getQdPrincipalMap(Map<String, RFImputationQdsData> qdsAimputerMap, String idQd)
            throws Exception {

        if (!JadeStringUtil.isBlankOrZero(idQd)) {

            if (qdsAimputerMap.containsKey(idQd)) {

                RFImputationQdsData qdPrincipaleData = qdsAimputerMap.get(idQd);

                if (null != qdPrincipaleData) {
                    return qdPrincipaleData;
                } else {
                    throw new Exception("RFGenererPaiementService.getQdPrincipalMap(): Qd null");
                }
            } else {
                throw new Exception("RFGenererPaiementService.getQdPrincipalMap(): Impossible de retrouver la Qd");
            }

        } else {
            throw new Exception("RFGenererPaiementService.getQdPrincipalMap(): id Qd null");
        }
    }

    public BSession getSession() {
        return session;
    }

    public BITransaction getTransaction() {
        return transaction;
    }

    private Boolean isSourceImportation(String codeTypeDeSoin, String csSource) {
        if (codeTypeDeSoin.equals(IRFCodeTypesDeSoins.TYPE_20_FINANCEMENT_DES_SOINS)
                && csSource.equals(IRFDemande.SYSTEME)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private void modificationPrestationAccordee(String idDecisionRfmAccordee, String dateFinDroitDernierPaiementRente)
            throws Exception {

        // Recherche de la prestation accordee RFM
        RFPrestationAccordeeManager prestationAccordeeMgr = new RFPrestationAccordeeManager();
        prestationAccordeeMgr.setSession(session);
        // prestationAccordeeMgr.setForIdRFMAccordee(RFGenererPaiementService.idRfmAccordee);
        prestationAccordeeMgr.setForIdDecision(idDecisionRfmAccordee);
        prestationAccordeeMgr.changeManagerSize(0);
        prestationAccordeeMgr.find();

        if (prestationAccordeeMgr.size() > 0) {

            for (Iterator<RFPrestationAccordee> it = prestationAccordeeMgr.iterator(); it.hasNext();) {

                RFPrestationAccordee RFMAccordee = it.next();

                // Recherche de la prestation accordee Rente
                if (null != RFMAccordee) {

                    REPrestationsAccordees renteAccordee = new REPrestationsAccordees();
                    renteAccordee.setSession(session);
                    renteAccordee.setIdPrestationAccordee(RFMAccordee.getIdRFMAccordee());
                    renteAccordee.retrieve();

                    if (!renteAccordee.isNew()) {

                        RFPrestationAccordee rfPrestationAccordee = new RFPrestationAccordee();
                        rfPrestationAccordee.setSession(session);
                        rfPrestationAccordee.setIdRFMAccordee(renteAccordee.getIdPrestationAccordee());
                        // rfPrestationAccordee.setIdLot(renteAccordee.getIdLot());

                        rfPrestationAccordee.retrieve();

                        if (!rfPrestationAccordee.isNew()) {

                            rfPrestationAccordee.setIdGestionnairePreparerDecision(idGestionnaire);

                            if (JadeStringUtil.isBlankOrZero(renteAccordee.getDateFinDroit())) {
                                rfPrestationAccordee.setDateFinDroitInitiale("");
                            } else {
                                rfPrestationAccordee.setDateFinDroitInitiale("01." + renteAccordee.getDateFinDroit());
                            }

                            rfPrestationAccordee.setDateDiminutionInitiale(rfPrestationAccordee.getDateDiminution());

                            JADate dateFinDroitJaD = new JADate("01." + renteAccordee.getDateFinDroit());
                            JADate dateFinDroitDernierPaiementRenteJaD = new JADate("01."
                                    + dateFinDroitDernierPaiementRente);
                            JACalendar cal = new JACalendarGregorian();

                            // Si la date de fin de droit est plus petite que la date de dernier paiement des rentes, on
                            // la garde sinon on utilise
                            // la date de dernier paiement des rentes
                            if (!JadeStringUtil.isBlankOrZero(renteAccordee.getDateFinDroit())
                                    && (cal.compare(dateFinDroitJaD, dateFinDroitDernierPaiementRenteJaD) == JACalendar.COMPARE_FIRSTLOWER)) { // <
                                rfPrestationAccordee.setDateDiminution(rfPrestationAccordee.getDateDiminution());
                                renteAccordee.setDateFinDroit(renteAccordee.getDateFinDroit());
                            } else { // >= et 0
                                rfPrestationAccordee.setDateDiminution("01."
                                        + (dateFinDroitDernierPaiementRenteJaD.getMonth() > 9 ? String
                                                .valueOf(dateFinDroitDernierPaiementRenteJaD.getMonth()) : String
                                                .valueOf("0" + dateFinDroitDernierPaiementRenteJaD.getMonth())) + "."
                                        + dateFinDroitDernierPaiementRenteJaD.getYear());

                                renteAccordee
                                        .setDateFinDroit((dateFinDroitDernierPaiementRenteJaD.getMonth() > 9 ? String
                                                .valueOf(dateFinDroitDernierPaiementRenteJaD.getMonth()) : String
                                                .valueOf("0" + dateFinDroitDernierPaiementRenteJaD.getMonth()))
                                                + "." + dateFinDroitDernierPaiementRenteJaD.getYear());
                            }

                            renteAccordee.update(transaction);
                            rfPrestationAccordee.update(transaction);

                            getMemoryLog()
                                    .logMessage(
                                            getSession()
                                                    .getLabel(
                                                            "PROCESS_PREPARER_DECISIONS_CORRECTION_PLUSIEURS_ANNEES_PRESTATION_ACCORDEE")
                                                    + " " + rfPrestationAccordee.getIdRFMAccordee(),
                                            FWMessage.INFORMATION,
                                            "RFGenererPaiementService.modificationPrestationAccordee()");
                        } else {
                            throw new Exception(
                                    "RFGenererPaiementService.modificationPrestationAccordee: Rente RFM accordée non trouvée");
                        }

                    } else {
                        throw new Exception(
                                "RFGenererPaiementService.modificationPrestationAccordee: Rente accordée non trouvée");
                    }

                } else {
                    throw new Exception(
                            "RFGenererPaiementService.modificationPrestationAccordee: Prestation accordée non trouvée");
                }
            }
        } else {
            throw new Exception(
                    "RFGenererPaiementService.modificationPrestationAccordee: Prestation accordée non trouvée");
        }
    }

    private void rechercherDettesLotOuvert() throws Exception {

        RFLotJointPrestationJointOVManager rfLotPreJoiOVMgr = new RFLotJointPrestationJointOVManager();
        rfLotPreJoiOVMgr.setSession(getSession());
        rfLotPreJoiOVMgr.setForEtatsLot(new String[] { IRELot.CS_ETAT_LOT_OUVERT, IRELot.CS_ETAT_LOT_ERREUR });
        rfLotPreJoiOVMgr.find();

        Iterator<RFLotJointPrestationJointOV> rfLotPreJoiOVItr = rfLotPreJoiOVMgr.iterator();
        Set<String> ovTraiteSet = new HashSet<String>();
        while (rfLotPreJoiOVItr.hasNext()) {

            RFLotJointPrestationJointOV rfLotPreJoiOVCourant = rfLotPreJoiOVItr.next();
            if (rfLotPreJoiOVCourant != null) {

                if (rfLotPreJoiOVCourant.getTypeVersement().equals(IRFOrdresVersements.CS_TYPE_DETTE)) {
                    // Si l'ov n'a pas déja été traité
                    if (!ovTraiteSet.contains(rfLotPreJoiOVCourant.getIdOrdreVersement())) {
                        // Ajout de la dette pour le tiers de l'ov
                        if (montantDetteExistanteParTiersLotOuvertMap.containsKey(rfLotPreJoiOVCourant.getIdTiers())) {

                            Map<String, RFMyBigDecimal> montantDetteParSectionCourantMap = montantDetteExistanteParTiersLotOuvertMap
                                    .get(rfLotPreJoiOVCourant.getIdTiers());

                            // Ajout du montant par section
                            if (montantDetteParSectionCourantMap.containsKey(rfLotPreJoiOVCourant
                                    .getIdSectionOrdreVersement())) {

                                RFMyBigDecimal montantSectionCourantBigDec = montantDetteParSectionCourantMap
                                        .get(rfLotPreJoiOVCourant.getIdSectionOrdreVersement());
                                montantSectionCourantBigDec.setValeur(montantSectionCourantBigDec.getValeur().add(
                                        new BigDecimal(rfLotPreJoiOVCourant.getMontantOrdreVersement())));
                            } else {

                                montantDetteParSectionCourantMap.put(
                                        rfLotPreJoiOVCourant.getIdSectionOrdreVersement(),
                                        new RFMyBigDecimal(new BigDecimal(rfLotPreJoiOVCourant
                                                .getMontantOrdreVersement())));
                            }
                        } else {

                            Map<String, RFMyBigDecimal> montantDetteParSectionCourantMap = new HashMap<String, RFMyBigDecimal>();
                            montantDetteParSectionCourantMap
                                    .put(rfLotPreJoiOVCourant.getIdSectionOrdreVersement(), new RFMyBigDecimal(
                                            new BigDecimal(rfLotPreJoiOVCourant.getMontantOrdreVersement())));

                            montantDetteExistanteParTiersLotOuvertMap.put(rfLotPreJoiOVCourant.getIdTiers(),
                                    montantDetteParSectionCourantMap);

                        }

                        ovTraiteSet.add(rfLotPreJoiOVCourant.getIdOrdreVersement());
                    }
                }
            } else {
                throw new Exception("RFGenererPaiementService.rechercheRestitutionsLotOuvert(): Ovs courante null");
            }
        }
    }

    private void rechercherDettesParDecision(RFDecisionData decision) throws Exception {
        // Recherche de la dette pour le tiers bénéficiaire de la décision, seulement si une avance à été
        // enregistrée et si le requerant = le tiers payant
        if (tiersAvecAvanceMap.contains(decision.getIdRequerant())
                && decision.getIdRequerant().equals(decision.getIdAdressePaiement())) {

            // Si la dette n'a pas encore été ajoutée à la table dettesOsirisMap on l'ajoute
            if (!dettesOsirisMap.containsKey(decision.getIdRequerant())) {

                BISession sessionOsiris = PRSession.connectSession(session, "OSIRIS");

                Collection<APISection> propositionsCompensationsPourTiersEnCours = getCollectionSectionsACompenser(
                        (BSession) sessionOsiris, decision.getIdRequerant(), new FWCurrency(999999999));
                Iterator<APISection> propositionsCompensationsPourTiersEnCoursItr = propositionsCompensationsPourTiersEnCours
                        .iterator();

                // On renseigne la map contenant les dettes osiris
                Map<String, RFMyBigDecimal> dettesOsirisSectionMap = new HashMap<String, RFMyBigDecimal>();
                while (propositionsCompensationsPourTiersEnCoursItr.hasNext()) {

                    APISection sectionCourante = propositionsCompensationsPourTiersEnCoursItr.next();
                    if (APISection.ID_CATEGORIE_SECTION_AVANCE.equals(sectionCourante.getCategorieSection())) {

                        // MàJ de la dette en fonction des dettes existantes dans les lots ouverts
                        BigDecimal montantDetteOsirisPourSectionBigDec = new BigDecimal(sectionCourante.getSolde());
                        if (montantDetteExistanteParTiersLotOuvertMap.containsKey(decision.getIdRequerant())) {

                            Map<String, RFMyBigDecimal> montantDetteExistanteParSectionLotOuvert = montantDetteExistanteParTiersLotOuvertMap
                                    .get(decision.getIdRequerant());

                            if (montantDetteExistanteParSectionLotOuvert.containsKey(sectionCourante.getIdSection())) {
                                montantDetteOsirisPourSectionBigDec = montantDetteOsirisPourSectionBigDec
                                        .add(montantDetteExistanteParSectionLotOuvert.get(
                                                sectionCourante.getIdSection()).getValeur());
                            }
                        }

                        dettesOsirisSectionMap.put(sectionCourante.getIdSection(), new RFMyBigDecimal(
                                montantDetteOsirisPourSectionBigDec));
                    }
                }
                dettesOsirisMap.put(decision.getIdRequerant(), dettesOsirisSectionMap);
            }

            boolean isPaiementMensuelRetro = false;
            // Dispatch des dettes sur la décision courante
            Map<String, RFMyBigDecimal> dettesOsirisParSectionPourTiers = dettesOsirisMap
                    .get(decision.getIdRequerant());
            Map<String, BigDecimal> montantDetteParSectionDecisionMap = new HashMap<String, BigDecimal>();

            BigDecimal montantTotalAPayerDecisionBigDec = null;
            if (JadeStringUtil.isBlankOrZero(decision.getMontantCourantPartieRetro())) {
                montantTotalAPayerDecisionBigDec = new BigDecimal(decision.getMontantTotalAPayer());
            } else {
                if (null == decision.getMontantCourantPartieRetroDette()) {
                    decision.setMontantCourantPartieRetroDette(new BigDecimal(decision.getMontantCourantPartieRetro()));
                }
                montantTotalAPayerDecisionBigDec = decision.getMontantCourantPartieRetroDette();
                isPaiementMensuelRetro = true;
            }

            for (String keySection : dettesOsirisParSectionPourTiers.keySet()) {

                RFMyBigDecimal montantDetteOsirisPourSectionBigDec = dettesOsirisParSectionPourTiers.get(keySection);

                if ((montantDetteOsirisPourSectionBigDec.getValeur().compareTo(new BigDecimal("0")) > 0)
                        && (montantTotalAPayerDecisionBigDec.compareTo(new BigDecimal("0")) > 0)) {

                    if ((montantDetteOsirisPourSectionBigDec.getValeur().compareTo(montantTotalAPayerDecisionBigDec) <= 0)) {

                        montantTotalAPayerDecisionBigDec = montantTotalAPayerDecisionBigDec
                                .subtract(montantDetteOsirisPourSectionBigDec.getValeur());

                        // Ajout de la dette dans le tableau mémorisant les dettes à compenser par décision
                        montantDetteParSectionDecisionMap.put(keySection,
                                montantDetteOsirisPourSectionBigDec.getValeur());

                        montantDetteOsirisPourSectionBigDec.setValeur(new BigDecimal("0"));
                    } else {

                        montantDetteOsirisPourSectionBigDec.setValeur(montantDetteOsirisPourSectionBigDec.getValeur()
                                .subtract(montantTotalAPayerDecisionBigDec));

                        // Ajout de la dette dans le tableau mémorisant les dettes à compenser par décision
                        montantDetteParSectionDecisionMap.put(keySection, montantTotalAPayerDecisionBigDec);

                        montantTotalAPayerDecisionBigDec = new BigDecimal("0");
                    }
                }
            }

            if (!isPaiementMensuelRetro) {
                decision.setMontantTotalAPayer(montantTotalAPayerDecisionBigDec.toString());
            } else {
                decision.setMontantCourantPartieRetroDette(montantTotalAPayerDecisionBigDec);
            }

            montantDetteParDecisionMap.put(decision.getIdDecision(), montantDetteParSectionDecisionMap);
        }
    }

    private void rechercherTiersAvecAvance(Set<String> idTiersDecisions, Set<RFDecisionData> decisions)
            throws Exception {

        REAvanceManager reAvMgr = new REAvanceManager();
        reAvMgr.setSession(getSession());

        StringBuffer idTiersDecisionStr = new StringBuffer();
        idTiersDecisionStr.append("");
        boolean premiereIteration = true;
        for (String idTiersCourant : idTiersDecisions) {
            if (premiereIteration) {
                idTiersDecisionStr.append(idTiersCourant);
                premiereIteration = false;
            } else {
                idTiersDecisionStr.append("," + idTiersCourant);
            }
        }

        reAvMgr.setForIdTiersIn(idTiersDecisionStr.toString());
        reAvMgr.find();

        Iterator<REAvance> reAvItr = reAvMgr.iterator();

        while (reAvItr.hasNext()) {
            REAvance reAvCourante = reAvItr.next();
            if (reAvCourante != null) {
                tiersAvecAvanceMap.add(reAvCourante.getIdTiersBeneficiaire());
            }
        }
    }

    private Set<String> rechercheTypeDecision(Set<RFDecisionData> decisions,
            Map<String, RFImputationDemandesData> demandesAimputerMap) {

        Set<String> idTiersBeneficiaireDecisionSet = new HashSet<String>();
        for (RFDecisionData decision : decisions) {
            for (String idDemande : decision.getIdDemandes()) {
                RFImputationDemandesData demandeCourante = demandesAimputerMap.get(idDemande);

                if (IRFTypePaiement.PAIEMENT_RETROACTIF.equals(demandeCourante.getTypeDePaiment())
                        || IRFTypePaiement.PAIEMENT_COURANT.equals(demandeCourante.getTypeDePaiment())) {
                    hasDecisionPonctuelle = true;
                }

                if (IRFTypePaiement.PAIEMENT_FUTURE.equals(demandeCourante.getTypeDePaiment())
                        || IRFTypePaiement.PAIEMENT_COURANT.equals(demandeCourante.getTypeDePaiment())) {
                    hasDecisionMensuelle = true;
                }

                if (hasDecisionPonctuelle && hasDecisionMensuelle) {
                    break;
                }
            }
            idTiersBeneficiaireDecisionSet.add(decision.getIdRequerant());
        }
        return idTiersBeneficiaireDecisionSet;
    }

    private BigDecimal retrieveDetteDecision(String idDecision) {
        // recherche des dettes de la la décision
        BigDecimal montantDetteBigDec = new BigDecimal("0");
        if (montantDetteParDecisionMap.containsKey(idDecision)) {
            Map<String, BigDecimal> dettesParSectionsMap = montantDetteParDecisionMap.get(idDecision);
            for (String sectionKey : dettesParSectionsMap.keySet()) {
                montantDetteBigDec = montantDetteBigDec.add(dettesParSectionsMap.get(sectionKey));
            }
        }
        return montantDetteBigDec;
    }

    public void setHasDecisionAvasad(Boolean hasDecisionAvasad) {
        this.hasDecisionAvasad = hasDecisionAvasad;
    }

    public void setHasDecisionMensuelle(Boolean hasDecisionMensuelle) {
        this.hasDecisionMensuelle = hasDecisionMensuelle;
    }

    public void setHasDecisionPonctuelle(Boolean hasDecisionPonctuelle) {
        this.hasDecisionPonctuelle = hasDecisionPonctuelle;
    }

    public void setIdDepassementQD(String idDepassementQD) {
        this.idDepassementQD = idDepassementQD;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdLotDecision(String idLotDecision) {
        this.idLotDecision = idLotDecision;
    }

    public void setIdLotMensuel(String idLotMensuel) {
        this.idLotMensuel = idLotMensuel;
    }

    public void setMemoryLog(FWMemoryLog memoryLog) {
        this.memoryLog = memoryLog;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTransaction(BITransaction transaction) {
        this.transaction = transaction;
    }

    /**
     * 
     * @param decision
     * @throws Exception
     */
    private void traiterDecision(RFDecisionData decision, Map<String, RFImputationDemandesData> demandesAimputerMap,
            Map<String, RFImputationQdsData> qdsAimputerMap, BigDecimal montantTotal,
            HashMap<String, Set<String[]>> idQdIdsDossierMap) throws Exception {

        if (JadeStringUtil.isBlankOrZero(decision.getIdRequerant())) {
            throw new Exception(
                    "RFGenererPaiementService.traiterDecision(): Impossible de retrouver le bénéficiaire de la décision N° "
                            + decision.getIdDecision());
        }

        if (!JadeStringUtil.isBlankOrZero(decision.getTypeDePaiment())) {
            if (IRFTypePaiement.PAIEMENT_RETROACTIF.equals(decision.getTypeDePaiment())
                    && !decision.isPaiementMensuel()) {

                traiterPaiementPonctuel(decision, demandesAimputerMap, qdsAimputerMap, montantTotal,
                        decision.getIdRequerant());

            } else {

                traiterPaiementMensuel(decision, demandesAimputerMap, qdsAimputerMap, montantTotal,
                        decision.getIdRequerant());
            }
        }
    }

    private void traiterDette(RFDecisionData decision, String idSection, String montantDette) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(idSection) && !JadeStringUtil.isBlankOrZero(montantDette)
                && !JadeStringUtil.isBlankOrZero(decision.getIdPrestation())) {

            RFOrdresVersements ordreVersement = new RFOrdresVersements();
            ordreVersement.setSession(session);
            ordreVersement.setIdPrestation(decision.getIdPrestation());
            ordreVersement.setTypeVersement(IRFOrdresVersements.CS_TYPE_DETTE);
            ordreVersement.setMontant(montantDette);
            ordreVersement.setIdTiersAdressePaiement(decision.getIdAdressePaiement());
            ordreVersement.setIdTiers(decision.getIdRequerant());
            ordreVersement.setIsImportation(false);
            ordreVersement.setIdSectionDette(idSection);
            ordreVersement.setIsCompense(true);
            ordreVersement.setIdTypeSoin("");
            ordreVersement.setIsForcerPayement(false);
            ordreVersement.setMontantDepassementQD("");
            ordreVersement.setIdDecisionParent("");

            ordreVersement.add(transaction);
        }

    }

    private void traiterPaiementMensuel(RFDecisionData decision,
            Map<String, RFImputationDemandesData> demandesAimputerMap, Map<String, RFImputationQdsData> qdsAimputerMap,
            BigDecimal montantTotalPlusARembourserDsas, String idRequerant) throws Exception {

        if (IRFTypePaiement.PAIEMENT_RETROACTIF.equals(decision.getTypeDePaiment())) {
            traiterPaiementMensuelRetroactif(decision, demandesAimputerMap, qdsAimputerMap,
                    montantTotalPlusARembourserDsas, idRequerant);
        } else if (IRFTypePaiement.PAIEMENT_COURANT.equals(decision.getTypeDePaiment())) {
            traiterPaiementMensuelCourant(decision, demandesAimputerMap, qdsAimputerMap,
                    montantTotalPlusARembourserDsas, idRequerant);
        } else if (IRFTypePaiement.PAIEMENT_FUTURE.equals(decision.getTypeDePaiment())) {
            traiterPaiementMensuelFutur(decision, demandesAimputerMap, qdsAimputerMap);

        }
    }

    private void traiterPaiementMensuelCourant(RFDecisionData decision,
            Map<String, RFImputationDemandesData> demandesAimputerMap, Map<String, RFImputationQdsData> qdsAimputerMap,
            BigDecimal montantTotalPlusARembourserDsas, String idRequerant) throws Exception {

        // création de la prestation pour la partie rétro
        RFPrestation prestation = creationPrestationPaiementCourant(decision, qdsAimputerMap, idRequerant,
                demandesAimputerMap);

        // Création d'une OV avec le mnt de la prestation rétro
        creationOrdresVersementPaiementMensuelCourant(decision, prestation, demandesAimputerMap,
                idDepassementQD.toString(), qdsAimputerMap, idRequerant);

        RFImputationDemandesData demandeCourante = getDemandeMap(demandesAimputerMap,
                getIdDemandeRegimeDecision(decision));

        RFImputationQdsData qdPrincipaleData = getQdPrincipalMap(qdsAimputerMap, demandeCourante.getIdQdPrincipale());

        // Création de la partie futur
        String dateAugmentation = demandeCourante.getDateDernierPaiement_mm_yyyy();
        String dateDiminution = "";
        if (!JadeStringUtil.isBlankOrZero(demandeCourante.getDateFinTraitement())) {
            dateDiminution = PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(demandeCourante.getDateFinTraitement());
        }

        // Ajout de la prestation accordées
        creationPrestationAccordee(decision, demandeCourante, qdPrincipaleData, dateAugmentation, dateDiminution);

    }

    private void traiterPaiementMensuelFutur(RFDecisionData decision,
            Map<String, RFImputationDemandesData> demandesAimputerMap, Map<String, RFImputationQdsData> qdsAimputerMap)
            throws Exception {

        RFImputationDemandesData demandeCourante = getDemandeMap(demandesAimputerMap,
                getIdDemandeRegimeDecision(decision));

        RFImputationQdsData qdPrincipaleData = getQdPrincipalMap(qdsAimputerMap, demandeCourante.getIdQdPrincipale());

        // Création de la partie futur
        String dateAugmentation = PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(demandeCourante
                .getDateDebutTraitement());
        String dateDiminution = "";
        if (!JadeStringUtil.isBlankOrZero(demandeCourante.getDateFinTraitement())) {
            dateDiminution = PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(demandeCourante.getDateFinTraitement());
        }

        creationPrestationAccordee(decision, demandeCourante, qdPrincipaleData, dateAugmentation, dateDiminution);

    }

    /**
     * 
     * Traite le paiement mensuel rétroactif de la même façon qu'une décision ponctuelle
     * 
     * @param decision
     * @param demandesAimputerMap
     * @param qdsAimputerMap
     * @param montantTotalPlusARembourserDsas
     * @throws Exception
     */
    private void traiterPaiementMensuelRetroactif(RFDecisionData decision,
            Map<String, RFImputationDemandesData> demandesAimputerMap, Map<String, RFImputationQdsData> qdsAimputerMap,
            BigDecimal montantTotalPlusARembourserDsas, String idRequerant) throws Exception {

        RFPrestation prestation = creationPrestationPaiementPonctuel(decision, qdsAimputerMap,
                montantTotalPlusARembourserDsas, demandesAimputerMap, idRequerant);

        creationOrdresVersementPaiementPonctuel(decision, prestation, demandesAimputerMap, idDepassementQD.toString(),
                idRequerant);

        RFImputationDemandesData demandeCourante = getDemandeMap(demandesAimputerMap,
                getIdDemandeRegimeDecision(decision));

        RFImputationQdsData qdPrincipaleData = getQdPrincipalMap(qdsAimputerMap, demandeCourante.getIdQdPrincipale());

        String dateAugmentation = demandeCourante.getDateDernierPaiement_mm_yyyy();

        // Ajout de la prestation accordées
        creationPrestationAccordee(decision, demandeCourante, qdPrincipaleData, dateAugmentation, "");
    }

    /**
     * 
     * Créer la prestation et les OVs pour une décision ponctuelle
     * 
     * @param decision
     * @param demandesAimputerMap
     * @param qdsAimputerMap
     * @param montantTotal
     * @throws Exception
     */
    private void traiterPaiementPonctuel(RFDecisionData decision,
            Map<String, RFImputationDemandesData> demandesAimputerMap, Map<String, RFImputationQdsData> qdsAimputerMap,
            BigDecimal montantTotal, String idRequerant) throws Exception {

        RFPrestation prestation = creationPrestationPaiementPonctuel(decision, qdsAimputerMap, montantTotal,
                demandesAimputerMap, idRequerant);

        creationOrdresVersementPaiementPonctuel(decision, prestation, demandesAimputerMap, idDepassementQD.toString(),
                idRequerant);
    }

    /**
     * 
     * Créer une prestation si nécessaire, ajoute les OVs de restitutions à la préstation puis cloture les prestations
     * accordées si nécessaire
     * 
     * @param decision
     * @param demandesAimputerMap
     * @param qdsAimputerMap
     * @throws Exception
     */
    private void traiterRestitutions(RFDecisionData decision,
            Map<String, RFImputationDemandesData> demandesAimputerMap, Map<String, RFImputationQdsData> qdsAimputerMap,
            BigDecimal montantTotalBigDec, HashMap<String, Set<String[]>> idQdIdsDossierMap,
            Set<RFDecisionData> decisions) throws Exception {

        String idPrestationRestitutionCourante = decision.getIdPrestation();

        // Création des Ovs de type restitution
        for (String idDemande : decision.getIdDemandes()) {

            RFImputationDemandesData demandeCourante = demandesAimputerMap.get(idDemande);

            if (null != demandeCourante) {

                if (!JadeStringUtil.isBlankOrZero(demandeCourante.getRestitutionMontantAPayeParent())
                        || !JadeStringUtil.isBlankOrZero(demandeCourante.getRestitutionMontantDepassementQdParent())) {

                    if (!JadeStringUtil.isBlank(demandeCourante.getRestitutionIdDecisionRFMAccordee())) {
                        modificationPrestationAccordee(demandeCourante.getRestitutionIdDecisionRFMAccordee(),
                                demandeCourante.getDateDernierPaiement_mm_yyyy());
                    }

                    // Si le type PC ou le type de remboursement (voir Qd) de la demande parent à restituer différe de
                    // la prestation existante, on créé la restitution dans une autre prestation
                    String[] csTypePcTypeRemboursementCorrectionParent = getCsTypePcTypeRemboursementCourantCorrection(
                            demandeCourante, decision, qdsAimputerMap, decision.getIdRequerant());

                    if (!prestationsCreeesMap
                            .containsKey(genererClefTypesPrestationsMap(csTypePcTypeRemboursementCorrectionParent[1],
                                    csTypePcTypeRemboursementCorrectionParent[5]))) {

                        idPrestationRestitutionCourante = creationPrestationRestitution(decision, demandeCourante,
                                qdsAimputerMap, demandesAimputerMap, decision.getIdRequerant());

                    } else {
                        idPrestationRestitutionCourante = prestationsCreeesMap.get(genererClefTypesPrestationsMap(
                                csTypePcTypeRemboursementCorrectionParent[1],
                                csTypePcTypeRemboursementCorrectionParent[5]));
                    }

                    boolean isCompense = true;
                    // Si le tiers de la restitution est différent du requérant de la décision, on ne compense pas par
                    // défaut
                    if (!csTypePcTypeRemboursementCorrectionParent[2]
                            .equals(csTypePcTypeRemboursementCorrectionParent[3])) {
                        isCompense = false;
                    }

                    // Recherche de l'adresse de paiement de la restitution (adresse paiement de la demande parent)
                    RFDemande demandeParent = new RFDemande();
                    demandeParent.setSession(getSession());
                    demandeParent.setIdDemande(demandeCourante.getIdDemandeParent());
                    demandeParent.retrieve();
                    if (!demandeParent.isNew()) {
                        String idOrdreVersementRestitution = creationSimpleOrdreVersement(
                                idPrestationRestitutionCourante,
                                csTypePcTypeRemboursementCorrectionParent[3],
                                demandeParent.getIdAdressePaiement(),
                                JadeStringUtil.isBlankOrZero(demandeCourante.getRestitutionMontantAPayeParent()) ? ""
                                        : "-" + demandeCourante.getRestitutionMontantAPayeParent(),
                                JadeStringUtil.isBlankOrZero(demandeCourante.getRestitutionMontantDepassementQdParent()) ? ""
                                        : "-" + demandeCourante.getRestitutionMontantDepassementQdParent(),
                                IRFOrdresVersements.CS_TYPE_RESTITUTION,
                                decision.getCodeTypeDeSoin(),
                                demandeCourante.getRestitutionIsForcerPaiementParent(),
                                isSourceImportation(demandeCourante.getCodeTypeDeSoin(),
                                        demandeCourante.getRestitutionCsSource()), isCompense,
                                demandeParent.getIdDecision(), demandeCourante.getCodeSousTypeDeSoin());

                        // On renseigne la table association Ov restit - décision
                        ajoutAssociationOvRestitDecision(decision.getIdRequerant(), idOrdreVersementRestitution,
                                decisions);

                    } else {
                        throw new Exception(
                                "RFGenererPaiementService.traiterRestitutions: impossible de trouver la demande parent");
                    }
                }

            } else {
                throw new Exception("RFGenererPaiementService.traiterRestitutions: impossible de trouver la demande");
            }
        }
    }

}
