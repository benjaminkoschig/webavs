package ch.globaz.pegasus.businessimpl.checkers.restitution;

import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.restitution.PCRestitutionException;
import ch.globaz.pegasus.business.models.dossier.Dossier;
import ch.globaz.pegasus.business.models.restitution.SimpleRestitution;
import ch.globaz.pegasus.business.models.restitution.SimpleRestitutionSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class SimpleRestitutionChecker extends PegasusAbstractChecker {

    /**
     * @param restitution
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DossierException
     */
    public static void checkForCreate(SimpleRestitution restitution) throws PCRestitutionException, JadePersistenceException,
            JadeNoBusinessLogSessionError, JadeApplicationServiceNotAvailableException, DossierException {
        SimpleRestitutionChecker.checkMandatory(restitution);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleRestitutionChecker.checkIntegrity(restitution);
        }
    }

    /**
     *
     * @param restitution
     */
    public static void checkForUpdate(SimpleRestitution restitution) {
    }

    /**
     * @param restitution
     */
    private static void checkMandatory(SimpleRestitution restitution) {
        // Vérifie que le dossier a une demande de prestation
        if (JadeStringUtil.isEmpty(restitution.getIdDossier())) {
            JadeThread.logError(restitution.getClass().getName(), "pegasus.restitution.iddossier.mandatory");
        }
    }

    /**
     * @param restitution
     * @throws PCRestitutionException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleRestitution restitution) throws PCRestitutionException, JadePersistenceException,
            JadeNoBusinessLogSessionError, JadeApplicationServiceNotAvailableException, DossierException {

        // vérifie que l'id du dossier existe
        Dossier dossier = PegasusServiceLocator.getDossierService().read(restitution.getIdDossier());
        if (dossier == null) {
            JadeThread.logError(restitution.getClass().getName(), "pegasus.restitution.iddossier.mandatory");
        }

        // vérifie que le dossier n'est pas déjà associée à une restitution existante
        SimpleRestitutionSearch search = new SimpleRestitutionSearch();
        search.setForIdDossier(dossier.getId());
        try {
            if (!PegasusAbstractChecker.threadOnError()) {
                if (PegasusImplServiceLocator.getRestitutionService().count(search) > 0) {
                    JadeThread.logError(dossier.getClass().getName(),
                            "pegasus.restitution.restitutionUnique.integrity");
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCRestitutionException("Unable to check restitution", e);
        }

    }

}
