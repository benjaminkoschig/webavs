package ch.globaz.pegasus.businessimpl.checkers.dossier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.dossier.Dossier;
import ch.globaz.pegasus.business.models.dossier.DossierSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class DossierChecker extends PegasusAbstractChecker {
    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DossierException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCreate(Dossier dossier) throws DossierException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        DossierChecker.checkMandatory(dossier);
        if (!PegasusAbstractChecker.threadOnError()) {
            DossierChecker.checkIntegrity(dossier);
        }
    }

    /**
     * @param dossier
     */
    public static void checkForDelete(Dossier dossier) {
    }

    /**
     * @param dossier
     */
    public static void checkForUpdate(Dossier dossier) {
    }

    /**
     * @param dossier
     * @throws DossierException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(Dossier dossier) throws DossierException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

    }

    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DossierException
     */
    private static void checkMandatory(Dossier dossier) throws DossierException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        if (JadeStringUtil.isEmpty(dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers())) {
            JadeThread.logError(dossier.getClass().getName(), "pegasus.dossier.idTiers.mandatory");

        } else {

            // Vérifie qu'il n'existe pas déjà un dossier avec le même tiers
            DossierSearch dSearch = new DossierSearch();
            dSearch.setForIdTiers(dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers());
            try {
                if (PegasusServiceLocator.getDossierService().count(dSearch) > 0) {
                    JadeThread.logError(dossier.getClass().getName(), "pegasus.dossier.dossierUnique.mandatory");
                }
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new DossierException("Unable to check dossier", e);
            }

        }
    }
}
