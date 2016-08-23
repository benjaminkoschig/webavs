package globaz.cygnus.services.comptabilite;

import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.ordresversements.IRFOrdresVersements;
import globaz.cygnus.api.prestationsaccordees.IRFGenrePrestations;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.utils.RFLogToDB;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.paiement.IRFModuleComptable;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.api.BIMessage;
import globaz.globall.api.BIMessageLog;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntRole;
import globaz.osiris.utils.CAUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.util.errordriller.ErrorDriller;
import ch.globaz.common.util.errordriller.ErrorDriller.DrilledError;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;

/**
 * @author fha
 * @revision JJE 5.08.2011,22.08.2012
 * @author vch
 */
public class RFModCpt_Normal extends ARFModuleComptable implements IRFModuleComptable {

    private APIGestionComptabiliteExterne compta = null;

    private String dateComptable = "";
    private StringBuffer idLot = null;
    private Map<String, APISection> mapSections = new HashMap<String, APISection>();

    private BIMessageLog memoryLog = null;

    private BSession sessionOsiris = null;
    private BTransaction transaction = null;

    public RFModCpt_Normal(boolean isGenererEcritureComptable) throws Exception {
        super(isGenererEcritureComptable);
    }

    private BIMessage comptabiliserDemandeEnTenantCompteDuTypeDeHome(String idCompteAnnexe, String idSectionNormale,
            String idSectionDette, String lieuResidence, Boolean isFRQP, RFOrdreVersementData ov,
            RFPrestationData prestation) throws Exception {

        BIMessage message = null;
        FWCurrency montant = new FWCurrency(ov.getMontantOrdreVersement());

        if (!montant.isZero()) {

            APIRubrique rubriqueNormal = null;

            if (compta != null) {
                if (ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)) {
                    // Ecritures sur la rubrique concernée
                    rubriqueNormal = ARFModuleComptable.getRubriqueEnTenantCompteTypeDeHome(
                            prestation.getTypePrestation(), ARFModuleComptable.TYPE_RUBRIQUE_NORMAL,
                            ov.getIdTypeSoin(), ov.getIdSousTypeSoin(), ov.getIsImportation().booleanValue());

                } else if (ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_RESTITUTION)) {
                    // Ecritures sur la rubrique concernée
                    rubriqueNormal = ARFModuleComptable.getRubriqueEnTenantCompteTypeDeHome(
                            prestation.getTypePrestation(), ARFModuleComptable.TYPE_RUBRIQUE_RESTITUTION,
                            ov.getIdTypeSoin(), ov.getIdSousTypeSoin(), ov.getIsImportation().booleanValue());

                } else if (ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_DETTE)) {
                    rubriqueNormal = ARFModuleComptable.getRubriqueEnTenantCompteTypeDeHome(
                            prestation.getTypePrestation(), ARFModuleComptable.TYPE_RUBRIQUE_COMPENSATION,
                            ov.getIdTypeSoin(), ov.getIdSousTypeSoin(), ov.getIsImportation().booleanValue());

                    BigDecimal montantDette = new BigDecimal(montant.toString()).negate();
                    doEcriture(sessionOsiris, compta, montantDette.toString(), rubriqueNormal, idCompteAnnexe,
                            idSectionDette, dateComptable, null, prestation.getIdDecision());

                }
            }

            // Ecriture normal
            message = doEcriture(sessionOsiris, compta, montant.toString(), rubriqueNormal, idCompteAnnexe,
                    idSectionNormale, dateComptable, null, prestation.getIdDecision());
        }

        return message;

    }

    private BIMessage comptabiliserDemandeSansTenirCompteTypeDeHome(String idCompteAnnexe, String idSectionNormale,
            String idSectionDette, RFOrdreVersementData ov, RFPrestationData prestation) throws Exception {

        BIMessage message = null;
        FWCurrency montant = new FWCurrency(ov.getMontantOrdreVersement());

        if (!montant.isZero()) {

            if (compta != null) {

                APIRubrique rubriqueNormal = null;

                // Ecritures sur la rubrique concernée
                if (ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)) {
                    rubriqueNormal = ARFModuleComptable.getRubriqueSansTenirCompteTypeDeHome(
                            prestation.getTypePrestation(), ARFModuleComptable.TYPE_RUBRIQUE_NORMAL,
                            ov.getIdTypeSoin(), ov.getIsImportation().booleanValue());

                    // Compensation de la dette par défaut si 13.06: maintien à domicile - avances SAS.
                    if (ov.getIdTypeSoin().equals(IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13)
                            && ov.getIdSousTypeSoin().equals(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_AVANCES)) {

                        APIRubrique rubriqueAvancesSas = ARFModuleComptable.getRubriqueSansTenirCompteTypeDeHome(
                                prestation.getTypePrestation(), ARFModuleComptable.TYPE_RUBRIQUE_AVANCE_SAS,
                                ov.getIdTypeSoin(), ov.getIsImportation().booleanValue());

                        BigDecimal montantDetteSas = new BigDecimal(montant.toString()).negate();
                        doEcriture(sessionOsiris, compta, montantDetteSas.toString(), rubriqueAvancesSas,
                                idCompteAnnexe, idSectionNormale, dateComptable, null, prestation.getIdDecision());
                    }

                } else if (ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_RESTITUTION)) {
                    rubriqueNormal = ARFModuleComptable.getRubriqueSansTenirCompteTypeDeHome(
                            prestation.getTypePrestation(), ARFModuleComptable.TYPE_RUBRIQUE_RESTITUTION,
                            ov.getIdTypeSoin(), ov.getIsImportation().booleanValue());
                } else if (ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_DETTE)) {
                    rubriqueNormal = ARFModuleComptable.getRubriqueSansTenirCompteTypeDeHome(
                            prestation.getTypePrestation(), ARFModuleComptable.TYPE_RUBRIQUE_COMPENSATION,
                            ov.getIdTypeSoin(), ov.getIsImportation().booleanValue());

                    BigDecimal montantDette = new BigDecimal(montant.toString()).negate();
                    doEcriture(sessionOsiris, compta, montantDette.toString(), rubriqueNormal, idCompteAnnexe,
                            idSectionDette, dateComptable, null, prestation.getIdDecision());
                }

                // Ecriture normal
                message = doEcriture(sessionOsiris, compta, montant.toString(), rubriqueNormal, idCompteAnnexe,
                        idSectionNormale, dateComptable, null, prestation.getIdDecision());

            } else {
                throw new Exception("RFModCpt_Normal.comptabiliserDemandeSansTenirCompteTypeDeHome(): API compta null");
            }
        }

        return message;

    }

    /**
     * Comptabilise la partie dépassement de qd de l'OV
     * 
     * @param idCompteAnnexe
     * @param idSectionNormale
     * @param ov
     * @throws Exception
     */
    private BIMessage comptabiliserDepassementQD(String idCompteAnnexe, String idSectionNormale,
            RFOrdreVersementData ov, RFPrestationData prestation) throws Exception {

        BIMessage message = null;
        APIRubrique rubriqueDeppassementQd = null;
        boolean isSpas = isSpas(prestation);
        String idTypeSoin = ov.getIdTypeSoin();
        String idSousTypeSoin = ov.getIdSousTypeSoin();

        // Les restitutions s'effectuent sur les mêmes rubriques
        if (ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)
                || ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_RESTITUTION)) {

            if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(prestation.getTypePrestation())) {

                if (IRFTypesDeSoins.CS_FRANCHISE_ET_QUOTEPARTS_17.equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_FRQP_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_FRQP_AI_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_FRAIS_DE_TRAITEMENT_DENTAIRE_15.equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_TRAITEMENT_DENTAIRE_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_TRAITEMENT_DENTAIRE_AI_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13.equals(idTypeSoin)
                        && (IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE.equals(idSousTypeSoin)
                                || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_CONTRAT_DE_TRAVAIL.equals(idSousTypeSoin)
                                || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_SPITEX_OMSV_CMS.equals(idSousTypeSoin) || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UNE_ORGANISATION_OSAD
                                    .equals(idSousTypeSoin))) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_AIDE_AU_MENAGE_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_AIDE_AU_MENAGE_AI_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13.equals(idTypeSoin)
                        && IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC
                                .equals(idSousTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_FRAIS_DE_TRANSPORT_16.equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_FRAIS_DE_TRANSPORT_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_FRAIS_DE_TRANSPORT_AI_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_14.equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_COTISATIONS_PARITAIRES_01.equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_COTISATIONS_PARITAIRES_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_COTISATIONS_PARITAIRES_AI_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_03.equals(idTypeSoin)
                        || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_05.equals(idTypeSoin)
                        || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI_11
                                .equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_MOYEN_AUXILIAIRE_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_MOYEN_AUXILIAIRE_AI_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                        && IRFTypesDeSoins.st_12_SUPPLEMENT_FRAIS_DE_PENSION.equals(idSousTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AI_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                        && IRFTypesDeSoins.st_12_UNITE_D_ACCUEIL_TEMPORAIRE.equals(idSousTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_UNITE_ACCUEIL_TEMPORAIRE_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_UNITE_ACCUEIL_TEMPORAIRE_AI_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                        && IRFTypesDeSoins.st_12_COURT_SEJOUR.equals(idSousTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_PARTICIPATION_COURT_SEJOUR_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_PARTICIPATION_COURT_SEJOUR_AI_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                        && IRFTypesDeSoins.st_12_PENSION_HOME_DE_JOUR_14OMPC.equals(idSousTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_PENSION_HOME_DE_JOUR_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_PENSION_HOME_DE_JOUR_AI_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_REGIME_ALIMENTAIRE_02.equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_REGIME_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_REGIME_AI_SASH;
                    }

                } else {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_TIERS_BENEFICIAIRE_MALADIE_AI_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_TIERS_BENEFICIAIRE_MALADIE_AI_SASH;
                    }
                }

            } else if (IRFGenrePrestations.GENRE_VIEILLESSE.equals(prestation.getTypePrestation())
                    || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(prestation.getTypePrestation())
                    || IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(prestation.getTypePrestation())) {

                if (IRFTypesDeSoins.CS_FRANCHISE_ET_QUOTEPARTS_17.equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_FRQP_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_FRQP_AVS_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_FRAIS_DE_TRAITEMENT_DENTAIRE_15.equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_TRAITEMENT_DENTAIRE_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_TRAITEMENT_DENTAIRE_AVS_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13.equals(idTypeSoin)
                        && (IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE.equals(idSousTypeSoin)
                                || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_CONTRAT_DE_TRAVAIL.equals(idSousTypeSoin)
                                || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_SPITEX_OMSV_CMS.equals(idSousTypeSoin) || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UNE_ORGANISATION_OSAD
                                    .equals(idSousTypeSoin))) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_AIDE_AU_MENAGE_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_AIDE_AU_MENAGE_AVS_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13.equals(idTypeSoin)
                        && IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC
                                .equals(idSousTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_FRAIS_DE_TRANSPORT_16.equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_FRAIS_DE_TRANSPORT_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_FRAIS_DE_TRANSPORT_AVS_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_14.equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_COTISATIONS_PARITAIRES_01.equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_COTISATIONS_PARITAIRES_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_COTISATIONS_PARITAIRES_AVS_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_03.equals(idTypeSoin)
                        || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_05.equals(idTypeSoin)
                        || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI_11
                                .equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_MOYEN_AUXILIAIRE_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_MOYEN_AUXILIAIRE_AVS_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                        && IRFTypesDeSoins.st_12_SUPPLEMENT_FRAIS_DE_PENSION.equals(idSousTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AVS_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                        && IRFTypesDeSoins.st_12_UNITE_D_ACCUEIL_TEMPORAIRE.equals(idSousTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_UNITE_ACCUEIL_TEMPORAIRE_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_UNITE_ACCUEIL_TEMPORAIRE_AVS_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                        && IRFTypesDeSoins.st_12_COURT_SEJOUR.equals(idSousTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_PARTICIPATION_COURT_SEJOUR_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_PARTICIPATION_COURT_SEJOUR_AVS_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                        && IRFTypesDeSoins.st_12_PENSION_HOME_DE_JOUR_14OMPC.equals(idSousTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_PENSION_HOME_DE_JOUR_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_PENSION_HOME_DE_JOUR_AVS_SASH;
                    }

                } else if (IRFTypesDeSoins.CS_REGIME_ALIMENTAIRE_02.equals(idTypeSoin)) {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_REGIME_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_REGIME_AVS_SASH;
                    }

                } else {

                    if (isSpas) {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_TIERS_BENEFICIAIRE_MALADIE_AVS_SPAS;
                    } else {
                        rubriqueDeppassementQd = RFModuleComptableFactory.getInstance().RFM_TIERS_BENEFICIAIRE_MALADIE_AVS_SASH;
                    }
                }
            }
        }

        // écriture du dépassement
        message = doEcriture(sessionOsiris, compta, ov.getMontantDepassementQD(), rubriqueDeppassementQd,
                idCompteAnnexe, idSectionNormale, dateComptable, null, prestation.getIdDecision());

        return message;
    }

    /**
     * Recherche de la section et du compte annexe
     * 
     * @param idTiers
     * @param idExterneRole
     * @param ov
     * @param prestation
     * @param process
     * @return Object[] { compteAnnexe, sectionNormale }
     * @throws Exception
     */
    private Object[] compteAnnexeAndSectionNormale(String idTiers, String idExterneRole, String idPrestation,
            RFComptabiliserDecisionService process) throws Exception {

        if (compta != null) {

            APICompteAnnexe compteAnnexe = compta.getCompteAnnexeByRole(idTiers, IntRole.ROLE_RENTIER, idExterneRole);

            if (compteAnnexe == null) {
                memoryLog.logMessage(sessionOsiris.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"),
                        Integer.toString(JadeBusinessMessageLevels.ERROR),
                        "RFModCpt_Normal:compteAnnexeAndSectionNormaleBeneficiaire");
                throw new Exception(sessionOsiris.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"));
            }

            APISection sectionNormale = retrieveSection(transaction, idPrestation, idExterneRole,
                    compteAnnexe.getIdCompteAnnexe(), APISection.ID_CATEGORIE_SECTION_DECISION_RFM);

            return new Object[] { compteAnnexe, sectionNormale };

        } else {
            memoryLog.logMessage(sessionOsiris.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"),
                    Integer.toString(JadeBusinessMessageLevels.ERROR),
                    "RFModCpt_Normal:compteAnnexeAndSectionNormaleBeneficiaire");
            throw new Exception(sessionOsiris.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"));
        }
    }

    /**
     * Traitement des écritures comptables
     * 
     * Si l'objet compta == null, seul les écritures pour la récap seront effectuées
     * 
     * @throws Exception
     */
    private Object[] compteAnnexeAndSectionRestitution(String idTiers, String idExterneRole, RFOrdreVersementData ov,
            RFPrestationData prestation, RFComptabiliserDecisionService process) throws Exception {

        if (compta != null) {

            APICompteAnnexe compteAnnexe = compta.getCompteAnnexeByRole(idTiers, IntRole.ROLE_RENTIER, idExterneRole);

            if (compteAnnexe == null) {
                memoryLog.logMessage(sessionOsiris.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"),
                        Integer.toString(JadeBusinessMessageLevels.ERROR),
                        "RFModCpt_Normal:compteAnnexeAndSectionRestitution");
                throw new Exception(sessionOsiris.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"));
            }

            APISection sectionNormale = retrieveSection(transaction, ov.getIdOrdreVersement(), idExterneRole,
                    compteAnnexe.getIdCompteAnnexe(), APISection.ID_CATEGORIE_SECTION_RESTITUTIONS);

            return new Object[] { compteAnnexe, sectionNormale };

        } else {
            memoryLog.logMessage(sessionOsiris.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"),
                    Integer.toString(JadeBusinessMessageLevels.ERROR),
                    "RFModCpt_Normal:compteAnnexeAndSectionRestitution");
            throw new Exception(sessionOsiris.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"));
        }
    }

    /**
     * Effectue les écritures comptables en tenant compte de la caisse
     */
    @Override
    public BIMessageLog doTraitement(RFComptabiliserDecisionService process,
            List<RFPrestationData> prestationsMemeAdresseDePaiementList, RFLogToDB rfmLogger, boolean isLotAVASAD)
            throws Exception {

        memoryLog = new FWMemoryLog();
        APISection sectionNormale = null;
        APISection sectionDette = null;
        APICompteAnnexe compteAnnexe = null;
        BigDecimal montantOrdreVersementBigDec = new BigDecimal("0");
        String nss = "";
        String nom = "";
        String prenom = "";
        String refPaiement = "";
        String idTiersAdressePaiement = "";
        String idDomaineApplication = "";
        String idPrestationGroupees = "";
        String idTiersBeneficiairePrincipal = "";
        boolean isBeneficiarePrincipalInitialise = false;
        boolean isAVS = false;
        boolean isAI = false;
        boolean traiterOv = true;
        Set<String> datesPrestations = new HashSet<String>();

        // pour tous les Ovs des prestations de même adresse de paiement et même tiers bénéficiaire
        for (RFPrestationData prestationCourante : prestationsMemeAdresseDePaiementList) {
            if (isLotAVASAD) {
                rfmLogger.logInfoToDB("idPrestation prestation courante: " + prestationCourante.getIdPrestation(),
                        getClass().getName() + " - doTraitement");
            }
            if (RFUtils.TYPE_PC_AVS.equals(RFUtils.getTypePc_AVS_AI(prestationCourante.getTypePrestation()))) {
                isAVS = true;
            } else {
                isAI = true;
            }

            datesPrestations.add(prestationCourante.getDatePrestation());

            if (JadeStringUtil.isBlankOrZero(idPrestationGroupees)) {
                idPrestationGroupees = prestationCourante.getIdPrestation();
            }

            boolean isCompteAnnexeAndSectionRestitution = isCompteAnnexeAndSectionRestitution(prestationCourante);

            for (RFOrdreVersementData ov : prestationCourante.getOrdresVersement()) {
                traiterOv = true;
                if (ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)) {

                    Object[] compteAnnexeSectionNormaleObj = compteAnnexeAndSectionNormale(
                            this.getIdTiersBeneficiairePrincipal(ov, transaction), ov.getNssTiers(),
                            idPrestationGroupees, process);
                    compteAnnexe = (APICompteAnnexe) compteAnnexeSectionNormaleObj[0];
                    sectionNormale = (APISection) compteAnnexeSectionNormaleObj[1];

                    // Mise à jour du montant de l'OV, seulement si le type de soin ne se compense pas par défaut
                    if (!(ov.getIdTypeSoin().equals(IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13) && ov
                            .getIdSousTypeSoin().equals(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_AVANCES))) {
                        montantOrdreVersementBigDec = montantOrdreVersementBigDec.add(new BigDecimal(ov
                                .getMontantOrdreVersement()));
                        montantOrdreVersementBigDec = montantOrdreVersementBigDec.add(new BigDecimal(ov
                                .getMontantDepassementQD()));
                    }

                    if (!isBeneficiarePrincipalInitialise) {
                        nss = ov.getNssTiers();
                        nom = ov.getNomTiers();
                        refPaiement = ov.getRefPaiement();
                        prenom = ov.getPrenomTiers();
                        idTiersAdressePaiement = ov.getIdTiersAdressePaiement();
                        idTiersBeneficiairePrincipal = ov.getIdTiers();
                        idDomaineApplication = ov.getIdDomaineApplication();
                        isBeneficiarePrincipalInitialise = true;
                    }

                } else if (ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_RESTITUTION)) {
                    if (!ov.getIsCompense()) {
                        // Si l'Ov ne doit pas être compensée, on créé l'écriture dans une nouvelle section qui ne fera
                        // pas partie de l'ordre de versement comptable
                        Object[] compteAnnexeSectionNormaleObj = compteAnnexeAndSectionRestitution(
                                this.getIdTiersBeneficiairePrincipal(ov, transaction), ov.getNssTiers(), ov,
                                prestationCourante, process);
                        compteAnnexe = (APICompteAnnexe) compteAnnexeSectionNormaleObj[0];
                        sectionNormale = (APISection) compteAnnexeSectionNormaleObj[1];

                    } else {
                        // Màj du montant de l'ordre de versement
                        montantOrdreVersementBigDec = montantOrdreVersementBigDec.add(new BigDecimal(ov
                                .getMontantOrdreVersement()));
                        montantOrdreVersementBigDec = montantOrdreVersementBigDec.add(new BigDecimal(ov
                                .getMontantDepassementQD()));

                        // On garde le compte et la section du bénéficiare de la prestation
                        if (JadeStringUtil.isBlankOrZero(nss)) {
                            nss = getNss(prestationCourante.getIdTiersBeneficiaire());
                        }

                        Object[] compteAnnexeSectionNormaleObj = null;
                        if (isCompteAnnexeAndSectionRestitution) {
                            compteAnnexeSectionNormaleObj = compteAnnexeAndSectionRestitution(
                                    prestationCourante.getIdTiersBeneficiaire(), nss, ov, prestationCourante, process);
                        } else {
                            compteAnnexeSectionNormaleObj = compteAnnexeAndSectionNormale(
                                    prestationCourante.getIdTiersBeneficiaire(), nss, idPrestationGroupees, process);
                        }
                        compteAnnexe = (APICompteAnnexe) compteAnnexeSectionNormaleObj[0];
                        sectionNormale = (APISection) compteAnnexeSectionNormaleObj[1];
                    }
                } else if (ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_DETTE)) {
                    // Si l'OV n'est pas compensé , on ne fait rien. Sinon on utilise la section de l'OV (voir
                    // RFPreparerDecision-> RFGenererPaiementService) pour l'écriture de la dette
                    if (ov.getIsCompense()) {

                        // On garde le compte et la section du bénéficiare de la prestation
                        if (JadeStringUtil.isBlankOrZero(nss)) {
                            nss = getNss(prestationCourante.getIdTiersBeneficiaire());
                        }

                        Object[] compteAnnexeSectionNormaleObj = compteAnnexeAndSectionNormale(
                                prestationCourante.getIdTiersBeneficiaire(), nss, idPrestationGroupees, process);

                        compteAnnexe = (APICompteAnnexe) compteAnnexeSectionNormaleObj[0];
                        sectionNormale = (APISection) compteAnnexeSectionNormaleObj[1];

                        if (compteAnnexe == null) {
                            memoryLog.logMessage(sessionOsiris.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"),
                                    Integer.toString(JadeBusinessMessageLevels.ERROR),
                                    "RFModCpt_Normal:compteAnnexeAndSectionNormaleBeneficiaire");
                            throw new Exception(sessionOsiris.getLabel("ERREUR_CREATION_COMPTE_ANNEXE"));
                        }

                        sectionDette = new CASection();
                        sectionDette.setISession(getSession());
                        sectionDette.setIdSection(ov.getIdSectionDette());
                        sectionDette.retrieve(getTransaction());

                        if (sectionDette.isNew()) {
                            throw new Exception(
                                    "RFModCpt_Normal.doTraitement(): Impossible de retrouver la section de l'OV N° "
                                            + ov.getIdOrdreVersement() + " (NSS " + ov.getNssTiers() + ")");
                        }

                        montantOrdreVersementBigDec = montantOrdreVersementBigDec.add(new BigDecimal(ov
                                .getMontantOrdreVersement()));
                        montantOrdreVersementBigDec = montantOrdreVersementBigDec.add(new BigDecimal(ov
                                .getMontantDepassementQD()));
                    } else {
                        traiterOv = false;
                    }
                }

                if (traiterOv) {
                    if (process.isAjoutDemandesEnComptabiliteSansTenirCompteTypeDeHome()) {
                        memoryLog.logMessage(comptabiliserDemandeSansTenirCompteTypeDeHome(compteAnnexe
                                .getIdCompteAnnexe(), sectionNormale.getIdSection(), sectionDette == null ? ""
                                : sectionDette.getIdSection(), ov, prestationCourante));
                    } else {

                        if (!JadeStringUtil.isBlankOrZero(ov.getMontantDepassementQD())
                                && (new BigDecimal(ov.getMontantDepassementQD()).compareTo(new BigDecimal("0")) != 0)) {
                            // un OV qui à un dépassement de QD
                            memoryLog.logMessage(comptabiliserDepassementQD(compteAnnexe.getIdCompteAnnexe(),
                                    sectionNormale.getIdSection(), ov, prestationCourante));
                        }

                        memoryLog.logMessage(comptabiliserDemandeEnTenantCompteDuTypeDeHome(compteAnnexe
                                .getIdCompteAnnexe(), sectionNormale.getIdSection(), sectionDette == null ? ""
                                : sectionDette.getIdSection(), "", false, ov, prestationCourante));

                    }
                }
            }
        }

        if (compta == null) {
            return memoryLog;
        }

        // Création de l'ordre de versement
        if (montantOrdreVersementBigDec.compareTo(new BigDecimal("0")) > 0) {

            Object[] compteAnnexeSectionNormaleObj = compteAnnexeAndSectionNormale(idTiersBeneficiairePrincipal, nss,
                    idPrestationGroupees, process);
            compteAnnexe = (APICompteAnnexe) compteAnnexeSectionNormaleObj[0];
            sectionNormale = (APISection) compteAnnexeSectionNormaleObj[1];
            memoryLog.logMessage(preparerOrdreVersement(nss, nom, prenom, idTiersAdressePaiement, idDomaineApplication,
                    isAVS, isAI, datesPrestations, compteAnnexe, sectionNormale, sessionOsiris, transaction, compta,
                    dateComptable, process.getIdOrganeExecution(), montantOrdreVersementBigDec, rfmLogger, isLotAVASAD,
                    refPaiement, idTiersBeneficiairePrincipal));

        }

        return memoryLog;

    }

    public APIGestionComptabiliteExterne getCompta() {
        return compta;
    }

    public String getDateComptable() {
        return dateComptable;
    }

    public StringBuffer getIdLot() {
        return idLot;
    }

    protected String getIdTiersBeneficiairePrincipal(RFOrdreVersementData ordreVersement, BTransaction transaction)
            throws Exception {

        if (JadeStringUtil.isBlankOrZero(ordreVersement.getIdTiers())) {
            memoryLog.logMessage(sessionOsiris.getLabel("PROCESS_VALIDER_DECISION_BENEFICIAIRE_NON_TROUVE")
                    + "idOrdreVersement=" + ordreVersement.getIdOrdreVersement(),
                    Integer.toString(JadeBusinessMessageLevels.ERROR),
                    "RFModCpt_Normal::getIdTiersBeneficiairePrincipal");
            throw new Exception("Aucun bénéficiaire principal trouvé dans les OV pour idOrdreVersement = "
                    + ordreVersement.getIdOrdreVersement());
        } else {
            return ordreVersement.getIdTiers();
        }

    }

    protected String getIdTiersCaisse(RFOrdreVersementData ordreVersement, BTransaction transaction) throws Exception {
        if (JadeStringUtil.isBlankOrZero(ordreVersement.getIdTiers())) {
            memoryLog.logMessage(sessionOsiris.getLabel("PROCESS_VALIDER_DECISION_CAISSE_NON_TROUVE")
                    + "idOrdreVersement=" + ordreVersement.getIdOrdreVersement(),
                    Integer.toString(JadeBusinessMessageLevels.ERROR), "RFModCpt_Normal::getIdTiersCaisse");
            throw new Exception("Aucune caisse trouvé dans les OV pour idOrdreVersement = "
                    + ordreVersement.getIdOrdreVersement());
        } else {
            return ordreVersement.getIdTiers();
        }
    }

    private String getKeySection(String idOrdreVersement, String idExterneRole, String idCategorieSection, String idCA) {
        return idOrdreVersement + "-" + idExterneRole + "-" + idCategorieSection + "-" + idCA;
    }

    private String getNss(String idTiers) throws Exception {

        String nss = PRTiersHelper.getTiersParId(getSession(), idTiers).getNSS();

        if (JadeStringUtil.isBlankOrZero(nss)) {
            throw new Exception("RFModCpt_Normal.doTraitement(): Impossible de retrouver le NSS du tiers n°: "
                    + idTiers);
        }

        return nss;

    }

    @Override
    public int getPriority() {
        return 100;
    }

    public BSession getSession() {
        return sessionOsiris;
    }

    public BTransaction getTransaction() {
        return transaction;
    }

    private boolean isCompteAnnexeAndSectionRestitution(RFPrestationData prestationCourante) {
        BigDecimal montantTotalOvNonCompenseBigDec = new BigDecimal("0");
        for (RFOrdreVersementData ov : prestationCourante.getOrdresVersement()) {
            if (ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)
                    || (ov.getTypeOrdreVersement().equals(IRFOrdresVersements.CS_TYPE_RESTITUTION) && ov
                            .getIsCompense())) {
                montantTotalOvNonCompenseBigDec = montantTotalOvNonCompenseBigDec.add(new BigDecimal(ov
                        .getMontantOrdreVersement()));
                montantTotalOvNonCompenseBigDec = montantTotalOvNonCompenseBigDec.add(new BigDecimal(ov
                        .getMontantDepassementQD()));
            }
        }

        return montantTotalOvNonCompenseBigDec.compareTo(new BigDecimal("0")) == -1;
    }

    private boolean isSpas(RFPrestationData prestation) {
        return prestation.getRemboursementRequerant().equals(IRFQd.CS_TYPE_REMBOURSEMENT_SPAS);
    }

    private BIMessage preparerOrdreVersement(String nss, String nom, String prenom, String idTiersAdressePaiement,
            String idDomaineApplication, boolean isAVS, boolean isAI, Set<String> datesPrestations,
            APICompteAnnexe compteAnnexe, APISection sectionNormale, BSession session, BTransaction transaction,
            APIGestionComptabiliteExterne compta, String dateComptable, String idOrganeExecution,
            BigDecimal montantOrdreDeVersementBigDec, RFLogToDB rfmLogger, boolean isLotAVASAD, String refPaiement,
            final String idTiersBeneficiairePrincipal) throws Exception {

        String idTiersPrincipal = idTiersAdressePaiement;

        if (JadeStringUtil.isBlankOrZero(idTiersPrincipal)) {
            idTiersPrincipal = idTiersBeneficiairePrincipal;
        }

        String isoLangFromIdTiers = PRTiersHelper.getIsoLangFromIdTiers(getSession(), idTiersPrincipal);

        String motifVersement = getMotifVersement(session, nss, nom, prenom, refPaiement, isAVS, isAI,
                datesPrestations, isoLangFromIdTiers);
        TIAdressePaiementData adrPaiementData = loadAdressePaiement(session, transaction, dateComptable,
                idTiersAdressePaiement, idDomaineApplication);
        if (isLotAVASAD) {
            String logSource = this.getClass().getName() + " - preparerOrdreVersement";
            rfmLogger.logInfoToDB("idAdressePaiement: " + adrPaiementData.getIdAdressePaiement(), logSource);
            rfmLogger.logInfoToDB("montant: " + montantOrdreDeVersementBigDec.toString(), logSource);
        }
        ErrorDriller ed = new ErrorDriller().lookInThreadContext().add(session);
        List<DrilledError> errors = ed.drill();
        if (!errors.isEmpty()) {
            // oups, des méchantes erreurs... on les log et on fait tout péter, pour éviter des données incohérentes
            // plus loin
            String exceptionMessage = "ErrorDriller: errors found: " + errors.size();
            JadeLogger.error(this, exceptionMessage);
            for (DrilledError e : errors) {
                JadeLogger.error(this, e.toString());
            }
            throw new Exception("RFModCpt_Normal.preparerOrdreVersement before doOV. " + exceptionMessage, null);
        }
        BIMessage message = doOrdreVersement(session, compta, compteAnnexe.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), montantOrdreDeVersementBigDec.toString(),
                adrPaiementData.getIdAvoirPaiementUnique(), motifVersement, dateComptable, false, idOrganeExecution);
        errors = ed.drill();
        if (!errors.isEmpty()) {
            // oups, des méchantes erreurs... on les log et on fait tout péter, pour éviter des données incohérentes
            // plus loin
            String exceptionMessage = "ErrorDriller: errors found: " + errors.size();
            JadeLogger.error(this, exceptionMessage);
            for (DrilledError e : errors) {
                JadeLogger.error(this, e.toString());
            }
            throw new Exception("RFModCpt_Normal.preparerOrdreVersement after doOV. " + exceptionMessage, null);
        }
        return message;
    }

    private APISection retrieveSection(BTransaction transaction, String idPrestation, String idExterneRole,
            String idCompteAnnexe, String idCategorieSection) throws Exception {

        if (mapSections.containsKey(getKeySection(idPrestation, idExterneRole, idCategorieSection, idCompteAnnexe))) {

            return mapSections.get(getKeySection(idPrestation, idExterneRole, idCategorieSection, idCompteAnnexe));
        } else {
            String typeSection = null;
            if (APISection.ID_CATEGORIE_SECTION_DECISION_RFM.equals(idCategorieSection)
                    || APISection.ID_CATEGORIE_SECTION_DECISION_REGIME.equals(idCategorieSection)) {
                typeSection = APISection.ID_TYPE_SECTION_RFM;
            } else if (APISection.ID_CATEGORIE_SECTION_RESTITUTIONS.equals(idCategorieSection)) {
                typeSection = APISection.ID_TYPE_SECTION_RESTITUTION;
            }

            else {
                memoryLog.logMessage(sessionOsiris.getLabel("PROCESS_VALIDER_DECISION_CATEGORIE_SECTION_INCONNUE")
                        + idCategorieSection, new Integer(JadeBusinessMessageLevels.ERROR).toString(),
                        "RFModCpt_Normal::retrieveSection");

                throw new Exception("Unsupported idCategorieSection : " + idCategorieSection);
            }

            // on créé un numero de facture unique qui servira a creer la
            // section
            String noFacture = CAUtil
                    .creerNumeroSectionUnique(getSession(), transaction, IntRole.ROLE_RENTIER, idExterneRole,
                            typeSection, String.valueOf(new JADate(dateComptable).getYear()), idCategorieSection);

            APISection section = compta.getSectionByIdExterne(idCompteAnnexe, typeSection, noFacture,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, null);

            mapSections.put(getKeySection(idPrestation, idExterneRole, idCategorieSection, idCompteAnnexe), section);
            return section;
        }
    }

    public void setCompta(APIGestionComptabiliteExterne compta) {
        this.compta = compta;
    }

    public void setDateComptable(String dateComptable) {
        this.dateComptable = dateComptable;
    }

    public void setIdLot(StringBuffer idLot) {
        this.idLot = idLot;
    }

    public void setSession(BSession session) {
        sessionOsiris = session;
    }

    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }

}