package ch.globaz.perseus.businessimpl.checkers.decision;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class DecisionChecker extends PerseusAbstractChecker {

    public static void checkForCreate(Decision decision) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException {
        DecisionChecker.checkMandatory(decision);
    }

    public static void checkForUpdate(Decision decision) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException {
        DecisionChecker.checkMandatory(decision);
    }

    public static void checkMandatory(Decision decision) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException {

        if (CSTypeDemande.REVISION_PERIODIQUE.getCodeSystem().equals(
                decision.getDemande().getSimpleDemande().getTypeDemande())
                && decision.getDemande().getSimpleDemande().getFromRI()) {
            if (JadeNumericUtil.isEmptyOrZero(decision.getSimpleDecision().getMontantToucheAuRI())) {
                JadeThread.logError(SimpleDecisionChecker.class.getName(),
                        "perseus.decision.simpledecision.montant.ri.mandatory");
            }
        }

        // Controle des cases à cocher dans le cas d'une décision d'aide catégorielle
        if (CSTypeDemande.AIDES_CATEGORIELLES.getCodeSystem().equals(
                decision.getDemande().getSimpleDemande().getTypeDemande())
                && !CSTypeDecision.SUPPRESSION.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
            if ((decision.getSimpleDecision().getAideAuLogement() == false)
                    && (decision.getSimpleDecision().getAideAuxEtudes() == false)
                    && (decision.getSimpleDecision().getPensionAlimentaire() == false)) {
                JadeThread.logError(SimpleDecisionChecker.class.getName(),
                        "perseus.decision.simpledecision.typeAide.mandatory");
            }
        }

        if (!CSTypeDecision.PROJET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())
                && !CSTypeDecision.SUPPRESSION.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
            // Si la décision ne contient pas de texte, mais qu'elle est en refus forcé
            if (decision.getDemande().getSimpleDemande().getRefusForce() == true) {
                if (JadeStringUtil.isEmpty(decision.getSimpleDecision().getRemarqueUtilisateur())) {
                    JadeThread.logError(SimpleDecisionChecker.class.getName(),
                            "perseus.decision.simpledecision.remarqueUtilisateur.mandatory");
                }
            } else

            // Si la demande est de type annonce de changement ou
            if (CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem().equals(
                    decision.getDemande().getSimpleDemande().getTypeDemande())
                    && (!CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem().equals(
                            decision.getSimpleDecision().getCsTypeDecision()))
                    && (!CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(
                            decision.getSimpleDecision().getCsTypeDecision()))
                    && (!CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem().equals(
                            decision.getSimpleDecision().getCsTypeDecision()))) {
                if (JadeStringUtil.isEmpty(decision.getSimpleDecision().getRemarqueUtilisateur())) {
                    JadeThread.logError(SimpleDecisionChecker.class.getName(),
                            "perseus.decision.simpledecision.remarqueUtilisateur.mandatory");
                }
            } else

            // Si la décision est en partiel et qu'il existe une demande précédente
            if (CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
                // Récupération de la demande précédente
                Demande demandePrecedante = PerseusServiceLocator.getDemandeService().getDemandePrecedante(
                        decision.getDemande());
                // Si la décision à une demande précédente
                if (null != demandePrecedante) {
                    DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
                    decisionSearchModel.setForIdDemande(demandePrecedante.getSimpleDemande().getId());
                    decisionSearchModel.setForCsTypeDecision(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
                    decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
                    boolean hasDemandePartiel = PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0;
                    decisionSearchModel.setForCsTypeDecision(CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem());
                    boolean hasDemandeRefus = PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0;
                    decisionSearchModel.setForCsTypeDecision(CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem());
                    boolean hasNonEntreeMatiere = PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0;
                    if (!hasDemandePartiel && !hasDemandeRefus && !hasNonEntreeMatiere) {
                        if (JadeStringUtil.isEmpty(decision.getSimpleDecision().getRemarqueUtilisateur())) {
                            JadeThread.logError(SimpleDecisionChecker.class.getName(),
                                    "perseus.decision.simpledecision.remarqueUtilisateur.mandatory");
                        }

                    }
                }
            }
        }

    }
}

// // Si la zone texte doit apparaitre
// if (!JadeStringUtil.isEmpty(decision.getSimpleDecision().getRemarqueUtilisateur())
// || (decision.getDemande().getSimpleDemande().getRefusForce() == true)
// || ((CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem().equals(
// decision.getDemande().getSimpleDemande().getTypeDemande()) || (!CSTypeDecision.REFUS_SANS_CALCUL
// .getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision()))))) {
//
// // Selon la décision n° 7
// if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())
// || (CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem().equals(decision.getDemande()
// .getSimpleDemande().getTypeDemande()))) {
// if (JadeStringUtil.isEmpty(decision.getSimpleDecision().getRemarqueUtilisateur())) {
// JadeThread.logError(SimpleDecisionChecker.class.getName(),
// "perseus.decision.simpledecision.remarqueUtilisateur.mandatory");
// }
// }
//
// // Selon les décisions n° 9 & 10
// if (CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem().equals(
// decision.getSimpleDecision().getCsTypeDecision())) {
// if (JadeStringUtil.isEmpty(decision.getSimpleDecision().getRemarqueUtilisateur())) {
// JadeThread.logError(SimpleDecisionChecker.class.getName(),
// "perseus.decision.simpledecision.remarqueUtilisateur.mandatory");
// }
//
// }
//
// // Selon la décision n°8
// if (CSTypeDemande.REVISION_PERIODIQUE.getCodeSystem().equals(
// decision.getDemande().getSimpleDemande().getTypeDemande())
// && (CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(decision.getSimpleDecision()
// .getCsTypeDecision()))) {
// if (JadeStringUtil.isEmpty(decision.getSimpleDecision().getRemarqueUtilisateur())) {
// JadeThread.logError(SimpleDecisionChecker.class.getName(),
// "perseus.decision.simpledecision.remarqueUtilisateur.mandatory");
// }
// }
// }
//
// }
// }
