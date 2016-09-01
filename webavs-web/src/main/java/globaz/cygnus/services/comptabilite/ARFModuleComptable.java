package globaz.cygnus.services.comptabilite;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.prestationsaccordees.IRFGenrePrestations;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.Comparator;
import java.util.Set;
import ch.globaz.common.util.prestations.MotifVersementUtil;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;

/**
 * @author fha
 */
public class ARFModuleComptable implements Comparator {

    public static final int TYPE_RUBRIQUE_AVANCE_SAS = 600;
    public static final int TYPE_RUBRIQUE_COMPENSATION = 500;
    public static final int TYPE_RUBRIQUE_DSAS = 1100;
    public static final int TYPE_RUBRIQUE_NORMAL = 1000;
    public static final int TYPE_RUBRIQUE_RESTITUTION = 1400;
    public static final int TYPE_RUBRIQUE_RETROACTIF = 1200;

    public static String getLibelleRubrique(BSession session, String typePrestation, String isoLangue) throws Exception {
        String labelId = "";

        if (IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(typePrestation)
                || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(typePrestation)) {
            labelId = "PROCESS_COMPTABILISER_RFM_AVS";
        } else if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(typePrestation)) {
            labelId = "PROCESS_COMPTABILISER_RFM_AI";
        } else {
            throw new Exception("ARFModuleComptable::getLibelleRubrique: genrePrestation null ou inconnu : "
                    + typePrestation);
        }

