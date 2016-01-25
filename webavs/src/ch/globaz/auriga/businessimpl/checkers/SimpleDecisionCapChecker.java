package ch.globaz.auriga.businessimpl.checkers;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.naos.translation.CodeSystem;
import ch.globaz.ariesaurigacommon.utils.AriesAurigaNumericUtils;
import ch.globaz.auriga.business.constantes.AUDecisionEtat;
import ch.globaz.auriga.business.constantes.AUDecisionType;
import ch.globaz.auriga.business.exceptions.AurigaException;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;
import ch.globaz.naos.business.constantes.AFAffiliationType;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class SimpleDecisionCapChecker {

    public static void checkDecisionRectifieeForCreate(SimpleDecisionCAP decisionRectifiee) throws Exception {
        // la décision rectifiée doit être à l'état comptabilisée ou reprise lors de la création du rectificatif
        if (!JadeStringUtil.isBlankOrZero(decisionRectifiee.getEtat())
                && !decisionRectifiee.getEtat().equals(AUDecisionEtat.COMPTABILISEE.getCodeSystem())
                && !decisionRectifiee.getEtat().equals(AUDecisionEtat.REPRISE.getCodeSystem())) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(),
                    "auriga.rectificative.rectifiee.not.comptabilise.or.reprise");
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AurigaException("auriga.exception.metier");
        }
    }

    public static void checkEnfant(PersonneEtendueComplexModel enfant, String anneeDecision) throws Exception {
        // regarde si l'enfant est décédé avant l'année de la décision
        String dateDeces = enfant.getPersonne().getDateDeces();

        if (!JadeStringUtil.isBlankOrZero(dateDeces)) {
            boolean isValideParams = true;
            if (JadeStringUtil.isBlankOrZero(anneeDecision)) {
                isValideParams = false;
                JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.decisioncap.date.fin.mandatory");
            }
            if (!JadeDateUtil.isGlobazDate(dateDeces)) {
                isValideParams = false;
                JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.decisioncap.date.deces.invalide");
            }
            if (isValideParams) {
                int anneeDecesInt = JACalendar.getYear(dateDeces);
                int anneeDecisionInt = new Integer(anneeDecision);

                if (anneeDecesInt < anneeDecisionInt) {
                    JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.decisioncap.enfant.decede");
                }
            }
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AurigaException("auriga.exception.metier");
        }
    }

    public static void checkForCreate(SimpleDecisionCAP decisionCap, AffiliationSimpleModel affiliation,
            boolean isAlreadyADecisionActive) throws Exception {
        SimpleDecisionCapChecker.checkMandatoryAndIntegrity(decisionCap, affiliation, isAlreadyADecisionActive);

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AurigaException("auriga.exception.metier");
        }
    }

    public static void checkForDelete(SimpleDecisionCAP decisionCap) throws Exception {
        if (AUDecisionEtat.COMPTABILISEE.getCodeSystem().equals(decisionCap.getEtat())) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.decisioncap.delete.comptablisee");
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AurigaException("auriga.exception.metier");
        }
    }

    public static void checkForRectificative(SimpleDecisionCAP decisionRectificative,
            SimpleDecisionCAP decisionRectifiee) throws Exception {
        if (!decisionRectificative.getAnnee().equals(decisionRectifiee.getAnnee())) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.rectificative.not.meme.annee");
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AurigaException("auriga.exception.metier");
        }
    }

    public static void checkForUpdate(SimpleDecisionCAP decisionCap, AffiliationSimpleModel affiliation,
            boolean isAlreadyADecisionActive) throws Exception {
        SimpleDecisionCapChecker.checkMandatoryAndIntegrity(decisionCap, affiliation, isAlreadyADecisionActive);

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AurigaException("auriga.exception.metier");
        }
    }

    public static void checkMandatoryAndIntegrity(SimpleDecisionCAP decisionCap, AffiliationSimpleModel affiliation,
            boolean isAlreadyADecisionActive) throws Exception {
        String theAnnee = decisionCap.getAnnee();
        String theDateDebut = decisionCap.getDateDebut();
        String theDateFin = decisionCap.getDateFin();

        boolean isAnneeValide = true;
        boolean isDateDebutValide = true;
        boolean isDateFinValide = true;

        if (isAlreadyADecisionActive) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(),
                    "auriga.decisioncap.decision.active.existe.deja", new String[] { decisionCap.getDateDebut(),
                            decisionCap.getDateFin() });
        }

        if (CodeSystem.TYPE_ASS_CAP_15.equalsIgnoreCase(decisionCap.getCategorie())
                || CodeSystem.TYPE_ASS_CAP_16.equalsIgnoreCase(decisionCap.getCategorie())) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(),
                    "auriga.decisioncap.categorie.assurance.non.autorisee");
        }

        if (!AFAffiliationType.isTypeCAP(affiliation.getTypeAffiliation())) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.decisioncap.affiliation.non.cap");
        }

        if (!AriesAurigaNumericUtils.isNumericIntegerPositifFixedSize(theAnnee, 4)) {
            isAnneeValide = false;
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.decisioncap.annee.mandatory");
        }

        if (!JadeDateUtil.isGlobazDate(theDateDebut)) {
            isDateDebutValide = false;
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.decisioncap.date.debut.mandatory");
        }

        if (!JadeDateUtil.isGlobazDate(theDateFin)) {
            isDateFinValide = false;
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.decisioncap.date.fin.mandatory");
        }

        if (!JadeDateUtil.isGlobazDate(decisionCap.getDateDonnees())) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.decisioncap.date.decision.mandatory");
        }

        if (isDateDebutValide && (JACalendar.getDay(theDateDebut) != 1)) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(),
                    "auriga.decisioncap.date.debut.pas.debut.mois");
        }

        if (isDateFinValide && !(new JACalendarGregorian().isLastInMonth(theDateFin))) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.decisioncap.date.fin.pas.fin.mois");
        }

        if (isDateDebutValide && isDateFinValide && JadeDateUtil.isDateAfter(theDateDebut, theDateFin)) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(),
                    "auriga.decisioncap.date.fin.avant.date.debut");
        }

        if (isAnneeValide
                && isDateDebutValide
                && isDateFinValide
                && ((Integer.valueOf(theAnnee).intValue() != JACalendar.getYear(theDateDebut)) || (Integer.valueOf(
                        theAnnee).intValue() != JACalendar.getYear(theDateFin)))) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(),
                    "auriga.decisioncap.annee.annee.periode.pas.identique");
        }

        if (isDateDebutValide && JadeDateUtil.isDateAfter(affiliation.getDateDebut(), theDateDebut)) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(),
                    "auriga.decisioncap.date.debut.avant.date.debut.affiliation");
        }

        if (isDateFinValide && JadeDateUtil.isDateAfter(theDateFin, affiliation.getDateFin())) {

            int moisFinAffiliation = JACalendar.getMonth(affiliation.getDateFin());
            int moisFinDecision = JACalendar.getMonth(theDateFin);

            if (moisFinDecision > moisFinAffiliation) {
                JadeThread.logError(SimpleDecisionCapChecker.class.getName(),
                        "auriga.decisioncap.date.fin.apres.date.fin.affiliation");
            }

        }

        if (JadeStringUtil.isBlankOrZero(decisionCap.getIdPassageFacturation())) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(),
                    "auriga.decisioncap.id.passage.facturation.mandatory");
        }

        if (!JadeStringUtil.isBlankOrZero(decisionCap.getEtat())
                && decisionCap.getEtat().equals(AUDecisionEtat.SUPPRIMEE.getCodeSystem())) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.udpate.decisioncap.supprimee");
        }

        if (!JadeStringUtil.isBlankOrZero(decisionCap.getType())
                && SimpleDecisionCapChecker.isTypeRectif(decisionCap.getType())) {
            if (JadeStringUtil.isBlankOrZero(decisionCap.getIdDecisionRectifiee())) {
                JadeThread.logError(SimpleDecisionCapChecker.class.getName(),
                        "auriga.rectificative.no.idDecisionRectifiee");
            }
        }

        if (!JadeStringUtil.isBlankOrZero(decisionCap.getIdDecisionRectifiee())) {
            if (!JadeStringUtil.isBlankOrZero(decisionCap.getType())
                    && !SimpleDecisionCapChecker.isTypeRectif(decisionCap.getType())) {
                JadeThread.logError(SimpleDecisionCapChecker.class.getName(),
                        "auriga.rectificative.not.typeRectificative");
            }
        }
    }

    public static void checkMontantAf(FWCurrency montantAf, String nomPrenom) throws Exception {
        if (montantAf.isZero()) {
            JadeThread.logError(SimpleDecisionCapChecker.class.getName(), "auriga.decisioncap.enfant.mantantaf.zero",
                    new String[] { nomPrenom });
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            throw new AurigaException("auriga.exception.metier");
        }
    }

    public static boolean isTypeRectif(String type) {
        if (AUDecisionType.getListTypeRectif().contains(type)) {
            return true;
        } else {
            return false;
        }
    }
}
