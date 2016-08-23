package globaz.corvus.module.compta;

import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.recap.IRERecap;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.recap.access.RERecapInfo;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRente;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.enumere.genre.prestations.REGenrePrestationEnum;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.prestation.enums.codeprestation.PRCodePrestationRFM;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.utils.compta.PRRubriqueComptableResolver;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import ch.globaz.common.util.prestations.MotifVersementUtil;
import ch.globaz.corvus.domaine.Decision;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Module comptable de gestion des écritures comptable Traitement des cas écriture comptable pour le mois en cours, 1
 * seul bénéficiaire : l'assuré principal. Cas #21
 * 
 * @author : scr
 */
public abstract class AREModuleComptable implements Comparator<IREModuleComptable> {

    class RenteAccordeeInfo {
        public FWCurrency montantCourant = new FWCurrency();
        public List raEnCours = new ArrayList();
    }

    public static final int TYPE_RUBRIQUE_COMPENSATION = 500;
    public static final int TYPE_RUBRIQUE_EXTOURNE = 300;
    public static final int TYPE_RUBRIQUE_NORMAL = 100;
    public static final int TYPE_RUBRIQUE_RESTITUTION = 400;
    public static final int TYPE_RUBRIQUE_RETROACTIF = 200;

    /**
     * Retourne le code récap en fonction du genre de rente accordée
     * 
     * @return le code récap
     */
    protected synchronized static int getCodeRecap(final String genrePrestation, final int genreRecap) throws Exception {

        if (!REGenrePrestationEnum.groupe1.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe2.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe4.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe5.contains(genrePrestation)) {

            throw new Exception("Le genre de prestation est inconnu : " + genrePrestation);
        }

        // RO AVS
        if (REGenresPrestations.GENRE_10.equals(genrePrestation)
                || REGenresPrestations.GENRE_13.equals(genrePrestation)
                || REGenresPrestations.GENRE_14.equals(genrePrestation)
                || REGenresPrestations.GENRE_15.equals(genrePrestation)
                || REGenresPrestations.GENRE_16.equals(genrePrestation)
                || REGenresPrestations.GENRE_33.equals(genrePrestation)
                || REGenresPrestations.GENRE_34.equals(genrePrestation)
                || REGenresPrestations.GENRE_35.equals(genrePrestation)
                || REGenresPrestations.GENRE_36.equals(genrePrestation)) {

            switch (genreRecap) {
                case IRERecap.GENRE_RECAP_REC_FIN_MOIS_PRECEDENT:
                    return IRERecap.CODE_RENTE_EN_COURS_MOIS_PRECEDENT_AVS_RO;

                case IRERecap.GENRE_RECAP_AUGMENTATION:
                    return IRERecap.CODE_AUGMENTATION_MOIS_RAPPORT_AVS_RO;

                case IRERecap.GENRE_RECAP_ADAPTATION_BISANNUEL:
                    return IRERecap.CODE_AUGMENTATION_BISANNUELLE_AVS_RO;

                case IRERecap.GENRE_RECAP_DIMINUTION:
                    return IRERecap.CODE_DIMINUTION_MOIS_PRECEDENT_AVS_RO;

                case IRERecap.GENRE_RECAP_PMT_RETROACTIF:
                    return IRERecap.CODE_PAIEMENT_RETRO_AVS_RO;

                case IRERecap.GENRE_RECAP_ALLOC_UNIQUE_VEUVE:
                    return IRERecap.CODE_PAIEMENT_ALLOC_VEUVE_AVS_RO;

                case IRERecap.GENRE_RECAP_EXTOURNE_200_2115:
                    return IRERecap.CODE_EXTOURNE_2002115_AVS_RO;

                default:
                    throw new Exception("genreRecap inconnu : " + genreRecap);
            }
        }

        // REO AVS
        else if (REGenresPrestations.GENRE_20.equals(genrePrestation)
                || REGenresPrestations.GENRE_23.equals(genrePrestation)
                || REGenresPrestations.GENRE_24.equals(genrePrestation)
                || REGenresPrestations.GENRE_25.equals(genrePrestation)
                || REGenresPrestations.GENRE_26.equals(genrePrestation)
                || REGenresPrestations.GENRE_45.equals(genrePrestation)) {

            switch (genreRecap) {
                case IRERecap.GENRE_RECAP_REC_FIN_MOIS_PRECEDENT:
                    return IRERecap.CODE_RENTE_EN_COURS_MOIS_PRECEDENT_AVS_REO;

                case IRERecap.GENRE_RECAP_AUGMENTATION:
                    return IRERecap.CODE_AUGMENTATION_MOIS_RAPPORT_AVS_REO;

                case IRERecap.GENRE_RECAP_ADAPTATION_BISANNUEL:
                    return IRERecap.CODE_AUGMENTATION_BISANNUELLE_AVS_REO;

                case IRERecap.GENRE_RECAP_DIMINUTION:
                    return IRERecap.CODE_DIMINUTION_MOIS_PRECEDENT_AVS_REO;

                case IRERecap.GENRE_RECAP_PMT_RETROACTIF:
                    return IRERecap.CODE_PAIEMENT_RETRO_AVS_REO;

                case IRERecap.GENRE_RECAP_ALLOC_UNIQUE_VEUVE:
                    return IRERecap.CODE_PAIEMENT_ALLOC_VEUVE_AVS_REO;

                case IRERecap.GENRE_RECAP_EXTOURNE_200_2115:
                    return IRERecap.CODE_EXTOURNE_2002115_AVS_REO;

                default:
                    throw new Exception("genreRecap inconnu : " + genreRecap);
            }
        }

        // API AVS
        else if (REGenresPrestations.GENRE_85.equals(genrePrestation)
                || REGenresPrestations.GENRE_86.equals(genrePrestation)
                || REGenresPrestations.GENRE_87.equals(genrePrestation)
                || REGenresPrestations.GENRE_89.equals(genrePrestation)
                || REGenresPrestations.GENRE_94.equals(genrePrestation)
                || REGenresPrestations.GENRE_95.equals(genrePrestation)
                || REGenresPrestations.GENRE_96.equals(genrePrestation)
                || REGenresPrestations.GENRE_97.equals(genrePrestation)) {

            switch (genreRecap) {
                case IRERecap.GENRE_RECAP_REC_FIN_MOIS_PRECEDENT:
                    return IRERecap.CODE_RENTE_EN_COURS_MOIS_PRECEDENT_AVS_API;

                case IRERecap.GENRE_RECAP_AUGMENTATION:
                    return IRERecap.CODE_AUGMENTATION_MOIS_RAPPORT_AVS_API;

                case IRERecap.GENRE_RECAP_ADAPTATION_BISANNUEL:
                    return IRERecap.CODE_AUGMENTATION_BISANNUELLE_AVS_API;

                case IRERecap.GENRE_RECAP_DIMINUTION:
                    return IRERecap.CODE_DIMINUTION_MOIS_PRECEDENT_AVS_API;

                case IRERecap.GENRE_RECAP_PMT_RETROACTIF:
                    return IRERecap.CODE_PAIEMENT_RETRO_AVS_API;

                case IRERecap.GENRE_RECAP_EXTOURNE_200_2115:
                    return IRERecap.CODE_EXTOURNE_2002115_AVS_API;

                default:
                    throw new Exception("genreRecap inconnu : " + genreRecap);
            }
        }

        // AI RO
        else if (REGenresPrestations.GENRE_50.equals(genrePrestation)
                || REGenresPrestations.GENRE_53.equals(genrePrestation)
                || REGenresPrestations.GENRE_54.equals(genrePrestation)
                || REGenresPrestations.GENRE_55.equals(genrePrestation)
                || REGenresPrestations.GENRE_56.equals(genrePrestation)) {

            switch (genreRecap) {
                case IRERecap.GENRE_RECAP_REC_FIN_MOIS_PRECEDENT:
                    return IRERecap.CODE_RENTE_EN_COURS_MOIS_PRECEDENT_AI_RO;

                case IRERecap.GENRE_RECAP_AUGMENTATION:
                    return IRERecap.CODE_AUGMENTATION_MOIS_RAPPORT_AI_RO;

                case IRERecap.GENRE_RECAP_ADAPTATION_BISANNUEL:
                    return IRERecap.CODE_AUGMENTATION_BISANNUELLE_AI_RO;

                case IRERecap.GENRE_RECAP_DIMINUTION:
                    return IRERecap.CODE_DIMINUTION_MOIS_PRECEDENT_AI_RO;

                case IRERecap.GENRE_RECAP_PMT_RETROACTIF:
                    return IRERecap.CODE_PAIEMENT_RETRO_AI_RO;

                case IRERecap.GENRE_RECAP_EXTOURNE_200_2115:
                    return IRERecap.CODE_EXTOURNE_2002115_AI_RO;

                default:
                    throw new Exception("genreRecap inconnu : " + genreRecap);
            }
        }

        // AI REO
        else if (REGenresPrestations.GENRE_70.equals(genrePrestation)
                || REGenresPrestations.GENRE_72.equals(genrePrestation)
                || REGenresPrestations.GENRE_73.equals(genrePrestation)
                || REGenresPrestations.GENRE_74.equals(genrePrestation)
                || REGenresPrestations.GENRE_75.equals(genrePrestation)
                || REGenresPrestations.GENRE_76.equals(genrePrestation)) {

            switch (genreRecap) {
                case IRERecap.GENRE_RECAP_REC_FIN_MOIS_PRECEDENT:
                    return IRERecap.CODE_RENTE_EN_COURS_MOIS_PRECEDENT_AI_REO;

                case IRERecap.GENRE_RECAP_AUGMENTATION:
                    return IRERecap.CODE_AUGMENTATION_MOIS_RAPPORT_AI_REO;

                case IRERecap.GENRE_RECAP_ADAPTATION_BISANNUEL:
                    return IRERecap.CODE_AUGMENTATION_BISANNUELLE_AI_REO;

                case IRERecap.GENRE_RECAP_DIMINUTION:
                    return IRERecap.CODE_DIMINUTION_MOIS_PRECEDENT_AI_REO;

                case IRERecap.GENRE_RECAP_PMT_RETROACTIF:
                    return IRERecap.CODE_PAIEMENT_RETRO_AI_REO;

                case IRERecap.GENRE_RECAP_EXTOURNE_200_2115:
                    return IRERecap.CODE_EXTOURNE_2002115_AI_REO;

                default:
                    throw new Exception("genreRecap inconnu : " + genreRecap);
            }
        }

        // API AI
        else if (REGenresPrestations.GENRE_81.equals(genrePrestation)
                || REGenresPrestations.GENRE_82.equals(genrePrestation)
                || REGenresPrestations.GENRE_83.equals(genrePrestation)
                || REGenresPrestations.GENRE_84.equals(genrePrestation)
                || REGenresPrestations.GENRE_88.equals(genrePrestation)
                || REGenresPrestations.GENRE_91.equals(genrePrestation)
                || REGenresPrestations.GENRE_92.equals(genrePrestation)
                || REGenresPrestations.GENRE_93.equals(genrePrestation)) {

            switch (genreRecap) {
                case IRERecap.GENRE_RECAP_REC_FIN_MOIS_PRECEDENT:
                    return IRERecap.CODE_RENTE_EN_COURS_MOIS_PRECEDENT_AI_API;

                case IRERecap.GENRE_RECAP_AUGMENTATION:
                    return IRERecap.CODE_AUGMENTATION_MOIS_RAPPORT_AI_API;

                case IRERecap.GENRE_RECAP_ADAPTATION_BISANNUEL:
                    return IRERecap.CODE_AUGMENTATION_BISANNUELLE_AI_API;

                case IRERecap.GENRE_RECAP_DIMINUTION:
                    return IRERecap.CODE_DIMINUTION_MOIS_PRECEDENT_AI_API;

                case IRERecap.GENRE_RECAP_PMT_RETROACTIF:
                    return IRERecap.CODE_PAIEMENT_RETRO_AI_API;

                case IRERecap.GENRE_RECAP_EXTOURNE_200_2115:
                    return IRERecap.CODE_EXTOURNE_2002115_AI_API;

                default:
                    throw new Exception("genreRecap inconnu : " + genreRecap);
            }
        } else {
            throw new Exception("genrePrestation inconnu : " + genrePrestation);
        }

    }

