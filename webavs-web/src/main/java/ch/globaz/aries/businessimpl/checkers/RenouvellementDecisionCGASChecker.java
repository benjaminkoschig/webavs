package ch.globaz.aries.businessimpl.checkers;

import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.musca.db.facturation.FAPassage;
import java.util.List;
import ch.globaz.aries.business.beans.decisioncgas.DecisionCGASBean;
import ch.globaz.aries.business.constantes.ARDecisionEtat;
import ch.globaz.aries.business.exceptions.AriesException;
import ch.globaz.aries.business.models.SimpleDecisionCGAS;
import ch.globaz.ariesaurigacommon.utils.AriesAurigaNumericUtils;
import ch.globaz.musca.business.models.PassageModel;

public class RenouvellementDecisionCGASChecker {

    public static void checkDecisionCGASARenouveler(DecisionCGASBean decisionCGASBeanARenouveler,
            List<SimpleDecisionCGAS> theListDecisionCGASDejaExistanteDansAnneeDuRenouvellement,
            List<String> theListAffiliePlusieursCotisationDansAnneeDuRenouvellement) throws Exception {

        if (ARDecisionEtat.SUPPRIMEE.getCodeSystem().equalsIgnoreCase(
                decisionCGASBeanARenouveler.getDecisionCGAS().getEtat())) {
            JadeThread.logError(RenouvellementDecisionCGASChecker.class.getName(),
                    "aries.renouvellement.decision.decision.supprimee");
        }

        for (SimpleDecisionCGAS aDecisionCGAS : theListDecisionCGASDejaExistanteDansAnneeDuRenouvellement) {
            if (decisionCGASBeanARenouveler.getDecisionCGAS().getIdAffiliation()
                    .equalsIgnoreCase(aDecisionCGAS.getIdAffiliation())) {
                JadeThread.logError(RenouvellementDecisionCGASChecker.class.getName(),
                        "aries.renouvellement.decision.decision.deja.existante.annee.du.renouvellement");
                break;
            }
        }

        if (theListAffiliePlusieursCotisationDansAnneeDuRenouvellement.contains(decisionCGASBeanARenouveler
                .getDecisionCGAS().getIdAffiliation())) {
            JadeThread.logError(RenouvellementDecisionCGASChecker.class.getName(),
                    "aries.renouvellement.decision.plusieurs.cotisation.annee.du.renouvellement");
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AriesException("aries.exception.metier");
        }
    }

    public static void checkPassageFacturation(PassageModel passage, boolean hasPassageModuleFacturationDecisionCGAS)
            throws Exception {

        if (!FAPassage.CS_ETAT_OUVERT.equalsIgnoreCase(passage.getStatus())) {
            JadeThread.logError(RenouvellementDecisionCGASChecker.class.getName(),
                    "aries.renouvellement.decision.passage.pas.ouvert");
        }

        if (!hasPassageModuleFacturationDecisionCGAS) {
            JadeThread.logError(RenouvellementDecisionCGASChecker.class.getName(),
                    "aries.renouvellement.decision.passage.pas.module.facturation.decision.cgas");
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AriesException("aries.exception.metier");
        }
    }

    public static void checkSaisieEcranLancementTraitementMasse(String numeroPassage, String annee,
            String numeroAffilieDebut, String numeroAffilieFin, String email) throws Exception {

        if (JadeStringUtil.isBlankOrZero(numeroPassage)) {
            JadeThread.logError(RenouvellementDecisionCGASChecker.class.getName(),
                    "aries.renouvellement.decision.passage.mandatory");
        }

        if (!JadeStringUtil.isEmpty(numeroAffilieDebut) && !JadeStringUtil.isEmpty(numeroAffilieFin)
                && (numeroAffilieDebut.compareToIgnoreCase(numeroAffilieFin) >= 1)) {
            JadeThread.logError(RenouvellementDecisionCGASChecker.class.getName(),
                    "aries.renouvellement.decision.numero.affilie.fin.avant.numero.affilie.debut");
        }

        if (!AriesAurigaNumericUtils.isNumericIntegerPositifFixedSize(annee, 4)) {
            JadeThread.logError(RenouvellementDecisionCGASChecker.class.getName(),
                    "aries.renouvellement.decision.annee.mandatory");
        } else {
            int anneeActuelle = JACalendar.today().getYear();
            int anneeInt = new Integer(annee);
            if (anneeInt < anneeActuelle) {
                JadeThread.logError(RenouvellementDecisionCGASChecker.class.getName(),
                        "aries.renouvellement.decision.annee.invalide");
            }
        }

        if (JadeStringUtil.isBlankOrZero(email)) {
            JadeThread.logError(RenouvellementDecisionCGASChecker.class.getName(),
                    "aries.renouvellement.decision.mail.mandatory");
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AriesException("aries.exception.metier");
        }

    }

}
