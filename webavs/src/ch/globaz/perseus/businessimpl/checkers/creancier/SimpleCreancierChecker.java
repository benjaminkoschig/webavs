/**
 * 
 */
package ch.globaz.perseus.businessimpl.checkers.creancier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.constantes.CSTypeCreance;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.models.creancier.SimpleCreancier;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author MBO
 * 
 */
public class SimpleCreancierChecker extends PerseusAbstractChecker {

    /**
     * @param simpleCreancier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ch.globaz.perseus.business.exceptions.models.creancier.CreancierException
     * @throws CreancierException
     */
    public static void checkForCreate(SimpleCreancier simpleCreancier) throws CreancierException,
            ch.globaz.perseus.business.exceptions.models.creancier.CreancierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeNoBusinessLogSessionError {
        SimpleCreancierChecker.checkMandatory(simpleCreancier);
    }

    /**
     * @param simpleCreancier
     */
    public static void checkForDelete(SimpleCreancier simpleCreancier) {

    }

    /**
     * @param simpleCreancier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ch.globaz.perseus.business.exceptions.models.creancier.CreancierException
     * @throws CreancierException
     */
    public static void checkForUpdate(SimpleCreancier simpleCreancier) throws CreancierException,
            ch.globaz.perseus.business.exceptions.models.creancier.CreancierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeNoBusinessLogSessionError {
        SimpleCreancierChecker.checkMandatory(simpleCreancier);
    }

    /**
     * @param simpleCreancier
     * @throws ch.globaz.perseus.business.exceptions.models.creancier.CreancierException
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationServiceNotAvailableException
     */
    private static void checkMandatory(SimpleCreancier simpleCreancier) throws CreancierException,
            JadePersistenceException, ch.globaz.perseus.business.exceptions.models.creancier.CreancierException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError {
        if (!CSTypeCreance.TYPE_CREANCE_IMPOT_SOURCE.getCodeSystem().equals(simpleCreancier.getCsTypeCreance())
                && JadeStringUtil.isEmpty(simpleCreancier.getIdTiers())) {
            JadeThread.logError(SimpleCreancierChecker.class.getName(), "perseus.creancier.creancier.tiers.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleCreancier.getMontantRevendique())) {
            JadeThread
                    .logError(SimpleCreancierChecker.class.getName(), "perseus.creancier.creancier.montant.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleCreancier.getCsTypeCreance())) {
            JadeThread.logError(SimpleCreancierChecker.class.getName(), "perseus.creancier.creancier.type.mandatory");
        }
        Float montantAccorde = new Float(0);
        if (!JadeStringUtil.isEmpty(simpleCreancier.getMontantAccorde())) {
            montantAccorde = Float.parseFloat(simpleCreancier.getMontantAccorde().replace("'", ""));
        }
        Float montantRevendique = new Float(0);
        if (!JadeStringUtil.isEmpty(simpleCreancier.getMontantRevendique())) {
            montantRevendique = Float.parseFloat(simpleCreancier.getMontantRevendique().replace("'", ""));
        }
        if (montantAccorde > montantRevendique) {
            JadeThread.logError(SimpleCreancierChecker.class.getName(),
                    "perseus.creancier.creanceAccordee.montant.integrity");
        }

    }
}
