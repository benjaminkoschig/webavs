package ch.globaz.aries.businessimpl.checkers;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import ch.globaz.aries.business.beans.decisioncgas.DecisionCGASBean;
import ch.globaz.aries.business.constantes.ARDecisionEtat;
import ch.globaz.aries.business.constantes.ARDecisionType;
import ch.globaz.aries.business.exceptions.AriesException;
import ch.globaz.ariesaurigacommon.utils.AriesAurigaNumericUtils;
import ch.globaz.naos.business.constantes.AFAffiliationType;
import ch.globaz.naos.business.model.AffiliationSimpleModel;

public class DecisionCGASBeanChecker {

    public static void checkDecisionRectifieeForCreate(DecisionCGASBean decisionRectifieeBean) throws Exception {
        // la décision rectifiée doit être à l'état comptabilisée ou reprise lors de la création du rectificatif
        if (!JadeStringUtil.isBlankOrZero(decisionRectifieeBean.getDecisionCGAS().getEtat())
                && !decisionRectifieeBean.getDecisionCGAS().getEtat()
                        .equals(ARDecisionEtat.COMPTABILISEE.getCodeSystem())
                && !decisionRectifieeBean.getDecisionCGAS().getEtat().equals(ARDecisionEtat.REPRISE.getCodeSystem())) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.rectificative.rectifiee.not.comptabilise.or.reprise");
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AriesException("aries.exception.metier");
        }
    }

    public static void checkForCreate(DecisionCGASBean decisionCGASBean, AffiliationSimpleModel affiliation,
            boolean isAlreadyADecisionActive) throws Exception {

        DecisionCGASBeanChecker.checkMandatory(decisionCGASBean, affiliation, isAlreadyADecisionActive);

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AriesException("aries.exception.metier");
        }

    }

    public static void checkForDelete(DecisionCGASBean decisionCGASBean) throws Exception {
        if (ARDecisionEtat.COMPTABILISEE.getCodeSystem().equals(decisionCGASBean.getDecisionCGAS().getEtat())) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(), "aries.decisioncgas.delete.comptablisee");
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AriesException("aries.exception.metier");
        }
    }

    public static void checkForRectificative(DecisionCGASBean decisionRectificative, DecisionCGASBean decisionRectifiee)
            throws Exception {
        if (!decisionRectificative.getDecisionCGAS().getAnnee().equals(decisionRectifiee.getDecisionCGAS().getAnnee())) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(), "aries.rectificative.not.meme.annee");
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AriesException("aries.exception.metier");
        }
    }

    public static void checkForUpdate(DecisionCGASBean decisionCGASBean, AffiliationSimpleModel affiliation,
            boolean isAlreadyADecisionActive) throws Exception {
        DecisionCGASBeanChecker.checkMandatory(decisionCGASBean, affiliation, isAlreadyADecisionActive);

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AriesException("aries.exception.metier");
        }
    }

    private static void checkMandatory(DecisionCGASBean decisionCGASBean, AffiliationSimpleModel affiliation,
            boolean isAlreadyADecisionActive) throws Exception {

        String theAnnee = decisionCGASBean.getDecisionCGAS().getAnnee();
        String theDateDebut = decisionCGASBean.getDecisionCGAS().getDateDebut();
        String theDateFin = decisionCGASBean.getDecisionCGAS().getDateFin();

        boolean isAnneeValide = true;
        boolean isDateDebutValide = true;
        boolean isDateFinValide = true;

        if (isAlreadyADecisionActive) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.decision.active.existe.deja", new String[] {
                            decisionCGASBean.getDecisionCGAS().getDateDebut(),
                            decisionCGASBean.getDecisionCGAS().getDateFin() });
        }

        if (!AFAffiliationType.isTypeCGAS(affiliation.getTypeAffiliation())) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(), "aries.decisioncgas.affiliation.non.cgas");
        }

        if (!AriesAurigaNumericUtils.isNumericIntegerPositifFixedSize(theAnnee, 4)) {
            isAnneeValide = false;
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(), "aries.decisioncgas.annee.mandatory");
        }

        if (!JadeDateUtil.isGlobazDate(theDateDebut)) {
            isDateDebutValide = false;
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(), "aries.decisioncgas.date.debut.mandatory");
        }

        if (!JadeDateUtil.isGlobazDate(theDateFin)) {
            isDateFinValide = false;
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(), "aries.decisioncgas.date.fin.mandatory");
        }

        if (isDateDebutValide && JadeDateUtil.isDateAfter(affiliation.getDateDebut(), theDateDebut)) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.date.debut.avant.date.debut.affiliation");
        }

        if (isDateFinValide && JadeDateUtil.isDateAfter(theDateFin, affiliation.getDateFin())) {

            int moisFinAffiliation = JACalendar.getMonth(affiliation.getDateFin());
            int moisFinDecision = JACalendar.getMonth(theDateFin);

            if (moisFinDecision > moisFinAffiliation) {
                JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                        "aries.decisioncgas.date.fin.apres.date.fin.affiliation");
            }

        }

        if (!JadeDateUtil.isGlobazDate(decisionCGASBean.getDecisionCGAS().getDateDonnees())) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(), "aries.decisioncgas.date.decision.mandatory");
        }

        if (isDateDebutValide && (JACalendar.getDay(theDateDebut) != 1)) {
            JadeThread
                    .logError(DecisionCGASBeanChecker.class.getName(), "aries.decisioncgas.date.debut.pas.debut.mois");
        }

        if (isDateFinValide && !(new JACalendarGregorian().isLastInMonth(theDateFin))) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(), "aries.decisioncgas.date.fin.pas.fin.mois");
        }

        if (isDateDebutValide && isDateFinValide && JadeDateUtil.isDateAfter(theDateDebut, theDateFin)) {
            JadeThread
                    .logError(DecisionCGASBeanChecker.class.getName(), "aries.decisioncgas.date.fin.avant.date.debut");
        }

        if (isAnneeValide
                && isDateDebutValide
                && isDateFinValide
                && ((Integer.valueOf(theAnnee).intValue() != JACalendar.getYear(theDateDebut)) || (Integer.valueOf(
                        theAnnee).intValue() != JACalendar.getYear(theDateFin)))) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.annee.annee.periode.pas.identique");
        }

        if (JadeStringUtil.isBlankOrZero(decisionCGASBean.getDecisionCGAS().getIdPassageFacturation())) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.id.passage.facturation.mandatory");
        }

        if (!JadeStringUtil.isEmpty(decisionCGASBean.getCulturePlaine().getNombre())
                && !AriesAurigaNumericUtils.isNumericDecimalPositif(decisionCGASBean.getCulturePlaine().getNombre(),
                        13, 2)) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.culture.plaine.nombre.mandatory");
        }

        if (!JadeStringUtil.isEmpty(decisionCGASBean.getCultureArboricole().getNombre())
                && !AriesAurigaNumericUtils.isNumericDecimalPositif(
                        decisionCGASBean.getCultureArboricole().getNombre(), 13, 2)) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.culture.arboricole.nombre.mandatory");
        }

        if (!JadeStringUtil.isEmpty(decisionCGASBean.getCultureMaraichere().getNombre())
                && !AriesAurigaNumericUtils.isNumericDecimalPositif(
                        decisionCGASBean.getCultureMaraichere().getNombre(), 13, 2)) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.culture.maraichere.nombre.mandatory");
        }

        if (!JadeStringUtil.isEmpty(decisionCGASBean.getVigneNordCanton().getNombre())
                && !AriesAurigaNumericUtils.isNumericDecimalPositif(decisionCGASBean.getVigneNordCanton().getNombre(),
                        13, 2)) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.vigne.nord.canton.nombre.mandatory");
        }

        if (!JadeStringUtil.isEmpty(decisionCGASBean.getVigneEstCanton().getNombre())
                && !AriesAurigaNumericUtils.isNumericDecimalPositif(decisionCGASBean.getVigneEstCanton().getNombre(),
                        13, 2)) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.vigne.est.canton.nombre.mandatory");
        }

        if (!JadeStringUtil.isEmpty(decisionCGASBean.getVigneLaCote().getNombre())
                && !AriesAurigaNumericUtils.isNumericDecimalPositif(decisionCGASBean.getVigneLaCote().getNombre(), 13,
                        2)) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.vigne.la.cote.nombre.mandatory");
        }

        if (!JadeStringUtil.isEmpty(decisionCGASBean.getUgbPlaine().getNombre())
                && !AriesAurigaNumericUtils.isNumericDecimalPositif(decisionCGASBean.getUgbPlaine().getNombre(), 13, 2)) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.ugb.plaine.nombre.mandatory");
        }

        if (!JadeStringUtil.isEmpty(decisionCGASBean.getUgbMontagne().getNombre())
                && !AriesAurigaNumericUtils.isNumericDecimalPositif(decisionCGASBean.getUgbMontagne().getNombre(), 13,
                        2)) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.ugb.montagne.nombre.mandatory");
        }

        if (!JadeStringUtil.isEmpty(decisionCGASBean.getUgbSpecial().getNombre())
                && !AriesAurigaNumericUtils
                        .isNumericDecimalPositif(decisionCGASBean.getUgbSpecial().getNombre(), 13, 2)) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                    "aries.decisioncgas.ugb.elevage.special.nombre.mandatory");
        }

        if (!JadeStringUtil.isEmpty(decisionCGASBean.getAlpage().getNombre())
                && !AriesAurigaNumericUtils.isNumericDecimalPositif(decisionCGASBean.getAlpage().getNombre(), 13, 2)) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(), "aries.decisioncgas.alpage.nombre.mandatory");
        }

        if (!JadeStringUtil.isBlankOrZero(decisionCGASBean.getDecisionCGAS().getEtat())
                && decisionCGASBean.getDecisionCGAS().getEtat().equals(ARDecisionEtat.SUPPRIMEE.getCodeSystem())) {
            JadeThread.logError(DecisionCGASBeanChecker.class.getName(), "aries.udpate.decisioncgas.supprimee");
        }

        if (!JadeStringUtil.isBlankOrZero(decisionCGASBean.getDecisionCGAS().getType())
                && DecisionCGASBeanChecker.isTypeRectif(decisionCGASBean.getDecisionCGAS().getType())) {
            if (JadeStringUtil.isBlankOrZero(decisionCGASBean.getDecisionCGAS().getIdDecisionRectifiee())) {
                JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                        "aries.rectificative.no.idDecisionRectifiee");
            }
        }

        if (!JadeStringUtil.isBlankOrZero(decisionCGASBean.getDecisionCGAS().getIdDecisionRectifiee())) {
            if (!JadeStringUtil.isBlankOrZero(decisionCGASBean.getDecisionCGAS().getType())
                    && !DecisionCGASBeanChecker.isTypeRectif(decisionCGASBean.getDecisionCGAS().getType())) {
                JadeThread.logError(DecisionCGASBeanChecker.class.getName(),
                        "aries.rectificative.not.typeRectificative");
            }
        }
    }

    public static boolean isTypeRectif(String type) {
        if (ARDecisionType.getListTypeRectif().contains(type)) {
            return true;
        } else {
            return false;
        }
    }
}