    public synchronized static String getLibelleRubrique(final BSession session, final String genrePrestation,
            String isoLangue) throws Exception {

        String labelId = "";

        if (!REGenrePrestationEnum.groupe1.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe2.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe3.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe4.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe5.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe6.contains(genrePrestation)) {

            throw new Exception(session.getApplication().getLabel("GENRE_PREST_INCONNU", isoLangue) + genrePrestation);
        }

        if (REGenresPrestations.GENRE_10.equals(genrePrestation)
                || REGenresPrestations.GENRE_13.equals(genrePrestation)
                || REGenresPrestations.GENRE_14.equals(genrePrestation)
                || REGenresPrestations.GENRE_15.equals(genrePrestation)
                || REGenresPrestations.GENRE_16.equals(genrePrestation)
                || REGenresPrestations.GENRE_33.equals(genrePrestation)
                || REGenresPrestations.GENRE_34.equals(genrePrestation)
                || REGenresPrestations.GENRE_35.equals(genrePrestation)
                || REGenresPrestations.GENRE_36.equals(genrePrestation)) {
            labelId = "RENTE_AVS";
        } else if (REGenresPrestations.GENRE_20.equals(genrePrestation)
                || REGenresPrestations.GENRE_23.equals(genrePrestation)
                || REGenresPrestations.GENRE_24.equals(genrePrestation)
                || REGenresPrestations.GENRE_25.equals(genrePrestation)
                || REGenresPrestations.GENRE_26.equals(genrePrestation)
                || REGenresPrestations.GENRE_45.equals(genrePrestation)) {
            labelId = "RENTE_AVS";
        } else if (REGenresPrestations.GENRE_50.equals(genrePrestation)
                || REGenresPrestations.GENRE_53.equals(genrePrestation)
                || REGenresPrestations.GENRE_54.equals(genrePrestation)
                || REGenresPrestations.GENRE_55.equals(genrePrestation)
                || REGenresPrestations.GENRE_56.equals(genrePrestation)) {
            labelId = "RENTE_AI";
        } else if (REGenresPrestations.GENRE_70.equals(genrePrestation)
                || REGenresPrestations.GENRE_72.equals(genrePrestation)
                || REGenresPrestations.GENRE_73.equals(genrePrestation)
                || REGenresPrestations.GENRE_74.equals(genrePrestation)
                || REGenresPrestations.GENRE_75.equals(genrePrestation)
                || REGenresPrestations.GENRE_76.equals(genrePrestation)) {
            labelId = "RENTE_AI";
        } else if (REGenresPrestations.GENRE_85.equals(genrePrestation)
                || REGenresPrestations.GENRE_86.equals(genrePrestation)
                || REGenresPrestations.GENRE_87.equals(genrePrestation)
                || REGenresPrestations.GENRE_89.equals(genrePrestation)
                || REGenresPrestations.GENRE_94.equals(genrePrestation)
                || REGenresPrestations.GENRE_95.equals(genrePrestation)
                || REGenresPrestations.GENRE_96.equals(genrePrestation)
                || REGenresPrestations.GENRE_97.equals(genrePrestation)) {
            labelId = "RENTE_API_AVS";
        } else if (REGenresPrestations.GENRE_81.equals(genrePrestation)
                || REGenresPrestations.GENRE_82.equals(genrePrestation)
                || REGenresPrestations.GENRE_83.equals(genrePrestation)
                || REGenresPrestations.GENRE_84.equals(genrePrestation)
                || REGenresPrestations.GENRE_88.equals(genrePrestation)
                || REGenresPrestations.GENRE_91.equals(genrePrestation)
                || REGenresPrestations.GENRE_92.equals(genrePrestation)
                || REGenresPrestations.GENRE_93.equals(genrePrestation)) {
            labelId = "RENTE_API_AI";
        } else if (REGenresPrestations.GENRE_110.equals(genrePrestation)
                || REGenresPrestations.GENRE_113.equals(genrePrestation)) {
            labelId = "RENTE_PC_AVS";
        } else if (REGenresPrestations.GENRE_150.equals(genrePrestation)) {
            labelId = "RENTE_PC_AI";
        } else if (REGenresPrestations.GENRE_210.equals(genrePrestation)
                || REGenresPrestations.GENRE_213.equals(genrePrestation)) {
            labelId = "RENTE_RFM_AVS";
        } else if (REGenresPrestations.GENRE_250.equals(genrePrestation)) {
            labelId = "RENTE_RFM_AI";
        } else {
            return null;
        }

        return MotifVersementUtil.getTranslatedLabelFromIsolangue(isoLangue, labelId, session);
    }

