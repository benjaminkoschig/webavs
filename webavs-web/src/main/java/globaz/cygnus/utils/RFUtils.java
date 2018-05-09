/*
 * Créé le 30 novembre 2009
 */
package globaz.cygnus.utils;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.utils.REPmtMensuel;
import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.dossiers.IRFDossiers;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.demandes.RFAssDemandeDev19Ftd15;
import globaz.cygnus.db.demandes.RFAssDemandeDev19Ftd15Manager;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossierManager;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefusManager;
import globaz.cygnus.db.paiement.RFLot;
import globaz.cygnus.db.paiement.RFLotManager;
import globaz.cygnus.db.paiement.RFPrestation;
import globaz.cygnus.db.qds.RFAssQdDossier;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFAssQdDossierManager;
import globaz.cygnus.db.qds.RFQdAssureJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFQdAugmentation;
import globaz.cygnus.db.qds.RFQdAugmentationManager;
import globaz.cygnus.db.qds.RFQdSoldeCharge;
import globaz.cygnus.db.qds.RFQdSoldeChargeManager;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenu;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenuManager;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoinJointSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoinJointSousTypeDeSoinManager;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoinManager;
import globaz.cygnus.exceptions.RFRetrieveIsEnfantException;
import globaz.cygnus.exceptions.RFRetrieveTypeHomeException;
import globaz.cygnus.services.RFMembreFamilleService;
import globaz.cygnus.services.adaptationJournaliere.RFAdaptationJournaliereContext;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCHomes;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.vo.decision.DecisionPcVO;

/**
 * @author JJE
 */
public class RFUtils {

    public static final String add = "add";

    public static final String CODE_SOUS_TYPE_DE_SOIN_REGIME_DIABETIQUE_2_2_STR = "02";
    // codes pour les attestations
    public static final int CODE_TYPE_DE_SOIN_DEVIS_DENTAIRE = 19;
    public static final int CODE_TYPE_DE_SOIN_FINANCEMENT_DES_SOINS = 20;
    public static final String CODE_TYPE_DE_SOIN_FINANCEMENT_DES_SOINS_STR = "20";
    public static final int CODE_TYPE_DE_SOIN_FRAIS_DENTAIRE = 15;
    public static final int CODE_TYPE_DE_SOIN_FRAIS_REFUSES = 18;
    public static final int CODE_TYPE_DE_SOIN_FRQP = 17;
    public static final String CODE_TYPE_DE_SOIN_FRQP_STR = "17";
    public static final int CODE_TYPE_DE_SOIN_MAINTIEN_A_DOMICILE = 13;
    public static final String CODE_TYPE_DE_SOIN_MAINTIEN_A_DOMICILE_STR = "13";
    public static final int CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_5 = 5;
    public static final String CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_5_STR = "05";
    public static final int CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_6 = 6;
    public static final int CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_7 = 7;
    public static final int CODE_TYPE_DE_SOIN_REGIME_2 = 2;
    public static final String CODE_TYPE_DE_SOIN_REGIME_2_STR = "02";
    public static final int CODE_TYPE_DE_SOIN_TRANSPORT_16 = 16;
    public static final String del = "del";
    public static final String MAX_DATE_VALUE = "99999999";
    public static final String MAX_JADATE_VALUE = "31.12.9999";

    public static final String MONTANT_INITIAL_FRQP_PP = "1000.00";
    public static final String MONTANT_PRESTATION_ACCORDEE_FRQP_1_ER_PAIEMENT = "600.00";
    public static final String MONTANT_PRESTATION_ACCORDEE_FRQP_2_EME_PAIEMENT = "200.00";

    public static final String montantInitialChargeRfmZero = "-1.00";

    public final static String TYPE_PC_AI = "type_pc_ai";
    public final static String TYPE_PC_AVS = "type_pc_avs";

    public static final List<String> typeBeneficiairePlusieursPersonnesComprisesDansCalcul = new ArrayList<String>(
            Arrays.asList(IRFTypesBeneficiairePc.COUPLE_A_DOMICILE, IRFTypesBeneficiairePc.COUPLE_AVEC_ENFANTS,
                    IRFTypesBeneficiairePc.ADULTE_AVEC_ENFANTS, IRFTypesBeneficiairePc.ENFANTS_AVEC_ENFANTS));

    public static final String upd = "upd";

    public static RELot addRELot(String typeDecision, BSession session, BTransaction transaction) throws Exception {

        RELot lotRente = new RELot();
        lotRente.setSession(session);

        lotRente.setCsEtatLot(IRELot.CS_ETAT_LOT_OUVERT);
        lotRente.setCsLotOwner(IRELot.CS_LOT_OWNER_RFM);
        lotRente.setCsTypeLot(typeDecision);
        lotRente.setDescription(session.getLabel("PROCESS_PREPARER_DECISIONS_DESCRIPTION_LOT"));
        lotRente.setDateCreationLot(JadeDateUtil.getDMYDate(new Date()));

        lotRente.add(transaction);

        return lotRente;
    }

    /**
     * Convertis le code système PC (typeDeHome) vers les codes système RFM
     * 
     * @param codeSystemPCTypeHome
     * @throws RFRetrieveTypeHomeException Si l'argument <code>codeSystemPCTypeHome</code> est null ou ne correspond pas
     *             à un code système de type home PC
     */
    public static String convertCSTypeHomePCversCSTypeHomeRFM(String codeSystemPCTypeHome)
            throws RFRetrieveTypeHomeException {
        String value = "";
        try {
            if (EPCProperties.LVPC.getBooleanValue()) {
                if (IPCHomes.CS_SERVICE_ETAT_SASH.equals(codeSystemPCTypeHome)
                        || IPCHomes.CS_SERVICE_ETAT_EPS.equals(codeSystemPCTypeHome)) {
                    value = IRFQd.CS_TYPE_REMBOURSEMENT_SASH;
                } else if (IPCHomes.CS_SERVICE_ETAT_SPAS.equals(codeSystemPCTypeHome)) {
                    value = IRFQd.CS_TYPE_REMBOURSEMENT_SPAS;
                } else {
                    throw new RFRetrieveTypeHomeException("Invalid code system for type de home ["
                            + codeSystemPCTypeHome + "]. Possible values are [" + IRFQd.CS_TYPE_REMBOURSEMENT_SASH
                            + ", " + IRFQd.CS_TYPE_REMBOURSEMENT_SPAS + "]");
                }
            }
        } catch (PropertiesException e) {
            throw new RFRetrieveTypeHomeException("Missing property declaration : " + e.toString(), e);
        }
        return value;
    }

