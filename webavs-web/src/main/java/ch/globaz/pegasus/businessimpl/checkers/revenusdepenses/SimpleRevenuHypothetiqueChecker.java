package ch.globaz.pegasus.businessimpl.checkers.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuHypothetiqueException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuHypothetique;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleRevenuHypothetiqueChecker extends PegasusAbstractChecker {
    /**
     * @param simpleRevenuHypothetique
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws RevenuHypothetiqueException
     */
    public static void checkForCreate(SimpleRevenuHypothetique simpleRevenuHypothetique)
            throws RevenuHypothetiqueException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleRevenuHypothetiqueChecker.checkMandatory(simpleRevenuHypothetique);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleRevenuHypothetiqueChecker.checkIntegrity(simpleRevenuHypothetique);
        }
    }

    /**
     * @param simpleRevenuHypothetique
     */
    public static void checkForDelete(SimpleRevenuHypothetique simpleRevenuHypothetique) {
    }

    /**
     * @param simpleRevenuHypothetique
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws RevenuHypothetiqueException
     */
    public static void checkForUpdate(SimpleRevenuHypothetique simpleRevenuHypothetique)
            throws RevenuHypothetiqueException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleRevenuHypothetiqueChecker.checkMandatory(simpleRevenuHypothetique);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleRevenuHypothetiqueChecker.checkIntegrity(simpleRevenuHypothetique);
        }

    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleRevenuHypothetique
     * @throws RevenuHypothetiqueException
     * @throws RevenuHypothetiqueException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleRevenuHypothetique simpleRevenuHypothetique)
            throws RevenuHypothetiqueException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleRevenuHypothetique ait un motif
     * 
     * @param simpleRevenuHypothetique
     */
    private static void checkMandatory(SimpleRevenuHypothetique simpleRevenuHypothetique) {

        /*
         * if (JadeStringUtil.isEmpty(simpleRevenuHypothetique.getCsMotif())) {
         * JadeThread.logError(simpleRevenuHypothetique.getClass().getName(),
         * "pegasus.simpleRevenuHypothetique.csMotif.mandatory"); } else if
         * (simpleRevenuHypothetique.getCsMotif().equals("64034006") &&
         * JadeStringUtil.isEmpty(simpleRevenuHypothetique.getAutreMotif())) {
         * JadeThread.logError(simpleRevenuHypothetique.getClass().getName(),
         * "pegasus.simpleRevenuHypothetique.autresMotifs.mandatory"); }
         */

        if (simpleRevenuHypothetique.getCsMotif().equals("64034001")
                || simpleRevenuHypothetique.getCsMotif().equals("64034002")) {

            if (JadeStringUtil.isBlank(simpleRevenuHypothetique.getMontantRevenuHypothetiqueNet())) {
                JadeThread.logError(simpleRevenuHypothetique.getClass().getName(),
                        "pegasus.simpleRevenuHypothetique.montantrevenuhyponet.mandatory");
            }

        } else if (simpleRevenuHypothetique.getCsMotif().equals("64034003")
                || simpleRevenuHypothetique.getCsMotif().equals("64034004")
                || simpleRevenuHypothetique.getCsMotif().equals("64034005")) {

            if (JadeStringUtil.isBlank(simpleRevenuHypothetique.getMontantRevenuHypothetiqueBrut())) {
                JadeThread.logError(simpleRevenuHypothetique.getClass().getName(),
                        "pegasus.simpleRevenuHypothetique.montantrevenuhypobrut.mandatory");
            }
            if (JadeStringUtil.isEmpty(simpleRevenuHypothetique.getDeductionsSociales())) {
                JadeThread.logError(simpleRevenuHypothetique.getClass().getName(),
                        "pegasus.simpleRevenuHypothetique.deductionsociale.mandatory");
            }
            if (JadeStringUtil.isEmpty(simpleRevenuHypothetique.getDeductionLPP())) {
                JadeThread.logError(simpleRevenuHypothetique.getClass().getName(),
                        "pegasus.simpleRevenuHypothetique.deductionlpp.mandatory");
            }
            if (JadeStringUtil.isEmpty(simpleRevenuHypothetique.getFraisDeGarde())) {
                JadeThread.logError(simpleRevenuHypothetique.getClass().getName(),
                        "pegasus.simpleRevenuHypothetique.fraisdegarde.mandatory");
            }

        } else if (simpleRevenuHypothetique.getCsMotif().equals("64034006")) {

            if (JadeStringUtil.isEmpty(simpleRevenuHypothetique.getAutreMotif())) {
                JadeThread.logError(simpleRevenuHypothetique.getClass().getName(),
                        "pegasus.simpleRevenuHypothetique.autremotif.mandatory");
            }
        }

    }

}