    /**
     * Retourne la rubrique concernée, suivant le genre de prestation Ceci uniquement pour les cas standard. Les
     * rubriques pour Extourne, Restitution, Retro, IS, .... seront traitées dans les modules concerné.
     * 
     * @return la rubrique concernée, ou null si non trouvé
     */
    public synchronized static APIRubrique getRubrique(final String genrePrestation,
            final String sousTypeGenrePrestation, final int typeRubrique) throws Exception {

        if (AREModuleComptable.TYPE_RUBRIQUE_COMPENSATION == typeRubrique) {

            return REModuleComptableFactory.getInstance().COMPENSATION;
        }

        if (!REGenrePrestationEnum.groupe1.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe2.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe3.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe4.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe5.contains(genrePrestation)
                && !REGenrePrestationEnum.groupe6.contains(genrePrestation)) {

            throw new Exception("Le genre de prestation est inconnu : " + genrePrestation);
        }

        if (REGenresPrestations.GENRE_10.equals(genrePrestation)
                || REGenresPrestations.GENRE_13.equals(genrePrestation)
                || REGenresPrestations.GENRE_14.equals(genrePrestation)
                || REGenresPrestations.GENRE_15.equals(genrePrestation)
                || REGenresPrestations.GENRE_16.equals(genrePrestation)
                || REGenresPrestations.GENRE_33.equals(genrePrestation)
                || REGenresPrestations.GENRE_34.equals(genrePrestation)
                || REGenresPrestations.GENRE_35.equals(genrePrestation)
                || REGenresPrestations.GENRE_36.equals(genrePrestation)) {

            switch (typeRubrique) {
                case TYPE_RUBRIQUE_NORMAL:
                    return REModuleComptableFactory.getInstance().RO_AVS;
                case TYPE_RUBRIQUE_EXTOURNE:
                    return REModuleComptableFactory.getInstance().RO_AVS_EXTOURNE;
                case TYPE_RUBRIQUE_RESTITUTION:
                    return REModuleComptableFactory.getInstance().PRST_AVS_RESTITUER;
                case TYPE_RUBRIQUE_RETROACTIF:
                    return REModuleComptableFactory.getInstance().RO_AVS_RETRO;
                default:
                    throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            }

        } else if (REGenresPrestations.GENRE_20.equals(genrePrestation)
                || REGenresPrestations.GENRE_23.equals(genrePrestation)
                || REGenresPrestations.GENRE_24.equals(genrePrestation)
                || REGenresPrestations.GENRE_25.equals(genrePrestation)
                || REGenresPrestations.GENRE_26.equals(genrePrestation)
                || REGenresPrestations.GENRE_45.equals(genrePrestation)) {

            switch (typeRubrique) {
                case TYPE_RUBRIQUE_NORMAL:
                    return REModuleComptableFactory.getInstance().REO_AVS;
                case TYPE_RUBRIQUE_EXTOURNE:
                    return REModuleComptableFactory.getInstance().REO_AVS_EXTOURNE;
                case TYPE_RUBRIQUE_RESTITUTION:
                    return REModuleComptableFactory.getInstance().PRST_AVS_RESTITUER;
                case TYPE_RUBRIQUE_RETROACTIF:
                    return REModuleComptableFactory.getInstance().REO_AVS_RETRO;
                default:
                    throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            }
        }

        else if (REGenresPrestations.GENRE_50.equals(genrePrestation)
                || REGenresPrestations.GENRE_53.equals(genrePrestation)
                || REGenresPrestations.GENRE_54.equals(genrePrestation)
                || REGenresPrestations.GENRE_55.equals(genrePrestation)
                || REGenresPrestations.GENRE_56.equals(genrePrestation)) {

            switch (typeRubrique) {
                case TYPE_RUBRIQUE_NORMAL:
                    return REModuleComptableFactory.getInstance().RO_AI;
                case TYPE_RUBRIQUE_EXTOURNE:
                    return REModuleComptableFactory.getInstance().RO_AI_EXTOURNE;
                case TYPE_RUBRIQUE_RESTITUTION:
                    return REModuleComptableFactory.getInstance().PRST_AI_RESTITUER;
                case TYPE_RUBRIQUE_RETROACTIF:
                    return REModuleComptableFactory.getInstance().RO_AI_RETRO;
                default:
                    throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            }
        }

        // ////////////
        // code Prestation PC
        else if (PRCodePrestationPC.isCodePrestationPC(genrePrestation)) {
            switch (typeRubrique) {
                case TYPE_RUBRIQUE_NORMAL:
                    String codeRubrique = null;
                    // En premier lieu, on regarde s'il s'agit d'allocation de Noël car il n'y à pas de sous-type genre
                    // prestation
                    if (PRCodePrestationPC.isCodePrestationAllocationNoel(genrePrestation)) {
                        if (PRCodePrestationPC.isCodePrestationAllocationNoelAI(genrePrestation)) {
                            codeRubrique = APIReferenceRubrique.PC_AI_ALLOCATIONS_DE_NOEL;
                        } else if (PRCodePrestationPC.isCodePrestationAllocationNoelAVS(genrePrestation)) {
                            codeRubrique = APIReferenceRubrique.PC_AVS_ALLOCATIONS_DE_NOEL;
                        }
                    } else {
                        codeRubrique = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMStandard(genrePrestation,
                                sousTypeGenrePrestation);
                    }
                    if (codeRubrique == null) {
                        throw new Exception(
                                "Impossible de retrouver la rubique comptable pour le type de rubrique inconnu : "
                                        + typeRubrique + ", genrePrestation : " + genrePrestation);
                    } else {
                        return REModuleComptableFactory.getInstance().getRubriqueComptablePC(codeRubrique);
                    }
                default:
                    throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            }
        }

        // ///////////

        // PC AVS
        // else if (REGenresPrestations.GENRE_110.equals(genrePrestation)
        // || REGenresPrestations.GENRE_113.equals(genrePrestation)) {
        else if (PRCodePrestationRFM.isCodePrestationRFM(genrePrestation)) {
            // if (JadeStringUtil.isBlankOrZero(sousTypeGenrePrestation)) {
            // switch (typeRubrique) {
            // case TYPE_RUBRIQUE_NORMAL:
            // return REModuleComptableFactory.getInstance().PC_AVS;
            // default:
            // throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            // }
            // } else {
            switch (typeRubrique) {
                case TYPE_RUBRIQUE_NORMAL:
                    String codeRubrique = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMStandard(
                            genrePrestation, sousTypeGenrePrestation);
                    return REModuleComptableFactory.getInstance().getRubriqueComptableRFM(codeRubrique);
                default:
                    throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            }
            // }
        }

        // PC AI
        else if (REGenresPrestations.GENRE_150.equals(genrePrestation)) {
            // if (JadeStringUtil.isBlankOrZero(sousTypeGenrePrestation)) {
            // switch (typeRubrique) {
            // case TYPE_RUBRIQUE_NORMAL:
            // return REModuleComptableFactory.getInstance().PC_AI;
            // default:
            // throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            // }
            // } else {
            switch (typeRubrique) {
                case TYPE_RUBRIQUE_NORMAL:
                    String codeRubrique = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMStandard(
                            genrePrestation, sousTypeGenrePrestation);
                    return REModuleComptableFactory.getInstance().getRubriqueComptablePC(codeRubrique);
                default:
                    throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            }
            // }
        }

        // RFM AVS
        else if (REGenresPrestations.GENRE_210.equals(genrePrestation)
                || REGenresPrestations.GENRE_213.equals(genrePrestation)) {
            // if (JadeStringUtil.isBlankOrZero(sousTypeGenrePrestation)) {
            // switch (typeRubrique) {
            // case TYPE_RUBRIQUE_NORMAL:
            // return REModuleComptableFactory.getInstance().RFM_AVS;
            // default:
            // throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            // }
            // } else {
            switch (typeRubrique) {
                case TYPE_RUBRIQUE_NORMAL:
                    String codeRubrique = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMStandard(
                            genrePrestation, sousTypeGenrePrestation);
                    return REModuleComptableFactory.getInstance().getRubriqueComptableRFM(codeRubrique);
                default:
                    throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            }
            // }
        }

        // RFM AI
        else if (REGenresPrestations.GENRE_250.equals(genrePrestation)) {
            // if (JadeStringUtil.isBlankOrZero(sousTypeGenrePrestation)) {
            // switch (typeRubrique) {
            // case TYPE_RUBRIQUE_NORMAL:
            // return REModuleComptableFactory.getInstance().RFM_AI;
            // default:
            // throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            // }
            // } else {
            switch (typeRubrique) {
                case TYPE_RUBRIQUE_NORMAL:
                    String codeRubrique = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMStandard(
                            genrePrestation, sousTypeGenrePrestation);
                    return REModuleComptableFactory.getInstance().getRubriqueComptableRFM(codeRubrique);
                default:
                    throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            }
            // }
        }

        else if (REGenresPrestations.GENRE_85.equals(genrePrestation)
                || REGenresPrestations.GENRE_86.equals(genrePrestation)
                || REGenresPrestations.GENRE_87.equals(genrePrestation)
                || REGenresPrestations.GENRE_89.equals(genrePrestation)
                || REGenresPrestations.GENRE_94.equals(genrePrestation)
                || REGenresPrestations.GENRE_95.equals(genrePrestation)
                || REGenresPrestations.GENRE_96.equals(genrePrestation)
                || REGenresPrestations.GENRE_97.equals(genrePrestation)) {

            switch (typeRubrique) {
                case TYPE_RUBRIQUE_NORMAL:
                    return REModuleComptableFactory.getInstance().API_AVS;
                case TYPE_RUBRIQUE_EXTOURNE:
                    return REModuleComptableFactory.getInstance().API_AVS_EXTOURNE;
                case TYPE_RUBRIQUE_RESTITUTION:
                    return REModuleComptableFactory.getInstance().PRST_API_AVS_RESTITUER;
                case TYPE_RUBRIQUE_RETROACTIF:
                    return REModuleComptableFactory.getInstance().API_AVS_RETRO;
                default:
                    throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            }
        } else if (REGenresPrestations.GENRE_70.equals(genrePrestation)
                || REGenresPrestations.GENRE_72.equals(genrePrestation)
                || REGenresPrestations.GENRE_73.equals(genrePrestation)
                || REGenresPrestations.GENRE_74.equals(genrePrestation)
                || REGenresPrestations.GENRE_75.equals(genrePrestation)
                || REGenresPrestations.GENRE_76.equals(genrePrestation)) {

            switch (typeRubrique) {
                case TYPE_RUBRIQUE_NORMAL:
                    return REModuleComptableFactory.getInstance().REO_AI;
                case TYPE_RUBRIQUE_EXTOURNE:
                    return REModuleComptableFactory.getInstance().REO_AI_EXTOURNE;
                case TYPE_RUBRIQUE_RESTITUTION:
                    return REModuleComptableFactory.getInstance().PRST_AI_RESTITUER;
                case TYPE_RUBRIQUE_RETROACTIF:
                    return REModuleComptableFactory.getInstance().REO_AI_RETRO;
                default:
                    throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            }
        } else if (REGenresPrestations.GENRE_81.equals(genrePrestation)
                || REGenresPrestations.GENRE_82.equals(genrePrestation)
                || REGenresPrestations.GENRE_83.equals(genrePrestation)
                || REGenresPrestations.GENRE_84.equals(genrePrestation)
                || REGenresPrestations.GENRE_88.equals(genrePrestation)
                || REGenresPrestations.GENRE_91.equals(genrePrestation)
                || REGenresPrestations.GENRE_92.equals(genrePrestation)
                || REGenresPrestations.GENRE_93.equals(genrePrestation)) {
            switch (typeRubrique) {
                case TYPE_RUBRIQUE_NORMAL:
                    return REModuleComptableFactory.getInstance().API_AI;
                case TYPE_RUBRIQUE_EXTOURNE:
                    return REModuleComptableFactory.getInstance().API_AI_EXTOURNE;
                case TYPE_RUBRIQUE_RESTITUTION:
                    return REModuleComptableFactory.getInstance().PRST_API_AI_RESTITUER;
                case TYPE_RUBRIQUE_RETROACTIF:
                    return REModuleComptableFactory.getInstance().API_AI_RETRO;
                default:
                    throw new Exception("Type de rubrique inconnu : " + typeRubrique);
            }
        } else {
            return null;
        }

    }