    public static void addRFLot(RELot lotRente, String idGestionnaire, BSession session, BTransaction transaction)
            throws Exception {

        RFLot lotRFM = new RFLot();
        lotRFM.setSession(session);

        lotRFM.setIdGestionnaire(idGestionnaire);
        lotRFM.setIdLotRFM(lotRente.getIdLot());

        lotRFM.add(transaction);
    }

    public static void ajouterAssociationDossierQdMembreFamille(Vector<String[]> membresFamille, String idGestionnaire,
            BSession session, BITransaction transaction, String idQd, String csTypeBeneficiaire) throws Exception {

        for (String[] membreCourant : membresFamille) {

            // Si le type de relation n'est pas un enfant et que le membre n'est pas pris dans le calcul, on ne l'ajoute
            // pas
            if (membreCourant[8].equals(Boolean.TRUE.toString())
                    || membreCourant[1].equals(IPCDroits.CS_ROLE_FAMILLE_ENFANT)) {

                RFAssQdDossier rfAssQdDossier = new RFAssQdDossier();
                rfAssQdDossier.setSession(session);
                rfAssQdDossier.setIdQd(idQd);

                RFPrDemandeJointDossier rfPrDemJoiDos = RFUtils.getDossierJointPrDemande(membreCourant[0], session);

                if (null == rfPrDemJoiDos) {
                    rfAssQdDossier.setIdDossier(RFUtils.ajouterDossier(membreCourant[0], idGestionnaire, session,
                            transaction));
                } else {
                    rfAssQdDossier.setIdDossier(rfPrDemJoiDos.getIdDossier());
                }

                rfAssQdDossier.setTypeRelation(membreCourant[1]);
                rfAssQdDossier.setIsComprisDansCalcul(membreCourant[8].equals(Boolean.TRUE.toString()));

                rfAssQdDossier.add(transaction);
            }

        }
    }

    /**
     * 
     * Création d'un dossier RFM selon un idTiers et un gestionnaire
     * 
     * @param idTiers
     * @param idGestionnaire
     * @param session
     * @param transaction
     * @return idDossier
     * @throws Exception
     */
    public static String ajouterDossier(String idTiers, String idGestionnaire, BSession session,
            BITransaction transaction) throws Exception {

        // creation du dossier prdemap
        PRDemande demandePrestation = new PRDemande();
        demandePrestation.setSession(session);
        demandePrestation.setIdTiers(idTiers);
        demandePrestation.setTypeDemande(IPRDemande.CS_TYPE_RFM);
        demandePrestation.setEtat(IPRDemande.CS_ETAT_OUVERT);

        demandePrestation.add(transaction);

        // creation du dossier RFM
        RFDossier dossierRFM = new RFDossier();
        dossierRFM.setSession(session);
        dossierRFM.setIdGestionnaire(idGestionnaire);
        dossierRFM.setDateDebut(JACalendar.todayJJsMMsAAAA());
        // dossierRFM.setDateFin();
        dossierRFM.setCsEtatDossier(IRFDossiers.OUVERT);
        dossierRFM.setIdPrDem(demandePrestation.getIdDemande());
        dossierRFM.setCsSource(IRFDossiers.SYSTEME);

        dossierRFM.add(transaction);

        return dossierRFM.getIdDossier();

    }

    /**
     * 
     * Ajoute une ligne dans le log des adaptations new String[] { typeDeMessage, idAdaptationJournaliere,
     * idTiersBeneficiaire, nss, msgErreur, idDecisionPc }
     * 
     * 
     * @param typeDeMessage
     * @param idAdaptationJournaliere
     * @param idTiersBeneficiaire
     * @param nss
     * @param idDecisionPc
     * @param msgErreur
     * @param logsList
     */
    public static void ajouterLogAdaptation(String typeDeMessage, String idAdaptationJournaliere,
            String idTiersBeneficiaire, String nss, String idDecisionPc, String numDecisionPc, String msgErreur,
            List<String[]> logsList) {

        logsList.add(new String[] { typeDeMessage, idAdaptationJournaliere, idTiersBeneficiaire, nss, msgErreur,
                idDecisionPc, numDecisionPc });

    }

    /**
     * 
     * Ajoute une ligne dans le log des importations AVASAD
     * 
     * @param typeDeMessage
     * @param idLigne
     * @param idTiersBeneficiaire
     * @param nss
     * @param msgErreur
     * @param logsList
     */
    public static void ajouterLogImportationsAvasad(int typeDeMessage, String numeroLigne, String nss,
            String msgErreur, boolean isErreurImportation, List<String[]> logsList) {

        logsList.add(new String[] { Integer.valueOf(typeDeMessage).toString(), numeroLigne, nss, msgErreur });

    }

