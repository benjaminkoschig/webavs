/*
 * Créé le 10 novembre 2010
 */
package globaz.cygnus.services.validerDemande;

import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.db.demandes.RFDemandeDedoublonnageManager;
import globaz.cygnus.services.preparerDecision.RFVerificationDoublonData;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;

/**
 * 
 * @author jje
 * 
 *         Vérifie si une demande à déjà été remboursée
 * 
 */
public class RFVerificationDoublonService {

    /**
     * @param data
     * @param session
     * @param isProcessPreparerValiderDecision
     * @return String warningLabel, null si aucune demande trouvée
     * @throws Exception
     */
    public static String doublons(RFVerificationDoublonData data, BSession session,
            boolean isProcessPreparerValiderDecision, BITransaction transaction) throws Exception {

        if (!data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_18_FRAIS_REFUSES)
                && !data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_19_DEVIS_DENTAIRE)
                && !data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_20_FINANCEMENT_DES_SOINS)) {

            // Recherche d'une facture comportant le même montant et/ou date de
            // traitement et/ou No de facture
            RFDemandeDedoublonnageManager rfDemDedMgr = new RFDemandeDedoublonnageManager();

            rfDemDedMgr.setSession(session);
            rfDemDedMgr.setForIdTiers(data.getIdTiers());

            // Contrôler si concerne uniquement le meme sous type de soin
            rfDemDedMgr.setForCodeSousTypeDeSoinList(data.getCodeSousTypeDeSoin());
            rfDemDedMgr.setForCodeTypeDeSoinList(data.getCodeTypeDeSoin());

            if ((null != data.getIdsDemandeToIgnore()) && (data.getIdsDemandeToIgnore().size() > 0)) {
                rfDemDedMgr.setForIdsDemandeToIgnore(data.getIdsDemandeToIgnore());
            }

            // Dédoublonnage sur la date de la facture
            if (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_3_MOYENS_AUXILIAIRES)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_4_RETOUCHES_COUTEUSES_DE_CHAUSSURES)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_6_REPARTITION_DES_MOYENS_AUXILIAIRES)
                    || data.getCodeTypeDeSoin()
                            .equals(IRFCodeTypesDeSoins.TYPE_7_REPARTITION_DES_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI)
                    || data.getCodeTypeDeSoin().equals(
                            IRFCodeTypesDeSoins.TYPE_8_LOCATION_DE_MOYENS_AUXILIAIRES_SUBSIDIAIREMENT_A_L_AI)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_9_FRAIS_DE_LIVRAISON)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_10_REPRISE_DE_LIT_ELECTRIQUE)
                    || data.getCodeTypeDeSoin().equals(
                            IRFCodeTypesDeSoins.TYPE_11_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_12_STRUCTURE_ET_SEJOURS)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_13_MAINTIEN_A_DOMICILE)
                    || data.getCodeTypeDeSoin()
                            .equals(IRFCodeTypesDeSoins.TYPE_14_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_15_FRAIS_DE_TRAITEMENT_DENTAIRE)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_16_FRAIS_DE_TRANSPORT)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS)) {

                rfDemDedMgr.setForDateFacture(data.getDateFacture());
                rfDemDedMgr.setPeriodeTraitementCumulative(false);

            }

            // Dédoublonnage sur la période de traitement
            if (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_1_COTISATIONS_PARITAIRES)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_2_REGIME_ALIMENTAIRE)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_3_MOYENS_AUXILIAIRES)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_4_RETOUCHES_COUTEUSES_DE_CHAUSSURES)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_6_REPARTITION_DES_MOYENS_AUXILIAIRES)
                    || data.getCodeTypeDeSoin()
                            .equals(IRFCodeTypesDeSoins.TYPE_7_REPARTITION_DES_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI)
                    || data.getCodeTypeDeSoin().equals(
                            IRFCodeTypesDeSoins.TYPE_8_LOCATION_DE_MOYENS_AUXILIAIRES_SUBSIDIAIREMENT_A_L_AI)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_9_FRAIS_DE_LIVRAISON)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_10_REPRISE_DE_LIT_ELECTRIQUE)
                    || data.getCodeTypeDeSoin().equals(
                            IRFCodeTypesDeSoins.TYPE_11_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_12_STRUCTURE_ET_SEJOURS)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_13_MAINTIEN_A_DOMICILE)
                    || data.getCodeTypeDeSoin()
                            .equals(IRFCodeTypesDeSoins.TYPE_14_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_15_FRAIS_DE_TRAITEMENT_DENTAIRE)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_16_FRAIS_DE_TRANSPORT)) {

                rfDemDedMgr.setFromDateDebutTraitement(data.getDateDebutTraitement());
                rfDemDedMgr.setFromDateFinTraitement(data.getDateFinTraitement());

                rfDemDedMgr.setHasPeriodeTraitement(true);

                /*
                 * if (RFPropertiesUtils.isPeriodeDeTraitementRegCotParCumulative(session)) {
                 * rfDemDedMgr.setPeriodeTraitementCumulative(true); } else { if
                 * (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_2_REGIME_ALIMENTAIRE) ||
                 * data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_1_COTISATIONS_PARITAIRES)) {
                 * rfDemDedMgr.setPeriodeTraitementCumulative(true); } }
                 */
            }

            // Dédoublonnage sur le montant de la facture
            if (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_15_FRAIS_DE_TRAITEMENT_DENTAIRE)
                    || data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS)) {
                rfDemDedMgr.setForMontantFacture(data.getMontantFacture().replace("'", ""));
                rfDemDedMgr.setHasMontantfacture(true);
            }

            // Dédoublonnage sur le no de facture
            if (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS)) {
                rfDemDedMgr.setForNoFacture(data.getNumeroDecompte());
                rfDemDedMgr.setHasNumeroFacture(true);
            }
            rfDemDedMgr.changeManagerSize(0);
            rfDemDedMgr.find(transaction);

            if (rfDemDedMgr.size() > 0) {
                return "WARNING_RF_DEM_S_DECOMPTE_FACTURE_DEJA_REMBOURSE";
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

}
