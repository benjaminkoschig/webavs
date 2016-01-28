package ch.globaz.pegasus.businessimpl.checkers.dossier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.demande.DemandeSearch;
import ch.globaz.pegasus.business.models.dossier.SimpleDossier;
import ch.globaz.pegasus.business.models.dossier.SimpleDossierSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;
import ch.globaz.prestation.business.models.demande.DemandePrestationSearch;
import ch.globaz.prestation.business.services.PrestationCommonServiceLocator;

public abstract class SimpleDossierChecker extends PegasusAbstractChecker {
    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DossierException
     */
    public static void checkForCreate(SimpleDossier dossier) throws DossierException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        SimpleDossierChecker.checkMandatory(dossier);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDossierChecker.checkIntegrity(dossier);
        }
    }

    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    public static void checkForDelete(SimpleDossier dossier) throws DossierException, JadePersistenceException {
        try {
            if (SimpleDossierChecker.hasDemande(dossier)) {
                JadeThread.logError(dossier.getClass().getName(), "pegasus.dossier.demandePrestation.integrity");

            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DossierException("Unable to get demandePrestation service", e);
        } catch (JadeNoBusinessLogSessionError e) {
            throw new DossierException("Unable to get demandePrestation service", e);
        } catch (DemandeException e) {
            throw new DossierException("Unable to check simpleDemande for delete dossier", e);
        }
    }

    /**
     * @param dossier
     */
    public static void checkForUpdate(SimpleDossier dossier) {
    }

    /**
     * @param dossier
     * @throws DossierException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleDossier dossier) throws DossierException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        // vérifie que l'id de demande de prestation du dossier existe
        DemandePrestationSearch dpSearch = new DemandePrestationSearch();
        dpSearch.setForIdDemandePrestation(dossier.getIdDemandePrestation());
        try {
            if (PrestationCommonServiceLocator.getDemandePrestationService().count(dpSearch) < 1) {
                JadeThread.logError(dossier.getClass().getName(), "pegasus.dossier.demandePrestation.integrity");

            }
        } catch (DemandePrestationException e1) {
            throw new DossierException("Unable to check demandePrestation", e1);
        } catch (JadeApplicationServiceNotAvailableException e1) {
            throw new DossierException("Unable to get demandePrestation service", e1);
        }

        // vérifie que la demande de prestation n'est pas déjà associée à un
        // dossier existant
        SimpleDossierSearch search = new SimpleDossierSearch();
        search.setForIdDemandePrestation(dossier.getIdDemandePrestation());
        try {
            if (!PegasusAbstractChecker.threadOnError()) {
                if (PegasusImplServiceLocator.getSimpleDossierService().count(search) > 0) {
                    JadeThread.logError(dossier.getClass().getName(),
                            "pegasus.dossier.demandePrestationUnique.integrity");
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DossierException("Unable to check dossier", e);
        }

    }

    /**
     * @param dossier
     */
    private static void checkMandatory(SimpleDossier dossier) {
        // Vérifie que le dossier a une demande de prestation
        if (JadeStringUtil.isEmpty(dossier.getIdDemandePrestation())) {
            JadeThread.logError(dossier.getClass().getName(), "pegasus.dossier.iddemandeprestation.mandatory");
        }
    }

    private static boolean hasDemande(SimpleDossier dossier) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        boolean hasDemande = true;
        DemandeSearch search = new DemandeSearch();
        search.setForIdDossier(dossier.getId());
        hasDemande = PegasusServiceLocator.getDemandeService().count(search) > 0;
        return hasDemande;
    }
}