    public static boolean containsIdArrayList(String idCopie, ArrayList<String> idSuppressionCopieArray) {
        for (int i = 0; i < idSuppressionCopieArray.size(); i++) {
            if (idCopie.equals(idSuppressionCopieArray.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Méthode qui calcul l'augmentation d'une Qd
     * 
     * @param String
     *            , BSession
     */
    public static String getAugmentationQd(String idQdStr, BSession session) throws Exception {

        RFQdAugmentationManager rfQdAugmentationMgr = new RFQdAugmentationManager();

        rfQdAugmentationMgr.setForIdQd(idQdStr);
        rfQdAugmentationMgr.setSession(session);
        rfQdAugmentationMgr.setForDerniereVersion(Boolean.TRUE.toString());
        rfQdAugmentationMgr.changeManagerSize(0);
        rfQdAugmentationMgr.find();

        Iterator<RFQdAugmentation> rfQdAugmentationIter = rfQdAugmentationMgr.iterator();
        BigDecimal totalMontantAug = new BigDecimal(0.00);

        while (rfQdAugmentationIter.hasNext()) {
            RFQdAugmentation rfQdAugmentation = rfQdAugmentationIter.next();
            if (null != rfQdAugmentation) {
                totalMontantAug = totalMontantAug.add(new BigDecimal(JANumberFormatter.deQuote(rfQdAugmentation
                        .getMontantAugmentationQd().trim())));
            }
        }

        return totalMontantAug.setScale(2).toString();
    }

    /**
     * Méthode qui recherche les codes type et sous-type de soin
     * 
     * @param FWViewBeanInterface
     *            , BITransaction
     * @throws Exception
     * @return String[codeType, codeSousType]
     */
    public static String[] getCodesTypeDeSoin(String csSousTypeDeSoin, BSession session) throws Exception {

        RFTypeDeSoinJointSousTypeDeSoinManager rfTypeDeSoinJointSousTypeDeSoinMgr = new RFTypeDeSoinJointSousTypeDeSoinManager();
        rfTypeDeSoinJointSousTypeDeSoinMgr.setForCsSousTypeDeSoin(csSousTypeDeSoin);
        rfTypeDeSoinJointSousTypeDeSoinMgr.setSession(session);
        rfTypeDeSoinJointSousTypeDeSoinMgr.changeManagerSize(0);
        rfTypeDeSoinJointSousTypeDeSoinMgr.find();

        if (rfTypeDeSoinJointSousTypeDeSoinMgr.size() == 1) {
            RFTypeDeSoinJointSousTypeDeSoin rfTypeDeSoinJointSousTypeDeSoin = (RFTypeDeSoinJointSousTypeDeSoin) rfTypeDeSoinJointSousTypeDeSoinMgr
                    .getFirstEntity();
            if (null != rfTypeDeSoinJointSousTypeDeSoin) {
                return new String[] { rfTypeDeSoinJointSousTypeDeSoin.getCode(),
                        rfTypeDeSoinJointSousTypeDeSoin.getCodeSousTypeSoin() };
            } else {
                throw new Exception("RFUtils.getCodesTypeDeSoin()1: impossible de retrouver les codes type de soin");
            }
        } else {
            throw new Exception("RFUtils.getCodesTypeDeSoin()2: impossible de retrouver les codes type de soin");
        }
    }

    public static String getCsTypeRelationPc(String csRelation) {

        if (csRelation.equals(ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT)
                || csRelation.equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {

            return IPCDroits.CS_ROLE_FAMILLE_CONJOINT;

        } else if (csRelation.equals(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT)
                || csRelation.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {

            return IPCDroits.CS_ROLE_FAMILLE_REQUERANT;

        } else if (csRelation.equals(ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT)
                || csRelation.equals(IPCDroits.CS_ROLE_FAMILLE_ENFANT)) {

            return IPCDroits.CS_ROLE_FAMILLE_ENFANT;

        } else {
            return "";
        }

    }

    /**
     * 
     * @param dateDernierPaiementMensuelRente
     *            , session
     * @return La date du dernier pmt mensuel effectué, au format mm.aaaa
     */
    public static String getDateDernierPaiementMensuelRente(String dateDernierPaiementMensuelRente, BSession session) {
        if (JadeStringUtil.isBlankOrZero(dateDernierPaiementMensuelRente)) {
            return REPmtMensuel.getDateDernierPmt(session);
        } else {
            return dateDernierPaiementMensuelRente;
        }
    }

    public static RFPrDemandeJointDossier getDossierJointPrDemande(String idTiers, BSession session) throws Exception {
        return RFUtils.getDossierJointPrDemande(idTiers, "", session);
    }

    /**
     * Retourne le dossier en fonction d'un idTiers, retourne null si non trouvé
     * 
     * @param String
     *            , BSession
     * @throws Exception
     */
    private static RFPrDemandeJointDossier getDossierJointPrDemande(String idTiers, String idDossier, BSession session)
            throws Exception {

        // Recherche du dossier en fonction de l'idTiers
        RFPrDemandeJointDossierManager rfPrDemandeJointDossierMgr = new RFPrDemandeJointDossierManager();
        rfPrDemandeJointDossierMgr.setSession(session);
        rfPrDemandeJointDossierMgr.setForIdTiers(idTiers);
        rfPrDemandeJointDossierMgr.setForIdDossier(idDossier);
        rfPrDemandeJointDossierMgr.setForTypeDemande(IPRDemande.CS_TYPE_RFM);
        rfPrDemandeJointDossierMgr.setForEtatDemande(IPRDemande.CS_ETAT_OUVERT);
        rfPrDemandeJointDossierMgr.setForEtatDossier(IRFDossiers.OUVERT);
        rfPrDemandeJointDossierMgr.changeManagerSize(0);
        rfPrDemandeJointDossierMgr.find();

        if (rfPrDemandeJointDossierMgr.size() == 1) {
            RFPrDemandeJointDossier rfPrDemandeJointDossier = (RFPrDemandeJointDossier) rfPrDemandeJointDossierMgr
                    .getFirstEntity();
            return rfPrDemandeJointDossier;
        } else {
            return null;
        }
    }

    public static Map<String, String[]> getIdsMotifDeRefusSysteme(BSession session, BTransaction transaction)
            throws Exception {

        Map<String, String[]> resultats = new HashMap<String, String[]>();

        RFMotifsDeRefusManager rfMotRefMgr = new RFMotifsDeRefusManager();
        rfMotRefMgr.setSession(session);

        Set<String> setIds = new HashSet<String>();
        setIds.add(IRFMotifsRefus.ID_DELAI_15_MOIS_DEPASSE);
        setIds.add(IRFMotifsRefus.ID_DELAI_DECES_DEPASSE);
        setIds.add(IRFMotifsRefus.ID_FRAIS_DE_LIVRAISON_FOURNISSEUR_NON_CONVENTIONNE);
        setIds.add(IRFMotifsRefus.ID_ENFANT_EXCLUS_PC);
        setIds.add(IRFMotifsRefus.ID_FRQP_DEJA_REMBOURSEES);
        setIds.add(IRFMotifsRefus.ID_FRQP_MAXIMUM_N_FRANC_PAR_ASSURE);
        setIds.add(IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE);
        setIds.add(IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE_CONVENTION);
        setIds.add(IRFMotifsRefus.ID_MENAGE_TIERS_MAXIMUM_N_FRANC);
        setIds.add(IRFMotifsRefus.ID_MOYEN_AUXILIAIRE_MAXIMUM_1_3_CONTRIBUTION_AVS_AI);
        setIds.add(IRFMotifsRefus.ID_MOYEN_AUXILIAIRE_NON_CAR_HOME);
        setIds.add(IRFMotifsRefus.ID_PAS_DE_DOCUMENTS_POUR_CALCULER_LA_PC);
        setIds.add(IRFMotifsRefus.ID_PAS_DROIT_A_LA_PC);
        setIds.add(IRFMotifsRefus.ID_PRIX_DE_PENSION_SUPERIEUR_AU_MAXIMUM_CANTONAL);
        setIds.add(IRFMotifsRefus.ID_SOLDE_EXECEDENT_DE_REVENU);
        setIds.add(IRFMotifsRefus.ID_ATTESTATION_NON_TROUVEE);
        setIds.add(IRFMotifsRefus.ID_MENAGE_NON_CAR_HOME);
        setIds.add(IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE_PETITE_QD);
        setIds.add(IRFMotifsRefus.ID_FINANCEMENT_NON_CAR_DOMICILE);

        rfMotRefMgr.setForIdsMotifRefusSysteme(setIds);
        rfMotRefMgr.changeManagerSize(0);
        rfMotRefMgr.find(transaction);

        Iterator<RFMotifsDeRefus> rfMotRefItr = rfMotRefMgr.iterator();

        while (rfMotRefItr.hasNext()) {

            RFMotifsDeRefus rfMotRef = rfMotRefItr.next();

            if (null != rfMotRef) {
                resultats.put(rfMotRef.getIdMotifRefusSysteme(), new String[] { rfMotRef.getIdMotifRefus(),
                        rfMotRef.getHasMontant().toString(), rfMotRef.getDescriptionFR(), rfMotRef.getDescriptionDE(),
                        rfMotRef.getDescriptionIT() });
            } else {
                throw new Exception(
                        "RFImputationDemandesService.getIdsMotifDeRefusSysteme(): Impossible de retrouver l'id du motif de refus système");
            }
        }

        return resultats;
    }

    /**
     * Méthode qui recherche l'id cs sous-type de soin en fonction des codes type et sous-type de soin du viewBean
     * 
     * @param FWViewBeanInterface
     *            , BITransaction
     * @throws Exception
     */
    public static String getIdSousTypeDeSoin(String codeTypeDeSoin, String codeSousTypeDeSoin, BSession session)
            throws Exception {

        RFTypeDeSoinJointSousTypeDeSoinManager rfTypeDeSoinJointSousTypeDeSoinMgr = new RFTypeDeSoinJointSousTypeDeSoinManager();
        rfTypeDeSoinJointSousTypeDeSoinMgr.setForCodeTypeDeSoin(codeTypeDeSoin.length() > 1 ? codeTypeDeSoin : "0"
                + codeTypeDeSoin);
        rfTypeDeSoinJointSousTypeDeSoinMgr
                .setForCodeSousTypeDeSoin(codeSousTypeDeSoin.length() > 1 ? codeSousTypeDeSoin : "0"
                        + codeSousTypeDeSoin);
        rfTypeDeSoinJointSousTypeDeSoinMgr.setSession(session);
        rfTypeDeSoinJointSousTypeDeSoinMgr.changeManagerSize(0);
        rfTypeDeSoinJointSousTypeDeSoinMgr.find();

        if (rfTypeDeSoinJointSousTypeDeSoinMgr.size() == 1) {
            RFTypeDeSoinJointSousTypeDeSoin rfTypeDeSoinJointSousTypeDeSoin = (RFTypeDeSoinJointSousTypeDeSoin) rfTypeDeSoinJointSousTypeDeSoinMgr
                    .getFirstEntity();
            if (null != rfTypeDeSoinJointSousTypeDeSoin) {
                return rfTypeDeSoinJointSousTypeDeSoin.getIdSousTypeSoin();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getIdTiersFromIdDossier(String idDossier, BSession session) throws Exception {
        return RFUtils.getDossierJointPrDemande("", idDossier, session).getIdTiers();
    }

    /**
     * Méthode qui retourne le code système d'un code type de soin
     * 
     * @param String
     */
    public static String getIdTypeDeSoin(String codeTypeDeSoin, BSession session) throws Exception {
        RFTypeDeSoinManager rfTypeDeSoinMgr = new RFTypeDeSoinManager();
        rfTypeDeSoinMgr.setForCodeTypeDeSoin(codeTypeDeSoin.length() > 1 ? codeTypeDeSoin : "0" + codeTypeDeSoin);
        rfTypeDeSoinMgr.setSession(session);
        rfTypeDeSoinMgr.changeManagerSize(0);
        rfTypeDeSoinMgr.find();

        // on prend le premier
        if (rfTypeDeSoinMgr.size() > 0) {
            RFTypeDeSoin rfTypeDeSoin = (RFTypeDeSoin) rfTypeDeSoinMgr.getFirstEntity();
            if (null != rfTypeDeSoin) {
                return rfTypeDeSoin.getIdTypeSoin();
            } else {
                return "";
            }
        } else {
            return "";
        }

    }

    public static String getLibelleCourtSexe(String csSexe) {
        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return "H";// getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return "F";// getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    public static String getLibellePays(String csNationalite, BSession session) {

        String code = session.getSystemCode("CIPAYORI", csNationalite);

        if ("999".equals(session.getCode(code))) {
            return "";
        } else {
            return session.getCodeLibelle(code);
        }

    }

    /**
     * Méthode renvoyant les membres de la famille d'un idTiers sous forme d'un tableau de String[IdTiers,
     * RelationAuRequerant, Nss, Nom, Prénom, DateNaissance, CsSexe, CsNationalite]
     * 
     * @return Vector
     * @throws Exception
     */
    public static Vector<String[]> getMembreFamille(BTransaction transaction, String idTiers, String date,
            boolean isFratrie, BSession session) throws Exception {

        Vector<String[]> membresFamilleVec = new Vector<String[]>();

        RFMembreFamilleService rfMembreFamilleService = new RFMembreFamilleService(transaction);

        MembreFamilleVO[] searchMembresFamilleRequerantDomaineRentes = rfMembreFamilleService.getMembreFamille(idTiers,
                date, isFratrie);
        if (searchMembresFamilleRequerantDomaineRentes != null) {
            for (MembreFamilleVO membreFamille : searchMembresFamilleRequerantDomaineRentes) {
                if (membreFamille != null) {

                    if (membreFamille.getRelationAuRequerant().equals(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT)
                            || membreFamille.getRelationAuRequerant().equals(
                                    ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT)
                            || membreFamille.getRelationAuRequerant().equals(
                                    ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT)) {

                        // Pas d'idTiers, pas de RFM
                        if (!JadeStringUtil.isIntegerEmpty(membreFamille.getIdTiers())) {

                            String[] tabBeneficiaire = new String[9];

                            tabBeneficiaire[0] = membreFamille.getIdTiers();

                            if (isFratrie && idTiers.equals(membreFamille.getIdTiers())) {
                                tabBeneficiaire[1] = IPCDroits.CS_ROLE_FAMILLE_REQUERANT;
                            } else {
                                tabBeneficiaire[1] = RFUtils
                                        .getCsTypeRelationPc(membreFamille.getRelationAuRequerant());
                            }

                            tabBeneficiaire[2] = membreFamille.getNss();
                            tabBeneficiaire[3] = membreFamille.getNom();
                            tabBeneficiaire[4] = membreFamille.getPrenom();
                            tabBeneficiaire[5] = membreFamille.getDateNaissance();
                            tabBeneficiaire[6] = membreFamille.getCsSexe();
                            tabBeneficiaire[7] = membreFamille.getCsNationalite();
                            tabBeneficiaire[8] = Boolean.TRUE.toString();

                            membresFamilleVec.add(tabBeneficiaire);
                        }
                    }
                }
            }
        }

        return membresFamilleVec;

    }

    /**
     * Construit un tableau représentant le détail d'une famille
     * 
     * @param idTiers
     * @param typeRelation
     * @param nss
     * @param nom
     * @param prenom
     * @param dateNaissance
     * @param csSexe
     * @param csNationalite
     * @return String[idTiers,typeRelation,nss,nom,prenom,dateNaissance,csSexe,csNationalite,isComprisDansCalcul]
     */
    public static String[] getMembreFamilleTabString(String idTiers, String typeRelation, String nss, String nom,
            String prenom, String dateNaissance, String csSexe, String csNationalite, Boolean isComprisDansCalcul) {

        String[] tab = new String[9];

        tab[0] = idTiers;
        tab[1] = typeRelation;
        tab[2] = nss;
        tab[3] = nom;
        tab[4] = prenom;
        tab[5] = dateNaissance;
        tab[6] = csSexe;
        tab[7] = csNationalite;
        tab[8] = isComprisDansCalcul.booleanValue() ? Boolean.TRUE.toString() : Boolean.FALSE.toString();

        return tab;

    }

    public static RFAssQdDossierJointDossierJointTiersManager getMembresFamilleGrandeQd(BSession session, String idQd)
            throws Exception {

        RFAssQdDossierJointDossierJointTiersManager rfAssQdDossierJointDossierJointTiersMgr = new RFAssQdDossierJointDossierJointTiersManager();
        rfAssQdDossierJointDossierJointTiersMgr.setSession(session);
        rfAssQdDossierJointDossierJointTiersMgr.setForIdQd(idQd);
        rfAssQdDossierJointDossierJointTiersMgr.changeManagerSize(0);
        rfAssQdDossierJointDossierJointTiersMgr.find();

        return rfAssQdDossierJointDossierJointTiersMgr;
    }

    /**
     * Méthode qui calcul le montant résiduel d'une Qd
     * 
     * @param String
     *            , String, String, String
     */
    public static String getMntResiduel(String limiteAnnuelle, String augmentationQd, String soldeCharge,
            String chargeRFM) {

        BigDecimal montantResiduel = new BigDecimal(0.00);

        montantResiduel = montantResiduel.add(new BigDecimal(JANumberFormatter.deQuote(limiteAnnuelle.trim())));

        montantResiduel = montantResiduel.add(new BigDecimal(JANumberFormatter.deQuote(augmentationQd.trim())));
        montantResiduel = montantResiduel.add(new BigDecimal(JANumberFormatter.deQuote(soldeCharge.trim())).negate());
        if (!JadeStringUtil.isBlankOrZero(chargeRFM)) {
            montantResiduel = montantResiduel.add(new BigDecimal(JANumberFormatter.deQuote(chargeRFM.trim())).negate());
        }

        return montantResiduel.setScale(2).toString();

    }

    /**
     * 
     * Méthode retournant les qds assurées selon les critères passés en paramètres
     * 
     * @param session
     * @param idsMembresFamille
     * @param idTiers
     * @param codeTypeDeSoin
     * @param codeSousTypeDeSoin
     * @param etatQd
     * @param anneeQd
     * @param dateDebut
     * @param dateDeFin
     * @param dateBetweenPeriode
     * @param forCsEtatNotCloture
     * @param idQdToIgnore
     * @return RFQdAssureJointDossierJointTiersManager
     * @throws Exception
     */
    public static RFQdAssureJointDossierJointTiersManager getRFQdAssureJointDossierJointTiersManager(BSession session,
            Set<String> idsMembresFamille, String idTiers, String codeTypeDeSoin, String codeSousTypeDeSoin,
            String etatQd, String anneeQd, String dateDebutBetweenPeriode, String dateFinBetweenPeriode,
            String dateBetweenPeriode, boolean forCsEtatNotCloture, String idQdToIgnore, String csTypeRelation)
            throws Exception {

        RFQdAssureJointDossierJointTiersManager rfQdAssJointDosJointTieMgr = new RFQdAssureJointDossierJointTiersManager();
        rfQdAssJointDosJointTieMgr.setSession(session);

        if (null != idsMembresFamille) {
            rfQdAssJointDosJointTieMgr.setForIdsTiers(idsMembresFamille);
        } else {
            rfQdAssJointDosJointTieMgr.setForIdTiers(idTiers);
        }

        rfQdAssJointDosJointTieMgr.setForCodeTypeDeSoin(codeTypeDeSoin);
        rfQdAssJointDosJointTieMgr.setForCodeSousTypeDeSoin(codeSousTypeDeSoin);
        rfQdAssJointDosJointTieMgr.setForCsEtatQd(etatQd);
        rfQdAssJointDosJointTieMgr.setForCsEtatNotCloture(forCsEtatNotCloture);
        rfQdAssJointDosJointTieMgr.setForAnneeQd(anneeQd);
        rfQdAssJointDosJointTieMgr.setForDateDebutBetweenPeriode(dateDebutBetweenPeriode);
        rfQdAssJointDosJointTieMgr.setForDateFinBetweenPeriode(dateFinBetweenPeriode);
        rfQdAssJointDosJointTieMgr.setForDateBetweenPeriode(dateBetweenPeriode);
        rfQdAssJointDosJointTieMgr.setForCsTypeRelation(csTypeRelation);
        rfQdAssJointDosJointTieMgr.setIdQdToIgnore(idQdToIgnore);
        rfQdAssJointDosJointTieMgr.changeManagerSize(0);
        rfQdAssJointDosJointTieMgr.find();

        return rfQdAssJointDosJointTieMgr;

    }

    /**
     * Méthode qui calcul le solde de charge d'une Qd
     * 
     * @param String
     *            , BSession
     */
    public static String getSoldeDeCharge(String idQdStr, BSession session) throws Exception {

        RFQdSoldeChargeManager rfQdSoldeChargeMgr = new RFQdSoldeChargeManager();

        rfQdSoldeChargeMgr.setForIdQd(idQdStr);
        rfQdSoldeChargeMgr.setSession(session);
        rfQdSoldeChargeMgr.setForDerniereVersion(Boolean.TRUE.toString());
        rfQdSoldeChargeMgr.changeManagerSize(0);
        rfQdSoldeChargeMgr.find();

        Iterator<RFQdSoldeCharge> rfQdSoldeChargeIter = rfQdSoldeChargeMgr.iterator();
        BigDecimal totalMontantSolde = new BigDecimal(0.00);

        while (rfQdSoldeChargeIter.hasNext()) {
            RFQdSoldeCharge rfQdSoldeCharge = rfQdSoldeChargeIter.next();
            if (null != rfQdSoldeCharge) {
                totalMontantSolde = totalMontantSolde.add(new BigDecimal(JANumberFormatter.deQuote(rfQdSoldeCharge
                        .getMontantSolde().trim())));
            }
        }

        return totalMontantSolde.setScale(2).toString();

    }

    /**
     * Méthode qui calcul le solde excedent de revenu d'une Qd
     * 
     * @param String
     *            , BSession
     */
    public static String getSoldeExcedentDeRevenu(String idQdStr, BSession session) throws Exception {

        RFQdSoldeExcedentDeRevenuManager rfQdSoldeExcedentDeRevenuMgr = new RFQdSoldeExcedentDeRevenuManager();

        rfQdSoldeExcedentDeRevenuMgr.setForIdQd(idQdStr);
        rfQdSoldeExcedentDeRevenuMgr.setSession(session);
        rfQdSoldeExcedentDeRevenuMgr.setForDerniereVersion(Boolean.TRUE.toString());
        rfQdSoldeExcedentDeRevenuMgr.changeManagerSize(0);
        rfQdSoldeExcedentDeRevenuMgr.find();

        Iterator<RFQdSoldeExcedentDeRevenu> rfQdSoldeExcedentDeRevenuIter = rfQdSoldeExcedentDeRevenuMgr.iterator();
        BigDecimal totalMontantSolde = new BigDecimal(0.00);

        while (rfQdSoldeExcedentDeRevenuIter.hasNext()) {
            RFQdSoldeExcedentDeRevenu rfQdSoldeExcedentDeRevenu = rfQdSoldeExcedentDeRevenuIter.next();
            if (null != rfQdSoldeExcedentDeRevenu) {
                totalMontantSolde = totalMontantSolde.add(new BigDecimal(JANumberFormatter
                        .deQuote(rfQdSoldeExcedentDeRevenu.getMontantSoldeExcedent().trim())));
            }
        }

        return totalMontantSolde.setScale(2).toString();

    }

    public static String getTypePc_AVS_AI(String csTypePrestationPc) throws Exception {

        if (!JadeStringUtil.isBlank(csTypePrestationPc)) {
            if (IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(csTypePrestationPc)
                    || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(csTypePrestationPc)) {
                return RFUtils.TYPE_PC_AVS;
            } else {
                if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(csTypePrestationPc)) {
                    return RFUtils.TYPE_PC_AI;
                } else {
                    throw new Exception("RFUtils.getTypePc_AVS_AI(): cs type Pc non valable");
                }
            }
        } else {
            return "";
        }
    }

    public static boolean hasErrorsLogList(List<String[]> logsList) {

        boolean hasErrors = false;
        for (String[] log : logsList) {
            if (Integer.parseInt(log[0]) == JadeBusinessMessageLevels.ERROR) {
                hasErrors = true;
            }
        }

        return hasErrors;
    }

    public static RFAdaptationJournaliereContext initContextAdaptation(DecisionPcVO pcVOs, String idGestionnaire) {

        try {

            RFAdaptationJournaliereContext contextCourant = new RFAdaptationJournaliereContext();
            contextCourant.setIdAdaptationJournaliere("");
            contextCourant.setTypeDeDecisionPc(pcVOs.getCsTypeDecision());
            contextCourant.setDateDebutDecision("01." + pcVOs.getDateDebut());

            String dateFinStr = "";
            if (!JadeStringUtil.isBlankOrZero(pcVOs.getDateFin())) {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                GregorianCalendar calendar = new GregorianCalendar();

                Date dateFinDate = dateFormat.parse("01." + pcVOs.getDateFin());
                calendar.setTime(dateFinDate);

                calendar.add(Calendar.MONTH, 1);
                calendar.add(Calendar.DAY_OF_WEEK, -1);
                dateFinDate = calendar.getTime();
                dateFinStr = dateFormat.format(dateFinDate);

            }

            contextCourant.setDateFinDecision(dateFinStr);

            contextCourant.setGestionnaire(idGestionnaire);
            contextCourant.setIdTiersBeneficiaire(pcVOs.getIdTiersBeneficiaire());
            contextCourant.setIdDecisionPc(pcVOs.getIdDecision());
            contextCourant.setNumeroDecisionPc(pcVOs.getNoDecision());
            contextCourant.setNssTiersBeneficiaire(pcVOs.getNss());
            // nouveauContext.setDateDernierPmt(this.getDateDernierPmt());

            return contextCourant;

        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Teste si un montant est arrondi au 5 cts
     * 
     * @param FWViewBeanInterface
     *            , String, String
     * @throws Exception
     */
    public static Boolean isMontantArrondiCinqCts(String montant) {

        if (!JadeStringUtil.isEmpty(montant)) {
            montant = new FWCurrency(montant).toString();
            char dernierCharactere = montant.charAt(montant.length() - 1);

            if ((dernierCharactere == '0') || (dernierCharactere == '5')) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            return Boolean.TRUE;
        }

    }

    /**
     * 
     * Méthode qui test le format d'un NSS
     * 
     * @param numAvs
     * @return boolean
     */
    public static boolean isNNS(String numAvs) {
        char[] listCharacter = numAvs.toCharArray();
        for (int i = 0; i < listCharacter.length; i++) {
            char c = listCharacter[i];
            if ((i != 3) && (i != 8) && (i != 13)) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }

        }
        if ((numAvs.length() == 16) && numAvs.startsWith("756.") && (numAvs.charAt(3) == '.')
                && (numAvs.charAt(8) == '.') && (numAvs.charAt(13) == '.')) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Retourne vrai si le sous type de soin concerne plusieurs personnes
     * 
     * @param codeTypeDeSoin
     * @param codeSousTypeDeSoin
     * @return
     */
    public static boolean isSousTypeDeSoinCodeConcernePlusieursPersonnes(String codeTypeDeSoin,
            String codeSousTypeDeSoin) {
        return ((codeTypeDeSoin.equals(IRFCodeTypesDeSoins.TYPE_13_MAINTIEN_A_DOMICILE) && codeSousTypeDeSoin
                .equals(IRFCodeTypesDeSoins.SOUS_TYPE_13_2_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE)) || (codeTypeDeSoin
                .equals(IRFCodeTypesDeSoins.TYPE_13_MAINTIEN_A_DOMICILE) && codeSousTypeDeSoin
                .equals(IRFCodeTypesDeSoins.SOUS_TYPE_13_5_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC)));

    }

    /**
     * Retourne vrai si le sous type de soin concerne plusieurs personnes
     * 
     * @param codeTypeDeSoin
     * @param codeSousTypeDeSoin
     * @return
     */
    public static boolean isSousTypeDeSoinCsConcernePlusieursPersonnes(String csSousTypeDeSoin) {
        return (csSousTypeDeSoin.equals(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE) || csSousTypeDeSoin
                .equals(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC));

    }

    /**
     * Retourne vrai si le sous-type de soin ne s'impute pas sur la grande Qd
     * 
     * @param codeTypeDeSoin
     * @param codeSousTypeDeSoin
     * @return
     */
    public static boolean isSousTypeDeSoinNonImputeSurGrandeQd(String codeTypeDeSoin, String codeSousTypeDeSoin) {
        return (/*
                 * (codeTypeDeSoin.equals(IRFCodeTypesDeSoins.TYPE_12_STRUCTURE_ET_SEJOURS) && codeSousTypeDeSoin
                 * .equals(IRFCodeTypesDeSoins.SOUS_TYPE_12_7_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE)) ||
                 */(codeTypeDeSoin.equals(IRFCodeTypesDeSoins.TYPE_20_FINANCEMENT_DES_SOINS) && codeSousTypeDeSoin
                .equals(IRFCodeTypesDeSoins.SOUS_TYPE_20_1_FINANCEMENT_DES_SOINS)));

    }

    public static RFLotManager retrieveLotOuvert(BSession session, String typeLot) throws Exception {

        RFLotManager lotManager = new RFLotManager();
        lotManager.setSession(session);
        lotManager.setForCsLotOwner(IRELot.CS_LOT_OWNER_RFM);
        lotManager.setForCsEtat(IRELot.CS_ETAT_LOT_OUVERT);
        lotManager.setForCsType(typeLot);
        lotManager.changeManagerSize(0);
        lotManager.find();

        return lotManager;
    }

    public static BigDecimal retrieveMontantAffecteDevis(String idDevis, BSession session) throws Exception {

        BigDecimal montantAffecteBigDec = new BigDecimal("0");

        RFAssDemandeDev19Ftd15Manager rfDevisMgr = new RFAssDemandeDev19Ftd15Manager();
        rfDevisMgr.setSession(session);
        rfDevisMgr.setForIdDemande19(idDevis);
        rfDevisMgr.changeManagerSize(0);
        rfDevisMgr.find();

        Iterator<RFAssDemandeDev19Ftd15> rfDevisItr = rfDevisMgr.iterator();

        while (rfDevisItr.hasNext()) {
            RFAssDemandeDev19Ftd15 rfAssDemDevFtd = rfDevisItr.next();

            if (null != rfAssDemDevFtd) {

                montantAffecteBigDec = montantAffecteBigDec.add(new BigDecimal(rfAssDemDevFtd
                        .getMontantAssocieAuDevis().replace("'", "")));

            }

        }

        return montantAffecteBigDec;

    }

    /**
     * Méthode qui ajoute une erreur inattendue dans le viewBean
     * 
     * @param FWViewBeanInterface
     *            , String, String
     * @throws Exception
     */
    public static void setMsgErreurInattendueViewBean(FWViewBeanInterface viewBean, String methode, String classe) {
        if (null != viewBean) {
            viewBean.setMessage(classe + "." + methode + " : Erreur inattendue");
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    /**
     * Ajoute une erreur au viewBean
     * 
     * @param FWViewBeanInterface
     *            , String
     * @throws Exception
     */
    public static void setMsgErreurViewBean(FWViewBeanInterface viewBean, String labelLibelle) {
        if (null != viewBean) {
            viewBean.setMessage(JadeStringUtil.isBlank(viewBean.getMessage()) ? ((BSession) viewBean.getISession())
                    .getLabel(labelLibelle) : ((viewBean.getMessage() + "<BR/>" + ((BSession) viewBean.getISession())
                    .getLabel(labelLibelle))));

            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    /**
     * Ajoute une erreur au viewBean
     * 
     * @param FWViewBeanInterface
     *            , String
     * @throws Exception
     */
    public static void setMsgExceptionErreurViewBean(FWViewBeanInterface viewBean, String message) {
        if (null != viewBean) {
            viewBean.setMessage(JadeStringUtil.isBlank(viewBean.getMessage()) ? message : ((viewBean.getMessage()
                    + "<BR/>" + message)));

            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    /**
     * Ajoute une erreur au viewBean
     * 
     * @param FWViewBeanInterface
     *            , String
     * @throws Exception
     */
    public static void setMsgExceptionWarningViewBean(FWViewBeanInterface viewBean, String message) {
        if (null != viewBean) {
            viewBean.setMessage(JadeStringUtil.isBlank(viewBean.getMessage()) ? message : ((viewBean.getMessage()
                    + "<BR/>" + message)));

            viewBean.setMsgType(FWViewBeanInterface.WARNING);
        }
    }

    /**
     * Méthode qui ajoute un avertissement dans le viewBean
     * 
     * @param FWViewBeanInterface
     *            , String
     * @throws Exception
     */
    public static void setMsgWarningViewBean(FWViewBeanInterface viewBean, String labelLibelle, Object... args) {

        if ((null != viewBean) && !FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            viewBean.setMessage(String.format(
                    JadeStringUtil.isBlank(viewBean.getMessage()) ? ((BSession) viewBean.getISession())
                            .getLabel(labelLibelle) : ((viewBean.getMessage() + "<BR/>" + ((BSession) viewBean
                            .getISession()).getLabel(labelLibelle))), args));

            viewBean.setMsgType(FWViewBeanInterface.WARNING);
        }
    }

    /**
     * Méthode qui ajoute un avertissement dans le viewBean
     * 
     * @param FWViewBeanInterface
     *            , String
     * @throws Exception
     */
    public static void setMsgWarningViewBean(FWViewBeanInterface viewBean, String labelLibelle) {

        if ((null != viewBean) && !FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            viewBean.setMessage(JadeStringUtil.isBlank(viewBean.getMessage()) ? ((BSession) viewBean.getISession())
                    .getLabel(labelLibelle) : ((viewBean.getMessage() + "<BR/>" + ((BSession) viewBean.getISession())
                    .getLabel(labelLibelle))));

            viewBean.setMsgType(FWViewBeanInterface.WARNING);
        }
    }

    /**
     * Charge et renvoie une administration en fonction de son idTiers.
     * 
     * @param idAdministration l'id de l'administration/idTiers
     * @param aSession pour accéder à la DB.
     * @return une administration. Sera <code>null</code> si la session est <code>null</code>, que l'idAdministration
     *         est <code>null</code> ou vide ou ne correspond à aucune administration.
     * @throws Exception si un problème d'accès DB survient.
     */
    public static TIAdministrationViewBean loadAdministration(String idAdministration, BSession aSession)
            throws Exception {
        if (JadeStringUtil.isEmpty(idAdministration)) {
            return null;
        }
        if (aSession == null) {
            return null;
        }
        if (!aSession.isConnected()) {
            aSession = (BSession) PRSession.connectSession(aSession, TIApplication.DEFAULT_APPLICATION_PYXIS);
        }
        TIAdministrationViewBean t = new TIAdministrationViewBean();
        t.setISession(aSession);
        t.setIdTiersAdministration(idAdministration);
        t.retrieve();
        if (t.isNew()) {
            return null;
        } else {
            return t;
        }
    }

    /**
     * Calcule la date d'envoi du lot en fonction de la prestation correspondante.
     * 
     * @return Jamais <code>null</code>. La date d'envoi du lot, ou la date du jour si le lot n'a pas pu être lu;
     * @throws Exception si la session n'est pas bonne ou qu'un problème d'accès DB se produit.
     */
    public static String computeDateEnvoiLotFromPrestation(String idPrestation, BSession session) throws Exception {
        // Recherche de la date d'envoi du lot
        String dateEnvoiLot = "";
        RFPrestation rfPrest = new RFPrestation();
        rfPrest.setSession(session);
        rfPrest.setIdPrestation(idPrestation);
        rfPrest.retrieve();
        if (!rfPrest.isNew()) {
            RELot reLot = new RELot();
            reLot.setSession(session);
            reLot.setIdLot(rfPrest.getIdLot());
            reLot.retrieve();
            if (!reLot.isNew()) {
                if (!JadeStringUtil.isBlankOrZero(reLot.getDateEnvoiLot())) {
                    dateEnvoiLot = reLot.getDateEnvoiLot();
                } else {
                    dateEnvoiLot = JACalendar.todayJJsMMsAAAA();
                }
            } else {
                dateEnvoiLot = JACalendar.todayJJsMMsAAAA();
            }
        }
        return dateEnvoiLot;
    }

    /**
     * Recherche si le bénéficiaire concerne un enfant
     * 
     * @param session
     * @param idTier
     * @param idQd
     * @return
     * @throws Exception
     */
    public static boolean isEnfant(BSession session, String idTier, String idQd) throws Exception {

        RFAssQdDossierManager rfAssQdDosMgr = new RFAssQdDossierManager();
        rfAssQdDosMgr.setSession(session);
        rfAssQdDosMgr.setForIdQdBase(idQd);
        String idDossier = RFUtils.getDossierJointPrDemande(idTier, session).getIdDossier();
        if (null != idDossier) {
            rfAssQdDosMgr.setForIdDossier(idDossier);
            rfAssQdDosMgr.changeManagerSize(0);
            rfAssQdDosMgr.find();
            if (rfAssQdDosMgr.size() == 1) {
                RFAssQdDossier rfAssQdDos = (RFAssQdDossier) rfAssQdDosMgr.getFirstEntity();
                if (null != rfAssQdDos) {
                    if (IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(rfAssQdDos.getTypeRelation())) {
                        return true;
                    }
                } else {
                    throw new RFRetrieveIsEnfantException(
                            "RFValidationDemandeService.montantsQdAssure(): RFAssQdDossier null");
                }
            } else {
                throw new RFRetrieveIsEnfantException(
                        "RFValidationDemandeService.montantsQdAssure(): RFAssQdDossier introuvable");
            }
        } else {
            throw new RFRetrieveIsEnfantException("RFValidationDemandeService.montantsQdAssure(): Dossier introuvable");
        }

        return false;
    }
}
