/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.revenu;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminant;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;

/**
 * Checker for the simple revenu determinant model actions
 * 
 * @author dhi
 * 
 */
public class SimpleRevenuDeterminantChecker extends AmalAbstractChecker {

    /**
     * Contr�les n�cessaires pour la cr�ation d'un simple revenu d�terminant
     * 
     * @param revenuDeterminant
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws RevenuException
     */
    public static void checkForCreate(SimpleRevenuDeterminant revenuDeterminant) throws RevenuException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleRevenuDeterminantChecker.checkMandatory(revenuDeterminant);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleRevenuDeterminantChecker.checkIntegrity(revenuDeterminant);
        }
    }

    /**
     * Contr�les n�cessaires pour la suppression d'un simple revenu d�terminant
     * 
     * @param revenuDeterminant
     */
    public static void checkForDelete(SimpleRevenuDeterminant revenuDeterminant) {

    }

    /**
     * Contr�les n�cessaires pour la mise � jour d'un simple revenu d�terminant
     * 
     * @param revenuDeterminant
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws RevenuException
     */
    public static void checkForUpdate(SimpleRevenuDeterminant revenuDeterminant) throws RevenuException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleRevenuDeterminantChecker.checkMandatory(revenuDeterminant);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleRevenuDeterminantChecker.checkIntegrity(revenuDeterminant);
        }
    }

    /**
     * Contr�le d'int�grit� sur un simple revenu d�terminant
     * 
     * @param revenuDeterminant
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws RevenuException
     */
    private static void checkIntegrity(SimpleRevenuDeterminant revenuDeterminant) throws RevenuException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // SimpleRevenu simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().read(
        // revenuDeterminant.getIdRevenuHistorique());
        // if (simpleRevenu.isNew()) {
        // JadeThread.logError(revenuDeterminant.getClass().getName(), "amal.revenu.idRevenuContribuable.integrity");
        // }
    }

    /**
     * Contr�le de la pr�sence de l'ensemble des champs r�sumant le calcul du revenu d�terminant
     * 
     * @param revenuDeterminant
     */
    private static void checkMandatory(SimpleRevenuDeterminant revenuDeterminant) {
        if (JadeStringUtil.isBlankOrZero(revenuDeterminant.getIdRevenuHistorique())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.idHistorique.mandatory");
        }

        // contr�le que tous les champs soient renseign�s (sauf les ids)
        if (JadeStringUtil.isNull(revenuDeterminant.getDeductionContribAvecEnfantChargeCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.deductionContribAvecEnfantChargeCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getDeductionContribNonCelibSansEnfantChargeCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.deductionContribNonCelibSansEnfantChargeCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getDeductionSelonNbreEnfantCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.deductionSelonNbreEnfantCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getExcedentDepensesPropImmoCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.excedentDepensesPropImmoCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getExcedentDepensesSuccNonPartageesCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.excedentDepensesSuccNonPartageesCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getFortuneImposableCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.fortuneImposableCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getFortuneImposablePercentCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.fortuneImposablePercentCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getInteretsPassifsCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.interetsPassifsCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getNbEnfants())) {
            // JadeThread.logError(revenuDeterminant.getClass().getName(),"amal.simpleRevenuDeterminant.nbEnfants.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getPartRendementImmobExedantIntPassifsCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.partRendementImmobExedantIntPassifsCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getPerteExercicesCommerciauxCalcul())) {
            // JadeThread.logError(revenuDeterminant.getClass().getName(),"amal.simpleRevenuDeterminant.perteExercicesCommerciauxCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getPerteLiquidationCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.perteLiquidationCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getPerteReporteeExercicesCommerciauxCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.perteReporteeExercicesCommerciauxCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getRendementFortuneImmoCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.rendementFortuneImmoCalcul.mandatory");
        }
        if (JadeStringUtil.isNull(revenuDeterminant.getRevenuImposableCalcul())) {
            JadeThread.logError(revenuDeterminant.getClass().getName(),
                    "amal.simpleRevenuDeterminant.revenuImposableCalcul.mandatory");
        }
    }

}
