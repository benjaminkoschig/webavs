package globaz.cygnus.services.comptabilite;

import globaz.cygnus.api.ordresversements.IRFOrdresVersements;
import globaz.cygnus.vb.paiement.IRFModuleComptable;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author fha
 */
public class RFModuleComptableFactory {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // Class statique, référence par cette instance
    private static RFModuleComptableFactory instance = null;

    /**
     * @return L'instance de cette classe
     * @throws Exception
     */
    public static synchronized RFModuleComptableFactory getInstance() throws Exception {
        if (RFModuleComptableFactory.instance == null) {
            RFModuleComptableFactory.instance = new RFModuleComptableFactory();
        }
        return RFModuleComptableFactory.instance;
    }

    public APIRubrique RFM_AIDE_AU_MENAGE_AI = null;
    public APIRubrique RFM_AIDE_AU_MENAGE_AI_SASH = null;
    public APIRubrique RFM_AIDE_AU_MENAGE_AI_SPAS = null;
    public APIRubrique RFM_AIDE_AU_MENAGE_AVS = null;
    public APIRubrique RFM_AIDE_AU_MENAGE_AVS_SASH = null;
    public APIRubrique RFM_AIDE_AU_MENAGE_AVS_SPAS = null;

    public APIRubrique RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI = null;
    public APIRubrique RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI_SASH = null;
    public APIRubrique RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI_SPAS = null;
    public APIRubrique RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS = null;
    public APIRubrique RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS_SASH = null;
    public APIRubrique RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS_SPAS = null;

    public APIRubrique RFM_CCJU_AVANCE_SAS = null;

    public APIRubrique RFM_CCJU_TIERS_FINANCEMENT_SOINS_AI = null;
    public APIRubrique RFM_CCJU_TIERS_FINANCEMENT_SOINS_AI_IMPORTATION = null;
    public APIRubrique RFM_CCJU_TIERS_FINANCEMENT_SOINS_AVS = null;
    public APIRubrique RFM_CCJU_TIERS_FINANCEMENT_SOINS_AVS_IMPORTATION = null;

    public APIRubrique RFM_COMPENSATION = null;

    public APIRubrique RFM_COTISATIONS_PARITAIRES_AI = null;
    public APIRubrique RFM_COTISATIONS_PARITAIRES_AI_SASH = null;
    public APIRubrique RFM_COTISATIONS_PARITAIRES_AI_SPAS = null;
    public APIRubrique RFM_COTISATIONS_PARITAIRES_AVS = null;
    public APIRubrique RFM_COTISATIONS_PARITAIRES_AVS_SASH = null;
    public APIRubrique RFM_COTISATIONS_PARITAIRES_AVS_SPAS = null;

    public APIRubrique RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI = null;
    public APIRubrique RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI_SASH = null;
    public APIRubrique RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI_SPAS = null;
    public APIRubrique RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS = null;
    public APIRubrique RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS_SASH = null;
    public APIRubrique RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS_SPAS = null;

    public APIRubrique RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AI = null;
    public APIRubrique RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AI_SASH = null;
    public APIRubrique RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AI_SPAS = null;
    public APIRubrique RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AVS = null;
    public APIRubrique RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AVS_SASH = null;
    public APIRubrique RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AVS_SPAS = null;

    public APIRubrique RFM_FRAIS_DE_TRANSPORT_AI = null;
    public APIRubrique RFM_FRAIS_DE_TRANSPORT_AI_SASH = null;
    public APIRubrique RFM_FRAIS_DE_TRANSPORT_AI_SPAS = null;
    public APIRubrique RFM_FRAIS_DE_TRANSPORT_AVS = null;
    public APIRubrique RFM_FRAIS_DE_TRANSPORT_AVS_SASH = null;
    public APIRubrique RFM_FRAIS_DE_TRANSPORT_AVS_SPAS = null;

    public APIRubrique RFM_FRQP_AI = null;
    public APIRubrique RFM_FRQP_AI_SASH = null;
    public APIRubrique RFM_FRQP_AI_SPAS = null;
    public APIRubrique RFM_FRQP_AVS = null;
    public APIRubrique RFM_FRQP_AVS_SASH = null;
    public APIRubrique RFM_FRQP_AVS_SPAS = null;

    public APIRubrique RFM_MOYEN_AUXILIAIRE_AI = null;
    public APIRubrique RFM_MOYEN_AUXILIAIRE_AI_SASH = null;
    public APIRubrique RFM_MOYEN_AUXILIAIRE_AI_SPAS = null;
    public APIRubrique RFM_MOYEN_AUXILIAIRE_AVS = null;
    public APIRubrique RFM_MOYEN_AUXILIAIRE_AVS_SASH = null;
    public APIRubrique RFM_MOYEN_AUXILIAIRE_AVS_SPAS = null;

