package ch.globaz.pegasus.businessimpl.checkers.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.pegasus.business.constantes.IPCRenteijapi;
import ch.globaz.pegasus.business.models.renteijapi.SimpleIjApg;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

/**
 * @author DMA
 * @date 8 juil. 2010
 */
public class SimpleIjApgChecker extends PegasusAbstractChecker {
    public static void checkForCreate(SimpleIjApg ijAbg) {
        SimpleIjApgChecker.checkMandatory(ijAbg);
    }

    /**
     * @param ijAbg
     */
    public static void checkForDelete(SimpleIjApg ijAbg) {
    }

    /**
     * @param ijAbg
     */
    public static void checkForUpdate(SimpleIjApg ijAbg) {
        SimpleIjApgChecker.checkMandatory(ijAbg);
        // SimpleIjApgChecker.checkIntegrity(ijAbg);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param ijAbg
     * @throws ijAbgException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    /*
     * private static void checkIntegrity(SimpleIjApg ijAbg) throws IjApgException, JadePersistenceException,
     * JadeNoBusinessLogSessionError {
     * 
     * }
     */

    /**
     * Verification des donnees obligatoires:
     * 
     * @param ijAbg
     */
    /**
     * @param ijApg
     */
    private static void checkMandatory(SimpleIjApg ijApg) {
        if (JadeStringUtil.isEmpty(ijApg.getCsGenrePrestation())) {
            JadeThread.logError(ijApg.getClass().getName(), "pegasus.ijApg.csGentrePrestation.mandatory");
        }
        if (IPCRenteijapi.CS_GENRE_PRESTATION_IJ_CHOMAGE.equals(ijApg.getCsGenrePrestation())) {
            if (JadeStringUtil.isBlankOrZero(ijApg.getMontantBrutAC())) {
                JadeThread.logError(ijApg.getClass().getName(), "pegasus.ijApg.montantBrutAC.mandatory");
            }
            if (JadeStringUtil.isBlankOrZero(ijApg.getNbJours())) {
                JadeThread.logError(ijApg.getClass().getName(), "pegasus.ijApg.nbJours.mandatory");
            }
            /*
             * si le type le genre de presation vaut "Autres" le champs autreGenreest obligatoire
             */
        } else if (IPCRenteijapi.CS_GENRE_PRESTATION_AUTRE.equals(ijApg.getCsGenrePrestation())) {
            if (JadeStringUtil.isEmpty(ijApg.getAutreGenrePresation())) {
                JadeThread.logError(ijApg.getClass().getName(), "pegasus.ijApg.autreGenrePrestation.mandatory");
            }
            if (JadeStringUtil.isBlankOrZero(ijApg.getMontant())) {
                JadeThread.logError(ijApg.getClass().getName(), "pegasus.ijApg.montant.mandatory");
            }
        } else {
            /*
             * if (JadeStringUtil.isEmpty(ijApg.getIdFournisseurPrestation())) {
             * JadeThread.logError(ijApg.getClass().getName(), "pegasus.ijApg.idFournisseurPersation.mandatory"); }
             */
            if (JadeStringUtil.isBlankOrZero(ijApg.getMontant())) {
                JadeThread.logError(ijApg.getClass().getName(), "pegasus.ijApg.montant.mandatory");
            }
        }

    }
}
