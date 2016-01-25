/*
 * Créé le 10 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.application.RFApplication;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.application.PRAbstractApplication;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;

/**
 * 
 * @author jje
 * 
 *         Test spécifiquement certain sous type de soin
 * 
 */
public class RFVerificationTypesDeSoinsService {

    private RFVerificationTypesDeSoinsData data = null;
    // private static BSession session = null;
    private Set<String[]> labelsErrors = null;

    public RFVerificationTypesDeSoinsService(RFVerificationTypesDeSoinsData vData, BSession vSession) {
        data = vData;
        labelsErrors = new HashSet<String[]>();
        // session = vSession;
    }

    /**
     * 
     * @return [idMotifDeRefus, montantMotifRefus, montantAccepte]
     */
    private String[] calculMontantAPayer(String plafond, String idMotifDeRefus, String montantAPayer) throws Exception {

        plafond = plafond.replace("'", "");
        montantAPayer = montantAPayer.replace("'", "");

        BigDecimal plafondBigDec = new BigDecimal(plafond);
        String[] idMotifsDeRefus = null;

        if (!JadeStringUtil.isBlankOrZero(montantAPayer)) {
            BigDecimal futurMontantResiduel = plafondBigDec.add(new BigDecimal(montantAPayer).negate());

            if (futurMontantResiduel.floatValue() < 0) {
                if (plafondBigDec.compareTo(new BigDecimal(0)) == 0) {
                    idMotifsDeRefus = new String[] { idMotifDeRefus, montantAPayer, "0" };
                } else {
                    idMotifsDeRefus = new String[] { idMotifDeRefus, futurMontantResiduel.abs().toString(), plafond };
                }
            } else {
                idMotifsDeRefus = new String[] { "", "", futurMontantResiduel.toString() };
            }
        }

        return idMotifsDeRefus;

    }

    /*
     * private static void conventionType9() {
     * 
     * if (IRFCodeTypesDeSoins.TYPE_9_FRAIS_DE_LIVRAISON.equals(RFVerificationTypesDeSoinsService.data
     * .getCodeTypeDeSoin()) && !RFVerificationTypesDeSoinsService.data.getIsConventionne()) {
     * 
     * RFVerificationTypesDeSoinsService.labelsErrors.add(new String[] {
     * IRFMotifsRefus.ID_FRAIS_DE_LIVRAISON_FOURNISSEUR_NON_CONVENTIONNE,
     * RFVerificationTypesDeSoinsService.data.getMontantAPayer().replace("'", ""), "0" }); }
     * 
     * }
     */

    private void menage_13() {

        if (data.getCsGenrePcAccordee().equals(IPCPCAccordee.CS_GENRE_PC_HOME)
                && IRFCodeTypesDeSoins.TYPE_13_MAINTIEN_A_DOMICILE.equals(data.getCodeTypeDeSoin())) {

            labelsErrors.add(new String[] { IRFMotifsRefus.ID_MENAGE_NON_CAR_HOME,
                    data.getMontantAPayer().replace("'", ""), "0" });

        }
    }

