package ch.globaz.pegasus.businessimpl.checkers.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.pegasus.business.constantes.IPCRenteijapi;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAutreApi;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleAutreApiChecker extends PegasusAbstractChecker {
    public static void checkForCreate(SimpleAutreApi autreApi) {
        SimpleAutreApiChecker.checkMandatory(autreApi);
    }

    /**
     * @param autreApi
     */
    public static void checkForDelete(SimpleAutreApi autreApi) {
    }

    /**
     * @param autreApi
     */
    public static void checkForUpdate(SimpleAutreApi autreApi) {
        SimpleAutreApiChecker.checkMandatory(autreApi);
        // SimpleAutreApiChecker.checkIntegrity(autreApi);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param autreApi
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    /*
     * private static void checkIntegrity(SimpleAutreApi autreApi) throws JadePersistenceException,
     * JadeNoBusinessLogSessionError {
     * 
     * }
     */

    /**
     * Verification des donnees obligatoires:
     * 
     * @param autreApi
     */
    private static void checkMandatory(SimpleAutreApi autreApi) {
        if (JadeStringUtil.isEmpty(autreApi.getCsGenre())) {
            JadeThread.logError(autreApi.getClass().getName(), "pegasus.autreApi.csGentre.mandatory");
        }
        if (JadeStringUtil.isEmpty(autreApi.getMontant())) {
            JadeThread.logError(autreApi.getClass().getName(), "pegasus.autreApi.montant.mandatory");
        }
        if (JadeStringUtil.isEmpty(autreApi.getCsType())) {
            JadeThread.logError(autreApi.getClass().getName(), "pegasus.autreApi.csType.mandatory");
        }
        if (JadeStringUtil.isEmpty(autreApi.getCsDegre())) {
            JadeThread.logError(autreApi.getClass().getName(), "pegasus.autreApi.csDegre.mandatory");
        }
        // si le type API vaut "Autres" le champs autre API est
        // obligatoire
        if ((IPCRenteijapi.CS_AUTRES_API_AUTRE.equals(autreApi.getCsType()))
                && JadeStringUtil.isEmpty(autreApi.getAutre())) {
            JadeThread.logError(autreApi.getClass().getName(), "pegasus.autreApi.autreGenre.mandatory");
        }

    }
}