    public APIRubrique RFM_PARTICIPATION_COURT_SEJOUR_AI = null;
    public APIRubrique RFM_PARTICIPATION_COURT_SEJOUR_AI_SASH = null;
    public APIRubrique RFM_PARTICIPATION_COURT_SEJOUR_AI_SPAS = null;
    public APIRubrique RFM_PARTICIPATION_COURT_SEJOUR_AVS = null;
    public APIRubrique RFM_PARTICIPATION_COURT_SEJOUR_AVS_SASH = null;
    public APIRubrique RFM_PARTICIPATION_COURT_SEJOUR_AVS_SPAS = null;

    public APIRubrique RFM_PENSION_HOME_DE_JOUR_AI = null;
    public APIRubrique RFM_PENSION_HOME_DE_JOUR_AI_SASH = null;
    public APIRubrique RFM_PENSION_HOME_DE_JOUR_AI_SPAS = null;
    public APIRubrique RFM_PENSION_HOME_DE_JOUR_AVS = null;
    public APIRubrique RFM_PENSION_HOME_DE_JOUR_AVS_SASH = null;
    public APIRubrique RFM_PENSION_HOME_DE_JOUR_AVS_SPAS = null;

    public APIRubrique RFM_REGIME_AI = null;
    public APIRubrique RFM_REGIME_AI_SASH = null;
    public APIRubrique RFM_REGIME_AI_SPAS = null;
    public APIRubrique RFM_REGIME_AVS = null;
    public APIRubrique RFM_REGIME_AVS_SASH = null;
    public APIRubrique RFM_REGIME_AVS_SPAS = null;

    public APIRubrique RFM_RESTITUTION_AI = null;

    public APIRubrique RFM_RESTITUTION_AIDE_AU_MENAGE_AI = null;
    public APIRubrique RFM_RESTITUTION_AIDE_AU_MENAGE_AVS = null;
    public APIRubrique RFM_RESTITUTION_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI = null;
    public APIRubrique RFM_RESTITUTION_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS = null;
    public APIRubrique RFM_RESTITUTION_AVS = null;
    public APIRubrique RFM_RESTITUTION_COTISATIONS_PARITAIRES_AI = null;
    public APIRubrique RFM_RESTITUTION_COTISATIONS_PARITAIRES_AVS = null;
    public APIRubrique RFM_RESTITUTION_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI = null;
    public APIRubrique RFM_RESTITUTION_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS = null;
    public APIRubrique RFM_RESTITUTION_FRAIS_DE_PENSION_COURT_SEJOUR_AI = null;
    public APIRubrique RFM_RESTITUTION_FRAIS_DE_PENSION_COURT_SEJOUR_AVS = null;
    public APIRubrique RFM_RESTITUTION_FRAIS_DE_TRANSPORT_AI = null;
    public APIRubrique RFM_RESTITUTION_FRAIS_DE_TRANSPORT_AVS = null;
    public APIRubrique RFM_RESTITUTION_FRQP_AI = null;
    public APIRubrique RFM_RESTITUTION_FRQP_AVS = null;
    public APIRubrique RFM_RESTITUTION_MOYENS_AUXILIAIRES_AI = null;
    public APIRubrique RFM_RESTITUTION_MOYENS_AUXILIAIRES_AVS = null;
    public APIRubrique RFM_RESTITUTION_PARTICIPATION_COURT_SEJOUR_AI = null;
    public APIRubrique RFM_RESTITUTION_PARTICIPATION_COURT_SEJOUR_AVS = null;
    public APIRubrique RFM_RESTITUTION_PENSION_HOME_DE_JOUR_AI = null;
    public APIRubrique RFM_RESTITUTION_PENSION_HOME_DE_JOUR_AVS = null;
    public APIRubrique RFM_RESTITUTION_REGIME_AI = null;
    public APIRubrique RFM_RESTITUTION_REGIME_AVS = null;
    public APIRubrique RFM_RESTITUTION_TRAITMENT_DENTAIRE_AI = null;
    public APIRubrique RFM_RESTITUTION_TRAITMENT_DENTAIRE_AVS = null;
    public APIRubrique RFM_RESTITUTION_UNITE_ACCUEIL_TEMPORAIRE_AI = null;
    public APIRubrique RFM_RESTITUTION_UNITE_ACCUEIL_TEMPORAIRE_AVS = null;

    public APIRubrique RFM_TIERS_BENEFICIAIRE_MALADIE_AI = null;
    public APIRubrique RFM_TIERS_BENEFICIAIRE_MALADIE_AI_SASH = null;
    public APIRubrique RFM_TIERS_BENEFICIAIRE_MALADIE_AI_SPAS = null;
    public APIRubrique RFM_TIERS_BENEFICIAIRE_MALADIE_AVS = null;
    public APIRubrique RFM_TIERS_BENEFICIAIRE_MALADIE_AVS_SASH = null;
    public APIRubrique RFM_TIERS_BENEFICIAIRE_MALADIE_AVS_SPAS = null;