    // Contrôle spécifique à un ou des types de soins
    private void moyensAuxiliaires_3_5_6_7_8_11_20() throws Exception {

        if (IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES.equals(data.getCodeTypeDeSoin())) {

            // Maximum 1/3 contribution AVS/AI
            if (!JadeStringUtil.isBlankOrZero(data.getMontantVerseOAI())
                    && !JadeStringUtil.isBlankOrZero(data.getMontantFacture44())) {

                BigDecimal montantVerseOAI = new BigDecimal(data.getMontantVerseOAI().replace("'", ""));
                BigDecimal montantFacture44 = new BigDecimal(data.getMontantFacture44().replace("'", ""));

                if (montantVerseOAI.floatValue() < montantFacture44.floatValue()) {
                    labelsErrors.add(new String[] { IRFMotifsRefus.ID_MOYEN_AUXILIAIRE_MAXIMUM_1_3_CONTRIBUTION_AVS_AI,
                            data.getMontantAPayer().replace("'", ""), "0" });
                    // Pas de montant accepté car déjà comptabilisé dans l'affichage de l'écran de la demande
                }
            }
        }

        // TODO: MBO refactoriser cette partie (corrections effectués dans BZ_8124)
        if (IRFCodeTypesDeSoins.TYPE_3_MOYENS_AUXILIAIRES.equals(data.getCodeTypeDeSoin())
                || IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES.equals(data.getCodeTypeDeSoin())
                || IRFCodeTypesDeSoins.TYPE_6_REPARTITION_DES_MOYENS_AUXILIAIRES.equals(data.getCodeTypeDeSoin())
                || IRFCodeTypesDeSoins.TYPE_7_REPARTITION_DES_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI
                        .equals(data.getCodeTypeDeSoin())
                || IRFCodeTypesDeSoins.TYPE_8_LOCATION_DE_MOYENS_AUXILIAIRES_SUBSIDIAIREMENT_A_L_AI.equals(data
                        .getCodeTypeDeSoin())
                || IRFCodeTypesDeSoins.TYPE_11_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI.equals(data
                        .getCodeTypeDeSoin())) {

            // Non car home
            if (data.getCsGenrePcAccordee().equals(IPCPCAccordee.CS_GENRE_PC_HOME)
                    && (data.getCsTypeCsBeneficiaire().equals(
                            IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE)
                            || data.getCsTypeCsBeneficiaire().equals(
                                    IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME) || data
                            .getCsTypeCsBeneficiaire().equals(IRFTypesBeneficiairePc.PERSONNES_SEULES_VEUVES))) {

                // Autorise les types de soins ci-dessous si Home
                if (!((data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_3_MOYENS_AUXILIAIRES) && (data
                        .getCodeSousTypeDeSoin().equals(IRFCodeTypesDeSoins.SOUS_TYPE_3_CORSET_ORTHOPEDIQUE)))
                        || (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_3_MOYENS_AUXILIAIRES) && (data
                                .getCodeSousTypeDeSoin()
                                .equals(IRFCodeTypesDeSoins.SOUS_TYPE_3_LUNETTES_VERRES_DE_CONTACT)))
                        || (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_3_MOYENS_AUXILIAIRES) && (data
                                .getCodeSousTypeDeSoin().equals(IRFCodeTypesDeSoins.SOUS_TYPE_3_PILES_ACOUSTIQUES)))
                        || (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES) && (data
                                .getCodeSousTypeDeSoin().equals(IRFCodeTypesDeSoins.SOUS_TYPE_5_APPAREIL_ACOUSTIQUE)))
                        || (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES) && (data
                                .getCodeSousTypeDeSoin()
                                .equals(IRFCodeTypesDeSoins.SOUS_TYPE_5_CHAUSSURES_ORTHOPEDIQUE)))
                        || (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES) && (data
                                .getCodeSousTypeDeSoin().equals(IRFCodeTypesDeSoins.SOUS_TYPE_5_PERRUQUE)))
                        || (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES) && (data
                                .getCodeSousTypeDeSoin().equals(IRFCodeTypesDeSoins.SOUS_TYPE_5_APPAREIL_ORTHOPEDIQUE)))
                        || (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES) && (data
                                .getCodeSousTypeDeSoin().equals(IRFCodeTypesDeSoins.SOUS_TYPE_5_EPITHESE)))
                        || (data.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES) && (data
                                .getCodeSousTypeDeSoin().equals(IRFCodeTypesDeSoins.SOUS_TYPE_5_PROTHESE)))
                        || (data.getCodeTypeDeSoin().equals(
                                IRFCodeTypesDeSoins.TYPE_6_REPARTITION_DES_MOYENS_AUXILIAIRES) && (data
                                .getCodeSousTypeDeSoin().equals(IRFCodeTypesDeSoins.SOUS_TYPE_6_APPAREIL_ACOUSTIQUE)))
                        || (data.getCodeTypeDeSoin().equals(
                                IRFCodeTypesDeSoins.TYPE_6_REPARTITION_DES_MOYENS_AUXILIAIRES) && (data
                                .getCodeSousTypeDeSoin().equals(IRFCodeTypesDeSoins.SOUS_TYPE_6_PILES_ACOUSTIQUE))) || (data
                        .getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_6_REPARTITION_DES_MOYENS_AUXILIAIRES) && (data
                        .getCodeSousTypeDeSoin().equals(IRFCodeTypesDeSoins.SOUS_TYPE_6_CHAUSSURES_ORTHOPEDIQUES))))) {

                    labelsErrors.add(new String[] { IRFMotifsRefus.ID_MOYEN_AUXILIAIRE_NON_CAR_HOME,
                            data.getMontantAPayer().replace("'", ""), "0" });

                }
            }
        }

        if (IRFCodeTypesDeSoins.TYPE_20_FINANCEMENT_DES_SOINS.equals(data.getCodeTypeDeSoin())
                && !data.getCsGenrePcAccordee().equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {

            labelsErrors.add(new String[] { IRFMotifsRefus.ID_FINANCEMENT_NON_CAR_DOMICILE,
                    data.getMontantAPayer().replace("'", ""), "0" });

        }

    }

    // Voir doublon
    /*
     * private void franchiseEtQuoteParts_17() throws Exception{ //FRQP déjà remboursées (spécifique CCJU) }
     */

    // Institutions
    private void structureEtSejour_12() throws Exception {

        if (IRFCodeTypesDeSoins.TYPE_12_STRUCTURE_ET_SEJOURS.equals(data.getCodeTypeDeSoin())) {

            String maxCantonal = PRAbstractApplication.getApplication(RFApplication.DEFAULT_APPLICATION_CYGNUS)
                    .getProperty(RFApplication.PROPERTY_MNT_MAX_CANTONAL_STRUCTURES_ET_SEJOUR);

            // Prix de pension supérieur au maximal cantonal -> propriété
            labelsErrors.add(calculMontantAPayer(maxCantonal,
                    IRFMotifsRefus.ID_PRIX_DE_PENSION_SUPERIEUR_AU_MAXIMUM_CANTONAL, data.getMontantAPayer()));
        }
    }

    /**
     * 
     * @param data
     * @param session
     * @return Set<String[]> labelsErrors (idMotifsDeRefus, montantMotifsRefus, montantAccepte)
     * @throws Exception
     */
    public Set<String[]> verifierTypesDeSoins() throws Exception {

        moyensAuxiliaires_3_5_6_7_8_11_20();

        structureEtSejour_12();

        // conventionType9();

        menage_13();

        return labelsErrors;
    }

}
