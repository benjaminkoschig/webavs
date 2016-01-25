package ch.globaz.auriga.businessimpl.checkers;

import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.musca.db.facturation.FAPassage;
import java.util.List;
import ch.globaz.ariesaurigacommon.utils.AriesAurigaNumericUtils;
import ch.globaz.auriga.business.beans.decisioncap.DecisionCAPBean;
import ch.globaz.auriga.business.constantes.AUDecisionEtat;
import ch.globaz.auriga.business.exceptions.AurigaException;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;
import ch.globaz.musca.business.models.PassageModel;

public class RenouvellementDecisionCAPChecker {

    public static void checkDecisionCAPARenouveler(DecisionCAPBean decisionCAPBeanARenouveler, String theTypeAssurance,
            List<SimpleDecisionCAP> theListDecisionCAPDejaExistanteDansAnneeDuRenouvellement,
            List<String> theListAffiliePlusieursCotisationDansAnneeDuRenouvellement) throws Exception {

        if (AUDecisionEtat.SUPPRIMEE.getCodeSystem().equalsIgnoreCase(
                decisionCAPBeanARenouveler.getDecisionCAP().getEtat())) {
            JadeThread.logError(RenouvellementDecisionCAPChecker.class.getName(),
                    "auriga.renouvellement.decision.decision.supprimee");
        }

        if (!decisionCAPBeanARenouveler.getDecisionCAP().getCategorie().equalsIgnoreCase(theTypeAssurance)) {
            JadeThread.logError(
                    RenouvellementDecisionCAPChecker.class.getName(),
                    "auriga.renouvellement.decision.changement.categorie.exploitant",
                    new String[] {
                            BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                                    decisionCAPBeanARenouveler.getDecisionCAP().getCategorie()),
                            BSessionUtil.getSessionFromThreadContext().getCodeLibelle(theTypeAssurance) });
        }

        for (SimpleDecisionCAP aDecisionCAP : theListDecisionCAPDejaExistanteDansAnneeDuRenouvellement) {
            if (decisionCAPBeanARenouveler.getDecisionCAP().getIdAffiliation()
                    .equalsIgnoreCase(aDecisionCAP.getIdAffiliation())) {
                JadeThread.logError(RenouvellementDecisionCAPChecker.class.getName(),
                        "auriga.renouvellement.decision.decision.deja.existante.annee.du.renouvellement");
                break;
            }
        }

        if (theListAffiliePlusieursCotisationDansAnneeDuRenouvellement.contains(decisionCAPBeanARenouveler
                .getDecisionCAP().getIdAffiliation())) {
            JadeThread.logError(RenouvellementDecisionCAPChecker.class.getName(),
                    "auriga.renouvellement.decision.plusieurs.cotisation.annee.du.renouvellement");
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AurigaException("auriga.exception.metier");
        }
    }

    public static void checkPassageFacturation(PassageModel passage, boolean hasPassageModuleFacturationDecisionCAP)
            throws Exception {

        if (!FAPassage.CS_ETAT_OUVERT.equalsIgnoreCase(passage.getStatus())) {
            JadeThread.logError(RenouvellementDecisionCAPChecker.class.getName(),
                    "auriga.renouvellement.decision.passage.pas.ouvert");
        }

        if (!hasPassageModuleFacturationDecisionCAP) {
            JadeThread.logError(RenouvellementDecisionCAPChecker.class.getName(),
                    "auriga.renouvellement.decision.passage.pas.module.facturation.decision.cap");
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AurigaException("auriga.exception.metier");
        }
    }

    public static void checkSaisieEcranLancementTraitementMasse(String numeroPassage, String annee,
            String numeroAffilieDebut, String numeroAffilieFin, String email) throws Exception {

        if (JadeStringUtil.isBlankOrZero(numeroPassage)) {
            JadeThread.logError(RenouvellementDecisionCAPChecker.class.getName(),
                    "auriga.renouvellement.decision.passage.mandatory");
        }

        if (!JadeStringUtil.isEmpty(numeroAffilieDebut) && !JadeStringUtil.isEmpty(numeroAffilieFin)
                && (numeroAffilieDebut.compareToIgnoreCase(numeroAffilieFin) >= 1)) {
            JadeThread.logError(RenouvellementDecisionCAPChecker.class.getName(),
                    "auriga.renouvellement.decision.numero.affilie.fin.avant.numero.affilie.debut");
        }

        if (!AriesAurigaNumericUtils.isNumericIntegerPositifFixedSize(annee, 4)) {
            JadeThread.logError(RenouvellementDecisionCAPChecker.class.getName(),
                    "auriga.renouvellement.decision.annee.mandatory");
        } else {
            int anneeActuelle = JACalendar.today().getYear();
            int anneeInt = new Integer(annee);
            if (anneeInt < anneeActuelle) {
                JadeThread.logError(RenouvellementDecisionCAPChecker.class.getName(),
                        "auriga.renouvellement.decision.annee.invalide");
            }
        }

        if (JadeStringUtil.isBlankOrZero(email)) {
            JadeThread.logError(RenouvellementDecisionCAPChecker.class.getName(),
                    "auriga.renouvellement.decision.mail.mandatory");
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AurigaException("auriga.exception.metier");
        }

    }

}