        return MotifVersementUtil.getTranslatedLabelFromIsolangue(isoLangue, labelId, session);
    }

    /**
     * Retourne la rubrique concernée
     * 
     * @return la rubrique concernée, ou null si non trouvé
     */
    public static APIRubrique getRubriqueEnTenantCompteTypeDeHome(String typePrestation, int typeRubrique,
            String idTypeSoin, String idSousTypeSoin, boolean isImportation) throws Exception {

        if (JadeStringUtil.isEmpty(typePrestation) || JadeStringUtil.isEmpty(idTypeSoin)) {
            throw new IllegalArgumentException(
                    "ARFModuleComptable::getRubriqueEnTenantCompteTypeDeHome:paramètres passées à la fonction incorrectes (null) : "
                            + typePrestation);
        }

        switch (typeRubrique) {

            case TYPE_RUBRIQUE_NORMAL:

                if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(typePrestation)) {

                    if (IRFTypesDeSoins.CS_FRANCHISE_ET_QUOTEPARTS_17.equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_FRQP_AI;

                    } else if (IRFTypesDeSoins.CS_FRAIS_DE_TRAITEMENT_DENTAIRE_15.equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_TRAITEMENT_DENTAIRE_AI;

                    } else if (IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13.equals(idTypeSoin)
                            && (IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE.equals(idSousTypeSoin)
                                    || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_CONTRAT_DE_TRAVAIL
                                            .equals(idSousTypeSoin)
                                    || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_SPITEX_OMSV_CMS.equals(idSousTypeSoin) || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UNE_ORGANISATION_OSAD
                                        .equals(idSousTypeSoin))) {

                        return RFModuleComptableFactory.getInstance().RFM_AIDE_AU_MENAGE_AI;

                    } else if (IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13.equals(idTypeSoin)
                            && IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC
                                    .equals(idSousTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI;

                    } else if (IRFTypesDeSoins.CS_FRAIS_DE_TRANSPORT_16.equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_FRAIS_DE_TRANSPORT_AI;

                    } else if (IRFTypesDeSoins.CS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_14.equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI;

                    } else if (IRFTypesDeSoins.CS_COTISATIONS_PARITAIRES_01.equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_COTISATIONS_PARITAIRES_AI;

                    } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                            && IRFTypesDeSoins.st_12_SUPPLEMENT_FRAIS_DE_PENSION.equals(idSousTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AI_SPAS;

                    } else if (IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_03.equals(idTypeSoin)
                            || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_05.equals(idTypeSoin)
                            || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI_11
                                    .equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_MOYEN_AUXILIAIRE_AI;

                    } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                            && IRFTypesDeSoins.st_12_UNITE_D_ACCUEIL_TEMPORAIRE.equals(idSousTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_UNITE_ACCUEIL_TEMPORAIRE_AI;

                    } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                            && IRFTypesDeSoins.st_12_COURT_SEJOUR.equals(idSousTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_PARTICIPATION_COURT_SEJOUR_AI;

                    } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                            && IRFTypesDeSoins.st_12_PENSION_HOME_DE_JOUR_14OMPC.equals(idSousTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_PENSION_HOME_DE_JOUR_AI;

                    } else if (IRFTypesDeSoins.CS_REGIME_ALIMENTAIRE_02.equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_REGIME_AI;

                    } else {
                        return RFModuleComptableFactory.getInstance().RFM_TIERS_BENEFICIAIRE_MALADIE_AI;
                    }

                } else {

                    if (IRFGenrePrestations.GENRE_VIEILLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(typePrestation)) {

                        if (IRFTypesDeSoins.CS_FRANCHISE_ET_QUOTEPARTS_17.equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_FRQP_AVS;

                        } else if (IRFTypesDeSoins.CS_FRAIS_DE_TRAITEMENT_DENTAIRE_15.equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_TRAITEMENT_DENTAIRE_AVS;

                        } else if (IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13.equals(idTypeSoin)
                                && (IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE.equals(idSousTypeSoin)
                                        || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_CONTRAT_DE_TRAVAIL
                                                .equals(idSousTypeSoin)
                                        || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_SPITEX_OMSV_CMS
                                                .equals(idSousTypeSoin) || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UNE_ORGANISATION_OSAD
                                            .equals(idSousTypeSoin))) {

                            return RFModuleComptableFactory.getInstance().RFM_AIDE_AU_MENAGE_AVS;

                        } else if (IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13.equals(idTypeSoin)
                                && IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC
                                        .equals(idSousTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS;

                        } else if (IRFTypesDeSoins.CS_FRAIS_DE_TRANSPORT_16.equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_FRAIS_DE_TRANSPORT_AVS;

                        } else if (IRFTypesDeSoins.CS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_14.equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS;

                        } else if (IRFTypesDeSoins.CS_COTISATIONS_PARITAIRES_01.equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_COTISATIONS_PARITAIRES_AVS;

                        } else if (IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_03.equals(idTypeSoin)
                                || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_05.equals(idTypeSoin)
                                || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI_11
                                        .equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_MOYEN_AUXILIAIRE_AVS;

                        } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                                && IRFTypesDeSoins.st_12_SUPPLEMENT_FRAIS_DE_PENSION.equals(idSousTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_FRAIS_DE_PENSION_COURT_SEJOUR_AVS;

                        } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                                && IRFTypesDeSoins.st_12_UNITE_D_ACCUEIL_TEMPORAIRE.equals(idSousTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_UNITE_ACCUEIL_TEMPORAIRE_AVS;

                        } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                                && IRFTypesDeSoins.st_12_COURT_SEJOUR.equals(idSousTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_PARTICIPATION_COURT_SEJOUR_AVS;

                        } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                                && IRFTypesDeSoins.st_12_PENSION_HOME_DE_JOUR_14OMPC.equals(idSousTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_PENSION_HOME_DE_JOUR_AVS;

                        } else if (IRFTypesDeSoins.CS_REGIME_ALIMENTAIRE_02.equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_REGIME_AVS;

                        } else {
                            return RFModuleComptableFactory.getInstance().RFM_TIERS_BENEFICIAIRE_MALADIE_AVS;
                        }

                    } else {

                        throw new Exception(
                                "ARFModuleComptable::getRubriqueEnTenantCompteTypeDeHome:genre de prestation inconnu : "
                                        + typePrestation);

                    }
                }

            case TYPE_RUBRIQUE_RESTITUTION:

                if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(typePrestation)) {

                    if (IRFTypesDeSoins.CS_FRANCHISE_ET_QUOTEPARTS_17.equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_FRQP_AI;

                    } else if (IRFTypesDeSoins.CS_FRAIS_DE_TRAITEMENT_DENTAIRE_15.equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_TRAITMENT_DENTAIRE_AI;

                    } else if (IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13.equals(idTypeSoin)
                            && (IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE.equals(idSousTypeSoin)
                                    || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_CONTRAT_DE_TRAVAIL
                                            .equals(idSousTypeSoin)
                                    || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_SPITEX_OMSV_CMS.equals(idSousTypeSoin) || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UNE_ORGANISATION_OSAD
                                        .equals(idSousTypeSoin))) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_AIDE_AU_MENAGE_AI;

                    } else if (IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13.equals(idTypeSoin)
                            && IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC
                                    .equals(idSousTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AI;

                    } else if (IRFTypesDeSoins.CS_FRAIS_DE_TRANSPORT_16.equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_FRAIS_DE_TRANSPORT_AI;

                    } else if (IRFTypesDeSoins.CS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_14.equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AI;

                    } else if (IRFTypesDeSoins.CS_COTISATIONS_PARITAIRES_01.equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_COTISATIONS_PARITAIRES_AI;

                    } else if (IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_03.equals(idTypeSoin)
                            || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_05.equals(idTypeSoin)
                            || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI_11
                                    .equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_MOYENS_AUXILIAIRES_AI;

                    } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                            && IRFTypesDeSoins.st_12_SUPPLEMENT_FRAIS_DE_PENSION.equals(idSousTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_FRAIS_DE_PENSION_COURT_SEJOUR_AI;

                    } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                            && IRFTypesDeSoins.st_12_UNITE_D_ACCUEIL_TEMPORAIRE.equals(idSousTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_UNITE_ACCUEIL_TEMPORAIRE_AI;

                    } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                            && IRFTypesDeSoins.st_12_COURT_SEJOUR.equals(idSousTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_PARTICIPATION_COURT_SEJOUR_AI;

                    } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                            && IRFTypesDeSoins.st_12_PENSION_HOME_DE_JOUR_14OMPC.equals(idSousTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_PENSION_HOME_DE_JOUR_AI;

                    } else if (IRFTypesDeSoins.CS_REGIME_ALIMENTAIRE_02.equals(idTypeSoin)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_REGIME_AI;

                    } else {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_AI;

                    }
                } else {
                    if (IRFGenrePrestations.GENRE_VIEILLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(typePrestation)) {

                        if (IRFTypesDeSoins.CS_FRANCHISE_ET_QUOTEPARTS_17.equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_FRQP_AVS;

                        } else if (IRFTypesDeSoins.CS_FRAIS_DE_TRAITEMENT_DENTAIRE_15.equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_TRAITMENT_DENTAIRE_AVS;

                        } else if (IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13.equals(idTypeSoin)
                                && (IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE.equals(idSousTypeSoin)
                                        || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_CONTRAT_DE_TRAVAIL
                                                .equals(idSousTypeSoin)
                                        || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_SPITEX_OMSV_CMS
                                                .equals(idSousTypeSoin) || IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UNE_ORGANISATION_OSAD
                                            .equals(idSousTypeSoin))) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_AIDE_AU_MENAGE_AVS;

                        } else if (IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13.equals(idTypeSoin)
                                && IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC
                                        .equals(idSousTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_AVS;

                        } else if (IRFTypesDeSoins.CS_FRAIS_DE_TRANSPORT_16.equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_FRAIS_DE_TRANSPORT_AVS;

                        } else if (IRFTypesDeSoins.CS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_14.equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_AVS;

                        } else if (IRFTypesDeSoins.CS_COTISATIONS_PARITAIRES_01.equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_COTISATIONS_PARITAIRES_AVS;

                        } else if (IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_03.equals(idTypeSoin)
                                || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_05.equals(idTypeSoin)
                                || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI_11
                                        .equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_MOYENS_AUXILIAIRES_AVS;

                        } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                                && IRFTypesDeSoins.st_12_SUPPLEMENT_FRAIS_DE_PENSION.equals(idSousTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_FRAIS_DE_PENSION_COURT_SEJOUR_AVS;

                        } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                                && IRFTypesDeSoins.st_12_UNITE_D_ACCUEIL_TEMPORAIRE.equals(idSousTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_UNITE_ACCUEIL_TEMPORAIRE_AVS;

                        } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                                && IRFTypesDeSoins.st_12_COURT_SEJOUR.equals(idSousTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_PARTICIPATION_COURT_SEJOUR_AVS;

                        } else if (IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12.equals(idTypeSoin)
                                && IRFTypesDeSoins.st_12_PENSION_HOME_DE_JOUR_14OMPC.equals(idSousTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_PENSION_HOME_DE_JOUR_AVS;

                        } else if (IRFTypesDeSoins.CS_REGIME_ALIMENTAIRE_02.equals(idTypeSoin)) {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_REGIME_AVS;

                        } else {

                            return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_AVS;

                        }
                    } else {

                        throw new Exception(
                                "ARFModuleComptable::getRubriqueEnTenantCompteTypeDeHome:genre de prestation inconnu : "
                                        + typePrestation);

                    }
                }

            case TYPE_RUBRIQUE_COMPENSATION:

                if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(typePrestation)) {

                    return RFModuleComptableFactory.getInstance().RFM_COMPENSATION;
                } else {
                    if (IRFGenrePrestations.GENRE_VIEILLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(typePrestation)) {

                        return RFModuleComptableFactory.getInstance().RFM_COMPENSATION;

                    } else {

                        throw new Exception("ARFModuleComptable::getRubrique:genre de prestation inconnu : "
                                + typePrestation);

                    }
                }

            default:
                throw new Exception(
                        "ARFModuleComptable::getRubriqueEnTenantCompteTypeDeHome:Type de rubrique inconnu : "
                                + typeRubrique);
        }
    }

    /**
     * Retourne la rubrique concernée
     * 
     * @return la rubrique concernée, ou null si non trouvé
     */
    public static APIRubrique getRubriqueSansTenirCompteTypeDeHome(String typePrestation, int typeRubrique,
            String idTypeSoin, boolean isImportation) throws Exception {

        if (JadeStringUtil.isEmpty(typePrestation) || JadeStringUtil.isEmpty(idTypeSoin)) {
            throw new Exception(
                    "ARFModuleComptable::getRubriqueSansTenirCompteTypeDeHome:paramètres passées à la fonction incorrectes (null) : "
                            + typePrestation);
        }

        switch (typeRubrique) {

            case TYPE_RUBRIQUE_NORMAL:

                if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(typePrestation)) {

                    if (IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_03.equals(idTypeSoin)
                            || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_05.equals(idTypeSoin)
                            || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI_11
                                    .equals(idTypeSoin)) {
                        return RFModuleComptableFactory.getInstance().RFM_MOYEN_AUXILIAIRE_AI;
                    } else {
                        if (IRFTypesDeSoins.CS_FINANCEMENT_DES_SOINS_20.equals(idTypeSoin)) {
                            if (!isImportation) {
                                return RFModuleComptableFactory.getInstance().RFM_CCJU_TIERS_FINANCEMENT_SOINS_AI;
                            } else {
                                return RFModuleComptableFactory.getInstance().RFM_CCJU_TIERS_FINANCEMENT_SOINS_AI_IMPORTATION;
                            }
                        } else {
                            return RFModuleComptableFactory.getInstance().RFM_TIERS_BENEFICIAIRE_MALADIE_AI;
                        }
                    }

                } else {

                    if (IRFGenrePrestations.GENRE_VIEILLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(typePrestation)) {

                        if (IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_03.equals(idTypeSoin)
                                || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_05.equals(idTypeSoin)
                                || IRFTypesDeSoins.CS_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI_11
                                        .equals(idTypeSoin)) {
                            return RFModuleComptableFactory.getInstance().RFM_MOYEN_AUXILIAIRE_AVS;
                        } else {
                            if (IRFTypesDeSoins.CS_FINANCEMENT_DES_SOINS_20.equals(idTypeSoin)) {
                                if (!isImportation) {
                                    return RFModuleComptableFactory.getInstance().RFM_CCJU_TIERS_FINANCEMENT_SOINS_AVS;
                                } else {
                                    return RFModuleComptableFactory.getInstance().RFM_CCJU_TIERS_FINANCEMENT_SOINS_AVS_IMPORTATION;
                                }
                            } else {
                                return RFModuleComptableFactory.getInstance().RFM_TIERS_BENEFICIAIRE_MALADIE_AVS;
                            }
                        }

                    } else {

                        throw new Exception(
                                "ARFModuleComptable::getRubriqueSansTenirCompteTypeDeHome:genre de prestation inconnu : "
                                        + typePrestation);

                    }
                }

            case TYPE_RUBRIQUE_RESTITUTION:

                if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(typePrestation)) {

                    return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_AI;
                } else {
                    if (IRFGenrePrestations.GENRE_VIEILLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(typePrestation)) {

                        return RFModuleComptableFactory.getInstance().RFM_RESTITUTION_AVS;

                    } else {

                        throw new Exception(
                                "ARFModuleComptable::getRubriqueSansTenirCompteTypeDeHome:genre de prestation inconnu : "
                                        + typePrestation);

                    }
                }

            case TYPE_RUBRIQUE_COMPENSATION:

                if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(typePrestation)) {

                    return RFModuleComptableFactory.getInstance().RFM_COMPENSATION;
                } else {
                    if (IRFGenrePrestations.GENRE_VIEILLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(typePrestation)) {

                        return RFModuleComptableFactory.getInstance().RFM_COMPENSATION;

                    } else {

                        throw new Exception("ARFModuleComptable::getRubrique:genre de prestation inconnu : "
                                + typePrestation);

                    }
                }

            case TYPE_RUBRIQUE_AVANCE_SAS:

                if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(typePrestation)) {

                    return RFModuleComptableFactory.getInstance().RFM_CCJU_AVANCE_SAS;
                } else {
                    if (IRFGenrePrestations.GENRE_VIEILLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(typePrestation)
                            || IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(typePrestation)) {

                        return RFModuleComptableFactory.getInstance().RFM_CCJU_AVANCE_SAS;

                    } else {

                        throw new Exception("ARFModuleComptable::getRubrique:genre de prestation inconnu : "
                                + typePrestation);

                    }
                }

            default:
                throw new Exception(
                        "ARFModuleComptable::getRubriqueSansTenirCompteTypeDeHome:Type de rubrique inconnu : "
                                + typeRubrique);
        }

    }

    protected boolean isGenererEcritureComptable = false;

    public ARFModuleComptable(boolean isGenererEcritureComptable) throws Exception {
        this.isGenererEcritureComptable = isGenererEcritureComptable;
    }

    @Override
    public int compare(Object o1, Object o2) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * écrit une écriture en compta. Ne fait rien si le montant est nul.
     * 
     * @param session
     *            session orisis
     * @param compta
     *            une instance de APIProcessComptabilisation
     * @param montantSigne
     *            Le montant a écrire, signé
     * @param rubrique
     *            l'id de la rubrique
     * @param idCompteAnnexe
     *            l'id du compta annexe
     * @param idSection
     *            l'id de la section
     * @param dateComptable
     *            date valeur comptable
     * @param libelle
     *            libelle
     */
    protected BIMessage doEcriture(BSession session, APIGestionComptabiliteExterne compta, String montantSigne,
            APIRubrique rubrique, String idCompteAnnexe, String idSection, String dateComptable, String libelle,
            String idDecision) {

        FWMemoryLog log = new FWMemoryLog();
        if (compta == null) {
            log.logMessage("Aucune écriture comptable. APIGestionComptabiliteExterne non instancié (1).",
                    FWViewBeanInterface.WARNING, this.getClass().getName());
        }
        if ((compta.getJournal() == null) || compta.getJournal().isNew()) {
            compta.createJournal();
        }

        if (/* this.isGenererEcritureComptable && */!JadeStringUtil.isDecimalEmpty(montantSigne)) {
            FWCurrency montant = new FWCurrency(montantSigne);
            boolean positif = true;

            if (montant.isNegative()) {
                montant.negate();
                positif = false;
            }

            APIEcriture ecriture = compta.createEcriture();
            ecriture.setIdCompteAnnexe(idCompteAnnexe);
            ecriture.setIdSection(idSection);
            ecriture.setDate(dateComptable);
            ecriture.setIdCompte(rubrique.getIdRubrique());
            ecriture.setMontant(montant.toString());
            if (libelle != null) {
                ecriture.setLibelle(libelle);
            }

            log.logMessage("Ecriture " + rubrique + session.getLabel("MONTANT_ARE") + montantSigne.toString(),
                    FWMessage.INFORMATION, this.getClass().getName());

            if (positif) {
                ecriture.setCodeDebitCredit(APIEcriture.CREDIT);
            } else {
                ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
            }
            compta.addOperation(ecriture);

        } else {
            log.logMessage(session.getLabel("ERREUR_AUCUNE_ECRITURE_GENEREE_MNT") + montantSigne.toString(),
                    FWMessage.INFORMATION, this.getClass().getName());

        }
        return log.getMessage(0);
    }

    /**
     * Effectue un ordre de versement, lance une Exception si le montant est négatif
     * 
     * @param compta
     *            DOCUMENT ME!
     * @param idCompteAnnexe
     *            DOCUMENT ME!
     * @param idSection
     *            DOCUMENT ME!
     * @param montant
     *            DOCUMENT ME!
     * @param idAdressePaiement
     *            DOCUMENT ME!
     * @param nssRequerant
     *            String, NSS du requérant principal. Cette valeur peut être null.
     * 
     * @throws Exception
     *             Si le montant est négatif
     * @throws IllegalArgumentException
     *             DOCUMENT ME!
     */
    protected BIMessage doOrdreVersement(BSession session, APIGestionComptabiliteExterne compta, String idCompteAnnexe,
            String idSection, String montant, String idAdressePaiement, String motifVersement, String dateComptable,
            boolean isAvance, String idOrganeExecution) throws Exception {

        FWMemoryLog log = new FWMemoryLog();

        if (compta == null) {
            log.logMessage("Aucune écriture comptable. APIGestionComptabiliteExterne non instancié (2).",
                    FWViewBeanInterface.WARNING, this.getClass().getName());
        }

        if (new FWCurrency(montant).isNegative()) {
            throw new IllegalArgumentException(session.getLabel("MONTANT_NEG_NON_ACCEPTE_POUR_OV"));
        }

        if (new FWCurrency(montant).isZero()) {
            log.logMessage(session.getLabel("PAS_OV_POUR_MNT_ZERO") + motifVersement, FWMessage.INFORMATION, this
                    .getClass().getName());
            return log.getMessage(0);
        }

        APIOperationOrdreVersement ordreVersement = compta.createOperationOrdreVersement();
        ordreVersement.setIdAdressePaiement(idAdressePaiement);
        ordreVersement.setDate(dateComptable);
        ordreVersement.setIdCompteAnnexe(idCompteAnnexe);
        ordreVersement.setIdSection(idSection);
        ordreVersement.setMontant(montant);
        if (isAvance) {
            ordreVersement.setIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENTAVANCE);
        }
        ordreVersement.setCodeISOMonnaieBonification(session
                .getCode(IPRConstantesExternes.OSIRIS_CS_CODE_ISO_MONNAIE_CHF));
        ordreVersement.setCodeISOMonnaieDepot(session.getCode(IPRConstantesExternes.OSIRIS_CS_CODE_ISO_MONNAIE_CHF));
        ordreVersement.setTypeVirement(APIOperationOrdreVersement.VIREMENT);
        ordreVersement.setIdOrganeExecution(idOrganeExecution);

        log.logMessage(session.getLabel("OV_MNT") + montant.toString() + " idAdrPmt = " + idAdressePaiement,
                FWMessage.INFORMATION, this.getClass().getName());

        ordreVersement.setNatureOrdre(CAOrdreGroupe.NATURE_RENTES_AVS_AI);
        ordreVersement.setMotif(motifVersement);
        compta.addOperation(ordreVersement);

        return log.getMessage(0);
    }

    protected String getIdTiersBeneficiairePrincipal(REDecisionEntity decision, BTransaction transaction)
            throws Exception {

        if (JadeStringUtil.isBlankOrZero(decision.getIdTiersBeneficiairePrincipal())) {
            throw new Exception("Aucun bénéficiaire principal trouvé dans les OV pour idDecision = "
                    + decision.getIdDecision());
        } else {
            return decision.getIdTiersBeneficiairePrincipal();
        }

    }

    protected String getMotifVersement(BSession session, String nss, String nom, String prenom, String refPmt,
            boolean isAVS, boolean isAI, Set<String> datesPrestations, final String isoLangue) throws Exception {

        final String nomPrenom = nom + " " + prenom;
        final StringBuilder genrePrestation = new StringBuilder();

        if (isAVS) {
            genrePrestation.append(ARFModuleComptable.getLibelleRubrique(session, IPCPCAccordee.CS_TYPE_PC_SURVIVANT,
                    isoLangue));
        }

        if (isAI) {
            genrePrestation.append(isAVS ? "/" : " ");
            genrePrestation.append(ARFModuleComptable.getLibelleRubrique(session, IPCPCAccordee.CS_TYPE_PC_INVALIDITE,
                    isoLangue));
        }

        final StringBuilder msgDecision = new StringBuilder(MotifVersementUtil.getTranslatedLabelFromIsolangue(
                isoLangue, "PMT_MENS_DECISION_DU", session));

        boolean premierPassage = true;
        for (String dateCourante : datesPrestations) {
            if (!premierPassage) {
                msgDecision.append(", ");
            } else {
                msgDecision.append(" ");
            }

            msgDecision.append(dateCourante);
            premierPassage = false;
        }

        return MotifVersementUtil.formatDecision(nss, nomPrenom, refPmt.trim(), genrePrestation.toString(), "",
                msgDecision.toString());
    }

    /**
     * charge l'adresse de paiement.
     * 
     * @return une adresse de paiement ou null.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public TIAdressePaiementData loadAdressePaiement(BSession session, BTransaction transaction,
            String dateValeurCompta, String idTiersAdressePaiement, String idDomaine) throws Exception {

        TIAdressePaiementData retValue = PRTiersHelper.getAdressePaiementData(session, transaction,
                idTiersAdressePaiement, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", dateValeurCompta);

        return retValue;
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }
}