    public APIRubrique RFM_TRAITEMENT_DENTAIRE_AI = null;
    public APIRubrique RFM_TRAITEMENT_DENTAIRE_AI_SASH = null;
    public APIRubrique RFM_TRAITEMENT_DENTAIRE_AI_SPAS = null;
    public APIRubrique RFM_TRAITEMENT_DENTAIRE_AVS = null;
    public APIRubrique RFM_TRAITEMENT_DENTAIRE_AVS_SASH = null;
    public APIRubrique RFM_TRAITEMENT_DENTAIRE_AVS_SPAS = null;

    public APIRubrique RFM_UNITE_ACCUEIL_TEMPORAIRE_AI = null;
    public APIRubrique RFM_UNITE_ACCUEIL_TEMPORAIRE_AI_SASH = null;
    public APIRubrique RFM_UNITE_ACCUEIL_TEMPORAIRE_AI_SPAS = null;
    public APIRubrique RFM_UNITE_ACCUEIL_TEMPORAIRE_AVS = null;
    public APIRubrique RFM_UNITE_ACCUEIL_TEMPORAIRE_AVS_SASH = null;
    public APIRubrique RFM_UNITE_ACCUEIL_TEMPORAIRE_AVS_SPAS = null;

    private RFModuleComptableFactory() throws Exception {
    }

    /**
     * @param decision
     * @param isEcritureComptablePourCeLot
     * @return
     * @throws Exception
     */
    public IRFModuleComptable[] getModules(RFPrestationData prestation, boolean isEcritureComptablePourCeLot,
            BSession session, String typePaiement, StringBuffer idLot) throws Exception {

        Map<String, IRFModuleComptable> modulesComptable = new TreeMap<String, IRFModuleComptable>();

        IRFModuleComptable modCpt = null;
        boolean isSansEcritureComptablePourCetteDecision = false;

        // récupérer toutes les OV de la prest
        for (Iterator<RFOrdreVersementData> it = prestation.getOrdresVersement().iterator(); it.hasNext();) {
            RFOrdreVersementData ordreVersement = it.next();
            if (IRFOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL.equals(ordreVersement.getTypeOrdreVersement())
                    && !isSansEcritureComptablePourCetteDecision) {

                modCpt = new RFModCpt_Normal(!isSansEcritureComptablePourCetteDecision);
                if (!modulesComptable.containsKey(modCpt.toString())) {
                    modulesComptable.put(modCpt.toString(), modCpt);
                }

            }

            // TODO isValide???
            /*
             * if (IRFOrdresVersements.CS_TYPE_DETTE.equals(ordreVersement.getTypeOrdreVersement())) { modCpt = new
             * RFModCpt_Restitution(!isSansEcritureComptablePourCetteDecision); if
             * (!modulesComptable.containsKey(modCpt.toString())) { modulesComptable.put(modCpt.toString(), modCpt); } }
             */
        }
        Set<String> keys = modulesComptable.keySet();
        IRFModuleComptable[] result = new IRFModuleComptable[keys.size()];
        int i = 0;
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            result[i] = modulesComptable.get(key);
            i++;

        }

