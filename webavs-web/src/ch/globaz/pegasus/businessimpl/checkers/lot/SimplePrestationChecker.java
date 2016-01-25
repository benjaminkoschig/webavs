package ch.globaz.pegasus.businessimpl.checkers.lot;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.constantes.IPCPresation;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.models.lot.SimplePrestationSearch;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class SimplePrestationChecker {
    public static void checkForCreate(SimplePrestation simplePrestation) throws PrestationException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        SimplePrestationChecker.checkMandatory(simplePrestation);
        if (IPCPresation.CS_TYPE_DE_PRESTATION_DECISION.equals(simplePrestation.getCsTypePrestation())) {
            if (!SimplePrestationChecker.isUnique(simplePrestation)) {
                JadeThread
                        .logError(simplePrestation.getClass().getName(), "pegasus.simplePrestationisUnique.integrity");
            }
        }
    }

    /**
     * @param simplePrestation
     */
    public static void checkForDelete(SimplePrestation simplePrestation) {
    }

    /**
     * @param simplePrestation
     */
    public static void checkForUpdate(SimplePrestation simplePrestation) {
        SimplePrestationChecker.checkMandatory(simplePrestation);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simplePrestation
     * @throws PrestationException
     */
    private static void checkIntegrity(SimplePrestation simplePrestation) throws PrestationException {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * @param simplePrestation
     */
    private static void checkMandatory(SimplePrestation simplePrestation) {
        if (JadeStringUtil.isBlankOrZero(simplePrestation.getCsTypePrestation())) {
            JadeThread.logError(simplePrestation.getClass().getName(),
                    "pegasus.simplePrestation.csTypePrestation.mandatory");
        }
    }

    private static boolean isUnique(SimplePrestation simplePrestation) throws PrestationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        SimplePrestationSearch simplePrestationSearch = new SimplePrestationSearch();
        simplePrestationSearch.setForCsEtat(simplePrestation.getCsEtat());
        simplePrestationSearch.setForIdLot(simplePrestation.getIdLot());
        simplePrestationSearch.setForIdVersionDroit(simplePrestation.getIdVersionDroit());

        simplePrestationSearch = PegasusImplServiceLocator.getSimplePrestationService().search(simplePrestationSearch);

        int nb = simplePrestationSearch.getSearchResults().length;

        return nb == 0;

    }
}
