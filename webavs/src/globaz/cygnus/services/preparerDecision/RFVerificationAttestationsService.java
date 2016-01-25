package globaz.cygnus.services.preparerDecision;

import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.attestations.IRFAttestations;
import globaz.cygnus.db.attestations.RFAttestationJointAssAttestationDossier;
import globaz.cygnus.db.attestations.RFAttestationJointAssAttestationDossierManager;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashMap;
import java.util.Map;

public class RFVerificationAttestationsService {

    private BISession session = null;
    // Map<Cs sous-type de soin, Niveau d'avertissement>
    private Map<String, String> sousTypesDeSoinsAttestations = null;

    public RFVerificationAttestationsService(BISession session) {
        this.session = session;
    }

    public VerificationAttestation hasAttestationSelonSousTypeDeSoin(String codeTypeDeSoin, String codeSousTypeDeSoin,
            String date, String idDossier, String idCsSousTypeDemande) throws Exception {

        initSousTypeDeSoinsAttestation();

        if (JadeStringUtil.isBlankOrZero(idCsSousTypeDemande)) {
            idCsSousTypeDemande = RFUtils.getIdSousTypeDeSoin(codeTypeDeSoin, codeSousTypeDeSoin, (BSession) session);
        }

        if (sousTypesDeSoinsAttestations.containsKey(idCsSousTypeDemande)) {

            String niveauAvertissement = sousTypesDeSoinsAttestations.get(idCsSousTypeDemande);

            if ((null != niveauAvertissement) && !JadeStringUtil.isBlank(niveauAvertissement)) {

                RFAttestationJointAssAttestationDossierManager rfAttJoiAssAttDosMgr = new RFAttestationJointAssAttestationDossierManager();
                rfAttJoiAssAttDosMgr.setSession((BSession) session);

                if (date.length() == 7) {
                    rfAttJoiAssAttDosMgr.setForDate("01." + date);
                } else {
                    rfAttJoiAssAttDosMgr.setForDate(date);
                }
                rfAttJoiAssAttDosMgr.setForIdSousTypeDeSoin(idCsSousTypeDemande);
                rfAttJoiAssAttDosMgr.setForIdDossier(idDossier);
                rfAttJoiAssAttDosMgr.changeManagerSize(0);
                rfAttJoiAssAttDosMgr.find();
                VerificationAttestation verificationAttestation = new VerificationAttestation(niveauAvertissement);
                if (rfAttJoiAssAttDosMgr.size() > 0) {
                    RFAttestationJointAssAttestationDossier assAttestationDossier = (RFAttestationJointAssAttestationDossier) rfAttJoiAssAttDosMgr
                            .get(0);
                    verificationAttestation = new VerificationAttestation(niveauAvertissement, assAttestationDossier);
                    return verificationAttestation;
                }
                return verificationAttestation;

            } else {
                throw new Exception(
                        "RFVerificationAttestationsService.hasAttestationSelonSousTypeDeSoin: Impossible de retrouver le niveau d'avertissement");
            }

        } else {
            return new VerificationAttestation();
        }

    }

    private void initSousTypeDeSoinsAttestation() throws Exception {

        sousTypesDeSoinsAttestations = new HashMap<String, String>();

        /**
         * Certains soins son spécifiques à certaines caisse. Ces soins sont insérés selon paramètres en properties
         */
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE,
                IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE_DIABETIQUE,
                IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);