        return result;

    }

    /**
     * Initialise les Id des rubriques
     * 
     * @param sessionOsiris
     *            une instance de APIProcessComptabilisation
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void initIdsRubriques(BISession sessionOsiris, boolean isAjoutDemEnComSanTenComTypDeHom) throws Exception {

        APIReferenceRubrique referenceRubrique = (APIReferenceRubrique) sessionOsiris
                .getAPIFor(APIReferenceRubrique.class);

        Map<String, APIRubrique> rubriquesSanTenComTypDeHomMap = new HashMap<String, APIRubrique>();
        Map<String, APIRubrique> rubriquesEnTenComTypDeHomMap = new HashMap<String, APIRubrique>();

        // // C
        RFM_RESTITUTION_AI = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER);
        rubriquesSanTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER, RFM_RESTITUTION_AI);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER, RFM_RESTITUTION_AI);

        // // C
        RFM_RESTITUTION_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER);
        rubriquesSanTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER, RFM_RESTITUTION_AVS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER, RFM_RESTITUTION_AVS);

        // // C
        RFM_COMPENSATION = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.COMPENSATION_RENTES);
        rubriquesSanTenComTypDeHomMap.put(APIReferenceRubrique.COMPENSATION_RENTES, RFM_COMPENSATION);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.COMPENSATION_RENTES, RFM_COMPENSATION);

        // // C
        RFM_TIERS_BENEFICIAIRE_MALADIE_AI = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI);
        rubriquesSanTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI, RFM_TIERS_BENEFICIAIRE_MALADIE_AI);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI, RFM_TIERS_BENEFICIAIRE_MALADIE_AI);

        // // C
        RFM_TIERS_BENEFICIAIRE_MALADIE_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS);
        rubriquesSanTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS, RFM_TIERS_BENEFICIAIRE_MALADIE_AVS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS, RFM_TIERS_BENEFICIAIRE_MALADIE_AVS);

        // // C
        RFM_MOYEN_AUXILIAIRE_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_MOYENS_AUXILIAIRES);
        rubriquesSanTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_MOYENS_AUXILIAIRES, RFM_MOYEN_AUXILIAIRE_AI);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_MOYENS_AUXILIAIRES, RFM_MOYEN_AUXILIAIRE_AI);

        // // C
        RFM_MOYEN_AUXILIAIRE_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_MOYENS_AUXILIAIRES);
        rubriquesSanTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_MOYENS_AUXILIAIRES, RFM_MOYEN_AUXILIAIRE_AVS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_MOYENS_AUXILIAIRES, RFM_MOYEN_AUXILIAIRE_AVS);

        // C
        RFM_CCJU_TIERS_FINANCEMENT_SOINS_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FINANCEMENT_DES_SOINS);
        rubriquesSanTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_FINANCEMENT_DES_SOINS,
                RFM_CCJU_TIERS_FINANCEMENT_SOINS_AI);

        // C
        RFM_CCJU_TIERS_FINANCEMENT_SOINS_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FINANCEMENT_DES_SOINS);
        rubriquesSanTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_FINANCEMENT_DES_SOINS,
                RFM_CCJU_TIERS_FINANCEMENT_SOINS_AVS);

        // C
        RFM_CCJU_TIERS_FINANCEMENT_SOINS_AI_IMPORTATION = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FINANCEMENT_DES_SOINS_IMPORTATION_AUTO);
        rubriquesSanTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_FINANCEMENT_DES_SOINS_IMPORTATION_AUTO,
                RFM_CCJU_TIERS_FINANCEMENT_SOINS_AI_IMPORTATION);

        // C
        RFM_CCJU_TIERS_FINANCEMENT_SOINS_AVS_IMPORTATION = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FINANCEMENT_DES_SOINS_IMPORTATION_AUTO);
        rubriquesSanTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_FINANCEMENT_DES_SOINS_IMPORTATION_AUTO,
                RFM_CCJU_TIERS_FINANCEMENT_SOINS_AVS_IMPORTATION);

        // C
        RFM_CCJU_AVANCE_SAS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVANCE_SAS);
        rubriquesSanTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVANCE_SAS, RFM_CCJU_AVANCE_SAS);

        /******************************************************************/
        //
        RFM_RESTITUTION_AIDE_AU_MENAGE_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_AIDE_AU_MENAGE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_AIDE_AU_MENAGE,
                RFM_RESTITUTION_AIDE_AU_MENAGE_AI);

        //
        RFM_RESTITUTION_AIDE_AU_MENAGE_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_AIDE_AU_MENAGE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_AIDE_AU_MENAGE,
                RFM_RESTITUTION_AIDE_AU_MENAGE_AVS);

        //
        RFM_RESTITUTION_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE,
                RFM_RESTITUTION_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI);

        //
        RFM_RESTITUTION_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE,
                RFM_RESTITUTION_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS);

        //
        RFM_RESTITUTION_COTISATIONS_PARITAIRES_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_COTISATIONS_PARITAIRES);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_COTISATIONS_PARITAIRES,
                RFM_RESTITUTION_COTISATIONS_PARITAIRES_AI);

        //
        RFM_RESTITUTION_COTISATIONS_PARITAIRES_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_COTISATIONS_PARITAIRES);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_COTISATIONS_PARITAIRES,
                RFM_RESTITUTION_COTISATIONS_PARITAIRES_AVS);

        //
        RFM_RESTITUTION_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL,
                RFM_RESTITUTION_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI);

        //
        RFM_RESTITUTION_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL,
                RFM_RESTITUTION_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS);

        //
        RFM_RESTITUTION_FRAIS_DE_TRANSPORT_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_FRAIS_DE_TRANSPORT);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_FRAIS_DE_TRANSPORT,
                RFM_RESTITUTION_FRAIS_DE_TRANSPORT_AI);

        //
        RFM_RESTITUTION_FRAIS_DE_TRANSPORT_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_FRAIS_DE_TRANSPORT);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_FRAIS_DE_TRANSPORT,
                RFM_RESTITUTION_FRAIS_DE_TRANSPORT_AVS);

        //
        RFM_RESTITUTION_FRQP_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_FRQP);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_FRQP, RFM_RESTITUTION_FRQP_AI);

        //
        RFM_RESTITUTION_FRQP_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_FRQP);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_FRQP, RFM_RESTITUTION_FRQP_AVS);

        //
        RFM_RESTITUTION_MOYENS_AUXILIAIRES_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_MOYENS_AUXILIAIRES);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_MOYENS_AUXILIAIRES,
                RFM_RESTITUTION_MOYENS_AUXILIAIRES_AI);

        //
        RFM_RESTITUTION_MOYENS_AUXILIAIRES_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_MOYENS_AUXILIAIRES);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_MOYENS_AUXILIAIRES,
                RFM_RESTITUTION_MOYENS_AUXILIAIRES_AVS);

        //
        RFM_RESTITUTION_PARTICIPATION_COURT_SEJOUR_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_PARTICIPATION_COURT_SEJOUR);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_PARTICIPATION_COURT_SEJOUR,
                RFM_RESTITUTION_PARTICIPATION_COURT_SEJOUR_AI);

        //
        RFM_RESTITUTION_PARTICIPATION_COURT_SEJOUR_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_PARTICIPATION_COURT_SEJOUR);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_PARTICIPATION_COURT_SEJOUR,
                RFM_RESTITUTION_PARTICIPATION_COURT_SEJOUR_AVS);

        //
        RFM_RESTITUTION_PENSION_HOME_DE_JOUR_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_PENSION_HOME_DE_JOUR);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_PENSION_HOME_DE_JOUR,
                RFM_RESTITUTION_PENSION_HOME_DE_JOUR_AI);

        //
        RFM_RESTITUTION_PENSION_HOME_DE_JOUR_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_PENSION_HOME_DE_JOUR);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_PENSION_HOME_DE_JOUR,
                RFM_RESTITUTION_PENSION_HOME_DE_JOUR_AVS);

        //
        RFM_RESTITUTION_REGIME_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_REGIME);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_REGIME, RFM_RESTITUTION_REGIME_AI);

        //
        RFM_RESTITUTION_REGIME_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_REGIME);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_REGIME, RFM_RESTITUTION_REGIME_AVS);

        //
        RFM_RESTITUTION_TRAITMENT_DENTAIRE_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_TRAITMENT_DENTAIRE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_TRAITMENT_DENTAIRE,
                RFM_RESTITUTION_TRAITMENT_DENTAIRE_AI);

        //
        RFM_RESTITUTION_TRAITMENT_DENTAIRE_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_TRAITMENT_DENTAIRE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_TRAITMENT_DENTAIRE,
                RFM_RESTITUTION_TRAITMENT_DENTAIRE_AVS);

        //
        RFM_RESTITUTION_UNITE_ACCUEIL_TEMPORAIRE_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_UNITE_ACCUEIL_TEMPORAIRE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_UNITE_ACCUEIL_TEMPORAIRE,
                RFM_RESTITUTION_UNITE_ACCUEIL_TEMPORAIRE_AI);

        //
        RFM_RESTITUTION_UNITE_ACCUEIL_TEMPORAIRE_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_UNITE_ACCUEIL_TEMPORAIRE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_UNITE_ACCUEIL_TEMPORAIRE,
                RFM_RESTITUTION_UNITE_ACCUEIL_TEMPORAIRE_AVS);

        //
        RFM_RESTITUTION_FRAIS_DE_PENSION_COURT_SEJOUR_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_A_RESTITUER_FRAIS_DE_PENSION_COURT_SEJOUR);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_A_RESTITUER_FRAIS_DE_PENSION_COURT_SEJOUR,
                RFM_RESTITUTION_FRAIS_DE_PENSION_COURT_SEJOUR_AI);

        //
        RFM_RESTITUTION_FRAIS_DE_PENSION_COURT_SEJOUR_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_A_RESTITUER_FRAIS_DE_PENSION_COURT_SEJOUR);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_A_RESTITUER_FRAIS_DE_PENSION_COURT_SEJOUR,
                RFM_RESTITUTION_FRAIS_DE_PENSION_COURT_SEJOUR_AVS);

        /******************************************************************************************/

        //
        RFM_TIERS_BENEFICIAIRE_MALADIE_AI_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_SASH, RFM_TIERS_BENEFICIAIRE_MALADIE_AI_SASH);

        //
        RFM_TIERS_BENEFICIAIRE_MALADIE_AI_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_SPAS, RFM_TIERS_BENEFICIAIRE_MALADIE_AI_SPAS);

        //
        RFM_TIERS_BENEFICIAIRE_MALADIE_AVS_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_SASH, RFM_TIERS_BENEFICIAIRE_MALADIE_AVS_SASH);

        //
        RFM_TIERS_BENEFICIAIRE_MALADIE_AVS_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_SPAS, RFM_TIERS_BENEFICIAIRE_MALADIE_AVS_SPAS);

        //
        RFM_MOYEN_AUXILIAIRE_AI_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_MOYENS_AUXILIAIRES_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_MOYENS_AUXILIAIRES_SASH,
                RFM_MOYEN_AUXILIAIRE_AI_SASH);

        //
        RFM_MOYEN_AUXILIAIRE_AI_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_MOYENS_AUXILIAIRES_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_MOYENS_AUXILIAIRES_SPAS,
                RFM_MOYEN_AUXILIAIRE_AI_SPAS);

        //
        RFM_MOYEN_AUXILIAIRE_AVS_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_MOYENS_AUXILIAIRES_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_MOYENS_AUXILIAIRES_SASH,
                RFM_MOYEN_AUXILIAIRE_AVS_SASH);

        //
        RFM_MOYEN_AUXILIAIRE_AVS_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_MOYENS_AUXILIAIRES_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_MOYENS_AUXILIAIRES_SPAS,
                RFM_MOYEN_AUXILIAIRE_AVS_SPAS);

        //
        RFM_AIDE_AU_MENAGE_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE, RFM_AIDE_AU_MENAGE_AI);

        //
        RFM_AIDE_AU_MENAGE_AI_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE_SASH, RFM_AIDE_AU_MENAGE_AI_SASH);

        //
        RFM_AIDE_AU_MENAGE_AI_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE_SPAS, RFM_AIDE_AU_MENAGE_AI_SPAS);

        //
        RFM_AIDE_AU_MENAGE_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE, RFM_AIDE_AU_MENAGE_AVS);

        //
        RFM_AIDE_AU_MENAGE_AVS_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE_SASH, RFM_AIDE_AU_MENAGE_AVS_SASH);

        //
        RFM_AIDE_AU_MENAGE_AVS_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE_SPAS, RFM_AIDE_AU_MENAGE_AVS_SPAS);

        //
        RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE,
                RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI);

        //
        RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SASH,
                RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI_SASH);

        //
        RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SPAS,
                RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI_SPAS);

        //
        RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE,
                RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS);

        //
        RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SASH,
                RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS_SASH);

        //
        RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SPAS,
                RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS_SPAS);

        //
        RFM_COTISATIONS_PARITAIRES_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_COTISATIONS_PARITAIRES);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_COTISATIONS_PARITAIRES,
                RFM_COTISATIONS_PARITAIRES_AI);

        //
        RFM_COTISATIONS_PARITAIRES_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_COTISATIONS_PARITAIRES);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_COTISATIONS_PARITAIRES,
                RFM_COTISATIONS_PARITAIRES_AVS);

        //
        RFM_COTISATIONS_PARITAIRES_AVS_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_COTISATIONS_PARITAIRES_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_COTISATIONS_PARITAIRES_SASH,
                RFM_COTISATIONS_PARITAIRES_AVS_SASH);

        //
        RFM_COTISATIONS_PARITAIRES_AVS_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_COTISATIONS_PARITAIRES_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_COTISATIONS_PARITAIRES_SPAS,
                RFM_COTISATIONS_PARITAIRES_AVS_SPAS);

        //
        RFM_COTISATIONS_PARITAIRES_AI_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_COTISATIONS_PARITAIRES_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_COTISATIONS_PARITAIRES_SASH,
                RFM_COTISATIONS_PARITAIRES_AI_SASH);

        //
        RFM_COTISATIONS_PARITAIRES_AI_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_COTISATIONS_PARITAIRES_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_COTISATIONS_PARITAIRES_SPAS,
                RFM_COTISATIONS_PARITAIRES_AI_SPAS);

        //
        RFM_PARTICIPATION_COURT_SEJOUR_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_PARTICIPATION_COURT_SEJOUR);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_PARTICIPATION_COURT_SEJOUR,
                RFM_PARTICIPATION_COURT_SEJOUR_AI);

        //
        RFM_PARTICIPATION_COURT_SEJOUR_AI_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_PARTICIPATION_COURT_SEJOUR_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_PARTICIPATION_COURT_SEJOUR_SASH,
                RFM_PARTICIPATION_COURT_SEJOUR_AI_SASH);

        //
        RFM_PARTICIPATION_COURT_SEJOUR_AI_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_PARTICIPATION_COURT_SEJOUR_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_PARTICIPATION_COURT_SEJOUR_SPAS,
                RFM_PARTICIPATION_COURT_SEJOUR_AI_SPAS);

        //
        RFM_PARTICIPATION_COURT_SEJOUR_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_PARTICIPATION_COURT_SEJOUR);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_PARTICIPATION_COURT_SEJOUR,
                RFM_PARTICIPATION_COURT_SEJOUR_AVS);

        //
        RFM_PARTICIPATION_COURT_SEJOUR_AVS_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_PARTICIPATION_COURT_SEJOUR_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_PARTICIPATION_COURT_SEJOUR_SASH,
                RFM_PARTICIPATION_COURT_SEJOUR_AVS_SASH);

        //
        RFM_PARTICIPATION_COURT_SEJOUR_AVS_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_PARTICIPATION_COURT_SEJOUR_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_PARTICIPATION_COURT_SEJOUR_SPAS,
                RFM_PARTICIPATION_COURT_SEJOUR_AVS_SPAS);

        //
        RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL,
                RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI);

        //
        RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SASH,
                RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI_SASH);

        //
        RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SPAS,
                RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI_SPAS);

        //
        RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL,
                RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS);

        //
        RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SASH,
                RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS_SASH);

        //
        RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SPAS,
                RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS_SPAS);

        //
        RFM_FRAIS_DE_TRANSPORT_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FRAIS_DE_TRANSPORT);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_FRAIS_DE_TRANSPORT, RFM_FRAIS_DE_TRANSPORT_AI);

        //
        RFM_FRAIS_DE_TRANSPORT_AI_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FRAIS_DE_TRANSPORT_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_FRAIS_DE_TRANSPORT_SASH,
                RFM_FRAIS_DE_TRANSPORT_AI_SASH);

        //
        RFM_FRAIS_DE_TRANSPORT_AI_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FRAIS_DE_TRANSPORT_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_FRAIS_DE_TRANSPORT_SPAS,
                RFM_FRAIS_DE_TRANSPORT_AI_SPAS);

        //
        RFM_FRAIS_DE_TRANSPORT_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FRAIS_DE_TRANSPORT);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_FRAIS_DE_TRANSPORT, RFM_FRAIS_DE_TRANSPORT_AVS);

        //
        RFM_FRAIS_DE_TRANSPORT_AVS_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FRAIS_DE_TRANSPORT_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_FRAIS_DE_TRANSPORT_SASH,
                RFM_FRAIS_DE_TRANSPORT_AVS_SASH);

        //
        RFM_FRAIS_DE_TRANSPORT_AVS_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FRAIS_DE_TRANSPORT_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_FRAIS_DE_TRANSPORT_SPAS,
                RFM_FRAIS_DE_TRANSPORT_AVS_SPAS);

        //
        RFM_FRQP_AI = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FRQP);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_FRQP, RFM_FRQP_AI);

        //
        RFM_FRQP_AI_SASH = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FRQP_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_FRQP_SASH, RFM_FRQP_AI_SASH);

        //
        RFM_FRQP_AI_SPAS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FRQP_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_FRQP_SPAS, RFM_FRQP_AI_SPAS);

        //
        RFM_FRQP_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FRQP);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_FRQP, RFM_FRQP_AVS);

        //
        RFM_FRQP_AVS_SASH = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FRQP_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_FRQP_SASH, RFM_FRQP_AVS_SASH);

        //
        RFM_FRQP_AVS_SPAS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FRQP_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_FRQP_SPAS, RFM_FRQP_AVS_SPAS);

        //
        RFM_PENSION_HOME_DE_JOUR_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_PENSION_HOME_DE_JOUR);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_PENSION_HOME_DE_JOUR, RFM_PENSION_HOME_DE_JOUR_AI);

        //
        RFM_PENSION_HOME_DE_JOUR_AI_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_PENSION_HOME_DE_JOUR_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_PENSION_HOME_DE_JOUR_SASH,
                RFM_PENSION_HOME_DE_JOUR_AI_SASH);

        //
        RFM_PENSION_HOME_DE_JOUR_AI_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_PENSION_HOME_DE_JOUR_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_PENSION_HOME_DE_JOUR_SPAS,
                RFM_PENSION_HOME_DE_JOUR_AI_SPAS);

        //
        RFM_PENSION_HOME_DE_JOUR_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_PENSION_HOME_DE_JOUR);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_PENSION_HOME_DE_JOUR,
                RFM_PENSION_HOME_DE_JOUR_AVS);

        //
        RFM_PENSION_HOME_DE_JOUR_AVS_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_PENSION_HOME_DE_JOUR_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_PENSION_HOME_DE_JOUR_SASH,
                RFM_PENSION_HOME_DE_JOUR_AVS_SASH);

        //
        RFM_PENSION_HOME_DE_JOUR_AVS_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_PENSION_HOME_DE_JOUR_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_PENSION_HOME_DE_JOUR_SPAS,
                RFM_PENSION_HOME_DE_JOUR_AVS_SPAS);

        //
        RFM_REGIME_AI = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_REGIME);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_REGIME, RFM_REGIME_AI);

        //
        RFM_REGIME_AI_SASH = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_REGIME_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_REGIME_SASH, RFM_REGIME_AI_SASH);

        //
        RFM_REGIME_AI_SPAS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_REGIME_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_REGIME_SPAS, RFM_REGIME_AI_SPAS);

        //
        RFM_REGIME_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_REGIME);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_REGIME, RFM_REGIME_AVS);

        //
        RFM_REGIME_AVS_SASH = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_REGIME_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_REGIME_SASH, RFM_REGIME_AVS_SASH);

        //
        RFM_REGIME_AVS_SPAS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_REGIME_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_REGIME_SPAS, RFM_REGIME_AVS_SPAS);

        //
        RFM_TRAITEMENT_DENTAIRE_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_TRAITEMENT_DENTAIRE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_TRAITEMENT_DENTAIRE, RFM_TRAITEMENT_DENTAIRE_AI);

        //
        RFM_TRAITEMENT_DENTAIRE_AI_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_TRAITEMENT_DENTAIRE_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_TRAITEMENT_DENTAIRE_SASH,
                RFM_TRAITEMENT_DENTAIRE_AI_SASH);

        //
        RFM_TRAITEMENT_DENTAIRE_AI_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_TRAITEMENT_DENTAIRE_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_TRAITEMENT_DENTAIRE_SPAS,
                RFM_TRAITEMENT_DENTAIRE_AI_SPAS);

        //
        RFM_TRAITEMENT_DENTAIRE_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_TRAITEMENT_DENTAIRE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_TRAITEMENT_DENTAIRE, RFM_TRAITEMENT_DENTAIRE_AVS);

        //
        RFM_TRAITEMENT_DENTAIRE_AVS_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_TRAITEMENT_DENTAIRE_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_TRAITEMENT_DENTAIRE_SASH,
                RFM_TRAITEMENT_DENTAIRE_AVS_SASH);

        //
        RFM_TRAITEMENT_DENTAIRE_AVS_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_TRAITEMENT_DENTAIRE_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_TRAITEMENT_DENTAIRE_SPAS,
                RFM_TRAITEMENT_DENTAIRE_AVS_SPAS);

        //
        RFM_UNITE_ACCUEIL_TEMPORAIRE_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_UNITE_ACCUEIL_TEMPORAIRE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_UNITE_ACCUEIL_TEMPORAIRE,
                RFM_UNITE_ACCUEIL_TEMPORAIRE_AI);

        //
        RFM_UNITE_ACCUEIL_TEMPORAIRE_AI_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_UNITE_ACCUEIL_TEMPORAIRE_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_UNITE_ACCUEIL_TEMPORAIRE_SASH,
                RFM_UNITE_ACCUEIL_TEMPORAIRE_AI_SASH);

        //
        RFM_UNITE_ACCUEIL_TEMPORAIRE_AI_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_UNITE_ACCUEIL_TEMPORAIRE_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_UNITE_ACCUEIL_TEMPORAIRE_SPAS,
                RFM_UNITE_ACCUEIL_TEMPORAIRE_AI_SPAS);

        //
        RFM_UNITE_ACCUEIL_TEMPORAIRE_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE,
                RFM_UNITE_ACCUEIL_TEMPORAIRE_AVS);

        //
        RFM_UNITE_ACCUEIL_TEMPORAIRE_AVS_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE_SASH,
                RFM_UNITE_ACCUEIL_TEMPORAIRE_AVS_SASH);

        //
        RFM_UNITE_ACCUEIL_TEMPORAIRE_AVS_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE_SPAS,
                RFM_UNITE_ACCUEIL_TEMPORAIRE_AVS_SPAS);

        // Pas utilisé ???
        RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AI = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR);
        // rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR,RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AI);

        //
        RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AI_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR_SASH,
                RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AI_SASH);

        //
        RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AI_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR_SPAS,
                RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AI_SPAS);

        //
        RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AVS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR,
                RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AVS);

        //
        RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AVS_SASH = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR_SASH);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR_SASH,
                RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AVS_SASH);

        //
        RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AVS_SPAS = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR_SPAS);
        rubriquesEnTenComTypDeHomMap.put(APIReferenceRubrique.RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR_SPAS,
                RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AVS_SPAS);

        isRubriquesInitialisees(isAjoutDemEnComSanTenComTypDeHom, rubriquesSanTenComTypDeHomMap,
                rubriquesEnTenComTypDeHomMap);

    }

    private void isRubriquesInitialisees(boolean isAjoutDemEnComSanTenComTypDeHom,
            Map<String, APIRubrique> rubriquesSanTenComTypDeHomMap,
            Map<String, APIRubrique> rubriquesEnTenComTypDeHomMap) throws Exception {

        // Selon la propriété isAjoutDemandeSansTenirCompteTypeDeHome, on test les rubriques devant être initialisées
        List<String> nomRubriqueNullesList = new ArrayList<String>();
        if (isAjoutDemEnComSanTenComTypDeHom) {
            for (Map.Entry<String, APIRubrique> entry : rubriquesSanTenComTypDeHomMap.entrySet()) {
                if (null == entry.getValue()) {
                    nomRubriqueNullesList.add(entry.getKey());
                }
            }
        } else {
            for (Map.Entry<String, APIRubrique> entry : rubriquesEnTenComTypDeHomMap.entrySet()) {
                if (null == entry.getValue()) {
                    nomRubriqueNullesList.add(entry.getKey());
                }
            }
        }

        if (nomRubriqueNullesList.size() > 0) {

            StringBuffer msgErreur = new StringBuffer();
            msgErreur
                    .append("RFModuleComptableFactory.initIdsRubriques(): Les rubriques suivantes ne sont pas initialisées: ");
            for (int i = 0; i < nomRubriqueNullesList.size(); i++) {
                String nomRubrique = nomRubriqueNullesList.get(i);
                if (i == nomRubriqueNullesList.size() - 1) {
                    msgErreur.append(nomRubrique);
                } else {
                    msgErreur.append(nomRubrique + ",");
                }
            }

            throw new Exception(msgErreur.toString());

        }
    }
}