    public synchronized static APIRubrique getRubriqueWithInit(final BISession sessionOsiris,
            final String genrePrestation, final String sousTypeCodePrestation, final int typeRubrique) throws Exception {

        if (REModuleComptableFactory.getInstance().RO_AVS == null) {
            REModuleComptableFactory.getInstance().initIdsRubriques(sessionOsiris);
        }
        return AREModuleComptable.getRubrique(genrePrestation, sousTypeCodePrestation, typeRubrique);
    }

    protected boolean isGenererEcritureComptable = false;

    public AREModuleComptable(final boolean isGenererEcritureComptable) throws Exception {
        this.isGenererEcritureComptable = isGenererEcritureComptable;
    }

    @Override
    public int compare(final IREModuleComptable o1, final IREModuleComptable o2) {
        if (o1.getPriority() > o2.getPriority()) {
            return 1;
        } else if (o1.getPriority() < o2.getPriority()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * écrit une écriture en compta. Ne fait rien si le montant est nul.
     * 
     * @param session
     *            session orisis
     * @param compta
     *            une instance de APIProcessComptabilisation
     * @param memoryLog
     *            log des msg.
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
     */
    protected BIMessage doEcriture(final BSession session, final APIGestionComptabiliteExterne compta,
            final String montantSigne, final APIRubrique rubrique, final String idCompteAnnexe, final String idSection,
            final String dateComptable, final String libelle) {

        FWMemoryLog log = new FWMemoryLog();
        if (compta == null) {
            log.logMessage("Aucune écriture comptable. APIGestionComptabiliteExterne non instancié (1).",
                    FWViewBeanInterface.WARNING, this.getClass().getName());
        }
        if ((compta.getJournal() == null) || compta.getJournal().isNew()) {
            compta.createJournal();
        }

        if (isGenererEcritureComptable && !JadeStringUtil.isDecimalEmpty(montantSigne)) {
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

            /*
             * log.logMessage(session.getLabel("ERREUR_ECRITURE_RECAP")+ rubrique.getIdExterne() +
             * session.getLabel("MONTANT_ARE")+ montantSigne.toString(), FWMessage.INFORMATION,
             * this.getClass().getName());
             */

            log.logMessage(
                    "Ecriture " + rubrique.getIdExterne() + session.getLabel("MONTANT_ARE") + montantSigne.toString(),
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

    protected BIMessage doEcritureRecap(final BSession session, final BTransaction transaction, final int codeRecap,
            final FWCurrency montant, final String idTiers, final String dateRecap, final String idLot)
            throws Exception {

        FWMemoryLog log = new FWMemoryLog();

        RERecapInfo ir = new RERecapInfo();
        ir.setSession(session);
        ir.setCodeRecap(String.valueOf(codeRecap));

        ir.setDatePmt(dateRecap);
        ir.setIdTiers(idTiers);
        ir.setMontant(montant.toString());
        ir.setRestoreTag(idLot);
        ir.add(transaction);

        log.logMessage(session.getLabel("ERREUR_ECRITURE_RECAP") + codeRecap + session.getLabel("MONTANT_ARE")
                + montant.toString(), FWMessage.INFORMATION, this.getClass().getSimpleName());

        return log.getMessage(0);

    }

    /**
     * @param session
     * @param transaction
     * @param idDemandeRente
     * @param codeRecap
     * @param montant
     * @param idTiers
     * @param dateRecap
     *            Peut être null. Format mm.aaaa. Si non renseignée, sera calculée avec plus grande date
     *            (dateDernierPmt, ddDroit)
     * @return
     * @throws Exception
     */
    protected BIMessage doEcritureRecap(final BSession session, final BTransaction transaction,
            final String idDecision, final int codeRecap, final FWCurrency montant, final String idTiers,
            final String idLot) throws Exception {

        FWMemoryLog log = new FWMemoryLog();

        RERecapInfo ir = new RERecapInfo();
        ir.setSession(session);
        ir.setCodeRecap(String.valueOf(codeRecap));

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // On récupére la plus petite date de début des RA liées à cette décision.
        // --> ddPeriodeDecision
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        JADate ddPeriodeDecision = null;
        JADate id1 = null;

        REDecisionJointDemandeRenteManager mgr = new REDecisionJointDemandeRenteManager();
        mgr.setSession(session);
        mgr.setForIdDecision(idDecision);
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        if (mgr.isEmpty()) {
            throw new Exception("Erreur, aucune RA trouvée pour idDecision : " + idDecision);
        }

        REDecisionJointDemandeRente line = (REDecisionJointDemandeRente) mgr.getEntity(0);
        if ((line != null) && !JadeStringUtil.isBlankOrZero(line.getDateDebutDroit())) {
            ddPeriodeDecision = new JADate(line.getDateDebutDroit());
        } else {
            // FIXME c'est quoi cette merde !
            ddPeriodeDecision = new JADate("31.12.2999");
        }

        JACalendar cal = new JACalendarGregorian();

        for (int i = 0; i < mgr.size(); i++) {
            line = (REDecisionJointDemandeRente) mgr.getEntity(i);
            if (!JadeStringUtil.isBlankOrZero(line.getDateDebutDroit())) {
                id1 = new JADate(line.getDateDebutDroit());
                if (cal.compare(ddPeriodeDecision, id1) == JACalendar.COMPARE_SECONDLOWER) {
                    ddPeriodeDecision = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(id1.toStrAMJ()));
                }
            }
        }

        if (cal.compare(ddPeriodeDecision, new JADate("31.12.2999")) == JACalendar.COMPARE_EQUALS) {
            throw new Exception("Erreur : La/Les RA n'ont pas de date de début pour idDecision = " + idDecision);
        }

        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        // Fin de récupération de la plus petite date de début des RA liées à cette décision.
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        // mm.aaaa
        JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt(session));

        if (cal.compare(dateDernierPmt, ddPeriodeDecision) == JACalendar.COMPARE_FIRSTLOWER) {
            ir.setDatePmt(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(ddPeriodeDecision.toStrAMJ()));
        } else {
            ir.setDatePmt(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateDernierPmt.toStrAMJ()));
        }

        ir.setIdTiers(idTiers);
        ir.setMontant(montant.toString());
        ir.setRestoreTag(idLot);
        ir.add(transaction);

        log.logMessage(session.getLabel("ERREUR_ECRITURE_RECAP") + codeRecap + session.getLabel("MONTANT_ARE")
                + montant.toString(), FWMessage.INFORMATION, this.getClass().getName());

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
     * @throws Exception
     *             Si le montant est négatif
     * @throws IllegalArgumentException
     *             DOCUMENT ME!
     */
    protected BIMessage doOrdreVersement(final BSession session, final APIGestionComptabiliteExterne compta,
            final String idCompteAnnexe, final String idSection, final String montant, final String idAdressePaiement,
            final String motifVersement, final String dateComptable, final boolean isAvance) throws Exception {

        FWMemoryLog log = new FWMemoryLog();

        if (compta == null) {
            log.logMessage("Aucune écriture comptable. APIGestionComptabiliteExterne non instancié (2).",
                    FWViewBeanInterface.WARNING, this.getClass().getName());
        }

        if (new FWCurrency(montant).isNegative()) {
            throw new IllegalArgumentException(session.getLabel("MONTANT_NEG_NON_ACCEPTE_POUR_OV"));
        }

        if (!isGenererEcritureComptable) {
            log.logMessage(session.getLabel("PAS_OV_POUR_MNT_ZERO") + motifVersement, FWMessage.INFORMATION, this
                    .getClass().getName());
            return log.getMessage(0);
        }

        if (new FWCurrency(montant).isZero()) {
            log.logMessage(session.getLabel("PAS_OV_POUR_MNT_ZERO") + motifVersement, FWMessage.INFORMATION, this
                    .getClass().getName());
            return log.getMessage(0);
        }

        if (JadeStringUtil.isBlankOrZero(idAdressePaiement)) {
            String nss = "";
            try {
                CACompteAnnexe ca = new CACompteAnnexe();
                ca.setSession(session);
                ca.setIdCompteAnnexe(idCompteAnnexe);
                ca.retrieve();

                PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, ca.getIdTiers());
                nss = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            } catch (Exception e) {
                e.printStackTrace();
            }

            log.logMessage(session.getLabel("GENERER_OV_ADRESSE_PMT_ABSENTE") + " (nss = " + nss + ")",
                    FWViewBeanInterface.WARNING, this.getClass().getName());
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
        ordreVersement.setIdOrganeExecution(session.getApplication().getProperty(
                REApplication.PROPERTY_ID_ORGANE_EXECUTION));

        log.logMessage(session.getLabel("OV_MNT") + montant.toString() + " idAdrPmt = " + idAdressePaiement,
                FWMessage.INFORMATION, this.getClass().getName());

        ordreVersement.setNatureOrdre(CAOrdreGroupe.NATURE_RENTES_AVS_AI);
        ordreVersement.setMotif(motifVersement);
        compta.addOperation(ordreVersement);

        return log.getMessage(0);
    }

    protected String getIdTiersBeneficiairePrincipal(final REDecisionEntity decision, final BTransaction transaction)
            throws Exception {

        if (JadeStringUtil.isBlankOrZero(decision.getIdTiersBeneficiairePrincipal())) {
            throw new Exception("Aucun bénéficiaire principal trouvé dans les OV pour idDecision = "
                    + decision.getIdDecision());
        } else {
            return decision.getIdTiersBeneficiairePrincipal();
        }

    }

    /**
     * Retourne le montant mensuel en cours pour la décision. 0 pour du rétro pure.
     * 
     * @param session
     * @param transaction
     * @param decision
     * @return
     * @throws Exception
     */
    protected FWCurrency getMontantCourant(final BSession session, final BTransaction transaction,
            final REDecisionEntity decision) throws Exception {

        RenteAccordeeInfo rasEnCours = getRentesAccordeesEnCours(session, transaction, decision);

        if (!rasEnCours.raEnCours.isEmpty()) {
            return rasEnCours.montantCourant;
        } else {
            return new FWCurrency(0);
        }

    }

    /**
     * Calcul du montant rétroactif de la décision
     * 
     * @param session
     * @param transaction
     * @param decision
     * @return
     * @throws Exception
     */
    protected FWCurrency getMontantRetro(final BSession session, final BTransaction transaction,
            final REDecisionEntity decision) throws Exception {

        RenteAccordeeInfo rasEnCours = getRentesAccordeesEnCours(session, transaction, decision);
        REPrestations prestation = decision.getPrestation(transaction);

        // Calcul du montant rétroactif.
        // MontantRétro = montant total prestation - montant courant (s'il y en a)
        // Il y a du courant
        FWCurrency montantCourant = new FWCurrency(0);
        if (!rasEnCours.raEnCours.isEmpty()) {
            montantCourant = rasEnCours.montantCourant;
        }

        // Dans le cas d'une décision de type retro, il ne faut pas déduire le montant courant,
        // faute de quoi il serait comptabilisé à double avec la décision de type COURANT
        //

        FWCurrency montantRetroactif = new FWCurrency(prestation.getMontantPrestation());
        if (!IREDecision.CS_TYPE_DECISION_RETRO.equals(decision.getCsTypeDecision())) {
            montantRetroactif.sub(montantCourant);
        }

        return montantRetroactif;

    }

    protected String getMotifVersement(final BSession session, final String refPmt, final Decision decision)
            throws Exception {

        PersonneAVS beneficiairePrincipal = decision.getBeneficiairePrincipal();
        RenteAccordee renteAccordeePrincipale = decision.getRenteAccordeePrincipale();

        String idTiersPrincipal = decision.getTiersCorrespondance().getId().toString();

        if (JadeStringUtil.isBlankOrZero(idTiersPrincipal)) {
            idTiersPrincipal = decision.getBeneficiairePrincipal().getId().toString();
        }

        String isoLangFromIdTiers = PRTiersHelper.getIsoLangFromIdTiers(session, idTiersPrincipal);

        String msgDecision = MotifVersementUtil.getTranslatedLabelFromIsolangue(isoLangFromIdTiers,
                "PMT_MENS_DECISION_DU", session);
        msgDecision += " " + decision.getDateDecision();

        final String nss = beneficiairePrincipal.getNss().toString();
        final String nomPrenom = beneficiairePrincipal.getNom() + " " + beneficiairePrincipal.getPrenom();

        final String periode = renteAccordeePrincipale.getMoisDebut() + " - " + renteAccordeePrincipale.getMoisFin();
        final String prestation = AREModuleComptable.getLibelleRubrique(session,
                Integer.toString(renteAccordeePrincipale.getCodePrestation().getCodePrestation()), isoLangFromIdTiers);
        return MotifVersementUtil.formatDecision(nss, nomPrenom, refPmt, prestation, periode, msgDecision);
    }

    protected String getMotifVersementDeblocage(final BSession session, final PRTiersWrapper tw, final String refPmt,
            final String genrePrestation, final String idTiersAdrPmt) throws Exception {

        String idTiersPrincipal = idTiersAdrPmt;

        if (JadeStringUtil.isBlankOrZero(idTiersPrincipal)) {
            idTiersPrincipal = tw.getIdTiers();
        }

        String isoLangFromIdTiers = PRTiersHelper.getIsoLangFromIdTiers(session, idTiersPrincipal);

        String versementMsg = MotifVersementUtil.getTranslatedLabelFromIsolangue(isoLangFromIdTiers,
                "DEBLOCAGE_VERSEMENT_DU", session);

        final String nss = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        final String nomPrenom = tw.getProperty(PRTiersWrapper.PROPERTY_NOM + " " + PRTiersWrapper.PROPERTY_PRENOM);
        final String prestation = AREModuleComptable.getLibelleRubrique(session, genrePrestation, isoLangFromIdTiers);

        return MotifVersementUtil.formatDeblocage(nss, nomPrenom, refPmt, prestation, versementMsg);
    }

    /**
     * Retourne 1 RA faisant partie de la décision
     * 
     * @param session
     * @param transaction
     * @param decision
     * @return
     * @throws Exception
     */
    protected RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions getRenteAccordee(final BSession session,
            final BTransaction transaction, final REDecisionEntity decision) throws Exception {

        // Récupération d'une des RA de la décision.
        // Pour une décision, les RA sont obligatoirement sur la même rubrique -> on prend la première que l'on trouve.
        RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager mgr = new RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager();
        mgr.setSession(session);
        mgr.setForIdDecision(decision.getIdDecision());
        mgr.setOrderBy(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + " DESC ");
        mgr.find(transaction, BManager.SIZE_NOLIMIT);
        if (mgr.size() == 0) {
            throw new Exception(session.getLabel("AUCUNE_RA_TROUVEE_POUR_DEC") + decision.getIdDecision());
        }

        return (RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions) mgr.getEntity(0);
    }

    /**
     * Retourne la 1ère RA trouvée sans date de fin, cad une RA en cours.
     * 
     * @param session
     * @param transaction
     * @param decision
     * @return 1ère RA trouvée sans date de fin. Si Retro pure, return null.
     * @throws Exception
     */
    protected RenteAccordeeInfo getRentesAccordeesEnCours(final BSession session, final BTransaction transaction,
            final REDecisionEntity decision) throws Exception {

        RenteAccordeeInfo result = new RenteAccordeeInfo();

        // Récupération d'une des RA de la décision.
        // Pour une décision, les RA sont obligatoirement sur la même rubrique -> on prend la première que l'on trouve.
        RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager mgr = new RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager();
        mgr.setSession(session);
        mgr.setForIdDecision(decision.getIdDecision());
        mgr.setForCsTypePrstDue(IREPrestationDue.CS_TYPE_PMT_MENS); // $p only
        mgr.setOrderBy(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + " DESC ");
        mgr.find(transaction, BManager.SIZE_NOLIMIT);
        if (mgr.size() == 0) {
            throw new Exception(session.getLabel("AUCUNE_RA_TROUVEE_POUR_DEC") + decision.getIdDecision());
        }

        for (int i = 0; i < mgr.size(); i++) {
            RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions elm = (RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions) mgr
                    .getEntity(i);

            if (JadeStringUtil.isBlankOrZero(elm.getFinPaiement())) {
                if (!IREDecision.CS_TYPE_DECISION_RETRO.equals(elm.getCsTypeDecision())) {
                    result.raEnCours.add(elm);
                    result.montantCourant.add(elm.getMontantPrestation());
                }
            }
        }
        return result;
    }

    /**
     * charge l'adresse de paiement.
     * 
     * @return une adresse de paiement ou null.
     * @throws Exception
     *             DOCUMENT ME!
     */
    public TIAdressePaiementData loadAdressePaiement(final BSession session, final BTransaction transaction,
            final String dateValeurCompta, final String idTiersAdressePaiement, final String idDomaine)
            throws Exception {

        TIAdressePaiementData retValue = PRTiersHelper.getAdressePaiementData(session, transaction,
                idTiersAdressePaiement, idDomaine, "", dateValeurCompta);

        return retValue;
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }

}