        // Selon properties caisses
        if (RFPropertiesUtils.verificationAttestationAjoutchaisePerce()) {
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_3_CHAISES_PERCEES,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        }
        // Selon properties caisses
        if (RFPropertiesUtils.verificationAttestationAjoutLombostatOrthopedique()) {
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_3_LOMBOSTAT_ORTHOPEDIQUE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        }
        // Selon properties caisses
        if (RFPropertiesUtils.verificationAttestationAjoutCorsetOrthopedique()) {
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_3_CORSET_ORTHOPEDIQUE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        }
        // Selon properties caisses
        if (RFPropertiesUtils.verificationAttestationAjoutMinerve()) {
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_3_MINERVE, IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        }
        // Selon properties caisses
        if (RFPropertiesUtils.verificationAttestationAjoutLunetteVerresContact()) {
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_3_LUNETTES_VERRES_DE_CONTACT,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        }
        // Selon properties caisses
        if (RFPropertiesUtils.verificationAttestationAjoutFraisEndoprotheses()) {
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_3_FRAIS_D_ENDOPROTHESES,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        }
        // Selon properties caisses
        if (RFPropertiesUtils.verificationAttestationAjoutTaxi()) {
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_16_Taxi,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT);
        }

        // Selon properties caisses
        if (RFPropertiesUtils.verificationAttestationAjoutLit()) {
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_8_LIT_ELECTRIQUE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        }

        // Selon properties caisses
        if (RFPropertiesUtils.verificationAttestationMoyensAuxRemisEnPretAvecBon()) {
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_INSTALLATION_SANITAIRE_AUTOMATIQUE_COMPLEMENTAIRE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_APPAREIL_RESPIRATOIRE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_CANNE_LONGUE_D_AVEUGLE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_CHAISE_POUR_COXARTHROSE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_CHIEN_GUIDE_POUR_AVEUGLE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_DISPOSITIF_AUTOMATIQUE_COMMANDE_TELEPHONE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_ELEVATEUR_POUR_MALADE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_INHALATEUR,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MACHINE_A_ECRIRE_AUTOMATIQUE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MACHINE_A_ECRIRE_ELECTRIQUE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MACHINE_A_ECRIRE_EN_BRAILLE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MAGNETOPHONE_POUR_AVEUGLE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MAGNETOPHONE_POUR_PARALYSE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_TOURNEUR_DE_PAGE,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        }
        // Selon properties caisses
        if (RFPropertiesUtils.verificationAttestationAjoutAideMenageParOsad()) {
            sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UNE_ORGANISATION_OSAD,
                    IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        }

        // this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_APPAREIL_ACOUSTIQUE,
        // IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_CHAUSSURES_ORTHOPEDIQUES,
                IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_FAUTEUIL_ROULANT_PAS_DANS_HOME,
                IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_PERRUQUE, IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_LUNNETTES_LOUPE,
                IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_APPAREIL_ORTHOPHONIQUE,
                IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_EPITHESES_DE_L_OEIL,
                IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_PROTHESE_FACIALE_EPITHESES,
                IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);

        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_9_BARRIERES, IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_9_LIT_ELECTRIQUE,
                IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_9_POTENCE, IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE,
                IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_CONTRAT_DE_TRAVAIL,
                IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC,
                IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_16_AU_LIEU_DU_TRAITEMENT_MEDICAL,
                IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT);
        sousTypesDeSoinsAttestations.put(
                IRFTypesDeSoins.st_16_DANS_UN_ATELIER_PROTEGE_OU_AUTRE_LIEU_DE_FORMATION_REHABILITATION,
                IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_16_DANS_UN_UAT_OU_ACCUEIL_DE_JOUR,
                IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT);
        sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_16_VISITE_CHEZ_LES_PARENTS_ENFANT_EN_EMS,
                IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT);

        /**
         * 
         // TODO DEPLACEMENT_PROPERTIES_MBO: Commenté properties dans RFApplication
         * if(RFApplication.PROPERTY_NUMERO_CAISSE_CCJU.equals(((BSession) this.session).getApplication().getProperty(
         * "noCaisse"))) {
         * 
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE_DIABETIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * 
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_3_CHAISES_PERCEES,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_3_LOMBOSTAT_ORTHOPEDIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_3_CORSET_ORTHOPEDIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_3_MINERVE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_3_LUNETTES_VERRES_DE_CONTACT,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_3_FRAIS_D_ENDOPROTHESES,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * 
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_APPAREIL_ACOUSTIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_CHAUSSURES_ORTHOPEDIQUES,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_FAUTEUIL_ROULANT_PAS_DANS_HOME,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_PERRUQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_LUNNETTES_LOUPE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_APPAREIL_ORTHOPHONIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_EPITHESES_DE_L_OEIL,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_PROTHESE_FACIALE_EPITHESES,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * 
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_9_BARRIERES,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_9_LIT_ELECTRIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_9_POTENCE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * 
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_APPAREIL_RESPIRATOIRE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_CANNE_LONGUE_D_AVEUGLE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_CHAISE_POUR_COXARTHROSE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_CHIEN_GUIDE_POUR_AVEUGLE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_DISPOSITIF_AUTOMATIQUE_COMMANDE_TELEPHONE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_ELEVATEUR_POUR_MALADE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_INHALATEUR,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS); this.sousTypesDeSoinsAttestations.put(
         * IRFTypesDeSoins.st_11_INSTALLATION_SANITAIRE_AUTOMATIQUE_COMPLEMENTAIRE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MACHINE_A_ECRIRE_AUTOMATIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MACHINE_A_ECRIRE_ELECTRIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MACHINE_A_ECRIRE_EN_BRAILLE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MAGNETOPHONE_POUR_AVEUGLE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MAGNETOPHONE_POUR_PARALYSE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_TOURNEUR_DE_PAGE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * 
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_CONTRAT_DE_TRAVAIL,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS); this.sousTypesDeSoinsAttestations.put(
         * IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * 
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_16_AU_LIEU_DU_TRAITEMENT_MEDICAL,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT); this.sousTypesDeSoinsAttestations.put(
         * IRFTypesDeSoins.st_16_DANS_UN_ATELIER_PROTEGE_OU_AUTRE_LIEU_DE_FORMATION_REHABILITATION,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_16_DANS_UN_UAT_OU_ACCUEIL_DE_JOUR,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_16_VISITE_CHEZ_LES_PARENTS_ENFANT_EN_EMS,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT); }
         * 
         * else {
         * 
         * // sousTypesDeSoinsAttestations.put( IRFTypesDeSoins.st_1_COTISATIONS_AVSAF_PARITAIRES, //
         * IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT); sousTypesDeSoinsAttestations.put( //
         * IRFTypesDeSoins.st_1_COTISATIONS_LAA, IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT); //
         * sousTypesDeSoinsAttestations.put( IRFTypesDeSoins.st_1_COTISATIONS_LPP, //
         * IRFAttestations.NIVEAU_AVERTISSEMENT_AVERTISSEMENT);
         * 
         * 
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE_DIABETIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * 
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_APPAREIL_ACOUSTIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_CHAUSSURES_ORTHOPEDIQUES,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_FAUTEUIL_ROULANT_PAS_DANS_HOME,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_PERRUQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_LUNNETTES_LOUPE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_APPAREIL_ORTHOPHONIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_EPITHESES_DE_L_OEIL,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_5_PROTHESE_FACIALE_EPITHESES,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * 
         * 
         * // sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_8_LIT_ELECTRIQUE , //
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * 
         * 
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_9_BARRIERES,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_9_LIT_ELECTRIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_9_POTENCE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * 
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_APPAREIL_RESPIRATOIRE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_CANNE_LONGUE_D_AVEUGLE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_CHAISE_POUR_COXARTHROSE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_CHIEN_GUIDE_POUR_AVEUGLE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_DISPOSITIF_AUTOMATIQUE_COMMANDE_TELEPHONE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_ELEVATEUR_POUR_MALADE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_INHALATEUR,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS); this.sousTypesDeSoinsAttestations.put(
         * IRFTypesDeSoins.st_11_INSTALLATION_SANITAIRE_AUTOMATIQUE_COMPLEMENTAIRE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MACHINE_A_ECRIRE_AUTOMATIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MACHINE_A_ECRIRE_ELECTRIQUE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MACHINE_A_ECRIRE_EN_BRAILLE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MAGNETOPHONE_POUR_AVEUGLE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_MAGNETOPHONE_POUR_PARALYSE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_11_TOURNEUR_DE_PAGE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * 
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_CONTRAT_DE_TRAVAIL,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * this.sousTypesDeSoinsAttestations.put(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UNE_ORGANISATION_OSAD,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS); this.sousTypesDeSoinsAttestations.put(
         * IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC,
         * IRFAttestations.NIVEAU_AVERTISSEMENT_REFUS);
         * 
         * }
         */
    }

}
